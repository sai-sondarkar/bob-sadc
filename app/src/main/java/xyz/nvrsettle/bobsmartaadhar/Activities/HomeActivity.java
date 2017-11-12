package xyz.nvrsettle.bobsmartaadhar.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import xyz.nvrsettle.bobsmartaadhar.R;
import xyz.nvrsettle.bobsmartaadhar.Utility.MarshMallowPermission;

public class HomeActivity extends AppCompatActivity {

    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InitElements();
        initMarshmallowPermission();
    }

    public void InitElements(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");

        TextView tx = (TextView) toolbar.findViewById(R.id.tx) ;
        Typeface custom_font = Typeface.createFromAsset(getAssets(),"fonts/Rubik-Medium.ttf");
        tx.setTypeface(custom_font);
    }

    void initMarshmallowPermission() {

        if ( !marshMallowPermission.checkPermissionForCamera()) {
            marshMallowPermission.requestPermissionForCamera();
        }

    }

    public void openRegisterAccount(View view){
        Intent intent = new Intent(HomeActivity.this,ScanAadharActivity.class);
        intent.putExtra("filter",1);
        startActivity(intent);
    }

    public void depositMoney(View view){
        Intent intent = new Intent(HomeActivity.this,ScanAadharActivity.class);
        intent.putExtra("filter",2);
        startActivity(intent);
    }

    public void withdrawMoney(View view){
        Intent intent = new Intent(HomeActivity.this,ScanAadharActivity.class);
        intent.putExtra("filter",3);
        startActivity(intent);
    }

    public void purchaseMoney(View view){
        Intent intent = new Intent(HomeActivity.this,ScanAadharActivity.class);
        intent.putExtra("filter",3);
        startActivity(intent);
    }

    public void transferMoney(View view){
        Intent intent = new Intent(HomeActivity.this,ScanAadharActivity.class);
        intent.putExtra("filter",4);
        startActivity(intent);
    }

    public void openWallet(View view){
        Intent intent = new Intent(HomeActivity.this,WalletActivity.class);
        startActivity(intent);
    }




}

