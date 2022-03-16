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
import com.example.bookrecommend.databinding.ItemCollectedBookViewBinding;
import com.example.bookrecommend.item.BookItemClick;
import com.example.bookrecommend.pojo.BookEntity;

import java.util.List;

public class HasBeenCollectedAdapter
        extends
            RecyclerView.Adapter<HasBeenCollectedAdapter.CollectedViewHolder>
        implements
            BookItemClick {
    private final List<BookEntity> bookEntities;

    private final Context context;

    private final View view;

    public HasBeenCollectedAdapter(List<BookEntity> bookEntities,
                                   Context context,
                                   View view) {
        this.bookEntities = bookEntities;
        this.context = context;
        this.view = view;
    }

    // 这个页面的视图持有类
    public static class CollectedViewHolder extends RecyclerView.ViewHolder {
        // 该视图的数据绑定对象
        private final ItemCollectedBookViewBinding binding;

        // 使用构造函数的方式注入对应的数据绑定对象
        public CollectedViewHolder(@NonNull ItemCollectedBookViewBinding binding) {
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
    public CollectedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());

        ItemCollectedBookViewBinding binding =
                ItemCollectedBookViewBinding.inflate(inflater, parent, false);

        return new HasBeenCollectedAdapter.CollectedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectedViewHolder holder, int position) {
        BookEntity bookEntity = bookEntities.get(position);

        holder.binding.setBookEntity(bookEntity);
        holder.binding.setItemClick(this);

        if (Tools.isUrl(bookEntity.getImageUrl())) {
            Glide.with(holder.binding.collectedBookImage.getContext())
                    .load(bookEntity.getImageUrl())
                    .into(holder.binding.collectedBookImage);
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
        NavDirections action = HasBeenCollectedFragmentDirections
                .actionHasBeenCollectedFragmentToNavigationBookDetail(obj.getIsbn());

        Navigation.findNavController(view).navigate(action);
    }
}
