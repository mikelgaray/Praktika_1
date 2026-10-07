package PRAKTIKA;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmaZerrenda {

    private static FilmaZerrenda nireFilmaZerrenda = null;
    private List<Filma> zerrenda;

    public FilmaZerrenda() {
        this.zerrenda = new ArrayList<>();
    }

    public static FilmaZerrenda getNireFilmaZerrenda() {
        if (nireFilmaZerrenda == null) {
            nireFilmaZerrenda = new FilmaZerrenda();
        }
        return nireFilmaZerrenda;
    }

    public List<Filma> getZerrenda() {
        return zerrenda;
    }

    public void gehitzenFilma(Filma f) {
        if (f != null && !zerrenda.contains(f)) {
            zerrenda.add(f);
        }
    }

    public Filma bilatuFilmaIdz(String id) {
        for (Filma f : zerrenda) {
            if (f.getId().equalsIgnoreCase(id.trim())) {
                return f;
            }
        }
        return null;
    }

    /**
     * Film bateko aktoreak itzultzea (ez inprimatzea)
     */
    public List<Aktoreak> lortuFilmarenAktoreak(String filmaId) {
        Filma f = bilatuFilmaIdz(filmaId);
        if (f != null) {
            return f.getAktoreak(); // Aktoreen zerrenda itzultzen du
        }
        return new ArrayList<>();
    }

    /**
     * Film baten estreinaldiaren urtea aldatzea
     */
    public boolean urteaAldatu(String filmaId, int urteaBerria) {
        Filma f = bilatuFilmaIdz(filmaId);
        if (f != null) {
            f.setUrtea(urteaBerria);
            return true;
        }
        return false;
    }
}
