package com.example.svaiyapu.petutrecht.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.widget.Toast;

import com.example.svaiyapu.petutrecht.data.Model.Pet;

import java.util.ArrayList;
import java.util.List;

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
    public static boolean isAll(String type) {
        return type.startsWith("A");
    }

    public static boolean petTypeEqual(String type1, String type2){
        if(isDog(type1)) {
            return isDog(type2);
        }
        else if(isCat(type1)) {
            return isCat(type2);
        } else {
            return isAll(type2);
        }
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

    public static void composeEmail(Context context, String address, String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + address)); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(intent, "Send mail..."));
        } else {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static List<Pet> filterDogs(List<Pet> pets) {
        List<Pet> dogs = new ArrayList<>();
        for(Pet p: pets) {
            if(PetUtil.isDog(p.getType())) {
                dogs.add(p);
            }
        }
        return dogs;
    }

    public static List<Pet> filterCats(List<Pet> pets) {
        List<Pet> dogs = new ArrayList<>();
        for(Pet p: pets) {
            if(PetUtil.isCat(p.getType())) {
                dogs.add(p);
            }
        }
        return dogs;
    }
}
