package com.mycf.edittracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by John on 9/7/2017.
 */

public class CrewUtils {

    /**
     * Utility class for moving to a new activity and sending data
     *
     * @param thisContext The context this call is originating from
     * @param nextClass The class we will end upon
     * @param bundleItems All data stored in a "key", "value" set
     */
    static public void sendIntent(Context thisContext, Class nextClass, HashMap<String, String> bundleItems) {
        Intent intent = new Intent(thisContext, nextClass);
        Bundle b = new Bundle();

        for (Map.Entry<String, String> entry : bundleItems.entrySet()) {
            b.putString(entry.getKey(), entry.getValue()); //Your id
        }
        intent.putExtras(b); //Put your id to your next Intent
        thisContext.startActivity(intent);
    }
}
