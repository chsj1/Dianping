package com.dianping.wed.baby.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.v1.R.layout;
import com.dianping.wed.baby.activity.WeddingPackageListActivity;
import java.util.ArrayList;

public class WeddingPackageListFragment extends WeddingProductBaseFragment
{
  int productCategoryId;
  DPObject[] topNavObjects;

  public void dispatchAgentChanged(String paramString, Bundle paramBundle)
  {
    super.dispatchAgentChanged(paramString, paramBundle);
    if ((paramBundle != null) && (paramBundle.containsKey("packageobject")))
    {
      this.topNavObjects = ((DPObject)paramBundle.getParcelable("packageobject")).getArray("TopNav");
      if ((this.topNavObjects != null) && (this.topNavObjects.length > 0) && (getActivity() != null) && ((getActivity() instanceof WeddingPackageListActivity)))
        ((WeddingPackageListActivity)getActivity()).resetTitleBar(this.topNavObjects);
    }
  }

  void dispatchProductId()
  {
    Bundle localBundle = new Bundle();
    localBundle.putInt("productcategoryid", this.productCategoryId);
    dispatchAgentChanged("packagelist/category", localBundle);
    dispatchAgentChanged("packagelist/list", localBundle);
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new WeddingPackageListFragment.1(this));
    return localArrayList;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    dispatchProductId();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if ((getArguments() != null) && (getArguments().containsKey("productcategoryid")))
      this.productCategoryId = getArguments().getInt("productcategoryid");
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.common_linearlayout_fragment, paramViewGroup, false);
    setAgentContainerView((ViewGroup)paramLayoutInflater);
    return paramLayoutInflater;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.fragment.WeddingPackageListFragment
 * JD-Core Version:    0.6.0
 */