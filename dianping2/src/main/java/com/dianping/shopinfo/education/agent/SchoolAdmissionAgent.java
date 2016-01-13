package com.dianping.shopinfo.education.agent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.TicketCell;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.ViewUtils;

public class SchoolAdmissionAgent extends ShopCellAgent
{
  private static final String CELL_SCHOOL_ORDER = "0500school.03admission";

  public SchoolAdmissionAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    paramBundle = (DPObject)getSharedObject("eduRelatedInfo");
    if ((paramBundle == null) || (paramBundle.getArray("SchoolSimpleInfoList") == null));
    do
    {
      do
      {
        return;
        paramBundle = paramBundle.getArray("SchoolSimpleInfoList");
      }
      while (paramBundle.length < 3);
      localObject = paramBundle[2];
    }
    while ((localObject == null) || (TextUtils.isEmpty(((DPObject)localObject).getString("Title"))));
    String str2 = ((DPObject)localObject).getString("Title");
    String str1 = ((DPObject)localObject).getString("DetailLink");
    paramBundle = "";
    String[] arrayOfString = ((DPObject)localObject).getStringArray("TagList");
    Object localObject = paramBundle;
    if (arrayOfString != null)
    {
      localObject = paramBundle;
      if (arrayOfString.length > 0)
      {
        int j = arrayOfString.length;
        int i = 0;
        while (true)
        {
          localObject = paramBundle;
          if (i >= j)
            break;
          localObject = arrayOfString[i];
          paramBundle = paramBundle + (String)localObject + " ";
          i += 1;
        }
      }
    }
    paramBundle = createTicketCell();
    paramBundle.setTitle(str2);
    if (!TextUtils.isEmpty((CharSequence)localObject))
      paramBundle.setRightText((String)localObject);
    paramBundle.setTitleLineSpacing(6.4F);
    paramBundle.setTitleMaxLines(1);
    localObject = new LinearLayout.LayoutParams(-1, -2);
    ((LinearLayout.LayoutParams)localObject).topMargin = ViewUtils.dip2px(getContext(), 4.3F);
    paramBundle.getTitleView().setLayoutParams((ViewGroup.LayoutParams)localObject);
    paramBundle.setGAString("edu_schoolrecruitment", getGAExtra());
    paramBundle.setOnClickListener(new View.OnClickListener(str1)
    {
      public void onClick(View paramView)
      {
        try
        {
          if (!TextUtils.isEmpty(this.val$detailLink))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$detailLink));
            SchoolAdmissionAgent.this.getFragment().startActivity(paramView);
          }
          return;
        }
        catch (java.lang.Exception paramView)
        {
        }
      }
    });
    addCell("0500school.03admission", paramBundle, 256);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.agent.SchoolAdmissionAgent
 * JD-Core Version:    0.6.0
 */