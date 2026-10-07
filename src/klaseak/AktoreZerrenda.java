package PRAKTIKA;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AktoreZerrenda {

    // Singleton instantzia bakarra
    private static AktoreZerrenda nNireAktoreZerrenda = null;

    // Datu-egiturak: O(1) bilaketak ahalbidetzeko
    private Map<String, Aktorea> aktoreakIdMap;
    private Map<String, List<Aktorea>> aktoreakIzenaMap;

    // Eraikitzaile pribatua (Singleton patroia)
    private AktoreZerrenda() {
        this.aktoreakIdMap = new HashMap<>();
        this.aktoreakIzenaMap = new HashMap<>();
    }

    // Instantzia bakarra lortzeko metodo estatikoa
    public static synchronized AktoreZerrenda getNireAktoreZerrenda() {
        if (nNireAktoreZerrenda == null) {
            nNireAktoreZerrenda = new AktoreZerrenda();
        }
        return nNireAktoreZerrenda;
    }

    /**
     * Aktore berria zerrendan txertatu
     */
    public boolean gehituAktorea(Aktorea aktorea) {
        if (aktorea == null || aktoreakIdMap.containsKey(aktorea.getId())) {
            return false;
        }
        aktoreakIdMap.put(aktorea.getId(), aktorea);
        aktoreakIzenaMap.computeIfAbsent(aktorea.getIzena(), k -> new ArrayList<>()).add(aktorea);
        return true;
    }

    /**
     * Aktorea bilatu bere ID-aren bidez - O(1)
     */
    public Aktorea bilatuAktoreaIdz(String id) {
        if (id == null) return null;
        return aktoreakIdMap.get(id.trim());
    }

    /**
     * Aktorea(k) bilatu izen-abizenen arabera - O(1)
     */
    public List<Aktorea> bilatuAktoreaIzenez(String izenAbizenak) {
        if (izenAbizenak == null) return Collections.emptyList();
        List<Aktorea> zerrenda = aktoreakIzenaMap.get(izenAbizenak.trim());
        if (zerrenda == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(zerrenda);
    }

    /**
     * Aktore baten filmak lortu (ez inprimatu) - O(1)
     */
    public List<Filma> lortuAktorearenFilmak(String aktoreId) {
        Aktorea a = bilatuAktoreaIdz(aktoreId);
        return (a != null) ? a.getFilmak() : Collections.emptyList();
    }

    /**
     * Aktore bat ezabatu eta bere erreferentzia guztiak garbitu - O(k)
     */
    public boolean ezabatuAktorea(String aktoreId) {
        Aktorea aktorea = aktoreakIdMap.remove(aktoreId);
        if (aktorea == null) {
            return false;
        }
        

        // Izenen mapatik kendu
        List<Aktorea> izenZerrenda = aktoreakIzenaMap.get(aktorea.getIzena());
        if (izenZerrenda != null) {
            izenZerrenda.remove(aktorea);
            if (izenZerrenda.isEmpty()) {
                aktoreakIzenaMap.remove(aktorea.getIzena());
            }
        }

        // Zuzenean parte hartu duen film bakoitzetik ezabatu aktorea
        for (Filma filma : aktorea.getFilmak()) {
            filma.ezabatuAktorea(aktorea);
        }

        return true;
    }

    /**
     * Aktoreen zerrenda izen-abizenez ordenatuta lortu (jatorrizkoa aldatu gabe)
     */
    public List<Aktorea> lortuAktoreakOrdenatuta() {
        List<Aktorea> kopia = new ArrayList<>(aktoreakIdMap.values());
        Collections.sort(kopia);
        return kopia;
    }

    public List<Aktorea> getAktoreGuztiak() {
        return new ArrayList<>(aktoreakIdMap.values());
    }

    public int getAktoreKopurua() {
        return aktoreakIdMap.size();
    }

    public void garbitu() {
        this.aktoreakIdMap.clear();
        this.aktoreakIzenaMap.clear();
    }
}