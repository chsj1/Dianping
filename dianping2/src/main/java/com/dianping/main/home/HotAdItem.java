package com.dianping.main.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.util.ThirdGaUtil.AdvertisementGa;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class HotAdItem extends NovaLinearLayout
  implements View.OnClickListener
{
  public String feedback;
  private NetworkImageView image;
  private int index;
  private JSONObject mHotAdItem;
  private String schema;
  private TextView subTitle;
  private TextView title;

  public HotAdItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onClick(View paramView)
  {
    paramView = new ArrayList();
    paramView.add(new BasicNameValuePair("position", String.valueOf(this.index + 1)));
    ((DPActivity)getContext()).statisticsEvent("index5", "index5_richbutton", this.title.getText().toString(), 0, paramView);
    paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.schema));
    getContext().startActivity(paramView);
    if (this.mHotAdItem != null)
    {
      paramView = this.mHotAdItem.optString("adClickUrl");
      if (!android.text.TextUtils.isEmpty(paramView))
        new AdvertisementGa().sendAdGA(paramView);
      HomeAgent.record(2, this.mHotAdItem, this.index, this.feedback);
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.title = ((TextView)findViewById(R.id.title));
    this.subTitle = ((TextView)findViewById(R.id.subtitle));
    this.image = ((NetworkImageView)findViewById(R.id.image));
    setOnClickListener(this);
    setBackgroundResource(R.drawable.home_listview_bg);
  }

  public void setHotAd(JSONObject paramJSONObject, int paramInt)
  {
    if (paramJSONObject == null);
    do
    {
      return;
      this.mHotAdItem = paramJSONObject;
      this.title.setText(com.dianping.util.TextUtils.jsonParseText(paramJSONObject.optString("title")));
      this.subTitle.setText(com.dianping.util.TextUtils.jsonParseText(paramJSONObject.optString("subTitle")));
      this.image.setImage(paramJSONObject.optString("icon"));
      this.schema = paramJSONObject.optString("schema");
      this.feedback = paramJSONObject.optString("cpmFeedback");
      this.index = paramInt;
      setGAString("richbutton", this.title.getText().toString(), paramInt);
      this.gaUserInfo.biz_id = paramJSONObject.optString("bizId");
      this.gaUserInfo.ad_id = paramJSONObject.optString("adId");
      paramJSONObject = GAHelper.instance().getDpActivity(getContext());
    }
    while (!(paramJSONObject instanceof DPActivity));
    ((DPActivity)paramJSONObject).addGAView(this, paramInt, "home", "home".equals(((DPActivity)paramJSONObject).getPageName()));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HotAdItem
 * JD-Core Version:    0.6.0
 */