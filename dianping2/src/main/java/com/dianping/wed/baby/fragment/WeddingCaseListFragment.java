package com.dianping.wed.baby.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.v1.R.layout;
import com.dianping.wed.baby.activity.WeddingCaseListActivity;
import java.util.ArrayList;

public class WeddingCaseListFragment extends WeddingProductBaseFragment
{
  boolean isBeautyShop = false;
  int officalTypeId;
  int productCategoryId;
  DPObject[] topNavObjects;

  public void dispatchAgentChanged(String paramString, Bundle paramBundle)
  {
    super.dispatchAgentChanged(paramString, paramBundle);
    int i;
    if ((paramBundle != null) && (paramBundle.containsKey("caseobject")))
    {
      this.topNavObjects = ((DPObject)paramBundle.getParcelable("caseobject")).getArray("TopNav");
      if ((this.topNavObjects != null) && (this.topNavObjects.length > 0) && (getActivity() != null) && ((getActivity() instanceof WeddingCaseListActivity)))
      {
        ((WeddingCaseListActivity)getActivity()).resetTitleBar(this.topNavObjects);
        i = 0;
      }
    }
    while (true)
    {
      if (i < this.topNavObjects.length)
      {
        if (this.topNavObjects[i].getInt("Type") == 1)
        {
          paramString = this.topNavObjects[i].getString("ID");
          i = 0;
          if (TextUtils.isDigitsOnly(paramString))
            i = Integer.parseInt(paramString);
          this.productCategoryId = i;
        }
      }
      else
        return;
      i += 1;
    }
  }

  void dispatchProductId()
  {
    if (isBeautyShop())
    {
      localBundle = new Bundle();
      localBundle.putSerializable("beautyfiltermap", Integer.valueOf(0));
      dispatchAgentChanged("caselist/list", localBundle);
      return;
    }
    Bundle localBundle = new Bundle();
    localBundle.putInt("productcategoryid", this.productCategoryId);
    dispatchAgentChanged("caselist/filterbar", localBundle);
    dispatchAgentChanged("caselist/list", localBundle);
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new WeddingCaseListFragment.1(this));
    return localArrayList;
  }

  public int getOfficalTypeId()
  {
    return this.officalTypeId;
  }

  public boolean isBeautyShop()
  {
    return this.isBeautyShop;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    dispatchProductId();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.common_linearlayout_fragment, paramViewGroup, false);
    setAgentContainerView((ViewGroup)paramLayoutInflater);
    return paramLayoutInflater;
  }

  public void setIsBeautyShop(boolean paramBoolean)
  {
    this.isBeautyShop = paramBoolean;
  }

  public void setOfficalTypeId(int paramInt)
  {
    this.officalTypeId = paramInt;
  }

  public void setProductCategoryId(int paramInt)
  {
    if (this.productCategoryId == paramInt)
      return;
    this.productCategoryId = paramInt;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.fragment.WeddingCaseListFragment
 * JD-Core Version:    0.6.0
 */