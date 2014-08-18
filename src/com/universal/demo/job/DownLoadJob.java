package com.universal.demo.job;

import android.util.Log;

import com.universal.framwork.job.Job;
import com.universal.framwork.job.Params;
import com.universal.framwork.util.LogUtil;

public class DownLoadJob extends Job<String>
{
  private static final String TAG = "DownLoadJob";
  
  public DownLoadJob()
  {
    
  }
  
  public DownLoadJob(Listener<String> listener)
  {
    super(listener);
  }
  
  public DownLoadJob(Params param,Listener<String> listener)
  {
    super(param,listener);
  }

  @Override
  public String onRun()
  {
    try
    {
      Thread.sleep(5*1000);
    } catch (InterruptedException e)
    {
      
    }
    return "success";
  }
  
  @Override
  public void onAdded()
  {
    super.onAdded();
    Log.v("job", "download job is add");
  }



}
