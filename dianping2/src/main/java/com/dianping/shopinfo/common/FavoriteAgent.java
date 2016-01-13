package com.dianping.shopinfo.common;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.base.util.FavoriteHelper;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.shopinfo.base.PostAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.LoginUtils;
import com.dianping.v1.R.drawable;
import com.dianping.widget.view.NovaImageView;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class FavoriteAgent extends PostAgent
  implements View.OnClickListener
{
  public static final String TITLE_TAG = "5Fav";

  public FavoriteAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void showToast(Context paramContext, String paramString1, String paramString2, int paramInt)
  {
    if (paramContext == null)
      return;
    String str = paramString2;
    if (!TextUtils.isEmpty(paramString1))
      str = paramString1 + "\n" + paramString2;
    paramContext = Toast.makeText(paramContext, str, paramInt);
    paramString1 = (TextView)paramContext.getView().findViewById(16908299);
    if (paramString1 != null)
      paramString1.setGravity(17);
    paramContext.setGravity(17, 0, 0);
    paramContext.show();
  }

  private void update()
  {
    Object localObject = getFragment().accountService();
    if (((AccountService)localObject).token() == null)
      localObject = getFragment().setTitleRightButton("5Fav", R.drawable.ic_action_favorite_off_normal, this);
    while (true)
    {
      ((NovaImageView)localObject).setGAString("favor", getGAExtra());
      return;
      if (FavoriteHelper.isFavoriteShop(((AccountService)localObject).token(), String.valueOf(shopId())))
      {
        localObject = getFragment().setTitleRightButton("5Fav", R.drawable.ic_action_favorite_on_normal, this);
        continue;
      }
      localObject = getFragment().setTitleRightButton("5Fav", R.drawable.ic_action_favorite_off_normal, this);
    }
  }

  public void addFavorite()
  {
    AccountService localAccountService = getFragment().accountService();
    sendRequest(BasicMApiRequest.mapiPost("http://m.api.dianping.com/addfavoriteshop.bin", new String[] { "shopid", String.valueOf(shopId()), "token", localAccountService.token() }));
  }

  public void onClick(View paramView)
  {
    paramView = getFragment().accountService();
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BasicNameValuePair("shopid", shopId() + ""));
    if (paramView.token() == null)
    {
      LoginUtils.setLoginGASource(getContext(), "sp_fav");
      paramView.login(new LoginResultListener()
      {
        public void onLoginCancel(AccountService paramAccountService)
        {
        }

        public void onLoginSuccess(AccountService paramAccountService)
        {
          FavoriteAgent.this.onClick(null);
        }
      });
      statisticsEvent("shopinfo5", "shopinfo5_favo", "", 0, localArrayList);
      return;
    }
    if (FavoriteHelper.isFavoriteShop(paramView.token(), shopId()))
    {
      removeFavorite();
      statisticsEvent("shopinfo5", "shopinfo5_favo", "取消收藏", 0, localArrayList);
      return;
    }
    addFavorite();
    statisticsEvent("shopinfo5", "shopinfo5_favo", "添加收藏", 0, localArrayList);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    super.onRequestFinish(paramMApiRequest, paramMApiResponse);
    if (paramMApiRequest.url().contains("/addfavoriteshop.bin"))
    {
      FavoriteHelper.addFavoriteShop(getFragment().accountService().token(), shopId());
      showToast(getContext(), "收藏成功！", "可在\"我的收藏\"中查看", 0);
    }
    while (true)
    {
      update();
      return;
      if (!paramMApiRequest.url().contains("/delfavoriteshop.bin"))
        continue;
      FavoriteHelper.delFavoriteShop(getFragment().accountService().token(), shopId());
      showToast(getContext(), "", "已取消～", 0);
    }
  }

  public void onResume()
  {
    super.onResume();
    String str = getExSearchResultShopView();
    if ((isExSearchResultType()) && (!"community_common".equalsIgnoreCase(str)))
      return;
    update();
  }

  public void removeFavorite()
  {
    AccountService localAccountService = getFragment().accountService();
    sendRequest(BasicMApiRequest.mapiPost("http://m.api.dianping.com/delfavoriteshop.bin", new String[] { "shopid", String.valueOf(shopId()), "token", localAccountService.token() }));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.FavoriteAgent
 * JD-Core Version:    0.6.0
 */