package cls.android.simplecar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.smartcar.sdk.SmartcarCodeReceiver;

public class InterceptSmartCarCodeActivity extends AppCompatActivity {
    private static final String TAG = "InterceptSmartCarCodeAc";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        Uri uri = intent.getData();
        Log.d(TAG, "onCreate: " + uri);
        Intent intentCar = new Intent(this, SmartcarCodeReceiver.class);
        intentCar.setData(uri);
        Intent intentMain = new Intent(this,MainActivity.class);
        startActivity(intentMain);
        startActivity(intentCar);
        this.finish();
    }

}
