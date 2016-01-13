package com.dianping.widget.emoji;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.styleable;
import com.dianping.widget.FlipperPager;
import com.dianping.widget.FlipperPager.FlipperPagerAdapter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class EmojiView extends FrameLayout
{
  private static final Emoji DELETEEMOTION = new Emoji("delete", R.drawable.ic_key_delete);
  private static final Emoji TRANSPARENTEMOTION = new Emoji("transparent", 17170445);
  private final int columnCount;
  ArrayList<Emoji> emotionImages = new ArrayList();
  private final int emotionItemSize;
  private final int emotionItemSpacing;
  private FlipperPager emotionPage;
  private final int gridViewPaddingBottom;
  private final int gridViewPaddingLeft;
  private final int gridViewPaddingRight;
  private final int gridViewPaddingTop;
  private WeakReference<OnEmotionItemSelectedListener> listener;
  private int pages;
  private final int perPageCount;
  private final int rowCount;

  public EmojiView(Context paramContext)
  {
    this(paramContext, null);
  }

  public EmojiView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.EmojiView);
    this.columnCount = paramAttributeSet.getInt(R.styleable.EmojiView_emotionColumnCount, 7);
    this.rowCount = paramAttributeSet.getInt(R.styleable.EmojiView_emotionRowCount, 3);
    this.gridViewPaddingLeft = paramAttributeSet.getDimensionPixelSize(R.styleable.EmojiView_gridViewPaddingLeft, 0);
    this.gridViewPaddingRight = paramAttributeSet.getDimensionPixelSize(R.styleable.EmojiView_gridViewPaddingRight, 0);
    this.gridViewPaddingTop = paramAttributeSet.getDimensionPixelSize(R.styleable.EmojiView_gridViewPaddingTop, 0);
    this.gridViewPaddingBottom = paramAttributeSet.getDimensionPixelSize(R.styleable.EmojiView_gridViewPaddingBottom, 0);
    paramAttributeSet.recycle();
    this.emotionItemSize = getResources().getDimensionPixelSize(R.dimen.emoji_image_size);
    this.emotionItemSpacing = getResources().getDimensionPixelSize(R.dimen.emoji_image_spacing);
    this.perPageCount = (this.columnCount * this.rowCount);
    scanEmotionResource();
    this.emotionPage = new FlipperPager(paramContext);
    this.emotionPage.setLayoutParams(new FrameLayout.LayoutParams(-1, -1, 17));
    this.emotionPage.enableNavigationDotMode(true);
    addView(this.emotionPage);
    this.emotionPage.setAdapter(new FlipperAdapter());
    if ((getContext() instanceof OnEmotionItemSelectedListener))
      setOnEmotionItemSelectedListener((OnEmotionItemSelectedListener)getContext());
  }

  public void scanEmotionResource()
  {
    ArrayList localArrayList = EmojiMap.emojiList();
    int i = 0;
    if (i < localArrayList.size())
    {
      if (this.perPageCount + i - 1 < localArrayList.size());
      for (j = this.perPageCount + i - 1; ; j = localArrayList.size())
      {
        this.emotionImages.addAll(localArrayList.subList(i, j));
        this.emotionImages.add(DELETEEMOTION);
        i += this.perPageCount - 1;
        break;
      }
    }
    i = this.emotionImages.size();
    int j = this.perPageCount - i % this.perPageCount;
    if (j < this.perPageCount)
    {
      i = 0;
      while (i < j)
      {
        this.emotionImages.add(this.emotionImages.size() - 1, TRANSPARENTEMOTION);
        i += 1;
      }
    }
    this.pages = (this.emotionImages.size() / this.perPageCount);
  }

  public void setOnEmotionItemSelectedListener(OnEmotionItemSelectedListener paramOnEmotionItemSelectedListener)
  {
    this.listener = new WeakReference(paramOnEmotionItemSelectedListener);
  }

  private class FlipperAdapter extends FlipperPager.FlipperPagerAdapter
    implements AdapterView.OnItemClickListener
  {
    public FlipperAdapter()
    {
    }

    public int getCount()
    {
      return EmojiView.this.pages;
    }

    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
    {
      GridView localGridView = new GridView(EmojiView.this.getContext());
      localGridView.setGravity(17);
      localGridView.setPadding(EmojiView.this.gridViewPaddingLeft, EmojiView.this.gridViewPaddingTop, EmojiView.this.gridViewPaddingRight, EmojiView.this.gridViewPaddingBottom);
      localGridView.setSelector(R.drawable.item_emoji_bg);
      localGridView.setColumnWidth(EmojiView.this.emotionItemSize);
      localGridView.setVerticalSpacing(EmojiView.this.emotionItemSpacing);
      localGridView.setStretchMode(2);
      localGridView.setNumColumns(EmojiView.this.columnCount);
      localGridView.setAdapter(new EmojiView.ImageAdapter(EmojiView.this, EmojiView.this.getContext(), paramInt));
      localGridView.setOnItemClickListener(this);
      paramViewGroup.addView(localGridView, 0);
      return localGridView;
    }

    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return paramView == paramObject;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      paramAdapterView = (Emoji)paramAdapterView.getItemAtPosition(paramInt);
      if ((EmojiView.this.listener == null) || (EmojiView.this.listener.get() == null) || (paramAdapterView == EmojiView.TRANSPARENTEMOTION))
        return;
      if (paramAdapterView == EmojiView.DELETEEMOTION)
      {
        ((EmojiView.OnEmotionItemSelectedListener)EmojiView.this.listener.get()).onDeleteItemSelected();
        return;
      }
      ((EmojiView.OnEmotionItemSelectedListener)EmojiView.this.listener.get()).onEmotionItemSelected(paramAdapterView);
    }
  }

  private class ImageAdapter extends BaseAdapter
  {
    private final Context mContext;
    private AbsListView.LayoutParams mImageViewLayoutParams;
    private int offset;

    public ImageAdapter(Context paramInt, int arg3)
    {
      int i;
      this.offset = (EmojiView.this.perPageCount * i);
      this.mContext = paramInt;
      this.mImageViewLayoutParams = new AbsListView.LayoutParams(EmojiView.this.emotionItemSize, EmojiView.this.emotionItemSize);
    }

    public int getCount()
    {
      return EmojiView.this.perPageCount;
    }

    public Object getItem(int paramInt)
    {
      return EmojiView.this.emotionImages.get(this.offset + paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
      {
        paramView = new ImageView(this.mContext);
        paramView.setScaleType(ImageView.ScaleType.CENTER);
        paramView.setLayoutParams(this.mImageViewLayoutParams);
        paramView.setPadding(paramView.getPaddingLeft(), paramView.getPaddingRight(), 6, paramView.getPaddingBottom());
      }
      while (true)
      {
        paramView.setImageResource(((Emoji)getItem(paramInt)).drawableId);
        return paramView;
        paramView = (ImageView)paramView;
      }
    }

    public boolean isEnabled(int paramInt)
    {
      return EmojiView.this.emotionImages.get(this.offset + paramInt) != EmojiView.TRANSPARENTEMOTION;
    }
  }

  public static abstract interface OnEmotionItemSelectedListener
  {
    public abstract void onDeleteItemSelected();

    public abstract void onEmotionItemSelected(Emoji paramEmoji);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.emoji.EmojiView
 * JD-Core Version:    0.6.0
 */