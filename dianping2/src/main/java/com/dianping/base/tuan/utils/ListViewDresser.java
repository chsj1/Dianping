package com.dianping.base.tuan.utils;

import android.widget.Button;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.util.TextUtils;

public class ListViewDresser
{
  public static void setReviewButton(DPActivity paramDPActivity, Button paramButton, DPObject paramDPObject)
  {
    paramButton.setVisibility(8);
    if ((paramDPObject.getObject("OrderReviewData") != null) && (paramDPObject.getObject("OrderReviewData").getInt("Type") == 1))
    {
      Object localObject = paramDPObject.getObject("OrderReviewData");
      paramDPObject = ((DPObject)localObject).getString("ButtonText");
      localObject = ((DPObject)localObject).getString("Url");
      if ((!TextUtils.isEmpty(paramDPObject)) && (!TextUtils.isEmpty((CharSequence)localObject)))
      {
        paramButton.setVisibility(0);
        paramButton.setText(paramDPObject);
        paramButton.setOnClickListener(new ListViewDresser.1(paramDPActivity, (String)localObject));
      }
    }
  }

  public static void setText(TextView paramTextView, DPObject paramDPObject, String paramString)
  {
    setText(paramTextView, paramDPObject, paramString, false);
  }

  public static void setText(TextView paramTextView, DPObject paramDPObject, String paramString, boolean paramBoolean)
  {
    paramString = paramDPObject.getString(paramString);
    if (TextUtils.isEmpty(paramString))
    {
      paramTextView.setVisibility(8);
      return;
    }
    paramDPObject = paramString;
    if (paramBoolean)
      paramDPObject = TextUtils.jsonParseText(paramString);
    paramTextView.setText(paramDPObject);
    paramTextView.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.utils.ListViewDresser
 * JD-Core Version:    0.6.0
 */