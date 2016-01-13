package com.dianping.takeaway.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.dialogfilter.FilterDialog;
import com.dianping.base.widget.dialogfilter.FilterDialog.OnFilterListener;
import com.dianping.takeaway.activity.TakeawaySampleShopListActivity;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TAMultiFilterDialog extends FilterDialog
{
  TAListFilterDialogAdapter taAdapter;
  private final AdapterView.OnItemClickListener taListener = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
      if ((paramAdapterView instanceof TAMultiFilterDialog.TAFilterOption))
        paramAdapterView = (TAMultiFilterDialog.TAFilterOption)paramAdapterView;
      switch (TAMultiFilterDialog.4.$SwitchMap$com$dianping$takeaway$view$TAMultiFilterDialog$TAOptionStatus[paramAdapterView.optionStatus.ordinal()])
      {
      default:
        return;
      case 1:
        paramAdapterView.optionStatus = TAMultiFilterDialog.TAOptionStatus.Unchecked;
        TAMultiFilterDialog.this.taAdapter.notifyDataSetChanged();
        return;
      case 2:
      }
      paramAdapterView.optionStatus = TAMultiFilterDialog.TAOptionStatus.Checked;
      TAMultiFilterDialog.this.taAdapter.notifyDataSetChanged();
    }
  };
  List<TAFilterOption> taOptions;

  public TAMultiFilterDialog(Activity paramActivity)
  {
    super(paramActivity);
    View localView = getLayoutInflater().inflate(R.layout.takeaway_multifilter_dialog, getFilterViewParent(), false);
    ListView localListView = (ListView)localView.findViewById(R.id.list);
    this.taOptions = new ArrayList();
    this.taAdapter = new TAListFilterDialogAdapter(null);
    localListView.setAdapter(this.taAdapter);
    localListView.setOnItemClickListener(this.taListener);
    localView.findViewById(R.id.clear_btn).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (TAMultiFilterDialog.this.taOptions != null)
        {
          int i = 0;
          paramView = TAMultiFilterDialog.this.taOptions.iterator();
          while (paramView.hasNext())
          {
            TAMultiFilterDialog.TAFilterOption localTAFilterOption = (TAMultiFilterDialog.TAFilterOption)paramView.next();
            if (localTAFilterOption.optionStatus != TAMultiFilterDialog.TAOptionStatus.Checked)
              continue;
            localTAFilterOption.optionStatus = TAMultiFilterDialog.TAOptionStatus.Unchecked;
            i = 1;
          }
          if (i != 0)
            TAMultiFilterDialog.this.taAdapter.notifyDataSetChanged();
        }
      }
    });
    localView.findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener(paramActivity)
    {
      public void onClick(View paramView)
      {
        if ((TAMultiFilterDialog.this.listener != null) && (TAMultiFilterDialog.this.taOptions != null))
        {
          paramView = "";
          Object localObject = TAMultiFilterDialog.this.taOptions.iterator();
          while (((Iterator)localObject).hasNext())
          {
            TAMultiFilterDialog.TAFilterOption localTAFilterOption = (TAMultiFilterDialog.TAFilterOption)((Iterator)localObject).next();
            if (localTAFilterOption.optionStatus != TAMultiFilterDialog.TAOptionStatus.Checked)
              continue;
            if (TextUtils.isEmpty(paramView));
            for (paramView = localTAFilterOption.optionId; ; paramView = paramView + "|" + localTAFilterOption.optionId)
              break;
          }
          TAMultiFilterDialog.this.listener.onFilter(TAMultiFilterDialog.this, paramView);
          localObject = ((TakeawaySampleShopListActivity)TAMultiFilterDialog.this.getActivity()).getGAUserInfo();
          ((GAUserInfo)localObject).title = paramView;
          GAHelper.instance().contextStatisticsEvent(this.val$activity, "filtered", (GAUserInfo)localObject, "view");
        }
      }
    });
    setFilterView(localView);
  }

  public void setFilterView(View paramView)
  {
    super.setFilterView(paramView);
    getFilterViewParent().setBackgroundResource(R.drawable.takeaway_multifilter_content_background);
  }

  public void setTAOptionsData(DPObject[] paramArrayOfDPObject, String paramString)
  {
    this.taOptions.clear();
    ArrayList localArrayList = new ArrayList();
    if (!TextUtils.isEmpty(paramString))
      localArrayList.addAll(Arrays.asList(paramString.split("\\|")));
    if (paramArrayOfDPObject != null)
    {
      int j = paramArrayOfDPObject.length;
      int i = 0;
      if (i < j)
      {
        paramString = paramArrayOfDPObject[i];
        TAFilterOption localTAFilterOption = new TAFilterOption(null);
        localTAFilterOption.optionDesp = paramString.getString("Name");
        localTAFilterOption.optionId = paramString.getString("ID");
        if (paramString.getBoolean("State"))
          if (localArrayList.contains(localTAFilterOption.optionId))
            localTAFilterOption.optionStatus = TAOptionStatus.Checked;
        while (true)
        {
          this.taOptions.add(localTAFilterOption);
          i += 1;
          break;
          localTAFilterOption.optionStatus = TAOptionStatus.Unchecked;
          continue;
          localTAFilterOption.optionStatus = TAOptionStatus.Disabled;
        }
      }
    }
    this.taAdapter.notifyDataSetChanged();
  }

  private class TAFilterOption
  {
    String optionDesp;
    String optionId;
    TAMultiFilterDialog.TAOptionStatus optionStatus;

    private TAFilterOption()
    {
    }
  }

  private class TAListFilterDialogAdapter extends BaseAdapter
  {
    private TAListFilterDialogAdapter()
    {
    }

    public int getCount()
    {
      return TAMultiFilterDialog.this.taOptions.size();
    }

    public Object getItem(int paramInt)
    {
      return TAMultiFilterDialog.this.taOptions.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView != null);
      TextView localTextView;
      ImageView localImageView;
      for (paramView = (NovaLinearLayout)paramView; ; paramView = (NovaLinearLayout)TAMultiFilterDialog.this.getLayoutInflater().inflate(R.layout.takeaway_multifilter_dialog_item, paramViewGroup, false))
      {
        paramViewGroup = (TAMultiFilterDialog.TAFilterOption)TAMultiFilterDialog.this.taOptions.get(paramInt);
        localTextView = (TextView)paramView.findViewById(16908308);
        localImageView = (ImageView)paramView.findViewById(R.id.option_checkbox);
        ViewUtils.setVisibilityAndContent(localTextView, paramViewGroup.optionDesp);
        switch (TAMultiFilterDialog.4.$SwitchMap$com$dianping$takeaway$view$TAMultiFilterDialog$TAOptionStatus[paramViewGroup.optionStatus.ordinal()])
        {
        case 2:
        default:
          localTextView.setTextColor(TAMultiFilterDialog.this.getContext().getResources().getColor(R.color.deep_gray));
          paramView.setBackgroundResource(R.drawable.filter_sub_list_item);
          localImageView.setSelected(false);
          localImageView.setEnabled(true);
          return paramView;
        case 1:
        case 3:
        }
      }
      localTextView.setTextColor(TAMultiFilterDialog.this.getContext().getResources().getColor(R.color.light_red));
      localImageView.setSelected(true);
      localImageView.setEnabled(true);
      paramView.setBackgroundResource(R.drawable.filter_sub_list_item);
      return paramView;
      localTextView.setTextColor(TAMultiFilterDialog.this.getContext().getResources().getColor(R.color.text_hint_light_gray));
      localImageView.setSelected(false);
      localImageView.setEnabled(false);
      paramView.setBackgroundResource(R.drawable.filter_sub_normal);
      return paramView;
    }
  }

  private static enum TAOptionStatus
  {
    static
    {
      Disabled = new TAOptionStatus("Disabled", 2);
      $VALUES = new TAOptionStatus[] { Checked, Unchecked, Disabled };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.view.TAMultiFilterDialog
 * JD-Core Version:    0.6.0
 */