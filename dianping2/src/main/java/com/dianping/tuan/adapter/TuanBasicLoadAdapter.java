package com.dianping.tuan.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.tuan.utils.ViewItemUtils;
import com.dianping.tuan.widget.ViewItemData;
import com.dianping.tuan.widget.viewitem.TuanItemDisplayType;
import com.dianping.tuan.widget.viewitem.TuanItemDividerAdjustable;
import com.dianping.tuan.widget.viewitem.TuanItemFactory;
import com.dianping.tuan.widget.viewitem.TuanItemInterface;
import com.dianping.util.network.NetworkUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class TuanBasicLoadAdapter extends BasicLoadAdapter
{
  protected ArrayList<ViewItemData> displayData = new ArrayList();
  protected double lat;
  protected double lng;
  Context mContext;
  protected RequestInfoInterface requestInfoInterface;
  private boolean shouldShowImage;
  protected ArrayList<Integer> speardIndexList = new ArrayList();
  int typeCount;

  public TuanBasicLoadAdapter(Context paramContext)
  {
    this(paramContext, 0.0D, 0.0D, 0);
  }

  public TuanBasicLoadAdapter(Context paramContext, double paramDouble1, double paramDouble2, int paramInt)
  {
    super(paramContext);
    this.mContext = paramContext;
    this.lat = paramDouble1;
    this.lng = paramDouble2;
    this.typeCount = paramInt;
    this.shouldShowImage = DPActivity.preferences().getBoolean("isShowListImage", true);
  }

  protected static boolean defaultViewItemOnClickAction(Context paramContext, DPObject paramDPObject)
  {
    if (!DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem"));
    do
    {
      return false;
      paramDPObject = paramDPObject.getString("Link");
    }
    while (TextUtils.isEmpty(paramDPObject));
    paramContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramDPObject)));
    return true;
  }

  public static int getTypeCount()
  {
    return TuanItemDisplayType.values().length + 3;
  }

  protected static DPObject getViewItemFromViewTag(View paramView)
  {
    if (paramView == null)
      paramView = null;
    DPObject localDPObject;
    do
    {
      return paramView;
      paramView = paramView.getTag();
      if (!(paramView instanceof ViewItemData))
        return null;
      localDPObject = ((ViewItemData)paramView).viewItem;
      paramView = localDPObject;
    }
    while (DPObjectUtils.isDPObjectof(localDPObject, "ViewItem"));
    return null;
  }

  protected View.OnClickListener createDefaultDealOnClickListener()
  {
    return new TuanBasicLoadAdapter.3(this);
  }

  protected View.OnClickListener createDefaultHuiOnClickListener()
  {
    return new TuanBasicLoadAdapter.4(this);
  }

  protected View.OnClickListener createDefaultMoreDealsClickListener()
  {
    return new TuanBasicLoadAdapter.6(this);
  }

  public View.OnClickListener createDefaultOnClickListener(TuanItemDisplayType paramTuanItemDisplayType)
  {
    switch (TuanBasicLoadAdapter.7.$SwitchMap$com$dianping$tuan$widget$viewitem$TuanItemDisplayType[paramTuanItemDisplayType.ordinal()])
    {
    default:
      return null;
    case 1:
      return createDefaultShopOnClickListener();
    case 2:
      return createDefaultDealOnClickListener();
    case 3:
      return createDefaultHuiOnClickListener();
    case 4:
      return createDefaultViewItemOnClickListener();
    case 5:
      return createDefaultMoreDealsClickListener();
    case 6:
      return createDefaultViewItemOnClickListener();
    case 7:
      return createDefaultDealOnClickListener();
    case 8:
      return createDefaultShopOnClickListener();
    case 9:
      return createDefaultHuiOnClickListener();
    case 10:
      return createDefaultViewItemOnClickListener();
    case 11:
    }
    return createDefaultViewItemOnClickListener();
  }

  protected View.OnClickListener createDefaultShopOnClickListener()
  {
    return new TuanBasicLoadAdapter.2(this);
  }

  protected View.OnClickListener createDefaultViewItemOnClickListener()
  {
    return new TuanBasicLoadAdapter.5(this);
  }

  public MApiRequest createRequest(int paramInt)
  {
    if (this.requestInfoInterface != null)
      return this.requestInfoInterface.createRequest(paramInt);
    return null;
  }

  public int getCount()
  {
    if (this.mIsEnd)
    {
      if (this.displayData.size() == 0)
        return 1;
      return this.displayData.size();
    }
    return this.displayData.size() + 1;
  }

  protected TextView getEmptyView(String paramString1, String paramString2, ViewGroup paramViewGroup, View paramView)
  {
    paramString1 = super.getEmptyView(paramString1, paramString2, paramViewGroup, paramView);
    if (paramString1 != null)
      paramString1.setTextColor(this.mContext.getResources().getColor(R.color.tuan_common_black));
    return paramString1;
  }

  public Object getItem(int paramInt)
  {
    if (paramInt < this.displayData.size())
      return this.displayData.get(paramInt);
    if ((this.mEmptyMsg != null) || ((this.mIsEnd) && (this.mData.size() == 0)))
      return EMPTY;
    if (this.mErrorMsg == null)
      return LOADING;
    return ERROR;
  }

  public int getItemViewType(int paramInt)
  {
    Object localObject = getItem(paramInt);
    if (localObject == ERROR)
      return this.typeCount + 0;
    if (localObject == LOADING)
      return this.typeCount + 1;
    if (localObject == EMPTY)
      return this.typeCount + 2;
    if (this.displayData.get(paramInt) == null)
      return this.typeCount + 3;
    return ((ViewItemData)this.displayData.get(paramInt)).displayType.ordinal() + 3 + this.typeCount;
  }

  protected View getLoadingView(ViewGroup paramViewGroup, View paramView)
  {
    View localView = null;
    if (paramView == null);
    while (true)
    {
      paramView = localView;
      if (localView == null)
      {
        paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.tuan_basic_loading_item, paramViewGroup, false);
        paramView.setTag(LOADING);
      }
      return paramView;
      if (paramView.getTag() != LOADING)
        continue;
      localView = paramView;
    }
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject = getItem(paramInt);
    if ((localObject instanceof ViewItemData))
      return itemViewWithData((ViewItemData)localObject, paramInt, paramView, paramViewGroup, this.lat, this.lng);
    if (localObject == LOADING)
    {
      if (this.mErrorMsg == null)
        loadNewPage();
      return getLoadingView(paramViewGroup, paramView);
    }
    if (localObject == EMPTY)
      return getEmptyView(emptyMessage(), "暂时没有你要找的哦，看看别的吧", paramViewGroup, paramView);
    return getFailedView(this.mErrorMsg, new TuanBasicLoadAdapter.1(this), paramViewGroup, paramView);
  }

  public int getViewTypeCount()
  {
    return getTypeCount();
  }

  protected boolean isImageOn()
  {
    return (this.shouldShowImage) || (NetworkUtils.isWIFIConnection(DPApplication.instance()));
  }

  protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    return null;
  }

  protected View itemViewWithData(ViewItemData paramViewItemData, int paramInt, View paramView, ViewGroup paramViewGroup, double paramDouble1, double paramDouble2)
  {
    if (TuanItemFactory.match(paramViewItemData, paramView))
    {
      ((TuanItemInterface)paramView).updateData(paramViewItemData.viewItem, paramDouble1, paramDouble2, isImageOn());
      paramViewGroup = paramView;
      if ((paramView instanceof TuanItemDividerAdjustable))
        ((TuanItemDividerAdjustable)paramView).adjustDivider(paramViewItemData);
    }
    for (paramViewGroup = paramView; ; paramViewGroup = TuanItemFactory.getView(this.mContext, paramViewItemData, isImageOn(), paramDouble1, paramDouble2))
    {
      if (paramViewGroup != null)
      {
        paramViewGroup.setTag(paramViewItemData);
        paramViewGroup.setOnClickListener(createDefaultOnClickListener(paramViewItemData.displayType));
      }
      return paramViewGroup;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      Object localObject = (DPObject)paramMApiResponse.result();
      if (this.mIsPullToRefresh)
      {
        this.mIsPullToRefresh = false;
        this.mData.clear();
        this.displayData.clear();
        this.speardIndexList.clear();
      }
      this.mEmptyMsg = ((DPObject)localObject).getString("EmptyMsg");
      this.mIsEnd = ((DPObject)localObject).getBoolean("IsEnd");
      this.mNextStartIndex = ((DPObject)localObject).getInt("NextStartIndex");
      this.mRecordCount = ((DPObject)localObject).getInt("RecordCount");
      this.mQueryId = ((DPObject)localObject).getString("QueryID");
      localObject = ((DPObject)localObject).getArray("List");
      if (localObject == null)
        break label200;
      appendDataToList(localObject);
      if ((this.mData.size() == 0) && (this.mEmptyMsg == null))
        this.mEmptyMsg = "数据为空";
      if (localObject.length != 0);
    }
    label200: for (this.mIsEnd = true; ; this.mIsEnd = true)
    {
      parseDisplayData();
      notifyDataSetChanged();
      this.mReq = null;
      onRequestComplete(true, paramMApiRequest, paramMApiResponse);
      if (this.requestInfoInterface != null)
        this.requestInfoInterface.onRequestFinish(paramMApiRequest, paramMApiResponse);
      return;
    }
  }

  public void parseDisplayData()
  {
    this.displayData.clear();
    int i = 0;
    while (i < this.mData.size())
    {
      boolean bool = false;
      if (this.speardIndexList.contains(Integer.valueOf(i)))
        bool = true;
      this.displayData.addAll(ViewItemUtils.unfoldViewItem((DPObject)this.mData.get(i), i, bool));
      i += 1;
    }
  }

  public void reset()
  {
    this.displayData.clear();
    this.speardIndexList.clear();
    super.reset();
  }

  public void setLocation(double paramDouble1, double paramDouble2)
  {
    this.lat = paramDouble1;
    this.lng = paramDouble2;
  }

  public void setRequestInfoInterface(RequestInfoInterface paramRequestInfoInterface)
  {
    this.requestInfoInterface = paramRequestInfoInterface;
  }

  public void spreadData(int paramInt)
  {
    if (!this.speardIndexList.contains(Integer.valueOf(paramInt)))
    {
      this.speardIndexList.add(Integer.valueOf(paramInt));
      parseDisplayData();
    }
  }

  public static abstract interface RequestInfoInterface
  {
    public abstract MApiRequest createRequest(int paramInt);

    public abstract void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.adapter.TuanBasicLoadAdapter
 * JD-Core Version:    0.6.0
 */