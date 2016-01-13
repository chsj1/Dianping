package com.dianping.base.tuan.agent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.fragment.TuanAgentFragment.Styleable;
import com.dianping.base.tuan.widget.TuanFlipper;
import com.dianping.base.tuan.widget.TuanFlipperAdapter;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.base.widget.Flipper;
import com.dianping.base.widget.NetworkPhotoView;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class DealInfoFlipperAgent extends TuanGroupCellAgent
{
  public static final int FLIPPER_STYLE_NOTITLE = 1;
  protected ImageAdapter adapter;
  protected TextView content;
  protected TextView countNavigator;
  protected DPObject dpDeal;
  protected Flipper<Integer> flipperView;
  protected FrameLayout headView;
  protected boolean isShowBigPhoto = true;
  protected TextView lastPicText;
  protected NetworkPhotoView photo;
  protected FrameLayout photoContainer;
  protected View photoMask;
  protected TextView shortTitle;
  protected View singlePhotoContainer;
  protected ImageView tagImageView;
  protected View titleContainer;

  public DealInfoFlipperAgent(Object paramObject)
  {
    super(paramObject);
  }

  public int getStyle()
  {
    if ((this.fragment instanceof TuanAgentFragment.Styleable))
      return ((TuanAgentFragment.Styleable)this.fragment).getStyle();
    return 0;
  }

  public View getView()
  {
    if (this.headView == null)
      setupView(this.isShowBigPhoto);
    return this.headView;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (paramBundle != null)
    {
      paramBundle = (DPObject)paramBundle.getParcelable("deal");
      if (this.dpDeal != paramBundle)
        this.dpDeal = paramBundle;
    }
    this.isShowBigPhoto = NovaConfigUtils.isShowImageInMobileNetwork();
    if (getContext() == null);
    do
      return;
    while (this.dpDeal == null);
    if (this.headView == null)
      setupView(this.isShowBigPhoto);
    updateView(this.isShowBigPhoto);
  }

  public SharedPreferences preferences(Context paramContext)
  {
    return paramContext.getSharedPreferences(paramContext.getPackageName(), 0);
  }

  protected void setupView(boolean paramBoolean)
  {
    this.headView = ((FrameLayout)this.res.inflate(getContext(), R.layout.tuan_deal_info_header, getParentView(), false));
    this.photoContainer = ((FrameLayout)this.headView.findViewById(R.id.photo_container));
    if (paramBoolean)
    {
      int i = getContext().getResources().getDisplayMetrics().widthPixels * 5 / 8;
      this.photoContainer.getLayoutParams().height = i;
    }
    this.tagImageView = ((ImageView)this.headView.findViewById(R.id.tag_image));
    this.singlePhotoContainer = this.headView.findViewById(R.id.single_photo);
    this.photo = ((NetworkPhotoView)this.headView.findViewById(16908294));
    this.photoMask = this.headView.findViewById(R.id.deal_photo_mask);
    this.titleContainer = this.headView.findViewById(R.id.deal_info_header_title_ll);
    this.shortTitle = ((TextView)this.headView.findViewById(R.id.short_title));
    this.content = ((TextView)this.headView.findViewById(R.id.title));
    this.lastPicText = ((TextView)this.headView.findViewById(R.id.last_pic_text));
    this.countNavigator = ((TextView)this.headView.findViewById(R.id.deal_flipper_count));
    if ((getStyle() & 0x1) == 1)
    {
      this.titleContainer.setVisibility(4);
      this.photoMask.setVisibility(4);
    }
  }

  protected void updateFlip()
  {
    if (this.dpDeal == null);
    String[] arrayOfString;
    while (true)
    {
      return;
      arrayOfString = this.dpDeal.getStringArray("DetailPhotos");
      if ((arrayOfString != null) && (arrayOfString.length != 0) && (arrayOfString[0] != null))
        break;
      if (TextUtils.isEmpty(this.dpDeal.getString("BigPhoto")))
        continue;
      this.photo.setImage(this.dpDeal.getString("BigPhoto"));
      this.photo.setImageModule("tuandealheadimage");
      this.countNavigator.setVisibility(8);
      return;
    }
    if (this.flipperView != null)
      this.photoContainer.removeView(this.flipperView);
    this.flipperView = new TuanFlipper(getContext());
    this.flipperView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
    this.adapter = new ImageAdapter(getContext());
    this.flipperView.setAdapter(this.adapter);
    this.flipperView.setCurrentItem(Integer.valueOf(0));
    this.flipperView.enableNavigationDotView(0);
    this.photoContainer.addView(this.flipperView, 0);
    this.singlePhotoContainer.setVisibility(8);
    if (arrayOfString.length > 1)
    {
      this.countNavigator.setVisibility(0);
      return;
    }
    this.countNavigator.setVisibility(8);
  }

  protected void updateView(boolean paramBoolean)
  {
    removeAllCells();
    if (this.dpDeal == null)
      return;
    if (this.dpDeal.getInt("DealType") == 4)
    {
      this.tagImageView.setVisibility(0);
      this.shortTitle.setText(this.dpDeal.getString("ShortTitle"));
      this.content.setText(this.dpDeal.getString("DealTitle"));
      if (!paramBoolean)
        break label147;
      this.singlePhotoContainer.setVisibility(0);
      this.countNavigator.setVisibility(0);
      this.photoMask.setVisibility(0);
      this.titleContainer.setBackgroundColor(0);
      this.shortTitle.setTextColor(-1);
      this.content.setTextColor(-1);
      updateFlip();
    }
    while (true)
    {
      addCell("010Basic.010Flipper", this.headView);
      return;
      this.tagImageView.setVisibility(4);
      break;
      label147: this.singlePhotoContainer.setVisibility(8);
      this.countNavigator.setVisibility(8);
      this.photoMask.setVisibility(8);
      this.titleContainer.setBackgroundColor(-1);
      this.shortTitle.setTextColor(-16777216);
      this.content.setTextColor(-7829368);
    }
  }

  protected class ImageAdapter
    implements TuanFlipperAdapter<Integer>
  {
    public ImageAdapter(Context arg2)
    {
    }

    public Integer getNextItem(Integer paramInteger)
    {
      String[] arrayOfString = DealInfoFlipperAgent.this.dpDeal.getStringArray("DetailPhotos");
      if ((arrayOfString == null) || (arrayOfString.length == 0));
      do
        return null;
      while (paramInteger.intValue() + 1 >= arrayOfString.length);
      return Integer.valueOf(paramInteger.intValue() + 1);
    }

    public Integer getPreviousItem(Integer paramInteger)
    {
      if (paramInteger.intValue() > 0)
        return Integer.valueOf(paramInteger.intValue() - 1);
      return null;
    }

    public View getView(Integer paramInteger, View paramView)
    {
      View localView;
      if ((paramInteger == null) || (paramInteger.intValue() < 0))
      {
        localView = null;
        return localView;
      }
      if ((paramView != null) && (paramView.getTag() == this));
      while (true)
      {
        NetworkPhotoView localNetworkPhotoView = (NetworkPhotoView)paramView.findViewById(16908294);
        localView = paramView;
        if (DealInfoFlipperAgent.this.dpDeal.getStringArray("DetailPhotos") == null)
          break;
        localView = paramView;
        if (paramInteger.intValue() >= DealInfoFlipperAgent.this.dpDeal.getStringArray("DetailPhotos").length)
          break;
        localNetworkPhotoView.setImage(DealInfoFlipperAgent.this.dpDeal.getStringArray("DetailPhotos")[paramInteger.intValue()]);
        localNetworkPhotoView.setImageModule("tuandealheadimage");
        return paramView;
        paramView = DealInfoFlipperAgent.this.res.inflate(DealInfoFlipperAgent.this.getContext(), R.layout.tuan_deal_detail_flipper_item, null, false);
        paramView.setTag(this);
      }
    }

    public boolean isLastItem(Integer paramInteger)
    {
      String[] arrayOfString = DealInfoFlipperAgent.this.dpDeal.getStringArray("DetailPhotos");
      return (arrayOfString != null) && (paramInteger.intValue() == arrayOfString.length - 1);
    }

    public void onMoveToPrevious(boolean paramBoolean)
    {
      DealInfoFlipperAgent.this.lastPicText.setVisibility(8);
    }

    public void onMoved(Integer paramInteger1, Integer paramInteger2)
    {
      paramInteger1 = new SpannableString(paramInteger2.intValue() + 1 + "/" + DealInfoFlipperAgent.this.dpDeal.getStringArray("DetailPhotos").length);
      paramInteger1.setSpan(new ForegroundColorSpan(-1), 0, 1, 17);
      DealInfoFlipperAgent.this.countNavigator.setText(paramInteger1);
    }

    public void onMovedToEnd()
    {
      if (DealInfoFlipperAgent.this.lastPicText.getVisibility() == 0)
      {
        Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://dealdetailmore"));
        localIntent.putExtra("mDeal", DealInfoFlipperAgent.this.dpDeal);
        DealInfoFlipperAgent.this.getContext().startActivity(localIntent);
        return;
      }
      DealInfoFlipperAgent.this.lastPicText.setVisibility(0);
    }

    public void onMoving(Integer paramInteger1, Integer paramInteger2)
    {
    }

    public void onTap(Integer paramInteger)
    {
    }

    public void recycleView(View paramView)
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoFlipperAgent
 * JD-Core Version:    0.6.0
 */