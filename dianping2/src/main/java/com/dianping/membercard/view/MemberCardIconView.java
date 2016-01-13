package com.dianping.membercard.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.v1.R.drawable;

public class MemberCardIconView extends NetworkThumbView
{
  Context mContext;

  public MemberCardIconView(Context paramContext)
  {
    super(paramContext);
    this.mContext = paramContext;
  }

  public MemberCardIconView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
  }

  public MemberCardIconView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mContext = paramContext;
  }

  public void setImage(String paramString)
  {
    if (paramString == null);
    do
    {
      return;
      i = paramString.lastIndexOf("/");
    }
    while (i < 0);
    String str = paramString.substring(i + 1);
    str = str.substring(0, str.indexOf(".")).replace("mc_card_logo_", "");
    int j;
    try
    {
      j = Integer.valueOf(str).intValue();
      if ((j > 9) || (j == 0))
      {
        super.setImage(paramString);
        return;
      }
    }
    catch (Exception localException)
    {
      super.setImage(paramString);
      return;
    }
    int i = 0;
    switch (j)
    {
    default:
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
    case 8:
    case 9:
    }
    while (i != 0)
    {
      setImageDrawable(this.mContext.getResources().getDrawable(i));
      this.url = paramString;
      this.imageRetrieve = Boolean.valueOf(true);
      return;
      i = R.drawable.mc_card_logo_1;
      continue;
      i = R.drawable.mc_card_logo_2;
      continue;
      i = R.drawable.mc_card_logo_3;
      continue;
      i = R.drawable.mc_card_logo_4;
      continue;
      i = R.drawable.mc_card_logo_5;
      continue;
      i = R.drawable.mc_card_logo_6;
      continue;
      i = R.drawable.mc_card_logo_7;
      continue;
      i = R.drawable.mc_card_logo_8;
      continue;
      i = R.drawable.mc_card_logo_9;
    }
    super.setImage(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.MemberCardIconView
 * JD-Core Version:    0.6.0
 */