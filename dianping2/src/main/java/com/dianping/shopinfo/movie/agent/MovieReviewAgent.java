package com.dianping.shopinfo.movie.agent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopinfoCommonCell;
import com.dianping.base.widget.ToolbarButton;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.common.ReviewAgent;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class MovieReviewAgent extends ReviewAgent
{
  public MovieReviewAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected View createAgentFooter()
  {
    View localView = this.res.inflate(getContext(), R.layout.shopinfo_movie_review_bottom_layout, getParentView(), false);
    localView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        MovieReviewAgent.this.clickReviewItem(0);
      }
    });
    return localView;
  }

  protected ShopinfoCommonCell createAgentHeader()
  {
    ShopinfoCommonCell localShopinfoCommonCell = (ShopinfoCommonCell)this.res.inflate(getContext(), R.layout.shopinfo_common_cell_layout, getParentView(), false);
    localShopinfoCommonCell.removeViewAt(0);
    View localView = this.res.inflate(getContext(), R.layout.shopinfo_movie_review_title_layout, getParentView(), false);
    TextView localTextView1 = (TextView)localView.findViewById(R.id.title);
    TextView localTextView2 = (TextView)localView.findViewById(R.id.sub_title);
    ((ToolbarButton)localView.findViewById(R.id.cinema_addcomment)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        MovieReviewAgent.this.addReview();
      }
    });
    localTextView1.setText("网友点评");
    localTextView2.setText("(" + this.mShopReviewFeedList.getInt("RecordCount") + ")");
    localShopinfoCommonCell.addView(localView, 0);
    return localShopinfoCommonCell;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.movie.agent.MovieReviewAgent
 * JD-Core Version:    0.6.0
 */