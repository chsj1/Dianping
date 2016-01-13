package com.dianping.hotel.deal.agent;

import android.os.Bundle;
import android.view.View;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.agent.TuanGroupCellAgent;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.hotel.deal.widget.HotelProdStrategyWidget;
import com.dianping.hotel.deal.widget.HotelProdStrategyWidget.TextAndWeight;
import com.dianping.loader.MyResources;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.List;

public class HotelProdRefundAgent extends TuanGroupCellAgent
{
  private DPObject dpHotelProdBase;
  protected View mContentView;
  protected HotelProdStrategyWidget mStrategyWidget;

  public HotelProdRefundAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getContext() == null);
    do
    {
      return;
      if (paramBundle == null)
        continue;
      this.dpHotelProdBase = ((DPObject)paramBundle.getParcelable("hotelprodbase"));
    }
    while (this.dpHotelProdBase == null);
    if (this.mContentView == null)
      setupView();
    updateView();
  }

  protected void setupView()
  {
    this.mContentView = this.res.inflate(getContext(), R.layout.hotel_prod_strategy_layout, getParentView(), false);
    this.mStrategyWidget = ((HotelProdStrategyWidget)this.mContentView.findViewById(R.id.strategy_layout));
  }

  protected void updateView()
  {
    removeAllCells();
    if (!DPObjectUtils.isArrayEmpty(this.dpHotelProdBase.getArray("ServiceLabelList")))
    {
      ArrayList localArrayList = new ArrayList();
      DPObject[] arrayOfDPObject = this.dpHotelProdBase.getArray("ServiceLabelList");
      int j = arrayOfDPObject.length;
      int i = 0;
      while (i < j)
      {
        DPObject localDPObject = arrayOfDPObject[i];
        if (localDPObject != null)
          localArrayList.add(new HotelProdStrategyWidget.TextAndWeight(localDPObject.getString("Name"), localDPObject.getString("Url"), 3));
        i += 1;
      }
      this.mStrategyWidget.setupTags(localArrayList, TextUtils.jsonParseText(this.dpHotelProdBase.getString("SaleCountDesc")));
    }
    addCell("", this.mContentView);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.agent.HotelProdRefundAgent
 * JD-Core Version:    0.6.0
 */