package cls.simplecar;

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

import cls.simplecar.fragments.CarViewModel;
import cls.simplecar.fragments.GooglePayFragment;
import cls.simplecar.fragments.IntroductionFragment;
import cls.simplecar.fragments.IntroductionSliderFragment;
import cls.simplecar.fragments.MainFragment;
import cls.simplecar.fragments.SplashScreen;
import cls.simplecar.tools.BillingTool;
import cls.simplecar.fragments.CarViewModel;

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
                initViewModel();
                getPermissions();
                observe();
                //showFragment(new IntroductionSliderFragment());

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
        showFragment(new MainFragment());

        //Purchase purchase = BillingTool.getInstance().getPurchase();
        //if (purchase == null){
        //    showBilling();
        //    return;
        //}
        //else{
        //    showFragment(new MainFragment());
//
        //}

    }
    private void observe() {
        viewModelCar.getHasSmartCarAccessAfterCheck(this).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d(TAG, "onChanged: " + aBoolean);
                if (aBoolean){
                    //if (Boolean.FALSE.equals(viewModelCar.hasSimpleCarAccess.getValue())){
                    //    showFragment(new IntroductionFragment());
                    //}
                    //else {
                        redirect();
                        viewModelCar.updateCarsFromOnline(getApplicationContext());
                    //}
                }
                else {
                    showFragment(new IntroductionFragment());
                }
            }
        });
        //viewModelCar.getHasSimpleCarAccess().observe(this, new Observer<Boolean>() {
        //    @Override
        //    public void onChanged(Boolean aBoolean) {
        //        if (aBoolean){
        //            if (Boolean.FALSE.equals(viewModelCar.hasSmartCarAccess.getValue())){
        //                viewModelCar.connect(MainActivity.this);
        //            }
        //            else {
        //                redirect();
        //                viewModelCar.updateCarsFromOnline(getApplicationContext());
        //            }
        //        }
        //        else {
        //            showFragment(new IntroductionFragment());
        //        }
        //    }
        //});
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
        //TODO change this
        showFragment(new GooglePayFragment());
    }
}