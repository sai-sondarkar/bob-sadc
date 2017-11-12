package xyz.nvrsettle.bobsmartaadhar.Activities;

import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.w3c.dom.Text;

import io.paperdb.Paper;
import xyz.nvrsettle.bobsmartaadhar.FirebaseInfo.FirebaseDatabaseCheck;
import xyz.nvrsettle.bobsmartaadhar.Models.UserModel;
import xyz.nvrsettle.bobsmartaadhar.R;

public class WalletActivity extends AppCompatActivity {

    TextView tv1,tv2,tv3;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        InitElements();
        Paper.init(this);
        uid = Paper.book().read("uid");
        tv1 = (TextView) findViewById(R.id.rupess);
        tv2 = (TextView) findViewById(R.id.name);
        tv3 = (TextView) findViewById(R.id.uid);

        getDataFromFirebase();
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

    public void getDataFromFirebase(){
        FirebaseDatabaseCheck.getDatabase().getReference().child("merchants").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                if(userModel.getAadhar_number().equals(uid)){
                    tv1.setText(userModel.getCurrentAmountInWallet() + "\u20B9");
                    tv2.setText(userModel.getName());
                    tv3.setText(userModel.getAadhar_number());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
