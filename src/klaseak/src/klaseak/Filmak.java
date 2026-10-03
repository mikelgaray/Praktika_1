package klaseak;

import java.util.ArrayList;

public class Filmak {
	private String titulua;
	private int id;
	private int urtea;
	private ArrayList<Aktoreak> aktoreak;
	
	public Filmak(String titulua, int id, int urtea, ArrayList<Aktoreak> aktoreak) {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	@Override
	public String toString() {
		return id + " - " + titulua + " (" + urtea + ")";
	}
	
}