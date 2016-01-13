package com.dianping.hui.activity;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.share.enums.ShareType;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.TitleBar;
import com.dianping.hui.entity.HuiOrderDetailDataSource;
import com.dianping.hui.entity.HuiOrderDetailDataSource.HuiOrderDetailDataLoaderListener;
import com.dianping.hui.entity.HuiOrderDetailDataSource.OrderDetail;
import com.dianping.hui.entity.HuiOrderDetailDataSource.PayStatus;
import com.dianping.hui.entity.HuiOrderDetailShopVoucherAdapter;
import com.dianping.hui.util.HuiDialogAdapter;
import com.dianping.hui.util.HuiUtils;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.util.telephone.ContactUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.DPBasicItem;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.pulltorefresh.PullToRefreshBase;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class HuiOrderDetailActivity extends NovaActivity
  implements HuiOrderDetailDataSource.HuiOrderDetailDataLoaderListener, View.OnClickListener
{
  private static final DateFormat FMT_DATE;
  private static final String TAG = HuiOrderDetailActivity.class.getSimpleName();
  private Button btnCoupon;
  private Button btnDialCustomService;
  private Button btnDialShop;
  private Button btnUnpaidDialCustomerService;
  private Button btnUnpaidRepay;
  private Dialog contactCustomerServiceDialog;
  private Dialog contactShopDialog;
  private HuiOrderDetailDataSource dataSource;
  private ImageView ivIcon;
  private View layoutCoupon;
  private FrameLayout layoutError;
  private View layoutLoading;
  private LinearLayout layoutNormal;
  private View layoutShopName;
  private View layoutUnpaid;
  private View layoutUserPay;
  private LinearLayout mBuffetLayoutContainer;
  private DPBasicItem pointsMall;
  private View pointsMallLayout;
  private TextView tvBonus;
  private TextView tvBonusTitle;
  private TextView tvBoughtVoucher;
  private TextView tvBoughtVoucherBelow;
  private TextView tvContactMerchantTip;
  private TextView tvCouponDesc;
  private TextView tvCouponExtra;
  private TextView tvCouponTitle;
  private TextView tvCouponValidTime;
  private TextView tvCouponValue;
  private TextView tvNoDiscountPayAmount;
  private TextView tvNoDiscountPayAmountBelow;
  private TextView tvPayDate;
  private TextView tvPayOriAmount;
  private TextView tvSerialNumber;
  private TextView tvShopAmount;
  private TextView tvShopName;
  private TableView tvShopVouchers;
  private TextView tvSubtitle;
  private TextView tvTitle;
  private TextView tvUnpaidPayDate;
  private TextView tvUnpaidPayOriAmount;
  private TextView tvUnpaidShopName;
  private TextView tvUnpaidTitle;
  private TextView tvUsedVoucher;
  private TextView tvUserMobileNo;
  private TextView tvUserMobileNoTitle;
  private TextView tvUserPay;
  private PullToRefreshScrollView wholeLayout;

  static
  {
    FMT_DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  }

  private void addBuffetLayout()
  {
    if ((this.dataSource.buffetDescs == null) || (this.dataSource.buffetDescs.length == 0));
    while (true)
    {
      return;
      this.mBuffetLayoutContainer.removeAllViews();
      String[] arrayOfString = this.dataSource.buffetDescs;
      int j = arrayOfString.length;
      int i = 0;
      while (i < j)
      {
        Object localObject = arrayOfString[i];
        TextView localTextView = new TextView(this);
        localTextView.setText((CharSequence)localObject);
        localTextView.setTextColor(getResources().getColor(R.color.light_gray));
        localTextView.setTextSize(0, getResources().getDimension(R.dimen.text_size_14));
        localObject = new LinearLayout.LayoutParams(-2, -2);
        ((LinearLayout.LayoutParams)localObject).setMargins(0, ViewUtils.dip2px(this, 5.0F), 0, 0);
        localTextView.setLayoutParams((ViewGroup.LayoutParams)localObject);
        this.mBuffetLayoutContainer.addView(localTextView);
        i += 1;
      }
    }
  }

  private SpannableString createSaveAmountDesc(String paramString1, String paramString2)
  {
    if (!TextUtils.isEmpty(paramString1));
    int k;
    for (paramString2 = new SpannableString("已为您节省" + paramString2 + "元" + paramString1); ; paramString2 = new SpannableString("已为您节省" + paramString2 + "元"))
    {
      int i = getResources().getDimensionPixelSize(R.dimen.text_size_14);
      int j = getResources().getColor(R.color.light_gray);
      k = getResources().getColor(R.color.light_red);
      paramString2.setSpan(new AbsoluteSizeSpan(i), 0, paramString2.length(), 17);
      paramString2.setSpan(new ForegroundColorSpan(j), 0, paramString2.length(), 17);
      if (TextUtils.isEmpty(paramString1))
        break;
      paramString2.setSpan(new ForegroundColorSpan(k), 5, paramString2.length() - paramString1.length() - 1, 17);
      return paramString2;
    }
    paramString2.setSpan(new ForegroundColorSpan(k), 5, paramString2.length() - 1, 17);
    return paramString2;
  }

  private void initViews()
  {
    this.wholeLayout = ((PullToRefreshScrollView)super.findViewById(R.id.hui_pay_detail_whole_layout));
    this.layoutNormal = ((LinearLayout)super.findViewById(R.id.normal_layout));
    this.ivIcon = ((ImageView)super.findViewById(R.id.pay_success_icon));
    this.tvTitle = ((TextView)super.findViewById(R.id.title));
    this.tvSubtitle = ((TextView)super.findViewById(R.id.subtitle));
    this.layoutShopName = this.layoutNormal.findViewById(R.id.paydetails_shopname);
    this.tvShopName = ((TextView)super.findViewById(R.id.tv_pay_shopname));
    this.tvPayOriAmount = ((TextView)super.findViewById(R.id.tv_pay_ori_amount));
    this.tvShopAmount = ((TextView)super.findViewById(R.id.tv_shop_pay));
    this.tvShopVouchers = ((TableView)this.layoutNormal.findViewById(R.id.tv_shop_vouchers));
    this.tvShopVouchers.setDivider(new ColorDrawable(0));
    this.tvShopVouchers.setDividerOfGroupEnd(new ColorDrawable(0));
    this.layoutUserPay = this.layoutNormal.findViewById(R.id.user_pay_layout);
    this.tvUserPay = ((TextView)super.findViewById(R.id.tv_pay_cur_amount));
    this.tvUsedVoucher = ((TextView)this.layoutNormal.findViewById(R.id.tv_used_voucher));
    this.tvBoughtVoucherBelow = ((TextView)this.layoutNormal.findViewById(R.id.tv_bought_voucher_below));
    this.tvBoughtVoucher = ((TextView)this.layoutNormal.findViewById(R.id.tv_bought_voucher));
    this.tvNoDiscountPayAmount = ((TextView)super.findViewById(R.id.tv_no_discount_amount));
    this.tvNoDiscountPayAmountBelow = ((TextView)this.layoutNormal.findViewById(R.id.tv_no_discount_amount_below));
    this.tvPayDate = ((TextView)super.findViewById(R.id.tv_pay_date));
    this.tvUserMobileNoTitle = ((TextView)super.findViewById(R.id.user_mobile_no_title));
    this.tvUserMobileNo = ((TextView)super.findViewById(R.id.tv_user_mobile_no));
    this.tvSerialNumber = ((TextView)super.findViewById(R.id.identifying_code));
    this.tvBonusTitle = ((TextView)super.findViewById(R.id.bonus_title));
    this.tvBonus = ((TextView)super.findViewById(R.id.bonus));
    this.tvContactMerchantTip = ((TextView)super.findViewById(R.id.tv_contact_merchant_tip));
    this.layoutCoupon = super.findViewById(R.id.coupon_layout);
    this.tvCouponValue = ((TextView)this.layoutCoupon.findViewById(R.id.coupon_value));
    this.tvCouponTitle = ((TextView)this.layoutCoupon.findViewById(R.id.coupon_title));
    this.tvCouponDesc = ((TextView)this.layoutCoupon.findViewById(R.id.coupon_desc));
    this.tvCouponValidTime = ((TextView)this.layoutCoupon.findViewById(R.id.coupon_valid_time));
    this.tvCouponExtra = ((TextView)this.layoutCoupon.findViewById(R.id.coupon_extra));
    this.btnCoupon = ((Button)this.layoutCoupon.findViewById(R.id.coupon_button));
    this.btnDialCustomService = ((Button)super.findViewById(R.id.dial_custom_service));
    this.btnDialShop = ((Button)super.findViewById(R.id.dial_shop));
    this.layoutUnpaid = super.findViewById(R.id.unpaid_layout);
    this.tvUnpaidTitle = ((TextView)this.layoutUnpaid.findViewById(R.id.layout_unpaid_title));
    this.tvUnpaidShopName = ((TextView)this.layoutUnpaid.findViewById(R.id.layout_unpaid_tv_shopname));
    this.tvUnpaidPayOriAmount = ((TextView)this.layoutUnpaid.findViewById(R.id.layout_unpaid_tv_pay_ori_amount));
    this.tvUnpaidPayDate = ((TextView)this.layoutUnpaid.findViewById(R.id.layout_unpaid_date));
    this.btnUnpaidRepay = ((Button)this.layoutUnpaid.findViewById(R.id.layout_unpaid_repay));
    this.btnUnpaidRepay.setOnClickListener(this);
    this.btnUnpaidDialCustomerService = ((Button)this.layoutUnpaid.findViewById(R.id.layout_unpaid_dial_customer_service));
    this.btnUnpaidDialCustomerService.setOnClickListener(this);
    this.layoutLoading = super.findViewById(R.id.loading_layout);
    this.layoutError = ((FrameLayout)super.findViewById(R.id.error_layout));
    this.pointsMallLayout = findViewById(R.id.points_mall_layout);
    this.pointsMall = ((DPBasicItem)this.pointsMallLayout.findViewById(R.id.points_mall));
    this.mBuffetLayoutContainer = ((LinearLayout)super.findViewById(R.id.buffet_layout_container));
    manageLayouts(true, false, false, false);
  }

  private void loadPaidOrder()
  {
    if (this.dataSource.orderShareInfo != null)
      super.getTitleBar().addRightViewItem("share", R.drawable.ic_action_share, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = new ShareHolder();
          paramView.title = HuiOrderDetailActivity.this.dataSource.orderShareInfo.getString("Title");
          paramView.imageUrl = HuiOrderDetailActivity.this.dataSource.orderShareInfo.getString("IconUrl");
          paramView.webUrl = HuiOrderDetailActivity.this.dataSource.orderShareInfo.getString("Url");
          paramView.desc = HuiOrderDetailActivity.this.dataSource.orderShareInfo.getString("Desc");
          ShareUtil.gotoShareTo(HuiOrderDetailActivity.this, ShareType.Pay, paramView, "", "");
        }
      });
    HuiOrderDetailDataSource.OrderDetail localOrderDetail = this.dataSource.orderDetail;
    this.tvTitle.setText(this.dataSource.statusMsg);
    label153: Object localObject;
    switch (9.$SwitchMap$com$dianping$hui$entity$HuiOrderDetailDataSource$PayStatus[this.dataSource.payStatus.ordinal()])
    {
    default:
      if (this.dataSource.pointMallEnable != 10)
        break;
      this.pointsMallLayout.setVisibility(0);
      this.pointsMall.setTitle(this.dataSource.pointMallMessage);
      this.pointsMall.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (HuiOrderDetailActivity.this.dataSource.pointMallUrl != null)
            HuiOrderDetailActivity.this.startActivity(HuiOrderDetailActivity.this.dataSource.pointMallUrl);
        }
      });
      localObject = new SpannableString("");
      if ((this.dataSource.pointsInfo != null) && (!this.dataSource.pointsInfo.isEmpty()))
      {
        localObject = SpannableString.valueOf(TextUtils.jsonParseText(this.dataSource.pointsInfo));
        label201: if (((SpannableString)localObject).length() <= 0)
          break label1075;
        this.tvSubtitle.setText((CharSequence)localObject);
        this.tvSubtitle.setVisibility(0);
        label224: this.layoutShopName.setOnClickListener(this);
        this.tvShopName.setText(localOrderDetail.shopName);
        this.tvPayOriAmount.setText(localOrderDetail.totalAmount.stripTrailingZeros().toPlainString() + "元");
        localObject = new StringBuilder();
        if (TextUtils.isEmpty(localOrderDetail.shopVoucher))
          break label1087;
        ((StringBuilder)localObject).append(localOrderDetail.shopVoucher);
        if (localOrderDetail.shopAmount.doubleValue() != 0.0D)
          ((StringBuilder)localObject).append(" + ").append(localOrderDetail.shopAmount.stripTrailingZeros().toPlainString()).append("元");
        label343: this.tvShopAmount.setText(((StringBuilder)localObject).toString());
        if ((this.dataSource.shopVouchers == null) || (this.dataSource.shopVouchers.length <= 0))
          break label1110;
        this.tvShopVouchers.setAdapter(new HuiOrderDetailShopVoucherAdapter(this, this.dataSource.shopVouchers));
        this.tvShopVouchers.setVisibility(0);
        label405: if (TextUtils.isEmpty(this.dataSource.userAmountStr))
          break label1234;
        this.tvUserPay.setText(localOrderDetail.userPay);
        if (!TextUtils.isEmpty(localOrderDetail.boughtVoucher))
          break label1122;
        localObject = "";
        label443: if (!TextUtils.isEmpty((CharSequence)localObject))
          break label1155;
        this.tvBoughtVoucher.setVisibility(8);
        this.tvBoughtVoucherBelow.setVisibility(8);
        label468: if (!TextUtils.isEmpty(localOrderDetail.usedVoucher))
          break label1207;
        localObject = "";
        label482: ViewUtils.setVisibilityAndContent(this.tvUsedVoucher, (String)localObject);
        this.layoutUserPay.setVisibility(0);
        label498: if ((localOrderDetail.noDiscountAmount != null) && (localOrderDetail.noDiscountAmount.doubleValue() > 0.0D))
          break label1246;
        this.tvNoDiscountPayAmount.setVisibility(8);
        this.tvNoDiscountPayAmountBelow.setVisibility(8);
        label535: this.tvPayDate.setText(FMT_DATE.format(new Date(localOrderDetail.timeInMillis)));
        if (!TextUtils.isEmpty(localOrderDetail.phoneNumber))
          break label1334;
        this.tvUserMobileNoTitle.setVisibility(8);
        this.tvUserMobileNo.setVisibility(8);
        label587: if (!TextUtils.isEmpty(localOrderDetail.serialNumber))
          break label1367;
        localObject = "订单号获取失败";
        label601: this.tvSerialNumber.setText((CharSequence)localObject);
        if (!TextUtils.isEmpty(localOrderDetail.bonus))
          break label1378;
        this.tvBonusTitle.setVisibility(8);
        this.tvBonus.setVisibility(8);
        label637: localObject = this.dataSource.ticketInfo;
        if ((localObject == null) || (((DPObject)localObject).getInt("TicketShareStatus") == 0))
          break label1408;
        this.tvCouponValue.setText(((DPObject)localObject).getString("TicketValue"));
        ViewUtils.setVisibilityAndContent(this.tvCouponTitle, ((DPObject)localObject).getString("TicketTitle"));
        ViewUtils.setVisibilityAndContent(this.tvCouponDesc, ((DPObject)localObject).getString("TicketDesc"));
        ViewUtils.setVisibilityAndContent(this.tvCouponValidTime, ((DPObject)localObject).getString("TicketValidPeriod"));
        this.tvCouponExtra.setText(((DPObject)localObject).getString("ExtraTickets"));
        this.btnCoupon.setOnClickListener(this);
        this.btnCoupon.setText(((DPObject)localObject).getString("TicketButtonText"));
        this.btnCoupon.setEnabled(((DPObject)localObject).getBoolean("TicketButtonClickable"));
      }
    case 5:
    case 1:
    case 3:
    case 4:
    case 2:
    }
    while (true)
    {
      ViewUtils.setVisibilityAndContent(this.tvContactMerchantTip, this.dataSource.contactMerchantTip);
      this.btnDialCustomService.setOnClickListener(this);
      addBuffetLayout();
      this.btnDialShop.setOnClickListener(this);
      manageLayouts(false, false, false, true);
      statisticsEvent("pay5", "pay5_order", "支付成功", 0);
      return;
      this.ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_pay_pending));
      break;
      this.ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_pay_success));
      break;
      this.ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_pay_fail));
      break;
      this.pointsMallLayout.setVisibility(8);
      break label153;
      int i;
      if ((this.dataSource.ticketInfo != null) && (!TextUtils.isEmpty(this.dataSource.ticketInfo.getString("PayTicketDesc"))))
      {
        i = 1;
        label925: if ((this.dataSource.payStatus != HuiOrderDetailDataSource.PayStatus.PENDING) && (this.dataSource.payStatus != HuiOrderDetailDataSource.PayStatus.SUCCESS))
          break label1038;
        if (localOrderDetail.saveAmount.doubleValue() <= 0.0D)
          break label1010;
        if (i == 0)
          break label1005;
      }
      label1005: for (localObject = this.dataSource.ticketInfo.getString("PayTicketDesc"); ; localObject = null)
      {
        localObject = createSaveAmountDesc((String)localObject, localOrderDetail.saveAmount.stripTrailingZeros().toPlainString());
        break;
        i = 0;
        break label925;
      }
      label1010: if (i == 0)
        break label201;
      localObject = new SpannableString(this.dataSource.ticketInfo.getString("PayTicketDesc"));
      break label201;
      label1038: if (this.dataSource.payFailSubtitle == null);
      for (localObject = ""; ; localObject = this.dataSource.payFailSubtitle)
      {
        localObject = new SpannableString((CharSequence)localObject);
        break;
      }
      label1075: this.tvSubtitle.setVisibility(8);
      break label224;
      label1087: ((StringBuilder)localObject).append(localOrderDetail.shopAmount.stripTrailingZeros().toPlainString()).append("元");
      break label343;
      label1110: this.tvShopVouchers.setVisibility(8);
      break label405;
      label1122: localObject = "(" + localOrderDetail.boughtVoucher + ")";
      break label443;
      label1155: this.tvBoughtVoucherBelow.setText((CharSequence)localObject);
      this.tvBoughtVoucherBelow.setVisibility(8);
      this.tvBoughtVoucher.setText((CharSequence)localObject);
      this.tvBoughtVoucher.setVisibility(4);
      this.tvBoughtVoucher.post(new Runnable()
      {
        public void run()
        {
          Layout localLayout = HuiOrderDetailActivity.this.tvBoughtVoucher.getLayout();
          if (localLayout != null)
          {
            int i = localLayout.getLineCount();
            if (i > 0)
            {
              if (localLayout.getEllipsisCount(i - 1) <= 0)
                break label57;
              HuiOrderDetailActivity.this.tvBoughtVoucher.setVisibility(4);
              HuiOrderDetailActivity.this.tvBoughtVoucherBelow.setVisibility(0);
            }
          }
          return;
          label57: HuiOrderDetailActivity.this.tvBoughtVoucher.setVisibility(0);
          HuiOrderDetailActivity.this.tvBoughtVoucherBelow.setVisibility(8);
        }
      });
      break label468;
      label1207: localObject = "+ " + localOrderDetail.usedVoucher;
      break label482;
      label1234: this.layoutUserPay.setVisibility(8);
      break label498;
      label1246: localObject = "(含不参与优惠￥" + localOrderDetail.noDiscountAmount.stripTrailingZeros().toPlainString() + ")";
      this.tvNoDiscountPayAmountBelow.setText((CharSequence)localObject);
      this.tvNoDiscountPayAmountBelow.setVisibility(8);
      this.tvNoDiscountPayAmount.setText((CharSequence)localObject);
      this.tvNoDiscountPayAmount.setVisibility(4);
      this.tvNoDiscountPayAmount.post(new Runnable()
      {
        public void run()
        {
          Layout localLayout = HuiOrderDetailActivity.this.tvNoDiscountPayAmount.getLayout();
          if (localLayout != null)
          {
            int i = localLayout.getLineCount();
            if (i > 0)
            {
              if (localLayout.getEllipsisCount(i - 1) <= 0)
                break label57;
              HuiOrderDetailActivity.this.tvNoDiscountPayAmount.setVisibility(4);
              HuiOrderDetailActivity.this.tvNoDiscountPayAmountBelow.setVisibility(0);
            }
          }
          return;
          label57: HuiOrderDetailActivity.this.tvNoDiscountPayAmount.setVisibility(0);
          HuiOrderDetailActivity.this.tvNoDiscountPayAmountBelow.setVisibility(8);
        }
      });
      break label535;
      label1334: this.tvUserMobileNoTitle.setVisibility(0);
      this.tvUserMobileNo.setText(HuiUtils.splitPhoneNumber(localOrderDetail.phoneNumber));
      this.tvUserMobileNo.setVisibility(0);
      break label587;
      label1367: localObject = HuiUtils.splitSerialNumber(localOrderDetail.serialNumber);
      break label601;
      label1378: this.tvBonus.setText(localOrderDetail.bonus);
      this.tvBonus.setVisibility(0);
      this.tvBonusTitle.setVisibility(0);
      break label637;
      label1408: this.layoutCoupon.setVisibility(8);
    }
  }

  private void loadUnpaidOrder()
  {
    TextView localTextView = this.tvUnpaidTitle;
    if (TextUtils.isEmpty(this.dataSource.statusMsg));
    for (String str = "支付失败"; ; str = this.dataSource.statusMsg)
    {
      localTextView.setText(str);
      this.tvUnpaidShopName.setText(this.dataSource.orderDetail.shopName);
      this.tvUnpaidPayOriAmount.setText(this.dataSource.orderDetail.totalAmount.stripTrailingZeros().toPlainString() + "元");
      this.tvUnpaidPayDate.setText(FMT_DATE.format(new Date(this.dataSource.orderDetail.timeInMillis)));
      manageLayouts(false, false, true, false);
      statisticsEvent("pay5", "pay5_order", "支付失败", 0);
      return;
    }
  }

  private void manageLayouts(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    int j = 0;
    Object localObject = this.layoutLoading;
    if (paramBoolean1)
    {
      i = 0;
      ((View)localObject).setVisibility(i);
      localObject = this.layoutError;
      if (!paramBoolean2)
        break label93;
      i = 0;
      label36: ((FrameLayout)localObject).setVisibility(i);
      localObject = this.layoutUnpaid;
      if (!paramBoolean3)
        break label100;
      i = 0;
      label56: ((View)localObject).setVisibility(i);
      localObject = this.layoutNormal;
      if (!paramBoolean4)
        break label107;
    }
    label93: label100: label107: for (int i = j; ; i = 8)
    {
      ((LinearLayout)localObject).setVisibility(i);
      return;
      i = 8;
      break;
      i = 8;
      break label36;
      i = 8;
      break label56;
    }
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void loadOrderDetail()
  {
    this.wholeLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener()
    {
      public void onRefresh(PullToRefreshBase<ScrollView> paramPullToRefreshBase)
      {
        HuiOrderDetailActivity.this.dataSource.reqOrderDetail();
        HuiOrderDetailActivity.this.wholeLayout.onRefreshComplete();
      }
    });
    switch (9.$SwitchMap$com$dianping$hui$entity$HuiOrderDetailDataSource$PayStatus[this.dataSource.payStatus.ordinal()])
    {
    default:
      loadOrderDetailError();
      return;
    case 1:
    case 2:
    case 3:
    case 4:
      loadPaidOrder();
      return;
    case 5:
    case 6:
    }
    loadUnpaidOrder();
  }

  public void loadOrderDetailError()
  {
    this.layoutError.removeAllViews();
    this.layoutError.addView(getFailedView("网络连接失败 点击重新加载", new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        HuiOrderDetailActivity.this.dataSource.reqOrderDetail();
      }
    }));
    manageLayouts(false, true, false, false);
  }

  public void onClick(View paramView)
  {
    if (paramView == this.btnCoupon)
      if (this.dataSource.ticketInfo.getInt("TicketShareStatus") == 1)
      {
        accountService().login(this);
        statisticsEvent("hui7", "hui7_quan_login", "买单", 0);
      }
    do
    {
      do
        return;
      while (this.dataSource.ticketInfo.getInt("TicketShareStatus") != 2);
      paramView = new ShareHolder();
      Object localObject = this.dataSource.ticketInfo.getObject("ShareDo");
      paramView.title = ((DPObject)localObject).getString("Title");
      paramView.imageUrl = ((DPObject)localObject).getString("IconUrl");
      paramView.webUrl = ((DPObject)localObject).getString("Url");
      paramView.desc = ((DPObject)localObject).getString("Desc");
      ShareUtil.gotoShareTo(this, ShareType.Pay, paramView, "", "", 3);
      statisticsEvent("hui7", "hui7_quan_share", "买单", 0);
      paramView = new GAUserInfo();
      paramView.title = "查看";
      GAHelper.instance().contextStatisticsEvent(this, "quan_share", paramView, "tap");
      return;
      if ((paramView == this.btnDialCustomService) || (paramView == this.btnUnpaidDialCustomerService))
      {
        if (this.contactCustomerServiceDialog == null)
        {
          localObject = new AlertDialog.Builder(this);
          ((AlertDialog.Builder)localObject).setTitle("联系客服");
          ((AlertDialog.Builder)localObject).setAdapter(new HuiDialogAdapter(this, Arrays.asList(new String[] { "在线客服", "拨打电话" })), new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              switch (paramInt)
              {
              default:
                ContactUtils.dial(HuiOrderDetailActivity.this, "400-820-5527");
                return;
              case 0:
                GAHelper.instance().contextStatisticsEvent(HuiOrderDetailActivity.this, "customservice_online", null, "tap");
                if (HuiOrderDetailActivity.this.accountService().token() == null);
                for (paramDialogInterface = ""; ; paramDialogInterface = HuiOrderDetailActivity.this.accountService().token())
                {
                  paramDialogInterface = String.format("http://kf.dianping.com/third-part/user/app/consultCategory?d.user_token=%s&d.city_id=%s&d.user_type=user&d.from=app&d.consult_code=shanhuiOrder&activityName=shanhuiOrder", new Object[] { paramDialogInterface, Integer.valueOf(HuiOrderDetailActivity.this.cityId()) });
                  paramDialogInterface = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + Uri.encode(paramDialogInterface)));
                  HuiOrderDetailActivity.this.startActivity(paramDialogInterface);
                  return;
                }
              case 1:
              }
              GAHelper.instance().contextStatisticsEvent(HuiOrderDetailActivity.this, "customservice_phone", null, "tap");
              ContactUtils.dial(HuiOrderDetailActivity.this, "400-820-5527");
            }
          });
          this.contactCustomerServiceDialog = ((AlertDialog.Builder)localObject).create();
          this.contactCustomerServiceDialog.setCanceledOnTouchOutside(true);
        }
        this.contactCustomerServiceDialog.show();
        statisticsEvent("pay5", "pay5_complain", "", 0);
        if (paramView == this.btnDialCustomService)
        {
          GAHelper.instance().contextStatisticsEvent(this, "paydetails_success_contact", null, "tap");
          return;
        }
        GAHelper.instance().contextStatisticsEvent(this, "complain_call", null, "tap");
        return;
      }
      if (paramView == this.btnDialShop)
      {
        paramView = this.dataSource.shopPhoneNumbers;
        if ((paramView != null) && (paramView.length > 1))
        {
          if (this.contactShopDialog == null)
          {
            localObject = new AlertDialog.Builder(this);
            ((AlertDialog.Builder)localObject).setTitle("联系商户");
            ArrayList localArrayList = new ArrayList();
            int j = paramView.length;
            int i = 0;
            while (i < j)
            {
              String str = paramView[i];
              localArrayList.add("拨打电话: " + str);
              i += 1;
            }
            ((AlertDialog.Builder)localObject).setAdapter(new HuiDialogAdapter(this, localArrayList), new DialogInterface.OnClickListener(paramView)
            {
              public void onClick(DialogInterface paramDialogInterface, int paramInt)
              {
                ContactUtils.dial(HuiOrderDetailActivity.this, this.val$shopPhoneNumbers[paramInt]);
              }
            });
            this.contactShopDialog = ((AlertDialog.Builder)localObject).create();
            this.contactShopDialog.setCanceledOnTouchOutside(true);
          }
          this.contactShopDialog.show();
          return;
        }
        if ((paramView != null) && (paramView.length == 1))
        {
          ContactUtils.dial(this, paramView[0]);
          return;
        }
        showToast("无法获取商户电话，您可以联系点评客服咨询");
        return;
      }
      if (paramView != this.btnUnpaidRepay)
        continue;
      GAHelper.instance().contextStatisticsEvent(this, "resubmit", null, "tap");
      if (TextUtils.isEmpty(this.dataSource.repayUri));
      for (paramView = "dianping://huiunifiedcashier?shopid=" + this.dataSource.shopId + "&shopname=" + this.dataSource.orderDetail.shopName; ; paramView = this.dataSource.repayUri)
      {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramView)));
        return;
      }
    }
    while (paramView != this.layoutShopName);
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?shopid=" + this.dataSource.shopId)));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setTitle("买单详情");
    super.setContentView(R.layout.hui_pay_result_activity);
    super.getWindow().setBackgroundDrawable(null);
    initViews();
    this.dataSource = new HuiOrderDetailDataSource(this);
    this.dataSource.dataLoadListener = this;
    this.dataSource.serializedId = getStringParam("serializedid");
    this.dataSource.ordertime = getStringParam("ordertime");
    this.dataSource.bizType = getIntParam("biztype");
    this.dataSource.reqOrderDetail();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    this.dataSource.releaseRequests();
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if (paramBoolean)
      this.dataSource.reqOrderDetail();
    return false;
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.dataSource.restoreData(paramBundle);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.dataSource.saveData(paramBundle);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.activity.HuiOrderDetailActivity
 * JD-Core Version:    0.6.0
 */