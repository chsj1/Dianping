package com.dianping.wed.home.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.shoplist.widget.ShopListItem;
import com.dianping.base.widget.MeasuredGridView;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.base.widget.ReviewItem;
import com.dianping.content.CityUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaRelativeLayout;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class HomeProductDetailActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, AdapterView.OnItemClickListener
{
  public static final String HOST = "http://m.api.dianping.com/";
  static final String TAG = HomeProductDetailActivity.class.getSimpleName();
  BasicAdapter adapter;
  int albumFrameHeight;
  int albumFrameWidth;
  LinearLayout bottomView;
  String errorMsg;
  MeasuredGridView gridView;
  Boolean isEnd = Boolean.valueOf(false);
  Boolean isTaskRunning = Boolean.valueOf(false);
  int limit = 5;
  View.OnClickListener mListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      if (HomeProductDetailActivity.this.reviewList != null)
      {
        paramView = new StringBuilder("dianping://review?id=");
        if (HomeProductDetailActivity.this.shop != null)
        {
          paramView.append(HomeProductDetailActivity.this.shop.getInt("ID"));
          paramView.append("&shopname=").append(HomeProductDetailActivity.this.shop.getString("Name"));
          String str = HomeProductDetailActivity.this.shop.getString("BranchName");
          if (!TextUtils.isEmpty(str))
            paramView.append("(").append(str).append(")");
          paramView.append("&shopstatus=").append(HomeProductDetailActivity.this.shop.getInt("Status"));
        }
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView.toString()));
        paramView.putExtra("shop", HomeProductDetailActivity.this.shop);
        HomeProductDetailActivity.this.startActivity(paramView);
      }
    }
  };
  List<DPObject> otherProductList;
  int otherProductsTotalCount;
  LinearLayout phoneButton;
  PhotoAdapter photoAdapter;
  ArrayList<DPObject> photoList = new ArrayList();
  MApiRequest photoRequest;
  String productId;
  ListView productListView;
  String productName;
  DPObject reviewList;
  int screenWidth;
  DPObject shop;
  MApiRequest shopEventRequest;
  MApiRequest shopRequest;
  MApiRequest shopReviewRequest;
  List<Integer> showedPicList = new ArrayList();
  int start = 0;
  String styleName;
  List<DPObject> tagList;
  int verticalAlbumFrameHeight;
  int verticalAlbumFrameWidth;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent().getData();
    if (paramBundle != null)
      this.productId = paramBundle.getQueryParameter("productid");
    if (this.productId == null)
      return;
    paramBundle = new ArrayList();
    paramBundle.add(new BasicNameValuePair("productId", this.productId + ""));
    statisticsEvent("homemain6", "homemain6_product", this.styleName, 0, paramBundle);
    GAHelper.instance().contextStatisticsEvent(this, "homemain6_product", this.styleName, Integer.parseInt(this.productId), "tap");
    this.screenWidth = ViewUtils.getScreenWidthPixels(this);
    this.albumFrameWidth = (this.screenWidth * 43 / 100);
    this.albumFrameHeight = (this.albumFrameWidth * 210 / 280);
    this.verticalAlbumFrameWidth = this.albumFrameWidth;
    this.verticalAlbumFrameHeight = (this.verticalAlbumFrameWidth * 374 / 280);
    this.photoAdapter = new PhotoAdapter(this);
    paramBundle = Uri.parse("http://m.api.dianping.com/wedding/homeproductdetail.bin").buildUpon();
    paramBundle.appendQueryParameter("productid", "" + this.productId);
    paramBundle.appendQueryParameter("cityid", "" + cityId());
    this.shopRequest = BasicMApiRequest.mapiGet(paramBundle.build().toString(), CacheType.DISABLED);
    mapiService().exec(this.shopRequest, this);
    photoTask(0);
    setContentView(R.layout.home_product_detail_layout);
    this.productListView = ((ListView)findViewById(R.id.home_product_detail_view));
    this.productListView.setAdapter(this.photoAdapter);
    this.productListView.setVisibility(0);
    this.productListView.setOnItemClickListener(this);
    this.bottomView = ((LinearLayout)findViewById(R.id.bottom_view));
    this.phoneButton = ((LinearLayout)findViewById(R.id.phone_right_now));
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = getCloneUserInfo();
    paramAdapterView.shop_id = Integer.valueOf(this.shop.getInt("ID"));
    paramAdapterView.index = Integer.valueOf(paramInt);
    paramAdapterView.biz_id = (this.productId + "");
    GAHelper.instance().contextStatisticsEvent(this, "viewphoto", paramAdapterView, "tap");
    Object localObject = new ArrayList();
    paramAdapterView = new DPObject();
    paramAdapterView.edit().putInt("ID", 0);
    ((ArrayList)localObject).add(paramAdapterView);
    paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showphoto"));
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    while (i < this.photoList.size())
    {
      localArrayList.add(this.photoList.get(i));
      i += 1;
    }
    paramAdapterView.putExtra("pageList", localArrayList);
    paramAdapterView.putExtra("position", paramInt);
    paramAdapterView.putExtra("arrShopObjs", (Serializable)localObject);
    paramAdapterView.putExtra("name", "shopName");
    paramView = (NetworkThumbView)paramView.findViewById(R.id.img_shop_photo);
    if ((BitmapDrawable)paramView.getDrawable() != null)
    {
      localObject = new ByteArrayOutputStream();
      ((BitmapDrawable)paramView.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, (OutputStream)localObject);
      paramAdapterView.putExtra("currentbitmap", ((ByteArrayOutputStream)localObject).toByteArray());
    }
    startActivity(paramAdapterView);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.photoRequest)
    {
      this.photoRequest = null;
      this.isTaskRunning = Boolean.valueOf(false);
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      this.isEnd = Boolean.valueOf(paramMApiRequest.getBoolean("IsEnd"));
      this.start = paramMApiRequest.getInt("NextStartIndex");
      paramMApiRequest = paramMApiRequest.getStringArray("List");
      int j = paramMApiRequest.length;
      int i = 0;
      while (i < j)
      {
        paramMApiResponse = paramMApiRequest[i];
        this.photoList.add(new DPObject().edit().putString("Url", paramMApiResponse).generate());
        i += 1;
      }
      this.photoAdapter.notifyDataSetChanged();
    }
    do
    {
      do
        while (true)
        {
          return;
          if (paramMApiRequest == this.shopRequest)
          {
            this.shopRequest = null;
            paramMApiRequest = (DPObject)paramMApiResponse.result();
            this.shop = paramMApiRequest.getObject("Shop");
            this.styleName = paramMApiRequest.getString("StyleName");
            this.productName = paramMApiRequest.getString("ProductName");
            paramMApiResponse = paramMApiRequest.getArray("OtherProducts");
            if ((paramMApiResponse != null) && (paramMApiResponse.length > 0))
              this.otherProductList = Arrays.asList(paramMApiResponse);
            paramMApiResponse = paramMApiRequest.getArray("TagList");
            if ((paramMApiResponse != null) && (paramMApiResponse.length > 0))
              this.tagList = Arrays.asList(paramMApiResponse);
            this.otherProductsTotalCount = paramMApiRequest.getInt("OtherProductsTotalCount");
            if ((this.shop != null) && (this.shop.getStringArray("PhoneNos") != null) && (this.shop.getStringArray("PhoneNos").length > 0))
            {
              paramMApiRequest = getCloneUserInfo();
              paramMApiRequest.shop_id = Integer.valueOf(this.shop.getInt("ID"));
              paramMApiRequest.biz_id = (this.productId + "");
              GAHelper.instance().contextStatisticsEvent(this, "actionbar_tel", paramMApiRequest, "view");
              this.phoneButton.setOnClickListener(new View.OnClickListener(this)
              {
                public void onClick(View paramView)
                {
                  paramView = HomeProductDetailActivity.this.getCloneUserInfo();
                  paramView.shop_id = Integer.valueOf(HomeProductDetailActivity.this.shop.getInt("ID"));
                  paramView.biz_id = (HomeProductDetailActivity.this.productId + "");
                  GAHelper.instance().contextStatisticsEvent(this.val$context, "actionbar_tel", paramView, "tap");
                  Object localObject1 = HomeProductDetailActivity.this.shop.getStringArray("PhoneNos")[0];
                  City localCity = HomeProductDetailActivity.this.city();
                  if (HomeProductDetailActivity.this.shop.getInt("CityID") != HomeProductDetailActivity.this.cityId())
                    localCity = CityUtils.getCityById(HomeProductDetailActivity.this.shop.getInt("CityID"));
                  Intent localIntent = new Intent("android.intent.action.DIAL");
                  paramView = (View)localObject1;
                  while (true)
                  {
                    try
                    {
                      if (paramView.length() > 6)
                        continue;
                      i = 0;
                      localObject1 = paramView;
                      Object localObject2 = new StringBuilder();
                      if ((i == 0) || (localCity == null) || (TextUtils.isEmpty(localCity.areaCode())))
                        break label479;
                      paramView = localCity.areaCode();
                      paramView = paramView + (String)localObject1;
                      localIntent.setData(Uri.parse("tel:" + paramView));
                      HomeProductDetailActivity.this.startActivity(localIntent);
                      return;
                      if ((paramView.length() != 11) || (!paramView.startsWith("1")))
                        continue;
                      i = 0;
                      localObject1 = paramView;
                      continue;
                      if ((paramView.startsWith("400")) || (paramView.startsWith("800")))
                      {
                        int j = 0;
                        localObject2 = "";
                        if (!((String)localObject1).contains("-"))
                          continue;
                        i = ((String)localObject1).indexOf("-");
                        paramView = ((String)localObject1).substring(0, i);
                        localObject2 = ((String)localObject1).substring(i + 1);
                        i = j;
                        localObject1 = paramView;
                        if (TextUtils.isEmpty((CharSequence)localObject2))
                          continue;
                        localObject1 = Toast.makeText(this.val$context, "拨打电话的时候请手动加拨分机号" + (String)localObject2 + "哦", 1);
                        ((Toast)localObject1).setGravity(17, 0, 0);
                        if (!(((Toast)localObject1).getView() instanceof LinearLayout))
                          continue;
                        localObject2 = (LinearLayout)((Toast)localObject1).getView();
                        if (!(((LinearLayout)localObject2).getChildAt(0) instanceof TextView))
                          continue;
                        ((TextView)((LinearLayout)localObject2).getChildAt(0)).setTextSize(0, HomeProductDetailActivity.this.getResources().getDimension(R.dimen.text_large));
                        ((Toast)localObject1).show();
                        i = j;
                        localObject1 = paramView;
                        continue;
                      }
                    }
                    catch (java.lang.Exception paramView)
                    {
                      Toast.makeText(this.val$context, "无法启动拨号程序", 0).show();
                      return;
                    }
                    int i = 1;
                    localObject1 = paramView;
                    continue;
                    label479: paramView = "";
                  }
                }
              });
              this.photoAdapter.notifyDataSetChanged();
              this.shopEventRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/getshopeventlist.bin").buildUpon().appendQueryParameter("shopids", this.shop.getInt("ID") + "").build().toString(), CacheType.DISABLED);
              mapiService().exec(this.shopEventRequest, this);
              paramMApiRequest = new StringBuffer("http://m.api.dianping.com/review.bin?");
              paramMApiRequest.append("shopid=").append(this.shop.getInt("ID"));
              paramMApiRequest.append("&start=0");
              paramMApiRequest.append("&limit=1");
              paramMApiResponse = accountService();
              if (paramMApiResponse.token() != null)
                paramMApiRequest.append("&token=").append(paramMApiResponse.token());
              this.shopReviewRequest = BasicMApiRequest.mapiGet(paramMApiRequest.toString(), CacheType.DISABLED);
              mapiService().exec(this.shopReviewRequest, this);
              return;
            }
            this.bottomView.setVisibility(8);
            return;
          }
          if (paramMApiRequest != this.shopEventRequest)
            break;
          this.shopEventRequest = null;
          if ((paramMApiResponse.result() == null) || (!(paramMApiResponse.result() instanceof DPObject)))
            continue;
          paramMApiRequest = Arrays.asList(((DPObject)paramMApiResponse.result()).getArray("EventList"));
          if (paramMApiRequest.size() <= 0)
            continue;
          paramMApiRequest = ((DPObject)paramMApiRequest.get(0)).getString("EventTag");
          if (TextUtils.isEmpty(paramMApiRequest))
            continue;
          this.shop = this.shop.edit().putString("EventText", paramMApiRequest).generate();
          this.photoAdapter.notifyDataSetChanged();
          return;
        }
      while (paramMApiRequest != this.shopReviewRequest);
      this.shopReviewRequest = null;
    }
    while ((paramMApiResponse.result() == null) || (!(paramMApiResponse.result() instanceof DPObject)));
    this.reviewList = ((DPObject)paramMApiResponse.result());
    this.photoAdapter.notifyDataSetChanged();
  }

  void photoTask(int paramInt)
  {
    this.isTaskRunning = Boolean.valueOf(true);
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/wedding/productdetailpics.bin").buildUpon();
    localBuilder.appendQueryParameter("productid", "" + this.productId).appendQueryParameter("start", paramInt + "").appendQueryParameter("limit", this.limit + "");
    this.photoRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    mapiService().exec(this.photoRequest, this);
  }

  public class PhotoAdapter extends BasicAdapter
  {
    Context context;

    public PhotoAdapter(Context arg2)
    {
      Object localObject;
      this.context = localObject;
    }

    public int getCount()
    {
      if (HomeProductDetailActivity.this.photoList.size() == 0)
        return 2;
      return HomeProductDetailActivity.this.photoList.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < HomeProductDetailActivity.this.photoList.size())
        return HomeProductDetailActivity.this.photoList.get(paramInt);
      if (!HomeProductDetailActivity.this.isEnd.booleanValue())
      {
        if (HomeProductDetailActivity.this.errorMsg == null)
          return LOADING;
        return ERROR;
      }
      if (paramInt == 0)
        return "nopic";
      return "end";
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject1 = getItem(paramInt);
      Object localObject2;
      Object localObject3;
      Object localObject5;
      Object localObject4;
      Object localObject6;
      Object localObject7;
      if ("nopic".equals(localObject1.toString()))
      {
        paramView = (LinearLayout)LayoutInflater.from(this.context).inflate(R.layout.home_product_detail_top_item, paramViewGroup, false);
        ((TextView)paramView.findViewById(R.id.home_design_product_name)).setText(HomeProductDetailActivity.this.productName);
        localObject1 = (LinearLayout)paramView.findViewById(R.id.homedesign_product_style_icon_list);
        if ((HomeProductDetailActivity.this.tagList != null) && (HomeProductDetailActivity.this.tagList.size() > 0))
        {
          localObject2 = HomeProductDetailActivity.this.tagList.iterator();
          if (((Iterator)localObject2).hasNext())
          {
            localObject3 = (DPObject)((Iterator)localObject2).next();
            localObject5 = new LinearLayout.LayoutParams(0, -2);
            ((LinearLayout.LayoutParams)localObject5).weight = 1.0F;
            localObject4 = (LinearLayout)LayoutInflater.from(this.context).inflate(R.layout.home_product_detail_icon_item, paramViewGroup, false);
            ((LinearLayout)localObject4).setLayoutParams((ViewGroup.LayoutParams)localObject5);
            localObject5 = (ImageView)((LinearLayout)localObject4).findViewById(R.id.home_design_product_tag_icon);
            localObject6 = (TextView)((LinearLayout)localObject4).findViewById(R.id.home_design_product_tag_text);
            localObject7 = ((DPObject)localObject3).getString("ID");
            ((TextView)localObject6).setText(((DPObject)localObject3).getString("Name"));
            if ("style".equals(localObject7))
              ((ImageView)localObject5).setImageDrawable(HomeProductDetailActivity.this.getResources().getDrawable(R.drawable.home_design_fengge_icon));
            while (true)
            {
              ((LinearLayout)localObject1).addView((View)localObject4);
              break;
              if ("housetype".equals(localObject7))
              {
                ((ImageView)localObject5).setImageDrawable(HomeProductDetailActivity.this.getResources().getDrawable(R.drawable.home_design_huxing_icon));
                continue;
              }
              if ("area".equals(localObject7))
              {
                ((ImageView)localObject5).setImageDrawable(HomeProductDetailActivity.this.getResources().getDrawable(R.drawable.home_design_mianji_icon));
                continue;
              }
              if (!"price".equals(localObject7))
                continue;
              ((ImageView)localObject5).setImageDrawable(HomeProductDetailActivity.this.getResources().getDrawable(R.drawable.home_design_jiage_icon));
            }
          }
        }
        else
        {
          ((LinearLayout)localObject1).setVisibility(8);
        }
        paramView.findViewById(R.id.no_pics).setVisibility(0);
        paramView.setOnClickListener(null);
        return paramView;
      }
      if ((localObject1 instanceof DPObject))
      {
        if (paramInt == 0)
        {
          paramView = (LinearLayout)LayoutInflater.from(this.context).inflate(R.layout.home_product_detail_top_item, paramViewGroup, false);
          ((TextView)paramView.findViewById(R.id.home_design_product_name)).setText(HomeProductDetailActivity.this.productName);
          localObject2 = (LinearLayout)paramView.findViewById(R.id.homedesign_product_style_icon_list);
          if ((HomeProductDetailActivity.this.tagList != null) && (HomeProductDetailActivity.this.tagList.size() > 0))
          {
            localObject3 = HomeProductDetailActivity.this.tagList.iterator();
            if (((Iterator)localObject3).hasNext())
            {
              localObject4 = (DPObject)((Iterator)localObject3).next();
              localObject6 = new LinearLayout.LayoutParams(0, -2);
              ((LinearLayout.LayoutParams)localObject6).weight = 1.0F;
              localObject5 = (LinearLayout)LayoutInflater.from(this.context).inflate(R.layout.home_product_detail_icon_item, paramViewGroup, false);
              ((LinearLayout)localObject5).setLayoutParams((ViewGroup.LayoutParams)localObject6);
              localObject6 = (ImageView)((LinearLayout)localObject5).findViewById(R.id.home_design_product_tag_icon);
              localObject7 = (TextView)((LinearLayout)localObject5).findViewById(R.id.home_design_product_tag_text);
              String str = ((DPObject)localObject4).getString("ID");
              ((TextView)localObject7).setText(((DPObject)localObject4).getString("Name"));
              if ("style".equals(str))
                ((ImageView)localObject6).setImageDrawable(HomeProductDetailActivity.this.getResources().getDrawable(R.drawable.home_design_fengge_icon));
              while (true)
              {
                ((LinearLayout)localObject2).addView((View)localObject5);
                break;
                if ("housetype".equals(str))
                {
                  ((ImageView)localObject6).setImageDrawable(HomeProductDetailActivity.this.getResources().getDrawable(R.drawable.home_design_huxing_icon));
                  continue;
                }
                if ("area".equals(str))
                {
                  ((ImageView)localObject6).setImageDrawable(HomeProductDetailActivity.this.getResources().getDrawable(R.drawable.home_design_mianji_icon));
                  continue;
                }
                if (!"price".equals(str))
                  continue;
                ((ImageView)localObject6).setImageDrawable(HomeProductDetailActivity.this.getResources().getDrawable(R.drawable.home_design_jiage_icon));
              }
            }
          }
          else
          {
            ((LinearLayout)localObject2).setVisibility(8);
          }
          setViewDetailsByType((DPObject)localObject1, paramView);
          paramView.findViewById(R.id.img_shop_photo).setVisibility(0);
          return paramView;
        }
        if (!HomeProductDetailActivity.this.showedPicList.contains(Integer.valueOf(paramInt)))
        {
          HomeProductDetailActivity.this.showedPicList.add(Integer.valueOf(paramInt));
          paramView = HomeProductDetailActivity.this.getCloneUserInfo();
          if (HomeProductDetailActivity.this.shop != null)
            paramView.shop_id = Integer.valueOf(HomeProductDetailActivity.this.shop.getInt("ID"));
          paramView.biz_id = (HomeProductDetailActivity.this.productId + "");
          paramView.index = Integer.valueOf(paramInt);
          GAHelper.instance().contextStatisticsEvent(this.context, "viewphoto", paramView, "view");
        }
        paramView = (DPObject)localObject1;
        paramViewGroup = LayoutInflater.from(this.context).inflate(R.layout.item_of_wedding_product_pic_detail, paramViewGroup, false);
        setViewDetailsByType(paramView, paramViewGroup);
        return paramViewGroup;
      }
      if (localObject1 == ERROR)
      {
        if (HomeProductDetailActivity.this.errorMsg != null)
          return getFailedView(HomeProductDetailActivity.this.errorMsg, new HomeProductDetailActivity.PhotoAdapter.1(this), paramViewGroup, paramView);
      }
      else
      {
        if (localObject1 == LOADING)
        {
          if (!HomeProductDetailActivity.this.isTaskRunning.booleanValue())
            HomeProductDetailActivity.this.photoTask(HomeProductDetailActivity.this.start);
          return getLoadingView(paramViewGroup, paramView);
        }
        if ("end".equals(localObject1.toString()))
        {
          if (HomeProductDetailActivity.this.shop == null)
            return new TextView(this.context);
          localObject2 = LayoutInflater.from(this.context).inflate(R.layout.home_product_detail_shop_item, paramViewGroup, false);
          paramView = (LinearLayout)((View)localObject2).findViewById(R.id.product_shop_item);
          localObject1 = (ShopListItem)LayoutInflater.from(this.context).inflate(R.layout.shop_item, null, false);
          localObject3 = HomeProductDetailActivity.this.shop;
          double d1;
          double d2;
          if (HomeProductDetailActivity.this.location() == null)
          {
            d1 = 1.0D;
            if (HomeProductDetailActivity.this.location() != null)
              break label1691;
            d2 = 1.0D;
            label1116: ((ShopListItem)localObject1).setShop((DPObject)localObject3, -1, d1, d2, true);
            ((ShopListItem)localObject1).setGAString("shopinfoh_shopinfo", HomeProductDetailActivity.this.styleName, Integer.parseInt(HomeProductDetailActivity.this.productId));
            ((ShopListItem)localObject1).setOnClickListener(new HomeProductDetailActivity.PhotoAdapter.2(this));
            paramView.addView((View)localObject1);
            localObject3 = (LinearLayout)((View)localObject2).findViewById(R.id.review_container);
            if (HomeProductDetailActivity.this.reviewList == null)
              break label1793;
            localObject4 = (ReviewItem)LayoutInflater.from(this.context).inflate(R.layout.wedding_review_item, paramViewGroup, false);
            localObject1 = null;
            if ((HomeProductDetailActivity.this.reviewList == null) || (HomeProductDetailActivity.this.reviewList.getArray("List") == null) || (HomeProductDetailActivity.this.reviewList.getArray("List").length <= 0))
              break label1725;
            if ((HomeProductDetailActivity.this.reviewList.getObject("OwnerReview") == null) || (!new Date(HomeProductDetailActivity.this.reviewList.getArray("List")[0].getTime("Time")).before(new Date(HomeProductDetailActivity.this.reviewList.getObject("OwnerReview").getTime("Time")))))
              break label1706;
            paramView = HomeProductDetailActivity.this.reviewList.getObject("OwnerReview");
            label1352: ((ReviewItem)localObject4).setReview(paramView);
            ((ReviewItem)localObject4).setReviewCountVisible(8);
            ((ReviewItem)localObject4).setOnClickListener(HomeProductDetailActivity.this.mListener);
            ((LinearLayout)localObject3).addView((View)localObject4);
            ((LinearLayout)localObject3).setVisibility(0);
            paramView = LayoutInflater.from(this.context).inflate(R.layout.wedding_expand_layout, paramViewGroup, false);
            if (paramView != null)
            {
              if (HomeProductDetailActivity.this.reviewList == null)
                break label1774;
              ((TextView)paramView.findViewById(16908308)).setText("查看全部" + HomeProductDetailActivity.this.reviewList.getInt("RecordCount") + "条网友点评");
              label1471: paramView.setOnClickListener(HomeProductDetailActivity.this.mListener);
              ((LinearLayout)localObject3).addView(paramView);
            }
            label1488: paramView = ((View)localObject2).findViewById(R.id.product_expand);
            paramViewGroup = (TextView)((View)localObject2).findViewById(R.id.all_product);
            if ((HomeProductDetailActivity.this.otherProductList == null) || (HomeProductDetailActivity.this.otherProductList.size() <= 0))
              break label1816;
            HomeProductDetailActivity.this.adapter = new HomeProductDetailActivity.ProductAdapter(HomeProductDetailActivity.this, this.context, HomeProductDetailActivity.this.otherProductList);
            HomeProductDetailActivity.this.gridView = ((MeasuredGridView)((View)localObject2).findViewById(R.id.homedesign_product_gallery_gridview));
            HomeProductDetailActivity.this.gridView.setAdapter(HomeProductDetailActivity.this.adapter);
            paramViewGroup.setText("查看全部" + HomeProductDetailActivity.this.otherProductsTotalCount + "个案例");
            ((NovaRelativeLayout)paramView).setGAString("shopinfoh_productmore", HomeProductDetailActivity.this.styleName, Integer.parseInt(HomeProductDetailActivity.this.productId));
            paramView.setOnClickListener(new HomeProductDetailActivity.PhotoAdapter.3(this));
          }
          while (true)
          {
            return localObject2;
            d1 = HomeProductDetailActivity.this.location().latitude();
            break;
            label1691: d2 = HomeProductDetailActivity.this.location().longitude();
            break label1116;
            label1706: paramView = HomeProductDetailActivity.this.reviewList.getArray("List")[0];
            break label1352;
            label1725: paramView = (View)localObject1;
            if (HomeProductDetailActivity.this.reviewList == null)
              break label1352;
            paramView = (View)localObject1;
            if (HomeProductDetailActivity.this.reviewList.getObject("OwnerReview") == null)
              break label1352;
            paramView = HomeProductDetailActivity.this.reviewList.getObject("OwnerReview");
            break label1352;
            label1774: ((TextView)paramView.findViewById(16908308)).setText("查看全部网友点评");
            break label1471;
            label1793: ((LinearLayout)localObject3).setVisibility(8);
            ((View)localObject2).findViewById(R.id.review_text).setVisibility(8);
            break label1488;
            label1816: ((View)localObject2).findViewById(R.id.product_text).setVisibility(8);
            paramView.setVisibility(8);
          }
        }
      }
      return (View)(View)(View)(View)(View)(View)(View)null;
    }

    void setViewDetailsByType(DPObject paramDPObject, View paramView)
    {
      paramDPObject = paramDPObject.getString("Url");
      paramView = (NetworkThumbView)paramView.findViewById(R.id.img_shop_photo);
      if (paramView.getImageHandler() != null)
        return;
      paramView.setImageHandler(new HomeProductDetailActivity.PhotoAdapter.4(this, paramView, HomeProductDetailActivity.this.screenWidth));
      paramView.setImage(paramDPObject);
    }
  }

  public class ProductAdapter extends BasicAdapter
  {
    Context context;
    List<DPObject> dataList;

    public ProductAdapter(List<DPObject> arg2)
    {
      Object localObject1;
      this.context = localObject1;
      Object localObject2;
      this.dataList = localObject2;
    }

    public int getCount()
    {
      if (this.dataList == null)
        return 0;
      return this.dataList.size();
    }

    public Object getItem(int paramInt)
    {
      if ((this.dataList == null) || (this.dataList.size() <= paramInt))
        return null;
      return this.dataList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
      {
        int i = R.layout.home_design_product_item;
        localObject = (DPObject)localObject;
        if ((paramView == null) || (paramView.getId() != R.id.item_of_photo_album))
          paramView = ((LayoutInflater)(LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(i, paramViewGroup, false);
        while (true)
        {
          setViewDetailsByType((DPObject)localObject, paramView);
          setOnClickListenerByType((DPObject)localObject, paramView, paramInt);
          return paramView;
        }
      }
      if (localObject == ERROR)
        Log.e(HomeProductDetailActivity.TAG, "ERROR IN getView");
      while (true)
      {
        return null;
        if (localObject != LOADING)
          continue;
        Log.e(HomeProductDetailActivity.TAG, "LOADING in getView");
        getLoadingView(paramViewGroup, paramView);
      }
    }

    void setOnClickListenerByType(DPObject paramDPObject, View paramView, int paramInt)
    {
      paramView.setOnClickListener(new HomeProductDetailActivity.ProductAdapter.1(this, paramInt, paramDPObject));
    }

    void setViewDetailsByType(DPObject paramDPObject, View paramView)
    {
      Object localObject1 = paramDPObject.getString("DefaultPic");
      int i = paramDPObject.getInt("CoverStyleType");
      Object localObject2 = (NetworkImageView)paramView.findViewById(R.id.img_shop_photo);
      if (i == 2)
        ((NetworkImageView)localObject2).getLayoutParams().width = HomeProductDetailActivity.this.verticalAlbumFrameWidth;
      for (((NetworkImageView)localObject2).getLayoutParams().height = HomeProductDetailActivity.this.verticalAlbumFrameHeight; ; ((NetworkImageView)localObject2).getLayoutParams().height = HomeProductDetailActivity.this.albumFrameHeight)
      {
        ((NetworkImageView)localObject2).setImage((String)localObject1);
        localObject1 = (TextView)paramView.findViewById(R.id.lay_img_desc_title);
        localObject2 = (TextView)paramView.findViewById(R.id.lay_img_desc_area);
        paramView = (TextView)paramView.findViewById(R.id.lay_img_desc_style);
        String str = paramDPObject.getString("Name");
        i = paramDPObject.getInt("Area");
        paramDPObject = paramDPObject.getString("Style");
        ((TextView)localObject1).setText(str);
        ((TextView)localObject2).setText(i + "平米");
        paramView.setText(paramDPObject);
        return;
        ((NetworkImageView)localObject2).getLayoutParams().width = HomeProductDetailActivity.this.albumFrameWidth;
      }
    }
  }

  public class TagAdapter extends BasicAdapter
  {
    Context context;
    List<DPObject> dataList;

    public TagAdapter(List<DPObject> arg2)
    {
      Object localObject1;
      this.context = localObject1;
      Object localObject2;
      this.dataList = localObject2;
    }

    public int getCount()
    {
      if (this.dataList == null)
        return 0;
      return this.dataList.size();
    }

    public Object getItem(int paramInt)
    {
      if ((this.dataList == null) || (this.dataList.size() <= paramInt))
        return null;
      return this.dataList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = (LinearLayout)LayoutInflater.from(this.context).inflate(R.layout.home_product_detail_icon_item, paramViewGroup, false);
      paramViewGroup = (ImageView)paramView.findViewById(R.id.home_design_product_tag_icon);
      if (paramInt == 0)
        paramViewGroup.setImageDrawable(HomeProductDetailActivity.this.getResources().getDrawable(R.drawable.home_design_fengge_icon));
      do
      {
        return paramView;
        if (paramInt == 1)
        {
          paramViewGroup.setImageDrawable(HomeProductDetailActivity.this.getResources().getDrawable(R.drawable.home_design_huxing_icon));
          return paramView;
        }
        if (paramInt != 2)
          continue;
        paramViewGroup.setImageDrawable(HomeProductDetailActivity.this.getResources().getDrawable(R.drawable.home_design_mianji_icon));
        return paramView;
      }
      while (paramInt != 3);
      paramViewGroup.setImageDrawable(HomeProductDetailActivity.this.getResources().getDrawable(R.drawable.home_design_jiage_icon));
      return paramView;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.home.activity.HomeProductDetailActivity
 * JD-Core Version:    0.6.0
 */