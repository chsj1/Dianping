package com.dianping.takeaway.entity;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.base.widget.NumOperateButton;
import com.dianping.base.widget.NumOperateButton.NumOperateListener;
import com.dianping.takeaway.activity.TakeawayMenuActivity;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.OnLoadChangeListener;
import com.dianping.widget.view.GAHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TakeawayDishMenuAdapter extends BaseAdapter
  implements AdapterView.OnItemClickListener
{
  private static final int VIEW_CONTENT = 1;
  private static final int VIEW_TITLE = 0;
  private static final int VIEW_TYPE_COUNT = 2;
  private List<TakeawayDishInfo> data;
  private Dialog dishDetailInfoDialog;
  private TakeawayDishMenuDataSource dishMenuDataSource;
  public final boolean isRecommedDishList;
  public final boolean isSelectedDishList;
  private TakeawayMenuActivity menuActivity;

  public TakeawayDishMenuAdapter(TakeawayMenuActivity paramTakeawayMenuActivity, TakeawayDishMenuDataSource paramTakeawayDishMenuDataSource, boolean paramBoolean1, boolean paramBoolean2, List<TakeawayDishInfo> paramList)
  {
    this.menuActivity = paramTakeawayMenuActivity;
    this.isSelectedDishList = paramBoolean1;
    this.isRecommedDishList = paramBoolean2;
    this.dishMenuDataSource = paramTakeawayDishMenuDataSource;
    this.data = paramList;
    this.dishDetailInfoDialog = new Dialog(paramTakeawayMenuActivity, R.style.dialog);
  }

  private View getContentView(TakeawayDishInfo paramTakeawayDishInfo, int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    Object localObject;
    label49: int i;
    label80: TextView localTextView;
    if (paramView != null)
    {
      paramViewGroup = paramView.findViewById(R.id.little_img_view);
      localObject = (NetworkImageView)paramView.findViewById(R.id.little_img);
      if ((!this.isSelectedDishList) && (!TextUtils.isEmpty(paramTakeawayDishInfo.littleImageUrl)))
        break label314;
      paramViewGroup.setVisibility(8);
      paramViewGroup = (TextView)paramView.findViewById(R.id.menu_item_name);
      paramViewGroup.setText(paramTakeawayDishInfo.dishName);
      if (!this.isSelectedDishList)
        break label349;
      i = 2;
      paramViewGroup.setMaxLines(i);
      paramViewGroup = (RMBLabelItem)paramView.findViewById(R.id.menu_item_price);
      if (!paramTakeawayDishInfo.hasOldPrice())
        break label355;
      paramViewGroup.setRMBLabelStyle(2, 3, false, this.menuActivity.getResources().getColor(R.color.light_red));
      paramViewGroup.setRMBLabelValue(paramTakeawayDishInfo.curPrice, paramTakeawayDishInfo.originPrice);
      label140: localObject = paramView.findViewById(R.id.sold_count_layout);
      if ((this.isSelectedDishList) || (TextUtils.isEmpty(paramTakeawayDishInfo.salesVolume)))
        break label407;
      ((TextView)paramView.findViewById(R.id.sold_count)).setText(paramTakeawayDishInfo.salesVolume);
      localTextView = (TextView)paramView.findViewById(R.id.hot_num);
      if (paramTakeawayDishInfo.status != 1)
        break label397;
      if (paramTakeawayDishInfo.hotNum != null)
        break label388;
      paramViewGroup = "";
      label214: localTextView.setText(paramViewGroup);
      localTextView.setVisibility(0);
      label227: ((View)localObject).setVisibility(0);
    }
    while (true)
    {
      inflateOperationView(paramTakeawayDishInfo, paramView, paramInt);
      paramViewGroup = paramView.findViewById(R.id.desp_divider);
      localObject = (TextView)paramView.findViewById(R.id.dish_desp);
      if ((!this.isSelectedDishList) && (!TextUtils.isEmpty(paramTakeawayDishInfo.dishIntro)))
        break label416;
      paramViewGroup.setVisibility(8);
      ((TextView)localObject).setVisibility(8);
      return paramView;
      paramView = LayoutInflater.from(this.menuActivity).inflate(R.layout.takeaway_menu_item, paramViewGroup, false);
      break;
      label314: if (!paramViewGroup.isShown())
        paramViewGroup.setVisibility(0);
      ((NetworkImageView)localObject).setTag(paramTakeawayDishInfo.littleImageUrl);
      ((NetworkImageView)localObject).setImage(paramTakeawayDishInfo.littleImageUrl);
      break label49;
      label349: i = 4;
      break label80;
      label355: paramViewGroup.setRMBLabelStyle(2, 2, false, this.menuActivity.getResources().getColor(R.color.light_red));
      paramViewGroup.setRMBLabelValue(paramTakeawayDishInfo.curPrice);
      break label140;
      label388: paramViewGroup = paramTakeawayDishInfo.hotNum;
      break label214;
      label397: localTextView.setVisibility(8);
      break label227;
      label407: ((View)localObject).setVisibility(4);
    }
    label416: paramViewGroup.setVisibility(0);
    ((TextView)localObject).setVisibility(0);
    ((TextView)localObject).setText(paramTakeawayDishInfo.dishIntro);
    return (View)paramView;
  }

  private View getTitleView(TakeawayDishInfo paramTakeawayDishInfo, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView != null);
    while (true)
    {
      ((TextView)paramView.findViewById(R.id.menu_category_name)).setText(paramTakeawayDishInfo.categoryName);
      return paramView;
      paramView = LayoutInflater.from(this.menuActivity).inflate(R.layout.takeaway_menu_title_item, paramViewGroup, false);
    }
  }

  private void inflateOperationView(TakeawayDishInfo paramTakeawayDishInfo, View paramView, int paramInt)
  {
    TextView localTextView = (TextView)paramView.findViewById(R.id.menu_soldout);
    localTextView.setTextColor(this.menuActivity.getResources().getColor(R.color.text_hint_light_gray));
    paramView = (NumOperateButton)paramView.findViewById(R.id.operateButton);
    if (this.dishMenuDataSource.mShopInfo.isAvailable)
    {
      if ((!paramTakeawayDishInfo.isSoldout) && (!paramTakeawayDishInfo.isInShortSupply()))
      {
        localTextView.setVisibility(8);
        paramView.setVisibility(0);
        paramView.setNumOperateListener(new NumOperateButton.NumOperateListener(paramView)
        {
          public void addResult(boolean paramBoolean, int paramInt)
          {
            TakeawayDishMenuAdapter.this.menuActivity.addClickListener.onClick(this.val$operateButton);
          }

          public void subtractResult(boolean paramBoolean, int paramInt)
          {
            TakeawayDishMenuAdapter.this.menuActivity.removeClickListener.onClick(this.val$operateButton);
          }
        });
        paramView.setCurrentValue(paramTakeawayDishInfo.selectedNum);
        paramView.setTag(new DishOperation(paramView, paramTakeawayDishInfo, this));
        return;
      }
      paramView.setVisibility(8);
      localTextView.setVisibility(0);
      if (paramTakeawayDishInfo.isInShortSupply());
      for (paramTakeawayDishInfo = paramTakeawayDishInfo.tip; ; paramTakeawayDishInfo = "已售完")
      {
        localTextView.setText(paramTakeawayDishInfo);
        return;
      }
    }
    if ((!paramTakeawayDishInfo.isSoldout) && (!paramTakeawayDishInfo.isInShortSupply()))
    {
      paramView.setEnabled(false);
      paramView.setVisibility(0);
      localTextView.setVisibility(8);
      return;
    }
    paramView.setVisibility(8);
    localTextView.setTextColor(1724697804);
    localTextView.setVisibility(0);
    if (paramTakeawayDishInfo.isInShortSupply());
    for (paramTakeawayDishInfo = paramTakeawayDishInfo.tip; ; paramTakeawayDishInfo = "已售完")
    {
      localTextView.setText(paramTakeawayDishInfo);
      return;
    }
  }

  public boolean areAllItemsEnabled()
  {
    return false;
  }

  public DPObject getContents()
  {
    Object localObject = new StringBuilder();
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.data.iterator();
    while (localIterator.hasNext())
    {
      TakeawayDishInfo localTakeawayDishInfo = (TakeawayDishInfo)localIterator.next();
      if (!localTakeawayDishInfo.hasOrder())
        continue;
      ((StringBuilder)localObject).append(localTakeawayDishInfo.id).append(",").append(localTakeawayDishInfo.selectedNum).append("|");
      localArrayList.add(new DPObject().edit().putString("Title", localTakeawayDishInfo.dishName).putDouble("Price", localTakeawayDishInfo.curPrice * localTakeawayDishInfo.selectedNum).putInt("Count", localTakeawayDishInfo.selectedNum).generate());
    }
    if ((((StringBuilder)localObject).length() > 0) && (((StringBuilder)localObject).charAt(((StringBuilder)localObject).length() - 1) == '|'));
    for (localObject = ((StringBuilder)localObject).substring(0, ((StringBuilder)localObject).length() - 1); ; localObject = ((StringBuilder)localObject).toString())
      return new DPObject().edit().putString("server", (String)localObject).putArray("page", (DPObject[])localArrayList.toArray(new DPObject[localArrayList.size()])).generate();
  }

  public int getCount()
  {
    return this.data.size();
  }

  public Object getItem(int paramInt)
  {
    if ((this.data != null) && (!this.data.isEmpty()))
      return (TakeawayDishInfo)this.data.get(paramInt);
    return null;
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public int getItemViewType(int paramInt)
  {
    if (((TakeawayDishInfo)this.data.get(paramInt)).isTitle)
      return 0;
    return 1;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    TakeawayDishInfo localTakeawayDishInfo = (TakeawayDishInfo)this.data.get(paramInt);
    if (getItemViewType(paramInt) == 0)
      return getTitleView(localTakeawayDishInfo, paramView, paramViewGroup);
    return getContentView(localTakeawayDishInfo, paramInt, paramView, paramViewGroup);
  }

  public int getViewTypeCount()
  {
    return 2;
  }

  public boolean isEnabled(int paramInt)
  {
    return !((TakeawayDishInfo)this.data.get(paramInt)).isTitle;
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if ((paramInt <= 0) || (paramInt > this.data.size()));
    do
    {
      return;
      paramAdapterView = (TakeawayDishInfo)this.data.get(paramInt);
    }
    while ((paramAdapterView == null) || (TextUtils.isEmpty(paramAdapterView.bigImageUrl)));
    paramView = LayoutInflater.from(this.menuActivity).inflate(R.layout.takeaway_menu_item_detail_info_dialog, null, false);
    this.dishDetailInfoDialog.setContentView(paramView);
    this.dishDetailInfoDialog.setCanceledOnTouchOutside(true);
    ((ImageView)paramView.findViewById(R.id.image_close)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        TakeawayDishMenuAdapter.this.dishDetailInfoDialog.cancel();
      }
    });
    Object localObject = (ProgressBar)paramView.findViewById(R.id.progress_img);
    ((ProgressBar)localObject).setVisibility(0);
    NetworkImageView localNetworkImageView = (NetworkImageView)paramView.findViewById(R.id.normal_img);
    View localView = paramView.findViewById(R.id.error_img);
    localView.setVisibility(8);
    localNetworkImageView.setLoadChangeListener(new OnLoadChangeListener((ProgressBar)localObject, localView)
    {
      public void onImageLoadFailed()
      {
        this.val$errorView.setVisibility(0);
      }

      public void onImageLoadStart()
      {
      }

      public void onImageLoadSuccess(Bitmap paramBitmap)
      {
        this.val$progessView.setVisibility(8);
      }
    });
    localNetworkImageView.setImage(paramAdapterView.bigImageUrl);
    ((TextView)paramView.findViewById(R.id.dish_name)).setText(paramAdapterView.dishName);
    localObject = (RMBLabelItem)paramView.findViewById(R.id.detail_price);
    if (paramAdapterView.hasOldPrice())
    {
      ((RMBLabelItem)localObject).setRMBLabelStyle(3, 3, false, this.menuActivity.getResources().getColor(R.color.light_red));
      ((RMBLabelItem)localObject).setRMBLabelValue(paramAdapterView.curPrice, paramAdapterView.originPrice);
    }
    while (true)
    {
      inflateOperationView(paramAdapterView, paramView, paramInt);
      ViewUtils.setVisibilityAndContent((EditText)paramView.findViewById(R.id.dish_detail_intro), paramAdapterView.dishIntro);
      this.dishDetailInfoDialog.show();
      this.menuActivity.statisticsEvent("takeaway6", "takeaway6_dish_dishdetail", "", 0);
      GAHelper.instance().contextStatisticsEvent(this.menuActivity, "bigphoto", null, "tap");
      return;
      ((RMBLabelItem)localObject).setRMBLabelStyle(3, 2, false, this.menuActivity.getResources().getColor(R.color.light_red));
      ((RMBLabelItem)localObject).setRMBLabelValue(paramAdapterView.curPrice);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayDishMenuAdapter
 * JD-Core Version:    0.6.0
 */