package com.dianping.search.history;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.NovaTabBaseFragment;
import com.dianping.base.basic.NovaTabFragmentActivity;
import com.dianping.base.util.HistoryHelper;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class HistoryFragment extends NovaTabBaseFragment
  implements AdapterView.OnItemClickListener
{
  private HistoryListAdapter adapter;
  private ListView mListView;

  private View getEmptyView()
  {
    TextView localTextView = (TextView)getActivity().getLayoutInflater().inflate(R.layout.simple_list_item_18, null, false);
    localTextView.setMovementMethod(LinkMovementMethod.getInstance());
    Drawable localDrawable = getResources().getDrawable(R.drawable.empty_page_nothing);
    localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
    localTextView.setCompoundDrawablePadding(8);
    localTextView.setCompoundDrawables(localDrawable, null, null, null);
    localTextView.setText(Html.fromHtml("您还没有浏览过商户哦"));
    return localTextView;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    double d2 = 0.0D;
    double d4 = 0.0D;
    paramBundle = null;
    double d1 = d2;
    try
    {
      if (locationService().location() != null)
      {
        d1 = d2;
        paramBundle = (Location)locationService().location().decodeToObject(Location.DECODER);
      }
      d1 = d2;
      d3 = d4;
      if (paramBundle != null)
      {
        d1 = d2;
        d2 = paramBundle.offsetLatitude();
        d1 = d2;
        d3 = paramBundle.offsetLongitude();
        d1 = d2;
      }
      this.adapter = new HistoryListAdapter((NovaActivity)getActivity(), d1, d3);
      this.mListView.setAdapter(this.adapter);
      paramBundle = (NovaTabFragmentActivity)getActivity();
      if (HistoryHelper.getInstance().getIds().length > 0)
      {
        bool = true;
        paramBundle.setActivityEditable(bool, this);
        ((NovaTabFragmentActivity)getActivity()).setActivityIsEdit(false);
        return;
      }
    }
    catch (ArchiveException paramBundle)
    {
      while (true)
      {
        paramBundle.printStackTrace();
        double d3 = d4;
        continue;
        boolean bool = false;
      }
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.history_shop_layout, paramViewGroup, false);
    this.mListView = ((ListView)paramLayoutInflater.findViewById(R.id.list));
    this.mListView.setOnItemClickListener(this);
    paramViewGroup = (ViewGroup)paramLayoutInflater.findViewById(R.id.empty);
    paramViewGroup.addView(getEmptyView());
    this.mListView.setEmptyView(paramViewGroup);
    return paramLayoutInflater;
  }

  public void onDeleteButtonClicked()
  {
    Object localObject1 = this.adapter.getDeleteList();
    if (((ArrayList)localObject1).size() == 0)
    {
      showToast("请至少选择一项");
      return;
    }
    HistoryHelper localHistoryHelper = HistoryHelper.getInstance();
    monitorenter;
    int i = 0;
    while (true)
    {
      try
      {
        int j = ((ArrayList)localObject1).size();
        if (i >= j)
          continue;
        int k = ((DPObject)((ArrayList)localObject1).get(i)).getInt("ID");
        localHistoryHelper.removeId(k);
        localHistoryHelper.removeShop(k);
        i += 1;
        continue;
        localHistoryHelper.flushIds();
        monitorexit;
        this.adapter.reset();
        localObject1 = (NovaTabFragmentActivity)getActivity();
        if (localHistoryHelper.getIds().length > 0)
        {
          bool = true;
          ((NovaTabFragmentActivity)localObject1).setActivityEditable(bool, this);
          ((NovaTabFragmentActivity)getActivity()).setActivityIsEdit(false);
          return;
        }
      }
      finally
      {
        monitorexit;
      }
      boolean bool = false;
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    this.adapter.onFinish();
  }

  public void onEditModeChanged(boolean paramBoolean)
  {
    if (this.adapter != null)
    {
      this.adapter.resetCheckList();
      this.adapter.setIsEdit(paramBoolean);
      ((NovaTabFragmentActivity)getActivity()).setButtonView(this.adapter.getCheckedSize());
    }
  }

  public void onImageSwitchChanged()
  {
    if (this.adapter != null)
      this.adapter.notifyDataSetChanged();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = this.adapter.getItem(paramInt);
    if ((paramAdapterView instanceof DPObject))
    {
      paramAdapterView = (DPObject)paramAdapterView;
      if (paramAdapterView.isClass("Shop"))
      {
        if (!((NovaTabFragmentActivity)getActivity()).isEdit())
          break label70;
        this.adapter.itemBeChecked(paramInt);
        ((NovaTabFragmentActivity)getActivity()).setButtonView(this.adapter.getCheckedSize());
      }
    }
    return;
    label70: paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + paramAdapterView.getInt("ID")));
    paramView.putExtra("shop", paramAdapterView);
    startActivity(paramView);
    statisticsEvent("profile5", "profile5_recentview_shop", "" + paramAdapterView.getInt("ID"), 0);
  }

  public void onResume()
  {
    super.onResume();
    this.adapter.reset();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.history.HistoryFragment
 * JD-Core Version:    0.6.0
 */