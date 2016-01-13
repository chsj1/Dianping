package com.dianping.shopinfo.common;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.base.share.enums.ShareType;
import com.dianping.base.share.util.ShareUtil;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.drawable;
import com.dianping.widget.view.NovaImageView;

public class ShareAgent extends ShopCellAgent
  implements View.OnClickListener
{
  private static final String TITLE_TAG = "2Share";
  private String mGAAction = "";
  private final Handler mHandler = new Handler();

  public ShareAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onClick(View paramView)
  {
    paramView = getShop();
    if (paramView == null)
      return;
    ShareUtil.gotoShareTo(getContext(), ShareType.SHOP, paramView, "shopinfo5", "shopinfo5_share");
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (isExSearchResultType());
    do
    {
      do
      {
        return;
        ((NovaImageView)getFragment().setTitleRightButton("2Share", R.drawable.ic_action_share, this)).setGAString("share", getGAExtra());
      }
      while (!(getContext() instanceof Activity));
      paramBundle = ((Activity)getContext()).getIntent().getData();
    }
    while (paramBundle == null);
    this.mGAAction = paramBundle.getQueryParameter("type");
  }

  public void onResume()
  {
    super.onResume();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.ShareAgent
 * JD-Core Version:    0.6.0
 */