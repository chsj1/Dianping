package com.dianping.shopinfo.hotel;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;

public class HotelAcommodationReasonAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_HOTEL_ACOMMODATION_REASON = "2998Hotel.Acommodation";
  private DPObject[] reasons;
  private MApiRequest request;
  private String title;

  public HotelAcommodationReasonAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void addContent(View paramView, boolean paramBoolean, View.OnClickListener paramOnClickListener, ViewGroup paramViewGroup)
  {
    NovaRelativeLayout localNovaRelativeLayout;
    if (paramViewGroup != null)
    {
      localNovaRelativeLayout = (NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.shopinfo_common_cell_item_layout, paramViewGroup, false);
      localNovaRelativeLayout.setBackgroundResource(R.drawable.home_listview_bg);
      ((FrameLayout)localNovaRelativeLayout.findViewById(R.id.item_content)).addView(paramView);
      localNovaRelativeLayout.findViewById(R.id.divider_line).setVisibility(8);
      if (!paramBoolean)
        break label105;
      localNovaRelativeLayout.findViewById(R.id.indicator).setVisibility(0);
    }
    while (true)
    {
      if (paramOnClickListener != null)
        localNovaRelativeLayout.setOnClickListener(new View.OnClickListener(paramOnClickListener, paramView)
        {
          public void onClick(View paramView)
          {
            this.val$listener.onClick(this.val$view);
          }
        });
      paramViewGroup.addView(localNovaRelativeLayout);
      return;
      label105: localNovaRelativeLayout.findViewById(R.id.indicator).setVisibility(8);
    }
  }

  private View createCell()
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    localShopinfoCommonCell.findViewById(R.id.indicator).setVisibility(8);
    localShopinfoCommonCell.setTitle(this.title, null);
    localShopinfoCommonCell.setIcon(R.drawable.detail_icon_good);
    int i = 0;
    while (i < this.reasons.length)
    {
      DPObject localDPObject = this.reasons[i];
      boolean bool = false;
      if (i == this.reasons.length - 1)
        bool = true;
      addContent(createContentItem(localDPObject, getContext(), bool), false, null, (ViewGroup)localShopinfoCommonCell.findViewById(R.id.content));
      i += 1;
    }
    return localShopinfoCommonCell;
  }

  private View createContentItem(DPObject paramDPObject, Context paramContext, boolean paramBoolean)
  {
    LinearLayout localLinearLayout = new LinearLayout(paramContext);
    localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
    localLinearLayout.setOrientation(0);
    Object localObject = new TextView(paramContext);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
    ((TextView)localObject).setGravity(17);
    localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 15.0F);
    localLayoutParams.topMargin = 25;
    ((TextView)localObject).setLayoutParams(localLayoutParams);
    ((TextView)localObject).setText("●");
    ((TextView)localObject).setTextColor(-4473925);
    ((TextView)localObject).setTextSize(12.0F);
    localLinearLayout.addView((View)localObject);
    paramContext = new TextView(paramContext);
    paramContext.setGravity(48);
    localObject = new LinearLayout.LayoutParams(-2, -2);
    ((LinearLayout.LayoutParams)localObject).leftMargin = ViewUtils.dip2px(getContext(), 15.0F);
    ((LinearLayout.LayoutParams)localObject).rightMargin = ViewUtils.dip2px(getContext(), 12.0F);
    ((LinearLayout.LayoutParams)localObject).topMargin = 25;
    if (paramBoolean)
      ((LinearLayout.LayoutParams)localObject).bottomMargin = 25;
    paramContext.setLayoutParams((ViewGroup.LayoutParams)localObject);
    paramContext.setText(paramDPObject.getString("Comment"));
    if (!paramDPObject.getBoolean("Good"))
      paramContext.setTextColor(-3355444);
    localLinearLayout.addView(paramContext);
    return (View)localLinearLayout;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((this.reasons != null) && (this.reasons.length > 0))
      addCell("2998Hotel.Acommodation", createCell());
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.request)
      this.request = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.request == paramMApiRequest)
    {
      this.request = null;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      this.title = paramMApiRequest.getString("Title");
      this.reasons = paramMApiRequest.getArray("Reasons");
      dispatchAgentChanged(false);
    }
  }

  public void sendRequest()
  {
    this.request = BasicMApiRequest.mapiGet("http://m.api.dianping.com/hotel/acommodationreason.hotel?shopid=" + shopId(), CacheType.DISABLED);
    if (this.request != null)
      getFragment().mapiService().exec(this.request, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.HotelAcommodationReasonAgent
 * JD-Core Version:    0.6.0
 */