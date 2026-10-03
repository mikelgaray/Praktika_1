package view;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import control.Kontrolatzailea;
import klaseak.Aktoreak;
import klaseak.Filmak;

public class Bista {

    private final Kontrolatzailea kontrolatzailea;
    private final Scanner sarrera;

    public Bista() {
        this.kontrolatzailea = new Kontrolatzailea();
        this.sarrera = new Scanner(System.in);
    }

    // ==================================================================
    // Menua
    // ==================================================================

    public void hasi() {
        int aukera;
        do {
            System.out.println();
            System.out.println("===== MENUA =====");
            System.out.println("5. Film bateko aktoreak");
            System.out.println("6. Film baten estreinaldiaren urtea aldatu");
            System.out.println("8. Zerrenda fitxategi batean gorde");
            System.out.println("9. Test-datuak sortu (20 aktore eta 20 film)");
            System.out.println("0. Irten");
            aukera = irakurriEdukia("Aukera: ", 0, 9);
            switch (aukera) {
                case 5: filmarenAktoreak(); break;
                case 6: urteaAldatu(); break;
                case 8: zerrendaGorde(); break;
                case 9: testDatuak(); break;
                default: break;
            }
        } while (aukera != 0);
        System.out.println("Agur!");
    }

    // ==================================================================
    // Eragiketak
    // ==================================================================

    /** 5. Film bateko aktoreak. */
    private void filmarenAktoreak() {
        Filmak f = filmaAukeratu();
        if (f == null) return;
        List<Aktoreak> aktoreak = kontrolatzailea.pelikularenAktoreak(f.getId());
        if (aktoreak.isEmpty()) {
            System.out.println("Filmak ez du aktorerik.");
            return;
        }
        System.out.println(f.getTitulua() + " (" + f.getUrtea() + ") -> " + aktoreak.size() + " aktore:");
        for (Aktoreak a : aktoreak) System.out.println("  " + a);
    }

    /** 6. Estreinaldiaren urtea aldatu. */
    private void urteaAldatu() {
        Filmak f = filmaAukeratu();
        if (f == null) return;
        System.out.println("Oraingo urtea: " + f.getUrtea());
        int urtea = irakurriEdukia("Urte berria: ", 1, 9999);
        if (kontrolatzailea.filmaUrteaAldatu(f.getId(), urtea)) {
            System.out.println("Urtea aldatuta: " + f.getTitulua() + " -> " + urtea);
        } else {
            System.out.println("Ezin izan da urtea aldatu.");
        }
    }

    /** 8. Zerrenda fitxategi batean gorde. */
    private void zerrendaGorde() {
        String izena = irakurriTestua("Fitxategiaren izena (adib. zerrenda.txt): ");
        Path helburua = Paths.get(izena);
        try {
            kontrolatzailea.zerrendaGorde(helburua);
            System.out.println("Gordeta: " + helburua.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Errorea fitxategia gordetzean: " + e.getMessage());
        }
    }

    /** 0. Test-datuak. */
    private void testDatuak() {
        kontrolatzailea.testDatuakSortu();
        System.out.println("Datuak sortuta: " + kontrolatzailea.aktoreKopurua() + " aktore, "
                + kontrolatzailea.filmaKopurua() + " film (filmen ID-ak: 101-120).");
    }

    // ==================================================================
    // Laguntzaileak
    // ==================================================================

    /** Filma aukeratzen du IDz edo izenez. @return filma, edo null ez bada aurkitu */
    private Filmak filmaAukeratu() {
        System.out.println("Filma:  1. IDz   2. Izenez");
        int mota = irakurriEdukia("Aukera: ", 1, 2);
        if (mota == 1) {
            int id = irakurriEdukia("Filmaren ID-a: ");
            Filmak f = kontrolatzailea.filmaBilatu(id);
            if (f == null) System.out.println("Ez da filmarik aurkitu ID horrekin.");
            return f;
        }
        String izena = irakurriTestua("Filmaren izena: ");
        List<Filmak> aurkituak = kontrolatzailea.filmakBilatu(izena);
        if (aurkituak.isEmpty()) {
            System.out.println("Ez da filmarik aurkitu.");
            List<Filmak> antzekoak = kontrolatzailea.filmakAntzekoak(izena, 5);
            if (!antzekoak.isEmpty()) {
                System.out.println("Antzeko izenak:");
                for (Filmak f : antzekoak) filmaInprimatu(f);
            }
            return null;
        }
        if (aurkituak.size() == 1) return aurkituak.get(0);

        System.out.println(aurkituak.size() + " film aurkitu dira izen horrekin:");
        for (int i = 0; i < aurkituak.size(); i++) {
            Filmak f = aurkituak.get(i);
            System.out.println("  " + (i + 1) + ". " + f.getId() + " - " + f.getTitulua()
                    + " (" + f.getUrtea() + ")  [" + f.getAktoreak().size() + " aktore]");
        }
        int n = irakurriEdukia("Aukeratu zein (1-" + aurkituak.size() + "): ", 1, aurkituak.size());
        return aurkituak.get(n - 1);
    }

    private void filmaInprimatu(Filmak f) {
        System.out.println("  " + f.getId() + " - " + f.getTitulua() + " (" + f.getUrtea() + ")");
    }

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