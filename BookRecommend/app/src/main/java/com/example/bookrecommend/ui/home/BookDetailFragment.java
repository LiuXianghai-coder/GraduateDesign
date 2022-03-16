package com.example.bookrecommend.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.bookrecommend.R;
import com.example.bookrecommend.databinding.FragmentBookDetailBinding;
import com.example.bookrecommend.db.UserInfoEntity;
import com.example.bookrecommend.pojo.Author;
import com.example.bookrecommend.pojo.BookChapter;
import com.example.bookrecommend.pojo.BookDetail;
import com.example.bookrecommend.pojo.UserBook;
import com.example.bookrecommend.pojo.UserBookCollection;
import com.example.bookrecommend.pojo.UserBookStar;
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
 * Use the {@link BookDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentBookDetailBinding binding;

    private final UserBookStar bookStar = new UserBookStar();

    private final UserBookCollection collection = new UserBookCollection();

    private long isbn;

    public BookDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static BookDetailFragment newInstance(String param1, String param2) {
        BookDetailFragment fragment = new BookDetailFragment();
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
        binding = FragmentBookDetailBinding.inflate(inflater, container, false);
        assert getArguments() != null;
        isbn = getArguments().getLong("isbn");

        long userId = SingleObject.getUserInfoEntity().getUserId();

        bookStar.setUserId(userId);

        collection.setUserId(userId);

        binding.bookDetailStar.setOnClickListener(v -> {
            bookStar.setIsbn(isbn);
            Call<UserBookStar> starCall = ServiceSingle
                    .getBookService().updateStar(bookStar);
            starCall.enqueue(new Callback<UserBookStar>() {
                @Override
                public void onResponse(@NotNull Call<UserBookStar> call,
                                       @NotNull Response<UserBookStar> response) {
                    if (!response.isSuccessful()) {
                        Log.d("UserBookStar", "UserBook Star Failed");
                        return;
                    }

                    UserBookStar userBookStar = response.body();
                    assert userBookStar != null;

                    // 更新单例 UserBookStar 的属性值
                    bookStar.setStar(userBookStar.getStar());
                    bookStar.setStarDate(userBookStar.getStarDate());
                    bookStar.setIsbn(userBookStar.getIsbn());

                    System.out.println("bookStar: " + bookStar.toString());

                    reloadAfterStarClick(bookStar);
                }

                @Override
                public void onFailure(@NotNull Call<UserBookStar> call,
                                      @NotNull Throwable t) {
                    Log.d("UserBookStar", Objects.requireNonNull(t.getLocalizedMessage()));
                }
            });
        });

        binding.bookDetailCollection.setOnClickListener(v -> {
            collection.setIsbn(isbn);
            Call<UserBookCollection> collectionCall = ServiceSingle
                    .getBookService().updateCollection(collection);
            collectionCall.enqueue(new Callback<UserBookCollection>() {
                @Override
                public void onResponse(@NotNull Call<UserBookCollection> call,
                                       @NotNull Response<UserBookCollection> response) {
                    if (!response.isSuccessful()) {
                        Log.d("rUserBookCollection", "Update UserBookCollection Failed");
                        return;
                    }

                    UserBookCollection bookCollection = response.body();
                    assert bookCollection != null;

                    collection.setCollection(bookCollection.getCollection());
                    collection.setCollectDate(bookCollection.getCollectDate());
                    collection.setCollectDate(bookCollection.getCollectDate());

                    reloadAfterCollectionClick(collection);
                }

                @Override
                public void onFailure(@NotNull Call<UserBookCollection> call,
                                      @NotNull Throwable t) {
                }
            });
        });

        binding.bookDetailComment.setOnClickListener(v -> {
            NavDirections action =
                    BookDetailFragmentDirections
                            .actionNavigationBookDetailToBookCommentFragment(isbn);
            Navigation.findNavController(requireView()).navigate(action);
        });

        binding.bookDetailChapters.setOnClickListener(v -> {
            NavDirections action =
                    BookDetailFragmentDirections
                            .actionNavigationBookDetailToBookChapterFragment(isbn);
            Navigation.findNavController(requireView()).navigate(action);
        });

        binding.bookScoreLayout.setOnClickListener(v -> {
            NavDirections action =
                    BookDetailFragmentDirections
                    .actionNavigationBookDetailToBookMarkFragment(isbn);

            Navigation.findNavController(v).navigate(action);
        });

        binding.bookDetailReviews.setOnClickListener(v -> {
            NavDirections action =
                    BookDetailFragmentDirections
                    .actionNavigationBookDetailToBookReviewFragment(isbn);

            Navigation.findNavController(v).navigate(action);
        });

        loadBookDetailInfo(isbn);
        loadUserBook(isbn);

        return binding.getRoot();
    }

    // 加载书籍的详细信息
    private void loadBookDetailInfo(@NonNull Long isbn) {
        Call<BookDetail> bookDetailCall = ServiceSingle
                .getBookService().findBookByIsbn(isbn);
        // 加载书籍的详细信息
        bookDetailCall.enqueue(new Callback<BookDetail>() {
            @Override
            public void onResponse(@NotNull Call<BookDetail> call,
                                   @NotNull Response<BookDetail> response) {
                if (!response.isSuccessful()) {
                    Log.d("BookDetail", "Get BookDetail Object failed");
                    return;
                }

                BookDetail bookDetail = response.body();
                assert bookDetail != null;
                Glide.with(requireView()).load(bookDetail.getBookImage())
                        .into(binding.bookDetailHeadImage);
                binding.bookDetailName.setText(bookDetail.getBookName());
                StringBuilder authors = new StringBuilder();
                for (Author author : bookDetail.getAuthorList()) {
                    authors.append(author.getAuthorName()).append(" ");
                }

                binding.bookDetailAuthorName.setText(authors.toString());
                binding.bookDetailPublisherInfo.setText(bookDetail.getPublisherInfo());
                binding.bookDetailScore.setText(String.valueOf(bookDetail.getBookScore()));
                binding.fiveScoreText.setText(String.valueOf(bookDetail.getFiveScoreNum()));
                binding.fourScoreText.setText(String.valueOf(bookDetail.getFourScoreNum()));
                binding.threeScoreText.setText(String.valueOf(bookDetail.getThreeScoreNum()));
                binding.twoScoreText.setText(String.valueOf(bookDetail.getTwoScoreNum()));
                binding.oneScoreText.setText(String.valueOf(bookDetail.getOneScoreNum()));
                binding.bookDetailIntro.setText(bookDetail.getIntroduction());

                StringBuilder authorIntro = new StringBuilder();
                for (Author author : bookDetail.getAuthorList()) {
                    authorIntro.append(author.getAuthorName()).append("\n")
                            .append(author.getAuthorIntroduction()).append("\n");
                }

                binding.bookDetailAuthorIntro.setText(authorIntro.toString());
            }

            @Override
            public void onFailure(@NotNull Call<BookDetail> call,
                                  @NotNull Throwable t) {
                Log.d("BookDetail", Objects.requireNonNull(t.getLocalizedMessage()));
            }
        });

        Call<List<BookChapter>> chaptersCall = ServiceSingle
                .getBookService().findBookChaptersByIsbn(isbn);
        chaptersCall.enqueue(new Callback<List<BookChapter>>() {
            @Override
            public void onResponse(@NotNull Call<List<BookChapter>> call,
                                   @NotNull Response<List<BookChapter>> response) {
                if (!response.isSuccessful()) {
                    Log.d("BookChapters", "Get BookChapters Failed");
                    return;
                }

                List<BookChapter> bookChapters = response.body();
                assert bookChapters != null;

                if (bookChapters.size() > 2) {
                    binding.bookDetailChapterOne.setText(bookChapters.get(0).getChapterName());
                    binding.bookDetailChapterTwo.setText(bookChapters.get(1).getChapterName());
                    return;
                }

                if (bookChapters.size() > 0)
                    binding.bookDetailChapterOne.setText(bookChapters.get(0).getChapterName());
            }

            @Override
            public void onFailure(@NotNull Call<List<BookChapter>> call,
                                  @NotNull Throwable t) {
                Log.d("BookChapters", Objects.requireNonNull(t.getLocalizedMessage()));
            }
        });
    }

    // 加载当前用户与书籍的对应关系
    private void loadUserBook(@NonNull Long isbn) {
        UserInfoEntity userInfoEntity = SingleObject.getUserInfoEntity();
        Call<UserBook> userBookCall = ServiceSingle.getBookService()
                .userBookInfo(isbn, userInfoEntity.getUserId());
        userBookCall.enqueue(new Callback<UserBook>() {
            @Override
            public void onResponse(@NotNull Call<UserBook> call,
                                   @NotNull Response<UserBook> response) {
                if (!response.isSuccessful()) {
                    Log.d("UserBook", "Get UserBook Object Failed");
                    return;
                }

                UserBook userBook = response.body();

                assert userBook != null;
                if (userBook.getStar()) {
                    binding.bookDetailStar.setColorFilter(requireContext()
                            .getResources().getColor(R.color.is_star_color));
                    bookStar.setStar(true);
                } else {
                    binding.bookDetailStar.setColorFilter(requireContext()
                            .getResources().getColor(R.color.no_star_color));
                    bookStar.setStar(false);
                }

                if (userBook.getCollection()) {
                    binding.bookDetailCollection.setColorFilter(requireContext()
                            .getResources().getColor(R.color.is_collection_color));
                    collection.setCollection(true);
                } else {
                    binding.bookDetailCollection.setColorFilter(requireContext()
                            .getResources().getColor(R.color.no_collection_color));
                    collection.setCollection(false);
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserBook> call,
                                  @NotNull Throwable t) {
                Log.d("UserBook", Objects.requireNonNull(t.getLocalizedMessage()));
            }
        });
    }

    private void reloadAfterStarClick(@NonNull UserBookStar userBookStar) {
        if (userBookStar.getStar()) {
            binding.bookDetailStar.setColorFilter(requireContext()
                    .getResources().getColor(R.color.is_star_color));
            return;
        }

        binding.bookDetailStar.setColorFilter(requireContext()
                .getResources().getColor(R.color.no_star_color));
    }

    private void reloadAfterCollectionClick(@NonNull UserBookCollection collection) {
        if (collection.getCollection()) {
            binding.bookDetailCollection.setColorFilter(requireContext()
                    .getResources().getColor(R.color.is_collection_color));
            return;
        }

        binding.bookDetailCollection.setColorFilter(requireContext()
                .getResources().getColor(R.color.no_collection_color));
    }
}