package com.dianping.shopinfo.verticalchannel.book;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPActivity;
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
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaLinearLayout;

public class BookingAgent extends ShopCellAgent
{
  private static final String API_URL = "http://mapi.dianping.com/beauty/getbookinfo.bin?";
  private static final String CELL_VERTICAL_CHANNEL_BOOKING = "0200Basic.07Book";
  private static final int ORDER_TYPE_HAS_ORDERED = 2;
  private static final int ORDER_TYPE_NOT_ORDERED = 1;
  private static final String TAG = BookingAgent.class.getSimpleName();
  protected int categoryId;
  private View.OnClickListener clickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = paramView.getTag();
      if ((paramView == null) || (!(paramView instanceof BookingAgent.BookingHolder)));
      do
      {
        return;
        paramView = (BookingAgent.BookingHolder)paramView;
        if (paramView.type != 2)
          continue;
        BookingAgent.this.showDialog(paramView);
        return;
      }
      while ((paramView.type != 1) || (TextUtils.isEmpty(paramView.bookAction)));
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView.bookAction));
      BookingAgent.this.startActivity(paramView);
    }
  };
  protected DPObject mBookingInfo;
  private View mBookingView;
  protected MApiRequest mRequest;
  private RequestHandler<MApiRequest, MApiResponse> mRequestHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      BookingAgent.this.mRequest = null;
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (BookingAgent.this.mRequest != paramMApiRequest);
      do
      {
        return;
        BookingAgent.this.mBookingInfo = ((DPObject)paramMApiResponse.result());
      }
      while (BookingAgent.this.mBookingInfo == null);
      BookingAgent.this.dispatchAgentChanged(false);
    }
  };
  protected int shopId;

  public BookingAgent(Object paramObject)
  {
    super(paramObject);
  }

  private BookingHolder createBookingHolder()
  {
    if (this.mBookingInfo == null);
    int i;
    String str1;
    String str2;
    do
    {
      return null;
      i = this.mBookingInfo.getInt("Type");
      str1 = this.mBookingInfo.getString("Content");
      str2 = this.mBookingInfo.getString("BookActionLabel");
    }
    while ((TextUtils.isEmpty(str1)) || (TextUtils.isEmpty(str2)));
    String str3 = this.mBookingInfo.getString("Promo");
    String str4 = this.mBookingInfo.getString("OrderViewActionLabel");
    String str5 = this.mBookingInfo.getString("BookAgainActionLabel");
    String str6 = this.mBookingInfo.getString("BookAction");
    String str7 = this.mBookingInfo.getString("OrderViewAction");
    String str8 = this.mBookingInfo.getString("BookAgainAction");
    String str9 = this.mBookingInfo.getString("Remark");
    String str10 = this.mBookingInfo.getString("TotalCount");
    BookingHolder localBookingHolder = new BookingHolder();
    localBookingHolder.bookContent = str1;
    localBookingHolder.bookPromo = str3;
    localBookingHolder.type = i;
    localBookingHolder.bookActionLabel = str2;
    localBookingHolder.viewActionLabel = str4;
    localBookingHolder.bookAgainActionLabel = str5;
    localBookingHolder.bookAction = str6;
    localBookingHolder.viewAction = str7;
    localBookingHolder.bookAgainAction = str8;
    localBookingHolder.remark = str9;
    localBookingHolder.orderCountInfo = str10;
    return localBookingHolder;
  }

  private View createBookingView(BookingHolder paramBookingHolder, boolean paramBoolean)
  {
    RelativeLayout localRelativeLayout = (RelativeLayout)this.res.inflate(getContext(), R.layout.shop_vertical_channel_booking_cell, getParentView(), false);
    NovaLinearLayout localNovaLinearLayout = (NovaLinearLayout)localRelativeLayout.findViewById(R.id.booking_container);
    localNovaLinearLayout.gaUserInfo.shop_id = Integer.valueOf(this.shopId);
    localNovaLinearLayout.gaUserInfo.category_id = Integer.valueOf(this.categoryId);
    localNovaLinearLayout.setGAString("shopinfo5_newbooking");
    if (!TextUtils.isEmpty(paramBookingHolder.bookContent))
    {
      localObject = (TextView)localNovaLinearLayout.findViewById(R.id.booking_content);
      ((TextView)localObject).setText(paramBookingHolder.bookContent);
      ((TextView)localObject).setVisibility(0);
    }
    if (!TextUtils.isEmpty(paramBookingHolder.orderCountInfo))
      ((TextView)localNovaLinearLayout.findViewById(R.id.booking_ordercntinfo)).setText(paramBookingHolder.orderCountInfo);
    Object localObject = (NovaButton)localNovaLinearLayout.findViewById(R.id.booking_button);
    ((NovaButton)localObject).gaUserInfo.shop_id = Integer.valueOf(this.shopId);
    ((NovaButton)localObject).gaUserInfo.category_id = Integer.valueOf(this.categoryId);
    ((NovaButton)localObject).setGAString("shopinfo5_newbooking");
    ((DPActivity)getFragment().getActivity()).addGAView(localNovaLinearLayout, -1);
    ((NovaButton)localObject).setText(paramBookingHolder.bookActionLabel);
    ((NovaButton)localObject).setTag(paramBookingHolder);
    ((NovaButton)localObject).setOnClickListener(this.clickListener);
    localNovaLinearLayout.setTag(paramBookingHolder);
    localNovaLinearLayout.setOnClickListener(this.clickListener);
    if (paramBoolean)
      localRelativeLayout.findViewById(R.id.book_group_bottom_line).setVisibility(0);
    return (View)localRelativeLayout;
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

  private void sendRequest(String paramString)
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/beauty/getbookinfo.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", this.shopId + "");
    localBuilder.appendQueryParameter("token", paramString);
    this.mRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mRequest, this.mRequestHandler);
  }

  private void showDialog(BookingHolder paramBookingHolder)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getContext());
    String str2 = paramBookingHolder.bookAgainActionLabel;
    String str3 = paramBookingHolder.viewActionLabel;
    if ((TextUtils.isEmpty(str2)) || (TextUtils.isEmpty(str3)))
      return;
    String str1 = "已有订单, 是否再次预约?";
    if (!TextUtils.isEmpty(paramBookingHolder.remark))
      str1 = paramBookingHolder.remark;
    localBuilder.setTitle(str1);
    paramBookingHolder = new DialogInterface.OnClickListener(paramBookingHolder)
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
          BookingAgent.this.dialogGoAction(this.val$holder.bookAgainAction);
          BookingAgent.this.statisticsManually("shopinfo5_newbooking_booked_again", "tap");
          return;
        case 1:
        }
        BookingAgent.this.dialogGoAction(this.val$holder.viewAction);
        BookingAgent.this.statisticsManually("shopinfo5_newbooking_booked_check", "tap");
      }
    };
    localBuilder.setItems(new String[] { str2, str3 }, paramBookingHolder);
    localBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.dismiss();
      }
    });
    localBuilder.show();
    statisticsManually("shopinfo5_newbooking_booked", "view");
  }

  protected void dialogGoAction(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return;
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramString)));
  }

  public View getView()
  {
    return this.mBookingView;
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
    this.mBookingView = createBookingView(paramBundle, false);
    addCell("0200Basic.07Book", this.mBookingView);
    setTopView(createBookingView(paramBundle, true), this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    extractShopInfo();
    if (this.shopId <= 0)
      return;
    String str = accountService().token();
    paramBundle = str;
    if (str == null)
      paramBundle = "";
    sendRequest(paramBundle);
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

  class BookingHolder
  {
    protected String bookAction;
    protected String bookActionLabel;
    protected String bookAgainAction;
    protected String bookAgainActionLabel;
    protected String bookContent;
    protected String bookPromo;
    protected String orderCountInfo;
    protected String remark;
    protected int type;
    protected String viewAction;
    protected String viewActionLabel;

    BookingHolder()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.verticalchannel.book.BookingAgent
 * JD-Core Version:    0.6.0
 */