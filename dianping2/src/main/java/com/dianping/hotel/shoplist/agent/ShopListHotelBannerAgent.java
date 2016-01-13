package com.dianping.hotel.shoplist.agent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.shoplist.agent.ShopListBannerAgent;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.hotel.shoplist.data.model.HotelBannerModel;
import com.dianping.loader.MyResources;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaRelativeLayout;
import java.lang.reflect.Method;

public class ShopListHotelBannerAgent extends ShopListBannerAgent
{
  private int bannerCount;
  private boolean bannerLoaded;
  private HotelBannerModel leftBannerModel;
  private HotelBannerModel rightBannerModel;

  public ShopListHotelBannerAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void destroyBannerModel()
  {
    this.rightBannerModel = null;
    this.leftBannerModel = null;
  }

  private boolean prepareBannerModel()
  {
    int j = 1;
    int i = 1;
    DPObject[] arrayOfDPObject = getDataSource().operatingLocation;
    if ((arrayOfDPObject == null) || (arrayOfDPObject.length == 0));
    while (true)
    {
      return false;
      if (arrayOfDPObject.length <= 0)
        break;
      if (arrayOfDPObject[0] == null)
        continue;
      this.leftBannerModel = new HotelBannerModel();
      this.leftBannerModel.setSmallPicUrl(arrayOfDPObject[0].getString("SmallPicUrl"));
      this.leftBannerModel.setPicUrl(arrayOfDPObject[0].getString("PicUrl"));
      this.leftBannerModel.setUrl(arrayOfDPObject[0].getString("Url"));
      this.leftBannerModel.setName(arrayOfDPObject[0].getString("Name"));
      this.bannerCount = 1;
    }
    if (arrayOfDPObject.length > 1)
    {
      if (arrayOfDPObject[1] == null)
      {
        if (this.bannerCount > 0);
        while (true)
        {
          return i;
          i = 0;
        }
      }
      this.rightBannerModel = new HotelBannerModel();
      this.rightBannerModel.setSmallPicUrl(arrayOfDPObject[1].getString("SmallPicUrl"));
      this.rightBannerModel.setPicUrl(arrayOfDPObject[1].getString("PicUrl"));
      this.rightBannerModel.setUrl(arrayOfDPObject[1].getString("Url"));
      this.rightBannerModel.setName(arrayOfDPObject[1].getString("Name"));
      this.bannerCount = 2;
    }
    if (this.bannerCount > 0);
    for (i = j; ; i = 0)
      return i;
  }

  private boolean setBanner(NetworkImageView paramNetworkImageView, HotelBannerModel paramHotelBannerModel)
  {
    if ((paramHotelBannerModel == null) || (paramNetworkImageView == null))
      return false;
    if (paramHotelBannerModel.getName() != null);
    for (String str = paramHotelBannerModel.getName(); ; str = "")
    {
      paramNetworkImageView.setGAString("hotellist_rb", str);
      if (!TextUtils.isEmpty(paramHotelBannerModel.getPicUrl(this.bannerCount)))
        paramNetworkImageView.setImage(paramHotelBannerModel.getPicUrl(this.bannerCount));
      if (!TextUtils.isEmpty(paramHotelBannerModel.getUrl()))
        paramNetworkImageView.setOnClickListener(new View.OnClickListener(paramHotelBannerModel)
        {
          public void onClick(View paramView)
          {
            if (!TextUtils.isEmpty(this.val$model.getUrl()))
            {
              paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$model.getUrl()));
              ShopListHotelBannerAgent.this.getContext().startActivity(paramView);
            }
          }
        });
      return true;
    }
  }

  private void setupBanners()
  {
    if ((this.listView == null) || (this.bannerLoaded))
      return;
    if (!prepareBannerModel())
    {
      destroyBannerModel();
      return;
    }
    NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)this.res.inflate(getContext(), R.layout.hotel_shoplist_ad_banner, null, false);
    Object localObject1 = (NetworkImageView)localNovaRelativeLayout.findViewById(R.id.left_icon);
    NetworkImageView localNetworkImageView = (NetworkImageView)localNovaRelativeLayout.findViewById(R.id.right_icon);
    Object localObject2 = new DisplayMetrics();
    getActivity().getWindowManager().getDefaultDisplay().getMetrics((DisplayMetrics)localObject2);
    if (this.bannerCount == 1)
      if (setBanner((NetworkImageView)localObject1, this.leftBannerModel))
      {
        localNovaRelativeLayout.setPadding(0, 0, 0, 0);
        ((NetworkImageView)localObject1).setVisibility(0);
        ((NetworkImageView)localObject1).getLayoutParams().width = ((DisplayMetrics)localObject2).widthPixels;
        ((NetworkImageView)localObject1).getLayoutParams().height = (((DisplayMetrics)localObject2).widthPixels * 169 / 1080);
      }
    while (true)
    {
      this.bannerLoaded = true;
      destroyBannerModel();
      addListHeader(localNovaRelativeLayout);
      GAHelper.instance().contextStatisticsEvent(getFragment().getActivity(), "hotellist_selected_view", null, "view");
      return;
      if (this.bannerCount != 2)
        continue;
      boolean bool = setBanner((NetworkImageView)localObject1, this.leftBannerModel);
      if (bool)
        setBanner(localNetworkImageView, this.rightBannerModel);
      if (!bool)
        continue;
      ((NetworkImageView)localObject1).setVisibility(0);
      localNetworkImageView.setVisibility(0);
      int i = (((DisplayMetrics)localObject2).widthPixels - ViewUtils.dip2px(getContext(), 30.0F)) / 2;
      int j = i * 94 / 345;
      localObject2 = ((NetworkImageView)localObject1).getLayoutParams();
      localNetworkImageView.getLayoutParams().height = j;
      ((ViewGroup.LayoutParams)localObject2).height = j;
      localObject1 = ((NetworkImageView)localObject1).getLayoutParams();
      localNetworkImageView.getLayoutParams().width = i;
      ((ViewGroup.LayoutParams)localObject1).width = i;
    }
  }

  protected boolean fetchListView()
  {
    if (this.listView != null)
      return true;
    while (true)
    {
      try
      {
        if (Build.VERSION.SDK_INT > 10)
        {
          Object localObject = "shoplist/hotelcontentlist";
          localObject = getFragment().findAgent((String)localObject);
          if (localObject == null)
            return false;
          this.listView = ((PullToRefreshListView)localObject.getClass().getDeclaredMethod("getListView", new Class[0]).invoke(localObject, new Object[0]));
          localObject = this.listView;
          return localObject != null;
        }
      }
      catch (Exception localException)
      {
        return false;
      }
      String str = "shoplist/contentlist";
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    try
    {
      setupBanners();
      return;
    }
    catch (Exception paramBundle)
    {
    }
  }

  protected void resetListView()
  {
    if (Build.VERSION.SDK_INT > 10);
    for (Object localObject = "shoplist/hotelcontentlist"; ; localObject = "shoplist/contentlist")
    {
      localObject = getFragment().findAgent((String)localObject);
      if (localObject != null)
        break;
      return;
    }
    Class localClass = localObject.getClass();
    try
    {
      localClass.getDeclaredMethod("resetListView", new Class[0]).invoke(localObject, new Object[0]);
      this.listView = null;
      return;
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shoplist.agent.ShopListHotelBannerAgent
 * JD-Core Version:    0.6.0
 */