package bewis09.util;

public class MathUtil {
    public static double inRangeThen(double i, double range, double distance) {
        if(i>range-distance&&i<range+distance) {
            return range;
        }
        return i;
    }

    public static String withAfterComma(double number, double afterComma) {
        double v = Math.round(number * Math.pow(10, afterComma)) / Math.pow(10, afterComma);
        return zeroAfter(v,afterComma+1+String.valueOf((int) v).length());
    }
    
    public static String zeroAfter(double number, double count) {
        StringBuilder str = new StringBuilder(String.valueOf(number));
        while (str.length()<count) {
            str.append("0");
        }
        return str.toString();
    }

    public static String zeroBefore(int number, int count) {
        StringBuilder str = new StringBuilder(String.valueOf(number));
        while (str.length()<count) {
            str.insert(0,"0");
        }
        return str.toString();
    }
}
