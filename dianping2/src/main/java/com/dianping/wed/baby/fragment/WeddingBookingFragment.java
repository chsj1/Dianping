package com.dianping.wed.baby.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.wed.configure.DefaultBookingConfigure;
import java.util.ArrayList;

public class WeddingBookingFragment extends WeddingProductBaseFragment
{
  public String bookingText;
  public String shopName;

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new DefaultBookingConfigure());
    return localArrayList;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (getArguments() != null)
    {
      if (getArguments().containsKey("productid"))
        this.productid = getArguments().getInt("productid");
      if (getArguments().containsKey("shopid"))
        this.shopid = getArguments().getInt("shopid");
      if (getArguments().containsKey("shopname"))
        this.shopName = getArguments().getString("shopname");
      if (getArguments().containsKey("bookingBtnText"))
        this.bookingText = getArguments().getString("bookingBtnText");
    }
    this.needShopRequest = false;
    this.needProductReuqest = false;
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.wedding_booking_fragment, paramViewGroup, false);
    setAgentContainerView((ViewGroup)paramLayoutInflater.findViewById(R.id.content));
    this.bottomView = ((ViewGroup)paramLayoutInflater.findViewById(R.id.bottom_view));
    this.bottomCellContainer = new WeddingProductBaseFragment.CellContainer(this, getActivity());
    this.bottomCellContainer.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
    this.bottomView.setVisibility(8);
    this.bottomView.addView(this.bottomCellContainer);
    return paramLayoutInflater;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.fragment.WeddingBookingFragment
 * JD-Core Version:    0.6.0
 */