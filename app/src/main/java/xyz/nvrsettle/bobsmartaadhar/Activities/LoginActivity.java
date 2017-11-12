package xyz.nvrsettle.bobsmartaadhar.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;
import xyz.nvrsettle.bobsmartaadhar.R;

public class LoginActivity extends AppCompatActivity {

    MaterialDialog dialog;
    String TAG = LoginActivity.class.getSimpleName();
    TextView name_tx;
    TextView singup_btn;
    EditText email_et;
    Button loginButton;
    private FirebaseAuth mAuth; // for the auth state
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance(); //  getting the instance in the context
        Paper.init(this);
        InitUiElements();

    }

    public void InitUiElements() {

        TextView tv = (TextView) findViewById(R.id.name);
        Typeface tt1 = Typeface.createFromAsset(getAssets(), "fonts/Rubik-Medium.ttf");
        tv.setTypeface(tt1);

        name_tx = (TextView) findViewById(R.id.name);
        singup_btn = (TextView) findViewById(R.id.signup_btn);
        loginButton = (Button) findViewById(R.id.login_btn);
        email_et = (EditText) findViewById(R.id.email_et);


        Typeface tt2 = Typeface.createFromAsset(getAssets(), "fonts/Rubik-Light.ttf");
        singup_btn.setTypeface(tt2);
        email_et.setTypeface(tt2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dialog = ProgressDialog.show(LoginActivity.this, "",
//                        "Sending OTP SMS to your Registered Mobile Number", true);

                getDialog();
                final Handler handler1 = new Handler();

                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.getInputEditText().setText("123456");
//                        startHomeActivity();
                    }
                }, 3000);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startHomeActivity();
                    }
                }, 4000);
            }
        });
        email_et.addTextChangedListener(new TextWatcher() {
            int len = 0;
            @Override
            public void afterTextChanged(Editable s) {
                String str = email_et.getText().toString();
                if ((str.length() == 4 || str.length() == 9 || str.length() == 14) && len < str.length()) {//len check for backspace
                    email_et.append(" ");
                }
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                String str = email_et.getText().toString();
                len = str.length();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }


    public void startHomeActivity() {

        Toast.makeText(LoginActivity.this, "Authentication successful", Toast.LENGTH_SHORT).show();
        Paper.book().write("uid",email_et.getText().toString().replaceAll("\\s+",""));
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }


    public void getDialog(){
        MaterialDialog.Builder builder =  new MaterialDialog.Builder(this)
                .title("Enter OTP")
                .content("Please enter the OTP or Wait to Auto Retrieve ")
                .positiveText("")
                .inputType(InputType.TYPE_CLASS_TEXT )
                .input("******", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                    }
                });

        dialog = builder.build();
        dialog.show();

    }

}
