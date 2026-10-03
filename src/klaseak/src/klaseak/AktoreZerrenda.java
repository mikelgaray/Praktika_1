package klaseak;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

public class AktoreZerrenda {
	private static final Pattern MARKAK = Pattern.compile("\\p{M}");
    private static final Pattern HUTSUNEAK = Pattern.compile("\\s+");

    private final HashMap<Integer, Aktoreak> aktoreak;               // id -> aktorea
    private final TreeMap<String, ArrayList<Aktoreak>> izenIndizea;  // izen normalizatua -> homonimoak

    public AktoreZerrenda() {
        this.aktoreak = new HashMap<>();
        this.izenIndizea = new TreeMap<>();
    }

    /** "Adriána  Barraza" -> "adriana barraza" (bilaketa eta ordenaketarako gakoa) */
    public static String gakuaSortu(String izena) {
        String s = Normalizer.normalize(izena.trim(), Normalizer.Form.NFD);
        s = MARKAK.matcher(s).replaceAll("");
        s = HUTSUNEAK.matcher(s).replaceAll(" ");
        return s.toLowerCase(Locale.ROOT);
    }

    public Aktoreak getAktorea(int id) { return aktoreak.get(id); }

    /** @return false aktorea (ID berdinarekin) jada existitzen bada */
    public boolean gehituAktorea(Aktoreak a) {
        if (a == null) throw new IllegalArgumentException("Aktorea ezin da null izan");
        if (aktoreak.containsKey(a.getId())) return false;
        aktoreak.put(a.getId(), a);
        izenIndizea.computeIfAbsent(gakuaSortu(a.getIzena()), k -> new ArrayList<>()).add(a);
        return true;
    }

    /** @return ezabatutako aktorea, edo null ez bazegoen */
    public Aktoreak ezabatuAktorea(int id) {
        Aktoreak a = aktoreak.remove(id);
        if (a == null) return null;
        String gakoa = gakuaSortu(a.getIzena());
        ArrayList<Aktoreak> berdinak = izenIndizea.get(gakoa);
        if (berdinak != null) {
            berdinak.remove(a);
            if (berdinak.isEmpty()) izenIndizea.remove(gakoa);
        }
        return a;
    }

    /** Izen-abizen berdineko aktore guztiak (homonimoak barne) */
    public List<Aktoreak> bilatuIzenez(String izena) {
        ArrayList<Aktoreak> berdinak = izenIndizea.get(gakuaSortu(izena));
        return berdinak == null ? Collections.emptyList() : Collections.unmodifiableList(berdinak);
    }

    /** Kopia berri bat, izenaren arabera ordenatuta. Jatorrizkoa ez da aldatzen. */
    public ArrayList<Aktoreak> ordenatutakoZerrenda() {
        ArrayList<Aktoreak> emaitza = new ArrayList<>(aktoreak.size());
        for (ArrayList<Aktoreak> taldea : izenIndizea.values()) emaitza.addAll(taldea);
        return emaitza;
    }

    public int tamaina() { return aktoreak.size(); }

    public Collection<Aktoreak> getAktoreak() {
        return Collections.unmodifiableCollection(aktoreak.values());
    }
}
