package com.dianping.tuan.fragment;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.tuan.widget.CouponQRcodeRequest;
import com.dianping.util.TextUtils;
import com.dianping.util.network.NetworkUtils;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;

public class UnusedCouponListFragment extends BaseTuanPtrListFragment
  implements RequestHandler<MApiRequest, MApiResponse>, PullToRefreshListView.OnRefreshListener, Animation.AnimationListener
{
  private static final String KEY_LAST_RECODE = "last_record";
  protected UnusedCouponListFragment.Adapter adapter;
  protected MApiRequest couponHintReq;
  boolean disableQRGrouponVerify;
  protected MApiRequest expiredCouponReq;
  protected View footView;
  protected boolean hasExpired;
  protected DPObject lastResult;
  protected String mAuthorKey;
  private String mLastRecord;
  private Animation mPopupInAnim;
  private Animation mPopupOutAnim;
  protected String mQRcodeString;
  protected boolean mQrCodeLoaded;
  protected BroadcastReceiver mReceiver = new UnusedCouponListFragment.1(this);
  private DPObject mRefundHint;
  protected SharedPreferences preferences;

  private void hideNotification()
  {
    this.notificationView.clearAnimation();
    if (this.notificationView.getVisibility() == 0)
      this.notificationView.startAnimation(this.mPopupOutAnim);
  }

  private void showNotification()
  {
    this.notificationView.clearAnimation();
    if (this.notificationView.getVisibility() != 0)
      this.notificationView.startAnimation(this.mPopupInAnim);
  }

  private void showOfflineHeader()
  {
    this.notificationView.setText("请连接网络，以查看最新团购券");
    this.notificationView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    this.notificationView.setClickable(false);
    if (8 == this.notificationView.getVisibility())
      this.notificationView.setVisibility(0);
  }

  private void updateRefundHint(DPObject paramDPObject)
  {
    this.mRefundHint = paramDPObject;
    if (this.mRefundHint == null);
    do
    {
      do
      {
        return;
        if (this.mLastRecord != null)
          continue;
        paramDPObject = this.mRefundHint.getString("Record");
        if (TextUtils.isEmpty(paramDPObject))
          continue;
        this.mLastRecord = paramDPObject;
        this.preferences.edit().putString("last_record", paramDPObject).commit();
      }
      while (!this.mRefundHint.getBoolean("Show"));
      this.notificationView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_white_arrow), null);
      this.notificationView.setClickable(true);
      paramDPObject = this.mRefundHint.getString("Msg");
    }
    while (TextUtils.isEmpty(paramDPObject));
    this.notificationView.setText(TextUtils.jsonParseText(paramDPObject));
    showNotification();
  }

  protected void loadExpiredCoupons()
  {
    if (this.expiredCouponReq != null)
      mapiService().abort(this.expiredCouponReq, this, true);
    this.expiredCouponReq = mapiGet(this, "http://app.t.dianping.com/couponlistgn.bin?token=" + accountService().token() + "&filter=4" + "&start=" + 0, CacheType.CRITICAL);
    mapiService().exec(this.expiredCouponReq, this);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.adapter = new UnusedCouponListFragment.Adapter(this, getActivity());
    this.listView.setAdapter(this.adapter);
    new CouponQRcodeRequest(this, new UnusedCouponListFragment.4(this)).requestQrCodeInfo();
  }

  public void onAnimationEnd(Animation paramAnimation)
  {
    if (paramAnimation == this.mPopupInAnim);
    do
      return;
    while (paramAnimation != this.mPopupOutAnim);
    this.notificationView.setVisibility(8);
  }

  public void onAnimationRepeat(Animation paramAnimation)
  {
  }

  public void onAnimationStart(Animation paramAnimation)
  {
    if (paramAnimation == this.mPopupInAnim)
      this.notificationView.setVisibility(0);
    do
      return;
    while (paramAnimation != this.mPopupOutAnim);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.disableQRGrouponVerify = ConfigHelper.disableQRGrouponVerify;
    paramBundle = new IntentFilter("tuan:order_refund_status_changed");
    paramBundle.addAction("android.net.conn.CONNECTIVITY_CHANGE");
    registerReceiver(this.mReceiver, paramBundle);
    this.mPopupInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.notification_popup_in);
    this.mPopupInAnim.setAnimationListener(this);
    this.mPopupOutAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.notification_popup_out);
    this.mPopupOutAnim.setAnimationListener(this);
  }

  public void onDestroy()
  {
    super.onDestroy();
    this.adapter.cancelLoad();
    try
    {
      unregisterReceiver(this.mReceiver);
      this.mReceiver = null;
      return;
    }
    catch (Exception localException)
    {
    }
  }

  protected void onPullToRefresh()
  {
    this.adapter.reset();
  }

  public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
  {
    this.adapter.pullToReset(true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.expiredCouponReq == paramMApiRequest)
      this.expiredCouponReq = null;
    do
      return;
    while (this.couponHintReq != paramMApiRequest);
    this.couponHintReq = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.expiredCouponReq == paramMApiRequest)
    {
      this.expiredCouponReq = null;
      if (isDPObjectof(paramMApiResponse.result()))
      {
        if (DPObjectUtils.isResultListEmpty((DPObject)paramMApiResponse.result()))
          break label55;
        this.footView.setVisibility(0);
        this.hasExpired = true;
      }
    }
    label55: 
    do
    {
      return;
      this.footView.setVisibility(8);
      this.hasExpired = false;
      return;
    }
    while (this.couponHintReq != paramMApiRequest);
    this.couponHintReq = null;
    updateRefundHint((DPObject)paramMApiResponse.result());
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    this.listView.setDivider(null);
    this.listView.setDividerHeight(0);
    this.listView.setBackgroundResource(R.color.common_bk_color);
    this.listView.setSelector(new ColorDrawable(0));
    this.listView.setOnItemClickListener(null);
    this.listView.setOnRefreshListener(this);
    this.preferences = preferences(getActivity());
    this.mLastRecord = this.preferences.getString("last_record", null);
    this.notificationView.setOnClickListener(new UnusedCouponListFragment.2(this));
    if (!NetworkUtils.isConnectingToInternet(getActivity()))
      showOfflineHeader();
    while (true)
    {
      this.footView = LayoutInflater.from(getActivity()).inflate(R.layout.go_expired_coupon, this.listView, false);
      this.footView.setVisibility(8);
      this.footView.findViewById(R.id.expiredcoupon).setOnClickListener(new UnusedCouponListFragment.3(this));
      this.listView.setFooterDividersEnabled(false);
      this.listView.addFooterView(this.footView, null, false);
      return;
      this.notificationView.setVisibility(8);
    }
  }

  public void requestCouponHint()
  {
    if (this.couponHintReq != null)
      return;
    String str2 = accountService().token();
    if (this.mLastRecord == null);
    for (String str1 = ""; ; str1 = this.mLastRecord)
    {
      this.couponHintReq = mapiPost(this, "http://app.t.dianping.com/refundnotificationgn.bin", new String[] { "token", str2, "lastrecord", str1 });
      mapiService().exec(this.couponHintReq, this);
      return;
    }
  }

  protected void sendMyCouponBroadCast(int paramInt)
  {
    if (isAdded())
    {
      Intent localIntent = new Intent("com.dianping.action.MYCOUPON_REFRESH_ACTION");
      localIntent.putExtra("coupon_count", paramInt);
      getActivity().sendBroadcast(localIntent);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.fragment.UnusedCouponListFragment
 * JD-Core Version:    0.6.0
 */