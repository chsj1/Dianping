package com.dianping.main.find.pictureplaza;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlazaHomeMainFragment extends PlazaAdapterAgentFragment
{
  private PullToRefreshListView mListView;

  private void redirectToAddPhoto()
  {
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://addcommunityphoto").buildUpon().appendQueryParameter("maxnum", String.valueOf(9)).build()));
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new AgentListConfig()
    {
      public Map<String, AgentInfo> getAgentInfoList()
      {
        return null;
      }

      public Map<String, Class<? extends CellAgent>> getAgentList()
      {
        HashMap localHashMap = new HashMap();
        localHashMap.put("photocommunity/banner", PlazaHomeBannerAgent.class);
        localHashMap.put("photocommunity/topic", PlazaHomeTopicAgent.class);
        localHashMap.put("photocommunity/hotugc", PlazaHomeFeedAgent.class);
        return localHashMap;
      }

      public boolean shouldShow()
      {
        return true;
      }
    });
    return localArrayList;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.find_photo_community_main, null, false);
    paramLayoutInflater.findViewById(R.id.publisher).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (PlazaHomeMainFragment.this.accountService().token() == null)
        {
          PlazaHomeMainFragment.this.accountService().login(new LoginResultListener()
          {
            public void onLoginCancel(AccountService paramAccountService)
            {
            }

            public void onLoginSuccess(AccountService paramAccountService)
            {
              PlazaHomeMainFragment.this.redirectToAddPhoto();
            }
          });
          return;
        }
        PlazaHomeMainFragment.this.redirectToAddPhoto();
      }
    });
    this.mListView = ((PullToRefreshListView)paramLayoutInflater.findViewById(R.id.photo_plaza_list));
    this.mListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener()
    {
      public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
      {
        PlazaHomeMainFragment.this.startRefresh(paramPullToRefreshListView);
      }
    });
    setAgentContainerListView(this.mListView);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    super.onDestroy();
  }

  void onRefreshComplete()
  {
    this.mListView.onRefreshComplete();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaHomeMainFragment
 * JD-Core Version:    0.6.0
 */