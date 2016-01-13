package com.dianping.booking.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import org.json.JSONObject;

public class BookingCountryCodeView extends TextView
{
  public static final String BOOKING_SHARED_PREFERENCE = "bookingSharedPreference";
  static final String COUNTRY_CODE_KEY = "account:countryCode";
  static final String DEFAULT_COUNTRY_CODE = "86";
  private String currentMode = "booking";
  SharedPreferences.OnSharedPreferenceChangeListener mainSharedPreference;

  public BookingCountryCodeView(Context paramContext)
  {
    this(paramContext, null);
  }

  public BookingCountryCodeView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842884);
  }

  public BookingCountryCodeView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    paramContext = new LinearLayout.LayoutParams(-2, -1);
    paramContext.setMargins(0, 0, ViewUtils.dip2px(getContext(), 7.0F), 0);
    setCompoundDrawablePadding(ViewUtils.dip2px(getContext(), 7.0F));
    setLayoutParams(paramContext);
    setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.mc_arrow_down, 0);
    setGravity(16);
    setBackgroundResource(R.drawable.showver_line_bg_right);
    setTextColor(getResources().getColor(R.color.text_color_black));
    setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_18));
    setSingleLine(true);
    setEllipsize(TextUtils.TruncateAt.END);
    setPadding(0, 0, ViewUtils.dip2px(getContext(), 16.0F), 0);
  }

  private void setCountryCodeFromPreference(SharedPreferences paramSharedPreferences)
  {
    paramSharedPreferences = paramSharedPreferences.getString("account:countryCode", "");
    if (!TextUtils.isEmpty(paramSharedPreferences))
      try
      {
        if ((!TextUtils.isEmpty(new JSONObject(paramSharedPreferences).optString("mode"))) && (getCurrentMode().equals("booker")))
          return;
        paramSharedPreferences = new JSONObject(paramSharedPreferences).optString("code");
        if (TextUtils.isEmpty(paramSharedPreferences))
          return;
        setCountryCode(paramSharedPreferences);
        return;
      }
      catch (org.json.JSONException paramSharedPreferences)
      {
        return;
      }
    else
      setCountryCode(getContext().getSharedPreferences("bookingSharedPreference", 0).getString(this.currentMode, "86"));
  }

  public String getCurrentMode()
  {
    return this.currentMode;
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    SharedPreferences localSharedPreferences = getContext().getSharedPreferences("jsbridge_storage", 0);
    BookingCountryCodeView.1 local1 = new BookingCountryCodeView.1(this);
    this.mainSharedPreference = local1;
    localSharedPreferences.registerOnSharedPreferenceChangeListener(local1);
    if (this.currentMode.equals("booking"))
    {
      setCountryCodeFromPreference(localSharedPreferences);
      return;
    }
    setText("+" + getContext().getSharedPreferences("bookingSharedPreference", 0).getString(this.currentMode, "86"));
  }

  protected void onDetachedFromWindow()
  {
    getContext().getSharedPreferences("jsbridge_storage", 0).unregisterOnSharedPreferenceChangeListener(this.mainSharedPreference);
    super.onDetachedFromWindow();
  }

  public void setCountryCode(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      setText("+" + paramString);
      getContext().getSharedPreferences("bookingSharedPreference", 0).edit().putString(this.currentMode, paramString).commit();
      if (this.currentMode.equals("booking"))
        DPActivity.preferences().edit().putString("last_country_code", paramString).commit();
    }
    else
    {
      return;
    }
    paramString = DPActivity.preferences().getString("last_country_code", "86");
    getContext().getSharedPreferences("jsbridge_storage", 0).edit().putString("account:countryCode", "{\"code\":\"" + paramString + "\",\"mode\":\"buffer\"}").commit();
  }

  public void setCurrentMode(String paramString)
  {
    this.currentMode = paramString;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.view.BookingCountryCodeView
 * JD-Core Version:    0.6.0
 */