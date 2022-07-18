package cls.android.simplecar.fragments;

import cls.android.simplecar.R;
import cls.android.simplecar.views.StandardButton;
import cls.android.simplecar.MainActivity;
import cls.android.simplecar.SaveDataTool;
import cls.android.simplecar.tools.BillingTool;
import cls.android.simplecar.views.StandardButton;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;

import java.util.List;

public class GooglePayFragment extends Fragment {
    private static final int PREMIUM = 1;
    private static final int BASIC = 0;
    private StandardButton btnBasic;
    private StandardButton btnPremium;
    SaveDataTool saveDataTool = new SaveDataTool(getContext());

    private SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            Log.d(TAG, "onSharedPreferenceChanged: ");
            Purchase purchase = saveDataTool.getPurchase();
            if(getActivity() != null){
                Log.d(TAG, "onSharedPreferenceChanged: 1");

                if (purchase != null && purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                    Log.d(TAG, "onSharedPreferenceChanged: 123");

                    ((MainActivity) getActivity()).connectToCar();
                }
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_google_pay,container,false);
    }

    private static final String TAG = "GooglePayFragment";
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnBasic = view.findViewById(R.id.btn_pay_basic);
        btnBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBilling(BASIC);
            }
        });
        btnPremium = view.findViewById(R.id.btn_pay_pro);
        btnPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBilling(PREMIUM);
            }
        });
    }

    private void handleBilling(int type) {
        BillingTool billingTool =BillingTool.getInstance();

        if (!billingTool.isConnected()){
            billingTool.post(new Runnable() {
                @Override
                public void run() {
                    handleBillingAction(type, billingTool);
                }
            });

        }else
            handleBillingAction(type, billingTool);

        saveDataTool.listen(listener);


    }

    private void handleBillingAction(int type, BillingTool billingTool) {
        switch (type){
            case BASIC:
                billingTool.queryProductsBasic(new ProductDetailsResponseListener() {
                    @Override
                    public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull List<ProductDetails> list) {
                        Log.d(TAG, "onProductDetailsResponse: " + billingResult + "" + list);
                        if (list.size() != 0)
                            billingTool.launchFlow(getActivity(),list.get(0),list.get(0).getSubscriptionOfferDetails().get(0).getOfferToken());

                    }
                });
                break;
            case PREMIUM:
                billingTool.queryProductsPremium(new ProductDetailsResponseListener() {
                    @Override
                    public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull List<ProductDetails> list) {
                        Log.d(TAG, "onProductDetailsResponse: " + billingResult + "" + list);
                        if (list.size() != 0)
                            billingTool.launchFlow(getActivity(),list.get(0),list.get(0).getSubscriptionOfferDetails().get(0).getOfferToken());

                    }
                });
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        saveDataTool.stopListening(listener);

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        saveDataTool.stopListening(listener);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveDataTool.stopListening(listener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveDataTool.stopListening(listener);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        saveDataTool.stopListening(listener);

    }
}
