package com.dianping.tunnel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;

public class BlackWhiteList
{
  private Object[] blackList = compileList(paramJSONArray2);
  private Object[] whiteList = compileList(paramJSONArray1);

  public BlackWhiteList(JSONArray paramJSONArray1, JSONArray paramJSONArray2)
    throws Exception
  {
  }

  private Object compile(String paramString)
  {
    Object localObject = paramString;
    if (paramString.charAt(0) == '`')
      localObject = Pattern.compile(paramString.substring(1));
    return localObject;
  }

  private Object[] compileList(JSONArray paramJSONArray)
    throws Exception
  {
    Object localObject;
    if (paramJSONArray == null)
    {
      localObject = new Object[0];
      return localObject;
    }
    int j = paramJSONArray.length();
    Object[] arrayOfObject = new Object[j];
    int i = 0;
    while (true)
    {
      localObject = arrayOfObject;
      if (i >= j)
        break;
      arrayOfObject[i] = compile(paramJSONArray.getString(i));
      i += 1;
    }
  }

  static String getCommand(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0))
      return "";
    int j = paramString.indexOf('?');
    int i = j;
    if (j < 0)
      i = paramString.length();
    int k = paramString.lastIndexOf('/', i);
    j = k;
    if (k < 0)
      j = -1;
    return paramString.substring(j + 1, i);
  }

  private boolean inList(String paramString1, String paramString2, Object[] paramArrayOfObject)
  {
    int j = paramArrayOfObject.length;
    int i = 0;
    while (i < j)
    {
      Object localObject = paramArrayOfObject[i];
      if (((localObject instanceof String)) && (paramString2.equalsIgnoreCase((String)localObject)))
        return true;
      if (((localObject instanceof Pattern)) && (((Pattern)localObject).matcher(paramString1).matches()))
        return true;
      i += 1;
    }
    return false;
  }

  public boolean block(String paramString)
  {
    String str = getCommand(paramString);
    if (this.whiteList.length == 0)
      return inList(paramString, str, this.blackList);
    return !inList(paramString, str, this.whiteList);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tunnel.BlackWhiteList
 * JD-Core Version:    0.6.0
 */