package klaseak;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

public class AktoreZerrenda {
	private static final Pattern MARKAK = Pattern.compile("\\p{M}");
	private static final Pattern HUTSUNEAK = Pattern.compile("\\s+");

	private static HashMap<Integer, Aktoreak> aktoreak; // id (String, adib. "Q12345") -> aktorea
	private final TreeMap<String, ArrayList<Aktoreak>> izenIndizea; // izen normalizatua -> homonimoak

	public AktoreZerrenda() {
		this.aktoreak = new HashMap<>();
		this.izenIndizea = new TreeMap<>();
	}

	public static String gakuaSortu(String izena) {
		String s = Normalizer.normalize(izena.trim(), Normalizer.Form.NFD);
		s = MARKAK.matcher(s).replaceAll("");
		s = HUTSUNEAK.matcher(s).replaceAll(" ");
		return s.toLowerCase(Locale.ROOT);
	}

	/**
	 * AKTORE BAT BILATU: ID bidez
	 */
	public Aktoreak getAktorea(String id) {
		return aktoreak.get(id);
	}

	/**
	 * AKTORE BAT BILATU: Izen eta abizenez (zerrenda itzultzen du homonimoak egon baitaitezke)
	 */
	public List<Aktoreak> bilatuIzenez(String izena) {
		ArrayList<Aktoreak> berdinak = izenIndizea.get(gakuaSortu(izena));
		return berdinak == null ? Collections.emptyList() : Collections.unmodifiableList(berdinak);
	}

	public boolean gehituAktorea(Aktoreak a) {
		if (a == null)
			throw new IllegalArgumentException("Aktorea ezin da null izan");
		if (aktoreak.containsKey(a.getId()))
			return false;
		aktoreak.put(a.getId(), a);
		izenIndizea.computeIfAbsent(gakuaSortu(a.getIzena()), k -> new ArrayList<>()).add(a);
		return true;
	}

	/**
	 * AKTORE BAT EZABATU: ID-aren bidez ezabatzen du eta bere erreferentziak garbitzen ditu.
	 */
	public Aktoreak ezabatuAktorea(String id) {
		Aktoreak a = aktoreak.remove(id);
		if (a == null)
			return null;
		
		// Izen indizetik kendu
		String gakoa = gakuaSortu(a.getIzena());
		ArrayList<Aktoreak> berdinak = izenIndizea.get(gakoa);
		if (berdinak != null) {
			berdinak.remove(a);
			if (berdinak.isEmpty())
				izenIndizea.remove(gakoa);
		}

		// Aktorea dagoen film guztietatik bere erreferentzia ezabatu bikoiztasunik ez geratzeko
		if (a.getFilmak() != null) {
			for (Filmak f : a.getFilmak()) {
				if (f.getAktoreak() != null) {
					f.getAktoreak().remove(a);
				}
			}
		}

		return a;
	}

	public ArrayList<Aktoreak> ordenatutakoZerrenda() {
		ArrayList<Aktoreak> emaitza = new ArrayList<>(aktoreak.size());
		for (ArrayList<Aktoreak> taldea : izenIndizea.values())
			emaitza.addAll(taldea);
		return emaitza;
	}

	public int tamaina() {
		return aktoreak.size();
	}

	public Collection<Aktoreak> getAktoreak() {
		return Collections.unmodifiableCollection(aktoreak.values());
	}
}
