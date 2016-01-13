package com.dianping.selectdish.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.TitleBar;
import com.dianping.base.widget.WordGroupLayout;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.selectdish.NewCartManager;
import com.dianping.selectdish.NewCartManager.CartChangedListener;
import com.dianping.selectdish.NumberUtils;
import com.dianping.selectdish.TogetherCartManager;
import com.dianping.selectdish.TogetherCartManager.TogetherCartChangedListener;
import com.dianping.selectdish.animation.SelectDishCartAnimationManager;
import com.dianping.selectdish.animation.SelectDishCartAnimationManager.AddDishAniListener;
import com.dianping.selectdish.model.CartFreeItem;
import com.dianping.selectdish.model.CartItem;
import com.dianping.selectdish.model.DishInfo;
import com.dianping.selectdish.model.GiftInfo;
import com.dianping.selectdish.view.SelectDishDetailBuyItem;
import com.dianping.selectdish.view.SelectDishMenuCartView;
import com.dianping.selectdish.view.SelectDishPagePortalPopupWindow;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.nineoldandroids.animation.Animator;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersListAdapterWrapper;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

public class SelectDishesDetailInfoActivity extends NovaActivity
  implements View.OnClickListener, AdapterView.OnItemClickListener, NewCartManager.CartChangedListener, RequestHandler<MApiRequest, MApiResponse>
{
  private SelectDishMenuCartView cartMenuInfoView;
  private GAUserInfo gaUserInfo = new GAUserInfo();
  private boolean isTogetherMenu;
  private ImageAdapter mAdapter;
  private View mBasicInfoRootView;
  private SelectDishDetailBuyItem mBuyLayout;
  private TextView mCartCountView;
  private WordGroupLayout mDishDescriptionView;
  private DishInfo mDishInfo;
  private DPObject mDpObjShop;
  private StickyGridHeadersGridView mGridView;
  private LinearLayout mHeardView;
  private TextView mReviewTitleView;
  private int mShopId;
  private String mShopName;
  private ArrayList<DPObject> mTagList;
  private SelectDishDetailBuyItem mTopBuyLayout;
  private StickyGridHeadersListAdapterWrapper mWrapper;
  private View morePortalView;
  private SelectDishPagePortalPopupWindow portalPopupWindow;
  private View titleBarView;
  private final TogetherCartManager.TogetherCartChangedListener togetherCartChangedListener = new TogetherCartManager.TogetherCartChangedListener()
  {
    public void onCountChanged()
    {
      SelectDishesDetailInfoActivity.this.onCountChanged();
    }

    public void onDishChanged(CartItem paramCartItem)
    {
      SelectDishesDetailInfoActivity.this.onDishChanged(paramCartItem);
    }

    public void onFreeDishChanged(ArrayList<CartFreeItem> paramArrayList)
    {
    }

    public void onGroupOnOrSetChanged()
    {
    }

    public void onManulRefreshComplete()
    {
    }

    public void onMistakeRecieved(int paramInt, String paramString)
    {
      if ((SelectDishesDetailInfoActivity.this.isResumed) && (!TextUtils.isEmpty(paramString)))
        SelectDishesDetailInfoActivity.this.showShortToast(paramString);
      if (paramInt == 100)
        SelectDishesDetailInfoActivity.this.finish();
    }

    public void onSyncComplete()
    {
      int i;
      if (SelectDishesDetailInfoActivity.this.mDishInfo != null)
      {
        i = SelectDishesDetailInfoActivity.this.getDishCountByDishId(SelectDishesDetailInfoActivity.this.mDishInfo.dishId);
        if (i != 0)
          break label51;
        SelectDishesDetailInfoActivity.this.mCartCountView.setVisibility(4);
      }
      while (true)
      {
        SelectDishesDetailInfoActivity.this.updateCartInfoViews();
        return;
        label51: SelectDishesDetailInfoActivity.this.mCartCountView.setText(String.valueOf(i));
        SelectDishesDetailInfoActivity.this.mCartCountView.setVisibility(0);
        SelectDishesDetailInfoActivity.this.mWrapper.notifyDataSetChanged();
      }
    }
  };

  private void addCartChangedListener()
  {
    if (this.isTogetherMenu)
    {
      TogetherCartManager.getInstance().addCartChangedListener(this.togetherCartChangedListener);
      return;
    }
    NewCartManager.getInstance().addCartChangedListener(this);
  }

  private void addDish(DishInfo paramDishInfo)
  {
    if (this.isTogetherMenu)
    {
      TogetherCartManager.getInstance().addDish(paramDishInfo);
      return;
    }
    NewCartManager.getInstance().addDish(paramDishInfo);
  }

  private void generateMealSetView(DPObject[] paramArrayOfDPObject, String paramString)
  {
    Object localObject1 = new LinkedHashMap();
    int i = 0;
    Object localObject2;
    Object localObject3;
    if (i < paramArrayOfDPObject.length)
    {
      localObject2 = paramArrayOfDPObject[i].getString("GroupName");
      if (((LinkedHashMap)localObject1).containsKey(localObject2))
      {
        localObject3 = (ArrayList)((LinkedHashMap)localObject1).get(localObject2);
        ((ArrayList)localObject3).add(paramArrayOfDPObject[i]);
        ((LinkedHashMap)localObject1).put(localObject2, localObject3);
      }
      while (true)
      {
        i += 1;
        break;
        localObject3 = new ArrayList();
        ((ArrayList)localObject3).add(paramArrayOfDPObject[i]);
        ((LinkedHashMap)localObject1).put(localObject2, localObject3);
      }
    }
    this.mBasicInfoRootView.findViewById(R.id.sd_detail_mealset_detail_title).setVisibility(0);
    paramArrayOfDPObject = (TextView)this.mBasicInfoRootView.findViewById(R.id.sd_detail_mealset_validity_desc);
    if (this.mDishInfo.validityDesc != null)
      paramArrayOfDPObject.setText(this.mDishInfo.validityDesc);
    paramArrayOfDPObject = this.mBasicInfoRootView.findViewById(R.id.sd_detail_mealset_desc_view);
    paramArrayOfDPObject.setVisibility(0);
    if (!TextUtils.isEmpty(paramString))
    {
      this.mBasicInfoRootView.findViewById(R.id.sd_detail_mealset_desc_text).setVisibility(0);
      ((TextView)this.mBasicInfoRootView.findViewById(R.id.sd_detail_mealset_desc_text)).setText(paramString);
    }
    paramArrayOfDPObject = (TableLayout)paramArrayOfDPObject.findViewById(R.id.sd_detail_mealset_table);
    paramString = ((LinkedHashMap)localObject1).entrySet().iterator();
    while (paramString.hasNext())
    {
      localObject1 = (Map.Entry)paramString.next();
      localObject2 = (String)((Map.Entry)localObject1).getKey();
      TextView localTextView;
      if (!TextUtils.isEmpty((CharSequence)localObject2))
      {
        localObject3 = new TableRow(this);
        ((TableRow)localObject3).setBackgroundResource(R.drawable.background_selectdish_detail_mealset_row);
        localTextView = new TextView(this);
        localTextView.setPadding(ViewUtils.dip2px(this, 10.0F), ViewUtils.dip2px(this, 9.0F), 0, ViewUtils.dip2px(this, 9.0F));
        localTextView.setText((CharSequence)localObject2);
        localTextView.getPaint().setFakeBoldText(true);
        ((TableRow)localObject3).addView(localTextView);
        paramArrayOfDPObject.addView((View)localObject3);
      }
      localObject1 = ((ArrayList)((Map.Entry)localObject1).getValue()).iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (DPObject)((Iterator)localObject1).next();
        i = ((DPObject)localObject2).getInt("Count");
        localObject2 = ((DPObject)localObject2).getObject("Dish");
        localObject3 = (TableRow)LayoutInflater.from(this).inflate(R.layout.selected_dishes_detail_mealset_row_layout, null);
        ((TableRow)localObject3).setOnClickListener(new View.OnClickListener((DPObject)localObject2)
        {
          public void onClick(View paramView)
          {
            paramView = new DishInfo(this.val$singleDish);
            Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectdishdetail"));
            localIntent.putExtra("detail", paramView);
            localIntent.putExtra("istogethermenu", SelectDishesDetailInfoActivity.this.isTogetherMenu);
            SelectDishesDetailInfoActivity.this.startActivity(localIntent);
          }
        });
        ((TableRow)localObject3).findViewById(R.id.sd_detail_mealset_name_view).setVisibility(0);
        ((TableRow)localObject3).findViewById(R.id.sd_detail_mealset_copies_view).setVisibility(0);
        localTextView = (TextView)((TableRow)localObject3).findViewById(R.id.sd_detail_singledish_name);
        if (((DPObject)localObject2).getString("Name") != null)
          localTextView.setText(((DPObject)localObject2).getString("Name"));
        localTextView = (TextView)((TableRow)localObject3).findViewById(R.id.sd_detail_mealset_dish_count);
        if (((DPObject)localObject2).getString("DishSales") != null)
        {
          ((TableRow)localObject3).findViewById(R.id.sd_detail_mealset_sales_view).setVisibility(0);
          localTextView.setVisibility(0);
          localTextView.setText(((DPObject)localObject2).getString("DishSales"));
        }
        localTextView = (TextView)((TableRow)localObject3).findViewById(R.id.sd_detail_mealset_thump_up);
        if (((DPObject)localObject2).getInt("RecomCount") != 0)
        {
          ((TableRow)localObject3).findViewById(R.id.sd_detail_mealset_sales_view).setVisibility(0);
          localTextView.setVisibility(0);
          localTextView.setText(String.valueOf(((DPObject)localObject2).getInt("RecomCount")));
        }
        localTextView = (TextView)((TableRow)localObject3).findViewById(R.id.sd_detail_mealset_copies);
        if ((i != 0) && (((DPObject)localObject2).getString("Unit") != null))
          localTextView.setText(i + ((DPObject)localObject2).getString("Unit"));
        paramArrayOfDPObject.addView((View)localObject3);
      }
    }
    paramString = (TableRow)LayoutInflater.from(this).inflate(R.layout.selected_dishes_detail_mealset_row_layout, null);
    paramString.findViewById(R.id.sd_detail_mealset_price_view).setVisibility(0);
    ((TextView)paramString.findViewById(R.id.sd_detail_total_price_num)).setText(NumberUtils.convertDoubleToIntegerIfNecessary(this.mDishInfo.oldPrice));
    ((TextView)paramString.findViewById(R.id.sd_detail_mealset_price_num)).setText(NumberUtils.convertDoubleToIntegerIfNecessary(this.mDishInfo.currentPrice));
    paramArrayOfDPObject.addView(paramString);
    this.mWrapper.notifyDataSetChanged();
  }

  private int getDishCountByDishId(int paramInt)
  {
    if (this.isTogetherMenu)
    {
      TogetherCartManager localTogetherCartManager = TogetherCartManager.getInstance();
      if (localTogetherCartManager.isOwner == 1)
        return localTogetherCartManager.getDishCountByDishIdinTotalDish(paramInt);
      return localTogetherCartManager.getDishCountByDishIdinTotalDish(paramInt) + localTogetherCartManager.getDishCountByDishIdinOtherDish(paramInt);
    }
    return NewCartManager.getInstance().getDishCountByDishId(paramInt);
  }

  private void getDishSetRequest(String paramString)
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/orderdish/dishdetail.bin").buildUpon();
    localBuilder.appendQueryParameter("dishid", paramString);
    localBuilder.appendQueryParameter("shopid", String.valueOf(this.mShopId));
    mapiService().exec(mapiGet(this, localBuilder.toString(), CacheType.DISABLED), this);
  }

  private DPObject[] getMenuEntranceList()
  {
    if (this.isTogetherMenu)
      return null;
    return NewCartManager.getInstance().getMenuEntranceList();
  }

  private int getShopId()
  {
    if (this.isTogetherMenu)
      return TogetherCartManager.getInstance().getShopId();
    return NewCartManager.getInstance().getShopId();
  }

  private String getShopName()
  {
    if (this.isTogetherMenu)
      return TogetherCartManager.getInstance().getShopName();
    return NewCartManager.getInstance().getShopName();
  }

  private int getTotalDishCount()
  {
    if (this.isTogetherMenu)
    {
      TogetherCartManager localTogetherCartManager = TogetherCartManager.getInstance();
      if (localTogetherCartManager.isOwner == 1)
        return localTogetherCartManager.getTotalDishCount();
      return localTogetherCartManager.getTotalDishCount() + localTogetherCartManager.getOtherTotalCount();
    }
    return NewCartManager.getInstance().getTotalDishCount();
  }

  private int getTotalSelectFreeDishCount()
  {
    if (this.isTogetherMenu)
      return TogetherCartManager.getInstance().getTotalSelectFreeDishCount();
    return NewCartManager.getInstance().getTotalSelectFreeDishCount();
  }

  private boolean hasHistoryFreeDish()
  {
    if (this.isTogetherMenu)
      return TogetherCartManager.getInstance().hasHistoryFreeDish();
    return NewCartManager.getInstance().hasHistoryFreeDish();
  }

  private boolean haveGroupOnInfo()
  {
    if (this.isTogetherMenu);
    do
      return false;
    while (NewCartManager.getInstance().groupOnDealId <= 0);
    return true;
  }

  private void removeCartChangedListener()
  {
    if (this.isTogetherMenu)
    {
      TogetherCartManager.getInstance().removeCartChangedListener(this.togetherCartChangedListener);
      return;
    }
    NewCartManager.getInstance().removeCartChangedListener(this);
  }

  private void showMorePagePortalPopupWindow()
  {
    if (this.portalPopupWindow == null)
      this.portalPopupWindow = new SelectDishPagePortalPopupWindow(this, getMenuEntranceList());
    this.portalPopupWindow.showBelowView(this.titleBarView, this.morePortalView);
  }

  private void stickBuyView(int paramInt)
  {
    if (paramInt == 0)
    {
      View localView = this.mGridView.getChildAt(paramInt);
      localView.invalidate();
      if (Math.abs(localView.getTop()) <= this.mBuyLayout.getTop())
      {
        this.mTopBuyLayout.setVisibility(8);
        this.mCartCountView = ((TextView)this.mBuyLayout.findViewById(R.id.sd_detail_count));
        this.mTopBuyLayout.layout(0, this.mBuyLayout.getTop() + ViewUtils.dip2px(this, 50.0F) - Math.abs(localView.getTop()), this.mTopBuyLayout.getWidth(), this.mBuyLayout.getTop() + this.mTopBuyLayout.getHeight() + ViewUtils.dip2px(this, 50.0F) - Math.abs(localView.getTop()));
        if (getDishCountByDishId(this.mDishInfo.dishId) <= 0)
          break label314;
        this.mCartCountView.setVisibility(0);
      }
    }
    while (true)
    {
      this.mCartCountView.setText(String.valueOf(getDishCountByDishId(this.mDishInfo.dishId)));
      this.mTopBuyLayout.setData(this.mDishInfo);
      return;
      this.mTopBuyLayout.setVisibility(0);
      this.mCartCountView = ((TextView)this.mTopBuyLayout.findViewById(R.id.sd_detail_count));
      this.mTopBuyLayout.layout(0, ViewUtils.dip2px(this, 50.0F), this.mTopBuyLayout.getWidth(), ViewUtils.dip2px(this, 50.0F) + this.mTopBuyLayout.getHeight());
      break;
      this.mTopBuyLayout.setVisibility(0);
      this.mCartCountView = ((TextView)this.mTopBuyLayout.findViewById(R.id.sd_detail_count));
      this.mTopBuyLayout.layout(0, ViewUtils.dip2px(this, 50.0F), this.mTopBuyLayout.getWidth(), ViewUtils.dip2px(this, 50.0F) + this.mTopBuyLayout.getHeight());
      break;
      label314: this.mCartCountView.setVisibility(4);
    }
  }

  private void updateCartInfoViews()
  {
    int j = 0;
    SelectDishMenuCartView localSelectDishMenuCartView;
    if ((getTotalDishCount() + getTotalSelectFreeDishCount() > 0) || (hasHistoryFreeDish()) || (haveGroupOnInfo()))
    {
      i = 1;
      localSelectDishMenuCartView = this.cartMenuInfoView;
      if (i == 0)
        break label59;
    }
    label59: for (int i = j; ; i = 8)
    {
      localSelectDishMenuCartView.setVisibility(i);
      this.cartMenuInfoView.refresh();
      return;
      i = 0;
      break;
    }
  }

  public void addDishDescription(String[] paramArrayOfString)
  {
    if ((paramArrayOfString != null) && (paramArrayOfString.length > 0))
    {
      ArrayList localArrayList = new ArrayList();
      int i = 0;
      while (i < paramArrayOfString.length)
      {
        localArrayList.add(new DPObject().edit().putString("Name", paramArrayOfString[i]).generate());
        i += 1;
      }
      this.mTagList = localArrayList;
    }
  }

  public String getPageName()
  {
    return "menuorder_detail";
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  public void initTitleBar()
  {
    this.titleBarView = findViewById(R.id.sd_detail_title_bar);
    this.titleBarView.setBackgroundColor(getResources().getColor(R.color.white));
    LinearLayout localLinearLayout = (LinearLayout)findViewById(R.id.title_bar_right_view_container);
    ((TextView)findViewById(R.id.title_bar_title)).setText(R.string.sd_title_detail);
    findViewById(R.id.left_view).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        SelectDishesDetailInfoActivity.this.finish();
      }
    });
    if ((!this.isTogetherMenu) && (getMenuEntranceList() != null) && (getMenuEntranceList().length >= 1))
    {
      this.morePortalView = LayoutInflater.from(this).inflate(R.layout.selectdish_title_bar_more_view, null, false);
      this.morePortalView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          SelectDishesDetailInfoActivity.this.showMorePagePortalPopupWindow();
        }
      });
      localLinearLayout.addView(this.morePortalView);
    }
  }

  public void initViewsAndData(int paramInt)
  {
    this.mGridView = ((StickyGridHeadersGridView)findViewById(R.id.selected_gallery_gridview));
    this.mBasicInfoRootView = LayoutInflater.from(this).inflate(R.layout.selected_dishes_basic_info, null);
    Object localObject = (NetworkImageView)this.mBasicInfoRootView.findViewById(R.id.album_photo);
    TextView localTextView1 = (TextView)this.mBasicInfoRootView.findViewById(R.id.selected_dish_desc);
    TextView localTextView2 = (TextView)this.mBasicInfoRootView.findViewById(R.id.foot_notes);
    View localView = this.mBasicInfoRootView.findViewById(R.id.selected_dish_desc_view);
    TextView localTextView3 = (TextView)this.mBasicInfoRootView.findViewById(R.id.selected_dish_promotion);
    LinearLayout localLinearLayout = (LinearLayout)this.mBasicInfoRootView.findViewById(R.id.free_rule_promotion);
    TextView localTextView4 = (TextView)this.mBasicInfoRootView.findViewById(R.id.promotion_content);
    TextView localTextView5 = (TextView)this.mBasicInfoRootView.findViewById(R.id.have_bought_count);
    this.mReviewTitleView = ((TextView)this.mBasicInfoRootView.findViewById(R.id.selected_dish_review));
    this.mBuyLayout = ((SelectDishDetailBuyItem)this.mBasicInfoRootView.findViewById(R.id.sd_detail_cart_view));
    this.mCartCountView = ((TextView)this.mBuyLayout.findViewById(R.id.sd_detail_count));
    this.mBuyLayout.setmListener(this);
    this.mBuyLayout.setmBuyTag(getString(R.string.sd_detail_normal_buy_tag));
    this.mBuyLayout.setShopId(this.mShopId);
    this.mBuyLayout.setData(this.mDishInfo);
    this.mTopBuyLayout = ((SelectDishDetailBuyItem)findViewById(R.id.sd_detail_cart_view));
    this.mTopBuyLayout.setmListener(this);
    this.mTopBuyLayout.setmBuyTag(getString(R.string.sd_detail_top_buy_tag));
    this.mTopBuyLayout.setData(this.mDishInfo);
    if (paramInt > 0)
    {
      this.mCartCountView.setVisibility(0);
      this.mCartCountView.setText(String.valueOf(paramInt));
      this.mCartCountView.setGravity(17);
    }
    if (this.mDishInfo != null)
    {
      if (this.mDishInfo.url != null)
      {
        ViewGroup.LayoutParams localLayoutParams = ((NetworkImageView)localObject).getLayoutParams();
        localLayoutParams.width = getWindowManager().getDefaultDisplay().getWidth();
        localLayoutParams.height = (localLayoutParams.width * 9 / 16);
        ((NetworkImageView)localObject).setLayoutParams(localLayoutParams);
        ((NetworkImageView)localObject).setImage(this.mDishInfo.detailurl);
        ((NetworkImageView)localObject).setVisibility(0);
      }
      if ((this.mDishInfo.targetCount != 0) && (this.mDishInfo.freeCount != 0))
      {
        localLinearLayout.setVisibility(0);
        if (this.mDishInfo.freeItem.dishId != this.mDishInfo.dishId)
          break label879;
        localTextView3.setText(getString(R.string.sd_event).replace("%s", String.valueOf(this.mDishInfo.targetCount)).replace("%n", String.valueOf(this.mDishInfo.freeCount)));
        localTextView4.setText(this.mDishInfo.eventDesc);
        if (this.mDishInfo.giftCount <= 0)
          break label910;
        localTextView5.setVisibility(0);
        localTextView5.setText(getString(R.string.sd_summary_free).replace("%s", String.valueOf(this.mDishInfo.giftCount)) + this.mDishInfo.unit);
        localTextView5.setTextColor(getResources().getColor(R.color.light_red));
      }
      label580: if ((!TextUtils.isEmpty(this.mDishInfo.desc)) && (this.mDishInfo.dishType == 0))
      {
        localView.setVisibility(0);
        localTextView1.setText(this.mDishInfo.desc);
        if (this.mShopName == null)
          break label995;
        localTextView2.setText(this.mShopName);
      }
    }
    while (true)
    {
      this.mHeardView = ((LinearLayout)LayoutInflater.from(this).inflate(R.layout.selected_dish_detail_layout, this.mGridView, false));
      this.mHeardView.addView(this.mBasicInfoRootView);
      localObject = LayoutInflater.from(this).inflate(R.layout.selected_dish_user_description, this.mGridView, false);
      if (localObject != null)
        this.mDishDescriptionView = ((WordGroupLayout)((View)localObject).findViewById(R.id.dish_description));
      this.mHeardView.addView((View)localObject);
      this.mDpObjShop = new DPObject().edit().putInt("ID", this.mShopId).generate();
      this.mGridView.setAreHeadersSticky(false);
      this.mGridView.setOnScrollListener(new AbsListView.OnScrollListener()
      {
        public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
        {
          SelectDishesDetailInfoActivity.this.stickBuyView(paramInt1);
        }

        public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
        {
        }
      });
      this.mAdapter = new ImageAdapter(this);
      this.mWrapper = new StickyGridHeadersListAdapterWrapper(this.mAdapter)
      {
        public int getCountForHeader(int paramInt)
        {
          if (paramInt == 0)
            return SelectDishesDetailInfoActivity.this.mAdapter.getCount();
          return 0;
        }

        public View getHeaderView(int paramInt, View paramView, ViewGroup paramViewGroup)
        {
          if (paramView == null)
            paramView = SelectDishesDetailInfoActivity.this.mHeardView;
          do
            return paramView;
          while (paramInt == 0);
          return null;
        }

        public int getNumHeaders()
        {
          return 1;
        }
      };
      this.mGridView.setAdapter(this.mWrapper);
      this.mGridView.setOnItemClickListener(this);
      findViewById(R.id.sd_detail_layout).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
      {
        public void onGlobalLayout()
        {
          SelectDishesDetailInfoActivity.this.findViewById(R.id.sd_detail_layout).getViewTreeObserver().removeGlobalOnLayoutListener(this);
          SelectDishesDetailInfoActivity.this.stickBuyView(0);
        }
      });
      this.cartMenuInfoView = ((SelectDishMenuCartView)findViewById(R.id.sd_bottom_cart_layout));
      this.cartMenuInfoView.setIsTogetherMenu(this.isTogetherMenu);
      this.cartMenuInfoView.setCheckCartListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          SelectDishesDetailInfoActivity.this.gaUserInfo.title = "";
          GAHelper.instance().contextStatisticsEvent(SelectDishesDetailInfoActivity.this, "cart", SelectDishesDetailInfoActivity.this.gaUserInfo, "tap");
          if (SelectDishesDetailInfoActivity.this.isTogetherMenu);
          for (paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectdishtogethercart")); ; paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectdishcart")))
          {
            SelectDishesDetailInfoActivity.this.startActivity(paramView);
            return;
          }
        }
      });
      updateCartInfoViews();
      return;
      label879: localTextView3.setText(getString(R.string.sd_event2).replace("%s", String.valueOf(this.mDishInfo.targetCount)));
      break;
      label910: if (this.mDishInfo.bought <= 0)
        break label580;
      localTextView5.setVisibility(0);
      localTextView5.setText(getString(R.string.sd_summary_bought).replace("%s", String.valueOf(this.mDishInfo.bought)) + this.mDishInfo.unit);
      localTextView5.setTextColor(getResources().getColor(R.color.light_gray));
      break label580;
      label995: localTextView2.setVisibility(8);
    }
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    paramView.invalidate();
    View localView = this.mGridView.getChildAt(0);
    localView.invalidate();
    int j = localView.getTop();
    if (i == R.id.addcart)
      startAddDishAnimation(paramView, 600, j);
  }

  public void onCountChanged()
  {
    updateCartInfoViews();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.selected_user_review_photo);
    if (paramBundle == null)
      this.mDishInfo = ((DishInfo)getIntent().getParcelableExtra("detail"));
    for (this.isTogetherMenu = getBooleanParam("istogethermenu"); ; this.isTogetherMenu = paramBundle.getBoolean("istogethermenu"))
    {
      this.mShopId = getShopId();
      this.mShopName = getShopName();
      int i = 0;
      if (this.mDishInfo != null)
        i = getDishCountByDishId(this.mDishInfo.dishId);
      initTitleBar();
      initViewsAndData(i);
      if ((this.mShopId != 0) && (this.mDishInfo != null) && (this.mDishInfo.dishId != 0))
        getDishSetRequest(String.valueOf(this.mDishInfo.dishId));
      addCartChangedListener();
      return;
      this.mDishInfo = ((DishInfo)paramBundle.getParcelable("detail"));
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    removeCartChangedListener();
  }

  public void onDishChanged(CartItem paramCartItem)
  {
    if ((paramCartItem != null) && (paramCartItem.dishInfo.dishId == this.mDishInfo.dishId))
    {
      if (paramCartItem.getItemCount() == 0)
        this.mCartCountView.setVisibility(4);
    }
    else
      return;
    this.mCartCountView.setText(String.valueOf(paramCartItem.getItemCount()));
    this.mCartCountView.setVisibility(0);
    this.mWrapper.notifyDataSetChanged();
  }

  public void onFreeDishChanged(ArrayList<CartFreeItem> paramArrayList)
  {
  }

  public void onGroupOnOrSetChanged()
  {
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (paramView.getTag() == ImageAdapter.LOADING);
    do
    {
      return;
      paramAdapterView = (NetworkThumbView)paramView.findViewById(R.id.recommend);
    }
    while (paramAdapterView == null);
    Object localObject = new ArrayList();
    ((ArrayList)localObject).addAll(this.mAdapter.getDataList());
    paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showphoto"));
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(this.mDpObjShop);
    paramView.putParcelableArrayListExtra("arrShopObjs", localArrayList);
    paramView.putParcelableArrayListExtra("pageList", (ArrayList)localObject);
    if ((BitmapDrawable)paramAdapterView.getDrawable() != null)
    {
      localObject = new ByteArrayOutputStream();
      ((BitmapDrawable)paramAdapterView.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, (OutputStream)localObject);
      paramView.putExtra("currentbitmap", ((ByteArrayOutputStream)localObject).toByteArray());
    }
    paramView.putExtra("position", paramInt);
    startActivity(paramView);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      paramMApiResponse = (DPObject)paramMApiResponse.result();
      paramMApiRequest = paramMApiResponse.getArray("Dishes");
      paramMApiResponse = paramMApiResponse.getString("Desc");
      if ((paramMApiRequest != null) && (paramMApiRequest.length > 0))
        generateMealSetView(paramMApiRequest, paramMApiResponse);
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("shopid", this.mShopId);
    if (this.mDishInfo != null)
      paramBundle.putParcelable("detail", this.mDishInfo);
    if (!TextUtils.isEmpty(this.mShopName))
      paramBundle.putString("shopname", this.mShopName);
    paramBundle.putBoolean("istogethermenu", this.isTogetherMenu);
  }

  protected MApiRequest photoTask(String paramString, int paramInt)
  {
    if (paramInt != 0)
      statisticsEvent("shopinfo5", "shopinfo5_dish_dropdown", "", 0);
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/orderdish/dishphotos.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", paramString);
    localBuilder.appendQueryParameter("start", String.valueOf(paramInt));
    localBuilder.appendQueryParameter("id", String.valueOf(this.mDishInfo.dishId));
    localBuilder.appendQueryParameter("limit", "3");
    return BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
  }

  public void startAddDishAnimation(View paramView, int paramInt1, int paramInt2)
  {
    if (this.cartMenuInfoView.getVisibility() != 0)
    {
      this.cartMenuInfoView.setVisibility(0);
      this.cartMenuInfoView.post(new Runnable(paramView, paramInt1, paramInt2)
      {
        public void run()
        {
          SelectDishesDetailInfoActivity.this.startAddDishAnimation(this.val$view, this.val$duration, this.val$scrollDistance);
        }
      });
      return;
    }
    FrameLayout localFrameLayout = (FrameLayout)findViewById(R.id.sd_detail_moving_frame);
    ImageView localImageView = new ImageView(this);
    int i = ViewUtils.dip2px(this, 17.0F);
    localImageView.setLayoutParams(new ViewGroup.LayoutParams(i, i));
    localImageView.setImageResource(R.drawable.background_selectdish_count);
    localFrameLayout.addView(localImageView);
    paramView = paramView.findViewById(R.id.sd_detail_count);
    View localView = this.cartMenuInfoView.getDishCountView();
    SelectDishCartAnimationManager localSelectDishCartAnimationManager = new SelectDishCartAnimationManager(this);
    localSelectDishCartAnimationManager.getClass();
    localSelectDishCartAnimationManager.setAddDishAniListener(new SelectDishCartAnimationManager.AddDishAniListener(localSelectDishCartAnimationManager, localFrameLayout, localImageView)
    {
      public void onAnimationEnd(Animator paramAnimator)
      {
        super.onAnimationEnd(paramAnimator);
        this.val$frameLayout.removeView(this.val$movingView);
        SelectDishesDetailInfoActivity.this.addDish(SelectDishesDetailInfoActivity.this.mDishInfo);
        SelectDishesDetailInfoActivity.this.gaUserInfo.title = String.valueOf(SelectDishesDetailInfoActivity.this.mDishInfo.dishId);
        GAHelper.instance().contextStatisticsEvent(SelectDishesDetailInfoActivity.this, "addcart", SelectDishesDetailInfoActivity.this.gaUserInfo, "tap");
      }
    });
    localSelectDishCartAnimationManager.startAddDishAnimation(localImageView, paramView, localView, paramInt1, paramInt2);
  }

  class ImageAdapter extends BasicLoadAdapter
  {
    public ImageAdapter(Context arg2)
    {
      super();
    }

    public MApiRequest createRequest(int paramInt)
    {
      return SelectDishesDetailInfoActivity.this.photoTask(String.valueOf(SelectDishesDetailInfoActivity.this.mShopId), paramInt);
    }

    public int getCount()
    {
      if ((this.mIsEnd) && (this.mData.size() == 0))
        return 0;
      return super.getCount();
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = super.getView(paramInt, paramView, paramViewGroup);
      paramViewGroup = getItem(paramInt);
      if ((paramViewGroup == ERROR) || (paramViewGroup == LOADING) || (paramViewGroup == EMPTY))
        paramView.setVisibility(8);
      return paramView;
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      int i;
      if ((paramView == null) || (paramView.getId() != R.id.item_of_photo_album))
      {
        paramView = LayoutInflater.from(SelectDishesDetailInfoActivity.this).inflate(R.layout.selected_item_of_photo, paramViewGroup, false);
        i = (int)(ViewUtils.getScreenWidthPixels(SelectDishesDetailInfoActivity.this) * 28.699999999999999D / 100.0D);
        paramView.getLayoutParams().height = i;
        if (paramInt % 3 != 0)
          break label124;
        paramView.getLayoutParams().width = (ViewUtils.dip2px(SelectDishesDetailInfoActivity.this, 15.0F) + i);
        paramView.setPadding(ViewUtils.dip2px(SelectDishesDetailInfoActivity.this, 15.0F), 0, 0, 0);
      }
      while (true)
      {
        ((NetworkImageView)paramView.findViewById(R.id.recommend)).setImage(paramDPObject.getString("ThumbUrl"));
        return paramView;
        break;
        label124: if (paramInt % 3 == 2)
        {
          paramView.getLayoutParams().width = (ViewUtils.dip2px(SelectDishesDetailInfoActivity.this, 15.0F) + i);
          paramView.setPadding(0, 0, ViewUtils.dip2px(SelectDishesDetailInfoActivity.this, 15.0F), 0);
          continue;
        }
        paramView.getLayoutParams().width = i;
      }
    }

    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestFailed(paramMApiRequest, paramMApiResponse);
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      int j = 1;
      super.onRequestFinish(paramMApiRequest, paramMApiResponse);
      int i;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        if (paramMApiRequest.getInt("StartIndex") == 0)
          SelectDishesDetailInfoActivity.this.addDishDescription(paramMApiRequest.getStringArray("PhotoTagList"));
        if ((SelectDishesDetailInfoActivity.this.mTagList != null) && (SelectDishesDetailInfoActivity.this.mTagList.size() != 0) && (SelectDishesDetailInfoActivity.this.mDishDescriptionView != null))
        {
          SelectDishesDetailInfoActivity.this.mDishDescriptionView.enableWordCloudEffect();
          SelectDishesDetailInfoActivity.this.mDishDescriptionView.setButtonMargin(1);
          SelectDishesDetailInfoActivity.this.mDishDescriptionView.setButtonBackgroudResId(0);
          SelectDishesDetailInfoActivity.this.mDishDescriptionView.setTabCountLimit(10);
          SelectDishesDetailInfoActivity.this.mDishDescriptionView.setTabList(SelectDishesDetailInfoActivity.this.mTagList);
        }
        if ((SelectDishesDetailInfoActivity.this.mTagList == null) || (SelectDishesDetailInfoActivity.this.mTagList.size() == 0))
          break label211;
        i = 1;
        if (this.mData.size() == 0)
          break label216;
      }
      while (true)
      {
        if ((i | j) != 0)
          SelectDishesDetailInfoActivity.this.mReviewTitleView.setVisibility(0);
        SelectDishesDetailInfoActivity.this.mWrapper.notifyDataSetChanged();
        return;
        label211: i = 0;
        break;
        label216: j = 0;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.selectdish.ui.SelectDishesDetailInfoActivity
 * JD-Core Version:    0.6.0
 */