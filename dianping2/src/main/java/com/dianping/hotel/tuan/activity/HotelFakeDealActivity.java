package com.dianping.hotel.tuan.activity;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.Location;
import com.dianping.model.UserProfile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HotelFakeDealActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  protected MApiRequest dealDetailRequest;
  String dealIdStr;
  protected MApiRequest dealRequest;
  protected DPObject dpDeal;
  DPObject dpDealDetailH5;
  protected DPObject dpDetailDeal;
  boolean hasRoom = false;
  int isonlinebooking = 0;
  String selecteddate;

  private boolean checkIsLocked()
  {
    if (accountService().token() == null);
    do
      return false;
    while (!getAccount().grouponIsLocked());
    new AlertDialog.Builder(this).setTitle("提示").setMessage("您的账户存在异常已被锁定，请联系客服为您解除锁定。").setPositiveButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        HotelFakeDealActivity.this.finish();
      }
    }).setCancelable(false).show();
    return true;
  }

  private void goToOrder(int paramInt)
  {
    if (paramInt == 4)
      switch (this.dpDeal.getInt("JumpFlag"))
      {
      case 3:
      default:
        if ((!ConfigHelper.dynamicLogin) || (this.dpDeal.getInt("DealType") == 2))
          break;
        paramInt = 1;
        if ((!isLogined()) && (paramInt == 0))
          accountService().login(this);
      case 0:
      case 1:
      case 2:
      case 4:
      }
    label279: label435: 
    do
    {
      do
      {
        do
        {
          do
          {
            return;
            if ((ConfigHelper.dynamicLogin) && (this.dpDeal.getInt("DealType") != 2));
            for (paramInt = 1; (!isLogined()) && (paramInt == 0); paramInt = 0)
            {
              accountService().login(this);
              return;
            }
          }
          while (checkIsLocked());
          Object localObject1;
          if ((this.dpDeal.getArray("DealSelectList") != null) && (this.dpDeal.getArray("DealSelectList").length > 1))
          {
            localObject1 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://hoteldealselectlist"));
            ((Intent)localObject1).putExtra("dpDeal", this.dpDeal);
          }
          Intent localIntent;
          while (true)
          {
            startActivity((Intent)localObject1);
            return;
            localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://createorder"));
            localIntent.putExtra("deal", this.dpDeal);
            localObject1 = localIntent;
            if (this.selecteddate == null)
              continue;
            localObject1 = localIntent;
            if (this.dpDetailDeal == null)
              continue;
            if (this.dpDetailDeal.getObject("HotelDealGroupDetailInfo") == null)
              break;
            localObject3 = this.dpDetailDeal.getObject("HotelDealGroupDetailInfo").getArray("BookingInfoList");
            localObject1 = localIntent;
            if (localObject3 == null)
              continue;
            localObject1 = localIntent;
            if (localObject3.length <= 0)
              continue;
            localObject3 = localObject3[0].getArray("StatusList");
            localObject1 = localIntent;
            if (localObject3 == null)
              continue;
            localObject1 = localIntent;
            if (localObject3.length <= 0)
              continue;
            if (localObject3[0] == null)
              break label435;
          }
          for (Object localObject3 = localObject3[0].getObject("BookingDefaultInfo"); ; localObject3 = null)
          {
            while (true)
            {
              localObject1 = localIntent;
              if (localObject3 == null)
                break;
              try
              {
                long l = new SimpleDateFormat("yyyy-MM-dd").parse(this.selecteddate).getTime();
                localObject1 = ((DPObject)localObject3).edit().putTime("DefaultCheckin", l).putTime("DefaultCheckout", 86400000L + l).generate();
                localObject3 = new Bundle();
                ((Bundle)localObject3).putParcelable("bookingDefaultInfo", (Parcelable)localObject1);
                localIntent.putExtra("extradata", (Bundle)localObject3);
                localObject1 = localIntent;
              }
              catch (Exception localObject2)
              {
                localObject2 = localIntent;
              }
            }
            break;
            localObject3 = null;
            break label279;
          }
          if ((ConfigHelper.dynamicLogin) && (this.dpDeal.getInt("DealType") != 2));
          for (paramInt = 1; (!isLogined()) && (paramInt == 0); paramInt = 0)
          {
            accountService().login(this);
            return;
          }
        }
        while ((checkIsLocked()) || (this.dpDeal.getArray("DealSelectList") == null) || (this.dpDeal.getArray("DealSelectList").length <= 1));
        localObject2 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://hoteldealselectlist"));
        ((Intent)localObject2).putExtra("dpDeal", this.dpDeal);
        startActivity((Intent)localObject2);
        return;
        localObject2 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://hotelbookingpicktime?channel=4"));
        ((Intent)localObject2).putExtra("dpDeal", this.dpDeal);
        startActivity((Intent)localObject2);
        return;
        if (TextUtils.isEmpty(this.dpDeal.getString("JumpUrl")));
        for (localObject2 = ""; ; localObject2 = this.dpDeal.getString("JumpUrl"))
        {
          localObject2 = (String)localObject2 + "?dealgroupid=" + this.dpDeal.getInt("ID");
          startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://hotelbookingweb").buildUpon().appendQueryParameter("url", Uri.parse((String)localObject2).toString()).build()));
          return;
        }
        paramInt = 0;
        break;
      }
      while (checkIsLocked());
      localObject2 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://createorder"));
      ((Intent)localObject2).putExtra("deal", this.dpDeal);
      startActivity((Intent)localObject2);
      return;
      if ((isLogined()) || (this.dpDeal.getInt("DealType") != 2))
        continue;
      accountService().login(this);
      return;
    }
    while (checkIsLocked());
    if ((this.dpDeal.getArray("DealSelectList") != null) && (this.dpDeal.getArray("DealSelectList").length > 1))
    {
      if ((this.dpDealDetailH5 != null) && (this.hasRoom))
      {
        localObject2 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://hoteldealselectlist"));
        ((Intent)localObject2).putExtra("extradata", this.dpDealDetailH5);
      }
      while (true)
      {
        ((Intent)localObject2).putExtra("dpDeal", this.dpDeal);
        startActivity((Intent)localObject2);
        return;
        localObject2 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://dealselector"));
      }
    }
    Object localObject2 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://createorder"));
    ((Intent)localObject2).putExtra("deal", this.dpDeal);
    if (this.hasRoom)
      ((Intent)localObject2).putExtra("extrainfo", this.dpDealDetailH5.getArray("StatusList")[0]);
    startActivity((Intent)localObject2);
  }

  protected void anaysisDealDetail()
  {
    DPObject[] arrayOfDPObject = this.dpDetailDeal.getObject("HotelDealGroupDetailInfo").getArray("BookingInfoList");
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    int i;
    if ((arrayOfDPObject != null) && (arrayOfDPObject.length > 0) && (!TextUtils.isEmpty(this.selecteddate)))
      i = 0;
    while (true)
    {
      if (i < arrayOfDPObject.length)
      {
        String str = localSimpleDateFormat.format(new Date(arrayOfDPObject[i].getTime("Date")));
        if (this.selecteddate.equals(str))
          this.dpDealDetailH5 = arrayOfDPObject[i];
      }
      else
      {
        isFullRoomDeal();
        return;
      }
      i += 1;
    }
  }

  public void createDealDetailRequest()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("http://m.api.dianping.com/");
    localStringBuilder.append("hoteltg/dealdetailinfogn.hoteltg");
    localStringBuilder.append("?id=").append(this.dealIdStr);
    localStringBuilder.append("&cityid=").append(cityId());
    Object localObject = accountService().token();
    if (!TextUtils.isEmpty((CharSequence)localObject))
      localStringBuilder.append("&token=").append((String)localObject);
    localObject = location();
    if (localObject != null)
    {
      localStringBuilder.append("&lat=").append(((Location)localObject).latitude());
      localStringBuilder.append("&lng=").append(((Location)localObject).longitude());
    }
    this.dealDetailRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.dealDetailRequest, this);
  }

  protected boolean isFullRoomDeal()
  {
    DPObject[] arrayOfDPObject;
    int i;
    if (this.dpDealDetailH5 != null)
    {
      arrayOfDPObject = this.dpDealDetailH5.getArray("StatusList");
      if ((arrayOfDPObject != null) && (arrayOfDPObject.length > 0))
        i = 0;
    }
    while (true)
    {
      if (i < arrayOfDPObject.length)
      {
        if (arrayOfDPObject[i].getInt("Status") == 1)
          this.hasRoom = true;
      }
      else
        return false;
      i += 1;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.dealIdStr = getIntent().getData().getQueryParameter("dealid");
    try
    {
      this.isonlinebooking = Integer.valueOf(getIntent().getData().getQueryParameter("isonlinebooking")).intValue();
      this.selecteddate = getIntent().getData().getQueryParameter("selecteddate");
      if ((this.isonlinebooking == 1) && (!TextUtils.isEmpty(this.selecteddate)))
        createDealDetailRequest();
      return;
    }
    catch (Exception paramBundle)
    {
      while (true)
      {
        paramBundle.printStackTrace();
        if ((TextUtils.isEmpty(this.dealIdStr)) || (!TextUtils.isDigitsOnly(this.dealIdStr)))
          continue;
        sendDealRequest(Integer.parseInt(this.dealIdStr));
      }
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.dealRequest)
      this.dealRequest = null;
    do
      return;
    while (paramMApiRequest != this.dealDetailRequest);
    this.dealDetailRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.dealRequest)
    {
      this.dpDeal = ((DPObject)paramMApiResponse.result());
      this.dealRequest = null;
      goToOrder(this.dpDeal.getInt("DealChannel"));
      finish();
    }
    do
    {
      do
        return;
      while (paramMApiRequest != this.dealDetailRequest);
      this.dpDetailDeal = ((DPObject)paramMApiResponse.result());
      this.dealDetailRequest = null;
      anaysisDealDetail();
    }
    while ((TextUtils.isEmpty(this.dealIdStr)) || (!TextUtils.isDigitsOnly(this.dealIdStr)));
    sendDealRequest(Integer.parseInt(this.dealIdStr));
  }

  public void sendDealRequest(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("http://app.t.dianping.com/dealbaseinfogn.bin");
    localStringBuilder.append("?id=").append(paramInt);
    localStringBuilder.append("&cityid=").append(cityId());
    Object localObject = accountService().token();
    if (!TextUtils.isEmpty((CharSequence)localObject))
      localStringBuilder.append("&token=").append((String)localObject);
    localObject = location();
    if (localObject != null)
    {
      localStringBuilder.append("&lat=").append(((Location)localObject).latitude());
      localStringBuilder.append("&lng=").append(((Location)localObject).longitude());
    }
    this.dealRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.dealRequest, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.tuan.activity.HotelFakeDealActivity
 * JD-Core Version:    0.6.0
 */