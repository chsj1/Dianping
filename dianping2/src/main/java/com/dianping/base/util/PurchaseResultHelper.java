package com.dianping.base.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.dianping.archive.DPObject;

public class PurchaseResultHelper
{
  private static PurchaseResultHelper instance;
  private DPObject dpobj;
  private Intent intent;
  private PayFailTo to;

  public static void forwardUrlSchema(Context paramContext, String paramString)
  {
    paramContext.startActivity(instance().getIntentAfterBuy());
    if (!TextUtils.isEmpty(paramString))
    {
      paramString = new Intent("android.intent.action.VIEW", Uri.parse(paramString));
      paramString.addFlags(67108864);
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://me"));
      localIntent.addFlags(67108864);
      paramString.putExtra("next_redirect_", localIntent.toUri(1));
      paramContext.startActivity(paramString);
    }
  }

  public static PurchaseResultHelper instance()
  {
    if (instance == null)
      instance = new PurchaseResultHelper();
    return instance;
  }

  public DPObject getDPObject()
  {
    return this.dpobj;
  }

  public Intent getIntentAfterBuy()
  {
    if (this.intent == null)
      this.intent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://deallist"));
    this.intent.addFlags(67108864);
    return this.intent;
  }

  public PayFailTo getPayFailTo()
  {
    return this.to;
  }

  public void setDPObject(DPObject paramDPObject)
  {
    this.dpobj = paramDPObject;
  }

  public void setIntentAfterBuy(Intent paramIntent)
  {
    this.intent = paramIntent;
  }

  public void setPayFailTo(PayFailTo paramPayFailTo)
  {
    this.to = paramPayFailTo;
  }

  public static enum PayFailTo
  {
    static
    {
      $VALUES = new PayFailTo[] { DEALINFO, UNPAID };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.util.PurchaseResultHelper
 * JD-Core Version:    0.6.0
 */