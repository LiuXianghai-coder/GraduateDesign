package com.example.bookrecommend.ui.dashboard;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookrecommend.databinding.ShareItemViewBinding;
import com.example.bookrecommend.item.UserShareClick;
import com.example.bookrecommend.pojo.ShareEntity;
import com.example.bookrecommend.sington.ShareView;

import java.util.List;

import static com.example.bookrecommend.constant.Tools.isUrl;

/**
 * @Author : LiuXianghai
 * @Date : 2021/02/25 9:57
 * @Product :  BookRecommend
 */
public class DashboardAdapter
        extends
            RecyclerView.Adapter<DashboardAdapter.HolderAdapter>
        implements
            UserShareClick {
    // 该视图适配器内显示的对象列表
    private final List<ShareEntity> shareEntities;

    /*
        该对应的适配器显示的数据对象类
     */
    public static class HolderAdapter extends RecyclerView.ViewHolder {
        // 该视图对应的数据绑定对象
        private final ShareItemViewBinding binding;

        public HolderAdapter(@NonNull ShareItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /*
            设置对应的绑定对象
         */
        void bind(ShareEntity object) {
            if (object.isStar()) {
                // 如果当前登录的用户已经点赞了的话， 则设置点赞图片填充色为红色
                binding.starImage.setColorFilter(Color.rgb(255, 0, 0));
            }

            if (object.isCollection()) {
                // 如果当前登录用户已经收藏了这个动态的话， 则设置收藏图标颜色为黄色
                binding.collectionImage.setColorFilter(Color.rgb(255, 255, 0));
            }

            binding.setVariable(BR.shareEntity, object);
            binding.executePendingBindings();
        }
    }

    /*
        初始化时需要将要加载的数据对象加载进来
     */
    public DashboardAdapter(@NonNull List<ShareEntity> shareEntities) {
        this.shareEntities = shareEntities;
    }

    @NonNull
    @Override
    public HolderAdapter onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {
        LayoutInflater inflater =
                LayoutInflater.from(parent.getContext());

        ShareItemViewBinding binding =
                ShareItemViewBinding.inflate(inflater, parent, false);

        return new HolderAdapter(binding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull HolderAdapter holder,
            int position) {
        ShareEntity shareEntity = shareEntities.get(position);
        holder.bind(shareEntity);

        holder.binding.setUserShareClick(this);

        // 首先判断图像地址是不是合法的地址格式
        String imageUrl = shareEntity.getImageUrl();
        if (!isUrl(imageUrl)) return;

        // 从对应的图片地址中加载图片数据到指定的图片组件中
        Glide.with(holder.binding.shareImage.getContext())
                .load(imageUrl)
                .into(holder.binding.shareImage);

        holder.binding.setShareEntity(shareEntity);
    }

    @Override
    public int getItemCount() {
        return this.shareEntities.size();
    }

    @Override
    public void userShareClick(ShareEntity shareEntity) {
        Log.d("ShareEntity", "Will Navigation To ShareDetail");
        NavDirections action =
                DashboardFragmentDirections
                        .actionNavigationDashboardToShareDetailFragment(shareEntity.getShareId());
        Navigation.findNavController(ShareView.getShareView()).navigate(action);
    }
}
