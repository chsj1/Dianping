package com.dianping.membercard.view;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.util.TelephoneUtils;
import com.dianping.base.widget.ShopPower;
import com.dianping.model.GPSCoordinate;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class PrepaidCardNearestShop extends RelativeLayout
  implements View.OnClickListener
{
  private static double DISTANCE_FACTOR = 1.0D;
  private View allShopView;
  private DPObject dpShop;
  private Context mContext;
  private String[] phoneNos;
  private int prepaidCardId;
  private TextView shopAddressTextView;
  private ImageView shopDescTextView;
  private TextView shopDistanceTextView;
  private DPObject shopList;
  private TextView shopNameTextView;
  private TextView shopNumTextView;
  private View shopPhoneView;
  private ShopPower shopPower;

  public PrepaidCardNearestShop(Context paramContext)
  {
    this(paramContext, null);
  }

  public PrepaidCardNearestShop(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
  }

  private String getDistanceText(long paramLong)
  {
    if ((this.dpShop.getDouble("Latitude") == 0.0D) && (this.dpShop.getDouble("Longitude") == 0.0D))
      return "";
    String str;
    if (paramLong <= 100L)
      str = "<100m";
    while (true)
    {
      return str;
      if (paramLong < 1000L)
      {
        str = paramLong + "m";
        continue;
      }
      if (paramLong < 10000L)
      {
        paramLong /= 100L;
        str = paramLong / 10L + "." + paramLong % 10L + "km";
        continue;
      }
      if (paramLong < 100000L)
      {
        str = paramLong / 1000L + "km";
        continue;
      }
      str = ">100km";
    }
  }

  public void onClick(View paramView)
  {
    if (this.dpShop == null);
    do
    {
      while (true)
      {
        return;
        if (paramView.getId() != R.id.shop_phone)
          break;
        DPApplication.instance().statisticsEvent("paidcardinfo5", "paidcardinfo5_shop_tell", "", 0);
        if ((this.phoneNos == null) || (this.phoneNos.length == 0))
          continue;
        if (this.phoneNos.length == 1)
        {
          TelephoneUtils.dial(this.mContext, this.dpShop, this.phoneNos[0]);
          return;
        }
        new AlertDialog.Builder(this.mContext).setTitle("联系商户").setAdapter(new ArrayAdapter(this.mContext, R.layout.simple_list_item_1, 16908308, this.phoneNos)
        {
          public String getItem(int paramInt)
          {
            return "拨打电话：" + PrepaidCardNearestShop.this.phoneNos[paramInt];
          }
        }
        , new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            TelephoneUtils.dial(PrepaidCardNearestShop.this.mContext, PrepaidCardNearestShop.this.dpShop, PrepaidCardNearestShop.this.phoneNos[paramInt]);
          }
        }).show();
        return;
      }
      if (paramView.getId() != R.id.shop_info_layer)
        continue;
      DPApplication.instance().statisticsEvent("paidcardinfo5", "paidcardinfo5_shop", "", 0);
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + this.dpShop.getInt("ID")));
      this.mContext.startActivity(paramView);
      return;
    }
    while (paramView.getId() != R.id.all_shop);
    DPApplication.instance().statisticsEvent("paidcardinfo5", "paidcardinfo5_shop_more", "", 0);
    paramView = new Intent("android.intent.action.VIEW");
    paramView.setData(Uri.parse("dianping://prepaidcardaviliableshoplist?prepaidcardid=" + this.prepaidCardId));
    paramView.putExtra("shopList", this.shopList);
    this.mContext.startActivity(paramView);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.shopDescTextView = ((ImageView)findViewById(R.id.shop_desc));
    this.shopNameTextView = ((TextView)findViewById(R.id.shop_name));
    this.shopAddressTextView = ((TextView)findViewById(R.id.shop_address));
    this.shopDistanceTextView = ((TextView)findViewById(R.id.shop_distance));
    this.shopPower = ((ShopPower)findViewById(R.id.shop_power));
    this.shopPhoneView = findViewById(R.id.shop_phone);
    this.shopPhoneView.setOnClickListener(this);
    this.allShopView = findViewById(R.id.all_shop);
    this.allShopView.setOnClickListener(this);
    this.shopNumTextView = ((TextView)findViewById(R.id.shop_num));
    findViewById(R.id.shop_info_layer).setOnClickListener(this);
  }

  public boolean setDealShop(int paramInt, DPObject paramDPObject, double paramDouble1, double paramDouble2)
  {
    if ((paramDPObject == null) || (paramDPObject.getInt("RecordCount") <= 0) || (paramDPObject.getArray("List") == null) || (paramDPObject.getArray("List").length <= 0));
    do
    {
      return false;
      this.dpShop = paramDPObject.getArray("List")[0];
    }
    while (this.dpShop == null);
    setVisibility(0);
    this.phoneNos = this.dpShop.getStringArray("PhoneNos");
    if ((this.phoneNos == null) || (this.phoneNos.length == 0))
      this.shopPhoneView.setVisibility(8);
    this.prepaidCardId = paramInt;
    this.shopList = paramDPObject;
    StringBuilder localStringBuilder;
    if (paramDPObject.getInt("RecordCount") <= 1)
    {
      this.allShopView.setVisibility(8);
      paramDPObject = this.dpShop.getString("BranchName");
      localStringBuilder = new StringBuilder().append(this.dpShop.getString("Name"));
      if ((paramDPObject != null) && (paramDPObject.length() != 0))
        break label353;
    }
    label353: for (paramDPObject = ""; ; paramDPObject = "(" + paramDPObject + ")")
    {
      paramDPObject = paramDPObject;
      if (!TextUtils.isEmpty(paramDPObject))
        this.shopNameTextView.setText(paramDPObject);
      this.shopPower.setPower(this.dpShop.getInt("ShopPower"));
      setDistanceText(paramDouble1, paramDouble2);
      this.shopAddressTextView.setText(this.dpShop.getString("Address"));
      if (!TextUtils.isEmpty(this.dpShop.getString("CrossRoad")))
        this.shopAddressTextView.append("(" + this.dpShop.getString("CrossRoad") + ")");
      return true;
      this.allShopView.setVisibility(0);
      this.shopNumTextView.setText("查看全部" + paramDPObject.getInt("RecordCount") + "家商户");
      break;
    }
  }

  public void setDistanceText(double paramDouble1, double paramDouble2)
  {
    Object localObject2 = null;
    Object localObject1;
    if (DISTANCE_FACTOR <= 0.0D)
      localObject1 = localObject2;
    while (TextUtils.isEmpty((CharSequence)localObject1))
    {
      this.shopDistanceTextView.setVisibility(4);
      this.shopDescTextView.setVisibility(8);
      return;
      localObject1 = localObject2;
      if (paramDouble1 == 0.0D)
        continue;
      localObject1 = localObject2;
      if (paramDouble2 == 0.0D)
        continue;
      if (this.dpShop.getDouble("Latitude") == 0.0D)
      {
        localObject1 = localObject2;
        if (this.dpShop.getDouble("Longitude") == 0.0D)
          continue;
      }
      paramDouble1 = new GPSCoordinate(paramDouble1, paramDouble2).distanceTo(new GPSCoordinate(this.dpShop.getDouble("Latitude"), this.dpShop.getDouble("Longitude"))) * DISTANCE_FACTOR;
      localObject1 = localObject2;
      if (Double.isNaN(paramDouble1))
        continue;
      localObject1 = localObject2;
      if (paramDouble1 <= 0.0D)
        continue;
      localObject1 = getDistanceText((int)Math.round(paramDouble1 / 10.0D) * 10);
    }
    this.shopDistanceTextView.setVisibility(0);
    this.shopDistanceTextView.setText((CharSequence)localObject1);
    this.shopDescTextView.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.PrepaidCardNearestShop
 * JD-Core Version:    0.6.0
 */