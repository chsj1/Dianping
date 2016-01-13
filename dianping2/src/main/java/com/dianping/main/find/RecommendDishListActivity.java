package com.dianping.main.find;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.share.util.ShopUtil;
import com.dianping.base.ugc.review.AddReviewUtil;
import com.dianping.base.widget.TitleBar;
import com.dianping.util.LoginUtils;
import com.dianping.v1.R.layout;

public class RecommendDishListActivity extends NovaActivity
  implements View.OnClickListener
{
  private static final int RECOMMEND_DISH_REQ_CODE = 21;
  private DPObject objShop;
  int shopId;

  private void gotoChooseDish()
  {
    statisticsEvent("shopinfo5", "shopinfo5_dish_review", "", 0);
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://choosedish"));
    localIntent.putExtra("shopId", String.valueOf(this.shopId));
    startActivityForResult(localIntent, 21);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 21) && (paramInt2 == -1))
    {
      Bundle localBundle = new Bundle();
      if (this.objShop != null)
      {
        localBundle.putParcelable("shop", this.objShop);
        localBundle.putString("shopName", ShopUtil.getShopName(this.objShop));
        localBundle.putBoolean("fromRecommend", true);
      }
      localBundle.putString("source", "shopinfo");
      localBundle.putStringArrayList("dishes", paramIntent.getStringArrayListExtra("dishes"));
      localBundle.putBoolean("checkdraft", false);
      AddReviewUtil.addReview(this, this.shopId, null, localBundle);
    }
  }

  public void onClick(View paramView)
  {
    if (accountService().token() == null)
    {
      LoginUtils.setLoginGASource(this, "rec_add");
      gotoLogin();
      return;
    }
    gotoChooseDish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.recommend_layout);
    this.objShop = ((DPObject)getIntent().getParcelableExtra("objShop"));
    this.shopId = getIntent().getIntExtra("shopId", 0);
    if ((this.shopId == 0) && (this.objShop != null))
      this.shopId = this.objShop.getInt("ID");
    if (this.shopId == 0)
      finish();
    super.setTitle("网友推荐");
    super.getTitleBar().addRightViewItem("我来推荐", null, this);
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
    gotoChooseDish();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.RecommendDishListActivity
 * JD-Core Version:    0.6.0
 */