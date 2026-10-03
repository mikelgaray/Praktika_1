package view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import control.Kontrolatzailea;
import klaseak.Aktoreak;
import klaseak.Filmak;



public class Bistak {
	
	private final Kontrolatzailea kontrolatzailea;
    private final Scanner sarrera;

    /** Azken aldiz exekutatutako eragiketa (1-9). -1 = oraindik bat ere ez. */
    private int azkenAukera = -1;

    public Bista() {
        this.kontrolatzailea = new Kontrolatzailea();
        this.sarrera = new Scanner(System.in);
    }
    
	// ==================================================================
    // Eragiketak
    // ==================================================================
	
	/** 5. Pelikula bateko aktoreak. */
    private void filmarenAktoreak() {
        Filmak f = filmaAukeratu();
        if (f == null) return;
        List<Aktoreak> aktoreak = kontrolatzailea.pelikularenAktoreak(f.getId());
        if (aktoreak.isEmpty()) {
            System.out.println("Pelikulak ez du aktorerik.");
            return;
        }
        System.out.println(f.getTitulua() + " (" + f.getUrtea() + ") -> " + aktoreak.size() + " aktore:");
        for (Aktoreak a : aktoreak) System.out.println("  " + a);
    }
    
    /**
     * Pelikula bat aukeratzen du, IDz edo izenez. Izen bereko pelikula batzuk badaude,
     * zerrenda bat erakusten da eta bat aukeratu behar da.
     *
     * @return aukeratutako pelikula, edo null ez bada aurkitu
     */
    private Filmak filmaAukeratu() {
        System.out.println("Pelikula:  1. IDz   2. Izenez");
        int mota = irakurriEdukia("Aukera: ", 1, 2);
        if (mota == 1) {
            int id = irakurriEdukia("Pelikularen ID-a: ");
            Filmak f = kontrolatzailea.pelikulaBilatu(id);
            if (f == null) System.out.println("Ez da pelikularik aurkitu ID horrekin.");
            return f;
        }
        String izena = irakurriTestua("Pelikularen izena: ");
        List<Filmak> aurkituak = kontrolatzailea.pelikulakBilatu(izena);
        if (aurkituak.isEmpty()) {
            System.out.println("Ez da pelikularik aurkitu.");
            List<Filmak> antzekoak = kontrolatzailea.pelikulakAntzekoak(izena, 5);
            if (!antzekoak.isEmpty()) {
                System.out.println("Antzeko izenak:");
                for (Filmak f : antzekoak) filmaInprimatu(f);
            }
            return null;
        }
        if (aurkituak.size() == 1) return aurkituak.get(0);

        System.out.println(aurkituak.size() + " filma aurkitu dira izen horrekin:");
        for (int i = 0; i < aurkituak.size(); i++) {
            Filmak f = aurkituak.get(i);
            System.out.println("  " + (i + 1) + ". " + f.getId() + " - " + f.getTitulua()
                    + " (" + f.getUrtea() + ")  [" + f.getAktoreak().size() + " aktore]");
        }
        int n = irakurriEdukia("Aukeratu zein (1-" + aurkituak.size() + "): ", 1, aurkituak.size());
        return aurkituak.get(n - 1);
    }
    
    /**
     * Izen zehatza aurkitu ez denean, proposamenak ematen ditu: lehenik testu osoa duten
     * aktoreak, eta ezer ez bada, hitz bakoitzeko (luzeenetik hasita) lehen 5 aktoreak.
     */
    private void antzekoakErakutsi(String testua) {
        List<Aktoreak> denak = kontrolatzailea.aktoreakOrdenatuta();
        String gakoa = klaseak.AktoreZerrenda.gakuaSortu(testua);
        List<String> bilatzekoak = new ArrayList<>();
        bilatzekoak.add(gakoa);
        String[] hitzak = gakoa.split(" ");
        List<String> hitzZerrenda = new ArrayList<>();
        for (String h : hitzak) if (h.length() >= 3) hitzZerrenda.add(h);
        hitzZerrenda.sort((a, b) -> b.length() - a.length());
        if (hitzZerrenda.size() > 1) bilatzekoak.addAll(hitzZerrenda);

        for (String bilatua : bilatzekoak) {
            int kopurua = 0;
            for (Aktoreak a : denak) {
                if (klaseak.AktoreZerrenda.gakuaSortu(a.getIzena()).contains(bilatua)) {
                    if (kopurua == 0) System.out.println("\"" + bilatua + "\" duten izenak:");
                    System.out.println("  " + a);
                    if (++kopurua == 5) break;
                }
            }
            if (kopurua > 0) return;
        }
    }
    
    // ==================================================================
    // Inprimatzeko laguntzaileak
    // ==================================================================
    
