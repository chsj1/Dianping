package com.dianping.main.favorite;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.adapter.MulDeleListAdapter;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.NovaTabBaseFragment;
import com.dianping.base.basic.NovaTabFragmentActivity;
import com.dianping.base.shoplist.widget.ShopListItem;
import com.dianping.base.util.FavoriteHelper;
import com.dianping.base.util.FavoriteHelper.FavoriteInfo;
import com.dianping.base.util.FavoriteHelper.FavoriteListener;
import com.dianping.base.widget.FilterBar;
import com.dianping.base.widget.FilterBar.OnItemClickListener;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.base.widget.dialogfilter.ListFilterDialog;
import com.dianping.base.widget.dialogfilter.TwinListFilterDialog;
import com.dianping.dataservice.FullRequestHandle;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.view.GAHelper;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class FavoriteShopFragment extends NovaTabBaseFragment
  implements FavoriteHelper.FavoriteListener, FilterDialog.OnFilterListener, AdapterView.OnItemClickListener, FullRequestHandle<MApiRequest, MApiResponse>
{
  private static final boolean DEBUG = true;
  private static final String FILTERBAR_DEFAULT_TITLE1 = "全部地区";
  private static final String FILTERBAR_DEFAULT_TITLE2 = "全部类别";
  private static final String FILTERBAR_DEFAULT_TITLE3 = "按时间排序";
  private static final String FILTERBAR_TYPE1 = "地区";
  private static final int FILTERBAR_TYPE1_BASEID = 0;
  private static final String FILTERBAR_TYPE2 = "类别";
  private static final int FILTERBAR_TYPE2_BASEID = 0;
  private static final String FILTERBAR_TYPE3 = "排序";
  private static final int ORDER_BY_DISTANCE = 1;
  private static final int ORDER_BY_TIME = 0;
  private static final String TAG = "FavoriteShopFragment";
  private TextView emptyTV;
  String fShopListEmptyMsg = "";
  String[] ids = null;
  boolean[] idsRemoved = null;
  private boolean isOversea;
  double latitude;
  double longitude;
  private View mContentView;
  DPObject mCurrentType1FilterItem = null;
  DPObject mCurrentType2FilterItem = null;
  DPObject mCurrentType3FilterItem = null;
  private ViewGroup mEmptyView;
  View mFailedView;
  FavoriteHelper mFavoriteHelper;
  private Adapter mFavoriteListByDistanceAdapter = new Adapter(1);
  private Adapter mFavoriteListByTimeAdapter = new Adapter(0);
  private Adapter mFavoriteListCurrentAdapter;
  private FilterBar mFilterBar;
  private ListView mListView;
  private View mLoadingView;
  private int mOrderMode = 0;
  private MApiRequest mRemoveFavoriteRequest;
  private Integer[] mRemovedIds;
  ArrayList<DPObject> mType1FilterItems = new ArrayList();
  ArrayList<DPObject> mType2FilterItems = new ArrayList();
  ArrayList<DPObject> mType3FilterItems = new ArrayList();
  int mUserId = 0;
  double offsetLatitude;
  double offsetLongitude;

  private void showEmptyView(String paramString)
  {
    this.mEmptyView.setVisibility(0);
    this.mContentView.setVisibility(8);
    int j = this.mEmptyView.getChildCount();
    int i = 0;
    while (i < j)
    {
      this.mEmptyView.getChildAt(i).setVisibility(8);
      i += 1;
    }
    if (this.emptyTV == null)
    {
      this.emptyTV = ((TextView)getActivity().getLayoutInflater().inflate(R.layout.simple_list_item_18, null, false));
      this.emptyTV.setMovementMethod(LinkMovementMethod.getInstance());
      Drawable localDrawable = getResources().getDrawable(R.drawable.empty_page_nothing);
      localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
      this.emptyTV.setCompoundDrawablePadding(8);
      this.emptyTV.setCompoundDrawables(localDrawable, null, null, null);
      this.mEmptyView.addView(this.emptyTV);
    }
    if (!TextUtils.isEmpty(paramString))
      this.emptyTV.setText(Html.fromHtml(paramString));
    this.emptyTV.setVisibility(0);
  }

  private View showFailedView(String paramString, boolean paramBoolean, LoadingErrorView.LoadRetry paramLoadRetry)
  {
    int j = 0;
    Object localObject = this.mEmptyView;
    if (paramBoolean)
    {
      i = 0;
      ((ViewGroup)localObject).setVisibility(i);
      localObject = this.mContentView;
      if (!paramBoolean)
        break label93;
    }
    label93: for (int i = 8; ; i = 0)
    {
      ((View)localObject).setVisibility(i);
      int k = this.mEmptyView.getChildCount();
      i = 0;
      while (i < k)
      {
        this.mEmptyView.getChildAt(i).setVisibility(8);
        i += 1;
      }
      i = 8;
      break;
    }
    if (this.mFailedView == null)
    {
      this.mFailedView = getActivity().getLayoutInflater().inflate(R.layout.error_item, null, false);
      this.mEmptyView.addView(this.mFailedView);
    }
    ((TextView)this.mFailedView.findViewById(16908308)).setText(paramString);
    ((LoadingErrorView)this.mFailedView).setCallBack(paramLoadRetry);
    paramString = this.mFailedView;
    if (paramBoolean);
    for (i = j; ; i = 8)
    {
      paramString.setVisibility(i);
      return this.mFailedView;
    }
  }

  private void showLoadingView(boolean paramBoolean)
  {
    int j = 0;
    Object localObject = this.mEmptyView;
    if (paramBoolean)
    {
      i = 0;
      ((ViewGroup)localObject).setVisibility(i);
      localObject = this.mContentView;
      if (!paramBoolean)
        break label79;
    }
    label79: for (int i = 8; ; i = 0)
    {
      ((View)localObject).setVisibility(i);
      int k = this.mEmptyView.getChildCount();
      i = 0;
      while (i < k)
      {
        this.mEmptyView.getChildAt(i).setVisibility(8);
        i += 1;
      }
      i = 8;
      break;
    }
    if (this.mLoadingView == null)
    {
      this.mLoadingView = getActivity().getLayoutInflater().inflate(R.layout.loading_item_fullscreen, null, false);
      this.mEmptyView.addView(this.mLoadingView);
    }
    localObject = this.mLoadingView;
    if (paramBoolean);
    for (i = j; ; i = 8)
    {
      ((View)localObject).setVisibility(i);
      return;
    }
  }

  private void updateAfterRemove(Integer[] paramArrayOfInteger)
  {
    this.mFavoriteHelper.updateFavoriteShops(paramArrayOfInteger);
    ArrayList localArrayList = new ArrayList();
    paramArrayOfInteger = this.mType1FilterItems.iterator();
    while (paramArrayOfInteger.hasNext())
    {
      localObject = (DPObject)paramArrayOfInteger.next();
      if (((DPObject)localObject).isClass("City"))
      {
        if (this.mFavoriteHelper.hasCity(((DPObject)localObject).getString("Name")))
          continue;
        localArrayList.add(localObject);
        continue;
      }
      if ((!((DPObject)localObject).isClass("Region")) || (this.mFavoriteHelper.hasRegion(((DPObject)localObject).getString("Name"))))
        continue;
      localArrayList.add(localObject);
    }
    int i = 0;
    Object localObject = localArrayList.iterator();
    DPObject localDPObject;
    if (((Iterator)localObject).hasNext())
    {
      localDPObject = (DPObject)((Iterator)localObject).next();
      if (localDPObject == this.mCurrentType1FilterItem)
        if (this.mType1FilterItems.size() == 0)
          break label206;
      label206: for (paramArrayOfInteger = (DPObject)this.mType1FilterItems.get(0); ; paramArrayOfInteger = null)
      {
        this.mCurrentType1FilterItem = paramArrayOfInteger;
        this.mFilterBar.setItem("地区", "全部地区");
        i = 1;
        this.mType1FilterItems.remove(localDPObject);
        break;
      }
    }
    localArrayList.clear();
    paramArrayOfInteger = this.mType2FilterItems.iterator();
    while (paramArrayOfInteger.hasNext())
    {
      localObject = (DPObject)paramArrayOfInteger.next();
      if (((DPObject)localObject).isClass("Category"))
      {
        if (this.mFavoriteHelper.hasCategory(((DPObject)localObject).getString("Name")))
          continue;
        localArrayList.add(localObject);
        continue;
      }
      if ((!((DPObject)localObject).isClass("Tag")) || (this.mFavoriteHelper.hasType(((DPObject)localObject).getString("Name"))))
        continue;
      localArrayList.add(localObject);
    }
    localObject = localArrayList.iterator();
    if (((Iterator)localObject).hasNext())
    {
      localDPObject = (DPObject)((Iterator)localObject).next();
      if (localDPObject == this.mCurrentType2FilterItem)
        if (this.mType2FilterItems.size() == 0)
          break label402;
      label402: for (paramArrayOfInteger = (DPObject)this.mType2FilterItems.get(0); ; paramArrayOfInteger = null)
      {
        this.mCurrentType2FilterItem = paramArrayOfInteger;
        this.mFilterBar.setItem("类别", "全部类别");
        i = 1;
        this.mType2FilterItems.remove(localDPObject);
        break;
      }
    }
    localArrayList.clear();
    if (i != 0)
      this.mFavoriteListCurrentAdapter.refreshShownShopList(true, true);
  }

  public boolean locationCare()
  {
    return this.mOrderMode == 1;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    boolean bool = true;
    super.onActivityCreated(paramBundle);
    this.mFavoriteHelper = new FavoriteHelper(paramBundle);
    int i;
    label159: label176: Object localObject;
    if (paramBundle != null)
    {
      this.ids = paramBundle.getStringArray("ids");
      this.idsRemoved = paramBundle.getBooleanArray("idsRemoved");
      this.mUserId = paramBundle.getInt("userid");
      this.mType1FilterItems = paramBundle.getParcelableArrayList("filter1");
      this.mType2FilterItems = paramBundle.getParcelableArrayList("filter2");
      this.mType3FilterItems = paramBundle.getParcelableArrayList("filter3");
      this.mCurrentType1FilterItem = ((DPObject)paramBundle.getParcelable("currentfilter1"));
      this.mCurrentType2FilterItem = ((DPObject)paramBundle.getParcelable("currentfilter2"));
      this.mCurrentType3FilterItem = ((DPObject)paramBundle.getParcelable("currentfilter3"));
      if (this.mCurrentType3FilterItem != null)
      {
        i = this.mCurrentType3FilterItem.getInt("ID");
        if (i != 0)
          break label310;
        i = 0;
        this.mOrderMode = i;
        this.fShopListEmptyMsg = paramBundle.getString("fShopListEmptyMsg");
        localObject = location();
        if (localObject != null)
        {
          this.offsetLatitude = ((Location)localObject).offsetLatitude();
          this.offsetLongitude = ((Location)localObject).offsetLongitude();
          this.latitude = ((Location)localObject).latitude();
          this.longitude = ((Location)localObject).longitude();
        }
        if (paramBundle != null)
        {
          this.mFavoriteListByTimeAdapter.onRestoreInstanceState(paramBundle);
          this.mFavoriteListByDistanceAdapter.onRestoreInstanceState(paramBundle);
        }
        if (this.mOrderMode != 0)
          break label397;
        paramBundle = this.mFavoriteListByTimeAdapter;
        label249: this.mFavoriteListCurrentAdapter = paramBundle;
        if (this.ids != null)
          break label405;
        this.mFavoriteHelper.setFavoriteListener(this);
        this.mFavoriteHelper.refresh(this.mUserId, true);
        label281: paramBundle = (FavoriteBaseActivity)getActivity();
        if (this.mUserId != 0)
          break label460;
      }
    }
    while (true)
    {
      while (true)
      {
        paramBundle.setActivityEditable(bool, this);
        return;
        i = 0;
        break;
        label310: i = 1;
        break label159;
        localObject = getActivity().getIntent();
        if (((Intent)localObject).getData() != null)
        {
          String str = ((Intent)localObject).getData().getQueryParameter("userid");
          try
          {
            this.mUserId = Integer.parseInt(str);
            this.isOversea = "1".equals(((Intent)localObject).getData().getQueryParameter("oversea"));
          }
          catch (NumberFormatException localNumberFormatException)
          {
            while (true)
              this.mUserId = 0;
          }
        }
      }
      this.mUserId = ((Intent)localObject).getIntExtra("userId", 0);
      break label176;
      label397: paramBundle = this.mFavoriteListByDistanceAdapter;
      break label249;
      label405: if (this.ids.length != 0)
        break label281;
      if (TextUtils.isEmpty(this.fShopListEmptyMsg))
      {
        if (this.mUserId > 0);
        for (paramBundle = "TA还没收藏过商户，真可惜……"; ; paramBundle = "您还没有收藏过商户哦")
        {
          showEmptyView(paramBundle);
          break;
        }
      }
      showEmptyView(this.fShopListEmptyMsg);
      break label281;
      label460: bool = false;
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramViewGroup = paramLayoutInflater.inflate(R.layout.favorite_shop_layout, paramViewGroup, false);
    this.mEmptyView = ((ViewGroup)paramViewGroup.findViewById(R.id.empty));
    this.mContentView = paramViewGroup.findViewById(R.id.content);
    this.mListView = ((ListView)paramViewGroup.findViewById(R.id.list));
    this.mListView.setOnItemClickListener(this);
    this.mFilterBar = ((FilterBar)paramViewGroup.findViewById(R.id.filterBar));
    paramBundle = this.mFilterBar;
    if (this.mCurrentType1FilterItem == null)
    {
      paramLayoutInflater = "全部地区";
      paramBundle.addItem("地区", paramLayoutInflater);
      paramBundle = this.mFilterBar;
      if (this.mCurrentType2FilterItem != null)
        break label171;
      paramLayoutInflater = "全部类别";
      label109: paramBundle.addItem("类别", paramLayoutInflater);
      paramBundle = this.mFilterBar;
      if (this.mCurrentType3FilterItem != null)
        break label185;
    }
    label171: label185: for (paramLayoutInflater = "按时间排序"; ; paramLayoutInflater = this.mCurrentType3FilterItem.getString("Name"))
    {
      paramBundle.addItem("排序", paramLayoutInflater);
      this.mFilterBar.setOnItemClickListener(new FilterBar.OnItemClickListener()
      {
        public void onClickItem(Object paramObject, View paramView)
        {
          Iterator localIterator;
          DPObject localDPObject;
          boolean bool;
          FavoriteHelper localFavoriteHelper;
          String str1;
          int i;
          label178: String str2;
          int j;
          if (paramObject.equals("地区"))
          {
            localObject = new TwinListFilterDialog(FavoriteShopFragment.this.getActivity());
            ((TwinListFilterDialog)localObject).setTag(paramObject);
            ((TwinListFilterDialog)localObject).setOnFilterListener(FavoriteShopFragment.this);
            ((TwinListFilterDialog)localObject).setDisplayIcon(false);
            if ((FavoriteShopFragment.this.mCurrentType2FilterItem != null) && ((FavoriteShopFragment.this.mCurrentType2FilterItem.isClass("Tag")) || (FavoriteShopFragment.this.mCurrentType2FilterItem.isClass("Category"))))
            {
              paramObject = new ArrayList();
              localIterator = FavoriteShopFragment.this.mType1FilterItems.iterator();
              if (localIterator.hasNext())
              {
                localDPObject = (DPObject)localIterator.next();
                bool = true;
                if ((localDPObject.isClass("City")) || (localDPObject.isClass("Region")))
                {
                  localFavoriteHelper = FavoriteShopFragment.this.mFavoriteHelper;
                  str1 = localDPObject.getString("Name");
                  if (!localDPObject.isClass("City"))
                    break label240;
                  i = 0;
                  str2 = FavoriteShopFragment.this.mCurrentType2FilterItem.getString("Name");
                  if (!FavoriteShopFragment.this.mCurrentType2FilterItem.isClass("Tag"))
                    break label246;
                }
                label240: label246: for (j = 2; ; j = 3)
                {
                  bool = localFavoriteHelper.hasFilterItem(str1, i, str2, j);
                  if (!bool)
                    break;
                  paramObject.add(localDPObject);
                  break;
                  i = 1;
                  break label178;
                }
              }
              ((TwinListFilterDialog)localObject).setItems((DPObject[])paramObject.toArray(new DPObject[paramObject.size()]));
              if (FavoriteShopFragment.this.mCurrentType1FilterItem == null)
                break label332;
              paramObject = FavoriteShopFragment.this.mCurrentType1FilterItem;
              label288: ((TwinListFilterDialog)localObject).setSelectedItem(paramObject);
              ((TwinListFilterDialog)localObject).show(paramView);
            }
          }
          label332: label608: label614: 
          do
          {
            return;
            ((TwinListFilterDialog)localObject).setItems((DPObject[])FavoriteShopFragment.this.mType1FilterItems.toArray(new DPObject[FavoriteShopFragment.this.mType1FilterItems.size()]));
            break;
            if (FavoriteShopFragment.this.mType1FilterItems.size() != 0)
            {
              paramObject = (DPObject)FavoriteShopFragment.this.mType1FilterItems.get(0);
              break label288;
            }
            paramObject = null;
            break label288;
            if (!paramObject.equals("类别"))
              continue;
            localObject = new TwinListFilterDialog(FavoriteShopFragment.this.getActivity());
            ((TwinListFilterDialog)localObject).setTag(paramObject);
            ((TwinListFilterDialog)localObject).setOnFilterListener(FavoriteShopFragment.this);
            ((TwinListFilterDialog)localObject).setDisplayIcon(false);
            if ((FavoriteShopFragment.this.mCurrentType1FilterItem != null) && ((FavoriteShopFragment.this.mCurrentType1FilterItem.isClass("City")) || (FavoriteShopFragment.this.mCurrentType1FilterItem.isClass("Region"))))
            {
              paramObject = new ArrayList();
              localIterator = FavoriteShopFragment.this.mType2FilterItems.iterator();
              if (localIterator.hasNext())
              {
                localDPObject = (DPObject)localIterator.next();
                bool = true;
                if ((localDPObject.isClass("Tag")) || (localDPObject.isClass("Category")))
                {
                  localFavoriteHelper = FavoriteShopFragment.this.mFavoriteHelper;
                  str1 = localDPObject.getString("Name");
                  if (!localDPObject.isClass("Tag"))
                    break label608;
                  i = 2;
                  str2 = FavoriteShopFragment.this.mCurrentType1FilterItem.getString("Name");
                  if (!FavoriteShopFragment.this.mCurrentType1FilterItem.isClass("City"))
                    break label614;
                }
                for (j = 0; ; j = 1)
                {
                  bool = localFavoriteHelper.hasFilterItem(str1, i, str2, j);
                  if (!bool)
                    break;
                  paramObject.add(localDPObject);
                  break;
                  i = 3;
                  break label546;
                }
              }
              ((TwinListFilterDialog)localObject).setItems((DPObject[])paramObject.toArray(new DPObject[paramObject.size()]));
              if (FavoriteShopFragment.this.mCurrentType2FilterItem == null)
                break label700;
              paramObject = FavoriteShopFragment.this.mCurrentType2FilterItem;
            }
            while (true)
            {
              ((TwinListFilterDialog)localObject).setSelectedItem(paramObject);
              ((TwinListFilterDialog)localObject).show(paramView);
              return;
              ((TwinListFilterDialog)localObject).setItems((DPObject[])FavoriteShopFragment.this.mType2FilterItems.toArray(new DPObject[FavoriteShopFragment.this.mType2FilterItems.size()]));
              break;
              if (FavoriteShopFragment.this.mType2FilterItems.size() != 0)
              {
                paramObject = (DPObject)FavoriteShopFragment.this.mType2FilterItems.get(0);
                continue;
              }
              paramObject = null;
            }
          }
          while (!paramObject.equals("排序"));
          label546: label700: Object localObject = new ListFilterDialog(FavoriteShopFragment.this.getActivity());
          ((ListFilterDialog)localObject).setTag(paramObject);
          ((ListFilterDialog)localObject).setOnFilterListener(FavoriteShopFragment.this);
          ((ListFilterDialog)localObject).setItems((DPObject[])FavoriteShopFragment.this.mType3FilterItems.toArray(new DPObject[FavoriteShopFragment.this.mType3FilterItems.size()]));
          if (FavoriteShopFragment.this.mCurrentType3FilterItem != null)
            paramObject = FavoriteShopFragment.this.mCurrentType3FilterItem;
          while (true)
          {
            ((ListFilterDialog)localObject).setSelectedItem(paramObject);
            ((ListFilterDialog)localObject).show(paramView);
            return;
            if (FavoriteShopFragment.this.mType3FilterItems.size() != 0)
            {
              paramObject = (DPObject)FavoriteShopFragment.this.mType3FilterItems.get(0);
              continue;
            }
            paramObject = null;
          }
        }
      });
      return paramViewGroup;
      paramLayoutInflater = this.mCurrentType1FilterItem.getString("Name");
      break;
      paramLayoutInflater = this.mCurrentType2FilterItem.getString("Name");
      break label109;
    }
  }

  public void onDeleteButtonClicked()
  {
    Object localObject = this.mFavoriteListCurrentAdapter.getDeleteList();
    StringBuilder localStringBuilder = new StringBuilder();
    this.mRemovedIds = new Integer[((ArrayList)localObject).size()];
    int i = 0;
    localObject = ((ArrayList)localObject).iterator();
    while (((Iterator)localObject).hasNext())
    {
      int j = ((DPObject)((Iterator)localObject).next()).getInt("ID");
      localStringBuilder.append(String.valueOf(j)).append(",");
      this.mRemovedIds[i] = Integer.valueOf(j);
      i += 1;
    }
    if (localStringBuilder.length() > 0)
    {
      localObject = accountService();
      this.mRemoveFavoriteRequest = BasicMApiRequest.mapiPost("http://m.api.dianping.com/delfavorshops.bin", new String[] { "shopids", localStringBuilder.substring(0, localStringBuilder.length() - 1), "token", ((AccountService)localObject).token() });
      mapiService().exec(this.mRemoveFavoriteRequest, this);
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mRemoveFavoriteRequest != null)
      mapiService().abort(this.mRemoveFavoriteRequest, null, true);
    this.mFavoriteHelper.removeFavoriteListener(this);
    this.mFavoriteHelper = null;
    this.mFavoriteListByTimeAdapter.onFinish();
    this.mFavoriteListByDistanceAdapter.onFinish();
  }

  public void onEditModeChanged(boolean paramBoolean)
  {
    if (this.mFavoriteListCurrentAdapter != null)
    {
      ((FavoriteBaseActivity)getActivity()).setButtonView(this.mFavoriteListCurrentAdapter.getCheckedSize());
      this.mFavoriteListCurrentAdapter.resetCheckList();
      this.mFavoriteListCurrentAdapter.setIsEdit(paramBoolean);
    }
  }

  public void onFilter(FilterDialog paramFilterDialog, Object paramObject)
  {
    Object localObject = paramFilterDialog.getTag();
    if ("地区".equals(localObject))
      if (paramObject != this.mCurrentType1FilterItem)
      {
        this.mCurrentType1FilterItem = ((DPObject)paramObject);
        localObject = this.mCurrentType1FilterItem.getString("Name");
        paramObject = localObject;
        if (this.mCurrentType1FilterItem.getInt("ParentID") == 0)
        {
          paramObject = localObject;
          if (!((String)localObject).startsWith("全部"))
            paramObject = "全部" + (String)localObject;
        }
        this.mFilterBar.setItem("地区", paramObject);
        this.mFavoriteListCurrentAdapter.refreshShownShopList(true, true);
        GAHelper.instance().contextStatisticsEvent(getActivity(), "filter_district", null, "tap");
      }
    do
      while (true)
      {
        paramFilterDialog.dismiss();
        ((FavoriteBaseActivity)getActivity()).setActivityIsEdit(false);
        return;
        if (!"类别".equals(localObject))
          break;
        if (paramObject == this.mCurrentType2FilterItem)
          continue;
        this.mCurrentType2FilterItem = ((DPObject)paramObject);
        localObject = this.mCurrentType2FilterItem.getString("Name");
        paramObject = localObject;
        if (this.mCurrentType2FilterItem.getInt("ParentID") == 0)
        {
          paramObject = localObject;
          if (!((String)localObject).startsWith("全部"))
            paramObject = "全部" + (String)localObject;
        }
        this.mFilterBar.setItem("类别", paramObject);
        this.mFavoriteListCurrentAdapter.refreshShownShopList(true, true);
        GAHelper.instance().contextStatisticsEvent(getActivity(), "filter_category", null, "tap");
      }
    while ((!"排序".equals(localObject)) || (paramObject == this.mCurrentType3FilterItem));
    this.mCurrentType3FilterItem = ((DPObject)paramObject);
    this.mFilterBar.setItem("排序", this.mCurrentType3FilterItem.getString("Name"));
    if (this.mCurrentType3FilterItem.getInt("ID") == 0);
    for (paramObject = this.mFavoriteListByTimeAdapter; ; paramObject = this.mFavoriteListByDistanceAdapter)
    {
      this.mFavoriteListCurrentAdapter = paramObject;
      this.mFavoriteListCurrentAdapter.refreshShownShopList(false, true);
      this.mListView.setAdapter(this.mFavoriteListCurrentAdapter);
      GAHelper.instance().contextStatisticsEvent(getActivity(), "filter_time_space", null, "tap");
      break;
    }
  }

  public void onImageSwitchChanged()
  {
    if (this.mFavoriteListCurrentAdapter != null)
      this.mFavoriteListCurrentAdapter.notifyDataSetChanged();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = this.mFavoriteListCurrentAdapter.getItem(paramInt);
    if (((paramAdapterView instanceof DPObject)) && (((DPObject)paramAdapterView).isClass("Shop")))
    {
      if (((NovaTabFragmentActivity)getActivity()).isEdit())
      {
        this.mFavoriteListCurrentAdapter.itemBeChecked(paramInt);
        ((NovaTabFragmentActivity)getActivity()).setButtonView(this.mFavoriteListCurrentAdapter.getCheckedSize());
      }
    }
    else
      return;
    paramAdapterView = (DPObject)paramAdapterView;
    paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + paramAdapterView.getInt("ID")));
    paramView.putExtra("shop", paramAdapterView);
    startActivity(paramView);
    paramView = ((NovaActivity)getActivity()).getCloneUserInfo();
    paramView.shop_id = Integer.valueOf(paramAdapterView.getInt("ID"));
    GAHelper.instance().contextStatisticsEvent(getActivity(), "list_item", paramView, "tap");
  }

  public void onLocationChanged(LocationService paramLocationService)
  {
    Location localLocation = location();
    if (localLocation != null)
    {
      if (((this.latitude == 0.0D) || (this.longitude == 0.0D)) && (this.mOrderMode == 1))
        if (this.mUserId <= 0)
          break label134;
      label134: for (paramLocationService = "TA还没收藏过商户，真可惜……"; ; paramLocationService = "您还没有收藏过商户哦")
      {
        showEmptyView(paramLocationService);
        this.offsetLatitude = localLocation.offsetLatitude();
        this.offsetLongitude = localLocation.offsetLongitude();
        this.latitude = localLocation.latitude();
        this.longitude = localLocation.longitude();
        int i = 0;
        while (i < this.mListView.getChildCount())
        {
          paramLocationService = this.mListView.getChildAt(i);
          if ((paramLocationService instanceof ShopListItem))
            ((ShopListItem)paramLocationService).refreshDistance(this.offsetLatitude, this.offsetLongitude);
          i += 1;
        }
      }
    }
  }

  public void onRefreshComplete(FavoriteHelper.FavoriteInfo paramFavoriteInfo, String paramString)
  {
    showLoadingView(false);
    if (paramFavoriteInfo == null)
      this.mFailedView = showFailedView("网络不给力哦，请稍后再试", true, new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          FavoriteShopFragment.this.mFailedView.setVisibility(8);
          FavoriteShopFragment.this.mFavoriteHelper.refresh(FavoriteShopFragment.this.mUserId, true);
        }
      });
    label922: 
    while (true)
    {
      return;
      int i = 3;
      this.mType1FilterItems.add(new DPObject("CityGroup").edit().putInt("ID", 1).putString("Name", "全部地区").putInt("ParentID", 0).generate());
      boolean bool1 = this.isOversea;
      Object localObject1 = city().name();
      int j = 0;
      Object localObject2;
      while (j < paramFavoriteInfo.cities.size())
      {
        localObject2 = new DPObject("City").edit().putInt("ID", j + 2).putString("Name", (String)paramFavoriteInfo.cities.get(j)).putInt("ParentID", 0).generate();
        this.mType1FilterItems.add(localObject2);
        boolean bool2 = bool1;
        if (bool1)
        {
          bool2 = bool1;
          if (((String)localObject1).equals(paramFavoriteInfo.cities.get(j)))
          {
            this.mCurrentType1FilterItem = ((DPObject)localObject2);
            this.mFilterBar.setItem("地区", (String)paramFavoriteInfo.cities.get(j));
            bool2 = false;
          }
        }
        localObject2 = (ArrayList)paramFavoriteInfo.regions.get(paramFavoriteInfo.cities.get(j));
        int k = i;
        if (localObject2 != null)
        {
          localObject2 = ((ArrayList)localObject2).iterator();
          while (true)
          {
            k = i;
            if (!((Iterator)localObject2).hasNext())
              break;
            String str = (String)((Iterator)localObject2).next();
            if (str == null)
              continue;
            this.mType1FilterItems.add(new DPObject("Region").edit().putInt("ID", i).putString("Name", str).putInt("ParentID", j + 2).generate());
            i += 1;
          }
        }
        j += 1;
        i = k;
        bool1 = bool2;
      }
      this.mType2FilterItems.add(new DPObject("CategoryGroup").edit().putInt("ID", 1).putString("Name", "分类").putInt("ParentID", 0).generate());
      localObject1 = paramFavoriteInfo.categories.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (String)((Iterator)localObject1).next();
        if (localObject2 == null)
          continue;
        this.mType2FilterItems.add(new DPObject("Category").edit().putInt("ID", i).putString("Name", (String)localObject2).putInt("ParentID", 1).generate());
        i += 1;
      }
      this.mType2FilterItems.add(new DPObject("TagGroup").edit().putInt("ID", 2).putString("Name", "标签").putInt("ParentID", 0).generate());
      localObject1 = paramFavoriteInfo.types.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (String)((Iterator)localObject1).next();
        if (localObject2 == null)
          continue;
        this.mType2FilterItems.add(new DPObject("Tag").edit().putInt("ID", i).putString("Name", (String)localObject2).putInt("ParentID", 2).generate());
        i += 1;
      }
      this.mType3FilterItems.add(new DPObject("Order").edit().putInt("ID", 0).putString("Name", "按时间排序").generate());
      this.mType3FilterItems.add(new DPObject("Order").edit().putInt("ID", 1).putString("Name", "按距离排序").generate());
      this.ids = ((String[])paramFavoriteInfo.ids.toArray(new String[0]));
      this.idsRemoved = new boolean[this.ids.length];
      Log.d("FavoriteShopFragment", "get favorite shop ids=" + this.ids);
      if (this.ids.length != 0)
        break;
      if (TextUtils.isEmpty(paramString))
        if (this.mUserId > 0)
        {
          paramFavoriteInfo = "TA还没收藏过商户，真可惜……";
          showEmptyView(paramFavoriteInfo);
        }
      while (true)
      {
        if (!(getActivity() instanceof NovaTabFragmentActivity))
          break label922;
        ((NovaTabFragmentActivity)getActivity()).setActivityEditable(false, this);
        return;
        paramFavoriteInfo = "您还没有收藏过商户哦";
        break;
        this.fShopListEmptyMsg = paramString;
        showEmptyView(paramString);
      }
    }
    this.mContentView.setVisibility(0);
    this.mEmptyView.setVisibility(8);
    this.mListView.setAdapter(this.mFavoriteListCurrentAdapter);
  }

  public void onRefreshStart()
  {
    showLoadingView(true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mRemoveFavoriteRequest)
    {
      this.mRemoveFavoriteRequest = null;
      dismissDialog();
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mRemoveFavoriteRequest)
    {
      this.mRemoveFavoriteRequest = null;
      dismissDialog();
      paramMApiRequest = this.mFavoriteListCurrentAdapter.getDeleteList();
      this.mFavoriteListByTimeAdapter.deleteShops(paramMApiRequest);
      this.mFavoriteListByDistanceAdapter.deleteShops(paramMApiRequest);
      updateAfterRemove(this.mRemovedIds);
      this.mRemovedIds = null;
      ((FavoriteBaseActivity)getActivity()).setActivityIsEdit(false);
    }
  }

  public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
  {
  }

  public void onRequestStart(MApiRequest paramMApiRequest)
  {
    if (paramMApiRequest == this.mRemoveFavoriteRequest)
      showProgressDialog("正在删除..");
  }

  public void onResume()
  {
    super.onResume();
    if ((this.ids == null) || (this.ids.length == 0));
    ArrayList localArrayList;
    label267: 
    do
    {
      do
        return;
      while (this.mUserId != 0);
      localArrayList = new ArrayList();
      Object localObject = FavoriteHelper.getFavoriteShop();
      String[] arrayOfString;
      if (localObject != null)
      {
        j = 0;
        arrayOfString = this.ids;
        k = arrayOfString.length;
        i = 0;
        while (true)
        {
          if (i >= k)
            break label267;
          String str = arrayOfString[i];
          if ((!((List)localObject).contains(str)) && (this.idsRemoved[j] == 0));
          try
          {
            localArrayList.add(Integer.valueOf(str));
            this.idsRemoved[j] = true;
            Log.d("FavoriteShopFragment", "shop " + str + " is removed");
            j += 1;
            i += 1;
          }
          catch (NumberFormatException localNumberFormatException2)
          {
            while (true)
              localNumberFormatException2.printStackTrace();
          }
        }
      }
      int j = 0;
      localObject = this.ids;
      int k = localObject.length;
      int i = 0;
      while (true)
        if (i < k)
        {
          arrayOfString = localObject[i];
          try
          {
            localArrayList.add(Integer.valueOf(arrayOfString));
            this.idsRemoved[j] = true;
            j += 1;
            Log.d("FavoriteShopFragment", "shop " + arrayOfString + " is removed");
            i += 1;
          }
          catch (NumberFormatException localNumberFormatException1)
          {
            while (true)
              localNumberFormatException1.printStackTrace();
          }
        }
    }
    while (localArrayList.size() <= 0);
    Log.d("FavoriteShopFragment", localArrayList.size() + " shops are removed. update adapter");
    this.mFavoriteListByTimeAdapter.deleteShopsByIds(localArrayList);
    this.mFavoriteListByDistanceAdapter.deleteShopsByIds(localArrayList);
    updateAfterRemove((Integer[])localArrayList.toArray(new Integer[localArrayList.size()]));
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    this.mFavoriteHelper.onSaveInstanceState(paramBundle);
    this.mFavoriteListByTimeAdapter.onSaveInstanceState(paramBundle);
    this.mFavoriteListByDistanceAdapter.onSaveInstanceState(paramBundle);
    paramBundle.putStringArray("ids", this.ids);
    paramBundle.putBooleanArray("idsRemoved", this.idsRemoved);
    paramBundle.putInt("userid", this.mUserId);
    paramBundle.putParcelableArrayList("filter1", this.mType1FilterItems);
    paramBundle.putParcelableArrayList("filter2", this.mType2FilterItems);
    paramBundle.putParcelableArrayList("filter3", this.mType3FilterItems);
    paramBundle.putParcelable("currentfilter1", this.mCurrentType1FilterItem);
    paramBundle.putParcelable("currentfilter2", this.mCurrentType2FilterItem);
    paramBundle.putParcelable("currentfilter3", this.mCurrentType3FilterItem);
    paramBundle.putString("fShopListEmptyMsg", this.fShopListEmptyMsg);
    super.onSaveInstanceState(paramBundle);
  }

  private class Adapter extends MulDeleListAdapter
  {
    private ArrayList<DPObject> allshops = new ArrayList();
    private String emptyMsg;
    private String errorMsg;
    private RequestHandler<MApiRequest, MApiResponse> handler = new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (paramMApiRequest == FavoriteShopFragment.Adapter.this.request)
        {
          FavoriteShopFragment.Adapter.this.request = null;
          FavoriteShopFragment.Adapter.this.setError(paramMApiResponse.message().toString());
        }
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if (paramMApiRequest == FavoriteShopFragment.Adapter.this.request)
        {
          FavoriteShopFragment.Adapter.this.request = null;
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          if ((paramMApiRequest != null) && (paramMApiRequest.getArray("List") != null))
            FavoriteShopFragment.Adapter.this.appendShops(paramMApiRequest);
        }
      }

      public void onRequestProgress(MApiRequest paramMApiRequest, int paramInt1, int paramInt2)
      {
      }

      public void onRequestStart(MApiRequest paramMApiRequest)
      {
      }
    };
    private boolean isEnd;
    private int mode;
    private int nextStartIndex;
    MApiRequest request;
    private ArrayList<DPObject> shownshops = this.dataList;

    public Adapter(int arg2)
    {
      int i;
      this.mode = i;
    }

    public void appendShops(DPObject paramDPObject)
    {
      DPObject[] arrayOfDPObject = paramDPObject.getArray("List");
      if (arrayOfDPObject != null)
      {
        this.emptyMsg = paramDPObject.getString("EmptyMsg");
        int i = arrayOfDPObject.length;
        this.nextStartIndex += i;
        int n;
        label77: DPObject localDPObject;
        int m;
        int i1;
        int j;
        if (this.nextStartIndex >= FavoriteShopFragment.this.ids.length)
        {
          n = 1;
          this.isEnd = n;
          paramDPObject = new ArrayList(arrayOfDPObject.length);
          int k = arrayOfDPObject.length;
          i = 0;
          if (i < k)
          {
            localDPObject = arrayOfDPObject[i];
            m = localDPObject.getInt("ID");
            i1 = 0;
            j = 0;
          }
        }
        else
        {
          while (true)
          {
            n = i1;
            if (j < FavoriteShopFragment.this.ids.length)
            {
              if (FavoriteShopFragment.this.ids[j].equals(String.valueOf(m)))
                n = FavoriteShopFragment.this.idsRemoved[j];
            }
            else
            {
              if (n == 0)
                paramDPObject.add(localDPObject);
              i += 1;
              break label77;
              n = 0;
              break;
            }
            j += 1;
          }
        }
        this.allshops.addAll(paramDPObject);
        refreshShownShopList(false, false);
        appendCheckList(arrayOfDPObject.length);
        notifyDataSetChanged();
      }
    }

    public boolean areAllItemsEnabled()
    {
      return true;
    }

    public void deleteShops(ArrayList<DPObject> paramArrayList)
    {
      Iterator localIterator1 = paramArrayList.iterator();
      while (localIterator1.hasNext())
      {
        DPObject localDPObject = (DPObject)localIterator1.next();
        Object localObject = null;
        Iterator localIterator2 = this.shownshops.iterator();
        do
        {
          paramArrayList = localObject;
          if (!localIterator2.hasNext())
            break;
          paramArrayList = (DPObject)localIterator2.next();
        }
        while (paramArrayList.getInt("ID") != localDPObject.getInt("ID"));
        if (paramArrayList == null)
          continue;
        this.shownshops.remove(paramArrayList);
        this.allshops.remove(paramArrayList);
      }
      resetCheckList();
      notifyDataSetChanged();
    }

    public void deleteShopsByIds(ArrayList<Integer> paramArrayList)
    {
      Iterator localIterator1 = paramArrayList.iterator();
      while (localIterator1.hasNext())
      {
        int i = ((Integer)localIterator1.next()).intValue();
        Object localObject = null;
        Iterator localIterator2 = this.shownshops.iterator();
        do
        {
          paramArrayList = localObject;
          if (!localIterator2.hasNext())
            break;
          paramArrayList = (DPObject)localIterator2.next();
        }
        while (paramArrayList.getInt("ID") != i);
        if (paramArrayList == null)
          continue;
        this.shownshops.remove(paramArrayList);
        this.allshops.remove(paramArrayList);
      }
      notifyDataSetChanged();
    }

    public int getCount()
    {
      if (this.isEnd)
        return this.shownshops.size();
      return this.shownshops.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < this.shownshops.size())
        return this.shownshops.get(paramInt);
      if (this.errorMsg == null)
        return LOADING;
      return ERROR;
    }

    public long getItemId(int paramInt)
    {
      Object localObject = getItem(paramInt);
      if (((localObject instanceof DPObject)) && (((DPObject)localObject).isClass("Shop")))
        return ((DPObject)localObject).getInt("ID");
      if (localObject == LOADING)
        return -paramInt;
      return -2147483648L;
    }

    public int getItemViewType(int paramInt)
    {
      Object localObject = getItem(paramInt);
      if (((localObject instanceof DPObject)) && (((DPObject)localObject).isClass("Shop")))
        return 0;
      if (localObject == LOADING)
        return 1;
      return 2;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if (((localObject instanceof DPObject)) && (((DPObject)localObject).isClass("Shop")))
      {
        DPObject localDPObject = (DPObject)localObject;
        paramInt = getChecked(paramInt);
        if ((paramView instanceof ShopListItem));
        for (paramView = (ShopListItem)paramView; ; paramView = null)
        {
          localObject = paramView;
          if (paramView == null)
            localObject = (ShopListItem)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.shop_item, paramViewGroup, false);
          ((ShopListItem)localObject).setShop(localDPObject, -1, FavoriteShopFragment.this.offsetLatitude, FavoriteShopFragment.this.offsetLongitude, FavoriteShopFragment.this.getActivity());
          ((ShopListItem)localObject).showOrHideShopImg(NovaTabFragmentActivity.isShowShopImg);
          ((ShopListItem)localObject).setEditable(this.isEdit);
          ((ShopListItem)localObject).setChecked(paramInt);
          return localObject;
        }
      }
      if (localObject == LOADING)
      {
        if (this.errorMsg == null)
          loadNewPage();
        return getLoadingView(paramViewGroup, paramView);
      }
      return (View)getFailedView(this.errorMsg, new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          FavoriteShopFragment.Adapter.this.reset();
        }
      }
      , paramViewGroup, paramView);
    }

    public int getViewTypeCount()
    {
      return 3;
    }

    public boolean hasStableIds()
    {
      return true;
    }

    public boolean loadNewPage()
    {
      if (this.isEnd);
      do
      {
        return false;
        if ((FavoriteShopFragment.this.ids != null) && (FavoriteShopFragment.this.ids.length != 0))
          continue;
        setEmpty();
        return false;
      }
      while (this.request != null);
      this.errorMsg = null;
      shopList(this.mode, this.nextStartIndex);
      return true;
    }

    public void onFinish()
    {
      if (this.request != null)
      {
        FavoriteShopFragment.this.mapiService().abort(this.request, null, true);
        this.request = null;
      }
    }

    public void onRestoreInstanceState(Bundle paramBundle)
    {
      if (this.mode == 0)
      {
        str = "time_shops";
        this.allshops = paramBundle.getParcelableArrayList(str);
        if (this.mode != 0)
          break label158;
        str = "time_isEnd";
        label31: this.isEnd = paramBundle.getBoolean(str);
        if (this.mode != 0)
          break label165;
        str = "time_error";
        label51: this.errorMsg = paramBundle.getString(str);
        if (this.mode != 0)
          break label172;
        str = "time_emptyMsg";
        label71: this.emptyMsg = paramBundle.getString(str);
        if (this.mode != 0)
          break label179;
        str = "time_nextStartIndex";
        label91: this.nextStartIndex = paramBundle.getInt(str);
        if (this.mode != 0)
          break label186;
        str = "time_checkedSize";
        label111: this.checkedSize = paramBundle.getInt(str);
        if (this.mode != 0)
          break label193;
      }
      label158: label165: label172: label179: label186: label193: for (String str = "time_checkList"; ; str = "distance_checkList")
      {
        this.checkList = paramBundle.getIntegerArrayList(str);
        refreshShownShopList(false, true);
        notifyDataSetChanged();
        return;
        str = "distance_shops";
        break;
        str = "distance_isEnd";
        break label31;
        str = "distance_error";
        break label51;
        str = "distance_emptyMsg";
        break label71;
        str = "distance_nextStartIndex";
        break label91;
        str = "distance_checkedSize";
        break label111;
      }
    }

    public void onSaveInstanceState(Bundle paramBundle)
    {
      if (this.mode == 0)
      {
        str = "time_shops";
        paramBundle.putParcelableArrayList(str, this.allshops);
        if (this.mode != 0)
          break label148;
        str = "time_isEnd";
        label31: paramBundle.putBoolean(str, this.isEnd);
        if (this.mode != 0)
          break label155;
        str = "time_error";
        label51: paramBundle.putString(str, this.errorMsg);
        if (this.mode != 0)
          break label162;
        str = "time_emptyMsg";
        label71: paramBundle.putString(str, this.emptyMsg);
        if (this.mode != 0)
          break label169;
        str = "time_nextStartIndex";
        label91: paramBundle.putInt(str, this.nextStartIndex);
        if (this.mode != 0)
          break label176;
        str = "time_checkedSize";
        label111: paramBundle.putInt(str, this.checkedSize);
        if (this.mode != 0)
          break label183;
      }
      label148: label155: label162: label169: label176: label183: for (String str = "time_checkList"; ; str = "distance_checkList")
      {
        paramBundle.putIntegerArrayList(str, this.checkList);
        return;
        str = "distance_shops";
        break;
        str = "distance_isEnd";
        break label31;
        str = "distance_error";
        break label51;
        str = "distance_emptyMsg";
        break label71;
        str = "distance_nextStartIndex";
        break label91;
        str = "distance_checkedSize";
        break label111;
      }
    }

    void refreshShownShopList(boolean paramBoolean1, boolean paramBoolean2)
    {
      this.shownshops.clear();
      Object localObject3 = null;
      Object localObject4 = null;
      Object localObject5 = null;
      Object localObject6 = null;
      Object localObject1 = localObject3;
      Object localObject2 = localObject4;
      if (FavoriteShopFragment.this.mCurrentType1FilterItem != null)
      {
        if (FavoriteShopFragment.this.mCurrentType1FilterItem.isClass("Group"))
        {
          localObject2 = localObject4;
          localObject1 = localObject3;
        }
      }
      else
      {
        localObject3 = localObject5;
        localObject4 = localObject6;
        if (FavoriteShopFragment.this.mCurrentType2FilterItem != null)
        {
          if (!FavoriteShopFragment.this.mCurrentType2FilterItem.isClass("Category"))
            break label272;
          localObject3 = FavoriteShopFragment.this.mCurrentType2FilterItem.getString("Name");
          localObject4 = localObject6;
        }
      }
      while (true)
      {
        localObject1 = FavoriteShopFragment.this.mFavoriteHelper.getMatchedShops((String)localObject1, (String)localObject2, (String)localObject3, (String)localObject4);
        localObject2 = this.allshops.iterator();
        while (((Iterator)localObject2).hasNext())
        {
          localObject3 = (DPObject)((Iterator)localObject2).next();
          if (!((HashSet)localObject1).contains(Integer.valueOf(((DPObject)localObject3).getInt("ID"))))
            continue;
          this.shownshops.add(localObject3);
        }
        if (FavoriteShopFragment.this.mCurrentType1FilterItem.isClass("City"))
        {
          localObject1 = FavoriteShopFragment.this.mCurrentType1FilterItem.getString("Name");
          localObject2 = localObject4;
          break;
        }
        localObject1 = localObject3;
        localObject2 = localObject4;
        if (!FavoriteShopFragment.this.mCurrentType1FilterItem.isClass("Region"))
          break;
        localObject2 = FavoriteShopFragment.this.mCurrentType1FilterItem.getString("Name");
        localObject1 = localObject3;
        break;
        label272: localObject3 = localObject5;
        localObject4 = localObject6;
        if (!FavoriteShopFragment.this.mCurrentType2FilterItem.isClass("Tag"))
          continue;
        localObject4 = FavoriteShopFragment.this.mCurrentType2FilterItem.getString("Name");
        localObject3 = localObject5;
      }
      if (paramBoolean2)
        resetCheckList();
      if (paramBoolean1)
        notifyDataSetChanged();
    }

    public void reset()
    {
      onFinish();
      this.allshops.clear();
      this.shownshops.clear();
      this.nextStartIndex = 0;
      this.isEnd = false;
      this.errorMsg = null;
      notifyDataSetChanged();
    }

    public void setEmpty()
    {
      onFinish();
      this.allshops.clear();
      this.shownshops.clear();
      this.isEnd = true;
      this.errorMsg = null;
      notifyDataSetChanged();
    }

    public void setError(String paramString)
    {
      this.errorMsg = paramString;
      notifyDataSetChanged();
    }

    void shopList(int paramInt1, int paramInt2)
    {
      Object localObject = Location.FMT;
      StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/");
      localStringBuilder.append("shoplist.bin?");
      if (paramInt1 == 1)
      {
        if ((FavoriteShopFragment.this.latitude != 0.0D) && (FavoriteShopFragment.this.longitude != 0.0D))
        {
          localStringBuilder.append("&lat=").append(((NumberFormat)localObject).format(FavoriteShopFragment.this.latitude));
          localStringBuilder.append("&lng=").append(((NumberFormat)localObject).format(FavoriteShopFragment.this.longitude));
        }
        localStringBuilder.append("&start=").append(paramInt2);
      }
      localObject = new StringBuilder();
      if (paramInt1 == 0)
      {
        int i = paramInt2 + 25;
        if (FavoriteShopFragment.this.ids != null)
        {
          paramInt1 = i;
          if (FavoriteShopFragment.this.ids.length < i)
            paramInt1 = FavoriteShopFragment.this.ids.length;
        }
        while (true)
        {
          paramInt1 -= 1;
          if (paramInt1 < 0)
            break;
          while (true)
            if (paramInt2 < paramInt1)
            {
              ((StringBuilder)localObject).append(FavoriteShopFragment.this.ids[paramInt2]).append(',');
              paramInt2 += 1;
              continue;
              paramInt1 = 0;
              break;
            }
          ((StringBuilder)localObject).append(FavoriteShopFragment.this.ids[paramInt1]);
        }
      }
      while (true)
      {
        this.request = BasicMApiRequest.mapiPost(localStringBuilder.toString(), new String[] { "ids", ((StringBuilder)localObject).toString() });
        FavoriteShopFragment.this.mapiService().exec(this.request, this.handler);
        return;
        paramInt2 = FavoriteShopFragment.this.ids.length - 1;
        paramInt1 = 0;
        while (paramInt1 < paramInt2)
        {
          ((StringBuilder)localObject).append(FavoriteShopFragment.this.ids[paramInt1]).append(',');
          paramInt1 += 1;
        }
        ((StringBuilder)localObject).append(FavoriteShopFragment.this.ids[paramInt2]);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.favorite.FavoriteShopFragment
 * JD-Core Version:    0.6.0
 */