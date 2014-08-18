package com.universal.demo.imgs;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.Toast;

import com.universal.framwork.BaseActivity;
import com.universal.framwork.annotation.InjectView;
import com.universal.framwork.bitmapfun.ImageCache.ImageCacheParams;
import com.universal.framwork.bitmapfun.ImageFetcher;
import com.universal.framwork.demo.R;

public class ImageActivity extends BaseActivity
{
  private static final String TAG = "ImageActivity";
  @InjectView(id=R.id.gridView)
  private GridView mGridView;
  private int mGridViewItemSize;
  private int mGridViewSpace;
  private ImageAdapter mAdapter;
  //Bitmapfun用来获取图片的对象
  private ImageFetcher mFetcher;
  
  private DisplayMetrics dm=new DisplayMetrics();
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.gridlayout);
    this.getWindowManager().getDefaultDisplay().getMetrics(dm);
    mGridViewItemSize=this.getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
    mGridViewSpace=this.getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);
    //创建ImageFetcher对象，需要传递Context和图片尺寸
    mFetcher=new ImageFetcher(this ,mGridViewItemSize);
    //创建缓存所需要的参数
    ImageCacheParams cacheParams = new ImageCacheParams(this, "thumb1");
    cacheParams.setMemCacheSizePercent(0.25f);
    //设置ImageView在Loading过程中显示的图片
    mFetcher.setLoadingImage(R.drawable.empty_photo);
    //为mFetcher添加缓存，如果不调用这句，那么图片时无法加载的，mFetcher中的磁盘缓存的锁一直没有释放
    mFetcher.addImageCache(this.getSupportFragmentManager(), cacheParams);
    mAdapter=new ImageAdapter(this, mFetcher);
    mGridView.setAdapter(mAdapter);
    
    mGridView.setOnScrollListener(new OnScrollListener()
    {
      
      @Override
      public void onScrollStateChanged(AbsListView absListView, int scrollState)
      {
        if(scrollState==AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
        {
          mFetcher.setPauseWork(false);
        }else
        {
          mFetcher.setPauseWork(true);
        }
      }
      
      @Override
      public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount)
      {
      }
    });
    
    mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
    {
      
      @Override
      public void onGlobalLayout()
      {
        final int mColNum=(int) Math.floor(mGridView.getWidth()/mGridViewItemSize);
        final int itemHeight=mGridView.getWidth()/mColNum-mGridViewSpace; 
        mAdapter.setItemHeight(itemHeight);
        mGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
      }
    });
  }
  
  @Override
  protected void onResume()
  {
    super.onResume();
    //取消提前退出任务
    mFetcher.setExitTasksEarly(false);
  }
  
  @Override
  protected void onPause()
  {
    super.onPause();
    //一定要调用提前退出任务，不然可能导致线程阻塞，无法退出
    mFetcher.setExitTasksEarly(true);
    mFetcher.flushCache();
  }
  
  @Override
  protected void onDestroy()
  {
    super.onDestroy();
    mFetcher.closeCache();
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId()) {
      case R.id.clear_cache:
          mFetcher.clearCache();
          Toast.makeText(this, "缓存清理完毕！",
                  Toast.LENGTH_SHORT).show();
          return true;
  }
    return super.onOptionsItemSelected(item);
  }
  
  

}
