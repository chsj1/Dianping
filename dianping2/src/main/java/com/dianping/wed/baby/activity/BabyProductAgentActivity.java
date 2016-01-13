package com.dianping.wed.baby.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.wed.baby.fragment.WeddingCrawlProductDetailFragment;
import com.dianping.wed.baby.fragment.WeddingProductAgentFragment;

public class BabyProductAgentActivity extends AgentActivity
{
  protected AgentFragment getAgentFragment()
  {
    if (getIntent().getData() == null)
      return null;
    if (getIntent().getData().getHost().equals("babyproductdetail"))
      return new WeddingProductAgentFragment();
    if (getIntent().getData().getHost().equals("babycrawlproductdetail"))
      return new WeddingCrawlProductDetailFragment();
    return new WeddingProductAgentFragment();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setTitle("");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.activity.BabyProductAgentActivity
 * JD-Core Version:    0.6.0
 */