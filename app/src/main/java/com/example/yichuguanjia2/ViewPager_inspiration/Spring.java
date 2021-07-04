package com.example.yichuguanjia2.ViewPager_inspiration;


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
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import androidx.fragment.app.Fragment;

import com.cocosw.bottomsheet.BottomSheet;
import com.example.yichuguanjia2.M_Activity.M_canlender;
import com.example.yichuguanjia2.MultiplePicture.ImgFileListActivity;
import com.example.yichuguanjia2.R;
import com.example.yichuguanjia2.base.BaseFragment;
import com.example.yichuguanjia2.sql.MyDatabaseHelper;
import com.example.yichuguanjia2.w_Activity.w_clothes;
import com.example.yichuguanjia2.w_Activity.w_type;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class Spring extends BaseFragment {
    ImageView imageView;
    private GridView gridView1;                   //网格显示缩略图
    private final int IMAGE_OPEN = 1;        //打开图片标记
    private String pathImage;                       //选择图片路径
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器
    ArrayList<String> list;
    ArrayList<String> listfile=new ArrayList<String>();
    private static int saveCode = 0;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    String tableName = "ImagePath";
    int version = 16;
    private static SQLiteDatabase db;
    static int number;
    static int position;
    static int flag = 0;

    /**
     * 分界线——相机启动的权限申请
     */
    // 申请相机权限的requestCode
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static int resumeCode;
    //用于保存拍照图片的uri
    private Uri mCameraUri;

    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;

    // 是否是Android 10以上手机
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //打开图片
        if (resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {
            resumeCode = requestCode;
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                //查询选择图片
                Cursor cursor = getActivity().getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA},
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
    public void onResume() {
        super.onResume();
        if (listfile.size()!=0) {
            flag = 1;
            Bitmap bm;
            ContentValues values = new ContentValues();
            if(resumeCode == 3){
                for(int i=0;i<listfile.size();i++) {
                    values.put("path",listfile.get(i));
                    db.insert(tableName+version,null,values);
                    values.clear();
                    number++;
                    bm = getBitmapFromUri(getActivity(), getImageContentUri(getActivity(), listfile.get(i)));
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("itemImage", bm);
                    imageItem.add(map);
                }

            }else if(resumeCode == IMAGE_OPEN){
                HashMap<String, Object> map = new HashMap<String, Object>();
                values.put("path",pathImage);
                db.insert(tableName+version,null,values);
                values.clear();
                number++;
                bm = getBitmapFromUri(getActivity(), getImageContentUri(getActivity(),pathImage));
                map.put("itemImage", bm);
                imageItem.add(map);
            } else {
                HashMap<String, Object> map = new HashMap<String, Object>();
                values.put("path",getPathFromUri(getActivity(),mCameraUri));
                db.insert(tableName+version,null,values);
                values.clear();
                number++;
                bm = getBitmapFromUri(getActivity(), mCameraUri);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String pos = position + "";
                db.execSQL("CREATE TABLE tempt(id INTEGER PRIMARY KEY,path text)");
                db.execSQL("insert into tempt select null,path from "+tableName+version);
                db.execSQL("DROP TABLE "+tableName+version);
                db.execSQL("ALTER TABLE tempt RENAME TO "+tableName+version);
                db.delete(tableName+version,"id=?",new String[]{pos});
                number--;
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

    //Uri转路径
    @SuppressLint("NewApi")
    public static String getPathFromUri(final Context context, final Uri uri) {
        if (uri == null) {
            return null;
        }
        // 判斷是否為Android 4.4之後的版本
        final boolean after44 = Build.VERSION.SDK_INT >= 19;
        if (after44 && DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是Android 4.4之後的版本，而且屬於文件URI
            final String authority = uri.getAuthority();
            // 判斷Authority是否為本地端檔案所使用的
            if ("com.android.externalstorage.documents".equals(authority)) {
                // 外部儲存空間
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] divide = docId.split(":");
                final String type = divide[0];
                if ("primary".equals(type)) {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/").concat(divide[1]);
                    return path;
                } else {
                    String path = "/storage/".concat(type).concat("/").concat(divide[1]);
                    return path;
                }
            } else if ("com.android.providers.downloads.documents".equals(authority)) {
                // 下載目錄
                final String docId = DocumentsContract.getDocumentId(uri);
                if (docId.startsWith("raw:")) {
                    final String path = docId.replaceFirst("raw:", "");
                    return path;
                }
                final Uri downloadUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                String path = queryAbsolutePath(context, downloadUri);
                return path;
            } else if ("com.android.providers.media.documents".equals(authority)) {
                // 圖片、影音檔案
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] divide = docId.split(":");
                final String type = divide[0];
                Uri mediaUri = null;
                if ("image".equals(type)) {
                    mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    mediaUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    return null;
                }
                mediaUri = ContentUris.withAppendedId(mediaUri, Long.parseLong(divide[1]));
                String path = queryAbsolutePath(context, mediaUri);
                return path;
            }
        } else {
            // 如果是一般的URI
            final String scheme = uri.getScheme();
            String path = null;
            if ("content".equals(scheme)) {
                // 內容URI
                path = queryAbsolutePath(context, uri);
            } else if ("file".equals(scheme)) {
                // 檔案URI
                path = uri.getPath();
            }
            return path;
        }
        return null;
    }
    public static String queryAbsolutePath(final Context context, final Uri uri) {
        final String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                return cursor.getString(index);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    void UIUpgrade(){
        simpleAdapter = new SimpleAdapter(getActivity(), imageItem, R.layout.item_image, new String[]{"itemImage"}, new int[]{R.id.imageView1}){
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (convertView == null) {
                    imageView = view.findViewById(R.id.imageView1);
                    //设置显示图片的大小
                    DisplayMetrics dm = getResources().getDisplayMetrics();
                    int w = dm.widthPixels;
                    imageView.setLayoutParams(new GridView.LayoutParams(w/3, w/3));
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setPadding(1, 1, 1, 1);
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
    
    @Override
    protected void initViews() {
        list = new ArrayList<>();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        //锁定屏幕
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        //获取控件对象
        gridView1 = findViewById(R.id.gridView_collection);
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
        simpleAdapter = new SimpleAdapter(getContext(), imageItem, R.layout.item_image, new String[]{"itemImage"}, new int[]{R.id.imageView1}){
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (convertView == null) {
                    imageView = view.findViewById(R.id.imageView1);
                    //imageView = new ImageView(MainActivity.this);
                    //设置显示图片的大小
                    DisplayMetrics dm = getResources().getDisplayMetrics();
                    int w = dm.widthPixels;
                    imageView.setLayoutParams(new GridView.LayoutParams(w/3, w/3));
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setPadding(1, 1, 1, 1);
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
                    Toast.makeText(getActivity(), "添加图片", Toast.LENGTH_SHORT).show();
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    }
                    resumeCode = 3;
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ImgFileListActivity.class);
                    startActivityForResult(intent,7);
                } else {
                    dialog(position);
                }

            }
        });
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getActivity(),"ImagePathX.db",null,4);
        db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("ImagePath16",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                //遍历Cursor对象，取出数据
                String path = cursor.getString(cursor.getColumnIndex("path"));
                Bitmap bm;
                bm = getBitmapFromUri(getActivity(), getImageContentUri(getContext(), path));
                if(bm == null)
                    bm = BitmapFactory.decodeFile(path);
                HashMap<String, Object> maps = new HashMap<String, Object>();
                maps.put("itemImage", bm);
                imageItem.add(maps);
            }while (cursor.moveToNext());
            cursor.close();
        }
        UIUpgrade();
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.winter_viewpager;
    }
}