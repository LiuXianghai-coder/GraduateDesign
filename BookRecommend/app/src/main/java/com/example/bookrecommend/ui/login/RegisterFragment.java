package com.example.bookrecommend.ui.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.bookrecommend.databinding.FragmentRegisterBinding;
import com.example.bookrecommend.db.UserInfoEntity;
import com.example.bookrecommend.service.UserService;
import com.example.bookrecommend.sington.AccountCheckTool;
import com.example.bookrecommend.sington.HttpStatus;
import com.example.bookrecommend.sington.ServiceSingle;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bookrecommend.constant.Tools.setStopGetVerifyCode;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private String mParam2;

    private FragmentRegisterBinding binding;

    // 接受到的验证码， 保存起来以便验证
    private String verifyCode;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        binding.sendCodeButton.setOnClickListener(v -> {
            UserService userService = ServiceSingle.getUserService();
            String account = binding.registerAccount.getText().toString();
            if (!AccountCheckTool.isValidAccount(account)) {
                Toast.makeText(requireContext(), "请输入正确的邮箱地址或手机号", Toast.LENGTH_SHORT).show();
                return;
            }

            // 禁用发送验证码的点击功能，防止用户恶意点击
            setStopGetVerifyCode(requireContext(), binding.sendCodeButton);
            if (AccountCheckTool.isPhone(account)) {
                Log.d("Account", account + " is phone");
                Call<String> result = userService.getPhoneCode(account);
                result.enqueue(new Callback<String>() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onResponse(@NotNull Call<String> call,
                                           @NotNull Response<String> response) {
                        if (!response.isSuccessful()) {
                            Log.d("Phone VerifyCode", "Get VerifyCode failed");
                            Toast.makeText(requireContext(), "加载验证码失败", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        verifyCode = response.body();

                        System.out.println("verifyCode: " + verifyCode);
                    }

                    @Override
                    public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                        Log.d("Phone VerifyCode", Objects.requireNonNull(t.getLocalizedMessage()));
                    }
                });
            } else if (AccountCheckTool.isEmail(account)) {
                Log.d("Account", account + " is mail");
                Call<String> result = userService.getMailCode(account);
                result.enqueue(new Callback<String>() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onResponse(@NotNull Call<String> call,
                                           @NotNull Response<String> response) {
                        if (!response.isSuccessful()) {
                            Log.d("Mail VerifyCode", "Get VerifyCode Failed");
                            Toast.makeText(requireContext(), "加载验证码失败", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        verifyCode = response.body();
                    }

                    @Override
                    public void onFailure(@NotNull Call<String> call,
                                          @NotNull Throwable t) {
                        Log.d("Mail VerifyCode", Objects.requireNonNull(t.getLocalizedMessage()));
                    }
                });
            }
        });

        binding.registerButton.setOnClickListener(v -> {
            String account = binding.registerAccount.getText().toString();
            String code = binding.registerCode.getText().toString();
            assert getArguments() != null;
            UserInfoEntity obj = (UserInfoEntity) getArguments().get("userInfo");
//            navigationToBasicInfo(requireView(), obj);
            if (AccountCheckTool.isPhone(account)) {
                Call<HttpStatus> checkCall = ServiceSingle.getUserService().findUserInfoByPhone(account);
                checkCall.enqueue(new Callback<HttpStatus>() {
                    @Override
                    public void onResponse(@NotNull Call<HttpStatus> call,
                                           @NotNull Response<HttpStatus> response) {
                        if (!response.isSuccessful()) {
                            Log.d("CheckAccount", "The Phone has been registered.");
                            return;
                        }

                        if (response.body() == HttpStatus.ACCEPTED) {
                            Log.d("CheckAccount", "The Check Phone account failed");
                            return;
                        }

                        if (code.trim().equals(verifyCode)) {
                            obj.setUserPhone(account);

                            navigationToBasicInfo(v, obj);
                        } else {
                            Toast.makeText(requireContext(), "验证码不匹配", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<HttpStatus> call,
                                          @NotNull Throwable t) {
                        Log.d("CheckPhone", Objects.requireNonNull(t.getLocalizedMessage()));
                    }
                });
            } else if (AccountCheckTool.isEmail(account)) {
                Call<HttpStatus> checkCall = ServiceSingle.getUserService().findUserInfoByEmail(account);
                checkCall.enqueue(new Callback<HttpStatus>() {
                    @Override
                    public void onResponse(@NotNull Call<HttpStatus> call,
                                           @NotNull Response<HttpStatus> response) {
                        if (!response.isSuccessful()) {
                            Log.d("CheckAccount", "The Email has been registered.");
                            return;
                        }

                        if (response.body() == HttpStatus.ACCEPTED) {
                            Log.d("CheckAccount", "The Check Email account failed");
                            return;
                        }

                        if (code.trim().equals(verifyCode)) {
                            obj.setUserEmail(account);

                            navigationToBasicInfo(v, obj);
                        } else {
                            Toast.makeText(requireContext(), "验证码不匹配", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<HttpStatus> call,
                                          @NotNull Throwable t) {
                        Log.d("CheckEmail", Objects.requireNonNull(t.getLocalizedMessage()));
                    }
                });
            } else {
                Toast.makeText(requireContext(), "请输入有效的邮箱地址或手机号", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    /**
     * 导航到下一基础信息注册页面
     */
    private void navigationToBasicInfo(@NonNull View v,
                                       @NonNull UserInfoEntity obj) {
        // 导航到基础信息注册界面
        NavDirections action =
                RegisterFragmentDirections.actionRegisterFragmentToBasicInfoRegisterFragment(obj);
        Navigation.findNavController(v).navigate(action);
    }
}