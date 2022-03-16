package com.example.bookrecommend.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.bookrecommend.constant.ConstVariable;
import com.example.bookrecommend.databinding.FragmentShareDetailBinding;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShareDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShareDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentShareDetailBinding binding;

    public ShareDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShareDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShareDetailFragment newInstance(String param1, String param2) {
        ShareDetailFragment fragment = new ShareDetailFragment();
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
        binding = FragmentShareDetailBinding
                .inflate(inflater, container, false);

        assert getArguments() != null;
        long shareId = getArguments().getLong("shareId");

        binding.shareDetailWebView.loadUrl(ConstVariable.SHARE_DETAIL_BASIC_URL + shareId);

        return binding.getRoot();
    }
}