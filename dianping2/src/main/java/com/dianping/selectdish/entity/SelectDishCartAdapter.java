package com.dianping.selectdish.entity;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.base.widget.NumOperateButton;
import com.dianping.base.widget.NumOperateButton.NumOperateListener;
import com.dianping.selectdish.NewCartManager;
import com.dianping.selectdish.TogetherCartManager;
import com.dianping.selectdish.model.CartFreeItem;
import com.dianping.selectdish.model.CartItem;
import com.dianping.selectdish.model.DishInfo;
import com.dianping.selectdish.model.GiftInfo;
import com.dianping.selectdish.model.GroupOnItem;
import com.dianping.selectdish.model.SetItem;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.util.ArrayList;
import java.util.Iterator;

public class SelectDishCartAdapter extends BaseAdapter
{
  private final int CART_VIEW_TYPE_COUNT = 4;
  private final int ITEM_VIEW_TYPE_DISH = 0;
  private final int ITEM_VIEW_TYPE_FREE_DISH = 2;
  private final int ITEM_VIEW_TYPE_GROUPON = 3;
  private final int ITEM_VIEW_TYPE_TITLE = 1;
  private final Object TAG_TITLE_FREEITEM = new Object();
  private final Object TAG_TITLE_OTHERDISH = new Object();
  private final Object TAG_TITLE_TOTALDISH = new Object();
  private final Object TAG_TITLE_TUANGOU = new Object();
  private Context context;
  private GAUserInfo gaUserInfo = new GAUserInfo();
  private boolean isTogetherOrder = false;
  private NewCartManager manager;
  private TogetherCartManager togetherCartManager;

  public SelectDishCartAdapter(NewCartManager paramNewCartManager, TogetherCartManager paramTogetherCartManager, Context paramContext)
  {
    this.manager = paramNewCartManager;
    this.context = paramContext;
    if (paramTogetherCartManager != null)
    {
      this.togetherCartManager = paramTogetherCartManager;
      this.isTogetherOrder = true;
      this.gaUserInfo.shop_id = Integer.valueOf(paramTogetherCartManager.getShopId());
      return;
    }
    this.gaUserInfo.shop_id = Integer.valueOf(paramNewCartManager.getShopId());
  }

  private int getViewWidth(View paramView, int paramInt)
  {
    paramView.measure(paramInt, paramInt);
    return paramView.getMeasuredWidth();
  }

  private void updateCartFreeItemView(View paramView, CartFreeItem paramCartFreeItem)
  {
    boolean bool = paramCartFreeItem.soldout;
    View localView1 = paramView.findViewById(R.id.sd_freeitem_tag);
    View localView2 = paramView.findViewById(R.id.sd_cart_freeitem_onepiece);
    TextView localTextView1 = (TextView)paramView.findViewById(R.id.sd_freeitem_name);
    localTextView1.setText(paramCartFreeItem.giftInfo.name);
    TextView localTextView2 = (TextView)paramView.findViewById(R.id.sd_freeitem_validtime);
    localTextView2.setText(String.valueOf(paramCartFreeItem.giftInfo.validTime));
    View localView3 = paramView.findViewById(R.id.sd_cart_freeitem_soldout);
    paramView = (CheckBox)paramView.findViewById(R.id.exchange);
    paramView.setChecked(paramCartFreeItem.use);
    paramView.setOnClickListener(new View.OnClickListener(paramView, paramCartFreeItem)
    {
      public void onClick(View paramView)
      {
        boolean bool2 = true;
        boolean bool1 = true;
        if (!SelectDishCartAdapter.this.isTogetherOrder)
        {
          if (SelectDishCartAdapter.this.manager.exchangedGiftId != 0)
            SelectDishCartAdapter.this.manager.exchangedGiftId = 0;
          paramView = this.val$checkBox;
          if (!this.val$cartItem.use);
          while (true)
          {
            paramView.setChecked(bool1);
            SelectDishCartAdapter.this.manager.operateFreeDish(this.val$cartItem);
            return;
            bool1 = false;
          }
        }
        paramView = this.val$checkBox;
        if (!this.val$cartItem.use);
        for (bool1 = bool2; ; bool1 = false)
        {
          paramView.setChecked(bool1);
          SelectDishCartAdapter.this.togetherCartManager.operateFreeDish(this.val$cartItem);
          return;
        }
      }
    });
    if (bool)
    {
      paramView.setVisibility(4);
      localView3.setVisibility(0);
      localView1.setEnabled(false);
      localTextView1.setEnabled(false);
      localTextView2.setEnabled(false);
      localView2.setEnabled(false);
      return;
    }
    paramView.setVisibility(0);
    localView3.setVisibility(4);
    localView1.setEnabled(true);
    localTextView1.setEnabled(true);
    localTextView2.setEnabled(true);
    localView2.setEnabled(true);
  }

