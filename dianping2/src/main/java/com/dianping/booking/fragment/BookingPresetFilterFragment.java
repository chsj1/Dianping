package com.dianping.booking.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.base.widget.NovaFragment;
import com.dianping.booking.view.BookingInfoPickerView;
import com.dianping.util.DateUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;
import java.util.Calendar;
import java.util.Date;

public class BookingPresetFilterFragment extends NovaFragment
{
  private Dialog bookingInfoDialog;
  private BookingInfoPickerView bookingInfoDialogView;
  private TextView filterContent;
  private LinearLayout filterView;
  private ImageView ivIcon;
  private BookingInfoListener listener;
  private Calendar preferCal;
  private int preferPerson;

  private void initBookingInfoDialog()
  {
    this.bookingInfoDialog = new Dialog(getActivity(), R.style.dialog);
    this.bookingInfoDialog.setCanceledOnTouchOutside(true);
    this.bookingInfoDialogView = ((BookingInfoPickerView)LayoutInflater.from(getActivity()).inflate(R.layout.booking_info_picker_dialog, null, false));
    this.bookingInfoDialogView.setDateView(28, 1);
    this.bookingInfoDialogView.setTimeView(30);
    this.bookingInfoDialogView.setNumView(50);
    this.bookingInfoDialogView.setOnButtonClickListener(new BookingPresetFilterFragment.2(this));
    this.bookingInfoDialog.setContentView(this.bookingInfoDialogView);
  }

  private void initData()
  {
    this.preferCal = Calendar.getInstance();
    this.preferPerson = 4;
    if ((this.preferCal.get(11) > 17) || ((this.preferCal.get(11) == 17) && (this.preferCal.get(12) > 0)))
      this.preferCal.add(5, 1);
    this.preferCal.set(11, 18);
    this.preferCal.set(12, 0);
  }

  private void updateBookingInfo()
  {
    String str = String.format("%s  %s  %s人", new Object[] { DateFormat.format("MM-dd  E", this.preferCal).toString(), DateUtils.formatTime(this.preferCal), Integer.valueOf(this.preferPerson) });
    this.filterContent.setText(str);
  }

  public LinearLayout getBookingInfoView()
  {
    return this.filterView;
  }

  public int getBookingPerson()
  {
    return this.preferPerson;
  }

  public long getBookingTime()
  {
    return this.preferCal.getTimeInMillis();
  }

  public ImageView getIcon()
  {
    return this.ivIcon;
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.booking_info_filter_view, paramViewGroup, false);
    initData();
    this.ivIcon = ((ImageView)paramLayoutInflater.findViewById(R.id.iv_booking_info_filter_icon));
    this.filterContent = ((TextView)paramLayoutInflater.findViewById(R.id.filter_content));
    this.filterView = ((LinearLayout)paramLayoutInflater.findViewById(R.id.filter_view));
    this.filterView.setOnClickListener(new BookingPresetFilterFragment.1(this));
    updateBookingInfo();
    initBookingInfoDialog();
    return paramLayoutInflater;
  }

  public void setBookingInfoListener(BookingInfoListener paramBookingInfoListener)
  {
    this.listener = paramBookingInfoListener;
  }

  public void updateBookingInfo(long paramLong, int paramInt)
  {
    this.preferCal.setTime(new Date(paramLong));
    this.preferPerson = paramInt;
    updateBookingInfo();
  }

  public void updateBookingInfo(String paramString)
  {
    this.filterContent.setText(paramString);
  }

  public static abstract interface BookingInfoListener
  {
    public abstract void addGA();

    public abstract void onBookingInfoChanged(int paramInt, long paramLong);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.fragment.BookingPresetFilterFragment
 * JD-Core Version:    0.6.0
 */