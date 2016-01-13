package com.dianping.booking;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.util.NovaConfigUtils;
import com.dianping.base.widget.CustomImageButton;
import com.dianping.base.widget.TitleBar;
import com.dianping.booking.adapter.BookingChannelAdapter;
import com.dianping.booking.fragment.BookingPresetFilterFragment;
import com.dianping.booking.fragment.BookingPresetFilterFragment.BookingInfoListener;
import com.dianping.booking.util.OrderSource;
import com.dianping.booking.view.BookingLazyScrollView;
import com.dianping.booking.view.BookingLazyScrollView.OnScrollListener;
import com.dianping.booking.view.BookingShopListItem;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.UserProfile;
import com.dianping.util.DeviceUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.style;
import com.dianping.widget.view.GAHelper;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class BookingMainActivity extends NovaActivity
{
  public static final int ACTIVITY_NUM_COLUMNS = 2;
  private static final String MY_BOOKING_LIST_URL = "dianping://bookinglist";
  private static final int PROMO_IMAGE_HEIGHT = 93;
  private static final int RECOMMEND_SHOPLIST_ITEM_HEIGHT = 100;
  private static final Object RECOMMEND_SHOPLIST_TAG = new Object();
  private TextView bookableShopNumView;
  private TextView businessView;
  private RelativeLayout categoryLayout;
  private BookingLazyScrollView channelView;
  private TextView cityNameView;
  private TextView coupleView;
  private TextView familyView;
  private BookingPresetFilterFragment filterFragment;
  private LinearLayout fixedShoplistTitlelayout;
  private List<TitleViewHolder> fixedTitleViewHolders;
  private TextView friendView;
  private MApiRequest getChannelInfoRequest;
  private MApiRequest getNewUserTicketInfoRequest;
  private View lineAboveCategoryLayout;
  private View lineAbovePromoView;
  private View lineBelowCategoryLayout;
  private View lineBelowPromoView;
  private View loadingView;
  private BookingChannelAdapter mPromosAdapter;
  private GridView promoView;
  private List<DPObject> promosList = new ArrayList();
  private List<DPObject> recommendShopList = new ArrayList();
  private BookingRecommendShopListAdapter recommendShopListAdapter;
  private ListView recommendShoplistView;
  private TextView searchArea;
  private Button searchBtn;
  private int selectedTitleIndex = 0;
  private TextView shoplistSubTitleView;
  private LinearLayout shoplistTitleLayout;
  private Dialog ticketPopUpDialog;
  private List<TitleViewHolder> titleViewHolders;
  private RelativeLayout wholeBookableLayout;

  private void getBookingChannelInfoTask(String paramString1, String paramString2, int paramInt)
  {
    if (this.getChannelInfoRequest != null)
      return;
    paramString2 = String.format("%s%s?clientUUID=%s&cityID=%s", new Object[] { "http://rs.api.dianping.com/", "getbookingfacade.yy", paramString2, Integer.valueOf(paramInt) });
    if (TextUtils.isEmpty(paramString1))
    {
      paramString1 = paramString2;
      if (super.location() != null)
        break label109;
    }
    while (true)
    {
      this.getChannelInfoRequest = BasicMApiRequest.mapiGet(paramString1, CacheType.CRITICAL);
      mapiService().exec(this.getChannelInfoRequest, new RequestHandler()
      {
        public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
        {
          BookingMainActivity.this.loadingView.setVisibility(8);
          BookingMainActivity.this.channelView.setVisibility(0);
          BookingMainActivity.this.showShortToast("错误：网络不给力哦");
          BookingMainActivity.access$902(BookingMainActivity.this, null);
        }

        public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
        {
          if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
          {
            BookingMainActivity.this.loadingView.setVisibility(8);
            BookingMainActivity.this.channelView.setVisibility(0);
            BookingMainActivity.this.setupViewData((DPObject)paramMApiResponse.result());
          }
          BookingMainActivity.access$902(BookingMainActivity.this, null);
        }
      });
      return;
      paramString1 = String.format("%s&token=%s", new Object[] { paramString2, paramString1 });
      break;
      label109: paramString1 = String.format("%s&mylat=%s&mylng=%s", new Object[] { paramString1, Location.FMT.format(super.location().latitude()), Location.FMT.format(super.location().longitude()) });
    }
  }

  private void getNewUserTicketInfoTask(String paramString)
  {
    if (this.getNewUserTicketInfoRequest != null)
      return;
    this.getNewUserTicketInfoRequest = BasicMApiRequest.mapiGet(String.format("%stoken=%s", new Object[] { "http://rs.api.dianping.com/genticket4user.yy?", paramString }), CacheType.DISABLED);
    mapiService().exec(this.getNewUserTicketInfoRequest, new RequestHandler()
    {
      public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        BookingMainActivity.access$502(BookingMainActivity.this, null);
      }

      public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
      {
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          if (paramMApiRequest != null)
          {
            BookingMainActivity.this.setupTicketInfoData(paramMApiRequest);
            BookingMainActivity.this.statisticsEvent("booking6", "booking6_fanpiao_fornewuser", "channel", 0);
          }
        }
        BookingMainActivity.access$502(BookingMainActivity.this, null);
      }
    });
  }

  private List<TitleViewHolder> getShopListTitleViewHolders(DPObject[] paramArrayOfDPObject)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if (i < paramArrayOfDPObject.length)
    {
      DPObject localDPObject = paramArrayOfDPObject[i];
      String str = localDPObject.getString("Title");
      TitleViewHolder localTitleViewHolder = new TitleViewHolder(LayoutInflater.from(this).inflate(R.layout.booking_recommend_shoplist_title_item, null, false));
      localTitleViewHolder.setTitle(str);
      localTitleViewHolder.setOnClickListener(new BookingMainActivity.TitleViewHolder.OnClickListener(localArrayList, localDPObject)
      {
        public void onClick(BookingMainActivity.TitleViewHolder paramTitleViewHolder)
        {
          int j = this.val$viewHolderList.indexOf(paramTitleViewHolder);
          if (BookingMainActivity.this.selectedTitleIndex != j)
          {
            BookingMainActivity.access$302(BookingMainActivity.this, j);
            int i = 0;
            if (i < this.val$viewHolderList.size())
            {
              paramTitleViewHolder = (BookingMainActivity.TitleViewHolder)BookingMainActivity.this.titleViewHolders.get(i);
              if (j == i)
              {
                bool = true;
                label71: paramTitleViewHolder.setSelected(bool);
                paramTitleViewHolder = (BookingMainActivity.TitleViewHolder)BookingMainActivity.this.fixedTitleViewHolders.get(i);
                if (j != i)
                  break label121;
              }
              label121: for (boolean bool = true; ; bool = false)
              {
                paramTitleViewHolder.setSelected(bool);
                i += 1;
                break;
                bool = false;
                break label71;
              }
            }
            BookingMainActivity.this.updateRecommendShopList(this.val$shopListInfo);
            BookingMainActivity.this.statisticsEvent("booking6", "booking6_channel_tab_change", "", j + 1);
          }
        }
      });
      if (i == 0)
      {
        localTitleViewHolder.setSelected(true);
        updateRecommendShopList(localDPObject);
      }
      while (true)
      {
        localArrayList.add(localTitleViewHolder);
        i += 1;
        break;
        localTitleViewHolder.setSelected(false);
      }
    }
    return localArrayList;
  }

  private String getToken()
  {
    if (super.getAccount() == null)
      return "";
    return super.getAccount().token();
  }

  private void initViewLayout()
  {
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
    localLayoutParams.setMargins(0, 0, ViewUtils.dip2px(this, -12.0F), 0);
    CustomImageButton localCustomImageButton = new CustomImageButton(this);
    localCustomImageButton.setLayoutParams(localLayoutParams);
    localCustomImageButton.setBackgroundColor(getResources().getColor(17170445));
    localCustomImageButton.setImageDrawable(getResources().getDrawable(R.drawable.booking_mylist_icon));
    getTitleBar().addRightViewItem(localCustomImageButton, "mylisticon", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        BookingMainActivity.this.startActivity("dianping://bookinglist");
        BookingMainActivity.this.statisticsEvent("booking6", "booking6_channel_myentry", "", 0);
      }
    });
    this.loadingView = findViewById(R.id.loading_view);
    this.loadingView.setVisibility(0);
    this.channelView = ((BookingLazyScrollView)findViewById(R.id.channel_view));
    this.channelView.setVisibility(8);
    this.filterFragment = ((BookingPresetFilterFragment)getSupportFragmentManager().findFragmentById(R.id.picker_fragment));
    this.filterFragment.getIcon().setVisibility(8);
    this.filterFragment.setBookingInfoListener(new BookingPresetFilterFragment.BookingInfoListener()
    {
      public void addGA()
      {
        GAHelper.instance().contextStatisticsEvent(BookingMainActivity.this, "condition", null, "tap");
        BookingMainActivity.this.statisticsEvent("booking6", "booking6_channel_condition", "", 0);
      }

      public void onBookingInfoChanged(int paramInt, long paramLong)
      {
      }
    });
    this.searchArea = ((TextView)findViewById(R.id.search_layout));
    this.searchArea.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = String.format("dianping://bookingsearch?ordersource=%s&bookingdate=%s&bookingpersonnum=%s", new Object[] { Integer.valueOf(OrderSource.KeyWordSearch.fromType), Long.valueOf(BookingMainActivity.access$000(BookingMainActivity.this).getBookingTime()), Integer.valueOf(BookingMainActivity.access$000(BookingMainActivity.this).getBookingPerson()) });
        BookingMainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramView)));
        BookingMainActivity.this.overridePendingTransition(0, 0);
        BookingMainActivity.this.statisticsEvent("booking6", "booking6_channel_searchbox", "", 0);
        GAHelper.instance().contextStatisticsEvent(BookingMainActivity.this, "keyword", null, "tap");
      }
    });
    this.searchBtn = ((Button)findViewById(R.id.search_btn));
    this.searchBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = String.format("dianping://bookingshoplist?ordersource=%s&bookingdate=%s&bookingpersonnum=%s", new Object[] { Integer.valueOf(OrderSource.SearchBtn.fromType), Long.valueOf(BookingMainActivity.access$000(BookingMainActivity.this).getBookingTime()), Integer.valueOf(BookingMainActivity.access$000(BookingMainActivity.this).getBookingPerson()) });
        BookingMainActivity.this.redirectShopListPage(paramView);
        BookingMainActivity.this.statisticsEvent("booking6", "booking6_channel_searchbutton", "", 0);
        GAHelper.instance().contextStatisticsEvent(BookingMainActivity.this, "search", null, "tap");
      }
    });
    this.cityNameView = ((TextView)findViewById(R.id.city_name));
    this.cityNameView.setText(city().name());
    this.bookableShopNumView = ((TextView)findViewById(R.id.bookable_shop_num));
    this.wholeBookableLayout = ((RelativeLayout)findViewById(R.id.whole_bookable_layout));
    this.wholeBookableLayout.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        BookingMainActivity.this.redirectShopListPage(String.format("dianping://bookingshoplist?ordersource=%s", new Object[] { Integer.valueOf(OrderSource.WholeShopsSearch.fromType) }));
        BookingMainActivity.this.statisticsEvent("booking6", "booking6_channel_seeall", "", 0);
      }
    });
    this.categoryLayout = ((RelativeLayout)findViewById(R.id.category_layout));
    this.lineAboveCategoryLayout = findViewById(R.id.line_above_category_layout);
    this.lineBelowCategoryLayout = findViewById(R.id.line_below_category_layout);
    this.businessView = ((TextView)findViewById(R.id.business));
    this.coupleView = ((TextView)findViewById(R.id.couple));
    this.friendView = ((TextView)findViewById(R.id.friend));
    this.familyView = ((TextView)findViewById(R.id.family));
    this.promoView = ((GridView)findViewById(R.id.promo_view));
    this.lineAbovePromoView = findViewById(R.id.line_above_promo_view);
    this.lineBelowPromoView = findViewById(R.id.line_below_promo_view);
    this.mPromosAdapter = new BookingChannelAdapter(this, this.promosList, 2);
    this.promoView.setAdapter(this.mPromosAdapter);
    this.fixedShoplistTitlelayout = ((LinearLayout)findViewById(R.id.fixed_recommend_shoplist_title));
    this.fixedShoplistTitlelayout.setVisibility(8);
    this.shoplistTitleLayout = ((LinearLayout)findViewById(R.id.recommend_shoplist_title));
    this.shoplistSubTitleView = ((TextView)findViewById(R.id.recommend_shoplist_subtitle));
    this.recommendShoplistView = ((ListView)findViewById(R.id.recommend_shoplist));
    this.recommendShopListAdapter = new BookingRecommendShopListAdapter(null);
    this.recommendShoplistView.setAdapter(this.recommendShopListAdapter);
    this.recommendShoplistView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
        if ((paramAdapterView instanceof DPObject))
        {
          paramAdapterView = (DPObject)paramAdapterView;
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + paramAdapterView.getInt("ID")));
          paramView.putExtra("shopId", paramAdapterView.getInt("ID"));
          paramView.putExtra("ordersource", OrderSource.RecommendedShops.fromType);
          BookingMainActivity.this.startActivity(paramView);
          BookingMainActivity.this.statisticsEvent("booking6", "booking6_channel_tab_shopclick", "" + BookingMainActivity.this.selectedTitleIndex + 1, paramInt + 1);
        }
      }
    });
  }

  private void redirectShopListPage(String paramString)
  {
    super.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramString)));
  }

  private void setCategoryItem(TextView paramTextView, DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    ViewUtils.setVisibilityAndContent(paramTextView, paramDPObject.getString("Title"));
    paramTextView.setOnClickListener(new View.OnClickListener(paramDPObject)
    {
      public void onClick(View paramView)
      {
        paramView = this.val$data.getString("ActionUrl");
        if (!TextUtils.isEmpty(paramView))
          BookingMainActivity.this.redirectShopListPage(String.format("%s&ordersource=%s", new Object[] { paramView, Integer.valueOf(OrderSource.SceneBooking.fromType) }));
        BookingMainActivity.this.statisticsEvent("booking6", "booking6_channel_scene", this.val$data.getString("Title"), 0);
      }
    });
  }

  private void setupCategorysData(DPObject[] paramArrayOfDPObject)
  {
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length < 4))
      return;
    setCategoryItem(this.businessView, paramArrayOfDPObject[0]);
    setCategoryItem(this.coupleView, paramArrayOfDPObject[1]);
    setCategoryItem(this.friendView, paramArrayOfDPObject[2]);
    setCategoryItem(this.familyView, paramArrayOfDPObject[3]);
    this.categoryLayout.setVisibility(0);
    this.lineAboveCategoryLayout.setVisibility(0);
    this.lineBelowCategoryLayout.setVisibility(0);
  }

  private void setupPromosData(DPObject[] paramArrayOfDPObject)
  {
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length == 0))
      return;
    this.promosList.clear();
    this.promosList.addAll(Arrays.asList(paramArrayOfDPObject));
    this.mPromosAdapter.notifyDataSetChanged();
    this.promoView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        paramAdapterView = (DPObject)BookingMainActivity.this.promosList.get(paramInt);
        if ((paramAdapterView != null) && (!TextUtils.isEmpty(paramAdapterView.getString("ActionUrl"))))
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + paramAdapterView.getString("ActionUrl")));
          BookingMainActivity.this.startActivity(paramView);
          BookingMainActivity.this.statisticsEvent("booking6", "booking6_channel_activity", paramAdapterView.getString("Title"), paramInt + 1);
          GAHelper.instance().contextStatisticsEvent(BookingMainActivity.this, "activity", null, paramInt + 1, "tap");
        }
      }
    });
    paramArrayOfDPObject = new LinearLayout.LayoutParams(-1, (int)Math.ceil(paramArrayOfDPObject.length / 2) * ViewUtils.dip2px(this, 93.0F) + 1);
    this.promoView.setLayoutParams(paramArrayOfDPObject);
    this.promoView.setVisibility(0);
    this.lineAbovePromoView.setVisibility(0);
    this.lineBelowPromoView.setVisibility(0);
    GAHelper.instance().contextStatisticsEvent(this, "activity", null, "view");
  }

  private void setupRecommendShopListTitleView(LinearLayout paramLinearLayout, List<TitleViewHolder> paramList)
  {
    paramLinearLayout.removeAllViews();
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, ViewUtils.dip2px(this, 50.0F), 1.0F);
    paramList = paramList.iterator();
    while (paramList.hasNext())
      paramLinearLayout.addView(((TitleViewHolder)paramList.next()).parent, localLayoutParams);
  }

  private void setupTicketInfoData(DPObject paramDPObject)
  {
    View localView = LayoutInflater.from(this).inflate(R.layout.booking_channel_newuser_ticket, null, false);
    Object localObject = (TextView)localView.findViewById(R.id.ticket_price);
    TextView localTextView1 = (TextView)localView.findViewById(R.id.ticket_use_rule);
    TextView localTextView2 = (TextView)localView.findViewById(R.id.congratulation_text1);
    TextView localTextView3 = (TextView)localView.findViewById(R.id.congratulation_text2);
    TextView localTextView4 = (TextView)localView.findViewById(R.id.congratulation_text3);
    Button localButton = (Button)localView.findViewById(R.id.ticket_known);
    this.ticketPopUpDialog = new Dialog(this, R.style.dialog);
    this.ticketPopUpDialog.setContentView(localView);
    ((TextView)localObject).setText(Integer.toString(paramDPObject.getInt("Deduce")));
    localTextView1.setText(paramDPObject.getString("UnderSectionMessage"));
    localTextView2.setText(paramDPObject.getString("Message"));
    localTextView3.setText(paramDPObject.getString("SubMessage"));
    localTextView4.setText(paramDPObject.getString("ThirdMessage"));
    paramDPObject = this.ticketPopUpDialog.getWindow();
    localObject = paramDPObject.getAttributes();
    ((WindowManager.LayoutParams)localObject).dimAmount = 0.8F;
    paramDPObject.setAttributes((WindowManager.LayoutParams)localObject);
    paramDPObject.setFlags(2, 2);
    this.ticketPopUpDialog.show();
    localView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.booking_channel_ticket_popup));
    localButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        BookingMainActivity.this.ticketPopUpDialog.dismiss();
      }
    });
    this.ticketPopUpDialog.setCanceledOnTouchOutside(false);
  }

  private void setupViewData(DPObject paramDPObject)
  {
    int i = paramDPObject.getInt("BookingPerson");
    this.filterFragment.updateBookingInfo(paramDPObject.getTime("BookingTime"), i);
    this.bookableShopNumView.setText(paramDPObject.getInt("TotalCount") + "家");
    setupPromosData(paramDPObject.getArray("ActivityItems"));
    setupCategorysData(paramDPObject.getArray("CategoryItems"));
    paramDPObject = paramDPObject.getArray("RecommendShops");
    if ((paramDPObject != null) && (paramDPObject.length != 0))
    {
      this.titleViewHolders = getShopListTitleViewHolders(paramDPObject);
      this.fixedTitleViewHolders = getShopListTitleViewHolders(paramDPObject);
      setupRecommendShopListTitleView(this.shoplistTitleLayout, this.titleViewHolders);
      setupRecommendShopListTitleView(this.fixedShoplistTitlelayout, this.fixedTitleViewHolders);
      this.shoplistTitleLayout.setVisibility(0);
      this.recommendShoplistView.setVisibility(0);
      this.channelView.setOnScrollListener(new BookingLazyScrollView.OnScrollListener()
      {
        public void onAutoScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
        {
          if (paramInt2 > BookingMainActivity.this.shoplistTitleLayout.getTop())
          {
            BookingMainActivity.this.fixedShoplistTitlelayout.setVisibility(0);
            return;
          }
          BookingMainActivity.this.fixedShoplistTitlelayout.setVisibility(8);
        }
      });
      return;
    }
    this.shoplistTitleLayout.setVisibility(8);
    this.shoplistSubTitleView.setVisibility(8);
    this.recommendShoplistView.setVisibility(8);
  }

  private void updateRecommendShopList(DPObject paramDPObject)
  {
    ViewUtils.setVisibilityAndContent(this.shoplistSubTitleView, paramDPObject.getString("SubTitle"));
    this.recommendShopList.clear();
    this.recommendShopList.addAll(Arrays.asList(paramDPObject.getArray("ShopList")));
    this.recommendShopListAdapter.notifyDataSetChanged();
    paramDPObject = new LinearLayout.LayoutParams(-1, this.recommendShopList.size() * (ViewUtils.dip2px(this, 100.0F) + 1));
    this.recommendShoplistView.setLayoutParams(paramDPObject);
    this.recommendShoplistView.setSelection(0);
  }

  public String getPageName()
  {
    return "bookingmain";
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getWindow().setBackgroundDrawable(null);
    super.setContentView(R.layout.booking_channel_layout);
    initViewLayout();
    getBookingChannelInfoTask(getToken(), Environment.uuid(), cityId());
    if (!TextUtils.isEmpty(getToken()))
      getNewUserTicketInfoTask(getToken());
    paramBundle = super.getStringParam("push");
    if (!TextUtils.isEmpty(paramBundle))
      super.statisticsEvent("mybooking6", "mybooking6_order_push", paramBundle, 0);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.getChannelInfoRequest != null)
    {
      mapiService().abort(this.getChannelInfoRequest, null, true);
      this.getChannelInfoRequest = null;
    }
    if (this.getNewUserTicketInfoRequest != null)
    {
      mapiService().abort(this.getNewUserTicketInfoRequest, null, true);
      this.getNewUserTicketInfoRequest = null;
    }
  }

  private class BookingRecommendShopListAdapter extends BaseAdapter
  {
    private BookingRecommendShopListAdapter()
    {
    }

    public int getCount()
    {
      return BookingMainActivity.this.recommendShopList.size();
    }

    public Object getItem(int paramInt)
    {
      return BookingMainActivity.this.recommendShopList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if (paramView != null)
      {
        paramViewGroup = paramView;
        if (paramView.getTag() == BookingMainActivity.RECOMMEND_SHOPLIST_TAG);
      }
      else
      {
        paramViewGroup = new BookingShopListItem(BookingMainActivity.this, null);
        paramViewGroup.setTag(BookingMainActivity.RECOMMEND_SHOPLIST_TAG);
      }
      ((BookingShopListItem)paramViewGroup).bindView((DPObject)localObject, -1, NovaConfigUtils.isShowImageInMobileNetwork(), DeviceUtils.isCurrentCity());
      return paramViewGroup;
    }
  }

  private static class TitleViewHolder
  {
    private static final int COLOR_GRAY = -13421773;
    private static final int COLOR_LIGHT_RED = -39373;
    private OnClickListener listener;
    private View parent;
    private TextView titleContentView;
    private View titleHighlight;

    public TitleViewHolder(View paramView)
    {
      this.parent = paramView;
      this.titleContentView = ((TextView)paramView.findViewById(R.id.title_content));
      this.titleHighlight = paramView.findViewById(R.id.title_highlight);
    }

    public void setOnClickListener(OnClickListener paramOnClickListener)
    {
      this.listener = paramOnClickListener;
      this.parent.setOnClickListener(new BookingMainActivity.TitleViewHolder.1(this));
    }

    public void setSelected(boolean paramBoolean)
    {
      this.parent.setSelected(paramBoolean);
      if (paramBoolean)
      {
        this.titleContentView.setTextColor(-39373);
        this.titleHighlight.setVisibility(0);
        return;
      }
      this.titleContentView.setTextColor(-13421773);
      this.titleHighlight.setVisibility(8);
    }

    public void setTitle(String paramString)
    {
      this.titleContentView.setText(paramString);
    }

    static abstract interface OnClickListener
    {
      public abstract void onClick(BookingMainActivity.TitleViewHolder paramTitleViewHolder);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.BookingMainActivity
 * JD-Core Version:    0.6.0
 */