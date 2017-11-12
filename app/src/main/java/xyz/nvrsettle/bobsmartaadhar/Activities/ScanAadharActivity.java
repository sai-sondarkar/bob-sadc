package xyz.nvrsettle.bobsmartaadhar.Activities;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import io.paperdb.Paper;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import xyz.nvrsettle.bobsmartaadhar.FirebaseInfo.FirebaseDatabaseCheck;
import xyz.nvrsettle.bobsmartaadhar.Models.TranstModel;
import xyz.nvrsettle.bobsmartaadhar.Models.UserModel;
import xyz.nvrsettle.bobsmartaadhar.R;
import xyz.nvrsettle.bobsmartaadhar.Utility.DataAttributes;
import xyz.nvrsettle.bobsmartaadhar.Utility.XMLPullParserHandler;

public class ScanAadharActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String TAG = ScanAadharActivity.class.getName();
    private ZXingScannerView mScannerView;
    String rawData;
    String uid,name,address,dob,gender;
    Context context;
    int flag;
    EditText editText,edittext1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        flag = getIntent().getIntExtra("filter",0);
        context = this;
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void handleResult(Result rawResult) {

        // Do something with the result here
        Log.v(TAG, rawResult.getText()); // Prints scan results
        rawData = rawResult.getText();
        decideActivity();


        // If you would like to resume scanning, call this method below:
//        mScannerView.resumeCameraPreview(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void decideActivity(){

        getDataFromRawData();

        switch (flag){
            case 1 : showAddNewUser();
                break;
            case 2 : showDepositMoney();
                break;
            case 3 : showWithdrawMoney();
                break;
            case 4 : showTransferMoney();
                break;
            default:

                Toast.makeText(ScanAadharActivity.this,"Error!",Toast.LENGTH_SHORT).show();
        }

    }


    public void showTransferMoney(){

        MaterialDialog dialog;
        final View view;
        boolean wrapInScrollView = true;
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Withdraw Money in Account")
                .customView(R.layout.transfer_layout, wrapInScrollView)
                .positiveText("confirm");
        dialog = builder.build();
        view = dialog.getCustomView();

        TextView uidTextView = (TextView) view.findViewById(R.id.uidNumber);
        TextView nameTextView = (TextView) view.findViewById(R.id.nametext);
        TextView depositMoney = (TextView) view.findViewById(R.id.password);
        depositMoney.setText("Enter Amount to Withdraw");
        uidTextView.setText(uid);
        nameTextView.setText(name);
        editText = (EditText) view.findViewById(R.id.edittext);
        edittext1 = (EditText) view.findViewById(R.id.edittext1);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        edittext1.setHint("Enter Amount in "+"\u20B9");

          dialog.getBuilder().onPositive(new MaterialDialog.SingleButtonCallback() {
            public String key;

            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                try{

                    final TranstModel transtModel = new TranstModel();
                    final String uidFrom = Paper.book().read("uid","--");
                    transtModel.setToAadharNo(edittext1.getText().toString().replaceAll("\\s+",""));
                    transtModel.setFromAadharNo(uid);
                    transtModel.setByAadharNo(uidFrom);
                    transtModel.setAmountTranst(Float.valueOf(editText.getText().toString()));
                    transtModel.setDate(System.currentTimeMillis());

                    FirebaseDatabaseCheck.getDatabase().getReference().child("customer").orderByChild("aadhar_number").equalTo(uid).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            final UserModel userModel = dataSnapshot.getValue(UserModel.class);
                            if(userModel.getCurrentAmountInWallet()>transtModel.getAmountTranst()){
                                userModel.setCurrentAmountInWallet(userModel.getCurrentAmountInWallet()-transtModel.getAmountTranst());
                                key = dataSnapshot.getKey();
                                FirebaseDatabaseCheck.getDatabase().getReference().child("transt").push().setValue(transtModel);
                                FirebaseDatabaseCheck.getDatabase().getReference().child("customer").child(key).setValue(userModel);
                            }else{
                                Toast.makeText(ScanAadharActivity.this, "No Money in Wallet - "+userModel.getCurrentAmountInWallet() +"/- ", Toast.LENGTH_SHORT).show();
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

                    FirebaseDatabaseCheck.getDatabase().getReference().child("customer").orderByChild("aadhar_number").equalTo(transtModel.getToAadharNo()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            final UserModel userModel = dataSnapshot.getValue(UserModel.class);

                                userModel.setCurrentAmountInWallet(userModel.getCurrentAmountInWallet()+transtModel.getAmountTranst());
                                key = dataSnapshot.getKey();
                                FirebaseDatabaseCheck.getDatabase().getReference().child("customer").child(key).setValue(userModel);
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



                    finish();
                }catch (Exception e){
                }
            }
        });
        dialog.show();

    }

    public void showWithdrawMoney(){

        MaterialDialog dialog;
        final View view;
        boolean wrapInScrollView = true;
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Withdraw Money in Account")
                .customView(R.layout.withdraw_layout, wrapInScrollView)
                .positiveText("confirm");
        dialog = builder.build();
        view = dialog.getCustomView();

        TextView uidTextView = (TextView) view.findViewById(R.id.uidNumber);
        TextView nameTextView = (TextView) view.findViewById(R.id.nametext);
        TextView depositMoney = (TextView) view.findViewById(R.id.password);
        uidTextView.setText(uid);
        nameTextView.setText(name);
        editText = (EditText) view.findViewById(R.id.edittext);
        edittext1 = (EditText) view.findViewById(R.id.edittext1);
        edittext1.setInputType(InputType.TYPE_CLASS_TEXT);
        edittext1.setHint("Enter Amount in "+"\u20B9");

        dialog.getBuilder().onPositive(new MaterialDialog.SingleButtonCallback() {
            public String key;

            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                try{

                    final TranstModel transtModel = new TranstModel();
                    final String uidFrom = Paper.book().read("uid","--");
                    transtModel.setToAadharNo(uidFrom);
                    transtModel.setFromAadharNo(uid);
                    transtModel.setByAadharNo(uidFrom);
                    transtModel.setAmountTranst(Float.valueOf(editText.getText().toString()));
                    transtModel.setDate(System.currentTimeMillis());

                    FirebaseDatabaseCheck.getDatabase().getReference().child("customer").orderByChild("aadhar_number").equalTo(uid).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            final UserModel userModel = dataSnapshot.getValue(UserModel.class);

                            if(userModel.getCurrentAmountInWallet()>transtModel.getAmountTranst()&& userModel.getPin().equals(editText.getText().toString())){
                                userModel.setCurrentAmountInWallet(userModel.getCurrentAmountInWallet()-transtModel.getAmountTranst());
                                key = dataSnapshot.getKey();
                                FirebaseDatabaseCheck.getDatabase().getReference().child("transt").push().setValue(transtModel);
                                FirebaseDatabaseCheck.getDatabase().getReference().child("customer").child(key).setValue(userModel);
                            }else if(userModel.getPin().equals(editText.getText().toString())){
                                Toast.makeText(ScanAadharActivity.this, "No Money in Wallet  - "+userModel.getCurrentAmountInWallet() +"/- ", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(ScanAadharActivity.this, "User Pin Wrong", Toast.LENGTH_SHORT).show();
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


                    FirebaseDatabaseCheck.getDatabase().getReference().child("merchants").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            UserModel userModel = dataSnapshot.getValue(UserModel.class);
                            if(userModel.getAadhar_number().equals(uidFrom)){
                                userModel.setCurrentAmountInWallet(userModel.getCurrentAmountInWallet()+Float.valueOf(editText.getText().toString()));
                                FirebaseDatabaseCheck.getDatabase().getReference().child("merchants").child(dataSnapshot.getKey()).setValue(userModel);
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

                    finish();
                }catch (Exception e){
                }
            }
        });
        dialog.show();

    }

    public void showDepositMoney(){
        MaterialDialog dialog;
        final View view;
        boolean wrapInScrollView = true;
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Deposit Money in Account")
                .customView(R.layout.registration_layout, wrapInScrollView)
                .positiveText("confirm");
        dialog = builder.build();
        view = dialog.getCustomView();

        TextView uidTextView = (TextView) view.findViewById(R.id.uidNumber);
        TextView nameTextView = (TextView) view.findViewById(R.id.nametext);
        TextView depositMoney = (TextView) view.findViewById(R.id.password);
        depositMoney.setText("Enter Amount to deposit");
        uidTextView.setText(uid);
        nameTextView.setText(name);

        editText = (EditText) view.findViewById(R.id.edittext);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setHint("Enter Money in "+"\u20B9");

        dialog.getBuilder().onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                try{

                    final TranstModel transtModel = new TranstModel();
                    final String uidFrom = Paper.book().read("uid","--");
                    transtModel.setToAadharNo(uid);
                    transtModel.setFromAadharNo(uidFrom);
                    transtModel.setByAadharNo(uidFrom);
                    transtModel.setAmountTranst(Float.valueOf(editText.getText().toString()));
                    transtModel.setDate(System.currentTimeMillis());

                    FirebaseDatabaseCheck.getDatabase().getReference().child("transt").push().setValue(transtModel);

                    FirebaseDatabaseCheck.getDatabase().getReference().child("customer").orderByChild("aadhar_number").equalTo(uid).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            UserModel userModel = dataSnapshot.getValue(UserModel.class);
                            userModel.setCurrentAmountInWallet(userModel.getCurrentAmountInWallet()+transtModel.getAmountTranst());
                            FirebaseDatabaseCheck.getDatabase().getReference().child("customer").child(dataSnapshot.getKey()).setValue(userModel);
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

                    FirebaseDatabaseCheck.getDatabase().getReference().child("merchants").child(uidFrom).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            UserModel userModel = dataSnapshot.getValue(UserModel.class);
                            userModel.setCurrentAmountInWallet(userModel.getCurrentAmountInWallet()-Float.valueOf(editText.getText().toString()));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    FirebaseDatabaseCheck.getDatabase().getReference().child("merchants").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            UserModel userModel = dataSnapshot.getValue(UserModel.class);
                            if(userModel.getAadhar_number().equals(uidFrom)){
                                userModel.setCurrentAmountInWallet(userModel.getCurrentAmountInWallet()-Float.valueOf(editText.getText().toString()));
                                FirebaseDatabaseCheck.getDatabase().getReference().child("merchants").child(dataSnapshot.getKey()).setValue(userModel);
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



                    finish();
                }catch (Exception e){
                }
            }
        });
        dialog.show();
    }

    public void showAddNewUser(){
        MaterialDialog dialog;
        final View view;
        boolean wrapInScrollView = true;
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Create New Account ")
                .customView(R.layout.registration_layout, wrapInScrollView)
                .positiveText("confirm");
        dialog = builder.build();
        view = dialog.getCustomView();

        TextView uidTextView = (TextView) view.findViewById(R.id.uidNumber);
        TextView nameTextView = (TextView) view.findViewById(R.id.nametext);

        uidTextView.setText(uid);
        nameTextView.setText(name);

        dialog.getBuilder().onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                try{
                    EditText editText = (EditText) view.findViewById(R.id.edittext);
                    editText.getText().toString();
                    UserModel userModel = new UserModel();
                    userModel.setAadhar_number(uid);
                    userModel.setAddress(address);
                    userModel.setName(name);
                    userModel.setCurrentAmountInWallet(0.0f);
                    userModel.setPin(editText.getText().toString());
                    FirebaseDatabaseCheck.getDatabase().getReference().child("customer").push().setValue(userModel);
                    finish();
                }catch (Exception e){
                }
            }
        });
        dialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getDataFromRawData(){

        InputStream stream = new ByteArrayInputStream(rawData.getBytes(StandardCharsets.UTF_8));
        new XMLPullParserHandler().parse(stream);

        XmlPullParserFactory pullParserFactory;

        try {
            // init the parserfactory
            pullParserFactory = XmlPullParserFactory.newInstance();
            // get the parser
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(rawData));

            // parse the XML
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d("Rajdeol","Start document");
                } else if(eventType == XmlPullParser.START_TAG && DataAttributes.AADHAAR_DATA_TAG.equals(parser.getName())) {
                    // extract data from tag
                    //uid
                    uid = parser.getAttributeValue(null,DataAttributes.AADHAR_UID_ATTR);
                    //name

                    name = parser.getAttributeValue(null,DataAttributes.AADHAR_NAME_ATTR);

                    gender = parser.getAttributeValue(null,DataAttributes.AADHAR_GENDER_ATTR);
                    dob = parser.getAttributeValue(null,DataAttributes.AADHAR_YOB_ATTR);

                    address = parser.getAttributeValue(null,DataAttributes.AADHAR_VTC_ATTR);
                    address = address + " " +parser.getAttributeValue(null,DataAttributes.AADHAR_DIST_ATTR);
                    address = address + " " +parser.getAttributeValue(null,DataAttributes.AADHAR_STATE_ATTR);
                    address = address + " " + parser.getAttributeValue(null,DataAttributes.AADHAR_PC_ATTR);




                } else if(eventType == XmlPullParser.END_TAG) {
                    Log.d("Rajdeol","End tag "+parser.getName());

                } else if(eventType == XmlPullParser.TEXT) {
                    Log.d("Rajdeol","Text "+parser.getText());
                }
                // update eventType
                eventType = parser.next();
            }
            // display the data on screen

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
