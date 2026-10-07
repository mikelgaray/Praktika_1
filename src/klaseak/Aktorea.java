package PRAKTIKA;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Aktorea implements Comparable<Aktorea> {
    private String id;
    private String izena;
    private Set<Filma> filmak;

    public Aktorea(String id, String izena) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Aktorearen IDa ezin da hutsa izan.");
        }
        this.id = id.trim();
        this.izena = (izena != null) ? izena.trim() : "";
        this.filmak = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        if (izena != null && !izena.trim().isEmpty()) {
            this.izena = izena.trim();
        }
    }

    // O(1) konplexutasuna filmak gehitzeko
    public boolean gehituFilma(Filma filma) {
        if (filma == null) return false;
        return this.filmak.add(filma);
    }

    // O(1) konplexutasuna filmak ezabatzeko
    public boolean ezabatuFilma(Filma filma) {
        if (filma == null) return false;
        return this.filmak.remove(filma);
    }

    // Kopiatutako zerrenda itzultzen du kapsulazioa babesteko
    public List<Filma> getFilmak() {
        return new ArrayList<>(this.filmak);
    }

    @Override
    public int compareTo(Aktorea beste) {
        int izenKonparazioa = this.izena.compareToIgnoreCase(beste.izena);
        if (izenKonparazioa != 0) {
            return izenKonparazioa;
        }
        return this.id.compareTo(beste.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aktorea aktorea = (Aktorea) o;
        return Objects.equals(id, aktorea.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Aktorea{id='" + id + "', izena='" + izena + "'}";
    }
}