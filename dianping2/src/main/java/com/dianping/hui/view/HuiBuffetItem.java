package com.dianping.hui.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.hui.activity.HuiBuffetSelecteListActivity;
import com.dianping.hui.entity.Buffet;
import com.dianping.hui.entity.BuffetRule;
import com.dianping.hui.entity.HuiBuffetDataSource.ScrollListener;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.List;

public class HuiBuffetItem extends NovaLinearLayout
{
  boolean _justOneItem = false;
  HuiBuffetSelecteListActivity context;
  RMBLabelItem curPrice;
  private ImageView ivBuffetMore;
  LinearLayout layoutBuffetRules;
  View layoutMoreBuffetRule;
  Operator operator;
  RMBLabelItem originPrice;
  private TextView tvBuffetRuleMore;
  private TextView tvBuffetStatus;
  TextView tvBuffetTitle;
  private TextView tvBuffetUnit;
  private TextView tvOriginPriceUnit;

  public HuiBuffetItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public HuiBuffetItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.context = ((HuiBuffetSelecteListActivity)paramContext);
  }

  private void addBuffetRuleItem(List<BuffetRule> paramList, boolean paramBoolean)
  {
    this.layoutBuffetRules.removeAllViews();
    if ((paramList == null) || (paramList.size() == 0));
    while (true)
    {
      return;
      int i = 0;
      while ((i < paramList.size()) && ((this._justOneItem) || (paramBoolean) || (paramList.size() != 3) || (i != 2)) && ((this._justOneItem) || (paramBoolean) || (paramList.size() != 2) || (i != 1)))
      {
        addDescItem(i, paramList.size(), (BuffetRule)paramList.get(i), paramBoolean);
        i += 1;
      }
    }
  }

  private void addDescItem(int paramInt1, int paramInt2, BuffetRule paramBuffetRule, boolean paramBoolean)
  {
    LinearLayout localLinearLayout = (LinearLayout)LayoutInflater.from(this.context).inflate(R.layout.hui_buffet_desc_parent, null, false);
    Object localObject1 = (TextView)localLinearLayout.findViewById(R.id.meal_desc_title);
    ((TextView)localObject1).setText(paramBuffetRule.title);
    Object localObject2 = new LinearLayout.LayoutParams(-2, -2);
    Object localObject3 = getContext();
    float f;
    if (paramInt1 == 0)
      f = 12.0F;
    while (true)
    {
      ((LinearLayout.LayoutParams)localObject2).setMargins(0, ViewUtils.dip2px((Context)localObject3, f), 0, 0);
      ((TextView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
      paramBuffetRule = paramBuffetRule.desc;
      if ((paramBuffetRule != null) && (paramBuffetRule.size() != 0))
        break;
      return;
      f = 10.0F;
    }
    int j = 0;
    if ((j >= paramBuffetRule.size()) || ((!this._justOneItem) && (!paramBoolean) && (j == 1)))
    {
      this.layoutBuffetRules.addView(localLinearLayout);
      return;
    }
    localObject1 = (String)paramBuffetRule.get(j);
    localObject2 = (LinearLayout)LayoutInflater.from(this.context).inflate(R.layout.hui_buffet_desc_item, null, true);
    localObject3 = new LinearLayout.LayoutParams(-2, -2);
    if ((isLastDescItem(paramInt1, paramInt2, paramBuffetRule, j)) || (isVirtualLastDescItem(paramInt2, paramInt1, paramBoolean, j)));
    for (int i = 15; ; i = 0)
    {
      int k = i;
      if (this._justOneItem)
      {
        k = i;
        if (isLastDescItem(paramInt1, paramInt2, paramBuffetRule, j))
          k = 20;
      }
      ((LinearLayout.LayoutParams)localObject3).setMargins(0, ViewUtils.dip2px(getContext(), 5.0F), 0, ViewUtils.dip2px(getContext(), k));
      ((LinearLayout)localObject2).setLayoutParams((ViewGroup.LayoutParams)localObject3);
      ((TextView)((LinearLayout)localObject2).findViewById(R.id.meal_desc)).setText((CharSequence)localObject1);
      localLinearLayout.addView((View)localObject2);
      j += 1;
      break;
    }
  }

  private boolean isLastDescItem(int paramInt1, int paramInt2, List<String> paramList, int paramInt3)
  {
    return (paramInt1 == paramInt2 - 1) && (paramInt3 == paramList.size() - 1);
  }

  private boolean isLastRule(int paramInt1, int paramInt2)
  {
    return ((paramInt1 == 3) && (paramInt2 == 1)) || ((paramInt1 == 2) && (paramInt2 == 0));
  }

  private boolean isVirtualLastDescItem(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
  {
    return (!this._justOneItem) && (!paramBoolean) && (isLastRule(paramInt1, paramInt2)) && (paramInt3 == 0);
  }

  private void restoreViewStatus(Buffet paramBuffet, List<BuffetRule> paramList)
  {
    Operator localOperator = this.operator;
    if (paramBuffet.status == 0);
    for (boolean bool = false; ; bool = true)
    {
      localOperator.restoreValue(bool, paramBuffet.count, paramBuffet.maxCount, paramBuffet.minCount);
      if (!paramBuffet.isExpand)
        break;
      addBuffetRuleItem(paramList, true);
      this.ivBuffetMore.setImageResource(R.drawable.navibar_arrow_up);
      this.tvBuffetRuleMore.setText("收起");
      return;
    }
    addBuffetRuleItem(paramList, false);
    this.ivBuffetMore.setImageResource(R.drawable.navibar_arrow_down);
    this.tvBuffetRuleMore.setText("更多信息");
  }

  public void bindView(HuiBuffetDataSource.ScrollListener paramScrollListener, Buffet paramBuffet, boolean paramBoolean)
  {
    this._justOneItem = paramBoolean;
    List localList = paramBuffet.details;
    if (paramBoolean)
    {
      addBuffetRuleItem(localList, true);
      this.layoutMoreBuffetRule.setVisibility(8);
      this.operator.setOperationCallback(new HuiBuffetItem.2(this, paramBuffet));
      this.curPrice.setRMBLabelValue(Double.parseDouble(paramBuffet.discountPrice));
      if (paramBuffet.originalPrice != null)
        break label232;
      this.originPrice.setVisibility(8);
      this.tvOriginPriceUnit.setVisibility(8);
      label86: this.tvBuffetTitle.setText(paramBuffet.title);
      if (paramBuffet.status != 0)
        break label265;
      this.tvBuffetStatus.setVisibility(0);
      this.tvBuffetStatus.setText(paramBuffet.statusDesc);
      this.curPrice.setRMBLabelStyle(3, 2, false, getResources().getColor(R.color.light_gray));
      this.tvBuffetUnit.setTextColor(getResources().getColor(R.color.light_gray));
    }
    while (true)
    {
      if (paramScrollListener != null)
        this.operator.setTouchListenerForEditText(new HuiBuffetItem.3(this, paramScrollListener));
      restoreViewStatus(paramBuffet, localList);
      this.operator.setTag(paramBuffet);
      return;
      addBuffetRuleItem(localList, false);
      this.layoutMoreBuffetRule.setVisibility(0);
      this.layoutMoreBuffetRule.setOnClickListener(new HuiBuffetItem.1(this, paramBuffet, localList));
      break;
      label232: this.originPrice.setRMBLabelValue(Double.parseDouble(paramBuffet.originalPrice));
      this.originPrice.setVisibility(0);
      this.tvOriginPriceUnit.setVisibility(0);
      break label86;
      label265: if (paramBuffet.status != 10)
        continue;
      this.tvBuffetStatus.setVisibility(8);
      this.curPrice.setRMBLabelStyle(3, 2, false, getResources().getColor(R.color.tuan_common_orange));
      this.tvBuffetUnit.setTextColor(getResources().getColor(R.color.deep_gray));
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.tvBuffetTitle = ((TextView)findViewById(R.id.tv_self_meal_title));
    this.tvBuffetRuleMore = ((TextView)findViewById(R.id.tv_buffet_rule_more));
    this.layoutMoreBuffetRule = findViewById(R.id.rl_more_self_rule);
    this.layoutBuffetRules = ((LinearLayout)findViewById(R.id.ll_self_use_rule));
    this.tvBuffetUnit = ((TextView)findViewById(R.id.tv_buffet_unit));
    this.tvBuffetStatus = ((TextView)findViewById(R.id.tv_self_meal_status));
    this.tvOriginPriceUnit = ((TextView)findViewById(R.id.tv_self_meal_origin_price_unit));
    this.curPrice = ((RMBLabelItem)findViewById(R.id.cur_price));
    this.originPrice = ((RMBLabelItem)findViewById(R.id.origin_price));
    this.ivBuffetMore = ((ImageView)findViewById(R.id.iv_self_meal_more));
    this.operator = ((Operator)findViewById(R.id.buffet_operator));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.view.HuiBuffetItem
 * JD-Core Version:    0.6.0
 */