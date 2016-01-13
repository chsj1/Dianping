package com.dianping.base.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class ShopNearRecommendItem extends LinearLayout
  implements View.OnClickListener
{
  View book;
  View checkin;
  DPObject dpObjShop;
  List<NameValuePair> extras = new ArrayList();
  String label = "";
  View memberCard;
  TextView price;
  View promo;
  View sceneryOrder;
  int shopID = 0;
  ShopPower starIcon;
  View takeaway;
  TextView title;
  View tuan;

  public ShopNearRecommendItem(Context paramContext)
  {
    super(paramContext);
  }

  public ShopNearRecommendItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean hasMemberCard()
  {
    return ((this.dpObjShop.getInt("ShopMemberCardID") > 0) || ((this.dpObjShop.getArray("StoreCardGroupList") != null) && (this.dpObjShop.getArray("StoreCardGroupList").length > 0))) && (ConfigHelper.enableCard);
  }

  protected boolean hasSceneryOrder()
  {
    return (this.dpObjShop.getBoolean("TicketBookable")) && (!ConfigHelper.disableSceneryOrder);
  }

  protected boolean hasTakeaway()
  {
    return (this.dpObjShop.getBoolean("HasTakeaway")) && (ConfigHelper.enableTakeaway);
  }

  public void onClick(View paramView)
  {
    ((DPActivity)getContext()).statisticsEvent("shopinfo5", "shopinfo5_aroundrecommend", this.label, 0, this.extras);
    paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?shopid=" + this.shopID));
    paramView.putExtra("shop", this.dpObjShop);
    getContext().startActivity(paramView);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.title = ((TextView)findViewById(R.id.shop_name));
    this.starIcon = ((ShopPower)findViewById(R.id.star));
    this.price = ((TextView)findViewById(R.id.price));
    this.tuan = findViewById(R.id.ic_tuan);
    this.promo = findViewById(R.id.ic_promo);
    this.checkin = findViewById(R.id.ic_checkin);
    this.book = findViewById(R.id.ic_book);
    this.memberCard = findViewById(R.id.ic_membercard);
    this.takeaway = findViewById(R.id.ic_takeaway);
    this.sceneryOrder = findViewById(R.id.ic_sceneryorder);
  }

  public void setGroupon(DPObject paramDPObject, String paramString)
  {
    this.dpObjShop = paramDPObject;
    if (!TextUtils.isEmpty(this.dpObjShop.getString("BranchName")))
    {
      this.title.setText(this.dpObjShop.getString("Name") + "(" + this.dpObjShop.getString("BranchName") + ")");
      this.shopID = this.dpObjShop.getInt("ID");
      this.extras.add(new BasicNameValuePair("shop_id", String.valueOf(paramString)));
      paramString = paramDPObject.getObject("Deals");
      if ((paramString != null) && (paramString.getArray("List").length != 0))
        this.label = "团购";
      paramString = this.tuan;
      if (paramDPObject.getObject("HotelDealList") == null)
        break label589;
      i = 0;
      label153: paramString.setVisibility(i);
      paramString = this.promo;
      if ((paramDPObject.getArray("Promos") == null) || (paramDPObject.getArray("Promos").length <= 0))
        break label595;
      i = 0;
      label186: paramString.setVisibility(i);
      paramString = this.checkin;
      if (paramDPObject.getObject("Campaign") == null)
        break label601;
      i = 0;
      label208: paramString.setVisibility(i);
      paramString = this.book;
      if ((!paramDPObject.getBoolean(2034)) && (!paramDPObject.getBoolean("MovieBookable")) && (!paramDPObject.getBoolean("HotelBooking")) && ((!paramDPObject.getBoolean("Bookable")) || (!ConfigHelper.enableYY)))
        break label607;
      i = 0;
      label266: paramString.setVisibility(i);
      paramString = this.memberCard;
      if (!hasMemberCard())
        break label613;
      i = 0;
      label285: paramString.setVisibility(i);
      paramString = this.sceneryOrder;
      if (!hasSceneryOrder())
        break label619;
      i = 0;
      label304: paramString.setVisibility(i);
      paramString = this.takeaway;
      if (!hasTakeaway())
        break label625;
    }
    label589: label595: label601: label607: label613: label619: label625: for (int i = 0; ; i = 8)
    {
      paramString.setVisibility(i);
      this.starIcon.setVisibility(0);
      this.starIcon.setPower(paramDPObject.getInt("ShopPower"));
      i = paramDPObject.getInt("Price");
      if (i != 0)
      {
        this.price.setVisibility(0);
        int j = getResources().getDimensionPixelSize(R.dimen.text_large);
        int k = getResources().getDimensionPixelSize(R.dimen.text_medium);
        paramDPObject = "￥" + i + "起";
        paramString = new SpannableString(paramDPObject);
        paramString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), 0, paramDPObject.length() - 1, 18);
        paramString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_gray)), paramDPObject.length() - 1, paramDPObject.length(), 18);
        paramString.setSpan(new AbsoluteSizeSpan(j), 0, paramDPObject.length() - 1, 18);
        paramString.setSpan(new AbsoluteSizeSpan(k), paramDPObject.length() - 1, paramDPObject.length(), 18);
        paramString.setSpan(new StyleSpan(1), 0, paramDPObject.length() - 1, 18);
        this.price.setText(paramString);
      }
      return;
      this.title.setText(this.dpObjShop.getString("Name"));
      break;
      i = 8;
      break label153;
      i = 8;
      break label186;
      i = 8;
      break label208;
      i = 8;
      break label266;
      i = 8;
      break label285;
      i = 8;
      break label304;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ShopNearRecommendItem
 * JD-Core Version:    0.6.0
 */