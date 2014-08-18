package com.universal.demo.db;

import com.universal.framwork.annotation.Column;
import com.universal.framwork.annotation.PrimaryKey;
import com.universal.framwork.annotation.Table;
import com.universal.framwork.annotation.Transient;
@Table(name="person")
public class Person
{
  @PrimaryKey(name="ID",autoIncrement=true)
  private int id;
  
  @Column(name="Name")
  private String name;
  
  @Column(name="Addr")
  private String addr;
  
  @Column(name="Sex")
  private String sex;

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getAddr()
  {
    return addr;
  }

  public void setAddr(String addr)
  {
    this.addr = addr;
  }

  public String getSex()
  {
    return sex;
  }

  public void setSex(String sex)
  {
    this.sex = sex;
  }
  
  
  
  
  
}
