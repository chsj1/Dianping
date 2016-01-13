package com.dianping.base.widget.phototag;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PictureTagLayout extends NovaRelativeLayout
{
  private static final int CLICKRANGE = 10;
  private static final int DEFAULT_TAG_HEIGHT = 50;
  private static final int LONGCLICKRANGE = 10;
  private static final int LONG_CLICK_DURATION = 1000;
  private Map<String, PictureDecalView> DecalMap;
  private PictureTagView.TagItem addressItem;
  private View addressView;
  private boolean canChangeDirection;
  private boolean canMove;
  private View clickView;
  private boolean isMoved;
  private View longClickView;
  private Context mContext;
  private Runnable mLongPressRunnable;
  Generate9RectItem mRectItem;
  private OnTagAddListener mTagAddListener;
  private OnTagClickListener mTagClickListener;
  private OnTagLongClickListener mTagLongClickListener;
  private OnFocusedChangedDecalListener onFocusedChangedDecalListener;
  private OnRemoveDecalListener onRemoveDecalListener;
  private OnRetryDecalListener onRetryDecalListener;
  private int startTouchViewLeft = 0;
  private int startTouchViewTop = 0;
  private int startX = 0;
  private int startY = 0;
  private Map<Integer, PictureTagView.TagItem> tagItemMap = new HashMap();
  private int tagViewHeight = 50;
  private int tagWidthWithoutText;
  private View touchView;

  public PictureTagLayout(Context paramContext)
  {
    super(paramContext, null);
    int i = PictureTagView.TagItem.tagid + 1;
    PictureTagView.TagItem.tagid = i;
    this.addressItem = new PictureTagView.TagItem(i, 2);
    this.canChangeDirection = true;
    this.canMove = true;
    this.DecalMap = new HashMap();
    this.mRectItem = new Generate9RectItem();
    this.mContext = paramContext;
  }

  public PictureTagLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    int i = PictureTagView.TagItem.tagid + 1;
    PictureTagView.TagItem.tagid = i;
    this.addressItem = new PictureTagView.TagItem(i, 2);
    this.canChangeDirection = true;
    this.canMove = true;
    this.DecalMap = new HashMap();
    this.mRectItem = new Generate9RectItem();
    this.mContext = paramContext;
    init();
  }

  private void changeTagDirection(PictureTagView paramPictureTagView, boolean paramBoolean)
  {
    int i = paramPictureTagView.getWidth();
    if (paramBoolean)
      i = measureTagViewWidth(paramPictureTagView);
    PictureTagView.TagItem localTagItem = paramPictureTagView.tagItem;
    RelativeLayout.LayoutParams localLayoutParams;
    if (localTagItem.direction == PictureTagView.Direction.Right)
    {
      localLayoutParams = (RelativeLayout.LayoutParams)paramPictureTagView.getLayoutParams();
      i = localTagItem.x - i + this.tagViewHeight / 2;
      if (i >= 0);
    }
    do
    {
      return;
      localLayoutParams.leftMargin = i;
      paramPictureTagView.setLayoutParams(localLayoutParams);
      localTagItem.direction = PictureTagView.Direction.Left;
      paramPictureTagView.directionChange();
      return;
      localLayoutParams = (RelativeLayout.LayoutParams)paramPictureTagView.getLayoutParams();
    }
    while (localTagItem.x + i - this.tagViewHeight / 2 > getWidth());
    localLayoutParams.leftMargin = (localTagItem.x - this.tagViewHeight / 2);
    paramPictureTagView.setLayoutParams(localLayoutParams);
    localTagItem.direction = PictureTagView.Direction.Right;
    paramPictureTagView.directionChange();
  }

  private void checkNeedChangeDirection(PictureTagView paramPictureTagView)
  {
    if ((paramPictureTagView.tagItem.direction == PictureTagView.Direction.Right) && (paramPictureTagView.tagItem.x > getWidth() / 2))
      if (paramPictureTagView.tagItem.x + measureTagViewWidth(paramPictureTagView) - this.tagViewHeight / 2 >= getWidth())
        changeTagDirection(paramPictureTagView, true);
    do
      return;
    while ((paramPictureTagView.tagItem.direction != PictureTagView.Direction.Left) || (paramPictureTagView.tagItem.x >= getWidth() / 2) || (paramPictureTagView.tagItem.x - measureTagViewWidth(paramPictureTagView) + this.tagViewHeight / 2 > 0));
    changeTagDirection(paramPictureTagView, true);
  }

  private void checkPositionIsValidate(PictureTagView paramPictureTagView)
  {
    RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
    int i = 1;
    if (paramPictureTagView.tagItem.y + this.tagViewHeight / 2 > getHeight())
    {
      paramPictureTagView.tagItem.y = (getHeight() - this.tagViewHeight / 2);
      localLayoutParams.leftMargin = paramPictureTagView.tagItem.x;
      localLayoutParams.topMargin = paramPictureTagView.tagItem.y;
      i = 0;
    }
    if (i == 0)
      paramPictureTagView.setLayoutParams(localLayoutParams);
    if (paramPictureTagView.tagItem.x + measureTagViewWidth(paramPictureTagView) - this.tagViewHeight / 2 >= getWidth())
      changeTagDirection(paramPictureTagView, true);
  }

  private boolean hasView(int paramInt1, int paramInt2)
  {
    int i = 0;
    if (i < getChildCount())
    {
      View localView = getChildAt(i);
      if ((localView instanceof PictureDecalView));
      do
      {
        i += 1;
        break;
      }
      while (!new Rect(localView.getLeft(), localView.getTop(), localView.getRight(), localView.getBottom()).contains(paramInt1, paramInt2));
      this.touchView = localView;
      this.touchView.bringToFront();
      return true;
    }
    this.touchView = null;
    return false;
  }

  private void init()
  {
    this.tagViewHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.ugc_picturetag_height);
    this.tagWidthWithoutText = ViewUtils.dip2px(this.mContext, 14.0F);
    this.mLongPressRunnable = new Runnable()
    {
      public void run()
      {
        if ((PictureTagLayout.this.touchView == null) || (PictureTagLayout.this.isMoved));
        do
        {
          do
          {
            return;
            PictureTagLayout.access$202(PictureTagLayout.this, PictureTagLayout.this.touchView);
            PictureTagLayout.access$002(PictureTagLayout.this, null);
          }
          while (PictureTagLayout.this.mTagLongClickListener == null);
          if (PictureTagLayout.this.longClickView != PictureTagLayout.this.addressView)
            continue;
          PictureTagLayout.this.mTagLongClickListener.onTagLongClick(PictureTagLayout.this.addressItem);
          return;
        }
        while (!(PictureTagLayout.this.longClickView instanceof PictureTagView));
        PictureTagView localPictureTagView = (PictureTagView)PictureTagLayout.this.longClickView;
        PictureTagLayout.this.mTagLongClickListener.onTagLongClick(localPictureTagView.tagItem);
      }
    };
  }

  private boolean isDirectionRect(PictureTagView paramPictureTagView, int paramInt1, int paramInt2)
  {
    return new RectF(paramPictureTagView.tagItem.x - this.tagViewHeight / 2, paramPictureTagView.tagItem.y - this.tagViewHeight / 2, paramPictureTagView.tagItem.x + this.tagViewHeight / 2, paramPictureTagView.tagItem.y + this.tagViewHeight / 2).contains(paramInt1, paramInt2);
  }

  private int measureTagViewWidth(PictureTagView paramPictureTagView)
  {
    return measureTextLength(paramPictureTagView) + this.tagWidthWithoutText + this.tagViewHeight;
  }

  private int measureTextLength(PictureTagView paramPictureTagView)
  {
    PictureTagView.TagItem localTagItem = paramPictureTagView.tagItem;
    return (int)paramPictureTagView.getLeftTextView().getPaint().measureText("" + localTagItem.tagContent);
  }

  private void moveView(int paramInt1, int paramInt2)
  {
    if ((!this.canMove) || (this.touchView == null) || (this.touchView == this.addressView));
    RelativeLayout.LayoutParams localLayoutParams;
    do
    {
      return;
      localLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
      localLayoutParams.leftMargin = (paramInt1 - this.startX + this.startTouchViewLeft);
      localLayoutParams.topMargin = (paramInt2 - this.startY + this.startTouchViewTop);
      if ((localLayoutParams.leftMargin < 0) || (localLayoutParams.leftMargin + this.touchView.getWidth() > getWidth()))
        localLayoutParams.leftMargin = this.touchView.getLeft();
      if ((localLayoutParams.topMargin < 0) || (localLayoutParams.topMargin + this.touchView.getHeight() > getHeight()))
        localLayoutParams.topMargin = this.touchView.getTop();
      this.touchView.setLayoutParams(localLayoutParams);
    }
    while (!(this.touchView instanceof PictureTagView));
    PictureTagView localPictureTagView = (PictureTagView)this.touchView;
    if (localPictureTagView.tagItem.direction == PictureTagView.Direction.Left);
    for (localPictureTagView.tagItem.x = (localLayoutParams.leftMargin + localPictureTagView.getWidth() - this.tagViewHeight / 2); ; localPictureTagView.tagItem.x = (localLayoutParams.leftMargin + this.tagViewHeight / 2))
    {
      localPictureTagView.tagItem.y = (localLayoutParams.topMargin + this.tagViewHeight / 2);
      return;
    }
  }

  public void addDecalItem(PictureDecalView.DecalItem paramDecalItem, Bitmap paramBitmap)
  {
    addDecalItem(paramDecalItem, paramBitmap, true);
  }

  public void addDecalItem(PictureDecalView.DecalItem paramDecalItem, Bitmap paramBitmap, boolean paramBoolean)
  {
    PictureDecalView localPictureDecalView = new PictureDecalView(this.mContext);
    RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
    localPictureDecalView.setDecalItem(paramDecalItem);
    localPictureDecalView.setFocusable(true);
    if ((paramDecalItem.centerX == -1) && (paramDecalItem.centerY == -1))
    {
      generateNext9RectItem();
      localPictureDecalView.setImage(paramBitmap, this.mRectItem.nextX + 40, this.mRectItem.nextY + 40, this.mRectItem.itemWidth - 80, this.mRectItem.itemHeight - 80);
    }
    while (true)
    {
      addView(localPictureDecalView, this.DecalMap.size(), localLayoutParams);
      this.DecalMap.put(paramDecalItem.name, localPictureDecalView);
      localPictureDecalView.setOnDecalDeleteListener(new PictureDecalView.OnDecalDeleteListener(paramDecalItem)
      {
        public void onDelete()
        {
          PictureDecalView localPictureDecalView = (PictureDecalView)PictureTagLayout.this.DecalMap.get(this.val$item.name);
          if (localPictureDecalView != null)
          {
            PictureTagLayout.this.DecalMap.remove(this.val$item.name);
            PictureTagLayout.this.removeView(localPictureDecalView);
          }
          if (PictureTagLayout.this.onRemoveDecalListener != null)
            PictureTagLayout.this.onRemoveDecalListener.onDecalRemove(this.val$item.name);
        }
      });
      localPictureDecalView.setOnDecalFocusedListener(new PictureDecalView.OnDecalFocusedListener(paramDecalItem)
      {
        public void onFocused(boolean paramBoolean)
        {
          if (paramBoolean)
            PictureTagLayout.this.updateDecalsFocused(this.val$item.name);
        }
      });
      localPictureDecalView.setOnRetryListener(new PictureDecalView.OnRetryListener(paramDecalItem)
      {
        public void onRetry()
        {
          if (PictureTagLayout.this.onRetryDecalListener != null)
            PictureTagLayout.this.onRetryDecalListener.onDecalRetry(this.val$item);
        }
      });
      if (!paramDecalItem.isFirstAddNoneFocused)
        localPictureDecalView.setFocused(true);
      return;
      localPictureDecalView.setImage(paramBitmap, paramDecalItem.centerX - paramDecalItem.width / 2, paramDecalItem.centerY - paramDecalItem.height / 2, paramDecalItem.width, paramDecalItem.height);
    }
  }

  public void addDecalItem(String paramString1, String paramString2, String paramString3, Bitmap paramBitmap)
  {
    PictureDecalView.DecalItem localDecalItem = new PictureDecalView.DecalItem();
    localDecalItem.groupId = paramString1;
    localDecalItem.name = paramString2;
    localDecalItem.url = paramString3;
    localDecalItem.centerX = -1;
    localDecalItem.centerY = -1;
    addDecalItem(localDecalItem, paramBitmap);
  }

  public void addTagItem(PictureTagView.TagItem paramTagItem)
  {
    if (paramTagItem.type == 1)
    {
      localObject1 = new PictureTagView(getContext(), paramTagItem);
      if ((paramTagItem.x < 0) && (paramTagItem.y < 0))
      {
        generateNext9RectItem();
        if ((measureTagViewWidth((PictureTagView)localObject1) + 15 > this.mRectItem.itemWidth) && (this.mRectItem.nextPosition % 3 == 2))
        {
          paramTagItem.x = (getWidth() - 15);
          ((PictureTagView)localObject1).directionChange();
          if (this.mRectItem.nextPosition % 3 != 0)
            break label251;
          paramTagItem.y = (this.mRectItem.nextY + this.mRectItem.itemHeight / 2 - this.tagViewHeight);
        }
      }
      else
      {
        label123: localObject2 = new RelativeLayout.LayoutParams(-2, -2);
        if (paramTagItem.direction != PictureTagView.Direction.Left)
          break label317;
        ((RelativeLayout.LayoutParams)localObject2).leftMargin = (paramTagItem.x - measureTagViewWidth((PictureTagView)localObject1) + this.tagViewHeight / 2);
        label166: ((RelativeLayout.LayoutParams)localObject2).topMargin = (paramTagItem.y - this.tagViewHeight / 2);
        ((PictureTagView)localObject1).updateContent(paramTagItem);
        addView((View)localObject1, (ViewGroup.LayoutParams)localObject2);
        this.tagItemMap.put(Integer.valueOf(paramTagItem.ID), paramTagItem);
        new Handler().postDelayed(new Runnable((PictureTagView)localObject1)
        {
          public void run()
          {
            PictureTagLayout.this.checkPositionIsValidate(this.val$tagView);
          }
        }
        , 500L);
      }
    }
    label251: 
    do
    {
      return;
      paramTagItem.x = (this.mRectItem.nextX + 15);
      break;
      if (this.mRectItem.nextPosition % 3 == 1)
      {
        paramTagItem.y = (this.mRectItem.nextY + this.mRectItem.itemHeight / 2);
        break label123;
      }
      paramTagItem.y = (this.mRectItem.nextY + this.mRectItem.itemHeight / 2 + this.tagViewHeight);
      break label123;
      ((RelativeLayout.LayoutParams)localObject2).leftMargin = (paramTagItem.x - this.tagViewHeight / 2);
      break label166;
    }
    while (paramTagItem.type != 2);
    label317: this.addressItem = paramTagItem;
    Object localObject1 = paramTagItem.tagContent;
    if (this.addressView == null)
      this.addressView = LayoutInflater.from(this.mContext).inflate(R.layout.phototag_address_item_layout, null);
    Object localObject2 = (ImageView)this.addressView.findViewById(R.id.address_icon);
    if (paramTagItem.addressType == 2)
    {
      ((ImageView)localObject2).setImageResource(R.drawable.phototag_address_icon_movie);
      removeView(this.addressView);
      paramTagItem = new RelativeLayout.LayoutParams(-2, -2);
      if (getHeight() != 0)
        break label526;
    }
    label526: for (int i = getLayoutParams().height; ; i = getHeight())
    {
      paramTagItem.topMargin = (i - this.tagViewHeight - 15);
      paramTagItem.leftMargin = 10;
      if (paramTagItem.topMargin <= 0)
        paramTagItem.topMargin = (getMeasuredWidth() - this.tagViewHeight - 15);
      addView(this.addressView, paramTagItem);
      ((TextView)this.addressView.findViewById(R.id.address_text)).setText(TextUtils.jsonParseText((String)localObject1));
      return;
      ((ImageView)localObject2).setImageResource(R.drawable.phototag_address_icon_locate);
      break;
    }
  }

  public void generateNext9RectItem()
  {
    if ((getWidth() > 0) && (getHeight() > 0))
    {
      this.mRectItem.itemWidth = (getWidth() / 3);
      this.mRectItem.itemHeight = (getHeight() / 3);
    }
    this.mRectItem.nextPosition = ((this.mRectItem.nextPosition + 1) % 9);
    switch (this.mRectItem.nextPosition)
    {
    default:
    case 0:
    case 3:
    case 6:
    case 1:
    case 4:
    case 7:
    case 2:
    case 5:
    case 8:
    }
    while (true)
      switch (this.mRectItem.nextPosition)
      {
      default:
        return;
        this.mRectItem.nextX = 0;
        continue;
        this.mRectItem.nextX = this.mRectItem.itemWidth;
        continue;
        this.mRectItem.nextX = (this.mRectItem.itemWidth * 2);
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      }
    this.mRectItem.nextY = 0;
    return;
    this.mRectItem.nextY = this.mRectItem.itemHeight;
    return;
    this.mRectItem.nextY = (this.mRectItem.itemHeight * 2);
  }

  public ArrayList<PictureDecalView.DecalItem> getAllDecals()
  {
    Iterator localIterator = this.DecalMap.values().iterator();
    ArrayList localArrayList = new ArrayList();
    while (localIterator.hasNext())
    {
      PictureDecalView localPictureDecalView = (PictureDecalView)localIterator.next();
      localPictureDecalView.updateData();
      localArrayList.add(localPictureDecalView.decalItem);
    }
    return localArrayList;
  }

  public Collection<PictureTagView.TagItem> getAllTags()
  {
    return this.tagItemMap.values();
  }

  public boolean hasDecalLoading()
  {
    Iterator localIterator = this.DecalMap.values().iterator();
    while (localIterator.hasNext())
      if (((PictureDecalView)localIterator.next()).showLoading)
        return true;
    return false;
  }

  public boolean hasDecalRetry()
  {
    Iterator localIterator = this.DecalMap.values().iterator();
    while (localIterator.hasNext())
      if (((PictureDecalView)localIterator.next()).showRetry)
        return true;
    return false;
  }

  public boolean hasDecalsFocused()
  {
    Iterator localIterator = this.DecalMap.values().iterator();
    while (localIterator.hasNext())
      if (((PictureDecalView)localIterator.next()).getFocused())
        return true;
    return false;
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    switch (paramMotionEvent.getAction())
    {
    default:
    case 0:
    }
    do
    {
      return false;
      this.startX = (int)paramMotionEvent.getX();
      this.startY = (int)paramMotionEvent.getY();
    }
    while (!hasView(this.startX, this.startY));
    return true;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    switch (paramMotionEvent.getAction())
    {
    default:
    case 0:
    case 2:
      do
      {
        do
        {
          return true;
          this.touchView = null;
          this.longClickView = null;
          if (this.clickView != null)
            this.clickView = null;
          this.startX = (int)paramMotionEvent.getX();
          this.startY = (int)paramMotionEvent.getY();
          this.isMoved = false;
        }
        while (!hasView(this.startX, this.startY));
        this.startTouchViewLeft = this.touchView.getLeft();
        this.startTouchViewTop = this.touchView.getTop();
        postDelayed(this.mLongPressRunnable, 1000L);
        return true;
        i = (int)paramMotionEvent.getX();
        j = (int)paramMotionEvent.getY();
        moveView(i, j);
      }
      while ((this.touchView == null) || (this.isMoved) || ((Math.abs(i - this.startX) <= 10) && (Math.abs(j - this.startY) <= 10)));
      this.isMoved = true;
      removeCallbacks(this.mLongPressRunnable);
      return true;
    case 1:
    }
    int i = (int)paramMotionEvent.getX();
    int j = (int)paramMotionEvent.getY();
    removeCallbacks(this.mLongPressRunnable);
    if ((this.longClickView == null) && (Math.abs(i - this.startX) < 10) && (Math.abs(j - this.startY) < 10))
    {
      if (this.touchView != null)
        break label338;
      if ((!updateDecalsFocused("")) && (this.mTagAddListener != null))
      {
        i = PictureTagView.TagItem.tagid + 1;
        PictureTagView.TagItem.tagid = i;
        paramMotionEvent = new PictureTagView.TagItem(i);
        paramMotionEvent.x = this.startX;
        paramMotionEvent.y = this.startY;
        this.mTagAddListener.onTagAdd(paramMotionEvent);
      }
    }
    while (true)
    {
      this.touchView = null;
      return true;
      label338: this.clickView = this.touchView;
      if ((this.clickView instanceof PictureTagView))
      {
        paramMotionEvent = (PictureTagView)this.clickView;
        if ((isDirectionRect(paramMotionEvent, i, j)) && (this.canChangeDirection))
        {
          changeTagDirection(paramMotionEvent, false);
          return true;
        }
      }
      if (this.mTagClickListener == null)
        continue;
      if (this.clickView == this.addressView)
      {
        this.mTagClickListener.onTagClick(this.addressItem);
        continue;
      }
      if (!(this.clickView instanceof PictureTagView))
        continue;
      paramMotionEvent = (PictureTagView)this.clickView;
      this.mTagClickListener.onTagClick(paramMotionEvent.tagItem);
    }
  }

  public boolean removeTagItem(PictureTagView.TagItem paramTagItem)
  {
    if (paramTagItem.type == 1)
    {
      PictureTagView localPictureTagView = (PictureTagView)this.longClickView;
      if ((this.longClickView != null) && (localPictureTagView.tagItem.ID == paramTagItem.ID))
      {
        removeView(this.longClickView);
        this.tagItemMap.remove(Integer.valueOf(paramTagItem.ID));
        return true;
      }
    }
    else if ((paramTagItem.type == 2) && (this.addressView != null))
    {
      removeView(this.addressView);
      this.addressView = null;
      return true;
    }
    return false;
  }

  public void setCanChangeDirection(boolean paramBoolean)
  {
    this.canChangeDirection = paramBoolean;
  }

  public void setCanMove(boolean paramBoolean)
  {
    this.canMove = paramBoolean;
  }

  public void setDecalLoading(String paramString)
  {
    paramString = (PictureDecalView)this.DecalMap.get(paramString);
  }

  public void setDecalRetry(String paramString)
  {
    paramString = (PictureDecalView)this.DecalMap.get(paramString);
    if (paramString != null)
      paramString.showRetry(true);
  }

  public void setOnFocusedChangedDecalListener(OnFocusedChangedDecalListener paramOnFocusedChangedDecalListener)
  {
    this.onFocusedChangedDecalListener = paramOnFocusedChangedDecalListener;
  }

  public void setOnRetryDecalListener(OnRetryDecalListener paramOnRetryDecalListener)
  {
    this.onRetryDecalListener = paramOnRetryDecalListener;
  }

  public void setRemoveDecalListener(OnRemoveDecalListener paramOnRemoveDecalListener)
  {
    this.onRemoveDecalListener = paramOnRemoveDecalListener;
  }

  public void setTagAddListener(OnTagAddListener paramOnTagAddListener)
  {
    this.mTagAddListener = paramOnTagAddListener;
  }

  public void setTagClickListener(OnTagClickListener paramOnTagClickListener)
  {
    this.mTagClickListener = paramOnTagClickListener;
  }

  public void setTagLongClickListener(OnTagLongClickListener paramOnTagLongClickListener)
  {
    this.mTagLongClickListener = paramOnTagLongClickListener;
  }

  public void updateDecalItem(String paramString, Bitmap paramBitmap)
  {
    paramString = (PictureDecalView)this.DecalMap.get(paramString);
    if (paramString != null)
      paramString.setImage(paramBitmap);
  }

  public boolean updateDecalsFocused(String paramString)
  {
    Iterator localIterator = this.DecalMap.values().iterator();
    int i = 0;
    while (localIterator.hasNext())
    {
      PictureDecalView localPictureDecalView = (PictureDecalView)localIterator.next();
      if ((!TextUtils.isEmpty(localPictureDecalView.decalItem.name)) && (localPictureDecalView.decalItem.name.equals(paramString)))
      {
        if (this.onFocusedChangedDecalListener == null)
          continue;
        this.onFocusedChangedDecalListener.onFocusedChanged(localPictureDecalView.decalItem, true);
        continue;
      }
      if (!localPictureDecalView.getFocused())
        continue;
      int j = 1;
      localPictureDecalView.setFocused(false);
      i = j;
      if (this.onFocusedChangedDecalListener == null)
        continue;
      this.onFocusedChangedDecalListener.onFocusedChanged(localPictureDecalView.decalItem, false);
      i = j;
    }
    return i;
  }

  public void updateTagItem(PictureTagView.TagItem paramTagItem)
  {
    if (paramTagItem.type == 1)
      if ((this.clickView != null) && ((this.clickView instanceof PictureTagView)))
      {
        PictureTagView localPictureTagView = (PictureTagView)this.clickView;
        localPictureTagView.updateContent(paramTagItem);
        localPictureTagView.tagItem.tagContent = paramTagItem.tagContent;
        if (paramTagItem.direction == PictureTagView.Direction.Left)
        {
          int i = paramTagItem.x;
          int j = measureTagViewWidth(localPictureTagView);
          int k = this.tagViewHeight / 2;
          paramTagItem = (RelativeLayout.LayoutParams)localPictureTagView.getLayoutParams();
          paramTagItem.leftMargin = (i - j + k);
          localPictureTagView.setLayoutParams(paramTagItem);
        }
        checkNeedChangeDirection(localPictureTagView);
      }
    do
    {
      do
        return;
      while (paramTagItem.type != 2);
      this.addressItem = paramTagItem;
      paramTagItem = paramTagItem.tagContent;
    }
    while (this.addressView == null);
    ((TextView)this.addressView.findViewById(R.id.address_text)).setText(paramTagItem);
  }

  public class Generate9RectItem
  {
    public int itemHeight = 200;
    public int itemWidth = 200;
    public int nextPosition = -1;
    public int nextX;
    public int nextY;

    public Generate9RectItem()
    {
    }
  }

  public static abstract interface OnFocusedChangedDecalListener
  {
    public abstract void onFocusedChanged(PictureDecalView.DecalItem paramDecalItem, boolean paramBoolean);
  }

  public static abstract interface OnRemoveDecalListener
  {
    public abstract void onDecalRemove(String paramString);
  }

  public static abstract interface OnRetryDecalListener
  {
    public abstract void onDecalRetry(PictureDecalView.DecalItem paramDecalItem);
  }

  public static abstract interface OnTagAddListener
  {
    public abstract void onTagAdd(PictureTagView.TagItem paramTagItem);
  }

  public static abstract interface OnTagClickListener
  {
    public abstract void onTagClick(PictureTagView.TagItem paramTagItem);
  }

  public static abstract interface OnTagLongClickListener
  {
    public abstract void onTagLongClick(PictureTagView.TagItem paramTagItem);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.phototag.PictureTagLayout
 * JD-Core Version:    0.6.0
 */