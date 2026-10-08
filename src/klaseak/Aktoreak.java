package klaseak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aktoreak {
	private String izena;
	private int id; 
	private ArrayList<Filmak> filmak;
	
	public Aktoreak(String izena, int id, ArrayList<Filmak> filmak) {
		this.izena = izena;
		this.id = id;
		this.filmak = filmak != null ? filmak : new ArrayList<>();
	}

	public String getIzena() {
		return izena;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * AKTORE BATEN FILMAK ITZULI (ez inprimatu)
	 */
	public List<Filmak> getFilmak() {
		return Collections.unmodifiableList(filmak);
	}

	public void setFilmak(ArrayList<Filmak> filmak) {
		this.filmak = filmak;
	}

	public void gehituFilma(Filmak f) {
		if (f != null && !this.filmak.contains(f)) {
			this.filmak.add(f);
		}
	}

	@Override
	public String toString() {
		return id + " - " + izena;
	}
}
