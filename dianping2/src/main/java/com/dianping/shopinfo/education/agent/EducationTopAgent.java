package com.dianping.shopinfo.education.agent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.shopinfo.common.TopAgent;
import com.dianping.shopinfo.education.view.EducationShopHeaderView;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.shopinfo.widget.ShopInfoHeaderView;
import com.dianping.v1.R.layout;

public class EducationTopAgent extends TopAgent
{
  protected static final String CELL_TOP = "0200Basic.01top";
  protected final View.OnClickListener iconClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      boolean bool = true;
      paramView = EducationTopAgent.this.getShop();
      if (paramView == null);
      do
        return;
      while ((!paramView.contains("PicCount")) || (paramView.getInt("PicCount") <= 0));
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopphoto"));
      localIntent.putExtra("objShop", paramView);
      if ((paramView.getInt("Status") == 1) || (paramView.getInt("Status") == 4))
        bool = false;
      localIntent.putExtra("enableUpload", bool);
      EducationTopAgent.this.getFragment().startActivity(localIntent);
    }
  };
  protected ShopInfoHeaderView topView;

  public EducationTopAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    paramBundle = getShop();
    removeAllCells();
    if (paramBundle == null);
    do
      return;
    while (getFragment() == null);
    if ((this.topView != null) && (!this.topView.getClass().equals(EducationShopHeaderView.class)))
      this.topView = null;
    if (this.topView == null)
      this.topView = ((EducationShopHeaderView)LayoutInflater.from(getContext()).inflate(R.layout.shop_education_head, getParentView(), false));
    if (getSharedObject("EducationShopInfo") != null)
      ((EducationShopHeaderView)this.topView).setHeaderInfo((DPObject)getSharedObject("EducationShopInfo"));
    if (getShopStatus() == 0)
      this.topView.setShop(paramBundle, 0);
    while (true)
    {
      this.topView.setIconClickListen(this.iconClickListener);
      addCell("0200Basic.01top", this.topView, 0);
      return;
      this.topView.setShop(paramBundle);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.agent.EducationTopAgent
 * JD-Core Version:    0.6.0
 */