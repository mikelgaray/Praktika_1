package control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import klaseak.Aktoreak;
import klaseak.AktoreZerrenda;
import klaseak.Filmak;
import klaseak.FilmaZerrenda;

public class Kontrolatzailea {

	private AktoreZerrenda aktoreZerrendaGlobala;
	private FilmaZerrenda pelikulaZerrendaGlobala;
	
	public Kontrolatzailea() {
	        zerrendakBaieztatu();
	    }
	 
	// ==================================================================
    // Laguntzaileak (hainbatetan berrerabiliak)
    // ==================================================================

    /** Zerrenda globalak existitzen direla bermatzen du; falta denik sortzen du. */
    private void zerrendakBaieztatu() {
        if (aktoreZerrendaGlobala == null) {
            aktoreZerrendaGlobala = new AktoreZerrenda();
        }
        if (pelikulaZerrendaGlobala == null) {
            pelikulaZerrendaGlobala = new FilmaZerrenda();
        }
    }
    
    // ==================================================================
    // 5. Film bateko aktoreak (itzuli, ez inprimatu)
    // ==================================================================

    /** Irakurtzeko soilik den ikuspegia; pelikula ez badago, zerrenda hutsa. */
    public List<Aktoreak> pelikularenAktoreak(int filmaId) {
        zerrendakBaieztatu();
        Filmak f = pelikulaZerrendaGlobala.getFilma(filmaId);
        if (f == null) return Collections.emptyList();
        return Collections.unmodifiableList(f.getAktoreak());
    }
	    

}
