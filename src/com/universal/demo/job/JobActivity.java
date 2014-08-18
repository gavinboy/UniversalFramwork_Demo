package com.universal.demo.job;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.universal.framwork.BaseActivity;
import com.universal.framwork.annotation.InjectView;
import com.universal.framwork.demo.R;
import com.universal.framwork.job.Job.Listener;
import com.universal.framwork.job.JobManager;
import com.universal.framwork.job.Params;

public class JobActivity extends BaseActivity
{
  private static final String TAG = "JobActivity";
  private ProgressDialog dialog;
  
  @InjectView(id=R.id.start,click="onClickStartJob")
  private Button btnStartJob;
  
  @InjectView(id=R.id.startDelay,click="onClickStartDelayJob")
  private Button btnStartDelayJob;
  
  @InjectView(id=R.id.tvresult)
  private TextView tvResult;
  
  @InjectView(id=R.id.tvresultDelay)
  private TextView tvDelayResult;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.job);
  }
  
  public void onClickStartJob(View view)
  {
    DownLoadJob downJob=new DownLoadJob(new Listener<String>()
    {

      @Override
      public void onSuccess(String result)
      {
        tvResult.setText(result);
        Log.v("job", "downjob is onSuccess");
        if(dialog!=null)
        {
          dialog.dismiss();
        }
      }

      @Override
      public void onCancel()
      {
        Log.v("job", "downjob is onCancel");
        if(dialog!=null)
        {
          dialog.dismiss();
        }
        
      }

      @Override
      public void onStart()
      {
        Log.v("job", "downjob is onStart");
        if(dialog==null)
        {
          dialog=new ProgressDialog(JobActivity.this);
          dialog.setMessage("数据加载中...");
        }
        if(!dialog.isShowing())
        {
          dialog.show();
        }
      }

      @Override
      public void onFailure(Exception e)
      {
        tvResult.setText("failure");
        Log.v("job", "downjob is onFailure");
        if(dialog!=null)
        {
          dialog.dismiss();
        }
      }
    });
    
    JobManager.get().addJob(downJob);
    
  }
  
  public void onClickStartDelayJob(View view)
  {
    //对于延时消息，一般不需要显示LoadingDialog
    Params mParams=new Params();
    mParams.delayInMs(5*1000);
    DownLoadJob downJob=new DownLoadJob(mParams, new Listener<String>(){

      @Override
      public void onSuccess(String result)
      {
        tvDelayResult.setText(result);
        Log.v("job", "downjob is onSuccess");
      }

      @Override
      public void onCancel()
      {
        Log.v("job", "downjob is onCancel");
      }

      @Override
      public void onStart()
      {
        Log.v("job", "downjob is onStart");
      }

      @Override
      public void onFailure(Exception e)
      {
        Log.v("job", "downjob is onFailure and reason is -->"+e.getMessage());
      }
      
    });
    
    JobManager.get().addJob(downJob);
    
  }
}
