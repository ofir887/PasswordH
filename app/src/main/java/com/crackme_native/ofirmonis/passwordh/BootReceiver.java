package com.crackme_native.ofirmonis.passwordh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Ofir-M on 14/07/2017.
 */

//Broadcast receiver for launching activity when device boot completed
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)){
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.d("Locking Activity","is showing");
            context.startActivity(i);
            
        }

    }
}
