package com.example.bookrecommend.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookrecommend.databinding.FragmentBookChapterBinding;
import com.example.bookrecommend.pojo.BookChapter;
import com.example.bookrecommend.sington.ServiceSingle;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookChapterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookChapterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentBookChapterBinding binding;

    private RecyclerView recyclerView;

    public BookChapterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookChapterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookChapterFragment newInstance(String param1, String param2) {
        BookChapterFragment fragment = new BookChapterFragment();
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
        binding = FragmentBookChapterBinding
                .inflate(inflater, container, false);

        recyclerView = binding.bookChapters;

        assert getArguments() != null;
        Long isbn = getArguments().getLong("isbn");

        loadBookChapters(isbn);

        return binding.getRoot();
    }

    /**
     * 加载对应的章节信息
     * @param isbn ： 加载的书籍的 ISBN 号
     */
    private void loadBookChapters(@NonNull Long isbn) {
        Call<List<BookChapter>> chaptersCall = ServiceSingle
                .getBookService().findBookChaptersByIsbn(isbn);

        chaptersCall.enqueue(new Callback<List<BookChapter>>() {
            @Override
            public void onResponse(@NotNull Call<List<BookChapter>> call,
                                   @NotNull Response<List<BookChapter>> response) {
                if (!response.isSuccessful()) {
                    Log.d("BookChapters", "Load BookChapters Failed");
                    return;
                }

                List<BookChapter> chapters = response.body();
                assert chapters != null;
//                Log.d("BookChapters", Arrays.toString(chapters.toArray()));

                LinearLayoutManager linearLayoutManager =
                        new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(linearLayoutManager);

                BookChapterAdapter chapterAdapter = new BookChapterAdapter(chapters);
                recyclerView.setAdapter(chapterAdapter);
            }

            @Override
            public void onFailure(@NotNull Call<List<BookChapter>> call,
                                  @NotNull Throwable t) {
                Log.d("BookChapters", Objects.requireNonNull(t.getLocalizedMessage()));
            }
        });
    }
}