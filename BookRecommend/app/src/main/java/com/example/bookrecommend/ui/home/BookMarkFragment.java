package com.example.bookrecommend.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.bookrecommend.R;
import com.example.bookrecommend.databinding.FragmentBookMarkBinding;
import com.example.bookrecommend.pojo.UserBookMark;
import com.example.bookrecommend.sington.ServiceSingle;
import com.example.bookrecommend.sington.SingleObject;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookMarkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookMarkFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentBookMarkBinding binding;

    private Short score = 0;

    public BookMarkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookMarkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookMarkFragment newInstance(String param1, String param2) {
        BookMarkFragment fragment = new BookMarkFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookMarkBinding
                .inflate(inflater, container, false);

        assert getArguments() != null;
        long isbn = getArguments().getLong("isbn");

        binding.oneScoreImage.setOnClickListener(v -> {
            binding.oneScoreImage.setImageResource(R.drawable.ic_mark_star);
            binding.twoScoreImage.setImageResource(R.drawable.ic_no_mark_star);
            binding.threeScoreImage.setImageResource(R.drawable.ic_no_mark_star);
            binding.fourScoreImage.setImageResource(R.drawable.ic_no_mark_star);
            binding.fiveScoreImage.setImageResource(R.drawable.ic_no_mark_star);

            score = 1;
        });

        binding.twoScoreImage.setOnClickListener(v -> {
            binding.oneScoreImage.setImageResource(R.drawable.ic_mark_star);
            binding.twoScoreImage.setImageResource(R.drawable.ic_mark_star);
            binding.threeScoreImage.setImageResource(R.drawable.ic_no_mark_star);
            binding.fourScoreImage.setImageResource(R.drawable.ic_no_mark_star);
            binding.fiveScoreImage.setImageResource(R.drawable.ic_no_mark_star);

            score = 2;
        });

        binding.threeScoreImage.setOnClickListener(v -> {
            binding.oneScoreImage.setImageResource(R.drawable.ic_mark_star);
            binding.twoScoreImage.setImageResource(R.drawable.ic_mark_star);
            binding.threeScoreImage.setImageResource(R.drawable.ic_mark_star);
            binding.fourScoreImage.setImageResource(R.drawable.ic_no_mark_star);
            binding.fiveScoreImage.setImageResource(R.drawable.ic_no_mark_star);

            score = 3;
        });

        binding.fourScoreImage.setOnClickListener(v -> {
            binding.oneScoreImage.setImageResource(R.drawable.ic_mark_star);
            binding.twoScoreImage.setImageResource(R.drawable.ic_mark_star);
            binding.threeScoreImage.setImageResource(R.drawable.ic_mark_star);
            binding.fourScoreImage.setImageResource(R.drawable.ic_mark_star);
            binding.fiveScoreImage.setImageResource(R.drawable.ic_no_mark_star);

            score = 4;
        });

        binding.fiveScoreImage.setOnClickListener(v -> {
            binding.oneScoreImage.setImageResource(R.drawable.ic_mark_star);
            binding.twoScoreImage.setImageResource(R.drawable.ic_mark_star);
            binding.threeScoreImage.setImageResource(R.drawable.ic_mark_star);
            binding.fourScoreImage.setImageResource(R.drawable.ic_mark_star);
            binding.fiveScoreImage.setImageResource(R.drawable.ic_mark_star);

            score = 5;
        });

        binding.addBookMarkButton.setOnClickListener(v -> {
            Long userId = SingleObject.getUserInfoEntity().getUserId();

            UserBookMark mark = new UserBookMark();

            mark.setUserId(userId);
            mark.setIsbn(isbn);
            mark.setScore(score);
            mark.setComment(binding.markContentText.getText().toString());

            System.out.println("Mark: " + mark.toString());

            Call<String> saveBookMark = ServiceSingle.getBookService()
                    .saveBookMark(mark);
            saveBookMark.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NotNull Call<String> call,
                                       @NotNull Response<String> response) {
                    if (!response.isSuccessful()) {
                        Log.d("SaveBookMark", "Save user Book Mark Failed");
                        return;
                    }

                    Log.d("SaveBookMark", "Save user Book Mark Success.");
                    NavDirections action =
                            BookMarkFragmentDirections
                                    .actionBookMarkFragmentToNavigationBookDetail(isbn);
                    Navigation.findNavController(v).navigate(action);
                }

                @Override
                public void onFailure(@NotNull Call<String> call,
                                      @NotNull Throwable t) {
                    Log.d("SaveBookMark", Objects.requireNonNull(t.getLocalizedMessage()));
                }
            });
        });

        return binding.getRoot();
    }
}