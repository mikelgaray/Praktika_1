package PRAKTIKA;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PelikulaZerrenda {

    // Singleton instantzia bakarra
    private static PelikulaZerrenda nNirePelikulaZerrenda = null;

    // Datu-egitura nagusia: O(1) bilaketetarako
    private Map<String, Filma> filmakIdMap;

    // Eraikitzaile pribatua
    private PelikulaZerrenda() {
        this.filmakIdMap = new HashMap<>();
    }

    // Instantzia bakarra lortzeko metodo estatikoa
    public static synchronized PelikulaZerrenda getNirePelikulaZerrenda() {
        if (nNirePelikulaZerrenda == null) {
            nNirePelikulaZerrenda = new PelikulaZerrenda();
        }
        return nNirePelikulaZerrenda;
    }

    /**
     * Filma berria zerrendan txertatu
     */
    public boolean gehituFilma(Filma filma) {
        if (filma == null || filmakIdMap.containsKey(filma.getId())) {
            return false;
        }
        filmakIdMap.put(filma.getId(), filma);
        return true;
    }

    /**
     * Filma bilatu bere ID-aren bidez - O(1)
     */
    public Filma bilatuFilmaIdz(String id) {
        if (id == null) return null;
        return filmakIdMap.get(id.trim());
    }

    /**
     * Film bateko aktoreen zerrenda lortu (ez inprimatu) - O(1)
     */
    public List<Aktorea> lortuFilmarenAktoreak(String filmId) {
        Filma f = bilatuFilmaIdz(filmId);
        return (f != null) ? f.getAktoreak() : Collections.emptyList();
    }

    /**
     * Film baten estreinaldi urtea aldatu - O(1)
     */
    public boolean aldatuFilmUrtea(String filmId, int urteBerria) {
        Filma f = bilatuFilmaIdz(filmId);
        if (f != null) {
            f.setUrtea(urteBerria);
            return true;
        }
        return false;
    }

    public List<Filma> getFilmaGuztiak() {
        return new ArrayList<>(filmakIdMap.values());
    }

    public int getPelikulaKopurua() {
        return filmakIdMap.size();
    }

    public void garbitu() {
        this.filmakIdMap.clear();
    }
}