package com.dianping.takeaway.entity;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.takeaway.util.TakeawayUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public class OrderDetailAdapter extends BaseAdapter
{
  private Context context;
  private final List<TAOrderItem> data = new LinkedList();

  public OrderDetailAdapter(Context paramContext)
  {
    this.context = paramContext;
  }

  private LinearLayout.LayoutParams createTALinearLayoutParams(float paramFloat)
  {
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(0, -2);
    localLayoutParams.gravity = 16;
    localLayoutParams.weight = paramFloat;
    return localLayoutParams;
  }

  public int getCount()
  {
    return this.data.size();
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
    float f2 = 1.0F;
    TextView localTextView;
    RMBLabelItem localRMBLabelItem;
    TAOrderItem localTAOrderItem;
    label102: float f1;
    if (paramView == null)
    {
      paramView = LayoutInflater.from(this.context).inflate(R.layout.takeaway_orderlist_item, paramViewGroup, false);
      paramViewGroup = (TextView)paramView.findViewById(R.id.takeaway_orderlist_item_text_name);
      localTextView = (TextView)paramView.findViewById(R.id.takeaway_orderlist_item_text_count);
      localRMBLabelItem = (RMBLabelItem)paramView.findViewById(R.id.takeaway_orderlist_item_text_price);
      localTAOrderItem = (TAOrderItem)this.data.get(paramInt);
      paramViewGroup.setText(localTAOrderItem.title);
      ViewUtils.setVisibilityAndContent(localTextView, localTAOrderItem.count);
      if (localTextView.getVisibility() != 0)
        break label222;
      paramInt = 1;
      if (paramInt == 0)
        break label227;
      f1 = 9.0F;
      label110: paramViewGroup.setLayoutParams(createTALinearLayoutParams(f1));
      if (paramInt == 0)
        break label233;
      f1 = 2.0F;
      label127: localTextView.setLayoutParams(createTALinearLayoutParams(f1));
      f1 = f2;
      if (paramInt != 0)
        f1 = 3.0F;
      localRMBLabelItem.setLayoutParams(createTALinearLayoutParams(f1));
      if (localTAOrderItem.type != TAOrderItemType.ORDER_ACTIVITY)
        break label239;
    }
    label222: label227: label233: label239: for (paramInt = this.context.getResources().getColor(R.color.light_red); ; paramInt = this.context.getResources().getColor(R.color.deep_black))
    {
      paramViewGroup.setTextColor(paramInt);
      localTextView.setTextColor(paramInt);
      localRMBLabelItem.setRMBLabelStyle6(false, paramInt);
      localRMBLabelItem.setRMBLabelValue(Double.parseDouble(localTAOrderItem.subTitle));
      return paramView;
      break;
      paramInt = 0;
      break label102;
      f1 = 1.0F;
      break label110;
      f1 = 0.0F;
      break label127;
    }
  }

  public void setData(DPObject[] paramArrayOfDPObject1, DPObject[] paramArrayOfDPObject2, DPObject[] paramArrayOfDPObject3, DPObject[] paramArrayOfDPObject4)
  {
    this.data.clear();
    int j;
    int i;
    Object localObject2;
    Object localObject1;
    TAOrderItemType localTAOrderItemType;
    if ((paramArrayOfDPObject1 != null) && (paramArrayOfDPObject1.length > 0))
    {
      j = paramArrayOfDPObject1.length;
      i = 0;
      while (i < j)
      {
        localObject2 = paramArrayOfDPObject1[i];
        localObject1 = ((DPObject)localObject2).getString("Title");
        localObject2 = TakeawayUtils.PRICE_DF.format(((DPObject)localObject2).getDouble("Price"));
        localTAOrderItemType = TAOrderItemType.ORDER_ITEM;
        this.data.add(new TAOrderItem((String)localObject1, (String)localObject2, "", localTAOrderItemType));
        i += 1;
      }
    }
    if ((paramArrayOfDPObject2 != null) && (paramArrayOfDPObject2.length > 0))
    {
      j = paramArrayOfDPObject2.length;
      i = 0;
      while (i < j)
      {
        localObject2 = paramArrayOfDPObject2[i];
        paramArrayOfDPObject1 = ((DPObject)localObject2).getString("Title");
        localObject1 = String.format("%s", new Object[] { TakeawayUtils.PRICE_DF.format(((DPObject)localObject2).getDouble("Price")) });
        localObject2 = String.format("%s份", new Object[] { Integer.valueOf(((DPObject)localObject2).getInt("Count")) });
        localTAOrderItemType = TAOrderItemType.ORDER_ITEM;
        this.data.add(new TAOrderItem(paramArrayOfDPObject1, (String)localObject1, (String)localObject2, localTAOrderItemType));
        i += 1;
      }
    }
    if ((paramArrayOfDPObject3 != null) && (paramArrayOfDPObject3.length > 0))
    {
      j = paramArrayOfDPObject3.length;
      i = 0;
      while (i < j)
      {
        localObject1 = paramArrayOfDPObject3[i];
        paramArrayOfDPObject1 = ((DPObject)localObject1).getObject("ActivityButton").getString("Message");
        paramArrayOfDPObject2 = ((DPObject)localObject1).getString("ActivityInfo");
        if (((DPObject)localObject1).getBoolean("IsShow"))
        {
          localObject1 = TAOrderItemType.ORDER_ACTIVITY;
          this.data.add(new TAOrderItem(paramArrayOfDPObject1, paramArrayOfDPObject2, "", (TAOrderItemType)localObject1));
        }
        i += 1;
      }
    }
    if ((paramArrayOfDPObject4 != null) && (paramArrayOfDPObject4.length > 0))
    {
      j = paramArrayOfDPObject4.length;
      i = 0;
      while (i < j)
      {
        paramArrayOfDPObject2 = paramArrayOfDPObject4[i];
        paramArrayOfDPObject1 = paramArrayOfDPObject2.getString("Title");
        paramArrayOfDPObject2 = String.format("%s", new Object[] { TakeawayUtils.PRICE_DF.format(paramArrayOfDPObject2.getDouble("Price")) });
        paramArrayOfDPObject3 = TAOrderItemType.ORDER_FEE;
        this.data.add(new TAOrderItem(paramArrayOfDPObject1, paramArrayOfDPObject2, "", paramArrayOfDPObject3));
        i += 1;
      }
    }
    notifyDataSetChanged();
  }

  private class TAOrderItem
  {
    String count;
    String subTitle;
    String title;
    OrderDetailAdapter.TAOrderItemType type;

    TAOrderItem(String paramString1, String paramString2, String paramTAOrderItemType, OrderDetailAdapter.TAOrderItemType arg5)
    {
      this.title = paramString1;
      this.subTitle = paramString2;
      this.count = paramTAOrderItemType;
      Object localObject;
      this.type = localObject;
    }
  }

  private static enum TAOrderItemType
  {
    static
    {
      ORDER_ACTIVITY = new TAOrderItemType("ORDER_ACTIVITY", 2);
      ORDER_FEE = new TAOrderItemType("ORDER_FEE", 3);
      $VALUES = new TAOrderItemType[] { ORDER_DISCOUNT, ORDER_ITEM, ORDER_ACTIVITY, ORDER_FEE };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.OrderDetailAdapter
 * JD-Core Version:    0.6.0
 */