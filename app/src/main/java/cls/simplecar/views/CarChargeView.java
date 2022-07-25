package cls.simplecar.views;
import cls.simplecar.R;
import cls.simplecar.models.Car;
import cls.simplecar.tools.OnCarUpdate;
import cls.simplecar.models.Car;
import cls.simplecar.tools.OnCarUpdate;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class CarChargeView extends FrameLayout implements OnCarUpdate {
    private View chargingPercentView;
    private ConstraintLayout parent;
    private TextView textView;
    private Double percent = 50.0;

    public CarChargeView(@NonNull Context context) {
        super(context);
        init();
    }

    public CarChargeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CarChargeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CarChargeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_charge_main,this);
        parent = findViewById(R.id.charge_container_parent);
        textView = findViewById(R.id.charge_txt_box);
        setChargingPercentView(percent);
    }

    private static final String TAG = "CarChargeView";
    @Override
    public void update(Car car) {
        this.percent = car.getDriveProductAmountPercent();
        Log.d(TAG, "update:percent " + percent);
        if (parent != null) {
            setChargingPercentView(percent);
            textView.setText((new Double(percent * 100)).intValue()+ "%");
            if (percent < 0)
                textView.setText("~~~");

        }

    }
    public void setChargingPercentView(Double percent){
        // Get the constraint layout of the parent constraint view.

// Define a constraint set that will be used to modify the constraint layout parameters of the child.
        ConstraintSet mConstraintSet = new ConstraintSet();

// Start with a copy the original constraints.
        mConstraintSet.clone(parent);

// Define new constraints for the child (or multiple children as the case may be).
        mConstraintSet.constrainPercentWidth(R.id.charge_loading_view,percent.floatValue());

// Apply the constraints for the child view to the parent layout.
        mConstraintSet.applyTo(parent);
    }
}
