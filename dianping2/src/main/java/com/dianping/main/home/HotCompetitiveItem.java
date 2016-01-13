package com.dianping.main.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.util.ThirdGaUtil.AdvertisementGa;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;
import org.json.JSONObject;

public class HotCompetitiveItem extends NovaLinearLayout
  implements View.OnClickListener
{
  private View divider;
  private NetworkThumbView icon;
  private String mClickUrl;
  private String mFeedback;
  private JSONObject mHotAdItem;
  private int mIndex;
  private String mUrl;
  private TextView subTitleView;
  private TextView titleView;

  public HotCompetitiveItem(Context paramContext)
  {
    super(paramContext);
  }

  public HotCompetitiveItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onClick(View paramView)
  {
    if (!android.text.TextUtils.isEmpty(this.mUrl))
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.mUrl));
      getContext().startActivity(paramView);
      if (!android.text.TextUtils.isEmpty(this.mClickUrl))
        new AdvertisementGa().sendAdGA(this.mClickUrl);
      HomeAgent.record(2, this.mHotAdItem, this.mIndex, this.mFeedback);
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.icon = ((NetworkThumbView)findViewById(16908294));
    this.titleView = ((TextView)findViewById(R.id.title));
    this.subTitleView = ((TextView)findViewById(R.id.subtitle));
    this.divider = findViewById(R.id.item_foot_divider);
    setOnClickListener(this);
  }

  public void setCompetitiveAd(JSONObject paramJSONObject, boolean paramBoolean, int paramInt)
  {
    if (paramJSONObject == null)
      return;
    this.mHotAdItem = paramJSONObject;
    this.mUrl = this.mHotAdItem.optString("schema");
    this.mIndex = paramInt;
    this.mFeedback = this.mHotAdItem.optString("cpmFeedback");
    this.icon.setImage(this.mHotAdItem.optString("icon"));
    this.titleView.setText(com.dianping.util.TextUtils.jsonParseText(this.mHotAdItem.optString("title")));
    this.subTitleView.setText(com.dianping.util.TextUtils.jsonParseText(this.mHotAdItem.optString("subTitle")));
    LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)this.divider.getLayoutParams();
    if (paramBoolean);
    for (int i = 0; ; i = ViewUtils.dip2px(getContext(), 10.0F))
    {
      localLayoutParams.setMargins(i, 0, 0, 0);
      setGAString("recommend", this.titleView.getText().toString(), paramInt);
      this.mClickUrl = paramJSONObject.optString("adClickUrl");
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HotCompetitiveItem
 * JD-Core Version:    0.6.0
 */