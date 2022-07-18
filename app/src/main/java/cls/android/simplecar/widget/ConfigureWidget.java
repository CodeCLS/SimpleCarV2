package cls.android.simplecar.widget;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import cls.android.simplecar.R;

public class ConfigureWidget extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
    }
}
