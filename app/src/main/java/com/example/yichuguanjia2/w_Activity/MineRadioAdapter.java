package com.example.yichuguanjia2.w_Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yichuguanjia2.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Decoder.BASE64Decoder;

/**
 * Created by guohao on 2017/9/6.
 */

public class MineRadioAdapter extends RecyclerView.Adapter<MineRadioAdapter.ViewHolder> {

    private static final int MYLIVE_MODE_CHECK = 0;
    int mEditMode = MYLIVE_MODE_CHECK;
    private Context context;
    /**
     * 把addList()获得的数据添加过来
     */
    private ArrayList<DataModel> mList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public MineRadioAdapter(Context context) {
        this.context = context;
    }


    public void notifyAdapter(ArrayList<DataModel> myLiveList, boolean isAdd) {
        if (!isAdd) {
            this.mList = myLiveList;
        } else {
            this.mList.addAll(myLiveList);
        }
        notifyDataSetChanged();
    }

    public ArrayList<DataModel> getMyLiveList() {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        return mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.m_case_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DataModel myLive = mList.get(holder.getAdapterPosition());
        if (mList.size()>0){
            Glide.with(holder.itemView.getContext()).load(GenerateImage(mList.get(position).getImage())).into(holder.clothesImage);
            holder.e_brand.setText(mList.get(position).getBrand());
            holder.e_color.setText(mList.get(position).getColor());
            holder.e_scene.setText(mList.get(position).getScene());
            holder.e_season.setText(mList.get(position).getSeason());
            holder.e_size.setText(mList.get(position).getSize());
            holder.e_type.setText(mList.get(position).getType());
            holder.clothesTime.setText(dealDateFormat(mList.get(position).getTime()));
        }
        if (mEditMode == MYLIVE_MODE_CHECK) {
            holder.mCheckBox.setVisibility(View.GONE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);

            if (myLive.isSelect()) {
                holder.mCheckBox.setImageResource(R.mipmap.ic_checked);
            } else {
                holder.mCheckBox.setImageResource(R.mipmap.ic_uncheck);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClickListener(holder.getAdapterPosition(), mList);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClickListener(int pos, ArrayList<DataModel> myLiveList);
    }
    public void setEditMode(int editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView e_brand;
        private TextView e_color;
        private TextView e_scene;
        private TextView e_season;
        private TextView e_type;
        private TextView e_size;
        private TextView clothesTime;
        private ImageView clothesImage;
        private ImageView mCheckBox;
        public ViewHolder(View itemView) {
            super(itemView);
            e_brand = itemView.findViewById(R.id.e_brand);
            e_color = itemView.findViewById(R.id.e_color);
            e_scene = itemView.findViewById(R.id.e_scene);
            e_season = itemView.findViewById(R.id.e_season);
            e_type = itemView.findViewById(R.id.e_type);
            e_size = itemView.findViewById(R.id.e_size);
            clothesTime = itemView.findViewById(R.id.clothesTime);
            clothesImage = itemView.findViewById(R.id.clothesImage);
            mCheckBox = itemView.findViewById(R.id.check_box);
//            e_brand.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
//            e_color.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
//            e_scene.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
//            e_season.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
//            e_type.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
//            e_size.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        }
    }

    // 对字节数组字符串进行Base64解码并生成图片
    public static Bitmap GenerateImage(String imgStr) {
        Bitmap bitmap = null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpeg图片
            String imgFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/clothesImage.png";// 新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            bitmap= BitmapFactory.decodeFile(imgFilePath);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 时间戳转换成日期格式字符串
     */
    public static String dealDateFormat(String oldDate) {
        oldDate = oldDate.substring(0,oldDate.length() - 6);
        Date date = null;
        DateFormat df2 = null;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            date = df.parse(oldDate);
            df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df2.format(date);
    }
}
