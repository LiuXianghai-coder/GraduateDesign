package com.example.bookrecommend.ui.login;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.bookrecommend.R;
import com.example.bookrecommend.databinding.FragmentRegisterFeatureBinding;
import com.example.bookrecommend.db.UserInfoEntity;
import com.example.bookrecommend.pojo.Feature;
import com.example.bookrecommend.sington.ServiceSingle;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFeatureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFeatureFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentRegisterFeatureBinding binding;

    private List<Feature> featureList;

    private final List<CheckBox> featureCheckBox = new LinkedList<>();

    public RegisterFeatureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFeatureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFeatureFragment newInstance(String param1, String param2) {
        RegisterFeatureFragment fragment = new RegisterFeatureFragment();
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
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterFeatureBinding.inflate(inflater, container, false);

        Call<List<Feature>> featuresCall = ServiceSingle.getUserService().allFeature();
        featuresCall.enqueue(new Callback<List<Feature>>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(@NotNull Call<List<Feature>> call,
                                   @NotNull Response<List<Feature>> response) {
                if (!response.isSuccessful()) {
                    Log.d("AllFeature", "Get All Feature Object Failed");
                    return;
                }

                featureList = response.body();

                assert featureList != null;
                int size = featureList.size();
                // 从服务端获取相关的书籍种类
                for (int i = 0; i < size; ) {
                    LinearLayout row = generateFeatureLinear();
                    CheckBox checkBox1 = generateFeatureCheck(featureList.get(i++));
                    row.addView(checkBox1);
                    featureCheckBox.add(checkBox1);

                    // 避免元素访问越界
                    if (i == size) {
                        binding.bookKinds.addView(row);
                        break;
                    }
                    CheckBox checkBox2 = generateFeatureCheck(featureList.get(i++));
                    row.addView(checkBox2);
                    featureCheckBox.add(checkBox2);

                    // 避免元素访问越界
                    if (i == size) {
                        binding.bookKinds.addView(row);
                        break;
                    }
                    CheckBox checkBox3 = generateFeatureCheck(featureList.get(i++));
                    row.addView(checkBox3);
                    featureCheckBox.add(checkBox3);

                    binding.bookKinds.addView(row);
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Feature>> call,
                                  @NotNull Throwable t) {
                Log.d("AllFeature", Objects.requireNonNull(t.getLocalizedMessage()));
            }
        });

        binding.bookFeaturesNext.setOnClickListener(v -> {
            assert getArguments() != null;
            UserInfoEntity obj = (UserInfoEntity) getArguments().get("userInfo");
            Set<Feature> features = new HashSet<>();

            // 遍历所有的书籍种类， 得到用户选择的喜欢的书籍的类型
            for (CheckBox checkBox : featureCheckBox) {
                if (checkBox.isChecked()) {
                    Feature feature = new Feature();
                    feature.setFeatureId(Short.parseShort(checkBox.getHint().toString()));
                    feature.setFeatureName(checkBox.getText().toString());
                    features.add(feature);
                }
            }
            obj.setFeatures(features);

            NavDirections action = RegisterFeatureFragmentDirections
                    .actionRegisterFeatureFragmentToRegisterPasswordFragment(obj);
            Navigation.findNavController(v).navigate(action);
        });

        return binding.getRoot();
    }

    /**
     * 产生一个当前界面需要的线性布局，这个布局应当是水平排列元素的，这是为了放入需要的复选框
     *
     * @return : 适合当前界面的一个线性布局对象
     */
    private LinearLayout generateFeatureLinear() {
        LinearLayout row = new LinearLayout(requireContext());
        row.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setWeightSum(3.0f);

        return row;
    }

    /**
     * 通过传入的 Feature 对象创建对应的复选框
     *
     * @param feature ： 传入的 Feature 对象
     * @return ： 得到的对应的复选框
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private CheckBox generateFeatureCheck(Feature feature) {
        CheckBox checkBox = new CheckBox(requireContext());
        checkBox.setHint(String.valueOf(feature.getFeatureId()));
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        checkBox.setText(feature.getFeatureName());
        checkBox.setGravity(Gravity.CENTER);
        checkBox.setTextAppearance(R.style.RegisterTextStyle);

        return checkBox;
    }
}