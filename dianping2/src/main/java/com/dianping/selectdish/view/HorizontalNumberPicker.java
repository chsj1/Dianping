package com.dianping.selectdish.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class HorizontalNumberPicker extends LinearLayout
{
  private static final int DEFAULT_BUFFER = 5;
  private static final int DEFAULT_NUM = 15;
  private int defaultNum = 1;
  private GalleryAdapter gaAdapter;
  private boolean manualScroll = true;
  private int maxNum = 15;
  private NumberPickerListener numberPickerListener;
  private RecyclerView rv;

  public HorizontalNumberPicker(Context paramContext)
  {
    super(paramContext);
    initView(paramContext);
  }

  public HorizontalNumberPicker(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView(paramContext);
  }

  public HorizontalNumberPicker(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initView(paramContext);
  }

  private void initView(Context paramContext)
  {
    LayoutInflater.from(paramContext).inflate(R.layout.horizontal_number_picker, this);
    this.rv = ((RecyclerView)findViewById(R.id.recyclerview));
    LinearLayoutManager localLinearLayoutManager = new LinearLayoutManager(paramContext);
    localLinearLayoutManager.setOrientation(0);
    this.rv.setLayoutManager(localLinearLayoutManager);
    this.gaAdapter = new GalleryAdapter(paramContext, this.maxNum);
    this.rv.setAdapter(this.gaAdapter);
    this.rv.setOnScrollListener(new RecyclerView.OnScrollListener()
    {
      public void onScrollStateChanged(RecyclerView paramRecyclerView, int paramInt)
      {
        super.onScrollStateChanged(paramRecyclerView, paramInt);
        int k;
        View localView1;
        View localView2;
        int m;
        int n;
        if (paramInt == 0)
        {
          k = paramRecyclerView.getWidth();
          i = paramRecyclerView.getChildCount();
          localView1 = paramRecyclerView.getChildAt(0);
          paramInt = 0;
          if (paramInt < i)
          {
            localView1 = paramRecyclerView.getChildAt(paramInt);
            if ((localView1.getLeft() >= k / 2) || (k / 2 > localView1.getRight()))
              break label158;
          }
          localView2 = paramRecyclerView.getChildAt(0);
          m = localView2.getWidth();
          paramInt = paramRecyclerView.getAdapter().getItemCount();
          n = paramRecyclerView.getChildAdapterPosition(localView1);
          if (n >= 5)
            break label171;
          paramRecyclerView.smoothScrollBy((4 - n) * m + localView1.getRight() - (k - m) / 2, 0);
          HorizontalNumberPicker.this.notifyNumberPickerChanged(1);
          paramRecyclerView = HorizontalNumberPicker.this;
          if (HorizontalNumberPicker.this.manualScroll)
            break label165;
        }
        label158: label165: for (boolean bool = true; ; bool = false)
        {
          HorizontalNumberPicker.access$202(paramRecyclerView, bool);
          return;
          paramInt += 1;
          break;
        }
        label171: if (n >= paramInt - 5)
        {
          paramRecyclerView.smoothScrollBy(-((n - paramInt + 5 + 1) * m + k / 2 - (localView1.getRight() + localView1.getLeft()) / 2), 0);
          HorizontalNumberPicker.this.notifyNumberPickerChanged(HorizontalNumberPicker.this.maxNum);
          paramRecyclerView = HorizontalNumberPicker.this;
          if (!HorizontalNumberPicker.this.manualScroll);
          for (bool = true; ; bool = false)
          {
            HorizontalNumberPicker.access$202(paramRecyclerView, bool);
            return;
          }
        }
        int i = 2147483647;
        paramInt = localView2.getLeft() + m / 2;
        while (paramInt < k)
        {
          int j = i;
          if (Math.abs(i) > Math.abs(paramInt - k / 2))
            j = paramInt - k / 2;
          paramInt += m;
          i = j;
        }
        paramRecyclerView.smoothScrollBy(i, 0);
        if (HorizontalNumberPicker.this.manualScroll)
          HorizontalNumberPicker.this.notifyNumberPickerChanged(n - 5 + 1);
        paramRecyclerView = HorizontalNumberPicker.this;
        if (!HorizontalNumberPicker.this.manualScroll);
        for (bool = true; ; bool = false)
        {
          HorizontalNumberPicker.access$202(paramRecyclerView, bool);
          return;
        }
      }

      public void onScrolled(RecyclerView paramRecyclerView, int paramInt1, int paramInt2)
      {
        super.onScrolled(paramRecyclerView, paramInt1, paramInt2);
        paramInt2 = paramRecyclerView.getWidth();
        int i = paramRecyclerView.getChildCount();
        paramInt1 = 0;
        while (paramInt1 < i)
        {
          Object localObject = paramRecyclerView.getChildAt(paramInt1);
          int j = Math.abs(paramInt2 / 2 - (((View)localObject).getRight() + ((View)localObject).getLeft()) / 2);
          localObject = (TextView)((View)localObject).findViewById(R.id.textview);
          ((TextView)localObject).setScaleX(1.0F - j * 0.5F / paramInt2);
          ((TextView)localObject).setScaleY(1.0F - j * 0.5F / paramInt2);
          paramInt1 += 1;
        }
      }
    });
  }

  private void notifyNumberPickerChanged(int paramInt)
  {
    if (this.numberPickerListener != null)
      this.numberPickerListener.onPick(paramInt);
  }

  public void removeNumberPickerListener()
  {
    this.numberPickerListener = null;
  }

  public void scrollToNum(int paramInt)
  {
    postDelayed(new Runnable(paramInt)
    {
      public void run()
      {
        int m;
        View localView;
        int j;
        int k;
        int i;
        if (HorizontalNumberPicker.this.rv != null)
        {
          m = HorizontalNumberPicker.this.rv.getChildCount();
          localView = HorizontalNumberPicker.this.rv.getChildAt(0);
          j = localView.getWidth();
          k = HorizontalNumberPicker.this.rv.getWidth();
          i = 0;
        }
        while (true)
        {
          if (i < m)
          {
            localView = HorizontalNumberPicker.this.rv.getChildAt(i);
            if ((localView.getLeft() >= k / 2) || (k / 2 > localView.getRight()));
          }
          else
          {
            i = HorizontalNumberPicker.this.rv.getChildAdapterPosition(localView);
            m = this.val$num;
            k /= 2;
            int n = (localView.getLeft() + localView.getRight()) / 2;
            HorizontalNumberPicker.this.rv.scrollBy((m + 5 - i - 1) * j - k + n, 0);
            return;
          }
          i += 1;
        }
      }
    }
    , 500L);
  }

  public void setMaxNum(int paramInt1, int paramInt2)
  {
    if (paramInt1 > 0)
      this.maxNum = paramInt1;
    if (paramInt2 < 1)
      this.defaultNum = 1;
    while (true)
    {
      this.gaAdapter.reset(paramInt1);
      scrollToNum(this.defaultNum);
      return;
      if (paramInt2 > paramInt1)
      {
        this.defaultNum = paramInt1;
        continue;
      }
      this.defaultNum = paramInt2;
    }
  }

  public void setNumberPickerListener(NumberPickerListener paramNumberPickerListener)
  {
    this.numberPickerListener = paramNumberPickerListener;
  }

  public class GalleryAdapter extends RecyclerView.Adapter<HorizontalNumberPicker.ViewHolder>
  {
    private ArrayList<String> data = new ArrayList();
    private LayoutInflater mInflater;
    private int maxNum;

    public GalleryAdapter(Context paramInt, int arg3)
    {
      this.mInflater = LayoutInflater.from(paramInt);
      int i;
      this.maxNum = i;
      initData();
    }

    private void initData()
    {
      int i = 0;
      while (i < 5)
      {
        this.data.add("");
        i += 1;
      }
      i = 0;
      while (i < this.maxNum)
      {
        this.data.add(String.valueOf(i + 1));
        i += 1;
      }
      i = 0;
      while (i < 5)
      {
        this.data.add("");
        i += 1;
      }
    }

    public int getItemCount()
    {
      return this.data.size();
    }

    public void onBindViewHolder(HorizontalNumberPicker.ViewHolder paramViewHolder, int paramInt)
    {
      paramViewHolder.txt.setText((CharSequence)this.data.get(paramInt));
    }

    public HorizontalNumberPicker.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      paramViewGroup = this.mInflater.inflate(R.layout.selectdish_layout_textview, paramViewGroup, false);
      HorizontalNumberPicker.ViewHolder localViewHolder = new HorizontalNumberPicker.ViewHolder(paramViewGroup);
      localViewHolder.txt = ((TextView)paramViewGroup.findViewById(R.id.textview));
      return localViewHolder;
    }

    public void reset(int paramInt)
    {
      this.maxNum = paramInt;
      this.data.clear();
      initData();
      notifyDataSetChanged();
    }
  }

  public static abstract interface NumberPickerListener
  {
    public abstract void onPick(int paramInt);
  }

  public static class ViewHolder extends RecyclerView.ViewHolder
  {
    TextView txt;

    public ViewHolder(View paramView)
    {
      super();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.view.HorizontalNumberPicker
 * JD-Core Version:    0.6.0
 */