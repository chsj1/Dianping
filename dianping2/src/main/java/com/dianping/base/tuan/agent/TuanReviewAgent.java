package com.dianping.base.tuan.agent;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.base.app.loader.GroupAgentFragment;
import com.dianping.loader.MyResources;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;

public class TuanReviewAgent extends TuanGroupCellAgent
{
  public String CELL_ID;
  private NovaRelativeLayout contentView;
  public String mGAString;
  public String mReviewCount;
  private ReviewOnClickListener mReviewOnClickListener;
  public String mReviewRatio;
  private TextView recCount;
  private ImageView recIcon;
  private TextView recText;

  public TuanReviewAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void setupView()
  {
    this.contentView = ((NovaRelativeLayout)this.res.inflate(getContext(), R.layout.deal_info_review, getParentView(), false));
    this.recIcon = ((ImageView)this.contentView.findViewById(R.id.deal_info_rec_icon));
    this.recText = ((TextView)this.contentView.findViewById(R.id.deal_info_rec_text));
    this.recCount = ((TextView)this.contentView.findViewById(R.id.deal_info_rec_count));
    this.contentView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (TuanReviewAgent.this.mReviewOnClickListener != null)
          TuanReviewAgent.this.mReviewOnClickListener.onClick(paramView);
      }
    });
  }

  private void updateView()
  {
    removeAllCells();
    if (!TextUtils.isEmpty(this.mReviewCount))
    {
      if (!TextUtils.isEmpty(this.mReviewRatio))
      {
        this.recIcon.setVisibility(0);
        SpannableString localSpannableString = new SpannableString("好评度 " + this.mReviewRatio);
        localSpannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tuan_common_orange)), 3, localSpannableString.length(), 33);
        this.recText.setText(localSpannableString);
        if (this.mReviewCount.contains("评价"))
        {
          this.recCount.setText(this.mReviewCount);
          this.contentView.setVisibility(0);
          if (!(this.fragment instanceof GroupAgentFragment))
            break label416;
          addCell(this.CELL_ID + "2", this.contentView);
        }
      }
      while (true)
      {
        if (!TextUtils.isEmpty(this.mGAString))
          this.contentView.setGAString(this.mGAString);
        return;
        if (this.mReviewCount.startsWith("共"))
        {
          this.recCount.setText(this.mReviewCount + "个消费评价");
          break;
        }
        this.recCount.setText("共" + this.mReviewCount + "个消费评价");
        break;
        this.recIcon.setVisibility(8);
        if (this.mReviewCount.contains("评价"))
          this.recCount.setText(this.mReviewCount);
        while (true)
        {
          this.recText.setPadding(ViewUtils.dip2px(getContext(), 10.0F), 0, 0, 0);
          this.recCount.setText("");
          break;
          if (this.mReviewCount.startsWith("共"))
          {
            this.recCount.setText(this.mReviewCount + "个消费评价");
            continue;
          }
          this.recCount.setText("共" + this.mReviewCount + "个消费评价");
        }
        label416: addDividerLine(this.CELL_ID + "1");
        addCell(this.CELL_ID + "2", this.contentView);
        addDividerLine(this.CELL_ID + "3");
        addEmptyCell(this.CELL_ID + "4");
      }
    }
    this.contentView.setVisibility(8);
  }

  public View getAgentView()
  {
    return this.contentView;
  }

  public void setReviewOnClickListener(ReviewOnClickListener paramReviewOnClickListener)
  {
    this.mReviewOnClickListener = paramReviewOnClickListener;
  }

  public void updateAgent()
  {
    if (this.contentView == null)
      setupView();
    updateView();
  }

  public static abstract interface ReviewOnClickListener
  {
    public abstract void onClick(View paramView);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.TuanReviewAgent
 * JD-Core Version:    0.6.0
 */