  private void updateCartItemView(CartItem paramCartItem, View paramView)
  {
    ((NetworkImageView)paramView.findViewById(R.id.sd_cartitem_photo)).setImage(paramCartItem.dishInfo.url);
    TextView localTextView1 = (TextView)paramView.findViewById(R.id.sd_cartitem_name);
    localTextView1.setText(paramCartItem.dishInfo.name);
    TextView localTextView2 = (TextView)paramView.findViewById(R.id.sd_cartitem_name_copies);
    Object localObject1 = (RMBLabelItem)paramView.findViewById(R.id.sd_menuitem_price);
    if ((paramCartItem.dishInfo.oldPrice != 0.0D) && (paramCartItem.dishInfo.oldPrice > paramCartItem.dishInfo.currentPrice))
    {
      ((RMBLabelItem)localObject1).setRMBLabelStyle(2, 3, false, this.context.getResources().getColor(R.color.light_red));
      ((RMBLabelItem)localObject1).setRMBLabelValue(paramCartItem.dishInfo.currentPrice, paramCartItem.dishInfo.oldPrice);
    }
    while (true)
    {
      localObject1 = (LinearLayout)paramView.findViewById(R.id.sd_cart_mealset_item);
      ((LinearLayout)localObject1).removeAllViews();
      if (paramCartItem.dishInfo.dishType != 1)
        break;
      ((LinearLayout)localObject1).setVisibility(0);
      localObject2 = paramCartItem.dishInfo.setItems.iterator();
      while (((Iterator)localObject2).hasNext())
      {
        SetItem localSetItem = (SetItem)((Iterator)localObject2).next();
        RelativeLayout localRelativeLayout = (RelativeLayout)LayoutInflater.from(this.context).inflate(R.layout.selectdish_cart_mealset_singledish_item, null);
        TextView localTextView3 = (TextView)localRelativeLayout.findViewById(R.id.sd_cart_single_dish_name);
        localTextView3.setText(localSetItem.name);
        TextView localTextView4 = (TextView)localRelativeLayout.findViewById(R.id.sd_cart_single_dish_copies);
        if ((localSetItem.unit != null) && (localSetItem.count > 0))
          localTextView4.setText(localSetItem.count + localSetItem.unit);
        localTextView3.setTextColor(this.context.getResources().getColor(R.color.deep_gray));
        localTextView4.setTextColor(this.context.getResources().getColor(R.color.deep_gray));
        ((LinearLayout)localObject1).addView(localRelativeLayout);
      }
      ((RMBLabelItem)localObject1).setRMBLabelStyle(2, 2, false, this.context.getResources().getColor(R.color.light_red));
      ((RMBLabelItem)localObject1).setRMBLabelValue(paramCartItem.dishInfo.currentPrice);
    }
    localObject1 = paramView.findViewById(R.id.sd_cartitem_event_view);
    Object localObject2 = (TextView)paramView.findViewById(R.id.sd_cartitem_event);
    if ((paramCartItem.dishInfo.freeItem != null) && (paramCartItem.dishInfo.targetCount != 0) && (paramCartItem.dishInfo.freeCount != 0))
    {
      ((View)localObject1).setVisibility(0);
      if (paramCartItem.dishInfo.freeItem.dishId != paramCartItem.dishInfo.dishId)
      {
        ((TextView)localObject2).setText(this.context.getResources().getString(R.string.sd_event2).replace("%s", String.valueOf(paramCartItem.dishInfo.targetCount)));
        localObject1 = (TextView)paramView.findViewById(R.id.together_who_choose);
        if (this.isTogetherOrder)
          break label719;
        ((TextView)localObject1).setVisibility(8);
      }
    }
    while (true)
    {
      localObject2 = (NumOperateButton)paramView.findViewById(R.id.sd_cartitem_operate);
      localObject1 = paramView.findViewById(R.id.sd_cartitem_not_sale_view);
      paramView = (TextView)paramView.findViewById(R.id.sd_cartitem_partner_count);
      if ((this.togetherCartManager == null) || (this.togetherCartManager.isOwner != 0) || (this.togetherCartManager.getAllDishesinTotalDish().contains(paramCartItem)) || (!this.togetherCartManager.isDishboughtByPartners(paramCartItem.dishInfo.dishId)))
        break label791;
      ((NumOperateButton)localObject2).setVisibility(8);
      ((View)localObject1).setVisibility(8);
      paramView.setVisibility(0);
      paramView.setText(paramCartItem.getItemCount() + paramCartItem.dishInfo.unit);
      return;
      ((TextView)localObject2).setText(this.context.getResources().getString(R.string.sd_event).replace("%s", String.valueOf(paramCartItem.dishInfo.targetCount)).replace("%n", String.valueOf(paramCartItem.dishInfo.freeCount)));
      break;
      ((View)localObject1).setVisibility(8);
      break;
      label719: if ((this.togetherCartManager != null) && (this.togetherCartManager.isOwner == 1))
      {
        if (this.togetherCartManager.isDishboughtByPartners(paramCartItem.dishInfo.dishId))
        {
          ((TextView)localObject1).setText("小伙伴点的");
          ((TextView)localObject1).setVisibility(0);
          continue;
        }
        ((TextView)localObject1).setVisibility(8);
        continue;
      }
      ((TextView)localObject1).setVisibility(8);
    }
    label791: paramView.setVisibility(8);
    if (paramCartItem.dishInfo.isValidity)
    {
      localTextView2.setVisibility(8);
      ((View)localObject1).setVisibility(8);
      ((NumOperateButton)localObject2).setVisibility(0);
      ((NumOperateButton)localObject2).setCurrentValue(paramCartItem.getItemCount());
      ((NumOperateButton)localObject2).setNumOperateListener(new NumOperateButton.NumOperateListener(paramCartItem)
      {
        public void addResult(boolean paramBoolean, int paramInt)
        {
          if (paramBoolean)
          {
            if (SelectDishCartAdapter.this.isTogetherOrder)
              break label56;
            SelectDishCartAdapter.this.manager.addDish(this.val$cartItem.dishInfo);
          }
          while (true)
          {
            GAHelper.instance().contextStatisticsEvent(SelectDishCartAdapter.this.context, "change", SelectDishCartAdapter.this.gaUserInfo, "tap");
            return;
            label56: SelectDishCartAdapter.this.togetherCartManager.addDish(this.val$cartItem.dishInfo);
          }
        }

        public void subtractResult(boolean paramBoolean, int paramInt)
        {
          if (paramBoolean)
          {
            if (SelectDishCartAdapter.this.isTogetherOrder)
              break label56;
            SelectDishCartAdapter.this.manager.reduceDish(this.val$cartItem.dishInfo);
          }
          while (true)
          {
            GAHelper.instance().contextStatisticsEvent(SelectDishCartAdapter.this.context, "change", SelectDishCartAdapter.this.gaUserInfo, "tap");
            return;
            label56: SelectDishCartAdapter.this.togetherCartManager.reduceDish(this.val$cartItem.dishInfo);
          }
        }
      });
      return;
    }
    int k = View.MeasureSpec.makeMeasureSpec(0, 0);
    localTextView2.setVisibility(0);
    ((View)localObject1).setVisibility(0);
    ((NumOperateButton)localObject2).setVisibility(8);
    localTextView2.setText("×" + paramCartItem.getItemCount());
    paramView = (TextView)((View)localObject1).findViewById(R.id.sd_cartitem_not_sale_tip);
    paramView.setText(paramCartItem.dishInfo.saleTime);
    int i = getViewWidth(localTextView1, k);
    int j = getViewWidth(localTextView2, k);
    k = getViewWidth(paramView, k);
    if (this.context.getResources().getDisplayMetrics().widthPixels - k - ViewUtils.dip2px(this.context, 116.0F) < i + j)
    {
      paramView = (LinearLayout.LayoutParams)localTextView1.getLayoutParams();
      paramView.width = 0;
      paramView.weight = 1.0F;
      localTextView1.setLayoutParams(paramView);
    }
    while (true)
    {
      ((View)localObject1).findViewById(R.id.sd_cartitem_delete_dish).setOnClickListener(new View.OnClickListener(paramCartItem)
      {
        public void onClick(View paramView)
        {
          if (!SelectDishCartAdapter.this.isTogetherOrder)
          {
            SelectDishCartAdapter.this.manager.deleteDish(this.val$cartItem.dishInfo);
            return;
          }
          SelectDishCartAdapter.this.togetherCartManager.deleteDish(this.val$cartItem.dishInfo);
        }
      });
      return;
      localTextView1.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
    }
  }

