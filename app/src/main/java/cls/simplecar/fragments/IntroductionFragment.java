package cls.simplecar.fragments;


import cls.simplecar.R;
import cls.simplecar.views.StandardButton;
import cls.simplecar.MainActivity;
import cls.simplecar.database.CarDataBaseRepo;
import cls.simplecar.views.StandardButton;
import cls.simplecar.MainActivity;
import cls.simplecar.database.CarDataBaseRepo;
import cls.simplecar.views.StandardButton;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class IntroductionFragment extends Fragment {
    private static final String TAG = "IntroductionFragment";
    private StandardButton button;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_introduction,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CarDataBaseRepo.getInstance(getContext()).deleteAll();
        button = view.findViewById(R.id.standardButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).showFragment(new IntroductionSliderFragment());
                //((MainActivity)getActivity()).connectToCar();

            }
        });
    }


}
