package com.dianping.takeaway.agents;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.loader.MyResources;
import com.dianping.takeaway.entity.TakeawayOrderDetail;
import com.dianping.takeaway.fragment.TakeawayOrderDetailFragment;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.SimpleDateFormat;

public class TakeawayOrderStatusAgent extends CellAgent
{
  private static final int MSG_TA_CONFIRM_COUNT_DOWN = 1;
  private static final int MSG_TA_FIXED_REFRESH = 2;
  private TextView confirmedCountDown;
  private TextView confirmedDesp;
  private ImageView confirmedIcon;
  private TextView evaluatedDesp;
  private ImageView evaluatedIcon;
  private View firProcessLine;
  private View orderStatusContainer;
  private TextView orderedDesp;
  private ImageView orderedIcon;
  private View processDivider;
  private View processLayout;
  private TextView processTipView;
  private TextView receivedDesp;
  private ImageView receivedIcon;
  private Handler refreshHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
        paramMessage = TakeawayOrderStatusAgent.this.takeawayOrderDetail;
        paramMessage.leftSeconds -= 1;
        if (TakeawayOrderStatusAgent.this.takeawayOrderDetail.leftSeconds > 0)
        {
          TakeawayOrderStatusAgent.this.setCountDownSeconds(TakeawayOrderStatusAgent.this.takeawayOrderDetail.leftSeconds);
          TakeawayOrderStatusAgent.this.refreshHandler.removeMessages(1);
          TakeawayOrderStatusAgent.this.refreshHandler.sendEmptyMessageDelayed(1, 1000L);
          return;
        }
        TakeawayOrderStatusAgent.this.setCountDownSeconds(0);
        TakeawayOrderStatusAgent.this.refreshHandler.removeMessages(2);
        TakeawayOrderStatusAgent.this.refreshHandler.sendEmptyMessageDelayed(2, 1000L);
        return;
      case 2:
      }
      TakeawayOrderStatusAgent.this.clearHandlerMsg();
      ((TakeawayOrderDetailFragment)TakeawayOrderStatusAgent.this.getFragment()).getTakeawayOrderDetailTask(TakeawayOrderStatusAgent.this.takeawayOrderDetail.orderViewId);
    }
  };
  private View secProcessLine;
  private TakeawayOrderDetail takeawayOrderDetail;
  private View thiProcessLine;
  private View tipDivider;
  private ImageView tipIcon;
  private View tipLayout;
  private TextView tipView;

  public TakeawayOrderStatusAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void setCountDownSeconds(int paramInt)
  {
    String str = new SimpleDateFormat("mm:ss").format(Integer.valueOf(paramInt * 1000));
    this.confirmedCountDown.setText(str);
  }

  private void setupView(TakeawayOrderDetail paramTakeawayOrderDetail)
  {
    int i8 = paramTakeawayOrderDetail.deliverStatus;
    Object localObject = paramTakeawayOrderDetail.tips;
    int i = 0;
    int i2 = 0;
    int i4 = 0;
    int k = 0;
    int n = 0;
    int i6 = 0;
    int i7 = 0;
    int j = 0;
    int i3 = 0;
    int i5 = 0;
    int m = 0;
    int i1;
    switch (i8 / 10)
    {
    default:
      i1 = 0;
      if (i1 == 0)
        break label607;
      this.confirmedIcon.setImageResource(i);
      this.orderedIcon.setImageResource(i2);
      this.receivedIcon.setImageResource(i4);
      this.evaluatedIcon.setImageResource(k);
      this.firProcessLine.setBackgroundColor(getResources().getColor(n));
      this.secProcessLine.setBackgroundColor(getResources().getColor(i6));
      this.thiProcessLine.setBackgroundColor(getResources().getColor(i7));
      this.confirmedDesp.setTextColor(getResources().getColor(j));
      this.orderedDesp.setTextColor(getResources().getColor(i3));
      this.receivedDesp.setTextColor(getResources().getColor(i5));
      this.evaluatedDesp.setTextColor(getResources().getColor(m));
      this.processTipView.setText((CharSequence)localObject);
      this.processLayout.setVisibility(0);
      this.processDivider.setVisibility(0);
      this.tipLayout.setVisibility(8);
      this.tipDivider.setVisibility(8);
      if ((paramTakeawayOrderDetail.leftSeconds <= 0) || (i8 / 10 != 0))
        break;
      setCountDownSeconds(paramTakeawayOrderDetail.leftSeconds);
      this.refreshHandler.removeMessages(1);
      this.refreshHandler.sendEmptyMessageDelayed(1, 1000L);
      this.confirmedCountDown.setVisibility(0);
    case 0:
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      if (paramTakeawayOrderDetail.interval > 0)
      {
        this.refreshHandler.removeMessages(2);
        this.refreshHandler.sendEmptyMessageDelayed(2, paramTakeawayOrderDetail.interval * 1000);
      }
      return;
      i1 = 1;
      i = R.drawable.wm_process_tobeconfrimed;
      i2 = R.drawable.wm_process_notorder;
      i4 = R.drawable.wm_process_notreceive;
      k = R.drawable.wm_process_notevaluate;
      i7 = R.color.text_hint_light_gray;
      i6 = i7;
      n = i7;
      j = R.color.wm_common_green;
      m = R.color.text_hint_light_gray;
      i5 = m;
      i3 = m;
      break;
      i1 = 1;
      i = R.drawable.wm_process_confrimed;
      i2 = R.drawable.wm_process_tobeordered;
      i4 = R.drawable.wm_process_notreceive;
      k = R.drawable.wm_process_notevaluate;
      n = R.color.wm_common_green;
      i7 = R.color.text_hint_light_gray;
      i6 = i7;
      j = R.color.text_gray;
      i3 = R.color.wm_common_green;
      m = R.color.text_hint_light_gray;
      i5 = m;
      break;
      i1 = 1;
      i = R.drawable.wm_process_confrimed;
      i2 = R.drawable.wm_process_ordered;
      i4 = R.drawable.wm_process_tobereceived;
      k = R.drawable.wm_process_notevaluate;
      i6 = R.color.wm_common_green;
      n = i6;
      i7 = R.color.text_hint_light_gray;
      i3 = R.color.text_gray;
      j = i3;
      i5 = R.color.wm_common_green;
      m = R.color.text_hint_light_gray;
      break;
      i1 = 1;
      i = R.drawable.wm_process_confrimed;
      i2 = R.drawable.wm_process_ordered;
      i4 = R.drawable.wm_process_receive;
      k = R.drawable.wm_process_evaluated;
      i7 = R.color.wm_common_green;
      i6 = i7;
      n = i7;
      i5 = R.color.text_gray;
      i3 = i5;
      j = i5;
      m = R.color.wm_common_green;
      break;
      setCountDownSeconds(0);
      this.confirmedCountDown.setVisibility(8);
    }
    label607: if (i8 / 10 == 5)
    {
      i = 1;
      label618: this.tipView.setText((CharSequence)localObject);
      localObject = this.tipIcon;
      if (i == 0)
        break label713;
      j = R.drawable.wm_order_icon_determined;
      ((ImageView)localObject).setImageResource(j);
      localObject = this.tipLayout;
      if (i == 0)
        break label721;
    }
    label640: label713: label721: for (i = getResources().getColor(R.color.wm_orange_bg); ; i = getResources().getColor(R.color.separator_color))
    {
      ((View)localObject).setBackgroundColor(i);
      this.tipLayout.setVisibility(0);
      this.tipDivider.setVisibility(0);
      this.processLayout.setVisibility(8);
      this.processDivider.setVisibility(8);
      break;
      i = 0;
      break label618;
      j = R.drawable.wm_order_icon_cancel;
      break label640;
    }
  }

  public void clearHandlerMsg()
  {
    this.refreshHandler.removeMessages(1);
    this.refreshHandler.removeMessages(2);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    addCell("01.order_status.0", this.orderStatusContainer);
    if (paramBundle == null)
      return;
    switch (paramBundle.getInt("type"))
    {
    default:
      return;
    case 0:
    }
    this.takeawayOrderDetail = new TakeawayOrderDetail((DPObject)paramBundle.getParcelable("order"));
    setupView(this.takeawayOrderDetail);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.orderStatusContainer = this.res.inflate(getContext(), R.layout.takeaway_order_status_layout, null, false);
    this.processLayout = this.orderStatusContainer.findViewById(R.id.process_layout);
    this.processDivider = this.orderStatusContainer.findViewById(R.id.process_divider);
    this.processTipView = ((TextView)this.orderStatusContainer.findViewById(R.id.process_tip));
    this.confirmedIcon = ((ImageView)this.orderStatusContainer.findViewById(R.id.confirmed_icon));
    this.confirmedDesp = ((TextView)this.orderStatusContainer.findViewById(R.id.confirmed_desp));
    this.confirmedCountDown = ((TextView)this.orderStatusContainer.findViewById(R.id.confirmed_count_down));
    this.orderedIcon = ((ImageView)this.orderStatusContainer.findViewById(R.id.ordered_icon));
    this.orderedDesp = ((TextView)this.orderStatusContainer.findViewById(R.id.ordered_desp));
    this.receivedIcon = ((ImageView)this.orderStatusContainer.findViewById(R.id.received_icon));
    this.receivedDesp = ((TextView)this.orderStatusContainer.findViewById(R.id.received_desp));
    this.evaluatedIcon = ((ImageView)this.orderStatusContainer.findViewById(R.id.evaluated_icon));
    this.evaluatedDesp = ((TextView)this.orderStatusContainer.findViewById(R.id.evaluated_desp));
    this.firProcessLine = this.orderStatusContainer.findViewById(R.id.fir_process_line);
    this.secProcessLine = this.orderStatusContainer.findViewById(R.id.sec_process_line);
    this.thiProcessLine = this.orderStatusContainer.findViewById(R.id.thi_process_line);
    this.tipLayout = this.orderStatusContainer.findViewById(R.id.tip_layout);
    this.tipDivider = this.orderStatusContainer.findViewById(R.id.tip_divider);
    this.tipIcon = ((ImageView)this.orderStatusContainer.findViewById(R.id.tip_icon));
    this.tipView = ((TextView)this.orderStatusContainer.findViewById(R.id.tip_content));
  }

  public void onDestroy()
  {
    clearHandlerMsg();
    super.onDestroy();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.agents.TakeawayOrderStatusAgent
 * JD-Core Version:    0.6.0
 */