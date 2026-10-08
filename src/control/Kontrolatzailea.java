package control;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import klaseak.Aktoreak;
import klaseak.AktoreZerrenda;
import klaseak.Filmak;
import klaseak.FilmaZerrenda;

public class Kontrolatzailea {

    private static final String WIKIDATA_URL = "http://www.wikidata.org/entity/";
    private static final String BANATZAILEA = "###";

    private AktoreZerrenda aktoreZerrendaGlobala;
    private FilmaZerrenda filmaZerrendaGlobala;

    public Kontrolatzailea() {
        zerrendakBaieztatu();
    }

    // ==================================================================
    // Laguntzaileak
    // ==================================================================

    /** Zerrenda globalak existitzen direla bermatzen du; falta denik sortzen du. */
    private void zerrendakBaieztatu() {
        if (aktoreZerrendaGlobala == null) {
            aktoreZerrendaGlobala = new AktoreZerrenda();
        }
        if (filmaZerrendaGlobala == null) {
            filmaZerrendaGlobala = new FilmaZerrenda();
        }
    }

    /** "Q123" -> 123 (ordenaketetarako, ID-a zenbaki bihurtzen da lerro berean) */
    private static int idZenbakia(String id) {
        return Integer.parseInt(id.substring(1));
    }

    /** Aktorea eta filma erlazionatzen ditu bi norabidetan (bikoizketarik gabe). */
    private void erlazionatu(Aktoreak a, Filmak f) {
        if (!a.getFilmak().contains(f)) {
            a.getFilmak().add(f);
            f.getAktoreak().add(a);
        }
    }

    // ==================================================================
    // Bilaketak (Aktoreak)
    // ==================================================================

    /** ID bidez aktorea bilatu: O(1). Ez bada existitzen, null. */
    public Aktoreak aktoreaBilatu(String id) {
        zerrendakBaieztatu();
        return aktoreZerrendaGlobala.getAktorea(id);
    }

    /** Izen-abizenez bilatu: O(log n). Homonimoak daudenez, zerrenda bat itzultzen da. */
    public List<Aktoreak> aktoreakBilatu(String izena) {
        zerrendakBaieztatu();
        if (izena == null || izena.isBlank()) return Collections.emptyList();
        return aktoreZerrendaGlobala.bilatuIzenez(izena);
    }

    /** Kopia berri bat, izen-abizenen arabera ordenatuta: O(n). */
    public List<Aktoreak> aktoreakOrdenatuta() {
        zerrendakBaieztatu();
        return aktoreZerrendaGlobala.ordenatutakoZerrenda();
    }

    // ==================================================================
    // Bilaketak (Filmak)
    // ==================================================================

    /** ID bidez filma bilatu: O(1). Ez bada existitzen, null. */
    public Filmak filmaBilatu(String id) {
        zerrendakBaieztatu();
        return filmaZerrendaGlobala.getFilma(id);
    }
    
    // Método alias para mantener la compatibilidad con Bista/Bistak
    public Filmak pelikulaBilatu(String id) {
        return filmaBilatu(id);
    }

    /** Izenez filmak bilatu (maiuskulak, azentuak eta tarteak kontuan hartu gabe): O(m). IDz ordenatuta. */
    public List<Filmak> filmakBilatu(String izena) {
        zerrendakBaieztatu();
        if (izena == null || izena.isBlank()) return Collections.emptyList();
        String gakoa = AktoreZerrenda.gakuaSortu(izena);
        List<Filmak> emaitza = new ArrayList<>();
        for (Filmak f : filmaZerrendaGlobala.getPelikulak()) {
            if (AktoreZerrenda.gakuaSortu(f.getTitulua()).equals(gakoa)) emaitza.add(f);
        }
        emaitza.sort((f1, f2) -> Integer.compare(idZenbakia(f1.getId()), idZenbakia(f2.getId())));
        return emaitza;
    }

    // Método alias para mantener la compatibilidad con Bista/Bistak
    public List<Filmak> pelikulakBilatu(String izena) {
        return filmakBilatu(izena);
    }

