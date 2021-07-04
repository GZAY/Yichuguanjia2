package com.example.yichuguanjia2.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.yichuguanjia2.GlideRoundTransform;
import com.example.yichuguanjia2.MainActivity;
import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.base.Image;
import com.example.yichuguanjia2.sql.MyDatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private List<Image> imageList;
    private Context context;
    private Dialog dialog;
    Image image;
    ImageView mImageView;
    Bitmap myBitmap;
    String newFilePath;
    Boolean flag = false;
    String title;
    int num;
    private static SQLiteDatabase db;
    ContentValues values = new ContentValues();
    public RecyclerViewAdapter(Context context, List<Image> images){
        this.context = context;
        imageList = images;
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context,"ImagePathX.db",null,4);
        db = dbHelper.getWritableDatabase();
    }
    // 利用接口 -> 给RecyclerView设置点击事件
    private ItemClickListener mItemClickListener ;
    public interface ItemClickListener{
        public void onItemClick(int position) ;
    }
    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener ;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        image = imageList.get(i);

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

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        myBitmap = null;
                        try {
                            myBitmap = Glide.with(context).asBitmap().load(imageList.get(i).getImageUrl()).centerCrop().into(500, 500).get();
                            title = imageList.get(i).getTitle();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mImageView = getImageView(myBitmap);
                        callback();
                    }
                    void callback(){
                        System.out.println("子线程执行结束");
                        flag=true;
                    }
                });
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                init();
                dialog.show();
            }
        });
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

    private void init() {
        //大图所依附的dialog
        dialog = new Dialog(context, R.style.AlertDialog_AppCompat_Light_);
        if((mImageView.getParent())!=null)
            ((ViewGroup) mImageView.getParent()).removeView(mImageView);
        dialog.setContentView(mImageView);
        //大图的点击事件（点击让他消失）
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //大图的长按监听
        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //弹出的“保存图片”的Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(new String[]{context.getResources().getString(R.string.save_picture)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveCroppedImage(((BitmapDrawable) mImageView.getDrawable()).getBitmap());
                        values.put("path",newFilePath);
                        db.insert("ImagePath16",null,values);
                        values.clear();
                        num++;
                        Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                return true;
            }
        });

    }
    //保存图片
    private void saveCroppedImage(Bitmap bmp) {
        File file = new File("/sdcard/myFolder");
        if (!file.exists())
            file.mkdir();

        file = new File("/sdcard/temp.jpg".trim());
        String fileName = file.getName();
        String sName = fileName.substring(fileName.lastIndexOf("."));
        // /sdcard/myFolder/temp_cropped.jpg
        newFilePath = "/sdcard/myFolder" + "/" + title + sName;
        file = new File(newFilePath);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //动态的ImageView
    private ImageView getImageView(Bitmap resource){
        ImageView iv = new ImageView(context);
        //宽高
        iv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //设置Padding
        iv.setPadding(20,20,20,20);
        //imageView设置图片
        iv.setImageBitmap(resource);
        return iv;
    }
}