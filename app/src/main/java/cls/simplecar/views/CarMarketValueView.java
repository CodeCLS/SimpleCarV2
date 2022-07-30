package cls.simplecar.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import cls.simplecar.R;
import cls.simplecar.models.Car;
import cls.simplecar.tools.OnCarUpdate;

public class CarMarketValueView extends FrameLayout implements OnCarUpdate {
    private cls.simplecar.api.CarMarketValue carMarketValue;

    private LinearLayout valueLinearLayout;


    public CarMarketValueView(@NonNull Context context) {
        super(context);
        init();
    }

    public CarMarketValueView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CarMarketValueView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CarMarketValueView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_log_car,this);
        valueLinearLayout = findViewById(R.id.value_linear_car_market_value);
        if (carMarketValue != null){
            populateLayout();
        }
    }

    private void populateLayout() {
        valueLinearLayout.removeAllViews();
        valueLinearLayout.addView(generateView("Vin: " + carMarketValue.getVinCar()));
        valueLinearLayout.addView(generateView("Retail: " +carMarketValue.getRetail()));
        valueLinearLayout.addView(generateView("Loan Value: " +carMarketValue.getLoanValue()));
        valueLinearLayout.addView(generateView("Msrp: " +carMarketValue.getMsrp()));
        valueLinearLayout.addView(generateView("Trade In: " +carMarketValue.getTradeIn()));
        valueLinearLayout.addView(generateView("Average Trade In: " +carMarketValue.getAverageTradeIn()));
        valueLinearLayout.addView(generateView("Rough Trade In: " +carMarketValue.getRoughTradeIn()));

    }

    private View generateView(String vinCar) {
        StandardButton standardButton = new StandardButton(getContext());
        standardButton.setText(vinCar);
        standardButton.setClickableTheme(false);
        return standardButton;
    }

    @Override
    public void update(Car car) {
        this.carMarketValue = car.getCarMarketValue();
        if (carMarketValue != null){
            populateLayout();
        }

    }
}