    /** Izenean testua duten filmak (gehienez "muga"), izenaren eta IDren arabera ordenatuta. */
    public List<Filmak> filmakAntzekoak(String testua, int muga) {
        zerrendakBaieztatu();
        if (testua == null || testua.isBlank() || muga <= 0) return Collections.emptyList();
        String gakoa = AktoreZerrenda.gakuaSortu(testua);
        List<Filmak> emaitza = new ArrayList<>();
        for (Filmak f : filmaZerrendaGlobala.getPelikulak()) {
            if (AktoreZerrenda.gakuaSortu(f.getTitulua()).contains(gakoa)) emaitza.add(f);
        }
        emaitza.sort((f1, f2) -> {
            int c = f1.getTitulua().compareToIgnoreCase(f2.getTitulua());
            return c != 0 ? c : Integer.compare(idZenbakia(f1.getId()), idZenbakia(f2.getId()));
        });
        return emaitza.size() > muga ? new ArrayList<>(emaitza.subList(0, muga)) : emaitza;
    }

    // Método alias para mantener la compatibilidad con Bista/Bistak
    public List<Filmak> pelikulakAntzekoak(String testua, int muga) {
        return filmakAntzekoak(testua, muga);
    }

    // ==================================================================
    // 5. Film bateko aktoreak (itzuli, ez inprimatu)
    // ==================================================================

    /** Irakurtzeko soilik den ikuspegia; filma ez badago, zerrenda hutsa. */
    public List<Aktoreak> pelikularenAktoreak(String filmaId) {
        zerrendakBaieztatu();
        Filmak f = filmaZerrendaGlobala.getFilma(filmaId);
        if (f == null) return Collections.emptyList();
        return Collections.unmodifiableList(f.getAktoreak());
    }

    // ==================================================================
    // 6. Film baten estreinaldiaren urtea aldatu
    // ==================================================================

    /** @return false filma existitzen ez bada */
    public boolean filmaUrteaAldatu(String filmaId, int urteBerria) {
        zerrendakBaieztatu();
        Filmak f = filmaZerrendaGlobala.getFilma(filmaId);
        if (f == null) return false;
        f.setUrtea(urteBerria);
        return true;
    }

    // ==================================================================
    // 8. Zerrenda fitxategi batean gorde
    // ==================================================================

    /**
     * Bi ataletan gordetzen du: lehenik aktore GUZTIAK (izenaren arabera ordenatuta),
     * gero filma GUZTIAK (izenaren arabera ordenatuta).
     */
    public void zerrendaGorde(Path helburua) throws IOException {
        zerrendakBaieztatu();
        List<Aktoreak> aktoreak = aktoreZerrendaGlobala.ordenatutakoZerrenda();
        List<Filmak> filmak = new ArrayList<>(filmaZerrendaGlobala.getPelikulak());
        filmak.sort((f1, f2) -> {
            int c = f1.getTitulua().compareToIgnoreCase(f2.getTitulua());
            return c != 0 ? c : Integer.compare(idZenbakia(f1.getId()), idZenbakia(f2.getId()));
        });

        try (BufferedWriter idazlea = Files.newBufferedWriter(helburua, StandardCharsets.UTF_8)) {
            idazlea.write("=== AKTOREAK (" + aktoreak.size() + ") ===");
            idazlea.newLine();
            for (Aktoreak a : aktoreak) {
                idazlea.write(WIKIDATA_URL + a.getId() + " " + BANATZAILEA + " " + a.getIzena());
                idazlea.newLine();
            }
            idazlea.newLine();
            idazlea.write("=== FILMAK (" + filmak.size() + ") ===");
            idazlea.newLine();
            for (Filmak f : filmak) {
                idazlea.write(WIKIDATA_URL + f.getId() + " " + BANATZAILEA + " " + f.getTitulua()
                        + " " + BANATZAILEA + " " + f.getUrtea());
                idazlea.newLine();
            }
        }
    }

    // ==================================================================
    // Test-datuak: 20 aktore errealak (ID 1-20) eta 20 film errealak (ID 101-120)
    // ==================================================================

    private static final String[] TEST_AKTOREAK = {
        "Leonardo DiCaprio", "Kate Winslet", "Tom Hardy", "Samuel L. Jackson", "Uma Thurman",
        "John Travolta", "Bruce Willis", "Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss",
        "Hugo Weaving", "Joseph Gordon-Levitt", "Christoph Waltz", "Jamie Foxx", "Jim Carrey",
        "Adrien Brody", "Adriana Barraza", "Brad Pitt", "Edward Norton", "Alan Rickman"
    };

