package com.dianping.base.tuan.agent;

import android.os.Bundle;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.tuan.fragment.TuanAgentFragment.Styleable;
import java.util.Arrays;
import java.util.List;

@Deprecated
public abstract class DealBaseAgent extends TuanGroupCellAgent
{
  public static final int STYLE_CLICKABLE = 1;
  public static final int STYLE_FLAT_CELL = 1024;
  public static final int STYLE_INDICATOR = 256;
  public static final int STYLE_LONG_CLICKABLE = 2;
  protected final NovaActivity activity = (NovaActivity)getContext();
  protected int dealId;
  protected DPObject dpDeal;

  public DealBaseAgent(Object paramObject)
  {
    super(paramObject);
  }

  public int dealId()
  {
    return this.dealId;
  }

  public int getStyle()
  {
    if ((this.fragment instanceof TuanAgentFragment.Styleable))
      return ((TuanAgentFragment.Styleable)this.fragment).getStyle();
    return 0;
  }

  public void gotoLogin(LoginResultListener paramLoginResultListener)
  {
    dismissDialog();
    accountService().login(paramLoginResultListener);
  }

  public boolean hasChannelTag(String paramString)
  {
    List localList;
    if ((this.dpDeal != null) && (this.dpDeal.getStringArray("DealChannelTags") != null))
      localList = Arrays.asList(this.dpDeal.getStringArray("DealChannelTags"));
    return (localList != null) && (localList.contains(paramString));
  }

  protected boolean isOldStyle()
  {
    return false;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (paramBundle != null)
    {
      this.dealId = paramBundle.getInt("dealid");
      paramBundle = (DPObject)paramBundle.getParcelable("deal");
      if (this.dpDeal != paramBundle)
      {
        this.dpDeal = paramBundle;
        onDealRetrived(paramBundle);
      }
    }
  }

  protected void onDealRetrived(DPObject paramDPObject)
  {
  }

  public void setDealObject(DPObject paramDPObject)
  {
    this.dpDeal = paramDPObject;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealBaseAgent
 * JD-Core Version:    0.6.0
 */