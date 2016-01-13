package com.dianping.shopinfo.hotel.senic.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.util.TelephoneUtils;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.map.utils.MapUtils;
import com.dianping.model.Location;
import com.dianping.shopinfo.widget.ShopDetailFeatureItem;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaTextView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ScenicDetailsActivity extends NovaActivity
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private ArrayList<DPObject> mFeatureList;
  private ArrayList<DPObject> mRouteList = new ArrayList();
  private int mRouteType = 0;
  private DPObject mShop;
  private MApiRequest routeRequest;
  private DPObject shopExtra;
  private int shopid;

  private void requestRoute()
  {
    Object localObject = locationService().location();
    if (localObject != null)
    {
      String str = Location.FMT.format(((DPObject)localObject).getDouble("OffsetLat"));
      localObject = Location.FMT.format(((DPObject)localObject).getDouble("OffsetLng"));
      this.routeRequest = BasicMApiRequest.mapiGet("http://m.api.dianping.com/direction.bin?srclat=" + str + "&srclng=" + (String)localObject + "&destlat=" + this.mShop.getDouble("Latitude") + "&destlng=" + this.mShop.getDouble("Longitude") + "&maptype=" + 1 + "&navitype=" + this.mRouteType, CacheType.NORMAL);
      mapiService().exec(this.routeRequest, this);
    }
  }

  private void setupView(String paramString)
  {
    super.setContentView(R.layout.scenic_shop_details_layout);
    Object localObject2;
    Object localObject3;
    if (this.mShop != null)
    {
      paramString = this.mShop.getString("StarTips");
      if (!TextUtils.isEmpty(paramString))
      {
        localObject1 = findViewById(R.id.star_tips_header);
        localObject2 = (TextView)findViewById(R.id.star_tips);
        ((View)localObject1).setVisibility(0);
        ((TextView)localObject2).setVisibility(0);
        ((TextView)localObject2).setText(paramString);
      }
      paramString = this.mShop.getStringArray("PhoneNos");
      localObject1 = new StringBuffer();
      if ((paramString != null) && (paramString.length > 0))
      {
        int i = 0;
        int j = paramString.length;
        while (i < j)
        {
          ((StringBuffer)localObject1).append(paramString[i]);
          if (i != paramString.length - 1)
            ((StringBuffer)localObject1).append("，");
          i += 1;
        }
        localObject2 = findViewById(R.id.business_phone_header);
        localObject3 = (NovaTextView)findViewById(R.id.business_phone);
        ((View)localObject2).setVisibility(0);
        ((NovaTextView)localObject3).setVisibility(0);
        ((NovaTextView)localObject3).setText(((StringBuffer)localObject1).toString());
        ((NovaTextView)localObject3).setOnClickListener(new View.OnClickListener((NovaTextView)localObject3, paramString)
        {
          public void onClick(View paramView)
          {
            this.val$business_phone.setGAString("scenic_tel");
            if (this.val$phoneNos != null)
            {
              if (this.val$phoneNos.length == 1)
              {
                TelephoneUtils.dial(ScenicDetailsActivity.this, ScenicDetailsActivity.this.mShop, this.val$phoneNos[0]);
                ScenicDetailsActivity.this.statisticsEvent("shopinfo5", "shopinfo5_hoteltel", "", ScenicDetailsActivity.this.shopid);
              }
            }
            else
              return;
            paramView = new String[this.val$phoneNos.length];
            int i = 0;
            while (i < this.val$phoneNos.length)
            {
              paramView[i] = ("拨打电话: " + this.val$phoneNos[i]);
              i += 1;
            }
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(ScenicDetailsActivity.this);
            localBuilder.setTitle("联系商户").setItems(paramView, new DialogInterface.OnClickListener()
            {
              public void onClick(DialogInterface paramDialogInterface, int paramInt)
              {
                TelephoneUtils.dial(ScenicDetailsActivity.this, ScenicDetailsActivity.this.mShop, ScenicDetailsActivity.1.this.val$phoneNos[paramInt]);
                ScenicDetailsActivity.this.statisticsEvent("shopinfo5", "shopinfo5_scenic", "", ScenicDetailsActivity.this.shopid);
              }
            });
            localBuilder.create().show();
          }
        });
      }
      paramString = this.mShop.getString("AltName");
      if (!TextUtils.isEmpty(paramString))
      {
        localObject1 = findViewById(R.id.alt_name_header);
        localObject2 = (TextView)findViewById(R.id.alt_name_view);
        ((View)localObject1).setVisibility(0);
        ((TextView)localObject2).setVisibility(0);
        ((TextView)localObject2).setText(paramString);
      }
    }
    if (this.shopExtra != null)
    {
      paramString = this.shopExtra.getString("Time");
      if (!TextUtils.isEmpty(paramString))
      {
        localObject1 = findViewById(R.id.business_hours_header);
        localObject2 = (TextView)findViewById(R.id.business_hours);
        ((View)localObject1).setVisibility(0);
        ((TextView)localObject2).setVisibility(0);
        ((TextView)localObject2).setText(paramString);
      }
      paramString = this.shopExtra.getObject("BrandStory");
      if ((paramString != null) && (!TextUtils.isEmpty(paramString.getString("Desc"))))
      {
        localObject1 = findViewById(R.id.story_name_header);
        localObject2 = (NovaTextView)findViewById(R.id.story_name_view);
        ((View)localObject1).setVisibility(0);
        ((NovaTextView)localObject2).setVisibility(0);
        ((NovaTextView)localObject2).setText(paramString.getString("Desc"));
        ((NovaTextView)localObject2).setOnClickListener(new View.OnClickListener((NovaTextView)localObject2, paramString)
        {
          public void onClick(View paramView)
          {
            try
            {
              this.val$story_name_view.setGAString("scenic_intro");
              paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$BrandStory.getString("Url")));
              ScenicDetailsActivity.this.startActivity(paramView);
              return;
            }
            catch (Exception paramView)
            {
              paramView.printStackTrace();
            }
          }
        });
      }
      paramString = this.shopExtra.getString("Path");
      this.mRouteType = Integer.valueOf(this.shopExtra.getInt("Pathtype")).intValue();
      if (!TextUtils.isEmpty(paramString))
      {
        localObject1 = findViewById(R.id.route_header);
        localObject2 = findViewById(R.id.route_lay);
        ((View)localObject2).setOnClickListener(this);
        localObject3 = findViewById(R.id.more_route);
        ((View)localObject3).setOnClickListener(this);
        ((View)localObject1).setVisibility(0);
        ((View)localObject2).setVisibility(0);
        ((View)localObject3).setVisibility(0);
        localObject1 = (TextView)findViewById(R.id.route_view);
        localObject2 = (TextView)findViewById(R.id.route_time);
        ((TextView)localObject1).setText(paramString);
        ((TextView)localObject2).setText(this.shopExtra.getString("Pathtime"));
      }
    }
    Object localObject1 = findViewById(R.id.feature_header);
    paramString = (LinearLayout)findViewById(R.id.feature_list);
    if ((this.mFeatureList != null) && (this.mFeatureList.size() > 0))
    {
      ((View)localObject1).setVisibility(0);
      paramString.setVisibility(0);
      paramString.removeAllViews();
      localObject1 = this.mFeatureList.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (DPObject)((Iterator)localObject1).next();
        localObject3 = (ShopDetailFeatureItem)LayoutInflater.from(this).inflate(R.layout.detail_characteristic_item, null, false);
        ((ShopDetailFeatureItem)localObject3).setFeatureContent((DPObject)localObject2);
        paramString.addView((View)localObject3);
      }
    }
    requestRoute();
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 6);
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if ((i == R.id.more_route) || (i == R.id.route_lay))
    {
      MapUtils.gotoNavi(this, this.mShop);
      if (i == R.id.more_route)
        statisticsEvent("shopinfo5", "shopinfo5_info_route", "查看更多路线", 0);
    }
    else
    {
      return;
    }
    statisticsEvent("shopinfo5", "shopinfo5_info_route", "查看最优路线", 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mShop = ((DPObject)getIntent().getParcelableExtra("shop"));
    this.shopid = getIntent().getIntExtra("shopid", 0);
    this.shopExtra = ((DPObject)getIntent().getParcelableExtra("shopextra"));
    this.mFeatureList = getIntent().getParcelableArrayListExtra("featurelist");
    setTitle("实用信息");
    if (this.mShop != null)
    {
      setSubtitle(this.mShop.getString("Name") + this.mShop.getString("BranchName"));
      if (this.mShop.getObject("ClientShopStyle") != null)
        break label143;
    }
    label143: for (paramBundle = null; ; paramBundle = this.mShop.getObject("ClientShopStyle").getString("ShopView"))
    {
      setupView(paramBundle);
      return;
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.routeRequest != null)
    {
      mapiService().abort(this.routeRequest, this, true);
      this.routeRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.routeRequest)
      this.routeRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.routeRequest)
    {
      this.routeRequest = null;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if ((paramMApiRequest != null) && (paramMApiRequest.getArray("Paths") != null))
      {
        this.mRouteList.clear();
        this.mRouteList.addAll(Arrays.asList(paramMApiRequest.getArray("Paths")));
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.senic.activity.ScenicDetailsActivity
 * JD-Core Version:    0.6.0
 */