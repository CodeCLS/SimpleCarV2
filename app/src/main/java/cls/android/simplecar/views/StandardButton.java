package cls.android.simplecar.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import cls.android.simplecar.R;

public class StandardButton extends FrameLayout {
    public static final int ENABLED = 1;
    public static final int DISABLED = 0;

    private ConstraintLayout parent;
    private ImageView icon;
    private TextView textView;
    private int iconReference = -1;
    private String text = "";
    private boolean clickableTheme = true;

    public StandardButton(@NonNull Context context) {
        super(context);
        init(null,0);
    }

    public StandardButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public StandardButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    public StandardButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs,defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(R.layout.btn_standard_view,this);
        textView = findViewById(R.id.txt_button_standard_btn);
        parent = findViewById(R.id.parent_standard_button);
        icon = findViewById(R.id.btn_standard_img);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StandardButton,
                defStyleAttr, 0);

        try {
            iconReference = a.getResourceId(R.styleable.StandardButton_standard_btn_icon,-1);
            text = a.getString(R.styleable.StandardButton_standard_btn_text);
        } finally {
            a.recycle();
        }
        try {
            icon.setImageResource(iconReference);
        }catch (Exception e){
            Log.e(TAG, "init: ", e);
            icon.setVisibility(GONE);
        }
        textView.setText(text);
    }

    private static final String TAG = "StandardButton";
    Float firstTouchX = null;
    Float firstTouchY = null;
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!clickableTheme)
            return false;
        Drawable buttonDrawable = parent.getBackground();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                theme(R.drawable.background_button_standard_pressed, R.color.cultured_white);

                firstTouchX = event.getRawX();
                firstTouchY = event.getRawY();

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                theme(R.drawable.background_button_standard, R.color.black);

                if (firstTouchX != null && firstTouchX+100 > event.getRawX() && firstTouchY != null && firstTouchY + 100 > event.getRawY()){
                    clicked();

                }
                firstTouchX = null;
                firstTouchY = null;
                break;


        }
        return true;
    }

    private void theme(int p, int p2) {
        Drawable buttonDrawable;
        buttonDrawable = ContextCompat.getDrawable(getContext(), p);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            parent.setBackground(buttonDrawable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                icon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), p2)));
            }

        }
        textView.setTextColor(ContextCompat.getColor(getContext(), p2));
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void clicked() {
        callOnClick();
    }

    public void setIcon(int btnIcon) {
        icon.setImageResource(btnIcon);

    }

    public void setText(String btnText) {
        text = btnText;
        textView.setText(text);
        postInvalidate();
    }

    public void theme(int enabled) {
        if (enabled == ENABLED){
            theme(R.drawable.background_button_standard, R.color.black);
        }
        else{
            theme(R.drawable.background_button_standard_pressed, R.color.cultured_white);


        }

    }

    public void setClickableTheme(boolean b) {
        this.clickableTheme = b;
    }
}
