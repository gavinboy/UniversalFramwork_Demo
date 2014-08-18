package com.universal.demo.db;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.universal.framwork.BaseActivity;
import com.universal.framwork.XApplication;
import com.universal.framwork.annotation.InjectView;
import com.universal.framwork.db.XSQLiteDataBase;
import com.universal.framwork.demo.R;
import com.universal.framwork.exception.DBNotOpenException;
import com.universal.framwork.util.LogUtil;

public class DBActivity extends BaseActivity 
{
  private XSQLiteDataBase mSQLiteDataBase;
  @InjectView(id=R.id.btn_insert,click="onClickInsert")
  private Button btn_insert;
  
  @InjectView(id=R.id.btn_update,click="onClickUpdate")
  private Button btn_update;
  
  @InjectView(id=R.id.btn_query,click="onClickQuery")
  private Button btn_query;
  
  @InjectView(id=R.id.btn_delete,click="onClickDelete")
  private Button btn_delete;
  
  @InjectView(id=R.id.db_result)
  private ListView mListView;
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.dblayout);
    initSQLiteDataBase();
    if(mSQLiteDataBase!=null)
    {
      try
      {
        if(!mSQLiteDataBase.hasTable(Person.class))
        {
//          if(mSQLiteDataBase.dropTable(Person.class))
//          {
//            Log.v("sql", "删除数据库成功!");
//          }else
//          {
//            Log.v("sql", "删除数据库失败!");
//          }
          if(mSQLiteDataBase.createTable(Person.class))
          {
            Log.v("sql", "创建数据库成功!");
          }else
          {
            Log.v("sql", "创建数据库失败!");
          }
        }
        else
        {
          LogUtil.v("sql", "数据库已经存在，不用创建");
        }
//        if(mSQLiteDataBase.createTable(Person.class))
//        {
//          Log.v("sql", "创建数据库成功!");
//        }else
//        {
//          Log.v("sql", "创建数据库失败!");
//        }
        
      } catch (DBNotOpenException e)
      {
        Log.v("sql", "创建数据库失败!");
        e.printStackTrace();
      }
    }
    releaseSQLiteDataBase();
    
  }
  
  @Override
  protected void onResume()
  {
    super.onResume();
    initSQLiteDataBase();
    showData();
    releaseSQLiteDataBase();
  }
  
  //------------onClick ---------start----------
  public void onClickInsert(View view)
  {
    initSQLiteDataBase();
    if(mSQLiteDataBase!=null)
    {
      Person person=new Person();
      person.setName("gavin");
      person.setAddr("shanghai");
      person.setSex("man");
      mSQLiteDataBase.insert(person);
      showData();
    }
    releaseSQLiteDataBase();
  }
  
  public void onClickUpdate(View view)
  {
    initSQLiteDataBase();
    Person person=new Person();
    person.setName("aviva");
    person.setId(17);
    mSQLiteDataBase.update(person);
    showData();
    releaseSQLiteDataBase();
  }
  
  public void onClickQuery(View view)
  {
    initSQLiteDataBase();
    if(mSQLiteDataBase!=null)
    {
      showData();
    }
    releaseSQLiteDataBase();
  }
  
  public void onClickDelete(View view)
  {
    initSQLiteDataBase();
    if(mSQLiteDataBase!=null)
    {
      Person person=new Person();
      //person.setName("aviva");
      person.setAddr("shanghai");
      person.setSex("man");
      mSQLiteDataBase.delete(person);
      showData();
     
    }
    releaseSQLiteDataBase();
  }
  //------------onClick ----------end-----------
  

  
  private void initSQLiteDataBase()
  {
    mSQLiteDataBase=XApplication.getInstance().getSQLiteDataBasePool().getSQLiteDataBase();
  }
  
  private void releaseSQLiteDataBase()
  {
    XApplication.getInstance().getSQLiteDataBasePool().releaseSQLiteDataBase(mSQLiteDataBase);
  }
  
  
  private void showData()
  {
    
    try
    {
      ArrayList<HashMap<String, String>>result=mSQLiteDataBase.query("select * from person", null);
      SimpleAdapter adapter=new SimpleAdapter(this, result,R.layout.dbitem, new String[]{"ID","Name","Addr","Sex"},new int[]{R.id.tv0,R.id.tv1,R.id.tv2,R.id.tv3});
      mListView.setAdapter(adapter);
    } catch (DBNotOpenException e)
    {
      e.printStackTrace();
    }
  }
  
  
}
