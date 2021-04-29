package com.example.bookrecommend.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookrecommend.databinding.FragmentBookReviewBinding;
import com.example.bookrecommend.pojo.UserBookReview;
import com.example.bookrecommend.sington.BookReviewView;
import com.example.bookrecommend.sington.ServiceSingle;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookReviewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentBookReviewBinding binding;

    private RecyclerView recyclerView;

    public BookReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookReviewFragment newInstance(String param1, String param2) {
        BookReviewFragment fragment = new BookReviewFragment();
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
        binding = FragmentBookReviewBinding
                .inflate(inflater, container, false);

        assert getArguments() != null;
        long isbn = getArguments().getLong("isbn");

        BookReviewView.setReviewView(container);

        recyclerView = binding.bookReviewList;

        loadBookReviews(isbn);

        binding.createBookReview.setOnClickListener(v -> {
            assert getArguments() != null;
            long isbn1 = getArguments().getLong("isbn");

            NavDirections action =
                    BookReviewFragmentDirections
                            .actionBookReviewFragmentToCreateBookReviewFragment(isbn1);
            Navigation.findNavController(v).navigate(action);
        });

        return binding.getRoot();
    }

    private void loadBookReviews(long isbn) {
        Call<List<UserBookReview>> reviewCall = ServiceSingle
                .getBookService().findUserBookReviewsByIsbn(isbn);

        reviewCall.enqueue(new Callback<List<UserBookReview>>() {
            @Override
            public void onResponse(@NotNull Call<List<UserBookReview>> call,
                                   @NotNull Response<List<UserBookReview>> response) {
                if (!response.isSuccessful()) {
                    Log.d("UserBookReviews", "Get UserBook Reviews Failed");
                    return;
                }

                List<UserBookReview> reviews = response.body();
                assert reviews != null;

                LinearLayoutManager linearLayoutManager =
                        new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(linearLayoutManager);

                BookReviewAdapter adapter = new BookReviewAdapter(reviews);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NotNull Call<List<UserBookReview>> call,
                                  @NotNull Throwable t) {
                Log.d("UserBookReviews", Objects.requireNonNull(t.getLocalizedMessage()));
            }
        });
    }
}