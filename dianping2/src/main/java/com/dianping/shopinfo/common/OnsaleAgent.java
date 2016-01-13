package com.dianping.shopinfo.common;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.accountservice.AccountService;
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
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class OnsaleAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private static final String CELL_ONSALE = "0500Cash.60Onsale";
  private static final String CELL_ONSALE_WEDDING = "0500Cash.40Onsale";
  private static final String IS_REQUEST_FINISHED = "isRequestFinished";
  private static final String ON_SALE_OBJECTS = "onsaleObjs";
  private static final String URL = "http://m.api.dianping.com/midas_promo/shortpromotion.bin";
  private MApiRequest mOnsaleReq;
  private ArrayList<DPObject> mSaleList = new ArrayList();
  private boolean mbReqFinished;

  public OnsaleAgent(Object paramObject)
  {
    super(paramObject);
  }

  private CommonCell createOnsaleCell()
  {
    CommonCell localCommonCell = (CommonCell)this.res.inflate(getContext(), R.layout.onsale_cell, getParentView(), false);
    localCommonCell.setLeftIcon(R.drawable.detail_couponicon);
    return localCommonCell;
  }

  private void reqOnsale()
  {
    if (getFragment() == null)
      return;
    if (this.mOnsaleReq != null)
      getFragment().mapiService().abort(this.mOnsaleReq, this, true);
    int i = shopId();
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/midas_promo/shortpromotion.bin").buildUpon().appendQueryParameter("shopid", String.valueOf(i));
    AccountService localAccountService = getFragment().accountService();
    String str3 = localAccountService.token();
    if (str3 != null)
    {
      String str2 = accountService().profile().getString("GrouponPhone");
      String str1 = str2;
      if (TextUtils.isEmpty(str2))
        str1 = localAccountService.profile().getString("PhoneNo");
      localBuilder.appendQueryParameter("token", str3);
      localBuilder.appendQueryParameter("phoneno", str1);
    }
    this.mOnsaleReq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mOnsaleReq, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if ((getShop() != null) && (getShopStatus() == 0) && (getShop().getBoolean("HasPromo")) && (!this.mbReqFinished))
      reqOnsale();
    do
      return;
    while (this.mSaleList.size() <= 0);
    paramBundle = new LinearLayout(getContext());
    Object localObject = new LinearLayout.LayoutParams(-1, -2);
    paramBundle.setOrientation(1);
    paramBundle.setLayoutParams((ViewGroup.LayoutParams)localObject);
    int i = 0;
    while (i < this.mSaleList.size())
    {
      localObject = (DPObject)this.mSaleList.get(i);
      CommonCell localCommonCell = createOnsaleCell();
      localCommonCell.setGAString("promotion", "", i);
      localCommonCell.setTitle(((DPObject)localObject).getString("Title"));
      localCommonCell.setSubTitle(((DPObject)localObject).getString("SubTitle"));
      if (i > 0)
      {
        localCommonCell.findViewById(16908295).setVisibility(4);
        setBackground(localCommonCell.findViewById(R.id.content), R.drawable.cell_item_white);
      }
      localCommonCell.setTag(Integer.valueOf(i));
      localCommonCell.setOnClickListener(this);
      paramBundle.addView(localCommonCell);
      i += 1;
    }
    if ((isWeddingShopType()) || (isWeddingType()) || (isHomeDesignShopType()) || (isHomeMarketShopType()))
    {
      addCell("0500Cash.40Onsale", paramBundle, "promotion", 0);
      return;
    }
    addCell("0500Cash.60Onsale", paramBundle, "promotion", 0);
  }

  public void onClick(View paramView)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BasicNameValuePair("shopid", shopId() + ""));
    if (isWeddingType())
      statisticsEvent("shopinfow", "shopinfow_coupon", "", 0, localArrayList);
    while (true)
    {
      if (isWeddingShopType())
      {
        localArrayList = new ArrayList();
        localArrayList.add(new BasicNameValuePair("shopid", shopId() + ""));
        statisticsEvent("shopinfoq", "shopinfoq_coupon", "", 0, localArrayList);
      }
      paramView = ((DPObject)this.mSaleList.get(((Integer)(Integer)paramView.getTag()).intValue())).getString("Url");
      try
      {
        paramView = URLEncoder.encode(paramView, "utf-8");
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramView)));
        return;
        statisticsEvent("shopinfo5", "shopinfo5_promo", "", 0, localArrayList);
      }
      catch (UnsupportedEncodingException paramView)
      {
        paramView.printStackTrace();
      }
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.mSaleList = paramBundle.getParcelableArrayList("onsaleObjs");
      this.mbReqFinished = paramBundle.getBoolean("isRequestFinished");
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.mOnsaleReq = null;
    this.mbReqFinished = true;
    dispatchAgentChanged(false);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.mOnsaleReq = null;
    if ((paramMApiResponse.result() instanceof DPObject[]))
    {
      paramMApiRequest = (DPObject[])(DPObject[])paramMApiResponse.result();
      this.mSaleList.clear();
      this.mSaleList.addAll(Arrays.asList(paramMApiRequest));
    }
    this.mbReqFinished = true;
    dispatchAgentChanged(false);
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelableArrayList("onsaleObjs", this.mSaleList);
    localBundle.putBoolean("isRequestFinished", this.mbReqFinished);
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
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.OnsaleAgent
 * JD-Core Version:    0.6.0
 */