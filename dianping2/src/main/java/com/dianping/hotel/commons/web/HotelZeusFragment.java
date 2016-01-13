package com.dianping.hotel.commons.web;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import com.dianping.app.DPActivity;
import com.dianping.base.web.client.NovaZeusWebViewClient;
import com.dianping.base.web.ui.NovaZeusFragment;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.zeus.client.ZeusWebViewClient;
import com.dianping.zeus.ui.ZeusFragment;

public class HotelZeusFragment extends NovaZeusFragment
{
  private static final String TAG = HotelZeusFragment.class.getSimpleName();
  private FrameLayout loadView;
  private String name;

  private void otaStatisticsEvent(String paramString1, String paramString2)
  {
    if (paramString1.contains("携程"))
    {
      ((DPActivity)getActivity()).statisticsEvent("hotelreserve5", paramString2, "ctrip", 0);
      return;
    }
    if (paramString1.contains("艺龙"))
    {
      ((DPActivity)getActivity()).statisticsEvent("hotelreserve5", paramString2, "elong", 0);
      return;
    }
    ((DPActivity)getActivity()).statisticsEvent("hotelreserve5", paramString2, paramString1, 0);
  }

  protected ZeusWebViewClient createWebViewClient()
  {
    return new HotelWebViewClient(this);
  }

  public void navigateBackward()
  {
    this.webView.goBack();
    if (this.name != null)
      otaStatisticsEvent(this.name, "browser5_hotel_back");
  }

  public void navigateForward()
  {
    this.webView.goForward();
    if (this.name != null)
      otaStatisticsEvent(this.name, "browser5_hotel_forward");
  }

  public void navigateRefresh()
  {
    this.webView.reload();
    if (this.name != null)
      otaStatisticsEvent(this.name, "browser5_hotel_refresh");
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("name", this.name);
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    Intent localIntent = getActivity().getIntent();
    super.onViewCreated(paramView, paramBundle);
    if (paramBundle != null);
    for (this.name = paramBundle.getString("name"); ; this.name = localIntent.getStringExtra("name"))
    {
      this.loadView = new FrameLayout(getContext());
      this.loadView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
      paramView = new ImageView(getContext());
      paramBundle = new FrameLayout.LayoutParams(ViewUtils.dip2px(getContext(), 44.0F), ViewUtils.dip2px(getContext(), 50.0F));
      paramBundle.gravity = 17;
      paramView.setLayoutParams(paramBundle);
      paramBundle = new AnimationDrawable();
      paramBundle.addFrame(getResources().getDrawable(R.drawable.hotel_booking_web_loading_1), 600);
      paramBundle.addFrame(getResources().getDrawable(R.drawable.hotel_booking_web_loading_2), 600);
      paramBundle.addFrame(getResources().getDrawable(R.drawable.hotel_booking_web_loading_3), 600);
      paramBundle.addFrame(getResources().getDrawable(R.drawable.hotel_booking_web_loading_2), 600);
      paramBundle.setOneShot(false);
      paramView.setBackgroundDrawable(paramBundle);
      paramBundle.start();
      this.loadView.addView(paramView);
      this.webView.addView(this.loadView);
      return;
    }
  }

  class HotelWebViewClient extends NovaZeusWebViewClient
  {
    public HotelWebViewClient(ZeusFragment arg2)
    {
      super();
    }

    public void onPageFinished(WebView paramWebView, String paramString)
    {
      super.onPageFinished(paramWebView, paramString);
      HotelZeusFragment.this.webView.removeView(HotelZeusFragment.this.loadView);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.commons.web.HotelZeusFragment
 * JD-Core Version:    0.6.0
 */