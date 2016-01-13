package com.dianping.base.tuan.agent;

import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.GroupAgentFragment;
import com.dianping.base.tuan.widget.DealInfoCommonCell;
import com.dianping.base.tuan.widget.TuanDealDish;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;

public class TuanDishAgent extends TuanGroupCellAgent
{
  private DealInfoCommonCell commCell;
  public String dishStr;
  public String mGAString;
  public View.OnClickListener mListener;
  private TuanDealDish tuanDealDish;

  public TuanDishAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void setupView()
  {
    this.tuanDealDish = new TuanDealDish(getContext());
    this.commCell = new DealInfoCommonCell(getContext());
    this.commCell.setTitleSize(0, getResources().getDimension(R.dimen.deal_info_agent_title_text_size));
    this.commCell.setArrowPreSize(0, getResources().getDimension(R.dimen.deal_info_agent_subtitle_text_size));
    this.commCell.setPaddingLeft((int)getResources().getDimension(R.dimen.deal_info_padding_left));
    this.commCell.setPaddingRight((int)getResources().getDimension(R.dimen.deal_info_padding_right));
    this.commCell.addContent(this.tuanDealDish, false);
  }

  private void updateView()
  {
    if (this.dishStr != null)
    {
      removeAllCells();
      this.tuanDealDish.setClickListener(this.mListener);
      this.tuanDealDish.setGAString(this.mGAString);
      ((NovaActivity)getContext()).addGAView(this.tuanDealDish, -1, "tuandeal", "tuandeal".equals(((NovaActivity)getContext()).getPageName()));
      this.tuanDealDish.setDishText(this.dishStr);
      this.commCell.setTitle("网友推荐", this.mListener);
      this.commCell.setIcon(R.drawable.detail_icon_good);
      if ((this.fragment instanceof GroupAgentFragment))
        addCell("031Dish.01Dish0", this.commCell);
    }
    else
    {
      return;
    }
    addCell("031Dish.01Dish0", this.commCell);
    addEmptyCell("031Dish.01Dish1");
  }

  public View getAgentView()
  {
    return this.tuanDealDish;
  }

  public void setDishOnClickListener(View.OnClickListener paramOnClickListener)
  {
    this.mListener = paramOnClickListener;
  }

  public void updateAgent()
  {
    if (this.commCell == null)
      setupView();
    updateView();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.TuanDishAgent
 * JD-Core Version:    0.6.0
 */