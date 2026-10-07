package PRAKTIKA;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AktoreZerrenda {

    private static AktoreZerrenda nireAktoreZerrenda = null;
    private List<Aktoreak> zerrenda;

    public AktoreZerrenda() {
        this.zerrenda = new ArrayList<>();
    }

    public static AktoreZerrenda getNireAktoreZerrenda() {
        if (nireAktoreZerrenda == null) {
            nireAktoreZerrenda = new AktoreZerrenda();
        }
        return nireAktoreZerrenda;
    }

    public List<Aktoreak> getZerrenda() {
        return zerrenda;
    }

    // ==================================================================
    // 1. Datuak fitxategietatik kargatu (Wikidata formatua: "###")
    // ==================================================================
    public void kargatuFitxategitik(String fitxategiBidea, int urtea) {
        try (BufferedReader br = new BufferedReader(new FileReader(fitxategiBidea))) {
            String lerroa;
            while ((lerroa = br.readLine()) != null) {
                if (lerroa.trim().isEmpty()) continue;
                
                // Formatu adibidea: Aktore_ID ### Aktore_Izena ### Filma_ID ### Filma_Izena
                String[] zatiak = lerroa.split("###");
                if (zatiak.length == 4) {
                    String aktoreId = zatiak[0].trim();
                    String aktoreIzena = zatiak[1].trim();
                    String filmaId = zatiak[2].trim();
                    String filmaIzena = zatiak[3].trim();

                    // Aktorea bilatu edo sortu
                    Aktoreak aktorea = bilatuAktoreaIdz(aktoreId);
                    if (aktorea == null) {
                        aktorea = new Aktoreak(aktoreId, aktoreIzena);
                        zerrenda.add(aktorea);
                    }

                    // Filma bilatu edo sortu
                    FilmaZerrenda fZerrenda = FilmaZerrenda.getNireFilmaZerrenda();
                    Filma filma = fZerrenda.bilatuFilmaIdz(filmaId);
                    if (filma == null) {
                        filma = new Filma(filmaId, filmaIzena, urtea);
                        fZerrenda.gehitzenFilma(filma);
                    }

                    // Bi norabideko harremana ezarri (Aktorea <-> Filma)
                    aktorea.gehitzenFilma(filma);
                    filma.gehitzenAktorea(aktorea);
                }
            }
        } catch (IOException e) {
            System.err.println("Errorea fitxategia irakurtzean: " + e.getMessage());
        }
    }

    // ==================================================================
    // 2. Aktore bat bilatu (ID edo Izen-abizenen arabera)
    // ==================================================================
    public Aktoreak bilatuAktoreaIdz(String id) {
        for (Aktoreak a : zerrenda) {
            if (a.getId().equalsIgnoreCase(id.trim())) {
                return a;
            }
        }
        return null;
    }

    public List<Aktoreak> bilatuAktoreaIzenez(String izenAbizena) {
        List<Aktoreak> aurkituak = new ArrayList<>();
        String gakoa = gakuaSortu(izenAbizena);
        for (Aktoreak a : zerrenda) {
            if (gakuaSortu(a.getIzena()).contains(gakoa)) {
                aurkituak.add(a);
            }
        }
        return aurkituak;
    }

    // ==================================================================
    // 3. Aktore berri bat txertatu
    // ==================================================================
    public boolean txertatuAktorea(Aktoreak a) {
        if (a != null && bilatuAktoreaIdz(a.getId()) == null) {
            return zerrenda.add(a);
        }
        return false;
    }

    // ==================================================================
    // 4. Aktore baten filmak itzultzea (ez inprimatzea)
    // ==================================================================
    public List<Filma> lortuAktorearenFilmak(String aktoreId) {
        Aktoreak a = bilatuAktoreaIdz(aktoreId);
        if (a != null) {
            return a.getFilmak(); // Filmen zerrenda itzultzen du
        }
        return new ArrayList<>();
    }

    // ==================================================================
    // 5. Aktore bat ezabatu
    // ==================================================================
    public boolean ezabatuAktorea(String aktoreId) {
        Aktoreak a = bilatuAktoreaIdz(aktoreId);
        if (a != null) {
            // Filmen aktore-zerrendatik erreferentzia kendu
            for (Filma f : a.getFilmak()) {
                f.ezabatuAktorea(a);
            }
            return zerrenda.remove(a);
        }
        return false;
    }

    // ==================================================================
    // 6. Zerrenda fitxategi batean gorde
    // ==================================================================
    public void gordeFitxategian(String fitxategiBidea) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fitxategiBidea))) {
            for (Aktoreak a : zerrenda) {
                for (Filma f : a.getFilmak()) {
                    bw.write(a.getId() + " ### " + a.getIzena() + " ### " + f.getId() + " ### " + f.getTitulua());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Errorea fitxategia gordetzean: " + e.getMessage());
        }
    }

    // ==================================================================
    // 7. Aktoreen zerrenda ordenatua lortu (jatorrizkoa aldatu gabe)
    // ==================================================================
    public List<Aktoreak> lortuAktoreakOrdenatuta() {
        List<Aktoreak> kopia = new ArrayList<>(this.zerrenda);
        Collections.sort(kopia, new Comparator<Aktoreak>() {
            @Override
            public int compare(Aktoreak a1, Aktoreak a2) {
                return a1.getIzena().compareToIgnoreCase(a2.getIzena());
            }
        });
        return kopia; // Zerrenda ordenatu berria itzultzen du, jatorrizkoa aldatu gabe
    }

    public static String gakuaSortu(String testua) {
        if (testua == null) return "";
        return testua.trim().toLowerCase();
    }
}
