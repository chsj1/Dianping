package com.dianping.shopinfo.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import java.util.ArrayList;

public class FacilitiesGridAdapter extends BaseAdapter
{
  private Context mContext;
  private ArrayList<DPObject> mDataList;

  public FacilitiesGridAdapter(Context paramContext, ArrayList<DPObject> paramArrayList)
  {
    this.mDataList = paramArrayList;
    this.mContext = paramContext;
  }

  public int getCount()
  {
    return this.mDataList.size();
  }

  public Object getItem(int paramInt)
  {
    return this.mDataList.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    DPObject localDPObject = ((DPObject)this.mDataList.get(paramInt)).getObject("FeatureTag");
    if (paramView == null)
    {
      paramView = LayoutInflater.from(this.mContext).inflate(R.layout.shopinfo_facility_label_item, null);
      paramViewGroup = new ViewHolder();
      paramViewGroup.mIcon = ((NetworkImageView)paramView.findViewById(R.id.icon));
      paramViewGroup.mTitle = ((TextView)paramView.findViewById(R.id.text));
      paramView.setTag(paramViewGroup);
    }
    while (true)
    {
      paramViewGroup.mIcon.setImage(localDPObject.getString("PictureUrl"));
      paramViewGroup.mTitle.setText(localDPObject.getString("Title"));
      return paramView;
      paramViewGroup = (ViewHolder)paramView.getTag();
    }
  }

  class ViewHolder
  {
    public NetworkImageView mIcon;
    public TextView mTitle;

    ViewHolder()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.utils.FacilitiesGridAdapter
 * JD-Core Version:    0.6.0
 */