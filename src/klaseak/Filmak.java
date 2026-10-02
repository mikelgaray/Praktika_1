package klaseak;

import java.util.ArrayList;
import java.util.Date;

public class Filmak {
	private String titulua;
	private String id;
	private Date urtea;
	private ArrayList<Aktoreak> aktoreak;
	
	public Filmak(String titulua, String id, Date urtea, ArrayList<Aktoreak> aktoreak) {
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

	public Date getUrtea() {
		return urtea;
	}

	public void setUrtea(Date urtea) {
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
		return "Filmak [titulua=" + titulua + ", id=" + id + ", urtea=" + urtea + ", aktoreak=" + aktoreak + "]";
	}
	
}
