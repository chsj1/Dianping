package com.dianping.main.find;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.adapter.MainFilterAdapter;
import com.dianping.base.adapter.SubFilterAdapter;
import com.dianping.base.widget.SectionListItem;
import com.dianping.base.widget.TipsDialog;
import com.dianping.base.widget.TipsDialog.TipsType;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.AlphabetBar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RegionFilterActivity extends FindFilterActivity
{
  private static final DPObject ALL_REGION = new DPObject("Region").edit().putInt("ID", 0).putString("Name", "全部商区").putInt("ParentID", 0).generate();
  private static final int REQUEST_CODE_CUSTOM_LOCATION = 1;
  private RegionSubAdapter regionSubItemAdapter;
  String source;

  private DPObject findRegionMainItem(ArrayList<DPObject> paramArrayList, DPObject paramDPObject)
  {
    paramArrayList = findMainItem(paramArrayList, paramDPObject);
    if (paramArrayList != null)
      return paramArrayList;
    return ALL_REGION;
  }

  private DPObject[] sortBy1stChar(DPObject[] paramArrayOfDPObject)
  {
    if (paramArrayOfDPObject == null)
      return null;
    Arrays.sort(paramArrayOfDPObject, new Comparator()
    {
      public int compare(DPObject paramDPObject1, DPObject paramDPObject2)
      {
        return String.valueOf((char)paramDPObject1.getInt("FirstChar")).compareTo(String.valueOf((char)paramDPObject2.getInt("FirstChar")));
      }
    });
    return paramArrayOfDPObject;
  }

  protected String createUrl()
  {
    if (this.cityId == 0)
      this.cityId = cityId();
    return "http://m.api.dianping.com/region.bin?cityid=" + this.cityId;
  }

  protected void finishWithResult(DPObject paramDPObject)
  {
    DPObject localDPObject = findRegionMainItem(this.mainItems, paramDPObject);
    Intent localIntent = new Intent();
    localIntent.putExtra("resultExtra", localDPObject);
    localIntent.putExtra("result", paramDPObject);
    setResult(-1, localIntent);
    statisticsEvent("area5", "area5_morearea", localDPObject.getString("Name"), 0);
    finish();
  }

  protected String getId(DPObject paramDPObject)
  {
    return String.valueOf(paramDPObject.getInt("ID"));
  }

  protected String getParentId(DPObject paramDPObject)
  {
    return String.valueOf(paramDPObject.getInt("ParentId"));
  }

  protected void initView()
  {
    this.regionSubItemAdapter = new RegionSubAdapter(null, this);
    this.subItemSectionListView.setAdapter(this.regionSubItemAdapter);
    this.subItemSectionListView.setOnItemClickListener(this);
    this.subItemSectionListView.setVisibility(0);
    this.subItemListView.setVisibility(8);
    this.mIndexBar.setVisibility(0);
    this.mIndexBar.setListView(this.subItemSectionListView);
    this.mIndexBar.setSectionIndexter(this.regionSubItemAdapter);
    this.subItemAdapter = new FindFilterActivity.SubAdapter(this, null, this);
    this.subItemListView.setAdapter(this.subItemAdapter);
    this.subItemListView.setOnItemClickListener(this);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt2 == -1) && (paramInt1 == 1))
    {
      Object localObject = new Intent();
      paramIntent = new DPObject().edit().putDouble("lat", paramIntent.getDoubleExtra("lat", 0.0D)).putDouble("lng", paramIntent.getDoubleExtra("lng", 0.0D)).putInt("maptype", paramIntent.getIntExtra("maptype", 1)).putString("address", paramIntent.getStringExtra("address")).generate();
      ((Intent)localObject).putExtra("result", paramIntent);
      setResult(-1, (Intent)localObject);
      finish();
      localObject = preferences().edit();
      ((SharedPreferences.Editor)localObject).putString("findconditions_region", paramIntent.getDouble("lat") + "IAMSPLIT" + paramIntent.getDouble("lng") + "IAMSPLIT" + paramIntent.getString("address") + "IAMSPLIT" + paramIntent.getInt("range"));
      ((SharedPreferences.Editor)localObject).commit();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent().getData();
    if (paramBundle != null)
      this.source = paramBundle.getQueryParameter("source");
    if ((!"addshop".equals(this.source)) && (!"hotel".equals(this.source)))
    {
      setRightTitleButton(R.drawable.navibar_icon_search, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://customlocationlist"));
          RegionFilterActivity.this.startActivityForResult(paramView, 1);
          if ("findconditionsmain".equals(RegionFilterActivity.this.source))
          {
            RegionFilterActivity.this.statisticsEvent("search5", "search5_areahot_localsearch", "", 0);
            return;
          }
          RegionFilterActivity.this.statisticsEvent("search5", "search5_area_localsearch", "", 0);
        }
      });
      if (!preferences().getBoolean("hasCustomLocationTipShow", false))
        new Handler()
        {
          public void handleMessage(Message paramMessage)
          {
            int i = RegionFilterActivity.this.subItemListView.getTop();
            int j = RegionFilterActivity.this.subItemListView.getRight();
            paramMessage = new TipsDialog(RegionFilterActivity.this, "不知道在哪个商区？在地图上找找吧：）", TipsDialog.TipsType.UP_RIGHT, j - 40, i + 55);
            paramMessage.setOnCancelListener(new DialogInterface.OnCancelListener()
            {
              public void onCancel(DialogInterface paramDialogInterface)
              {
                paramDialogInterface = DPActivity.preferences().edit();
                paramDialogInterface.putBoolean("hasCustomLocationTipShow", true);
                paramDialogInterface.commit();
              }
            });
            paramMessage.show();
          }
        }
        .sendEmptyMessageDelayed(0, 100L);
    }
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramView = paramAdapterView.getItemAtPosition(paramInt);
    int i = paramAdapterView.getId();
    if (i == R.id.list3)
      if ((paramView instanceof SectionListItem))
        finishWithResult((DPObject)((SectionListItem)paramView).item);
    do
    {
      return;
      paramAdapterView = (DPObject)paramView;
      if (i != R.id.list1)
        continue;
      this.mainItemAdapter.setSelectItem(paramInt);
      this.mainItemAdapter.notifyDataSetInvalidated();
      showSubItemDropdownList((ArrayList)this.subMapItems.get(paramAdapterView), null);
      return;
    }
    while (i != R.id.list2);
    finishWithResult(paramAdapterView);
  }

  protected void showList(MApiResponse paramMApiResponse)
  {
    Object localObject1 = (DPObject[])(DPObject[])paramMApiResponse.result();
    if (localObject1 == null)
    {
      onRequestFailed(this.req, paramMApiResponse);
      return;
    }
    sortBy1stChar(localObject1);
    this.mainItems.add(ALL_REGION);
    int j = localObject1.length;
    int i = 0;
    if (i < j)
    {
      paramMApiResponse = localObject1[i];
      if (paramMApiResponse.getInt("ParentID") == 0)
        this.mainItems.add(paramMApiResponse);
      while (true)
      {
        i += 1;
        break;
        this.subItems.add(paramMApiResponse);
      }
    }
    localObject1 = this.mainItems.iterator();
    if (((Iterator)localObject1).hasNext())
    {
      DPObject localDPObject = (DPObject)((Iterator)localObject1).next();
      ArrayList localArrayList = new ArrayList();
      this.subMapItems.put(localDPObject, localArrayList);
      Object localObject2 = new DPObject("Region").edit().putInt("ID", localDPObject.getInt("ID"));
      if (localDPObject == ALL_REGION);
      for (paramMApiResponse = localDPObject.getString("Name"); ; paramMApiResponse = localDPObject.getString("Name") + "全境")
      {
        localArrayList.add(((DPObject.Editor)localObject2).putString("Name", paramMApiResponse).putInt("ParentID", localDPObject.getInt("ID")).generate());
        paramMApiResponse = this.subItems.iterator();
        while (paramMApiResponse.hasNext())
        {
          localObject2 = (DPObject)paramMApiResponse.next();
          if (localDPObject == ALL_REGION)
            localArrayList.add(localObject2);
          if (localDPObject.getInt("ID") != ((DPObject)localObject2).getInt("ParentID"))
            continue;
          localArrayList.add(localObject2);
        }
        break;
      }
    }
    localObject1 = this.mainItems;
    if (this.selectedSubItem == null);
    for (paramMApiResponse = ALL_REGION; ; paramMApiResponse = this.selectedSubItem)
    {
      showMainItemDropdownList((ArrayList)localObject1, paramMApiResponse);
      return;
    }
  }

  protected void showSubItemDropdownList(ArrayList<DPObject> paramArrayList, DPObject paramDPObject)
  {
    if (this.mainItemAdapter.getSelectItem() == 0)
    {
      this.regionSubItemAdapter.setDataSet(paramArrayList, paramDPObject);
      this.subItemSectionListView.setVisibility(0);
      this.subItemListView.setVisibility(8);
      this.mIndexBar.setVisibility(0);
      this.mIndexBar.setSections(this.regionSubItemAdapter.sections());
      return;
    }
    this.subItemAdapter.setDataSet(paramArrayList, paramDPObject);
    this.subItemSectionListView.setVisibility(8);
    this.subItemListView.setVisibility(0);
    this.mIndexBar.setVisibility(8);
  }

  class RegionSubAdapter extends SubFilterAdapter
    implements SectionIndexer
  {
    private final Map<View, String> currentViewSections = new HashMap();
    private List<SectionListItem> data = new ArrayList();
    private final Map<Integer, Integer> itemPositions = new LinkedHashMap();
    private final Map<Integer, String> sectionPositions = new LinkedHashMap();
    private List<String> sections = new ArrayList();

    public RegionSubAdapter(Context arg2)
    {
      super(localContext);
      updateSessionCache();
    }

    private View getSectionView(View paramView, String paramString)
    {
      View localView = paramView;
      if (paramView == null)
        localView = createNewSectionView();
      setSectionText(paramString, localView);
      replaceSectionViewsInMaps(paramString, localView);
      return localView;
    }

    private boolean isTheSame(String paramString1, String paramString2)
    {
      if (paramString1 == null)
        return paramString2 == null;
      return paramString1.equals(paramString2);
    }

    protected View createNewSectionView()
    {
      return RegionFilterActivity.this.getLayoutInflater().inflate(R.layout.section_view, null, false);
    }

    public int getCount()
    {
      return this.sectionPositions.size() + this.itemPositions.size();
    }

    public Object getItem(int paramInt)
    {
      if (isSection(paramInt))
        return this.sectionPositions.get(Integer.valueOf(paramInt));
      return this.data.get(((Integer)this.itemPositions.get(Integer.valueOf(paramInt))).intValue());
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public int getItemViewType(int paramInt)
    {
      if (isSection(paramInt))
        return 0;
      return 1;
    }

    protected String getName(int paramInt)
    {
      return ((DPObject)super.getItem(((Integer)this.itemPositions.get(Integer.valueOf(paramInt))).intValue())).getString("Name");
    }

    public int getPositionForSection(int paramInt)
    {
      Iterator localIterator = this.sectionPositions.keySet().iterator();
      while (localIterator.hasNext())
      {
        Integer localInteger = (Integer)localIterator.next();
        if (((String)this.sectionPositions.get(localInteger)).equals(this.sections.get(paramInt)))
          return localInteger.intValue();
      }
      return 0;
    }

    public int getSectionForPosition(int paramInt)
    {
      Object localObject = getItem(paramInt);
      int i;
      if (!(localObject instanceof SectionListItem))
      {
        i = 0;
        return i;
      }
      paramInt = 0;
      while (true)
      {
        if (paramInt >= this.sections.size())
          break label67;
        i = paramInt;
        if (((String)this.sections.get(paramInt)).equals(((SectionListItem)localObject).section))
          break;
        paramInt += 1;
      }
      label67: return 0;
    }

    public Object[] getSections()
    {
      String[] arrayOfString = new String[this.sections.size()];
      this.sections.toArray(arrayOfString);
      return arrayOfString;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (isSection(paramInt))
        return getSectionView(paramView, (String)this.sectionPositions.get(Integer.valueOf(paramInt)));
      return super.getView(paramInt, paramView, paramViewGroup);
    }

    public int getViewTypeCount()
    {
      return 2;
    }

    public boolean isEnabled(int paramInt)
    {
      return !isSection(paramInt);
    }

    public boolean isSection(int paramInt)
    {
      return this.sectionPositions.containsKey(Integer.valueOf(paramInt));
    }

    protected void replaceSectionViewsInMaps(String paramString, View paramView)
    {
      monitorenter;
      try
      {
        if (this.currentViewSections.containsKey(paramView))
          this.currentViewSections.remove(paramView);
        this.currentViewSections.put(paramView, paramString);
        return;
      }
      finally
      {
        monitorexit;
      }
      throw paramString;
    }

    public String[] sections()
    {
      return (String[])this.sections.toArray(new String[0]);
    }

    public void setData(List<DPObject> paramList)
    {
      this.sections.clear();
      this.data.clear();
      paramList = paramList.iterator();
      while (paramList.hasNext())
      {
        DPObject localDPObject = (DPObject)paramList.next();
        String str = String.valueOf((char)localDPObject.getInt("FirstChar"));
        if ((!this.sections.contains(str)) && (!"".equals(str)))
          this.sections.add(str);
        this.data.add(new SectionListItem(localDPObject, str));
      }
      if (this.sections.size() == 0)
        RegionFilterActivity.this.mIndexBar.setVisibility(8);
    }

    public void setDataSet(ArrayList<DPObject> paramArrayList)
    {
      super.setDataSet(paramArrayList);
      setData(paramArrayList);
      updateSessionCache();
      notifyDataSetChanged();
    }

    public void setDataSet(List<DPObject> paramList, DPObject paramDPObject)
    {
      super.setDataSet(paramList, paramDPObject);
      setData(paramList);
      updateSessionCache();
      notifyDataSetChanged();
    }

    protected void setSectionText(String paramString, View paramView)
    {
      ((TextView)paramView.findViewById(R.id.listTextView)).setText(paramString);
    }

    public void updateSessionCache()
    {
      monitorenter;
      int i = 0;
      try
      {
        this.sectionPositions.clear();
        this.itemPositions.clear();
        Object localObject1 = null;
        int m = this.data.size();
        int j = 0;
        while (j < m)
        {
          SectionListItem localSectionListItem = (SectionListItem)this.data.get(j);
          int k = i;
          Object localObject3 = localObject1;
          if (!"".equals(localSectionListItem.section))
          {
            k = i;
            localObject3 = localObject1;
            if (!isTheSame((String)localObject1, localSectionListItem.section))
            {
              this.sectionPositions.put(Integer.valueOf(i), localSectionListItem.section);
              localObject3 = localSectionListItem.section;
              k = i + 1;
            }
          }
          this.itemPositions.put(Integer.valueOf(k), Integer.valueOf(j));
          i = k + 1;
          j += 1;
          localObject1 = localObject3;
        }
        return;
      }
      finally
      {
        monitorexit;
      }
      throw localObject2;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.RegionFilterActivity
 * JD-Core Version:    0.6.0
 */