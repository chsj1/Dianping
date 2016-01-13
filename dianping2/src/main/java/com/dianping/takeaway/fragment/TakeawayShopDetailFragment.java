package com.dianping.takeaway.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.widget.NovaFragment;
import com.dianping.map.utils.MapUtils;
import com.dianping.takeaway.activity.TakeawayMenuActivity;
import com.dianping.takeaway.entity.TakeawayNetLoadStatus;
import com.dianping.takeaway.entity.TakeawayShopDetailDataSource;
import com.dianping.takeaway.entity.TakeawayShopDetailDataSource.DataLoadListener;
import com.dianping.takeaway.util.TakeawayUtils;
import com.dianping.takeaway.view.TAOperateItem;
import com.dianping.takeaway.view.TAOperateItem.BORDER;
import com.dianping.takeaway.view.TAStarView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaImageView;

public class TakeawayShopDetailFragment extends NovaFragment
  implements TakeawayShopDetailDataSource.DataLoadListener
{
  TakeawayMenuActivity activity;
  private LinearLayout activityLayout;
  private TextView announceDespView;
  private View announceDivider;
  private View announceView;
  private View commentDivider;
  private TAOperateItem commentView;
  private TextView compensateDespView;
  private TextView compensateTagView;
  private View compensateView;
  private TakeawayShopDetailDataSource detailDataSource;
  private View dividerView;
  private TextView imgCountView;
  LayoutInflater inflater;
  private TextView invoiceDespView;
  private TextView invoiceTagView;
  private View invoiceView;
  private View legalInfoDivider;
  private TAOperateItem legalInfoView;
  private TAOperateItem locationItem;
  private View noImgTipView;
  private TextView onlinePayDespView;
  private TextView onlinePayTagView;
  private View onlinePayView;
  private NovaImageView phoneView;
  private TextView ratingView;
  private TextView saleView;
  private TAOperateItem serveTimeItem;
  private View serviceDivider;
  private View serviceView;
  private NetworkImageView shopImgView;
  private TextView shopNameView;
  private View shopSendInfoView;
  private TAStarView starView;
  private LinearLayout statusView;
  private TextView thirdPartyView;

  private void configActivities()
  {
    if ((this.detailDataSource.activityList == null) || (this.detailDataSource.activityList.length == 0))
    {
      this.activityLayout.setVisibility(8);
      return;
    }
    this.activityLayout.setVisibility(0);
    this.activityLayout.removeAllViews();
    LinearLayout.LayoutParams localLayoutParams1 = new LinearLayout.LayoutParams(-2, -2);
    localLayoutParams1.setMargins(0, ViewUtils.dip2px(getActivity(), 8.0F), 0, 0);
    LinearLayout.LayoutParams localLayoutParams2 = new LinearLayout.LayoutParams(-2, -2);
    localLayoutParams2.setMargins(0, ViewUtils.dip2px(getActivity(), 2.0F), 0, 0);
    DPObject[] arrayOfDPObject = this.detailDataSource.activityList;
    int j = arrayOfDPObject.length;
    int i = 0;
    label120: Object localObject2;
    Object localObject1;
    Object localObject3;
    if (i < j)
    {
      localObject2 = arrayOfDPObject[i];
      if (localObject2 != null)
      {
        localObject1 = "";
        localObject3 = ((DPObject)localObject2).getObject("ActivityButton");
        if (localObject3 != null)
        {
          if (((DPObject)localObject3).getString("Message") != null)
            break label291;
          localObject1 = "";
        }
        label166: localObject2 = ((DPObject)localObject2).getString("ActivityInfo");
        if (localObject2 != null)
          break label302;
        localObject2 = "";
      }
    }
    label291: label302: 
    while (true)
    {
      localObject3 = this.inflater.inflate(R.layout.takeaway_shop_item_activity_tag, null, false);
      TextView localTextView = (TextView)((View)localObject3).findViewById(R.id.tag_icon);
      localTextView.setText((CharSequence)localObject1);
      localTextView.setLayoutParams(localLayoutParams2);
      localObject1 = (TextView)((View)localObject3).findViewById(R.id.tag_content);
      ((TextView)localObject1).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_16));
      ((TextView)localObject1).setTextColor(getResources().getColor(R.color.deep_gray));
      ((TextView)localObject1).setText((CharSequence)localObject2);
      ((View)localObject3).setLayoutParams(localLayoutParams1);
      this.activityLayout.addView((View)localObject3);
      i += 1;
      break label120;
      break;
      localObject1 = ((DPObject)localObject3).getString("Message");
      break label166;
    }
  }

  private void configBasicInfo()
  {
    if (!TextUtils.isEmpty(this.detailDataSource.defaultPicUrl))
    {
      this.shopImgView.setImage(this.detailDataSource.defaultPicUrl);
      this.shopImgView.setGAString("shop_photo");
      this.shopImgView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          TakeawayShopDetailFragment.this.startActivity("dianping://shopphoto?id=" + TakeawayShopDetailFragment.this.detailDataSource.shopId);
          TakeawayShopDetailFragment.this.statisticsEvent("takeaway6", "takeaway6_shopdetails_photoclk", TakeawayShopDetailFragment.this.detailDataSource.shopId, 0);
        }
      });
      this.imgCountView.setText(String.valueOf(this.detailDataSource.picCount));
      this.imgCountView.setVisibility(0);
      this.noImgTipView.setVisibility(8);
      ViewUtils.setVisibilityAndContent(this.shopNameView, this.detailDataSource.shopName);
      this.starView.setScore(this.detailDataSource.starScore);
      ViewUtils.setVisibilityAndContent(this.saleView, this.detailDataSource.saleText);
      ViewUtils.setVisibilityAndContent(this.ratingView, "口味 " + this.detailDataSource.tasteScore + " " + "服务 " + this.detailDataSource.packageScore);
      if ((this.detailDataSource.phoneNums != null) && (this.detailDataSource.phoneNums.length != 0))
        break label249;
      this.phoneView.setVisibility(8);
    }
    while (true)
    {
      configSendInfo();
      configActivities();
      configWorkTimeAndAddress();
      configShopService();
      return;
      this.imgCountView.setVisibility(8);
      this.noImgTipView.setVisibility(0);
      break;
      label249: this.phoneView.setVisibility(0);
      this.phoneView.setGAString("phone");
      this.phoneView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          TakeawayUtils.callShopPhones(TakeawayShopDetailFragment.this.getActivity(), TakeawayShopDetailFragment.this.detailDataSource.phoneNums);
        }
      });
    }
  }

  private void configCommentView()
  {
    if (this.detailDataSource.dpReviewCount == 0)
    {
      this.commentDivider.setVisibility(8);
      this.commentView.setVisibility(8);
      return;
    }
    this.commentView.iconView.setVisibility(8);
    this.commentView.contentText.setText("到店点评");
    this.commentView.expandView.setVisibility(0);
    this.commentView.setBorder(TAOperateItem.BORDER.BOTH);
    this.commentView.expandText.setText(this.detailDataSource.dpReviewCount + "条");
    this.commentView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        GAHelper.instance().contextStatisticsEvent(TakeawayShopDetailFragment.this.activity, "shopcomment", TakeawayShopDetailFragment.this.detailDataSource.getGAUserInfo(), "tap");
        paramView = Uri.parse("dianping://review").buildUpon().appendQueryParameter("id", TakeawayShopDetailFragment.this.detailDataSource.shopId).appendQueryParameter("shopname", TakeawayShopDetailFragment.this.detailDataSource.shopName).toString();
        TakeawayShopDetailFragment.this.startActivity(paramView);
      }
    });
    this.commentDivider.setVisibility(0);
    this.commentView.setVisibility(0);
  }

  private void configLegalInfoView()
  {
    boolean bool = TextUtils.isEmpty(this.detailDataSource.legalInfo);
    if ((this.detailDataSource.legalImgs == null) || (this.detailDataSource.legalImgs.length == 0));
    for (int i = 1; (bool) && (i != 0); i = 0)
    {
      this.legalInfoDivider.setVisibility(8);
      this.legalInfoView.setVisibility(8);
      return;
    }
    this.legalInfoView.iconView.setVisibility(8);
    this.legalInfoView.contentText.setText("商户营业资质");
    this.legalInfoView.setBorder(TAOperateItem.BORDER.BOTH);
    if (bool)
    {
      this.legalInfoView.expandText.setVisibility(8);
      if (i == 0)
        break label181;
      this.legalInfoView.expandView.setVisibility(8);
    }
    while (true)
    {
      this.legalInfoDivider.setVisibility(0);
      this.legalInfoView.setVisibility(0);
      return;
      this.legalInfoView.expandView.setVisibility(0);
      this.legalInfoView.expandText.setText(this.detailDataSource.legalInfo);
      break;
      label181: this.legalInfoView.expandView.setVisibility(0);
      this.legalInfoView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          GAHelper.instance().contextStatisticsEvent(TakeawayShopDetailFragment.this.activity, "qualifications", null, "tap");
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawaylegalinfo"));
          paramView.putExtra("legalimgs", TakeawayShopDetailFragment.this.detailDataSource.legalImgs);
          TakeawayShopDetailFragment.this.startActivity(paramView);
        }
      });
    }
  }

  private void configSendInfo()
  {
    if (!TextUtils.isEmpty(this.detailDataSource.arrivedInterval))
      ((TextView)this.shopSendInfoView.findViewById(R.id.timeText)).setText(this.detailDataSource.arrivedInterval);
    ((TextView)this.shopSendInfoView.findViewById(R.id.delfeeText)).setText(this.detailDataSource.minAmount);
    if (!this.detailDataSource.isDeliveryFree)
      ((ImageView)this.shopSendInfoView.findViewById(R.id.delfeeImg)).setImageResource(R.drawable.wm_shopdetail_icon_deliveryfee);
    ((TextView)this.shopSendInfoView.findViewById(R.id.sendfeeText)).setText(this.detailDataSource.minFee);
    if (TextUtils.isEmpty(this.detailDataSource.arrivedInterval))
    {
      this.shopSendInfoView.findViewById(R.id.timeLayout).setVisibility(8);
      this.shopSendInfoView.findViewById(R.id.holdLayout).setVisibility(0);
      ((LinearLayout)this.shopSendInfoView.findViewById(R.id.delfeeLayout)).setGravity(19);
      ((LinearLayout)this.shopSendInfoView.findViewById(R.id.sendfeeLayout)).setGravity(17);
    }
  }

  private void configShopService()
  {
    int i = 0;
    int i3;
    int j;
    int k;
    int m;
    label97: String str1;
    int n;
    int i1;
    int i2;
    String str2;
    if (!TextUtils.isEmpty(this.detailDataSource.announceText))
    {
      i = 1;
      this.announceDespView.setText(this.detailDataSource.announceText);
      this.announceView.setVisibility(0);
      this.announceDivider.setVisibility(0);
      if ((this.detailDataSource.extraServices == null) || (this.detailDataSource.extraServices.length == 0))
        break label472;
      i3 = 1;
      i = 0;
      j = 0;
      k = 0;
      localObject = this.detailDataSource.extraServices;
      int i4 = localObject.length;
      m = 0;
      if (m >= i4)
        break label327;
      str1 = localObject[m];
      DPObject localDPObject = str1.getObject("ActivityButton");
      str1 = str1.getString("ActivityInfo");
      n = k;
      i1 = j;
      i2 = i;
      if (localDPObject != null)
      {
        str2 = localDPObject.getString("Message");
        switch (localDPObject.getInt("Type"))
        {
        default:
          i2 = i;
          i1 = j;
          n = k;
        case 1:
        case 2:
        case 3:
        }
      }
    }
    while (true)
    {
      m += 1;
      k = n;
      j = i1;
      i = i2;
      break label97;
      this.announceView.setVisibility(8);
      this.announceDivider.setVisibility(8);
      break;
      i2 = 1;
      this.onlinePayTagView.setText(str2);
      this.onlinePayDespView.setText(str1);
      n = k;
      i1 = j;
      continue;
      n = 1;
      this.compensateTagView.setText(str2);
      this.compensateDespView.setText(str1);
      i1 = j;
      i2 = i;
      continue;
      i1 = 1;
      this.invoiceTagView.setText(str2);
      this.invoiceDespView.setText(str1);
      n = k;
      i2 = i;
    }
    label327: Object localObject = this.onlinePayView;
    if (i != 0)
    {
      m = 0;
      ((View)localObject).setVisibility(m);
      localObject = this.invoiceView;
      if (j == 0)
        break label451;
      m = 0;
      label359: ((View)localObject).setVisibility(m);
      localObject = this.compensateView;
      if (k == 0)
        break label458;
      m = 0;
      label378: ((View)localObject).setVisibility(m);
      localObject = this.dividerView;
      if ((i == 0) && (j == 0) && (k == 0))
        break label465;
      i = 0;
      label407: ((View)localObject).setVisibility(i);
      i = i3;
      label417: if (i == 0)
        break label511;
    }
    label451: label458: label465: label472: label511: for (i = 0; ; i = 8)
    {
      this.serviceView.setVisibility(i);
      this.serviceDivider.setVisibility(i);
      return;
      m = 8;
      break;
      m = 8;
      break label359;
      m = 8;
      break label378;
      i = 8;
      break label407;
      this.onlinePayView.setVisibility(8);
      this.invoiceView.setVisibility(8);
      this.compensateView.setVisibility(8);
      this.dividerView.setVisibility(8);
      break label417;
    }
  }

  private void configThirdPartyView()
  {
    if (TextUtils.isEmpty(this.detailDataSource.thirdPartyName))
    {
      this.thirdPartyView.setVisibility(8);
      return;
    }
    this.thirdPartyView.setText(Html.fromHtml("本商户由<font color=#333333>" + this.detailDataSource.thirdPartyName + "</font>" + "提供售后服务"));
    this.thirdPartyView.setVisibility(0);
  }

  private void configWorkTimeAndAddress()
  {
    this.serveTimeItem.expandView.setVisibility(8);
    this.serveTimeItem.contentText.setSingleLine(true);
    this.serveTimeItem.contentText.setEllipsize(TextUtils.TruncateAt.END);
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    while (i < this.detailDataSource.serveTimeList.length)
    {
      localStringBuilder.append(this.detailDataSource.serveTimeList[i]);
      if (i != this.detailDataSource.serveTimeList.length - 1)
        localStringBuilder.append("  ");
      i += 1;
    }
    localStringBuilder.append(" 外送");
    this.serveTimeItem.contentText.setText(localStringBuilder.toString());
    this.serveTimeItem.iconView.setImageResource(R.drawable.wm_shopdetail_icon_senddinnertime);
    this.locationItem.contentText.setText(this.detailDataSource.address);
    this.locationItem.expandView.setVisibility(0);
    this.locationItem.setBorder(TAOperateItem.BORDER.BOTTOM);
    this.locationItem.iconView.setImageResource(R.drawable.wm_shoplist_titlebar_adress_icon_location);
    this.locationItem.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new DPObject().edit().putDouble("Latitude", Double.parseDouble(TakeawayShopDetailFragment.this.detailDataSource.shopLat)).putDouble("Longitude", Double.parseDouble(TakeawayShopDetailFragment.this.detailDataSource.shopLng)).putInt("ID", Integer.parseInt(TakeawayShopDetailFragment.this.detailDataSource.shopId)).putString("Address", TakeawayShopDetailFragment.this.detailDataSource.address).generate();
        MapUtils.gotoNavi(TakeawayShopDetailFragment.this.getActivity(), paramView);
        GAHelper.instance().contextStatisticsEvent(TakeawayShopDetailFragment.this.activity, "address", null, "tap");
      }
    });
  }

  public static TakeawayShopDetailFragment newInstance(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    TakeawayShopDetailFragment localTakeawayShopDetailFragment = new TakeawayShopDetailFragment();
    Bundle localBundle = new Bundle();
    localBundle.putString("shopid", paramString1);
    localBundle.putString("shopname", paramString2);
    localBundle.putString("lat", paramString3);
    localBundle.putString("lng", paramString4);
    localTakeawayShopDetailFragment.setArguments(localBundle);
    return localTakeawayShopDetailFragment;
  }

  private void setupBasicInfoView(View paramView)
  {
    this.shopImgView = ((NetworkImageView)paramView.findViewById(R.id.shop_img));
    this.imgCountView = ((TextView)paramView.findViewById(R.id.img_count));
    this.noImgTipView = paramView.findViewById(R.id.no_img);
    this.shopNameView = ((TextView)paramView.findViewById(R.id.shop_name));
    this.starView = ((TAStarView)paramView.findViewById(R.id.star));
    this.saleView = ((TextView)paramView.findViewById(R.id.sale_desp));
    this.ratingView = ((TextView)paramView.findViewById(R.id.reating_desp));
    this.phoneView = ((NovaImageView)paramView.findViewById(R.id.shop_phone));
    this.shopSendInfoView = paramView.findViewById(R.id.send_info);
    this.activityLayout = ((LinearLayout)paramView.findViewById(R.id.activity_layout));
    this.serveTimeItem = ((TAOperateItem)paramView.findViewById(R.id.serve_time));
    this.locationItem = ((TAOperateItem)paramView.findViewById(R.id.shop_location));
    this.serviceDivider = paramView.findViewById(R.id.service_divider);
    this.serviceView = paramView.findViewById(R.id.service_layout);
    this.announceDivider = paramView.findViewById(R.id.announce_divider);
    this.announceView = paramView.findViewById(R.id.announce_layout);
    this.announceDespView = ((TextView)paramView.findViewById(R.id.announce_desp));
    this.dividerView = paramView.findViewById(R.id.divider_view);
    this.onlinePayView = paramView.findViewById(R.id.onlinepay_layout);
    this.onlinePayTagView = ((TextView)paramView.findViewById(R.id.onlinepay_icon));
    this.onlinePayDespView = ((TextView)paramView.findViewById(R.id.onlinepay_desp));
    this.invoiceView = paramView.findViewById(R.id.invoice_layout);
    this.invoiceTagView = ((TextView)paramView.findViewById(R.id.invoice_icon));
    this.invoiceDespView = ((TextView)paramView.findViewById(R.id.invoice_desp));
    this.compensateView = paramView.findViewById(R.id.compensate_layout);
    this.compensateTagView = ((TextView)paramView.findViewById(R.id.compensate_icon));
    this.compensateDespView = ((TextView)paramView.findViewById(R.id.compensate_desp));
    this.legalInfoDivider = paramView.findViewById(R.id.legalinfo_divider);
    this.legalInfoView = ((TAOperateItem)paramView.findViewById(R.id.legalinfo_view));
    this.commentDivider = paramView.findViewById(R.id.comment_divider);
    this.commentView = ((TAOperateItem)paramView.findViewById(R.id.comment_view));
    this.thirdPartyView = ((TextView)paramView.findViewById(R.id.third_party_view));
  }

  private void setupStatusView(Status paramStatus)
  {
    this.statusView.removeAllViews();
    RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
    switch (7.$SwitchMap$com$dianping$takeaway$fragment$TakeawayShopDetailFragment$Status[paramStatus.ordinal()])
    {
    default:
    case 1:
    case 2:
    }
    while (true)
    {
      this.statusView.setVisibility(0);
      return;
      paramStatus = this.inflater.inflate(R.layout.loading_item_fullscreen, null);
      this.statusView.addView(paramStatus, localLayoutParams);
      continue;
      paramStatus = this.inflater.inflate(R.layout.takeaway_empty_item, null);
      ((TextView)paramStatus.findViewById(16908308)).setText("网络不给力哦");
      Button localButton = (Button)paramStatus.findViewById(R.id.btn_change);
      localButton.setText("重新加载");
      localButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          TakeawayShopDetailFragment.this.setupStatusView(TakeawayShopDetailFragment.Status.INITIAL_LOADING);
          TakeawayShopDetailFragment.this.detailDataSource.loadBasicData();
        }
      });
      this.statusView.addView(paramStatus, localLayoutParams);
    }
  }

  public void loadBasicDataFinsh(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, Object paramObject)
  {
    switch (7.$SwitchMap$com$dianping$takeaway$entity$TakeawayNetLoadStatus[paramTakeawayNetLoadStatus.ordinal()])
    {
    default:
      return;
    case 1:
      configBasicInfo();
      configCommentView();
      configLegalInfoView();
      configThirdPartyView();
      this.statusView.setVisibility(8);
      return;
    case 2:
    }
    setupStatusView(Status.NETWORK_ERROR);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    setupStatusView(Status.INITIAL_LOADING);
    this.detailDataSource.loadBasicData();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.activity = ((TakeawayMenuActivity)getActivity());
    this.inflater = LayoutInflater.from(this.activity);
    this.detailDataSource = new TakeawayShopDetailDataSource(this);
    this.detailDataSource.setDataLoadListener(this);
    if (paramBundle != null)
    {
      this.detailDataSource.shopId = paramBundle.getString("shopid");
      this.detailDataSource.shopName = paramBundle.getString("shopname");
      this.detailDataSource.lat = paramBundle.getString("lat");
      this.detailDataSource.lng = paramBundle.getString("lng");
      return;
    }
    paramBundle = getArguments();
    this.detailDataSource.shopId = paramBundle.getString("shopid");
    this.detailDataSource.shopName = paramBundle.getString("shopname");
    this.detailDataSource.lat = paramBundle.getString("lat");
    this.detailDataSource.lng = paramBundle.getString("lng");
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.takeaway_shopdetail_fragment, null);
    this.statusView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.status_view));
    setupBasicInfoView(paramLayoutInflater);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    this.detailDataSource.onDestroy();
    super.onDestroy();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("shopid", this.detailDataSource.shopId);
    paramBundle.putString("shopname", this.detailDataSource.shopName);
    paramBundle.putString("lat", this.detailDataSource.lat);
    paramBundle.putString("lng", this.detailDataSource.lng);
    super.onSaveInstanceState(paramBundle);
  }

  public static enum Status
  {
    static
    {
      $VALUES = new Status[] { INITIAL_LOADING, NETWORK_ERROR };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.fragment.TakeawayShopDetailFragment
 * JD-Core Version:    0.6.0
 */