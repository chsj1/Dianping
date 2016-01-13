package com.dianping.search.deallist.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.tuan.agent.TuanFilterDefaultDPObject;
import com.dianping.base.tuan.fragment.TuanAgentFragment;
import com.dianping.base.tuan.utils.TuanSharedDataKey;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.search.deallist.agent.DealListType;
import com.dianping.search.deallist.config.DefaultDealListConfig;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class TuanDealListAgentFragment extends TuanAgentFragment
{
  protected DPObject category;
  protected String channel;
  protected int cityId;
  protected String fromwhere;
  protected String keyword;
  protected DPObject region;
  LinearLayout rootView;
  protected String screening;
  protected String showMall;
  protected DPObject sort;

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new DefaultDealListConfig());
    return localArrayList;
  }

  protected boolean getSharedBoolean(TuanSharedDataKey paramTuanSharedDataKey)
  {
    paramTuanSharedDataKey = getSharedObject(paramTuanSharedDataKey);
    if ((paramTuanSharedDataKey != null) && ((paramTuanSharedDataKey instanceof Boolean)))
      return ((Boolean)paramTuanSharedDataKey).booleanValue();
    return false;
  }

  protected DPObject getSharedDPObject(TuanSharedDataKey paramTuanSharedDataKey)
  {
    paramTuanSharedDataKey = getSharedObject(paramTuanSharedDataKey);
    if ((paramTuanSharedDataKey != null) && ((paramTuanSharedDataKey instanceof DPObject)))
      return (DPObject)paramTuanSharedDataKey;
    return null;
  }

  protected DPObject[] getSharedDPObjectArray(TuanSharedDataKey paramTuanSharedDataKey)
  {
    paramTuanSharedDataKey = getSharedObject(paramTuanSharedDataKey);
    if ((paramTuanSharedDataKey != null) && ((paramTuanSharedDataKey instanceof DPObject[])))
      return (DPObject[])(DPObject[])paramTuanSharedDataKey;
    return null;
  }

  protected double getSharedDouble(TuanSharedDataKey paramTuanSharedDataKey)
  {
    paramTuanSharedDataKey = getSharedObject(paramTuanSharedDataKey);
    if ((paramTuanSharedDataKey != null) && ((paramTuanSharedDataKey instanceof Double)))
      return ((Double)paramTuanSharedDataKey).doubleValue();
    return 0.0D;
  }

  protected int getSharedInt(TuanSharedDataKey paramTuanSharedDataKey)
  {
    paramTuanSharedDataKey = getSharedObject(paramTuanSharedDataKey);
    if ((paramTuanSharedDataKey != null) && ((paramTuanSharedDataKey instanceof Integer)))
      return ((Integer)paramTuanSharedDataKey).intValue();
    return 0;
  }

  protected Object getSharedObject(TuanSharedDataKey paramTuanSharedDataKey)
  {
    return sharedObject(paramTuanSharedDataKey.toString());
  }

  protected String getSharedString(TuanSharedDataKey paramTuanSharedDataKey)
  {
    paramTuanSharedDataKey = getSharedObject(paramTuanSharedDataKey);
    if ((paramTuanSharedDataKey != null) && ((paramTuanSharedDataKey instanceof String)))
      return (String)paramTuanSharedDataKey;
    return null;
  }

  protected void handleIntent(Intent paramIntent)
  {
    if (paramIntent == null);
    while (true)
    {
      return;
      this.cityId = 0;
      try
      {
        localObject = paramIntent.getData().getQueryParameter("cityid");
        if (!TextUtils.isEmpty((CharSequence)localObject))
          this.cityId = Integer.parseInt((String)localObject);
        localObject = paramIntent.getExtras();
        if (localObject != null)
        {
          if (((Bundle)localObject).containsKey("category"))
            this.category = ((DPObject)((Bundle)localObject).getParcelable("category"));
          if (((Bundle)localObject).containsKey("region"))
            this.region = ((DPObject)((Bundle)localObject).getParcelable("region"));
          if (((Bundle)localObject).containsKey("sort"))
            this.sort = ((DPObject)((Bundle)localObject).getParcelable("sort"));
          if (((Bundle)localObject).containsKey("screening"))
            this.screening = ((Bundle)localObject).getString("screening");
          if (((Bundle)localObject).containsKey("fromwhere"))
            this.fromwhere = ((Bundle)localObject).getString("fromwhere");
        }
        if (this.category == null)
          this.category = TuanFilterDefaultDPObject.ALL_CATEGORY;
        if (this.region == null)
          this.region = TuanFilterDefaultDPObject.ALL_REGION;
        if (this.sort == null)
          this.sort = TuanFilterDefaultDPObject.DEFAULT_SORT;
        localUri = paramIntent.getData();
        if (localUri == null)
          continue;
        if ((localUri.toString().contains("categoryid=")) || (localUri.toString().contains("category=")) || (localUri.toString().contains("categoryenname=")))
          i = 0;
      }
      catch (Exception paramIntent)
      {
        try
        {
          j = Integer.valueOf(localUri.getQueryParameter("categoryid")).intValue();
          i = j;
          this.category = this.category.edit().putInt("ID", i).putString("EnName", localUri.getQueryParameter("categoryenname")).generate();
          if ((localUri.toString().contains("regionid=")) || (localUri.toString().contains("region=")) || (localUri.toString().contains("regionenname=")))
            i = 0;
        }
        catch (Exception paramIntent)
        {
          try
          {
            j = Integer.valueOf(localUri.getQueryParameter("regionid")).intValue();
            i = j;
            this.region = this.region.edit().putInt("ID", i).putString("EnName", localUri.getQueryParameter("regionenname")).putInt("Type", 2).generate();
            if ((localUri.toString().contains("rangeid=")) || (localUri.toString().contains("range=")) || (localUri.toString().contains("rangeenname=")))
              i = 0;
          }
          catch (Exception paramIntent)
          {
            try
            {
              j = Integer.valueOf(localUri.getQueryParameter("rangeid")).intValue();
              i = j;
              this.region = this.region.edit().putInt("ID", i).putString("EnName", localUri.getQueryParameter("rangeenname")).putInt("Type", 3).generate();
              Object localObject = localUri.getQueryParameter("sort");
              paramIntent = (Intent)localObject;
              if (TextUtils.isEmpty((CharSequence)localObject))
                paramIntent = localUri.getQueryParameter("filter");
              if (!TextUtils.isEmpty(paramIntent))
                i = 0;
            }
            catch (Exception paramIntent)
            {
              try
              {
                while (true)
                {
                  Uri localUri;
                  int j = Integer.parseInt(paramIntent);
                  int i = j;
                  this.sort = this.sort.edit().putInt("ID", i).generate();
                  if (localUri.toString().contains("screening="))
                    this.screening = localUri.getQueryParameter("screening");
                  this.channel = localUri.getQueryParameter("channel");
                  this.keyword = localUri.getQueryParameter("keyword");
                  this.showMall = localUri.getQueryParameter("showmall");
                  return;
                  localException1 = localException1;
                  localException1.printStackTrace();
                  continue;
                  paramIntent = paramIntent;
                  try
                  {
                    j = Integer.valueOf(localUri.getQueryParameter("category")).intValue();
                    i = j;
                  }
                  catch (Exception localException2)
                  {
                    paramIntent.printStackTrace();
                    localException2.printStackTrace();
                  }
                  continue;
                  paramIntent = paramIntent;
                  try
                  {
                    j = Integer.valueOf(localUri.getQueryParameter("region")).intValue();
                    i = j;
                  }
                  catch (Exception localException3)
                  {
                    paramIntent.printStackTrace();
                    localException3.printStackTrace();
                  }
                  continue;
                  paramIntent = paramIntent;
                  try
                  {
                    j = Integer.valueOf(localUri.getQueryParameter("range")).intValue();
                    i = j;
                  }
                  catch (Exception localException4)
                  {
                    paramIntent.printStackTrace();
                    localException4.printStackTrace();
                  }
                }
              }
              catch (Exception paramIntent)
              {
                while (true)
                  paramIntent.printStackTrace();
              }
            }
          }
        }
      }
    }
  }

  protected void initialFromIntent()
  {
    handleIntent(getActivity().getIntent());
    saveIntentData();
    String str = TuanSharedDataKey.DEAL_LIST_IS_SEARCH_MODE.toString();
    boolean bool;
    TuanSharedDataKey localTuanSharedDataKey;
    if (!TextUtils.isEmpty(this.keyword))
    {
      bool = true;
      setSharedObject(str, Boolean.valueOf(bool));
      setSharedObject(TuanSharedDataKey.DEAL_LIST_TYPE.toString(), listType());
      localTuanSharedDataKey = TuanSharedDataKey.FROM_WHERE_KEY;
      if (!TextUtils.isEmpty(this.keyword))
        break label87;
    }
    label87: for (str = "list"; ; str = "search")
    {
      setSharedObject(localTuanSharedDataKey, str);
      return;
      bool = false;
      break;
    }
  }

  protected DealListType listType()
  {
    if (getSharedBoolean(TuanSharedDataKey.DEAL_LIST_IS_SEARCH_MODE))
      return DealListType.SEARCH;
    return DealListType.NEARBY;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (DPObjectUtils.isDPObjectof(getSharedDPObject(TuanSharedDataKey.CURRENT_CATEGORY_KEY)))
    {
      ((DPActivity)getActivity()).gaExtra.category_id = Integer.valueOf(getSharedDPObject(TuanSharedDataKey.CURRENT_CATEGORY_KEY).getInt("ID"));
      return;
    }
    ((DPActivity)getActivity()).gaExtra.category_id = Integer.valueOf(0);
  }

  public void onCreate(Bundle paramBundle)
  {
    initialFromIntent();
    super.onCreate(paramBundle);
    if (paramBundle == null)
      return;
    setSharedObject(TuanSharedDataKey.CATEGORY_NAVI_KEY, paramBundle.getParcelable(TuanSharedDataKey.CATEGORY_NAVI_KEY.toString()));
    setSharedObject(TuanSharedDataKey.CATEGORY_NAVI_KEY, paramBundle.getParcelable(TuanSharedDataKey.CATEGORY_NAVI_KEY.toString()));
    setSharedObject(TuanSharedDataKey.CATEGORY_NAVI_KEY, paramBundle.getParcelable(TuanSharedDataKey.CATEGORY_NAVI_KEY.toString()));
    setSharedObject(TuanSharedDataKey.SCREENING_LIST_KEY, paramBundle.getParcelableArray(TuanSharedDataKey.SCREENING_LIST_KEY.toString()));
    setSharedObject(TuanSharedDataKey.CURRENT_CATEGORY_KEY, paramBundle.getParcelable(TuanSharedDataKey.CURRENT_CATEGORY_KEY.toString()));
    setSharedObject(TuanSharedDataKey.CURRENT_REGION_KEY, paramBundle.getParcelable(TuanSharedDataKey.CURRENT_REGION_KEY.toString()));
    setSharedObject(TuanSharedDataKey.CURRENT_SORT_KEY, paramBundle.getParcelable(TuanSharedDataKey.CURRENT_SORT_KEY.toString()));
    setSharedObject(TuanSharedDataKey.CURRENT_SCREENING_KEY, paramBundle.getString(TuanSharedDataKey.CURRENT_SCREENING_KEY.toString()));
    setSharedObject(TuanSharedDataKey.HEAD_VIEW_TYPE_KEY, Integer.valueOf(paramBundle.getInt(TuanSharedDataKey.HEAD_VIEW_TYPE_KEY.toString())));
    setSharedObject(TuanSharedDataKey.KEYWORD_KEY, paramBundle.getString(TuanSharedDataKey.KEYWORD_KEY.toString()));
    setSharedObject(TuanSharedDataKey.CHANNEL_KEY, paramBundle.getString(TuanSharedDataKey.CHANNEL_KEY.toString()));
    setSharedObject(TuanSharedDataKey.FROM_WHERE_KEY, paramBundle.getString(TuanSharedDataKey.FROM_WHERE_KEY.toString()));
    setSharedObject(TuanSharedDataKey.DEAL_LIST_IS_SEARCH_MODE, Boolean.valueOf(paramBundle.getBoolean(TuanSharedDataKey.DEAL_LIST_IS_SEARCH_MODE.toString())));
    setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOW_MALL, paramBundle.getString(TuanSharedDataKey.DEAL_LIST_SHOW_MALL.toString()));
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.rootView = ((LinearLayout)paramLayoutInflater.inflate(R.layout.deal_list_agent_container, paramViewGroup, false).findViewById(R.id.content_root));
    setAgentContainerView(this.rootView);
    return this.rootView;
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable(TuanSharedDataKey.CATEGORY_NAVI_KEY.toString(), getSharedDPObject(TuanSharedDataKey.CATEGORY_NAVI_KEY));
    paramBundle.putParcelable(TuanSharedDataKey.REGION_NAVI_KEY.toString(), getSharedDPObject(TuanSharedDataKey.REGION_NAVI_KEY));
    paramBundle.putParcelable(TuanSharedDataKey.SORT_NAVI_KEY.toString(), getSharedDPObject(TuanSharedDataKey.SORT_NAVI_KEY));
    paramBundle.putParcelableArray(TuanSharedDataKey.SCREENING_LIST_KEY.toString(), getSharedDPObjectArray(TuanSharedDataKey.SCREENING_LIST_KEY));
    paramBundle.putParcelable(TuanSharedDataKey.CURRENT_CATEGORY_KEY.toString(), getSharedDPObject(TuanSharedDataKey.CURRENT_CATEGORY_KEY));
    paramBundle.putParcelable(TuanSharedDataKey.CURRENT_REGION_KEY.toString(), getSharedDPObject(TuanSharedDataKey.CURRENT_REGION_KEY));
    paramBundle.putParcelable(TuanSharedDataKey.CURRENT_SORT_KEY.toString(), getSharedDPObject(TuanSharedDataKey.CURRENT_SORT_KEY));
    paramBundle.putString(TuanSharedDataKey.CURRENT_SCREENING_KEY.toString(), getSharedString(TuanSharedDataKey.CURRENT_SCREENING_KEY));
    paramBundle.putInt(TuanSharedDataKey.HEAD_VIEW_TYPE_KEY.toString(), getSharedInt(TuanSharedDataKey.HEAD_VIEW_TYPE_KEY));
    paramBundle.putString(TuanSharedDataKey.KEYWORD_KEY.toString(), getSharedString(TuanSharedDataKey.KEYWORD_KEY));
    paramBundle.putString(TuanSharedDataKey.CHANNEL_KEY.toString(), getSharedString(TuanSharedDataKey.CHANNEL_KEY));
    paramBundle.putString(TuanSharedDataKey.FROM_WHERE_KEY.toString(), getSharedString(TuanSharedDataKey.FROM_WHERE_KEY));
    paramBundle.putBoolean(TuanSharedDataKey.DEAL_LIST_IS_SEARCH_MODE.toString(), getSharedBoolean(TuanSharedDataKey.DEAL_LIST_IS_SEARCH_MODE));
    paramBundle.putSerializable(TuanSharedDataKey.DEAL_LIST_SHOW_MALL.toString(), getSharedString(TuanSharedDataKey.DEAL_LIST_SHOW_MALL));
  }

  protected void saveIntentData()
  {
    setSharedObject(TuanSharedDataKey.CITY_ID_KEY.toString(), Integer.valueOf(this.cityId));
    setSharedObject(TuanSharedDataKey.CURRENT_CATEGORY_KEY.toString(), this.category);
    setSharedObject(TuanSharedDataKey.CURRENT_REGION_KEY.toString(), this.region);
    setSharedObject(TuanSharedDataKey.CURRENT_SORT_KEY.toString(), this.sort);
    setSharedObject(TuanSharedDataKey.CURRENT_SCREENING_KEY.toString(), this.screening);
    setSharedObject(TuanSharedDataKey.CHANNEL_KEY.toString(), this.channel);
    setSharedObject(TuanSharedDataKey.KEYWORD_KEY.toString(), this.keyword);
    setSharedObject(TuanSharedDataKey.FROM_WHERE_KEY.toString(), this.fromwhere);
    setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOW_MALL.toString(), this.showMall);
  }

  protected void setSharedObject(TuanSharedDataKey paramTuanSharedDataKey, Object paramObject)
  {
    setSharedObject(paramTuanSharedDataKey.toString(), paramObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.fragment.TuanDealListAgentFragment
 * JD-Core Version:    0.6.0
 */