  private void updateGroupOnItemView(View paramView, GroupOnItem paramGroupOnItem)
  {
    Object localObject = (TextView)paramView.findViewById(R.id.dish_name_textview);
    TextView localTextView2 = (TextView)paramView.findViewById(R.id.dish_item_amount_textview);
    TextView localTextView1 = (TextView)paramView.findViewById(R.id.dish_more_detail_textview);
    paramView = (LinearLayout)paramView.findViewById(R.id.dish_sets_layout);
    ((TextView)localObject).setText(paramGroupOnItem.groupOnName);
    localTextView2.setText(paramGroupOnItem.groupOnItemAmount);
    paramView.removeAllViews();
    if ((paramGroupOnItem.groupOnSet != null) && (paramGroupOnItem.groupOnSet.length >= 1))
    {
      localObject = new StringBuilder();
      paramGroupOnItem = paramGroupOnItem.groupOnSet;
      int j = paramGroupOnItem.length;
      int i = 0;
      while (i < j)
      {
        localTextView2 = paramGroupOnItem[i];
        ((StringBuilder)localObject).setLength(0);
        DPObject localDPObject = localTextView2.getObject("Dish");
        if (localDPObject != null)
          ((StringBuilder)localObject).append(localDPObject.getString("Name"));
        ((StringBuilder)localObject).append(" x ").append(localTextView2.getInt("Count"));
        localTextView2 = (TextView)LayoutInflater.from(this.context).inflate(R.layout.selectdish_dish_set_item_view, null);
        localTextView2.setText(((StringBuilder)localObject).toString());
        paramView.addView(localTextView2);
        i += 1;
      }
    }
    localTextView1.setOnClickListener(new View.OnClickListener(paramView)
    {
      public void onClick(View paramView)
      {
        SelectDishCartAdapter.this.gaUserInfo.title = "menuorder_dealcart";
        GAHelper.instance().contextStatisticsEvent(SelectDishCartAdapter.this.context, "menuorder_dealcart_dealdetail", SelectDishCartAdapter.this.gaUserInfo, "tap");
        paramView.setVisibility(8);
        this.val$dishSetsLayout.setVisibility(0);
      }
    });
  }

