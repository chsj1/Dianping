package com.dianping.booking;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.shoplist.ShopListAdapter.ShopListReloadHandler;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.base.web.ui.NovaZeusFragment;
import com.dianping.base.widget.ClickableFrameLayout;
import com.dianping.base.widget.ClickableFrameLayout.ClickListener;
import com.dianping.base.widget.CustomImageButton;
import com.dianping.base.widget.TitleBar;
import com.dianping.booking.adapter.BookingCategoryFilterAdapter;
import com.dianping.booking.adapter.BookingShopListAdapter;
import com.dianping.booking.fragment.BookingPresetFilterFragment;
import com.dianping.booking.fragment.BookingPresetFilterFragment.BookingInfoListener;
import com.dianping.booking.fragment.BookingShopListZuesFragment;
import com.dianping.booking.fragment.BookingShopListZuesFragment.BookingShopListZeusListener;
import com.dianping.booking.util.BookingShoplistDataSource;
import com.dianping.booking.util.BookingShoplistDataSource.DataLoadListener;
import com.dianping.booking.util.BookingShoplistDataSource.DataStatus;
import com.dianping.booking.view.BookingFilterBar;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import org.json.JSONException;
import org.json.JSONObject;

public class BookingShoplistActvity extends NovaActivity
  implements BookingShoplistDataSource.DataLoadListener, ShopListAdapter.ShopListReloadHandler, BookingShopListZuesFragment.BookingShopListZeusListener
{
  public static final String BOOKING_INFO_FILTER_TIPS = "可选预订条件";
  private static final int SEARCH_REQUEST = 10;
  private BookingCategoryFilterViewStatus bookingFilterViewStatus = BookingCategoryFilterViewStatus.FILTER_STATUS_CLOSED;
  private BookingCategoryFilterAdapter categoryFilterAdapter;
  private ImageButton categoryFilterIcon;
  private ListView categoryFilterListView;
  private LinearLayout categoryFilterView;
  private BookingShoplistDataSource dataSource;
  private BookingFilterBar filterBar;
  private int height;
  private int layoutHeight;
  private int layoutWidth;
  private View loadingView;
  private NovaZeusFragment mWebFragment;
  private ClickableFrameLayout mainView;
  private BookingPresetFilterFragment presetFilterFragment;
  private TextView searchBarContentView;
  private LinearLayout searchBarView;
  private BookingShopListAdapter shopListAdapter;
  private RelativeLayout shopListEmptyView;
  private View shopListErrorView;
  private ListView shopListView;
  private int width;
  private FrameLayout zeusListLayout;

  private void fetchParams()
  {
    boolean bool = true;
    this.dataSource = new BookingShoplistDataSource(this);
    this.dataSource.setDataLoadListener(this);
    this.dataSource.orderSource = super.getIntParam("ordersource", -1);
    String str = super.getStringParam("tagid");
    BookingShoplistDataSource localBookingShoplistDataSource = this.dataSource;
    Object localObject = str;
    if (TextUtils.isEmpty(str))
      localObject = "-1";
    localBookingShoplistDataSource.curTagId = ((String)localObject);
    localObject = super.getStringParam("src");
    if ((TextUtils.isEmpty((CharSequence)localObject)) || (((String)localObject).equals("0")))
    {
      this.dataSource.isZeus = false;
      this.dataSource.keyword = super.getStringParam("keyword");
      localObject = this.dataSource;
      if (TextUtils.isEmpty(super.getStringParam("range")))
        break label451;
    }
    while (true)
    {
      ((BookingShoplistDataSource)localObject).isRange = bool;
      this.dataSource.bookingPerson = super.getIntParam("bookingpersonnum", -1);
      this.dataSource.bookingTime = super.getLongParam("bookingdate", -1L);
      int i = super.getIntParam("regionid", -1);
      if (i >= 0)
        this.dataSource.setCurRegion(new DPObject("Region").edit().putInt("ID", i).putString("Name", "").putInt("ParentID", -10000).generate());
      localObject = super.getStringParam("range");
      if (!TextUtils.isEmpty((CharSequence)localObject))
        this.dataSource.setCurRange(new DPObject("Pair").edit().putString("ID", (String)localObject).putString("Name", "").generate());
      i = super.getIntParam("categoryid", -1);
      if (i >= 0)
        this.dataSource.setCurCategory(new DPObject("Category").edit().putInt("ID", i).putString("Name", "").putInt("ParentID", -1).generate());
      localObject = super.getStringParam("sort");
      if (!TextUtils.isEmpty((CharSequence)localObject))
        this.dataSource.setCurSort(new DPObject("Pair").edit().putString("ID", (String)localObject).putString("Name", "").generate());
      localObject = super.getStringParam("push");
      if (!TextUtils.isEmpty((CharSequence)localObject))
        statisticsEvent("mybooking6", "mybooking6_order_push", (String)localObject, 0);
      return;
      this.dataSource.isZeus = true;
      break;
      label451: bool = false;
    }
  }

  private ObjectAnimator hideFilter()
  {
    if ((this.bookingFilterViewStatus == BookingCategoryFilterViewStatus.FILTER_STATUS_MOVING) || (this.bookingFilterViewStatus == BookingCategoryFilterViewStatus.FILTER_STATUS_CLOSED))
      return null;
    float f3;
    float f4;
    float f1;
    float f2;
    if (Build.VERSION.SDK_INT < 11)
    {
      f3 = 0.0F;
      f4 = this.width * 0.7F;
      f1 = 1.0F;
      f2 = 1.25F;
    }
    while (true)
    {
      ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(this.mainView, "scaleX", new float[] { f1, f2 }).setDuration(200L);
      localObjectAnimator1.start();
      ObjectAnimator.ofFloat(this.mainView, "scaleY", new float[] { f1, f2 }).setDuration(200L).start();
      ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(this.mainView, "translationX", new float[] { f3, f4 }).setDuration(200L);
      localObjectAnimator2.addListener(new Animator.AnimatorListener()
      {
        public void onAnimationCancel(Animator paramAnimator)
        {
        }

        public void onAnimationEnd(Animator paramAnimator)
        {
          BookingShoplistActvity.this.addTitleBarShadow();
          BookingShoplistActvity.access$002(BookingShoplistActvity.this, BookingShoplistActvity.BookingCategoryFilterViewStatus.FILTER_STATUS_CLOSED);
          if (Build.VERSION.SDK_INT < 11)
          {
            int i = -ViewUtils.dip2px(BookingShoplistActvity.this, 15.0F);
            int j = -ViewUtils.dip2px(BookingShoplistActvity.this, 20.0F);
            int k = BookingShoplistActvity.this.layoutWidth;
            int m = BookingShoplistActvity.this.layoutHeight;
            BookingShoplistActvity.this.mainView.clearAnimation();
            paramAnimator = (FrameLayout.LayoutParams)BookingShoplistActvity.this.mainView.getLayoutParams();
            paramAnimator.setMargins(i, j, 0, 0);
            paramAnimator.width = k;
            paramAnimator.height = m;
            BookingShoplistActvity.this.mainView.setLayoutParams(paramAnimator);
          }
        }

        public void onAnimationRepeat(Animator paramAnimator)
        {
        }

        public void onAnimationStart(Animator paramAnimator)
        {
          BookingShoplistActvity.access$002(BookingShoplistActvity.this, BookingShoplistActvity.BookingCategoryFilterViewStatus.FILTER_STATUS_MOVING);
        }
      });
      localObjectAnimator2.start();
      return localObjectAnimator1;
      f3 = -this.width * 0.7F;
      f4 = 0.0F;
      f1 = 0.8F;
      f2 = 1.0F;
    }
  }

  private void initViews()
  {
    this.height = ViewUtils.getScreenHeightPixels(this);
    this.width = ViewUtils.getScreenWidthPixels(this);
    this.mainView = ((ClickableFrameLayout)findViewById(R.id.booking_shoplist_layout));
    this.mainView.setClickListener(new ClickableFrameLayout.ClickListener()
    {
      public boolean isClickable()
      {
        return BookingShoplistActvity.this.bookingFilterViewStatus == BookingShoplistActvity.BookingCategoryFilterViewStatus.FILTER_STATUS_OPENED;
      }
    });
    this.mainView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        BookingShoplistActvity.this.hideFilter();
        BookingShoplistActvity.this.resetCategoryFilter();
      }
    });
    ((CustomImageButton)findViewById(R.id.booking_left_title_btn)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        KeyboardUtils.hideKeyboard(paramView);
        BookingShoplistActvity.this.finish();
      }
    });
    this.presetFilterFragment = ((BookingPresetFilterFragment)getSupportFragmentManager().findFragmentById(R.id.booking_preset_filter_fragment));
    this.presetFilterFragment.getBookingInfoView().setGravity(17);
    Object localObject;
    if ((this.dataSource.bookingPerson > 0) && (this.dataSource.bookingTime > 0L))
    {
      this.presetFilterFragment.updateBookingInfo(this.dataSource.bookingTime, this.dataSource.bookingPerson);
      this.presetFilterFragment.setBookingInfoListener(new BookingPresetFilterFragment.BookingInfoListener()
      {
        public void addGA()
        {
          BookingShoplistActvity.this.statisticsEvent("booking6", "booking6_channel_list_condition", "", 0);
        }

        public void onBookingInfoChanged(int paramInt, long paramLong)
        {
          BookingShoplistActvity.this.dataSource.bookingPerson = paramInt;
          BookingShoplistActvity.this.dataSource.bookingTime = paramLong;
          BookingShoplistActvity.this.reloadBookingShopList();
        }
      });
      this.categoryFilterIcon = ((ImageButton)findViewById(R.id.booking_category_filter_icon));
      this.categoryFilterIcon.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (BookingShoplistActvity.this.bookingFilterViewStatus == BookingShoplistActvity.BookingCategoryFilterViewStatus.FILTER_STATUS_CLOSED)
          {
            KeyboardUtils.hideKeyboard(paramView);
            BookingShoplistActvity.this.showFilter();
            BookingShoplistActvity.access$002(BookingShoplistActvity.this, BookingShoplistActvity.BookingCategoryFilterViewStatus.FILTER_STATUS_OPENED);
          }
          while (true)
          {
            BookingShoplistActvity.this.statisticsEvent("booking6", "booking6_channel_filter_funnel", "", 0);
            return;
            BookingShoplistActvity.this.hideFilter();
            BookingShoplistActvity.access$002(BookingShoplistActvity.this, BookingShoplistActvity.BookingCategoryFilterViewStatus.FILTER_STATUS_CLOSED);
            BookingShoplistActvity.this.reloadBookingShopList();
          }
        }
      });
      this.categoryFilterIcon.setVisibility(4);
      this.searchBarView = ((LinearLayout)findViewById(R.id.booking_shoplist_searchbar));
      this.searchBarContentView = ((TextView)findViewById(R.id.booking_shoplist_searchbar_content));
      this.searchBarContentView.setText(this.dataSource.keyword);
      this.searchBarView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          int j = BookingShoplistActvity.this.dataSource.orderSource;
          long l = BookingShoplistActvity.this.dataSource.bookingTime;
          int k = BookingShoplistActvity.this.dataSource.bookingPerson;
          paramView = BookingShoplistActvity.this.dataSource.curTagId;
          if (BookingShoplistActvity.this.dataSource.isZeus);
          for (int i = 1; ; i = 0)
          {
            String str = String.format("dianping://bookingsearch?ordersource=%s&bookingdate=%s&bookingpersonnum=%s&tagid=%s&src=%s", new Object[] { Integer.valueOf(j), Long.valueOf(l), Integer.valueOf(k), paramView, Integer.valueOf(i) });
            paramView = str;
            if (!TextUtils.isEmpty(BookingShoplistActvity.this.dataSource.keyword))
              paramView = String.format("%s&oldkeyword=%s", new Object[] { str, Uri.encode(BookingShoplistActvity.access$300(BookingShoplistActvity.this).keyword) });
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
            BookingShoplistActvity.this.startActivityForResult(paramView, 10);
            BookingShoplistActvity.this.overridePendingTransition(0, 0);
            return;
          }
        }
      });
      this.filterBar = ((BookingFilterBar)findViewById(R.id.booking_shop_filterbar));
      this.filterBar.setBookingShopListDataSource(this.dataSource);
      BookingFilterBar localBookingFilterBar = this.filterBar;
      if (this.dataSource.curRegion() != null)
        break label644;
      localObject = "";
      label305: localBookingFilterBar.addItem("region", (String)localObject);
      localBookingFilterBar = this.filterBar;
      if (this.dataSource.curCategory() != null)
        break label660;
      localObject = "";
      label332: localBookingFilterBar.addItem("category", (String)localObject);
      localBookingFilterBar = this.filterBar;
      if (this.dataSource.curSort() != null)
        break label676;
      localObject = "";
      label359: localBookingFilterBar.addItem("rank", (String)localObject);
      this.loadingView = findViewById(R.id.loadingView);
      this.loadingView.setBackgroundColor(getResources().getColor(R.color.common_bk_color));
      this.shopListErrorView = findViewById(R.id.booking_shoplist_error);
      this.shopListErrorView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          BookingShoplistActvity.this.reloadBookingShopList();
        }
      });
      this.zeusListLayout = ((FrameLayout)findViewById(R.id.zeusWebContainer));
      if (this.dataSource.isZeus)
        break label692;
      ((ViewStub)findViewById(R.id.nativeListView)).inflate();
      this.shopListView = ((ListView)findViewById(R.id.booking_shoplist_content));
      this.shopListEmptyView = ((RelativeLayout)findViewById(R.id.booking_shoplist_empty));
      this.shopListAdapter = new BookingShopListAdapter(this, this);
      this.shopListAdapter.setShopList(this.dataSource);
      this.shopListView.setAdapter(this.shopListAdapter);
      this.shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
        public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
        {
          paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
          if ((paramAdapterView instanceof DPObject))
          {
            paramAdapterView = (DPObject)paramAdapterView;
            paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + paramAdapterView.getInt("ID")));
            paramView.putExtra("shopId", paramAdapterView.getInt("ID"));
            paramView.putExtra("ordersource", BookingShoplistActvity.this.dataSource.orderSource);
            if ((BookingShoplistActvity.this.dataSource.bookingTime > 0L) && (BookingShoplistActvity.this.dataSource.bookingPerson > 0))
            {
              paramView.putExtra("bookingdate", BookingShoplistActvity.this.dataSource.bookingTime);
              paramView.putExtra("bookingpersonnum", BookingShoplistActvity.this.dataSource.bookingPerson);
            }
            BookingShoplistActvity.this.startActivity(paramView);
            BookingShoplistActvity.this.statisticsEvent("booking6", "booking6_channel_list_itemclick", "", 0);
          }
        }
      });
      this.zeusListLayout.setVisibility(8);
    }
    while (true)
    {
      this.categoryFilterView = ((LinearLayout)findViewById(R.id.booking_category_filter_layout));
      this.categoryFilterListView = ((ListView)findViewById(R.id.booking_category_filter_content));
      ((FrameLayout.LayoutParams)this.categoryFilterView.getLayoutParams()).setMargins((int)(this.width * 0.25D), (int)(this.height * 0.07000000000000001D), 0, 0);
      ((Button)findViewById(R.id.booking_category_filter_confirm_btn)).setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          Object localObject = null;
          BookingShoplistActvity.this.hideFilter();
          BookingShoplistActvity.access$002(BookingShoplistActvity.this, BookingShoplistActvity.BookingCategoryFilterViewStatus.FILTER_STATUS_CLOSED);
          BookingShoplistDataSource localBookingShoplistDataSource = BookingShoplistActvity.this.dataSource;
          if (BookingShoplistActvity.this.categoryFilterAdapter == null)
          {
            paramView = null;
            localBookingShoplistDataSource.curTagId = paramView;
            localBookingShoplistDataSource = BookingShoplistActvity.this.dataSource;
            if (BookingShoplistActvity.this.categoryFilterAdapter != null)
              break label139;
            paramView = null;
            label66: localBookingShoplistDataSource.curMinPrice = paramView;
            localBookingShoplistDataSource = BookingShoplistActvity.this.dataSource;
            if (BookingShoplistActvity.this.categoryFilterAdapter != null)
              break label153;
          }
          label139: label153: for (paramView = localObject; ; paramView = BookingShoplistActvity.this.categoryFilterAdapter.getMaxPrice())
          {
            localBookingShoplistDataSource.curMaxPrice = paramView;
            BookingShoplistActvity.this.reloadBookingShopList();
            BookingShoplistActvity.this.setCategoryFilterStatus();
            BookingShoplistActvity.this.statisticsEvent("booking6", "booking6_channel_filter_funnel_submit", "", 0);
            return;
            paramView = BookingShoplistActvity.this.categoryFilterAdapter.getCurTagId();
            break;
            paramView = BookingShoplistActvity.this.categoryFilterAdapter.getMinPrice();
            break label66;
          }
        }
      });
      return;
      this.presetFilterFragment.updateBookingInfo("可选预订条件");
      break;
      label644: localObject = this.dataSource.curRegion().getString("Name");
      break label305;
      label660: localObject = this.dataSource.curCategory().getString("Name");
      break label332;
      label676: localObject = this.dataSource.curSort().getString("Name");
      break label359;
      label692: this.zeusListLayout.setVisibility(0);
      this.mWebFragment = ((NovaZeusFragment)getSupportFragmentManager().findFragmentByTag("bookinglist_web_fragment"));
      if (this.mWebFragment == null)
      {
        localObject = new Bundle();
        ((Bundle)localObject).putString("url", "http://h5.dianping.com/tuan/rs/shop-list.html");
        this.mWebFragment = ((NovaZeusFragment)Fragment.instantiate(this, BookingShopListZuesFragment.class.getName(), (Bundle)localObject));
      }
      localObject = getSupportFragmentManager().beginTransaction();
      ((FragmentTransaction)localObject).replace(R.id.zeusWebContainer, this.mWebFragment, "bookinglist_web_fragment");
      ((FragmentTransaction)localObject).commit();
    }
  }

  private void publish(String paramString1, String paramString2)
  {
    try
    {
      JSONObject localJSONObject1 = new JSONObject();
      localJSONObject1.put("action", "booking:shoplist:load");
      JSONObject localJSONObject2 = new JSONObject();
      localJSONObject2.put("data", paramString2);
      localJSONObject2.put("type", paramString1);
      localJSONObject2.put("imageload", NovaConfigUtils.isShowImageInMobileNetwork());
      localJSONObject1.put("value", localJSONObject2);
      this.mWebFragment.publish(localJSONObject1);
      return;
    }
    catch (Exception paramString1)
    {
      paramString1.printStackTrace();
    }
  }

  private void resetCategoryFilter()
  {
    if ((this.dataSource.curTag != null) && (this.dataSource.curSwitches != null) && (this.dataSource.curTags != null))
    {
      if (this.categoryFilterAdapter != null)
        break label84;
      this.categoryFilterAdapter = new BookingCategoryFilterAdapter(this, this.dataSource);
      this.categoryFilterListView.setAdapter(this.categoryFilterAdapter);
    }
    while (true)
    {
      this.dataSource.curTagId = this.dataSource.curTag.getString("ID");
      return;
      label84: this.categoryFilterAdapter.setData(this.dataSource);
    }
  }

  private void setCategoryFilterStatus()
  {
    if ((this.categoryFilterAdapter != null) && (this.categoryFilterAdapter.hasFilterInfo().booleanValue()))
    {
      this.categoryFilterIcon.setImageResource(R.drawable.list_icon_filter_select);
      return;
    }
    this.categoryFilterIcon.setImageResource(R.drawable.list_icon_filter);
  }

  private ObjectAnimator showFilter()
  {
    if ((this.bookingFilterViewStatus == BookingCategoryFilterViewStatus.FILTER_STATUS_MOVING) || (this.bookingFilterViewStatus == BookingCategoryFilterViewStatus.FILTER_STATUS_OPENED))
      return null;
    ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(this.mainView, "scaleX", new float[] { 1.0F, 0.8F }).setDuration(200L);
    localObjectAnimator1.start();
    ObjectAnimator.ofFloat(this.mainView, "scaleY", new float[] { 1.0F, 0.8F }).setDuration(200L).start();
    ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(this.mainView, "translationX", new float[] { 0.0F, -this.width * 0.7F }).setDuration(200L);
    localObjectAnimator2.addListener(new Animator.AnimatorListener()
    {
      public void onAnimationCancel(Animator paramAnimator)
      {
      }

      public void onAnimationEnd(Animator paramAnimator)
      {
        BookingShoplistActvity.access$002(BookingShoplistActvity.this, BookingShoplistActvity.BookingCategoryFilterViewStatus.FILTER_STATUS_OPENED);
        if (Build.VERSION.SDK_INT < 11)
        {
          BookingShoplistActvity.access$702(BookingShoplistActvity.this, BookingShoplistActvity.this.mainView.getWidth());
          BookingShoplistActvity.access$902(BookingShoplistActvity.this, BookingShoplistActvity.this.mainView.getHeight());
          int i = (int)(-BookingShoplistActvity.this.width * 0.6D - ViewUtils.dip2px(BookingShoplistActvity.this, 15.0F));
          int j = (int)(BookingShoplistActvity.this.mainView.getHeight() * 0.1F - ViewUtils.dip2px(BookingShoplistActvity.this, 20.0F));
          int k = (int)(BookingShoplistActvity.this.layoutWidth * 0.8D);
          int m = (int)(BookingShoplistActvity.this.layoutHeight * 0.8D);
          BookingShoplistActvity.this.mainView.clearAnimation();
          paramAnimator = (FrameLayout.LayoutParams)BookingShoplistActvity.this.mainView.getLayoutParams();
          paramAnimator.setMargins(i, j, 0, 0);
          paramAnimator.width = k;
          paramAnimator.height = m;
          BookingShoplistActvity.this.mainView.setLayoutParams(paramAnimator);
        }
      }

      public void onAnimationRepeat(Animator paramAnimator)
      {
      }

      public void onAnimationStart(Animator paramAnimator)
      {
        BookingShoplistActvity.this.removeTitleBarShadow();
        BookingShoplistActvity.access$002(BookingShoplistActvity.this, BookingShoplistActvity.BookingCategoryFilterViewStatus.FILTER_STATUS_MOVING);
      }
    });
    localObjectAnimator2.start();
    this.mainView.requestFocus();
    this.mainView.requestFocusFromTouch();
    return localObjectAnimator1;
  }

  private void updateFilter()
  {
    this.filterBar.setItem("region", this.dataSource.curRegion().getString("Name"));
    this.filterBar.setItem("category", this.dataSource.curCategory().getString("Name"));
    this.filterBar.setItem("rank", this.dataSource.curSort().getString("Name"));
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void loadMore(int paramInt)
  {
    if ((this.mWebFragment != null) && (this.mWebFragment.isActivated()))
      this.dataSource.loadData(paramInt, false);
  }

  public void loadShopListFinsh(BookingShoplistDataSource.DataStatus paramDataStatus, Object paramObject)
  {
    paramObject = new JSONObject();
    if (this.dataSource.isZeus);
    try
    {
      paramObject.put("imageload", NovaConfigUtils.isShowImageInMobileNetwork());
      switch (12.$SwitchMap$com$dianping$booking$util$BookingShoplistDataSource$DataStatus[paramDataStatus.ordinal()])
      {
      default:
        this.dataSource.firstLoad = false;
        if (this.loadingView.isShown())
          this.loadingView.setVisibility(8);
        return;
      case 1:
      case 2:
      case 3:
      case 4:
      }
    }
    catch (JSONException paramObject)
    {
      while (true)
      {
        paramObject.printStackTrace();
        continue;
        updateFilter();
        this.filterBar.setVisibility(0);
        resetCategoryFilter();
        setCategoryFilterStatus();
        this.categoryFilterIcon.setVisibility(0);
        this.shopListErrorView.setVisibility(8);
        if (!this.dataSource.isZeus)
        {
          this.shopListEmptyView.setVisibility(8);
          this.shopListAdapter.setShopList(this.dataSource);
          continue;
        }
        publish("data", this.dataSource.zeusShopList);
        continue;
        updateFilter();
        this.filterBar.setVisibility(0);
        setCategoryFilterStatus();
        this.categoryFilterIcon.setVisibility(0);
        this.shopListErrorView.setVisibility(8);
        if (!this.dataSource.isZeus)
        {
          this.shopListEmptyView.setVisibility(0);
          continue;
        }
        publish("data", this.dataSource.zeusShopList);
        continue;
        this.filterBar.setVisibility(8);
        this.categoryFilterIcon.setVisibility(4);
        if (this.shopListEmptyView != null)
          this.shopListEmptyView.setVisibility(8);
        this.shopListErrorView.setVisibility(8);
        if ((this.dataSource.isZeus) && (paramDataStatus == BookingShoplistDataSource.DataStatus.ERROR_NETWORK) && (!this.dataSource.firstLoad))
        {
          publish("error", new JSONObject().toString());
          continue;
        }
        this.shopListErrorView.setVisibility(0);
        ViewUtils.setVisibilityAndContent((TextView)this.shopListErrorView.findViewById(R.id.msgText), this.dataSource.errorMsg());
      }
    }
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 10) && (paramInt2 == -1))
    {
      paramIntent = paramIntent.getStringExtra("keyword");
      if ((TextUtils.isEmpty(paramIntent)) || (!paramIntent.equals(this.dataSource.keyword)));
    }
    else
    {
      return;
    }
    this.dataSource.keyword = paramIntent;
    this.searchBarContentView.setText(this.dataSource.keyword);
    reloadBookingShopList();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Intent localIntent = getIntent();
    localIntent.setData(localIntent.getData().buildUpon().appendQueryParameter("notitlebar", "1").build());
    setContentView(R.layout.booking_shoplist_main_layout);
    super.getWindow().setBackgroundDrawable(null);
    hideTitleBar();
    fetchParams();
    if (paramBundle != null)
    {
      this.dataSource.keyword = paramBundle.getString("keyword");
      this.dataSource.bookingPerson = paramBundle.getInt("bookingperson");
      this.dataSource.bookingTime = paramBundle.getLong("bookingtime");
      this.dataSource.setCurRegion((DPObject)paramBundle.getParcelable("curregion"));
      this.dataSource.setCurRange((DPObject)paramBundle.getParcelable("currange"));
      this.dataSource.setCurCategory((DPObject)paramBundle.getParcelable("curcategory"));
      this.dataSource.setCurSort((DPObject)paramBundle.getParcelable("cursort"));
    }
    initViews();
    if (!this.dataSource.isZeus)
      this.dataSource.loadData(0, true);
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("keyword", this.dataSource.keyword);
    paramBundle.putInt("bookingperson", this.dataSource.bookingPerson);
    paramBundle.putLong("bookingtime", this.dataSource.bookingTime);
    paramBundle.putParcelable("curregion", this.dataSource.curRegion());
    paramBundle.putParcelable("currange", this.dataSource.curRange());
    paramBundle.putParcelable("curcategory", this.dataSource.curCategory());
    paramBundle.putParcelable("cursort", this.dataSource.curSort());
    super.onSaveInstanceState(paramBundle);
  }

  public void onWindowFocusChanged(boolean paramBoolean)
  {
    super.onWindowFocusChanged(paramBoolean);
    if (this.bookingFilterViewStatus == BookingCategoryFilterViewStatus.FILTER_STATUS_OPENED)
      removeTitleBarShadow();
  }

  public void ready()
  {
    if ((this.mWebFragment != null) && (this.mWebFragment.isActivated()))
      this.dataSource.loadData(0, true);
  }

  public void reload(boolean paramBoolean)
  {
    if (this.dataSource != null)
      this.dataSource.reload(paramBoolean);
  }

  public void reloadBookingShopList()
  {
    updateFilter();
    this.dataSource.reset(true);
    this.dataSource.setLoadingDataStatus();
    JSONObject localJSONObject;
    if (this.dataSource.isZeus)
      localJSONObject = new JSONObject();
    while (true)
    {
      try
      {
        localJSONObject.put("action", "booking:shoplist:loading");
        this.mWebFragment.publish(localJSONObject);
        this.dataSource.reload(true);
        return;
      }
      catch (JSONException localJSONException)
      {
        localJSONException.printStackTrace();
        continue;
      }
      this.shopListAdapter.setShopList(this.dataSource);
    }
  }

  private static enum BookingCategoryFilterViewStatus
  {
    static
    {
      FILTER_STATUS_CLOSED = new BookingCategoryFilterViewStatus("FILTER_STATUS_CLOSED", 1);
      FILTER_STATUS_OPENED = new BookingCategoryFilterViewStatus("FILTER_STATUS_OPENED", 2);
      $VALUES = new BookingCategoryFilterViewStatus[] { FILTER_STATUS_MOVING, FILTER_STATUS_CLOSED, FILTER_STATUS_OPENED };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.BookingShoplistActvity
 * JD-Core Version:    0.6.0
 */