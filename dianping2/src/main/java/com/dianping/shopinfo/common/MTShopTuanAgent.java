package com.dianping.shopinfo.common;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.archive.DPObject;
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
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaRelativeLayout;
import java.text.DecimalFormat;
import java.util.HashMap;

public class MTShopTuanAgent extends ShopInfoTuanAgent
{
  private DPObject MTDealObject;
  private DPObject[] MTDeals;
  private HashMap<String, String> dealMap = new HashMap();
  private MApiRequest mtGrouponUrlRequest;

  public MTShopTuanAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendJumpUrlRequest(String paramString)
  {
    showProgressDialog("正在跳转团购详情，请稍等");
    this.mtGrouponUrlRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/mshop/mtgrouponurl.bin").buildUpon().appendQueryParameter("shopid", Integer.toString(shopId())).appendQueryParameter("token", accountService().token()).appendQueryParameter("grouponid", paramString).toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mtGrouponUrlRequest, this);
  }

  public CommonCell createDefaultDealCell(DPObject paramDPObject, boolean paramBoolean)
  {
    TuanTicketCell localTuanTicketCell = (TuanTicketCell)MyResources.getResource(ShopCellAgent.class).inflate(getContext(), R.layout.tuan_cell_shopinfo_icon, getParentView(), false);
    if (paramDPObject.getString("Pic") != null)
      ((NetworkImageView)localTuanTicketCell.findViewById(R.id.icon)).setImage(paramDPObject.getString("Pic"));
    localTuanTicketCell.setGAString("meituan", getGAExtra());
    localTuanTicketCell.findViewById(R.id.arrow).setVisibility(4);
    localTuanTicketCell.setSubTitle(paramDPObject.getString("Title"));
    localTuanTicketCell.setClickable(true);
    localTuanTicketCell.setOnClickListener(this);
    double d1 = paramDPObject.getDouble("Price");
    double d2 = paramDPObject.getDouble("OriginaPirce");
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
    localTuanTicketCell.setSaleCount(paramDPObject.getString("SalesDesc"));
    localTuanTicketCell.setTitle(localSpannableStringBuilder);
    localTuanTicketCell.setTag(paramDPObject);
    setBackground(localTuanTicketCell.findViewById(R.id.layout), 0);
    this.tuanCells.append(paramDPObject.getInt("ID"), localTuanTicketCell);
    return localTuanTicketCell;
  }

  public void onClick(View paramView)
  {
    if ((paramView.getTag() instanceof DPObject))
    {
      String str1;
      String str2;
      try
      {
        paramView = (DPObject)paramView.getTag();
        if (paramView == null)
          return;
        str1 = (String)this.dealMap.get(paramView.getString("DealID"));
        if (TextUtils.isEmpty(str1))
        {
          str1 = paramView.getString("Url");
          str2 = paramView.getString("DealID");
          if (!paramView.getBoolean("IsNeedLogin"))
            break label126;
          if (!isLogined())
          {
            accountService().login(new LoginResultListener(str2)
            {
              public void onLoginCancel(AccountService paramAccountService)
              {
              }

              public void onLoginSuccess(AccountService paramAccountService)
              {
                MTShopTuanAgent.this.sendJumpUrlRequest(this.val$grouponId);
              }
            });
            return;
          }
        }
        else
        {
          getFragment().startActivity(str1);
          return;
        }
      }
      catch (java.lang.Exception paramView)
      {
        Log.e("shop", "fail to launch deal", paramView);
        return;
      }
      sendJumpUrlRequest(str2);
      return;
      label126: getFragment().startActivity(str1);
      return;
    }
    if (paramView.getTag() == "EXPAND")
    {
      if (!this.isExpand);
      for (boolean bool = true; ; bool = false)
      {
        this.isExpand = bool;
        setExpandAction();
        scrollToCenter();
        return;
      }
    }
  }

  public void onDestroy()
  {
    if (this.mGouponRequest != null)
    {
      mapiService().abort(this.mGouponRequest, this, true);
      this.mGouponRequest = null;
    }
    if (this.mtGrouponUrlRequest != null)
    {
      dismissDialog();
      mapiService().abort(this.mtGrouponUrlRequest, this, true);
      this.mtGrouponUrlRequest = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mGouponRequest)
      this.mGouponRequest = null;
    do
      return;
    while (paramMApiRequest != this.mtGrouponUrlRequest);
    dismissDialog();
    paramMApiRequest = Toast.makeText(getContext(), "获取团购详情失败，请点击重试", 0);
    paramMApiRequest.setGravity(17, 0, 0);
    paramMApiRequest.show();
    this.mtGrouponUrlRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mGouponRequest)
    {
      this.mGouponRequest = null;
      this.MTDealObject = ((DPObject)paramMApiResponse.result());
      if (this.MTDealObject != null)
      {
        this.MTDeals = this.MTDealObject.getArray("Deals");
        ShopInfoActivity.speedTest(getContext(), 1012);
        dispatchAgentChanged(false);
      }
    }
    do
    {
      do
        return;
      while (paramMApiRequest != this.mtGrouponUrlRequest);
      dismissDialog();
      paramMApiResponse = (DPObject)paramMApiResponse.result();
    }
    while (paramMApiResponse == null);
    paramMApiRequest = paramMApiResponse.getString("Url");
    paramMApiResponse = paramMApiResponse.getString("DealID");
    this.dealMap.put(paramMApiResponse, paramMApiRequest);
    getFragment().startActivity(paramMApiRequest);
  }

  protected void sendGouponRequest()
  {
    this.mGouponRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/mshop/mtgroupon.bin").buildUpon().appendQueryParameter("shopid", Integer.toString(shopId())).toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mGouponRequest, this);
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
      while (this.MTDeals == null);
      setShopDeals();
      arrayOfDPObject = this.MTDeals;
    }
    while ((arrayOfDPObject == null) || (arrayOfDPObject.length == 0));
    this.linearLayout = new LinearLayout(getContext());
    this.linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    this.linearLayout.setOrientation(1);
    View localView = MyResources.getResource(ShopCellAgent.class).inflate(getContext(), R.layout.shopinfo_tuan_header_view, getParentView(), false);
    TextView localTextView = (TextView)localView.findViewById(R.id.title);
    String str = this.MTDealObject.getString("Title");
    Object localObject = new int[4];
    localObject[0] = R.id.icon_refund;
    localObject[1] = R.id.refund_support_day;
    localObject[2] = R.id.icon_expire;
    localObject[3] = R.id.refund_support_expired;
    int i = 0;
    int j = localObject.length;
    while (i < j)
    {
      localView.findViewById(localObject[i]).setVisibility(8);
      i += 1;
    }
    localObject = str;
    if (TextUtils.isEmpty(str))
      localObject = "以下团购由美团网提供";
    localTextView.setText((CharSequence)localObject);
    this.linearLayout.addView(localView);
    this.isOverdueAutoRefund = true;
    this.isAutoRefund = true;
    this.linearLayout.addView(line());
    this.linearLayout.addView(createDealCell(arrayOfDPObject[0], true));
    j = getDisplayCount();
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
    addCell("0480Tuan.50Tuan", this.linearLayout, 0);
    ShopInfoActivity.speedTest(getContext(), 1013);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.MTShopTuanAgent
 * JD-Core Version:    0.6.0
 */