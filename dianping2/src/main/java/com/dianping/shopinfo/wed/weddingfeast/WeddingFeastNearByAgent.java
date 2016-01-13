package com.dianping.shopinfo.wed.weddingfeast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopPower;
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
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class WeddingFeastNearByAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_WEDDING_FEAST_NEARBY = "2900NearBy.0001";
  int categoryId;
  ShopinfoCommonCell commCell;
  NovaRelativeLayout expandView;
  DPObject extra;
  LinearLayout linearLayout;
  MApiRequest mNearByRequest;
  DPObject[] mNearByShopList;
  DPObject mResponse;
  private boolean send = false;

  public WeddingFeastNearByAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createNearByShopCell(DPObject paramDPObject, int paramInt)
  {
    if (paramDPObject == null)
      return null;
    View localView = this.res.inflate(getContext(), R.layout.wedding_feast_nearby_item, getParentView(), false);
    if (localView == null)
      return null;
    TextView localTextView1 = (TextView)localView.findViewById(R.id.name);
    TextView localTextView2 = (TextView)localView.findViewById(R.id.distance);
    TextView localTextView3 = (TextView)localView.findViewById(R.id.price);
    ShopPower localShopPower = (ShopPower)localView.findViewById(R.id.shop_power);
    if ((localTextView1 != null) && (paramDPObject.getString("Name") != null) && (!paramDPObject.getString("Name").equals("")))
      localTextView1.setText(paramDPObject.getString("Name"));
    if (localTextView2 != null)
    {
      if (this.mResponse.getInt("RecommendType") != 0)
        break label241;
      if ((paramDPObject.getString("Distance") != null) && (!paramDPObject.getString("Distance").equals("")))
        localTextView2.setText(paramDPObject.getString("Distance"));
    }
    while (true)
    {
      if ((localTextView3 != null) && (paramDPObject.getString("Price") != null) && (!paramDPObject.getString("Price").equals("")))
        localTextView3.setText(paramDPObject.getString("Price"));
      if (localShopPower != null)
        localShopPower.setPower(paramDPObject.getInt("Star"));
      localView.setOnClickListener(new View.OnClickListener(paramDPObject, paramInt)
      {
        public void onClick(View paramView)
        {
          paramView = new ArrayList();
          paramView.add(new BasicNameValuePair("shopid", WeddingFeastNearByAgent.this.shopId() + ""));
          WeddingFeastNearByAgent.this.statisticsEvent("shopinfow", "shopinfow_recommendshop", "" + this.val$deal.getInt("ShopID"), this.val$index, paramView);
          if (WeddingFeastNearByAgent.this.mResponse.getInt("RecommendType") == 0)
          {
            paramView = new GAUserInfo();
            paramView.shop_id = Integer.valueOf(WeddingFeastNearByAgent.this.shopId());
            paramView.index = Integer.valueOf(this.val$index);
            GAHelper.instance().contextStatisticsEvent(WeddingFeastNearByAgent.this.getContext(), "nearby_shop", paramView, "tap");
          }
          while (true)
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + this.val$deal.getInt("ShopID")));
            paramView.putExtra("shopId", this.val$deal.getInt("ShopID"));
            paramView.putExtra("shop", this.val$deal.getObject("Shop"));
            WeddingFeastNearByAgent.this.startActivity(paramView);
            return;
            paramView = new GAUserInfo();
            paramView.shop_id = Integer.valueOf(WeddingFeastNearByAgent.this.shopId());
            paramView.index = Integer.valueOf(this.val$index);
            GAHelper.instance().contextStatisticsEvent(WeddingFeastNearByAgent.this.getContext(), "popular_shop", paramView, "tap");
          }
        }
      });
      return localView;
      label241: if ((paramDPObject.getObject("Shop").getString("RegionName") == null) || (paramDPObject.getObject("Shop").getString("RegionName").equals("")))
        continue;
      localTextView2.setText(paramDPObject.getObject("Shop").getString("RegionName"));
    }
  }

  private void sendRequest()
  {
    StringBuffer localStringBuffer = new StringBuffer("http://m.api.dianping.com/wedding/weddinghotelnearbyhotel.bin?");
    localStringBuffer.append("shopid=").append(shopId());
    this.mNearByRequest = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mNearByRequest, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (!isWeddingType());
    while (true)
    {
      return;
      this.extra = ((DPObject)getSharedObject("WeddingHotelExtra"));
      if (this.extra == null)
        continue;
      this.categoryId = this.extra.getInt("CategoryID");
      if ((this.mNearByShopList != null) && (this.mNearByShopList.length != 0))
        break;
      if (this.send)
        continue;
      sendRequest();
      this.send = true;
      return;
    }
    removeAllCells();
    this.commCell = ((ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false));
    if ((this.mResponse != null) && (this.mResponse.getString("Title") != null) && (!this.mResponse.getString("Title").equals("")))
      this.commCell.setTitle(this.mResponse.getString("Title"));
    while (true)
    {
      this.commCell.hideArrow();
      this.linearLayout = new LinearLayout(getContext());
      this.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
      this.linearLayout.setOrientation(1);
      int i = 0;
      while (i < this.mNearByShopList.length)
      {
        this.linearLayout.addView(createNearByShopCell(this.mNearByShopList[i], i));
        i += 1;
      }
      this.commCell.setTitle("人气婚宴场地推荐");
    }
    this.expandView = ((NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.expand_wedding_nearby, getParentView(), false));
    if (this.mResponse.getInt("RecommendType") == 0)
    {
      this.expandView.setGAString("nearby_more");
      if (this.expandView != null)
      {
        if ((this.mResponse == null) || (this.mResponse.getString("MoreText") == null) || (this.mResponse.getString("MoreText").equals("")))
          break label439;
        ((TextView)(TextView)this.expandView.findViewById(16908308)).setText(this.mResponse.getString("MoreText"));
      }
    }
    while (true)
    {
      this.expandView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if ((WeddingFeastNearByAgent.this.mNearByShopList == null) || (WeddingFeastNearByAgent.this.mNearByShopList.length < 1))
            return;
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://categoryshoplist?categoryid=" + WeddingFeastNearByAgent.this.categoryId));
          WeddingFeastNearByAgent.this.startActivity(paramView);
        }
      });
      this.linearLayout.addView(this.expandView);
      this.commCell.addContent(this.linearLayout, false);
      addCell("2900NearBy.0001", this.commCell);
      return;
      this.expandView.setGAString("popular_more");
      break;
      label439: ((TextView)(TextView)this.expandView.findViewById(16908308)).setText("更多婚宴场地推荐");
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    if ((this.mNearByRequest != null) && (getFragment() != null) && (getFragment().mapiService() != null))
      getFragment().mapiService().abort(this.mNearByRequest, this, true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.mResponse = ((DPObject)paramMApiResponse.result());
    if (this.mResponse != null)
      this.mNearByShopList = this.mResponse.getArray("NearbyWeddingHotelList");
    dispatchAgentChanged(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.weddingfeast.WeddingFeastNearByAgent
 * JD-Core Version:    0.6.0
 */