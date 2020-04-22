package com.example.yichuguanjia2.MultiplePicture;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.yichuguanjia2.spl_clothes.w_clothes3;
import com.example.yichuguanjia2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class
ImgFileListActivity extends Activity implements OnItemClickListener{
	Bundle bundle;
	ListView listView;
	Util util;
	ImgFileListAdapter listAdapter;
	List<FileTraversal> locallist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imgfilelist);
		listView= findViewById(R.id.listView1);
		util=new Util(this);
		locallist=util.LocalImgFileList();
		List<HashMap<String, String>> listdata=new ArrayList<HashMap<String,String>>();
		Bitmap[] bitmap = null;
		if (locallist!=null) {
			bitmap=new Bitmap[locallist.size()];
			for (int i = 0; i < locallist.size(); i++) {
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("filecount", locallist.get(i).filecontent.size()+"张");
				map.put("imgpath", (locallist.get(i).filecontent.get(0)));
				map.put("filename", locallist.get(i).filename);
				listdata.add(map);
			}
		}
		listAdapter=new ImgFileListAdapter(this, listdata);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(this);
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent=new Intent(this,ImgsActivity.class);
		Bundle bundle=new Bundle();
		bundle.putParcelable("data", locallist.get(arg2));
		intent.putExtras(bundle);
		startActivityForResult(intent,7);
		//finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//多选相册
		if(resultCode == 7){
			Intent intent =new Intent(this, w_clothes3.class);
			setResult(7, data);
			finish();
		}
	}
}
