package com.dianping.takeaway.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.dianping.base.widget.TitleBar;
import com.dianping.takeaway.entity.TakeawayListAdapter;
import com.dianping.takeaway.entity.TakeawayOverRangeDataSource;
import com.dianping.takeaway.entity.TakeawayOverRangeListAdapter;
import com.dianping.takeaway.entity.TakeawaySampleShoplistDataSource;
import com.dianping.takeaway.entity.TakeawaySampleShoplistDataSource.ResultStatus;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import org.json.JSONException;
import org.json.JSONObject;

public class TakeawayOverRangeActivity extends TakeawaySampleShopListActivity
{
  private View bottomButtonView;
  private Button changeAddressBtn;
  private Button nearbyShoplistBtn;
  private TakeawayOverRangeDataSource overRangeDataSource;
  private TakeawayOverRangeListAdapter overRangeListAdapter;
  private View topTipView;
  private View topTitleView;

  private void initTipView()
  {
    this.topTipView = findViewById(R.id.takeaway_over_range_top_tip_view);
    this.topTitleView = findViewById(R.id.takeaway_over_range_top_title_view);
    this.bottomButtonView = findViewById(R.id.takeaway_over_range_bottom_button_view);
    this.changeAddressBtn = ((Button)findViewById(R.id.takeaway_over_range_button_change_address));
    this.nearbyShoplistBtn = ((Button)findViewById(R.id.takeaway_over_range_button_nearby_shoplist));
    this.changeAddressBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new StringBuilder("dianping://takeawayaddress");
        paramView.append("?source=").append(3);
        paramView.append("&shopid=").append(TakeawayOverRangeActivity.this.overRangeDataSource.overRangeShopID);
        paramView.append("&queryid=").append(TakeawayOverRangeActivity.this.overRangeDataSource.queryId());
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView.toString()));
        TakeawayOverRangeActivity localTakeawayOverRangeActivity = TakeawayOverRangeActivity.this;
        TakeawayOverRangeActivity.this.overRangeDataSource.getClass();
        localTakeawayOverRangeActivity.startActivityForResult(paramView, 5);
        paramView = TakeawayOverRangeActivity.this.overRangeDataSource.getGAUserInfo();
        GAHelper.instance().contextStatisticsEvent(TakeawayOverRangeActivity.this, "ood_address", paramView, "tap");
      }
    });
    this.nearbyShoplistBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawayshoplist"));
        paramView.putExtra("address", TakeawayOverRangeActivity.this.overRangeDataSource.curAddress);
        if ((!TextUtils.isEmpty(TakeawayOverRangeActivity.this.overRangeDataSource.lat)) && (!TextUtils.isEmpty(TakeawayOverRangeActivity.this.overRangeDataSource.lng)))
        {
          paramView.putExtra("lat", String.valueOf(TakeawayOverRangeActivity.this.overRangeDataSource.lat));
          paramView.putExtra("lng", String.valueOf(TakeawayOverRangeActivity.this.overRangeDataSource.lng));
        }
        paramView.putExtra("onlyfinish", true);
        TakeawayOverRangeActivity.this.startActivity(paramView);
        paramView = TakeawayOverRangeActivity.this.overRangeDataSource.getGAUserInfo();
        GAHelper.instance().contextStatisticsEvent(TakeawayOverRangeActivity.this, "ood_shop", paramView, "tap");
        TakeawayOverRangeActivity.this.finish();
      }
    });
  }

  private void updateTipView(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramBoolean1)
    {
      this.topTipView.setVisibility(0);
      this.topTitleView.setVisibility(0);
    }
    while (paramBoolean2)
    {
      this.bottomButtonView.setVisibility(0);
      return;
      this.topTipView.setVisibility(8);
      this.topTitleView.setVisibility(8);
    }
    this.bottomButtonView.setVisibility(4);
  }

  protected void fetchParams(Bundle paramBundle)
  {
    super.fetchParams(paramBundle);
    if (paramBundle == null)
    {
      this.overRangeDataSource.overRangeShopName = getStringParam("shopname");
      this.overRangeDataSource.overRangeShopID = getStringParam("shopid");
      try
      {
        paramBundle = new JSONObject(getStringParam("content"));
        this.overRangeDataSource.overRangeShopCateId = paramBundle.getInt("categoryid");
        this.overRangeDataSource.overRangeAddress = paramBundle.getString("address");
        this.overRangeDataSource.overRangeErrorMsg = paramBundle.getString("errormsg");
        return;
      }
      catch (JSONException paramBundle)
      {
        paramBundle.printStackTrace();
        return;
      }
    }
    this.overRangeDataSource.onRestoreInstanceState(paramBundle);
  }

  protected TakeawaySampleShoplistDataSource getDataSource()
  {
    if (this.overRangeDataSource == null)
      this.overRangeDataSource = new TakeawayOverRangeDataSource(this);
    return this.overRangeDataSource;
  }

  protected String getItemClickElementId()
  {
    return "ood_shoplist";
  }

  protected TakeawayListAdapter getListAdapter()
  {
    if (this.overRangeListAdapter == null)
      this.overRangeListAdapter = new TakeawayOverRangeListAdapter(this, this);
    return this.overRangeListAdapter;
  }

  public String getPageName()
  {
    return "takeawayoverrange";
  }

  protected void initFilterBarView()
  {
  }

  protected void initTitleBarView()
  {
    super.getTitleBar().setTitle(this.overRangeDataSource.overRangeShopName);
  }

  protected void initViews()
  {
    super.initViews();
    initTipView();
  }

  public void loadShopListFinsh(TakeawaySampleShoplistDataSource.ResultStatus paramResultStatus, Object paramObject)
  {
    switch (3.$SwitchMap$com$dianping$takeaway$entity$TakeawaySampleShoplistDataSource$ResultStatus[paramResultStatus.ordinal()])
    {
    default:
      return;
    case 1:
      super.resetPageVisibility(true);
      updateTipView(false, true);
      this.overRangeListAdapter.setShopList(this.overRangeDataSource);
      return;
    case 2:
      super.resetPageVisibility(true);
      updateTipView(true, true);
      this.overRangeListAdapter.setShopList(this.overRangeDataSource);
      return;
    case 3:
    case 4:
    }
    updateTipView(false, false);
    updateErrorView("网络不给力哦", paramResultStatus);
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.overRangeDataSource.getClass();
    if ((paramInt1 == 5) && (paramInt2 == -1))
    {
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawaydishlist"));
      localIntent.putExtra("lat", this.overRangeDataSource.lat);
      localIntent.putExtra("lng", this.overRangeDataSource.lng);
      localIntent.putExtra("shopid", this.overRangeDataSource.overRangeShopID);
      localIntent.putExtra("shopname", this.overRangeDataSource.overRangeShopName);
      localIntent.putExtra("source", 2);
      localIntent.putExtra("address", this.overRangeDataSource.curAddress);
      localIntent.putExtra("queryid", this.overRangeDataSource.queryId());
      startActivity(localIntent);
      finish();
    }
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onDestroy()
  {
    this.overRangeDataSource.onDestroy();
    super.onDestroy();
  }

  protected void onSetContentView()
  {
    super.setContentView(R.layout.takeaway_over_range_activity);
  }

  protected void updateFilterBarView()
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.activity.TakeawayOverRangeActivity
 * JD-Core Version:    0.6.0
 */