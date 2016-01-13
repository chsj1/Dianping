package com.dianping.main.user;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.main.find.pictureplaza.PlazaFeedItem;
import com.dianping.main.find.pictureplaza.PlazaFeedPoiView;
import com.dianping.v1.R.id;
import java.util.HashMap;

public class UserFeedItem extends PlazaFeedItem
{
  private PlazaFeedPoiView extraPoiLayout;

  public UserFeedItem(Context paramContext)
  {
    super(paramContext);
  }

  public UserFeedItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.extraPoiLayout = ((PlazaFeedPoiView)findViewById(R.id.extra_poi_layout));
  }

  public void setFeedUgc(DPObject paramDPObject, int paramInt, HashMap<Integer, Integer> paramHashMap1, HashMap<Integer, Integer> paramHashMap2, boolean paramBoolean)
  {
    super.setFeedUgc(paramDPObject, paramInt, paramHashMap1, paramHashMap2, paramBoolean);
    paramDPObject = paramDPObject.getObject("Poi");
    if (paramDPObject != null)
    {
      this.extraPoiLayout.setVisibility(0);
      this.extraPoiLayout.setPoiInfo(paramDPObject, this.reviewType);
      this.extraPoiLayout.setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          if (!TextUtils.isEmpty(UserFeedItem.this.actionUrl));
          for (paramView = new Intent("android.intent.action.VIEW", Uri.parse(UserFeedItem.this.actionUrl)); ; paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + this.val$poiObject.getInt("ShopId"))))
          {
            UserFeedItem.this.getContext().startActivity(paramView);
            return;
          }
        }
      });
      return;
    }
    this.extraPoiLayout.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.UserFeedItem
 * JD-Core Version:    0.6.0
 */