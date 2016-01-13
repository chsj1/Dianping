package com.dianping.debug;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class DebugShopConfigActivity extends NovaActivity
{
  List<ConfigData> dataList = new ArrayList();
  private boolean isExpandAll = false;
  ShopAdapter shopAdapter;

  public List<ConfigData> getConfigList(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    while (true)
    {
      int i;
      try
      {
        paramString = new JSONObject(paramString);
        Iterator localIterator = paramString.keys();
        if (localIterator != null)
          if (localIterator.hasNext())
          {
            String str = (String)localIterator.next();
            JSONArray localJSONArray = paramString.getJSONArray(str);
            StringBuilder localStringBuilder = new StringBuilder();
            i = 0;
            if (i >= localJSONArray.length())
              continue;
            if (i == 0)
              break label174;
            localStringBuilder.append("\n\n");
            break label174;
            if (j >= localJSONArray.getJSONArray(i).length())
              break label180;
            localStringBuilder.append(localJSONArray.getJSONArray(i).get(j));
            localStringBuilder.append("  ");
            j += 1;
            continue;
            localArrayList.add(new ConfigData(str, localStringBuilder.toString()));
            continue;
          }
      }
      catch (Exception paramString)
      {
        paramString.printStackTrace();
      }
      return localArrayList;
      label174: int j = 0;
      continue;
      label180: i += 1;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = new ListView(this);
    this.shopAdapter = new ShopAdapter();
    paramBundle.setAdapter(this.shopAdapter);
    this.dataList = getConfigList(getIntent().getStringExtra("shop_config"));
    this.shopAdapter.setData(this.dataList);
    setContentView(paramBundle);
    getTitleBar().findViewById(R.id.title_button).setVisibility(0);
    ((TextView)getTitleBar().findViewById(R.id.title_button)).setText("全部展开");
    getTitleBar().findViewById(R.id.title_button).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (!DebugShopConfigActivity.this.isExpandAll)
        {
          DebugShopConfigActivity.access$002(DebugShopConfigActivity.this, true);
          ((TextView)DebugShopConfigActivity.this.getTitleBar().findViewById(R.id.title_button)).setText("全部收起");
          paramView = DebugShopConfigActivity.this.dataList.iterator();
          while (paramView.hasNext())
            ((DebugShopConfigActivity.ConfigData)paramView.next()).setVisibile(true);
          DebugShopConfigActivity.this.shopAdapter.setData(DebugShopConfigActivity.this.dataList);
          return;
        }
        DebugShopConfigActivity.access$002(DebugShopConfigActivity.this, false);
        ((TextView)DebugShopConfigActivity.this.getTitleBar().findViewById(R.id.title_button)).setText("全部展开");
        paramView = DebugShopConfigActivity.this.dataList.iterator();
        while (paramView.hasNext())
          ((DebugShopConfigActivity.ConfigData)paramView.next()).setVisibile(false);
        DebugShopConfigActivity.this.shopAdapter.setData(DebugShopConfigActivity.this.dataList);
      }
    });
  }

  class ConfigData
  {
    String agentList;
    boolean isVisibile = false;
    String shopView;

    public ConfigData(String paramString1, String arg3)
    {
      this.shopView = paramString1;
      Object localObject;
      this.agentList = localObject;
    }

    public boolean getVisible()
    {
      return this.isVisibile;
    }

    public void setVisibile(boolean paramBoolean)
    {
      this.isVisibile = paramBoolean;
    }
  }

  class ShopAdapter extends BaseAdapter
  {
    List<DebugShopConfigActivity.ConfigData> configDataList = new ArrayList();

    ShopAdapter()
    {
    }

    public int getCount()
    {
      return this.configDataList.size();
    }

    public Object getItem(int paramInt)
    {
      return this.configDataList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = LayoutInflater.from(DebugShopConfigActivity.this.getApplicationContext()).inflate(R.layout.debug_shop_config_item, null);
      paramViewGroup = (TextView)paramView.findViewById(R.id.name);
      TextView localTextView = (TextView)paramView.findViewById(R.id.value);
      ImageView localImageView = (ImageView)paramView.findViewById(R.id.arrow);
      View localView = paramView.findViewById(R.id.arrow_name);
      DebugShopConfigActivity.ConfigData localConfigData = (DebugShopConfigActivity.ConfigData)this.configDataList.get(paramInt);
      paramViewGroup.setText(localConfigData.shopView);
      localTextView.setText(localConfigData.agentList);
      if (localConfigData.isVisibile)
      {
        localImageView.setImageResource(R.drawable.arrow_up);
        localTextView.setVisibility(0);
      }
      while (true)
      {
        localView.setOnClickListener(new DebugShopConfigActivity.ShopAdapter.1(this, localConfigData, localTextView, localImageView));
        return paramView;
        localImageView.setImageResource(R.drawable.arrow_down);
        localTextView.setVisibility(8);
      }
    }

    public void setData(List<DebugShopConfigActivity.ConfigData> paramList)
    {
      this.configDataList.clear();
      this.configDataList.addAll(paramList);
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.debug.DebugShopConfigActivity
 * JD-Core Version:    0.6.0
 */