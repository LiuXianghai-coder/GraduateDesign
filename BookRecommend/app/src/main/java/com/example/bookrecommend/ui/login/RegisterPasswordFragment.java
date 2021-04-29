package com.example.bookrecommend.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.bookrecommend.constant.Tools;
import com.example.bookrecommend.databinding.FragmentRegisterPasswordBinding;
import com.example.bookrecommend.db.UserInfoEntity;
import com.example.bookrecommend.pojo.UserInfo;
import com.example.bookrecommend.sington.HttpStatus;
import com.example.bookrecommend.sington.PasswordTool;
import com.example.bookrecommend.sington.ServiceSingle;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterPasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentRegisterPasswordBinding binding;

    private UserInfoEntity obj;

    public RegisterPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterPasswordFragment newInstance(String param1, String param2) {
        RegisterPasswordFragment fragment = new RegisterPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterPasswordBinding.inflate(inflater, container, false);

        binding.registerFinishButton.setOnClickListener(v -> {
            String originPassword = binding.inputPassword.getText().toString();
            String rePassword = binding.reInputPassword.getText().toString();

            if (!originPassword.equals(rePassword)) {
                Toast.makeText(requireContext(), "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                return;
            }

            String resultPassword = PasswordTool.SHA512(originPassword);
            assert getArguments() != null;
            obj = (UserInfoEntity) getArguments().get("userInfo");
            obj.setUserPassword(resultPassword);

            UserInfo userInfo = Tools.convertToUserInfo(obj);

            Call<HttpStatus> register = ServiceSingle.getUserService().register(userInfo);
            register.enqueue(new Callback<HttpStatus>() {
                @Override
                public void onResponse(@NotNull Call<HttpStatus> call,
                                       @NotNull Response<HttpStatus> response) {
                    if (!response.isSuccessful()) {
                        Log.d("RegisterUser", "register user info failed");
                        return;
                    }

                    HttpStatus status = response.body();
                    if (status == HttpStatus.ACCEPTED) {
                        Log.d("RegisterUser", "Receive Accepted, register failed");
                        return;
                    }


                    NavDirections action = RegisterPasswordFragmentDirections
                            .actionRegisterPasswordFragmentToLoginFragment(obj);
                    Navigation.findNavController(v).navigate(action);
                }

                @Override
                public void onFailure(@NotNull Call<HttpStatus> call,
                                      @NotNull Throwable t) {
                    Log.d("RegisterUser", Objects.requireNonNull(t.getLocalizedMessage()));
                }
            });


        });

        return binding.getRoot();
    }

}