    private static final String[] TEST_FILMAK = {
        "Pulp Fiction", "The Matrix", "Titanic", "Inception", "The Revenant",
        "Django Unchained", "Die Hard", "Fight Club", "Twelve Monkeys", "The Grand Budapest Hotel",
        "Babel", "Eternal Sunshine of the Spotless Mind", "Sense and Sensibility", "Inglourious Basterds",
        "The Dark Knight Rises", "The Matrix Reloaded", "The Matrix Revolutions",
        "Once Upon a Time in Hollywood", "Basic", "Unbreakable"
    };

    private static final int[] TEST_URTEAK = {
        1994, 1999, 1997, 2010, 2015, 2012, 1988, 1999, 1995, 2014,
        2006, 2004, 1995, 2009, 2012, 2003, 2003, 2019, 2003, 2000
    };

    private static final int[][] TEST_ERLAZIOAK = {
        {6, 4, 5, 7}, {8, 9, 10, 11}, {1, 2}, {1, 12, 3}, {1, 3},
        {14, 13, 1, 4}, {7, 20}, {18, 19}, {7, 18}, {16, 19},
        {17, 18}, {2, 15}, {2, 20}, {18, 13},
        {3, 12}, {8, 9, 10, 11}, {8, 9, 10, 11},
        {1, 18}, {6, 4}, {7, 4}
    };

    public void testDatuakSortu() {
        zerrendakBaieztatu();
        List<Aktoreak> aktoreak = new ArrayList<>();
        for (int i = 0; i < TEST_AKTOREAK.length; i++) {
            Aktoreak a = aktoreZerrendaGlobala.getAktorea("Q" + (i + 1));
            if (a == null) {
                a = new Aktoreak(TEST_AKTOREAK[i], "Q" + (i + 1), new ArrayList<Filmak>());
                aktoreZerrendaGlobala.gehituAktorea(a);
            }
            aktoreak.add(a);
        }
        for (int j = 0; j < TEST_FILMAK.length; j++) {
            Filmak f = filmaZerrendaGlobala.getFilma("Q" + (101 + j));
            if (f == null) {
                f = new Filmak(TEST_FILMAK[j], "Q" + (101 + j), TEST_URTEAK[j], new ArrayList<Aktoreak>());
                filmaZerrendaGlobala.setFilma(f);
            }
            for (int posizioa : TEST_ERLAZIOAK[j]) {
                erlazionatu(aktoreak.get(posizioa - 1), f);
            }
        }
    }

    // ==================================================================
    // Neurketak: datu askorekin eragiketa bakoitzak zenbat irauten duen (µs)
    // ==================================================================

    private static double batezbestekoa(int errepikapenak, Runnable ekintza) {
        long hasiera = System.nanoTime();
        for (int i = 0; i < errepikapenak; i++) ekintza.run();
        return (System.nanoTime() - hasiera) / 1000.0 / errepikapenak;
    }

