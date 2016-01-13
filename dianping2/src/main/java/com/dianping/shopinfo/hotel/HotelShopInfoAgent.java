package com.dianping.shopinfo.hotel;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
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
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import org.json.JSONObject;

public class HotelShopInfoAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private MApiRequest hotelDetailRequest;
  private DPObject hotelInfo;

  public HotelShopInfoAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void clickToHotelDetail()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://hoteldetails?cityId=" + getShop().getInt("CityID")));
    localIntent.putExtra("hoteldetail", this.hotelInfo);
    localIntent.putExtra("HotelDetailStatus", 0);
    getFragment().startActivity(localIntent);
    statisticsEvent("shopinfo5", "shopinfo5_hotelinfo", "", shopId());
  }

  private void sendHotelDetailRequest()
  {
    this.hotelDetailRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/hotel/hotelinfo.hotel?shopid=" + shopId(), CacheType.NORMAL);
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        if (HotelShopInfoAgent.this.hotelDetailRequest != null)
          HotelShopInfoAgent.this.getFragment().mapiService().exec(HotelShopInfoAgent.this.hotelDetailRequest, HotelShopInfoAgent.this);
      }
    }
    , 100L);
  }

  private void setupView()
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    View localView = LayoutInflater.from(getContext()).inflate(R.layout.shop_hotel_info_layout, null);
    Object localObject1 = (GridView)localView.findViewById(R.id.hotel_detail_facility_grid);
    Object localObject3 = (RelativeLayout)localView.findViewById(R.id.layout_click_grid);
    new LinearLayout.LayoutParams(-2, -2).setMargins((int)TypedValue.applyDimension(1, 8.0F, this.res.getResources().getDisplayMetrics()), 0, (int)TypedValue.applyDimension(1, 8.0F, this.res.getResources().getDisplayMetrics()), 0);
    Object localObject4 = this.hotelInfo.getArray("KeyFacilityList");
    if ((localObject4 != null) && (localObject4.length > 0))
    {
      ((GridView)localObject1).setAdapter(new KeyFacilityListAdapter(localObject4));
      ((RelativeLayout)localObject3).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          HotelShopInfoAgent.this.clickToHotelDetail();
        }
      });
      if (this.hotelInfo.getString("OpenningTime") != null)
        break label321;
    }
    label321: for (localObject1 = ""; ; localObject1 = this.hotelInfo.getString("OpenningTime"))
    {
      if (!TextUtils.isEmpty((CharSequence)localObject1))
      {
        localObject3 = (TextView)localView.findViewById(R.id.tv_shopinfo_opentime);
        ((TextView)localObject3).setVisibility(0);
        ((TextView)localObject3).setText((CharSequence)localObject1);
      }
      localObject3 = this.hotelInfo.getStringArray("PhoneList");
      localObject1 = new StringBuilder();
      if ((localObject3 == null) || (localObject3.length <= 0))
        break label334;
      ((StringBuilder)localObject1).append("电话：");
      int i = 0;
      int j = localObject3.length;
      while (i < j)
      {
        ((StringBuilder)localObject1).append(localObject3[i]);
        if (i != localObject3.length - 1)
          ((StringBuilder)localObject1).append("，");
        i += 1;
      }
      ((GridView)localObject1).setVisibility(8);
      ((ImageView)localView.findViewById(R.id.img_facility_divder)).setVisibility(8);
      ((RelativeLayout)localObject3).setVisibility(8);
      break;
    }
    label334: if ((localObject1 != null) && (((StringBuilder)localObject1).length() > 0))
    {
      localObject3 = (TextView)localView.findViewById(R.id.tv_shopinfo_phone);
      ((TextView)localObject3).setVisibility(0);
      ((TextView)localObject3).setText(((StringBuilder)localObject1).toString());
    }
    localObject4 = getShop().getString("ExtraJson");
    localObject3 = "";
    localObject1 = localObject3;
    if (!TextUtils.isEmpty((CharSequence)localObject4));
    try
    {
      localObject1 = new JSONObject((String)localObject4).optString("path");
      if (!((String)localObject1).isEmpty())
      {
        localObject3 = (TextView)localView.findViewById(R.id.tv_shopinfo_closeto);
        ((TextView)localObject3).setVisibility(0);
        ((TextView)localObject3).setText("位置距离：" + (String)localObject1);
      }
      localShopinfoCommonCell.addContent(localView, false, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          HotelShopInfoAgent.this.clickToHotelDetail();
        }
      });
      localShopinfoCommonCell.setTitle("设施简介", new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          HotelShopInfoAgent.this.clickToHotelDetail();
        }
      });
      addCell("", localShopinfoCommonCell);
      return;
    }
    catch (Exception localObject2)
    {
      while (true)
      {
        Log.e(localException.toString());
        Object localObject2 = localObject3;
      }
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.hotelInfo != null)
      setupView();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if ((this.hotelDetailRequest == null) && (getFragment().isUrlAvailable("dianping://hoteldetails")))
      sendHotelDetailRequest();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.hotelDetailRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.hotelDetailRequest = null;
    this.hotelInfo = ((DPObject)paramMApiResponse.result());
    dispatchAgentChanged(false);
  }

  class KeyFacilityListAdapter extends BaseAdapter
  {
    DPObject[] keyFacilityList;

    public KeyFacilityListAdapter(DPObject[] arg2)
    {
      Object localObject;
      this.keyFacilityList = localObject;
    }

    public int getCount()
    {
      return this.keyFacilityList.length;
    }

    public Object getItem(int paramInt)
    {
      return this.keyFacilityList[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
      {
        paramView = LayoutInflater.from(HotelShopInfoAgent.this.getContext()).inflate(R.layout.shopinfo_hotel_facility_item, null);
        paramViewGroup = new HotelShopInfoAgent.ViewHolder(HotelShopInfoAgent.this);
        paramViewGroup.hotel_info_facility_img = ((NetworkImageView)paramView.findViewById(R.id.hotel_info_facility_img));
        paramViewGroup.hotel_info_facility_text = ((TextView)paramView.findViewById(R.id.hotel_info_facility_text));
        paramView.setTag(paramViewGroup);
      }
      while (true)
      {
        paramViewGroup.hotel_info_facility_img.setImage(this.keyFacilityList[paramInt].getString("Icon"));
        paramViewGroup.hotel_info_facility_text.setText(this.keyFacilityList[paramInt].getString("Title"));
        return paramView;
        paramViewGroup = (HotelShopInfoAgent.ViewHolder)paramView.getTag();
      }
    }
  }

  class ViewHolder
  {
    NetworkImageView hotel_info_facility_img;
    TextView hotel_info_facility_text;

    ViewHolder()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.HotelShopInfoAgent
 * JD-Core Version:    0.6.0
 */