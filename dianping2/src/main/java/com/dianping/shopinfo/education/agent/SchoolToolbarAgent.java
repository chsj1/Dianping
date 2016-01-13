package com.dianping.shopinfo.education.agent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ToolbarButton;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopInfoToolbarAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class SchoolToolbarAgent extends ShopInfoToolbarAgent
{
  private DPObject schoolExtendInfo;

  public SchoolToolbarAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void editToolBar(DPObject paramDPObject)
  {
    Object localObject1 = getToolbarView().findViewWithTag("6Show");
    if (localObject1 != null)
      getToolbarView().removeView((View)localObject1);
    if ((paramDPObject != null) && (!TextUtils.isEmpty(paramDPObject.getString("ShowPersonUrl"))))
    {
      localObject1 = (ToolbarButton)this.res.inflate(getContext(), R.layout.school_toolbar_button, getParentView(), false);
      ((ToolbarButton)localObject1).setGAString("edu_school_showcelebrity", getGAExtra());
      Object localObject2 = paramDPObject.getString("ShowPersonUrl");
      paramDPObject = paramDPObject.getString("ShowPersonTitle");
      ((ToolbarButton)localObject1).setOnClickListener(new View.OnClickListener((String)localObject2)
      {
        public void onClick(View paramView)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$url));
          SchoolToolbarAgent.this.getFragment().startActivity(paramView);
        }
      });
      if (!TextUtils.isEmpty(paramDPObject))
      {
        localObject2 = (TextView)((ToolbarButton)localObject1).findViewById(R.id.show_flower_titleTV);
        ((TextView)localObject2).setText(paramDPObject);
        ((TextView)localObject2).setVisibility(0);
      }
      addToolbarButton((View)localObject1, "6Show");
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.schoolExtendInfo = ((DPObject)getSharedObject("SchoolShopInfo"));
    if (this.schoolExtendInfo == null)
      return;
    paramBundle = getToolbarView().findViewWithTag("4CheckIn");
    getToolbarView().removeView(paramBundle);
    editToolBar(this.schoolExtendInfo);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.agent.SchoolToolbarAgent
 * JD-Core Version:    0.6.0
 */