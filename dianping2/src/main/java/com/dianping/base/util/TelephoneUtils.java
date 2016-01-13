package com.dianping.base.util;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.content.CityUtils;
import com.dianping.model.City;
import com.dianping.util.telephone.ContactUtils;
import com.dianping.v1.R.dimen;

public class TelephoneUtils
{
  public static boolean dial(Context paramContext, DPObject paramDPObject, String paramString)
  {
    return (!TextUtils.isEmpty(paramString)) && (paramContext != null) && (paramDPObject != null) && (dial(paramContext, Integer.toString(paramDPObject.getInt("CityID")), paramString));
  }

  public static boolean dial(Context paramContext, String paramString1, String paramString2)
  {
    if ((TextUtils.isEmpty(paramString2)) || (paramContext == null) || (TextUtils.isEmpty(paramString1)))
      return false;
    Object localObject = DPApplication.instance().city();
    if (Integer.valueOf(paramString1).intValue() != DPApplication.instance().cityId())
      localObject = CityUtils.getCityById(Integer.valueOf(paramString1).intValue());
    if (ContactUtils.havaExtNumber(paramString2))
    {
      localObject = ContactUtils.getHotline(paramString2, ConfigHelper.enableHackPhone);
      paramString1 = (String)localObject;
      if (!ConfigHelper.enableHackPhone)
        showExtNumberToast(paramContext, ContactUtils.getExtNumber(paramString2));
    }
    for (paramString1 = (String)localObject; ; paramString1 = ContactUtils.getFullPhoneNum((City)localObject, paramString2))
      return ContactUtils.dial(paramContext, paramString1);
  }

  private static void showExtNumberToast(Context paramContext, String paramString)
  {
    paramString = Toast.makeText(paramContext, "拨打电话的时候请手动加拨分机号" + ContactUtils.getExtNumber(paramString) + "哦", 1);
    paramString.setGravity(17, 0, 0);
    if ((paramString.getView() instanceof LinearLayout))
    {
      LinearLayout localLinearLayout = (LinearLayout)paramString.getView();
      if ((localLinearLayout.getChildAt(0) instanceof TextView))
        ((TextView)localLinearLayout.getChildAt(0)).setTextSize(0, paramContext.getResources().getDimension(R.dimen.text_large));
    }
    paramString.show();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.util.TelephoneUtils
 * JD-Core Version:    0.6.0
 */