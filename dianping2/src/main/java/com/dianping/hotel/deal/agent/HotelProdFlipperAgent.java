package com.dianping.hotel.deal.agent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils.TruncateAt;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.agent.TuanGroupCellAgent;
import com.dianping.base.tuan.widget.TuanFlipper;
import com.dianping.base.tuan.widget.TuanFlipperAdapter;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.base.widget.Flipper;
import com.dianping.base.widget.NetworkPhotoView;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class HotelProdFlipperAgent extends TuanGroupCellAgent
{
  private final String HOTEL_PROD_HEAD_IMAGE = "hotelprodheadimage";
  protected ImageAdapter adapter;
  private DPObject dpHotelProdBase;
  private boolean isShowBigPhoto = true;
  protected TextView mCountNavigator;
  protected Flipper<Integer> mFlipperView;
  protected FrameLayout mHeadView;
  protected TextView mLastPicText;
  protected NetworkPhotoView mPhoto;
  protected FrameLayout mPhotoContainer;
  protected View mPhotoMask;
  protected View mSinglePhotoContainer;
  protected TextView mSubtitle;
  protected TextView mTitle;
  protected View mTitleContainer;

  public HotelProdFlipperAgent(Object paramObject)
  {
    super(paramObject);
  }

  public View getView()
  {
    if (this.mHeadView == null)
      setupView(this.isShowBigPhoto);
    return this.mHeadView;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getContext() == null);
    do
    {
      return;
      if (paramBundle != null)
        this.dpHotelProdBase = ((DPObject)paramBundle.getParcelable("hotelprodbase"));
      this.isShowBigPhoto = NovaConfigUtils.isShowImageInMobileNetwork();
    }
    while (this.dpHotelProdBase == null);
    if (this.mHeadView == null)
      setupView(this.isShowBigPhoto);
    updateView(this.isShowBigPhoto);
  }

  public SharedPreferences preferences(Context paramContext)
  {
    return paramContext.getSharedPreferences(paramContext.getPackageName(), 0);
  }

  protected void setupView(boolean paramBoolean)
  {
    this.mHeadView = ((FrameLayout)this.res.inflate(getContext(), R.layout.tuan_deal_info_header, getParentView(), false));
    this.mPhotoContainer = ((FrameLayout)this.mHeadView.findViewById(R.id.photo_container));
    if (paramBoolean)
    {
      int i = getContext().getResources().getDisplayMetrics().widthPixels * 5 / 8;
      this.mPhotoContainer.getLayoutParams().height = i;
    }
    this.mSinglePhotoContainer = this.mHeadView.findViewById(R.id.single_photo);
    this.mPhoto = ((NetworkPhotoView)this.mHeadView.findViewById(16908294));
    this.mPhotoMask = this.mHeadView.findViewById(R.id.deal_photo_mask);
    this.mTitleContainer = this.mHeadView.findViewById(R.id.deal_info_header_title_ll);
    this.mTitle = ((TextView)this.mHeadView.findViewById(R.id.short_title));
    this.mSubtitle = ((TextView)this.mHeadView.findViewById(R.id.title));
    this.mLastPicText = ((TextView)this.mHeadView.findViewById(R.id.last_pic_text));
    this.mCountNavigator = ((TextView)this.mHeadView.findViewById(R.id.deal_flipper_count));
    this.mTitle.setSingleLine(false);
    this.mTitle.setMaxLines(2);
    this.mTitle.setEllipsize(TextUtils.TruncateAt.END);
    this.mSubtitle.setSingleLine(false);
    this.mSubtitle.setMaxLines(2);
    this.mSubtitle.setEllipsize(TextUtils.TruncateAt.END);
  }

  protected void updateFlip()
  {
    if (this.dpHotelProdBase == null)
      return;
    String[] arrayOfString = this.dpHotelProdBase.getStringArray("PicUrlList");
    if ((arrayOfString == null) || (arrayOfString.length == 0))
    {
      this.mCountNavigator.setVisibility(8);
      return;
    }
    if (this.mFlipperView != null)
      this.mPhotoContainer.removeView(this.mFlipperView);
    this.mFlipperView = new TuanFlipper(getContext());
    this.mFlipperView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
    this.adapter = new ImageAdapter(getContext());
    this.mFlipperView.setAdapter(this.adapter);
    this.mFlipperView.setCurrentItem(Integer.valueOf(0));
    this.mFlipperView.enableNavigationDotView(0);
    this.mPhotoContainer.addView(this.mFlipperView, 0);
    if (arrayOfString.length > 1)
    {
      this.mCountNavigator.setVisibility(0);
      return;
    }
    this.mCountNavigator.setVisibility(8);
  }

  protected void updateView(boolean paramBoolean)
  {
    removeAllCells();
    if (this.dpHotelProdBase == null)
      return;
    String str;
    if (paramBoolean)
    {
      this.mSinglePhotoContainer.setVisibility(8);
      this.mCountNavigator.setVisibility(0);
      this.mPhotoMask.setVisibility(0);
      this.mTitleContainer.setBackgroundColor(0);
      this.mTitle.setTextColor(-1);
      this.mSubtitle.setTextColor(-1);
      updateFlip();
      str = this.dpHotelProdBase.getString("Title");
      if ((str != null) && (str.length() != 0))
        break label201;
      this.mTitle.setVisibility(8);
      label100: str = this.dpHotelProdBase.getString("Subtitle");
      if ((str != null) && (str.length() != 0))
        break label220;
      this.mSubtitle.setVisibility(8);
    }
    while (true)
    {
      addCell("", this.mHeadView);
      return;
      this.mSinglePhotoContainer.setVisibility(8);
      this.mCountNavigator.setVisibility(8);
      this.mPhotoMask.setVisibility(8);
      this.mTitleContainer.setBackgroundColor(-1);
      this.mTitle.setTextColor(-16777216);
      this.mSubtitle.setTextColor(-7829368);
      break;
      label201: this.mTitle.setVisibility(0);
      this.mTitle.setText(str);
      break label100;
      label220: this.mSubtitle.setVisibility(0);
      this.mSubtitle.setText(str);
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
      String[] arrayOfString = HotelProdFlipperAgent.this.dpHotelProdBase.getStringArray("PicUrlList");
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
      NetworkPhotoView localNetworkPhotoView = null;
      Object localObject = localNetworkPhotoView;
      if (paramInteger != null)
      {
        if (paramInteger.intValue() < 0)
          localObject = localNetworkPhotoView;
      }
      else
        return localObject;
      if ((paramView != null) && (paramView.getTag() == this));
      while (true)
      {
        localNetworkPhotoView = (NetworkPhotoView)paramView.findViewById(16908294);
        String[] arrayOfString = HotelProdFlipperAgent.this.dpHotelProdBase.getStringArray("PicUrlList");
        localObject = paramView;
        if (arrayOfString == null)
          break;
        localObject = paramView;
        if (paramInteger.intValue() >= arrayOfString.length)
          break;
        localNetworkPhotoView.setImage(arrayOfString[paramInteger.intValue()]);
        localNetworkPhotoView.setImageModule("hotelprodheadimage");
        return paramView;
        paramView = HotelProdFlipperAgent.this.res.inflate(HotelProdFlipperAgent.this.getContext(), R.layout.tuan_deal_detail_flipper_item, null, false);
        paramView.setTag(this);
      }
    }

    public boolean isLastItem(Integer paramInteger)
    {
      String[] arrayOfString = HotelProdFlipperAgent.this.dpHotelProdBase.getStringArray("PicUrlList");
      return (arrayOfString != null) && (paramInteger.intValue() == arrayOfString.length - 1);
    }

    public void onMoveToPrevious(boolean paramBoolean)
    {
      HotelProdFlipperAgent.this.mLastPicText.setVisibility(8);
    }

    public void onMoved(Integer paramInteger1, Integer paramInteger2)
    {
      paramInteger1 = new SpannableString(paramInteger2.intValue() + 1 + "/" + HotelProdFlipperAgent.this.dpHotelProdBase.getStringArray("PicUrlList").length);
      paramInteger1.setSpan(new ForegroundColorSpan(-1), 0, 1, 17);
      HotelProdFlipperAgent.this.mCountNavigator.setText(paramInteger1);
    }

    public void onMovedToEnd()
    {
      Object localObject = HotelProdFlipperAgent.this.dpHotelProdBase.getString("MoreDetailUrl");
      if ((localObject == null) || (!((String)localObject).startsWith("dianping")))
      {
        HotelProdFlipperAgent.this.mLastPicText.setVisibility(8);
        return;
      }
      if (HotelProdFlipperAgent.this.mLastPicText.getVisibility() == 0)
      {
        localObject = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject));
        HotelProdFlipperAgent.this.getContext().startActivity((Intent)localObject);
        return;
      }
      HotelProdFlipperAgent.this.mLastPicText.setVisibility(0);
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
 * Qualified Name:     com.dianping.hotel.deal.agent.HotelProdFlipperAgent
 * JD-Core Version:    0.6.0
 */