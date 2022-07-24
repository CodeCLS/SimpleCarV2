package cls.android.simplecar.fragments;

import cls.android.simplecar.MainActivity;
import cls.android.simplecar.R;
import cls.android.simplecar.UserRepository;
import cls.android.simplecar.api.SimpleCarSdk;
import cls.android.simplecar.api.Status;
import cls.android.simplecar.SaveDataTool;
import cls.android.simplecar.api.ApiErrorManager;
import cls.android.simplecar.tools.JsonUtil;
import cls.android.simplecar.views.StandardButton;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.billingclient.api.Purchase;

public class RetrieveInformationFragment extends Fragment {
    private StandardButton btn;
    private EditText inputTelephone;
    private EditText inputEmail;
    private EditText inputFirstName;
    private EditText inputSurName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_additional_info,container,false);
    }

    private static final String TAG = "RetrieveInformationFrag";
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn = view.findViewById(R.id.button_additional_fragment);
        inputTelephone = view.findViewById(R.id.input_additional_info_phone_fragment_view);
        inputEmail = view.findViewById(R.id.input_additional_info_email_fragment_view);
        inputFirstName = view.findViewById(R.id.input_additional_info_first_name_fragment_view);
        inputSurName = view.findViewById(R.id.input_additional_info_second_name_fragment_view);







        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                String phone = inputTelephone.getText().toString();
                String email = inputEmail.getText().toString();
                String firstName = inputFirstName.getText().toString();
                String secondName = inputSurName.getText().toString();
                if(validCellPhone(phone) && validEmail(email) && !firstName.isEmpty() && !secondName.isEmpty()){
                    ((MainActivity)getActivity()).getViewModelCar().signup(phone,
                            email, firstName, secondName, new SimpleCarSdk.OnSimpleCarSignUpFeedback() {
                                @Override
                                public void result(boolean apiResponse, @NonNull Status status) {
                                    if(apiResponse){
                                        Log.d(TAG, "result: ");
                                        ((MainActivity)getActivity()).getViewModelCar().saveUser(getContext(),
                                                new JsonUtil().parseUser(status.getAdditionalInformation()));
                                    }
                                    else{
                                        if (status.getErrorCode() == ApiErrorManager.API_INTERNAL_ERROR){
                                            Toast.makeText(getContext(), R.string.internal_error, Toast.LENGTH_SHORT).show();

                                        }
                                        if (status.getErrorCode() == ApiErrorManager.API_EMAIL_ERROR){
                                            Toast.makeText(getContext(), R.string.email_error_signup, Toast.LENGTH_SHORT).show();


                                        }
                                        if (status.getErrorCode() == ApiErrorManager.API_PHONE_ERROR){
                                            Toast.makeText(getContext(), R.string.phone_error_signup, Toast.LENGTH_SHORT).show();

                                        }
                                        if (status.getErrorCode() == ApiErrorManager.API_NAME_ERROR){
                                            Toast.makeText(getContext(), R.string.name_error_signup, Toast.LENGTH_SHORT).show();

                                        }

                                    }

                                }
                            });


                }
                else{
                    Toast.makeText(getContext(), R.string.a_field_invalid, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public boolean validCellPhone(String number) {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }
    public boolean validEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
