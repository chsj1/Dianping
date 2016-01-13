package com.tencent.stat;

public class StatGameUser
  implements Cloneable
{
  private String a = "";
  private String b = "";
  private String c = "";

  public StatGameUser clone()
  {
    try
    {
      StatGameUser localStatGameUser = (StatGameUser)super.clone();
      return localStatGameUser;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
    }
    return null;
  }

  public String getAccount()
  {
    return this.b;
  }

  public String getLevel()
  {
    return this.c;
  }

  public String getWorldName()
  {
    return this.a;
  }

  public void setAccount(String paramString)
  {
    this.b = paramString;
  }

  public void setLevel(String paramString)
  {
    this.c = paramString;
  }

  public void setWorldName(String paramString)
  {
    this.a = paramString;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.stat.StatGameUser
 * JD-Core Version:    0.6.0
 */