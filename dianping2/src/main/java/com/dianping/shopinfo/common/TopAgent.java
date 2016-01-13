package com.dianping.shopinfo.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.speed.SpeedMonitorHelper;
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.shopinfo.activity.ShopInfoActivity;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.ForeignBigShopInfoHeaderView;
import com.dianping.shopinfo.widget.KTVShopInfoHeaderView;
import com.dianping.shopinfo.widget.MallShopInfoHeaderView;
import com.dianping.shopinfo.widget.ShopInfoHeaderView;
import com.dianping.shopinfo.widget.TourShopInfoHeaderView;
import com.dianping.shopinfo.widget.WeddingShopInfoHeaderView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class TopAgent extends ShopCellAgent
{
  protected static final String CELL_TOP = "0200Basic.05Info";
  protected final View.OnClickListener iconClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = TopAgent.this.getShop();
      if (paramView == null)
        return;
      if ((paramView.contains("PicCount")) && (paramView.getInt("PicCount") == 0) && (TextUtils.isEmpty(paramView.getString("DefaultPic"))))
      {
        UploadPhotoUtil.uploadShopPhoto(TopAgent.this.getContext(), TopAgent.this.getShop());
        return;
      }
      Object localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopphoto"));
      ((Intent)localObject).putExtra("objShop", paramView);
      if (TopAgent.this.getSharedObject("WeddingHotelExtra") != null)
        ((Intent)localObject).putExtra("extraWeddingShop", (DPObject)TopAgent.this.getSharedObject("WeddingHotelExtra"));
      if ((paramView.getInt("Status") == 1) || (paramView.getInt("Status") == 4));
      for (boolean bool = false; ; bool = true)
      {
        ((Intent)localObject).putExtra("enableUpload", bool);
        TopAgent.this.getFragment().startActivity((Intent)localObject);
        localObject = new ArrayList();
        ((List)localObject).add(new BasicNameValuePair("shopid", String.valueOf(paramView.getInt("ID"))));
        if (!TopAgent.this.isTravelType())
          break;
        TopAgent.this.statisticsEvent("shopinfo5", "shopinfo5_viewphoto2", "", 0, (List)localObject);
        return;
      }
      TopAgent.this.statisticsEvent("shopinfo5", "shopinfo5_viewphoto", "", 0, (List)localObject);
    }
  };
  protected ShopInfoHeaderView topView;

  public TopAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void initHeaderView()
  {
    if (this.topView == null)
    {
      if (!isForeignBigType())
        break label57;
      this.topView = ((ForeignBigShopInfoHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shop_panel_foreign, getParentView(), false));
      GAHelper.instance().contextStatisticsEvent(getContext(), "shopmode", "大图", 0, "view");
    }
    label57: 
    do
    {
      return;
      if (isTravelType())
      {
        if ((getShop() != null) && (getShop().getBoolean("HasMultiPic")))
        {
          this.topView = ((ShopInfoHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shop_panel, getParentView(), false));
          return;
        }
        this.topView = ((TourShopInfoHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shop_panel_tour, getParentView(), false));
        NetworkImageView localNetworkImageView = (NetworkImageView)this.topView.findViewById(R.id.shop_panel_collect_icon);
        FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)localNetworkImageView.getLayoutParams();
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((DPActivity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        localLayoutParams.height = (int)(localDisplayMetrics.widthPixels * 0.5620000000000001D);
        localNetworkImageView.setLayoutParams(localLayoutParams);
        return;
      }
      if (isMallType())
      {
        this.topView = ((MallShopInfoHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shop_panel_mal, getParentView(), false));
        return;
      }
      if ((isKTVType()) && (ConfigHelper.enableKTVNewStyle))
      {
        this.topView = ((KTVShopInfoHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shop_ktv_panel, getParentView(), false));
        return;
      }
      this.topView = ((ShopInfoHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shop_panel, getParentView(), false));
    }
    while (!isForeignSmallType());
    GAHelper.instance().contextStatisticsEvent(getContext(), "shopmode", "小图", 0, "view");
  }

  private void resetHeaderView()
  {
    if (this.topView != null)
    {
      if (!isForeignBigType())
        break label35;
      if (!this.topView.getClass().equals(ForeignBigShopInfoHeaderView.class))
        this.topView = null;
    }
    label35: 
    do
      while (true)
      {
        return;
        if (isTravelType())
        {
          if ((getShop() != null) && (getShop().getBoolean("HasMultiPic")))
          {
            if (this.topView.getClass().equals(ShopInfoHeaderView.class))
              continue;
            this.topView = null;
            return;
          }
          if (this.topView.getClass().equals(TourShopInfoHeaderView.class))
            continue;
          this.topView = null;
          return;
        }
        if (!isKTVType())
          break;
        if (this.topView.getClass().equals(KTVShopInfoHeaderView.class))
          continue;
        this.topView = null;
        return;
      }
    while (this.topView.getClass().equals(ShopInfoHeaderView.class));
    this.topView = null;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    paramBundle = getShop();
    removeAllCells();
    if ((paramBundle == null) && (!isWeddingType()))
      break label25;
    label25: 
    do
      return;
    while ((getFragment() == null) || (isMallType()));
    resetHeaderView();
    initHeaderView();
    if ((getSharedObject("WeddingHotelExtra") != null) && ((this.topView instanceof WeddingShopInfoHeaderView)))
      ((WeddingShopInfoHeaderView)this.topView).setExtraInfo((DPObject)getSharedObject("WeddingHotelExtra"));
    if (getShopStatus() == 0)
    {
      this.topView.setShop(paramBundle, 0);
      label102: if (!isMallType())
        this.topView.setIconClickListen(this.iconClickListener);
      paramBundle = this.topView;
      if (!isTravelType())
        break label183;
    }
    label183: for (int i = 1024; ; i = 0)
    {
      addCell("0200Basic.05Info", paramBundle, i);
      if (!(getContext() instanceof ShopInfoActivity))
        break;
      ((ShopInfoActivity)getContext()).getSpeedMonitorHelper().setResponseTime(1, System.currentTimeMillis());
      return;
      this.topView.setShop(paramBundle);
      break label102;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        UploadPhotoUtil.uploadShopPhoto(TopAgent.this.getContext(), TopAgent.this.getShop());
      }
    };
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.TopAgent
 * JD-Core Version:    0.6.0
 */