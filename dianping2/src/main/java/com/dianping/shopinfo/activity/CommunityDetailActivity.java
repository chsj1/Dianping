package com.dianping.shopinfo.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommunityDetailActivity extends NovaActivity
{
  private BaseAdapter communityDetailAdapter;
  private ArrayList<DPObject> communityInfo;

  private void setHolder(ViewHolder paramViewHolder, DPObject paramDPObject)
  {
    paramViewHolder.nameText.setText(paramDPObject.getString("Name"));
    paramViewHolder.description.setText(paramDPObject.getString("Value"));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.communityInfo = getIntent().getParcelableArrayListExtra("info");
    paramBundle = getIntent().getStringExtra("shopname");
    this.communityDetailAdapter = new CommunityAdapter(this.communityInfo);
    ListView localListView = new ListView(this);
    localListView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    localListView.setAdapter(this.communityDetailAdapter);
    localListView.setBackgroundColor(-1);
    localListView.setSelector(new ColorDrawable(0));
    setContentView(localListView);
    setTitle(paramBundle);
  }

  class CommunityAdapter extends BaseAdapter
  {
    private List<DPObject> infoList = new ArrayList();

    public CommunityAdapter()
    {
      this.infoList.clear();
      Collection localCollection;
      if ((localCollection != null) && (localCollection.size() > 0))
        this.infoList.addAll(localCollection);
    }

    public int getCount()
    {
      return this.infoList.size();
    }

    public Object getItem(int paramInt)
    {
      return this.infoList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
      {
        paramView = LayoutInflater.from(CommunityDetailActivity.this.getApplicationContext()).inflate(R.layout.shopinfo_community_detail_item, null);
        paramViewGroup = new CommunityDetailActivity.ViewHolder();
        paramViewGroup.nameText = ((TextView)paramView.findViewById(R.id.name));
        paramViewGroup.description = ((TextView)paramView.findViewById(R.id.desc));
        paramView.setTag(paramViewGroup);
      }
      while (true)
      {
        CommunityDetailActivity.this.setHolder(paramViewGroup, (DPObject)this.infoList.get(paramInt));
        return paramView;
        paramViewGroup = (CommunityDetailActivity.ViewHolder)paramView.getTag();
      }
    }
  }

  static class ViewHolder
  {
    TextView description;
    TextView nameText;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.activity.CommunityDetailActivity
 * JD-Core Version:    0.6.0
 */