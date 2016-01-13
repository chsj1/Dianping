package com.dianping.hotel.deal.agent;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.agent.TuanGroupCellAgent;
import com.dianping.loader.MyResources;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class HotelProdReviewsAgent extends TuanGroupCellAgent
  implements View.OnClickListener
{
  private View contentView;
  private DPObject dpHotelProdDetail;
  private TextView recCount;
  private ImageView recIcon;
  private TextView recText;

  public HotelProdReviewsAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void updateView()
  {
    if (this.dpHotelProdDetail == null)
      return;
    Object localObject = this.dpHotelProdDetail.getString("ReviewRatio");
    String str = this.dpHotelProdDetail.getString("ReviewCountDesc");
    if (!TextUtils.isEmpty(str))
    {
      if (!TextUtils.isEmpty((CharSequence)localObject))
      {
        this.recIcon.setVisibility(0);
        localObject = new SpannableString("好评度 " + (String)localObject);
        ((SpannableString)localObject).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tuan_common_orange)), 3, ((SpannableString)localObject).length(), 33);
        this.recText.setText((CharSequence)localObject);
        this.recCount.setText(str);
      }
      while (true)
      {
        this.contentView.setVisibility(0);
        return;
        this.recIcon.setVisibility(8);
        this.recText.setText(str);
        this.recText.setPadding(ViewUtils.dip2px(getContext(), 10.0F), 0, 0, 0);
        this.recCount.setText("");
      }
    }
    this.contentView.setVisibility(8);
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
      this.dpHotelProdDetail = ((DPObject)paramBundle.getParcelable("hotelproddetail"));
    }
    while (this.dpHotelProdDetail == null);
    if (this.contentView == null)
      setupView();
    updateView();
  }

  public void onClick(View paramView)
  {
  }

  public void setupView()
  {
    this.contentView = this.res.inflate(getContext(), R.layout.deal_info_review, getParentView(), false);
    this.recIcon = ((ImageView)this.contentView.findViewById(R.id.deal_info_rec_icon));
    this.recText = ((TextView)this.contentView.findViewById(R.id.deal_info_rec_text));
    this.recCount = ((TextView)this.contentView.findViewById(R.id.deal_info_rec_count));
    addCell("", this.contentView);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.agent.HotelProdReviewsAgent
 * JD-Core Version:    0.6.0
 */