package cls.android.simplecar.fragments;

import cls.android.simplecar.R;
import cls.android.simplecar.views.StandardButton;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class IntroductionSliderItemFragment extends Fragment {
    private int index = 1;

    private ImageView imageView;
    private TextView textTitle;
    private TextView textDesc;
    private StandardButton button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_slider_item_introduction,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.slider_item_fragment_img);
        textTitle = view.findViewById(R.id.slider_item_fragment_title);
        textDesc = view.findViewById(R.id.slider_item_fragment_desc);
        button = view.findViewById(R.id.slider_item_fragment_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((IntroductionSliderFragment)getParentFragment()).nextIntroductionFragmentItem();
            }
        });
        update();

    }

    public void setIntroductionItem(int index) {
        this.index = index;
        if (imageView != null) {
            update();

        }
    }

    private void update() {
        switch(index){
            case 1:
                imageView.setImageResource(R.drawable.ic_battery__1_);
                textTitle.setText(R.string.battery_introduction_title);
                textDesc.setText(R.string.battery_introduction_desc);
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_unlock);
                textTitle.setText(R.string.unlock_introduction_title);
                textDesc.setText(R.string.unlock_introduction_desc);
                break;

            case 3:
                imageView.setImageResource(R.drawable.ic_location_pin);
                textTitle.setText(R.string.location_introduction_title);
                textDesc.setText(R.string.location_introduction_desc);

        }

    }
}
