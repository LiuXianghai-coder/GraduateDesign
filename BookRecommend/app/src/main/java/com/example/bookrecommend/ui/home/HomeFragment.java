package com.example.bookrecommend.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookrecommend.MainActivity;
import com.example.bookrecommend.constant.Tools;
import com.example.bookrecommend.databinding.FragmentHomeBinding;
import com.example.bookrecommend.db.UserInfoEntity;
import com.example.bookrecommend.entity.Configuration;
import com.example.bookrecommend.entity.ResultObject;
import com.example.bookrecommend.entity.SearchContent;
import com.example.bookrecommend.entity.SearchPage;
import com.example.bookrecommend.pojo.BookEntity;
import com.example.bookrecommend.sington.ContentType;
import com.example.bookrecommend.sington.HomeViewSingle;
import com.example.bookrecommend.sington.ServiceSingle;
import com.example.bookrecommend.sington.SingleObject;
import com.example.bookrecommend.sington.TimeTools;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bookrecommend.entity.Configuration.defaultSearchContent;
import static com.example.bookrecommend.entity.Configuration.defaultSearchPage;
import static com.example.bookrecommend.entity.Configuration.jacksonJsonMapper;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;

    // 基础书籍对象的循环视图
    private RecyclerView recyclerView;

    // Jackson Json 转换对象
    private final ObjectMapper mapper = jacksonJsonMapper();

    // 与该 Fragment 绑定的数据绑定对象
    private FragmentHomeBinding binding;

    // 初始 SearchPage 对象
    private final SearchPage searchPage = defaultSearchPage();

    // 初始 SearchContent 对象
    private final SearchContent searchContent = defaultSearchContent();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SneakyThrows
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding
                .inflate(inflater, container, false);

        UserInfoEntity obj = SingleObject.getUserInfoEntity();

        // 设置主页视图的引用
        HomeViewSingle.setHomeView(container);

        String maturityTime = obj.getMaturityTime();
        Long maturityTimeStamp = TimeTools.parseLocalDateTimeToUnixTimeStamp(maturityTime);
        Long currentTimeStamp = TimeTools.currentDateTimeUnixTimeStamp();

        if (obj.getRecordId() > 0 && obj.getIsUpdate() == 0) {
            MainActivity.setBottomNavigationVisibility(View.VISIBLE);

            // 设置初始页面显示
            setDefaultBookPage(obj.getUserId());
        } else if (currentTimeStamp > maturityTimeStamp || obj.getIsUpdate() > 0) {
            // 如果当前的时间大与预设的过期时间，则需要重新登录, 如果已经被更新，也需要重新登录
            MainActivity.setBottomNavigationVisibility(View.INVISIBLE);
            NavDirections action = HomeFragmentDirections
                    .actionNavigationHomeToLoginFragment(obj);
            Navigation.findNavController(container).navigate(action);
        }

        recyclerView = binding.bookEntityList;

        binding.searchImageButton.setOnClickListener(v -> {
            String inputText = binding.searchInputText.getText().toString();
            searchContent.setContent(inputText);

            if (Tools.isIsbn(inputText)) searchContent.setType(ContentType.ISBN);
            else searchContent.setType(ContentType.MIX);

            // 重新加载书籍页面信息列表
            reloadBookPage(searchContent, searchPage);
        });

        return binding.getRoot();
    }

    /**
     * 设置默认初始书籍显示列表界面
     */
    private void setDefaultBookPage(Long userId) {
        try {
            // 传入相关请求参数
            Call<List<BookEntity>> listCall = ServiceSingle.getBookService().defaultBookPage(userId);

            listCall.enqueue(new Callback<List<BookEntity>>() {
                @Override
                public void onResponse(@NotNull Call<List<BookEntity>> call,
                                       @NotNull Response<List<BookEntity>> response) {
                    if (!response.isSuccessful()) {
                        Log.i("HomeFragment", "response failed");
                        Log.i("HomeFragment", response.message());
                        return;
                    }

                    List<BookEntity> bookEntities = response.body();
                    assert bookEntities != null;

                    LinearLayoutManager linearLayoutManager =
                            new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    HomeAdapter homeAdapter = new HomeAdapter(bookEntities, requireView());
                    recyclerView.setAdapter(homeAdapter);
                }

                @Override
                public void onFailure(@NotNull Call<List<BookEntity>> call,
                                      @NotNull Throwable t) {
                    Log.i("HomeFragment", "OnFailure: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.d("ObjectToJson", "Convert Object to json throw exception.");
        }
    }

    /**
     * 重新加载书籍页面
     *
     * @param searchContent ： 传入的搜索内容对象，由于设置对应的查询条件
     * @param searchPage    ： 搜索的页面获取范湖以及排序的列
     */
    private void reloadBookPage(@NonNull SearchContent searchContent,
                                @NonNull SearchPage searchPage) {
        ObjectMapper mapper = Configuration.jacksonJsonMapper();

        try {
            // 传入相关请求参数
            Call<ResultObject<BookEntity>> resultObjectCall = ServiceSingle.getSearchService()
                    .searchBook(mapper.writeValueAsString(searchPage),
                            mapper.writeValueAsString(searchContent));

            resultObjectCall.enqueue(new Callback<ResultObject<BookEntity>>() {
                @Override
                public void onResponse(@NotNull Call<ResultObject<BookEntity>> call,
                                       @NotNull Response<ResultObject<BookEntity>> response) {
                    if (!response.isSuccessful()) {
                        Log.i("HomeFragment", "response failed");
                        Log.i("HomeFragment", response.message());
                        return;
                    }

                    ResultObject<BookEntity> resultObject = response.body();
                    assert resultObject != null;

                    LinearLayoutManager linearLayoutManager =
                            new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    HomeAdapter homeAdapter = new HomeAdapter(resultObject.getObjectList(), requireView());
                    recyclerView.setAdapter(homeAdapter);
                }

                @Override
                public void onFailure(@NotNull Call<ResultObject<BookEntity>> call,
                                      @NotNull Throwable t) {
                    Log.i("HomeFragment", "OnFailure: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.d("ObjectToJson", "Convert Object to json throw exception.");
        }
    }
}