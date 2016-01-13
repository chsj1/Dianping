package com.dianping.search.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.search.shoplist.data.model.DisplayContent;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;

public class ShopListRankContentItem extends NovaLinearLayout
{
  private TextView mCountView;
  private NetworkThumbView mIcon;
  private TextView mTitleView;

  public ShopListRankContentItem(Context paramContext)
  {
    super(paramContext);
  }

  public ShopListRankContentItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ShopListRankContentItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public void onFinishInflate()
  {
    super.onFinishInflate();
    this.mIcon = ((NetworkThumbView)findViewById(R.id.icon));
    this.mTitleView = ((TextView)findViewById(R.id.title));
    this.mCountView = ((TextView)findViewById(R.id.count));
  }

  public void setContent(DisplayContent paramDisplayContent)
  {
    if (!android.text.TextUtils.isEmpty(paramDisplayContent.iconUrl))
    {
      this.mIcon.setImage(paramDisplayContent.iconUrl);
      this.mIcon.setVisibility(0);
    }
    while (true)
    {
      this.mTitleView.setText(com.dianping.util.TextUtils.highLightShow(getContext(), paramDisplayContent.title, R.color.light_line_red));
      if (android.text.TextUtils.isEmpty(paramDisplayContent.countText))
        break;
      this.mCountView.setText(paramDisplayContent.countText);
      this.mCountView.setVisibility(0);
      return;
      this.mIcon.setVisibility(8);
    }
    this.mCountView.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.widget.ShopListRankContentItem
 * JD-Core Version:    0.6.0
 */