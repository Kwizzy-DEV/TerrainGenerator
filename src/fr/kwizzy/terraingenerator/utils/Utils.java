package fr.kwizzy.terraingenerator.utils;

import java.util.Random;

/**
 * Par Alexis le 23/07/2016.
 */

public class Utils
{

    public static double roundDouble(double d) {
        return ((int) (d * 100)) / 100.;
    }

    public static double roundDouble(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private static int roundUP(double d) {
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - (double) i;
        if (result == 0.0) {
            return (int) d;
        } else {
            return (int) d < 0 ? -(i + 1) : i + 1;
        }
    }

    public static <T> boolean arrayContains(final T[] array, final T v) {
        for (final T e : array)
            if (e == v || v.equals(e))
                return true;
        return false;
    }

    public static int randomInteger(int low, int high) {
        Random r = new Random();
        return r.nextInt((high +1 )- low) + low;
    }

    /**
     *
     * @param d 0-100
     * @return true or false
     */
    public static boolean chanceOf(int d){
        int fD = randomInteger(0, 100);
        return (fD <= d);
    }

    public static Double randDouble(double start, double end) {

        double random = new Random().nextDouble();
        return start + (random * (end - start));
    }
}
