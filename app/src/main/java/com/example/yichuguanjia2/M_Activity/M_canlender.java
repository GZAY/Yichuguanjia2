package com.example.yichuguanjia2.M_Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.util.LocaleData;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;

import com.cocosw.bottomsheet.BottomSheet;
import com.example.yichuguanjia2.MultiplePicture.ImgFileListActivity;
import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.base.BaseActivity;
import com.example.yichuguanjia2.sql.MyDatabaseHelper;
import com.example.yichuguanjia2.w_Activity.w_clothes;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class M_canlender extends BaseActivity {
    MaterialCalendarView calendarView;
    String Date;
    TextView textView;
    String time;
    String tbname;
    @Override
    protected int attachLayoutRes() {
        return R.layout.m_canlender;
    }

    @Override
    protected void initViews() {
        calendarView.addDecorator(new TodayDecorator());
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int day = date.getDay();
                int month = date.getMonth();
                int year = date.getYear();
                time = year+""+month+""+day;
                db.execSQL("create table if not exists '" + time + "_date'(id integer primary key autoincrement,path text)");
                tbname = "'" + time + "_date'";
                Date = year + "年" + month + "月" + day + "日";
                textView.setText(Date);
                Cursor cursor = db.query(tbname,null,null,null,null,null,null);
                imageItem = new ArrayList<HashMap<String, Object>>();
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("itemImage", R.drawable.icon_addpic_unfocused);
                imageItem.add(map);
                if(cursor.moveToFirst()){
                    do{
                        //遍历Cursor对象，取出数据
                        String path = cursor.getString(cursor.getColumnIndex("path"));
                        Bitmap bm;
                        bm = getBitmapFromUri(M_canlender.this, getImageContentUri(M_canlender.this, path));
                        HashMap<String, Object> maps = new HashMap<String, Object>();
                        maps.put("itemImage", bm);
                        imageItem.add(maps);
                    }while (cursor.moveToNext());
                    cursor.close();
                }
                UIUpgrade();
            }
        });

    }

    @Override
    protected void initFindViewById() {
        calendarView = findViewById(R.id.calendarView);
        textView = findViewById(R.id.calendarData);
    }
    private class TodayDecorator implements DayViewDecorator {

        private final CalendarDay today;
        private final Drawable backgroundDrawable;

        public TodayDecorator() {
            today = CalendarDay.today();
            backgroundDrawable = getResources().getDrawable(R.drawable.today_circle_background);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return today.equals(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(backgroundDrawable);
        }
    }

    ImageView imageView;
    private GridView gridView1;                   //网格显示缩略图
    private final int IMAGE_OPEN = 1;        //打开图片标记
    private String pathImage;                       //选择图片路径
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器
    ArrayList<String> list;
    ArrayList<String> listfile=new ArrayList<String>();
    private static int saveCode = 0;
    int version = 1;
    private static SQLiteDatabase db;
    static int flag = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView back = findViewById(R.id.calender_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView add = findViewById(R.id.clothes_add);
        list = new ArrayList<>();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        //获取控件对象
        gridView1 = findViewById(R.id.gridView_calender);
        /*
       /*
         * 载入默认图片添加图片加号
         * 通过适配器实现
         * SimpleAdapter参数imageItem为数据源 R.layout.griditem_addpic为布局
         */
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", R.drawable.icon_addpic_unfocused);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this, imageItem, R.layout.item_image, new String[]{"itemImage"}, new int[]{R.id.imageView1}){
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (convertView == null) {
                    imageView = view.findViewById(R.id.imageView1);
                    //imageView = new ImageView(MainActivity.this);
                    //设置显示图片的大小
                    DisplayMetrics dm = getResources().getDisplayMetrics();
                    int w = dm.widthPixels;
                    imageView.setLayoutParams(new GridView.LayoutParams(w/3-50, w/3-50));
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setPadding(2, 2, 2, 2);
                } else {
                    imageView = (ImageView) convertView;
                }
                return imageView;
            }
        };
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView img = findViewById(R.id.imageView1);
                    img.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView1.setAdapter(simpleAdapter);
        /*
         * 监听GridView点击事件
         * 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
         */
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) { //点击图片位置为+ 0对应0张图片
                    Toast.makeText(M_canlender.this, "添加图片", Toast.LENGTH_SHORT).show();
                    if (ActivityCompat.checkSelfPermission(M_canlender.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(M_canlender.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    }
                    resumeCode = 3;
                    Intent intent = new Intent();
                    intent.setClass(M_canlender.this, ImgFileListActivity.class);
                    startActivityForResult(intent,7);
                } else {
                    dialog(position);
                }

            }
        });
        Date t = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMdd");

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"Diary.db",null,1);
        db = dbHelper.getWritableDatabase();
        db.execSQL("create table if not exists '" + df.format(t) + "_date'(id integer primary key autoincrement,path text)");
        tbname = "'"+df.format(t) + "_date"+"'";
        Cursor cursor = db.query(tbname,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                //遍历Cursor对象，取出数据
                String path = cursor.getString(cursor.getColumnIndex("path"));
                Bitmap bm;
                bm = getBitmapFromUri(M_canlender.this, getImageContentUri(M_canlender.this, path));
                HashMap<String, Object> maps = new HashMap<String, Object>();
                maps.put("itemImage", bm);
                imageItem.add(maps);
            }while (cursor.moveToNext());
            cursor.close();
        }
        UIUpgrade();
    }
    /**
     * 分界线——相机启动的权限申请
     */
    // 申请相机权限的requestCode
    private static int resumeCode;

    //用于保存拍照图片的uri
    private Uri mCameraUri;

    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if (resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {
            resumeCode = requestCode;
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                //查询选择图片
                Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA},
                        null,
                        null,
                        null);
                //返回 没找到选择图片
                if (null == cursor) {
                    return;
                }
                //光标移动至开头 获取图片路径
                cursor.moveToFirst();
                pathImage = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                list.add(pathImage); //这里就是对应的所以图片
            }
        }

        //多选相册
        if(requestCode == 7){
            //接收多选图片的地址
            if(data!=null){
                Bundle bundle = data.getExtras();
                if (bundle!=null) {
                    if (bundle.getStringArrayList("files")!=null) {
                        listfile= bundle.getStringArrayList("files");
                    }
                }
            }
        }
    }

    /**
     * 分界线--从相册添加
     */
    //刷新图片
    @Override
    protected void onResume() {
        super.onResume();
        if (listfile.size()!=0 || !TextUtils.isEmpty(pathImage) || saveCode == 9) {
            flag = 1;
            Bitmap bm;
            ContentValues values = new ContentValues();
            if(resumeCode == 3){
                for(int i=0;i<listfile.size();i++) {
                    values.put("path",listfile.get(i));
                    db.insert(tbname,null,values);
                    values.clear();
                    bm = getBitmapFromUri(M_canlender.this, getImageContentUri(M_canlender.this, listfile.get(i)));
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("itemImage", bm);
                    imageItem.add(map);
                }

            }else if(resumeCode == IMAGE_OPEN){
                HashMap<String, Object> map = new HashMap<String, Object>();
                values.put("path",pathImage);
                db.insert(tbname,null,values);
                values.clear();
                bm = getBitmapFromUri(M_canlender.this, getImageContentUri(M_canlender.this,pathImage));
                map.put("itemImage", bm);
                imageItem.add(map);
            }
            resumeCode = 0;
            UIUpgrade();
        }
    }
    public static Uri getImageContentUri(Context context, String path) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { path }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            // 如果图片不在手机的共享图片数据库，就先把它插入。
            if (new File(path).exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, path);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    // 通过uri加载图片
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */

    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(M_canlender.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String pos = position + "";
                db.execSQL("CREATE TABLE tempt(id INTEGER PRIMARY KEY,path text)");
                db.execSQL("insert into tempt select null,path from "+tbname);
                db.execSQL("DROP TABLE "+tbname);
                db.execSQL("ALTER TABLE tempt RENAME TO "+tbname);
                db.delete(tbname,"id=?",new String[]{pos});
                flag = 1;
                imageItem.remove(position);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    void UIUpgrade(){
        simpleAdapter = new SimpleAdapter(this, imageItem, R.layout.item_image, new String[]{"itemImage"}, new int[]{R.id.imageView1}){
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (convertView == null) {
                    imageView = view.findViewById(R.id.imageView1);
                    //设置显示图片的大小
                    DisplayMetrics dm = getResources().getDisplayMetrics();
                    int w = dm.widthPixels;
                    imageView.setLayoutParams(new GridView.LayoutParams(w/3-50, w/3-50));
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setPadding(2, 2, 2, 2);
                } else {
                    imageView = (ImageView) convertView;
                }
                return imageView;
            }
        };
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView1.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();
        //刷新后释放防止手机休眠后自动添加
        pathImage = null;
    }
}
