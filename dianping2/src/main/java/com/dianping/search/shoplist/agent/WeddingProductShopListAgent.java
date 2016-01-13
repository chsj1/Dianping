package com.dianping.search.shoplist.agent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.shoplist.agent.ShopListAgent;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.search.shoplist.fragment.ShopListAgentFragment;
import com.dianping.search.shoplist.fragment.agentconfig.WeddingShopListAgentConfig;
import com.dianping.search.util.WedShopListUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.NetworkImageView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersListAdapterWrapper;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class WeddingProductShopListAgent extends ShopListAgent
{
  private static final String CELL_SHOP_LIST = "050WeddingShopList";
  public static final String COVER_STYLE_TYPE = "CoverStyleType";
  public static final String ERROR_MSG = "ErrorMsg";
  public static final String IS_END = "IsEnd";
  public static final String SHOP_LIST = "List";
  private final int SCREEN_WIDTH = getResources().getResources().getDisplayMetrics().widthPixels;
  int coverStyleType;
  TextView emptyView;
  String errorMsg;
  StickyGridHeadersGridView gridView;
  boolean initLoad = true;
  boolean isEnd;
  LinearLayout layoutHeader;
  boolean mFirstLoading = true;
  StickyGridHeadersListAdapterWrapper mHeaderWrapper;
  int mHorizontalGap;
  boolean mIsLoading = false;
  int mLandScapeHeight;
  int mPortraintHeight;
  int mPortraitWidth;
  MApiRequest mRequest;
  private RequestHandler mRequestHandler = new RequestHandler()
  {
    public void onRequestFailed(Request paramRequest, Response paramResponse)
    {
      WeddingProductShopListAgent.this.mIsLoading = false;
      WeddingProductShopListAgent.this.setSharedObject("ErrorMsg", "网络加载失败 请重新尝试");
      Toast.makeText(WeddingProductShopListAgent.this.getContext(), "网络加载失败 请重新尝试", 0).show();
      ((ShopListAgentFragment)WeddingProductShopListAgent.this.getFragment()).changeViewStatus(8);
    }

    public void onRequestFinish(Request paramRequest, Response paramResponse)
    {
      if (paramRequest != WeddingProductShopListAgent.this.mRequest)
        return;
      WeddingProductShopListAgent.this.mIsLoading = false;
      WeddingProductShopListAgent.this.mRequest = null;
      WedShopListUtils.getWedConfig((ShopListAgentFragment)WeddingProductShopListAgent.this.getFragment()).parseData((DPObject)paramResponse.result());
      if (WeddingProductShopListAgent.this.mFirstLoading)
      {
        WeddingProductShopListAgent.this.mFirstLoading = false;
        WeddingProductShopListAgent.this.updateGridView(true);
        WeddingProductShopListAgent.this.updateCityPanel();
      }
      while (true)
      {
        ((ShopListAgentFragment)WeddingProductShopListAgent.this.getFragment()).changeViewStatus(8);
        return;
        WeddingProductShopListAgent.this.updateGridView(false);
      }
    }
  };
  WeddingTypeShopListAdapter mShopListAdapter;
  ArrayList<DPObject> mWeddingShopList = new ArrayList();
  MyResources res = MyResources.getResource(WeddingProductShopListAgent.class);
  View wedProductListPanel;

  public WeddingProductShopListAgent(Object paramObject)
  {
    super(paramObject);
  }

  private LinearLayout getCityLine(int paramInt1, int paramInt2)
  {
    LinearLayout localLinearLayout = new LinearLayout(getContext());
    localLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, paramInt1));
    localLinearLayout.setBackgroundColor(this.res.getColor(R.color.gray_light_background));
    localLinearLayout.setOrientation(0);
    localLinearLayout.setGravity(16);
    localLinearLayout.setPadding(0, paramInt2, 0, 0);
    return localLinearLayout;
  }

  private TextView getCityTag(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    if (TextUtils.isEmpty(paramString))
      return null;
    TextView localTextView = new TextView(getContext());
    localTextView.setText(paramString);
    localTextView.setHeight(paramInt1);
    localTextView.setGravity(17);
    localTextView.setTag(paramString);
    localTextView.setTextColor(this.res.getColor(R.color.dark_black));
    localTextView.setTextSize(14.0F);
    localTextView.setBackgroundResource(R.drawable.wed_product_city_tag);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
    localLayoutParams.setMargins(paramInt3, 0, 0, 0);
    localTextView.setLayoutParams(localLayoutParams);
    if (paramString.length() >= 4)
      localTextView.setWidth(paramInt2 * 2 + paramInt3);
    while (true)
    {
      localTextView.setOnClickListener(new CityTagOnClickListener(null));
      return localTextView;
      localTextView.setWidth(paramInt2);
    }
  }

  private boolean isBabyProduct()
  {
    DPObject localDPObject = (DPObject)getFragment().sharedObject("curCategory");
    if (localDPObject != null)
    {
      int i = localDPObject.getInt("ID");
      if ((i == 193) || (i == 27814) || (i == 27813) || (i == 2782))
        return true;
    }
    return false;
  }

  private boolean isTravelProduct()
  {
    return ((DPObject)getFragment().sharedObject("curCategory")).getInt("ProductCategoryID") == 1632;
  }

  private void loadData(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      ((ShopListAgentFragment)getFragment()).changeViewStatus(0);
      getFragment().setSharedObject("Page", Integer.valueOf(1));
    }
    this.mIsLoading = true;
    this.mRequest = WedShopListUtils.getWedConfig((ShopListAgentFragment)getFragment()).createListRequest();
    if (this.mRequest != null)
      mapiService().exec(this.mRequest, this.mRequestHandler);
  }

  private void restRequestData()
  {
    getFragment().setSharedObject("Page", Integer.valueOf(1));
    getFragment().setSharedObject("IsEnd", Boolean.valueOf(false));
  }

  private void setupView()
  {
    if (this.gridView == null)
    {
      this.wedProductListPanel = LayoutInflater.from(getContext()).inflate(R.layout.wed_product_shop_list, getParentView(), false);
      this.gridView = ((StickyGridHeadersGridView)this.wedProductListPanel.findViewById(R.id.gallery_gridview));
      this.emptyView = ((TextView)this.wedProductListPanel.findViewById(R.id.empty_view));
      Drawable localDrawable = this.res.getDrawable(R.drawable.empty_page_nothing);
      localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
      this.emptyView.setCompoundDrawablePadding(8);
      this.emptyView.setCompoundDrawables(localDrawable, null, null, null);
      this.emptyView.setMovementMethod(LinkMovementMethod.getInstance());
      this.emptyView.setText("暂无产品");
      this.mShopListAdapter = new WeddingTypeShopListAdapter();
      this.layoutHeader = new LinearLayout(getContext());
      this.gridView.setAreHeadersSticky(false);
      this.gridView.setStickyHeaderIsTranscluent(false);
      this.mHeaderWrapper = new StickyGridHeadersListAdapterWrapper(this.mShopListAdapter)
      {
        public int getCountForHeader(int paramInt)
        {
          return WeddingProductShopListAgent.this.mShopListAdapter.getCount();
        }

        public View getHeaderView(int paramInt, View paramView, ViewGroup paramViewGroup)
        {
          return WeddingProductShopListAgent.this.layoutHeader;
        }

        public int getNumHeaders()
        {
          if (WeddingProductShopListAgent.this.isTravelProduct())
            return 1;
          return 0;
        }
      };
      this.gridView.setAdapter(this.mHeaderWrapper);
      this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
        public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
        {
          if ((WeddingProductShopListAgent.this.mShopListAdapter.getItemViewType(paramInt) == 1) || (WeddingProductShopListAgent.this.mShopListAdapter.getItemViewType(paramInt) == 0))
          {
            paramAdapterView = (DPObject)WeddingProductShopListAgent.this.mShopListAdapter.getItem(paramInt);
            paramInt = paramAdapterView.getInt("ProductID");
            if (paramInt <= 0)
              break label259;
          }
          int i;
          label259: for (paramAdapterView = Uri.parse("dianping://babyproductdetail").buildUpon().appendQueryParameter("productid", String.valueOf(paramAdapterView.getInt("ProductID"))).appendQueryParameter("shopid", String.valueOf(paramAdapterView.getInt("ShopID"))).build(); ; paramAdapterView = Uri.parse("dianping://shopinfo?shopid=" + String.valueOf(paramAdapterView.getInt("ShopID"))))
          {
            paramAdapterView = new Intent("android.intent.action.VIEW", paramAdapterView);
            WeddingProductShopListAgent.this.startActivity(paramAdapterView);
            if (!WeddingProductShopListAgent.this.isTravelProduct())
              break;
            paramAdapterView = new ArrayList();
            paramView = (DPObject)WeddingProductShopListAgent.this.getFragment().sharedObject("curCategory");
            if (paramView != null)
            {
              i = paramView.getInt("ID");
              paramAdapterView.add(new BasicNameValuePair("category_id", i + ""));
            }
            paramAdapterView.add(new BasicNameValuePair("cityid", WeddingProductShopListAgent.this.cityId() + ""));
            WeddingProductShopListAgent.this.statisticsEvent("shopinfoq", "productsearch_scrpage_product", "", Integer.valueOf(paramInt).intValue(), paramAdapterView);
            return;
          }
          paramAdapterView = new ArrayList();
          paramView = (DPObject)WeddingProductShopListAgent.this.getFragment().sharedObject("curCategory");
          if (paramView != null)
          {
            i = paramView.getInt("ID");
            paramAdapterView.add(new BasicNameValuePair("category_id", i + ""));
          }
          paramAdapterView.add(new BasicNameValuePair("cityid", WeddingProductShopListAgent.this.cityId() + ""));
          WeddingProductShopListAgent.this.statisticsEvent("shopinfoq", "productsearch_page_product", "", Integer.valueOf(paramInt).intValue(), paramAdapterView);
        }
      });
      this.gridView.setOnScrollListener(new AbsListView.OnScrollListener()
      {
        public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
        {
          if ((paramInt1 + paramInt2 >= paramInt3) && (!WeddingProductShopListAgent.this.isEnd) && (paramInt3 > 2))
          {
            if (WeddingProductShopListAgent.this.getSharedObject("Page") != null)
            {
              paramInt1 = ((Integer)WeddingProductShopListAgent.this.getSharedObject("Page")).intValue();
              WeddingProductShopListAgent.this.getFragment().setSharedObject("Page", Integer.valueOf(paramInt1 + 1));
            }
            if (!WeddingProductShopListAgent.this.mIsLoading)
            {
              WeddingProductShopListAgent.this.mIsLoading = false;
              WeddingProductShopListAgent.this.loadData(false);
            }
          }
        }

        public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
        {
        }
      });
      addCell("050WeddingShopList", this.wedProductListPanel);
    }
    this.mHeaderWrapper.notifyDataSetChanged();
    this.mPortraitWidth = (this.SCREEN_WIDTH * 45 / 100);
    this.mPortraintHeight = (this.mPortraitWidth * 210 / 280);
    this.mLandScapeHeight = (this.mPortraitWidth * 350 / 280);
  }

  private void updateCityPanel()
  {
    Object localObject = (DPObject[])(DPObject[])getSharedObject("PhotoLocList");
    if ((localObject == null) || (localObject.length == 0));
    do
    {
      return;
      updateHeader(localObject);
      if (this.mHeaderWrapper == null)
        continue;
      this.mHeaderWrapper.notifyDataSetChanged();
    }
    while (((DPObject)getFragment().sharedObject("curCategory")).getInt("ProductCategoryID") != 1632);
    localObject = new ArrayList();
    ((List)localObject).add(new BasicNameValuePair("cityid", cityId() + ""));
    statisticsEvent("shopinfoq", "productsearch_scrpage", "", 0, (List)localObject);
  }

  @SuppressLint({"NewApi"})
  private void updateGridView(boolean paramBoolean)
  {
    Object localObject;
    int i;
    if ((Boolean)getSharedObject("IsEnd") == null)
    {
      localObject = Boolean.valueOf(false);
      this.isEnd = ((Boolean)(Boolean)localObject).booleanValue();
      if ((Integer)getSharedObject("CoverStyleType") != null)
        break label236;
      i = 1;
      label46: this.coverStyleType = i;
      this.errorMsg = ((String)getSharedObject("ErrorMsg"));
      if (getSharedObject("List") == null)
        return;
      if (paramBoolean)
        this.mWeddingShopList.clear();
      localObject = (DPObject[])(DPObject[])getSharedObject("List");
      if (((this.mWeddingShopList != null) && (this.mWeddingShopList.size() != 0)) || ((localObject != null) && (localObject.length != 0)))
        break label253;
      FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-2, -2);
      int j = 0;
      i = j;
      if (isTravelProduct())
      {
        i = j;
        if (this.layoutHeader != null)
          i = this.layoutHeader.getMeasuredHeight();
      }
      localLayoutParams.setMargins(0, i, 0, 0);
      this.emptyView.setLayoutParams(localLayoutParams);
      this.emptyView.setVisibility(0);
    }
    while (true)
    {
      i = 0;
      while (i < localObject.length)
      {
        this.mWeddingShopList.add(localObject[i]);
        i += 1;
      }
      localObject = getSharedObject("IsEnd");
      break;
      label236: i = ((Integer)getSharedObject("CoverStyleType")).intValue();
      break label46;
      label253: this.emptyView.setVisibility(8);
    }
    this.mShopListAdapter.notifyDataSetChanged();
    if ((paramBoolean) && (this.mWeddingShopList.size() > 1) && (this.gridView != null))
      this.gridView.smoothScrollToPositionFromTop(0, 0);
  }

  private void updateHeader(DPObject[] paramArrayOfDPObject)
  {
    this.layoutHeader.removeAllViews();
    int m = (this.SCREEN_WIDTH * 2 / 10 - ViewUtils.dip2px(getContext(), 20.0F)) / 5;
    int n = this.SCREEN_WIDTH * 8 / 10 / 4;
    int i1 = n * 95 / 216;
    this.layoutHeader.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    this.layoutHeader.setPadding(0, 0, 0, m);
    this.layoutHeader.setBackgroundColor(getResources().getColor(R.color.gray_light_background));
    this.layoutHeader.setOrientation(1);
    int i = 1;
    int k = 1;
    LinearLayout localLinearLayout = new LinearLayout(getContext());
    int i2 = paramArrayOfDPObject.length;
    int j = 0;
    while (j < i2)
    {
      Object localObject = paramArrayOfDPObject[j];
      if (i == 1)
        localLinearLayout = getCityLine(i1 + m, m);
      String str = ((DPObject)localObject).getString("Name");
      int i3 = ((DPObject)localObject).getInt("Type");
      localObject = getCityTag(str, i1, n, m);
      if (i3 == 1)
        ((TextView)localObject).setBackgroundResource(R.drawable.wed_product_city_tag_select);
      localLinearLayout.addView((View)localObject);
      if ((i == 4) || (k == paramArrayOfDPObject.length))
      {
        this.layoutHeader.addView(localLinearLayout);
        i = 0;
      }
      i += 1;
      k += 1;
      j += 1;
    }
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((-1 == paramInt2) && (paramIntent != null) && (!TextUtils.isEmpty(paramIntent.getStringExtra("city"))))
    {
      paramIntent = paramIntent.getStringExtra("city");
      setSharedObject("curPhotoLoc", new DPObject().edit().putString("ID", paramIntent).generate());
      this.mFirstLoading = true;
      this.initLoad = true;
      loadData(true);
      ArrayList localArrayList = new ArrayList(0);
      statisticsEvent("shopinfoq", "productsearch_scrpage_area", paramIntent, Integer.valueOf(cityId()).intValue(), localArrayList);
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    label33: if (!(getCurrentAgentConfig() instanceof WeddingShopListAgentConfig))
    {
      this.initLoad = true;
      this.mWeddingShopList.clear();
      removeCell("050WeddingShopList");
      break label33;
    }
    do
      return;
    while (this.mIsLoading);
    setupView();
    int i;
    if (getFragment().sharedObject("FOCUS_RELOAD") != null)
    {
      i = 1;
      label60: if ((paramBundle == null) || (!paramBundle.containsKey("shoplist/weddingnavigatorfilter")))
        break label218;
      this.mFirstLoading = true;
      label79: if ((!this.initLoad) && (i == 0))
        break label225;
      loadData(true);
      this.initLoad = false;
    }
    while (true)
    {
      updateCityPanel();
      paramBundle = (DPObject)getFragment().sharedObject("curCategory");
      if (paramBundle == null)
        break;
      i = paramBundle.getInt("ID");
      if (paramBundle.getInt("ProductCategoryID") == 1632)
        break;
      paramBundle = new ArrayList();
      paramBundle.add(new BasicNameValuePair("cityid", cityId() + ""));
      statisticsEvent("shopinfoq", "productsearch_page", "", Integer.valueOf(i).intValue(), paramBundle);
      return;
      i = 0;
      break label60;
      label218: restRequestData();
      break label79;
      label225: if (!this.mFirstLoading)
        continue;
      this.mFirstLoading = false;
      updateGridView(true);
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mRequest != null)
    {
      mapiService().abort(this.mRequest, this.mRequestHandler, true);
      this.mRequest = null;
    }
  }

  private class CityTagOnClickListener
    implements View.OnClickListener
  {
    private CityTagOnClickListener()
    {
    }

    public void onClick(View paramView)
    {
      paramView = (String)paramView.getTag();
      Object localObject;
      if ("更多".equals(paramView))
      {
        localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://babymorecitypage"));
        WeddingProductShopListAgent.this.startActivityForResult((Intent)localObject, 0);
      }
      while (true)
      {
        localObject = new ArrayList(0);
        WeddingProductShopListAgent.this.statisticsEvent("shopinfoq", "productsearch_scrpage_area", paramView, Integer.valueOf(WeddingProductShopListAgent.this.cityId()).intValue(), (List)localObject);
        return;
        WeddingProductShopListAgent.this.setSharedObject("curPhotoLoc", new DPObject().edit().putString("ID", paramView).generate());
        WeddingProductShopListAgent.this.mFirstLoading = true;
        WeddingProductShopListAgent.this.initLoad = true;
        WeddingProductShopListAgent.this.loadData(true);
      }
    }
  }

  public class WeddingTypeShopListAdapter extends BasicAdapter
  {
    public static final int TYPE_COUNT = 3;
    public static final int TYPE_PRODUCT_ITEM_BABY = 1;
    public static final int TYPE_PRODUCT_ITEM_WED = 0;
    public static final int TYPE_PROMPT_ITEM = 2;

    public WeddingTypeShopListAdapter()
    {
    }

    private Drawable getDrawableByPower(int paramInt)
    {
      switch (paramInt)
      {
      default:
        return null;
      case 0:
        return WeddingProductShopListAgent.this.res.getDrawable(R.drawable.star0);
      case 10:
        return WeddingProductShopListAgent.this.res.getDrawable(R.drawable.star10);
      case 20:
        return WeddingProductShopListAgent.this.res.getDrawable(R.drawable.star20);
      case 30:
        return WeddingProductShopListAgent.this.res.getDrawable(R.drawable.star30);
      case 35:
        return WeddingProductShopListAgent.this.res.getDrawable(R.drawable.star35);
      case 40:
        return WeddingProductShopListAgent.this.res.getDrawable(R.drawable.star40);
      case 45:
        return WeddingProductShopListAgent.this.res.getDrawable(R.drawable.star45);
      case 50:
      }
      return WeddingProductShopListAgent.this.res.getDrawable(R.drawable.star50);
    }

    private View initBabyHolder(ViewHolderBaby paramViewHolderBaby, ViewGroup paramViewGroup)
    {
      View localView = WeddingProductShopListAgent.this.res.inflate(WeddingProductShopListAgent.this.getContext(), R.layout.wed_baby_shoplist_item, paramViewGroup, false);
      paramViewGroup = paramViewHolderBaby;
      if (paramViewHolderBaby == null)
        paramViewGroup = new ViewHolderBaby();
      paramViewGroup.contentView = ((FrameLayout)localView).getChildAt(0);
      paramViewHolderBaby = (FrameLayout.LayoutParams)paramViewGroup.contentView.getLayoutParams();
      paramViewHolderBaby.rightMargin = WeddingProductShopListAgent.this.mHorizontalGap;
      paramViewGroup.contentView.setLayoutParams(paramViewHolderBaby);
      paramViewGroup.productPic = ((NetworkImageView)localView.findViewById(R.id.product_pic));
      paramViewGroup.productPrice = ((TextView)localView.findViewById(R.id.product_price));
      paramViewGroup.icTuan = ((ImageView)localView.findViewById(R.id.ic_tuan));
      paramViewGroup.icPromo = ((ImageView)localView.findViewById(R.id.ic_promo));
      paramViewGroup.icBook = ((ImageView)localView.findViewById(R.id.ic_book));
      paramViewGroup.productName = ((TextView)localView.findViewById(R.id.product_name));
      paramViewGroup.shopPower = ((ImageView)localView.findViewById(R.id.shop_power));
      paramViewGroup.productReviewCount = ((TextView)localView.findViewById(R.id.product_review_count));
      localView.setTag(paramViewGroup);
      return localView;
    }

    private View initWedHolder(ViewHolderWed paramViewHolderWed, ViewGroup paramViewGroup)
    {
      View localView = WeddingProductShopListAgent.this.res.inflate(WeddingProductShopListAgent.this.getContext(), R.layout.wed_shoplist_item, paramViewGroup, false);
      paramViewGroup = paramViewHolderWed;
      if (paramViewHolderWed == null)
        paramViewGroup = new ViewHolderWed();
      paramViewGroup.productPic = ((NetworkImageView)localView.findViewById(R.id.product_pic));
      paramViewGroup.txtName = ((TextView)localView.findViewById(R.id.product_name));
      paramViewGroup.textSimbol = ((TextView)localView.findViewById(R.id.text1));
      paramViewGroup.textPrice = ((TextView)localView.findViewById(R.id.text2));
      paramViewGroup.textOriPrice = ((TextView)localView.findViewById(R.id.text3));
      paramViewGroup.imgFeature = ((ImageView)localView.findViewById(R.id.imageview_feature));
      localView.setTag(paramViewGroup);
      return localView;
    }

    private void setupView(DPObject paramDPObject, ViewHolderBaby paramViewHolderBaby)
    {
      String str = paramDPObject.getString("DefaultPic");
      if (str != null)
        paramViewHolderBaby.productPic.setImage(str);
      int i;
      int j;
      if (WeddingProductShopListAgent.this.coverStyleType == 1)
      {
        paramViewHolderBaby.productPic.getLayoutParams().width = WeddingProductShopListAgent.this.mPortraitWidth;
        paramViewHolderBaby.productPic.getLayoutParams().height = WeddingProductShopListAgent.this.mPortraintHeight;
        i = paramDPObject.getInt("ShowPriceType");
        j = paramDPObject.getInt("Price");
        paramViewHolderBaby.productPrice.setTextColor(WeddingProductShopListAgent.this.res.getColor(R.color.light_line_red));
        if (j != 0)
          break label347;
        if (i != 1)
          break label314;
        paramViewHolderBaby.productPrice.setText(String.valueOf("￥" + j));
        label143: if (paramDPObject.getIntArray("DealGroupIdList") != null)
          paramViewHolderBaby.icTuan.setVisibility(0);
        if (paramDPObject.getInt("PromoID") != 0)
          paramViewHolderBaby.icPromo.setVisibility(0);
        if (paramDPObject.getInt("Bookable") != 0)
          paramViewHolderBaby.icBook.setVisibility(0);
        str = paramDPObject.getString("ProductName");
        if (str == null)
          break label381;
        paramViewHolderBaby.productName.setText(str);
      }
      while (true)
      {
        i = paramDPObject.getInt("ShopPower");
        paramViewHolderBaby.shopPower.setImageDrawable(getDrawableByPower(i));
        i = paramDPObject.getInt("ReviewCount");
        paramViewHolderBaby.productReviewCount.setText(i + "条");
        return;
        paramViewHolderBaby.productPic.getLayoutParams().width = WeddingProductShopListAgent.this.mPortraitWidth;
        paramViewHolderBaby.productPic.getLayoutParams().height = WeddingProductShopListAgent.this.mLandScapeHeight;
        break;
        label314: paramViewHolderBaby.productPrice.setText("暂无价格");
        paramViewHolderBaby.productPrice.setTextColor(WeddingProductShopListAgent.this.res.getColor(R.color.wedding_text_color_hint));
        break label143;
        label347: paramViewHolderBaby.productPrice.setText(String.valueOf("￥" + j));
        break label143;
        label381: if (paramDPObject.getString("ShopName") != null)
        {
          paramViewHolderBaby.productName.setText(paramDPObject.getString("ShopName"));
          continue;
        }
        paramViewHolderBaby.productName.setText(0);
      }
    }

    public int getCount()
    {
      if (WeddingProductShopListAgent.this.isEnd)
        return WeddingProductShopListAgent.this.mWeddingShopList.size();
      if (WeddingProductShopListAgent.this.mWeddingShopList.size() == 0)
        return 0;
      return WeddingProductShopListAgent.this.mWeddingShopList.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < WeddingProductShopListAgent.this.mWeddingShopList.size())
        return WeddingProductShopListAgent.this.mWeddingShopList.get(paramInt);
      if (WeddingProductShopListAgent.this.errorMsg == null)
        return LOADING;
      return ERROR;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public int getItemViewType(int paramInt)
    {
      if (paramInt < WeddingProductShopListAgent.this.mWeddingShopList.size())
      {
        if (WeddingProductShopListAgent.this.isBabyProduct())
          return 1;
        return 0;
      }
      return 2;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      switch (getItemViewType(paramInt))
      {
      default:
      case 1:
      case 0:
      case 2:
      }
      label247: 
      do
        while (true)
        {
          return null;
          View localView;
          if (paramView == null)
            localView = initBabyHolder(null, paramViewGroup);
          while (true)
          {
            paramView = (ViewHolderBaby)localView.getTag();
            paramView.productPrice.setVisibility(0);
            paramView.icTuan.setVisibility(8);
            paramView.icBook.setVisibility(8);
            paramView.icPromo.setVisibility(8);
            paramView.productName.setVisibility(0);
            setupView((DPObject)WeddingProductShopListAgent.this.mWeddingShopList.get(paramInt), paramView);
            return localView;
            localView = paramView;
            if ((paramView.getTag() instanceof ViewHolderBaby))
              continue;
            localView = initBabyHolder(null, paramViewGroup);
          }
          int i;
          if (paramView == null)
          {
            localView = initWedHolder(null, paramViewGroup);
            paramView = (ViewHolderWed)localView.getTag();
            paramViewGroup = (DPObject)WeddingProductShopListAgent.this.mWeddingShopList.get(paramInt);
            String str = paramViewGroup.getString("DefaultPic");
            if (str != null)
              paramView.productPic.setImage(str);
            if (WeddingProductShopListAgent.this.coverStyleType != 1)
              break label496;
            paramView.productPic.getLayoutParams().width = WeddingProductShopListAgent.this.mPortraitWidth;
            paramView.productPic.getLayoutParams().height = WeddingProductShopListAgent.this.mPortraintHeight;
            str = paramViewGroup.getString("ProductName");
            if (str == null)
              break label533;
            paramView.txtName.setText(str);
            paramViewGroup.getInt("ShowPriceType");
            paramInt = paramViewGroup.getInt("Price");
            i = paramViewGroup.getInt("OriginPrice");
            paramView.textPrice.setVisibility(8);
            paramView.textOriPrice.setVisibility(8);
            if (paramInt != 0)
              break label571;
            paramView.textSimbol.setVisibility(8);
            paramView.textOriPrice.setVisibility(8);
            paramView.textPrice.setVisibility(0);
            paramView.textPrice.setTextColor(WeddingProductShopListAgent.this.res.getColor(R.color.wedding_text_color_hint));
            paramView.textPrice.setText("暂无价格");
            paramView.imgFeature.setVisibility(0);
            if (paramView.imgFeature != null)
              switch (paramViewGroup.getInt("Flags"))
              {
              case 3:
              case 5:
              case 7:
              case 9:
              case 11:
              case 13:
              default:
                paramView.imgFeature.setVisibility(8);
              case 2:
              case 4:
              case 8:
              case 6:
              case 10:
              case 12:
              case 14:
              }
          }
          while (true)
          {
            return localView;
            localView = paramView;
            if ((paramView.getTag() instanceof ViewHolderWed))
              break;
            localView = initWedHolder(null, paramViewGroup);
            break;
            paramView.productPic.getLayoutParams().width = WeddingProductShopListAgent.this.mPortraitWidth;
            paramView.productPic.getLayoutParams().height = WeddingProductShopListAgent.this.mLandScapeHeight;
            break label247;
            if (paramViewGroup.getString("ShopName") != null)
            {
              paramView.txtName.setText(paramViewGroup.getString("ShopName"));
              break label270;
            }
            paramView.txtName.setText(0);
            break label270;
            if (i > 0)
            {
              paramView.textOriPrice.getPaint().setFlags(16);
              paramView.textOriPrice.setText("¥" + i);
              paramView.textOriPrice.setVisibility(0);
            }
            paramView.textSimbol.setVisibility(0);
            paramView.textPrice.setVisibility(0);
            paramView.textPrice.setTextColor(WeddingProductShopListAgent.this.res.getColor(R.color.light_red));
            paramView.textPrice.setText(String.valueOf(paramInt));
            break label372;
            paramView.imgFeature.setBackgroundResource(R.drawable.wed_icon_jiudian);
            continue;
            paramView.imgFeature.setBackgroundResource(R.drawable.wed_icon_jipiao);
            continue;
            paramView.imgFeature.setBackgroundResource(R.drawable.wed_icon_qianzheng);
            continue;
            paramView.imgFeature.setBackgroundResource(R.drawable.wed_icon_ji_jiu);
            continue;
            paramView.imgFeature.setBackgroundResource(R.drawable.wed_icon_jiu_qian);
            continue;
            paramView.imgFeature.setBackgroundResource(R.drawable.wed_icon_ji_qian);
            continue;
            paramView.imgFeature.setBackgroundResource(R.drawable.wed_icon_ji_jiu_qian);
          }
          if (getItem(paramInt) != ERROR)
            break;
          if (WeddingProductShopListAgent.this.errorMsg != null)
            return getFailedView(WeddingProductShopListAgent.this.errorMsg, new LoadingErrorView.LoadRetry()
            {
              public void loadRetry(View paramView)
              {
                WeddingProductShopListAgent.this.errorMsg = null;
                WeddingProductShopListAgent.this.loadData(true);
                WeddingProductShopListAgent.WeddingTypeShopListAdapter.this.notifyDataSetChanged();
              }
            }
            , paramViewGroup, paramView);
        }
      while (getItem(paramInt) != LOADING);
      label270: label372: return getLoadingView(paramViewGroup, paramView);
    }

    public int getViewTypeCount()
    {
      return 3;
    }

    class ViewHolderBaby
    {
      View contentView;
      ImageView icBook;
      ImageView icPromo;
      ImageView icTuan;
      TextView productName;
      NetworkImageView productPic;
      TextView productPrice;
      TextView productReviewCount;
      ImageView shopPower;

      ViewHolderBaby()
      {
      }
    }

    class ViewHolderWed
    {
      ImageView imgFeature;
      NetworkImageView productPic;
      TextView textOriPrice;
      TextView textPrice;
      TextView textSimbol;
      TextView txtName;

      ViewHolderWed()
      {
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.agent.WeddingProductShopListAgent
 * JD-Core Version:    0.6.0
 */