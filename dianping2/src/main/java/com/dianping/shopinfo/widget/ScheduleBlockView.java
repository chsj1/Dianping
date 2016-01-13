package com.dianping.shopinfo.widget;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public class ScheduleBlockView extends NovaLinearLayout
{
  private final int MSG_DATE_CHANGED = 2;
  private final int MSG_TIME_CHANGED = 3;
  private LinearLayout blockNameContainer;
  private String blockNameStr;
  private DPObject[] dpScheduleDatas;
  public boolean[] expandState;
  private ScrollView fragmentScrollView;
  private int index;
  private LinearLayout layerShowSchedule;
  private Handler msgHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      super.handleMessage(paramMessage);
      if (paramMessage.what == 2)
      {
        paramMessage = paramMessage.getData();
        ScheduleBlockView.access$002(ScheduleBlockView.this, paramMessage.getInt("index"));
        int j = ScheduleBlockView.this.rgScheduleDates.getChildCount();
        if ((ScheduleBlockView.this.index < j) && (ScheduleBlockView.this.index >= 0))
        {
          int i = 0;
          while (i < j)
          {
            ScheduleBlockView.this.rgScheduleDates.getChildAt(i).setSelected(false);
            i += 1;
          }
          ScheduleBlockView.this.rgScheduleDates.getChildAt(ScheduleBlockView.this.index).setSelected(true);
          ScheduleBlockView.this.setTimeScrollView(ScheduleBlockView.this.dpScheduleDatas[ScheduleBlockView.this.index]);
          paramMessage = ScheduleBlockView.this.scheduleBlockInterface.getScheduleListData();
          ScheduleBlockView.this.setScheduleListView(paramMessage, ScheduleBlockView.this.scheduleBlockInterface.getTips());
        }
      }
      do
        return;
      while (paramMessage.what != 3);
      paramMessage = ScheduleBlockView.this.scheduleBlockInterface.getScheduleListData();
      ScheduleBlockView.this.setScheduleListView(paramMessage, ScheduleBlockView.this.scheduleBlockInterface.getTips());
    }
  };
  private RadioGroup rgScheduleDates;
  private ScheduleBlockInterface scheduleBlockInterface;
  private LinearLayout secondViewContainer;
  private TextView tvBlockName;
  private View viewShowAtScrollView;

  public ScheduleBlockView(Context paramContext)
  {
    super(paramContext);
  }

  public ScheduleBlockView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void setBlockName()
  {
    if (!TextUtils.isEmpty(this.blockNameStr))
    {
      this.tvBlockName.setText(this.blockNameStr);
      this.blockNameContainer.setVisibility(0);
      return;
    }
    this.blockNameContainer.setVisibility(8);
    this.tvBlockName.setText("");
  }

  private void setExpandState(int paramInt)
  {
    boolean[] arrayOfBoolean;
    if ((this.dpScheduleDatas != null) && (this.dpScheduleDatas.length > 0) && (this.expandState != null))
    {
      arrayOfBoolean = this.expandState;
      if (this.expandState[paramInt] != 0)
        break label43;
    }
    label43: for (int i = 1; ; i = 0)
    {
      arrayOfBoolean[paramInt] = i;
      return;
    }
  }

  private void setScheduleDatesScrollView()
  {
    this.rgScheduleDates.removeAllViews();
    int i = 0;
    while (i < this.dpScheduleDatas.length)
    {
      View localView = this.scheduleBlockInterface.getDateItemView(this.dpScheduleDatas[i], i, this.rgScheduleDates);
      if (localView != null)
        this.rgScheduleDates.addView(localView);
      i += 1;
    }
  }

  private void setScheduleListView(DPObject[] paramArrayOfDPObject, String paramString)
  {
    this.layerShowSchedule.removeAllViews();
    if ((paramArrayOfDPObject != null) && (paramArrayOfDPObject.length > 0))
    {
      paramString = new ScheduleListView(getContext())
      {
        View createListItem(DPObject paramDPObject)
        {
          return ScheduleBlockView.this.scheduleBlockInterface.getScheduleListItemView(paramDPObject, this);
        }

        public void setIsExpandState()
        {
          ScheduleBlockView.this.setExpandState(ScheduleBlockView.this.index);
        }
      };
      paramString.setExpandValue(this.expandState[this.index]);
      paramString.setScheduleDatas(paramArrayOfDPObject);
      if ((this.fragmentScrollView != null) && (this.viewShowAtScrollView != null))
        paramString.setViewShowAtScollView(this.fragmentScrollView, this.viewShowAtScrollView);
      this.layerShowSchedule.addView(paramString);
    }
    do
      return;
    while (TextUtils.isEmpty(paramString));
    paramArrayOfDPObject = (LinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.schedule_empty_view, this.layerShowSchedule, false);
    ((TextView)paramArrayOfDPObject.findViewById(R.id.tv_empty)).setText(paramString);
    this.layerShowSchedule.addView(paramArrayOfDPObject);
  }

  private void setTimeScrollView(DPObject paramDPObject)
  {
    this.secondViewContainer.removeAllViews();
    paramDPObject = this.scheduleBlockInterface.getSecondLevelView(paramDPObject);
    if (paramDPObject != null)
    {
      this.secondViewContainer.addView(paramDPObject);
      this.secondViewContainer.setVisibility(0);
      return;
    }
    this.secondViewContainer.setVisibility(8);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.tvBlockName = ((TextView)findViewById(R.id.block_name));
    this.blockNameContainer = ((LinearLayout)findViewById(R.id.block_name_container));
    this.rgScheduleDates = ((RadioGroup)findViewById(R.id.rg_schedule_show_date));
    this.layerShowSchedule = ((LinearLayout)findViewById(R.id.layer_show_schedule));
    this.secondViewContainer = ((LinearLayout)findViewById(R.id.second_view_container));
    WindowManager localWindowManager = (WindowManager)getContext().getSystemService("window");
  }

  public void sendDateChangeMsg(int paramInt)
  {
    Message localMessage = Message.obtain();
    Bundle localBundle = new Bundle();
    localBundle.putInt("index", paramInt);
    localMessage.setData(localBundle);
    localMessage.what = 2;
    this.msgHandler.sendMessage(localMessage);
  }

  public void sendTimeChangeMsg(int paramInt)
  {
    Message localMessage = Message.obtain();
    Bundle localBundle = new Bundle();
    localBundle.putInt("index", paramInt);
    localMessage.setData(localBundle);
    localMessage.what = 3;
    this.msgHandler.sendMessage(localMessage);
  }

  public void setAgentHeaderTitle(String paramString)
  {
    this.blockNameStr = paramString;
  }

  public void setScheduleBlockDate(DPObject[] paramArrayOfDPObject)
  {
    this.dpScheduleDatas = paramArrayOfDPObject;
    if ((this.dpScheduleDatas != null) && (this.dpScheduleDatas.length > 0))
    {
      this.expandState = new boolean[this.dpScheduleDatas.length];
      int i = 0;
      while (i < this.expandState.length)
      {
        this.expandState[i] = false;
        i += 1;
      }
      setScheduleDatesScrollView();
      setTimeScrollView(this.dpScheduleDatas[0]);
      setScheduleListView(this.scheduleBlockInterface.getScheduleListData(), this.scheduleBlockInterface.getTips());
      setBlockName();
    }
  }

  public void setScheduleBlockInterface(ScheduleBlockInterface paramScheduleBlockInterface)
  {
    this.scheduleBlockInterface = paramScheduleBlockInterface;
  }

  public void setViewShowAtScollView(ScrollView paramScrollView, View paramView)
  {
    this.fragmentScrollView = paramScrollView;
    this.viewShowAtScrollView = paramView;
  }

  public static abstract interface ScheduleBlockInterface
  {
    public abstract View getDateItemView(DPObject paramDPObject, int paramInt, RadioGroup paramRadioGroup);

    public abstract DPObject[] getScheduleListData();

    public abstract View getScheduleListItemView(DPObject paramDPObject, ScheduleListView paramScheduleListView);

    public abstract View getSecondLevelView(DPObject paramDPObject);

    public abstract String getTips();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.ScheduleBlockView
 * JD-Core Version:    0.6.0
 */