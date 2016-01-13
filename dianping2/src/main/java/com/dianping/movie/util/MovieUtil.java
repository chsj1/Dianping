package com.dianping.movie.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ColorBorderTextView;
import com.dianping.movie.view.MovieTicketInfoForGeneratePhotoView;
import com.dianping.util.PermissionCheckHelper;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import java.io.File;
import java.util.Arrays;
import java.util.Date;

public class MovieUtil
{
  public static SpannableString getAmountSpannableString(Context paramContext, String paramString)
  {
    paramString = new SpannableString("￥" + paramString);
    paramString.setSpan(new AbsoluteSizeSpan(paramContext.getResources().getDimensionPixelSize(R.dimen.text_very_small)), 0, 1, 33);
    return paramString;
  }

  public static boolean getDataBoolean(Context paramContext, String paramString)
  {
    return paramContext.getSharedPreferences("movie_app_data", 32768).getBoolean(paramString, false);
  }

  public static SpannableString getMinusAmountSpannableString(Context paramContext, String paramString)
  {
    paramString = new SpannableString("-￥" + paramString);
    paramString.setSpan(new AbsoluteSizeSpan(paramContext.getResources().getDimensionPixelSize(R.dimen.text_large)), 0, 1, 33);
    paramString.setSpan(new AbsoluteSizeSpan(paramContext.getResources().getDimensionPixelSize(R.dimen.text_very_small)), 1, 2, 33);
    return paramString;
  }

  public static float getSeatDensityFactor(Context paramContext)
  {
    return paramContext.getResources().getDisplayMetrics().density / 2.667F;
  }

  public static String loadPhoneNo(Context paramContext, AccountService paramAccountService)
  {
    Object localObject = paramContext.getSharedPreferences("moviephone", 0);
    String str = ((SharedPreferences)localObject).getString("phone", "");
    long l1 = ((SharedPreferences)localObject).getLong("addDate", 0L);
    long l2 = new Date().getTime();
    if (!TextUtils.isEmpty(str))
    {
      localObject = str;
      if (l2 - l1 <= 2592000000L);
    }
    else
    {
      if (paramAccountService.token() == null)
        break label170;
      paramAccountService = paramAccountService.profile().getString("PhoneNo");
      localObject = paramAccountService;
      if (!TextUtils.isEmpty(paramAccountService));
    }
    while (true)
    {
      try
      {
        localObject = ((TelephonyManager)paramContext.getSystemService("phone")).getLine1Number();
        paramContext = (Context)localObject;
        if (TextUtils.isEmpty((CharSequence)localObject))
          continue;
        paramContext = (Context)localObject;
        if (!((String)localObject).startsWith("+86"))
          continue;
        paramContext = ((String)localObject).substring(((String)localObject).indexOf("+86") + 3);
        paramAccountService = paramContext;
        if (paramContext == null)
          continue;
        paramAccountService = paramContext;
        if (paramContext.length() == 11)
          continue;
        paramAccountService = null;
        return paramAccountService;
      }
      catch (Exception paramContext)
      {
        paramContext.printStackTrace();
        localObject = paramAccountService;
        continue;
      }
      try
      {
        label170: localObject = ((TelephonyManager)paramContext.getSystemService("phone")).getLine1Number();
      }
      catch (Exception paramContext)
      {
        paramContext.printStackTrace();
        localObject = str;
      }
    }
  }

  public static void saveDataBoolean(Context paramContext, String paramString, boolean paramBoolean)
  {
    paramContext = paramContext.getSharedPreferences("movie_app_data", 32768).edit();
    paramContext.putBoolean(paramString, paramBoolean);
    paramContext.commit();
  }

  public static void savePhoneNo(Context paramContext, String paramString)
  {
    paramContext = paramContext.getSharedPreferences("moviephone", 0).edit();
    paramContext.putString("phone", paramString.trim());
    paramContext.putLong("addDate", new Date().getTime());
    paramContext.commit();
  }

  private static void savePhotoIfPermissionGranted(Context paramContext, DPObject paramDPObject)
  {
    Object localObject = (MovieTicketInfoForGeneratePhotoView)LayoutInflater.from(paramContext).inflate(R.layout.movie_ticketinfo_forgeneratephoto_view, null, false);
    ((MovieTicketInfoForGeneratePhotoView)localObject).setMovieTicketInfo(paramDPObject);
    ((MovieTicketInfoForGeneratePhotoView)localObject).setDrawingCacheEnabled(true);
    ((MovieTicketInfoForGeneratePhotoView)localObject).measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
    ((MovieTicketInfoForGeneratePhotoView)localObject).layout(0, 0, ((MovieTicketInfoForGeneratePhotoView)localObject).getMeasuredWidth(), ((MovieTicketInfoForGeneratePhotoView)localObject).getMeasuredHeight());
    ((MovieTicketInfoForGeneratePhotoView)localObject).buildDrawingCache();
    paramDPObject = ((MovieTicketInfoForGeneratePhotoView)localObject).getDrawingCache();
    if (!Environment.getExternalStorageState().equals("mounted"))
    {
      Toast.makeText(paramContext, "您手机里没有内存卡，无法保存图片", 1).show();
      return;
    }
    localObject = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dianping";
    if (paramDPObject != null)
    {
      new MovieUtil.2(paramContext, (String)localObject).execute(new Bitmap[] { paramDPObject });
      return;
    }
    Toast.makeText(paramContext, "保存失败", 0).show();
  }

