package com.dianping.main.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import org.json.JSONArray;
import org.json.JSONObject;

public class PromotionAdTemplate extends NovaLinearLayout
  implements View.OnClickListener
{
  private String backgroudColor;
  private String iconUrl;
  private TextView mainTittleTv;
  private NetworkImageView promotionImageView;
  private String promotionUrl;
  private TextView subTittleTv;

  public PromotionAdTemplate(Context paramContext)
  {
    super(paramContext, null);
  }

  public PromotionAdTemplate(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public PromotionAdTemplate(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public void SetPromotionAd(JSONObject paramJSONObject)
  {
    if (paramJSONObject == null)
      return;
    JSONObject localJSONObject = paramJSONObject.optJSONArray("promotionUnits").optJSONObject(0);
    this.promotionUrl = localJSONObject.optString("schema");
    this.iconUrl = localJSONObject.optString("icon");
    this.backgroudColor = localJSONObject.optString("background");
    this.mainTittleTv.setText(com.dianping.util.TextUtils.jsonParseText(localJSONObject.optString("title")));
    this.subTittleTv.setText(com.dianping.util.TextUtils.jsonParseText(localJSONObject.optString("subTitle")));
    if (com.dianping.util.TextUtils.isValidColor(this.backgroudColor))
      setBackgroundColor(Color.parseColor(this.backgroudColor));
    while (true)
    {
      this.promotionImageView.setImage(this.iconUrl);
      setOnClickListener(this);
      this.gaUserInfo.biz_id = paramJSONObject.optString("bizId");
      setGAString("promotion");
      paramJSONObject = GAHelper.instance().getDpActivity(getContext());
      if (!(paramJSONObject instanceof DPActivity))
        break;
      ((DPActivity)paramJSONObject).addGAView(this, -1);
      return;
      setBackgroundResource(R.color.white);
    }
  }

  public void onClick(View paramView)
  {
    if (!android.text.TextUtils.isEmpty(this.promotionUrl));
    try
    {
      getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.promotionUrl)));
      return;
    }
    catch (Exception paramView)
    {
      paramView.printStackTrace();
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mainTittleTv = ((TextView)findViewById(R.id.promotionad_main_tv));
    this.subTittleTv = ((TextView)findViewById(R.id.promotion_sub_tv));
    this.promotionImageView = ((NetworkImageView)findViewById(R.id.promotionad_imageview));
    setBackgroundResource(R.drawable.home_listview_bg);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.PromotionAdTemplate
 * JD-Core Version:    0.6.0
 */