package klaseak;

import java.util.ArrayList;

public class Aktoreak {
	private String izena;
	private int id;
	private ArrayList<Filmak> filmak;
	
	public Aktoreak(String izena, int id, ArrayList<Filmak> filmak) {
		this.izena = izena;
		this.id = id;
		this.filmak = filmak;
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

	public ArrayList<Filmak> getFilmak() {
		return filmak;
	}

	public void setFilmak(ArrayList<Filmak> filmak) {
		this.filmak = filmak;
	}

	@Override
	public String toString() {
		return id + " - " + izena;
	}
	
	
}