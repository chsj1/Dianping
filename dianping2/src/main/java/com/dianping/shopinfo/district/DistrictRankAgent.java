package com.dianping.shopinfo.district;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.layout;

public class DistrictRankAgent extends ShopCellAgent
{
  private static final String CELL_DISTRICT_RANK = "0200Basic.02Rank";
  private CommonCell rankView = null;

  public DistrictRankAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void initView()
  {
    this.rankView = ((CommonCell)this.res.inflate(getContext(), R.layout.shopinfo_district_cell, getParentView(), false));
    this.rankView.setGAString("ranking_list");
    ((NovaActivity)getContext()).addGAView(this.rankView, -1);
    DPObject localDPObject = getShop().getObject("Rank");
    if (localDPObject != null)
    {
      this.rankView.setLeftIconUrl(localDPObject.getString("Icon"));
      this.rankView.setTitle(TextUtils.jsonParseText(localDPObject.getString("Title")));
      this.rankView.setSubTitle(localDPObject.getString("SubTitle"));
      this.rankView.setOnClickListener(new View.OnClickListener(localDPObject)
      {
        public void onClick(View paramView)
        {
          DistrictRankAgent.this.startActivity(this.val$rankInfo.getString("Scheme"));
        }
      });
      addCell("0200Basic.02Rank", this.rankView);
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((getShop() == null) || (getShop().getObject("Rank") == null))
      removeAllCells();
    do
      return;
    while (this.rankView != null);
    initView();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if ((getFragment() == null) || (getShop() == null))
      return;
    initView();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.district.DistrictRankAgent
 * JD-Core Version:    0.6.0
 */