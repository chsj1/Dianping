package com.dianping.wed.home.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.shoplist.widget.ShopListItem;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.Location;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class HomeDecorateActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private String categoryId;
  private MApiRequest decorateRequest;
  private LinearLayout linearLayout;
  private DPObject[] shopList;

  private View createShopItem(DPObject paramDPObject)
  {
    double d2 = 1.0D;
    ShopListItem localShopListItem = (ShopListItem)LayoutInflater.from(this).inflate(R.layout.shop_item, null, false);
    double d1;
    if (location() == null)
    {
      d1 = 1.0D;
      if (location() != null)
        break label75;
    }
    while (true)
    {
      localShopListItem.setShop(paramDPObject, -1, d1, d2, true);
      localShopListItem.setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          paramView = new ArrayList();
          paramView.add(new BasicNameValuePair("productId", this.val$shop.getInt("ID") + ""));
          HomeDecorateActivity.this.statisticsEvent("homemain6", "homemain6_recshop_click", "", 0, paramView);
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + this.val$shop.getInt("ID")));
          HomeDecorateActivity.this.startActivity(paramView);
        }
      });
      return localShopListItem;
      d1 = location().latitude();
      break;
      label75: d2 = location().longitude();
    }
  }

  private void setUpView()
  {
    setContentView(R.layout.home_decorate_layout);
    this.linearLayout = ((LinearLayout)findViewById(R.id.home_decorate_list_view));
    if ((this.shopList != null) && (this.shopList.length > 0))
    {
      int i = 0;
      while (i < this.shopList.length)
      {
        this.linearLayout.addView(createShopItem(this.shopList[i]));
        localObject = new View(this);
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 1);
        localLayoutParams.setMargins(ViewUtils.dip2px(this, 10.0F), 0, 0, 0);
        ((View)localObject).setLayoutParams(localLayoutParams);
        ((View)localObject).setBackgroundColor(getResources().getColor(R.color.line_gray));
        this.linearLayout.addView((View)localObject);
        i += 1;
      }
      Object localObject = new TextView(this);
      ((TextView)localObject).setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
      ((TextView)localObject).setGravity(17);
      ((TextView)localObject).setPadding(0, ViewUtils.dip2px(this, 15.0F), 0, ViewUtils.dip2px(this, 15.0F));
      ((TextView)localObject).setTextSize(2, 18.0F);
      ((TextView)localObject).setText("查看全部设计公司");
      ((TextView)localObject).setTextColor(getResources().getColor(R.color.text_color_black));
      ((TextView)localObject).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          HomeDecorateActivity.this.statisticsEvent("homemain6", "homemain6_recshop_more", "", 0, null);
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://categoryshoplist?categoryid=" + HomeDecorateActivity.this.categoryId));
          HomeDecorateActivity.this.startActivity(paramView);
        }
      });
      this.linearLayout.addView((View)localObject);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Uri localUri = getIntent().getData();
    if (localUri != null)
      this.categoryId = localUri.getQueryParameter("categoryid");
    while (true)
    {
      statisticsEvent("homemain6", "homemain6_recshop", "", 0, null);
      paramBundle = Uri.parse("http://m.api.dianping.com/wedding/homedecoraterecommend.bin").buildUpon();
      paramBundle.appendQueryParameter("cityid", "" + cityId());
      paramBundle.appendQueryParameter("categoryid", "" + this.categoryId);
      this.decorateRequest = BasicMApiRequest.mapiGet(paramBundle.build().toString(), CacheType.DISABLED);
      mapiService().exec(this.decorateRequest, this);
      setUpView();
      return;
      if (paramBundle == null)
        continue;
      this.categoryId = paramBundle.getString("categoryid");
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.decorateRequest)
      this.decorateRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    int i;
    if (paramMApiRequest == this.decorateRequest)
    {
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject[])))
      {
        this.shopList = ((DPObject[])(DPObject[])paramMApiResponse.result());
        if ((this.shopList != null) && (this.shopList.length > 0))
        {
          paramMApiRequest = new StringBuilder();
          i = 0;
          while (i < this.shopList.length)
          {
            paramMApiRequest.append(this.shopList[i].getInt("ID")).append(",");
            i += 1;
          }
          if (paramMApiRequest.length() > 0)
            paramMApiRequest.deleteCharAt(paramMApiRequest.length() - 1);
          paramMApiRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/getshopeventlist.bin").buildUpon().appendQueryParameter("shopids", paramMApiRequest.toString()).build().toString(), CacheType.DISABLED);
          mapiService().exec(paramMApiRequest, this);
        }
      }
      this.decorateRequest = null;
    }
    do
      return;
    while ((paramMApiResponse.result() == null) || (!(paramMApiResponse.result() instanceof DPObject)));
    paramMApiResponse = Arrays.asList(((DPObject)paramMApiResponse.result()).getArray("EventList"));
    if ((paramMApiResponse != null) && (paramMApiResponse.size() > 0))
    {
      paramMApiRequest = new HashMap();
      i = 0;
      while (true)
      {
        if ((i >= paramMApiResponse.size()) || (paramMApiResponse.get(i) == null))
        {
          i = 0;
          while (i < this.shopList.length)
          {
            paramMApiResponse = (String)paramMApiRequest.get("" + this.shopList[i].getInt("ID"));
            if (!TextUtils.isEmpty(paramMApiResponse))
            {
              paramMApiResponse = this.shopList[i].edit().putString("EventText", paramMApiResponse).generate();
              this.shopList[i] = paramMApiResponse;
            }
            i += 1;
          }
        }
        paramMApiRequest.put("" + ((DPObject)paramMApiResponse.get(i)).getInt("ShopId"), ((DPObject)paramMApiResponse.get(i)).getString("EventTag"));
        i += 1;
      }
    }
    setUpView();
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("categoryid", this.categoryId);
    super.onSaveInstanceState(paramBundle);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.home.activity.HomeDecorateActivity
 * JD-Core Version:    0.6.0
 */