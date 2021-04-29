package com.example.bookrecommend.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookrecommend.R;
import com.example.bookrecommend.databinding.FragmentBookCommentBinding;
import com.example.bookrecommend.pojo.UserBookComment;
import com.example.bookrecommend.sington.HttpStatus;
import com.example.bookrecommend.sington.ServiceSingle;
import com.example.bookrecommend.sington.SingleObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookCommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookCommentFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentBookCommentBinding binding;

    private RecyclerView recyclerView;

    private Long userId;

    private long isbn;

    public BookCommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookCommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookCommentFragment newInstance(String param1, String param2) {
        BookCommentFragment fragment = new BookCommentFragment();
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
        binding = FragmentBookCommentBinding
                .inflate(inflater, container, false);

        recyclerView = binding.bookComments;

        assert getArguments() != null;
        isbn = getArguments().getLong("isbn");

        userId = SingleObject.getUserInfoEntity().getUserId();

        loadBookComments(isbn);

        binding.addCommentButton.setOnClickListener(v -> showAddCommentDialog());

        return binding.getRoot();
    }

    private void loadBookComments(@NonNull Long isbn) {
        Call<List<UserBookComment>> commentsCall = ServiceSingle
                .getBookService().bookComments(isbn);

        commentsCall.enqueue(new Callback<List<UserBookComment>>() {
            @Override
            public void onResponse(@NotNull Call<List<UserBookComment>> call,
                                   @NotNull Response<List<UserBookComment>> response) {
                if (!response.isSuccessful()) {
                    Log.d("UserBookComments", "Get BookComments Failed");
                    return;
                }

                List<UserBookComment> comments = response.body();
                assert comments != null;

                LinearLayoutManager linearLayoutManager =
                        new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(linearLayoutManager);

                BookCommentAdapter adapter = new BookCommentAdapter(comments);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NotNull Call<List<UserBookComment>> call,
                                  @NotNull Throwable t) {
                Log.d("UserBookComments", Objects.requireNonNull(t.getLocalizedMessage()));
            }
        });
    }

    public void showAddCommentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

//        AddCommentDialogBinding addCommentDialogBinding =
//                DataBindingUtil.inflate(LayoutInflater.from(requireContext()),
//                        R.layout.add_comment_dialog, null, false);

        View dialogView = inflater.inflate(R.layout.add_comment_dialog, null);

//        builder.setView(binding.getRoot());

        builder.setView(dialogView)
                .setPositiveButton(R.string.ok_string, (dialog, which) -> {
                    EditText editText = dialogView.findViewById(R.id.bookCommentInput);

                    UserBookComment comment = new UserBookComment();
                    comment.setUserId(userId);
                    comment.setIsbn(isbn);
                    comment.setCommentId(0);
                    comment.setCommentContent(editText.getText().toString());

                    System.out.println("Comment: " + comment.toString());

                    Call<HttpStatus> addCommentCall = ServiceSingle
                            .getBookService().addComment(comment);

                    addCommentCall.enqueue(new Callback<HttpStatus>() {
                        @Override
                        public void onResponse(@NotNull Call<HttpStatus> call,
                                               @NotNull Response<HttpStatus> response) {
                            if (!response.isSuccessful()) {
                                Log.d("AddBookComment", "Add Book Comment Failed");
                                return;
                            }

                            Log.d("AddBookComment", "Add Book Comment Success.");

                            loadBookComments(isbn);
                        }

                        @Override
                        public void onFailure(@NotNull Call<HttpStatus> call,
                                              @NotNull Throwable t) {
                            Log.d("AddBookComment", Objects.requireNonNull(t.getLocalizedMessage()));
                        }
                    });

                    NavDirections action =
                            BookCommentFragmentDirections
                                    .actionBookCommentFragmentToNavigationBookDetail(isbn);
                    Navigation.findNavController(requireView()).navigate(action);
                })
                .setNegativeButton(R.string.cancel_string, (dialog, which) -> dialog.dismiss())
                .create();
        builder.show();
    }
}