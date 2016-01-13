package com.dianping.main.find;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;

public class VerticalChannelFixedLayout extends NovaLinearLayout
  implements View.OnClickListener
{
  Context context;
  DPObject dpObject;
  private NetworkImageView image;
  private TextView subTitle;
  private ImageView tagView;
  private TextView title;
  private String url;

  public VerticalChannelFixedLayout(Context paramContext)
  {
    this(paramContext, null);
  }

  public VerticalChannelFixedLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.context = paramContext;
  }

  public void onClick(View paramView)
  {
    if (this.url != null);
    try
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.url));
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
    this.subTitle = ((TextView)findViewById(R.id.sub_title));
    this.image = ((NetworkImageView)findViewById(R.id.icon));
    this.tagView = ((ImageView)findViewById(R.id.tag_image));
    setOnClickListener(this);
  }

  public void setData(DPObject paramDPObject)
  {
    this.dpObject = paramDPObject;
    Object localObject = paramDPObject.getString("Title");
    this.title.setText((CharSequence)localObject);
    setGAString("channel", (String)localObject);
    localObject = TextUtils.jsonParseText(paramDPObject.getString("SubTitle"));
    if (localObject != null)
      this.subTitle.setText((CharSequence)localObject);
    this.image.setImage(paramDPObject.getString("Image"));
    this.url = paramDPObject.getString("Url");
    int i = paramDPObject.getInt("Tag");
    if (i == 0)
      this.tagView.setVisibility(8);
    do
    {
      return;
      if (i == 1)
      {
        this.tagView.setImageDrawable(getResources().getDrawable(R.drawable.home_0yuan));
        this.tagView.setVisibility(0);
        return;
      }
      if (i != 2)
        continue;
      this.tagView.setImageDrawable(getResources().getDrawable(R.drawable.home_xianshi));
      this.tagView.setVisibility(0);
      return;
    }
    while (i != 3);
    this.tagView.setImageDrawable(getResources().getDrawable(R.drawable.home_miaosha));
    this.tagView.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.VerticalChannelFixedLayout
 * JD-Core Version:    0.6.0
 */