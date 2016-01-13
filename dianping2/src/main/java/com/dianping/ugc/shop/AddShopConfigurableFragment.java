package com.dianping.ugc.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.Cell;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class AddShopConfigurableFragment extends AgentFragment
  implements OnCancelListener
{
  public static final String ADDSHOP_CELL_HANDLE = "addshop/handle";
  public static final String ADDSHOP_CELL_MANDATORYAGENT = "addshop/mandatoryagent";
  public static final String ADDSHOP_CELL_OPTIONALAGENT = "addshop/optionalagent";
  public static final String ADDSHOP_CELL_UPPHOTO = "addshop/upphotoview";
  private OnCancelListener mOnCancelListener;
  private String mShopName;
  private DPObject shop;

  public void OnCancel()
  {
    if (this.mOnCancelListener != null)
      this.mOnCancelListener.OnCancel();
  }

  protected void addCellToContainerView(String paramString, Cell paramCell)
  {
    if ("addshop/mandatoryagent".equals(paramString))
      ((ViewGroup)agentContainerView().findViewById(R.id.mandatory_layout)).addView(paramCell.view);
    do
    {
      return;
      if (!"addshop/optionalagent".equals(paramString))
        continue;
      ((ViewGroup)agentContainerView().findViewById(R.id.optional_layout)).addView(paramCell.view);
      return;
    }
    while (!"addshop/upphotoview".equals(paramString));
    ((ViewGroup)agentContainerView().findViewById(R.id.photoLayout)).addView(paramCell.view);
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new AddShopConfigurableFragment.DefaultAddShopAgentList());
    return localArrayList;
  }

  public DPObject getShop()
  {
    return this.shop;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    if (getArguments() != null)
    {
      this.shop = ((DPObject)getArguments().getParcelable("shop_object"));
      this.mShopName = getArguments().getString("shopname");
    }
    if (paramBundle != null)
    {
      if (this.shop == null)
        this.shop = ((DPObject)paramBundle.getParcelable("shop_object"));
      this.mShopName = paramBundle.getString("shopname");
    }
    ((AddShopActivity)getActivity()).setOnCancelListener(this);
    super.onActivityCreated(paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = (ViewGroup)paramLayoutInflater.inflate(R.layout.addshop, paramViewGroup, false);
    setAgentContainerView(paramLayoutInflater);
    return paramLayoutInflater;
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("shop_object", this.shop);
    paramBundle.putString("shopname", this.mShopName);
  }

  public void onViewStateRestored(Bundle paramBundle)
  {
    super.onViewStateRestored(paramBundle);
    if (paramBundle != null)
    {
      if (this.shop == null)
        this.shop = ((DPObject)paramBundle.getParcelable("shop_object"));
      this.mShopName = paramBundle.getString("shopname");
    }
  }

  protected void resetAgentContainerView()
  {
    ((ViewGroup)agentContainerView().findViewById(R.id.mandatory_layout)).removeAllViews();
    ((ViewGroup)agentContainerView().findViewById(R.id.optional_layout)).removeAllViews();
  }

  public void setOnCancelListener(OnCancelListener paramOnCancelListener)
  {
    this.mOnCancelListener = paramOnCancelListener;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.shop.AddShopConfigurableFragment
 * JD-Core Version:    0.6.0
 */