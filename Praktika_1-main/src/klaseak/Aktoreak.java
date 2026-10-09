package klaseak;

import java.util.ArrayList;
import java.util.Objects;

public class Aktoreak {
	private String izena;
	private String id;
	private ArrayList<Filmak> filmak;
	private String gakoa; // izen normalizatua, ordenatzeko

	public Aktoreak(String izena, String id, ArrayList<Filmak> filmak) {
		this.izena = izena;
		this.id = id;
		this.filmak = filmak;
	}

	public String getIzena() {
		return izena;
	}

	public void setIzena(String izena) {
		this.izena = izena;
		this.gakoa = null;
	}

	public String getGakoa() {
		if (gakoa == null) gakoa = AktoreZerrenda.gakuaSortu(izena);
		return gakoa;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<Filmak> getFilmak() {
		return filmak;
	}

	public void setFilmak(ArrayList<Filmak> filmak) {
		this.filmak = filmak;
	}

	/** Bi Aktoreak berdinak dira ID bera badute (Wikidatako identifikatzailea). */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Aktoreak beste = (Aktoreak) o;
		return Objects.equals(id, beste.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return id + " - " + izena;
	}
}