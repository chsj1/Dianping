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
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaRelativeLayout;
import org.json.JSONObject;

public class HomeNewUserAdItem extends NovaRelativeLayout
  implements View.OnClickListener
{
  private NetworkImageView image;
  private String schema;
  private TextView subTitle;
  private TextView tag;
  private TextView title;

  public HomeNewUserAdItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public HomeNewUserAdItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onClick(View paramView)
  {
    if (!android.text.TextUtils.isEmpty(this.schema));
    try
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.schema));
      getContext().startActivity(paramView);
      return;
    }
    catch (java.lang.Exception paramView)
    {
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.title = ((TextView)findViewById(R.id.title));
    this.subTitle = ((TextView)findViewById(R.id.subtitle));
    this.image = ((NetworkImageView)findViewById(R.id.image));
    this.tag = ((TextView)findViewById(R.id.tag));
    setOnClickListener(this);
  }

  public void setNewUserAd(JSONObject paramJSONObject, int paramInt, String paramString)
  {
    if (paramJSONObject == null)
      return;
    if (!android.text.TextUtils.isEmpty(paramString))
      this.tag.setText(paramString);
    this.subTitle.setText(com.dianping.util.TextUtils.jsonParseText(paramJSONObject.optString("subTitle")));
    this.image.setImage(paramJSONObject.optString("icon"));
    this.schema = paramJSONObject.optString("schema");
    if (com.dianping.util.TextUtils.isValidColor(paramJSONObject.optString("background")))
      setBackgroundColor(Color.parseColor(paramJSONObject.optString("background")));
    while (true)
    {
      setGAString("new", this.title.getText().toString());
      paramJSONObject = GAHelper.instance().getDpActivity(getContext());
      if (!(paramJSONObject instanceof DPActivity))
        break;
      ((DPActivity)paramJSONObject).addGAView(this, paramInt);
      return;
      setBackgroundResource(R.color.white);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeNewUserAdItem
 * JD-Core Version:    0.6.0
 */