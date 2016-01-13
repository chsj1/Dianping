package com.dianping.main.find;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.adapter.MainFilterAdapter;
import com.dianping.base.adapter.SubFilterAdapter;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.v1.R.id;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class CategoryFilterActivity extends FindFilterActivity
{
  private static final DPObject TOP_CATEGORY = new DPObject("Category").edit().putInt("ID", 0).putString("Name", "全部分类").putInt("ParentID", -2147483648).putInt("Distance", 500).generate();
  private boolean hasAllSubCatetory = true;
  private boolean hasHotCategory = true;
  private boolean hasTopCategory = true;

  protected String createUrl()
  {
    if (this.cityId == 0)
      this.cityId = cityId();
    return "http://m.api.dianping.com/category.bin?cityid=" + this.cityId;
  }

  protected void finishWithResult(DPObject paramDPObject)
  {
    DPObject localDPObject = findMainItem(this.mainItems, paramDPObject);
    Intent localIntent = new Intent();
    localIntent.putExtra("resultExtra", localDPObject);
    localIntent.putExtra("result", paramDPObject);
    setResult(-1, localIntent);
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
    this.hasTopCategory = getIntent().getBooleanExtra("hastopcategory", true);
    this.hasHotCategory = getIntent().getBooleanExtra("hashotcategory", true);
    this.hasAllSubCatetory = getIntent().getBooleanExtra("hasAllSubCategory", true);
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
    finishWithResult(paramView);
  }

  protected void showList(MApiResponse paramMApiResponse)
  {
    paramMApiResponse = (DPObject[])(DPObject[])paramMApiResponse.result();
    if (paramMApiResponse != null)
    {
      int j = paramMApiResponse.length;
      int i = 0;
      Object localObject;
      if (i < j)
      {
        localObject = paramMApiResponse[i];
        if (((DPObject)localObject).getInt("ParentID") == 0)
          if ((this.hasHotCategory) || (!((DPObject)localObject).getString("Name").contains("热门分类")));
        while (true)
        {
          i += 1;
          break;
          this.mainItems.add(localObject);
          continue;
          this.subItems.add(localObject);
        }
      }
      if (this.hasTopCategory)
        this.subItems.add(0, TOP_CATEGORY);
      paramMApiResponse = this.mainItems.iterator();
      while (paramMApiResponse.hasNext())
      {
        localObject = (DPObject)paramMApiResponse.next();
        ArrayList localArrayList = new ArrayList();
        this.subMapItems.put(localObject, localArrayList);
        if ((((DPObject)localObject).getInt("ID") != -2147483648) && (this.hasAllSubCatetory))
          localArrayList.add(new DPObject("Category").edit().putInt("ID", ((DPObject)localObject).getInt("ID")).putString("Name", "全部" + ((DPObject)localObject).getString("Name")).putInt("ParentID", ((DPObject)localObject).getInt("ID")).putString("FavIcon", ((DPObject)localObject).getString("FavIcon")).putInt("Distance", ((DPObject)localObject).getInt("Distance")).putString("SearchPara", ((DPObject)localObject).getString("SearchPara")).generate());
        Iterator localIterator = this.subItems.iterator();
        while (localIterator.hasNext())
        {
          DPObject localDPObject = (DPObject)localIterator.next();
          if (((DPObject)localObject).getInt("ID") != localDPObject.getInt("ParentID"))
            continue;
          localArrayList.add(localDPObject);
        }
      }
      showMainItemDropdownList(this.mainItems, this.selectedSubItem);
    }
  }

  protected void showSubItemDropdownList(ArrayList<DPObject> paramArrayList, DPObject paramDPObject)
  {
    this.subItemAdapter.setDataSet(paramArrayList, paramDPObject);
    this.subItemListView.setVisibility(0);
    this.subItemSectionListView.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.CategoryFilterActivity
 * JD-Core Version:    0.6.0
 */