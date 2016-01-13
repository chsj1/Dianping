package com.dianping.movie.activity;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.advertisement.AdClientUtils;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.share.enums.ShareType;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.movie.util.MovieUtil;
import com.dianping.movie.view.CinemaDealView;
import com.dianping.movie.view.MovieBannerView;
import com.dianping.movie.view.MovieBannerView.OnBannerClickGA;
import com.dianping.movie.view.MovieTicketCodeView;
import com.dianping.movie.view.PurchasePromoteShopView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class MoviePurchaseTipActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  public static final int AD_GA_CLICK = 2;
  public static final int AD_GA_REVEAL = 3;
  public static final int AD_GA_SHOW = 1;
  private static final int MAX_FAILED_RETRY = 3;
  private static final int MAX_ONGOING_RETRY = 6;
  private final int MSG_WHAT_ONGOING_TICKET = 0;
  private final int MSG_WHAT_TIMER = 1;
  private boolean bannerRevealed = false;
  private MovieBannerView bannerView;
  private Button btnBuyAgain;
  private Button btnContinueToBuy;
  private Button btnGotoOrder;
  private int currentStatus;
  private CinemaDealView dealView;
  private DPObject[] dpCellList;
  private Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        return;
      case 0:
      }
      MoviePurchaseTipActivity.this.requestMoviePayResult();
    }
  };
  private boolean hasLoadMovieSnack;
  private boolean isAnimationStarted = false;
  private boolean isLoading;
  private boolean isSuccess = false;
  private View layerBanner;
  private LinearLayout layerCinemaSnack;
  private ScrollView layerContent;
  private View layerLoading;
  private View layerSaveAsPhoto;
  private View line;
  private MApiRequest luckyMoneyRequest;
  private MApiRequest moviePayReusltRequst;
  private MApiRequest movieSnackRequest;
  private String orderId;
  private MApiRequest promoteAdRequest;
  private PurchasePromoteShopView promoteShopLayout;
  private MApiRequest promoteShopRequest;
  private int retryCount = 0;
  private HashMap<Integer, String> revealMap = new HashMap();
  private int shopId;
  private DPObject[] shops;
  private boolean shopsRevealed = false;
  private ImageView statusIcon;
  private MovieTicketCodeView ticketCodeView;
  DPObject ticketResult;
  private View tipLayer;
  private TextView tvMessageTip;
  private TextView tvPurchaseTip;
  private TextView tvStatusDesc;
  private TextView tvStatusTitle;

  private void gotoMovieMain()
  {
    Intent localIntent1 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://moviemain"));
    localIntent1.addFlags(67108864);
    Intent localIntent2 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://me"));
    localIntent2.addFlags(67108864);
    localIntent1.putExtra("next_redirect_", localIntent2.toUri(1));
    startActivity(localIntent1);
  }

  private void promoteRevealedGA()
  {
    if (this.layerContent == null);
    while (true)
    {
      return;
      Rect localRect = new Rect();
      this.layerContent.getHitRect(localRect);
      Object localObject2;
      Object localObject1;
      if ((this.layerBanner.getVisibility() == 0) && (this.bannerView.getLocalVisibleRect(localRect)) && (this.dpCellList != null))
      {
        localObject2 = this.dpCellList[0];
        if ((localObject2 != null) && (!this.bannerRevealed))
        {
          localObject1 = new HashMap();
          ((Map)localObject1).put("act", String.valueOf(3));
          ((Map)localObject1).put("adidx", String.valueOf(1));
          localObject2 = ((DPObject)localObject2).getString("CellData");
          if (localObject2 == null);
        }
      }
      try
      {
        AdClientUtils.report(new JSONObject((String)localObject2).optString("feedback"), (Map)localObject1);
        this.bannerRevealed = true;
        if ((this.promoteShopLayout.getVisibility() != 0) || (this.shops == null))
          continue;
        i = 3;
        while (i < this.promoteShopLayout.getChildCount())
        {
          localObject1 = this.promoteShopLayout.getChildAt(i);
          if (((View)localObject1).getLocalVisibleRect(localRect))
            break label204;
          i += 1;
        }
      }
      catch (JSONException localDPObject)
      {
        while (true)
        {
          int i;
          localJSONException.printStackTrace();
          continue;
          label204: if ((!(localJSONException instanceof NovaLinearLayout)) || (i - 3 >= this.shops.length))
            continue;
          DPObject localDPObject = this.shops[(i - 3)];
          if ((localDPObject == null) || (this.shopsRevealed))
            continue;
          localObject2 = new HashMap();
          ((Map)localObject2).put("act", String.valueOf(3));
          ((Map)localObject2).put("adidx", String.valueOf(i - 2));
          if (this.revealMap.get(Integer.valueOf(i)) == null)
          {
            AdClientUtils.report(localDPObject.getString("Feedback"), (Map)localObject2);
            this.revealMap.put(Integer.valueOf(i), "revealed");
          }
          if (i - 2 != this.shops.length)
            continue;
          this.shopsRevealed = true;
        }
      }
    }
  }

  private void requestLuckyMoneyData()
  {
    if (this.luckyMoneyRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/rs/orderbonusmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("orderid", this.orderId);
    if ((getAccount() != null) && (!android.text.TextUtils.isEmpty(getAccount().token())))
      localBuilder.appendQueryParameter("token", accountService().token());
    this.luckyMoneyRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.luckyMoneyRequest, this);
  }

  private void requestMoviePayResult()
  {
    if (this.moviePayReusltRequst != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/rs/movieticketpayresultmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("orderid", this.orderId);
    if ((getAccount() != null) && (!android.text.TextUtils.isEmpty(getAccount().token())))
      localBuilder.appendQueryParameter("token", accountService().token());
    this.moviePayReusltRequst = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.moviePayReusltRequst, this);
  }

  private void requestMovieSnack()
  {
    if (this.hasLoadMovieSnack);
    do
      return;
    while ((this.movieSnackRequest != null) || (this.shopId <= 0));
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/moviesnackmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(this.shopId));
    this.movieSnackRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.movieSnackRequest, this);
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
    localBuilder.appendQueryParameter("position", "purchaseresult");
    this.promoteAdRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.promoteAdRequest, this);
  }

  private void sendPromoteShopRequest()
  {
    if (this.promoteShopRequest != null)
      return;
    if (this.shopId <= 0)
    {
      this.promoteShopLayout.setVisibility(8);
      return;
    }
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/promotionshopsmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    localBuilder.appendQueryParameter("shopid", String.valueOf(3538801));
    this.promoteShopRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.promoteShopRequest, this);
  }

  private void setupView()
  {
    int i = this.ticketResult.getInt("TicketStatus");
    this.tvStatusDesc.setText(this.ticketResult.getString("TipForStatus"));
    this.line.setVisibility(0);
    this.tvPurchaseTip.setVisibility(8);
    this.ticketCodeView.setVisibility(8);
    this.layerSaveAsPhoto.setVisibility(8);
    this.currentStatus = i;
    switch (i)
    {
    default:
      findViewById(R.id.normal_status_btns).setVisibility(8);
    case 0:
    case 3:
    case 1:
    case 2:
    }
    do
    {
      do
      {
        return;
        this.statusIcon.setImageResource(R.drawable.movieticket_status_wait);
        this.tvStatusTitle.setText("正在出票中...");
        this.line.setVisibility(8);
        requestMovieSnack();
        showNormalStatusButtons();
        if (this.retryCount >= 6)
          continue;
        this.retryCount += 1;
        this.handler.sendEmptyMessageDelayed(0, 5000L);
      }
      while (this.isAnimationStarted);
      Object localObject = new RotateAnimation(0.0F, 360.0F, 1, 0.5F, 1, 0.5F);
      ((RotateAnimation)localObject).setDuration(1600L);
      ((RotateAnimation)localObject).setRepeatCount(-1);
      ((RotateAnimation)localObject).setInterpolator(new LinearInterpolator());
      this.statusIcon.startAnimation((Animation)localObject);
      this.isAnimationStarted = true;
      return;
      this.isSuccess = true;
      this.statusIcon.setImageResource(R.drawable.movieticket_status_complete);
      this.line.setVisibility(0);
      this.tvStatusTitle.setText("购买成功");
      requestMovieSnack();
      requestLuckyMoneyData();
      sendPromoteAdRequest();
      sendPromoteShopRequest();
      showNormalStatusButtons();
      localObject = this.ticketResult.getArray("TicketCodeList");
      if ((localObject != null) && (localObject.length > 0))
      {
        this.ticketCodeView.setTicketInfo(localObject, this.ticketResult.getString("TicketMachinePosition"), this.ticketResult.getString("QRCode"), this.ticketResult.getString("QRCodeTip"));
        this.ticketCodeView.setVisibility(0);
        this.layerSaveAsPhoto.setVisibility(0);
      }
      while (true)
      {
        sendBroadcast(new Intent("com.dianping.movie.ORDER_STATUS_CHANGED"));
        if (!android.text.TextUtils.isEmpty(this.ticketResult.getString("MessageTip")))
        {
          this.tipLayer.setVisibility(0);
          localObject = com.dianping.util.TextUtils.jsonParseText(this.ticketResult.getString("MessageTip"));
          this.tvMessageTip.setText((CharSequence)localObject);
        }
        statisticsEvent("movie5", "movie5_ticket_success", "", 0);
        if (!this.isAnimationStarted)
          break;
        this.statusIcon.clearAnimation();
        this.isAnimationStarted = false;
        return;
        this.tvPurchaseTip.setText(this.ticketResult.getString("TipForResult"));
        this.tvPurchaseTip.setVisibility(0);
        this.layerSaveAsPhoto.setVisibility(0);
      }
      this.statusIcon.setImageResource(R.drawable.movieticket_status_fail);
      this.tvStatusTitle.setText("抱歉，购买失败");
      findViewById(R.id.normal_status_btns).setVisibility(8);
      findViewById(R.id.layer_failure_tryagain).setVisibility(0);
      this.btnBuyAgain.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          MoviePurchaseTipActivity.this.statisticsEvent("movie5", "movie5_ticketfailure_tryagain", "", 0);
          MoviePurchaseTipActivity.this.startActivity(MoviePurchaseTipActivity.this.ticketResult.getString("LinkForBuyTicket"));
        }
      });
      this.tvPurchaseTip.setText(this.ticketResult.getString("TipForResult"));
      this.tvPurchaseTip.setVisibility(0);
      statisticsEvent("movie5", "movie5_ticket_failed", "", 0);
      sendBroadcast(new Intent("com.dianping.movie.ORDER_STATUS_CHANGED"));
    }
    while (!this.isAnimationStarted);
    this.statusIcon.clearAnimation();
    this.isAnimationStarted = false;
  }

  private void showNormalStatusButtons()
  {
    this.btnContinueToBuy.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (MoviePurchaseTipActivity.this.isSuccess)
          GAHelper.instance().contextStatisticsEvent(MoviePurchaseTipActivity.this, "success_buyanother", null, 0, "tap");
        while (true)
        {
          MoviePurchaseTipActivity.this.startActivity(MoviePurchaseTipActivity.this.ticketResult.getString("LinkForBuyTicket"));
          return;
          GAHelper.instance().contextStatisticsEvent(MoviePurchaseTipActivity.this, "ticketing_buyanother", null, 0, "tap");
        }
      }
    });
    this.btnGotoOrder.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (MoviePurchaseTipActivity.this.isSuccess)
          GAHelper.instance().contextStatisticsEvent(MoviePurchaseTipActivity.this, "success_vieworder", null, 0, "tap");
        while (true)
        {
          MoviePurchaseTipActivity.this.startActivity(MoviePurchaseTipActivity.this.ticketResult.getString("LinkForMyTicketOrders"));
          return;
          GAHelper.instance().contextStatisticsEvent(MoviePurchaseTipActivity.this, "ticketing_vieworder", null, 0, "tap");
        }
      }
    });
    findViewById(R.id.normal_status_btns).setVisibility(0);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setPageId("9040009");
    setContentView(R.layout.movie_purchase_tip_activity);
    paramBundle = getIntent().getBundleExtra("payextra");
    if (paramBundle != null)
      this.orderId = paramBundle.getString("orderid");
    for (this.shopId = paramBundle.getInt("shopid"); ; this.shopId = getIntParam("shopid"))
    {
      if (android.text.TextUtils.isEmpty(this.orderId))
        finish();
      this.statusIcon = ((ImageView)findViewById(R.id.icon_status));
      this.tvStatusTitle = ((TextView)findViewById(R.id.status_title));
      this.tvStatusDesc = ((TextView)findViewById(R.id.status_desc));
      this.tvPurchaseTip = ((TextView)findViewById(R.id.purchase_tip));
      this.line = findViewById(R.id.seperated_line);
      this.btnContinueToBuy = ((Button)findViewById(R.id.success_buyanother));
      this.btnGotoOrder = ((Button)findViewById(R.id.success_vieworder));
      this.btnBuyAgain = ((Button)findViewById(R.id.failure_tryagain));
      this.ticketCodeView = ((MovieTicketCodeView)findViewById(R.id.structured_ticketcode_view));
      this.layerCinemaSnack = ((LinearLayout)findViewById(R.id.cinema_snacks_layer));
      this.tipLayer = findViewById(R.id.layer_tip);
      this.tvMessageTip = ((TextView)findViewById(R.id.message_tip_tv));
      this.layerCinemaSnack.setVisibility(8);
      getTitleBar().setLeftView(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          MoviePurchaseTipActivity.this.gotoMovieMain();
        }
      });
      this.layerContent = ((ScrollView)findViewById(R.id.layer_cotent));
      this.layerContent.setVisibility(8);
      this.layerLoading = findViewById(R.id.layer_loading);
      this.layerLoading.setVisibility(0);
      ((TextView)this.layerLoading.findViewById(R.id.tips)).setText("正在查询订单状态，请稍候...");
      this.isLoading = true;
      this.layerSaveAsPhoto = findViewById(R.id.save_as_photo_layout);
      findViewById(R.id.download).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (MoviePurchaseTipActivity.this.ticketResult == null)
            return;
          paramView = new DPObject().edit().putString("Name", MoviePurchaseTipActivity.this.ticketResult.getString("MovieName")).generate();
          DPObject localDPObject = new DPObject().edit().putTime("ShowTime", MoviePurchaseTipActivity.this.ticketResult.getTime("ShowTime")).putString("HallName", MoviePurchaseTipActivity.this.ticketResult.getString("HallName")).generate();
          paramView = new DPObject().edit().putObject("Movie", paramView).putObject("MovieShow", localDPObject).putStringArray("SeatNameList", MoviePurchaseTipActivity.this.ticketResult.getStringArray("SeatNameList")).putString("ShopName", MoviePurchaseTipActivity.this.ticketResult.getString("ShopName")).putArray("TicketCodeList", MoviePurchaseTipActivity.this.ticketResult.getArray("TicketCodeList")).putString("ExchangeInfo", MoviePurchaseTipActivity.this.ticketResult.getString("TipForResult")).putString("TicketMachinePosition", MoviePurchaseTipActivity.this.ticketResult.getString("TicketMachinePosition")).putString("QRCode", MoviePurchaseTipActivity.this.ticketResult.getString("QRCode")).putString("QRCodeTip", MoviePurchaseTipActivity.this.ticketResult.getString("QRCodeTip")).generate();
          MovieUtil.saveTicketInfoAsPhoto(MoviePurchaseTipActivity.this, paramView);
        }
      });
      requestMoviePayResult();
      sendBroadcast(new Intent("com.dianping.movie.ORDER_STATUS_CHANGED"));
      this.promoteShopLayout = ((PurchasePromoteShopView)findViewById(R.id.promote_shop_layout));
      this.bannerView = ((MovieBannerView)findViewById(R.id.promote_bannerview));
      this.layerBanner = findViewById(R.id.layer_banner);
      this.layerBanner.setVisibility(8);
      this.promoteShopLayout.setVisibility(8);
      this.bannerView.setBtnOnCloseListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          MoviePurchaseTipActivity.this.layerBanner.setVisibility(8);
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
      this.layerContent.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener()
      {
        public void onScrollChanged()
        {
          MoviePurchaseTipActivity.this.promoteRevealedGA();
        }
      });
      return;
      this.orderId = getStringParam("orderid");
    }
  }

  protected void onDestroy()
  {
    if (this.promoteAdRequest != null)
    {
      mapiService().abort(this.promoteAdRequest, this, true);
      this.promoteAdRequest = null;
    }
    if (this.promoteShopRequest != null)
    {
      mapiService().abort(this.promoteShopRequest, this, true);
      this.promoteShopRequest = null;
    }
    if (this.moviePayReusltRequst != null)
    {
      mapiService().abort(this.moviePayReusltRequst, this, true);
      this.moviePayReusltRequst = null;
    }
    if (this.movieSnackRequest != null)
    {
      mapiService().abort(this.movieSnackRequest, this, true);
      this.movieSnackRequest = null;
    }
    if (this.luckyMoneyRequest != null)
    {
      mapiService().abort(this.luckyMoneyRequest, this, true);
      this.luckyMoneyRequest = null;
    }
    this.handler.removeMessages(1);
    this.handler.removeMessages(0);
    this.statusIcon.clearAnimation();
    super.onDestroy();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      gotoMovieMain();
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.moviePayReusltRequst)
    {
      this.moviePayReusltRequst = null;
      if (this.retryCount < 3)
      {
        this.retryCount += 1;
        this.handler.sendEmptyMessageDelayed(0, 1000L);
      }
    }
    do
    {
      do
      {
        do
        {
          return;
          dismissDialog();
        }
        while (isFinishing());
        paramMApiRequest = paramMApiResponse.message();
      }
      while (paramMApiRequest == null);
      new AlertDialog.Builder(this).setTitle(paramMApiRequest.title()).setMessage(paramMApiRequest.content()).setPositiveButton("确定", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          MoviePurchaseTipActivity.this.gotoMovieMain();
        }
      }).show();
      return;
      if (paramMApiRequest == this.movieSnackRequest)
      {
        this.movieSnackRequest = null;
        this.hasLoadMovieSnack = true;
        return;
      }
      if (paramMApiRequest == this.promoteAdRequest)
      {
        this.promoteAdRequest = null;
        return;
      }
      if (paramMApiRequest != this.promoteShopRequest)
        continue;
      this.promoteShopLayout.setVisibility(8);
      this.promoteShopRequest = null;
      return;
    }
    while (paramMApiRequest != this.luckyMoneyRequest);
    this.luckyMoneyRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.moviePayReusltRequst)
    {
      dismissDialog();
      this.isLoading = false;
      this.layerContent.setVisibility(0);
      this.layerLoading.setVisibility(8);
      this.moviePayReusltRequst = null;
      if (isDPObjectof(paramMApiResponse.result(), "TicketStatusResult"))
      {
        this.ticketResult = ((DPObject)paramMApiResponse.result());
        setupView();
      }
    }
    do
    {
      do
      {
        while (true)
        {
          return;
          if (paramMApiRequest != this.movieSnackRequest)
            break;
          this.movieSnackRequest = null;
          this.hasLoadMovieSnack = true;
          if (!isDPObjectof(paramMApiResponse.result(), "MovieDealList"))
            continue;
          paramMApiResponse = (DPObject)paramMApiResponse.result();
          if ((paramMApiResponse.getArray("List") != null) && (paramMApiResponse.getArray("List").length > 0))
          {
            if (this.currentStatus == 1);
            for (paramMApiRequest = "movie5_ticketsuccess_snackdeal"; ; paramMApiRequest = "movie5_ticketing_snackdeal")
            {
              this.dealView = ((CinemaDealView)LayoutInflater.from(this).inflate(R.layout.cinema_deal_view, this.layerCinemaSnack, false));
              this.dealView.setDeals(paramMApiResponse, paramMApiRequest, 0);
              this.layerCinemaSnack.addView(this.dealView);
              this.layerCinemaSnack.setVisibility(0);
              return;
            }
          }
          this.layerCinemaSnack.setVisibility(8);
          return;
        }
        int i;
        if (paramMApiRequest == this.promoteAdRequest)
        {
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
              i = 0;
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
          return;
        }
        if (paramMApiRequest != this.promoteShopRequest)
          continue;
        paramMApiRequest = paramMApiResponse.result();
        if ((DPObjectUtils.isDPObjectof(paramMApiRequest, "PromotionShop")) && (((DPObject)paramMApiRequest).getArray("List") != null) && (((DPObject)paramMApiRequest).getArray("List").length > 0))
        {
          this.shops = ((DPObject)paramMApiRequest).getArray("List");
          this.promoteShopLayout.setShopList(this.shops);
          this.promoteShopLayout.setVisibility(0);
          i = 0;
        }
        while (i < this.shops.length)
        {
          paramMApiRequest = this.shops[i];
          if (paramMApiRequest != null)
          {
            paramMApiResponse = new HashMap();
            paramMApiResponse.put("act", String.valueOf(1));
            paramMApiResponse.put("adidx", String.valueOf(i + 1));
            AdClientUtils.report(paramMApiRequest.getString("Feedback"), paramMApiResponse);
          }
          i += 1;
          continue;
          this.promoteShopLayout.setVisibility(8);
        }
        this.promoteShopRequest = null;
        this.handler.postDelayed(new Runnable()
        {
          public void run()
          {
            MoviePurchaseTipActivity.this.promoteRevealedGA();
          }
        }
        , 800L);
        return;
      }
      while (paramMApiRequest != this.luckyMoneyRequest);
      this.luckyMoneyRequest = null;
    }
    while (!DPObjectUtils.isDPObjectof(paramMApiResponse.result(), "MovieOrderBonus"));
    paramMApiRequest = (DPObject)paramMApiResponse.result();
    paramMApiResponse = (ViewStub)findViewById(R.id.lucky_money_stub);
    if (paramMApiResponse != null)
      paramMApiResponse.inflate();
    paramMApiResponse = findViewById(R.id.layer_lucky_money);
    Object localObject = paramMApiRequest.getString("Title");
    String str = paramMApiRequest.getString("SubTitle");
    if (!android.text.TextUtils.isEmpty((CharSequence)localObject))
      ((TextView)paramMApiResponse.findViewById(R.id.title)).setText((CharSequence)localObject);
    if (!android.text.TextUtils.isEmpty(str))
    {
      localObject = (TextView)paramMApiResponse.findViewById(R.id.subtitle);
      ((TextView)localObject).setText(str);
      ((TextView)localObject).setVisibility(0);
    }
    paramMApiResponse.findViewById(R.id.send_lucky_money).setOnClickListener(new View.OnClickListener(paramMApiRequest)
    {
      public void onClick(View paramView)
      {
        paramView = new DPObject().edit().putString("Title", this.val$luckyMoneyData.getString("ShareTitle")).putString("ShareMsg", this.val$luckyMoneyData.getString("ShareDesc")).putString("ShareImg", this.val$luckyMoneyData.getString("ShareImgUrl")).putString("Url", this.val$luckyMoneyData.getString("ShareUrl")).generate();
        ShareUtil.gotoShareTo(MoviePurchaseTipActivity.this, ShareType.LuckyMoney, paramView, "movie5", "movie_purchase_success_share", 3);
      }
    });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MoviePurchaseTipActivity
 * JD-Core Version:    0.6.0
 */