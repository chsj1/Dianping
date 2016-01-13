package com.dianping.shopinfo.verticalchannel.book;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaRelativeLayout;

public class FreeBookingAgent extends ShopCellAgent
{
  private static final String API_URL = "http://mapi.dianping.com/beauty/getfreebook.bin?";
  private static final String CELL_VERTICAL_CHANNEL_FREEBOOKING = "0200Basic.40FreeBooking";
  private static final int ORDER_TYPE_HAS_ORDERED = 2;
  private static final int ORDER_TYPE_NOT_ORDERED = 1;
  private static final String TAG = FreeBookingAgent.class.getSimpleName();
  protected int categoryId;
  protected DPObject mBookingInfo;
  private View mBookingView;
  protected MApiRequest mRequest;
  private RequestHandler<MApiRequest, MApiResponse> mRequestHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      FreeBookingAgent.this.mRequest = null;
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (FreeBookingAgent.this.mRequest != paramMApiRequest);
      do
      {
        do
          return;
        while (paramMApiResponse.result() == null);
        FreeBookingAgent.this.mBookingInfo = ((DPObject)paramMApiResponse.result());
      }
      while (FreeBookingAgent.this.mBookingInfo == null);
      FreeBookingAgent.this.dispatchAgentChanged(false);
    }
  };
  private View.OnClickListener mclickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if ((paramView.getTag() == null) || (!(paramView.getTag() instanceof FreeBookingAgent.FreeBookingHolder)));
      do
      {
        return;
        paramView = (FreeBookingAgent.FreeBookingHolder)paramView.getTag();
        if (paramView.type != 2)
          continue;
        FreeBookingAgent.this.showDialog(paramView);
        return;
      }
      while ((paramView.type != 1) || (TextUtils.isEmpty(paramView.bookAction)));
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView.bookAction));
      FreeBookingAgent.this.startActivity(paramView);
    }
  };
  protected int shopId;

  public FreeBookingAgent(Object paramObject)
  {
    super(paramObject);
  }

  private AutoHideTextView buildFreeBookingPromoView(String paramString)
  {
    AutoHideTextView localAutoHideTextView = new AutoHideTextView(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
    localLayoutParams.setMargins(5, 0, 0, 0);
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

  private FreeBookingHolder createFreeBookingHolder()
  {
    if (this.mBookingInfo == null);
    int i;
    String str1;
    String str2;
    String str3;
    String str4;
    String str5;
    String str6;
    String str7;
    String str8;
    do
    {
      return null;
      i = this.mBookingInfo.getInt("Type");
      str1 = this.mBookingInfo.getString("Desc");
      str2 = this.mBookingInfo.getString("OrderViewActionLabel");
      str3 = this.mBookingInfo.getString("BookAgainActionLabel");
      str4 = this.mBookingInfo.getString("Action");
      str5 = this.mBookingInfo.getString("OrderViewAction");
      str6 = this.mBookingInfo.getString("BookAgainAction");
      str7 = this.mBookingInfo.getString("Remark");
      str8 = this.mBookingInfo.getString("TotalCount");
    }
    while (TextUtils.isEmpty(str1));
    return new FreeBookingHolder(i, str1, str2, str3, str4, str5, str6, str7, str8);
  }

  private View createFreeBookingView(FreeBookingHolder paramFreeBookingHolder)
  {
    NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)this.res.inflate(getContext(), R.layout.shop_vertical_channel_freebooking_cell, getParentView(), false);
    localNovaRelativeLayout.gaUserInfo.shop_id = Integer.valueOf(this.shopId);
    localNovaRelativeLayout.gaUserInfo.category_id = Integer.valueOf(this.categoryId);
    localNovaRelativeLayout.setGAString("shopinfo5_newrspbooking");
    ((DPActivity)getFragment().getActivity()).addGAView(localNovaRelativeLayout, -1);
    localNovaRelativeLayout.setTag(paramFreeBookingHolder);
    localNovaRelativeLayout.setOnClickListener(this.mclickListener);
    ((TextView)localNovaRelativeLayout.findViewById(R.id.freebooking_desc)).setText(paramFreeBookingHolder.desc);
    if (!TextUtils.isEmpty(paramFreeBookingHolder.orderCountInfo))
      ((TextView)localNovaRelativeLayout.findViewById(R.id.freebooking_ordercntinfo)).setText(paramFreeBookingHolder.orderCountInfo);
    paramFreeBookingHolder = this.mBookingInfo.getStringArray("PromoList");
    if ((paramFreeBookingHolder != null) && (paramFreeBookingHolder.length > 0))
    {
      LinearLayout localLinearLayout = (LinearLayout)localNovaRelativeLayout.findViewById(R.id.freebooking_promos);
      int j = paramFreeBookingHolder.length;
      int i = 0;
      if (i < j)
      {
        CharSequence localCharSequence = paramFreeBookingHolder[i];
        if (TextUtils.isEmpty(localCharSequence));
        while (true)
        {
          i += 1;
          break;
          localLinearLayout.addView(buildFreeBookingPromoView(localCharSequence));
        }
      }
    }
    return localNovaRelativeLayout;
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
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/beauty/getfreebook.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", this.shopId + "");
    localBuilder.appendQueryParameter("token", paramString);
    this.mRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mRequest, this.mRequestHandler);
  }

  private void showDialog(FreeBookingHolder paramFreeBookingHolder)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getContext());
    String str2 = paramFreeBookingHolder.bookAgainActionLabel;
    String str3 = paramFreeBookingHolder.viewActionLabel;
    if ((TextUtils.isEmpty(str2)) || (TextUtils.isEmpty(str3)))
      return;
    String str1 = "已有订单, 是否再次预约?";
    if (!TextUtils.isEmpty(paramFreeBookingHolder.remark))
      str1 = paramFreeBookingHolder.remark;
    localBuilder.setTitle(str1);
    paramFreeBookingHolder = new DialogInterface.OnClickListener(paramFreeBookingHolder)
    {
      private static final int POSITION_BOOK_AGAIN = 0;
      private static final int POSITION_VIEW = 1;

      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        switch (paramInt)
        {
        default:
          return;
        case 0:
          FreeBookingAgent.this.dialogGoAction(this.val$holder.bookAgainAction);
          FreeBookingAgent.this.statisticsManually("shopinfo5_newrspbooking_booked_again", "tap");
          return;
        case 1:
        }
        FreeBookingAgent.this.dialogGoAction(this.val$holder.viewAction);
        FreeBookingAgent.this.statisticsManually("shopinfo5_newrspbooking_booked_check", "tap");
      }
    };
    localBuilder.setItems(new String[] { str2, str3 }, paramFreeBookingHolder);
    localBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.dismiss();
      }
    });
    localBuilder.show();
    statisticsManually("shopinfo5_newrspbooking_booked", "view");
  }

  protected void dialogGoAction(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return;
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramString)));
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (this.mBookingView != null);
    do
    {
      return;
      super.onAgentChanged(paramBundle);
      paramBundle = createFreeBookingHolder();
    }
    while (paramBundle == null);
    this.mBookingView = createFreeBookingView(paramBundle);
    addCell("0200Basic.40FreeBooking", this.mBookingView);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    extractShopInfo();
    if (!paramIsValid())
      return;
    sendRequest(extractToken());
  }

  public void onDestroy()
  {
    super.onDestroy();
    if ((this.mRequest != null) && (getFragment() != null) && (getFragment().mapiService() != null))
      getFragment().mapiService().abort(this.mRequest, this.mRequestHandler, true);
  }

  protected void statisticsManually(String paramString1, String paramString2)
  {
    GAUserInfo localGAUserInfo = new GAUserInfo();
    localGAUserInfo.shop_id = Integer.valueOf(this.shopId);
    localGAUserInfo.category_id = Integer.valueOf(this.categoryId);
    GAHelper.instance().contextStatisticsEvent(getContext(), paramString1, localGAUserInfo, paramString2);
  }

  class FreeBookingHolder
  {
    String bookAction;
    String bookAgainAction;
    String bookAgainActionLabel;
    String desc;
    String orderCountInfo;
    String remark;
    int type;
    String viewAction;
    String viewActionLabel;

    public FreeBookingHolder(int paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String arg10)
    {
      this.type = paramString1;
      this.desc = paramString2;
      this.viewActionLabel = paramString3;
      this.bookAgainActionLabel = paramString4;
      this.viewAction = paramString6;
      this.bookAction = paramString5;
      this.bookAgainAction = paramString7;
      this.remark = paramString8;
      Object localObject;
      this.orderCountInfo = localObject;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.verticalchannel.book.FreeBookingAgent
 * JD-Core Version:    0.6.0
 */