package com.universal.demo.download;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.universal.demo.download.DownInfo.DOWN_STATUS;
import com.universal.framwork.demo.R;
import com.universal.framwork.http.download.DownLoadCallback;
import com.universal.framwork.http.download.DownloadManager;
import com.universal.framwork.util.ToastUtil;

public class DownLoadAdapter extends BaseAdapter implements OnClickListener
{
  private static final String TAG = "DownLoadAdapter";
  private Context mContext;
  private List<DownInfo> downObjs=new ArrayList<DownInfo>();
  private ListView mListView;
  private DownloadManager mDownloadManager;
  
  
  public DownLoadAdapter(Context mContext,ListView mListView)
  {
    this.mContext=mContext;
    this.mListView=mListView;
    mDownloadManager=DownloadManager.getDownloadManager();
   
    mDownloadManager.setDownLoadCallback(new DownLoadCallback(){
      @Override
      public void onStart(String url)
      {
        super.onStart(url);
        View view=DownLoadAdapter.this.mListView.findViewWithTag(url);
        if(view!=null)
        {
          ImageView imgView= ((ImageView)view.findViewById(R.id.img_start_pause));
          ((DownInfo)imgView.getTag()).setStatus(DOWN_STATUS.DOWNING.ordinal());
          imgView.setImageResource(R.drawable.down_loading_selector);
        }
      }
      
      @Override
      public void onLoading(String url, long totalSize, long currentSize, float speed)
      {
        super.onLoading(url, totalSize, currentSize, speed);
        View view=DownLoadAdapter.this.mListView.findViewWithTag(url);
        Log.v("yzy", "onLoading-->url->"+url);
        if(view!=null)
        {
          ((TextView)view.findViewById(R.id.tv_down_size)).setText(String.format("%4.1fM/%4.1fM", currentSize/1024/1024.0f,totalSize/1024/1024.0f));
          ((TextView)view.findViewById(R.id.tv_down_speed)).setText(String.format("%4.1f KB/s", speed));
          ((ProgressBar)view.findViewById(R.id.pbar)).setProgress((int)(100*((float)currentSize/totalSize)));
        }else
        {
          Log.v("yzy", "onLoading-->view is null");
        }
      }
      
      @Override
      public void onSuccess(String url)
      {
        super.onSuccess(url);
        View view=DownLoadAdapter.this.mListView.findViewWithTag(url);
        if(view!=null)
        {
          DownInfo info=(DownInfo)view .findViewById(R.id.img_start_pause).getTag();
          if(info!=null)
          {
            downObjs.remove(info);
            notifyDataSetChanged();
          }
        }
        ToastUtil.AlertMessageInCenter("下载成功");
      }
      
      @Override
      public void onFailure(String url, String strMsg)
      {
        super.onFailure(url, strMsg);
        View view=DownLoadAdapter.this.mListView.findViewWithTag(url);
        if(view!=null)
        {
          ImageView imgView= ((ImageView)view.findViewById(R.id.img_start_pause));
          ((DownInfo)imgView.getTag()).setStatus(DOWN_STATUS.FAILING.ordinal());
          imgView.setImageResource(R.drawable.down_load_fail_selector);
        }
        ToastUtil.AlertMessageInBottom(strMsg);
      }
      
      @Override
      public void onFinish(String url)
      {
        super.onFinish(url);
      }
      
      @Override
      public void onPause(String url)
      {
        super.onPause(url);
        View view=DownLoadAdapter.this.mListView.findViewWithTag(url);
        if(view!=null)
        {
          ImageView imgView= ((ImageView)view.findViewById(R.id.img_start_pause));
          ((DownInfo)imgView.getTag()).setStatus(DOWN_STATUS.FAILING.ordinal());
          DownInfo info=(DownInfo) imgView.getTag();
          info.setStatus(DOWN_STATUS.PAUSEING.ordinal());
          imgView.setImageResource(R.drawable.down_load_pauseing_selector);
        }
        
      }
      
      
    });
  }
  
  public void setData(List<DownInfo> downObjs)
  {
    this.downObjs=downObjs;
    notifyDataSetChanged();
  }
  
  public void addData(DownInfo info)
  {
    if(downObjs!=null)
    {
      downObjs.add(info);
      notifyDataSetChanged();
      mDownloadManager.addHandler(info.getUrl());
    }
  }

  @Override
  public int getCount()
  {
    if(downObjs!=null)
    {
      return downObjs.size();
    }else
    {
      return 0;
    }
    
  }

  @Override
  public Object getItem(int arg0)
  {
    return downObjs!=null?downObjs.get(arg0):null;
  }

  @Override
  public long getItemId(int arg0)
  {
    return arg0;
  }

  @Override
  public View getView(int position, View contentView, ViewGroup parent)
  {
    ViewHolder holder=new ViewHolder();
    DownInfo info=downObjs.get(position);
    if(contentView==null)
    {
      contentView=LayoutInflater.from(mContext).inflate(R.layout.download_item , null);
      
    }
    
    contentView.setTag(info.getUrl());
    holder.mprogress=(ProgressBar) contentView.findViewById(R.id.pbar);
    holder.tv_size=(TextView) contentView.findViewById(R.id.tv_down_size);
    holder.tv_speed=(TextView) contentView.findViewById(R.id.tv_down_speed);
    holder.img_status=(ImageView) contentView.findViewById(R.id.img_start_pause);
    holder.img_status.setTag(info);
    holder.img_status.setOnClickListener(this);
    
    
    
    
    holder.mprogress.setProgress((int)(holder.mprogress.getMax()*((float)info.getCurrentSize()/info.getTotleSize())));
    holder.tv_speed.setText(String.format("%4.1f KB/s", info.getSpeed()));
    holder.tv_size.setText(String.format("%4.1fM/%4.1fM", info.getCurrentSize()/1024/1024.0f,info.getTotleSize()/1024/1024.0f));
    if(info.getStatus()==DOWN_STATUS.DOWNING.ordinal())
    {
      holder.img_status.setImageResource(R.drawable.down_loading_selector);
    }else if(info.getStatus()==DOWN_STATUS.WATING.ordinal())
    {
      holder.img_status.setImageResource(R.drawable.down_load_waiting_selector);
    }else if(info.getStatus()==DOWN_STATUS.PAUSEING.ordinal())
    {
      holder.img_status.setImageResource(R.drawable.down_load_pauseing_selector);
    }else if(info.getStatus()==DOWN_STATUS.FAILING.ordinal())
    {
      holder.img_status.setImageResource(R.drawable.down_load_fail_selector);
    }
    
    return contentView;
  }
  
  class ViewHolder
  {
    public ProgressBar mprogress;
    private TextView tv_size;
    private TextView tv_speed;
    private ImageView img_status;
  }

  @Override
  public void onClick(View view)
  {
    DownInfo info=(DownInfo) view.getTag();
    if(info.getStatus()==DOWN_STATUS.PAUSEING.ordinal())
    {
      mDownloadManager.continueHandler(info.getUrl());
    }else if(info.getStatus()==DOWN_STATUS.DOWNING.ordinal())
    {
      mDownloadManager.pauseHandler(info.getUrl());
    }
    
    
 
    
  }
}
