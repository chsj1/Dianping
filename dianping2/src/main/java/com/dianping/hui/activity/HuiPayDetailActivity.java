package com.dianping.hui.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.share.enums.ShareType;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.base.ugc.review.AddReviewUtil;
import com.dianping.base.widget.TitleBar;
import com.dianping.hui.entity.HuiPayDetailDataSource;
import com.dianping.hui.entity.HuiPayDetailDataSource.HuiPayDetailDataLoaderListener;
import com.dianping.hui.entity.HuiPayDetailDataSource.OrderDetail;
import com.dianping.hui.entity.HuiPayDetailDataSource.PayStatus;
import com.dianping.hui.entity.HuiPayDetailDataSource.PayType;
import com.dianping.hui.entity.HuiPayDetailDataSource.ReviewStatus;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.pulltorefresh.PullToRefreshBase;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HuiPayDetailActivity extends NovaActivity
  implements HuiPayDetailDataSource.HuiPayDetailDataLoaderListener, View.OnClickListener
{
  private static final DateFormat FMT_DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  private Button btnAddReview;
  private Button btnBookingFailDialCustomerService;
  private Button btnBookingFailRepay;
  private Button btnCoupon;
  private Button btnDialCustomService;
  private Button btnPayFailDialCustomerService;
  private Button btnPayFailRepay;
  private HuiPayDetailDataSource dataSource;
  private int isHobbit;
  private ImageView ivIcon;
  private ImageView ivLotteryArrow;
  private View layoutBookingFail;
  private View layoutCoupon;
  private FrameLayout layoutError;
  private View layoutFail;
  private View layoutLoading;
  private View layoutLottery;
  private LinearLayout layoutNormal;
  private NetworkImageView nivLotteryIcon;
  private TextView tvAddReviewDone;
  private TextView tvAddReviewTip;
  private TextView tvAddReviewTitle;
  private TextView tvBonus;
  private TextView tvBonusTitle;
  private TextView tvContactMerchantTip;
  private TextView tvCouponDesc;
  private TextView tvCouponExtra;
  private TextView tvCouponTitle;
  private TextView tvCouponValidTime;
  private TextView tvCouponValue;
  private TextView tvHobbit;
  private TextView tvHobbitTitle;
  private TextView tvIdentifyingCode;
  private TextView tvLotteryTitle;
  private TextView tvNoDiscountPayAmount;
  private TextView tvPayDate;
  private TextView tvPayFailPayDate;
  private TextView tvPayFailPayOriAmount;
  private TextView tvPayFailShopName;
  private TextView tvPayFailTitle;
  private TextView tvPayOriAmount;
  private TextView tvPrepay;
  private TextView tvPrepayTitle;
  private TextView tvSavedMoney;
  private TextView tvShopAmount;
  private TextView tvShopName;
  private TextView tvTitle;
  private TextView tvUserMobileNo;
  private TextView tvUserMobileNoTitle;
  private TextView tvUserPay;
  private View vAddReview;
  private PullToRefreshScrollView wholeLayout;

  private SpannableString createSaveAmountDesc(String paramString1, String paramString2)
  {
    if (!TextUtils.isEmpty(paramString1));
    int k;
    for (paramString2 = new SpannableString("已为您节省" + paramString2 + "元" + paramString1); ; paramString2 = new SpannableString("已为您节省" + paramString2 + "元"))
    {
      int i = getResources().getDimensionPixelSize(R.dimen.text_size_18);
      int j = getResources().getColor(R.color.light_gray);
      k = getResources().getColor(R.color.light_red);
      paramString2.setSpan(new AbsoluteSizeSpan(i), 0, 5, 17);
      paramString2.setSpan(new ForegroundColorSpan(j), 0, paramString2.length(), 17);
      if (TextUtils.isEmpty(paramString1))
        break;
      paramString2.setSpan(new ForegroundColorSpan(k), 5, paramString2.length() - paramString1.length() - 1, 17);
      return paramString2;
    }
    paramString2.setSpan(new ForegroundColorSpan(k), 5, paramString2.length() - 1, 17);
    return paramString2;
  }

  private void exit()
  {
    Object localObject;
    switch (12.$SwitchMap$com$dianping$hui$entity$HuiPayDetailDataSource$PayType[this.dataSource.payType.ordinal()])
    {
    default:
      localObject = "dianping://mopaylist";
    case 2:
    case 1:
    case 4:
    case 3:
    }
    while (true)
    {
      localObject = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject));
      ((Intent)localObject).setFlags(67108864);
      startActivity((Intent)localObject);
      finish();
      return;
      super.finish();
      return;
      localObject = "dianping://mopaylist";
      continue;
      if (TextUtils.isEmpty(this.dataSource.bookingSerializedId))
      {
        localObject = String.format("dianping://shopinfo?shopid=%s", new Object[] { Integer.valueOf(this.dataSource.shopId) });
        continue;
      }
      localObject = String.format("dianping://bookinginfo?serializedid=%s", new Object[] { this.dataSource.bookingSerializedId });
      continue;
      localObject = String.format("dianping://shopinfo?shopid=%s", new Object[] { Integer.valueOf(this.dataSource.shopId) });
    }
  }

  private void gotoLotteryPage()
  {
    String str = this.dataSource.bookingActivitys[0].getString("ActionUrl");
    Uri.Builder localBuilder;
    if (!TextUtils.isEmpty(str))
    {
      localBuilder = Uri.parse(str).buildUpon();
      if (!isLogined())
        break label83;
    }
    label83: for (str = this.dataSource.token; ; str = "")
    {
      localBuilder.appendQueryParameter("token", str);
      startActivity("dianping://web?url=" + localBuilder.toString());
      return;
    }
  }

  private void initViews()
  {
    this.wholeLayout = ((PullToRefreshScrollView)super.findViewById(R.id.hui_pay_detail_whole_layout));
    this.layoutNormal = ((LinearLayout)super.findViewById(R.id.normal_layout));
    this.ivIcon = ((ImageView)super.findViewById(R.id.pay_success_icon));
    this.tvTitle = ((TextView)super.findViewById(R.id.title));
    this.tvSavedMoney = ((TextView)super.findViewById(R.id.save_money));
    this.tvShopName = ((TextView)super.findViewById(R.id.tv_pay_shopname));
    this.tvPayOriAmount = ((TextView)super.findViewById(R.id.tv_pay_ori_amount));
    this.tvShopAmount = ((TextView)super.findViewById(R.id.tv_shop_pay));
    this.tvPrepayTitle = ((TextView)super.findViewById(R.id.pre_pay));
    this.tvPrepay = ((TextView)super.findViewById(R.id.tv_pre_pay));
    this.tvUserPay = ((TextView)super.findViewById(R.id.tv_pay_cur_amount));
    this.tvNoDiscountPayAmount = ((TextView)super.findViewById(R.id.tv_no_discount_amount));
    this.tvPayDate = ((TextView)super.findViewById(R.id.tv_pay_date));
    this.tvUserMobileNoTitle = ((TextView)super.findViewById(R.id.user_mobile_no_title));
    this.tvUserMobileNo = ((TextView)super.findViewById(R.id.tv_user_mobile_no));
    this.tvIdentifyingCode = ((TextView)super.findViewById(R.id.identifying_code));
    this.tvBonusTitle = ((TextView)super.findViewById(R.id.bonus_title));
    this.tvBonus = ((TextView)super.findViewById(R.id.bonus));
    this.tvHobbitTitle = ((TextView)super.findViewById(R.id.hobbit_title));
    this.tvHobbit = ((TextView)super.findViewById(R.id.hobbit));
    this.tvContactMerchantTip = ((TextView)super.findViewById(R.id.tv_contact_merchant_tip));
    this.layoutCoupon = super.findViewById(R.id.coupon_layout);
    this.tvCouponValue = ((TextView)this.layoutCoupon.findViewById(R.id.coupon_value));
    this.tvCouponTitle = ((TextView)this.layoutCoupon.findViewById(R.id.coupon_title));
    this.tvCouponDesc = ((TextView)this.layoutCoupon.findViewById(R.id.coupon_desc));
    this.tvCouponValidTime = ((TextView)this.layoutCoupon.findViewById(R.id.coupon_valid_time));
    this.tvCouponExtra = ((TextView)this.layoutCoupon.findViewById(R.id.coupon_extra));
    this.btnCoupon = ((Button)this.layoutCoupon.findViewById(R.id.coupon_button));
    this.layoutLottery = super.findViewById(R.id.lottery_layout);
    this.nivLotteryIcon = ((NetworkImageView)this.layoutLottery.findViewById(R.id.lottery_icon));
    this.ivLotteryArrow = ((ImageView)this.layoutLottery.findViewById(R.id.arrow));
    this.tvLotteryTitle = ((TextView)this.layoutLottery.findViewById(R.id.lottery_title));
    this.vAddReview = this.layoutNormal.findViewById(R.id.addreview_layout);
    this.tvAddReviewTitle = ((TextView)this.vAddReview.findViewById(R.id.tv_addreview_title));
    this.tvAddReviewTip = ((TextView)this.vAddReview.findViewById(R.id.tv_addreview_tip));
    this.tvAddReviewDone = ((TextView)this.vAddReview.findViewById(R.id.tv_addreview_done));
    this.btnAddReview = ((Button)this.vAddReview.findViewById(R.id.btn_addreview));
    this.btnDialCustomService = ((Button)super.findViewById(R.id.dial_custom_service));
    this.layoutFail = super.findViewById(R.id.layout_fail);
    this.tvPayFailTitle = ((TextView)this.layoutFail.findViewById(R.id.layout_fail_title));
    this.tvPayFailShopName = ((TextView)this.layoutFail.findViewById(R.id.layout_fail_tv_shopname));
    this.tvPayFailPayOriAmount = ((TextView)this.layoutFail.findViewById(R.id.layout_fail_tv_pay_ori_amount));
    this.tvPayFailPayDate = ((TextView)this.layoutFail.findViewById(R.id.layout_fail_date));
    this.btnPayFailRepay = ((Button)this.layoutFail.findViewById(R.id.layout_fail_repay));
    this.btnPayFailRepay.setOnClickListener(this);
    this.btnPayFailDialCustomerService = ((Button)this.layoutFail.findViewById(R.id.layout_fail_dial_customer_service));
    this.btnPayFailDialCustomerService.setOnClickListener(this);
    this.layoutBookingFail = super.findViewById(R.id.layout_booking_pay_fail);
    this.btnBookingFailRepay = ((Button)this.layoutBookingFail.findViewById(R.id.booking_pay_fail_repay));
    this.btnBookingFailRepay.setOnClickListener(this);
    this.btnBookingFailDialCustomerService = ((Button)this.layoutBookingFail.findViewById(R.id.booking_pay_fail_dial_customer_service));
    this.btnBookingFailDialCustomerService.setOnClickListener(this);
    this.layoutLoading = super.findViewById(R.id.loading_layout);
    this.layoutError = ((FrameLayout)super.findViewById(R.id.error_layout));
    manageLayouts(true, false, false, false, false);
  }

  private void loadPayFailOrder()
  {
    String str;
    if ((this.dataSource.payType == HuiPayDetailDataSource.PayType.HUI_PAY_RESULT) || (this.dataSource.payType == HuiPayDetailDataSource.PayType.HUI_ORDER_DETAIL) || (this.dataSource.payType == HuiPayDetailDataSource.PayType.HUI_ORDER_DETAIL_FROM_USER_ORDER_LIST))
    {
      TextView localTextView = this.tvPayFailTitle;
      if (TextUtils.isEmpty(this.dataSource.statusMsg))
      {
        str = "支付失败";
        localTextView.setText(str);
        this.tvPayFailShopName.setText(this.dataSource.orderDetail.shopName);
        this.tvPayFailPayOriAmount.setText(this.dataSource.orderDetail.totalCostAmount.stripTrailingZeros().toPlainString() + "元");
        this.tvPayFailPayDate.setText(FMT_DATE.format(new Date(this.dataSource.orderDetail.timeInMillis)));
        manageLayouts(false, false, true, false, false);
      }
    }
    while (true)
    {
      statisticsEvent("pay5", "pay5_order", "支付失败", 0);
      return;
      str = this.dataSource.statusMsg;
      break;
      manageLayouts(false, false, false, true, false);
    }
  }

  private void loadPaySuccessOrder()
  {
    if (this.dataSource.orderShareInfo != null);
    HuiPayDetailDataSource.OrderDetail localOrderDetail;
    int i;
    label128: TextView localTextView;
    Object localObject;
    switch (this.dataSource.bizType)
    {
    default:
      super.getTitleBar().addRightViewItem("share", R.drawable.ic_action_share, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = new ShareHolder();
          paramView.title = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("Title");
          paramView.imageUrl = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("IconUrl");
          paramView.webUrl = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("Url");
          paramView.desc = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("Desc");
          ShareUtil.gotoShareTo(HuiPayDetailActivity.this, ShareType.Pay, paramView, "", "");
        }
      });
      localOrderDetail = this.dataSource.orderDetail;
      this.tvTitle.setText(this.dataSource.statusMsg);
      if ((this.dataSource.ticketInfo == null) || (TextUtils.isEmpty(this.dataSource.ticketInfo.getString("PayTicketDesc"))))
        break;
      i = 1;
      if (localOrderDetail.savedAmount.doubleValue() > 0.0D)
      {
        localTextView = this.tvSavedMoney;
        if (i != 0)
        {
          localObject = this.dataSource.ticketInfo.getString("PayTicketDesc");
          label164: localTextView.setText(createSaveAmountDesc((String)localObject, localOrderDetail.savedAmount.stripTrailingZeros().toPlainString()));
          this.tvSavedMoney.setVisibility(0);
          label191: this.tvShopName.setText(localOrderDetail.shopName);
          this.tvPayOriAmount.setText(localOrderDetail.totalCostAmount.stripTrailingZeros().toPlainString() + "元");
          this.tvShopAmount.setText(localOrderDetail.shopAmount.stripTrailingZeros().toPlainString() + "元");
          if (localOrderDetail.userPrepayAmount.doubleValue() <= 0.0D)
            break label947;
          this.tvPrepay.setText(localOrderDetail.userPrepayAmount.stripTrailingZeros().toPlainString() + "元");
          this.tvPrepayTitle.setVisibility(0);
          this.tvPrepay.setVisibility(0);
          label335: this.tvUserPay.setText(localOrderDetail.userPayAmount.stripTrailingZeros().toPlainString() + "元");
          if ((localOrderDetail.noDiscountAmount != null) && (localOrderDetail.noDiscountAmount.doubleValue() > 0.0D))
            break label968;
          this.tvNoDiscountPayAmount.setVisibility(8);
          label398: this.tvPayDate.setText(FMT_DATE.format(new Date(localOrderDetail.timeInMillis)));
          if (!TextUtils.isEmpty(localOrderDetail.userMobileNo))
            break label1021;
          this.tvUserMobileNoTitle.setVisibility(8);
          this.tvUserMobileNo.setVisibility(8);
          label450: if (TextUtils.isEmpty(localOrderDetail.identifyingCode))
            break label1055;
          this.tvIdentifyingCode.setText(splitIdentifyingCode(localOrderDetail.identifyingCode));
          if (!TextUtils.isEmpty(localOrderDetail.bonus))
            break label1121;
          this.tvBonusTitle.setVisibility(8);
          this.tvBonus.setVisibility(8);
          label503: localObject = this.dataSource.ticketInfo;
          if ((localObject == null) || (((DPObject)localObject).getInt("TicketShareStatus") == 0))
            break label1151;
          this.tvCouponValue.setText(((DPObject)localObject).getString("TicketValue"));
          ViewUtils.setVisibilityAndContent(this.tvCouponTitle, ((DPObject)localObject).getString("TicketTitle"));
          ViewUtils.setVisibilityAndContent(this.tvCouponDesc, ((DPObject)localObject).getString("TicketDesc"));
          ViewUtils.setVisibilityAndContent(this.tvCouponValidTime, ((DPObject)localObject).getString("TicketValidPeriod"));
          this.tvCouponExtra.setText(((DPObject)localObject).getString("ExtraTickets"));
          this.btnCoupon.setOnClickListener(this);
          this.btnCoupon.setText(((DPObject)localObject).getString("TicketButtonText"));
          this.btnCoupon.setEnabled(((DPObject)localObject).getBoolean("TicketButtonClickable"));
          label631: if ((this.dataSource.bookingActivitys == null) || (this.dataSource.bookingActivitys.length <= 0))
            break label1163;
          localObject = this.dataSource.bookingActivitys[0];
          this.nivLotteryIcon.setImage(((DPObject)localObject).getString("IconUrl"));
          this.tvLotteryTitle.setText(((DPObject)localObject).getString("Title"));
          this.layoutLottery.setOnClickListener(this);
          this.layoutLottery.setVisibility(0);
          label706: if (this.dataSource.reviewStatus != HuiPayDetailDataSource.ReviewStatus.OPEN)
            break label1175;
          ViewUtils.setVisibilityAndContent(this.tvAddReviewTitle, this.dataSource.reviewInfo.getString("Title"));
          ViewUtils.setVisibilityAndContent(this.tvAddReviewTip, this.dataSource.reviewInfo.getString("Tip"));
          this.tvAddReviewDone.setVisibility(8);
          this.btnAddReview.setOnClickListener(this);
          this.btnAddReview.setVisibility(0);
          this.vAddReview.setVisibility(0);
        }
      }
    case 10:
    case 30:
    case 20:
    }
    while (true)
    {
      ViewUtils.setVisibilityAndContent(this.tvContactMerchantTip, this.dataSource.contactMerchantTip);
      this.btnDialCustomService.setOnClickListener(this);
      manageLayouts(false, false, false, false, true);
      statisticsEvent("pay5", "pay5_order", "支付成功", 0);
      return;
      super.getTitleBar().addRightViewItem("share", R.drawable.ic_action_share, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = new ShareHolder();
          paramView.title = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("Title");
          paramView.imageUrl = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("IconUrl");
          paramView.webUrl = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("Url");
          paramView.desc = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("Desc");
          ShareUtil.gotoShareTo(HuiPayDetailActivity.this, ShareType.Pay, paramView, "", "");
        }
      });
      break;
      super.getTitleBar().addRightViewItem("share", R.drawable.ic_action_share, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = new ShareHolder();
          paramView.title = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("ShareTitle");
          paramView.imageUrl = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("IconUrl");
          paramView.webUrl = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("ShareLink");
          paramView.desc = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("ShareContent");
          ShareUtil.gotoShareTo(HuiPayDetailActivity.this, ShareType.Pay, paramView, "", "");
        }
      });
      break;
      i = 0;
      break label128;
      localObject = null;
      break label164;
      if (i != 0)
      {
        this.tvSavedMoney.setText(this.dataSource.ticketInfo.getString("PayTicketDesc"));
        this.tvSavedMoney.setVisibility(0);
        break label191;
      }
      this.tvSavedMoney.setVisibility(8);
      break label191;
      label947: this.tvPrepayTitle.setVisibility(8);
      this.tvPrepay.setVisibility(8);
      break label335;
      label968: this.tvNoDiscountPayAmount.setText("(含不参与优惠" + localOrderDetail.noDiscountAmount.stripTrailingZeros().toPlainString() + "元)");
      this.tvNoDiscountPayAmount.setVisibility(0);
      break label398;
      label1021: this.tvUserMobileNoTitle.setVisibility(0);
      this.tvUserMobileNo.setText(splitIdentifyingCode(localOrderDetail.userMobileNo));
      this.tvUserMobileNo.setVisibility(0);
      break label450;
      label1055: localTextView = this.tvIdentifyingCode;
      if (this.dataSource.payStatus == HuiPayDetailDataSource.PayStatus.PENDING);
      for (localObject = "正在为您生成订单号...(" + this.dataSource.huiPayResultCountDownSeconds + "秒)"; ; localObject = "订单号获取失败")
      {
        localTextView.setText((CharSequence)localObject);
        break;
      }
      label1121: this.tvBonus.setText(localOrderDetail.bonus);
      this.tvBonus.setVisibility(0);
      this.tvBonusTitle.setVisibility(0);
      break label503;
      label1151: this.layoutCoupon.setVisibility(8);
      break label631;
      label1163: this.layoutLottery.setVisibility(8);
      break label706;
      label1175: if (this.dataSource.reviewStatus == HuiPayDetailDataSource.ReviewStatus.CLOSED)
      {
        ViewUtils.setVisibilityAndContent(this.tvAddReviewTitle, this.dataSource.reviewInfo.getString("Title"));
        this.tvAddReviewTip.setVisibility(8);
        this.tvAddReviewDone.setVisibility(0);
        this.btnAddReview.setVisibility(8);
        this.vAddReview.setVisibility(0);
        continue;
      }
      this.vAddReview.setVisibility(8);
    }
  }

  private void loadRefundOrder()
  {
    if (this.dataSource.orderShareInfo != null);
    HuiPayDetailDataSource.OrderDetail localOrderDetail;
    label136: label280: label367: Object localObject;
    switch (this.dataSource.bizType)
    {
    default:
      super.getTitleBar().addRightViewItem("share", R.drawable.ic_action_share, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = new ShareHolder();
          paramView.title = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("Title");
          paramView.imageUrl = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("IconUrl");
          paramView.webUrl = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("Url");
          paramView.desc = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("Desc");
          ShareUtil.gotoShareTo(HuiPayDetailActivity.this, ShareType.Pay, paramView, "", "");
        }
      });
      this.ivIcon.setVisibility(8);
      localOrderDetail = this.dataSource.orderDetail;
      this.tvTitle.setText(this.dataSource.statusMsg);
      if (this.dataSource.payStatus != HuiPayDetailDataSource.PayStatus.IN_REFUND)
        break;
      this.tvSavedMoney.setText("您的付款将于1~5个工作日内原路退还");
      this.tvSavedMoney.setVisibility(0);
      this.tvShopName.setText(localOrderDetail.shopName);
      this.tvPayOriAmount.setText(localOrderDetail.totalCostAmount.stripTrailingZeros().toPlainString() + "元");
      this.tvShopAmount.setText(localOrderDetail.shopAmount.stripTrailingZeros().toPlainString() + "元");
      if (localOrderDetail.userPrepayAmount.doubleValue() > 0.0D)
      {
        this.tvPrepay.setText(localOrderDetail.userPrepayAmount.stripTrailingZeros().toPlainString() + "元");
        this.tvPrepayTitle.setVisibility(0);
        this.tvPrepay.setVisibility(0);
        this.tvUserPay.setText(localOrderDetail.userPayAmount.stripTrailingZeros().toPlainString() + "元");
        this.tvPayDate.setText(FMT_DATE.format(new Date(localOrderDetail.timeInMillis)));
        if (!TextUtils.isEmpty(localOrderDetail.userMobileNo))
          break label758;
        this.tvUserMobileNoTitle.setVisibility(8);
        this.tvUserMobileNo.setVisibility(8);
        if (TextUtils.isEmpty(localOrderDetail.identifyingCode))
          break label792;
        this.tvIdentifyingCode.setText(splitIdentifyingCode(localOrderDetail.identifyingCode));
        if (!TextUtils.isEmpty(localOrderDetail.bonus))
          break label858;
        this.tvBonusTitle.setVisibility(8);
        this.tvBonus.setVisibility(8);
        label420: localObject = this.dataSource.ticketInfo;
        if ((localObject == null) || (((DPObject)localObject).getInt("TicketShareStatus") == 0))
          break label888;
        this.tvCouponValue.setText(((DPObject)localObject).getString("TicketValue"));
        ViewUtils.setVisibilityAndContent(this.tvCouponTitle, ((DPObject)localObject).getString("TicketTitle"));
        ViewUtils.setVisibilityAndContent(this.tvCouponDesc, ((DPObject)localObject).getString("TicketDesc"));
        ViewUtils.setVisibilityAndContent(this.tvCouponValidTime, ((DPObject)localObject).getString("TicketValidPeriod"));
        this.tvCouponExtra.setText(((DPObject)localObject).getString("ExtraTickets"));
        this.btnCoupon.setOnClickListener(this);
        this.btnCoupon.setText(((DPObject)localObject).getString("TicketButtonText"));
        this.btnCoupon.setEnabled(((DPObject)localObject).getBoolean("TicketButtonClickable"));
        label548: if ((this.dataSource.bookingActivitys == null) || (this.dataSource.bookingActivitys.length <= 0))
          break label900;
        localObject = this.dataSource.bookingActivitys[0];
        this.nivLotteryIcon.setImage(((DPObject)localObject).getString("IconUrl"));
        this.tvLotteryTitle.setText(((DPObject)localObject).getString("Title"));
        this.layoutLottery.setOnClickListener(this);
        this.layoutLottery.setVisibility(0);
      }
    case 10:
    case 30:
    case 20:
    }
    while (true)
    {
      this.btnDialCustomService.setOnClickListener(this);
      manageLayouts(false, false, false, false, true);
      return;
      super.getTitleBar().addRightViewItem("share", R.drawable.ic_action_share, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = new ShareHolder();
          paramView.title = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("Title");
          paramView.imageUrl = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("IconUrl");
          paramView.webUrl = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("Url");
          paramView.desc = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("Desc");
          ShareUtil.gotoShareTo(HuiPayDetailActivity.this, ShareType.Pay, paramView, "", "");
        }
      });
      break;
      super.getTitleBar().addRightViewItem("share", R.drawable.ic_action_share, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = new ShareHolder();
          paramView.title = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("ShareTitle");
          paramView.imageUrl = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("IconUrl");
          paramView.webUrl = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("ShareLink");
          paramView.desc = HuiPayDetailActivity.this.dataSource.orderShareInfo.getString("ShareContent");
          ShareUtil.gotoShareTo(HuiPayDetailActivity.this, ShareType.Pay, paramView, "", "");
        }
      });
      break;
      if (this.dataSource.payStatus == HuiPayDetailDataSource.PayStatus.DONE_REFUND)
      {
        this.tvSavedMoney.setText("您的付款将于1~5个工作日内原路退还");
        this.tvSavedMoney.setVisibility(0);
        break label136;
      }
      this.tvSavedMoney.setVisibility(8);
      break label136;
      this.tvPrepayTitle.setVisibility(8);
      this.tvPrepay.setVisibility(8);
      break label280;
      label758: this.tvUserMobileNoTitle.setVisibility(0);
      this.tvUserMobileNo.setText(splitIdentifyingCode(localOrderDetail.userMobileNo));
      this.tvUserMobileNo.setVisibility(0);
      break label367;
      label792: TextView localTextView = this.tvIdentifyingCode;
      if (this.dataSource.payStatus == HuiPayDetailDataSource.PayStatus.PENDING);
      for (localObject = "正在为您生成订单号...(" + this.dataSource.huiPayResultCountDownSeconds + "秒)"; ; localObject = "订单号获取失败")
      {
        localTextView.setText((CharSequence)localObject);
        break;
      }
      label858: this.tvBonus.setText(localOrderDetail.bonus);
      this.tvBonus.setVisibility(0);
      this.tvBonusTitle.setVisibility(0);
      break label420;
      label888: this.layoutCoupon.setVisibility(8);
      break label548;
      label900: this.layoutLottery.setVisibility(8);
    }
  }

  private void manageLayouts(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5)
  {
    int j = 0;
    Object localObject = this.layoutLoading;
    if (paramBoolean1)
    {
      i = 0;
      ((View)localObject).setVisibility(i);
      localObject = this.layoutError;
      if (!paramBoolean2)
        break label114;
      i = 0;
      label36: ((FrameLayout)localObject).setVisibility(i);
      localObject = this.layoutFail;
      if (!paramBoolean3)
        break label121;
      i = 0;
      label56: ((View)localObject).setVisibility(i);
      localObject = this.layoutBookingFail;
      if (!paramBoolean4)
        break label128;
      i = 0;
      label77: ((View)localObject).setVisibility(i);
      localObject = this.layoutNormal;
      if (!paramBoolean5)
        break label135;
    }
    label128: label135: for (int i = j; ; i = 8)
    {
      ((LinearLayout)localObject).setVisibility(i);
      return;
      i = 8;
      break;
      label114: i = 8;
      break label36;
      label121: i = 8;
      break label56;
      i = 8;
      break label77;
    }
  }

  private String splitIdentifyingCode(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return paramString;
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    while (i < paramString.length())
    {
      if ((i > 0) && (i < paramString.length() - 1) && (i % 4 == 0))
        localStringBuilder.append(" ");
      localStringBuilder.append(paramString.charAt(i));
      i += 1;
    }
    return localStringBuilder.toString();
  }

  public void countDown(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.tvIdentifyingCode.setText("正在为您生成订单号...(" + paramInt + "秒)");
      return;
    }
    this.tvIdentifyingCode.setText("订单号获取失败");
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void loadBookingPayResultFail(Object paramObject)
  {
    this.layoutError.removeAllViews();
    this.layoutError.addView(getFailedView("网络连接失败 点击重新加载", new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        HuiPayDetailActivity.this.dataSource.reqBookingPayResult();
      }
    }));
    manageLayouts(false, true, false, false, false);
  }

  public void loadHobbit()
  {
    if (TextUtils.isEmpty(this.dataSource.hobbitMenu))
    {
      this.tvHobbitTitle.setVisibility(8);
      this.tvHobbit.setVisibility(8);
      return;
    }
    this.tvHobbitTitle.setVisibility(0);
    this.tvHobbit.setText(this.dataSource.hobbitMenu);
    this.tvHobbit.setVisibility(0);
  }

  public void loadHobbitResultFail(Object paramObject)
  {
  }

  public void loadHuiPayResultFail(Object paramObject)
  {
    this.layoutError.removeAllViews();
    this.layoutError.addView(getFailedView("网络连接失败 点击重新加载", new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        HuiPayDetailActivity.this.dataSource.reqHuiPayResult();
      }
    }));
    manageLayouts(false, true, false, false, false);
  }

  public void loadOneOrder()
  {
    if ((this.dataSource.payType == HuiPayDetailDataSource.PayType.HUI_ORDER_DETAIL) || (this.dataSource.payType == HuiPayDetailDataSource.PayType.HUI_ORDER_DETAIL_FROM_USER_ORDER_LIST))
      this.wholeLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener()
      {
        public void onRefresh(PullToRefreshBase<ScrollView> paramPullToRefreshBase)
        {
          HuiPayDetailActivity.this.dataSource.reqGetOneOrder();
          if (HuiPayDetailActivity.this.isHobbit == 1)
            HuiPayDetailActivity.this.dataSource.reqHobbitResult();
          HuiPayDetailActivity.this.wholeLayout.onRefreshComplete();
        }
      });
    switch (12.$SwitchMap$com$dianping$hui$entity$HuiPayDetailDataSource$PayStatus[this.dataSource.payStatus.ordinal()])
    {
    default:
      loadPaySuccessOrder();
      return;
    case 1:
      if ((this.dataSource.payType == HuiPayDetailDataSource.PayType.HUI_ORDER_DETAIL) || (this.dataSource.payType == HuiPayDetailDataSource.PayType.HUI_ORDER_DETAIL_FROM_USER_ORDER_LIST) || (this.dataSource.payType == HuiPayDetailDataSource.PayType.BOOKING_PAY_RESULT))
      {
        loadPayFailOrder();
        return;
      }
      loadPaySuccessOrder();
      return;
    case 2:
      loadPayFailOrder();
      return;
    case 3:
      loadPaySuccessOrder();
      return;
    case 4:
    case 5:
    }
    loadRefundOrder();
  }

  public void loadOneOrderFail(Object paramObject)
  {
    this.layoutError.removeAllViews();
    this.layoutError.addView(getFailedView("网络连接失败 点击重新加载", new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        HuiPayDetailActivity.this.dataSource.reqGetOneOrder();
        if (HuiPayDetailActivity.this.isHobbit == 1)
          HuiPayDetailActivity.this.dataSource.reqHobbitResult();
      }
    }));
    manageLayouts(false, true, false, false, false);
  }

  public void onClick(View paramView)
  {
    if (paramView == this.btnCoupon)
      if (this.dataSource.ticketInfo.getInt("TicketShareStatus") == 1)
      {
        accountService().login(this);
        if ((this.dataSource.payType == HuiPayDetailDataSource.PayType.HUI_ORDER_DETAIL) || (this.dataSource.payType == HuiPayDetailDataSource.PayType.HUI_ORDER_DETAIL_FROM_USER_ORDER_LIST))
          statisticsEvent("hui7", "hui7_quan_login", "查看", 0);
      }
    do
    {
      do
      {
        return;
        statisticsEvent("hui7", "hui7_quan_login", "买单", 0);
        return;
      }
      while (this.dataSource.ticketInfo.getInt("TicketShareStatus") != 2);
      paramView = new ShareHolder();
      DPObject localDPObject = this.dataSource.ticketInfo.getObject("ShareDo");
      paramView.title = localDPObject.getString("Title");
      paramView.imageUrl = localDPObject.getString("IconUrl");
      paramView.webUrl = localDPObject.getString("Url");
      paramView.desc = localDPObject.getString("Desc");
      ShareUtil.gotoShareTo(this, ShareType.Pay, paramView, "", "", 3);
      if ((this.dataSource.payType == HuiPayDetailDataSource.PayType.HUI_ORDER_DETAIL) || (this.dataSource.payType == HuiPayDetailDataSource.PayType.HUI_ORDER_DETAIL_FROM_USER_ORDER_LIST))
      {
        statisticsEvent("hui7", "hui7_quan_share", "查看", 0);
        return;
      }
      statisticsEvent("hui7", "hui7_quan_share", "买单", 0);
      return;
      if ((paramView == this.btnDialCustomService) || (paramView == this.btnPayFailDialCustomerService) || (paramView == this.btnBookingFailDialCustomerService))
        try
        {
          startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:400-820-5527")));
          statisticsEvent("pay5", "pay5_complain", "", 0);
          return;
        }
        catch (java.lang.Exception paramView)
        {
          Log.e("HuiPayDetailActivity", "can not dial", paramView);
          return;
        }
      if (paramView == this.layoutLottery)
      {
        if (TextUtils.isEmpty(this.dataSource.token))
          accountService().login(new LoginResultListener()
          {
            public void onLoginCancel(AccountService paramAccountService)
            {
            }

            public void onLoginSuccess(AccountService paramAccountService)
            {
              HuiPayDetailActivity.this.dataSource.token = HuiPayDetailActivity.this.accountService().token();
              HuiPayDetailActivity.this.gotoLotteryPage();
            }
          });
        while (true)
        {
          statisticsEvent("booking6", "booking6_fanpiao_lottery", "", 0);
          return;
          gotoLotteryPage();
        }
      }
      if (paramView != this.btnAddReview)
        continue;
      AddReviewUtil.addReview(this, this.dataSource.shopId, this.dataSource.orderDetail.shopName);
      this.dataSource.reviewStatus = HuiPayDetailDataSource.ReviewStatus.OPEN_IN_EDIT;
      statisticsEvent("hui7", "hui7_comment_apply", "", 0);
      return;
    }
    while ((paramView != this.btnPayFailRepay) && (paramView != this.btnBookingFailRepay));
    paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://wxalipay"));
    paramView.putExtra("shopid", this.dataSource.shopId);
    paramView.putExtra("shopname", this.dataSource.orderDetail.shopName);
    if (this.dataSource.thirdPartyOrderType == 30)
    {
      paramView.putExtra("amount", this.dataSource.orderDetail.totalCostAmount.stripTrailingZeros().toPlainString());
      paramView.putExtra("orderid", this.dataSource.thirdPartyOrderId);
      paramView.putExtra("channel", 30);
    }
    while (true)
    {
      paramView.setFlags(67108864);
      startActivity(paramView);
      return;
      if (this.dataSource.orderBizType != 20)
        continue;
      paramView.putExtra("orderid", this.dataSource.orderId);
      paramView.putExtra("channel", 20);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setTitle("买单详情");
    super.setContentView(R.layout.hui_pay_detail_activity);
    super.getWindow().setBackgroundDrawable(null);
    initViews();
    this.dataSource = new HuiPayDetailDataSource(this);
    this.dataSource.dataLoadListener = this;
    this.dataSource.orderId = getStringParam("orderid");
    this.dataSource.orderBizType = getIntParam("orderbiztype");
    this.dataSource.thirdPartyOrderId = getStringParam("thirdpartyorderid");
    this.dataSource.thirdPartyOrderType = getIntParam("thirdpartyordertype");
    this.dataSource.huiOrderId = getStringParam("huiorderid");
    this.dataSource.shopId = getIntParam("shopid");
    this.dataSource.bizType = getIntParam("biztype");
    this.isHobbit = getIntParam("ishobbit");
    this.dataSource.bookingSerializedId = getStringParam("serializedid");
    int i = getIntParam("source");
    if (i / 10 == 1)
    {
      this.wholeLayout.setMode(PullToRefreshBase.Mode.DISABLED);
      if (this.dataSource.bizType == 20)
      {
        this.dataSource.payType = HuiPayDetailDataSource.PayType.BOOKING_PAY_RESULT;
        this.dataSource.payOrderId = Integer.toString(getIntParam("payorderid"));
      }
    }
    switch (12.$SwitchMap$com$dianping$hui$entity$HuiPayDetailDataSource$PayType[this.dataSource.payType.ordinal()])
    {
    default:
    case 1:
    case 2:
      do
      {
        return;
        this.dataSource.payType = HuiPayDetailDataSource.PayType.HUI_PAY_RESULT;
        break;
        if (i / 10 == 4)
        {
          this.dataSource.payType = HuiPayDetailDataSource.PayType.HUI_ORDER_DETAIL_FROM_USER_ORDER_LIST;
          this.dataSource.huiOrderId = getStringParam("orderid");
          break;
        }
        this.dataSource.payType = HuiPayDetailDataSource.PayType.HUI_ORDER_DETAIL;
        break;
        this.dataSource.reqGetOneOrder();
      }
      while (this.isHobbit != 1);
      this.dataSource.reqHobbitResult();
      return;
    case 3:
      this.dataSource.reqHuiPayResult();
      return;
    case 4:
    }
    this.dataSource.reqBookingPayResult();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    this.dataSource.releaseRequests();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      exit();
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  protected void onLeftTitleButtonClicked()
  {
    exit();
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.dataSource.token = accountService().token();
      switch (12.$SwitchMap$com$dianping$hui$entity$HuiPayDetailDataSource$PayType[this.dataSource.payType.ordinal()])
      {
      default:
        this.dataSource.reqGetOneOrder();
      case 1:
      case 2:
      case 3:
      case 4:
      }
    }
    while (true)
    {
      return false;
      this.dataSource.reqGetOneOrder();
      continue;
      this.dataSource.reqHuiPayResult();
      continue;
      this.dataSource.reqBookingPayResult();
    }
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.dataSource.restoreData(paramBundle);
  }

  protected void onResume()
  {
    super.onResume();
    if (this.dataSource.reviewStatus == HuiPayDetailDataSource.ReviewStatus.OPEN_IN_EDIT)
      this.dataSource.reqHuiPayResult();
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.dataSource.saveData(paramBundle);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.activity.HuiPayDetailActivity
 * JD-Core Version:    0.6.0
 */