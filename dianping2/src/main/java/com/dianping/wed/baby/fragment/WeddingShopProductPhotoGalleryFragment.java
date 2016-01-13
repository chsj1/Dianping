package com.dianping.wed.baby.fragment;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NovaFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.wed.baby.activity.BabyProductListActivity;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;

public class WeddingShopProductPhotoGalleryFragment extends NovaFragment
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private static final String API_URL_PRODUCT = "http://m.api.dianping.com/wedding/productlist.bin?";
  static final int REQUEST_LIMIT = 20;
  private static final String REQUEST_PAGE_NAME = "productlist";
  private static final String TAG = WeddingShopProductPhotoGalleryFragment.class.getSimpleName();
  private int albumFrameHeight;
  private int albumFrameWidth;
  private String categoryDesc;
  HorizontalScrollView categoryTitleLayout;
  private int coverStyleType;
  String currentCategoryID = "0";
  String emptyMsg;
  private TextView emptyTV;
  String errorMsg;
  private View fragmentView = null;
  private GridView gridView;
  boolean isEmptySource;
  boolean isEnd = false;
  private boolean isInitCategory = false;
  boolean isTaskRunning = false;
  WeddingShopProductPhotoGalleryFragment.PhotoAdapter photoAdapter;
  ArrayList<DPObject> photoList;
  MApiRequest photoRequest;
  private int photoType;
  private int screenWidth;
  private DPObject shop;
  int shopId;
  int start;
  private int verticalAlbumHeight;

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (TextUtils.isEmpty(this.categoryDesc))
      this.categoryDesc = "产品";
    this.emptyTV = ((TextView)this.fragmentView.findViewById(R.id.gallery_empty));
    paramBundle = getResources().getDrawable(R.drawable.empty_page_nothing);
    paramBundle.setBounds(0, 0, paramBundle.getIntrinsicWidth(), paramBundle.getIntrinsicHeight());
    this.emptyTV.setCompoundDrawablePadding(8);
    this.emptyTV.setCompoundDrawables(paramBundle, null, null, null);
    this.emptyTV.setMovementMethod(LinkMovementMethod.getInstance());
    this.gridView.setEmptyView(this.emptyTV);
    this.emptyMsg = ("商户暂时没有录入" + this.categoryDesc);
    this.emptyTV.setText(Html.fromHtml(this.emptyMsg));
  }

  public void onClick(View paramView)
  {
    String str;
    if ((this.categoryTitleLayout.getChildAt(0) != null) && ((this.categoryTitleLayout.getChildAt(0) instanceof LinearLayout)))
    {
      int i = 0;
      while (i < ((LinearLayout)this.categoryTitleLayout.getChildAt(0)).getChildCount())
      {
        ((LinearLayout)this.categoryTitleLayout.getChildAt(0)).getChildAt(i).setSelected(false);
        i += 1;
      }
      paramView.setSelected(true);
      paramView = (DPObject)paramView.getTag();
      str = paramView.getString("Name");
      if (!TextUtils.isEmpty(str))
        break label99;
    }
    label99: 
    do
      return;
    while ((!TextUtils.isEmpty(this.currentCategoryID)) && (this.currentCategoryID.equals(str)));
    this.photoAdapter.reset();
    this.currentCategoryID = paramView.getString("ID");
    photoTask(0, this.shopId);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.screenWidth = ViewUtils.getScreenWidthPixels(getActivity());
    this.albumFrameWidth = (this.screenWidth * 45 / 100);
    this.albumFrameHeight = (this.albumFrameWidth * 210 / 280);
    this.verticalAlbumHeight = (this.albumFrameWidth * 374 / 280);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.fragmentView = paramLayoutInflater.inflate(R.layout.shop_wedding_photo_gallery, paramViewGroup, false);
    this.gridView = ((GridView)this.fragmentView.findViewById(R.id.gallery_gridview));
    this.categoryTitleLayout = ((HorizontalScrollView)this.fragmentView.findViewById(R.id.select_title));
    if (this.photoList == null)
      this.photoList = new ArrayList();
    this.photoAdapter = new WeddingShopProductPhotoGalleryFragment.PhotoAdapter(this);
    this.gridView.setAdapter(this.photoAdapter);
    this.photoAdapter.notifyDataSetChanged();
    return this.fragmentView;
  }

  public void onDetach()
  {
    super.onDetach();
    if (this.photoRequest != null)
      mapiService().abort(this.photoRequest, null, true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (getActivity() == null)
      return;
    this.isTaskRunning = false;
    this.photoRequest = null;
    paramMApiRequest = null;
    try
    {
      paramMApiResponse = paramMApiResponse.message().content();
      paramMApiRequest = paramMApiResponse;
      label32: if (paramMApiRequest != null)
      {
        this.errorMsg = paramMApiRequest;
        this.photoAdapter.notifyDataSetChanged();
        return;
      }
      this.isEnd = true;
      this.photoAdapter.notifyDataSetChanged();
      return;
    }
    catch (java.lang.Exception paramMApiResponse)
    {
      break label32;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest != this.photoRequest);
    label149: 
    do
    {
      return;
      this.isTaskRunning = false;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if ((this.start == 0) && (paramMApiRequest != null))
      {
        paramMApiResponse = paramMApiRequest.getArray("ProductCategoryList");
        if ((paramMApiResponse != null) && (paramMApiResponse.length > 1))
          break label149;
        if (this.categoryTitleLayout != null)
          this.categoryTitleLayout.setVisibility(8);
      }
      while (true)
      {
        this.isEnd = paramMApiRequest.getBoolean("IsEnd");
        paramMApiResponse = paramMApiRequest.getString("CategoryDesc");
        this.start = paramMApiRequest.getInt("NextStartIndex");
        this.coverStyleType = paramMApiRequest.getInt("CoverStyleType");
        paramMApiRequest = paramMApiRequest.getArray("List");
        int i = 0;
        while (i < paramMApiRequest.length)
        {
          this.photoList.add(paramMApiRequest[i]);
          i += 1;
        }
        if (this.isInitCategory)
          continue;
        LinearLayout localLinearLayout = new LinearLayout(getActivity());
        localLinearLayout.setOrientation(0);
        int j = this.screenWidth / Math.min(4, paramMApiResponse.length);
        i = 0;
        while (i < paramMApiResponse.length)
        {
          NovaTextView localNovaTextView = (NovaTextView)LayoutInflater.from(getActivity()).inflate(R.layout.wed_shop_photo_tab_indicator, localLinearLayout, false);
          Object localObject = paramMApiResponse[i];
          localNovaTextView.setClickable(true);
          localNovaTextView.setOnClickListener(this);
          localNovaTextView.setTag(localObject);
          localNovaTextView.setText(localObject.getString("Name"));
          GAUserInfo localGAUserInfo = ((DPActivity)getActivity()).getCloneUserInfo();
          localGAUserInfo.index = Integer.valueOf(i);
          localGAUserInfo.shop_id = Integer.valueOf(this.shopId);
          localNovaTextView.setGAString("categorytab", localGAUserInfo);
          localLinearLayout.addView(localNovaTextView, j, (int)getResources().getDimension(R.dimen.deal_filter_button_height));
          if (this.currentCategoryID.equals(localObject.getString("ID")))
            localNovaTextView.setSelected(true);
          i += 1;
        }
        if (this.currentCategoryID.equals("0"))
        {
          localLinearLayout.getChildAt(0).setSelected(true);
          this.currentCategoryID = ((DPObject)localLinearLayout.getChildAt(0).getTag()).getString("ID");
        }
        this.categoryTitleLayout.addView(localLinearLayout, -1, -2);
        this.categoryTitleLayout.setVisibility(0);
        this.isInitCategory = true;
      }
      this.photoAdapter.notifyDataSetChanged();
    }
    while (!(getActivity() instanceof BabyProductListActivity));
    ((BabyProductListActivity)getActivity()).updateTitleByDesc(paramMApiResponse);
  }

  void photoTask(int paramInt1, int paramInt2)
  {
    MApiService localMApiService = (MApiService)getService("mapi");
    StringBuffer localStringBuffer = new StringBuffer();
    if (this.isEmptySource);
    do
      return;
    while (paramInt2 <= 0);
    localStringBuffer.append("http://m.api.dianping.com/wedding/productlist.bin?").append("shopid=").append(paramInt2 + "").append("&start=").append(paramInt1).append("&limit=").append(20).append("&pageName=").append("productlist").append("&productcategoryid=").append(this.currentCategoryID);
    this.photoRequest = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.NORMAL);
    localMApiService.exec(this.photoRequest, this);
  }

  public void setArgument(Bundle paramBundle)
  {
    if (paramBundle == null)
      return;
    this.shopId = paramBundle.getInt("shopId");
    this.photoType = paramBundle.getInt("photoType");
    this.shop = ((DPObject)paramBundle.getParcelable("shop"));
    this.categoryDesc = paramBundle.getString("categoryDesc");
    this.isEmptySource = paramBundle.getBoolean("isEmptySource");
    this.currentCategoryID = paramBundle.getString("productCategoryId");
    if (TextUtils.isEmpty(this.currentCategoryID))
      this.currentCategoryID = "0";
    this.photoAdapter.notifyDataSetChanged();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.fragment.WeddingShopProductPhotoGalleryFragment
 * JD-Core Version:    0.6.0
 */