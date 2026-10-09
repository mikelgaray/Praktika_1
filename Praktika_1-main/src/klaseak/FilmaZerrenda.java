package klaseak;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class FilmaZerrenda {
    private final HashMap<String, Filmak> filmak;
    private int maxId = 0; // ID handiena ("Q123" -> 123), ID berriak O(1)-en sortzeko

    public FilmaZerrenda() {
        this.filmak = new HashMap<>();
    }

    public Filmak getFilma(String id) {
        return filmak.get(id);
    }

    public void setFilma(Filmak a) {
        if (a == null) throw new IllegalArgumentException("Pelikula ezin da null izan");
        filmak.put(a.getId(), a);
        maxId = Math.max(maxId, Integer.parseInt(a.getId().substring(1)));
    }

    public int tamaina() { return filmak.size(); }

    public Collection<Filmak> getPelikulak() {
        return filmak.values();
    }

    public boolean lotuAktoreaDagoenFilmari(Aktoreak aktorea, String filmaID) {
        Filmak dagoenFilma = filmak.get(filmaID);
        if (dagoenFilma != null) {
            if (!aktorea.getFilmak().contains(dagoenFilma)) { // erlazioa ez bikoiztu
                aktorea.getFilmak().add(dagoenFilma);
                dagoenFilma.getAktoreak().add(aktorea);
            }
            return true;
        }
        return false;
    }

    public Filmak sortuEtaLotuFilmaBerria(Aktoreak aktorea, String izenburua, int urtea) {
        Filmak filmBerria = new Filmak(izenburua, "Q" + (maxId + 1), urtea, new ArrayList<>());
        setFilma(filmBerria); // O(1), maxId kontagailuari esker

        aktorea.getFilmak().add(filmBerria);
        filmBerria.getAktoreak().add(aktorea);

        return filmBerria;
    }

}
