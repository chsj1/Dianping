package com.dianping.shopinfo.common;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.archive.DPObject;
import com.dianping.base.util.TelephoneUtils;
import com.dianping.base.widget.BookingCell;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.BookingTicketCell;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class PhoneAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_PHONE = "0200Basic.30Phone";
  private static final String CELL_PHONE_WEDDING = "0250Basic.09Phone";
  private static final int MAX_RETRY_COUNT = 3;
  boolean actionCall;
  protected View cell;
  private final View.OnClickListener clickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (paramView.getId() == 2)
        PhoneAgent.this.callPhone();
    }
  };
  private DPObject mIamShoperObject;
  private MApiRequest mIamShoperRequest;
  private int mRetryCount = 0;

  public PhoneAgent(Object paramObject)
  {
    super(paramObject);
  }

  private BookingCell createBookingCell()
  {
    return (BookingCell)MyResources.getResource(ShopCellAgent.class).inflate(getContext(), R.layout.booking_cell, getParentView(), false);
  }

  private boolean hasPhone()
  {
    return (getShop().getStringArray("PhoneNos") != null) && (getShop().getStringArray("PhoneNos").length > 0);
  }

  private boolean hasWedding()
  {
    return getShop().getBoolean("WeddingBookable");
  }

  private boolean isEducationTypeAndCannotfixAllPhones(String[] paramArrayOfString)
  {
    if (!isEducationType());
    do
      return false;
    while ((paramArrayOfString.length == 1) || (paramArrayOfString.length < 2) || ((paramArrayOfString[0].length() <= 8) && (paramArrayOfString[1].length() <= 8)));
    return true;
  }

  private void sendRequest()
  {
    if (this.mIamShoperRequest != null);
    do
      return;
    while (!ConfigHelper.iamShoperSwitch);
    this.mIamShoperRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/mshop/iamshoper.bin").buildUpon().appendQueryParameter("type", Integer.toString(1)).appendQueryParameter("shopid", Integer.toString(shopId())).toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.mIamShoperRequest, this);
  }

  public void callPhone()
  {
    Object localObject1 = getShop();
    if (localObject1 == null);
    label572: label607: 
    while (true)
    {
      return;
      ArrayList localArrayList1 = new ArrayList();
      ArrayList localArrayList2 = new ArrayList();
      if (this.mIamShoperObject != null)
      {
        localObject2 = this.mIamShoperObject.getString("PhoneTitle");
        localObject3 = this.mIamShoperObject.getString("PhoneUrl");
        if ((!TextUtils.isEmpty((CharSequence)localObject2)) && (!TextUtils.isEmpty((CharSequence)localObject3)))
        {
          localArrayList1.add(0, localObject2);
          localArrayList2.add(0, new View.OnClickListener((String)localObject2, (String)localObject3)
          {
            public void onClick(View paramView)
            {
              GAHelper.instance().contextStatisticsEvent(PhoneAgent.this.getContext(), "tel", this.val$freeContactTitle, 2147483647, "tap");
              PhoneAgent.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.val$freeContactPhone)));
            }
          });
        }
      }
      Object localObject2 = ((DPObject)localObject1).getStringArray("PhoneNos");
      if (localObject2 != null)
      {
        int i = 0;
        while (i < localObject2.length)
        {
          localObject3 = localObject2[i];
          localArrayList1.add(localObject3);
          localArrayList2.add(new View.OnClickListener((DPObject)localObject1, (String)localObject3)
          {
            public void onClick(View paramView)
            {
              TelephoneUtils.dial(PhoneAgent.this.getContext(), this.val$shop, this.val$phone);
              GAHelper.instance().contextStatisticsEvent(PhoneAgent.this.getContext(), "tel", this.val$phone, 2147483647, "tap");
              PhoneAgent.this.statisticsEvent("shopinfo5", "shopinfo5_tel_call", this.val$shop.getInt("ID") + "|" + this.val$shop.getString("Name"), 0);
              if (PhoneAgent.this.isWeddingType())
              {
                paramView = new ArrayList();
                paramView.add(new BasicNameValuePair("shopid", PhoneAgent.this.shopId() + ""));
                PhoneAgent.this.statisticsEvent("shopinfow", "shopinfow_tel", "", 0, paramView);
              }
              if (PhoneAgent.this.isWeddingShopType())
              {
                paramView = new ArrayList();
                paramView.add(new BasicNameValuePair("shopid", PhoneAgent.this.shopId() + ""));
                PhoneAgent.this.statisticsEvent("shopinfoq", "shopinfoq_tel", "", 0, paramView);
              }
              if (PhoneAgent.this.isHomeDesignShopType())
              {
                paramView = new ArrayList();
                paramView.add(new BasicNameValuePair("shopid", PhoneAgent.this.shopId() + ""));
                PhoneAgent.this.statisticsEvent("shopinfoh", "shopinfoh_tel_call", "1", 0, paramView);
              }
              if (PhoneAgent.this.isHomeMarketShopType())
              {
                paramView = new ArrayList();
                paramView.add(new BasicNameValuePair("shopid", PhoneAgent.this.shopId() + ""));
                PhoneAgent.this.statisticsEvent("shopinfoh", "shopinfoh_tel_call", "2", 0, paramView);
              }
            }
          });
          i += 1;
        }
      }
      if (isHomeDesignShopType())
      {
        localObject1 = new ArrayList();
        ((List)localObject1).add(new BasicNameValuePair("shopid", shopId() + ""));
        statisticsEvent("shopinfoh", "shopinfoh_tel", "1", 0, (List)localObject1);
      }
      if (isHomeMarketShopType())
      {
        localObject1 = new ArrayList();
        ((List)localObject1).add(new BasicNameValuePair("shopid", shopId() + ""));
        statisticsEvent("shopinfoh", "shopinfoh_tel", "2", 0, (List)localObject1);
      }
      if (localArrayList2.size() == 1)
      {
        ((View.OnClickListener)localArrayList2.get(0)).onClick(null);
        return;
      }
      localObject2 = new AlertDialog.Builder(getContext());
      Object localObject3 = new StringBuilder().append("联系");
      if (isCommunityType())
      {
        localObject1 = "物业";
        ((AlertDialog.Builder)localObject2).setTitle((String)localObject1);
        ((AlertDialog.Builder)localObject2).setAdapter(new ArrayAdapter(getContext(), 17367043, localArrayList1), new DialogInterface.OnClickListener(localArrayList2)
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            ((View.OnClickListener)this.val$callbacks.get(paramInt)).onClick(null);
          }
        });
        localObject1 = ((AlertDialog.Builder)localObject2).create();
        ((AlertDialog)localObject1).show();
        ((AlertDialog)localObject1).setCanceledOnTouchOutside(true);
        localObject1 = new ArrayList();
        ((List)localObject1).add(new BasicNameValuePair("shopid", shopId() + ""));
        if (!isTravelType())
          break label572;
        statisticsEvent("shopinfo5", "shopinfo5_tel2", shopId() + "", 0, (List)localObject1);
      }
      while (true)
      {
        if (!isWeddingType())
          break label607;
        localObject1 = new ArrayList();
        ((List)localObject1).add(new BasicNameValuePair("shopid", shopId() + ""));
        statisticsEvent("shopinfow", "shopinfow_tel", "", 0, (List)localObject1);
        return;
        localObject1 = "商户";
        break;
        statisticsEvent("shopinfo5", "shopinfo5_tel", shopId() + "", 0, (List)localObject1);
      }
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    Object localObject2 = getShop();
    if (localObject2 == null)
      removeAllCells();
    label419: label714: label887: 
    while (true)
    {
      return;
      boolean bool1 = hasPhone();
      boolean bool2 = hasWedding();
      Object localObject1 = null;
      paramBundle = null;
      String[] arrayOfString;
      String str;
      if ((bool2) && (bool1) && (!isWeddingType()) && (!isWeddingShopType()) && (!isHomeDesignShopType()) && (!isHomeMarketShopType()))
      {
        if (!(this.cell instanceof BookingTicketCell))
        {
          this.cell = new BookingTicketCell(getContext());
          this.cell.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
          paramBundle = createBookingCell();
          paramBundle.setGAString("booking", getGAExtra());
          paramBundle.setId(1);
          paramBundle.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 0.6F));
          paramBundle.setBackgroundResource(R.drawable.list_item);
          paramBundle.setClickable(true);
          paramBundle.setOnClickListener(this.clickListener);
          ((BookingTicketCell)this.cell).addView(paramBundle);
          paramBundle = new View(getContext());
          paramBundle.setLayoutParams(new LinearLayout.LayoutParams(1, -1));
          paramBundle.setBackgroundColor(-4013374);
          ((BookingTicketCell)this.cell).addView(paramBundle);
          paramBundle = createCommonCell();
          paramBundle.setGAString("tel", getGAExtra());
          paramBundle.setId(2);
          paramBundle.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 0.4F));
          paramBundle.setBackgroundResource(R.drawable.list_item);
          paramBundle.setClickable(true);
          paramBundle.setOnClickListener(this.clickListener);
          ((BookingTicketCell)this.cell).addView(paramBundle);
        }
        paramBundle = (BookingCell)this.cell.findViewById(1);
        localObject1 = (CommonCell)this.cell.findViewById(2);
        if (localObject1 != null)
        {
          arrayOfString = ((DPObject)localObject2).getStringArray("PhoneNos");
          str = "";
          localObject2 = str;
          if (arrayOfString != null)
          {
            localObject2 = str;
            if (arrayOfString.length > 0)
            {
              if ((!isWeddingType()) && (!isWeddingShopType()) && (!isEducationTypeAndCannotfixAllPhones(arrayOfString)))
                break label714;
              localObject2 = arrayOfString[0];
            }
          }
          label381: ((CommonCell)localObject1).setLeftIcon(R.drawable.detail_icon_phone);
          if (!isCommunityType())
            break label800;
          ((CommonCell)localObject1).setTitle("物业：" + (String)localObject2);
          if ((isWeddingType()) || (isWeddingShopType()))
          {
            ((CommonCell)localObject1).setRightText("电话咨询");
            ((CommonCell)localObject1).setRightTextcolor(getResources().getColor(R.color.light_gray));
          }
          if (isEducationType())
          {
            ((CommonCell)localObject1).setRightText("免费咨询");
            ((CommonCell)localObject1).setRightTextcolor(getResources().getColor(R.color.light_gray));
          }
        }
        if ((paramBundle != null) && (bool2))
        {
          if (getShop().getString("WeddingTips") == null)
            break label808;
          localObject1 = getShop().getString("WeddingTips");
          paramBundle.setTitle((String)localObject1);
          paramBundle.setDiscount();
        }
        if ((!(this.cell instanceof CommonCell)) && (!(this.cell instanceof BookingCell)))
          break label831;
        if (!isWeddingType())
          break label815;
        addCell("0250Basic.09Phone", this.cell, 257);
      }
      while (true)
      {
        if ((!bool1) || (!this.actionCall))
          break label887;
        this.actionCall = false;
        callPhone();
        return;
        if (bool1)
        {
          if (!(this.cell instanceof CommonCell))
          {
            this.cell = createCommonCell();
            ((NovaLinearLayout)this.cell).setGAString("tel", getGAExtra());
          }
          localObject1 = (CommonCell)this.cell;
          break;
        }
        if (bool2)
        {
          if (!(this.cell instanceof BookingCell))
          {
            this.cell = createBookingCell();
            ((NovaLinearLayout)this.cell).setGAString("booking", getGAExtra());
          }
          paramBundle = (BookingCell)this.cell;
          break;
        }
        this.cell = null;
        if (isWeddingType())
        {
          removeCell("0250Basic.09Phone");
          return;
        }
        removeCell("0200Basic.30Phone");
        return;
        int i = 0;
        localObject2 = str;
        if (i >= arrayOfString.length)
          break label381;
        if (i == 0);
        for (str = str + arrayOfString[0]; ; str = str + "、" + arrayOfString[i])
        {
          i += 1;
          break;
        }
        ((CommonCell)localObject1).setTitle((CharSequence)localObject2);
        break label419;
        localObject1 = "免费预约看店";
        break label515;
        addCell("0200Basic.30Phone", this.cell, 257);
        continue;
        if (isWeddingType())
        {
          addCell("0250Basic.09Phone", this.cell, 0);
          continue;
        }
        if (isWeddingShopType())
        {
          addCell("0200Basic.30Phone", this.cell, 257);
          continue;
        }
        addCell("0200Basic.30Phone", this.cell, 0);
      }
    }
  }

  public void onCellClick(String paramString, View paramView)
  {
    super.onCellClick(paramString, paramView);
    if (hasPhone())
      callPhone();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle == null)
      this.actionCall = "call".equalsIgnoreCase(getFragment().getStringParam("action"));
    while (true)
    {
      sendRequest();
      return;
      this.actionCall = paramBundle.getBoolean("actionCall");
      this.mIamShoperObject = ((DPObject)paramBundle.getParcelable("IamShoperObject"));
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mIamShoperRequest == paramMApiRequest)
    {
      this.mRetryCount += 1;
      this.mIamShoperRequest = null;
      if (this.mRetryCount >= 3)
        dispatchAgentChanged(false);
    }
    else
    {
      return;
    }
    sendRequest();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest != null) && (this.mIamShoperRequest == paramMApiRequest))
    {
      this.mIamShoperRequest = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        this.mIamShoperObject = ((DPObject)paramMApiResponse.result());
        dispatchAgentChanged(false);
      }
    }
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putBoolean("actionCall", this.actionCall);
    localBundle.putParcelable("IamShoperObject", this.mIamShoperObject);
    return localBundle;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.PhoneAgent
 * JD-Core Version:    0.6.0
 */