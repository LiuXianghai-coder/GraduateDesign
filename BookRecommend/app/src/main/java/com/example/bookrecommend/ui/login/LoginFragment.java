package com.example.bookrecommend.ui.login;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.bookrecommend.constant.Tools;
import com.example.bookrecommend.databinding.FragmentLoginBinding;
import com.example.bookrecommend.db.DBUtil;
import com.example.bookrecommend.db.UserInfoEntity;
import com.example.bookrecommend.pojo.UserInfo;
import com.example.bookrecommend.service.UserService;
import com.example.bookrecommend.sington.PasswordTool;
import com.example.bookrecommend.sington.ServiceSingle;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bookrecommend.sington.AccountCheckTool.isPhone;
import static com.example.bookrecommend.sington.AccountCheckTool.isValidAccount;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentLoginBinding binding;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        assert getArguments() != null;
        UserInfoEntity obj = (UserInfoEntity) getArguments().get("userInfo");

        binding.registerButton.setOnClickListener(v -> {
            Log.d("ToRegister", obj.toString());
            NavDirections action =
                    LoginFragmentDirections.actionLoginFragmentToRegisterFragment(obj);
            Navigation.findNavController(v).navigate(action);
        });

        binding.verifyCodeButton.setOnClickListener(v -> {
            Log.d("ToVerifyCodeLogin", obj.toString());
            NavDirections action =
                    LoginFragmentDirections.actionLoginFragmentToVerifyCodeLoginFragment(obj);
            Navigation.findNavController(v).navigate(action);
        });

        binding.loginButton.setOnClickListener(v -> {
            String account = binding.loginAccount.getText().toString();
            String password = binding.loginPassword.getText().toString();

            Log.d("UserLogin", "LoginButton click");

            if (!isValidAccount(account)) {
                Log.d("UserLogin", "?????????????????????????????????");
                Toast.makeText(requireContext(), "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                return;
            }

            /*
                ??????????????????????????????????????????????????????????????? UserInfo ??????
             */
            UserInfo userInfo = new UserInfo();
            if (isPhone(account)) userInfo.setUserPhone(account);
            else userInfo.setUserEmail(account);
            userInfo.setUserPassword(PasswordTool.SHA512(password));

            Log.d("UserLogin", userInfo.toString());

            // ????????????????????? Retrofit ??????????????????????????????
            UserService userService = ServiceSingle.getUserService();
            Call<UserInfo> userServiceCall = userService.userLogin(userInfo);
            userServiceCall.enqueue(new Callback<UserInfo>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(@NotNull Call<UserInfo> call,
                                       @NotNull Response<UserInfo> response) {
                    if (!response.isSuccessful()) {
                        Log.d("UserLogin", "Login get response failed");
                        return;
                    }

                    UserInfo result = response.body();
                    Log.d("UserLogin", "Login successful");
                    assert result != null;
                    UserInfoEntity userInfoEntity = Tools.convertToUserInfoEntity(result);
                    userInfoEntity.setRecordId(obj.getRecordId());

                    // ????????????????????????
                    DBUtil. saveUserInfo(requireContext(), userInfoEntity);

                    // ??????????????? UserInfoEntity ????????????
                    Tools.updateUserInfoEntitySingle(userInfoEntity);

                    // ??????????????????
                    NavDirections action =
                            LoginFragmentDirections.actionLoginFragmentToNavigationHome(userInfoEntity);
                    Navigation.findNavController(v).navigate(action);
                }

                @Override
                public void onFailure(@NotNull Call<UserInfo> call,
                                      @NotNull Throwable t) {
                    Toast.makeText(requireContext(), "????????????????????????", Toast.LENGTH_SHORT).show();
                    Log.d("UserLogin", Objects.requireNonNull(t.getLocalizedMessage()));
                }
            });
        });

        return binding.getRoot();
    }
}