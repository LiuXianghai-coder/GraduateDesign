package com.example.bookrecommend.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookrecommend.R;
import com.example.bookrecommend.databinding.FragmentDashboardBinding;
import com.example.bookrecommend.pojo.ShareEntity;
import com.example.bookrecommend.sington.ServiceSingle;
import com.example.bookrecommend.sington.ShareView;
import com.example.bookrecommend.sington.SingleObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    // 当前动态 Fragment 对应的数据绑定对象
    private FragmentDashboardBinding binding;

    private RecyclerView recyclerView;

    public DashboardFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 将当前视图绑定到当前的数据绑定对象
        binding = FragmentDashboardBinding
                .inflate(inflater, container, false);

        ShareView.setShareView(container);

        recyclerView = binding.shareEntityRecycleView;

        loadUserShares();

        binding.createShareButton.setOnClickListener(v -> {
            // 带有传递信息的 Bundle 对象
            Bundle bundle = new Bundle();
            // 跳转到对应的创建动态页面
            Navigation.findNavController(v)
                    .navigate(R.id.action_navigation_dashboard_to_navigation_create_share,
                            bundle);
        });

        return binding.getRoot();
    }

    private void loadUserShares() {
        long userId = SingleObject.getUserInfoEntity().getUserId();
        Call<List<ShareEntity>> sharesCall = ServiceSingle
                .getUserShareService().defaultUserShares(userId);

        sharesCall.enqueue(new Callback<List<ShareEntity>>() {
            @Override
            public void onResponse(@NotNull Call<List<ShareEntity>> call,
                                   @NotNull Response<List<ShareEntity>> response) {
                if (!response.isSuccessful()) {
                    Log.d("LoadUserShares", "Load User Shares Failed");
                    return;
                }

                List<ShareEntity> shareEntities = response.body();
                assert shareEntities != null;

                LinearLayoutManager linearLayoutManager =
                        new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(linearLayoutManager);

                DashboardAdapter adapter = new DashboardAdapter(shareEntities);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NotNull Call<List<ShareEntity>> call,
                                  @NotNull Throwable t) {
                Log.d("LoadUserShares", Objects.requireNonNull(t.getLocalizedMessage()));
            }
        });
    }
}