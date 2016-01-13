package com.dianping.shopinfo.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
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
import com.dianping.widget.view.NovaRelativeLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class CouponAgent extends ShopCellAgent
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  protected static final String CELL_TUAN = "0475HuiPay.20Hui";
  protected static final DecimalFormat PRICE_DF = new DecimalFormat("#.###");
  private static final String URL = "http://m.api.dianping.com/shop/getdealdiscounts.bin";
  protected DPObject couponDeals;
  protected DPObject dealDiscountList;
  protected LinearLayout expandLayout;
  protected NovaRelativeLayout expandView;
  protected boolean hasPromoRequested;
  protected boolean isExpand;
  protected View line;
  protected LinearLayout linearLayout;
  private MApiRequest mPromoTagRequest;
  protected String moreText = "";
  protected SparseArray<CommonCell> tuanCells = new SparseArray();

  public CouponAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void requestPromoTag()
  {
    if (this.couponDeals == null);
    DPObject[] arrayOfDPObject;
    do
    {
      return;
      arrayOfDPObject = this.couponDeals.getArray("CouponDeals");
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

  private void scrollToCenter()
  {
    if (this.isExpand)
      this.linearLayout.postDelayed(new Runnable()
      {
        public void run()
        {
          ScrollView localScrollView = CouponAgent.this.getFragment().getScrollView();
          localScrollView.setSmoothScrollingEnabled(true);
          try
          {
            localScrollView.requestChildFocus(CouponAgent.this.linearLayout, CouponAgent.this.linearLayout);
            return;
          }
          catch (Exception localException)
          {
          }
        }
      }
      , 200L);
  }

  private void setExpandAction()
  {
    if (this.expandLayout == null)
      return;
    this.expandLayout.postDelayed(new Runnable()
    {
      public void run()
      {
        if (CouponAgent.this.isExpand)
          CouponAgent.this.expandLayout.setVisibility(0);
        while (true)
        {
          CouponAgent.this.setExpandState();
          return;
          CouponAgent.this.expandLayout.setVisibility(8);
        }
      }
    }
    , 100L);
  }

  public CommonCell createDefaultDealCell(DPObject paramDPObject, int paramInt)
  {
    TuanTicketCell localTuanTicketCell = (TuanTicketCell)MyResources.getResource(ShopCellAgent.class).inflate(getContext(), R.layout.coupon_cell_shopinfo_icon, getParentView(), false);
    localTuanTicketCell.setGAString("quan_ai", "", paramInt);
    if (paramInt == 0)
      localTuanTicketCell.findViewById(16908295).setVisibility(0);
    while (true)
    {
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
      setBackground(localTuanTicketCell.findViewById(R.id.layout), 0);
      this.tuanCells.append(paramDPObject.getInt("ID"), localTuanTicketCell);
      return localTuanTicketCell;
      localTuanTicketCell.findViewById(16908295).setVisibility(4);
    }
  }

  protected View line()
  {
    this.line = new View(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 1);
    localLayoutParams.leftMargin = ViewUtils.dip2px(getContext(), 47.0F);
    this.line.setLayoutParams(localLayoutParams);
    this.line.setBackgroundResource(R.color.background_gray);
    return this.line;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    this.tuanCells.clear();
    this.couponDeals = ((DPObject)super.getSharedObject("CouponDeals"));
    if ((this.couponDeals == null) && (paramBundle != null))
      this.couponDeals = ((DPObject)paramBundle.getParcelable("CouponDeals"));
    if (this.couponDeals == null)
      return;
    setupView();
  }

  public void onClick(View paramView)
  {
    if ((paramView.getTag() instanceof DPObject))
    {
      try
      {
        paramView = (DPObject)paramView.getTag();
        Object localObject = this.couponDeals.getString("Link");
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
          ((Intent)localObject).putExtra("buyLink", this.couponDeals.getString("BuyLink"));
          ((Intent)localObject).putExtra("selectLink", this.couponDeals.getString("SelectLink"));
          ((Intent)localObject).putExtra("detailLink", this.couponDeals.getString("DetailLink"));
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
          break label634;
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
        label634: statisticsEvent("shopinfo5", "shopinfo5_tuan_more", "收起", 0);
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
        this.couponDeals = ((DPObject)paramBundle.getParcelable("Deals"));
        this.dealDiscountList = ((DPObject)paramBundle.getParcelable("DealDiscountList"));
      }
      return;
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
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
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mPromoTagRequest)
    {
      this.mPromoTagRequest = null;
      this.dealDiscountList = ((DPObject)paramMApiResponse.result());
      updateTuanDealTags();
    }
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putBoolean("isExpand", this.isExpand);
    localBundle.putParcelable("Deals", this.couponDeals);
    localBundle.putParcelable("DealDiscountList", this.dealDiscountList);
    return localBundle;
  }

  public void setBackground(View paramView, int paramInt)
  {
    int i = paramView.getPaddingBottom();
    int j = paramView.getPaddingTop();
    int k = paramView.getPaddingRight();
    int m = paramView.getPaddingLeft();
    paramView.setBackgroundResource(paramInt);
    paramView.setPadding(m, j, k, i);
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
  }

  protected void setupView()
  {
    if (this.couponDeals == null);
    do
    {
      DPObject[] arrayOfDPObject;
      do
      {
        return;
        arrayOfDPObject = this.couponDeals.getArray("CouponDeals");
      }
      while ((arrayOfDPObject == null) || (arrayOfDPObject.length == 0));
      this.linearLayout = new LinearLayout(getContext());
      this.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
      this.linearLayout.setOrientation(1);
      this.linearLayout.addView(createDefaultDealCell(arrayOfDPObject[0], 0));
      if (arrayOfDPObject.length == 2)
      {
        this.linearLayout.addView(line());
        this.linearLayout.addView(createDefaultDealCell(arrayOfDPObject[1], 1));
      }
      if (arrayOfDPObject.length > 2)
      {
        this.linearLayout.addView(line());
        this.linearLayout.addView(createDefaultDealCell(arrayOfDPObject[1], 1));
        this.expandLayout = new LinearLayout(getContext());
        this.expandLayout.setOrientation(1);
        if (!this.isExpand)
          this.expandLayout.setVisibility(8);
        int i = 2;
        int j = arrayOfDPObject.length;
        while (i < j)
        {
          this.expandLayout.addView(line());
          this.expandLayout.addView(createDefaultDealCell(arrayOfDPObject[i], i));
          i += 1;
        }
        this.linearLayout.addView(this.expandLayout);
        this.expandView = ((NovaRelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.expand, getParentView(), false));
        this.expandView.setGAString("quan_ai_more");
        this.expandView.setTag("EXPAND");
        this.moreText = ("更多" + (arrayOfDPObject.length - 2) + "个代金券");
        ((TextView)this.expandView.findViewById(16908308)).setText(this.moreText);
        this.expandView.setClickable(true);
        this.expandView.setOnClickListener(this);
        this.linearLayout.addView(this.expandView);
        setExpandState();
      }
      updateDealSaleCount();
      addCell("0475HuiPay.20Hui", this.linearLayout, 64);
      if (this.hasPromoRequested)
        continue;
      requestPromoTag();
      this.hasPromoRequested = true;
    }
    while (this.dealDiscountList == null);
    updateTuanDealTags();
  }

  protected void updateDealSaleCount()
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
 * Qualified Name:     com.dianping.shopinfo.common.CouponAgent
 * JD-Core Version:    0.6.0
 */