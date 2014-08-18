package com.universal.demo.download;

public class DownInfo
{
  private static final String TAG = "DownLoadObj";
  private String url;
  private long totleSize;
  private long currentSize;
  private float speed;
  private int status=DOWN_STATUS.WATING.ordinal();
  
  public String getUrl()
  {
    return url;
  }
  public void setUrl(String url)
  {
    this.url = url;
  }
  public long getTotleSize()
  {
    return totleSize;
  }
  public void setTotleSize(long totleSize)
  {
    this.totleSize = totleSize;
  }
  public long getCurrentSize()
  {
    return currentSize;
  }
  public void setCurrentSize(long currentSize)
  {
    this.currentSize = currentSize;
  }
  public float getSpeed()
  {
    return speed;
  }
  public void setSpeed(float speed)
  {
    this.speed = speed;
  }
  public int getStatus()
  {
    return status;
  }
  public void setStatus(int status)
  {
    this.status = status;
  }
  
  @Override
  public int hashCode()
  {
    return url.hashCode();
  }
  
  @Override
  public boolean equals(Object o)
  {
    if(o instanceof DownInfo)
    {
      return ((DownInfo)o).url.equals(url);
    }
    return false;
  }
  
  public enum DOWN_STATUS
  {
    DOWNING,WATING,FAILING,PAUSEING
  }
  
  
}
