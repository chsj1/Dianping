package com.dianping.movie.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.advertisement.AdClientUtils;
import com.dianping.archive.DPObject;
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
import com.dianping.movie.view.MovieBannerView;
import com.dianping.movie.view.MovieBannerView.OnBannerClickGA;
import com.dianping.movie.view.MovieTicketCodeView;
import com.dianping.util.DateUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.DPBasicItem;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieTicketDetailActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener, LoginResultListener
{
  private final String BUYDATE_FROMAT = "yyyy-MM-dd E HH:mm";
  private final int MSG_TIMER_TICK = 1;
  private final String SHOWDATE_FROMAT = "MM月dd日(E) HH:mm";
  private boolean bannerRevealed = false;
  private MovieBannerView bannerView;
  private Button btnSendSmsTicketPassword;
  private DPBasicItem cinemaItem;
  private ScrollView contentView;
  private int countDown = 60;
  private DPObject[] dpCellList;
  private DPObject dpMovie;
  private DPObject dpMovieShow;
  private DPObject dpTicketOrder;
  private View errorView;
  private Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
      }
      MovieTicketDetailActivity.access$010(MovieTicketDetailActivity.this);
      if (MovieTicketDetailActivity.this.countDown == 0)
      {
        MovieTicketDetailActivity.this.btnSendSmsTicketPassword.setText("发送取票短信到手机");
        MovieTicketDetailActivity.this.btnSendSmsTicketPassword.setEnabled(true);
        MovieTicketDetailActivity.access$002(MovieTicketDetailActivity.this, 60);
        return;
      }
      MovieTicketDetailActivity.this.btnSendSmsTicketPassword.setText("已发送 " + MovieTicketDetailActivity.this.countDown + " 秒");
      MovieTicketDetailActivity.this.btnSendSmsTicketPassword.setEnabled(false);
      sendEmptyMessageDelayed(1, 1000L);
    }
  };
  private ImageView ivStatusImage;
  private View layerBanner;
  private LinearLayout layerExchangeInfo;
  private View layerSaveAsPhoto;
  private View loadingView;
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ((paramIntent.getAction().equals("com.dianping.movie.ORDER_STATUS_CHANGED")) && (MovieTicketDetailActivity.this.orderId == paramIntent.getIntExtra("orderid", 0)))
        MovieTicketDetailActivity.this.requestMovieTicketOrderDetail();
    }
  };
  private MApiRequest movieTicketOrderDetailRequest;
  private int orderId;
  private MApiRequest promoteAdRequest;
  private DPBasicItem refundBtn;
  private LinearLayout selectedSeatLayout;
  private View separatorForRefundBtn;
  private int shopId;
  private MApiRequest ticketCodeRequest;
  private MovieTicketCodeView ticketCodeView;
  private LinearLayout ticketLayer_BuyTicketStatusFail;
  private LinearLayout ticketLayer_BuyTicketStatusSucc;
  private LinearLayout ticketLayer_BuyTicketStatusTicketIn;
  private TextView tvAddDate;
  private TextView tvExchangeInfo;
  private TextView tvMovieName;
  private TextView tvShopName;
  private TextView tvShowTime;
  private TextView tvTotalAmount;
  private MApiRequest validateRefundableRequest;

  private void promoteRevealedGA()
  {
    if (this.contentView == null);
    do
    {
      do
      {
        return;
        localObject1 = new Rect();
        this.contentView.getHitRect((Rect)localObject1);
      }
      while ((this.layerBanner.getVisibility() != 0) || (!this.bannerView.getLocalVisibleRect((Rect)localObject1)) || (this.dpCellList == null));
      localObject2 = this.dpCellList[0];
    }
    while ((localObject2 == null) || (this.bannerRevealed));
    Object localObject1 = new HashMap();
    ((Map)localObject1).put("act", String.valueOf(3));
    ((Map)localObject1).put("adidx", String.valueOf(1));
    Object localObject2 = ((DPObject)localObject2).getString("CellData");
    if (localObject2 != null);
    try
    {
      AdClientUtils.report(new JSONObject((String)localObject2).optString("feedback"), (Map)localObject1);
      this.bannerRevealed = true;
      return;
    }
    catch (JSONException localJSONException)
    {
      while (true)
        localJSONException.printStackTrace();
    }
  }

  private void requestTicketCode()
  {
    if (this.ticketCodeRequest != null)
      return;
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("orderid");
    localArrayList.add(String.valueOf(this.orderId));
    if ((getAccount() != null) && (!TextUtils.isEmpty(getAccount().token())))
    {
      localArrayList.add("token");
      localArrayList.add(accountService().token());
    }
    this.ticketCodeRequest = BasicMApiRequest.mapiPost("http://app.movie.dianping.com/rs/sendmovieticketcodemv.bin", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.ticketCodeRequest, this);
    showProgressDialog("正在发送，请稍候...");
  }

  private void sendPromoteAdRequest()
  {
    if (this.promoteAdRequest != null);
    do
      return;
    while (this.shopId <= 0);
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/promotionadmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    localBuilder.appendQueryParameter("shopid", String.valueOf(this.shopId));
    localBuilder.appendQueryParameter("position", "ticketdetail");
    this.promoteAdRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.promoteAdRequest, this);
  }

  private void sendSMSTicketPassword()
  {
    new AlertDialog.Builder(this).setTitle("发送验票码").setMessage("确定将验票码发送给" + this.dpTicketOrder.getString("MobileNo").substring(0, 3) + "****" + this.dpTicketOrder.getString("MobileNo").substring(7, 11) + "吗？").setPositiveButton("确定", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        MovieTicketDetailActivity.this.requestTicketCode();
      }
    }).setNegativeButton("取消", null).show();
    statisticsEvent("movie5", "movie5_ticket_code", "", 0);
  }

  private void setSeatView(String[] paramArrayOfString, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramArrayOfString.length > 0)
    {
      this.selectedSeatLayout.removeAllViews();
      if ((paramArrayOfString != null) && (paramArrayOfString.length > 0))
        MovieUtil.showSelectedSeats(this, this.selectedSeatLayout, paramArrayOfString, 0, 0);
    }
  }

  private void setupData()
  {
    this.contentView.setVisibility(0);
    this.ticketCodeView.setVisibility(8);
    this.layerExchangeInfo.setVisibility(8);
    this.dpMovie = this.dpTicketOrder.getObject("Movie");
    this.dpMovieShow = this.dpTicketOrder.getObject("MovieShow");
    this.orderId = this.dpTicketOrder.getInt("ID");
    this.shopId = this.dpTicketOrder.getInt("ShopID");
    this.refundBtn.setVisibility(8);
    this.separatorForRefundBtn.setVisibility(8);
    this.layerSaveAsPhoto.setVisibility(8);
    int n;
    int i;
    int j;
    boolean bool;
    if ((this.dpTicketOrder != null) && (this.dpMovie != null) && (this.dpMovieShow != null))
    {
      int k = this.dpTicketOrder.getInt("TicketStatus");
      int m = this.dpTicketOrder.getInt("OrderStatus");
      n = this.dpTicketOrder.getInt("BuyTicketStatus");
      Object localObject1 = this.dpMovie.getString("Name");
      Object localObject2 = this.dpMovieShow.getString("Language") + "/" + this.dpMovieShow.getString("Dimensional");
      localObject2 = new SpannableString((String)localObject1 + (String)localObject2);
      ((SpannableString)localObject2).setSpan(new RelativeSizeSpan(1.0F), 0, ((String)localObject1).length() - 1, 33);
      ((SpannableString)localObject2).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.deep_black)), 0, ((String)localObject1).length() - 1, 34);
      ((SpannableString)localObject2).setSpan(new RelativeSizeSpan(0.7F), ((String)localObject1).length(), ((SpannableString)localObject2).length(), 33);
      ((SpannableString)localObject2).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_gray)), ((String)localObject1).length(), ((SpannableString)localObject2).length(), 34);
      this.tvMovieName.setText((CharSequence)localObject2);
      localObject1 = this.dpMovieShow.getString("HallName");
      this.tvShopName.setText(this.dpTicketOrder.getString("ShopName") + " (" + (String)localObject1 + ")");
      this.tvShowTime.setText(DateUtils.formatDate2TimeZone(this.dpMovieShow.getTime("ShowTime"), "MM月dd日(E) HH:mm", "GMT+8"));
      setSeatView(this.dpTicketOrder.getStringArray("SeatNameList"), k, m, n);
      this.tvTotalAmount.setText("总价: " + this.dpTicketOrder.getString("TotalAmount"));
      this.tvAddDate.setText("购买时间: " + DateUtils.formatDate2TimeZone(this.dpTicketOrder.getTime("AddDate"), "yyyy-MM-dd E HH:mm", "GMT+8"));
      localObject1 = this.ivStatusImage;
      if (k != 2)
        break label845;
      i = 0;
      ((ImageView)localObject1).setVisibility(i);
      i = 0;
      localObject1 = this.dpTicketOrder.getArray("TicketCodeList");
      j = i;
      if (n == 1)
        if ((localObject1 == null) || (localObject1.length <= 0))
        {
          j = i;
          if (TextUtils.isEmpty(this.dpTicketOrder.getString("ExchangeInfo")));
        }
        else
        {
          j = 1;
          if (k != 2)
            break label851;
          bool = true;
          label622: showExchangeInfo(bool);
          this.layerSaveAsPhoto.setVisibility(0);
        }
      this.ticketLayer_BuyTicketStatusSucc.setVisibility(8);
      this.ticketLayer_BuyTicketStatusTicketIn.setVisibility(8);
      this.ticketLayer_BuyTicketStatusFail.setVisibility(8);
      i = j;
      if (m != 0)
      {
        i = j;
        if (m != 3)
        {
          if (n != 1)
            break label857;
          i = j;
          if (k == 1)
          {
            i = 1;
            this.ticketLayer_BuyTicketStatusSucc.setVisibility(0);
          }
        }
      }
      label705: if (i == 0)
        break label917;
      findViewById(R.id.separator_for_ticketinfo).setVisibility(0);
    }
    while (true)
    {
      if (((this.dpTicketOrder.getBoolean("Refundable")) || (this.dpTicketOrder.getInt("OrderStatus") == 2) || (this.dpTicketOrder.getInt("OrderStatus") == 4) || (this.dpTicketOrder.getInt("OrderStatus") == 3)) && (!TextUtils.isEmpty(this.dpTicketOrder.getString("RefundButtonLink"))))
      {
        this.refundBtn.setVisibility(0);
        this.refundBtn.setTitle(this.dpTicketOrder.getString("RefundButtonText"));
        this.refundBtn.setOnClickListener(this);
        this.separatorForRefundBtn.setVisibility(0);
      }
      this.contentView.setVisibility(0);
      sendPromoteAdRequest();
      return;
      label845: i = 8;
      break;
      label851: bool = false;
      break label622;
      label857: if (n == 2)
      {
        i = 1;
        this.ticketLayer_BuyTicketStatusFail.setVisibility(0);
        break label705;
      }
      if (n == 3)
      {
        i = 1;
        this.ticketLayer_BuyTicketStatusTicketIn.setVisibility(0);
        break label705;
      }
      i = j;
      if (n != 4)
        break label705;
      i = 1;
      this.ticketLayer_BuyTicketStatusTicketIn.setVisibility(0);
      break label705;
      label917: findViewById(R.id.separator_for_ticketinfo).setVisibility(8);
    }
  }

  private void showExchangeInfo(boolean paramBoolean)
  {
    String str = this.dpTicketOrder.getString("ExchangeInfo");
    DPObject[] arrayOfDPObject = this.dpTicketOrder.getArray("TicketCodeList");
    if ((arrayOfDPObject != null) && (arrayOfDPObject.length > 0))
    {
      this.ticketCodeView.setTicketInfo(arrayOfDPObject, this.dpTicketOrder.getString("TicketMachinePosition"), this.dpTicketOrder.getString("QRCode"), this.dpTicketOrder.getString("QRCodeTip"));
      this.ticketCodeView.setVisibility(0);
      this.layerExchangeInfo.setVisibility(8);
      return;
    }
    if (paramBoolean)
      this.tvExchangeInfo.setTextColor(getResources().getColor(R.color.text_color_gray));
    if (TextUtils.isEmpty(str))
    {
      this.tvExchangeInfo.setText("");
      this.tvExchangeInfo.setVisibility(8);
      this.layerExchangeInfo.setVisibility(8);
      return;
    }
    this.tvExchangeInfo.setText(str);
    this.tvExchangeInfo.setVisibility(0);
    this.layerExchangeInfo.setVisibility(0);
  }

  private void validateRefundable()
  {
    if (this.validateRefundableRequest != null)
      return;
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("orderid");
    localArrayList.add(String.valueOf(this.orderId));
    if ((getAccount() != null) && (!TextUtils.isEmpty(getAccount().token())))
    {
      localArrayList.add("token");
      localArrayList.add(accountService().token());
    }
    this.validateRefundableRequest = BasicMApiRequest.mapiPost("http://app.movie.dianping.com/rs/orderrefundablemv.bin", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.validateRefundableRequest, this);
    showProgressDialog("正在校验订单状态，请稍候...");
  }

  protected boolean isNeedLogin()
  {
    return true;
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.viewcinema)
    {
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?shopid=" + this.shopId)));
      statisticsEvent("movie5", "movie5_ticket_viewcinema", "", 0);
    }
    do
    {
      return;
      if (i != R.id.ticket_code)
        continue;
      sendSMSTicketPassword();
      return;
    }
    while ((i != R.id.refund_apply) || (this.dpTicketOrder == null));
    if ((this.dpTicketOrder.getInt("OrderStatus") == 2) || (this.dpTicketOrder.getInt("OrderStatus") == 4) || (this.dpTicketOrder.getInt("OrderStatus") == 3))
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.dpTicketOrder.getString("RefundButtonLink"))));
    while (true)
    {
      statisticsEvent("movie5", "movie5_ticket_refundbutton", "" + this.dpTicketOrder.getInt("OrderStatus"), 0);
      return;
      if (!this.dpTicketOrder.getBoolean("Refundable"))
        continue;
      validateRefundable();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setPageId("9040010");
    setContentView(R.layout.movie_ticket_detail_activity);
    this.handler.removeMessages(1);
    this.dpTicketOrder = ((DPObject)getIntent().getParcelableExtra("movieticketorder"));
    this.orderId = getIntParam("orderid", 0);
    paramBundle = new IntentFilter("com.dianping.movie.ORDER_STATUS_CHANGED");
    registerReceiver(this.mReceiver, paramBundle);
    if ((this.dpTicketOrder == null) && (this.orderId == 0))
    {
      finish();
      return;
    }
    this.tvMovieName = ((TextView)findViewById(R.id.name));
    this.tvShopName = ((TextView)findViewById(R.id.shop_name));
    this.tvShowTime = ((TextView)findViewById(R.id.show_time));
    this.selectedSeatLayout = ((LinearLayout)findViewById(R.id.select_seat_layout));
    this.ivStatusImage = ((ImageView)findViewById(R.id.status_img));
    this.tvTotalAmount = ((TextView)findViewById(R.id.total_amount));
    this.tvAddDate = ((TextView)findViewById(R.id.add_date));
    this.layerExchangeInfo = ((LinearLayout)findViewById(R.id.layer_exchangeinfo));
    this.tvExchangeInfo = ((TextView)findViewById(R.id.exchange_info));
    this.ticketLayer_BuyTicketStatusSucc = ((LinearLayout)findViewById(R.id.ticketlayer_buyticketstatussucc));
    this.ticketLayer_BuyTicketStatusTicketIn = ((LinearLayout)findViewById(R.id.ticketlayer_buyticketstatusticketin));
    this.ticketLayer_BuyTicketStatusFail = ((LinearLayout)findViewById(R.id.ticketlayer_buyticketstatusfail));
    this.btnSendSmsTicketPassword = ((Button)findViewById(R.id.ticket_code));
    this.btnSendSmsTicketPassword.setOnClickListener(this);
    this.cinemaItem = ((DPBasicItem)findViewById(R.id.viewcinema));
    this.cinemaItem.setOnClickListener(this);
    this.refundBtn = ((DPBasicItem)findViewById(R.id.refund_apply));
    this.refundBtn.setVisibility(8);
    this.separatorForRefundBtn = findViewById(R.id.module_separator_for_refundbtn);
    this.separatorForRefundBtn.setVisibility(8);
    this.loadingView = findViewById(R.id.loading);
    this.errorView = findViewById(R.id.error);
    this.contentView = ((ScrollView)findViewById(R.id.content_view));
    this.ticketCodeView = ((MovieTicketCodeView)findViewById(R.id.structured_ticketcode_view));
    this.layerSaveAsPhoto = findViewById(R.id.save_as_photo_layout);
    findViewById(R.id.download).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        MovieUtil.saveTicketInfoAsPhoto(MovieTicketDetailActivity.this, MovieTicketDetailActivity.this.dpTicketOrder);
      }
    });
    this.bannerView = ((MovieBannerView)findViewById(R.id.promote_bannerview));
    this.layerBanner = findViewById(R.id.layer_banner);
    this.layerBanner.setVisibility(8);
    this.bannerView.setBtnOnCloseListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        MovieTicketDetailActivity.this.layerBanner.setVisibility(8);
      }
    });
    this.bannerView.setOnBannerClickGA(new MovieBannerView.OnBannerClickGA()
    {
      public void onBannerClick(DPObject paramDPObject)
      {
        HashMap localHashMap;
        if (paramDPObject != null)
        {
          localHashMap = new HashMap();
          localHashMap.put("act", String.valueOf(2));
          localHashMap.put("adidx", String.valueOf(1));
          paramDPObject = paramDPObject.getString("CellData");
          if (paramDPObject == null);
        }
        try
        {
          AdClientUtils.report(new JSONObject(paramDPObject).optString("feedback"), localHashMap);
          return;
        }
        catch (JSONException paramDPObject)
        {
          paramDPObject.printStackTrace();
        }
      }
    });
    this.bannerView.setCloseDrawable(R.drawable.banner_close);
    this.contentView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener()
    {
      public void onScrollChanged()
      {
        MovieTicketDetailActivity.this.promoteRevealedGA();
      }
    });
    if (this.dpTicketOrder != null)
    {
      setupData();
      return;
    }
    this.loadingView.setVisibility(0);
    this.contentView.setVisibility(8);
    requestMovieTicketOrderDetail();
  }

  protected void onDestroy()
  {
    if (this.promoteAdRequest != null)
    {
      mapiService().abort(this.promoteAdRequest, this, true);
      this.promoteAdRequest = null;
    }
    if (this.ticketCodeRequest != null)
    {
      mapiService().abort(this.ticketCodeRequest, this, true);
      this.ticketCodeRequest = null;
    }
    if (this.movieTicketOrderDetailRequest != null)
    {
      mapiService().abort(this.movieTicketOrderDetailRequest, this, true);
      this.movieTicketOrderDetailRequest = null;
    }
    if (this.validateRefundableRequest != null)
    {
      mapiService().abort(this.validateRefundableRequest, this, true);
      this.validateRefundableRequest = null;
    }
    if (this.mReceiver != null)
      unregisterReceiver(this.mReceiver);
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.ticketCodeRequest)
    {
      dismissDialog();
      super.showToast(paramMApiResponse.message().toString());
      this.ticketCodeRequest = null;
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
              MovieTicketDetailActivity.this.loadingView.setVisibility(0);
              MovieTicketDetailActivity.this.errorView.setVisibility(8);
              MovieTicketDetailActivity.this.requestMovieTicketOrderDetail();
            }
          });
        ((TextView)this.errorView.findViewById(16908308)).setText(paramMApiResponse);
        return;
      }
      if (paramMApiRequest != this.validateRefundableRequest)
        continue;
      dismissDialog();
      this.validateRefundableRequest = null;
      if (paramMApiResponse.message().flag() == 1)
      {
        new AlertDialog.Builder(this).setTitle(paramMApiResponse.message().title()).setMessage(paramMApiResponse.message().content()).setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            paramDialogInterface = new Intent("com.dianping.movie.ORDER_STATUS_CHANGED");
            paramDialogInterface.putExtra("orderid", MovieTicketDetailActivity.this.orderId);
            MovieTicketDetailActivity.this.sendBroadcast(paramDialogInterface);
          }
        }).setCancelable(false).create().show();
        return;
      }
      showAlertDialog(paramMApiResponse.message().title(), paramMApiResponse.message().content());
      return;
    }
    while (paramMApiRequest != this.promoteAdRequest);
    this.promoteAdRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.ticketCodeRequest)
    {
      dismissDialog();
      super.showToast(((DPObject)paramMApiResponse.result()).getString("Content"));
      this.ticketCodeRequest = null;
      this.handler.sendEmptyMessage(1);
    }
    do
    {
      while (true)
      {
        return;
        if (paramMApiRequest != this.movieTicketOrderDetailRequest)
          break;
        this.movieTicketOrderDetailRequest = null;
        this.loadingView.setVisibility(8);
        paramMApiRequest = paramMApiResponse.result();
        if (!DPObjectUtils.isDPObjectof(paramMApiRequest, "MovieTicketOrder"))
          continue;
        this.dpTicketOrder = ((DPObject)paramMApiRequest);
        setupData();
        return;
      }
      if (paramMApiRequest != this.validateRefundableRequest)
        continue;
      dismissDialog();
      this.validateRefundableRequest = null;
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.dpTicketOrder.getString("RefundButtonLink"))));
      return;
    }
    while (paramMApiRequest != this.promoteAdRequest);
    paramMApiRequest = paramMApiResponse.result();
    if ((paramMApiRequest instanceof DPObject))
    {
      this.dpCellList = ((DPObject)paramMApiRequest).getArray("List");
      if ((this.dpCellList != null) && (this.dpCellList.length > 0))
      {
        this.layerBanner.setVisibility(0);
        this.bannerView.setBanner(this.dpCellList);
        this.bannerView.showCloseBtn();
        this.bannerView.setVisibility(0);
        int i = 0;
        while (true)
          if (i < this.dpCellList.length)
          {
            paramMApiResponse = this.dpCellList[i];
            if (paramMApiResponse != null)
            {
              paramMApiRequest = new HashMap();
              paramMApiRequest.put("act", String.valueOf(1));
              paramMApiRequest.put("adidx", String.valueOf(i + 1));
              paramMApiResponse = paramMApiResponse.getString("CellData");
              if (paramMApiResponse == null);
            }
            try
            {
              AdClientUtils.report(new JSONObject(paramMApiResponse).optString("feedback"), paramMApiRequest);
              i += 1;
            }
            catch (JSONException paramMApiRequest)
            {
              while (true)
                paramMApiRequest.printStackTrace();
            }
          }
      }
    }
    this.promoteAdRequest = null;
  }

  public void requestMovieTicketOrderDetail()
  {
    if (this.movieTicketOrderDetailRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/rs/movieticketorderdetailmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("token", accountService().token());
    localBuilder.appendQueryParameter("orderid", String.valueOf(this.orderId));
    this.movieTicketOrderDetailRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.movieTicketOrderDetailRequest, this);
    this.loadingView.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MovieTicketDetailActivity
 * JD-Core Version:    0.6.0
 */