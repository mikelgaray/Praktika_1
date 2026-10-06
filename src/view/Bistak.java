package view;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import control.Kontrolatzailea;
import klaseak.Aktoreak;
import klaseak.Filmak;

public class Bistak {

    private final Kontrolatzailea kontrolatzailea;
    private final Scanner sarrera;

    public Bistak() {
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
            System.out.println("2. Aktore berria txertatu");
            System.out.println("5. Film bateko aktoreak");
            System.out.println("6. Film baten estreinaldiaren urtea aldatu");
            System.out.println("8. Zerrenda fitxategi batean gorde");
            System.out.println("9. Test-datuak sortu (20 aktore eta 20 film)");
            System.out.println("10.Aktoreak ordenatu (mergesort edo quicksort)");
            System.out.println("0. Irten");
            aukera = irakurriEdukia("Aukera: ", 0, 9);
            switch (aukera) {
            	case 2: aktoreaTxertatu(); break;
                case 5: filmarenAktoreak(); break;
                case 6: urteaAldatu(); break;
                case 8: zerrendaGorde(); break;
                case 9: testDatuak(); break;
                case 10: ordenatuAktoreak(); break;
                default: break;
            }
        } while (aukera != 0);
        System.out.println("Agur!");
    }

    // ==================================================================
    // Eragiketak
    // ==================================================================

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

    private void testDatuak() {
        kontrolatzailea.testDatuakSortu();
        System.out.println("Datuak sortuta: " + kontrolatzailea.aktoreKopurua() + " aktore, "
                + kontrolatzailea.filmaKopurua() + " film (filmen ID-ak: 101-120).");
    }

    // ==================================================================
    // Laguntzaileak
    // ==================================================================

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

    // Por si en el futuro quieres añadir buscar actores en el menú
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
    
    
    
    
    
    private void aktoreaTxertatu() {
    	String aukera;
        System.out.println("\n--- AKTORE BERRI BAT TXERTATU ---");
        System.out.print("Sartu aktorearen izena: ");
        String aktorearenIzena = sarrera.nextLine();

        Aktoreak aktoreBerria = kontrolatzailea.aktoreaTxertatu(aktorearenIzena);
        if (aktoreBerria == null) {
			System.out.println("Errorea: Aktorea ezin izan da sortu.");
			return;
		}
        System.out.println("Aktorea arrakastaz sortu da. Esleitutako IDa: " + aktoreBerria.getId());

        do {
            System.out.println("\nZer egin nahi duzu bere filmekin?");
            System.out.println("1. Lehendik dagoen film bati gehitu");
            System.out.println("2. Aktore honentzat film berri bat sortu");
            System.out.println("0. Ez gehitu filmik oraingoz");
            System.out.print("Aukera: ");
            aukera = sarrera.nextLine();

            switch (aukera) {
            case "1":
            	
            	//Lamar al metodo de mostrar filmak (Opcional)
                System.out.print("Sartu filmaren IDa (adib. 101-120): ");
                int filmaID = sarrera.nextInt();

                boolean loturaEginda = kontrolatzailea.aktoreaLotuFilma(aktoreBerria, filmaID);
                if (loturaEginda) {
                    System.out.println("Erlazioa sortu da.");
                } else {
                    System.out.println("Errorea: Ez da aurkitu ID hori duen filmarik.");
                }
                break;

            case "2":
                System.out.print("Sartu filma berriaren izenburua: ");
                String filmarenIzenburua = sarrera.nextLine();
                System.out.print("Sartu filma berriaren urtea: ");
                int urtea = sarrera.nextInt();
                Filmak filmBerria = kontrolatzailea.aktoreaLotuFilmaberria(aktoreBerria, filmarenIzenburua, urtea);
                if (filmBerria == null) {
					System.out.println("Errorea: Filma ezin izan da sortu.");
				} else {
                System.out.println("Filma sortu da " + filmBerria.getId() + " IDarekin eta aktorearekin erlazionatu da.");
				}
                break;

            case "0":
                System.out.println("Ados, aktorea gorde da.");
                break;

            default:
                System.out.println("Aukera baliogabea.");
                break;
            }
        } while (!aukera.equals("0"));
    }
    private void ordenatuAktoreak() {
    	System.out.println("Aktoreak ordenatzeko metodoa aukeratu: (mergesort edo quicksort)");
    	String metodoa = sarrera.nextLine().trim().toLowerCase();
    	if(metodoa.equals("mergesort")) {
    		kontrolatzailea.aktoreaOrdenatuMerge();
    	}else if(metodoa.equals("quicksort")) {
    		kontrolatzailea.aktoreaOrdenatuQuick();
    	}else {
			System.out.println("Metodo baliogabea. Mesedez, 'mergesort' edo 'quicksort' idatzi.");
		}
    }
}