package com.dianping.base.tuan.agent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.tuan.utils.UrlBuilder;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.drawable;

public class DealInfoFavorAgent extends TuanGroupCellAgent
  implements View.OnClickListener, FragmentManager.OnBackStackChangedListener, RequestHandler<MApiRequest, MApiResponse>
{
  protected static final String FAV_BTN = "02Favorite";
  protected MApiRequest addFavRequest;
  protected int dealId;
  protected MApiRequest delFavRequest;
  private DPObject dpDeal;
  protected boolean isInterested;

  public DealInfoFavorAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void addFavoriteDeal()
  {
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
    localUrlBuilder.appendPath("addfavoritedealgn.bin");
    localUrlBuilder.addParam("groupid", Integer.valueOf(this.dealId));
    localUrlBuilder.addParam("token", accountService().token());
    this.addFavRequest = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.DISABLED);
    mapiService().exec(this.addFavRequest, this);
  }

  private void delFavoriteDeal()
  {
    UrlBuilder localUrlBuilder = UrlBuilder.createBuilder("http://app.t.dianping.com/");
    localUrlBuilder.appendPath("delfavoritedealgn.bin");
    localUrlBuilder.addParam("groupid", Integer.valueOf(this.dealId));
    localUrlBuilder.addParam("token", accountService().token());
    this.delFavRequest = mapiGet(this, localUrlBuilder.buildUrl(), CacheType.DISABLED);
    mapiService().exec(this.delFavRequest, this);
  }

  private void favorite()
  {
    if (accountService().token() == null)
    {
      dismissDialog();
      accountService().login(new LoginResultListener()
      {
        public void onLoginCancel(AccountService paramAccountService)
        {
        }

        public void onLoginSuccess(AccountService paramAccountService)
        {
          DealInfoFavorAgent.this.addFavoriteDeal();
        }
      });
      return;
    }
    if (!this.isInterested)
    {
      addFavoriteDeal();
      return;
    }
    delFavoriteDeal();
  }

  private void updateFavButton()
  {
    if (getContext() == null)
      return;
    if (((FragmentActivity)getContext()).getSupportFragmentManager().getBackStackEntryCount() > 0)
    {
      getFragment().getTitleBar().removeRightViewItem("02Favorite");
      return;
    }
    if (this.isInterested)
    {
      getFragment().getTitleBar().addRightViewItem("02Favorite", R.drawable.ic_action_favorite_on_normal, this);
      return;
    }
    getFragment().getTitleBar().addRightViewItem("02Favorite", R.drawable.ic_action_favorite_off_normal, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (paramBundle != null)
    {
      this.dealId = paramBundle.getInt("dealid");
      paramBundle = (DPObject)paramBundle.getParcelable("deal");
      if (this.dpDeal != paramBundle)
        this.dpDeal = paramBundle;
    }
    if (this.dpDeal != null)
    {
      this.isInterested = this.dpDeal.getBoolean("Interested");
      updateFavButton();
    }
  }

  public void onBackStackChanged()
  {
    updateFavButton();
  }

  public void onClick(View paramView)
  {
    favorite();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (this.dpDeal != null)
      this.isInterested = this.dpDeal.getBoolean("Interested");
    getFragment().getTitleBar().addRightViewItem("02Favorite", R.drawable.ic_action_favorite_off_normal, this);
    getFragment().getFragmentManager().addOnBackStackChangedListener(this);
  }

  public void onDestroy()
  {
    if (this.addFavRequest != null)
    {
      mapiService().abort(this.addFavRequest, this, true);
      this.addFavRequest = null;
    }
    if (this.delFavRequest != null)
    {
      mapiService().abort(this.delFavRequest, this, true);
      this.delFavRequest = null;
    }
    super.onDestroy();
    getFragment().getFragmentManager().removeOnBackStackChangedListener(this);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.addFavRequest)
      this.addFavRequest = null;
    while (true)
    {
      paramMApiRequest = paramMApiResponse.message();
      if ((paramMApiRequest != null) && (getContext() != null))
        Toast.makeText(getContext(), paramMApiRequest.content(), 1).show();
      return;
      if (paramMApiRequest != this.delFavRequest)
        continue;
      this.delFavRequest = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.addFavRequest)
    {
      this.addFavRequest = null;
      Toast.makeText(getContext(), "关注成功", 0).show();
      this.isInterested = true;
      updateFavButton();
    }
    do
      return;
    while (paramMApiRequest != this.delFavRequest);
    this.delFavRequest = null;
    Toast.makeText(getContext(), "已取消~", 0).show();
    this.isInterested = false;
    updateFavButton();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoFavorAgent
 * JD-Core Version:    0.6.0
 */