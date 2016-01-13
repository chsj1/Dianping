package com.dianping.main.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import org.json.JSONArray;
import org.json.JSONObject;

public class HomeFeatureItem extends NovaLinearLayout
  implements View.OnClickListener
{
  public static final int MAX_TAGS = 2;
  public String feedback;
  private NetworkImageView image;
  private int index;
  private JSONObject mJson;
  private String schema;
  private TextView[] tags = new TextView[2];
  private TextView title;

  public HomeFeatureItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public HomeFeatureItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onClick(View paramView)
  {
    if (!android.text.TextUtils.isEmpty(this.schema))
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.schema));
      getContext().startActivity(paramView);
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.title = ((TextView)findViewById(R.id.title));
    this.tags[0] = ((TextView)findViewById(R.id.feature_title1));
    this.tags[1] = ((TextView)findViewById(R.id.feature_title2));
    this.image = ((NetworkImageView)findViewById(R.id.image));
    setOnClickListener(this);
  }

  public void setFeatureData(JSONObject paramJSONObject, int paramInt)
  {
    if (paramJSONObject == null);
    do
    {
      return;
      this.mJson = paramJSONObject;
      this.title.setText(com.dianping.util.TextUtils.jsonParseText(this.mJson.optString("title")));
      paramJSONObject = this.mJson.optJSONArray("tags");
      int i;
      if (paramJSONObject != null)
      {
        i = 0;
        if (i < 2)
        {
          if ((paramJSONObject.length() > i) && (!com.dianping.util.TextUtils.isEmpty(paramJSONObject.optString(i))))
          {
            this.tags[i].setVisibility(0);
            this.tags[i].setText(paramJSONObject.optString(i));
          }
          while (true)
          {
            i += 1;
            break;
            this.tags[i].setVisibility(4);
          }
        }
      }
      else
      {
        i = 0;
        while (i < 2)
        {
          this.tags[i].setVisibility(4);
          i += 1;
        }
      }
      this.image.setImage(this.mJson.optString("icon"));
      this.schema = this.mJson.optString("schema");
      this.index = paramInt;
      setGAString(this.mJson.optString("gaLabel"), this.title.getText().toString(), paramInt);
      paramJSONObject = GAHelper.instance().getDpActivity(getContext());
    }
    while (!(paramJSONObject instanceof DPActivity));
    ((DPActivity)paramJSONObject).addGAView(this, paramInt, "home", "home".equals(((DPActivity)paramJSONObject).getPageName()));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeFeatureItem
 * JD-Core Version:    0.6.0
 */