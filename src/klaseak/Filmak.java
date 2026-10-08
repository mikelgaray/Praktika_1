package klaseak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Filmak {
	private String titulua;
	private int id; 
	private int urtea;
	private ArrayList<Aktoreak> aktoreak;
	
	public Filmak(String titulua, int id, int urtea, ArrayList<Aktoreak> aktoreak) {
		this.titulua = titulua;
		this.id = id;
		this.urtea = urtea;
		this.aktoreak = aktoreak != null ? aktoreak : new ArrayList<>();
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

	/**
	 * FILM BATEN ESTREINALDI URTEA ALDATU
	 */
	public void setUrtea(int urtea) {
		if (urtea <= 0) throw new IllegalArgumentException("Urte baliogabea: " + urtea);
		this.urtea = urtea;
	}

	/**
	 * FILM BATEKO AKTOREAK ITZULI (ez inprimatu)
	 */
	public ArrayList<Aktoreak> getAktoreak() {
		return aktoreak;
	}

	public void setAktoreak(ArrayList<Aktoreak> aktoreak) {
		this.aktoreak = aktoreak;
	}

	public void gehituAktorea(Aktoreak a) {
		if (a != null && !this.aktoreak.contains(a)) {
			this.aktoreak.add(a);
		}
	}

	@Override
	public String toString() {
		return id + " - " + titulua + " (" + urtea + ")";
	}
}
