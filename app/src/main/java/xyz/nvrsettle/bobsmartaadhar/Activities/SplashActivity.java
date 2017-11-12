package xyz.nvrsettle.bobsmartaadhar.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import xyz.nvrsettle.bobsmartaadhar.R;

public class SplashActivity extends AppCompatActivity {

    protected boolean _active = true;
    protected int _splashTime = 3500; // time to display the splash screen in ms
    public boolean is_first = false;


    private Typeface gujuTypeFace,hindTypeFace;
    public TextView  textViewTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textViewTitle = (TextView) findViewById(R.id.textname);

        gujuTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Lohit-Gujarati.ttf");
        hindTypeFace = Typeface.createFromAsset(getAssets(), "fonts/DroidHindi.ttf");


//        FirebaseMessaging.getInstance().subscribeToTopic("common");

//        screendesign();
//
//        ScreenDesign();

        splashThread();

        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                textViewTitle.setTypeface(hindTypeFace);
                textViewTitle.setText("स्मार्ट आधार डेबिट कार्ड");
            }
        }, 1000);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                textViewTitle.setTypeface(gujuTypeFace);
                textViewTitle.setText("સ્માર્ટ આધાર ડેબિટ કાર્ડ");
            }
        }, 2000);


        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                textViewTitle.setTypeface(hindTypeFace);
                textViewTitle.setText("स्ਸਮਾਰਟ ਆਧਾਰ ਡੈਬਿਟ ਕਾਰਡ");

            }
        }, 3000);

    }

    public void screendesign(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();

            boolean shouldChangeStatusBarTintToDark = false;

            if (shouldChangeStatusBarTintToDark) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                // We want to change tint color to white again.
                // You can also record the flags in advance so that you can turn UI back completely if
                // you have set other flags before, such as translucent or full screen.
                decor.setSystemUiVisibility(0);
            }
        }
    }

    public void splashThread(){
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                            if(waited>_splashTime-200)
                            {

                            }
                        }
                    }
                } catch (Exception e) {

                } finally {                    {

                    startActivity(new Intent(SplashActivity.this,
                            LoginActivity.class));}

                    finish();
                }
            };
        };

        splashTread.start();

    }

    public void ScreenDesign(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            boolean shouldChangeStatusBarTintToDark = true;

            if (shouldChangeStatusBarTintToDark) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                // We want to change tint color to white again.
                // You can also record the flags in advance so that you can turn UI back completely if
                // you have set other flags before, such as translucent or full screen.
                decor.setSystemUiVisibility(0);

            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
            }
        }

    }

}

