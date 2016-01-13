package com.dianping.shopinfo.mall;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.Arrays;

public class HotShopsInMallAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private String CELL_HOTSHOP = "8800HotShopsInMall.";
  private LinearLayout adContent;
  protected MApiRequest infoRequest;
  private LinearLayout lastRow = null;
  private DPObject[] shopList;

  public HotShopsInMallAgent(Object paramObject)
  {
    super(paramObject);
  }

  private boolean addShop(DPObject paramDPObject, int paramInt)
  {
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.shopinfo_mall_hot_shop_item, getParentView(), false);
    ((DPActivity)getContext()).addGAView(localNovaLinearLayout, paramInt);
    NetworkImageView localNetworkImageView = (NetworkImageView)localNovaLinearLayout.findViewById(R.id.shop_image);
    TextView localTextView1 = (TextView)localNovaLinearLayout.findViewById(R.id.shop_name);
    TextView localTextView2 = (TextView)localNovaLinearLayout.findViewById(R.id.review_body);
    if (!TextUtils.isEmpty(paramDPObject.getString("PicUrl")))
      localNetworkImageView.setImage(paramDPObject.getString("PicUrl"));
    if (!TextUtils.isEmpty(paramDPObject.getString("ShopName")))
    {
      localTextView1.setText(paramDPObject.getString("ShopName"));
      if (TextUtils.isEmpty(paramDPObject.getString("ReviewBody")))
        break label176;
      localTextView2.setText(paramDPObject.getString("ReviewBody"));
    }
    while (true)
    {
      localNovaLinearLayout.setGAString("mallpic", "", paramInt);
      addView(localNovaLinearLayout, paramDPObject, paramInt);
      return true;
      ((LinearLayout)localNovaLinearLayout.findViewById(R.id.shop)).setVisibility(8);
      break;
      label176: ((LinearLayout)localNovaLinearLayout.findViewById(R.id.review)).setVisibility(8);
    }
  }

  private void addView(View paramView, DPObject paramDPObject, int paramInt)
  {
    if ((this.lastRow == null) || (this.lastRow.getChildCount() >= 2))
    {
      this.lastRow = new LinearLayout(this.adContent.getContext());
      localObject = new LinearLayout.LayoutParams(-1, -2);
      this.lastRow.setLayoutParams((ViewGroup.LayoutParams)localObject);
      this.lastRow.setOrientation(0);
      this.lastRow.setWeightSum(2.0F);
      this.adContent.addView(this.lastRow);
    }
    paramInt = (int)((getContext().getResources().getDisplayMetrics().widthPixels - 60) / 2 * 0.75D);
    Object localObject = (FrameLayout)paramView.findViewById(R.id.shop_frame);
    ViewGroup.LayoutParams localLayoutParams = ((FrameLayout)localObject).getLayoutParams();
    localLayoutParams.height = paramInt;
    ((FrameLayout)localObject).setLayoutParams(localLayoutParams);
    this.lastRow.addView(paramView);
    paramView.setOnClickListener(new View.OnClickListener(paramDPObject)
    {
      public void onClick(View paramView)
      {
        try
        {
          paramView = this.val$adItem.getString("Url");
          if (!TextUtils.isEmpty(paramView))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
            HotShopsInMallAgent.this.startActivity(paramView);
          }
          return;
        }
        catch (java.lang.Exception paramView)
        {
        }
      }
    });
  }

  private void sendRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/shopping/getmallshowreview.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopId", shopId() + "");
    localBuilder.appendQueryParameter("cityid", cityId() + "");
    this.infoRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.infoRequest, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (getShop() == null);
    do
    {
      do
        return;
      while ((this.shopList == null) || (this.shopList.length <= 1));
      if (this.shopList.length > 4)
        this.shopList = ((DPObject[])Arrays.copyOf(this.shopList, 4));
      while (true)
      {
        paramBundle = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.shopinfo_favor_shop, getParentView(), false);
        this.adContent = ((LinearLayout)paramBundle.findViewById(R.id.content));
        TextView localTextView = (TextView)paramBundle.findViewById(R.id.title);
        ((TextView)paramBundle.findViewById(R.id.sub_title)).setText("");
        localTextView.setText("商场热闹");
        int i = 0;
        while (i < this.shopList.length)
        {
          addShop(this.shopList[i], i);
          i += 1;
        }
        if (this.shopList.length != 3)
          continue;
        this.shopList = ((DPObject[])Arrays.copyOf(this.shopList, 2));
      }
    }
    while (this.adContent.getChildCount() <= 0);
    addCell(this.CELL_HOTSHOP, paramBundle, "mallpic", 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.infoRequest)
      this.infoRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.infoRequest)
    {
      this.infoRequest = null;
      if ((paramMApiResponse != null) && (paramMApiResponse.result() != null) && ((paramMApiResponse.result() instanceof DPObject[])))
      {
        this.shopList = ((DPObject[])(DPObject[])paramMApiResponse.result());
        if ((this.shopList != null) && (this.shopList.length > 0))
          dispatchAgentChanged(false);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.mall.HotShopsInMallAgent
 * JD-Core Version:    0.6.0
 */