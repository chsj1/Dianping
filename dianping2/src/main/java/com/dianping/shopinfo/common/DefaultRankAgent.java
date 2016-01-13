package com.dianping.shopinfo.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;

public class DefaultRankAgent extends ShopCellAgent
{
  private static final String CELL_DEFAULT_RANK_ITEM = "7000ShopInfo.10defaultrank";
  View.OnClickListener mClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (DefaultRankAgent.this.getShop() == null)
        return;
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(DefaultRankAgent.this.mRank.getString("Scheme")));
      DefaultRankAgent.this.getFragment().startActivity(paramView);
    }
  };
  private DPObject mRank;

  public DefaultRankAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createView()
  {
    String str = this.mRank.getString("Title");
    if (!TextUtils.isEmpty(str))
    {
      ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_no_title, getParentView(), false);
      TextView localTextView = (TextView)this.res.inflate(getContext(), R.layout.shopinfo_relevant_textview, getParentView(), false);
      localTextView.setPadding(ViewUtils.dip2px(getContext(), 15.0F), 0, 0, 0);
      localTextView.setLayoutParams(new LinearLayout.LayoutParams(-2, ViewUtils.dip2px(getContext(), 45.0F)));
      localTextView.setText(TextUtils.jsonParseText(str));
      ((NovaRelativeLayout)localShopinfoCommonCell.addContent(localTextView, true, this.mClickListener)).setGAString("shop_ranking_list", "tap");
      return localShopinfoCommonCell;
    }
    return null;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    paramBundle = getShop();
    if ((getShopStatus() == 0) && (paramBundle != null))
    {
      this.mRank = paramBundle.getObject("Rank");
      if (this.mRank != null)
        break label43;
    }
    label43: 
    do
    {
      return;
      paramBundle = createView();
    }
    while (paramBundle == null);
    addCell("7000ShopInfo.10defaultrank", paramBundle);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.DefaultRankAgent
 * JD-Core Version:    0.6.0
 */