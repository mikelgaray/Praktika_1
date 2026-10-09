package klaseak;

import java.util.ArrayList;
import java.util.Objects;

public class Filmak {
	private String titulua;
	private String id;
	private int urtea;
	private ArrayList<Aktoreak> aktoreak;
	
	public Filmak(String titulua, String id, int urtea, ArrayList<Aktoreak> aktoreak) {
		this.titulua = titulua;
		this.id = id;
		this.urtea = urtea;
		this.aktoreak = aktoreak;
	}

	public String getTitulua() {
		return titulua;
	}

	public void setTitulua(String titulua) {
		this.titulua = titulua;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getUrtea() {
		return urtea;
	}

	public void setUrtea(int urtea) {
		if (urtea <= 0) throw new IllegalArgumentException("Urte baliogabea: " + urtea);
		this.urtea = urtea;
	}

	public ArrayList<Aktoreak> getAktoreak() {
		return aktoreak;
	}

	public void setAktoreak(ArrayList<Aktoreak> aktoreak) {
		this.aktoreak = aktoreak;
	}

	/** Bi Filmak berdinak dira ID bera badute (Wikidatako identifikatzailea). */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Filmak beste = (Filmak) o;
		return Objects.equals(id, beste.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return id + " - " + titulua + " (" + urtea + ")";
	}
}