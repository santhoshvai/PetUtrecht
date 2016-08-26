package com.example.svaiyapu.petutrecht.Util;

import android.graphics.Color;

/**
 * Created by svaiyapu on 8/26/16.
 */
public class PetUtil {
    public static boolean isDog(String type) {
        return type.startsWith("D");
    }
    public static boolean isCat(String type) {
        return type.startsWith("C");
    }

    /**
     * decide font color in white or black depending on background color
     * Algorithm: http://stackoverflow.com/a/39031835/3394023
     * @param colorIntValue background color
     * @return font color
     */
    public static int getContrastColor(int colorIntValue) {
        int red = Color.red(colorIntValue);
        int green = Color.green(colorIntValue);
        int blue = Color.blue(colorIntValue);
        double lum = (((0.299 * red) + ((0.587 * green) + (0.114 * blue))));
        return lum > 186 ? 0xFF000000 : 0xFFFFFFFF;
    }
}
