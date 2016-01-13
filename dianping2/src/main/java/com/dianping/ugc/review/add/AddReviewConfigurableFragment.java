package com.dianping.ugc.review.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.Cell;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class AddReviewConfigurableFragment extends AgentFragment
{
  public static final String ADDREVIEW_CELL_CUSTOM = "addreview/custom";
  public static final String ADDREVIEW_CELL_EDITVIEW = "addreview/editview";
  public static final String ADDREVIEW_CELL_RATINGBAR = "addreview/ratingbar";
  public static final String ADDREVIEW_CELL_SCOREVIEW = "addreview/scoreview";
  public static final String ADDREVIEW_CELL_SNS = "addview/snsview";
  public static final String ADDREVIEW_CELL_SPEND = "addview/spendview";
  public static final String ADDREVIEW_CELL_TAG = "addreview/tagview";
  public static final String ADDREVIEW_CELL_UPPHOTO = "addreview/upphotoview";
  private DPObject shop;

  protected void addCellToContainerView(String paramString, Cell paramCell)
  {
    if (("addreview/tagview".equals(paramString)) || ("addreview/custom".equals(paramString)) || ("addview/spendview".equals(paramString)))
    {
      ((ViewGroup)agentContainerView().findViewById(R.id.tagLayout)).addView(paramCell.view);
      return;
    }
    if ("addview/snsview".equals(paramString))
    {
      ((ViewGroup)agentContainerView().findViewById(R.id.snsLayout)).addView(paramCell.view);
      return;
    }
    if ("addreview/upphotoview".equals(paramString))
    {
      ((ViewGroup)agentContainerView().findViewById(R.id.photoLayout)).addView(paramCell.view);
      return;
    }
    ((ViewGroup)agentContainerView().findViewById(R.id.layout)).addView(paramCell.view);
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new AddReviewConfigurableFragment.DefaultAddViewAgentListConfig());
    return localArrayList;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    if (((AddReviewActivity)getActivity()).extras != null)
      this.shop = ((DPObject)((AddReviewActivity)getActivity()).extras.getParcelable("shop"));
    if ((this.shop == null) && (paramBundle != null))
      this.shop = ((DPObject)paramBundle.getParcelable("shop"));
    super.onActivityCreated(paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = (ViewGroup)paramLayoutInflater.inflate(R.layout.addreview, paramViewGroup, false);
    setAgentContainerView(paramLayoutInflater);
    return paramLayoutInflater;
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("shop", this.shop);
  }

  public void onViewStateRestored(Bundle paramBundle)
  {
    super.onViewStateRestored(paramBundle);
    if ((this.shop == null) && (paramBundle != null))
      this.shop = ((DPObject)paramBundle.getParcelable("shop"));
  }

  protected void resetAgentContainerView()
  {
    ((ViewGroup)agentContainerView().findViewById(R.id.tagLayout)).removeAllViews();
    ((ViewGroup)agentContainerView().findViewById(R.id.snsLayout)).removeAllViews();
    ((ViewGroup)agentContainerView().findViewById(R.id.layout)).removeAllViews();
    ((ViewGroup)agentContainerView().findViewById(R.id.photoLayout)).removeAllViews();
  }

  public void showBannerView(String paramString)
  {
    ViewGroup localViewGroup = (ViewGroup)agentContainerView().findViewById(R.id.bannerLayout);
    if (localViewGroup != null)
    {
      TextView localTextView = (TextView)localViewGroup.findViewById(R.id.bannerView);
      if (localTextView != null)
      {
        localViewGroup.setVisibility(0);
        localTextView.setText(paramString);
      }
    }
  }

  public void showTuanTitleView(String paramString)
  {
    ViewGroup localViewGroup = (ViewGroup)agentContainerView().findViewById(R.id.tuanTitleLayout);
    if (localViewGroup != null)
    {
      TextView localTextView = (TextView)localViewGroup.findViewById(R.id.tvTuanTitle);
      if (localTextView != null)
      {
        localViewGroup.setVisibility(0);
        localTextView.setText(paramString);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.add.AddReviewConfigurableFragment
 * JD-Core Version:    0.6.0
 */