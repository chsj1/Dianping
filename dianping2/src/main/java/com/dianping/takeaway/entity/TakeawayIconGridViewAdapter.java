package com.dianping.takeaway.entity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;

public class TakeawayIconGridViewAdapter extends BasicAdapter
{
  private Context mContext;
  private DPObject[] pageIcons;

  private void setupIcon(View paramView, DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    NetworkImageView localNetworkImageView = (NetworkImageView)paramView.findViewById(R.id.icon_img);
    TextView localTextView = (TextView)paramView.findViewById(R.id.icon_name);
    if (paramDPObject == TakeawayMainDataSource.EntryParser.EMPTY_ITEM)
    {
      localNetworkImageView.setLocalDrawable(null);
      localTextView.setText("");
      return;
    }
    String str = paramDPObject.getString("Name");
    ViewUtils.setVisibilityAndContent(localTextView, str);
    localNetworkImageView.setImage(paramDPObject.getString("Icon"));
    paramView.setOnClickListener(new View.OnClickListener(paramDPObject)
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$iconObj.getString("Url")));
        TakeawayIconGridViewAdapter.this.mContext.startActivity(paramView);
      }
    });
    ((NovaLinearLayout)paramView).setGAString("entrance", str, paramDPObject.getInt("Index"));
    GAHelper.instance().statisticsEvent(paramView, "view");
  }

  public int getCount()
  {
    if (this.pageIcons != null)
      return this.pageIcons.length;
    return 0;
  }

  public DPObject getItem(int paramInt)
  {
    if ((this.pageIcons != null) && (paramInt < this.pageIcons.length))
      return this.pageIcons[paramInt];
    return null;
  }

  public long getItemId(int paramInt)
  {
    if ((this.pageIcons != null) && (paramInt < this.pageIcons.length) && (this.pageIcons[paramInt] != TakeawayMainDataSource.EntryParser.EMPTY_ITEM))
      return paramInt;
    return -1L;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    paramView = LayoutInflater.from(this.mContext).inflate(R.layout.takeaway_icon_grid_item, null);
    setupIcon(paramView, getItem(paramInt));
    return paramView;
  }

  public void setData(Context paramContext, DPObject[] paramArrayOfDPObject)
  {
    this.mContext = paramContext;
    this.pageIcons = paramArrayOfDPObject;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayIconGridViewAdapter
 * JD-Core Version:    0.6.0
 */