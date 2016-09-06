package com.example.svaiyapu.petutrecht.Util;

import android.annotation.TargetApi;
import android.app.SharedElementCallback;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TargetApi(21)
/**
 * Created by svaiyapu on 9/5/16.
 */
public class MainSharedElementCallback extends SharedElementCallback {

    private View sharedView;

    @Override
    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
        removeObsoleteElements(names, sharedElements, mapObsoleteElements(names));
        mapSharedElement(names, sharedElements, sharedView);
    }

    public void setSharedView(View sharedView) {
        this.sharedView = sharedView;
    }

    /**
     * Maps all views that don't start with "android" namespace.
     * Android namespace contains status bars, nav bars
     *
     * @param names All shared element names.
     * @return The obsolete shared element names.
     */
    @NonNull
    private List<String> mapObsoleteElements(List<String> names) {
        List<String> elementsToRemove = new ArrayList<>(names.size());
        for (String name : names) {
            if (name.startsWith("android")) continue;
            elementsToRemove.add(name);
        }
        Log.d("callback", "namesToRemove: " + elementsToRemove.toString());
        return elementsToRemove;
    }

    /**
     * Removes obsolete elements from names and shared elements.
     *
     * @param names Shared element names.
     * @param sharedElements Shared elements.
     * @param elementsToRemove The elements that should be removed.
     */
    private void removeObsoleteElements(List<String> names,
                                        Map<String, View> sharedElements,
                                        List<String> elementsToRemove) {
        if (elementsToRemove.size() > 0) {
            names.removeAll(elementsToRemove);
            for (String elementToRemove : elementsToRemove) {
                sharedElements.remove(elementToRemove);
            }
        }
    }

    /**
     * Puts a shared element to transitions and names.
     *
     * @param names The names for this transition.
     * @param sharedElements The elements for this transition.
     * @param view The view to add.
     */
    private void mapSharedElement(List<String> names, Map<String, View> sharedElements, View view) {
        String transitionName = view.getTransitionName();
        names.add(transitionName);
        sharedElements.put(transitionName, view);
    }

}
