package klaseak;

import java.util.Collection;
import java.util.HashMap;

public class FilmaZerrenda {
    private HashMap<Integer, Filmak> filmak;

    public FilmaZerrenda() {
        this.filmak = new HashMap<>();
    }

    public Filmak getFilma(int id) {
        return filmak.get(id);
    }

    public void setFilma(Filmak a) {
        if (a == null) throw new IllegalArgumentException("Pelikula ezin da null izan");
        filmak.put(a.getId(), a);
    }
    
    public int tamaina() { return filmak.size(); }

    public Collection<Filmak> getPelikulak() {
        return filmak.values();
    }
}