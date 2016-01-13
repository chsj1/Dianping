package com.dianping.hotel.mainsearch.agent;

import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.hotel.mainsearch.fragment.HotelSearchMainFragment;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;

public class HotelHomeAgent extends CellAgent
{
  public HotelHomeAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void addDividerCell(String paramString)
  {
    addDividerCell(paramString, R.drawable.home_cell_divider);
  }

  public void addDividerCell(String paramString, int paramInt)
  {
    LinearLayout localLinearLayout = new LinearLayout(getContext());
    localLinearLayout.setOrientation(1);
    localLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    ViewPager localViewPager = new ViewPager(getContext());
    localViewPager.setBackgroundColor(this.res.getColor(R.color.line_gray));
    localViewPager.setLayoutParams(new ViewGroup.LayoutParams(-1, 1));
    View localView = new View(getContext());
    localView.setBackgroundDrawable(this.res.getDrawable(paramInt));
    localView.setLayoutParams(new ViewGroup.LayoutParams(-1, (int)TypedValue.applyDimension(1, 15.0F, getFragment().getResources().getDisplayMetrics())));
    localLinearLayout.addView(localViewPager);
    localLinearLayout.addView(localView);
    addCell(paramString, localLinearLayout);
  }

  public DPObject getAdsObj()
  {
    return ((HotelSearchMainFragment)this.fragment).adsObj;
  }

  public boolean isConnecting()
  {
    return ((HotelSearchMainFragment)this.fragment).isConnecting();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.mainsearch.agent.HotelHomeAgent
 * JD-Core Version:    0.6.0
 */