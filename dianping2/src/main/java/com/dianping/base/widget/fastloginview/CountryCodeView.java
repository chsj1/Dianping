package com.dianping.base.widget.fastloginview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import org.json.JSONObject;

public class CountryCodeView extends TextView
  implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener
{
  static final String COUNTRY_CODE_KEY = "account:countryCode";
  static final String DEFAULT_COUNTRY_CODE = "86";
  static final String LAST_COUNTRY_CODE = "last_country_code";

  public CountryCodeView(Context paramContext)
  {
    this(paramContext, null);
  }

  public CountryCodeView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842884);
  }

  public CountryCodeView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    setOnClickListener(this);
    paramContext = new LinearLayout.LayoutParams(-2, -1);
    paramContext.setMargins(0, 0, ViewUtils.dip2px(getContext(), 7.0F), 0);
    setLayoutParams(paramContext);
    setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.mc_arrow_down, 0);
    setGravity(16);
    setBackgroundResource(R.drawable.showver_line_bg_right);
    setTextColor(getResources().getColor(R.color.text_color_black));
    setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_18));
    setSingleLine(true);
    setEllipsize(TextUtils.TruncateAt.END);
    setPadding(0, 0, ViewUtils.dip2px(getContext(), 12.0F), 0);
  }

  private void setCountryCodeFromPreference(SharedPreferences paramSharedPreferences)
  {
    paramSharedPreferences = paramSharedPreferences.getString("account:countryCode", "");
    if (!TextUtils.isEmpty(paramSharedPreferences));
    try
    {
      paramSharedPreferences = new JSONObject(paramSharedPreferences).optString("code");
      if (!TextUtils.isEmpty(paramSharedPreferences))
        setCountryCode(paramSharedPreferences);
      return;
      setCountryCode(DPActivity.preferences().getString("last_country_code", "86"));
      return;
    }
    catch (org.json.JSONException paramSharedPreferences)
    {
    }
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    SharedPreferences localSharedPreferences = getContext().getSharedPreferences("jsbridge_storage", 0);
    localSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    setCountryCodeFromPreference(localSharedPreferences);
  }

  public void onClick(View paramView)
  {
    getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=http://m.dianping.com/login/choosecountry")));
  }

  protected void onDetachedFromWindow()
  {
    getContext().getSharedPreferences("jsbridge_storage", 0).unregisterOnSharedPreferenceChangeListener(this);
    super.onDetachedFromWindow();
  }

  public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString)
  {
    if ("account:countryCode".equals(paramString))
      setCountryCodeFromPreference(paramSharedPreferences);
  }

  public void setCountryCode(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      setText("+" + paramString);
      DPActivity.preferences().edit().putString("last_country_code", paramString).commit();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.fastloginview.CountryCodeView
 * JD-Core Version:    0.6.0
 */