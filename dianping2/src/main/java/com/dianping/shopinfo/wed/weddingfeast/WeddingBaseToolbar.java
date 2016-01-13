package com.dianping.shopinfo.wed.weddingfeast;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.ugc.review.AddReviewUtil;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.util.TelephoneUtils;
import com.dianping.base.widget.ToolbarButton;
import com.dianping.base.widget.ToolbarImageButton;
import com.dianping.shopinfo.base.ShopInfoToolbarAgent;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;

public class WeddingBaseToolbar extends ShopInfoToolbarAgent
{
  private final View.OnClickListener banquetBookingListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      WeddingBaseToolbar.this.enterBookingPage();
    }
  };
  int cooperateType = 0;
  private final View.OnClickListener phoneListener = new View.OnClickListener()
  {
    String phoneStr;

    public void onClick(View paramView)
    {
      paramView = WeddingBaseToolbar.this.getShop();
      if (paramView == null);
      do
      {
        return;
        String[] arrayOfString = paramView.getStringArray("PhoneNos");
        if ((arrayOfString == null) || (arrayOfString.length <= 0))
          continue;
        this.phoneStr = arrayOfString[0];
      }
      while (TextUtils.isEmpty(this.phoneStr));
      new View.OnClickListener(paramView)
      {
        public void onClick(View paramView)
        {
          TelephoneUtils.dial(WeddingBaseToolbar.this.getContext(), this.val$shop, WeddingBaseToolbar.2.this.phoneStr);
        }
      }
      .onClick(null);
    }
  };
  private final View.OnClickListener reviewListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = WeddingBaseToolbar.this.getShop();
      if (paramView == null)
        return;
      switch (paramView.getInt("Status"))
      {
      case 2:
      case 3:
      default:
        Bundle localBundle = new Bundle();
        localBundle.putParcelable("shop", paramView);
        AddReviewUtil.addReview(WeddingBaseToolbar.this.getContext(), paramView.getInt("ID"), paramView.getString("Name"), localBundle);
        return;
      case 1:
      case 4:
      }
      Toast.makeText(WeddingBaseToolbar.this.getContext(), "暂停收录点评", 0).show();
    }
  };

  public WeddingBaseToolbar(Object paramObject)
  {
    super(paramObject);
  }

  private void BuildBooking()
  {
    ToolbarButton localToolbarButton = (ToolbarButton)LayoutInflater.from(getContext()).inflate(R.layout.wed_toolbar_booking_view, getToolbarView(), false);
    if (isBanquetType())
      localToolbarButton.setTitle("咨询底价");
    while (true)
    {
      localToolbarButton.setOnClickListener(this.banquetBookingListener);
      addToolbarButton(localToolbarButton, "6BookingView");
      return;
      if (!isWeddingType())
        continue;
      localToolbarButton.setTitle("预约看店");
    }
  }

  private void buildBottomToolbar()
  {
    getToolbarView().removeAllViews();
    buildPhone();
    buildReview();
    BuildBooking();
  }

  private void buildPhone()
  {
    ToolbarButton localToolbarButton = (ToolbarButton)LayoutInflater.from(getContext()).inflate(R.layout.wed_toolbar_button, getToolbarView(), false);
    ((ToolbarImageButton)localToolbarButton.findViewById(16908294)).setImageResource(R.drawable.wed_shopinfo_tel);
    localToolbarButton.setOnClickListener(this.phoneListener);
    ((TextView)localToolbarButton.findViewById(16908308)).setText("电话");
    addToolbarButton(localToolbarButton, "4Telephone");
  }

  private void buildReview()
  {
    ToolbarButton localToolbarButton = (ToolbarButton)LayoutInflater.from(getContext()).inflate(R.layout.wed_toolbar_button, getToolbarView(), false);
    ((ToolbarImageButton)localToolbarButton.findViewById(16908294)).setImageResource(R.drawable.detail_footerbar_icon_comment_u);
    ((TextView)localToolbarButton.findViewById(16908308)).setText("点评");
    addToolbarButton(localToolbarButton, "8Review");
    localToolbarButton.setOnClickListener(this.reviewListener);
  }

  private void enterBookingPage()
  {
    Object localObject = null;
    if (isBanquetType())
    {
      localObject = ((DPObject)getSharedObject("weddingShopBasicInfo")).getString("BookingUrl");
      localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + Uri.encode((String)localObject)));
    }
    while (localObject == null)
    {
      return;
      if (!isWeddingType())
        continue;
      localObject = new Intent("android.intent.action.VIEW", Uri.parse(Uri.parse("dianping://weddinghotelbooking").buildUpon().appendQueryParameter("shopid", String.valueOf(getShop().getInt("ID"))).appendQueryParameter("shopname", DPObjectUtils.getShopFullName(getShop())).build().toString()));
    }
    startActivity((Intent)localObject);
  }

  private void initCooperateType()
  {
    DPObject localDPObject = (DPObject)getSharedObject("WeddingHotelExtra");
    if (localDPObject == null)
      return;
    this.cooperateType = localDPObject.getInt("CooperateType");
  }

  private boolean isDataValid()
  {
    if (isWeddingType())
    {
      initCooperateType();
      if (this.cooperateType == 1);
    }
    String str;
    do
    {
      return false;
      if (!isBanquetType())
        break;
      str = "";
      DPObject localDPObject = (DPObject)getSharedObject("weddingShopBasicInfo");
      if (localDPObject == null)
        continue;
      str = localDPObject.getString("BookingUrl");
    }
    while (TextUtils.isEmpty(str));
    return true;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (!isDataValid())
      return;
    buildBottomToolbar();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.weddingfeast.WeddingBaseToolbar
 * JD-Core Version:    0.6.0
 */