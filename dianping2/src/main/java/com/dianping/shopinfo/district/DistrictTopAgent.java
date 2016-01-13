package com.dianping.shopinfo.district;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.TitleBar;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;

public class DistrictTopAgent extends ShopCellAgent
{
  private static final String CELL_DISTRICT_TOP = "0200Basic.01Top";
  private View mDistrictView = null;

  public DistrictTopAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void initView()
  {
    this.mDistrictView = this.res.inflate(getContext(), R.layout.shopinfo_district_top_layout, getParentView(), false);
    ((NetworkImageView)this.mDistrictView.findViewById(R.id.district_icon)).setImage(getShop().getString("DefaultPic"));
    ((TextView)this.mDistrictView.findViewById(R.id.district_name)).setText(getShop().getString("Name"));
    ((TextView)this.mDistrictView.findViewById(R.id.district_introduce)).setText(getShop().getString("Desc"));
    addCell("0200Basic.01Top", this.mDistrictView);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getShop() == null)
      removeAllCells();
    do
      return;
    while (this.mDistrictView != null);
    initView();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if ((getFragment() == null) || (getShop() == null))
      return;
    getFragment().getTitleBar().setTitle("商区");
    initView();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.district.DistrictTopAgent
 * JD-Core Version:    0.6.0
 */