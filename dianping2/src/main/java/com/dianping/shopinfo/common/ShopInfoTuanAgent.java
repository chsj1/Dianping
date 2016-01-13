package com.dianping.shopinfo.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.speed.SpeedMonitorHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.activity.ShopInfoActivity;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.shopinfo.widget.TuanTicketCell;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaRelativeLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class ShopInfoTuanAgent extends ShopCellAgent
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  protected static final String CELL_TUAN = "0480Tuan.50Tuan";
  protected static final DecimalFormat PRICE_DF = new DecimalFormat("#.###");
  protected static final String URL = "http://m.api.dianping.com/shop/getdealdiscounts.bin";
  protected DPObject Deals;
  protected DPObject dealDiscountList;
  protected LinearLayout expandLayout;
  protected NovaRelativeLayout expandView;
  protected boolean hasPromoRequested;
  protected boolean hasRequested;
  protected boolean isAutoRefund;
  protected boolean isExpand;
  protected boolean isOverdueAutoRefund;
  protected View line;
  protected LinearLayout linearLayout;
  protected MApiRequest mGouponRequest;
  protected MApiRequest mPromoTagRequest;
  protected String moreText = "";
  protected SparseArray<CommonCell> tuanCells = new SparseArray();

  public ShopInfoTuanAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected CommonCell createDealCell(DPObject paramDPObject, boolean paramBoolean)
  {
    return createDefaultDealCell(paramDPObject, paramBoolean);
  }

  public CommonCell createDefaultDealCell(DPObject paramDPObject, boolean paramBoolean)
  {
    TuanTicketCell localTuanTicketCell = (TuanTicketCell)MyResources.getResource(ShopCellAgent.class).inflate(getContext(), R.layout.tuan_cell_shopinfo_icon, getParentView(), false);
    localTuanTicketCell.setGAString("tuan", getGAExtra());
    localTuanTicketCell.findViewById(R.id.arrow).setVisibility(4);
    localTuanTicketCell.setSubTitle(paramDPObject.getString("ContentTitle"));
    localTuanTicketCell.setClickable(true);
    localTuanTicketCell.setOnClickListener(this);
    double d1 = paramDPObject.getDouble("Price");
    double d2 = paramDPObject.getDouble("OriginalPrice");
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
    SpannableString localSpannableString = new SpannableString("￥" + PRICE_DF.format(d1));
    localSpannableString.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_info)), 0, 1, 33);
    localSpannableString.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_title)), 1, localSpannableString.length(), 33);
    localSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), 0, localSpannableString.length(), 33);
    localSpannableStringBuilder.append(localSpannableString);
    localSpannableStringBuilder.append(" ");
    localSpannableString = new SpannableString("￥" + PRICE_DF.format(d2));
    localSpannableString.setSpan(new StrikethroughSpan(), 1, localSpannableString.length(), 33);
    localSpannableString.setSpan(new AbsoluteSizeSpan(this.res.getDimensionPixelSize(R.dimen.text_size_hint)), 0, localSpannableString.length(), 33);
    localSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_gray)), 0, localSpannableString.length(), 33);
    localSpannableStringBuilder.append(localSpannableString);
    localTuanTicketCell.setTitle(localSpannableStringBuilder);
    localTuanTicketCell.setTag(paramDPObject);
    if (paramDPObject.getString("Photo") != null)
      ((NetworkImageView)localTuanTicketCell.findViewById(R.id.icon)).setImage(paramDPObject.getString("Photo"));
    setBackground(localTuanTicketCell.findViewById(R.id.layout), 0);
    this.tuanCells.append(paramDPObject.getInt("ID"), localTuanTicketCell);
    if ((this.isOverdueAutoRefund) && (!paramDPObject.getBoolean("OverdueAutoRefund")))
      this.isOverdueAutoRefund = false;
    if ((this.isAutoRefund) && (!paramDPObject.getBoolean("IsAutoRefund")))
      this.isAutoRefund = false;
    return localTuanTicketCell;
  }

  protected DPObject getDeals()
  {
    return this.Deals;
  }

  protected int getDisplayCount()
  {
    return 2;
  }

  protected View line()
  {
    this.line = new View(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 1);
    localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 15.0F);
    this.line.setLayoutParams(localLayoutParams);
    this.line.setBackgroundResource(R.color.background_gray);
    return this.line;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    this.tuanCells.clear();
    setupView();
    if ((getContext() instanceof ShopInfoActivity))
      ((ShopInfoActivity)getContext()).getSpeedMonitorHelper().setResponseTime(2, System.currentTimeMillis());
  }

  public void onClick(View paramView)
  {
    if ((paramView.getTag() instanceof DPObject))
    {
      try
      {
        paramView = (DPObject)paramView.getTag();
        Object localObject = getDeals().getString("Link");
        if ((localObject != null) && (((String)localObject).startsWith("http://")))
        {
          Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuan"));
          localIntent.putExtra("url", ((String)localObject).replace("#", String.valueOf(paramView.getInt("ID"))));
          localIntent.putExtra("shopId", shopId());
          localIntent.putExtra("stack", "$");
          getContext().startActivity(localIntent);
        }
        while (true)
        {
          localObject = new ArrayList();
          ((List)localObject).add(new BasicNameValuePair("shopid", shopId() + ""));
          statisticsEvent("shopinfo5", "shopinfo5_tuan", String.valueOf(paramView.getInt("ID")), 0, (List)localObject);
          if (isWeddingType())
          {
            paramView = new ArrayList();
            paramView.add(new BasicNameValuePair("shopid", shopId() + ""));
            statisticsEvent("shopinfow", "shopinfow_tuan", "", 0, paramView);
          }
          if (!isWeddingShopType())
            break;
          paramView = new ArrayList();
          paramView.add(new BasicNameValuePair("shopid", shopId() + ""));
          statisticsEvent("shopinfoq", "shopinfoq_tuan", "", 0, paramView);
          return;
          localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuandeal"));
          ((Intent)localObject).putExtra("deal", paramView);
          ((Intent)localObject).putExtra("shopId", shopId());
          ((Intent)localObject).putExtra("buyLink", getDeals().getString("BuyLink"));
          ((Intent)localObject).putExtra("selectLink", getDeals().getString("SelectLink"));
          ((Intent)localObject).putExtra("detailLink", getDeals().getString("DetailLink"));
          getContext().startActivity((Intent)localObject);
        }
      }
      catch (Exception paramView)
      {
        Log.e("shop", "fail to launch deal", paramView);
        return;
      }
    }
    else if (paramView.getTag() == "EXPAND")
    {
      boolean bool;
      if (!this.isExpand)
      {
        bool = true;
        this.isExpand = bool;
        if (!this.isExpand)
          break label636;
        statisticsEvent("shopinfo5", "shopinfo5_tuan_more", "展开", 0);
        if (isWeddingShopType())
        {
          paramView = new ArrayList();
          paramView.add(new BasicNameValuePair("shopid", shopId() + ""));
          statisticsEvent("shopinfoq", "shopinfoq_tuan_more", "展开", 0, paramView);
        }
      }
      while (true)
      {
        if (isWeddingShopType())
        {
          paramView = new ArrayList();
          paramView.add(new BasicNameValuePair("shopid", shopId() + ""));
          statisticsEvent("shopinfoq", "shopinfoq_tuan_more", "", 0, paramView);
        }
        setExpandAction();
        scrollToCenter();
        return;
        bool = false;
        break;
        label636: statisticsEvent("shopinfo5", "shopinfo5_tuan_more", "收起", 0);
        if (!isWeddingShopType())
          continue;
        paramView = new ArrayList();
        paramView.add(new BasicNameValuePair("shopid", shopId() + ""));
        statisticsEvent("shopinfoq", "shopinfoq_tuan_more", "收起", 0, paramView);
      }
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle == null);
    for (boolean bool = false; ; bool = paramBundle.getBoolean("isExpand"))
    {
      this.isExpand = bool;
      if (paramBundle != null)
      {
        this.Deals = ((DPObject)paramBundle.getParcelable("Deals"));
        this.dealDiscountList = ((DPObject)paramBundle.getParcelable("DealDiscountList"));
      }
      return;
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mGouponRequest != null)
    {
      mapiService().abort(this.mGouponRequest, this, true);
      this.mGouponRequest = null;
    }
    if (this.mPromoTagRequest != null)
    {
      mapiService().abort(this.mPromoTagRequest, this, true);
      this.mPromoTagRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mPromoTagRequest)
    {
      this.mPromoTagRequest = null;
      updateDealSaleCount();
    }
    do
      return;
    while (paramMApiRequest != this.mGouponRequest);
    this.mGouponRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mPromoTagRequest)
    {
      this.mPromoTagRequest = null;
      this.dealDiscountList = ((DPObject)paramMApiResponse.result());
      updateTuanDealTags();
    }
    do
    {
      do
      {
        do
          return;
        while (paramMApiRequest != this.mGouponRequest);
        this.mGouponRequest = null;
        paramMApiRequest = (DPObject)paramMApiResponse.result();
      }
      while (paramMApiRequest == null);
      this.Deals = paramMApiRequest.getObject("Deals");
      ShopInfoActivity.speedTest(getContext(), 1012);
      dispatchAgentChanged(false);
    }
    while (this.hasPromoRequested);
    if (this.mPromoTagRequest != null)
      getFragment().mapiService().abort(this.mPromoTagRequest, this, true);
    requestPromoTag();
    this.hasPromoRequested = true;
  }

  protected void requestPromoTag()
  {
    if (getDeals() == null);
    DPObject[] arrayOfDPObject;
    do
    {
      return;
      arrayOfDPObject = getDeals().getArray("List");
    }
    while ((arrayOfDPObject == null) || (arrayOfDPObject.length == 0));
    String str1 = "";
    int j = arrayOfDPObject.length;
    int i = 0;
    while (i < j)
    {
      DPObject localDPObject = arrayOfDPObject[i];
      String str2 = str1;
      if (!TextUtils.isEmpty(str1))
        str2 = str1 + ",";
      str1 = str2 + localDPObject.getInt("ID");
      i += 1;
    }
    this.mPromoTagRequest = BasicMApiRequest.mapiPost("http://m.api.dianping.com/shop/getdealdiscounts.bin", new String[] { "deals", str1, "cityid", cityId() + "" });
    getFragment().mapiService().exec(this.mPromoTagRequest, this);
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putBoolean("isExpand", this.isExpand);
    localBundle.putParcelable("Deals", this.Deals);
    localBundle.putParcelable("DealDiscountList", this.dealDiscountList);
    return localBundle;
  }

  protected void scrollToCenter()
  {
    if (this.isExpand)
      this.linearLayout.postDelayed(new Runnable()
      {
        public void run()
        {
          ScrollView localScrollView = ShopInfoTuanAgent.this.getFragment().getScrollView();
          localScrollView.setSmoothScrollingEnabled(true);
          try
          {
            localScrollView.requestChildFocus(ShopInfoTuanAgent.this.linearLayout, ShopInfoTuanAgent.this.linearLayout);
            return;
          }
          catch (Exception localException)
          {
          }
        }
      }
      , 200L);
  }

  protected void sendGouponRequest()
  {
    this.mGouponRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/mshop/groupon.bin").buildUpon().appendQueryParameter("shopid", Integer.toString(shopId())).toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mGouponRequest, this);
  }

  protected void setBackground(View paramView, int paramInt)
  {
    int i = paramView.getPaddingBottom();
    int j = paramView.getPaddingTop();
    int k = paramView.getPaddingRight();
    int m = paramView.getPaddingLeft();
    paramView.setBackgroundResource(paramInt);
    paramView.setPadding(m, j, k, i);
  }

  protected void setExpandAction()
  {
    if (this.expandLayout == null)
      return;
    this.expandLayout.postDelayed(new Runnable()
    {
      public void run()
      {
        if (ShopInfoTuanAgent.this.isExpand)
          ShopInfoTuanAgent.this.expandLayout.setVisibility(0);
        while (true)
        {
          ShopInfoTuanAgent.this.setExpandState();
          return;
          ShopInfoTuanAgent.this.expandLayout.setVisibility(8);
        }
      }
    }
    , 100L);
  }

  protected void setExpandState()
  {
    if (this.expandView == null)
      return;
    if (this.isExpand)
    {
      ((ImageView)this.expandView.findViewById(R.id.arrow)).setImageResource(R.drawable.navibar_arrow_up);
      ((TextView)this.expandView.findViewById(16908308)).setText("收起");
      return;
    }
    ((ImageView)this.expandView.findViewById(R.id.arrow)).setImageResource(R.drawable.navibar_arrow_down);
    ((TextView)this.expandView.findViewById(16908308)).setText(this.moreText);
    this.expandView.findViewById(16908308).setVisibility(0);
  }

  protected void setShopDeals()
  {
    DPObject localDPObject = getShop();
    if ((localDPObject != null) && (getShopStatus() == 0))
      setShop(localDPObject.edit().putObject("Deals", getDeals()).generate());
  }

  protected void setupView()
  {
    if (!this.hasRequested)
    {
      ShopInfoActivity.speedTest(getContext(), 1011);
      sendGouponRequest();
      this.hasRequested = true;
    }
    DPObject[] arrayOfDPObject;
    do
    {
      do
        return;
      while (getDeals() == null);
      setShopDeals();
      arrayOfDPObject = getDeals().getArray("List");
    }
    while ((arrayOfDPObject == null) || (arrayOfDPObject.length == 0));
    this.linearLayout = new LinearLayout(getContext());
    this.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    this.linearLayout.setOrientation(1);
    View localView = MyResources.getResource(ShopCellAgent.class).inflate(getContext(), R.layout.shopinfo_tuan_header_view, getParentView(), false);
    this.linearLayout.addView(localView);
    this.isOverdueAutoRefund = true;
    this.isAutoRefund = true;
    this.linearLayout.addView(line());
    this.linearLayout.addView(createDealCell(arrayOfDPObject[0], true));
    int j = getDisplayCount();
    int i;
    int k;
    if (arrayOfDPObject.length <= j)
    {
      i = 1;
      k = arrayOfDPObject.length;
      while (i < k)
      {
        this.linearLayout.addView(line());
        this.linearLayout.addView(createDealCell(arrayOfDPObject[i], false));
        i += 1;
      }
    }
    if (arrayOfDPObject.length > j)
    {
      i = 1;
      while (i < j)
      {
        this.linearLayout.addView(line());
        this.linearLayout.addView(createDealCell(arrayOfDPObject[i], false));
        i += 1;
      }
      this.expandLayout = new LinearLayout(getContext());
      this.expandLayout.setOrientation(1);
      if (!this.isExpand)
        this.expandLayout.setVisibility(8);
      i = j;
      k = arrayOfDPObject.length;
      while (i < k)
      {
        this.expandLayout.addView(line());
        this.expandLayout.addView(createDealCell(arrayOfDPObject[i], false));
        i += 1;
      }
      this.linearLayout.addView(this.expandLayout);
      this.expandView = ((NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.expand, getParentView(), false));
      this.expandView.setTag("EXPAND");
      this.expandView.setPadding(ViewUtils.dip2px(getContext(), 15.0F), 0, 0, 0);
      this.moreText = ("更多" + (arrayOfDPObject.length - j) + "个团购");
      ((TextView)this.expandView.findViewById(16908308)).setText(this.moreText);
      this.expandView.setClickable(true);
      this.expandView.setOnClickListener(this);
      this.linearLayout.addView(this.expandView);
      setExpandState();
    }
    if (!this.isAutoRefund)
    {
      localView.findViewById(R.id.icon_refund).setVisibility(8);
      localView.findViewById(R.id.refund_support_day).setVisibility(8);
      label536: if (this.isOverdueAutoRefund)
        break label629;
      localView.findViewById(R.id.icon_expire).setVisibility(8);
      localView.findViewById(R.id.refund_support_expired).setVisibility(8);
    }
    while (true)
    {
      updateDealSaleCount();
      addCell("0480Tuan.50Tuan", this.linearLayout, 0);
      ShopInfoActivity.speedTest(getContext(), 1013);
      if (this.dealDiscountList == null)
        break;
      updateTuanDealTags();
      return;
      localView.findViewById(R.id.icon_refund).setVisibility(0);
      localView.findViewById(R.id.refund_support_day).setVisibility(0);
      break label536;
      label629: localView.findViewById(R.id.icon_expire).setVisibility(0);
      localView.findViewById(R.id.refund_support_expired).setVisibility(0);
    }
  }

  protected void updateDealSaleCount()
  {
    if (!isHotelType())
    {
      int i = 0;
      int j = this.tuanCells.size();
      while (i < j)
      {
        TuanTicketCell localTuanTicketCell = (TuanTicketCell)(CommonCell)this.tuanCells.valueAt(i);
        localTuanTicketCell.setSaleCount(((DPObject)localTuanTicketCell.getTag()).getString("SalesDesc"));
        i += 1;
      }
    }
  }

  protected void updateTuanDealTags()
  {
    if ((this.tuanCells == null) || (this.dealDiscountList == null));
    DPObject[] arrayOfDPObject;
    do
    {
      return;
      arrayOfDPObject = this.dealDiscountList.getArray("DealDiscountItems");
    }
    while ((arrayOfDPObject == null) || (arrayOfDPObject.length == 0));
    SparseArray localSparseArray = new SparseArray();
    int i = 0;
    int j = arrayOfDPObject.length;
    while (i < j)
    {
      localSparseArray.append(arrayOfDPObject[i].getInt("DealId"), arrayOfDPObject[i].getString("DiscountDesc"));
      i += 1;
    }
    i = 0;
    j = this.tuanCells.size();
    label95: int k;
    if (i < j)
    {
      k = this.tuanCells.keyAt(i);
      if (localSparseArray.get(k) != null)
        break label127;
    }
    while (true)
    {
      i += 1;
      break label95;
      break;
      label127: ((CommonCell)this.tuanCells.valueAt(i)).setRightText((String)localSparseArray.get(k));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.ShopInfoTuanAgent
 * JD-Core Version:    0.6.0
 */