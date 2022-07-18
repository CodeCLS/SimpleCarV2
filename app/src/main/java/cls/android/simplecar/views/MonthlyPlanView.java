package cls.android.simplecar.views;
import cls.android.simplecar.R;
import cls.android.simplecar.models.Car;
import cls.android.simplecar.tools.OnCarUpdate;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MonthlyPlanView extends FrameLayout implements OnCarUpdate {
    private TextView textView;
    public MonthlyPlanView(@NonNull Context context) {
        super(context);
        init();
    }

    public MonthlyPlanView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MonthlyPlanView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MonthlyPlanView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.monthly_plan_view,this);



    }

    @Override
    public void update(Car car) {

    }
}
