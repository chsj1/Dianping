package com.dianping.selectdish.view;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.app.DPActivity;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.PowerView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.selectdish.DishLikeManager;
import com.dianping.selectdish.NumberUtils;
import com.dianping.selectdish.model.DishInfo;
import com.dianping.selectdish.model.RecommendInfo;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.string;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaFrameLayout;

public class SelectDishDetailBuyItem extends RelativeLayout
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private NovaFrameLayout buy;
  private ImageView cartImageView;
  private TextView count;
  private TextView currentPrice;
  private DishInfo dishData;
  private DishLikeManager dishLikeManager = DishLikeManager.getInstance();
  private GAUserInfo gaUserInfo = new GAUserInfo();
  private String mBuyTag;
  private TextView mCartCountView;
  private View.OnClickListener mListener;
  private TextView name;
  private LinearLayout nameLayout;
  private TextView oldPriceTextView;
  private TextView recommend;
  private View recommendLayout;
  private int shopId;
  private TextView soldStatusTextView;
  private PowerView spicyPower;
  private TextView thumbsUpCount;

  public SelectDishDetailBuyItem(Context paramContext)
  {
    super(paramContext);
  }

  public SelectDishDetailBuyItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private int getViewWidth(View paramView, int paramInt)
  {
    paramView.measure(paramInt, paramInt);
    return paramView.getMeasuredWidth();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.buy = ((NovaFrameLayout)findViewById(R.id.addcart));
    this.recommendLayout = findViewById(R.id.sd_menuitem_recommend_layout);
    this.recommend = ((TextView)findViewById(R.id.sd_menuitem_recommend));
    this.cartImageView = ((ImageView)findViewById(R.id.selected_dish_cart));
    this.mCartCountView = ((TextView)findViewById(R.id.sd_detail_count));
    this.soldStatusTextView = ((TextView)findViewById(R.id.sd_detail_sold_status));
    this.nameLayout = ((LinearLayout)findViewById(R.id.sd_detail_name));
    this.name = ((TextView)findViewById(R.id.selected_dish_name));
    this.spicyPower = ((PowerView)findViewById(R.id.sd_detail_spicy));
    this.count = ((TextView)findViewById(R.id.selected_dish_count));
    this.thumbsUpCount = ((TextView)findViewById(R.id.selected_dish_thumbs_up));
    this.currentPrice = ((TextView)findViewById(R.id.selected_dish_current_price));
    this.oldPriceTextView = ((TextView)findViewById(R.id.selected_dish_old_price));
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void setData(DishInfo paramDishInfo)
  {
    this.dishData = paramDishInfo;
    Object localObject = this.dishLikeManager.getRecommendInfo(this.dishData.dishId);
    if (localObject != null)
    {
      setRecommend((RecommendInfo)localObject);
      findViewById(R.id.sd_menuitem_recommend_add);
      this.recommendLayout.setVisibility(0);
      findViewById(R.id.sd_menuitem_recommend_press_view).setOnClickListener(new View.OnClickListener((RecommendInfo)localObject, paramDishInfo)
      {
        public void onClick(View paramView)
        {
          if (((DPActivity)SelectDishDetailBuyItem.this.getContext()).accountService().token() != null)
          {
            if (this.val$recommendInfo.isRecommended)
            {
              DishLikeManager.deleteDishLike((NovaActivity)SelectDishDetailBuyItem.this.getContext(), SelectDishDetailBuyItem.this.shopId, this.val$recommendInfo, SelectDishDetailBuyItem.this);
              GAHelper.instance().contextStatisticsEvent(SelectDishDetailBuyItem.this.getContext(), "recommend_remove", SelectDishDetailBuyItem.this.gaUserInfo, "tap");
            }
            while (true)
            {
              SelectDishDetailBuyItem.this.setRecommend(this.val$recommendInfo);
              DishLikeManager.sendBroadCast(this.val$dishInfo, (NovaActivity)SelectDishDetailBuyItem.this.getContext());
              return;
              DishLikeManager.addDishLike((NovaActivity)SelectDishDetailBuyItem.this.getContext(), SelectDishDetailBuyItem.this.shopId, this.val$recommendInfo, SelectDishDetailBuyItem.this);
              GAHelper.instance().contextStatisticsEvent(SelectDishDetailBuyItem.this.getContext(), "recommend_add", SelectDishDetailBuyItem.this.gaUserInfo, "tap");
            }
          }
          ((DPActivity)SelectDishDetailBuyItem.this.getContext()).accountService().login(new LoginResultListener()
          {
            public void onLoginCancel(AccountService paramAccountService)
            {
            }

            public void onLoginSuccess(AccountService paramAccountService)
            {
              DishLikeManager.addDishLike((NovaActivity)SelectDishDetailBuyItem.this.getContext(), SelectDishDetailBuyItem.this.shopId, SelectDishDetailBuyItem.1.this.val$recommendInfo, SelectDishDetailBuyItem.this);
              SelectDishDetailBuyItem.this.setRecommend(SelectDishDetailBuyItem.1.this.val$recommendInfo);
              DishLikeManager.sendBroadCast(SelectDishDetailBuyItem.1.this.val$dishInfo, (NovaActivity)SelectDishDetailBuyItem.this.getContext());
            }
          });
        }
      });
    }
    while (true)
    {
      this.name.setText(this.dishData.name);
      if (this.dishData.spicy > 0)
      {
        this.spicyPower.setVisibility(0);
        this.spicyPower.setPower(this.dishData.spicy);
      }
      if ((this.dishData.tags == null) || (this.dishData.tags.length == 0))
        break;
      int k = View.MeasureSpec.makeMeasureSpec(0, 0);
      int i = getViewWidth(this.cartImageView, k);
      int j = ViewUtils.getScreenWidthPixels(getContext());
      int m = ViewUtils.dip2px(getContext(), 15.0F);
      int n = getViewWidth(this.name, k);
      int i1 = getViewWidth(this.spicyPower, k);
      j = j - i - m * 2 - n - this.dishData.spicy * i1;
      if (j <= 0)
        break;
      i = 0;
      while (i < this.dishData.tags.length)
      {
        paramDishInfo = new TextView(getContext());
        localObject = new LinearLayout.LayoutParams(-2, -2);
        ((LinearLayout.LayoutParams)localObject).leftMargin = ViewUtils.dip2px(getContext(), 6.0F);
        ((LinearLayout.LayoutParams)localObject).gravity = 16;
        paramDishInfo.setLayoutParams((ViewGroup.LayoutParams)localObject);
        paramDishInfo.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_orange_promotion));
        paramDishInfo.setTextColor(getResources().getColor(R.color.light_line_red));
        paramDishInfo.setTextSize(9.0F);
        paramDishInfo.setText(this.dishData.tags[i]);
        if (j < getViewWidth(paramDishInfo, k) + ViewUtils.dip2px(getContext(), 6.0F))
          break;
        this.nameLayout.addView(paramDishInfo);
        j = j - getViewWidth(paramDishInfo, k) - ViewUtils.dip2px(getContext(), 6.0F);
        i += 1;
      }
      this.recommendLayout.setVisibility(8);
    }
    if (!TextUtils.isEmpty(this.dishData.sales))
    {
      this.count.setVisibility(0);
      this.count.setText(this.dishData.sales);
    }
    if (this.dishData.recommend > 0)
    {
      this.thumbsUpCount.setVisibility(0);
      this.thumbsUpCount.setText(" " + this.dishData.recommend + getContext().getString(R.string.sd_recommend_person));
    }
    this.currentPrice.setText(getContext().getString(R.string.sd_price).replace("%s", NumberUtils.convertDoubleToIntegerIfNecessary(this.dishData.currentPrice)));
    if (this.dishData.oldPrice > this.dishData.currentPrice)
    {
      this.oldPriceTextView.setVisibility(0);
      this.oldPriceTextView.setText(getContext().getString(R.string.sd_price).replace("%s", NumberUtils.convertDoubleToIntegerIfNecessary(this.dishData.oldPrice)));
      this.oldPriceTextView.getPaint().setFlags(16);
    }
    if (this.dishData.soldout)
    {
      this.mCartCountView.setVisibility(4);
      this.soldStatusTextView.setVisibility(0);
      this.soldStatusTextView.setText(R.string.select_sold_out);
    }
    while (true)
    {
      this.mCartCountView.setTag(this.mBuyTag);
      return;
      this.cartImageView.setVisibility(0);
      this.buy.setOnClickListener(this.mListener);
    }
  }

  public void setRecommend(RecommendInfo paramRecommendInfo)
  {
    if (paramRecommendInfo.isRecommended)
      this.recommend.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.select_dish_hasthumbup_icon), null, null, null);
    while (true)
    {
      this.recommend.setCompoundDrawablePadding(ViewUtils.dip2px(getContext(), 3.0F));
      if ((paramRecommendInfo.recommendNum == 0) || (paramRecommendInfo.recommendNum >= 10000))
        break;
      this.recommend.setText(String.valueOf(paramRecommendInfo.recommendNum));
      return;
      this.recommend.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.selectdish_thumbup_icon), null, null, null);
    }
    if (paramRecommendInfo.recommendNum >= 10000)
    {
      this.recommend.setText("9999");
      return;
    }
    this.recommend.setText("");
    this.recommend.setCompoundDrawablePadding(0);
  }

  public void setShopId(int paramInt)
  {
    this.shopId = paramInt;
  }

  public void setmBuyTag(String paramString)
  {
    this.mBuyTag = paramString;
  }

  public void setmListener(View.OnClickListener paramOnClickListener)
  {
    this.mListener = paramOnClickListener;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.view.SelectDishDetailBuyItem
 * JD-Core Version:    0.6.0
 */