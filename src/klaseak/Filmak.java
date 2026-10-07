package klaseak;

import java.util.ArrayList;
import java.util.Date;

public class Filma {

    private String id;        // Wikidata ID / Etiketa
    private String titulua;   // Filmaren izena
    private int urtea;        // Estreinaldi urtea
    private List<Aktoreak> aktoreak;

    public Filma(String id, String titulua, int urtea) {
        this.id = id;
        this.titulua = titulua;
        this.urtea = urtea;
        this.aktoreak = new ArrayList<>();
    }

    // Getters eta Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulua() { return titulua; }
    public void setTitulua(String titulua) { this.titulua = titulua; }

    public int getUrtea() { return urtea; }
    public void setUrtea(int urtea) { this.urtea = urtea; }

    public List<Aktoreak> getAktoreak() { return aktoreak; }

    public void gehitzenAktorea(Aktoreak a) {
        if (a != null && !this.aktoreak.contains(a)) {
            this.aktoreak.add(a);
        }
    }

    public void ezabatuAktorea(Aktoreak a) {
        this.aktoreak.remove(a);
    }

    @Override
    public String toString() {
        return id + " ### " + titulua + " (" + urtea + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filma filma = (Filma) o;
        return Objects.equals(id, filma.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
