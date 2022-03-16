package com.example.bookrecommend.ui.home;

import android.util.Log;
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
import com.example.bookrecommend.databinding.BookItemViewBinding;
import com.example.bookrecommend.item.BookItemClick;
import com.example.bookrecommend.pojo.BookEntity;
import com.example.bookrecommend.pojo.UserBookBrowse;
import com.example.bookrecommend.sington.HttpStatus;
import com.example.bookrecommend.sington.ServiceSingle;
import com.example.bookrecommend.sington.SingleObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bookrecommend.constant.Tools.isUrl;

public class HomeAdapter
        extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
        implements
            BookItemClick {
    private final List<BookEntity> bookEntities;

    private final View view;

    // 这个页面的视图持有类
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // 该视图的数据绑定对象
        private final BookItemViewBinding binding;

        // 使用构造函数的方式注入对应的数据绑定对象
        public MyViewHolder(@NonNull BookItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Object object) {
            binding.setVariable(BR.bookEntity, object);
            binding.executePendingBindings();
        }
    }

    /*
        通过构造函数注入对应的数据集
     */
    public HomeAdapter(@NonNull List<BookEntity> bookEntities, View view){
        this.bookEntities = bookEntities;
        this.view = view;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());

        BookItemViewBinding binding =
                BookItemViewBinding.inflate(inflater, parent, false);

        return new MyViewHolder(binding);
    }

    /*
        设置对应的 Recycle View 对应的视图属性值
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BookEntity bookEntity = bookEntities.get(position);
        // 设置对应的绑定对象
        holder.binding.setBookEntity(bookEntity);
        holder.binding.setItemClick(this);
        // 使用 Glide 从对应的书籍对象的图像 URL 中加载图像内容到对应的 ImageView 中
        if (isUrl(bookEntity.getImageUrl())) {
            Glide.with(holder.binding.bookImage.getContext())
                    .load(bookEntity.getImageUrl())
//                    .apply(new RequestOptions().override(280, 280))
                    .into(holder.binding.bookImage);
        }
        // 设置当前 BookEntity 对象的作者字符串表示形式
        Tools.setAuthorsString(bookEntity);

        holder.bind(bookEntity);
    }

    @Override
    public int getItemCount() {
        return bookEntities.size();
    }


    @Override
    public void bookItemClick(BookEntity obj) {
        Log.d("BookDetail", "The Book has been clicked");
        UserBookBrowse bookBrowse = new UserBookBrowse();
        bookBrowse.setUserId(SingleObject.getUserInfoEntity().getUserId());
        bookBrowse.setIsbn(obj.getIsbn());

        Call<HttpStatus> call = ServiceSingle.getUserService()
                .updateUserBrowse(bookBrowse);

        call.enqueue(new Callback<HttpStatus>() {
            @Override
            public void onResponse(@NotNull Call<HttpStatus> call,
                                   @NotNull Response<HttpStatus> response) {
                if (!response.isSuccessful()) {
                    Log.d("ToBookDetail", "Record UserBookBrowse failed");
                    return;
                }

                Log.i("ToBookDetail", "Save UserBookBrowse Info Success.");
            }

            @Override
            public void onFailure(@NotNull Call<HttpStatus> call,
                                  @NotNull Throwable t) {
                Log.d("ToBookDetail", Objects.requireNonNull(t.getLocalizedMessage()));
            }
        });

        NavDirections directions =
                HomeFragmentDirections
                        .actionNavigationHomeToNavigationBookDetailFragment(obj.getIsbn());
        Navigation.findNavController(view).navigate(directions);
    }
}
