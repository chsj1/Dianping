package com.dianping.movie.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.movie.util.MovieUtil;
import com.dianping.util.DateUtil;
import com.dianping.util.DateUtils;
import com.dianping.util.DeviceUtils;
import com.dianping.util.KeyboardUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieTicketOrderConfirmActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, LoginResultListener, View.OnClickListener
{
  private final int LOGIN_STEP_MOVIETICKETORDERDETAIL = 2;
  private final int LOGIN_STEP_SUBMITORDER = 1;
  private final int LOGIN_STEP_UPDATEMOVIETICKETMOBILE = 3;
  private Button btnSubmitOrder;
  private DPObject currentMovieDiscountPayOption = null;
  private int discountid = 0;
  private DPObject dpMovieShow;
  private DPObject dpMovieTicketOrder;
  private View errorView;
  private EditText etTicketPhone;
  private final DecimalFormat fnum = new DecimalFormat("##0.00");
  private MApiRequest getMoviePurchaseTipRequest;
  private boolean isOrderSubmitted = false;
  private RelativeLayout layerMovieTicketOrderConfirm;
  private LinearLayout layerPurchaseTip;
  private View loadingView;
  private int loginStep = 1;
  private String movieName;
  private MApiRequest movieTicketOrderDetailRequest;
  private int orderid = 0;
  private ArrayList<DPObject> selectSeatList = new ArrayList();
  private String shopName;
  private MApiRequest submitOrderRequest;
  private String sumMoney;
  private TextView tvMovieName;
  private TextView tvMovieType;
  private TextView tvNormalTips;
  private TextView tvSeatText1;
  private TextView tvSeatText2;
  private TextView tvSeatText3;
  private TextView tvSeatText4;
  private TextView tvShopName;
  private TextView tvShowDate;
  private TextView tvSpecialTip;
  private TextView tvTicketNum;
  private MApiRequest updateMovieTicketMobileRequest;

  private String formatDate(long paramLong)
  {
    long l = DateUtil.currentTimeMillis();
    Calendar localCalendar1 = Calendar.getInstance();
    localCalendar1.setTimeInMillis(l);
    Calendar localCalendar2 = Calendar.getInstance();
    localCalendar2.setTimeInMillis(paramLong);
    if ((localCalendar1.get(1) == localCalendar2.get(1)) && (localCalendar1.get(2) == localCalendar2.get(2)))
    {
      int i = localCalendar2.get(5) - localCalendar1.get(5);
      if (i == 0)
        return "今天" + DateUtils.formatDate2TimeZone(paramLong, "MM月dd日(E) HH:mm", "GMT+8");
      if (i == 1)
        return "明天" + DateUtils.formatDate2TimeZone(paramLong, "MM月dd日(E) HH:mm", "GMT+8");
      if (i == 2)
        return "后天" + DateUtils.formatDate2TimeZone(paramLong, "MM月dd日(E) HH:mm", "GMT+8");
    }
    return DateUtils.formatDate2TimeZone(paramLong, "yyyy年MM月dd日(E) HH:mm", "GMT+8");
  }

  private void goToPayOrder()
  {
    if (this.dpMovieTicketOrder != null)
    {
      DPObject localDPObject = this.dpMovieTicketOrder.getObject("PayProduct");
      Object localObject2 = MovieUtil.loadPhoneNo(this, accountService());
      Object localObject1 = localDPObject;
      if (!TextUtils.isEmpty((CharSequence)localObject2))
      {
        localObject1 = localDPObject.getString("Title");
        localObject1 = ((String)localObject1).substring(0, ((String)localObject1).length() - 11) + ((String)localObject2).substring(0, 3) + "****" + ((String)localObject2).substring(7, 11);
        localObject1 = localDPObject.edit().putString("Title", (String)localObject1).generate();
      }
      localDPObject = this.dpMovieTicketOrder.edit().putString("MobileNo", (String)localObject2).generate();
      localObject2 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://moviepayorder"));
      ((Intent)localObject2).putExtra("payproduct", (Parcelable)localObject1);
      ((Intent)localObject2).putExtra("orderid", this.dpMovieTicketOrder.getInt("ID") + "");
      ((Intent)localObject2).putExtra("callbackurl", "dianping://purchasemovieticketresult");
      ((Intent)localObject2).putExtra("callbackfailurl", "dianping://home");
      localObject1 = new Bundle();
      ((Bundle)localObject1).putInt("shopid", this.dpMovieTicketOrder.getInt("ShopID"));
      ((Bundle)localObject1).putInt("orderid", this.dpMovieTicketOrder.getInt("ID"));
      ((Intent)localObject2).putExtra("payextra", (Bundle)localObject1);
      if (this.discountid > 0)
        ((Intent)localObject2).putExtra("moviediscountid", this.discountid);
      ((Intent)localObject2).putExtra("movieticketorder", localDPObject);
      startActivity((Intent)localObject2);
    }
  }

  private void initView()
  {
    setContentView(R.layout.movie_ticket_order_confirm_activity);
    this.layerMovieTicketOrderConfirm = ((RelativeLayout)findViewById(R.id.layer_movieticketorder_confirm));
    this.loadingView = findViewById(R.id.loading);
    this.errorView = findViewById(R.id.error);
    this.tvMovieName = ((TextView)findViewById(R.id.movie_name));
    this.tvMovieType = ((TextView)findViewById(R.id.movie_type));
    this.tvShopName = ((TextView)findViewById(R.id.shop_name));
    this.tvShowDate = ((TextView)findViewById(R.id.show_time));
    this.tvTicketNum = ((TextView)findViewById(R.id.ticket_num));
    this.tvSeatText1 = ((TextView)findViewById(R.id.seat_text_1));
    this.tvSeatText2 = ((TextView)findViewById(R.id.seat_text_2));
    this.tvSeatText3 = ((TextView)findViewById(R.id.seat_text_3));
    this.tvSeatText4 = ((TextView)findViewById(R.id.seat_text_4));
    this.tvSpecialTip = ((TextView)findViewById(R.id.special_tip));
    this.tvNormalTips = ((TextView)findViewById(R.id.normal_tips));
    this.layerPurchaseTip = ((LinearLayout)findViewById(R.id.tip_layout));
    this.etTicketPhone = ((EditText)findViewById(R.id.ticket_phone));
    this.btnSubmitOrder = ((Button)findViewById(R.id.submit_order));
    this.btnSubmitOrder.setOnClickListener(this);
  }

  private void requestMoviePurchaseTip()
  {
    if (this.dpMovieShow == null);
    do
      return;
    while (this.getMoviePurchaseTipRequest != null);
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/getmoviepurchasetipsmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("movieshowid", String.valueOf(this.dpMovieShow.getInt("ID")));
    this.getMoviePurchaseTipRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.getMoviePurchaseTipRequest, this);
  }

  private void requestSubmitTicketOrder()
  {
    if (!isLogined())
    {
      this.loginStep = 1;
      accountService().login(this);
    }
    do
      return;
    while (this.submitOrderRequest != null);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("token");
    localArrayList.add(getAccount().token());
    if (this.dpMovieShow != null)
    {
      localArrayList.add("movieshowid");
      localArrayList.add(String.valueOf(this.dpMovieShow.getInt("ID")));
    }
    String str2 = "";
    String str1 = "";
    int i = 0;
    if (i < this.selectSeatList.size())
    {
      if (i != this.selectSeatList.size() - 1)
        str2 = str2 + ((DPObject)this.selectSeatList.get(i)).getInt("ID") + ",";
      for (str1 = str1 + ((DPObject)this.selectSeatList.get(i)).getString("Name") + ","; ; str1 = str1 + ((DPObject)this.selectSeatList.get(i)).getString("Name"))
      {
        i += 1;
        break;
        str2 = str2 + ((DPObject)this.selectSeatList.get(i)).getInt("ID");
      }
    }
    if (!TextUtils.isEmpty(str2))
    {
      localArrayList.add("seatid");
      localArrayList.add(str2);
    }
    if (!TextUtils.isEmpty(str1))
    {
      localArrayList.add("seatname");
      localArrayList.add(str1);
    }
    localArrayList.add("mobileno");
    localArrayList.add(this.etTicketPhone.getText().toString());
    localArrayList.add("callid");
    localArrayList.add(UUID.randomUUID().toString());
    localArrayList.add("cityid");
    localArrayList.add(String.valueOf(cityId()));
    if (this.currentMovieDiscountPayOption != null)
    {
      localArrayList.add("discountid");
      localArrayList.add(String.valueOf(this.currentMovieDiscountPayOption.getInt("DiscountID")));
    }
    while (true)
    {
      localArrayList.add("cx");
      localArrayList.add(DeviceUtils.cxInfo("submitmovieorder"));
      this.submitOrderRequest = BasicMApiRequest.mapiPost("http://app.movie.dianping.com/rs/createmovieticketordermv.bin", (String[])localArrayList.toArray(new String[0]));
      mapiService().exec(this.submitOrderRequest, this);
      showProgressDialog("请稍候...");
      MovieUtil.savePhoneNo(this, this.etTicketPhone.getText().toString());
      return;
      localArrayList.add("discountid");
      localArrayList.add("0");
    }
  }

  private void setSeatView(ArrayList<DPObject> paramArrayList)
  {
    this.tvSeatText1.setVisibility(4);
    this.tvSeatText2.setVisibility(4);
    this.tvSeatText3.setVisibility(4);
    this.tvSeatText4.setVisibility(4);
    if (paramArrayList.size() > 0)
    {
      int i = 0;
      if (i < paramArrayList.size())
      {
        switch (i)
        {
        default:
        case 0:
        case 1:
        case 2:
        case 3:
        }
        while (true)
        {
          i += 1;
          break;
          this.tvSeatText1.setVisibility(0);
          this.tvSeatText1.setText(((DPObject)paramArrayList.get(i)).getString("Name"));
          continue;
          this.tvSeatText2.setVisibility(0);
          this.tvSeatText2.setText(((DPObject)paramArrayList.get(i)).getString("Name"));
          continue;
          this.tvSeatText3.setVisibility(0);
          this.tvSeatText3.setText(((DPObject)paramArrayList.get(i)).getString("Name"));
          continue;
          this.tvSeatText4.setVisibility(0);
          this.tvSeatText4.setText(((DPObject)paramArrayList.get(i)).getString("Name"));
        }
      }
    }
  }

  private void setupView()
  {
    int i = 0;
    this.layerMovieTicketOrderConfirm.setVisibility(0);
    this.tvMovieName.setText(this.movieName);
    if (this.dpMovieShow != null)
    {
      String str = this.dpMovieShow.getString("Language") + "/" + this.dpMovieShow.getString("Dimensional");
      this.tvMovieType.setText(str);
      this.tvShowDate.setText(formatDate(this.dpMovieShow.getTime("ShowTime")));
      this.tvShopName.setText(this.shopName + " (" + this.dpMovieShow.getString("HallName") + ")");
    }
    if (this.selectSeatList == null);
    while (true)
    {
      this.tvTicketNum.setText(String.valueOf(i));
      setSeatView(this.selectSeatList);
      showDefaultTotalAmount();
      this.etTicketPhone.setEnabled(true);
      if (this.orderid <= 0)
        break;
      this.etTicketPhone.setText(this.dpMovieTicketOrder.getString("MobileNo"));
      return;
      i = this.selectSeatList.size();
    }
    this.etTicketPhone.setText(MovieUtil.loadPhoneNo(this, accountService()));
  }

  private void showDefaultTotalAmount()
  {
    if (this.selectSeatList == null);
    for (int i = 0; ; i = this.selectSeatList.size())
    {
      this.sumMoney = String.valueOf(Float.valueOf(this.dpMovieShow.getString("Price")).floatValue() * i);
      return;
    }
  }

  private void submitOrder()
  {
    if (this.orderid > 0)
    {
      requestUpdateMovieTicketMobile();
      return;
    }
    requestSubmitTicketOrder();
  }

  private void submitOrderCheckPhone()
  {
    String str = this.etTicketPhone.getText().toString().trim();
    if (TextUtils.isEmpty(str))
    {
      KeyboardUtils.popupKeyboard(this.etTicketPhone);
      this.etTicketPhone.requestFocus();
      this.etTicketPhone.setError(Html.fromHtml("<font color=#ff0000> 请填写取票手机号 </font>"));
      return;
    }
    if (!Pattern.compile("(\\d{11})").matcher(str).matches())
    {
      KeyboardUtils.popupKeyboard(this.etTicketPhone);
      this.etTicketPhone.requestFocus();
      this.etTicketPhone.setError(Html.fromHtml("<font color=#ff0000> 手机号格式错误 </font>"));
      return;
    }
    submitOrder();
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.submit_order)
    {
      statisticsEvent("movie5", "movie5_order_submit", "", 0);
      if (this.isOrderSubmitted)
        goToPayOrder();
    }
    else
    {
      return;
    }
    submitOrderCheckPhone();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent();
    Uri localUri = paramBundle.getData();
    if (!TextUtils.isEmpty(localUri.getQueryParameter("orderid")))
      this.orderid = Integer.valueOf(localUri.getQueryParameter("orderid")).intValue();
    if (!TextUtils.isEmpty(localUri.getQueryParameter("discountid")))
      this.discountid = Integer.valueOf(localUri.getQueryParameter("discountid")).intValue();
    if (this.orderid == 0)
    {
      this.movieName = paramBundle.getStringExtra("moviename");
      this.dpMovieShow = ((DPObject)paramBundle.getParcelableExtra("movieshow"));
      this.selectSeatList = paramBundle.getParcelableArrayListExtra("selectseatlist");
      this.shopName = paramBundle.getStringExtra("shopname");
      this.sumMoney = paramBundle.getStringExtra("money");
      this.discountid = paramBundle.getIntExtra("discountid", 0);
    }
    initView();
    if (this.orderid > 0)
    {
      requestMovieTicketOrderDetail();
      return;
    }
    requestMoviePurchaseTip();
  }

  protected void onDestroy()
  {
    if (this.submitOrderRequest != null)
    {
      mapiService().abort(this.submitOrderRequest, this, true);
      this.submitOrderRequest = null;
    }
    if (this.movieTicketOrderDetailRequest != null)
    {
      mapiService().abort(this.movieTicketOrderDetailRequest, this, true);
      this.movieTicketOrderDetailRequest = null;
    }
    if (this.updateMovieTicketMobileRequest != null)
    {
      mapiService().abort(this.updateMovieTicketMobileRequest, this, true);
      this.updateMovieTicketMobileRequest = null;
    }
    if (this.getMoviePurchaseTipRequest != null)
    {
      mapiService().abort(this.getMoviePurchaseTipRequest, this, true);
      this.getMoviePurchaseTipRequest = null;
    }
    super.onDestroy();
  }

  protected void onLeftTitleButtonClicked()
  {
    if (this.orderid > 0)
    {
      setResult(-1);
      finish();
      return;
    }
    super.onLeftTitleButtonClicked();
  }

  public void onLoginCancel(AccountService paramAccountService)
  {
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
    switch (this.loginStep)
    {
    default:
      return;
    case 1:
      requestSubmitTicketOrder();
      return;
    case 2:
      requestMovieTicketOrderDetail();
      return;
    case 3:
    }
    requestUpdateMovieTicketMobile();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse.message();
    if (paramMApiRequest == this.submitOrderRequest)
    {
      dismissDialog();
      this.submitOrderRequest = null;
    }
    do
    {
      return;
      if (paramMApiRequest == this.movieTicketOrderDetailRequest)
      {
        this.loadingView.setVisibility(8);
        this.movieTicketOrderDetailRequest = null;
        paramMApiRequest = "";
        if ((paramMApiResponse.result() instanceof DPObject))
          paramMApiRequest = paramMApiResponse.message().content();
        paramMApiResponse = paramMApiRequest;
        if (TextUtils.isEmpty(paramMApiRequest))
          paramMApiResponse = "请求失败，请稍后再试";
        this.errorView.setVisibility(0);
        if ((this.errorView instanceof LoadingErrorView))
          ((LoadingErrorView)this.errorView).setCallBack(new LoadingErrorView.LoadRetry()
          {
            public void loadRetry(View paramView)
            {
              MovieTicketOrderConfirmActivity.this.loadingView.setVisibility(0);
              MovieTicketOrderConfirmActivity.this.errorView.setVisibility(8);
              MovieTicketOrderConfirmActivity.this.requestMovieTicketOrderDetail();
            }
          });
        ((TextView)this.errorView.findViewById(16908308)).setText(paramMApiResponse);
        return;
      }
      if (paramMApiRequest != this.updateMovieTicketMobileRequest)
        continue;
      dismissDialog();
      this.updateMovieTicketMobileRequest = null;
      return;
    }
    while (paramMApiRequest != this.getMoviePurchaseTipRequest);
    this.getMoviePurchaseTipRequest = null;
    this.tvSpecialTip.setVisibility(8);
    this.tvNormalTips.setText("1.请在15分钟内完成支付，超时须重新购买。\n2.电影票出票后不可退换，请仔细核对订单信息。\n3.总价中已包含服务费。\n4.出票成功后您将收到验票码短信，电影开映前30分钟请凭验票码至影院取票机打印电影票。");
    this.layerPurchaseTip.setVisibility(0);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest == this.submitOrderRequest)
    {
      dismissDialog();
      this.submitOrderRequest = null;
      if (DPObjectUtils.isDPObjectof(paramMApiResponse, "MovieTicketOrder"))
      {
        this.dpMovieTicketOrder = ((DPObject)paramMApiResponse);
        sendBroadcast(new Intent("com.dianping.movie.REFRESH_SEATING"));
        sendBroadcast(new Intent("com.dianping.movie.CREATE_ORDER"));
        this.etTicketPhone.setEnabled(false);
        this.isOrderSubmitted = true;
        goToPayOrder();
      }
    }
    do
    {
      do
      {
        return;
        if (paramMApiRequest == this.movieTicketOrderDetailRequest)
        {
          this.loadingView.setVisibility(8);
          this.movieTicketOrderDetailRequest = null;
          if (DPObjectUtils.isDPObjectof(paramMApiResponse, "MovieTicketOrder"))
          {
            this.dpMovieTicketOrder = ((DPObject)paramMApiResponse);
            this.movieName = this.dpMovieTicketOrder.getObject("Movie").getString("Name");
            this.dpMovieShow = this.dpMovieTicketOrder.getObject("MovieShow");
            this.selectSeatList.clear();
            this.selectSeatList.addAll(Arrays.asList(this.dpMovieTicketOrder.getArray("Seats")));
            this.shopName = this.dpMovieTicketOrder.getString("ShopName");
            requestMoviePurchaseTip();
          }
          setupView();
          return;
        }
        if (paramMApiRequest != this.updateMovieTicketMobileRequest)
          continue;
        dismissDialog();
        if (DPObjectUtils.isDPObjectof(paramMApiResponse, "SimpleMsg"))
        {
          paramMApiRequest = (DPObject)paramMApiResponse;
          if (!TextUtils.isEmpty(paramMApiRequest.getString("Content")))
          {
            this.dpMovieTicketOrder.edit().putString("ErrorMsg", paramMApiRequest.getString("Content")).generate();
            sendBroadcast(new Intent("com.dianping.movie.REFRESH_MOVIESHOWSCHEDULE"));
          }
        }
        this.updateMovieTicketMobileRequest = null;
        sendBroadcast(new Intent("com.dianping.movie.CREATE_ORDER"));
        this.etTicketPhone.setEnabled(false);
        this.isOrderSubmitted = true;
        goToPayOrder();
        return;
      }
      while (paramMApiRequest != this.getMoviePurchaseTipRequest);
      this.getMoviePurchaseTipRequest = null;
    }
    while (!DPObjectUtils.isDPObjectof(paramMApiResponse, "MoviePurchaseTips"));
    paramMApiRequest = ((DPObject)paramMApiResponse).getString("SpecialTips");
    if (!TextUtils.isEmpty(paramMApiRequest))
    {
      this.tvSpecialTip.setText(paramMApiRequest);
      this.tvSpecialTip.setVisibility(0);
    }
    paramMApiRequest = ((DPObject)paramMApiResponse).getString("NormalTips");
    if (!TextUtils.isEmpty(paramMApiRequest))
      this.tvNormalTips.setText(paramMApiRequest);
    while (true)
    {
      this.layerPurchaseTip.setVisibility(0);
      return;
      this.tvNormalTips.setText("1.请在15分钟内完成支付，超时须重新购买。\n2.电影票出票后不可退换，请仔细核对订单信息。\n3.总价中已包含服务费。\n4.出票成功后您将收到验票码短信，电影开映前30分钟请凭验票码至影院取票机打印电影票。");
    }
  }

  public void requestMovieTicketOrderDetail()
  {
    if (getAccount() == null)
    {
      this.loginStep = 2;
      accountService().login(this);
    }
    do
      return;
    while (this.movieTicketOrderDetailRequest != null);
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/rs/movieticketorderdetailmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("token", accountService().token());
    localBuilder.appendQueryParameter("orderid", String.valueOf(this.orderid));
    this.movieTicketOrderDetailRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.movieTicketOrderDetailRequest, this);
    this.loadingView.setVisibility(0);
  }

  public void requestUpdateMovieTicketMobile()
  {
    if (getAccount() == null)
    {
      this.loginStep = 3;
      accountService().login(this);
    }
    do
      return;
    while (this.updateMovieTicketMobileRequest != null);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("token");
    localArrayList.add(getAccount().token());
    localArrayList.add("mobileno");
    localArrayList.add(this.etTicketPhone.getText().toString());
    localArrayList.add("orderid");
    localArrayList.add(String.valueOf(this.orderid));
    localArrayList.add("discountid");
    localArrayList.add(String.valueOf(this.discountid));
    localArrayList.add("cx");
    localArrayList.add(DeviceUtils.cxInfo("submitmovieorder"));
    this.updateMovieTicketMobileRequest = BasicMApiRequest.mapiPost("http://app.movie.dianping.com/rs/updatemovieticketmobilemv.bin", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.updateMovieTicketMobileRequest, this);
    showProgressDialog("请稍候...");
    MovieUtil.savePhoneNo(this, this.etTicketPhone.getText().toString());
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MovieTicketOrderConfirmActivity
 * JD-Core Version:    0.6.0
 */