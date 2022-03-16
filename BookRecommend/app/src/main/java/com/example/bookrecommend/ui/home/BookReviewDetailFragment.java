package com.example.bookrecommend.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.bookrecommend.constant.ConstVariable;
import com.example.bookrecommend.databinding.FragmentBookReviewDetailBinding;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookReviewDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookReviewDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentBookReviewDetailBinding binding;

    public BookReviewDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookReviewDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookReviewDetailFragment newInstance(String param1, String param2) {
        BookReviewDetailFragment fragment = new BookReviewDetailFragment();
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
        binding = FragmentBookReviewDetailBinding
                .inflate(inflater, container, false);

        assert getArguments() != null;
        long reviewId = getArguments().getLong("reviewId");

        binding.bookReviewDetail.loadUrl(ConstVariable.BOOK_REVIEW_BASIC_URL + reviewId);

        return binding.getRoot();
    }
}