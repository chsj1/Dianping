package com.dianping.booking.fragment;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.dianping.base.web.ui.NovaZeusFragment;

public class BookingShopListZuesFragment extends NovaZeusFragment
{
  private BookingShopListZeusListener listener;
  private BroadcastReceiver receiver = new BookingShopListZuesFragment.1(this);

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.listener = ((BookingShopListZeusListener)(BookingShopListZeusListener)getActivity());
    paramBundle = new IntentFilter();
    paramBundle.addAction("booking:shoplist:loadmore");
    paramBundle.addAction("booking:shoplist:ready");
    getActivity().registerReceiver(this.receiver, paramBundle);
  }

  public void onDestroy()
  {
    getActivity().unregisterReceiver(this.receiver);
    this.receiver = null;
    this.listener = null;
    super.onDestroy();
  }

  public static abstract interface BookingShopListZeusListener
  {
    public abstract void loadMore(int paramInt);

    public abstract void ready();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.fragment.BookingShopListZuesFragment
 * JD-Core Version:    0.6.0
 */