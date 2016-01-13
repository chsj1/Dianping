package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;

public class FacilityGridView extends LinearLayout
{
  private Adapter adapter;
  private ImageView arrow;
  private DPObject[] data;
  private ExpandableHeightGridView gridView;
  private boolean isExpand;
  private Context mContext;

  public FacilityGridView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
    setClickable(true);
  }

  public void expand()
  {
    if (this.arrow.getVisibility() == 0)
    {
      if (this.isExpand)
      {
        this.arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down_black));
        this.gridView.setExpanded(false);
        this.isExpand = false;
        this.adapter.notifyDataSetChanged();
      }
    }
    else
      return;
    this.arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up_black));
    this.gridView.setExpanded(true);
    this.isExpand = true;
    this.adapter.notifyDataSetChanged();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.arrow = ((ImageView)findViewById(R.id.facility_arrow));
    this.gridView = ((ExpandableHeightGridView)findViewById(R.id.facility_gird));
    this.gridView.setMeasureHeight(true);
    this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        FacilityGridView.this.expand();
      }
    });
    this.adapter = new Adapter();
    this.gridView.setAdapter(this.adapter);
  }

  public void setData(DPObject[] paramArrayOfDPObject)
  {
    this.data = paramArrayOfDPObject;
    this.adapter.notifyDataSetChanged();
  }

  class Adapter extends BasicAdapter
  {
    Adapter()
    {
    }

    public int getCount()
    {
      int i;
      if (FacilityGridView.this.data == null)
      {
        i = 0;
        if ((i < 0) || (i > 60))
          break label48;
        FacilityGridView.this.arrow.setVisibility(8);
      }
      label48: 
      do
      {
        return i;
        i = FacilityGridView.this.data.length;
        break;
      }
      while (FacilityGridView.this.isExpand);
      FacilityGridView.this.arrow.setVisibility(0);
      return 60;
    }

    public DPObject getItem(int paramInt)
    {
      return FacilityGridView.this.data[paramInt];
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = getItem(paramInt);
      paramViewGroup = LayoutInflater.from(FacilityGridView.this.mContext).inflate(R.layout.shopinfo_hotel_facility_item, paramViewGroup, false);
      NetworkImageView localNetworkImageView = (NetworkImageView)paramViewGroup.findViewById(R.id.hotel_info_facility_img);
      TextView localTextView = (TextView)paramViewGroup.findViewById(R.id.hotel_info_facility_text);
      localNetworkImageView.setImage(paramView.getString("Icon"));
      localTextView.setText(paramView.getString("Title"));
      return paramViewGroup;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.FacilityGridView
 * JD-Core Version:    0.6.0
 */