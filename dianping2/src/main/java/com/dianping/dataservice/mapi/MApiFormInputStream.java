package com.dianping.dataservice.mapi;

import com.dianping.dataservice.http.FormInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class MApiFormInputStream extends FormInputStream
{
  public static final String DEFAULT_CHARSET = "UTF-8";
  public static final String UTF_8 = "UTF-8";

  public MApiFormInputStream(List<NameValuePair> paramList)
  {
    super(paramList, "UTF-8");
  }

  public MApiFormInputStream(String[] paramArrayOfString)
  {
    super(form(paramArrayOfString), "UTF-8");
  }

  private static List<NameValuePair> form(String[] paramArrayOfString)
  {
    int j = paramArrayOfString.length / 2;
    ArrayList localArrayList = new ArrayList(j);
    int i = 0;
    while (i < j)
    {
      localArrayList.add(new BasicNameValuePair(paramArrayOfString[(i * 2)], paramArrayOfString[(i * 2 + 1)]));
      i += 1;
    }
    return localArrayList;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.dataservice.mapi.MApiFormInputStream
 * JD-Core Version:    0.6.0
 */