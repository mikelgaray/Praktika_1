package klaseak;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

public class AktoreZerrenda {
	private static final Pattern MARKAK = Pattern.compile("\\p{M}");
	private static final Pattern HUTSUNEAK = Pattern.compile("\\s+");

	private final HashMap<String, Aktoreak> aktoreak; // id -> aktorea
	private final TreeMap<String, ArrayList<Aktoreak>> izenIndizea; // izen normalizatua -> homonimoak
	private int maxId = 0; // ID handiena ("Q123" -> 123), ID berriak O(1)-en sortzeko

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

	public Aktoreak getAktorea(String id) {
		return aktoreak.get(id);
	}

	public boolean gehituAktorea(Aktoreak a) {
		if (a == null)
			throw new IllegalArgumentException("Aktorea ezin da null izan");
		if (aktoreak.containsKey(a.getId()))
			return false;
		aktoreak.put(a.getId(), a);
		maxId = Math.max(maxId, Integer.parseInt(a.getId().substring(1)));
		izenIndizea.computeIfAbsent(gakuaSortu(a.getIzena()), k -> new ArrayList<>()).add(a);
		return true;
	}

	public Aktoreak ezabatuAktorea(String id) {
		Aktoreak a = aktoreak.remove(id);
		if (a == null)
			return null;
		String gakoa = gakuaSortu(a.getIzena());
		ArrayList<Aktoreak> berdinak = izenIndizea.get(gakoa);
		if (berdinak != null) {
			berdinak.remove(a);
			if (berdinak.isEmpty())
				izenIndizea.remove(gakoa);
		}
		for (Filmak f : a.getFilmak())
			f.getAktoreak().remove(a); // filmetan erreferentziarik ez geratzeko
		return a;
	}

	public List<Aktoreak> bilatuIzenez(String izena) {
		ArrayList<Aktoreak> berdinak = izenIndizea.get(gakuaSortu(izena));
		return berdinak == null ? Collections.emptyList() : Collections.unmodifiableList(berdinak);
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

	public Aktoreak gordeAktoreBerria(String izena) {
		Aktoreak aktoreBerria = new Aktoreak(izena, "Q" + (maxId + 1), new ArrayList<>());
		gehituAktorea(aktoreBerria); // HashMap + izenIndizea (TreeMap) biak eguneratzeko
		return aktoreBerria;
	}

	private static int konparatu(Aktoreak a1, Aktoreak a2) {
		return a1.getGakoa().compareTo(a2.getGakoa());
	}

	// MERGESORT Metodoa
	public ArrayList<Aktoreak> mergeSortAktoreak() {
		ArrayList<Aktoreak> aktoreZerrenda = new ArrayList<>(aktoreak.values());
		mergesort(aktoreZerrenda, 0, aktoreZerrenda.size() - 1);
		return aktoreZerrenda;
	}

	public static void mergesort(ArrayList<Aktoreak> zerrenda, int hasiera, int bukaera) {
		if (hasiera < bukaera) {
			mergesort(zerrenda, hasiera, (hasiera + bukaera) / 2);
			mergesort(zerrenda, ((hasiera + bukaera) / 2) + 1, bukaera);
			bateratze(zerrenda, hasiera, (hasiera + bukaera) / 2, bukaera);
		}
	}

	public static void bateratze(ArrayList<Aktoreak> zerrenda, int hasiera, int erdikoa, int bukaera) {
		ArrayList<Aktoreak> aldiBaterakoZerrenda = new ArrayList<>();
		int ezker = hasiera;
		int eskuin = erdikoa + 1;

		while (ezker <= erdikoa && eskuin <= bukaera) {
			if (konparatu(zerrenda.get(ezker), zerrenda.get(eskuin)) <= 0) {
				aldiBaterakoZerrenda.add(zerrenda.get(ezker));
				ezker++;
			} else {
				aldiBaterakoZerrenda.add(zerrenda.get(eskuin));
				eskuin++;
			}
		}
		while (ezker <= erdikoa) {
			aldiBaterakoZerrenda.add(zerrenda.get(ezker));
			ezker++;
		}

		while (eskuin <= bukaera) {
			aldiBaterakoZerrenda.add(zerrenda.get(eskuin));
			eskuin++;
		}

		for (int j = hasiera; j <= bukaera; j++) {
			zerrenda.set(j, aldiBaterakoZerrenda.get(j - hasiera));
		}
	}

	// QUICKSORT Metodoa
	public ArrayList<Aktoreak> quickSortAktoreak() {
		ArrayList<Aktoreak> aktoreZerrenda = new ArrayList<>(aktoreak.values());
		quickSort(aktoreZerrenda, 0, aktoreZerrenda.size() - 1);
		return aktoreZerrenda;
	}

	public static void quickSort(ArrayList<Aktoreak> zerrenda, int hasiera, int bukaera) {
		if (bukaera - hasiera > 0) {
			int zatiketaIndizea = zatiketa(zerrenda, hasiera, bukaera);
			quickSort(zerrenda, hasiera, zatiketaIndizea - 1);
			quickSort(zerrenda, zatiketaIndizea + 1, bukaera);
		}
	}

	public static int zatiketa(ArrayList<Aktoreak> zerrenda, int hasiera, int bukaera) {
		// Erdiko elementua pibote gisa (datu ordenatuekin O(n^2) saihesteko), hasierara eramanda
		int erdia = (hasiera + bukaera) / 2;
		Aktoreak pibotea = zerrenda.get(erdia);
		zerrenda.set(erdia, zerrenda.get(hasiera));
		zerrenda.set(hasiera, pibotea);
		int ezker = hasiera;
		int eskuin = bukaera;

		while (ezker < eskuin) {
			while (konparatu(zerrenda.get(ezker), pibotea) <= 0 && ezker < eskuin) {
				ezker++;
			}
			while (konparatu(zerrenda.get(eskuin), pibotea) > 0) {
				eskuin--;
			}
			if (ezker < eskuin) {
				Aktoreak tenporala = zerrenda.get(ezker);
				zerrenda.set(ezker, zerrenda.get(eskuin));
				zerrenda.set(eskuin, tenporala);
			}
		}
		zerrenda.set(hasiera, zerrenda.get(eskuin));
		zerrenda.set(eskuin, pibotea);
		return eskuin;
	}

}