package com.dianping.tuan.activity;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.thirdparty.wxapi.WeiXinCard;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.tuan.widget.DiscountListItem;
import com.dianping.util.Log;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DiscountListActivity extends BaseTuanActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final int DEFAULT_LIMITE = 25;
  private static final int NONEXPIRE = 0;
  private static final String TAG = DiscountListActivity.class.getSimpleName();
  protected Adapter adapter;
  protected Set<Integer> addedDiscountIds = new HashSet();
  protected MApiRequest discountListRequest;
  protected PullToRefreshListView discountListView;
  protected Map<Integer, MApiRequest> discountWeixinCardRequests = new HashMap();

  protected void initList()
  {
    if (this.discountListView.getAdapter() != null)
    {
      this.adapter.reset();
      return;
    }
    this.discountListView.setAdapter(this.adapter);
  }

  protected boolean isNeedLogin()
  {
    return true;
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
    setContentView(R.layout.discount_list_layout);
    this.discountListView = ((PullToRefreshListView)findViewById(R.id.list));
    this.discountListView.setMode(PullToRefreshBase.Mode.DISABLED);
    this.discountListView.setBackgroundColor(getResources().getColor(R.color.common_bk_color));
    this.discountListView.setDivider(null);
    this.discountListView.setSelector(new ColorDrawable(0));
    this.discountListView.setFastScrollEnabled(true);
    this.adapter = new Adapter();
    if (isLogined())
      initList();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.discountListRequest != null)
      mapiService().abort(this.discountListRequest, this, true);
    Iterator localIterator = this.discountWeixinCardRequests.values().iterator();
    while (localIterator.hasNext())
    {
      MApiRequest localMApiRequest = (MApiRequest)localIterator.next();
      mapiService().abort(localMApiRequest, null, true);
    }
    WeiXinCard.instance().release();
    this.discountWeixinCardRequests.clear();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    paramMApiResponse = paramMApiResponse.message();
    if (paramMApiRequest == this.discountListRequest)
    {
      this.discountListRequest = null;
      if (paramMApiResponse != null)
        break label42;
    }
    label42: for (paramMApiRequest = "数据请求错误，请稍候再试"; ; paramMApiRequest = paramMApiResponse.content())
    {
      this.adapter.appendDiscount(null, paramMApiRequest, false);
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    Object localObject;
    if (((paramMApiResponse.result() instanceof DPObject)) && (paramMApiRequest == this.discountListRequest))
    {
      this.discountListRequest = null;
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
        this.adapter.appendDiscount(paramMApiRequest.getArray("List"), paramMApiResponse, paramMApiRequest.getBoolean("IsEnd"));
        return;
      }
    }
    catch (Exception paramMApiResponse)
    {
      while (true)
      {
        Log.e(TAG, paramMApiResponse.getLocalizedMessage());
        paramMApiResponse = paramMApiResponse.getLocalizedMessage();
      }
      this.adapter.appendDiscount(null, paramMApiResponse, false);
    }
  }

  class Adapter extends BasicAdapter
  {
    protected ArrayList<DPObject> dpobjDiscountList = new ArrayList();
    protected String errorMsg;
    protected boolean isEnd;

    Adapter()
    {
    }

    public void appendDiscount(DPObject[] paramArrayOfDPObject, String paramString, boolean paramBoolean)
    {
      if ((paramArrayOfDPObject != null) && (paramArrayOfDPObject.length > 0))
        this.dpobjDiscountList.addAll(Arrays.asList(paramArrayOfDPObject));
      this.errorMsg = paramString;
      this.isEnd = paramBoolean;
      notifyDataSetChanged();
    }

    public int getCount()
    {
      if ((this.isEnd) && (this.dpobjDiscountList.size() == 0))
        return 1;
      return this.dpobjDiscountList.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      if ((this.isEnd) && (this.dpobjDiscountList.size() == 0))
        return EMPTY;
      if (paramInt < this.dpobjDiscountList.size())
        return this.dpobjDiscountList.get(paramInt);
      if (this.isEnd)
        return LAST_EXTRA;
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
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
      {
        if ((paramView instanceof DiscountListItem));
        for (paramView = (DiscountListItem)paramView; ; paramView = null)
        {
          paramViewGroup = paramView;
          if (paramView == null)
            paramViewGroup = (DiscountListItem)LayoutInflater.from(DiscountListActivity.this).inflate(R.layout.discount_list_item2, null, false);
          paramView = (DPObject)localObject;
          paramViewGroup.showItem(paramView, 0);
          paramViewGroup.showWeixinCardInfo(paramView, new DiscountListActivity.Adapter.1(this, paramView, paramViewGroup), DiscountListActivity.this.addedDiscountIds.contains(Integer.valueOf(paramView.getInt("ID"))));
          return paramViewGroup;
        }
      }
      if (localObject == EMPTY)
      {
        paramView = LayoutInflater.from(DiscountListActivity.this).inflate(R.layout.discount_list_empty, null, false);
        paramViewGroup = (Button)paramView.findViewById(R.id.button1);
        localObject = (Button)paramView.findViewById(R.id.button2);
        paramViewGroup.setOnClickListener(new DiscountListActivity.Adapter.2(this));
        ((Button)localObject).setOnClickListener(new DiscountListActivity.Adapter.3(this));
        return paramView;
      }
      if (localObject == LOADING)
      {
        loadDiscount();
        return getLoadingView(paramViewGroup, paramView);
      }
      if (localObject == LAST_EXTRA)
      {
        paramView = LayoutInflater.from(DiscountListActivity.this).inflate(R.layout.discount_list_footer, null, false);
        paramViewGroup = (Button)paramView.findViewById(R.id.button1);
        localObject = (Button)paramView.findViewById(R.id.button2);
        paramViewGroup.setOnClickListener(new DiscountListActivity.Adapter.4(this));
        ((Button)localObject).setOnClickListener(new DiscountListActivity.Adapter.5(this));
        return paramView;
      }
      return (View)getFailedView(this.errorMsg, new DiscountListActivity.Adapter.6(this), paramViewGroup, paramView);
    }

    public int getViewTypeCount()
    {
      return 3;
    }

    public boolean isEnabled(int paramInt)
    {
      return false;
    }

    protected void loadDiscount()
    {
      if (DiscountListActivity.this.discountListRequest != null)
      {
        Log.i(DiscountListActivity.TAG, "discountListRequest is running");
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder("http://app.t.dianping.com/");
      localStringBuilder.append("discountlistgn.bin");
      localStringBuilder.append("?token=").append(DiscountListActivity.this.accountService().token());
      localStringBuilder.append("&start=" + this.dpobjDiscountList.size());
      localStringBuilder.append("&expired=0");
      localStringBuilder.append("&limit=25");
      DiscountListActivity.this.discountListRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
      DiscountListActivity.this.mapiService().exec(DiscountListActivity.this.discountListRequest, DiscountListActivity.this);
    }

    public void reset()
    {
      this.dpobjDiscountList.clear();
      this.errorMsg = null;
      this.isEnd = false;
      notifyDataSetChanged();
    }

    protected void tryAddToWeixinCard(DPObject paramDPObject, DiscountListItem paramDiscountListItem)
    {
      int i = paramDPObject.getInt("ID");
      if (DiscountListActivity.this.discountWeixinCardRequests.containsKey(Integer.valueOf(i)))
        return;
      paramDPObject = new StringBuilder("http://app.t.dianping.com/");
      paramDPObject.append("adddiscounttoweixingn.bin");
      paramDPObject.append("?token=").append(DiscountListActivity.this.accountService().token());
      paramDPObject.append("&discountid=").append(i);
      paramDPObject = BasicMApiRequest.mapiGet(paramDPObject.toString(), CacheType.DISABLED);
      DiscountListActivity.this.discountWeixinCardRequests.put(Integer.valueOf(i), paramDPObject);
      paramDiscountListItem = new DiscountListActivity.Adapter.7(this, i);
      DiscountListActivity.this.mapiService().exec(paramDPObject, new DiscountListActivity.Adapter.8(this, i, paramDiscountListItem));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.DiscountListActivity
 * JD-Core Version:    0.6.0
 */