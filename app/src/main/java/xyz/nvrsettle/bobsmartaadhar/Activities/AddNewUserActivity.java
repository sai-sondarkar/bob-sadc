package xyz.nvrsettle.bobsmartaadhar.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import xyz.nvrsettle.bobsmartaadhar.R;
import xyz.nvrsettle.bobsmartaadhar.Utility.DataAttributes;
import xyz.nvrsettle.bobsmartaadhar.Utility.XMLPullParserHandler;

public class AddNewUserActivity extends AppCompatActivity {

    String rawData;
    String uid,name,address,dob,gender;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);

        rawData = getIntent().getExtras().getString("rawdata");

        getDataFromRawData();

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
                    Toast.makeText(AddNewUserActivity.this,uid,Toast.LENGTH_SHORT).show();
                    //name

                    name = parser.getAttributeValue(null,DataAttributes.AADHAR_NAME_ATTR);
                    Toast.makeText(AddNewUserActivity.this,name,Toast.LENGTH_SHORT).show();

                    gender = parser.getAttributeValue(null,DataAttributes.AADHAR_GENDER_ATTR);
                    dob = parser.getAttributeValue(null,DataAttributes.AADHAR_YOB_ATTR);
                    address = parser.getAttributeValue(null,DataAttributes.AADHAR_PC_ATTR );
                    address = address + parser.getAttributeValue(null,DataAttributes.AADHAR_VTC_ATTR);

                    Toast.makeText(AddNewUserActivity.this,name + gender + dob + address ,Toast.LENGTH_SHORT).show();

//                    Intent intent = new Intent(AddNewUserActivity.this,TaxInfoActivity.class);
//                    intent.putExtra("uid",uid);
//                    intent.putExtra("name",name);
//                    startActivity(intent);
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