    public Map<String, Double> neurketak(int n) throws IOException {
        zerrendakBaieztatu();
        Map<String, Double> emaitza = new LinkedHashMap<>();
        final int m = Math.max(1, n / 5);
        final Random rnd = new Random(42);

        List<Integer> zenbakiak = new ArrayList<>(n);
        for (int i = 0; i < n; i++) zenbakiak.add(i);
        Collections.shuffle(zenbakiak, rnd);

        long hasiera = System.nanoTime();
        final List<Aktoreak> aktoreak = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            Aktoreak a = new Aktoreak(String.format("Aktore %07d", zenbakiak.get(i)), "Q" + (i + 1), new ArrayList<Filmak>());
            aktoreZerrendaGlobala.gehituAktorea(a);
            aktoreak.add(a);
        }
        List<Filmak> filmak = new ArrayList<>(m);
        for (int j = 0; j < m; j++) {
            Filmak f = new Filmak("Film " + j, "Q" + (j + 1), 1900 + rnd.nextInt(120), new ArrayList<Aktoreak>());
            filmaZerrendaGlobala.setFilma(f);
            filmak.add(f);
        }
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < 3; k++) erlazionatu(aktoreak.get(i), filmak.get((i + k * 7919) % m));
        }
        emaitza.put("Kargatu (" + n + " aktore, " + m + " film, " + 3 * n + " erlazio)",
                (System.nanoTime() - hasiera) / 1000.0);

        emaitza.put("Aktorea izenez bilatu [TreeMap, O(log n)]",
                batezbestekoa(1000, () -> aktoreakBilatu(aktoreak.get(rnd.nextInt(n)).getIzena())));
        emaitza.put("Filma IDz bilatu [HashMap, O(1)]",
                batezbestekoa(1000, () -> filmaBilatu("Q" + (1 + rnd.nextInt(m)))));
        emaitza.put("Filma izenez bilatu [zeharkaldi lineala, O(m)]",
                batezbestekoa(20, () -> filmakBilatu("Film " + rnd.nextInt(m))));
        emaitza.put("Film bateko aktoreak itzuli",
                batezbestekoa(1000, () -> pelikularenAktoreak("Q" + (1 + rnd.nextInt(m)))));
        emaitza.put("Film baten urtea aldatu",
                batezbestekoa(1000, () -> filmaUrteaAldatu("Q" + (1 + rnd.nextInt(m)), 1900 + rnd.nextInt(120))));

        final int[] txertatuak = {n};
        emaitza.put("Aktore berria txertatu [TreeMap + HashMap]",
                batezbestekoa(1000, () -> {
                    int id = ++txertatuak[0];
                    aktoreZerrendaGlobala.gehituAktorea(new Aktoreak("Berria " + id, "Q" + id, new ArrayList<Filmak>()));
                }));
        final int[] ezabatuak = {n};
        emaitza.put("Aktorea ezabatu",
                batezbestekoa(1000, () -> aktoreZerrendaGlobala.ezabatuAktorea("Q" + (++ezabatuak[0]))));

        emaitza.put("Aktoreen zerrenda ordenatua lortu [O(n)]",
                batezbestekoa(5, () -> aktoreakOrdenatuta()));

        Path tmp = Files.createTempFile("neurketa", ".txt");
        try {
            hasiera = System.nanoTime();
            zerrendaGorde(tmp);
            emaitza.put("Zerrenda fitxategian gorde", (System.nanoTime() - hasiera) / 1000.0);
        } finally {
            Files.deleteIfExists(tmp);
        }
        return emaitza;
    }

    public int aktoreKopurua() { zerrendakBaieztatu(); return aktoreZerrendaGlobala.tamaina(); }

    public int filmaKopurua() { zerrendakBaieztatu(); return filmaZerrendaGlobala.tamaina(); }
    
    //Aktoreak txertatzeko kontrolatzaile metodoa
    public Aktoreak aktoreaTxertatu(String izena) {
        zerrendakBaieztatu();
        if (izena == null || izena.isBlank()) return null;
        return AktoreZerrenda.gordeAktoreBerria(izena);
    }
    //Lotu aktoreak filmarekin
    public boolean aktoreaLotuFilma(Aktoreak aktorea, String filmaID) {
		zerrendakBaieztatu();
		if (aktorea == null) return false;
		return filmaZerrendaGlobala.lotuAktoreaDagoenFilmari(aktorea, filmaID);
	}
    //Lotu aktoreak filma berriarekin
    public Filmak aktoreaLotuFilmaberria(Aktoreak aktorea, String izenburua, int urtea) {
		zerrendakBaieztatu();
		if (aktorea == null || izenburua == null || urtea == 0) return null;
		return filmaZerrendaGlobala.sortuEtaLotuFilmaBerria(aktorea, izenburua, urtea);
	}
    // MergeSort Metodoa aktoreak ordenatzeko
    public void aktoreaOrdenatuMerge() {
		zerrendakBaieztatu();
		AktoreZerrenda.mergeSortAktoreak();
	}
    
    // QuickSort Metodoa aktoreak ordenatzeko
    public void aktoreaOrdenatuQuick() {
		zerrendakBaieztatu();
		AktoreZerrenda.quickSortAktoreak();
	}
}