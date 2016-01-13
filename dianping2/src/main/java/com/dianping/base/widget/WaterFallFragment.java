package com.dianping.base.widget;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.model.SimpleMsg;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.util.ArrayList;

public class WaterFallFragment<T, V> extends NovaFragment
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private CheckinItemFramelayout error;
  private View error_item;
  protected boolean isEnd = false;
  int[] layoutHeights = new int[2];
  private T listItem;
  public ArrayList<V> listLeft = new ArrayList();
  protected MApiRequest listRequest;
  public ArrayList<V> listRight = new ArrayList();
  private View loading;
  protected int nextstart = 0;
  private int padding;
  private ArrayList<T> recycle = new ArrayList();
  private int res;
  public ArrayList<V> result = new ArrayList();
  int scrollNow;
  protected LinearLayout waterFallContainer;
  ArrayList<LinearLayout> waterFallItems;
  public LazyScrollView waterFallScroll;
  int windowsHeight;

  private int addToWhitchLayout(V paramV)
  {
    if (this.layoutHeights[0] <= this.layoutHeights[1])
    {
      this.listLeft.add(paramV);
      return 0;
    }
    this.listRight.add(paramV);
    return 1;
  }

  protected void addGetPageGa(int paramInt)
  {
  }

  protected void addItemClickGa()
  {
  }

  protected void addView()
  {
    if (getActivity() == null)
      return;
    Object localObject1 = (FrameLayout)(FrameLayout)((LinearLayout)this.waterFallItems.get(1)).getChildAt(0);
    if (localObject1 != null)
      ((FrameLayout)localObject1).removeAllViews();
    int i = 0;
    label40: if (i < this.result.size())
    {
      if (this.recycle.isEmpty() != true)
        break label411;
      if (getActivity() != null)
        this.listItem = getActivity().getLayoutInflater().inflate(this.res, null, false);
    }
    while (true)
    {
      localObject1 = new CheckinItemFramelayout(getActivity());
      if (this.listItem == null)
        Log.v("1", "listItemisNull");
      setItemValue(this.listItem, i, 2);
      addView((CheckinItemFramelayout)localObject1, this.listItem);
      ((View)this.listItem).measure(0, 0);
      int j = addToWhitchLayout(this.result.get(i));
      Log.v("1", "select" + j);
      ((LinearLayout)this.waterFallItems.get(j)).addView((View)localObject1);
      Log.v("1", "child" + ((LinearLayout)this.waterFallItems.get(j)).getChildCount());
      Log.v("1", "top" + this.layoutHeights[j]);
      Log.v("1", "bottom" + this.layoutHeights[j] + ((View)this.listItem).getMeasuredHeight());
      ((CheckinItemFramelayout)localObject1).setShowTop(this.layoutHeights[j]);
      int k = this.layoutHeights[j];
      ((CheckinItemFramelayout)localObject1).setShowBottom(((View)this.listItem).getMeasuredHeight() + k);
      Object localObject2 = this.layoutHeights;
      k = this.layoutHeights[j];
      localObject2[j] = (((View)this.listItem).getMeasuredHeight() + k);
      localObject2 = ((CheckinItemFramelayout)localObject1).getLayoutParams();
      ((ViewGroup.LayoutParams)localObject2).height = ((View)this.listItem).getMeasuredHeight();
      ((CheckinItemFramelayout)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
      i += 1;
      break label40;
      break;
      label411: this.listItem = this.recycle.get(0);
      this.recycle.remove(0);
    }
  }

  protected void addView(CheckinItemFramelayout paramCheckinItemFramelayout, T paramT)
  {
  }

  protected void getList(int paramInt, LinearLayout paramLinearLayout)
  {
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (paramBundle != null)
    {
      this.res = paramBundle.getInt("res", 0);
      this.padding = paramBundle.getInt("padding", 0);
    }
    this.windowsHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
    this.waterFallScroll.getView();
    this.waterFallScroll.setOnScrollListener(new LazyScrollView.OnScrollListener()
    {
      public void onAutoScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
      {
        if (WaterFallFragment.this.waterFallScroll.getScrollY() == 0)
          WaterFallFragment.this.waterFallScroll.scrollTo(0, 1);
        WaterFallFragment.this.scrollNow = paramInt2;
        WaterFallFragment.this.reset();
      }

      public void onBottom()
      {
        if (((LinearLayout)WaterFallFragment.this.waterFallItems.get(0)).getMeasuredHeight() < ((LinearLayout)WaterFallFragment.this.waterFallItems.get(1)).getMeasuredHeight())
        {
          WaterFallFragment.this.getList(WaterFallFragment.this.nextstart, (LinearLayout)WaterFallFragment.this.waterFallItems.get(0));
          return;
        }
        WaterFallFragment.this.getList(WaterFallFragment.this.nextstart, (LinearLayout)WaterFallFragment.this.waterFallItems.get(1));
      }

      public void onScroll()
      {
        if (WaterFallFragment.this.waterFallScroll.getScrollY() == 0)
          WaterFallFragment.this.waterFallScroll.scrollTo(0, 1);
      }

      public void onTop()
      {
        Log.d("LazyScroll", "Scroll to top");
        WaterFallFragment.this.waterFallScroll.scrollTo(0, 1);
      }
    });
    this.waterFallItems = new ArrayList();
    if (getActivity() == null)
      return;
    this.error_item = getActivity().getLayoutInflater().inflate(R.layout.error_item, null, false);
    this.error = new CheckinItemFramelayout(getActivity());
    this.error.setLoading(true);
    this.error.addView(this.error_item);
    int i = 0;
    if (i < 2)
    {
      paramBundle = new LinearLayout(getActivity());
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(getActivity().getWindowManager().getDefaultDisplay().getWidth() / 2, -2);
      if (i == 1)
        paramBundle.setPadding(2, this.padding + 2, 2, 2);
      while (true)
      {
        paramBundle.setOrientation(1);
        paramBundle.setLayoutParams(localLayoutParams);
        this.waterFallItems.add(paramBundle);
        this.waterFallContainer.addView(paramBundle);
        i += 1;
        break;
        paramBundle.setPadding(2, 2, 2, 2);
      }
    }
    getList(this.nextstart, (LinearLayout)this.waterFallItems.get(0));
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.shop_checkinlist, paramViewGroup, false);
    this.waterFallScroll = ((LazyScrollView)paramLayoutInflater.findViewById(R.id.waterfall_scroll));
    this.waterFallContainer = ((LinearLayout)paramLayoutInflater.findViewById(R.id.waterfall_container));
    return paramLayoutInflater;
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.listRequest)
    {
      this.listRequest = null;
      removeLoadingView(this.loading);
      removeErrorView();
      ((TextView)(TextView)this.error.getChildAt(0).findViewById(16908308)).setText(paramMApiResponse.message().toString());
      if ((this.error_item instanceof LoadingErrorView))
        ((LoadingErrorView)this.error_item).setCallBack(new LoadingErrorView.LoadRetry()
        {
          public void loadRetry(View paramView)
          {
            if (WaterFallFragment.this.layoutHeights[0] <= WaterFallFragment.this.layoutHeights[1])
            {
              WaterFallFragment.this.getList(WaterFallFragment.this.nextstart, (LinearLayout)WaterFallFragment.this.waterFallItems.get(0));
              return;
            }
            WaterFallFragment.this.getList(WaterFallFragment.this.nextstart, (LinearLayout)WaterFallFragment.this.waterFallItems.get(1));
          }
        });
      if (this.layoutHeights[0] <= this.layoutHeights[1])
        ((LinearLayout)this.waterFallItems.get(0)).addView(this.error);
    }
    else
    {
      return;
    }
    ((LinearLayout)this.waterFallItems.get(1)).addView(this.error);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("res", this.res);
    paramBundle.putInt("padding", this.padding);
  }

  protected void removeErrorView()
  {
    ((LinearLayout)this.waterFallItems.get(0)).removeView(this.error);
    ((LinearLayout)this.waterFallItems.get(1)).removeView(this.error);
  }

  protected void removeLoadingView(View paramView)
  {
    this.loading = paramView;
    ((LinearLayout)this.waterFallItems.get(0)).removeView(paramView);
    ((LinearLayout)this.waterFallItems.get(1)).removeView(paramView);
  }

  protected void removeValue(T paramT)
  {
  }

  public void reset()
  {
    if (getActivity() == null)
      return;
    int i = 0;
    CheckinItemFramelayout localCheckinItemFramelayout;
    View localView;
    while (i < ((LinearLayout)this.waterFallItems.get(0)).getChildCount())
    {
      localCheckinItemFramelayout = (CheckinItemFramelayout)(CheckinItemFramelayout)((LinearLayout)this.waterFallItems.get(0)).getChildAt(i);
      if ((this.listLeft.size() > i) && (!localCheckinItemFramelayout.isLoading) && ((localCheckinItemFramelayout.getShowBottom() < this.scrollNow - this.windowsHeight) || (localCheckinItemFramelayout.getShowTop() > this.scrollNow + this.windowsHeight * 2)))
      {
        localView = localCheckinItemFramelayout.getChildAt(0);
        if (localView != null)
        {
          removeValue(localView);
          this.recycle.add(localView);
        }
        localCheckinItemFramelayout.removeAllViews();
      }
      i += 1;
    }
    i = 0;
    while (i < ((LinearLayout)this.waterFallItems.get(1)).getChildCount())
    {
      localCheckinItemFramelayout = (CheckinItemFramelayout)(CheckinItemFramelayout)((LinearLayout)this.waterFallItems.get(1)).getChildAt(i);
      if ((this.listRight.size() > i) && (!localCheckinItemFramelayout.isLoading) && ((localCheckinItemFramelayout.getShowBottom() < this.scrollNow - this.windowsHeight) || (localCheckinItemFramelayout.getShowTop() > this.scrollNow + this.windowsHeight * 2)))
      {
        localView = localCheckinItemFramelayout.getChildAt(0);
        if (localView != null)
        {
          removeValue(localView);
          this.recycle.add(localView);
        }
        localCheckinItemFramelayout.removeAllViews();
      }
      i += 1;
    }
    i = 0;
    if (i < ((LinearLayout)this.waterFallItems.get(0)).getChildCount())
    {
      localCheckinItemFramelayout = (CheckinItemFramelayout)(CheckinItemFramelayout)((LinearLayout)this.waterFallItems.get(0)).getChildAt(i);
      if ((this.listLeft.size() > i) && (!localCheckinItemFramelayout.isLoading) && (localCheckinItemFramelayout.getShowBottom() > this.scrollNow - this.windowsHeight) && (localCheckinItemFramelayout.getShowTop() < this.scrollNow + this.windowsHeight * 2) && (localCheckinItemFramelayout.getChildCount() == 0))
      {
        if (this.recycle.isEmpty() != true)
          break label435;
        this.listItem = getActivity().getLayoutInflater().inflate(this.res, null, false);
      }
      while (true)
      {
        if (i < this.listLeft.size())
        {
          setItemValue(this.listItem, i, 0);
          addView(localCheckinItemFramelayout, this.listItem);
        }
        i += 1;
        break;
        label435: this.listItem = this.recycle.get(0);
        this.recycle.remove(0);
      }
    }
    i = 0;
    label461: if (i < ((LinearLayout)this.waterFallItems.get(1)).getChildCount())
    {
      localCheckinItemFramelayout = (CheckinItemFramelayout)(CheckinItemFramelayout)((LinearLayout)this.waterFallItems.get(1)).getChildAt(i);
      if ((this.listRight.size() > i) && (!localCheckinItemFramelayout.isLoading) && (localCheckinItemFramelayout.getShowBottom() > this.scrollNow - this.windowsHeight) && (localCheckinItemFramelayout.getShowTop() < this.scrollNow + this.windowsHeight * 2) && (localCheckinItemFramelayout.getChildCount() == 0))
      {
        if (this.recycle.isEmpty() != true)
          break label635;
        if (getActivity() != null)
          this.listItem = getActivity().getLayoutInflater().inflate(this.res, null, false);
      }
    }
    while (true)
    {
      if (i < this.listRight.size())
      {
        setItemValue(this.listItem, i, 1);
        addView(localCheckinItemFramelayout, this.listItem);
      }
      i += 1;
      break label461;
      break;
      label635: this.listItem = this.recycle.get(0);
      this.recycle.remove(0);
    }
  }

  protected void setItemValue(T paramT, int paramInt1, int paramInt2)
  {
  }

  public void setPadding(int paramInt)
  {
    this.padding = paramInt;
  }

  public void setResource(int paramInt)
  {
    this.res = paramInt;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.WaterFallFragment
 * JD-Core Version:    0.6.0
 */