package com.dianping.shopinfo.wed.home.design;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.UserProfile;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import java.net.URLEncoder;

public class HomeDesignFeatureAgent extends ShopCellAgent
{
  private static final String CELL_HOMEDESIGN_PRICE = "0310HomeDesign.01Feature";
  DPObject[] featureList;
  private int[] imageList = { R.drawable.yanfang, R.drawable.liangfang, R.drawable.sheji };
  boolean isSend = false;
  MApiRequest request;
  private RequestHandler<MApiRequest, MApiResponse> requestHandler;
  DPObject shop;

  public HomeDesignFeatureAgent(Object paramObject)
  {
    super(paramObject);
  }

  @SuppressLint({"NewApi"})
  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (!isHomeDesignShopType());
    do
      return;
    while ((this.featureList == null) || (this.featureList.length <= 0));
    paramBundle = this.res.inflate(getContext(), R.layout.home_design_feature_layout, getParentView(), false);
    int m = Build.VERSION.SDK_INT;
    int i = 0;
    if (i < this.featureList.length)
    {
      int j;
      if (i == 0)
      {
        localLinearLayout = (LinearLayout)paramBundle.findViewById(R.id.first_feature);
        localLinearLayout.setVisibility(0);
        localTextView1 = (TextView)localLinearLayout.findViewById(R.id.name_first);
        localTextView2 = (TextView)localLinearLayout.findViewById(R.id.cost_first);
        localTextView3 = (TextView)localLinearLayout.findViewById(R.id.useraction_first);
        str1 = this.featureList[i].getString("Name");
        str2 = this.featureList[i].getString("Cost");
        str3 = this.featureList[i].getString("UserAction");
        str4 = this.featureList[i].getString("Url");
        k = this.featureList[i].getInt("Sign");
        if (k >= 1)
        {
          j = k;
          if (k <= 3);
        }
        else
        {
          j = 1;
        }
        localTextView1.setText(str1);
        if (m < 16)
        {
          localTextView1.setBackgroundDrawable(this.res.getDrawable(this.imageList[(j - 1)]));
          label244: localTextView2.setText(str2);
          localTextView3.setText(str3);
          localLinearLayout.setOnClickListener(new ClickListener(str4, j));
        }
      }
      do
      {
        i += 1;
        break;
        localTextView1.setBackground(this.res.getDrawable(this.imageList[(j - 1)]));
        break label244;
        if (i != 1)
          continue;
        localLinearLayout = (LinearLayout)paramBundle.findViewById(R.id.second_feature);
        localLinearLayout.setVisibility(0);
        localTextView1 = (TextView)localLinearLayout.findViewById(R.id.name_second);
        localTextView2 = (TextView)localLinearLayout.findViewById(R.id.cost_second);
        localTextView3 = (TextView)localLinearLayout.findViewById(R.id.useraction_second);
        str1 = this.featureList[i].getString("Name");
        str2 = this.featureList[i].getString("Cost");
        str3 = this.featureList[i].getString("UserAction");
        str4 = this.featureList[i].getString("Url");
        k = this.featureList[i].getInt("Sign");
        if (k >= 1)
        {
          j = k;
          if (k <= 3);
        }
        else
        {
          j = 1;
        }
        localTextView1.setText(str1);
        if (m < 16)
          localTextView1.setBackgroundDrawable(this.res.getDrawable(this.imageList[(j - 1)]));
        while (true)
        {
          localTextView2.setText(str2);
          localTextView3.setText(str3);
          localLinearLayout.setOnClickListener(new ClickListener(str4, j));
          break;
          localTextView1.setBackground(this.res.getDrawable(this.imageList[(j - 1)]));
        }
      }
      while (i != 2);
      LinearLayout localLinearLayout = (LinearLayout)paramBundle.findViewById(R.id.third_feature);
      localLinearLayout.setVisibility(0);
      TextView localTextView1 = (TextView)localLinearLayout.findViewById(R.id.name_third);
      TextView localTextView2 = (TextView)localLinearLayout.findViewById(R.id.cost_third);
      TextView localTextView3 = (TextView)localLinearLayout.findViewById(R.id.useraction_third);
      String str1 = this.featureList[i].getString("Name");
      String str2 = this.featureList[i].getString("Cost");
      String str3 = this.featureList[i].getString("UserAction");
      String str4 = this.featureList[i].getString("Url");
      int k = this.featureList[i].getInt("Sign");
      if (k >= 1)
      {
        j = k;
        if (k <= 3);
      }
      else
      {
        j = 1;
      }
      localTextView1.setText(str1);
      if (m < 16)
        localTextView1.setBackgroundDrawable(this.res.getDrawable(this.imageList[(j - 1)]));
      while (true)
      {
        localTextView2.setText(str2);
        localTextView3.setText(str3);
        localLinearLayout.setOnClickListener(new ClickListener(str4, j));
        break;
        localTextView1.setBackground(this.res.getDrawable(this.imageList[(j - 1)]));
      }
    }
    addCell("0310HomeDesign.01Feature", paramBundle);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!isHomeDesignShopType());
    do
    {
      return;
      this.shop = getShop();
    }
    while ((this.shop == null) || (shopId() <= 0) || (this.isSend));
    paramBundle = new StringBuffer("http://m.api.dianping.com/wedding/homedesignfeature.bin?");
    paramBundle.append("shopid=").append(shopId());
    this.request = BasicMApiRequest.mapiGet(paramBundle.toString(), CacheType.DISABLED);
    this.requestHandler = new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        HomeDesignFeatureAgent.this.request = null;
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (HomeDesignFeatureAgent.this.request == paramMApiRequest)
        {
          HomeDesignFeatureAgent.this.isSend = true;
          HomeDesignFeatureAgent.this.request = null;
          if ((paramMApiResponse != null) && (paramMApiResponse.result() != null) && ((paramMApiResponse.result() instanceof DPObject[])))
          {
            HomeDesignFeatureAgent.this.featureList = ((DPObject[])(DPObject[])paramMApiResponse.result());
            if ((HomeDesignFeatureAgent.this.featureList != null) && (HomeDesignFeatureAgent.this.featureList.length > 0))
              HomeDesignFeatureAgent.this.dispatchAgentChanged(false);
          }
        }
      }
    };
    getFragment().mapiService().exec(this.request, this.requestHandler);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if ((this.request != null) && (getFragment() != null) && (getFragment().mapiService() != null))
      getFragment().mapiService().abort(this.request, this.requestHandler, true);
  }

  private class ClickListener
    implements View.OnClickListener
  {
    private int sign;
    private String url;

    public ClickListener(String paramInt, int arg3)
    {
      this.url = paramInt;
      int i;
      this.sign = i;
    }

    public void onClick(View paramView)
    {
      if (TextUtils.isEmpty(this.url))
        return;
      paramView = "";
      try
      {
        Object localObject = HomeDesignFeatureAgent.this.getAccount();
        if (localObject != null)
          paramView = ((UserProfile)localObject).id() + "";
        if (!TextUtils.isEmpty(paramView))
          this.url = (this.url + "&userId=" + paramView);
        paramView = URLEncoder.encode(this.url, "UTF-8");
        localObject = new Intent("android.intent.action.VIEW");
        ((Intent)localObject).setData(Uri.parse("dianping://weddinghotelweb?url=" + paramView));
        HomeDesignFeatureAgent.this.startActivity((Intent)localObject);
        paramView = HomeDesignFeatureAgent.this.getGAExtra();
        paramView.shop_id = Integer.valueOf(HomeDesignFeatureAgent.this.shop.getInt("ID"));
        paramView.index = Integer.valueOf(this.sign);
        GAHelper.instance().contextStatisticsEvent(HomeDesignFeatureAgent.this.getContext(), "serve_detail", paramView, "tap");
        return;
      }
      catch (java.io.UnsupportedEncodingException paramView)
      {
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.home.design.HomeDesignFeatureAgent
 * JD-Core Version:    0.6.0
 */