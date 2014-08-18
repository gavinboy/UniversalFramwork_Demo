package com.universal.demo.http;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.universal.framwork.BaseActivity;
import com.universal.framwork.annotation.InjectView;
import com.universal.framwork.demo.R;
import com.universal.framwork.http.AsyncHttpClient;
import com.universal.framwork.http.TextHttpResponseHandler;

public class HttpActivity extends BaseActivity
{
  private static final String TAG = "HttpActivity";
  
  private AsyncHttpClient mHttp=new AsyncHttpClient();
  private ProgressDialog dialog;
  
  @InjectView(id=R.id.btn_baidu,click="onClickBaidu")
  private Button btn_Baidu;
  @InjectView(id=R.id.tv_baidu)
  private TextView tv_baidu;
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.httplayout);
  }
  
  public void onClickBaidu(View view)
  {
    mHttp.get(this, "http://www.baidu.com", new TextHttpResponseHandler(){
      
      @Override
      public void onStart()
      {
        super.onStart();
        if(dialog==null)
        {
          dialog=new ProgressDialog(HttpActivity.this);
          dialog.setMessage("加载数据中...");
        }
        dialog.show();
      }
      @Override
      public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
      {
        super.onSuccess(statusCode, headers, responseBody);
        if(dialog!=null)
        {
          dialog.dismiss();
        }
        
        tv_baidu.setText(new String(responseBody));
        
      }
      
      @Override
      public void onFailure(String responseBody, Throwable error)
      {
        super.onFailure(responseBody, error);
        if(dialog!=null)
        {
          dialog.dismiss();
        }
        
        tv_baidu.setText(error.getMessage());
      }
      
      
      
     
    });
    
  }
}
