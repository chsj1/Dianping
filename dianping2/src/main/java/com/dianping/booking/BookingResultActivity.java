package com.dianping.booking;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.widget.TitleBar;
import com.dianping.booking.adapter.BookingDialogAdapter;
import com.dianping.booking.util.BookingShareUtil;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.Location;
import com.dianping.model.UserProfile;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BookingResultActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private DPObject[] activityItems;
  private DPObject bookingRecord;
  private DPObject bookingResult;
  private MApiRequest brandingRequest;
  private int shopId;
  private int successType;

  private void getBrandingTask()
  {
    if (this.brandingRequest != null)
      return;
    StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/yytopbarad.bin?");
    localStringBuilder.append("cityid=").append(cityId());
    localStringBuilder.append("&shopid=").append(this.shopId);
    Location localLocation = location();
    if (localLocation != null)
    {
      DecimalFormat localDecimalFormat = Location.FMT;
      localStringBuilder.append("&lat=").append(localDecimalFormat.format(localLocation.latitude()));
      localStringBuilder.append("&lng=").append(localDecimalFormat.format(localLocation.longitude()));
    }
    this.brandingRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.NORMAL);
    mapiService().exec(this.brandingRequest, this);
  }

  private String getToken()
  {
    if (accountService() == null)
      return "";
    return accountService().token();
  }

  private void initData()
  {
    Bundle localBundle = getIntent().getExtras();
    this.shopId = super.getIntParam("shopId");
    this.bookingResult = ((DPObject)localBundle.getParcelable("result"));
    this.successType = this.bookingResult.getInt("IsSuccess");
    this.bookingRecord = this.bookingResult.getObject("Record");
    this.activityItems = this.bookingResult.getArray("BookingResultActivity");
    if ((this.bookingResult.getBoolean("ShowAD")) && (this.successType != 60))
      getBrandingTask();
  }

  private void redirectToLotteryPage(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      paramString = Uri.parse(paramString).buildUpon();
      paramString.appendQueryParameter("token", getToken());
      startActivity("dianping://web?url=" + paramString.toString());
    }
  }

  private void setupActivityInfoView(DPObject[] paramArrayOfDPObject)
  {
    RelativeLayout localRelativeLayout = (RelativeLayout)findViewById(R.id.activity_layout);
    NetworkImageView localNetworkImageView = (NetworkImageView)findViewById(R.id.activity_icon);
    TextView localTextView = (TextView)findViewById(R.id.activity_title);
    if ((paramArrayOfDPObject != null) && (paramArrayOfDPObject.length != 0))
    {
      localNetworkImageView.setImage(paramArrayOfDPObject[0].getString("IconUrl"));
      ViewUtils.setVisibilityAndContent(localTextView, paramArrayOfDPObject[0].getString("Title"));
      localRelativeLayout.setOnClickListener(new View.OnClickListener(paramArrayOfDPObject[0].getString("ActionUrl"))
      {
        public void onClick(View paramView)
        {
          if (TextUtils.isEmpty(BookingResultActivity.this.getToken()))
            BookingResultActivity.this.accountService().login(new BookingResultActivity.2.1(this));
          while (true)
          {
            BookingResultActivity.this.statisticsEvent("booking6", "booking6_fanpiao_forsubmit", "", 0);
            return;
            BookingResultActivity.this.redirectToLotteryPage(this.val$url);
          }
        }
      });
      localRelativeLayout.setVisibility(0);
      return;
    }
    localRelativeLayout.setVisibility(8);
  }

  private void setupBranInfoView(DPObject paramDPObject)
  {
    RelativeLayout localRelativeLayout = (RelativeLayout)findViewById(R.id.branding_layout);
    NetworkImageView localNetworkImageView = (NetworkImageView)findViewById(R.id.branding_image);
    ImageView localImageView = (ImageView)findViewById(R.id.branding_close);
    paramDPObject = paramDPObject.getString("ImageUrl");
    if (!TextUtils.isEmpty(paramDPObject))
    {
      localNetworkImageView.setRoundPixels(20);
      localNetworkImageView.setImage(paramDPObject);
      localRelativeLayout.setVisibility(0);
    }
    while (true)
    {
      localImageView.setOnClickListener(new View.OnClickListener(localRelativeLayout)
      {
        public void onClick(View paramView)
        {
          if (ViewUtils.isShow(this.val$brandingLayout))
          {
            this.val$brandingLayout.setVisibility(8);
            BookingResultActivity.this.statisticsEvent("booking6", "booking6_order_adcancel", "", 0);
          }
        }
      });
      return;
      localRelativeLayout.setVisibility(8);
    }
  }

  private void setupOperationView(DPObject paramDPObject, int paramInt)
  {
    paramDPObject = (Button)findViewById(R.id.view_order);
    Button localButton = (Button)findViewById(R.id.send_friend);
    if (paramInt == 50);
    for (paramInt = 0; ; paramInt = 8)
    {
      localButton.setVisibility(paramInt);
      paramDPObject.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://bookinginfo"));
          paramView.setFlags(67108864);
          Bundle localBundle = new Bundle();
          localBundle.putParcelable("bookingRecord", BookingResultActivity.this.bookingRecord);
          paramView.putExtras(localBundle);
          BookingResultActivity.this.startActivity(paramView);
          BookingResultActivity.this.finish();
          BookingResultActivity.this.statisticsEvent("booking5", "booking5_vieworder", "", 0);
          GAHelper.instance().contextStatisticsEvent(BookingResultActivity.this, "process", null, "tap");
        }
      });
      localButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          BookingResultActivity.this.showShareDialog();
        }
      });
      return;
    }
  }

  private void setupRecordInfoView(boolean paramBoolean, DPObject paramDPObject)
  {
    LinearLayout localLinearLayout = (LinearLayout)findViewById(R.id.record_info);
    int i;
    Object localObject2;
    TextView localTextView5;
    TextView localTextView1;
    TextView localTextView2;
    if (paramBoolean)
    {
      i = R.drawable.booking_result_bg;
      localLinearLayout.setBackgroundResource(i);
      localLinearLayout.setPadding(ViewUtils.dip2px(this, 16.0F), 0, ViewUtils.dip2px(this, 16.0F), 0);
      localObject1 = (TextView)findViewById(R.id.shop_name);
      localObject2 = (ImageView)findViewById(R.id.fir_instead_icon);
      TextView localTextView3 = (TextView)findViewById(R.id.book_time);
      TextView localTextView4 = (TextView)findViewById(R.id.book_num);
      localTextView5 = (TextView)findViewById(R.id.book_room);
      localTextView1 = (TextView)findViewById(R.id.name);
      localTextView2 = (TextView)findViewById(R.id.phone);
      if (paramDPObject == null)
        break label384;
      ViewUtils.setVisibilityAndContent((TextView)localObject1, paramDPObject.getString("ShopName"));
      if (!paramDPObject.getBoolean("InsteadRecord"))
        break label348;
      i = 0;
      label160: ((ImageView)localObject2).setVisibility(i);
      ViewUtils.setVisibilityAndContent(localTextView3, new SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault()).format(Long.valueOf(paramDPObject.getTime("BookingTime"))));
      ViewUtils.setVisibilityAndContent(localTextView4, paramDPObject.getInt("PeopleNumber") + "位");
      switch (paramDPObject.getInt("PositionType"))
      {
      default:
        localTextView5.setText("大厅");
        label272: localObject2 = new StringBuilder().append(paramDPObject.getString("BookerName"));
        if (paramDPObject.getInt("BookerGender") != 10)
          break;
      case 20:
      case 30:
      }
    }
    for (Object localObject1 = "女士"; ; localObject1 = "先生")
    {
      localTextView1.setText((String)localObject1);
      ViewUtils.setVisibilityAndContent(localTextView2, paramDPObject.getString("BookerPhone"));
      localLinearLayout.setVisibility(0);
      return;
      i = R.drawable.booking_result_bg_bottom;
      break;
      label348: i = 8;
      break label160;
      localTextView5.setText("包房优先");
      break label272;
      localTextView5.setText("包房");
      break label272;
    }
    label384: localLinearLayout.setVisibility(8);
  }

  private void setupResultInfoView(DPObject paramDPObject)
  {
    Object localObject1 = paramDPObject.getObject("Record").getObject("PrepayInfo");
    String[] arrayOfString = paramDPObject.getStringArray("Tips");
    LinearLayout localLinearLayout1 = (LinearLayout)findViewById(R.id.deposit_booking_msg_layout);
    LinearLayout localLinearLayout2 = (LinearLayout)findViewById(R.id.common_booking_msg);
    TextView localTextView1 = (TextView)findViewById(R.id.deposit_text);
    TextView localTextView2 = (TextView)findViewById(R.id.deposit_price);
    Object localObject2 = (ImageView)findViewById(R.id.prompt_view);
    switch (paramDPObject.getInt("IsSuccess"))
    {
    default:
    case 10:
    case 11:
    }
    while (localObject1 != null)
    {
      localTextView2.setText(((DPObject)localObject1).getString("CurrencySign") + ((DPObject)localObject1).getString("PrepayAmount"));
      paramDPObject = (TextView)findViewById(R.id.deposit_booking_msg1);
      localObject1 = (TextView)findViewById(R.id.deposit_booking_msg2);
      localObject2 = (TextView)findViewById(R.id.deposit_booking_msg3);
      if ((arrayOfString != null) && (arrayOfString.length != 0))
      {
        ViewUtils.setVisibilityAndContent(paramDPObject, arrayOfString[0]);
        ViewUtils.setVisibilityAndContent((TextView)localObject1, arrayOfString[1]);
        ViewUtils.setVisibilityAndContent((TextView)localObject2, arrayOfString[2]);
      }
      localTextView1.setVisibility(0);
      localTextView2.setVisibility(0);
      localLinearLayout1.setVisibility(0);
      localLinearLayout2.setVisibility(8);
      return;
      ((ImageView)localObject2).setImageResource(R.drawable.yy_prompt_icon_wait);
      continue;
      ((ImageView)localObject2).setImageResource(R.drawable.yy_prompt_icon_closed);
    }
    localObject1 = (TextView)findViewById(R.id.msg1);
    localObject2 = (TextView)findViewById(R.id.msg2);
    TextView localTextView3 = (TextView)findViewById(R.id.msg3);
    switch (paramDPObject.getInt("IsSuccess"))
    {
    default:
    case 10:
    case 11:
    }
    while (true)
    {
      localTextView1.setVisibility(8);
      localTextView2.setVisibility(8);
      localLinearLayout1.setVisibility(8);
      localLinearLayout2.setVisibility(0);
      return;
      if ((arrayOfString == null) || (arrayOfString.length == 0))
        continue;
      ViewUtils.setVisibilityAndContent((TextView)localObject1, arrayOfString[0]);
      ViewUtils.setVisibilityAndContent((TextView)localObject2, arrayOfString[1]);
      ViewUtils.setVisibilityAndContent(localTextView3, arrayOfString[2]);
      continue;
      if ((arrayOfString == null) || (arrayOfString.length == 0))
        continue;
      ViewUtils.setVisibilityAndContent((TextView)localObject1, arrayOfString[0]);
      ViewUtils.setVisibilityAndContent((TextView)localObject2, arrayOfString[1]);
      localTextView3.setVisibility(8);
    }
  }

  private void setupViews()
  {
    super.setTitle("订座已提交");
    if (this.successType != 60)
    {
      setupResultInfoView(this.bookingResult);
      setupActivityInfoView(this.activityItems);
      if ((this.activityItems == null) || (this.activityItems.length == 0))
        break label71;
    }
    label71: for (boolean bool = true; ; bool = false)
    {
      setupRecordInfoView(bool, this.bookingRecord);
      setupOperationView(this.bookingRecord, this.successType);
      return;
    }
  }

  private void showShareDialog()
  {
    Object localObject = new ArrayList();
    ((ArrayList)localObject).add("发送短信");
    ((ArrayList)localObject).add("发给微信好友");
    ((ArrayList)localObject).add("分享到微博等网站");
    ((ArrayList)localObject).add("发送邮件");
    localObject = new BookingDialogAdapter(this, (ArrayList)localObject);
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle("发给好友").setAdapter((ListAdapter)localObject, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        if (BookingResultActivity.this.bookingRecord == null)
        {
          BookingResultActivity.this.showToast("请稍后再试");
          return;
        }
        paramDialogInterface = new SimpleDateFormat("yyyy-MM-dd E HH:mm", Locale.getDefault()).format(Long.valueOf(BookingResultActivity.this.bookingRecord.getTime("BookingTime")));
        String str2 = BookingResultActivity.this.bookingRecord.getString("ShopName");
        Object localObject = BookingResultActivity.this.bookingRecord.getString("ShopAddress");
        String str3 = BookingResultActivity.this.bookingRecord.getString("ShopContact");
        String str1 = BookingResultActivity.this.bookingRecord.getString("ShopUrl");
        ShareHolder localShareHolder = new ShareHolder();
        switch (paramInt)
        {
        default:
          return;
        case 0:
          localObject = "已预订" + paramDialogInterface + str2 + "," + (String)localObject;
          paramDialogInterface = (DialogInterface)localObject;
          if (!TextUtils.isEmpty(str3))
            paramDialogInterface = (String)localObject + ",联系电话:" + str3;
          localShareHolder.desc = (paramDialogInterface + ",欢迎届时光临!");
          BookingShareUtil.shareToSms(BookingResultActivity.this, localShareHolder);
          BookingResultActivity.this.statisticsEvent("mybooking5", "mybooking5_orderssucceed_sms", "", 0);
          return;
        case 1:
          localObject = "我预订了" + paramDialogInterface + str2 + "的座位,欢迎届时光临！";
          paramDialogInterface = (DialogInterface)localObject;
          if (!TextUtils.isEmpty(str1))
            paramDialogInterface = (String)localObject + str1;
          BookingShareUtil.shareToWX(BookingResultActivity.this, str2, paramDialogInterface, R.drawable.booking_icon_feed, "http://m.api.dianping.com/weixinshop?shopid=" + BookingResultActivity.this.shopId);
          BookingResultActivity.this.statisticsEvent("mybooking5", "mybooking5_orderssucceed_weixin", "", 0);
          return;
        case 2:
          localObject = "Hi,我在大众点评网预订了一家不错的餐厅" + str2 + ",快来看看吧~地址:" + (String)localObject;
          paramDialogInterface = (DialogInterface)localObject;
          if (!TextUtils.isEmpty(str3))
            paramDialogInterface = (String)localObject + ",联系电话:" + str3;
          localObject = paramDialogInterface;
          if (!TextUtils.isEmpty(str1))
            localObject = paramDialogInterface + "," + str1;
          if (TextUtils.isEmpty(BookingResultActivity.this.getToken()))
          {
            BookingResultActivity.this.accountService().login(new BookingResultActivity.5.1(this, (String)localObject));
            return;
          }
          BookingShareUtil.shareToThirdparty(BookingResultActivity.this, BookingResultActivity.this.shopId, BookingResultActivity.this.getAccount().feedFlag(), 3, (String)localObject);
          BookingResultActivity.this.statisticsEvent("mybooking5", "mybooking5_orderssucceed_sns", "", 0);
          return;
        case 3:
        }
        localObject = "Hi!\n我预订了" + paramDialogInterface + str2 + "的座位,欢迎届时光临!\n地址:" + (String)localObject + "\n";
        paramDialogInterface = (DialogInterface)localObject;
        if (!TextUtils.isEmpty(str3))
          paramDialogInterface = (String)localObject + "联系电话:" + str3 + "\n";
        localObject = paramDialogInterface;
        if (!TextUtils.isEmpty(str1))
          localObject = paramDialogInterface + str1;
        localShareHolder.title = ("我预订了" + str2 + "的座位,欢迎届时光临!");
        localShareHolder.desc = ((String)localObject);
        BookingShareUtil.shareToEmail(BookingResultActivity.this, localShareHolder);
        BookingResultActivity.this.statisticsEvent("mybooking5", "mybooking5_orderssucceed_mail", "", 0);
      }
    });
    localObject = localBuilder.create();
    ((AlertDialog)localObject).setCanceledOnTouchOutside(true);
    ((AlertDialog)localObject).show();
  }

  public String getPageName()
  {
    return "bookingresult";
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.online_booking_result);
    paramBundle = super.getTitleBar().findRightViewItemByTag("groupon");
    if (paramBundle != null)
      paramBundle.setVisibility(4);
    initData();
    setupViews();
  }

  public void onDestroy()
  {
    if (this.brandingRequest != null)
    {
      mapiService().abort(this.brandingRequest, this, true);
      this.brandingRequest = null;
    }
    super.onDestroy();
  }

  protected void onNewGAPager(GAUserInfo paramGAUserInfo)
  {
    GAUserInfo localGAUserInfo = paramGAUserInfo;
    if (paramGAUserInfo == null)
      localGAUserInfo = new GAUserInfo();
    if (this.bookingRecord != null)
      localGAUserInfo.order_id = Integer.valueOf(this.bookingRecord.getInt("ID"));
    super.onNewGAPager(localGAUserInfo);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)) && (paramMApiRequest == this.brandingRequest))
    {
      setupBranInfoView((DPObject)paramMApiResponse.result());
      this.brandingRequest = null;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.BookingResultActivity
 * JD-Core Version:    0.6.0
 */