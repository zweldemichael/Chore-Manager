package choremanager.ca.choremanager;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by asadullahsansi on 11/30/17.
 */

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
