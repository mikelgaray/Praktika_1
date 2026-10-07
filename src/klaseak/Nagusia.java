package PRAKTIKA;

import java.util.List;

public class Nagusia {

    public static void main(String[] args) {
        Kudeatzailea kudeatzailea = new Kudeatzailea();

        System.out.println("==========================================");
        System.out.println("  AKTORE EDO FILMEN KUDEAKETA SISTEMA");
        System.out.println("==========================================\n");

        System.out.println("1. Proba datuak sortzen...");
        Aktorea a1 = new Aktorea("http://www.wikidata.org/entity/Q101080493", "Q101080493");
        Aktorea a2 = new Aktorea("http://www.wikidata.org/entity/Q101080945", "Q101080945");
        Filma f1 = new Filma("http://www.wikidata.org/entity/Q12047846", "Burglar and Umbrella", 1912);

        a1.gehituFilma(f1);
        f1.gehituAktorea(a1);
        a2.gehituFilma(f1);
        f1.gehituAktorea(a2);

        kudeatzailea.gehituAktorea(a1);
        kudeatzailea.gehituAktorea(a2);
        kudeatzailea.gehituFilma(f1);

        System.out.println(" - Kargatutako aktoreak: " + AktoreZerrenda.getNireAktoreZerrenda().getAktoreKopurua());
        System.out.println(" - Kargatutako filmak: " + PelikulaZerrenda.getNirePelikulaZerrenda().getPelikulaKopurua());

        System.out.println("\n2. 'Burglar and Umbrella' filmeko aktoreak lortzen:");
        List<Aktorea> aktoreak = kudeatzailea.lortuFilmarenAktoreak("http://www.wikidata.org/entity/Q12047846");
        for (Aktorea a : aktoreak) {
            System.out.println("   * " + a.getId() + " -> " + a.getIzena());
        }

        System.out.println("\n3. Filmaren estreinaldi urtea aldatzen (1912 -> 1915):");
        boolean aldatua = kudeatzailea.aldatuFilmUrtea("http://www.wikidata.org/entity/Q12047846", 1915);
        System.out.println("   * Aldatua: " + aldatua + " | Urte berria: " + f1.getUrtea());

        System.out.println("\n4. Aktoreen zerrenda ordenatua lortzen (jatorrizkoa aldatu gabe):");
        List<Aktorea> ordenatuak = kudeatzailea.lortuAktoreakOrdenatuta();
        for (Aktorea a : ordenatuak) {
            System.out.println("   * " + a.getIzena());
        }

        System.out.println("\n5. Aktore bat ezabatzen (http://www.wikidata.org/entity/Q101080493):");
        boolean ezabatua = kudeatzailea.ezabatuAktorea("http://www.wikidata.org/entity/Q101080493");
        System.out.println("   * Ezabatua: " + ezabatua);
        System.out.println("   * Geratzen diren aktoreak: " + AktoreZerrenda.getNireAktoreZerrenda().getAktoreKopurua());

        System.out.println("\n==========================================");
        System.out.println("  PROBA GUZTIAK ONGI BURUTU DIRA");
        System.out.println("==========================================");
    }
}