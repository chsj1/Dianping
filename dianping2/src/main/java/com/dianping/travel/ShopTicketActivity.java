package com.dianping.travel;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.travel.view.ShopTicketItem;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ShopTicketActivity extends NovaActivity
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final SimpleDateFormat SDF2 = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
  long checkinTimeMills;
  long checkoutTimeMills;
  private int expandIndex = -1;
  private View notice;
  private int otaID;
  private String otaName;
  private MApiRequest req;
  private DPObject sceneryTickes;
  private int shopID;
  private LinearLayout ticketsTable;

  private void handlerResult()
  {
    if (this.sceneryTickes == null);
    while (true)
    {
      return;
      DPObject[] arrayOfDPObject = this.sceneryTickes.getArray("List");
      if (arrayOfDPObject == null)
        continue;
      int i = 0;
      while (i < arrayOfDPObject.length)
      {
        DPObject localDPObject = arrayOfDPObject[i];
        ShopTicketItem localShopTicketItem = (ShopTicketItem)LayoutInflater.from(this).inflate(R.layout.shopticket_item, null);
        localShopTicketItem.setData(localDPObject);
        localShopTicketItem.setClickable(true);
        localShopTicketItem.setOnBookBtnClickedListener(new View.OnClickListener(localDPObject)
        {
          public void onClick(View paramView)
          {
            Object localObject = this.val$ticket.getString("Url");
            if (localObject != null)
            {
              paramView = (View)localObject;
              if (!((String)localObject).equals(""));
            }
            else
            {
              paramView = "dianping";
            }
            if (paramView != null)
            {
              if (!paramView.startsWith("dianping"))
                break label153;
              paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://submitsceneryorder?shopid=" + ShopTicketActivity.this.shopID + "&aheaddays=" + ShopTicketActivity.this.sceneryTickes.getInt("AheadDays") + "&aheadtime=" + ShopTicketActivity.this.sceneryTickes.getString("AheadTime")));
              paramView.putExtra("ticket", this.val$ticket);
              ShopTicketActivity.this.startActivity(paramView);
              ShopTicketActivity.this.statisticsEvent("ticket5", "ticket5_info_order", "", 0);
            }
            label153: 
            do
              return;
            while (!paramView.startsWith("http"));
            localObject = Uri.parse("dianping://scenerybookingweb");
            paramView = Uri.parse(paramView).buildUpon().appendQueryParameter("shopId", ShopTicketActivity.this.shopID + "").appendQueryParameter("startDate", ShopTicketActivity.SDF2.format(Long.valueOf(ShopTicketActivity.this.checkinTimeMills))).appendQueryParameter("endDate", ShopTicketActivity.SDF2.format(Long.valueOf(ShopTicketActivity.this.checkoutTimeMills))).build();
            paramView = new Intent("android.intent.action.VIEW", ((Uri)localObject).buildUpon().appendQueryParameter("url", paramView.toString()).build());
            paramView.putExtra("name", ShopTicketActivity.this.otaName);
            ShopTicketActivity.this.startActivity(paramView);
          }
        });
        localShopTicketItem.setOnClickListener(this);
        this.ticketsTable.addView(localShopTicketItem);
        i += 1;
      }
    }
  }

  private void requestData()
  {
    showProgressDialog("正在加载数据，请稍候...");
    if (this.req != null)
      mapiService().abort(this.req, this, true);
    this.req = BasicMApiRequest.mapiGet("http://m.api.dianping.com/hotel/getsceneryticketlist.hotel?shopid=" + this.shopID + "&otaid=" + this.otaID, CacheType.NORMAL);
    mapiService().exec(this.req, this);
  }

  public void onClick(View paramView)
  {
    if (this.sceneryTickes == null)
      return;
    Object localObject;
    if (paramView == this.notice)
    {
      localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://sceneryordernotice"));
      ((Intent)localObject).putExtra("notices", this.sceneryTickes.getArray("Notices"));
      startActivity((Intent)localObject);
      statisticsEvent("ticket5", "ticket5_info_notice", "", 0);
    }
    int i = 0;
    label65: if (i < this.ticketsTable.getChildCount())
      if (paramView == this.ticketsTable.getChildAt(i))
      {
        localObject = (ShopTicketItem)paramView;
        if (((ShopTicketItem)localObject).expandAble())
        {
          if (!((ShopTicketItem)localObject).isExpand())
            break label123;
          ((ShopTicketItem)localObject).shrink();
          this.expandIndex = -1;
        }
      }
    while (true)
    {
      i += 1;
      break label65;
      break;
      label123: ((ShopTicketItem)localObject).expand();
      if (this.expandIndex >= 0)
        ((ShopTicketItem)this.ticketsTable.getChildAt(this.expandIndex)).shrink();
      this.expandIndex = i;
      statisticsEvent("ticket5", "ticket5_info_intro", "", 0);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    try
    {
      this.shopID = Integer.valueOf(getIntent().getData().getQueryParameter("shopid")).intValue();
      this.otaID = Integer.valueOf(getIntent().getData().getQueryParameter("otaid")).intValue();
      this.otaName = getIntent().getData().getQueryParameter("otaname");
      super.setContentView(R.layout.shopticket);
      this.notice = findViewById(R.id.notice);
      this.notice.setOnClickListener(this);
      this.ticketsTable = ((LinearLayout)findViewById(R.id.ticketsTable));
      paramBundle = getIntent();
      this.checkinTimeMills = paramBundle.getLongExtra("checkinTime", System.currentTimeMillis());
      this.checkoutTimeMills = paramBundle.getLongExtra("checkoutTime", System.currentTimeMillis() + 86400000L);
      requestData();
      return;
    }
    catch (Exception paramBundle)
    {
      while (true)
        Log.e(paramBundle.toString());
    }
  }

  protected void onDestroy()
  {
    if (this.req != null)
      mapiService().abort(this.req, this, true);
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    showMessageDialog(paramMApiResponse.message(), new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        ShopTicketActivity.this.finish();
      }
    });
    statisticsEvent("ticket5", "ticket5_submit_failure", paramMApiResponse.message().content(), 0);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      this.sceneryTickes = ((DPObject)paramMApiResponse.result());
      handlerResult();
    }
    dismissDialog();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.ShopTicketActivity
 * JD-Core Version:    0.6.0
 */