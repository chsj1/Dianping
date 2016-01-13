package com.dianping.wed.baby.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class WeddingCasePhotosFragment extends WeddingProductBaseFragment
{
  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new WeddingCasePhotosFragment.1(this));
    return localArrayList;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.needProductReuqest = false;
    this.needShopRequest = false;
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.wed_casephotos_fragment, paramViewGroup, false);
    setAgentContainerView((ViewGroup)paramLayoutInflater);
    return paramLayoutInflater;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.fragment.WeddingCasePhotosFragment
 * JD-Core Version:    0.6.0
 */