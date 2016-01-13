package com.dianping.search.deallist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.widget.TuanBannerView;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.ViewItemInterface;
import com.dianping.base.widget.ViewItemType;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;

public class BannerViewItem extends NovaLinearLayout
  implements ViewItemInterface
{
  protected static final int HEADER_VIEW_TYPE_BANNER = 2;
  protected static final int HEADER_VIEW_TYPE_NONE = 0;
  protected TuanBannerView bannerHeaderView;
  protected int headerViewType;
  protected View section;

  public BannerViewItem(Context paramContext)
  {
    super(paramContext);
  }

  public BannerViewItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public ViewItemType getType()
  {
    return ViewItemType.BANNER;
  }

  protected void hideHeaderView()
  {
    setVisibility(8);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.section = findViewById(R.id.section);
    this.section.setVisibility(8);
    this.bannerHeaderView = ((TuanBannerView)findViewById(R.id.ad_header_view));
    this.bannerHeaderView.setBtnOnCloseListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = BannerViewItem.this;
        paramView.headerViewType &= -3;
        BannerViewItem.this.setHeadViewType(BannerViewItem.this.headerViewType);
      }
    });
    setHeadViewType(this.headerViewType);
  }

  public void setBannerViewData(DPObject[] paramArrayOfDPObject)
  {
    this.bannerHeaderView.setBanner(paramArrayOfDPObject);
  }

  public void setHeadViewType(int paramInt)
  {
    this.headerViewType = paramInt;
    if (paramInt == 0)
      setVisibility(8);
    while ((paramInt & 0x2) != 0)
    {
      this.bannerHeaderView.setVisibility(0);
      return;
      setVisibility(0);
    }
    this.bannerHeaderView.setVisibility(8);
  }

  public void updateData(DPObject paramDPObject, double paramDouble1, double paramDouble2, boolean paramBoolean)
  {
    if (!DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem"));
    do
    {
      do
      {
        return;
        paramDPObject = paramDPObject.getObject("Agg");
      }
      while (!DPObjectUtils.isDPObjectof(paramDPObject, "ViewAggItem"));
      paramDPObject = paramDPObject.getArray("AggItems");
    }
    while (DPObjectUtils.isArrayEmpty(paramDPObject));
    DPObject[] arrayOfDPObject = new DPObject[paramDPObject.length];
    int i = 0;
    while (i < arrayOfDPObject.length)
    {
      if (paramDPObject[i] != null)
        arrayOfDPObject[i] = paramDPObject[i].getObject("Cell");
      i += 1;
    }
    setBannerViewData(arrayOfDPObject);
    if ((arrayOfDPObject != null) && (arrayOfDPObject.length > 0))
    {
      setHeadViewType(2);
      return;
    }
    setHeadViewType(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.widget.BannerViewItem
 * JD-Core Version:    0.6.0
 */