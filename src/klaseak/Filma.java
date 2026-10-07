package PRAKTIKA;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Filma {
    private String id;
    private String izenburua;
    private int urtea;
    private Set<Aktorea> aktoreak;

    public Filma(String id, String izenburua, int urtea) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Filmaren IDa ezin da hutsa izan.");
        }
        this.id = id.trim();
        this.izenburua = (izenburua != null) ? izenburua.trim() : "";
        this.urtea = urtea;
        this.aktoreak = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getIzenburua() {
        return izenburua;
    }

    public int getUrtea() {
        return urtea;
    }

    public void setUrtea(int urtea) {
        this.urtea = urtea;
    }

    // O(1) konplexutasuna
    public boolean gehituAktorea(Aktorea aktorea) {
        if (aktorea == null) return false;
        return this.aktoreak.add(aktorea);
    }

    // O(1) konplexutasuna
    public boolean ezabatuAktorea(Aktorea aktorea) {
        if (aktorea == null) return false;
        return this.aktoreak.remove(aktorea);
    }

    // Kopiatutako zerrenda itzultzen du
    public List<Aktorea> getAktoreak() {
        return new ArrayList<>(this.aktoreak);
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

    @Override
    public String toString() {
        return "Filma{id='" + id + "', izenburua='" + izenburua + "', urtea=" + urtea + "}";
    }
}