  private void updateTitleItemView(View paramView, Object paramObject)
  {
    paramView = (TextView)paramView;
    if (paramObject == this.TAG_TITLE_FREEITEM)
    {
      paramView.setText(this.context.getString(R.string.sd_cart_title_free));
      return;
    }
    if (paramObject == this.TAG_TITLE_TUANGOU)
    {
      paramView.setText("您已点菜品");
      return;
    }
    if (paramObject == this.TAG_TITLE_TOTALDISH)
    {
      paramView.setText("我点的菜品");
      return;
    }
    if (paramObject == this.TAG_TITLE_OTHERDISH)
    {
      paramView.setText("小伙伴们点的");
      return;
    }
    paramView.setText(null);
  }

  public int getCount()
  {
    int i = 0;
    int k = 0;
    int j = 0;
    if (!this.isTogetherOrder)
    {
      k = this.manager.getAllDishes().size();
      if (this.manager.getAllFreeDishes().size() > 0)
      {
        i = this.manager.getAllFreeDishes().size() + 1;
        if (this.manager.getGroupOnInfo() != null)
          break label71;
      }
      while (true)
      {
        return k + i + j;
        i = 0;
        break;
        label71: j = 2;
      }
    }
    if (this.togetherCartManager.isOwner == 1)
    {
      j = this.togetherCartManager.getAllDishesinTotalDish().size();
      if (this.togetherCartManager.getAllFreeDishes().size() > 0)
        i = this.togetherCartManager.getAllFreeDishes().size() + 1;
      return 0 + j + i;
    }
    if (this.togetherCartManager.getAllDishesinTotalDish().size() > 0);
    for (i = this.togetherCartManager.getAllDishesinTotalDish().size() + 1; ; i = 0)
    {
      j = k;
      if (this.togetherCartManager.getAllDishesinOtherDish().size() > 0)
        j = this.togetherCartManager.getAllDishesinOtherDish().size() + 1;
      return 0 + i + j;
    }
  }

