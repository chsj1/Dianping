package com.dianping.shopinfo.dish;

import android.os.Bundle;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;

public class DishDetailInfoActivity extends AgentActivity
{
  private DishDetailInfoFragment mFragment;
  private int shopinfoId;

  private void resolveArguments()
  {
    this.shopinfoId = getIntParam("dishshopid");
    String str;
    if (this.shopinfoId == 0)
      str = getStringParam("id");
    try
    {
      this.shopinfoId = Integer.parseInt(str);
      return;
    }
    catch (Exception localException)
    {
    }
  }

  protected AgentFragment getAgentFragment()
  {
    if (this.mFragment == null)
    {
      this.mFragment = new DishDetailInfoFragment();
      this.mFragment.setDishshopid(this.shopinfoId);
    }
    return this.mFragment;
  }

  public void onCreate(Bundle paramBundle)
  {
    resolveArguments();
    super.onCreate(paramBundle);
    setTitle("");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.dish.DishDetailInfoActivity
 * JD-Core Version:    0.6.0
 */