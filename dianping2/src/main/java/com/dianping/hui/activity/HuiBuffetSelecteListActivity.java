package com.dianping.hui.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.hui.entity.Buffet;
import com.dianping.hui.entity.HuiBuffetDataSource;
import com.dianping.hui.entity.HuiBuffetDataSource.HuiBuffetDataLoaderListener;
import com.dianping.hui.entity.HuiBuffetDataSource.ScrollListener;
import com.dianping.hui.entity.HuiMapiStatus;
import com.dianping.hui.entity.Message;
import com.dianping.hui.util.HuiUtils;
import com.dianping.hui.view.HuiBuffetItem;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.view.GAHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HuiBuffetSelecteListActivity extends NovaActivity
  implements HuiBuffetDataSource.HuiBuffetDataLoaderListener, HuiBuffetDataSource.ScrollListener
{
  private static final int EMPTY_LIST = 0;
  private static final int REQUEST_FAIL = 1;
  private static final int REQUEST_START = 3;
  private static final int REQUEST_SUCCESS = 2;
  public View.OnClickListener addClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = (Buffet)paramView.getTag();
      HuiBuffetSelecteListActivity.this.checkCount(paramView);
    }
  };
  private TextView emptyTV;
  private FrameLayout emptyView;
  public View.OnClickListener inputCompleteListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = (Buffet)paramView.getTag();
      HuiBuffetSelecteListActivity.this.checkCount(paramView);
    }
  };
  private View loadingLayout;
  private HuiBuffetDataSource mBuffetDataSource;
  private LinearLayout mScrollList;
  private ScrollView mScrollView;
  private Button mSubmitBtn;
  private View mSubmitLayout;
  public View.OnClickListener removeClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      HuiBuffetSelecteListActivity.this.updateSubmitBtn();
    }
  };
  private String requestParams;

  private void checkCount(Buffet paramBuffet)
  {
    if (paramBuffet == null)
      return;
    if (paramBuffet.count >= paramBuffet.maxCount)
      showShortToast("单品最多购买" + paramBuffet.maxCount + "份");
    updateSubmitBtn();
  }

  private void inflateListView()
  {
    this.mScrollList.removeAllViews();
    Iterator localIterator = this.mBuffetDataSource.buffetList.iterator();
    if (localIterator.hasNext())
    {
      Buffet localBuffet = (Buffet)localIterator.next();
      HuiBuffetItem localHuiBuffetItem = (HuiBuffetItem)LayoutInflater.from(this).inflate(R.layout.hui_buffet_item, null, false);
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
      localLayoutParams.setMargins(0, 0, 0, ViewUtils.dip2px(this, ViewUtils.dip2px(this, 5.0F)));
      localHuiBuffetItem.setLayoutParams(localLayoutParams);
      if (this.mBuffetDataSource.buffetList.size() == 1);
      for (boolean bool = true; ; bool = false)
      {
        localHuiBuffetItem.bindView(this, localBuffet, bool);
        this.mScrollList.addView(localHuiBuffetItem);
        break;
      }
    }
    updateSubmitBtn();
  }

  private void initData()
  {
    this.mBuffetDataSource = new HuiBuffetDataSource(this);
    this.mBuffetDataSource.setHuiBuffetDataLoaderListener(this);
    this.mBuffetDataSource.shopId = getStringParam("shopid");
    this.mBuffetDataSource.shopName = getStringParam("shopname");
    this.mBuffetDataSource.bizOrderId = getStringParam("bizorderid");
    this.mBuffetDataSource.bizOrderType = getIntParam("bizordertype");
    TitleBar localTitleBar = super.getTitleBar();
    if (!TextUtils.isEmpty(this.mBuffetDataSource.shopName));
    for (String str = this.mBuffetDataSource.shopName; ; str = "自助餐")
    {
      localTitleBar.setTitle(str);
      if (getIntent() == null)
        break;
      this.requestParams = getIntent().getDataString().split("huibuffetlist")[1];
      this.mBuffetDataSource.requestParams = this.requestParams;
      this.mBuffetDataSource.getBuffetList(this.mBuffetDataSource.requestParams);
      return;
    }
    finish();
  }

  private void initView()
  {
    this.mScrollList = ((LinearLayout)findViewById(R.id.ll_container));
    this.mScrollView = ((ScrollView)findViewById(R.id.scroll_container));
    this.emptyView = ((FrameLayout)findViewById(R.id.empty));
    this.loadingLayout = findViewById(R.id.hui_buffet_list_loading_layout);
    this.mSubmitLayout = findViewById(R.id.submit_layout);
    this.mSubmitBtn = ((Button)findViewById(R.id.btn_submit_buffet_order));
    this.mSubmitBtn.setEnabled(false);
    this.mSubmitBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new JSONArray();
        Iterator localIterator = HuiBuffetSelecteListActivity.this.mBuffetDataSource.buffetList.iterator();
        while (localIterator.hasNext())
        {
          Buffet localBuffet = (Buffet)localIterator.next();
          JSONObject localJSONObject = new JSONObject();
          try
          {
            localJSONObject.put("couponId", localBuffet.couponId);
            localJSONObject.put("count", localBuffet.count);
            paramView.put(localJSONObject);
          }
          catch (JSONException localJSONException)
          {
            localJSONException.printStackTrace();
          }
        }
        HuiBuffetSelecteListActivity.this.mBuffetDataSource.requestCreateOrder(paramView.toString());
        GAHelper.instance().contextStatisticsEvent(HuiBuffetSelecteListActivity.this, "merchandise_paybutton_click", null, "tap");
      }
    });
  }

  private void refreshPage()
  {
    this.mBuffetDataSource.buffetList.clear();
    updateView(3);
    this.mBuffetDataSource.getBuffetList(this.mBuffetDataSource.requestParams);
  }

  private void scrollView(View paramView)
  {
    new Handler().postDelayed(new Runnable(paramView)
    {
      public void run()
      {
        int i = HuiBuffetSelecteListActivity.this.getWindow().findViewById(16908290).getTop();
        int[] arrayOfInt = new int[2];
        this.val$view.getLocationInWindow(arrayOfInt);
        int j = arrayOfInt[0];
        int k = arrayOfInt[1];
        HuiBuffetSelecteListActivity.this.mScrollView.scrollBy(j, k - i);
      }
    }
    , 100L);
  }

  private void updateSubmitBtn()
  {
    double d = 0.0D;
    int i = 0;
    Object localObject = this.mBuffetDataSource.buffetList.iterator();
    while (((Iterator)localObject).hasNext())
    {
      Buffet localBuffet = (Buffet)((Iterator)localObject).next();
      d += localBuffet.totalValue;
      i += localBuffet.count;
    }
    if (i == 0)
    {
      this.mSubmitBtn.setEnabled(false);
      this.mSubmitBtn.setText("确认买单");
      return;
    }
    this.mSubmitBtn.setEnabled(true);
    localObject = HuiUtils.bigDecimalTrailingZerosToString(new BigDecimal(d), 2);
    this.mSubmitBtn.setText((String)localObject + "元 确认买单");
  }

  private void updateView(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return;
    case 3:
      this.mScrollList.setVisibility(8);
      this.loadingLayout.setVisibility(0);
      this.emptyView.setVisibility(8);
      this.mSubmitLayout.setVisibility(8);
      return;
    case 0:
      this.mScrollList.setVisibility(8);
      this.loadingLayout.setVisibility(8);
      this.emptyView.setVisibility(0);
      this.mSubmitLayout.setVisibility(8);
      setEmptyMsg("暂无符合条件的自助餐", false);
      return;
    case 1:
      this.mScrollList.setVisibility(8);
      this.loadingLayout.setVisibility(8);
      this.emptyView.setVisibility(0);
      this.mSubmitLayout.setVisibility(8);
      this.emptyView.removeAllViews();
      this.emptyView.addView(getFailedView("网络连接失败 点击重新加载", new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          HuiBuffetSelecteListActivity.this.refreshPage();
        }
      }));
      return;
    case 2:
    }
    if (this.mBuffetDataSource.buffetList.isEmpty())
    {
      updateView(0);
      return;
    }
    inflateListView();
    this.mScrollList.setVisibility(0);
    this.loadingLayout.setVisibility(8);
    this.emptyView.setVisibility(8);
    this.mSubmitLayout.setVisibility(0);
  }

  public void createOrderComplete(HuiMapiStatus paramHuiMapiStatus, Message paramMessage)
  {
    int i;
    if (paramHuiMapiStatus == HuiMapiStatus.STATUS_FINISH)
    {
      i = paramMessage.code;
      paramHuiMapiStatus = paramMessage.content;
      if (i == 10)
      {
        paramMessage = new Intent("android.intent.action.VIEW");
        paramMessage.setData(Uri.parse(paramHuiMapiStatus));
        startActivity(paramMessage);
      }
    }
    do
    {
      do
      {
        return;
        if (i != 20)
          continue;
        showShortToast(paramHuiMapiStatus);
        return;
      }
      while (i != 30);
      showMessageDialog(paramHuiMapiStatus, "确定", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          HuiBuffetSelecteListActivity.this.refreshPage();
        }
      });
      return;
    }
    while (paramHuiMapiStatus != HuiMapiStatus.STATUS_FAIL);
    showShortToast("订单创建失败，请稍后重试");
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void loadBuffetListComplete(HuiMapiStatus paramHuiMapiStatus, Object paramObject)
  {
    if (paramHuiMapiStatus == HuiMapiStatus.STATUS_FINISH)
      updateView(2);
    do
      return;
    while (paramHuiMapiStatus != HuiMapiStatus.STATUS_FAIL);
    updateView(1);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.hui_buffet_select_activity);
    super.getWindow().setBackgroundDrawable(null);
    initView();
    initData();
  }

  protected void onDestroy()
  {
    this.mBuffetDataSource.releaseRequests();
    super.onDestroy();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
      GAHelper.instance().contextStatisticsEvent(this, "merchandise_back", null, "tap");
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  protected void onLeftTitleButtonClicked()
  {
    GAHelper.instance().contextStatisticsEvent(this, "merchandise_back", null, "tap");
    super.onLeftTitleButtonClicked();
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.mBuffetDataSource.restoreData(paramBundle);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.mBuffetDataSource.saveData(paramBundle);
  }

  public void scrollToGivenViewPosition(View paramView)
  {
    scrollView(paramView);
  }

  protected void setEmptyMsg(String paramString, boolean paramBoolean)
  {
    if (this.emptyTV == null)
      this.emptyTV = ((TextView)getLayoutInflater().inflate(R.layout.simple_list_item_18, this.emptyView, false));
    Drawable localDrawable = getResources().getDrawable(R.drawable.empty_page_nothing);
    localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
    this.emptyTV.setCompoundDrawablePadding(8);
    this.emptyTV.setCompoundDrawables(localDrawable, null, null, null);
    this.emptyTV.setText(paramString);
    if (this.emptyView.getChildAt(0) != this.emptyTV)
    {
      this.emptyView.removeAllViews();
      this.emptyView.addView(this.emptyTV);
    }
  }

  void showMessageDialog(String paramString1, String paramString2, DialogInterface.OnClickListener paramOnClickListener)
  {
    if (this.isDestroyed)
      return;
    dismissDialog();
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setMessage(paramString1);
    localBuilder.setPositiveButton(paramString2, paramOnClickListener);
    paramString1 = localBuilder.create();
    paramString1.setCanceledOnTouchOutside(false);
    this.managedDialogId = 64006;
    this.managedDialog = paramString1;
    paramString1.show();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.activity.HuiBuffetSelecteListActivity
 * JD-Core Version:    0.6.0
 */