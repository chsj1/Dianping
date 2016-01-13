package com.dianping.pm.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import com.dianping.base.app.NovaActivity;
import com.dianping.pm.fragment.PmOrderTabFragment;
import java.net.URLEncoder;

public class PmOrderTabActivity extends NovaActivity
{
  private PmOrderTabFragment mTabFragment;

  private void showFragments()
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    Object localObject = new FrameLayout(this);
    ((FrameLayout)localObject).setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    ((FrameLayout)localObject).setId(16908300);
    setContentView((View)localObject);
    this.mTabFragment = new PmOrderTabFragment();
    localObject = new Bundle();
    ((Bundle)localObject).putString("target_tab", getIntent().getData().getQueryParameter("tab"));
    this.mTabFragment.setArguments((Bundle)localObject);
    localFragmentTransaction.replace(16908300, this.mTabFragment);
    localFragmentTransaction.commitAllowingStateLoss();
  }

  protected boolean isNeedLogin()
  {
    return true;
  }

  public void onBackPressed()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://me"));
    localIntent.addFlags(67108864);
    startActivity(localIntent);
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + URLEncoder.encode("http://h5.dianping.com/tuan/score/mall/index.html"))));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (isLogined())
      showFragments();
  }

  protected boolean onLogin(boolean paramBoolean)
  {
    if ((paramBoolean) && (isNeedLogin()))
      showFragments();
    return super.onLogin(paramBoolean);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.pm.activity.PmOrderTabActivity
 * JD-Core Version:    0.6.0
 */