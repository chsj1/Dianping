package com.dianping.main.favorite;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.dianping.accountservice.AccountService;
import com.dianping.base.app.NovaTabBaseFragment;
import com.dianping.base.basic.NovaTabFragmentActivity;
import com.dianping.util.Log;

public class FavoriteBaseActivity extends NovaTabFragmentActivity
{
  private static final boolean DEBUG = true;
  private static final String HOST_MYFAVORITE = "myfavorite";
  private static final String HOST_USERFAVORITE = "userfavorite";
  private static final String TAG = "FavoriteBaseActivity";
  private boolean isUserFavorite = false;
  private NovaTabBaseFragment mFavoriteShopFragment;
  private NovaTabBaseFragment mFavoriteTuanFragment;

  public NovaTabBaseFragment[] getFragments()
  {
    return new NovaTabBaseFragment[] { this.mFavoriteShopFragment, this.mFavoriteTuanFragment };
  }

  public String[] getTabTitles()
  {
    return new String[] { "商户收藏", "团购收藏" };
  }

  public void onCreate(Bundle paramBundle)
  {
    String str = getIntent().getData().getHost();
    Log.d("FavoriteBaseActivity", "host=" + str);
    if (paramBundle == null);
    for (this.isUserFavorite = "userfavorite".equals(str); ; this.isUserFavorite = paramBundle.getBoolean("isUserFavorite"))
    {
      this.mFavoriteShopFragment = new FavoriteShopFragment();
      if (!this.isUserFavorite)
        this.mFavoriteTuanFragment = new FavoriteTuanFragment();
      super.onCreate(paramBundle);
      if (!this.isUserFavorite)
        break;
      setTitle("Ta的收藏");
      return;
    }
    if (accountService().token() == null)
      super.gotoLogin();
    setButtonView(0);
    setActivityIsEdit(false);
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putBoolean("isUserFavorite", this.isUserFavorite);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.favorite.FavoriteBaseActivity
 * JD-Core Version:    0.6.0
 */