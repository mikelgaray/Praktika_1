package PRAKTIKA;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Kudeatzailea {

    private AktoreZerrenda aktoreZerrenda;
    private PelikulaZerrenda pelikulaZerrenda;

    public Kudeatzailea() {
        this.aktoreZerrenda = AktoreZerrenda.getNireAktoreZerrenda();
        this.pelikulaZerrenda = PelikulaZerrenda.getNirePelikulaZerrenda();
    }

    /**
     * Datuak fitxategitik kargatu
     */
    public void kargatuFitxategia(String fitxategiBidea, int urtea) {
        try (BufferedReader br = new BufferedReader(new FileReader(fitxategiBidea))) {
            String lerroa;
            while ((lerroa = br.readLine()) != null) {
                if (lerroa.trim().isEmpty()) continue;

                String[] zatiak = lerroa.split("###");
                if (zatiak.length < 4) continue;

                String aktoreId = zatiak[0].trim();
                String aktoreIzena = zatiak[1].trim();
                String filmId = zatiak[2].trim();
                String filmIzena = zatiak[3].trim();

                Aktorea aktorea = aktoreZerrenda.bilatuAktoreaIdz(aktoreId);
                if (aktorea == null) {
                    aktorea = new Aktorea(aktoreId, aktoreIzena);
                    aktoreZerrenda.gehituAktorea(aktorea);
                }

                Filma filma = pelikulaZerrenda.bilatuFilmaIdz(filmId);
                if (filma == null) {
                    filma = new Filma(filmId, filmIzena, urtea);
                    pelikulaZerrenda.gehituFilma(filma);
                }

                aktorea.gehituFilma(filma);
                filma.gehituAktorea(aktorea);
            }
        } catch (IOException e) {
            System.err.println("Errorea fitxategia irakurtzean: " + e.getMessage());
        }
    }

    /**
     * Datuak fitxategi batean gorde
     */
    public void gordeFitxategian(String fitxategiBidea) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fitxategiBidea))) {
            for (Aktorea aktorea : aktoreZerrenda.getAktoreGuztiak()) {
                for (Filma filma : aktorea.getFilmak()) {
                    String lerroa = String.format("%s ### %s ### %s ### %s",
                            aktorea.getId(),
                            aktorea.getIzena(),
                            filma.getId(),
                            filma.getIzenburua());
                    bw.write(lerroa);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Errorea fitxategian gorde bitartean: " + e.getMessage());
        }
    }

    // Facade metodoak erraztasunerako
    public List<Aktorea> bilatuAktoreaIzenez(String izena) {
        return aktoreZerrenda.bilatuAktoreaIzenez(izena);
    }

    public boolean gehituAktorea(Aktorea aktorea) {
        return aktoreZerrenda.gehituAktorea(aktorea);
    }

    public boolean gehituFilma(Filma filma) {
        return pelikulaZerrenda.gehituFilma(filma);
    }

    public List<Filma> lortuAktorearenFilmak(String aktoreId) {
        return aktoreZerrenda.lortuAktorearenFilmak(aktoreId);
    }

    public List<Aktorea> lortuFilmarenAktoreak(String filmId) {
        return pelikulaZerrenda.lortuFilmarenAktoreak(filmId);
    }

    public boolean aldatuFilmUrtea(String filmId, int urteBerria) {
        return pelikulaZerrenda.aldatuFilmUrtea(filmId, urteBerria);
    }

    public boolean ezabatuAktorea(String aktoreId) {
        return aktoreZerrenda.ezabatuAktorea(aktoreId);
    }

    public List<Aktorea> lortuAktoreakOrdenatuta() {
        return aktoreZerrenda.lortuAktoreakOrdenatuta();
    }
}