  public static void saveTicketInfoAsPhoto(Context paramContext, DPObject paramDPObject)
  {
    paramDPObject = new MovieUtil.1(paramContext, paramDPObject);
    String str = paramContext.getResources().getString(R.string.rationale_camera);
    PermissionCheckHelper.instance().requestPermissions(paramContext, 0, new String[] { "android.permission.WRITE_EXTERNAL_STORAGE" }, new String[] { str }, paramDPObject);
  }

  private static void showSeats(Context paramContext, LinearLayout paramLinearLayout, String[] paramArrayOfString, int paramInt1, int paramInt2, int paramInt3)
  {
    paramLinearLayout.removeAllViews();
    paramLinearLayout.setGravity(16);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
    localLayoutParams.setMargins(0, 0, ViewUtils.dip2px(paramContext, 4.0F), 0);
    int j = 0;
    while (j < paramArrayOfString.length)
    {
      String str = paramArrayOfString[j];
      int m = paramInt1;
      int i = paramInt2;
      int k = paramInt3;
      if (!TextUtils.isEmpty(str))
      {
        ColorBorderTextView localColorBorderTextView = new ColorBorderTextView(paramContext);
        i = paramInt2;
        if (paramInt2 == 0)
          i = R.color.text_gray_color;
        localColorBorderTextView.setTextColor(paramContext.getResources().getColor(i));
        paramInt2 = paramInt1;
        if (paramInt1 == 0)
          paramInt2 = R.color.review_delete_gray_color;
        localColorBorderTextView.setBorderColor(paramContext.getResources().getColor(paramInt2));
        localColorBorderTextView.setText(str);
        paramInt1 = paramInt3;
        if (paramInt3 == 0)
          paramInt1 = R.dimen.text_size_12;
        localColorBorderTextView.setTextSize(0, paramContext.getResources().getDimensionPixelSize(paramInt1));
        localColorBorderTextView.setSingleLine();
        localColorBorderTextView.setEllipsize(TextUtils.TruncateAt.END);
        localColorBorderTextView.setPadding(ViewUtils.dip2px(paramContext, 4.0F), ViewUtils.dip2px(paramContext, 2.0F), ViewUtils.dip2px(paramContext, 4.0F), ViewUtils.dip2px(paramContext, 2.0F));
        paramLinearLayout.addView(localColorBorderTextView, localLayoutParams);
        k = paramInt1;
        m = paramInt2;
      }
      j += 1;
      paramInt1 = m;
      paramInt2 = i;
      paramInt3 = k;
    }
  }

  public static void showSelectedSeats(Context paramContext, LinearLayout paramLinearLayout, String[] paramArrayOfString, int paramInt1, int paramInt2)
  {
    int i = R.dimen.text_size_12;
    int j = 4;
    if (paramArrayOfString[0].length() >= 7)
    {
      j = 3;
      i = R.dimen.text_size_10;
    }
    int n = (paramArrayOfString.length - 1) / j;
    int k = 0;
    while (k < n + 1)
    {
      LinearLayout localLinearLayout = new LinearLayout(paramContext);
      localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, ViewUtils.dip2px(paramContext, 26.0F)));
      localLinearLayout.setOrientation(0);
      int m = (k + 1) * j;
      if ((k + 1) * j > paramArrayOfString.length)
        m = paramArrayOfString.length;
      showSeats(paramContext, localLinearLayout, (String[])Arrays.copyOfRange(paramArrayOfString, k * j, m), paramInt1, paramInt2, i);
      paramLinearLayout.addView(localLinearLayout);
      k += 1;
    }
  }

  public static String speratedString(String paramString1, int paramInt, String paramString2)
  {
    if (TextUtils.isEmpty(paramString1));
    do
      return paramString1;
    while ((paramInt < 0) || (paramString1.length() <= paramInt));
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 0;
    while ((i + 1) * paramInt < paramString1.length())
    {
      localStringBuffer.append(paramString1.substring(i * paramInt, (i + 1) * paramInt));
      localStringBuffer.append(paramString2);
      i += 1;
    }
    localStringBuffer.append(paramString1.substring(i * paramInt, paramString1.length()));
    return localStringBuffer.toString();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.util.MovieUtil
 * JD-Core Version:    0.6.0
 */