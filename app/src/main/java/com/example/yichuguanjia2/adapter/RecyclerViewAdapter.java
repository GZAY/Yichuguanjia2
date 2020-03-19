package com.example.yichuguanjia2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.yichuguanjia2.GlideRoundTransform;
import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.base.Image;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private List<Image> imageList;
    private Context context;
    public RecyclerViewAdapter(Context context, List<Image> images){
        this.context = context;
        imageList = images;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item,viewGroup,false);
        context = viewGroup.getContext();
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Image image = imageList.get(i);

        //填充数据
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round) //预加载图片
                .error(R.drawable.ic_launcher_foreground) //加载失败图片
                .priority(Priority.HIGH) //优先级
                .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
                .transform(new GlideRoundTransform(5)); //圆角
        //Picasso.with(context).load(list.get(position).getImage()).into(holder.imageView);
        holder.imageTitle.setText(image.getTitle());
        Glide.with(context).load(image.getImageUrl()).into(holder.imageView);
        //由于需要实现瀑布流的效果,所以就需要动态的改变控件的高度了
        //ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();
        //holder.imageView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView imageTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.clothes_image);
            imageTitle = itemView.findViewById(R.id.clothes_title);

        }
    }
}