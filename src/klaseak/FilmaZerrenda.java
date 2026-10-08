package klaseak;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class FilmaZerrenda {
    private static HashMap<String, Filmak> filmak; // IDa String gisa kudeatzeko

    public FilmaZerrenda() {
        FilmaZerrenda.filmak = new HashMap<>();
    }

    /**
     * FILMA BAT BILATU ID BIDEZ
     */
    public Filmak getFilma(String id) {
        return filmak.get(id);
    }

    public void setFilma(Filmak a) {
        if (a == null) throw new IllegalArgumentException("Pelikula ezin da null izan");
        filmak.put(a.getId(), a);
    }
    
    public int tamaina() { 
        return filmak.size(); 
    }

    public Collection<Filmak> getPelikulak() {
        return Collections.unmodifiableCollection(filmak.values());
    }

    /**
     * DAGOEN FILM BATEN ETA AKTORE BATEN ARTEKO LOTURA EGIN
     */
    public boolean lotuAktoreaDagoenFilmari(Aktoreak aktorea, String filmaID) {
        Filmak dagoenFilma = filmak.get(filmaID);
        if (dagoenFilma != null && aktorea != null) {
            if (!aktorea.getFilmak().contains(dagoenFilma)) {
                aktorea.gehituFilma(dagoenFilma);
            }
            if (!dagoenFilma.getAktoreak().contains(aktorea)) {
                dagoenFilma.gehituAktorea(aktorea);
            }
            return true;
        }
        return false;
    }

    /**
     * FILMA BERRIA SORTU ETA AKTOREARI LOTU
     */
    public Filmak sortuEtaLotuFilmaBerria(Aktoreak aktorea, String izenburua, int urtea) {
        int maxNumero = 0;

        for (Filmak filma : filmak.values()) {
            if (filma.getId() != null && filma.getId().startsWith("Q")) {
                try {
                    int numero = Integer.parseInt(filma.getId().substring(1)); 
                    if (numero > maxNumero) {
                        maxNumero = numero;
                    }
                } catch (NumberFormatException ignored) {
                    // ID ez-zenbakizkoak alde batera utzi
                }
            }
        }
        
        String filmIdBerria = "Q" + (maxNumero + 1);
        Filmak filmBerria = new Filmak(izenburua, filmIdBerria, urtea, new ArrayList<>());
        filmak.put(filmBerria.getId(), filmBerria);
        
        if (aktorea != null) {
            aktorea.gehituFilma(filmBerria);
            filmBerria.gehituAktorea(aktorea);
        }
        
        return filmBerria;
    }
}
