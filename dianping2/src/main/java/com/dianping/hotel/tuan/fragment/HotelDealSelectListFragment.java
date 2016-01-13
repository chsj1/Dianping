package com.dianping.hotel.tuan.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.tuan.fragment.BaseTuanFragment;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class HotelDealSelectListFragment extends BaseTuanFragment
{
  static DPObject extradataDP;
  protected ArrayList<DealStatusHodler> dealStatusList = new ArrayList();
  ListAdapter mAdapter;
  protected DPObject mDeal;
  CharSequence mEmptyText;
  View mEmptyView;
  protected Bundle mExtraData;
  private final Handler mHandler = new Handler();
  ListView mList;
  View mListContainer;
  boolean mListShown;
  private final AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      HotelDealSelectListFragment.this.onListItemClick((ListView)paramAdapterView, paramView, paramInt, paramLong);
    }
  };
  private final AdapterView.OnItemLongClickListener mOnLongClickListener = new AdapterView.OnItemLongClickListener()
  {
    public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      return HotelDealSelectListFragment.this.onListItemLongClick((ListView)paramAdapterView, paramView, paramInt, paramLong);
    }
  };
  View mProgressContainer;
  private final Runnable mRequestFocus = new Runnable()
  {
    public void run()
    {
      HotelDealSelectListFragment.this.mList.focusableViewAvailable(HotelDealSelectListFragment.this.mList);
    }
  };
  TextView mStandardEmptyView;
  protected DPObject[] statusList;

  private void ensureList()
  {
    if (this.mList != null)
      return;
    Object localObject = getView();
    if (localObject == null)
      throw new IllegalStateException("Content view not yet created");
    if ((localObject instanceof ListView))
    {
      this.mList = ((ListView)localObject);
      this.mListShown = true;
      this.mList.setOnItemClickListener(this.mOnClickListener);
      this.mList.setOnItemLongClickListener(this.mOnLongClickListener);
      if (this.mAdapter == null)
        break label270;
      localObject = this.mAdapter;
      this.mAdapter = null;
      setListAdapter((ListAdapter)localObject);
    }
    while (true)
    {
      this.mHandler.post(this.mRequestFocus);
      return;
      this.mStandardEmptyView = ((TextView)((View)localObject).findViewById(16908292));
      if (this.mStandardEmptyView == null)
        this.mEmptyView = ((View)localObject).findViewById(16908292);
      while (true)
      {
        this.mProgressContainer = ((View)localObject).findViewById(R.id.progressContainer);
        this.mListContainer = ((View)localObject).findViewById(R.id.listContainer);
        localObject = ((View)localObject).findViewById(16908298);
        if ((localObject instanceof ListView))
          break;
        throw new RuntimeException("Content has view with id attribute 'android.R.id.list' that is not a ListView class");
        this.mStandardEmptyView.setVisibility(8);
      }
      this.mList = ((ListView)localObject);
      if (this.mList == null)
        throw new RuntimeException("Your content must have a ListView whose id attribute is 'android.R.id.list'");
      if (this.mEmptyView != null)
      {
        this.mList.setEmptyView(this.mEmptyView);
        break;
      }
      if (this.mEmptyText == null)
        break;
      this.mStandardEmptyView.setText(this.mEmptyText);
      this.mList.setEmptyView(this.mStandardEmptyView);
      break;
      label270: if (this.mProgressContainer == null)
        continue;
      setListShown(false, false);
    }
  }

  private int getHotelDealInfoStatusByDealId(DPObject paramDPObject)
  {
    int k = 0;
    int j = k;
    DPObject[] arrayOfDPObject;
    int m;
    int i;
    if (this.statusList != null)
    {
      j = k;
      if (this.statusList.length > 0)
      {
        arrayOfDPObject = this.statusList;
        m = arrayOfDPObject.length;
        i = 0;
      }
    }
    while (true)
    {
      j = k;
      if (i < m)
      {
        DPObject localDPObject = arrayOfDPObject[i];
        if (paramDPObject.getInt("ID") != ((DPObject)localDPObject).getInt("DealId"))
          break label95;
        j = k;
        if (((DPObject)localDPObject).getInt("Status") == 1)
          j = 1;
      }
      return j;
      label95: i += 1;
    }
  }

  public static HotelDealSelectListFragment newInstance(FragmentActivity paramFragmentActivity, DPObject paramDPObject1, DPObject paramDPObject2)
  {
    HotelDealSelectListFragment localHotelDealSelectListFragment = new HotelDealSelectListFragment();
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("deal", paramDPObject1);
    localBundle.putParcelable("extradata", paramDPObject2);
    extradataDP = paramDPObject2;
    localHotelDealSelectListFragment.setArguments(localBundle);
    paramFragmentActivity = paramFragmentActivity.getSupportFragmentManager().beginTransaction();
    paramFragmentActivity.add(16908290, localHotelDealSelectListFragment);
    paramFragmentActivity.setTransition(4097);
    paramFragmentActivity.addToBackStack(null);
    paramFragmentActivity.commit();
    return localHotelDealSelectListFragment;
  }

  private void setListShown(boolean paramBoolean1, boolean paramBoolean2)
  {
    ensureList();
    if (this.mProgressContainer == null)
      throw new IllegalStateException("Can't be used with a custom content view");
    if (this.mListShown == paramBoolean1)
      return;
    this.mListShown = paramBoolean1;
    if (paramBoolean1)
    {
      if (paramBoolean2)
      {
        this.mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432577));
        this.mListContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432576));
      }
      while (true)
      {
        this.mProgressContainer.setVisibility(8);
        this.mListContainer.setVisibility(0);
        return;
        this.mProgressContainer.clearAnimation();
        this.mListContainer.clearAnimation();
      }
    }
    if (paramBoolean2)
    {
      this.mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432576));
      this.mListContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), 17432577));
    }
    while (true)
    {
      this.mProgressContainer.setVisibility(0);
      this.mListContainer.setVisibility(8);
      return;
      this.mProgressContainer.clearAnimation();
      this.mListContainer.clearAnimation();
    }
  }

  public ListAdapter getListAdapter()
  {
    return this.mAdapter;
  }

  public ListView getListView()
  {
    ensureList();
    return this.mList;
  }

  public long getSelectedItemId()
  {
    ensureList();
    return this.mList.getSelectedItemId();
  }

  public int getSelectedItemPosition()
  {
    ensureList();
    return this.mList.getSelectedItemPosition();
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    getListView().setPadding(10, 10, 10, 10);
    getListView().setHeaderDividersEnabled(false);
    getListView().setBackgroundResource(R.drawable.main_background);
    getActivity().setTitle("选择套餐");
    ((NovaActivity)getActivity()).removeTitleBarShadow();
    ((NovaActivity)getActivity()).addTitleBarShadow();
    setListAdapter(new Adapter(null));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mDeal = ((DPObject)getArguments().getParcelable("deal"));
    this.mExtraData = getArguments().getBundle("extradata");
    extradataDP = (DPObject)getArguments().getParcelable("extradata");
    if (extradataDP != null)
      this.statusList = extradataDP.getArray("StatusList");
    if (this.mDeal == null)
      getFragmentManager().popBackStackImmediate();
    while (true)
    {
      return;
      paramBundle = new ArrayList();
      paramBundle.addAll(Arrays.asList(this.mDeal.getArray("DealSelectList")));
      paramBundle = paramBundle.iterator();
      while (paramBundle.hasNext())
      {
        DPObject localDPObject = (DPObject)paramBundle.next();
        if ((this.statusList != null) && (this.statusList.length > 0))
        {
          Object localObject = this.statusList;
          int j = localObject.length;
          int i = 0;
          while (true)
          {
            if (i >= j)
              break label245;
            localDealStatusHodler = localObject[i];
            if (localDPObject.getInt("ID") == ((DPObject)localDealStatusHodler).getInt("DealId"))
            {
              localObject = new DealStatusHodler();
              ((DealStatusHodler)localObject).bookingDefaultInfo = ((DPObject)localDealStatusHodler).getObject("BookingDefaultInfo");
              ((DealStatusHodler)localObject).deal = localDPObject;
              this.dealStatusList.add(localObject);
              break;
            }
            i += 1;
          }
          label245: continue;
        }
        DealStatusHodler localDealStatusHodler = new DealStatusHodler();
        localDealStatusHodler.bookingDefaultInfo = null;
        localDealStatusHodler.deal = localDPObject;
        this.dealStatusList.add(localDealStatusHodler);
      }
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return paramLayoutInflater.inflate(R.layout.list_content, paramViewGroup, false);
  }

  public void onDestroyView()
  {
    this.mHandler.removeCallbacks(this.mRequestFocus);
    this.mList = null;
    this.mListShown = false;
    this.mListContainer = null;
    this.mProgressContainer = null;
    this.mEmptyView = null;
    this.mStandardEmptyView = null;
    super.onDestroyView();
  }

  public void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    paramListView = paramListView.getItemAtPosition(paramInt);
    if ((paramListView instanceof DealStatusHodler))
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://createorder"));
      paramView.addFlags(67108864);
      paramView.putExtra("deal", this.mDeal);
      paramView.putExtra("dealSelect", ((DealStatusHodler)paramListView).deal);
      Bundle localBundle;
      if ((extradataDP != null) && (getHotelDealInfoStatusByDealId(this.mDeal) == 1))
      {
        localBundle = new Bundle();
        localBundle.putParcelable("bookingDefaultInfo", extradataDP);
        paramView.putExtra("extradata", localBundle);
      }
      if (getHotelDealInfoStatusByDealId(((DealStatusHodler)paramListView).deal) == 1)
      {
        localBundle = new Bundle();
        localBundle.putParcelable("bookingDefaultInfo", ((DealStatusHodler)paramListView).bookingDefaultInfo);
        paramView.putExtra("extradata", localBundle);
      }
      startActivityForResult(paramView, 1);
      statisticsEvent("tuan5", "tuan5_choosedeal", "" + ((DealStatusHodler)paramListView).deal.getInt("ID"), 0);
    }
  }

  public boolean onListItemLongClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    return false;
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    ensureList();
  }

  public void setEmptyText(CharSequence paramCharSequence)
  {
    ensureList();
    if (this.mStandardEmptyView == null)
      throw new IllegalStateException("Can't be used with a custom content view");
    this.mStandardEmptyView.setText(paramCharSequence);
    if (this.mEmptyText == null)
      this.mList.setEmptyView(this.mStandardEmptyView);
    this.mEmptyText = paramCharSequence;
  }

  public void setListAdapter(ListAdapter paramListAdapter)
  {
    boolean bool = false;
    if (this.mAdapter != null);
    for (int i = 1; ; i = 0)
    {
      this.mAdapter = paramListAdapter;
      if (this.mList != null)
      {
        this.mList.setAdapter(paramListAdapter);
        if ((!this.mListShown) && (i == 0))
        {
          if (getView().getWindowToken() != null)
            bool = true;
          setListShown(true, bool);
        }
      }
      return;
    }
  }

  public void setListShown(boolean paramBoolean)
  {
    setListShown(paramBoolean, true);
  }

  public void setListShownNoAnimation(boolean paramBoolean)
  {
    setListShown(paramBoolean, false);
  }

  public void setSelection(int paramInt)
  {
    ensureList();
    this.mList.setSelection(paramInt);
  }

  private class Adapter extends BasicAdapter
  {
    private Adapter()
    {
    }

    public boolean areAllItemsEnabled()
    {
      return true;
    }

    public int getCount()
    {
      return HotelDealSelectListFragment.this.dealStatusList.size();
    }

    public Object getItem(int paramInt)
    {
      return HotelDealSelectListFragment.this.dealStatusList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject2 = getItem(paramInt);
      Object localObject1;
      if (paramView == null)
      {
        localObject1 = new HotelDealSelectListFragment.ViewHolder();
        paramView = HotelDealSelectListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.dealselect_list_item, paramViewGroup, false);
        ((HotelDealSelectListFragment.ViewHolder)localObject1).titleView = ((TextView)paramView.findViewById(16908308));
        ((HotelDealSelectListFragment.ViewHolder)localObject1).priceView = ((TextView)paramView.findViewById(16908309));
        ((HotelDealSelectListFragment.ViewHolder)localObject1).buyDealNum = ((TextView)paramView.findViewById(R.id.buy_deal_num));
        paramView.setBackgroundDrawable(HotelDealSelectListFragment.this.getResources().getDrawable(R.drawable.white_bg));
        paramView.setTag(localObject1);
        paramViewGroup = (ViewGroup)localObject1;
        localObject1 = ((HotelDealSelectListFragment.DealStatusHodler)localObject2).deal;
        if (((DPObject)localObject1).getInt("Status") != 2)
          break label257;
        paramViewGroup.titleView.setTextColor(Color.rgb(180, 172, 168));
        paramViewGroup.priceView.setTextColor(Color.rgb(180, 172, 168));
      }
      while (true)
      {
        paramViewGroup.titleView.setText(((DPObject)localObject1).getString("Title"));
        paramViewGroup.buyDealNum.setText(((DPObject)localObject1).getInt("Count") + "人已买");
        if (((DPObject)localObject1).getInt("Status") != 2)
          break label292;
        paramViewGroup.priceView.setText("已卖光");
        paramViewGroup.priceView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        return paramView;
        paramViewGroup = (HotelDealSelectListFragment.ViewHolder)paramView.getTag();
        break;
        label257: paramViewGroup.titleView.setTextColor(Color.rgb(77, 70, 65));
        paramViewGroup.priceView.setTextColor(Color.rgb(77, 70, 65));
      }
      label292: paramViewGroup.priceView.setText("¥" + ((DPObject)localObject1).getString("Price"));
      paramViewGroup.priceView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow, 0);
      return (View)paramView;
    }

    public boolean isEnabled(int paramInt)
    {
      Object localObject = getItem(paramInt);
      return (!(localObject instanceof DPObject)) || (((DPObject)localObject).getInt("Status") != 2);
    }
  }

  class DealStatusHodler
  {
    public DPObject bookingDefaultInfo;
    public DPObject deal;

    DealStatusHodler()
    {
    }
  }

  public static final class ViewHolder
  {
    public TextView buyDealNum;
    public TextView priceView;
    public TextView titleView;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.tuan.fragment.HotelDealSelectListFragment
 * JD-Core Version:    0.6.0
 */