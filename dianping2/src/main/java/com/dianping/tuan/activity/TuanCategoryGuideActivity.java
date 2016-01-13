package com.dianping.tuan.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.adapter.CustomGridViewAdapter;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.CustomGridView;
import com.dianping.base.widget.CustomGridView.OnItemClickListener;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.SimpleMsg;
import com.dianping.tuan.utils.CellUtils;
import com.dianping.tuan.widget.DPObjectCacheHelper;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.NetworkImageView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TuanCategoryGuideActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, CustomGridView.OnItemClickListener
{
  protected final String cachekey = "tuancategoryguide546614142324";
  protected HashMap<Integer, String> cateIdLinkMap;
  protected ArrayList<Integer> cateOrder;
  protected HashMap<Integer, DPObject> categoryMap;
  protected MApiRequest categorylistRequest;
  protected ArrayList<ArrayList<DPObject>> cellDataArray;
  protected DPObject[] dpCategoryList;
  protected View errorView;
  protected boolean hasCache;
  protected LinearLayout list;
  protected View loadingView;
  protected int personizedNum = 0;
  protected ScrollView scrollView;

  protected void createGrid()
  {
    if ((!this.categoryMap.isEmpty()) && (!this.cellDataArray.isEmpty()))
    {
      this.list.removeAllViews();
      int i = 0;
      Iterator localIterator = this.cateOrder.iterator();
      if (localIterator.hasNext())
      {
        Integer localInteger = (Integer)localIterator.next();
        LinearLayout localLinearLayout;
        Object localObject;
        if (localInteger != null)
        {
          localLinearLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.tuan_category_list, null, false);
          localObject = (RelativeLayout)localLinearLayout.findViewById(R.id.category_sublist_title);
          ((TextView)((RelativeLayout)localObject).findViewById(R.id.category_sublist_title_label)).setText(((DPObject)this.categoryMap.get(localInteger)).getString("Name"));
          ((NetworkImageView)((RelativeLayout)localObject).findViewById(R.id.category_sublist_title_img)).setImage(((DPObject)this.categoryMap.get(localInteger)).getString("FavIcon"));
          if (localInteger.intValue() == -2)
            break label241;
          ((RelativeLayout)localObject).setTag(localInteger);
          ((RelativeLayout)localObject).setOnClickListener(new View.OnClickListener((RelativeLayout)localObject)
          {
            public void onClick(View paramView)
            {
              paramView = (Integer)paramView.getTag();
              String str = (String)TuanCategoryGuideActivity.this.cateIdLinkMap.get(paramView);
              if (!com.dianping.util.TextUtils.isEmpty(str))
              {
                CellUtils.performClick(this.val$title.getContext(), str);
                return;
              }
              paramView = new Intent("android.intent.action.VIEW", Uri.parse(String.valueOf(String.format("dianping://deallist?categoryid=%d&regionid=0", new Object[] { paramView }))));
              TuanCategoryGuideActivity.this.startActivity(paramView);
            }
          });
          localLinearLayout.findViewById(R.id.sublist_arrow).setVisibility(0);
        }
        while (true)
        {
          localObject = (CustomGridView)localLinearLayout.findViewById(R.id.category_sublist_grid);
          ((CustomGridView)localObject).setAdapter(new SubCateGridAdapter(localInteger, i));
          ((CustomGridView)localObject).setOnItemClickListener(this);
          this.list.addView(localLinearLayout);
          i += 1;
          break;
          label241: localLinearLayout.findViewById(R.id.sublist_arrow).setVisibility(8);
        }
      }
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.tuan_category_guide_activity);
    this.loadingView = findViewById(R.id.loading);
    this.errorView = findViewById(R.id.error);
    this.scrollView = new ScrollView(this);
    this.list = new LinearLayout(this);
    this.list.setOrientation(1);
    this.list.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
    this.scrollView.addView(this.list);
    addContentView(this.scrollView, new LinearLayout.LayoutParams(-2, -2));
    if (DPObjectCacheHelper.getInstance().getCache("tuancategoryguide546614142324") == null);
    for (this.hasCache = false; ; this.hasCache = true)
    {
      requestCategoryList();
      return;
    }
  }

  protected void onDestroy()
  {
    if (this.categorylistRequest != null)
    {
      mapiService().abort(this.categorylistRequest, this, true);
      this.categorylistRequest = null;
    }
    super.onDestroy();
  }

  public void onItemClick(CustomGridView paramCustomGridView, View paramView, int paramInt, long paramLong)
  {
    SubCateGridAdapter localSubCateGridAdapter = (SubCateGridAdapter)paramCustomGridView.getAdapter();
    if ((paramInt == localSubCateGridAdapter.getCount() - 1) && (localSubCateGridAdapter.getDataSize() > localSubCateGridAdapter.getColumnCount() * localSubCateGridAdapter.getDisplayRows()))
      if (localSubCateGridAdapter.isFullyDisplayed())
      {
        paramInt = ((View)paramCustomGridView.getParent()).getTop();
        if (this.scrollView.getScrollY() > paramInt)
          this.scrollView.smoothScrollTo(0, paramInt);
        localSubCateGridAdapter.setFullyDisplayed(false);
        localSubCateGridAdapter.notifyDataSetChanged();
      }
    do
    {
      return;
      localSubCateGridAdapter.setFullyDisplayed(true);
      break;
    }
    while ((paramInt >= ((ArrayList)this.cellDataArray.get(localSubCateGridAdapter.getOrderId())).size()) || (paramView.getTag() == null));
    paramCustomGridView = (DPObject)paramView.getTag();
    if (!android.text.TextUtils.isEmpty(paramCustomGridView.getString("Link")))
    {
      CellUtils.performClick(this, paramCustomGridView.getString("Link"));
      return;
    }
    paramView = new StringBuilder("dianping://deallist");
    paramView.append("?categoryid=" + paramCustomGridView.getInt("ID"));
    paramView.append("&parentcategoryid=" + paramCustomGridView.getInt("ParentID"));
    paramView.append("&categoryenname=" + paramCustomGridView.getString("EnName"));
    paramView.append("&parentcategoryenname=" + paramCustomGridView.getString("ParentEnName"));
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramView.toString())));
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.categorylistRequest)
    {
      if (this.hasCache)
        break label122;
      this.loadingView.setVisibility(8);
      paramMApiRequest = "";
      if ((paramMApiResponse.result() instanceof DPObject))
        paramMApiRequest = paramMApiResponse.message().content();
      paramMApiResponse = paramMApiRequest;
      if (android.text.TextUtils.isEmpty(paramMApiRequest))
        paramMApiResponse = "请求失败，请稍后再试";
      this.errorView.setVisibility(0);
      if ((this.errorView instanceof LoadingErrorView))
        ((LoadingErrorView)this.errorView).setCallBack(new LoadingErrorView.LoadRetry()
        {
          public void loadRetry(View paramView)
          {
            TuanCategoryGuideActivity.this.loadingView.setVisibility(0);
            TuanCategoryGuideActivity.this.errorView.setVisibility(8);
            TuanCategoryGuideActivity.this.requestCategoryList();
          }
        });
      ((TextView)this.errorView.findViewById(16908308)).setText(paramMApiResponse);
      this.categorylistRequest = null;
    }
    label122: 
    do
    {
      return;
      paramMApiRequest = DPObjectCacheHelper.getInstance().getCache("tuancategoryguide546614142324");
      if (paramMApiRequest == null)
      {
        this.hasCache = false;
        return;
      }
      this.personizedNum = paramMApiRequest.getInt("PersonalizeNum");
      this.dpCategoryList = paramMApiRequest.getArray("List");
      this.hasCache = true;
      if (this.dpCategoryList == null)
        continue;
      transformData(this.dpCategoryList);
    }
    while (this.cellDataArray.isEmpty());
    createGrid();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest == this.categorylistRequest)
    {
      if (!this.hasCache)
        this.loadingView.setVisibility(8);
      if (!DPObjectUtils.isDPObjectof(paramMApiResponse, "CategoryList"))
        break label104;
      DPObjectCacheHelper.getInstance().writeCache((DPObject)paramMApiResponse, "tuancategoryguide546614142324");
      this.hasCache = true;
      this.personizedNum = ((DPObject)paramMApiResponse).getInt("PersonalizeNum");
      this.dpCategoryList = ((DPObject)paramMApiResponse).getArray("List");
      transformData(this.dpCategoryList);
      createGrid();
    }
    while (true)
    {
      this.categorylistRequest = null;
      return;
      label104: paramMApiRequest = DPObjectCacheHelper.getInstance().getCache("tuancategoryguide546614142324");
      if (paramMApiRequest == null)
      {
        this.hasCache = false;
        continue;
      }
      this.personizedNum = paramMApiRequest.getInt("PersonalizeNum");
      this.dpCategoryList = paramMApiRequest.getArray("List");
      this.hasCache = true;
      if (this.dpCategoryList != null)
        transformData(this.dpCategoryList);
      if (this.cellDataArray.isEmpty())
        continue;
      createGrid();
    }
  }

  protected void requestCategoryList()
  {
    if (this.categorylistRequest != null);
    do
    {
      return;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("http://app.t.dianping.com/");
      localStringBuilder.append("categorylistgn.bin");
      localStringBuilder.append("?cityid=" + city().id());
      this.categorylistRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
      mapiService().exec(this.categorylistRequest, this);
    }
    while (this.hasCache);
    this.loadingView.setVisibility(0);
  }

  protected void transformData(DPObject[] paramArrayOfDPObject)
  {
    HashMap localHashMap = new HashMap();
    this.categoryMap = new HashMap();
    this.cellDataArray = new ArrayList();
    this.cateOrder = new ArrayList();
    this.cateIdLinkMap = new HashMap();
    int i = 0;
    Object localObject1;
    Object localObject2;
    while (i < paramArrayOfDPObject.length)
    {
      localObject1 = paramArrayOfDPObject[i];
      localObject2 = Integer.valueOf(((DPObject)localObject1).getInt("ID"));
      String str = ((DPObject)localObject1).getString("Link");
      if (((DPObject)localObject1).getInt("ParentID") == 0)
      {
        this.categoryMap.put(localObject2, localObject1);
        localHashMap.put(localObject2, new ArrayList());
        this.cateIdLinkMap.put(localObject2, str);
        this.cateOrder.add(localObject2);
      }
      i += 1;
    }
    i = 0;
    if (i < paramArrayOfDPObject.length)
    {
      localObject1 = paramArrayOfDPObject[i];
      localObject2 = Boolean.valueOf(((DPObject)localObject1).getBoolean("IsHotCategory"));
      if ((localObject2 != null) && (((Boolean)localObject2).booleanValue()))
        ((ArrayList)localHashMap.get(Integer.valueOf(-2))).add(localObject1);
      while (true)
      {
        i += 1;
        break;
        localObject2 = Integer.valueOf(((DPObject)localObject1).getInt("ParentID"));
        if ((((Integer)localObject2).intValue() == 0) || (!localHashMap.containsKey(localObject2)))
          continue;
        ((ArrayList)localHashMap.get(localObject2)).add(localObject1);
      }
    }
    paramArrayOfDPObject = this.cateOrder.iterator();
    while (paramArrayOfDPObject.hasNext())
    {
      localObject1 = (Integer)paramArrayOfDPObject.next();
      this.cellDataArray.add(localHashMap.get(localObject1));
    }
  }

  class SubCateGridAdapter extends CustomGridViewAdapter
  {
    protected Integer categoryId;
    protected Boolean fullyDisplayed = Boolean.valueOf(false);
    protected int order;

    public SubCateGridAdapter(Integer paramInt, int arg3)
    {
      this.categoryId = paramInt;
      int i;
      this.order = i;
    }

    public Integer getCategoryId()
    {
      return this.categoryId;
    }

    public int getColumnCount()
    {
      return 3;
    }

    public int getCount()
    {
      int i = getDataSize();
      if (isFullyDisplayed())
        return (int)Math.ceil((i + 1) / 3.0D) * getColumnCount();
      if (i <= getDisplayRows() * getColumnCount())
        return (int)Math.ceil(i / 3.0D) * getColumnCount();
      return getDisplayRows() * getColumnCount();
    }

    public int getDataSize()
    {
      return ((ArrayList)TuanCategoryGuideActivity.this.cellDataArray.get(this.order)).size();
    }

    public int getDisplayRows()
    {
      return 3;
    }

    public Object getItem(int paramInt)
    {
      return ((ArrayList)TuanCategoryGuideActivity.this.cellDataArray.get(this.order)).get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getItemView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = null;
      if (paramInt < ((ArrayList)TuanCategoryGuideActivity.this.cellDataArray.get(this.order)).size())
        paramViewGroup = (DPObject)getItem(paramInt);
      FrameLayout localFrameLayout = (FrameLayout)paramView;
      if (paramView == null)
        localFrameLayout = (FrameLayout)LayoutInflater.from(TuanCategoryGuideActivity.this).inflate(R.layout.tuan_category_grid_item, null, false);
      paramView = (TextView)localFrameLayout.findViewById(R.id.category_name);
      if (paramViewGroup != null)
      {
        Object localObject = paramViewGroup.getString("Name");
        if (localObject != null)
          paramView.setText((CharSequence)localObject);
        localObject = Boolean.valueOf(paramViewGroup.getBoolean("Highlight"));
        if ((localObject != null) && (((Boolean)localObject).booleanValue()))
          paramView.setTextColor(TuanCategoryGuideActivity.this.getResources().getColor(R.color.tuan_guide_highlight));
        localFrameLayout.setTag(paramViewGroup);
      }
      if ((paramInt == getCount() - 1) && (getDataSize() > getColumnCount() * getDisplayRows()))
      {
        paramView.setTextColor(TuanCategoryGuideActivity.this.getResources().getColor(R.color.tuan_guide_more));
        if (isFullyDisplayed())
          paramView.setText("收起");
      }
      while (Build.VERSION.SDK_INT <= 10)
      {
        localFrameLayout.findViewById(R.id.catagory_mask).setVisibility(8);
        return localFrameLayout;
        paramView.setText("全部");
        continue;
        if (paramViewGroup != null)
          continue;
        localFrameLayout.setClickable(false);
      }
      localFrameLayout.findViewById(R.id.catagory_mask).setVisibility(0);
      return (View)localFrameLayout;
    }

    public int getOrderId()
    {
      return this.order;
    }

    public boolean isFullyDisplayed()
    {
      return this.fullyDisplayed.booleanValue();
    }

    public void setFullyDisplayed(boolean paramBoolean)
    {
      this.fullyDisplayed = Boolean.valueOf(paramBoolean);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.TuanCategoryGuideActivity
 * JD-Core Version:    0.6.0
 */