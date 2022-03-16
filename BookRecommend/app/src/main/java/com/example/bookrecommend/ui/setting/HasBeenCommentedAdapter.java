package com.example.bookrecommend.ui.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookrecommend.constant.Tools;
import com.example.bookrecommend.databinding.ItemCommentedBookViewBinding;
import com.example.bookrecommend.item.BookItemClick;
import com.example.bookrecommend.pojo.BookEntity;

import java.util.List;

public class HasBeenCommentedAdapter
        extends
            RecyclerView.Adapter<HasBeenCommentedAdapter.CommentedViewHolder>
        implements
            BookItemClick {
    private final List<BookEntity> bookEntities;

    private final Context context;

    private final View view;

    public HasBeenCommentedAdapter(List<BookEntity> bookEntities,
                                   Context context,
                                   View view) {
        this.bookEntities = bookEntities;
        this.context = context;
        this.view = view;
    }

    // 这个页面的视图持有类
    public static class CommentedViewHolder extends RecyclerView.ViewHolder {
        // 该视图的数据绑定对象
        private final ItemCommentedBookViewBinding binding;

        // 使用构造函数的方式注入对应的数据绑定对象
        public CommentedViewHolder(@NonNull ItemCommentedBookViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Object object) {
            binding.setVariable(BR.bookEntity, object);
            binding.executePendingBindings();
        }
    }

    @NonNull
    @Override
    public CommentedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());

        ItemCommentedBookViewBinding binding =
                ItemCommentedBookViewBinding.inflate(inflater, parent, false);

        return new HasBeenCommentedAdapter.CommentedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentedViewHolder holder, int position) {
        BookEntity bookEntity = bookEntities.get(position);

        holder.binding.setBookEntity(bookEntity);
        holder.binding.setItemClick(this);

        if (Tools.isUrl(bookEntity.getImageUrl())) {
            Glide.with(holder.binding.commentedBookImage.getContext())
                    .load(bookEntity.getImageUrl())
                    .into(holder.binding.commentedBookImage);
        }

        Tools.setAuthorsString(bookEntity);
        holder.bind(bookEntity);
    }

    @Override
    public int getItemCount() {
        return bookEntities.size();
    }

    @Override
    public void bookItemClick(BookEntity obj) {
        NavDirections action = HasBeenCommentedFragmentDirections
                .actionHasBeenCommentedFragmentToNavigationBookDetail(obj.getIsbn());

        Navigation.findNavController(view).navigate(action);
    }
}
