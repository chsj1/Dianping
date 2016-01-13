package com.dianping.hotel.ugc;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.TextView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;

public class ReviewTitleView extends TextView
{
  public ReviewTitleView(Context paramContext)
  {
    super(paramContext);
    init();
  }

  public ReviewTitleView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }

  private void init()
  {
    setTextColor(getResources().getColor(R.color.light_gray));
    setTextSize(0, getResources().getDimensionPixelOffset(R.dimen.text_large));
    setBackgroundColor(getResources().getColor(R.color.gray_background));
    setFocusable(false);
    int i = ViewUtils.dip2px(getContext(), 13.0F);
    int j = ViewUtils.dip2px(getContext(), 8.0F);
    setPadding(i, j, i, j);
  }

  public void setData(ReviewItem paramReviewItem)
  {
    setText(paramReviewItem.title);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.ugc.ReviewTitleView
 * JD-Core Version:    0.6.0
 */