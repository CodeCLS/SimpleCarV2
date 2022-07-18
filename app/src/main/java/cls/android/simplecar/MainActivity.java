package cls.android.simplecar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;

import com.android.billingclient.api.Purchase;

import cls.android.simplecar.fragments.CarViewModel;
import cls.android.simplecar.fragments.GooglePayFragment;
import cls.android.simplecar.fragments.IntroductionFragment;
import cls.android.simplecar.fragments.MainFragment;
import cls.android.simplecar.fragments.SplashScreen;
import cls.android.simplecar.tools.BillingTool;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int TAG_CODE_PERMISSION_LOCATION = 123;
    private static final long CONSTANT_SPLASH_SCREEN_TIME = 1500;
    private FrameLayout frameLayout;
    private CarViewModel viewModelCar;
    private Bundle savedInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        showSplash();
        savedInstance = savedInstanceState;
        initViewModel();

        //getPermissions();

    }

    private void getPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {


        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    TAG_CODE_PERMISSION_LOCATION);
        }
    }

    private void showSplash() {
        showFragment(new SplashScreen());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                observe();

            }
        },CONSTANT_SPLASH_SCREEN_TIME);
    }

    private void initViews() {
        frameLayout = findViewById(R.id.frame_main);
    }

    private void initViewModel() {
        if (viewModelCar == null) {
            viewModelCar = new ViewModelProvider(this).get(CarViewModel.class);
            viewModelCar.init(getApplicationContext());
        }
        BillingTool.getInstance(this);

    }
    private void redirect() {
        Purchase purchase = new SaveDataTool(this).getPurchase();
        Log.d(TAG, "redirect: " + purchase);
        if (purchase == null){
            showBilling();
            return;
        }

    }
    private void observe() {
        viewModelCar.getHasAccessAfterCheck(this).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    redirect();

                    viewModelCar.updateCars(getApplicationContext());
                    showFragment(new MainFragment());
                }
                else {
                    //TODO has aldready done introduction?
                    showFragment(new IntroductionFragment());
                }
            }
        });
    }



    public CarViewModel getViewModelCar() {
        return viewModelCar;
    }

    public void connectToCar() {
        viewModelCar.connect(getApplicationContext());
    }

    public void showFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(frameLayout.getId(),fragment)
                .commit();
    }


    public Bundle getSavedBundle() {
        return savedInstance;
    }

    public void showBilling() {
        showFragment(new GooglePayFragment());
    }
}