package com.dianping.main.find;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.dianping.archive.DPObject;
import com.dianping.base.adapter.MainFilterAdapter;
import com.dianping.base.adapter.SubFilterAdapter;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.AlphabetBar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public abstract class FindFilterActivity extends NovaActivity
  implements AdapterView.OnItemClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final String LOG_TAG = FindFilterActivity.class.getSimpleName();
  protected int cityId = 0;
  private FrameLayout emptyView;
  protected AlphabetBar mIndexBar;
  protected MainFilterAdapter mainItemAdapter;
  protected ListView mainItemListView;
  protected ArrayList<DPObject> mainItems = new ArrayList();
  protected MApiRequest req;
  protected DPObject selectedSubItem;
  protected SubFilterAdapter subItemAdapter;
  protected FrameLayout subItemListContainer;
  protected ListView subItemListView;
  protected ListView subItemSectionListView;
  protected ArrayList<DPObject> subItems = new ArrayList();
  protected HashMap<DPObject, ArrayList<DPObject>> subMapItems = new HashMap();

  private void requestData()
  {
    this.emptyView.setVisibility(0);
    String str = createUrl();
    if (str.equals(""))
    {
      Log.i(LOG_TAG, "can't request data because the url is null");
      return;
    }
    if (this.req != null)
      mapiService().abort(this.req, this, true);
    this.req = BasicMApiRequest.mapiGet(str, CacheType.DAILY);
    mapiService().exec(this.req, this);
  }

  private void setEmptyView()
  {
    View localView = getLayoutInflater().inflate(R.layout.loading_item_fullscreen, this.emptyView, false);
    this.emptyView.removeAllViews();
    this.emptyView.addView(localView);
  }

  private void setupView()
  {
    this.emptyView = ((FrameLayout)findViewById(R.id.empty));
    this.mainItemListView = ((ListView)findViewById(R.id.list1));
    this.subItemListContainer = ((FrameLayout)findViewById(R.id.sub_list));
    this.subItemListView = ((ListView)findViewById(R.id.list2));
    this.subItemSectionListView = ((ListView)findViewById(R.id.list3));
    this.mainItemListView.setSelector(R.drawable.list_item_no_gradual);
    this.subItemListView.setSelector(R.drawable.list_item_no_gradual);
    this.mIndexBar = ((AlphabetBar)findViewById(R.id.sidebar));
    this.mainItemAdapter = new MainAdapter(null, this);
    this.mainItemListView.setAdapter(this.mainItemAdapter);
    this.mainItemListView.setOnItemClickListener(this);
    initView();
  }

  protected abstract String createUrl();

  protected DPObject findMainItem(ArrayList<DPObject> paramArrayList, DPObject paramDPObject)
  {
    paramDPObject = getParentId(paramDPObject);
    if (paramDPObject == null)
    {
      this.mainItemAdapter.setSelectItem(0);
      return (DPObject)paramArrayList.get(0);
    }
    int i = 0;
    paramArrayList = paramArrayList.iterator();
    while (paramArrayList.hasNext())
    {
      DPObject localDPObject = (DPObject)paramArrayList.next();
      if (paramDPObject.equals(getId(localDPObject)))
      {
        this.mainItemAdapter.setSelectItem(i);
        return localDPObject;
      }
      i += 1;
    }
    return null;
  }

  protected abstract void finishWithResult(DPObject paramDPObject);

  protected abstract String getId(DPObject paramDPObject);

  protected String getParentId(DPObject paramDPObject)
  {
    return null;
  }

  protected abstract void initView();

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent().getData();
    if (paramBundle != null)
    {
      paramBundle = paramBundle.getQueryParameter("cityid");
      if (!TextUtils.isEmpty(paramBundle))
        this.cityId = Integer.parseInt(paramBundle);
    }
    super.setContentView(R.layout.find_fliter_fragment);
    this.selectedSubItem = ((DPObject)getIntent().getParcelableExtra("selectedSubItem"));
    setupView();
    setEmptyView();
    requestData();
  }

  protected void onDestroy()
  {
    if (this.req != null)
      mapiService().abort(this.req, this, true);
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.emptyView.setVisibility(8);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiResponse.result() instanceof DPObject[]))
      showList(paramMApiResponse);
    this.emptyView.setVisibility(8);
  }

  protected abstract void showList(MApiResponse paramMApiResponse);

  protected void showMainItemDropdownList(ArrayList<DPObject> paramArrayList, DPObject paramDPObject)
  {
    this.mainItemListView.setVisibility(0);
    DPObject localDPObject = findMainItem(paramArrayList, paramDPObject);
    this.mainItemAdapter.setDataSet(paramArrayList);
    if (localDPObject != null)
    {
      showSubItemDropdownList((ArrayList)this.subMapItems.get(localDPObject), paramDPObject);
      return;
    }
    this.subItemListView.setVisibility(4);
  }

  protected abstract void showSubItemDropdownList(ArrayList<DPObject> paramArrayList, DPObject paramDPObject);

  private class MainAdapter extends MainFilterAdapter
  {
    public MainAdapter(Context arg2)
    {
      super(localContext);
    }

    protected int getMainItemLayout()
    {
      return R.layout.find_filter_main_list_item;
    }
  }

  public class SubAdapter extends SubFilterAdapter
  {
    public SubAdapter(Context arg2)
    {
      super(localContext);
    }

    protected int getSubFilterItemLayout()
    {
      return R.layout.find_filter_sub_list_item;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.FindFilterActivity
 * JD-Core Version:    0.6.0
 */