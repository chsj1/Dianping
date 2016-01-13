package com.dianping.main.find;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.dianping.archive.DPObject;
import com.dianping.base.adapter.MainFilterAdapter;
import com.dianping.base.adapter.SubFilterAdapter;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.v1.R.id;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MetroFilterActivity extends FindFilterActivity
{
  private int selectedMainItemId;

  protected String createUrl()
  {
    return "http://m.api.dianping.com/metro.bin?cityid=" + cityId();
  }

  protected void finishWithResult(DPObject paramDPObject)
  {
    DPObject localDPObject = findMainItem(this.mainItems, paramDPObject);
    Intent localIntent = new Intent();
    localIntent.putExtra("resultExtra", localDPObject);
    localIntent.putExtra("result", paramDPObject);
    setResult(-1, localIntent);
    statisticsEvent("area5", "area5_subway", "", 0);
    finish();
  }

  protected String getId(DPObject paramDPObject)
  {
    return String.valueOf(paramDPObject.getInt("ID"));
  }

  protected String getParentId(DPObject paramDPObject)
  {
    if ((paramDPObject instanceof DPObject))
      return String.valueOf(paramDPObject.getInt("ParentID"));
    return null;
  }

  protected void initView()
  {
    this.subItemAdapter = new FindFilterActivity.SubAdapter(this, null, this);
    this.subItemListView.setAdapter(this.subItemAdapter);
    this.subItemListView.setOnItemClickListener(this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent().getData().getQueryParameter("selectedid");
    if (!TextUtils.isEmpty(paramBundle))
      this.selectedMainItemId = Integer.parseInt(paramBundle);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramView = (DPObject)paramAdapterView.getItemAtPosition(paramInt);
    int i = paramAdapterView.getId();
    if (i == R.id.list1)
    {
      this.mainItemAdapter.setSelectItem(paramInt);
      this.mainItemAdapter.notifyDataSetInvalidated();
      showSubItemDropdownList((ArrayList)this.subMapItems.get(paramView), null);
    }
    do
      return;
    while (i != R.id.list2);
    statisticsEvent("search5", "search5_metro_submit", paramView.getString("Name"), 0);
    finishWithResult(paramView);
  }

  protected void showList(MApiResponse paramMApiResponse)
  {
    paramMApiResponse = (DPObject[])(DPObject[])paramMApiResponse.result();
    Object localObject;
    if (paramMApiResponse != null)
    {
      int j = paramMApiResponse.length;
      int i = 0;
      if (i < j)
      {
        localObject = paramMApiResponse[i];
        if (((DPObject)localObject).getInt("ParentID") == 0)
          this.mainItems.add(localObject);
        while (true)
        {
          i += 1;
          break;
          this.subItems.add(localObject);
        }
      }
    }
    paramMApiResponse = this.mainItems.iterator();
    while (paramMApiResponse.hasNext())
    {
      localObject = (DPObject)paramMApiResponse.next();
      ArrayList localArrayList = new ArrayList();
      this.subMapItems.put(localObject, localArrayList);
      Iterator localIterator = this.subItems.iterator();
      while (localIterator.hasNext())
      {
        DPObject localDPObject = (DPObject)localIterator.next();
        if (((DPObject)localObject).getInt("ID") != localDPObject.getInt("ParentID"))
          continue;
        localArrayList.add(localDPObject);
      }
    }
    this.mainItemListView.setVisibility(0);
    this.mainItemAdapter.setDataSet(this.mainItems);
    this.mainItemAdapter.setSelectItem(this.selectedMainItemId);
    this.mainItemListView.setSelection(this.selectedMainItemId);
    showSubItemDropdownList((ArrayList)this.subMapItems.get(this.mainItems.get(this.selectedMainItemId)), this.selectedSubItem);
  }

  protected void showSubItemDropdownList(ArrayList<DPObject> paramArrayList, DPObject paramDPObject)
  {
    this.subItemAdapter.setDataSet(paramArrayList, paramDPObject);
    this.subItemListView.setVisibility(0);
    this.subItemSectionListView.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.MetroFilterActivity
 * JD-Core Version:    0.6.0
 */