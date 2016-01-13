package com.dianping.wed.weddingfeast.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.base.widget.MeasuredGridView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaButton;
import java.util.ArrayList;

public class WeddingFeastRegionsActivity extends DPActivity
{
  private NovaButton cancel;
  private NovaButton confirm;
  private View mLayRegions;
  private View mLayRegionsBg;
  private ArrayList<String> regionNames = new ArrayList();
  private ArrayList<String> selectedRegions = new ArrayList();
  private int shopId;

  private void initView()
  {
    this.confirm = ((NovaButton)findViewById(R.id.confirm));
    this.cancel = ((NovaButton)findViewById(R.id.cancel));
    this.confirm.setGAString("region_submit");
    this.cancel.setGAString("region_cancel");
    this.confirm.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent();
        paramView.putExtra("selectedRegions", WeddingFeastRegionsActivity.this.selectedRegions);
        WeddingFeastRegionsActivity.this.setResult(0, paramView);
        WeddingFeastRegionsActivity.this.finish();
        WeddingFeastRegionsActivity.this.overridePendingTransition(0, 0);
      }
    });
    this.cancel.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        WeddingFeastRegionsActivity.this.finish();
        WeddingFeastRegionsActivity.this.overridePendingTransition(0, 0);
      }
    });
    this.mLayRegionsBg = findViewById(R.id.lay_regions_bg);
    this.mLayRegionsBg.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        WeddingFeastRegionsActivity.this.finish();
        WeddingFeastRegionsActivity.this.overridePendingTransition(0, 0);
      }
    });
    this.mLayRegions = findViewById(R.id.lay_regions);
    Object localObject = AnimationUtils.loadAnimation(this, R.anim.popup_up_in);
    this.mLayRegions.startAnimation((Animation)localObject);
    localObject = (MeasuredGridView)findViewById(R.id.regionsView);
    ((MeasuredGridView)localObject).setAdapter(new GridViewAdapter(this));
    int i = ViewUtils.dip2px(this, 28.0F);
    int j = (int)Math.ceil((((MeasuredGridView)localObject).getCount() + 0.0D) / 3.0D);
    int k = ViewUtils.dip2px(this, 7.0F);
    int m = ViewUtils.dip2px(this, 70.0F);
    ViewGroup.LayoutParams localLayoutParams = ((MeasuredGridView)localObject).getLayoutParams();
    localLayoutParams.height = (i * j + (j - 1) * k + m);
    ((MeasuredGridView)localObject).setLayoutParams(localLayoutParams);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.wedding_feast_regions);
    this.regionNames = getIntent().getStringArrayListExtra("regionsNames");
    if ((this.regionNames == null) || (this.regionNames.size() < 1))
      return;
    this.selectedRegions = getIntent().getStringArrayListExtra("selectedRegions");
    this.shopId = getIntent().getIntExtra("shopId", 0);
    initView();
  }

  class GridViewAdapter extends BaseAdapter
  {
    Context context;

    GridViewAdapter(Context arg2)
    {
      Object localObject;
      this.context = localObject;
    }

    public int getCount()
    {
      return WeddingFeastRegionsActivity.this.regionNames.size();
    }

    public Object getItem(int paramInt)
    {
      return Integer.valueOf(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = LayoutInflater.from(this.context).inflate(R.layout.wedding_feast_region_item, null, false);
      paramViewGroup = (TextView)paramView.findViewById(R.id.regionText);
      paramViewGroup.setPadding(0, ViewUtils.dip2px(this.context, 7.0F), 0, ViewUtils.dip2px(this.context, 7.0F));
      paramViewGroup.setTextSize(2, 15.0F);
      ImageView localImageView = (ImageView)paramView.findViewById(R.id.check);
      String str = (String)WeddingFeastRegionsActivity.this.regionNames.get(paramInt);
      paramViewGroup.setText(str);
      if (WeddingFeastRegionsActivity.this.selectedRegions.contains(str))
      {
        paramView.setBackgroundDrawable(WeddingFeastRegionsActivity.this.getResources().getDrawable(R.drawable.red_line_box_bk));
        paramViewGroup.setTextColor(WeddingFeastRegionsActivity.this.getResources().getColor(R.color.light_red));
        localImageView.setVisibility(0);
      }
      while (true)
      {
        paramView.setOnClickListener(new WeddingFeastRegionsActivity.GridViewAdapter.1(this));
        return paramView;
        paramView.setBackgroundDrawable(WeddingFeastRegionsActivity.this.getResources().getDrawable(R.drawable.black_line_box_bk));
        paramViewGroup.setTextColor(WeddingFeastRegionsActivity.this.getResources().getColor(R.color.light_gray));
        localImageView.setVisibility(8);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.weddingfeast.activity.WeddingFeastRegionsActivity
 * JD-Core Version:    0.6.0
 */