package com.dianping.wed.home.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class HomeProductListActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String DEFAULT_ERRMSG = "网络链接失败，点击重新加载";
  private static final int LIMIT = 5;
  private String errorMsg;
  private ArrayList<DPObject> homeProductList;
  private MApiRequest homeProductListRequest;
  private ListView homeProductListView;
  private boolean isEnd = false;
  private boolean isInRequest = false;
  private ListViewAdapter listViewAdapter;
  private int start;
  private String stylename;
  private int targetWidth;

  private void queryApi(String paramString, int paramInt)
  {
    this.isInRequest = true;
    setTitle(paramString + "风格");
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/wedding/homeproductlist.bin").buildUpon();
    localBuilder.appendQueryParameter("cityid", cityId() + "");
    localBuilder.appendQueryParameter("stylename", paramString);
    localBuilder.appendQueryParameter("start", paramInt + "");
    localBuilder.appendQueryParameter("limit", "5");
    this.homeProductListRequest = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.NORMAL);
    mapiService().exec(this.homeProductListRequest, this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.targetWidth = ViewUtils.getScreenWidthPixels(this);
    paramBundle = getIntent().getData();
    if (paramBundle != null)
      this.stylename = paramBundle.getQueryParameter("stylename");
    if (this.stylename == null)
      return;
    statisticsEvent("homemain6", "homemain6_stylelist", this.stylename, 0, null);
    if (this.homeProductList == null)
      this.homeProductList = new ArrayList();
    while (true)
    {
      super.setContentView(R.layout.home_product_list);
      this.listViewAdapter = new ListViewAdapter(null);
      this.homeProductListView = ((ListView)findViewById(R.id.home_productlist_view));
      this.homeProductListView.setAdapter(this.listViewAdapter);
      queryApi(this.stylename, this.start);
      return;
      this.homeProductList.clear();
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.isInRequest = false;
    this.homeProductListRequest = null;
    paramMApiRequest = null;
    try
    {
      String str = paramMApiResponse.message().content();
      paramMApiRequest = str;
      label24: if (paramMApiRequest != null)
      {
        this.errorMsg = paramMApiResponse.message().content();
        this.listViewAdapter.notifyDataSetChanged();
      }
      return;
    }
    catch (Exception localException)
    {
      break label24;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest != this.homeProductListRequest);
    do
    {
      return;
      this.isInRequest = false;
      paramMApiRequest = (DPObject)paramMApiResponse.result();
    }
    while (paramMApiRequest == null);
    this.isEnd = paramMApiRequest.getBoolean("IsEnd");
    int i = paramMApiRequest.getInt("NextStartIndex");
    if (this.start == i)
      this.isEnd = true;
    this.start = i;
    paramMApiRequest = paramMApiRequest.getArray("List");
    if ((paramMApiRequest == null) || (paramMApiRequest.length == 0))
      this.errorMsg = "网络链接失败，点击重新加载";
    while (true)
    {
      this.listViewAdapter.notifyDataSetChanged();
      return;
      int j = paramMApiRequest.length;
      i = 0;
      while (i < j)
      {
        paramMApiResponse = paramMApiRequest[i];
        this.homeProductList.add(paramMApiResponse);
        i += 1;
      }
    }
  }

  private class ListViewAdapter extends BasicAdapter
  {
    private ListViewAdapter()
    {
    }

    public int getCount()
    {
      if (!HomeProductListActivity.this.isEnd)
        return HomeProductListActivity.this.homeProductList.size() + 1;
      return HomeProductListActivity.this.homeProductList.size();
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < HomeProductListActivity.this.homeProductList.size())
        return HomeProductListActivity.this.homeProductList.get(paramInt);
      if (HomeProductListActivity.this.errorMsg == null)
        return LOADING;
      return ERROR;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    @SuppressLint({"ViewHolder"})
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      View localView = HomeProductListActivity.this.getLayoutInflater().inflate(R.layout.home_productlist_item, paramViewGroup, false);
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
      {
        paramView = (DPObject)localObject;
        paramViewGroup = (NetworkThumbView)localView.findViewById(R.id.home_product_img);
        if (paramViewGroup.getImageHandler() == null)
          paramViewGroup.setImageHandler(new HomeProductListActivity.ListViewAdapter.1(this, paramViewGroup));
        localObject = (TextView)localView.findViewById(R.id.home_product_squarenum);
        TextView localTextView1 = (TextView)localView.findViewById(R.id.home_product_room);
        TextView localTextView2 = (TextView)localView.findViewById(R.id.home_product_num);
        TextView localTextView3 = (TextView)localView.findViewById(R.id.home_product_priceinfo);
        paramViewGroup.setImage(paramView.getString("CoverImg"));
        ((TextView)localObject).setText(paramView.getString("Area"));
        localTextView1.setText(paramView.getString("Apartment"));
        localTextView2.setText("照片(" + paramView.getInt("ImgCount") + ")");
        localTextView3.setText(paramView.getString("PriceInfo"));
        localView.setOnClickListener(new HomeProductListActivity.ListViewAdapter.2(this, paramView.getInt("ProductID")));
      }
      do
      {
        return localView;
        if (localObject != LOADING)
          continue;
        if (!HomeProductListActivity.this.isInRequest)
          HomeProductListActivity.this.queryApi(HomeProductListActivity.this.stylename, HomeProductListActivity.this.start);
        return getLoadingView(paramViewGroup, paramView);
      }
      while ((localObject != ERROR) || (HomeProductListActivity.this.errorMsg == null));
      return (View)getFailedView(HomeProductListActivity.this.errorMsg, new HomeProductListActivity.ListViewAdapter.3(this), paramViewGroup, paramView);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.home.activity.HomeProductListActivity
 * JD-Core Version:    0.6.0
 */