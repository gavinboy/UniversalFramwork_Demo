package com.universal.demo.imgs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.universal.framwork.bitmapfun.ImageFetcher;
import com.universal.framwork.bitmapfun.RecyclingImageView;

public class ImageAdapter extends BaseAdapter
{
  private static final String TAG = "ImageAdapter";
  
  private Context context;
  private GridView.LayoutParams params;
  private ImageFetcher fetcher;
  private int mItemHeight;
  
  public ImageAdapter(Context context,ImageFetcher fetcher)
  {
    this.context=context;
    this.fetcher=fetcher;
    params=new GridView.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
  }

  @Override
  public int getCount()
  {
    return Images.imageUrls.length;
    //return 1;
  }

  @Override
  public Object getItem(int arg0)
  {
    return Images.imageUrls[arg0];
  }

  @Override
  public long getItemId(int arg0)
  {
    return arg0;
  }

  @Override
  public View getView(int position, View contentView, ViewGroup parent)
  {
    if(contentView==null)
    {
      contentView=new RecyclingImageView(context);
      ((ImageView)contentView).setScaleType(ImageView.ScaleType.CENTER_CROP);
      if(params.height!=mItemHeight)
      {
        params.height=mItemHeight;
      }
      contentView.setLayoutParams(params);
    }
    fetcher.loadImage(Images.imageUrls[position], (ImageView)contentView);
    return contentView;
  }
  
  
  public void setItemHeight(int itemHeight)
  {
    this.mItemHeight=itemHeight;
  }
  

  
}
