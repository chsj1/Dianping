package com.dianping.takeaway.entity;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.shoplist.ShopListAdapter;
import com.dianping.base.shoplist.ShopListAdapter.ShopListReloadHandler;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.takeaway.view.TAStarView;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;

public class TakeawayListAdapter extends ShopListAdapter
{
  private static Object LISTITEM = new Object();
  protected Context mContext;

  public TakeawayListAdapter(Context paramContext, ShopListAdapter.ShopListReloadHandler paramShopListReloadHandler)
  {
    super(paramShopListReloadHandler);
    this.mContext = paramContext;
  }

  protected void bindView(View paramView, DPObject paramDPObject, boolean paramBoolean)
  {
    Object localObject1 = (NetworkImageView)paramView.findViewById(R.id.takeaway_shop_image);
    Object localObject2 = paramDPObject.getString("DefaultPic");
    label91: int j;
    if ((paramBoolean) && (!TextUtils.isEmpty((CharSequence)localObject2)))
    {
      ((NetworkImageView)localObject1).setImage((String)localObject2);
      ((NetworkImageView)localObject1).setBackgroundResource(R.drawable.shop_item_img_border);
      i = ViewUtils.dip2px(this.mContext, 1.0F);
      ((NetworkImageView)localObject1).setPadding(i, i, i, i);
      localObject1 = paramView.findViewById(R.id.brand_icon);
      if (!paramDPObject.getBoolean("IsBrand"))
        break label310;
      i = 0;
      ((View)localObject1).setVisibility(i);
      localObject1 = (TextView)paramView.findViewById(R.id.takeaway_shop_name);
      ViewUtils.setVisibilityAndContent((TextView)localObject1, paramDPObject.getString("Name"));
      localObject2 = new LinearLayout.LayoutParams(-2, -2);
      ((LinearLayout.LayoutParams)localObject2).weight = 1.0F;
      ((TextView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
      localObject1 = "";
      localObject3 = "";
      localObject2 = "";
      localObject7 = paramDPObject.getArray("ExtraServices");
      localObject4 = localObject3;
      localObject5 = localObject2;
      localObject6 = localObject1;
      if (localObject7 == null)
        break label338;
      localObject4 = localObject3;
      localObject5 = localObject2;
      localObject6 = localObject1;
      if (localObject7.length == 0)
        break label338;
      j = localObject7.length;
      i = 0;
      label210: localObject4 = localObject3;
      localObject5 = localObject2;
      localObject6 = localObject1;
      if (i >= j)
        break label338;
      localObject5 = localObject7[i];
      localObject4 = ((DPObject)localObject5).getString("Message");
      switch (((DPObject)localObject5).getInt("Type"))
      {
      default:
      case 1:
      case 2:
      case 3:
      }
    }
    while (true)
    {
      i += 1;
      break label210;
      ((NetworkImageView)localObject1).setLocalDrawable(this.mContext.getResources().getDrawable(R.drawable.placeholder_empty));
      break;
      label310: i = 8;
      break label91;
      localObject1 = localObject4;
      continue;
      localObject3 = localObject4;
      continue;
      localObject2 = localObject4;
    }
    label338: ViewUtils.setVisibilityAndContent((TextView)paramView.findViewById(R.id.onlinepay_icon), (String)localObject6);
    ViewUtils.setVisibilityAndContent((TextView)paramView.findViewById(R.id.compensate_icon), (String)localObject4);
    ViewUtils.setVisibilityAndContent((TextView)paramView.findViewById(R.id.invoice_icon), (String)localObject5);
    ((TAStarView)paramView.findViewById(R.id.star)).setScore(paramDPObject.getInt("Power"));
    ViewUtils.setVisibilityAndContent((TextView)paramView.findViewById(R.id.sold_count), paramDPObject.getString("SoldCount"));
    ViewUtils.setVisibilityAndContent((TextView)paramView.findViewById(R.id.takeaway_shop_distance), paramDPObject.getString("Distance"));
    Object localObject7 = paramDPObject.getStringArray("ShowText");
    Object localObject6 = "";
    localObject1 = "";
    Object localObject5 = "";
    Object localObject3 = localObject6;
    Object localObject4 = localObject5;
    localObject2 = localObject1;
    if (localObject7 != null)
    {
      localObject3 = localObject6;
      localObject4 = localObject5;
      localObject2 = localObject1;
      if (localObject7.length != 0)
      {
        localObject6 = localObject7[0];
        if (localObject7.length > 1)
          localObject1 = localObject7[1];
        localObject3 = localObject6;
        localObject4 = localObject5;
        localObject2 = localObject1;
        if (localObject7.length > 2)
        {
          localObject4 = localObject7[2];
          localObject2 = localObject1;
          localObject3 = localObject6;
        }
      }
    }
    ViewUtils.setVisibilityAndContent((TextView)paramView.findViewById(R.id.arrive_time), (String)localObject3);
    ViewUtils.setVisibilityAndContent((TextView)paramView.findViewById(R.id.min_fee), (String)localObject2);
    ViewUtils.setVisibilityAndContent((TextView)paramView.findViewById(R.id.delivery_fee), (String)localObject4);
    localObject2 = paramView.findViewById(R.id.shop_info_layout);
    localObject3 = (TextView)paramView.findViewById(R.id.rest_status);
    int i = paramDPObject.getInt("Status");
    switch (i)
    {
    default:
      ((TextView)localObject3).setVisibility(8);
      ((View)localObject2).setVisibility(0);
      paramView.setBackgroundResource(R.drawable.takeaway_list_item_bg);
      localObject1 = paramView.findViewById(R.id.takeaway_inner_divider);
      localObject2 = (LinearLayout)paramView.findViewById(R.id.activity_layout);
      paramDPObject = paramDPObject.getArray("Activities");
      if ((paramDPObject == null) || (paramDPObject.length == 0))
        break label989;
      ((LinearLayout)localObject2).removeAllViews();
      localObject3 = new LinearLayout.LayoutParams(-2, -2);
      ((LinearLayout.LayoutParams)localObject3).setMargins(0, ViewUtils.dip2px(this.mContext, 5.0F), 0, 0);
      localObject4 = new LinearLayout.LayoutParams(-2, -2);
      ((LinearLayout.LayoutParams)localObject4).gravity = 16;
      j = paramDPObject.length;
      i = 0;
      label763: if (i >= j)
        break label976;
      localObject5 = paramDPObject[i];
      if (localObject5 != null)
      {
        paramView = "";
        localObject6 = ((DPObject)localObject5).getObject("ActivityButton");
        if (localObject6 == null)
          break;
        if (((DPObject)localObject6).getString("Message") != null)
          break label965;
      }
    case 1:
    case 2:
    }
    label965: for (paramView = ""; ; paramView = ((DPObject)localObject6).getString("Message"))
    {
      localObject5 = ((DPObject)localObject5).getString("ActivityInfo");
      localObject6 = LayoutInflater.from(this.mContext).inflate(R.layout.takeaway_shop_item_activity_tag, null, false);
      localObject7 = (TextView)((View)localObject6).findViewById(R.id.tag_icon);
      ((TextView)localObject7).setText(paramView);
      ((TextView)localObject7).setLayoutParams((ViewGroup.LayoutParams)localObject4);
      paramView = (TextView)((View)localObject6).findViewById(R.id.tag_content);
      paramView.setSingleLine(true);
      paramView.setEllipsize(TextUtils.TruncateAt.END);
      paramView.setText((CharSequence)localObject5);
      ((View)localObject6).setLayoutParams((ViewGroup.LayoutParams)localObject3);
      ((LinearLayout)localObject2).addView((View)localObject6);
      i += 1;
      break label763;
      if (i == 1);
      for (localObject1 = "休息中"; ; localObject1 = "暂不接单")
      {
        ((TextView)localObject3).setText((CharSequence)localObject1);
        ((TextView)localObject3).setVisibility(0);
        ((View)localObject2).setVisibility(8);
        paramView.setBackgroundResource(R.drawable.takeaway_list_item_rest);
        break;
      }
    }
    label976: ((LinearLayout)localObject2).setVisibility(0);
    ((View)localObject1).setVisibility(0);
    return;
    label989: ((LinearLayout)localObject2).setVisibility(8);
    ((View)localObject1).setVisibility(8);
  }

  protected View getShopEmptyView(ViewGroup paramViewGroup, View paramView)
  {
    if ((paramView != null) && (paramView.getTag() == EMPTY))
      paramViewGroup = paramView;
    do
    {
      return paramViewGroup;
      paramView = LayoutInflater.from(this.mContext).inflate(R.layout.takeaway_shop_notfound_layout, paramViewGroup, false);
      paramView.setTag(EMPTY);
      paramViewGroup = paramView;
    }
    while (this.mDataSource == null);
    ViewUtils.setVisibilityAndContent((TextView)paramView.findViewById(R.id.titleText), ((TakeawaySampleShoplistDataSource)this.mDataSource).noShopNotiTitle);
    ViewUtils.setVisibilityAndContent((TextView)paramView.findViewById(R.id.detailText), ((TakeawaySampleShoplistDataSource)this.mDataSource).noShopNotiDetail);
    return paramView;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject = getItem(paramInt);
    if ((localObject instanceof DPObject))
    {
      DPObject localDPObject = (DPObject)localObject;
      if (paramView != null)
      {
        localObject = paramView;
        if (paramView.getTag() == LISTITEM);
      }
      else
      {
        localObject = LayoutInflater.from(this.mContext).inflate(R.layout.takeaway_shop_item, paramViewGroup, false);
        ((View)localObject).setTag(LISTITEM);
      }
      ((NovaLinearLayout)localObject).setGAString("shop");
      ((NovaLinearLayout)localObject).gaUserInfo.shop_id = Integer.valueOf(localDPObject.getInt("ID"));
      ((NovaLinearLayout)localObject).gaUserInfo.title = localDPObject.getString("Name");
      ((NovaLinearLayout)localObject).gaUserInfo.index = Integer.valueOf(paramInt);
      bindView((View)localObject, localDPObject, NovaConfigUtils.isShowImageInMobileNetwork());
      return localObject;
    }
    if (localObject == LOADING)
    {
      if ((this.mDataSource == null) || (this.mDataSource.nextStartIndex() != 0))
        this.reloadHandler.reload(false);
      return getLoadingView(paramViewGroup, paramView);
    }
    if (localObject == LAST_EXTRA)
      return this.lastExtraView;
    if (localObject == EMPTY)
      return getShopEmptyView(paramViewGroup, paramView);
    return (View)getFailedView(this.errorMsg, new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        TakeawayListAdapter.this.reloadHandler.reload(false);
      }
    }
    , paramViewGroup, paramView);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayListAdapter
 * JD-Core Version:    0.6.0
 */