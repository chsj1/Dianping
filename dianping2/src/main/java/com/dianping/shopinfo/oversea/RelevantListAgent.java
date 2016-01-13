package com.dianping.shopinfo.oversea;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.CommonCell;
import com.dianping.v1.R.layout;

public class RelevantListAgent extends ShopCellAgent
{
  private static final String CELL_RELEVANT_LIST = "7200RelevantList.";
  private View.OnClickListener clickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (paramView == null);
      do
      {
        do
        {
          return;
          paramView = (DPObject)paramView.getTag();
        }
        while (paramView == null);
        paramView = paramView.getString("Scheme");
      }
      while (TextUtils.isEmpty(paramView));
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
      RelevantListAgent.this.getFragment().startActivity(paramView);
    }
  };
  private DPObject mRelevantListObject;

  public RelevantListAgent(Object paramObject)
  {
    super(paramObject);
  }

  private ViewGroup createRelevantListView()
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    Object localObject1 = this.mRelevantListObject.getObject("Ranks");
    localShopinfoCommonCell.setTitle(((DPObject)localObject1).getString("Title"));
    localShopinfoCommonCell.hideArrow();
    localObject1 = ((DPObject)localObject1).getArray("Items");
    int i = 0;
    int j = localObject1.length;
    while (i < j)
    {
      Object localObject2 = localObject1[i];
      CommonCell localCommonCell = (CommonCell)this.res.inflate(getContext(), R.layout.shopinfo_cell, getParentView(), false);
      localCommonCell.setTitle(localObject2.getString("Title"));
      localCommonCell.setSubTitle(localObject2.getString("SubTitle"));
      localCommonCell.setLeftIconUrl(localObject2.getString("Icon"));
      localCommonCell.setArrowIconVisibility(TextUtils.isEmpty(localObject2.getString("Scheme")));
      localCommonCell.setTag(localObject2);
      localCommonCell.setGAString("relatedshoplist", localObject2.getString("Title"), i);
      localCommonCell.setOnClickListener(this.clickListener);
      localShopinfoCommonCell.addContent(localCommonCell, false);
      i += 1;
    }
    return (ViewGroup)localShopinfoCommonCell;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (paramBundle != null)
    {
      this.mRelevantListObject = ((DPObject)paramBundle.getParcelable("RelevantListObject"));
      if (this.mRelevantListObject != null)
      {
        removeAllCells();
        paramBundle = this.mRelevantListObject.getObject("Ranks");
        if ((paramBundle != null) && (paramBundle.getArray("Items") != null) && (paramBundle.getArray("Items").length > 0))
          addCell("7200RelevantList.", createRelevantListView());
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.oversea.RelevantListAgent
 * JD-Core Version:    0.6.0
 */