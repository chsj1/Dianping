package com.dianping.hotel.shoplist.adapt;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.hotel.shoplist.data.model.HotelUniRegionModel;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class HotelRegionListAdapter extends BasicAdapter
{
  private static final int FIRST_LEVEL = 1;
  private static final int LAST_LEVEL = 3;
  private static final int MIDDLE_LEVEL = 2;
  private boolean isOriginIndexPath;
  private int level;
  private Context mContext;
  private LayoutInflater mInflater;
  private int pressedLevel;
  private HotelUniRegionModel regionData;
  private int selectedPosition = -1;

  public HotelRegionListAdapter(Context paramContext, int paramInt)
  {
    this.mContext = paramContext;
    this.mInflater = LayoutInflater.from(paramContext);
    this.level = paramInt;
  }

  private int getLevelType()
  {
    if (this.level == 0)
      return 1;
    if (this.selectedPosition == -1);
    for (int i = 0; ; i = this.selectedPosition)
    {
      HotelUniRegionModel localHotelUniRegionModel = (HotelUniRegionModel)getItem(i);
      if ((localHotelUniRegionModel != null) && (localHotelUniRegionModel.getSubNaviItemList() != null) && (!localHotelUniRegionModel.getSubNaviItemList().isEmpty()))
        break;
      return 3;
    }
    return 2;
  }

  public int getCount()
  {
    int j = 0;
    int i = j;
    if (this.regionData != null)
    {
      i = j;
      if (this.regionData.getSubNaviItemList() != null)
        i = this.regionData.getSubNaviItemList().size();
    }
    return i;
  }

  public Object getItem(int paramInt)
  {
    if (this.regionData == null);
    do
      return null;
    while ((this.regionData.getSubNaviItemList() == null) || (this.regionData.getSubNaviItemList().isEmpty()) || (paramInt >= this.regionData.getSubNaviItemList().size()) || (paramInt < 0));
    return this.regionData.getSubNaviItemList().get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return ((HotelUniRegionModel)this.regionData.getSubNaviItemList().get(paramInt)).getUid();
  }

  public HotelUniRegionModel getRegionData()
  {
    return this.regionData;
  }

  public int getSelectedPosition()
  {
    return this.selectedPosition;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    int i = 0;
    paramViewGroup = paramView;
    paramView = paramViewGroup;
    if (paramViewGroup == null)
      paramView = this.mInflater.inflate(R.layout.hotel_region_filter_list_item, null);
    Object localObject = (HotelUniRegionModel)getItem(paramInt);
    paramViewGroup = (TextView)paramView.findViewById(R.id.text1);
    paramViewGroup.setText(((HotelUniRegionModel)localObject).getName());
    localObject = paramView.findViewById(R.id.divider);
    View localView = paramView.findViewById(R.id.trig);
    switch (getLevelType())
    {
    default:
      return paramView;
    case 1:
      if (this.selectedPosition == paramInt)
        i = -1;
      paramView.setBackgroundColor(i);
      ((View)localObject).setVisibility(8);
      localView.setVisibility(8);
      return paramView;
    case 2:
      paramViewGroup.setTextColor(-16777216);
      if (this.selectedPosition == paramInt)
        localView.setVisibility(0);
      while (true)
      {
        ((View)localObject).setVisibility(8);
        return paramView;
        localView.setVisibility(8);
      }
    case 3:
    }
    if ((this.selectedPosition == paramInt) && ((this.isOriginIndexPath) || (this.pressedLevel == this.level)))
    {
      paramViewGroup.setTextColor(this.mContext.getResources().getColor(R.color.light_orange));
      ((View)localObject).setBackgroundColor(this.mContext.getResources().getColor(R.color.light_orange));
    }
    while (true)
    {
      ((View)localObject).setVisibility(0);
      localView.setVisibility(8);
      return paramView;
      paramViewGroup.setTextColor(this.mContext.getResources().getColor(R.color.filter_main_text_color));
      ((View)localObject).setBackgroundColor(this.mContext.getResources().getColor(R.color.inner_divider));
    }
  }

  public void setPressInfo(int paramInt, boolean paramBoolean)
  {
    this.pressedLevel = paramInt;
    this.isOriginIndexPath = paramBoolean;
  }

  public void setRegionData(HotelUniRegionModel paramHotelUniRegionModel)
  {
    this.regionData = paramHotelUniRegionModel;
  }

  public void setSelectedPosition(int paramInt, boolean paramBoolean)
  {
    this.selectedPosition = paramInt;
    if (paramBoolean)
      notifyDataSetChanged();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shoplist.adapt.HotelRegionListAdapter
 * JD-Core Version:    0.6.0
 */