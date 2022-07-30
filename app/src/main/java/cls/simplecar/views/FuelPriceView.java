package cls.simplecar.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cls.simplecar.R;
import cls.simplecar.models.Car;
import cls.simplecar.tools.OnCarUpdate;

public class FuelPriceView extends FrameLayout implements OnCarUpdate {
    public FuelPriceView(@NonNull Context context) {
        super(context);
        init();
    }

    public FuelPriceView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FuelPriceView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_fuel_price_view,this);
    }

    public FuelPriceView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public void update(Car car) {

    }
}
