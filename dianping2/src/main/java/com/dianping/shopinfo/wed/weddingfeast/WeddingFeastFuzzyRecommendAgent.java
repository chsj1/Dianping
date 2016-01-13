package com.dianping.shopinfo.wed.weddingfeast;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaButton;

public class WeddingFeastFuzzyRecommendAgent extends ShopCellAgent
  implements View.OnClickListener
{
  private static final String CELL_WEDDING_Fuzzy_Recommend = "0290FuzzyRecommend.0001";
  DPObject extra;

  public WeddingFeastFuzzyRecommendAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (!isWeddingType());
    do
    {
      return;
      this.extra = ((DPObject)getSharedObject("WeddingHotelExtra"));
    }
    while ((this.extra == null) || (!this.extra.getBoolean("IsShowFuzzy")));
    paramBundle = this.extra.getString("FuzzyTitle");
    String str = this.extra.getString("FuzzyBtnTitle");
    View localView = this.res.inflate(getContext(), R.layout.shopinfo_weddingfeast_fuzzyrecommend_cell, getParentView(), false);
    NovaButton localNovaButton = (NovaButton)localView.findViewById(R.id.find_conditions_go_btn);
    localNovaButton.setGAString("quicksearch");
    TextView localTextView = (TextView)localView.findViewById(R.id.find_conditions_go_text);
    if ((str != null) && (!str.isEmpty()))
      localNovaButton.setText(str);
    if ((paramBundle != null) && (!paramBundle.isEmpty()))
      localTextView.setText(paramBundle);
    localNovaButton.setOnClickListener(this);
    addCell("0290FuzzyRecommend.0001", localView);
  }

  public void onClick(View paramView)
  {
    paramView = this.extra.getString("FuzzyUrl");
    if ((paramView != null) && (!paramView.isEmpty()));
    while (true)
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
      paramView.putExtra("shopId", shopId());
      startActivity(paramView);
      return;
      paramView = Uri.parse("dianping://weddinghotelfuzzyrecommend").buildUpon().appendQueryParameter("shopid", String.valueOf(getShop().getInt("ID"))).build().toString();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }

  public Bundle saveInstanceState()
  {
    return super.saveInstanceState();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.wed.weddingfeast.WeddingFeastFuzzyRecommendAgent
 * JD-Core Version:    0.6.0
 */