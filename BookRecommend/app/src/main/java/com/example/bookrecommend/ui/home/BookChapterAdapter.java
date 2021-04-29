package com.example.bookrecommend.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookrecommend.R;
import com.example.bookrecommend.databinding.BookChapterItemViewBinding;
import com.example.bookrecommend.pojo.BookChapter;

import java.util.List;

public class BookChapterAdapter extends RecyclerView.Adapter<BookChapterAdapter.BookChapterViewHolder> {
    private final List<BookChapter> bookChapters;

    public BookChapterAdapter(@NonNull List<BookChapter> bookChapters) {
        this.bookChapters = bookChapters;
    }

    // 这个页面的视图持有类
    public static class BookChapterViewHolder extends RecyclerView.ViewHolder {
        // 该视图的数据绑定对象
        private final BookChapterItemViewBinding binding;

        // 使用构造函数的方式注入对应的数据绑定对象
        public BookChapterViewHolder(@NonNull BookChapterItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Object object) {
            binding.setVariable(BR.bookChapter, object);
            binding.executePendingBindings();
        }
    }

    @NonNull
    @Override
    public BookChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());

        BookChapterItemViewBinding binding =
                BookChapterItemViewBinding.inflate(inflater, parent, false);

        return new BookChapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookChapterViewHolder holder, int position) {
        BookChapter bookChapter = bookChapters.get(position);
        setChapterName(bookChapter);

        if (0 == bookChapter.getChapterKind()) {
            holder.binding.chapterName
                    .setTextAppearance(R.style.bookChapterOneKindTextStyle);
        } else if (1 == bookChapter.getChapterKind())
            holder.binding.chapterName
                    .setTextAppearance(R.style.bookChapterOneKindTextStyle);
        else if (2 == bookChapter.getChapterKind())
            holder.binding.chapterName
                    .setTextAppearance(R.style.bookChapterTwoKindTextStyle);
        else if (3 == bookChapter.getChapterKind())
            holder.binding.chapterName
                    .setTextAppearance(R.style.bookChapterThreeKindTextStyle);

        holder.binding.setBookChapter(bookChapter);
    }

    @Override
    public int getItemCount() {
        return bookChapters.size();
    }

    /**
     * 设置当前书籍章节对象的最终章节名称，这是为了使得 BookChapter 中的显示更加合理
     * @param bookChapter ： 待设置的 BookChapter 对象
     */
    private void setChapterName(@NonNull BookChapter bookChapter) {
        if (0 == bookChapter.getChapterKind()) return;
        if (3 < bookChapter.getChapterKind()) return;

        StringBuilder result = new StringBuilder();
        String fillStr = "  ";
        if (1 == bookChapter.getChapterKind()) result.append(fillStr);
        else if (2 == bookChapter.getChapterKind()) result.append(fillStr).append(fillStr);
        else if (3 == bookChapter.getChapterKind()) result.append(fillStr).append(fillStr).append(fillStr);

        result.append(bookChapter.getChapterName());
        bookChapter.setChapterName(result.toString());
    }
}
