package xyz.nvrsettle.bobsmartaadhar.FirebaseInfo;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by sai on 12/11/17.
 */

public class FirebaseDatabaseCheck {

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}
