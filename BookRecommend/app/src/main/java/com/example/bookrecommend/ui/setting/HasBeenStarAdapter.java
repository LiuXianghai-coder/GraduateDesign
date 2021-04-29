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
import com.example.bookrecommend.databinding.ItemStarBookViewBinding;
import com.example.bookrecommend.item.BookItemClick;
import com.example.bookrecommend.pojo.BookEntity;

import java.util.List;

public class HasBeenStarAdapter
            extends
                RecyclerView.Adapter<HasBeenStarAdapter.StaredViewHolder>
            implements
                BookItemClick {

    private final List<BookEntity> bookEntities;

    private final Context context;

    private final View view;

    public HasBeenStarAdapter(List<BookEntity> bookEntities,
                              Context context,
                              View view) {
        this.bookEntities = bookEntities;
        this.context = context;
        this.view = view;
    }

    // 这个页面的视图持有类
    public static class StaredViewHolder extends RecyclerView.ViewHolder {
        // 该视图的数据绑定对象
        private final ItemStarBookViewBinding binding;

        // 使用构造函数的方式注入对应的数据绑定对象
        public StaredViewHolder(@NonNull ItemStarBookViewBinding binding) {
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
    public StaredViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());

        ItemStarBookViewBinding binding =
                ItemStarBookViewBinding.inflate(inflater, parent, false);

        return new HasBeenStarAdapter.StaredViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StaredViewHolder holder, int position) {
        BookEntity bookEntity = bookEntities.get(position);

        holder.binding.setBookEntity(bookEntity);
        holder.binding.setItemClick(this);

        if (Tools.isUrl(bookEntity.getImageUrl())) {
            Glide.with(holder.binding.starBookImage.getContext())
                    .load(bookEntity.getImageUrl())
                    .into(holder.binding.starBookImage);
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
        NavDirections action = HasBeenStarFragmentDirections
                .actionHasBeenStarFragmentToNavigationBookDetail(obj.getIsbn());

        Navigation.findNavController(view).navigate(action);
    }
}
