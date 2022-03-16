package com.example.bookrecommend.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookrecommend.databinding.BookReviewItemViewBinding;
import com.example.bookrecommend.item.BookReviewClick;
import com.example.bookrecommend.pojo.UserBookReview;
import com.example.bookrecommend.sington.BookReviewView;

import java.util.List;

public class BookReviewAdapter
        extends
        RecyclerView.Adapter<BookReviewAdapter.BookReviewViewHold>
        implements
        BookReviewClick {

    private final List<UserBookReview> reviewList;

    public BookReviewAdapter(List<UserBookReview> reviewList) {
        this.reviewList = reviewList;
    }

    public static class BookReviewViewHold extends RecyclerView.ViewHolder {
        // 该视图的数据绑定对象
        private final BookReviewItemViewBinding binding;

        // 使用构造函数的方式注入对应的数据绑定对象
        public BookReviewViewHold(@NonNull BookReviewItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Object object) {
            binding.setVariable(BR.bookReview, object);
            binding.executePendingBindings();
        }
    }

    @NonNull
    @Override
    public BookReviewViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());

        BookReviewItemViewBinding binding =
                BookReviewItemViewBinding.inflate(inflater, parent, false);

        return new BookReviewAdapter.BookReviewViewHold(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookReviewViewHold holder, int position) {
        UserBookReview review = reviewList.get(position);

        holder.binding.setBookReview(review);
        holder.binding.setBookReviewClick(this);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }


    @Override
    public void bookReviewClick(@NonNull UserBookReview review) {
        NavDirections action =
                BookReviewFragmentDirections
                        .actionBookReviewFragmentToBookReviewDetailFragment(review.getBookReviewId());

        Navigation.findNavController(BookReviewView.getReviewView()).navigate(action);
    }
}
