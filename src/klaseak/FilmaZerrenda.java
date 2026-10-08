package klaseak;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

public class FilmaZerrenda {
    private static HashMap<Integer, Filmak> filmak;

    public FilmaZerrenda() {
        this.filmak = new HashMap<>();
    }

    public Filmak getFilma(int id) {
        return filmak.get(id);
    }

    public void setFilma(Filmak a) {
        if (a == null) throw new IllegalArgumentException("Pelikula ezin da null izan");
        filmak.put(a.getId(), a);
    }
    
    public int tamaina() { return filmak.size(); }

    public Collection<Filmak> getPelikulak() {
        return filmak.values();
    }

    public boolean lotuAktoreaDagoenFilmari(Aktoreak aktorea, int filmaID) {
        Filmak dagoenFilma = filmak.get(filmaID);
        if (dagoenFilma != null) {
            aktorea.getFilmak().add(dagoenFilma);
            dagoenFilma.getAktoreak().add(aktorea);
            return true;
        }
        return false;
    }
    public boolean lotuAktoreaDagoenFilmari(Aktoreak aktorea, String izenburua) {
        Filmak dagoenFilma = null;
        for(Filmak filma : filmak.values()) {
			if(filma.getTitulua().equalsIgnoreCase(izenburua)) {
				dagoenFilma = filma;
		        aktorea.getFilmak().add(dagoenFilma);
		        dagoenFilma.getAktoreak().add(aktorea);
		        return true;
			}
		}
        return false;
    }

    public Filmak sortuEtaLotuFilmaBerria(Aktoreak aktorea, String izenburua, int urtea) {
        int maxNumero = 0;
      //int filmIdBerria = filmak.size() + 1; Para pruebas

        for (Filmak filma : filmak.values()) {
            int numero = Integer.parseInt(filma.getId().substring(1)); 
            if (numero > maxNumero) {
                maxNumero = numero;
            }
        }
        String filmIdBerria = "Q" + (maxNumero + 1);
        Filmak filmBerria = new Filmak(izenburua, filmIdBerria, urtea, new ArrayList<>());
        filmak.put(filmBerria.getId(), filmBerria);
        
        aktorea.getFilmak().add(filmBerria);
        filmBerria.getAktoreak().add(aktorea);
        
        return filmBerria;
    }
    
}