package com.dianping.shopinfo.hotel;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaRelativeLayout;

public class HotelBottomBannerAgent extends ShopCellAgent
{
  protected static final String CELL_BOTTOMBANNER = "9999bottombanner.";
  private NovaRelativeLayout bottomBanner;

  public HotelBottomBannerAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void setupBottomBanner(DPObject paramDPObject)
  {
    this.bottomBanner = ((NovaRelativeLayout)this.res.inflate(getContext(), R.layout.hotel_shoplist_select_deal_banner, null, false));
    Object localObject;
    DisplayMetrics localDisplayMetrics;
    if (paramDPObject != null)
    {
      localObject = (NetworkImageView)this.bottomBanner.findViewById(16908294);
      localDisplayMetrics = new DisplayMetrics();
    }
    try
    {
      getFragment().getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
      ((NetworkImageView)localObject).getLayoutParams().height = (localDisplayMetrics.widthPixels * 160 / 640);
      label86: if (!TextUtils.isEmpty(paramDPObject.getString("PicUrl")))
        ((NetworkImageView)localObject).setImage(paramDPObject.getString("PicUrl"));
      if (!TextUtils.isEmpty(paramDPObject.getString("Name")))
      {
        localObject = (TextView)this.bottomBanner.findViewById(R.id.title);
        ((TextView)localObject).setVisibility(0);
        ((TextView)localObject).setText(paramDPObject.getString("Name"));
      }
      while (true)
      {
        addCell("9999bottombanner.", this.bottomBanner);
        if (!TextUtils.isEmpty(paramDPObject.getString("Url")))
          this.bottomBanner.setOnClickListener(new View.OnClickListener(paramDPObject)
          {
            public void onClick(View paramView)
            {
              paramView = new GAUserInfo();
              paramView.title = (this.val$ad.getInt("ID") + "");
              GAHelper.instance().contextStatisticsEvent(HotelBottomBannerAgent.this.getFragment().getActivity(), "bottom_ad_clk", paramView, "tap");
              paramView = this.val$ad.getString("Url");
              if (paramView.startsWith("dianping"))
              {
                paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
                HotelBottomBannerAgent.this.fragment.getActivity().startActivity(paramView);
                return;
              }
              paramView = Uri.parse("dianping://web");
              Uri localUri = Uri.parse(this.val$ad.getString("Url"));
              paramView = new Intent("android.intent.action.VIEW", paramView.buildUpon().appendQueryParameter("url", localUri.toString()).build());
              HotelBottomBannerAgent.this.fragment.getActivity().startActivity(paramView);
            }
          });
        localObject = new GAUserInfo();
        ((GAUserInfo)localObject).title = (paramDPObject.getInt("ID") + "");
        GAHelper.instance().contextStatisticsEvent(getFragment().getActivity(), "bottom_ad_view", (GAUserInfo)localObject, "view");
        return;
        this.bottomBanner.findViewById(R.id.title).setVisibility(8);
      }
    }
    catch (Exception localException)
    {
      break label86;
    }
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    if (paramAgentMessage.what.equals("com.dianping.shopinfo.hotel.HotelFavorShopAgent.HOTEL_FAVOR_SHOP_LOAD_DATA_NEW_CPMADS"))
      onAgentChanged(paramAgentMessage.body);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((paramBundle != null) && (paramBundle.getParcelable("SpecialAd") != null))
      setupBottomBanner((DPObject)paramBundle.getParcelable("SpecialAd"));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.HotelBottomBannerAgent
 * JD-Core Version:    0.6.0
 */