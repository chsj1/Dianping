package com.dianping.base.ugc.review;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;

public class AddReviewUtil
{
  public static final int REVIEW_FORM_TYPE_BEAUTYHAIR = 13;
  public static final int REVIEW_FORM_TYPE_COMMENT = 2;
  public static final int REVIEW_FORM_TYPE_DISHES = 9;
  public static final int REVIEW_FORM_TYPE_PRICE = 1;
  public static final int REVIEW_FORM_TYPE_SCORES = 3;
  public static final int REVIEW_FORM_TYPE_SHOPTAGS = 8;
  public static final int REVIEW_FORM_TYPE_STAR = 5;

  public static void addReview(Context paramContext, int paramInt, String paramString)
  {
    addReview(paramContext, paramInt, paramString, null, null, null);
  }

  public static void addReview(Context paramContext, int paramInt, String paramString, Bundle paramBundle)
  {
    addReview(paramContext, paramInt, paramString, null, null, paramBundle);
  }

  public static void addReview(Context paramContext, int paramInt, String paramString1, String paramString2, String paramString3)
  {
    addReview(paramContext, paramInt, paramString1, paramString2, paramString3, null);
  }

  private static void addReview(Context paramContext, int paramInt, String paramString1, String paramString2, String paramString3, Bundle paramBundle)
  {
    Uri.Builder localBuilder = Uri.parse("dianping://addreview").buildUpon();
    if (!TextUtils.isEmpty(paramString2))
      localBuilder.appendQueryParameter("source", paramString2);
    if (paramString3 != null)
      localBuilder.appendQueryParameter("referToken", paramString3);
    localBuilder.appendQueryParameter("shopid", String.valueOf(paramInt));
    if (paramString1 != null)
      localBuilder.appendQueryParameter("shopName", String.valueOf(paramString1));
    paramString1 = new Intent("android.intent.action.VIEW", localBuilder.build());
    paramString1.putExtra("checkdraft", true);
    if (paramBundle != null)
      paramString1.putExtras(paramBundle);
    paramContext.startActivity(paramString1);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.ugc.review.AddReviewUtil
 * JD-Core Version:    0.6.0
 */