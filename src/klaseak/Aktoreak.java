package klaseak;

import java.util.ArrayList;

public class Aktoreak {

    private String id;       // Wikidata ID / Etiketa
    private String izena;    // Izen-abizenak
    private List<Filma> filmak;

    public Aktoreak(String id, String izena) {
        this.id = id;
        this.izena = izena;
        this.filmak = new ArrayList<>();
    }

    // Getters eta Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }

    public List<Filma> getFilmak() { return filmak; }

    public void gehitzenFilma(Filma f) {
        if (f != null && !this.filmak.contains(f)) {
            this.filmak.add(f);
        }
    }

    public void ezabatuFilma(Filma f) {
        this.filmak.remove(f);
    }

    @Override
    public String toString() {
        return id + " ### " + izena;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aktoreak aktoreak = (Aktoreak) o;
        return Objects.equals(id, aktoreak.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