  public Object getItem(int paramInt)
  {
    if (!this.isTogetherOrder)
    {
      if (this.manager.getGroupOnInfo() == null)
      {
        if (paramInt < this.manager.getAllDishes().size())
          return this.manager.getAllDishes().get(paramInt);
        if (paramInt == this.manager.getAllDishes().size())
          return this.TAG_TITLE_FREEITEM;
        return this.manager.getAllFreeDishes().get(paramInt - this.manager.getAllDishes().size() - 1);
      }
      if (paramInt < this.manager.getAllDishes().size())
        return this.manager.getAllDishes().get(paramInt);
      if (paramInt == this.manager.getAllDishes().size())
        return this.TAG_TITLE_TUANGOU;
      if (paramInt == this.manager.getAllDishes().size() + 1)
        return this.manager.getGroupOnInfo();
      if (paramInt == this.manager.getAllDishes().size() + 2)
        return this.TAG_TITLE_FREEITEM;
      return this.manager.getAllFreeDishes().get(paramInt - this.manager.getAllDishes().size() - 3);
    }
    if (this.togetherCartManager.isOwner == 1)
    {
      if (paramInt < this.togetherCartManager.getAllDishesinTotalDish().size())
        return this.togetherCartManager.getAllDishesinTotalDish().get(paramInt);
      if (paramInt == this.togetherCartManager.getAllDishesinTotalDish().size())
        return this.TAG_TITLE_FREEITEM;
      return this.togetherCartManager.getAllFreeDishes().get(paramInt - this.togetherCartManager.getAllDishesinTotalDish().size() - 1);
    }
    if (this.togetherCartManager.getAllDishesinTotalDish().size() > 0)
    {
      if (paramInt == 0)
        return this.TAG_TITLE_TOTALDISH;
      if (paramInt < this.togetherCartManager.getAllDishesinTotalDish().size() + 1)
        return this.togetherCartManager.getAllDishesinTotalDish().get(paramInt - 1);
      if (paramInt == this.togetherCartManager.getAllDishesinTotalDish().size() + 1)
        return this.TAG_TITLE_OTHERDISH;
      return this.togetherCartManager.getAllDishesinOtherDish().get(paramInt - this.togetherCartManager.getAllDishesinTotalDish().size() - 2);
    }
    if (paramInt == 0)
      return this.TAG_TITLE_OTHERDISH;
    return this.togetherCartManager.getAllDishesinOtherDish().get(paramInt - 1);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public int getItemViewType(int paramInt)
  {
    if (!this.isTogetherOrder)
      if (this.manager.getGroupOnInfo() == null)
        if (paramInt >= this.manager.getAllDishes().size());
    do
      while (true)
      {
        return 0;
        if (paramInt == this.manager.getAllDishes().size())
          return 1;
        return 2;
        if (paramInt < this.manager.getAllDishes().size())
          continue;
        if (paramInt == this.manager.getAllDishes().size())
          return 1;
        if (paramInt == this.manager.getAllDishes().size() + 1)
          return 3;
        if (paramInt == this.manager.getAllDishes().size() + 2)
          return 1;
        return 2;
        if (this.togetherCartManager.isOwner == 1)
        {
          if (paramInt < this.togetherCartManager.getAllDishesinTotalDish().size())
            continue;
          if (paramInt == this.togetherCartManager.getAllDishesinTotalDish().size())
            return 1;
          return 2;
        }
        if (this.togetherCartManager.getAllDishesinTotalDish().size() <= 0)
          break;
        if (paramInt == 0)
          return 1;
        if ((paramInt >= this.togetherCartManager.getAllDishesinTotalDish().size() + 1) && (paramInt == this.togetherCartManager.getAllDishesinTotalDish().size() + 1))
          return 1;
      }
    while (paramInt != 0);
    return 1;
  }

  public double getTotalPrice()
  {
    if (!this.isTogetherOrder)
      return this.manager.getTotalPrice();
    return this.togetherCartManager.getTotalPrice();
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    int i = getItemViewType(paramInt);
    View localView = paramView;
    if (paramView == null);
    switch (i)
    {
    default:
      localView = paramView;
      paramView = getItem(paramInt);
      switch (i)
      {
      default:
      case 0:
      case 2:
      case 3:
      case 1:
      }
    case 0:
    case 2:
    case 3:
    case 1:
    }
    do
    {
      do
      {
        do
        {
          return localView;
          localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.selectdish_cart_item, paramViewGroup, false);
          break;
          localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.selectdish_cart_free_item, paramViewGroup, false);
          break;
          localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.selectdish_cart_groupon_item, paramViewGroup, false);
          break;
          localView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.selectdish_cart_title_item, paramViewGroup, false);
          break;
        }
        while (!(paramView instanceof CartItem));
        updateCartItemView((CartItem)paramView, localView);
        return localView;
      }
      while (!(paramView instanceof CartFreeItem));
      updateCartFreeItemView(localView, (CartFreeItem)paramView);
      return localView;
    }
    while (!(paramView instanceof GroupOnItem));
    updateGroupOnItemView(localView, (GroupOnItem)paramView);
    return localView;
    updateTitleItemView(localView, paramView);
    return localView;
  }

  public int getViewTypeCount()
  {
    return 4;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.entity.SelectDishCartAdapter
 * JD-Core Version:    0.6.0
 */