    /**
     * Aktore bat aukeratzen du, IDz edo izen-abizenez. Izen bereko aktore batzuk badaude,
     * zerrenda bat erakusten da eta bat aukeratu behar da.
     *
     * @return aukeratutako aktorea, edo null ez bada aurkitu
     */
    private Aktoreak aktoreaAukeratu(String mezua) {
        System.out.println(mezua + ":  1. IDz   2. Izen-abizenez");
        int mota = irakurriEdukia("Aukera: ", 1, 2);
        if (mota == 1) {
            int id = irakurriEdukia("Aktorearen ID-a: ");
            Aktoreak a = kontrolatzailea.aktoreaBilatu(id);
            if (a == null) System.out.println("Ez da aktorerik aurkitu ID horrekin.");
            return a;
        }
        String izena = irakurriTestua("Izen-abizenak: ");
        List<Aktoreak> aurkituak = kontrolatzailea.aktoreakBilatu(izena);
        if (aurkituak.isEmpty()) {
            System.out.println("Ez da aktorerik aurkitu.");
            antzekoakErakutsi(izena);
            return null;
        }
        if (aurkituak.size() == 1) return aurkituak.get(0);

        System.out.println(aurkituak.size() + " aktore aurkitu dira izen horrekin:");
        for (int i = 0; i < aurkituak.size(); i++) {
            Aktoreak a = aurkituak.get(i);
            System.out.println("  " + (i + 1) + ". " + a + "  [" + a.getFilmak().size() + " pelikula]");
        }
        int n = irakurriEdukia("Aukeratu zein (1-" + aurkituak.size() + "): ", 1, aurkituak.size());
        return aurkituak.get(n - 1);
    }

    /**
     * Pelikula bat aukeratzen du, IDz edo izenez. Izen bereko pelikula batzuk badaude,
     * zerrenda bat erakusten da eta bat aukeratu behar da.
     *
     * @return aukeratutako pelikula, edo null ez bada aurkitu
     */
    private Filmak pelikulaAukeratu() {
        System.out.println("Pelikula:  1. IDz   2. Izenez");
        int mota = irakurriEdukia("Aukera: ", 1, 2);
        if (mota == 1) {
            int id = irakurriEdukia("Pelikularen ID-a: ");
            Filmak f = kontrolatzailea.pelikulaBilatu(id);
            if (f == null) System.out.println("Ez da pelikularik aurkitu ID horrekin.");
            return f;
        }
        String izena = irakurriTestua("Pelikularen izena: ");
        List<Filmak> aurkituak = kontrolatzailea.pelikulakBilatu(izena);
        if (aurkituak.isEmpty()) {
            System.out.println("Ez da pelikularik aurkitu.");
            List<Filmak> antzekoak = kontrolatzailea.pelikulakAntzekoak(izena, 5);
            if (!antzekoak.isEmpty()) {
                System.out.println("Antzeko izenak:");
                for (Filmak f : antzekoak) filmaInprimatu(f);
            }
            return null;
        }
        if (aurkituak.size() == 1) return aurkituak.get(0);

        System.out.println(aurkituak.size() + " pelikula aurkitu dira izen horrekin:");
        for (int i = 0; i < aurkituak.size(); i++) {
            Filmak f = aurkituak.get(i);
            System.out.println("  " + (i + 1) + ". " + f.getId() + " - " + f.getTitulua()
                    + " (" + f.getUrtea() + ")  [" + f.getAktoreak().size() + " aktore]");
        }
        int n = irakurriEdukia("Aukeratu zein (1-" + aurkituak.size() + "): ", 1, aurkituak.size());
        return aurkituak.get(n - 1);
    }

    private void aktoreaInprimatu(Aktoreak a) {
        System.out.println("  " + a + "  [" + a.getFilmak().size() + " pelikula]");
    }

    private void filmaInprimatu(Filmak p) {
        System.out.println("  " + p.getId() + " - " + p.getTitulua() + " (" + p.getUrtea() + ")");
    }
   
    
    // ==================================================================
    // Sarrera laguntzaileak
    // ==================================================================
    
    private String irakurriTestua(String mezua) {
        while (true) {
            System.out.print(mezua);
            String s = sarrera.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("Ezin da hutsik egon.");
        }
    }

    private int irakurriEdukia(String mezua) {
        return irakurriEdukia(mezua, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private int irakurriEdukia(String mezua, int min, int max) {
        while (true) {
            System.out.print(mezua);
            String s = sarrera.nextLine().trim();
            try {
                int n = Integer.parseInt(s);
                if (n >= min && n <= max) return n;
                System.out.println("Balioa " + min + " eta " + max + " artean egon behar da.");
            } catch (NumberFormatException e) {
                System.out.println("Zenbaki oso bat idatzi, mesedez.");
            }
        }
    }
    
}
