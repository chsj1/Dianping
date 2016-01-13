package com.dianping.tuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.tuan.widget.UnusableCouponItem;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaImageView;
import java.util.ArrayList;

public class PayUnusableCouponActivity extends DPActivity
{
  private static final int MAX_SHOW_COUNT = 5;
  private final int COUPON_TYPE_DIANPING = 0;
  private final int COUPON_TYPE_SHOP = 1;
  private final int COUPON_TYPE_UNKNOWN = -1;
  private NovaImageView closeBtn;
  protected UnusableCouponAdapter couponAdapter;
  private View couponBg;
  private ListView couponListView;
  private TextView couponTitleView;
  private LinearLayout listLayer;
  private int mCouponType = -1;
  private ArrayList<DPObject> unusableCouponList;
  private ArrayList<DPObject> unusableShopCouponList;

  private void initView()
  {
    setContentView(R.layout.pay_unusable_coupon_activity);
    this.closeBtn = ((NovaImageView)findViewById(R.id.close));
    this.closeBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        PayUnusableCouponActivity.this.finish();
        PayUnusableCouponActivity.this.overridePendingTransition(0, 0);
      }
    });
    this.couponBg = findViewById(R.id.unusable_coupon_bg);
    this.couponBg.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        PayUnusableCouponActivity.this.finish();
        PayUnusableCouponActivity.this.overridePendingTransition(0, 0);
      }
    });
    this.couponTitleView = ((TextView)findViewById(R.id.coupon_title));
    if (this.mCouponType == 0)
    {
      this.couponTitleView.setText("点评抵用券");
      this.couponAdapter = new UnusableCouponAdapter(this.unusableCouponList);
    }
    while (true)
    {
      this.couponListView = ((ListView)findViewById(R.id.unusable_coupon_list));
      this.listLayer = ((LinearLayout)findViewById(R.id.content));
      this.couponListView.setAdapter(this.couponAdapter);
      this.listLayer.setVisibility(4);
      Animation localAnimation = AnimationUtils.loadAnimation(this, R.anim.popup_show);
      this.couponBg.startAnimation(localAnimation);
      this.couponListView.postDelayed(new Runnable()
      {
        public void run()
        {
          PayUnusableCouponActivity.this.adjustCouponList(PayUnusableCouponActivity.this.couponAdapter);
          PayUnusableCouponActivity.this.listLayer.setVisibility(0);
          Animation localAnimation = AnimationUtils.loadAnimation(PayUnusableCouponActivity.this, R.anim.popup_up_in);
          PayUnusableCouponActivity.this.listLayer.startAnimation(localAnimation);
        }
      }
      , 100L);
      return;
      if (this.mCouponType == 1)
      {
        this.couponTitleView.setText("商家抵用券");
        this.couponAdapter = new UnusableCouponAdapter(this.unusableShopCouponList);
        continue;
      }
      this.couponTitleView.setText("抵用券");
      this.couponAdapter = new UnusableCouponAdapter(null);
    }
  }

  protected void adjustCouponList(UnusableCouponAdapter paramUnusableCouponAdapter)
  {
    LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)this.couponListView.getLayoutParams();
    if (paramUnusableCouponAdapter.getCount() < 5)
      if (localLayoutParams.height != -2)
      {
        localLayoutParams.height = -2;
        this.couponListView.setLayoutParams(localLayoutParams);
      }
    do
    {
      return;
      paramUnusableCouponAdapter = this.couponListView.getChildAt(0);
    }
    while (paramUnusableCouponAdapter == null);
    localLayoutParams.height = ((paramUnusableCouponAdapter.getMeasuredHeight() + this.couponListView.getDividerHeight()) * 5);
    this.couponListView.setLayoutParams(localLayoutParams);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.unusableCouponList = getIntent().getParcelableArrayListExtra("unusableCouponList");
    this.unusableShopCouponList = getIntent().getParcelableArrayListExtra("unusableShopCouponList");
    if (this.unusableCouponList != null)
      this.mCouponType = 0;
    while (true)
    {
      initView();
      return;
      if (this.unusableShopCouponList == null)
        continue;
      this.mCouponType = 1;
    }
  }

  class UnusableCouponAdapter extends BasicAdapter
  {
    private ArrayList<DPObject> unusableCouponList;

    public UnusableCouponAdapter()
    {
      Object localObject;
      this.unusableCouponList = localObject;
    }

    public int getCount()
    {
      if (this.unusableCouponList == null)
        return 0;
      return this.unusableCouponList.size();
    }

    public DPObject getItem(int paramInt)
    {
      return (DPObject)this.unusableCouponList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((paramView instanceof UnusableCouponItem));
      for (paramView = (UnusableCouponItem)paramView; ; paramView = null)
      {
        Object localObject = paramView;
        if (paramView == null)
          localObject = (UnusableCouponItem)LayoutInflater.from(PayUnusableCouponActivity.this).inflate(R.layout.unusable_coupon_item, paramViewGroup, false);
        ((UnusableCouponItem)localObject).setContent(getItem(paramInt));
        return localObject;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.PayUnusableCouponActivity
 * JD-Core Version:    0.6.0
 */