import java.time.Year;        // gets the current year e.g 2026
import java.util.HashMap;     // stores a separate counter for each branch per year

// generates account numbers in format BRANCHCODE-YYYY-xxxxxx
// e.g KLA-2026-000001 for Kampala, GUL-2026-000002 for Gulu
// each branch has its own counter that resets every new year

public class AccountNumberGenerator {

    // key = branch code + year e.g "KLA2026", value = how many accounts opened so far
    private static HashMap<String, Integer> counters = new HashMap<>();

    // generates the next account number for the given branch
    public static String generate(String branch) {

        // convert full branch name to its 3-letter code
        String code = switch (branch) {
            case "Kampala" -> "KLA";
            case "Gulu"    -> "GUL";
            case "Mbarara" -> "MBR";
            case "Jinja"   -> "JIN";
            default        -> "MBL"; // Mbale
        };

        // key combines code + year so Kampala 2026 and Kampala 2027 count separately
        String key = code + Year.now().getValue();

        // get current count for this key, add 1, save it back
        int n = counters.getOrDefault(key, 0) + 1;
        counters.put(key, n);

        // %06d pads with leading zeros so 1 becomes 000001
        return String.format("%s-%d-%06d", code, Year.now().getValue(), n);
    }
}
