package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.util.AttributeSet;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.widget.view.NovaFrameLayout;

public class ExploreHotFeedGridViewItem extends NovaFrameLayout
{
  private DPNetworkImageView networkImageView;

  public ExploreHotFeedGridViewItem(Context paramContext)
  {
    super(paramContext);
  }

  public ExploreHotFeedGridViewItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ExploreHotFeedGridViewItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.networkImageView = ((DPNetworkImageView)findViewById(16908294));
  }

  public void setImageUrl(String paramString)
  {
    if (this.networkImageView != null)
      this.networkImageView.setImage(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.ExploreHotFeedGridViewItem
 * JD-Core Version:    0.6.0
 */