package com.universal.demo;

import java.lang.ref.WeakReference;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.universal.demo.db.DBActivity;
import com.universal.demo.download.DownLoadActivity;
import com.universal.demo.download.MultiThreadDownloadActivity;
import com.universal.demo.http.HttpActivity;
import com.universal.demo.imgs.ImageActivity;
import com.universal.demo.job.JobActivity;
import com.universal.framwork.BaseActivity;
import com.universal.framwork.XApplication;
import com.universal.framwork.annotation.InjectView;
import com.universal.framwork.demo.R;
import com.universal.framwork.util.ToastUtil;

public class MainActivity extends BaseActivity
{
  private static final String TAG = "MainActivity";
  public static final int MSG_TYPE_CLICK_BACK=0X001;
  private Handler mHandler;
  public boolean isFirstBack=true;
  
  //----------------Button----------
  @InjectView(id=R.id.btn_imgs,click="onClickImg")
  private Button btnImages;
  @InjectView(id=R.id.btn_db,click="onClickDb")
  private Button btnDB;
  @InjectView(id=R.id.btn_job,click="onClickJob")
  private Button btnJobManager;
  @InjectView(id=R.id.btn_http,click="onClickHttp")
  private Button btnHttp;
  @InjectView(id=R.id.btn_down,click="onClickDownLoad")
  private Button btnDownLoad;
  @InjectView(id=R.id.btn_multi_down,click="onClickMultiDownLoad")
  private Button btnMultiDownLoad;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.activity_main);
    mHandler=new BackHandler(this);
  }
  
  public void onClickImg(View view)
  {
    //ToastUtil.AlertMessageInCenter("Start Images Activity");
    Intent intent=new Intent(this,ImageActivity.class);
    this.startActivity(intent);
  }
  
  public void onClickDb(View view)
  {
    Intent intent=new Intent(this,DBActivity.class);
    this.startActivity(intent);
  }
  
  public void onClickJob(View view)
  {
    Intent intent=new Intent(this,JobActivity.class);
    this.startActivity(intent);
  }
  
  public void onClickHttp(View view)
  {
    Intent intent=new Intent(this,HttpActivity.class);
    this.startActivity(intent);
  }
  
  public void onClickDownLoad(View view)
  {
    Intent intent=new Intent(this,DownLoadActivity.class);
    this.startActivity(intent);
  }
  
  public void onClickMultiDownLoad(View view)
  {
    Intent intent=new Intent(this,MultiThreadDownloadActivity.class);
    this.startActivity(intent);
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  //---处理 连续两次返回退出应用逻辑-----start------
  public static class BackHandler extends Handler
  {
    WeakReference<MainActivity> ref;
    public BackHandler(MainActivity activity)
    {
      ref=new WeakReference<MainActivity>(activity);
    }
    
    @Override
    public void handleMessage(Message msg) 
    {
      switch(msg.what)
      {
        case MSG_TYPE_CLICK_BACK:
          MainActivity activity=ref.get();
          if(activity!=null)
          {
            activity.isFirstBack=true;
          }
          break;
      }
    }
  }
  
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) 
  {
    //如果按下的是返回键
    if(keyCode==KeyEvent.KEYCODE_BACK)
    {
      if(isFirstBack)
      {
        isFirstBack=false;
        ToastUtil.AlertMessageInCenter("连续返回即退出应用");
        //两秒内连续两次back键就是退出应用
        mHandler.sendEmptyMessageDelayed(MSG_TYPE_CLICK_BACK, 2000);
      }else
      {
        //完全退出应用
        XApplication.getInstance().exitApp(false);
      }
    }
    return true;
  }
//---处理 连续两次返回退出应用逻辑-----end------
}
