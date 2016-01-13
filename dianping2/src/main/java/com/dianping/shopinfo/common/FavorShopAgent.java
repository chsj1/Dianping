package com.dianping.shopinfo.common;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.advertisement.AdClientUtils;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopPower;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.Location;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.DeviceUtils;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FavorShopAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final int AD_GA_CLICK = 2;
  private static final int AD_GA_REVEAL = 3;
  private static final int AD_GA_SHOW = 1;
  private static final String TAG = "FavorShopAgent";
  private final int ACTIVITY_DISPLAY_ID = 119;
  private String CELL_INDEX = "8900Favor.10shop";
  private final int COMMON_SHOP_DISPLAY_ID = 110;
  private final int HUI_DISPLAY_ID = 150;
  private final int PAD_DISPLAY_ID = 0;
  private final int PROMO_DISCOUNT_DISPLAY_ID = 130;
  private final int PROMO_DISPLAY_ID = 112;
  private final int PROMO_STRUCTURED_INFO_DISPLAY_ID = 131;
  private final int SPECIAL_SHOP_DISPLAY_ID = 114;
  private final int TUAN_DISPLAY_ID = 140;
  private LinearLayout adContent;
  private NovaLinearLayout commCell;
  private DPObject cpmAd;
  private ArrayList<DPObject> cpmAdList;
  protected MApiRequest cpmAddsRequest;
  HashSet<Record> gaRecordSet = new HashSet();
  private Handler handler = new Handler();
  private boolean isReqAd = false;
  private LinearLayout lastRow = null;
  private ScrollView scrollView;

  public FavorShopAgent(Object paramObject)
  {
    super(paramObject);
  }

  private boolean addActivity(DPObject paramDPObject, int paramInt1, int paramInt2)
  {
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.shopinfo_favor_campaign_item, getParentView(), false);
    localNovaLinearLayout.setGAString("favor_campaign", null, paramInt2);
    ((DPActivity)getContext()).addGAView(localNovaLinearLayout, paramInt2);
    NetworkImageView localNetworkImageView = (NetworkImageView)localNovaLinearLayout.findViewById(R.id.shop_image);
    TextView localTextView1 = (TextView)localNovaLinearLayout.findViewById(R.id.activity_name);
    TextView localTextView2 = (TextView)localNovaLinearLayout.findViewById(R.id.desc);
    localNetworkImageView.setImage(paramDPObject.getString("ImgUrl"));
    setMarkText(paramDPObject, localNovaLinearLayout);
    localTextView1.setText(paramDPObject.getString("Title"));
    localTextView2.setText(paramDPObject.getString("Content"));
    addView(localNovaLinearLayout, paramDPObject, paramInt2);
    return true;
  }

  private boolean addCPM(DPObject paramDPObject, int paramInt1, int paramInt2)
  {
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.shopinfo_favor_item, getParentView(), false);
    localNovaLinearLayout.setGAString("favor", null, paramInt2);
    ((DPActivity)getContext()).addGAView(localNovaLinearLayout, paramInt2);
    Object localObject = (NetworkImageView)localNovaLinearLayout.findViewById(R.id.shop_image);
    ShopPower localShopPower = (ShopPower)localNovaLinearLayout.findViewById(R.id.shop_power);
    TextView localTextView1 = (TextView)localNovaLinearLayout.findViewById(R.id.desc);
    TextView localTextView2 = (TextView)localNovaLinearLayout.findViewById(R.id.shop_name);
    TextView localTextView3 = (TextView)localNovaLinearLayout.findViewById(R.id.region);
    TextView localTextView4 = (TextView)localNovaLinearLayout.findViewById(R.id.distance);
    ((NetworkImageView)localObject).setImage(paramDPObject.getString("ImgUrl"));
    setMarkText(paramDPObject, localNovaLinearLayout);
    localObject = paramDPObject.getString("BranchName");
    StringBuilder localStringBuilder = new StringBuilder().append(paramDPObject.getString("ShopName"));
    if ((localObject == null) || (((String)localObject).length() == 0))
    {
      localObject = "";
      localTextView2.setText((String)localObject);
      if (!TextUtils.isEmpty(paramDPObject.getString("RegionName")))
        break label304;
      localTextView3.setVisibility(8);
      label219: if (!TextUtils.isEmpty(paramDPObject.getString("SecondCategory")))
        break label318;
      localTextView1.setVisibility(8);
    }
    while (true)
    {
      localTextView4.setText(paramDPObject.getString("DistanceStr"));
      localShopPower.setPower(paramDPObject.getInt("Star"));
      addView(localNovaLinearLayout, paramDPObject, paramInt2);
      return true;
      localObject = "(" + (String)localObject + ")";
      break;
      label304: localTextView3.setText(paramDPObject.getString("RegionName"));
      break label219;
      label318: localTextView1.setText(paramDPObject.getString("SecondCategory"));
    }
  }

  private boolean addHui(DPObject paramDPObject, int paramInt1, int paramInt2)
  {
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.shopinfo_favor_hui, getParentView(), false);
    localNovaLinearLayout.setGAString("favor", null, paramInt2);
    ((DPActivity)getContext()).addGAView(localNovaLinearLayout, paramInt2);
    Object localObject = (NetworkImageView)localNovaLinearLayout.findViewById(R.id.shop_image);
    ShopPower localShopPower = (ShopPower)localNovaLinearLayout.findViewById(R.id.shop_power);
    TextView localTextView1 = (TextView)localNovaLinearLayout.findViewById(R.id.desc);
    TextView localTextView2 = (TextView)localNovaLinearLayout.findViewById(R.id.shop_name);
    TextView localTextView3 = (TextView)localNovaLinearLayout.findViewById(R.id.structured_info);
    TextView localTextView4 = (TextView)localNovaLinearLayout.findViewById(R.id.distance);
    ((NetworkImageView)localObject).setImage(paramDPObject.getString("ImgUrl"));
    setMarkText(paramDPObject, localNovaLinearLayout);
    localObject = paramDPObject.getString("BranchName");
    StringBuilder localStringBuilder = new StringBuilder().append(paramDPObject.getString("ShopName"));
    if ((localObject == null) || (((String)localObject).length() == 0))
    {
      localObject = "";
      localTextView2.setText((String)localObject);
      if (!TextUtils.isEmpty(paramDPObject.getString("StructuredInfo")))
        break label305;
      localTextView3.setVisibility(8);
      label220: if (!TextUtils.isEmpty(paramDPObject.getString("SecondCategory")))
        break label320;
      localTextView1.setVisibility(8);
    }
    while (true)
    {
      localTextView4.setText(paramDPObject.getString("DistanceStr"));
      localShopPower.setPower(paramDPObject.getInt("Star"));
      addView(localNovaLinearLayout, paramDPObject, paramInt2);
      return true;
      localObject = "(" + (String)localObject + ")";
      break;
      label305: localTextView3.setText(paramDPObject.getString("StructuredInfo"));
      break label220;
      label320: localTextView1.setText(paramDPObject.getString("SecondCategory"));
    }
  }

  private boolean addPromo(DPObject paramDPObject, int paramInt1, int paramInt2)
  {
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.shopinfo_favor_promo_item, getParentView(), false);
    localNovaLinearLayout.setGAString("favor_promo_tuan", null, paramInt2);
    ((DPActivity)getContext()).addGAView(localNovaLinearLayout, paramInt2);
    Object localObject = (NetworkImageView)localNovaLinearLayout.findViewById(R.id.shop_image);
    TextView localTextView1 = (TextView)localNovaLinearLayout.findViewById(R.id.shop_name);
    TextView localTextView2 = (TextView)localNovaLinearLayout.findViewById(R.id.promo_content);
    TextView localTextView3 = (TextView)localNovaLinearLayout.findViewById(R.id.promo_structured_info);
    TextView localTextView4 = (TextView)localNovaLinearLayout.findViewById(R.id.promo_price);
    TextView localTextView5 = (TextView)localNovaLinearLayout.findViewById(R.id.promo_origin_price);
    TextView localTextView6 = (TextView)localNovaLinearLayout.findViewById(R.id.tag);
    ((NetworkImageView)localObject).setImage(paramDPObject.getString("ImgUrl"));
    setMarkText(paramDPObject, localNovaLinearLayout);
    localObject = paramDPObject.getString("BranchName");
    StringBuilder localStringBuilder = new StringBuilder().append(paramDPObject.getString("ShopName"));
    if ((localObject == null) || (((String)localObject).length() == 0))
    {
      localObject = "";
      localTextView1.setText((String)localObject);
      localTextView2.setText(paramDPObject.getString("Content"));
      if (!TextUtils.isEmpty(paramDPObject.getString("Tag")))
        break label338;
      localTextView6.setVisibility(8);
      switch (paramInt1)
      {
      default:
        label245: Log.w("FavorShopAgent", "unknown displayID");
      case 112:
      case 131:
      case 130:
      case 140:
      }
    }
    while (true)
    {
      addView(localNovaLinearLayout, paramDPObject, paramInt2);
      return true;
      localObject = "(" + (String)localObject + ")";
      break;
      label338: localTextView6.setText(paramDPObject.getString("Tag"));
      break label245;
      fillStructuredInfoContent(localTextView3, paramDPObject.getString("StructuredInfo"));
      continue;
      fillPrice(localTextView4, paramDPObject.getString("Price"));
      fillOriginPrice(localTextView5, paramDPObject.getString("OriginalPrice"));
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
      this.adContent.addView(this.lastRow);
    }
    int i = (getContext().getResources().getDisplayMetrics().widthPixels - ViewUtils.dip2px(getContext(), 40.0F)) / 2;
    int j = (int)(i * 0.75D);
    Object localObject = (FrameLayout)paramView.findViewById(R.id.shop_frame);
    ViewGroup.LayoutParams localLayoutParams = ((FrameLayout)localObject).getLayoutParams();
    localLayoutParams.width = i;
    localLayoutParams.height = j;
    ((FrameLayout)localObject).setLayoutParams(localLayoutParams);
    localObject = new LinearLayout.LayoutParams(-2, -2);
    ((LinearLayout.LayoutParams)localObject).gravity = 16;
    i = ViewUtils.dip2px(getContext(), 5.0F);
    ((LinearLayout.LayoutParams)localObject).setMargins(i, i, i, i);
    paramView.setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.lastRow.addView(paramView);
    paramView.setOnClickListener(new View.OnClickListener(paramDPObject, paramInt)
    {
      public void onClick(View paramView)
      {
        try
        {
          String str1 = this.val$adItem.getString("ClickURL");
          paramView = null;
          if (!TextUtils.isEmpty(str1))
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(str1));
          while (true)
          {
            if (paramView != null)
              FavorShopAgent.this.startActivity(paramView);
            FavorShopAgent.this.record(this.val$adItem, 2, this.val$index);
            FavorShopAgent.this.statisticsEvent("shopinfo5", "shopinfo5_cpm", "", 0);
            return;
            if (this.val$adItem.getInt("ShopID") <= 0)
              continue;
            String str2 = this.val$adItem.getString("Feedback");
            str1 = "dianping://shopinfo?id=" + this.val$adItem.getInt("ShopID");
            paramView = str1;
            if (!TextUtils.isEmpty(str2))
              paramView = str1 + "&_fb_=" + str2;
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
            paramView.putExtra("shop", this.val$adItem);
          }
        }
        catch (Exception paramView)
        {
          Log.e("FavorShopAgent", "click error", paramView);
        }
      }
    });
  }

  private void fillOriginPrice(TextView paramTextView, String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      paramString = new SpannableString("￥" + paramString);
      paramString.setSpan(new StrikethroughSpan(), 1, paramString.length(), 33);
      paramTextView.setText(paramString);
      paramTextView.setVisibility(0);
    }
  }

  private void fillPrice(TextView paramTextView, String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      paramTextView.setText("￥" + paramString);
      paramTextView.setVisibility(0);
    }
  }

  private void fillStructuredInfoContent(TextView paramTextView, String paramString)
  {
    try
    {
      if (!TextUtils.isEmpty(paramString))
      {
        SpannableString localSpannableString = new SpannableString(paramString);
        paramString = Pattern.compile("(-?\\d+)(\\.\\d+)?", 2).matcher(paramString);
        if (paramString.find())
          localSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tuan_common_orange)), paramString.start(), paramString.end(), 18);
        paramTextView.setText(localSpannableString);
        paramTextView.setVisibility(0);
      }
      return;
    }
    catch (Exception paramTextView)
    {
      Log.e("FavorShopAgent", paramTextView.getMessage(), paramTextView);
    }
  }

  private void onReveal()
  {
    this.scrollView = getFragment().getScrollView();
    if ((this.scrollView != null) && (this.adContent != null))
    {
      Rect localRect = new Rect();
      this.scrollView.getHitRect(localRect);
      int i = 0;
      while (i < this.adContent.getChildCount())
      {
        View localView = this.adContent.getChildAt(i);
        if ((localView.getLocalVisibleRect(localRect)) && ((localView instanceof LinearLayout)))
        {
          int j = i * 2;
          if (j < this.cpmAdList.size())
            record((DPObject)this.cpmAdList.get(j), 3, j);
          j += 1;
          if (j < this.cpmAdList.size())
            record((DPObject)this.cpmAdList.get(j), 3, j);
        }
        i += 1;
      }
    }
  }

  private void record(int paramInt)
  {
    if (this.cpmAdList != null)
    {
      int i = 0;
      while (i < this.cpmAdList.size())
      {
        record((DPObject)this.cpmAdList.get(i), paramInt, i);
        i += 1;
      }
    }
  }

  private void record(DPObject paramDPObject, int paramInt1, int paramInt2)
  {
    if (paramDPObject != null)
    {
      Object localObject = new Record(paramDPObject, paramInt1);
      if (!this.gaRecordSet.contains(localObject))
      {
        this.gaRecordSet.add(localObject);
        localObject = new HashMap();
        ((Map)localObject).put("act", String.valueOf(paramInt1));
        ((Map)localObject).put("adidx", String.valueOf(paramInt2 + 1));
        AdClientUtils.report(paramDPObject.getString("Feedback"), (Map)localObject);
      }
    }
  }

  private void setMarkText(DPObject paramDPObject, NovaLinearLayout paramNovaLinearLayout)
  {
    paramNovaLinearLayout = (TextView)paramNovaLinearLayout.findViewById(R.id.mark_text);
    if (!TextUtils.isEmpty(paramDPObject.getString("Mark")))
    {
      paramNovaLinearLayout.setVisibility(0);
      paramNovaLinearLayout.setText(paramDPObject.getString("Mark"));
    }
  }

  private void statistics()
  {
    this.scrollView = getFragment().getScrollView();
    if (this.scrollView != null)
      this.scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener()
      {
        public void onScrollChanged()
        {
          FavorShopAgent.this.onReveal();
        }
      });
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if (getShop() == null);
    do
    {
      do
      {
        do
          return;
        while ((getShop().getInt("CategoryID") == 6700) || (getShopStatus() != 0));
        if (this.isReqAd)
          continue;
        reqCpmAds();
        return;
      }
      while ((this.cpmAdList == null) || (this.cpmAdList.size() <= 0));
      this.commCell = ((NovaLinearLayout)this.res.inflate(getContext(), R.layout.shopinfo_favor_shop, getParentView(), false));
      this.adContent = ((LinearLayout)this.commCell.findViewById(R.id.content));
      ((TextView)this.commCell.findViewById(R.id.sub_title)).setText(this.cpmAd.getString("Tag"));
      statistics();
      int i = 0;
      if (i >= this.cpmAdList.size())
        continue;
      paramBundle = (DPObject)this.cpmAdList.get(i);
      int j = paramBundle.getInt("DisplayID");
      switch (j)
      {
      default:
        Log.w("FavorShopAgent", "unknown displayID");
      case 110:
      case 114:
      case 0:
      case 119:
      case 112:
      case 130:
      case 131:
      case 140:
      case 150:
      }
      while (true)
      {
        i += 1;
        break;
        addCPM(paramBundle, j, i);
        continue;
        addActivity(paramBundle, j, i);
        continue;
        addPromo(paramBundle, j, i);
        continue;
        addHui(paramBundle, j, i);
      }
    }
    while (this.adContent.getChildCount() <= 0);
    addCell(this.CELL_INDEX, this.commCell, "cpm", 0);
    this.handler.postDelayed(new Runnable()
    {
      public void run()
      {
        FavorShopAgent.this.onReveal();
      }
    }
    , 800L);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (getFragment() == null);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.cpmAddsRequest != null)
    {
      getFragment().mapiService().abort(this.cpmAddsRequest, this, true);
      this.cpmAddsRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.cpmAddsRequest)
      this.cpmAddsRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.cpmAddsRequest)
    {
      this.cpmAddsRequest = null;
      this.isReqAd = true;
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        this.cpmAd = ((DPObject)paramMApiResponse.result());
        if (this.cpmAd.getArray("ShopAdList") != null)
        {
          this.cpmAdList = new ArrayList(Arrays.asList(this.cpmAd.getArray("ShopAdList")));
          dispatchAgentChanged(false);
          record(1);
        }
      }
    }
  }

  public void onResume()
  {
    super.onResume();
    onReveal();
  }

  protected void reqCpmAds()
  {
    if (this.cpmAddsRequest != null)
      getFragment().mapiService().abort(this.cpmAddsRequest, this, true);
    if ((getFragment() == null) || (getShop() == null))
      return;
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/midas/appshopads.bin").buildUpon().appendQueryParameter("cityId", String.valueOf(cityId())).appendQueryParameter("viewShopId", String.valueOf(shopId())).appendQueryParameter("shopType", String.valueOf(getShop().getInt("ShopType"))).appendQueryParameter("categoryId", String.valueOf(getShop().getInt("CategoryID"))).appendQueryParameter("categoryName", getShop().getString("CategoryName")).appendQueryParameter("slotId", String.valueOf(10004)).appendQueryParameter("dpId", DeviceUtils.dpid()).appendQueryParameter("shopCityId", String.valueOf(getShop().getInt("CityID")));
    if (location() != null)
      localBuilder.appendQueryParameter("longitude", String.valueOf(location().longitude())).appendQueryParameter("latitude", String.valueOf(location().latitude()));
    String str = accountService().token();
    if (!TextUtils.isEmpty(str))
      localBuilder.appendQueryParameter("token", str);
    this.cpmAddsRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.cpmAddsRequest, this);
  }

  private class Record
  {
    private int act;
    private DPObject cpmAd;

    public Record(DPObject paramInt, int arg3)
    {
      this.cpmAd = paramInt;
      int i;
      this.act = i;
    }

    public boolean equals(Object paramObject)
    {
      int j = 0;
      int i = j;
      if ((paramObject instanceof Record))
      {
        paramObject = (Record)paramObject;
        i = j;
        if (paramObject.cpmAd.equals(this.cpmAd))
        {
          i = j;
          if (paramObject.act == this.act)
            i = 1;
        }
      }
      return i;
    }

    public int hashCode()
    {
      return this.cpmAd.hashCode() + this.act;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.FavorShopAgent
 * JD-Core Version:    0.6.0
 */