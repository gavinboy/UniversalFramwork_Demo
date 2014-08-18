package com.universal.demo.download;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.universal.demo.download.DownInfo.DOWN_STATUS;
import com.universal.framwork.BaseActivity;
import com.universal.framwork.annotation.InjectView;
import com.universal.framwork.demo.R;
import com.universal.framwork.http.download.DownloadManager;
import com.universal.framwork.util.NetWorkUtil.NetType;
import com.universal.framwork.util.ToastUtil;

public class MultiThreadDownloadActivity extends BaseActivity
{
  private static final String TAG = "MultiThreadDownloadActivity";
  @InjectView(id=R.id.download_list)
  private ListView mListView;
  @InjectView(id=R.id.btn_add,click="addDownTask")
  private Button btnAdd;
  
  private DownLoadAdapter mAdapter;
  private int urlIndex=0;
  
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.multi_thread_download);
    mAdapter=new DownLoadAdapter(this, mListView);
    mListView.setAdapter(mAdapter);
  }
  
  public void addDownTask(View view)
  {
    if(urlIndex>4)
    {
      ToastUtil.AlertMessageInCenter("没有可以添加的任务了...");
      return;
    }
    else
    {
      DownInfo info=new DownInfo();
      info.setStatus(DOWN_STATUS.WATING.ordinal());
      info.setUrl(DownURL.downurls[urlIndex++]);
      mAdapter.addData(info);
    }
   
  }
  
  @Override
  protected void onPause()
  {
    super.onPause();
    DownloadManager.getDownloadManager().close();
  }
  
  @Override
  protected void onDestroy()
  {
    super.onDestroy();
  }
  
  @Override
  public void onConnect(NetType netType)
  {
    //DownloadManager.getDownloadManager().con
  }
  
  @Override
  public void onDisConnect()
  {
    Log.v("yzy", "net--->onDisConnect");
    DownloadManager.getDownloadManager().pauseAllHandler();
  }
}
