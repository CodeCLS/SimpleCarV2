package cls.android.simplecar;

import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;
import java.util.List;

import cls.android.simplecar.views.MessageToastView;

public class Spawner {
    private static List<Runnable> toDoArray = new ArrayList<>();
    private static boolean isToastShown = false;

    public static void setIsToastShown(boolean isToastShown) {
        Spawner.isToastShown = isToastShown;

    }

    public static void spawnMessageToast(View view, String unlock_request_send, String s, ViewGroup v,boolean hasButton) {
        if (isToastShown)
            return;
        setIsToastShown(true);
        MessageToastView messageToastView = new MessageToastView(view.getContext());
        messageToastView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        messageToastView.setTitle(unlock_request_send);
        messageToastView.setDesc(s);
        messageToastView.setBtnVis(true);
        messageToastView.setId(View.generateViewId());
        messageToastView.setStateCallback(new StateCallBack() {
            @Override
            public void destroyed() {
                setIsToastShown(false);
            }
        });
        v.addView(messageToastView);

        ConstraintSet set = new ConstraintSet();
        if (v instanceof ConstraintLayout){
            set.clone((ConstraintLayout) v);
            set.connect(messageToastView.getId(), ConstraintSet.TOP, ((ConstraintLayout) v).getId(), ConstraintSet.TOP, 0);
            set.connect(messageToastView.getId(), ConstraintSet.BOTTOM, ((ConstraintLayout) v).getId(), ConstraintSet.BOTTOM, 0);
            set.connect(messageToastView.getId(), ConstraintSet.END, ((ConstraintLayout) v).getId(), ConstraintSet.END, 0);
            set.connect(messageToastView.getId(), ConstraintSet.START, ((ConstraintLayout) v).getId(), ConstraintSet.START, 0);
            set.applyTo((ConstraintLayout) v);

        }
    }

    public static void queueSpawnMessageToast(String title, String desc, boolean hasButton, View view, ViewGroup parent) {
        if (isToastShown)
            toDoArray.add(new Runnable() {
                @Override
                public void run() {
                    spawnMessageToast(view,title,desc,parent,hasButton);
                }
            });
        else{
            spawnMessageToast(view,title,desc,parent,hasButton);
        }
    }

    public interface StateCallBack{
        void destroyed();
    }
}
