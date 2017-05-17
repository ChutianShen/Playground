package com.example.kevin_sct.beastchat.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by kevin_sct on 5/16/17.
 */

public class AlertUtils {
    /**
     * @param context
     * @param mess
     */
    public static void toastMess(Context context, String mess) {
        Toast.makeText(context, mess, Toast.LENGTH_SHORT).show();
    }
}
