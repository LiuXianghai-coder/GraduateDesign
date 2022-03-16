package com.example.bookrecommend.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookrecommend.databinding.BookCommentItemViewBinding;
import com.example.bookrecommend.pojo.UserBookComment;

import java.util.List;

public class BookCommentAdapter extends RecyclerView.Adapter<BookCommentAdapter.BookCommentViewHolder> {
    private final List<UserBookComment> commentList;

    public BookCommentAdapter(List<UserBookComment> commentList) {
        this.commentList = commentList;
    }

    public static class BookCommentViewHolder
            extends RecyclerView.ViewHolder {
        // 该视图的数据绑定对象
        private final BookCommentItemViewBinding binding;

        // 使用构造函数的方式注入对应的数据绑定对象
        public BookCommentViewHolder(@NonNull BookCommentItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Object object) {
            binding.setVariable(BR.bookComment, object);
            binding.executePendingBindings();
        }
    }

    @NonNull
    @Override
    public BookCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());

        BookCommentItemViewBinding binding =
                BookCommentItemViewBinding.inflate(inflater, parent, false);

        return new BookCommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookCommentViewHolder holder, int position) {
        UserBookComment comment = commentList.get(position);
        holder.binding.setBookComment(comment);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
