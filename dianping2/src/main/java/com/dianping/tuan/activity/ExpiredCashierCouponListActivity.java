package com.dianping.tuan.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.tuan.widget.CashierCouponListItem;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import java.util.ArrayList;
import java.util.Arrays;

public class ExpiredCashierCouponListActivity extends BaseTuanActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String TAG = "ExpiredCashierCouponListActivity";
  private MApiRequest cashierCouponRequest;
  private ArrayList<DPObject> dpobjCashierCouponList = new ArrayList();
  private CashierCouponAdapter mAdapter;
  private PullToRefreshListView mListView;

  private void initList()
  {
    if (this.mListView.getAdapter() != null)
    {
      this.mAdapter.reset();
      return;
    }
    this.mAdapter = new CashierCouponAdapter();
    this.mListView.setAdapter(this.mAdapter);
  }

  private void loadCashierCouponInfos()
  {
    if (this.cashierCouponRequest != null)
    {
      Log.i("ExpiredCashierCouponListActivity", "cashierCouponRequest is running");
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("http://api.p.dianping.com/");
    localStringBuilder.append("getuserredenvelope.pay");
    localStringBuilder.append("?token=").append(accountService().token());
    localStringBuilder.append("&expired=").append(2);
    localStringBuilder.append("&start=").append(this.dpobjCashierCouponList.size());
    this.cashierCouponRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.cashierCouponRequest, this);
  }

  public void onAccountSwitched(UserProfile paramUserProfile)
  {
    if (paramUserProfile == null)
    {
      finish();
      return;
    }
    initList();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.cashiercoupon_list_layout);
    this.mListView = ((PullToRefreshListView)findViewById(R.id.list));
    this.mListView.setMode(PullToRefreshBase.Mode.DISABLED);
    this.mListView.setSelector(new ColorDrawable(0));
    if (isLogined())
    {
      initList();
      return;
    }
    accountService().login(this);
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.cashierCouponRequest != null)
    {
      mapiService().abort(this.cashierCouponRequest, this, true);
      this.cashierCouponRequest = null;
    }
  }

  public void onLoginCancel()
  {
    finish();
    super.onLoginCancel();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.message();
    if (paramMApiRequest == this.cashierCouponRequest)
    {
      this.cashierCouponRequest = null;
      if (paramMApiResponse != null)
        break label38;
    }
    label38: for (paramMApiRequest = "数据请求错误，请稍候再试"; ; paramMApiRequest = paramMApiResponse.content())
    {
      this.mAdapter.appendCashierCoupon(null, paramMApiRequest, false);
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    Object localObject;
    if ((paramMApiRequest == this.cashierCouponRequest) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      this.cashierCouponRequest = null;
      localObject = null;
      paramMApiRequest = null;
    }
    try
    {
      DPObject localDPObject = (DPObject)paramMApiResponse.result();
      paramMApiResponse = localObject;
      paramMApiRequest = localDPObject;
      if (paramMApiRequest != null)
      {
        this.mAdapter.appendCashierCoupon(paramMApiRequest.getArray("List"), paramMApiResponse, paramMApiRequest.getBoolean("IsEnd"));
        return;
      }
    }
    catch (Exception paramMApiResponse)
    {
      while (true)
      {
        Log.e("ExpiredCashierCouponListActivity", paramMApiResponse.getLocalizedMessage());
        paramMApiResponse = paramMApiResponse.getLocalizedMessage();
      }
      this.mAdapter.appendCashierCoupon(null, paramMApiResponse, false);
    }
  }

  class CashierCouponAdapter extends BasicAdapter
  {
    private String errorMsg;
    private boolean isEnd;

    CashierCouponAdapter()
    {
    }

    public void appendCashierCoupon(DPObject[] paramArrayOfDPObject, String paramString, boolean paramBoolean)
    {
      if ((paramArrayOfDPObject != null) && (paramArrayOfDPObject.length > 0))
        ExpiredCashierCouponListActivity.this.dpobjCashierCouponList.addAll(Arrays.asList(paramArrayOfDPObject));
      this.errorMsg = paramString;
      this.isEnd = paramBoolean;
      notifyDataSetChanged();
    }

    public int getCount()
    {
      if ((this.isEnd) && (ExpiredCashierCouponListActivity.this.dpobjCashierCouponList.size() == 0))
        return 1;
      if (this.isEnd)
        return ExpiredCashierCouponListActivity.this.dpobjCashierCouponList.size();
      return ExpiredCashierCouponListActivity.this.dpobjCashierCouponList.size() + 1;
    }

    protected TextView getEmptyView(String paramString1, String paramString2, ViewGroup paramViewGroup, View paramView)
    {
      paramString2 = super.getEmptyView(paramString1, paramString2, paramViewGroup, paramView);
      paramString2.setText(paramString1);
      return paramString2;
    }

    public Object getItem(int paramInt)
    {
      if ((this.isEnd) && (ExpiredCashierCouponListActivity.this.dpobjCashierCouponList.size() == 0))
        return EMPTY;
      if (paramInt < ExpiredCashierCouponListActivity.this.dpobjCashierCouponList.size())
        return ExpiredCashierCouponListActivity.this.dpobjCashierCouponList.get(paramInt);
      if (TextUtils.isEmpty(this.errorMsg))
        return LOADING;
      return ERROR;
    }

    public long getItemId(int paramInt)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
        return ((DPObject)localObject).getInt("ID");
      if (localObject == LOADING)
        return -paramInt;
      return -2147483648L;
    }

    public int getItemViewType(int paramInt)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
        return 0;
      if (localObject == LOADING)
        return 1;
      return 2;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject2 = getItem(paramInt);
      Object localObject1;
      if (DPObjectUtils.isDPObjectof(localObject2, "RedEnvelope"))
      {
        if ((paramView instanceof CashierCouponListItem));
        for (paramView = (CashierCouponListItem)paramView; ; paramView = null)
        {
          localObject1 = paramView;
          if (paramView == null)
            localObject1 = (CashierCouponListItem)ExpiredCashierCouponListActivity.this.getLayoutInflater().inflate(R.layout.cashiercoupon_list_item, paramViewGroup, false);
          ((CashierCouponListItem)localObject1).showItem((DPObject)localObject2, 2);
          return localObject1;
        }
      }
      if (localObject2 == EMPTY)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("暂无过期现金券");
        return getEmptyView(((StringBuilder)localObject1).toString(), "", paramViewGroup, paramView);
      }
      if (localObject2 == LOADING)
      {
        ExpiredCashierCouponListActivity.this.loadCashierCouponInfos();
        return getLoadingView(paramViewGroup, paramView);
      }
      return (View)getFailedView(this.errorMsg, new ExpiredCashierCouponListActivity.CashierCouponAdapter.1(this), paramViewGroup, paramView);
    }

    public int getViewTypeCount()
    {
      return 3;
    }

    public void reset()
    {
      ExpiredCashierCouponListActivity.this.dpobjCashierCouponList.clear();
      this.errorMsg = null;
      this.isEnd = false;
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.ExpiredCashierCouponListActivity
 * JD-Core Version:    0.6.0
 */