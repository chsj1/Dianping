package com.dianping.shopinfo.community;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.Arrays;

public class SurroundingFacilitiesAgent extends ShopCellAgent
{
  private static final String CELL_SURROUNDING_FACILITIES = "0300facility.01surround";

  public SurroundingFacilitiesAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createView(DPObject[] paramArrayOfDPObject)
  {
    LinearLayout localLinearLayout1 = (LinearLayout)this.res.inflate(getContext(), R.layout.surround_facility, getParentView(), false);
    LinearLayout localLinearLayout2 = (LinearLayout)localLinearLayout1.findViewById(R.id.sf_content);
    int i = 0;
    while (i < paramArrayOfDPObject.length)
    {
      NovaRelativeLayout localNovaRelativeLayout = (NovaRelativeLayout)this.res.inflate(getContext(), R.layout.surround_facility_item, getParentView(), false);
      TextView localTextView1 = (TextView)localNovaRelativeLayout.findViewById(R.id.title);
      TextView localTextView2 = (TextView)localNovaRelativeLayout.findViewById(R.id.subtitle);
      localTextView1.setText(paramArrayOfDPObject[i].getString("Name"));
      localTextView2.setText(paramArrayOfDPObject[i].getString("SubTitle"));
      localNovaRelativeLayout.setOnClickListener(new View.OnClickListener(paramArrayOfDPObject[i].getString("Url"))
      {
        public void onClick(View paramView)
        {
          if ((!TextUtils.isEmpty(this.val$url)) && (SurroundingFacilitiesAgent.this.getFragment() != null))
            SurroundingFacilitiesAgent.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.val$url)));
        }
      });
      localNovaRelativeLayout.setGAString(new String[] { "supermarket", "hospital", "drugstore", "school" }[i], paramArrayOfDPObject[i].getString("Name"));
      localLinearLayout2.addView(localNovaRelativeLayout);
      i += 1;
    }
    return localLinearLayout1;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getShop() == null);
    do
    {
      DPObject[] arrayOfDPObject;
      do
      {
        do
          return;
        while (getShopStatus() != 0);
        arrayOfDPObject = getShop().getArray("ShopNearby");
      }
      while (arrayOfDPObject == null);
      paramBundle = arrayOfDPObject;
      if (arrayOfDPObject.length > 4)
        paramBundle = (DPObject[])Arrays.copyOf(arrayOfDPObject, 4);
      paramBundle = createView(paramBundle);
      removeAllCells();
    }
    while (paramBundle == null);
    addCell("0300facility.01surround", paramBundle, 0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.community.SurroundingFacilitiesAgent
 * JD-Core Version:    0.6.0
 */