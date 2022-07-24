package cls.simplecar.views;

import cls.simplecar.R;
import cls.simplecar.Spawner;
import cls.simplecar.Spawner;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MessageToastView extends FrameLayout {
    private static final String TAG = "MessageToast";
    private TextView title;
    private TextView desc;
    private StandardButton btn;
    private Spawner.StateCallBack stateCallback;

    private String titleS = "";
    private String descS = "";
    private int btnIcon = R.drawable.ic_ok;
    private String btnText = "OK";

    public MessageToastView(@NonNull Context context) {
        super(context);
        init();
    }

    public MessageToastView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MessageToastView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MessageToastView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.message_toast_view,this);
        btn = findViewById(R.id.message_toast_btn);
        title = findViewById(R.id.message_toast_title);
        desc = findViewById(R.id.message_toast_desc);
        title.setText(titleS);
        desc.setText(descS);
        btn.setIcon(btnIcon);
        btn.setText(btnText);
        Log.d(TAG, "init: ");
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageToastView.this.removeAllViews();
                if (MessageToastView.this.getParent() != null) {
                    ((ViewGroup) MessageToastView.this.getParent()).removeView(MessageToastView.this);
                    stateCallback.destroyed();

                }

            }
        });
    }

    public void setTitle(String unlock_request_send) {
        titleS = unlock_request_send;
        title.setText(titleS);


    }

    public void setDesc(String s) {
        descS = s;
        desc.setText(descS);


    }

    public void setBtnVis(boolean b) {
        btn.setVisibility(b? View.VISIBLE:View.GONE);
        postInvalidate();

    }
    public void setBtnText(String b) {
        btnText = b;
        postInvalidate();

    }
    public void setBtnIcon(int b) {
        btnIcon = b;
        postInvalidate();

    }
    public void setStateCallback(Spawner.StateCallBack stateCallback){
        this.stateCallback = stateCallback;
    }

}
