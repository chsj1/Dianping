package com.dianping.shopinfo.hotel.senic;

import android.content.Context;
import com.dianping.widget.view.GAHelper;
import java.util.ArrayList;

public class ScenicOnceGATool
{
  private ArrayList<String> onceTokenList = new ArrayList();

  public ScenicOnceGATool(String paramString, String[] paramArrayOfString)
  {
    if (paramString != null)
      this.onceTokenList.add(paramString);
    int j = paramArrayOfString.length;
    int i = 0;
    while (i < j)
    {
      paramString = paramArrayOfString[i];
      this.onceTokenList.add(paramString);
      i += 1;
    }
  }

  public void doGA(Context paramContext, String paramString)
  {
    if (this.onceTokenList.indexOf(paramString) >= 0)
    {
      GAHelper.instance().contextStatisticsEvent(paramContext, paramString, "", 0, "view");
      this.onceTokenList.remove(paramString);
    }
  }

  public void doGA(Context paramContext, String paramString1, String paramString2, int paramInt, String paramString3)
  {
    if (this.onceTokenList.indexOf(paramString1) >= 0)
    {
      GAHelper.instance().contextStatisticsEvent(paramContext, paramString1, paramString2, paramInt, paramString3);
      this.onceTokenList.remove(paramString1);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.senic.ScenicOnceGATool
 * JD-Core Version:    0.6.0
 */