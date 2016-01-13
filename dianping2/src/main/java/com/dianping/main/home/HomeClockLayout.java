package com.dianping.main.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.app.DPApplication;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.CountDownClock;
import com.dianping.base.widget.CountDownClock.OnTimeListener;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;
import org.json.JSONObject;

public class HomeClockLayout extends NovaLinearLayout
  implements CountDownClock.OnTimeListener, View.OnClickListener
{
  public static final int TYPE_NORMAL = 0;
  public static final int TYPE_TRIP = 1;
  private CountDownClock countDownClock;
  private View countDownClockLayout;
  private TextView currentPrice;
  private int mType;
  private ImageView nonCountDownPic;
  private TextView promPrice;
  private ImageView timeStatusImageView;
  private TextView title;
  private String url;

  public HomeClockLayout(Context paramContext)
  {
    super(paramContext);
  }

  public HomeClockLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onClick(View paramView)
  {
    if (!android.text.TextUtils.isEmpty(this.url))
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.url));
      getContext().startActivity(paramView);
      DPApplication.instance().statisticsEvent("index5", "index5_brand", "", 0);
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.currentPrice = ((TextView)findViewById(R.id.current_price));
    this.title = ((TextView)findViewById(R.id.famous_item_tittle));
    this.promPrice = ((TextView)findViewById(R.id.prom_price));
    this.countDownClock = ((CountDownClock)findViewById(R.id.famous_shop_clock));
    this.countDownClockLayout = findViewById(R.id.count_down_clock_layout);
    this.nonCountDownPic = ((ImageView)findViewById(R.id.non_count_down_pic));
    this.timeStatusImageView = ((ImageView)findViewById(R.id.famous_status_imv));
    setOnClickListener(this);
  }

  public void onTimesUp(int paramInt)
  {
    if (this.mType == 0)
      if (paramInt == 0)
        this.timeStatusImageView.setBackgroundResource(R.drawable.home_famous_clock_begin);
    do
    {
      return;
      if (paramInt == 1)
      {
        this.timeStatusImageView.setBackgroundResource(R.drawable.home_famous_clock_end);
        return;
      }
      this.timeStatusImageView.setBackgroundResource(R.drawable.home_famous_clock_ended);
      return;
    }
    while (this.mType != 1);
    if (paramInt == 0)
    {
      this.timeStatusImageView.setBackgroundResource(R.drawable.home_famous_brand_text_start);
      return;
    }
    if (paramInt == 1)
    {
      this.timeStatusImageView.setBackgroundResource(R.drawable.home_famous_brand_text_over);
      return;
    }
    this.timeStatusImageView.setBackgroundResource(R.drawable.home_famous_brand_text_overed);
  }

  public void resetCount()
  {
    if (this.countDownClock != null)
      this.countDownClock.resetCount();
  }

  public void restartCount()
  {
    if (this.countDownClock != null)
      this.countDownClock.reStartCount();
  }

  public void setFamousShopItem(JSONObject paramJSONObject, int paramInt)
  {
    if (paramJSONObject == null)
      return;
    this.mType = paramInt;
    this.url = paramJSONObject.optString("url");
    String str1 = paramJSONObject.optString("startTime");
    String str2 = paramJSONObject.optString("endTime");
    if (("0".equals(str1)) && ("0".equals(str2)))
    {
      this.countDownClockLayout.setVisibility(8);
      if (paramInt == 1)
        this.nonCountDownPic.setVisibility(0);
      this.currentPrice.setText(paramJSONObject.optString("originPrice"));
      setGAString(paramJSONObject.optString("gaLabel"));
      if (!android.text.TextUtils.isEmpty(paramJSONObject.optString("tag")))
        break label214;
      this.promPrice.setVisibility(8);
    }
    while (true)
    {
      while (true)
      {
        paramJSONObject = com.dianping.util.TextUtils.jsonParseText(paramJSONObject.optString("title"));
        this.title.setText(paramJSONObject);
        this.gaUserInfo.title = paramJSONObject.toString();
        ((NovaActivity)getContext()).addGAView(this, 0, "home", "home".equals(((NovaActivity)getContext()).getPageName()));
        return;
        try
        {
          this.countDownClock.setOnTimeListener(this);
          this.countDownClock.startCount(Long.parseLong(str1), Long.parseLong(str2));
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
      }
      break;
      label214: this.promPrice.setText(paramJSONObject.optString("tag"));
      this.promPrice.setVisibility(0);
    }
  }

  public void stopCount()
  {
    this.countDownClock.stopCount();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeClockLayout
 * JD-Core Version:    0.6.0
 */