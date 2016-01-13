package com.dianping.selectdish.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.app.DPActivity;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.base.widget.PowerView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.selectdish.DishLikeManager;
import com.dianping.selectdish.animation.SelectDishCartAnimationManager;
import com.dianping.selectdish.model.DishInfo;
import com.dianping.selectdish.model.GiftInfo;
import com.dianping.selectdish.model.RecommendInfo;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.string;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;

public class SelectDishMenuItem extends LinearLayout
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private View cart;
  private View cartView;
  private TextView count;
  private DishLikeManager dishLikeManager = DishLikeManager.getInstance();
  private TextView event;
  private GAUserInfo gaUserInfo = new GAUserInfo();
  private ImageView hasBoughtView;
  private int mode;
  private TextView name;
  private NetworkImageView photo;
  private RMBLabelItem price;
  private TextView recommend;
  private View recommendLayout;
  private int shopId;
  private TextView soldstatus;
  private PowerView spicy;
  private View statusView;
  private TextView summary;
  private TextView tag;

  public SelectDishMenuItem(Context paramContext)
  {
    super(paramContext);
  }

  public SelectDishMenuItem(Context paramContext, AttributeSet paramAttributeSet)
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
    this.photo = ((NetworkImageView)findViewById(R.id.sd_menuitem_photo));
    this.statusView = findViewById(R.id.sd_menuitem_status);
    this.recommendLayout = findViewById(R.id.sd_menuitem_recommend_layout);
    this.recommend = ((TextView)findViewById(R.id.sd_menuitem_recommend));
    this.name = ((TextView)findViewById(R.id.sd_menuitem_name));
    this.spicy = ((PowerView)findViewById(R.id.sd_menuitem_spicy));
    this.event = ((TextView)findViewById(R.id.sd_menuitem_event));
    this.tag = ((TextView)findViewById(R.id.sd_menuitem_tag));
    this.summary = ((TextView)findViewById(R.id.sd_menuitem_summary));
    this.price = ((RMBLabelItem)findViewById(R.id.sd_menuitem_price));
    this.cart = findViewById(R.id.sd_menuitem_cart);
    this.cartView = findViewById(R.id.addcart);
    this.count = ((TextView)findViewById(R.id.sd_menuitem_count));
    this.soldstatus = ((TextView)findViewById(R.id.sd_menuitem_sold_status));
    this.hasBoughtView = ((ImageView)findViewById(R.id.sd_has_bought));
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void setCartandRecommendWidth(int paramInt1, int paramInt2)
  {
    ViewGroup.LayoutParams localLayoutParams3 = this.recommendLayout.getLayoutParams();
    ViewGroup.LayoutParams localLayoutParams2 = this.cart.getLayoutParams();
    ViewGroup.LayoutParams localLayoutParams1 = this.cartView.getLayoutParams();
    if (localLayoutParams3 != null)
    {
      localLayoutParams3.width = paramInt1;
      localLayoutParams3.height = paramInt2;
      if (localLayoutParams2 == null)
        break label118;
      localLayoutParams2.width = paramInt1;
      localLayoutParams2.height = paramInt2;
    }
    while (true)
    {
      if (localLayoutParams1 == null)
        break label141;
      localLayoutParams1.width = (ViewUtils.dip2px(getContext(), 6.0F) + paramInt1);
      localLayoutParams1.height = (ViewUtils.dip2px(getContext(), 5.0F) + paramInt2);
      return;
      localLayoutParams3 = new ViewGroup.LayoutParams(paramInt1, paramInt2);
      this.recommendLayout.setLayoutParams(localLayoutParams3);
      break;
      label118: localLayoutParams2 = new ViewGroup.LayoutParams(paramInt1, paramInt2);
      this.cart.setLayoutParams(localLayoutParams2);
    }
    label141: localLayoutParams1 = new ViewGroup.LayoutParams(ViewUtils.dip2px(getContext(), 6.0F) + paramInt1, ViewUtils.dip2px(getContext(), 5.0F) + paramInt2);
    this.cartView.setLayoutParams(localLayoutParams1);
  }

  public void setCount(int paramInt)
  {
    if (paramInt <= 0)
    {
      this.count.setVisibility(4);
      return;
    }
    this.count.setText(String.valueOf(paramInt));
    this.count.setVisibility(0);
  }

  public void setData(DishInfo paramDishInfo)
  {
    this.photo.setImage(paramDishInfo.url);
    Object localObject = this.dishLikeManager.getRecommendInfo(paramDishInfo.dishId);
    if (localObject != null)
    {
      setRecommend((RecommendInfo)localObject);
      if (this.recommendLayout != null)
      {
        View localView = findViewById(R.id.sd_menuitem_recommend_add);
        this.recommendLayout.setVisibility(0);
        findViewById(R.id.sd_menuitem_recommend_press_view).setOnClickListener(new View.OnClickListener((RecommendInfo)localObject, localView)
        {
          public void onClick(View paramView)
          {
            if (((DPActivity)SelectDishMenuItem.this.getContext()).accountService().token() != null)
            {
              if (this.val$recommendInfo.isRecommended)
                DishLikeManager.deleteDishLike((NovaActivity)SelectDishMenuItem.this.getContext(), SelectDishMenuItem.this.shopId, this.val$recommendInfo, SelectDishMenuItem.this);
              while (true)
              {
                SelectDishMenuItem.this.setRecommend(this.val$recommendInfo);
                return;
                DishLikeManager.addDishLike((NovaActivity)SelectDishMenuItem.this.getContext(), SelectDishMenuItem.this.shopId, this.val$recommendInfo, SelectDishMenuItem.this);
                SelectDishCartAnimationManager.startAddRecommendAnimation(this.val$recommendAddImageView);
              }
            }
            ((DPActivity)SelectDishMenuItem.this.getContext()).accountService().login(new LoginResultListener()
            {
              public void onLoginCancel(AccountService paramAccountService)
              {
              }

              public void onLoginSuccess(AccountService paramAccountService)
              {
                DishLikeManager.addDishLike((NovaActivity)SelectDishMenuItem.this.getContext(), SelectDishMenuItem.this.shopId, SelectDishMenuItem.1.this.val$recommendInfo, SelectDishMenuItem.this);
                SelectDishCartAnimationManager.startAddRecommendAnimation(SelectDishMenuItem.1.this.val$recommendAddImageView);
                SelectDishMenuItem.this.setRecommend(SelectDishMenuItem.1.this.val$recommendInfo);
              }
            });
          }
        });
      }
    }
    this.name.setText(paramDishInfo.name);
    this.spicy.setPower(paramDishInfo.spicy);
    this.tag.setVisibility(8);
    localObject = new StringBuilder("");
    if (paramDishInfo.dishType == 1)
      ((StringBuilder)localObject).append("套餐");
    label224: int i;
    label273: label329: label344: label365: int k;
    label386: label408: int j;
    int m;
    int n;
    if ((paramDishInfo.freeItem != null) && (paramDishInfo.targetCount != 0) && (paramDishInfo.freeCount != 0))
      if (paramDishInfo.freeItem.dishId != paramDishInfo.dishId)
      {
        ((StringBuilder)localObject).append("满" + String.valueOf(paramDishInfo.targetCount) + "送礼");
        if (!TextUtils.isEmpty(((StringBuilder)localObject).toString()))
          break label704;
        this.event.setVisibility(8);
        this.summary.setVisibility(0);
        if (paramDishInfo.isValidity)
          break label726;
        this.summary.setText(getResources().getString(R.string.sd_sold_out_time));
        this.summary.setTextColor(getResources().getColor(R.color.light_gray));
        if ((paramDishInfo.oldPrice == 0.0D) || (paramDishInfo.oldPrice <= paramDishInfo.currentPrice))
          break label900;
        this.price.setRMBLabelStyle(2, 3, false, getResources().getColor(R.color.light_red));
        this.price.setRMBLabelValue(paramDishInfo.currentPrice, paramDishInfo.oldPrice);
        localObject = this.soldstatus;
        if (!paramDishInfo.soldout)
          break label934;
        i = 0;
        ((TextView)localObject).setVisibility(i);
        localObject = this.cart;
        if (!paramDishInfo.soldout)
          break label941;
        i = 4;
        ((View)localObject).setVisibility(i);
        localObject = this.count;
        if (!paramDishInfo.soldout)
          break label947;
        i = 4;
        ((TextView)localObject).setVisibility(i);
        if (paramDishInfo.hasbought != 1)
          break label953;
        this.hasBoughtView.setVisibility(0);
        if (this.mode == 0)
        {
          k = View.MeasureSpec.makeMeasureSpec(0, 0);
          i = getViewWidth(this.price, k);
          j = getViewWidth(this.event, k);
          k = getViewWidth(this.summary, k);
          m = (ViewUtils.getScreenWidthPixels(getContext()) - ViewUtils.dip2px(getContext(), 22.0F)) / 2;
          n = ViewUtils.dip2px(getContext(), 22.0F);
          if (m - (i + n + ViewUtils.dip2px(getContext(), 3.0F)) >= ViewUtils.dip2px(getContext(), 3.0F))
            break label984;
          this.event.setVisibility(8);
          this.summary.setVisibility(8);
        }
      }
    while (true)
    {
      if ((this.event.getVisibility() != 8) || (this.summary.getVisibility() != 8) || (this.tag.getVisibility() != 8))
        break label1112;
      this.statusView.setVisibility(8);
      return;
      ((StringBuilder)localObject).append("满" + String.valueOf(paramDishInfo.targetCount) + "送" + String.valueOf(paramDishInfo.freeCount));
      break;
      if ((paramDishInfo.freeRuleType == 2) || ((paramDishInfo.dishType == 1) && (paramDishInfo.isDiscountSetMeal)))
      {
        ((StringBuilder)localObject).append("特价");
        break;
      }
      if (TextUtils.isEmpty(paramDishInfo.tag))
        break;
      this.tag.setText(paramDishInfo.tag);
      this.tag.setVisibility(0);
      break;
      label704: this.event.setText(((StringBuilder)localObject).toString());
      this.event.setVisibility(0);
      break label224;
      label726: if (paramDishInfo.giftCount > 0)
      {
        this.summary.setText(getResources().getString(R.string.sd_summary_free).replace("%s", String.valueOf(paramDishInfo.giftCount)) + paramDishInfo.unit);
        this.summary.setTextColor(getResources().getColor(R.color.light_red));
        break label273;
      }
      if (paramDishInfo.bought > 0)
      {
        this.summary.setText(getResources().getString(R.string.sd_summary_bought).replace("%s", String.valueOf(paramDishInfo.bought)) + paramDishInfo.unit);
        this.summary.setTextColor(getResources().getColor(R.color.light_gray));
        break label273;
      }
      this.summary.setText(null);
      this.summary.setVisibility(8);
      break label273;
      label900: this.price.setRMBLabelStyle(2, 2, false, getResources().getColor(R.color.light_red));
      this.price.setRMBLabelValue(paramDishInfo.currentPrice);
      break label329;
      label934: i = 8;
      break label344;
      label941: i = 0;
      break label365;
      label947: i = 0;
      break label386;
      label953: if (paramDishInfo.hasbought == 2)
      {
        this.hasBoughtView.setVisibility(0);
        break label408;
      }
      this.hasBoughtView.setVisibility(8);
      break label408;
      label984: if (m - (i + n + ViewUtils.dip2px(getContext(), 3.0F) + ViewUtils.dip2px(getContext(), 3.0F)) < ViewUtils.dip2px(getContext(), 10.0F) + j)
      {
        this.event.setVisibility(8);
        this.summary.setVisibility(8);
        continue;
      }
      if (m - (i + n + ViewUtils.dip2px(getContext(), 3.0F) + ViewUtils.dip2px(getContext(), 3.0F) + j + ViewUtils.dip2px(getContext(), 10.0F)) >= k)
        continue;
      this.summary.setVisibility(8);
    }
    label1112: this.statusView.setVisibility(0);
  }

  public void setMode(int paramInt)
  {
    this.mode = paramInt;
  }

  public void setPhotoSize(int paramInt1, int paramInt2)
  {
    ViewGroup.LayoutParams localLayoutParams = this.photo.getLayoutParams();
    if (localLayoutParams != null)
    {
      localLayoutParams.width = paramInt1;
      localLayoutParams.height = paramInt2;
      return;
    }
    localLayoutParams = new ViewGroup.LayoutParams(paramInt1, paramInt2);
    this.photo.setLayoutParams(localLayoutParams);
  }

  public void setRecommend(RecommendInfo paramRecommendInfo)
  {
    if (paramRecommendInfo.isRecommended)
    {
      GAHelper.instance().contextStatisticsEvent(getContext(), "recommend_add", this.gaUserInfo, "tap");
      this.recommend.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.select_dish_hasthumbup_icon), null, null, null);
    }
    while (true)
    {
      this.recommend.setCompoundDrawablePadding(ViewUtils.dip2px(getContext(), 3.0F));
      if ((paramRecommendInfo.recommendNum == 0) || (paramRecommendInfo.recommendNum >= 10000))
        break;
      this.recommend.setText(String.valueOf(paramRecommendInfo.recommendNum));
      return;
      GAHelper.instance().contextStatisticsEvent(getContext(), "recommend_remove", this.gaUserInfo, "tap");
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
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.view.SelectDishMenuItem
 * JD-Core Version:    0.6.0
 */