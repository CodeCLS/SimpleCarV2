package cls.simplecar.tools;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList;

import java.util.List;

import cls.simplecar.SaveDataTool;
import cls.simplecar.Application;
import cls.simplecar.SaveDataTool;

import static cls.simplecar.Application.getContext;

public class BillingTool {
    private static BillingTool instance;
    private BillingClient billingClient;
    private final PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
            Log.d(TAG, "onPurchasesUpdated:3 ");
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                    && purchases != null) {
                Log.d(TAG, "onPurchasesUpdated:4 ");
                for (Purchase purchase : purchases) {
                    Log.d(TAG, "onPurchasesUpdated: 5");
                    handlePurchase(purchase);
                }
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
            } else {
                // Handle any other error codes.
            }        }
    };
    private static boolean isConnected = false;
    private Runnable runnableWhenConnected;
    private static final String TAG = "BillingTool";

    void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            Log.d(TAG, "handlePurchas12e: " + purchase);
            if (!purchase.isAcknowledged()) {
                Log.d(TAG, "handlePurchase:1 ");
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                        Log.d(TAG, "onAcknowledgePurchaseResponse: ");
                        SaveDataTool saveDataTool = new SaveDataTool(Application.getContext());
                        saveDataTool.savePurchase(purchase);
                    }
                });
            }
            else{
                SaveDataTool saveDataTool = new SaveDataTool(Application.getContext());
                saveDataTool.savePurchase(purchase);
            }
        }
        else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {

        }

        }

    public static BillingTool getInstance(){
        if (instance == null)
            instance = new BillingTool(Application.getContext());
        return instance;
    }

    public static BillingTool getInstance(Context context){
        if (instance == null)
            instance = new BillingTool(context);
        return instance;
    }

    public BillingTool(Context context) {
        init(context);
    }

    public void init(Context context){

        billingClient = BillingClient.newBuilder(context)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK) {
                    isConnected = true;
                    queryPurchases();
                    Log.d(TAG, "onBillingSetupFin5ished: ");
                    if (runnableWhenConnected!= null){
                        runnableWhenConnected.run();
                        runnableWhenConnected = null;
                    }
                    // The BillingClient is ready. You can query purchases here.
                }
                else if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.ERROR) {
                    isConnected = false;
                    queryPurchases();
                    // The BillingClient is ready. You can query purchases here.
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                isConnected = false;
                Log.d(TAG, "onBillingSe54rviceDisconnected: ");
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });


    }
    public void queryProductsBasic(ProductDetailsResponseListener productDetailsResponseListener){
        QueryProductDetailsParams queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder()
                        .setProductList(
                                ImmutableList.of(
                                        QueryProductDetailsParams.Product.newBuilder()
                                                .setProductId("abo_simplecar_basic_tier")
                                                .setProductType(BillingClient.ProductType.SUBS)
                                                .build()))
                        .build();

        billingClient.queryProductDetailsAsync(
                queryProductDetailsParams,
                productDetailsResponseListener);
    }
    public void queryPurchases(){
        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                /* purchaseResponseListener= */ new PurchasesResponseListener() {
                    @Override
                    public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
                        if (!list.isEmpty()){
                            Log.d(TAG, "onQueryPurchasesResponse12312: ");
                            for (Purchase purchase : list){
                                handlePurchase(purchase);
                            }

                        }
                        else{
                            Log.d(TAG, "onQueryPurchasesResponse:123122 ");
                            SaveDataTool saveDataTool = new SaveDataTool(Application.getContext());
                            saveDataTool.savePurchase(null);
                        }

                    }
                }
        );
    }
    public void launchFlow(Activity activity,ProductDetails productDetails, String selectedOfferToken){
        BillingFlowParams billingFlowParams =
                BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(
                                ImmutableList.of(
                                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                                // fetched via queryProductDetailsAsync
                                                .setProductDetails(productDetails)

                                                // to get an offer token, call ProductDetails.getOfferDetails()
                                                // for a list of offers that are available to the user
                                                .setOfferToken(selectedOfferToken)
                                                .build()
                                )
                        )
                        .build();

        BillingResult billingResult = billingClient.launchBillingFlow(activity, billingFlowParams);
    }


    public void queryProductsPremium(ProductDetailsResponseListener productDetailsResponseListener) {
        QueryProductDetailsParams queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder()
                        .setProductList(
                                ImmutableList.of(
                                        QueryProductDetailsParams.Product.newBuilder()
                                                .setProductId("abo_simplecar_premium_tier")
                                                .setProductType(BillingClient.ProductType.SUBS)
                                                .build()))
                        .build();

        billingClient.queryProductDetailsAsync(
                queryProductDetailsParams,
                productDetailsResponseListener);
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void post(Runnable runnable) {
        this.runnableWhenConnected = runnable;
    }

    public Purchase getPurchase() {
        return new SaveDataTool(Application.getContext()).getPurchase();
    }
}
