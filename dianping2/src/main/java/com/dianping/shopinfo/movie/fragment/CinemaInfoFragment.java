package com.dianping.shopinfo.movie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.movie.config.CinemaInfoAgentListConfig;
import java.util.ArrayList;

public class CinemaInfoFragment extends ShopInfoFragment
{
  public DPObject dpCinema;

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new CinemaInfoAgentListConfig());
    return localArrayList;
  }

  public DPObject getCinema()
  {
    return this.dpCinema;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.toolbarView.setVisibility(8);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getActivity().getIntent();
    this.dpCinema = ((DPObject)paramBundle.getParcelableExtra("cinema"));
    if (this.dpCinema == null)
      getActivity().finish();
    this.shop = ((DPObject)paramBundle.getParcelableExtra("main_shop"));
    this.shopRetrieved = true;
    this.shopId = this.shop.getInt("ID");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.movie.fragment.CinemaInfoFragment
 * JD-Core Version:    0.6.0
 */