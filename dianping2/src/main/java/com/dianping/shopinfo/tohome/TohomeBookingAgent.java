package com.dianping.shopinfo.tohome;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.AutoHideTextView;
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
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public class TohomeBookingAgent extends ShopCellAgent
{
  private static final String API_URL = "http://mapi.dianping.com/tohome/shop/status/load.tohome?";
  private static final String CELL_VERTICAL_CHANNEL_BOOKING = "0200Basic.45ToHomeBook";
  private static final String TAG = TohomeBookingAgent.class.getSimpleName();
  protected int categoryId;
  private View.OnClickListener clickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = paramView.getTag();
      if (paramView == null);
      do
      {
        do
          return;
        while (!(paramView instanceof TohomeBookingAgent.BookingHolder));
        paramView = (TohomeBookingAgent.BookingHolder)paramView;
      }
      while (TextUtils.isEmpty(paramView.linkUrl));
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView.linkUrl));
      TohomeBookingAgent.this.startActivity(paramView);
    }
  };
  protected DPObject mBookingInfo;
  private View mBookingView;
  protected MApiRequest mRequest;
  private RequestHandler<MApiRequest, MApiResponse> mRequestHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      TohomeBookingAgent.this.mRequest = null;
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (TohomeBookingAgent.this.mRequest != paramMApiRequest);
      do
      {
        return;
        TohomeBookingAgent.this.mBookingInfo = ((DPObject)paramMApiResponse.result());
      }
      while (TohomeBookingAgent.this.mBookingInfo == null);
      TohomeBookingAgent.this.dispatchAgentChanged(false);
    }
  };
  protected int shopId;

  public TohomeBookingAgent(Object paramObject)
  {
    super(paramObject);
  }

  private AutoHideTextView buildPromoView(String paramString)
  {
    AutoHideTextView localAutoHideTextView = new AutoHideTextView(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
    localLayoutParams.setMargins(0, 0, 5, 0);
    localAutoHideTextView.setLayoutParams(localLayoutParams);
    localAutoHideTextView.setBackgroundResource(R.drawable.background_round_textview_lightred);
    localAutoHideTextView.setEllipsize(TextUtils.TruncateAt.END);
    localAutoHideTextView.setGravity(16);
    localAutoHideTextView.setSingleLine(true);
    localAutoHideTextView.setTextColor(getContext().getResources().getColor(R.color.light_red));
    localAutoHideTextView.setTextSize(2, 11.0F);
    localAutoHideTextView.setText(paramString);
    return localAutoHideTextView;
  }

  private BookingHolder createBookingHolder()
  {
    if (this.mBookingInfo == null);
    do
      return null;
    while (TextUtils.isEmpty(this.mBookingInfo.getString("ToHomeName")));
    BookingHolder localBookingHolder = new BookingHolder();
    localBookingHolder.title = this.mBookingInfo.getString("ToHomeName");
    localBookingHolder.promos = this.mBookingInfo.getStringArray("ShopPromos");
    localBookingHolder.tips = this.mBookingInfo.getString("OrderNo");
    localBookingHolder.linkUrl = this.mBookingInfo.getString("H5Url");
    return localBookingHolder;
  }

  private View createBookingView(BookingHolder paramBookingHolder)
  {
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)this.res.inflate(getContext(), R.layout.shop_tohome_booking_cell, getParentView(), false);
    localNovaLinearLayout.gaUserInfo.shop_id = Integer.valueOf(this.shopId);
    localNovaLinearLayout.gaUserInfo.category_id = Integer.valueOf(this.categoryId);
    localNovaLinearLayout.setGAString("shopinfo5_tohome");
    if (!TextUtils.isEmpty(paramBookingHolder.title))
      ((TextView)localNovaLinearLayout.findViewById(R.id.text_tohome_tips)).setText(paramBookingHolder.title);
    if (paramBookingHolder.promos != null)
    {
      LinearLayout localLinearLayout = (LinearLayout)localNovaLinearLayout.findViewById(R.id.container_tohome_promos);
      String[] arrayOfString = paramBookingHolder.promos;
      int j = arrayOfString.length;
      int i = 0;
      if (i < j)
      {
        String str = arrayOfString[i];
        if (TextUtils.isEmpty(str));
        while (true)
        {
          i += 1;
          break;
          localLinearLayout.addView(buildPromoView(str));
        }
      }
    }
    if (!TextUtils.isEmpty(paramBookingHolder.tips))
      ((TextView)localNovaLinearLayout.findViewById(R.id.text_tohome_servicedcount)).setText(paramBookingHolder.tips);
    ((DPActivity)getFragment().getActivity()).addGAView(localNovaLinearLayout, -1);
    localNovaLinearLayout.setTag(paramBookingHolder);
    localNovaLinearLayout.setOnClickListener(this.clickListener);
    return localNovaLinearLayout;
  }

  private void extractShopInfo()
  {
    DPObject localDPObject = getShop();
    if (localDPObject == null)
    {
      Log.e(TAG, "Null shop data. Can not update shop info.");
      return;
    }
    this.shopId = localDPObject.getInt("ID");
    this.categoryId = localDPObject.getInt("CategoryID");
  }

  private String extractToken()
  {
    String str2 = accountService().token();
    String str1 = str2;
    if (TextUtils.isEmpty(str2))
      str1 = "";
    return str1;
  }

  private boolean paramIsValid()
  {
    return this.shopId > 0;
  }

  private void sendRequest(String paramString)
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/tohome/shop/status/load.tohome?").buildUpon();
    localBuilder.appendQueryParameter("shopid", this.shopId + "");
    localBuilder.appendQueryParameter("token", paramString);
    this.mRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mRequest, this.mRequestHandler);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (this.mBookingView != null);
    do
    {
      return;
      super.onAgentChanged(paramBundle);
      paramBundle = createBookingHolder();
    }
    while (paramBundle == null);
    this.mBookingView = createBookingView(paramBundle);
    addCell("0200Basic.45ToHomeBook", this.mBookingView);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    extractShopInfo();
    if (!paramIsValid())
      return;
    sendRequest(extractToken());
  }

  class BookingHolder
  {
    protected String linkUrl;
    protected String[] promos;
    protected String tips;
    protected String title;

    BookingHolder()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.tohome.TohomeBookingAgent
 * JD-Core Version:    0.6.0
 */