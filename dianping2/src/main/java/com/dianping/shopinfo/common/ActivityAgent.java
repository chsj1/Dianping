package com.dianping.shopinfo.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public class ActivityAgent extends ShopCellAgent
{
  private static final String CELL_ACTIVITY = "0500Cash.75Activity";
  NovaLinearLayout cell;
  private DPObject mActivityObject;

  public ActivityAgent(Object paramObject)
  {
    super(paramObject);
  }

  private NovaLinearLayout createActivityCell()
  {
    return (NovaLinearLayout)this.res.inflate(getContext(), R.layout.activity_cell, getParentView(), false);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    paramBundle = getShop();
    if ((paramBundle == null) || (getShopStatus() != 0));
    String str;
    do
    {
      do
      {
        return;
        paramBundle = paramBundle.getArray("Activity");
      }
      while ((paramBundle == null) || (paramBundle.length <= 0));
      paramBundle = paramBundle[0];
      str = paramBundle.getString("Title");
    }
    while (TextUtils.isEmpty(str));
    if (this.cell == null)
      this.cell = createActivityCell();
    this.cell.setGAString("free");
    TextView localTextView = (TextView)this.cell.findViewById(R.id.title);
    ImageView localImageView = (ImageView)this.cell.findViewById(R.id.icon);
    if (localTextView != null)
      localTextView.setText(str);
    if (localImageView != null)
      localImageView.setImageResource(R.drawable.detail_icon_huo);
    this.cell.setTag(paramBundle);
    addCell("0500Cash.75Activity", this.cell, 257);
  }

  public void onCellClick(String paramString, View paramView)
  {
    if (this.cell != null)
    {
      paramString = (DPObject)paramView.getTag();
      if (paramString != null)
      {
        paramString = paramString.getString("Url");
        if (!TextUtils.isEmpty(paramString))
        {
          paramString = new Intent("android.intent.action.VIEW", Uri.parse(paramString));
          getFragment().startActivity(paramString);
        }
      }
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null)
    {
      this.mActivityObject = ((DPObject)paramBundle.getParcelable("ActivityObject"));
      if (this.mActivityObject != null)
        dispatchAgentChanged(false);
    }
  }

  public Bundle saveInstanceState()
  {
    Bundle localBundle = super.saveInstanceState();
    localBundle.putParcelable("ActivityObject", this.mActivityObject);
    return localBundle;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.ActivityAgent
 * JD-Core Version:    0.6.0
 */