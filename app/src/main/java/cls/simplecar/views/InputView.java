package cls.simplecar.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cls.simplecar.R;

public class InputView extends FrameLayout {
    private EditText editText;
    private String hint;

    public InputView(@NonNull Context context) {
        super(context);
        init(null,0);
    }

    public InputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public InputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    public InputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs,defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_input,this);
        editText = findViewById(R.id.input_view_edit);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.InputView,
                defStyleAttr, 0);

        try {
            hint = a.getString(R.styleable.InputView_hint);
            editText.setHint(hint);
        } finally {
            a.recycle();
        }
    }

    public EditText getEditText() {
        return editText;
    }


}
