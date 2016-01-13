package com.dianping.tuan.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.List;

public class ReviewSelectShopActivity extends NovaListActivity
  implements AdapterView.OnItemClickListener
{
  private Adapter adapter;
  List<DPObject> items;
  int selectShopId;

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = new TextView(this);
    paramBundle.setText("取消");
    paramBundle.setGravity(17);
    paramBundle.setPadding((int)getResources().getDimension(R.dimen.title_bar_button_margin), 0, 0, 0);
    paramBundle.setTextColor(getResources().getColor(R.color.titlebar_action_hint_text_color));
    paramBundle.setTextSize(2, 15.0F);
    paramBundle.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ReviewSelectShopActivity.this.finish();
      }
    });
    super.getTitleBar().setCustomLeftView(paramBundle);
    this.selectShopId = getIntent().getIntExtra("selectShopId", 0);
    this.items = getIntent().getParcelableArrayListExtra("shopList");
    this.adapter = new Adapter();
    this.listView.setAdapter(this.adapter);
    this.listView.setOnItemClickListener(this);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = new Intent();
    paramAdapterView.putExtra("selectShopId", ((DPObject)this.items.get(paramInt)).getInt("id"));
    setResult(-1, paramAdapterView);
    finish();
  }

  class Adapter extends BasicAdapter
  {
    Adapter()
    {
    }

    public int getCount()
    {
      if (ReviewSelectShopActivity.this.items == null)
        return 0;
      return ReviewSelectShopActivity.this.items.size();
    }

    public DPObject getItem(int paramInt)
    {
      return (DPObject)ReviewSelectShopActivity.this.items.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return ((DPObject)ReviewSelectShopActivity.this.items.get(paramInt)).getInt("id");
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      View localView = paramView;
      if (paramView == null)
        localView = ReviewSelectShopActivity.this.getLayoutInflater().inflate(R.layout.tuan_review_shop_select, paramViewGroup, false);
      ((NovaLinearLayout)localView).setGAString("shop_change", "");
      paramView = (TextView)localView.findViewById(R.id.name);
      paramViewGroup = (RadioButton)localView.findViewById(R.id.radio);
      paramView.setText(getItem(paramInt).getString("name"));
      if (getItemId(paramInt) == ReviewSelectShopActivity.this.selectShopId)
      {
        paramView.setTextColor(ReviewSelectShopActivity.this.getResources().getColor(R.color.tuan_common_orange));
        paramViewGroup.setChecked(true);
        return localView;
      }
      paramView.setTextColor(ReviewSelectShopActivity.this.getResources().getColorStateList(R.color.filter_text_seletor));
      paramViewGroup.setChecked(false);
      return localView;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.ReviewSelectShopActivity
 * JD-Core Version:    0.6.0
 */