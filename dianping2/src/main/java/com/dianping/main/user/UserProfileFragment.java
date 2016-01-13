package com.dianping.main.user;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.app.DPActivity;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AdapterAgentFragment;
import com.dianping.base.app.loader.AgentInfo;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.widget.CustomImageButton;
import com.dianping.main.user.agent.UserInfoAgent;
import com.dianping.main.user.agent.UserProAgent;
import com.dianping.main.user.agent.UserProfileAgent;
import com.dianping.model.UserProfile;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserProfileFragment extends AdapterAgentFragment
  implements AccountListener
{
  private int mAlphaDist;
  private CustomImageButton mBackButtonOrange;
  private CustomImageButton mBackButtonWhite;
  private boolean mIsMySelf = false;
  private View mTitleBarLayout;
  private View mTitleBarShadow;
  private int preUserId;
  private UserProfile user;

  private int userId()
  {
    if (this.user == null)
      return this.preUserId;
    return this.user.id();
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
        localHashMap.put("user/userprofile", UserProfileAgent.class);
        localHashMap.put("user/userpro", UserProAgent.class);
        localHashMap.put("user/userinfo", UserInfoAgent.class);
        return localHashMap;
      }

      public boolean shouldShow()
      {
        return true;
      }
    });
    return localArrayList;
  }

  public void onAccountChanged(AccountService paramAccountService)
  {
    boolean bool = false;
    if (getAccount() != null)
    {
      if (getAccount().id() == userId())
        bool = true;
      this.mIsMySelf = bool;
      return;
    }
    if ((this.mIsMySelf) && (getActivity() != null))
    {
      getActivity().finish();
      return;
    }
    this.mIsMySelf = false;
  }

  public void onCreate(Bundle paramBundle)
  {
    boolean bool2 = false;
    super.onCreate(paramBundle);
    if (paramBundle == null)
    {
      this.preUserId = getActivity().getIntent().getIntExtra("userId", 0);
      if ((this.preUserId != 0) || (getActivity().getIntent().getData() == null));
    }
    while (true)
    {
      try
      {
        this.preUserId = Integer.parseInt(getActivity().getIntent().getData().getQueryParameter("userid"));
      }
      catch (java.lang.NumberFormatException paramBundle)
      {
        try
        {
          paramBundle = getActivity().getIntent().getParcelableExtra("user");
          if (!(paramBundle instanceof DPObject))
            continue;
          this.user = ((UserProfile)((DPObject)paramBundle).decodeToObject(UserProfile.DECODER));
          if (!(getActivity() instanceof DPActivity))
            continue;
          ((DPActivity)getActivity()).gaExtra.biz_id = String.valueOf(userId());
          setSharedObject("memberId", Integer.valueOf(userId()));
          setSharedObject("user", this.user);
          paramBundle = getAccount();
          boolean bool1 = bool2;
          if (paramBundle == null)
            continue;
          bool1 = bool2;
          if (paramBundle.id() != userId())
            continue;
          bool1 = true;
          this.mIsMySelf = bool1;
          accountService().addListener(this);
          return;
          paramBundle = paramBundle;
          if (getAccount() == null)
            continue;
          int i = getAccount().id();
          this.preUserId = i;
          continue;
          i = 0;
          continue;
          if (!(paramBundle instanceof UserProfile))
            continue;
          this.user = ((UserProfile)paramBundle);
          continue;
        }
        catch (ArchiveException paramBundle)
        {
          paramBundle.printStackTrace();
          continue;
        }
      }
      this.preUserId = paramBundle.getInt("preUserId");
      this.user = ((UserProfile)paramBundle.getParcelable("user"));
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.user_profile_layout, paramViewGroup, false);
    setAgentContainerListView((ListView)paramLayoutInflater.findViewById(R.id.user_profile));
    this.mTitleBarLayout = paramLayoutInflater.findViewById(R.id.user_title_bar_layout);
    paramBundle = this.mTitleBarLayout.findViewById(R.id.user_title_bar);
    this.mBackButtonOrange = ((CustomImageButton)this.mTitleBarLayout.findViewById(R.id.user_back_orange));
    this.mBackButtonWhite = ((CustomImageButton)this.mTitleBarLayout.findViewById(R.id.back));
    this.mTitleBarShadow = this.mTitleBarLayout.findViewById(R.id.user_title_shadow);
    if ((getContext() instanceof DPActivity))
      ((DPActivity)getContext()).addGAView(this.mBackButtonWhite, -1);
    this.mBackButtonWhite.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        UserProfileFragment.this.getActivity().finish();
      }
    });
    paramBundle.setBackgroundColor(Color.argb(0, 252, 252, 252));
    this.mBackButtonOrange.setAlpha(0);
    this.mBackButtonWhite.setAlpha(255);
    this.mAlphaDist = ViewUtils.dip2px(paramViewGroup.getContext(), 85.0F);
    this.listView.setOnScrollListener(new AbsListView.OnScrollListener(paramBundle)
    {
      private int getAlpha(int paramInt)
      {
        if (paramInt > UserProfileFragment.this.mAlphaDist)
        {
          UserProfileFragment.this.mTitleBarShadow.setVisibility(0);
          return 255;
        }
        UserProfileFragment.this.mTitleBarShadow.setVisibility(4);
        return (int)(255.0D / UserProfileFragment.this.mAlphaDist * paramInt);
      }

      public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
      {
        if (paramInt1 == 0)
        {
          if (UserProfileFragment.this.listView.getChildAt(0) == null)
            return;
          paramInt1 = getAlpha(Math.abs(UserProfileFragment.this.listView.getChildAt(0).getTop()));
          this.val$titleBar.setBackgroundColor(Color.argb(paramInt1, 252, 252, 252));
          UserProfileFragment.this.mBackButtonOrange.setAlpha(paramInt1);
          UserProfileFragment.this.mBackButtonWhite.setAlpha(255 - paramInt1);
          return;
        }
        this.val$titleBar.setBackgroundColor(Color.argb(255, 252, 252, 252));
        UserProfileFragment.this.mTitleBarShadow.setVisibility(0);
        UserProfileFragment.this.mBackButtonOrange.setAlpha(255);
        UserProfileFragment.this.mBackButtonWhite.setAlpha(0);
      }

      public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
      {
        onScroll(paramAbsListView, UserProfileFragment.this.listView.getFirstVisiblePosition(), 0, 0);
      }
    });
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    accountService().removeListener(this);
    super.onDestroy();
  }

  public void onProfileChanged(AccountService paramAccountService)
  {
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("preUserId", this.preUserId);
    paramBundle.putParcelable("user", this.user);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.UserProfileFragment
 * JD-Core Version:    0.6.0
 */