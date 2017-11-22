package ana;

public class Utils {
    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    static int getInt(String textValue, int defaultValue) {
        if (isEmpty(textValue)) {
            return defaultValue;
        }
        return Integer.parseInt(textValue);
    }

    public static double getDouble(String textValue, double defaultValue) {
        if (isEmpty(textValue)) {
            return defaultValue;
        }
        return Double.parseDouble(textValue);
    }
}
