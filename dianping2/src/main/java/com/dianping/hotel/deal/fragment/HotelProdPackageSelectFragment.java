package com.dianping.hotel.deal.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HotelProdPackageSelectFragment extends BaseTuanFragment
{
  private ListAdapter mAdapter;
  private CharSequence mEmptyText;
  private View mEmptyView;
  private final Handler mHandler = new Handler();
  private List<DPObject> mHotelProdPackageList = new ArrayList();
  private ListView mList;
  private View mListContainer;
  private boolean mListShown;
  private final AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      HotelProdPackageSelectFragment.this.onListItemClick((ListView)paramAdapterView, paramView, paramInt, paramLong);
    }
  };
  private final AdapterView.OnItemLongClickListener mOnLongClickListener = new AdapterView.OnItemLongClickListener()
  {
    public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      return HotelProdPackageSelectFragment.this.onListItemLongClick((ListView)paramAdapterView, paramView, paramInt, paramLong);
    }
  };
  private View mProgressContainer;
  private final Runnable mRequestFocus = new Runnable()
  {
    public void run()
    {
      HotelProdPackageSelectFragment.this.mList.focusableViewAvailable(HotelProdPackageSelectFragment.this.mList);
    }
  };
  private TextView mStandardEmptyView;

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

  public static HotelProdPackageSelectFragment newInstance(FragmentActivity paramFragmentActivity, ArrayList<DPObject> paramArrayList)
  {
    HotelProdPackageSelectFragment localHotelProdPackageSelectFragment = new HotelProdPackageSelectFragment();
    Bundle localBundle = new Bundle();
    localBundle.putParcelableArrayList("hotelProdPackages", paramArrayList);
    localHotelProdPackageSelectFragment.setArguments(localBundle);
    paramFragmentActivity = paramFragmentActivity.getSupportFragmentManager().beginTransaction();
    paramFragmentActivity.add(16908290, localHotelProdPackageSelectFragment);
    paramFragmentActivity.setTransition(4097);
    paramFragmentActivity.addToBackStack(null);
    paramFragmentActivity.commit();
    return localHotelProdPackageSelectFragment;
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
    paramBundle = getArguments().getParcelableArrayList("hotelProdPackages");
    if ((paramBundle == null) || (paramBundle.isEmpty()))
    {
      getFragmentManager().popBackStackImmediate();
      return;
    }
    this.mHotelProdPackageList.addAll(paramBundle);
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
    paramListView = paramListView.getAdapter().getItem(paramInt);
    if ((paramListView instanceof DPObject))
    {
      paramListView = ((DPObject)paramListView).getString("RedirectUrl");
      if (!TextUtils.isEmpty(paramListView));
    }
    else
    {
      return;
    }
    if (paramListView.startsWith("dianping"))
    {
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramListView)));
      return;
    }
    try
    {
      paramView = URLEncoder.encode(paramListView, "utf-8");
      paramListView = paramView;
      label76: startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramListView)));
      return;
    }
    catch (java.io.UnsupportedEncodingException paramView)
    {
      break label76;
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
      return HotelProdPackageSelectFragment.this.mHotelProdPackageList.size();
    }

    public Object getItem(int paramInt)
    {
      return HotelProdPackageSelectFragment.this.mHotelProdPackageList.get(paramInt);
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
        localObject1 = new HotelProdPackageSelectFragment.ViewHolder(null);
        paramView = HotelProdPackageSelectFragment.this.getActivity().getLayoutInflater().inflate(R.layout.dealselect_list_item, paramViewGroup, false);
        ((HotelProdPackageSelectFragment.ViewHolder)localObject1).titleView = ((TextView)paramView.findViewById(16908308));
        ((HotelProdPackageSelectFragment.ViewHolder)localObject1).priceView = ((TextView)paramView.findViewById(16908309));
        ((HotelProdPackageSelectFragment.ViewHolder)localObject1).buyDealNum = ((TextView)paramView.findViewById(R.id.buy_deal_num));
        paramView.setBackgroundDrawable(HotelProdPackageSelectFragment.this.getResources().getDrawable(R.drawable.white_bg));
        paramView.setTag(localObject1);
        paramViewGroup = (ViewGroup)localObject1;
        localObject1 = (DPObject)localObject2;
        if (!TextUtils.isEmpty(((DPObject)localObject1).getString("RedirectUrl")))
          break label266;
        paramViewGroup.titleView.setTextColor(Color.rgb(180, 172, 168));
        paramViewGroup.priceView.setTextColor(Color.rgb(180, 172, 168));
      }
      while (true)
      {
        paramViewGroup.titleView.setText(((DPObject)localObject1).getString("PackageTitle"));
        if (!TextUtils.isEmpty(((DPObject)localObject1).getString("SaleCountDesc")))
          paramViewGroup.buyDealNum.setText(((DPObject)localObject1).getString("SaleCountDesc"));
        paramViewGroup.priceView.setText("¥" + ((DPObject)localObject1).getDouble("PackagePrice"));
        paramViewGroup.priceView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow, 0);
        return paramView;
        paramViewGroup = (HotelProdPackageSelectFragment.ViewHolder)paramView.getTag();
        break;
        label266: paramViewGroup.titleView.setTextColor(Color.rgb(77, 70, 65));
        paramViewGroup.priceView.setTextColor(Color.rgb(77, 70, 65));
      }
    }

    public boolean isEnabled(int paramInt)
    {
      return (getItem(paramInt) instanceof DPObject);
    }
  }

  private static final class ViewHolder
  {
    public TextView buyDealNum;
    public TextView priceView;
    public TextView titleView;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.fragment.HotelProdPackageSelectFragment
 * JD-Core Version:    0.6.0
 */