package cls.android.simplecar.fragments;

import cls.android.simplecar.R;
import cls.android.simplecar.adapter.IntroductionSliderAdapter;
import cls.android.simplecar.MainActivity;
import cls.android.simplecar.adapter.IntroductionSliderAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class IntroductionSliderFragment extends Fragment {
    private ViewPager2 viewPager2;
    private IntroductionSliderAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_slider_introduction,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager2 = view.findViewById(R.id.introduction_viewpager);
        adapter =new IntroductionSliderAdapter(this);
        viewPager2.setAdapter(adapter);
    }

    private static final String TAG = "IntroductionSliderFragm";
    public void nextIntroductionFragmentItem() {
        Log.d(TAG, "nextIntroductionFragmentItem: " + viewPager2.getCurrentItem());
        int nextPos = viewPager2.getCurrentItem()+1;

        if (nextPos>2)
            ((MainActivity)getActivity()).showFragment(new RetrieveInformationFragment());
        viewPager2.setCurrentItem(nextPos);

    }
}
