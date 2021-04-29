package com.example.bookrecommend.ui.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookrecommend.databinding.FragmentHasBeenCollectedBinding;
import com.example.bookrecommend.db.UserInfoEntity;
import com.example.bookrecommend.pojo.BookEntity;
import com.example.bookrecommend.service.BookService;
import com.example.bookrecommend.sington.ServiceSingle;
import com.example.bookrecommend.sington.SingleObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HasBeenCollectedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HasBeenCollectedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HasBeenCollectedFragment() {
        // Required empty public constructor
    }

    private FragmentHasBeenCollectedBinding binding;

    private RecyclerView recyclerView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HasBeenCollectedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HasBeenCollectedFragment newInstance(String param1, String param2) {
        HasBeenCollectedFragment fragment = new HasBeenCollectedFragment();
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
        binding = FragmentHasBeenCollectedBinding
                .inflate(inflater, container, false);

        recyclerView = binding.collectedBookList;

        loadData();

        return binding.getRoot();
    }

    private void loadData() {
        BookService bookService = ServiceSingle.getBookService();
        UserInfoEntity obj = SingleObject.getUserInfoEntity();

        Call<List<BookEntity>> booksCall = bookService.collectedBook(obj.getUserId());
        booksCall.enqueue(new Callback<List<BookEntity>>() {
            @Override
            public void onResponse(@NotNull Call<List<BookEntity>> call,
                                   @NotNull Response<List<BookEntity>> response) {
                if (!response.isSuccessful()) {
                    Log.d("UserInfo", "Get User Books Failed");
                    return;
                }

                List<BookEntity> bookEntities = response.body();

                LinearLayoutManager linearLayoutManager =
                        new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(linearLayoutManager);

                HasBeenCollectedAdapter collectedAdapter = new HasBeenCollectedAdapter(bookEntities,
                        requireContext(), requireView());
                recyclerView.setAdapter(collectedAdapter);
            }

            @Override
            public void onFailure(@NotNull Call<List<BookEntity>> call,
                                  @NotNull Throwable t) {
                Log.d("LoadData", t.getLocalizedMessage());
            }
        });
    }
}