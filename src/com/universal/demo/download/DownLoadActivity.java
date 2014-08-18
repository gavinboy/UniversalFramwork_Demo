package com.universal.demo.download;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.universal.framwork.BaseActivity;
import com.universal.framwork.annotation.InjectView;
import com.universal.framwork.demo.R;
import com.universal.framwork.http.AsyncHttpClient;
import com.universal.framwork.http.FileHttpResponseHandler;
import com.universal.framwork.util.LogUtil;
import com.universal.framwork.util.ToastUtil;

public class DownLoadActivity extends BaseActivity
{
  private static final String TAG = "DownLoadActivity";
  
  private AsyncHttpClient mHttp=new AsyncHttpClient();
  @InjectView(id=R.id.btn_add_download,click="onClickAddDown")
  private Button btnAddDown;
  
  @InjectView(id=R.id.btn_cancel_download,click="onClickCancelDown")
  private Button btnCancelDown;
  
  @InjectView(id=R.id.tv_down_size)
  private TextView tv_progress;
  @InjectView(id=R.id.pbar)
  private ProgressBar mProgress;
  @InjectView(id=R.id.tv_down_speed)
  private TextView tv_speed;
  
  @InjectView(id=R.id.down_info)
  private View mDownInfo;
  
  
  private FileHttpResponseHandler handler=null;
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.download);
  }
  
  public void onClickAddDown(View view)
  {
    handler=new FileHttpResponseHandler(DownURL.downurls[0], "/mnt/sdcard/yzy", "qiyi.apk")
    {
      @Override
      public void onProgress(long totalSize, long currentSize, float speed)
      {
        //tv_result.setText();
        if(Math.abs(totalSize-currentSize)<=100)
        {
          currentSize=totalSize;
        }
        tv_progress.setText(String.format("%4.1f M/%4.1f M", currentSize/1024/1024.0f,totalSize/1024/1024.0f));
        mProgress.setProgress((int)((mProgress.getMax()*((float)currentSize/totalSize))));
        tv_speed.setText(String.format("%4.1f KB/s", speed));
      }
      
      
      @Override
      public void onSuccess(int statusCode, byte[] binaryData)
      {
        Toast.makeText(DownLoadActivity.this, new String(binaryData), 2000).show();
        mProgress.setProgress(mProgress.getMax());
        tv_speed.setText("");
      }
      
      @Override
      public void onFailure(Throwable error, byte[] content)
      {
        tv_speed.setText(new String(content));
      }
      
      @Override
      public void onStart()
      {
        super.onStart();
        mDownInfo.setVisibility(View.VISIBLE);
        
      }
      
      
      
    };
    mHttp.download(this, DownURL.downurls[0],handler);
  }
  
  public void onClickCancelDown(View view)
  {
    handler.setInterrupt(true);
    //mHttp.cancelRequests(this, true);
    //mHttp.threadPool.shutdownNow();
    ToastUtil.AlertMessageInBottom("Cancel");
  }
  
  @Override
  protected void onDestroy()
  {
    LogUtil.d("yzy", "DownLoadActivity onDestroy");
    if(handler!=null)
    handler.setInterrupt(true);
    super.onDestroy();
   
  }
}
