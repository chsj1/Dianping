package com.dianping.hui.activity;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.TextUtils.TruncateAt;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.share.enums.ShareType;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.base.ugc.review.AddReviewUtil;
import com.dianping.base.util.FavoriteHelper;
import com.dianping.base.util.SoundPlayer;
import com.dianping.base.widget.TitleBar;
import com.dianping.hui.entity.HuiPayResultDataSource;
import com.dianping.hui.entity.HuiPayResultDataSource.FavoriteStatus;
import com.dianping.hui.entity.HuiPayResultDataSource.OrderDetail;
import com.dianping.hui.entity.HuiPayResultDataSource.PayStatus;
import com.dianping.hui.entity.HuiPayResultDataSource.ReviewStatus;
import com.dianping.hui.entity.HuiPayResultDataSource.UniPayResultDataLoaderListener;
import com.dianping.hui.fragment.HuiPayResultFragment;
import com.dianping.hui.util.HuiDialogAdapter;
import com.dianping.hui.util.HuiUtils;
import com.dianping.hui.view.HuiPayResultPopUpMenu;
import com.dianping.hui.view.HuiPayResultPopUpMenu.PopItemClickListener;
import com.dianping.membercard.constant.MURelationshipProductCode;
import com.dianping.membercard.view.MCHuiInfoDislayView;
import com.dianping.membercard.view.MerchantUserRelationshipView;
import com.dianping.membercard.view.MerchantUserRelationshipView.MUViewShowListener;
import com.dianping.util.CrashReportHelper;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.util.telephone.ContactUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.raw;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class HuiPayResultActivity extends AgentActivity
  implements HuiPayResultDataSource.UniPayResultDataLoaderListener, View.OnClickListener, HuiPayResultPopUpMenu.PopItemClickListener
{
  private static final DateFormat FMT_DATE;
  private static final String TAG = HuiPayResultActivity.class.getSimpleName();
  private View addReview;
  private TextView btnAddReview;
  private Dialog contactCustomerServiceDialog;
  private Dialog contactShopDialog;
  private HuiPayResultDataSource dataSource;
  private ImageView huiPayResultArrowDown;
  private HuiPayResultPopUpMenu huiPayResultPopUpMenu;
  private boolean isMoreInfoClicked = false;
  private NetworkImageView ivAdvertisement;
  private ImageView ivIcon;
  private FrameLayout layoutError;
  private View layoutLoading;
  private LinearLayout layoutNormal;
  private View layoutShopName;
  private View mMemcardLayout;
  private LinearLayout mRemarksLayoutContainer;
  private View mRlShopInfoLayout;
  private MerchantUserRelationshipView memchantUserRelationShipView;
  private View noteLayout;
  private LinearLayout payTimeLayout;
  private View shopAddLayout;
  private LinearLayout shopPayLayout;
  private TextView tvPayDate;
  private TextView tvPayOriAmount;
  private TextView tvSerialNumber;
  private TextView tvShopAmount;
  private TextView tvShopAmountAdd;
  private TextView tvShopName;
  private TextView tvShopPayUnit;
  private TextView tvSubtitle;
  private TextView tvTitle;
  private TextView tvUserMobileNo;
  private TextView tvUserMobileNoTitle;
  private TextView tvUserPay;
  private TextView tvUserPayUnit;
  private LinearLayout userBenefitContentView;
  private LinearLayout userBenefitLayout;
  private View viewLine;
  private ScrollView wholeLayout;

  static
  {
    FMT_DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  }

  private void addRemarksLayout()
  {
    if ((this.dataSource.remarks == null) || (this.dataSource.remarks.length == 0))
      this.noteLayout.setVisibility(8);
    while (true)
    {
      return;
      this.noteLayout.setVisibility(0);
      this.viewLine.setVisibility(0);
      this.mRemarksLayoutContainer.removeAllViews();
      int i = 0;
      while (i < this.dataSource.remarks.length)
      {
        TextView localTextView = new TextView(this);
        localTextView.setSingleLine();
        localTextView.setEllipsize(TextUtils.TruncateAt.END);
        localTextView.setText(this.dataSource.remarks[i]);
        localTextView.setTextColor(getResources().getColor(R.color.light_gray));
        localTextView.setTextSize(0, getResources().getDimension(R.dimen.text_size_15));
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
        if (i != 0)
          localLayoutParams.setMargins(0, 8, 0, 0);
        localTextView.setLayoutParams(localLayoutParams);
        this.mRemarksLayoutContainer.addView(localTextView);
        i += 1;
      }
    }
  }

  private void addUserBenefitLayout()
  {
    if ((this.dataSource.rightDoArray == null) || (this.dataSource.rightDoArray.length == 0))
    {
      this.userBenefitLayout.setVisibility(8);
      return;
    }
    this.userBenefitLayout.setVisibility(0);
    this.userBenefitContentView.removeAllViews();
    int i = 0;
    label49: Object localObject;
    int j;
    DPObject localDPObject;
    View localView1;
    ImageView localImageView1;
    ImageView localImageView2;
    View localView2;
    if (i < this.dataSource.rightDoArray.length)
    {
      localObject = this.dataSource.rightDoArray[i].getString("Url");
      String str = this.dataSource.rightDoArray[i].getString("RichDesc");
      j = this.dataSource.rightDoArray[i].getInt("TagType");
      localDPObject = this.dataSource.rightDoArray[i].getObject("ShareDo");
      localView1 = LayoutInflater.from(this).inflate(R.layout.hui_user_benefit, null);
      localView1.setLayoutParams(new ViewGroup.LayoutParams(-1, ViewUtils.dip2px(this, 50.0F)));
      localImageView1 = (ImageView)localView1.findViewById(R.id.iv_benefit_icon);
      localImageView2 = (ImageView)localView1.findViewById(R.id.iv_benefit_right_icon);
      TextView localTextView = (TextView)localView1.findViewById(R.id.tv_benefit_title);
      localView2 = localView1.findViewById(R.id.benefit_divider_line);
      localTextView.setText(com.dianping.util.TextUtils.jsonParseText(str));
      if (j != 1)
        break label297;
      GAHelper.instance().contextStatisticsEvent(this, "hui7_payresult_ticket", null, "view");
      localImageView1.setImageResource(R.drawable.icon_benefit_coupon);
      localView1.setOnClickListener(new View.OnClickListener((String)localObject)
      {
        public void onClick(View paramView)
        {
          HuiPayResultActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + this.val$redictUrl)));
          GAHelper.instance().contextStatisticsEvent(HuiPayResultActivity.this, "hui7_payresult_ticket", null, "tap");
        }
      });
    }
    while (true)
    {
      if (i == this.dataSource.rightDoArray.length - 1)
        localView2.setVisibility(8);
      this.userBenefitContentView.addView(localView1);
      i += 1;
      break label49;
      break;
      label297: if (j == 2)
      {
        localImageView1.setImageResource(R.drawable.icon_benefit_point);
        GAHelper.instance().contextStatisticsEvent(this, "hui7_payresult_point", null, "view");
        localView1.setOnClickListener(new View.OnClickListener((String)localObject)
        {
          public void onClick(View paramView)
          {
            HuiPayResultActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.val$redictUrl)));
            GAHelper.instance().contextStatisticsEvent(HuiPayResultActivity.this, "hui7_payresult_point", null, "tap");
          }
        });
        continue;
      }
      if (j != 3)
        continue;
      localObject = (Button)localView1.findViewById(R.id.coupon_button);
      localImageView1.setImageResource(R.drawable.icon_benefit_envelope);
      localImageView2.setVisibility(8);
      if (localDPObject == null)
        continue;
      ((Button)localObject).setText(localDPObject.getString("BtnText"));
      ((Button)localObject).setVisibility(0);
      ((Button)localObject).setOnClickListener(new View.OnClickListener(localDPObject)
      {
        public void onClick(View paramView)
        {
          HuiPayResultActivity.this.sendRedEnvelope(this.val$shareDo);
        }
      });
    }
  }

  private void exit()
  {
    if (this.dataSource.backUri == null)
      initBackUri();
    Intent localIntent = new Intent("android.intent.action.VIEW", this.dataSource.backUri);
    localIntent.setFlags(67108864);
    startActivity(localIntent);
    finish();
  }

  private Uri getBackUriFromSchemaHistory(Uri paramUri, List<ISchemaMatcher> paramList)
  {
    if ((paramUri == null) || (paramList == null) || (paramList.isEmpty()))
      throw new InvalidParameterException("null parameter");
    try
    {
      Iterator localIterator1 = CrashReportHelper.listSchema.iterator();
      Object localObject;
      while (true)
        while (true)
        {
          localObject = paramUri;
          if (localIterator1.hasNext())
          {
            localObject = (String)localIterator1.next();
            Iterator localIterator2 = paramList.iterator();
            if (!localIterator2.hasNext())
              continue;
            ISchemaMatcher localISchemaMatcher = (ISchemaMatcher)localIterator2.next();
            if (!localISchemaMatcher.matchSchema((String)localObject))
              break;
            localObject = localISchemaMatcher.extractUri((String)localObject);
          }
        }
      return localObject;
    }
    catch (java.lang.Exception paramList)
    {
      Log.w(TAG, "Fail: get back uri", paramList);
    }
    return (Uri)paramUri;
  }

  private void gotoOnlineCustomeService()
  {
    if (!android.text.TextUtils.isEmpty(this.dataSource.serviceUrl))
      if (accountService().token() == null)
        break label96;
    label96: for (String str = this.dataSource.serviceUrl + "?cityId=" + cityId() + "&token=" + accountService().token(); ; str = this.dataSource.serviceUrl + "?cityId=" + cityId())
    {
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
      return;
    }
  }

  private void initBackUri()
  {
    Uri localUri = Uri.parse("dianping://shopinfo?id=" + this.dataSource.shopId);
    1 local1 = new ISchemaMatcher(localUri)
    {
      public Uri extractUri(String paramString)
      {
        return this.val$defaultBackUri;
      }

      public boolean matchSchema(String paramString)
      {
        return paramString.startsWith("dianping://shopinfo");
      }
    };
    2 local2 = new ISchemaMatcher()
    {
      public Uri extractUri(String paramString)
      {
        return Uri.parse(paramString);
      }

      public boolean matchSchema(String paramString)
      {
        return (paramString.startsWith("dianping://huipaydetail")) && (paramString.contains("serializedid"));
      }
    };
    3 local3 = new ISchemaMatcher()
    {
      public Uri extractUri(String paramString)
      {
        return Uri.parse(paramString);
      }

      public boolean matchSchema(String paramString)
      {
        return (paramString.startsWith("dianping://huidetail")) && (paramString.contains("id")) && (paramString.contains("shopid")) && (paramString.contains("dealid"));
      }
    };
    4 local4 = new ISchemaMatcher()
    {
      public Uri extractUri(String paramString)
      {
        return Uri.parse(paramString);
      }

      public boolean matchSchema(String paramString)
      {
        return paramString.startsWith("dianping://integrateordertab");
      }
    };
    5 local5 = new ISchemaMatcher()
    {
      public Uri extractUri(String paramString)
      {
        return Uri.parse(paramString);
      }

      public boolean matchSchema(String paramString)
      {
        return paramString.startsWith("dianping://bookinginfo?serializedid=");
      }
    };
    6 local6 = new ISchemaMatcher()
    {
      public Uri extractUri(String paramString)
      {
        return Uri.parse(paramString);
      }

      public boolean matchSchema(String paramString)
      {
        return paramString.startsWith("dianping://home");
      }
    };
    this.dataSource.backUri = getBackUriFromSchemaHistory(localUri, Arrays.asList(new ISchemaMatcher[] { local1, local2, local3, local4, local5, local6 }));
  }

  private void initViews()
  {
    this.wholeLayout = ((ScrollView)super.findViewById(R.id.hui_pay_detail_whole_layout));
    this.huiPayResultPopUpMenu = new HuiPayResultPopUpMenu(this);
    View localView = findViewById(R.id.title_bar);
    super.getTitleBar().addRightViewItem("帮助", null, new View.OnClickListener(localView)
    {
      public void onClick(View paramView)
      {
        HuiPayResultActivity.this.huiPayResultPopUpMenu.show(this.val$titleBar);
      }
    });
    this.layoutNormal = ((LinearLayout)super.findViewById(R.id.normal_layout));
    this.memchantUserRelationShipView = ((MerchantUserRelationshipView)super.findViewById(R.id.memchant_user_relation_shipview));
    this.mMemcardLayout = super.findViewById(R.id.memcard_layout);
    this.ivIcon = ((ImageView)super.findViewById(R.id.pay_success_icon));
    this.tvTitle = ((TextView)super.findViewById(R.id.title));
    this.tvSubtitle = ((TextView)super.findViewById(R.id.subtitle));
    this.layoutShopName = this.layoutNormal.findViewById(R.id.paydetails_shopname);
    this.mRemarksLayoutContainer = ((LinearLayout)super.findViewById(R.id.buffet_layout_container));
    this.tvPayOriAmount = ((TextView)super.findViewById(R.id.tv_pay_ori_amount));
    this.viewLine = findViewById(R.id.view_line);
    this.noteLayout = findViewById(R.id.rl_note);
    this.huiPayResultArrowDown = ((ImageView)findViewById(R.id.hui_pay_result_arrow_down));
    this.userBenefitLayout = ((LinearLayout)findViewById(R.id.hui_pay_result_user_benefit));
    this.userBenefitContentView = ((LinearLayout)this.userBenefitLayout.findViewById(R.id.tab_view_user_benefit));
    this.tvShopName = ((TextView)this.layoutNormal.findViewById(R.id.tv_pay_shopname));
    this.mRlShopInfoLayout = super.findViewById(R.id.rl_shop_info_layout);
    this.mRlShopInfoLayout.setOnClickListener(this);
    this.tvShopAmount = ((TextView)super.findViewById(R.id.tv_shop_pay));
    this.tvShopPayUnit = ((TextView)super.findViewById(R.id.shop_pay_unit));
    this.shopAddLayout = super.findViewById(R.id.shop_add_layout);
    this.tvShopAmountAdd = ((TextView)super.findViewById(R.id.tv_shop_pay_add));
    this.tvUserPay = ((TextView)super.findViewById(R.id.tv_pay_cur_amount));
    this.tvUserPayUnit = ((TextView)super.findViewById(R.id.tv_user_pay_unit));
    this.shopPayLayout = ((LinearLayout)super.findViewById(R.id.ll_shop_pay));
    this.payTimeLayout = ((LinearLayout)super.findViewById(R.id.ll_custome_time));
    this.tvPayDate = ((TextView)super.findViewById(R.id.tv_pay_date));
    this.tvUserMobileNoTitle = ((TextView)super.findViewById(R.id.user_mobile_no_title));
    this.tvUserMobileNo = ((TextView)super.findViewById(R.id.tv_user_mobile_no));
    this.tvSerialNumber = ((TextView)super.findViewById(R.id.identifying_code));
    this.addReview = this.layoutNormal.findViewById(R.id.addreview_layout);
    this.btnAddReview = ((TextView)this.addReview.findViewById(R.id.btn_addreview));
    this.ivAdvertisement = ((NetworkImageView)super.findViewById(R.id.advertisement));
    this.layoutLoading = super.findViewById(R.id.loading_layout);
    this.layoutError = ((FrameLayout)super.findViewById(R.id.error_layout));
    manageLayouts(true, false, false);
  }

  private void loadMemcardSection()
  {
    this.memchantUserRelationShipView.setSelectCityId(cityId()).setShopId(this.dataSource.shopId).setProductCode(MURelationshipProductCode.HUI).setMUShowListener(new MerchantUserRelationshipView.MUViewShowListener()
    {
      public void showOrHideMUView(boolean paramBoolean)
      {
        View localView = HuiPayResultActivity.this.mMemcardLayout;
        if (paramBoolean);
        for (int i = 0; ; i = 8)
        {
          localView.setVisibility(i);
          return;
        }
      }
    }).askIfMUVShowed();
    ((MCHuiInfoDislayView)findViewById(R.id.mc_huicashier_info_view)).refreshInfoDisplayView(this.dataSource.shopId, this.dataSource.huiOrderId);
  }

  private void loadPaidOrder()
  {
    HuiPayResultDataSource.OrderDetail localOrderDetail = this.dataSource.orderDetail;
    this.tvTitle.setText(this.dataSource.statusMsg);
    Object localObject2;
    Object localObject1;
    label147: label214: label347: int i;
    switch (15.$SwitchMap$com$dianping$hui$entity$HuiPayResultDataSource$PayStatus[this.dataSource.payStatus.ordinal()])
    {
    default:
      localObject2 = new SpannableString("");
      localObject1 = localObject2;
      if (this.dataSource.pointsInfo != null)
      {
        localObject1 = localObject2;
        if (!this.dataSource.pointsInfo.isEmpty())
          localObject1 = SpannableString.valueOf(com.dianping.util.TextUtils.jsonParseText(this.dataSource.pointsInfo));
      }
      if (((SpannableString)localObject1).length() > 0)
      {
        this.tvSubtitle.setText((CharSequence)localObject1);
        this.tvSubtitle.setVisibility(0);
        this.layoutShopName.setOnClickListener(this);
        this.tvShopName.setText(localOrderDetail.shopName);
        this.tvPayOriAmount.setText(localOrderDetail.totalAmount.stripTrailingZeros().toPlainString());
        if (!android.text.TextUtils.isEmpty(this.dataSource.userAmountStr))
          break label793;
        this.tvUserPay.setVisibility(8);
        this.tvUserPayUnit.setVisibility(8);
        if (android.text.TextUtils.isEmpty(localOrderDetail.shopVoucher))
          break label841;
        this.tvShopAmount.setText("代金券+");
        this.tvShopAmount.setTextColor(getResources().getColor(R.color.deep_gray));
        this.tvShopPayUnit.setVisibility(8);
        this.shopAddLayout.setVisibility(0);
        localObject1 = localOrderDetail.shopAmount.stripTrailingZeros().toPlainString();
        this.tvShopAmountAdd.setText((CharSequence)localObject1);
        this.tvShopAmountAdd.setVisibility(0);
        label295: this.tvPayDate.setText(FMT_DATE.format(new Date(localOrderDetail.timeInMillis)));
        if (!android.text.TextUtils.isEmpty(localOrderDetail.phoneNumber))
          break label896;
        this.tvUserMobileNoTitle.setVisibility(8);
        this.tvUserMobileNo.setVisibility(8);
        if (android.text.TextUtils.isEmpty(localOrderDetail.serialNumber))
          break label942;
        this.tvSerialNumber.setText(HuiUtils.splitSerialNumber(localOrderDetail.serialNumber));
        this.tvSerialNumber.setTextColor(getResources().getColor(R.color.deep_gray));
        if (this.dataSource.favoriteStatus == HuiPayResultDataSource.FavoriteStatus.UNINITIALIZED)
        {
          if ((!isLogined()) || (FavoriteHelper.isFavoriteShop(accountService().token(), String.valueOf(this.dataSource.shopId))))
            break label929;
          i = 1;
          label436: localObject2 = this.dataSource;
          if (i == 0)
            break label935;
          localObject1 = HuiPayResultDataSource.FavoriteStatus.SHOW;
          label450: ((HuiPayResultDataSource)localObject2).favoriteStatus = ((HuiPayResultDataSource.FavoriteStatus)localObject1);
        }
        if (this.dataSource.payStatus != HuiPayResultDataSource.PayStatus.SUCCESS)
          break;
        if (this.dataSource.reviewStatus != HuiPayResultDataSource.ReviewStatus.OPEN)
          break label1008;
        this.addReview.setOnClickListener(this);
        this.btnAddReview.setVisibility(0);
        this.addReview.setVisibility(0);
        if (!android.text.TextUtils.isEmpty(this.dataSource.reviewInfo.getString("CommentLabel")))
          this.btnAddReview.setText(this.dataSource.reviewInfo.getString("CommentLabel"));
        label544: if ((this.dataSource.advertisements == null) || (this.dataSource.advertisements.length <= 0))
          break label1062;
        localObject1 = this.dataSource.advertisements[0].getString("PicUrl");
        this.ivAdvertisement.setImage((String)localObject1);
        this.ivAdvertisement.setOnClickListener(this);
        this.ivAdvertisement.setTag(this.dataSource.advertisements[0].getString("LinkUrl"));
        this.ivAdvertisement.setVisibility(0);
      }
    case 1:
    case 2:
    case 4:
    case 5:
    case 3:
    }
    while (true)
    {
      ((HuiPayResultFragment)this.mFragment).requestAdvertisementAgent(this.dataSource.shopId, this.dataSource.orderDetail.phoneNumber);
      addUserBenefitLayout();
      loadMemcardSection();
      if ((this.dataSource.hasVoiceReport) && (!this.dataSource.backFromAddReviewPage))
        SoundPlayer.play(R.raw.paysucc);
      if (this.isMoreInfoClicked)
        addRemarksLayout();
      manageLayouts(false, false, true);
      statisticsEvent("pay5", "pay5_order", "支付成功", 0);
      return;
      this.ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_pay_pending));
      break;
      this.ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_pay_success));
      break;
      this.ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.icon_pay_fail));
      break;
      this.tvSubtitle.setVisibility(8);
      break label147;
      label793: localObject1 = new BigDecimal(this.dataSource.curAmount).stripTrailingZeros().toPlainString();
      this.tvUserPay.setText((CharSequence)localObject1);
      this.tvUserPay.setVisibility(0);
      this.tvUserPayUnit.setVisibility(0);
      break label214;
      label841: this.shopAddLayout.setVisibility(8);
      this.tvShopAmountAdd.setVisibility(8);
      this.tvShopAmount.setTextColor(getResources().getColor(R.color.tuan_common_orange));
      this.tvShopAmount.setText(localOrderDetail.shopAmount.stripTrailingZeros().toPlainString());
      break label295;
      label896: this.tvUserMobileNoTitle.setVisibility(0);
      this.tvUserMobileNo.setText(HuiUtils.splitPhoneNumber(localOrderDetail.phoneNumber));
      this.tvUserMobileNo.setVisibility(0);
      break label347;
      label929: i = 0;
      break label436;
      label935: localObject1 = HuiPayResultDataSource.FavoriteStatus.HIDE;
      break label450;
      label942: localObject2 = this.tvSerialNumber;
      if (this.dataSource.payStatus == HuiPayResultDataSource.PayStatus.PENDING);
      for (localObject1 = "正在获取(" + this.dataSource.huiPayResultCountDownSeconds + "s)"; ; localObject1 = "订单号获取失败")
      {
        ((TextView)localObject2).setText((CharSequence)localObject1);
        break;
      }
      label1008: if (this.dataSource.reviewStatus == HuiPayResultDataSource.ReviewStatus.CLOSED)
      {
        this.btnAddReview.setVisibility(8);
        this.addReview.setVisibility(0);
        this.addReview.setVisibility(8);
        break label544;
      }
      this.addReview.setVisibility(8);
      break label544;
      label1062: this.ivAdvertisement.setVisibility(8);
    }
  }

  private void manageLayouts(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    int j = 0;
    Object localObject = this.layoutLoading;
    if (paramBoolean1)
    {
      i = 0;
      ((View)localObject).setVisibility(i);
      localObject = this.layoutError;
      if (!paramBoolean2)
        break label72;
      i = 0;
      label36: ((FrameLayout)localObject).setVisibility(i);
      localObject = this.layoutNormal;
      if (!paramBoolean3)
        break label79;
    }
    label72: label79: for (int i = j; ; i = 8)
    {
      ((LinearLayout)localObject).setVisibility(i);
      return;
      i = 8;
      break;
      i = 8;
      break label36;
    }
  }

  private void sendRedEnvelope(DPObject paramDPObject)
  {
    ShareHolder localShareHolder = new ShareHolder();
    localShareHolder.title = paramDPObject.getString("Title");
    localShareHolder.imageUrl = paramDPObject.getString("IconUrl");
    localShareHolder.webUrl = paramDPObject.getString("Url");
    localShareHolder.desc = paramDPObject.getString("Desc");
    ShareUtil.gotoShareTo(this, ShareType.Pay, localShareHolder, "", "", 3);
    statisticsEvent("hui7", "hui7_quan_share", "买单", 0);
    paramDPObject = new GAUserInfo();
    paramDPObject.title = "买单";
    GAHelper.instance().contextStatisticsEvent(this, "quan_share", paramDPObject, "tap");
  }

  public void commonQuestion()
  {
    gotoOnlineCustomeService();
    GAHelper.instance().contextStatisticsEvent(this, "hui7_payresult_faq", null, "tap");
  }

  public void contactBusiness()
  {
    if ((this.dataSource.shopPhoneNumbers == null) || (this.dataSource.shopPhoneNumbers.length == 0))
    {
      showShortToast("无法获取商户电话，您可以联系点评客服咨询");
      return;
    }
    String[] arrayOfString = this.dataSource.shopPhoneNumbers;
    if ((arrayOfString != null) && (arrayOfString.length > 1))
    {
      if (this.contactShopDialog == null)
      {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setTitle("联系商户");
        ArrayList localArrayList = new ArrayList();
        int j = arrayOfString.length;
        int i = 0;
        while (i < j)
        {
          String str = arrayOfString[i];
          localArrayList.add("拨打电话: " + str);
          i += 1;
        }
        localBuilder.setAdapter(new HuiDialogAdapter(this, localArrayList), new DialogInterface.OnClickListener(arrayOfString)
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            ContactUtils.dial(HuiPayResultActivity.this, this.val$shopPhoneNumbers[paramInt]);
          }
        });
        this.contactShopDialog = localBuilder.create();
        this.contactShopDialog.setCanceledOnTouchOutside(true);
      }
      this.contactShopDialog.show();
      return;
    }
    if ((arrayOfString != null) && (arrayOfString.length == 1))
    {
      ContactUtils.dial(this, arrayOfString[0]);
      return;
    }
    showToast("无法获取商户电话，您可以联系点评客服咨询");
  }

  public void contactCustomerService()
  {
    if (this.contactCustomerServiceDialog == null)
    {
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
      localBuilder.setTitle("联系客服");
      localBuilder.setAdapter(new HuiDialogAdapter(this, Arrays.asList(new String[] { "在线客服", "拨打电话" })), new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          switch (paramInt)
          {
          default:
            ContactUtils.dial(HuiPayResultActivity.this, "400-820-5527");
            return;
          case 0:
            GAHelper.instance().contextStatisticsEvent(HuiPayResultActivity.this, "customservice_online", null, "tap");
            if (HuiPayResultActivity.this.accountService().token() == null);
            for (paramDialogInterface = ""; ; paramDialogInterface = HuiPayResultActivity.this.accountService().token())
            {
              paramDialogInterface = String.format("http://kf.dianping.com/third-part/user/app/consultCategory?d.user_token=%s&d.city_id=%s&d.user_type=user&d.from=app&d.consult_code=shanhuiOrder&activityName=shanhuiOrder", new Object[] { paramDialogInterface, Integer.valueOf(HuiPayResultActivity.this.cityId()) });
              paramDialogInterface = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + Uri.encode(paramDialogInterface)));
              HuiPayResultActivity.this.startActivity(paramDialogInterface);
              return;
            }
          case 1:
          }
          GAHelper.instance().contextStatisticsEvent(HuiPayResultActivity.this, "customservice_phone", null, "tap");
          ContactUtils.dial(HuiPayResultActivity.this, "400-820-5527");
        }
      });
      this.contactCustomerServiceDialog = localBuilder.create();
      this.contactCustomerServiceDialog.setCanceledOnTouchOutside(true);
    }
    this.contactCustomerServiceDialog.show();
    statisticsEvent("pay5", "pay5_complain", "", 0);
  }

  public void countDown(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.tvSerialNumber.setText("正在获取(" + paramInt + "s)");
      return;
    }
    this.tvSerialNumber.setText("订单号获取失败");
    this.tvSerialNumber.setTextColor(getResources().getColor(R.color.deep_gray));
  }

  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
      this.mFragment = new HuiPayResultFragment();
    return this.mFragment;
  }

  public ScrollView getScrollView()
  {
    return this.wholeLayout;
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected void initViewAgentView(Bundle paramBundle)
  {
    paramBundle = getSupportFragmentManager();
    this.mFragment = ((AgentFragment)paramBundle.findFragmentByTag("agentfragment"));
    if (this.mFragment == null)
      this.mFragment = getAgentFragment();
    paramBundle = paramBundle.beginTransaction();
    paramBundle.replace(R.id.hui_pay_result_fragment, this.mFragment, "agentfragment");
    paramBundle.commit();
  }

  public void loadHuiPayResultError()
  {
    this.layoutError.removeAllViews();
    this.layoutError.addView(getFailedView("网络连接失败 点击重新加载", new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        HuiPayResultActivity.this.dataSource.reqHuiPayResult();
      }
    }));
    manageLayouts(false, true, false);
  }

  public void loadOneOrder()
  {
    switch (15.$SwitchMap$com$dianping$hui$entity$HuiPayResultDataSource$PayStatus[this.dataSource.payStatus.ordinal()])
    {
    default:
      loadHuiPayResultError();
      return;
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    }
    loadPaidOrder();
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    getAgentFragment().onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onClick(View paramView)
  {
    if (paramView == this.addReview)
    {
      AddReviewUtil.addReview(this, this.dataSource.shopId, this.dataSource.orderDetail.shopName);
      this.dataSource.reviewStatus = HuiPayResultDataSource.ReviewStatus.OPEN_IN_EDIT;
      statisticsEvent("hui7", "hui7_comment_apply", "", 0);
      GAHelper.instance().contextStatisticsEvent(this, "comment_apply", null, "tap");
    }
    do
    {
      return;
      if (paramView != this.ivAdvertisement)
        continue;
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + Uri.encode((String)this.ivAdvertisement.getTag()))));
      return;
    }
    while (paramView != this.mRlShopInfoLayout);
    this.shopPayLayout.setVisibility(0);
    this.payTimeLayout.setVisibility(0);
    this.huiPayResultArrowDown.setVisibility(8);
    this.isMoreInfoClicked = true;
    addRemarksLayout();
    GAHelper.instance().contextStatisticsEvent(this, "hui7_payresult_dtoexpand", null, "tap");
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setTitle("买单详情");
    super.setContentView(R.layout.hui_pay_result_agent_activity);
    super.getWindow().setBackgroundDrawable(null);
    initViews();
    this.dataSource = new HuiPayResultDataSource(this);
    this.dataSource.dataLoadListener = this;
    this.dataSource.shopId = getIntParam("shopid");
    this.dataSource.serializedId = getStringParam("serializedid");
    this.dataSource.orderCreateTime = getStringParam("ordercreatetime");
    this.dataSource.reqHuiPayResult();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    this.dataSource.releaseRequests();
    this.memchantUserRelationShipView.destroyView();
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

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.dataSource.restoreData(paramBundle);
  }

  protected void onResume()
  {
    super.onResume();
    if (this.dataSource.reviewStatus == HuiPayResultDataSource.ReviewStatus.OPEN_IN_EDIT)
    {
      this.dataSource.backFromAddReviewPage = true;
      this.dataSource.reqHuiPayResult();
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.dataSource.saveData(paramBundle);
  }

  static abstract interface ISchemaMatcher
  {
    public abstract Uri extractUri(String paramString);

    public abstract boolean matchSchema(String paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.activity.HuiPayResultActivity
 * JD-Core Version:    0.6.0
 */