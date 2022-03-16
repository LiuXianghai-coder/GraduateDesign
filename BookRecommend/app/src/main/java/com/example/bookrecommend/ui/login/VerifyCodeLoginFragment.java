package com.example.bookrecommend.ui.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.bookrecommend.databinding.FragmentVerifyCodeLoginragmentBinding;
import com.example.bookrecommend.db.DBUtil;
import com.example.bookrecommend.db.UserInfoEntity;
import com.example.bookrecommend.service.UserService;
import com.example.bookrecommend.sington.AccountCheckTool;
import com.example.bookrecommend.sington.ServiceSingle;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bookrecommend.constant.Tools.setStopGetVerifyCode;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerifyCodeLoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerifyCodeLoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentVerifyCodeLoginragmentBinding binding;

    private String verifyCode;

    public VerifyCodeLoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VerifyCodeLoginragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VerifyCodeLoginFragment newInstance(String param1, String param2) {
        VerifyCodeLoginFragment fragment = new VerifyCodeLoginFragment();
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
        binding = FragmentVerifyCodeLoginragmentBinding
                .inflate(inflater, container, false);

        binding.sendVerifyCodeButton.setOnClickListener(v -> {
            UserService userService = ServiceSingle.getUserService();
            String account = binding.loginAccount.getText().toString();
            // 禁用发送验证码的点击功能，防止用户恶意点击
            setStopGetVerifyCode(requireContext(), binding.sendVerifyCodeButton);
            if (AccountCheckTool.isPhone(account)) {
                Log.d("Account", account + " is phone");
                Call<String> result = userService.getPhoneCode(account);
                result.enqueue(new Callback<String>() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onResponse(@NotNull Call<String> call,
                                           @NotNull Response<String> response) {
                        if (!response.isSuccessful()) {
                            Log.d("VerifyCode", "Get VerifyCode failed");
                            Toast.makeText(requireContext(), "加载验证码失败", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        verifyCode = response.body();
                    }

                    @Override
                    public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                        Log.d("VerifyCode", Objects.requireNonNull(t.getLocalizedMessage()));
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
                            Log.d("VerifyCode", "Get VerifyCode Failed");
                            Toast.makeText(requireContext(), "加载验证码失败", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        verifyCode = response.body();
                    }

                    @Override
                    public void onFailure(@NotNull Call<String> call,
                                          @NotNull Throwable t) {
                        Log.d("VerifyCode", Objects.requireNonNull(t.getLocalizedMessage()));
                    }
                });
            } else {
                Toast.makeText(requireContext(), "请输入有效的帐号！", Toast.LENGTH_SHORT).show();
            }
        });

        /*
            如果用户忘记了登录密码，由于我们在此处存储了用户的上次登录信息，
                我们保证用户的每次更新都会立刻保存到缓存中，因此无须再次访问服务端来获取相关的用户信息
         */
        UserInfoEntity obj = DBUtil.getRecentUserInfoRecord(requireContext());

        binding.verifyCodeLoginButton.setOnClickListener(v -> {
            String inputVerifyCode = binding.verifyCodeInput.getText().toString();
            if (!inputVerifyCode.equals(verifyCode)) {
                Toast.makeText(requireContext(), "验证码输入错误", Toast.LENGTH_SHORT).show();
                return;
            }

            NavDirections action = VerifyCodeLoginFragmentDirections
                    .actionVerifyCodeLoginFragmentToNavigationHome(obj);
            Navigation.findNavController(v).navigate(action);
        });

        return binding.getRoot();
    }
}