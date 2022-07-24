package cls.simplecar.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import cls.simplecar.fragments.IntroductionSliderItemFragment;


public class IntroductionSliderAdapter extends FragmentStateAdapter {
    public IntroductionSliderAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
        IntroductionSliderItemFragment fragment = new IntroductionSliderItemFragment();
        fragment.setIntroductionItem(position+1);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
