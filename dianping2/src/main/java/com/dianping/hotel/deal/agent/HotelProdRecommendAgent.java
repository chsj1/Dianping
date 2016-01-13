package com.dianping.hotel.deal.agent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.agent.TuanGroupCellAgent;
import com.dianping.base.tuan.widget.DealInfoCommonCell;
import com.dianping.hotel.deal.fragment.HotelProdInfoAgentFragment;
import com.dianping.loader.MyResources;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class HotelProdRecommendAgent extends TuanGroupCellAgent
{
  private static final String CELL_NAME = "hotelprodRecommendAgent";
  private static final int STYLE_PLAIN = 1;
  private static final int STYLE_RICH = 2;
  private DPObject dpHotelProdDetail;
  private HotelProdInfoAgentFragment mProdFragment;

  public HotelProdRecommendAgent(Object paramObject)
  {
    super(paramObject);
    this.mProdFragment = ((HotelProdInfoAgentFragment)paramObject);
  }

  private void addPlainStyleRecommendCell(String[] paramArrayOfString)
  {
    if ((paramArrayOfString == null) || (paramArrayOfString.length == 0))
      return;
    LinearLayout localLinearLayout = new LinearLayout(getContext());
    localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    localLinearLayout.setOrientation(1);
    int i = ViewUtils.dip2px(getContext(), 16.0F);
    int j = ViewUtils.dip2px(getContext(), 16.0F);
    localLinearLayout.setPadding(j, i, j, i);
    addRecommends(localLinearLayout, paramArrayOfString);
    paramArrayOfString = new DealInfoCommonCell(getContext());
    paramArrayOfString.setTitle("推荐理由");
    paramArrayOfString.hideArrow();
    paramArrayOfString.addContent(localLinearLayout, false);
    addCell("", paramArrayOfString);
  }

  private void addRecommends(LinearLayout paramLinearLayout, String[] paramArrayOfString)
  {
    int i = 0;
    while (i < paramArrayOfString.length)
    {
      RelativeLayout localRelativeLayout = (RelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.hotel_prod_recommend_item, null);
      localRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
      if (i != 0)
        localRelativeLayout.setPadding(0, ViewUtils.dip2px(getContext(), 10.0F), 0, 0);
      ((TextView)localRelativeLayout.findViewById(R.id.recommend_item)).setText(paramArrayOfString[i]);
      paramLinearLayout.addView(localRelativeLayout);
      i += 1;
    }
  }

  private void addRichStyleRecommendCell(String paramString, String[] paramArrayOfString)
  {
    LinearLayout localLinearLayout1 = (LinearLayout)this.res.inflate(getContext(), R.layout.hotel_prod_rich_style_recommend, getParentView(), false);
    RelativeLayout localRelativeLayout = (RelativeLayout)localLinearLayout1.findViewById(R.id.short_recommend_layout);
    TextView localTextView = (TextView)localLinearLayout1.findViewById(R.id.short_recommend);
    LinearLayout localLinearLayout2 = (LinearLayout)localLinearLayout1.findViewById(R.id.recommend_content);
    if (TextUtils.isEmpty(paramString))
    {
      localRelativeLayout.setVisibility(8);
      if ((paramArrayOfString != null) && (paramArrayOfString.length != 0))
        break label112;
      localLinearLayout2.setVisibility(8);
    }
    while (true)
    {
      addCell("hotelprodRecommendAgent", localLinearLayout1);
      return;
      localRelativeLayout.setVisibility(0);
      localTextView.setText(paramString);
      break;
      label112: localLinearLayout2.setVisibility(0);
      addRecommends(localLinearLayout2, paramArrayOfString);
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getContext() == null)
      return;
    if (paramBundle != null)
      this.dpHotelProdDetail = ((DPObject)paramBundle.getParcelable("hotelproddetail"));
    updateView();
  }

  protected void updateView()
  {
    removeAllCells();
    if (this.dpHotelProdDetail == null);
    String[] arrayOfString;
    do
    {
      return;
      arrayOfString = this.dpHotelProdDetail.getStringArray("RecommendReasonList");
    }
    while ((arrayOfString == null) || (arrayOfString.length == 0));
    switch (this.dpHotelProdDetail.getInt("RecommendReasonStyle"))
    {
    default:
      return;
    case 1:
      addPlainStyleRecommendCell(arrayOfString);
      return;
    case 2:
    }
    addRichStyleRecommendCell(this.dpHotelProdDetail.getString("ShortRecommendReason"), arrayOfString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.agent.HotelProdRecommendAgent
 * JD-Core Version:    0.6.0
 */