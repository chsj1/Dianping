package com.dianping.search.deallist.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.AgentListConfig;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.shoplist.activity.AbstractTabListActivity;
import com.dianping.base.shoplist.activity.AbstractTabListActivity.GaPager;
import com.dianping.base.shoplist.activity.AbstractTabListActivity.TitleListener;
import com.dianping.base.tuan.agent.TuanFilterDefaultDPObject;
import com.dianping.base.tuan.utils.TuanSharedDataKey;
import com.dianping.search.deallist.config.DealListTabConfig;
import com.dianping.widget.view.GAHelper;
import java.util.ArrayList;
import java.util.UUID;

public class TuanDealListTabAgentFragment extends TuanDealListAgentFragment
  implements AbstractTabListActivity.TitleListener, AbstractTabListActivity.GaPager
{
  public static final String SHOP_LIST_HOST_TUAN = "tgshoplist";
  public static final String SHOP_LIST_TAB_PARAM_KEY = "tab";
  public static final String SHOP_LIST_TAB_PARAM_VALUE_TUAN = "1";
  public static final String SHOP_LIST_TARGET_PARAM_KEY = "target";
  public static final String SHOP_LIST_TARGET_PARAM_VALUE_TUAN = "tgshoplist";
  protected boolean isCreate = false;
  protected boolean refreshable = false;
  protected int shopCategoryId = 0;
  protected int shopRangeId = 0;
  protected int shopRegionId = 0;
  protected int shopSortId = 0;

  public void dispatchMessage(AgentMessage paramAgentMessage)
  {
    super.dispatchMessage(paramAgentMessage);
    if ("deal_list_data_analized".equals(paramAgentMessage.what))
    {
      setSharedObject(TuanSharedDataKey.DEAL_LIST_TAB_IS_SHOP_DATA.toString(), Boolean.valueOf(false));
      formatCateRegionSave2Shop();
      paramAgentMessage = sharedObject(TuanSharedDataKey.KEYWORD_KEY.toString());
      if ((paramAgentMessage == null) || (!(paramAgentMessage instanceof String)))
        break label68;
    }
    label68: for (paramAgentMessage = (String)paramAgentMessage; ; paramAgentMessage = "")
    {
      saveKeyword(paramAgentMessage);
      return;
    }
  }

  public void formatCateRegionFromShop()
  {
    Object localObject = getActivity();
    if ((localObject instanceof AbstractTabListActivity))
    {
      setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOP_CATEGORY_ID.toString(), Integer.valueOf(((AbstractTabListActivity)localObject).getShopCategoryId()));
      setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOP_REGION_ID.toString(), Integer.valueOf(((AbstractTabListActivity)localObject).getShopRegionId()));
      setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOP_RANGE_ID.toString(), Integer.valueOf(((AbstractTabListActivity)localObject).getShopRangeId()));
      localObject = ((AbstractTabListActivity)localObject).getShopSortId();
    }
    try
    {
      setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOP_SORT_ID.toString(), Integer.valueOf(Integer.parseInt((String)localObject)));
      return;
    }
    catch (Exception localException)
    {
    }
  }

  public void formatCateRegionSave2Shop()
  {
    FragmentActivity localFragmentActivity = getActivity();
    Object localObject;
    if ((localFragmentActivity instanceof AbstractTabListActivity))
    {
      localObject = sharedObject(TuanSharedDataKey.DEAL_LIST_TAB_IS_SHOP_DATA.toString());
      if ((localObject == null) || (!((Boolean)localObject).booleanValue()))
        break label129;
    }
    label129: for (int i = 1; ; i = 0)
    {
      if (i == 0)
      {
        ((AbstractTabListActivity)localFragmentActivity).setShopCategoryId(getCurrentCategory().getInt("ExtraId"));
        ((AbstractTabListActivity)localFragmentActivity).setShopSordId(getCurrentSort().getInt("ExtraId") + "");
        localObject = getCurrentRegion();
        if (localObject != null)
        {
          if (((DPObject)localObject).getInt("Type") != 3)
            break;
          ((AbstractTabListActivity)localFragmentActivity).setShopRangeId(getCurrentRegion().getInt("ID"));
        }
      }
      return;
    }
    ((AbstractTabListActivity)localFragmentActivity).setShopRegionId(getCurrentRegion().getInt("ExtraId"));
  }

  protected ArrayList<AgentListConfig> generaterDefaultConfigAgentList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new DealListTabConfig());
    return localArrayList;
  }

  public DPObject getCurrentCategory()
  {
    DPObject localDPObject2 = getSharedDPObject(TuanSharedDataKey.CURRENT_CATEGORY_KEY);
    DPObject localDPObject1 = localDPObject2;
    if (localDPObject2 == null)
      localDPObject1 = TuanFilterDefaultDPObject.ALL_CATEGORY;
    return localDPObject1;
  }

  public DPObject getCurrentRegion()
  {
    DPObject localDPObject2 = getSharedDPObject(TuanSharedDataKey.CURRENT_REGION_KEY);
    DPObject localDPObject1 = localDPObject2;
    if (localDPObject2 == null)
      localDPObject1 = TuanFilterDefaultDPObject.ALL_REGION;
    return localDPObject1;
  }

  public String getCurrentScreening()
  {
    String str2 = getSharedString(TuanSharedDataKey.CURRENT_SCREENING_KEY);
    String str1 = str2;
    if (str2 == null)
      str1 = "";
    return str1;
  }

  public DPObject getCurrentSort()
  {
    DPObject localDPObject2 = getSharedDPObject(TuanSharedDataKey.CURRENT_SORT_KEY);
    DPObject localDPObject1 = localDPObject2;
    if (localDPObject2 == null)
      localDPObject1 = TuanFilterDefaultDPObject.DEFAULT_SORT;
    return localDPObject1;
  }

  public String getPageName()
  {
    return "shoptglist";
  }

  protected DPObject getSharedDPObject(TuanSharedDataKey paramTuanSharedDataKey)
  {
    paramTuanSharedDataKey = sharedObject(paramTuanSharedDataKey.toString());
    if ((paramTuanSharedDataKey != null) && ((paramTuanSharedDataKey instanceof DPObject)))
      return (DPObject)paramTuanSharedDataKey;
    return null;
  }

  protected String getSharedString(TuanSharedDataKey paramTuanSharedDataKey)
  {
    paramTuanSharedDataKey = sharedObject(paramTuanSharedDataKey.toString());
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
        Object localObject = paramIntent.getData().getQueryParameter("cityid");
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
        paramIntent = paramIntent.getData();
        if (paramIntent == null)
          continue;
        if (!paramIntent.toString().contains("categoryid="))
          if (!paramIntent.toString().contains("category="))
            break label266;
      }
      catch (Exception localException5)
      {
        try
        {
          this.shopCategoryId = Integer.valueOf(paramIntent.getQueryParameter("categoryid")).intValue();
          setSharedObject(TuanSharedDataKey.DEAL_LIST_TAB_IS_SHOP_DATA.toString(), Boolean.valueOf(true));
          label266: if (!paramIntent.toString().contains("regionid="))
            if (!paramIntent.toString().contains("region="))
              break label323;
        }
        catch (Exception localException5)
        {
          try
          {
            this.shopRegionId = Integer.valueOf(paramIntent.getQueryParameter("regionid")).intValue();
            setSharedObject(TuanSharedDataKey.DEAL_LIST_TAB_IS_SHOP_DATA.toString(), Boolean.valueOf(true));
            label323: this.shopRangeId = -2;
            if (!paramIntent.toString().contains("rangeid="))
              if (!paramIntent.toString().contains("range="))
                break label386;
          }
          catch (Exception localException5)
          {
            try
            {
              this.shopRangeId = Integer.valueOf(paramIntent.getQueryParameter("rangeid")).intValue();
              setSharedObject(TuanSharedDataKey.DEAL_LIST_TAB_IS_SHOP_DATA.toString(), Boolean.valueOf(true));
              label386: if (!paramIntent.toString().contains("sort="))
                if (!paramIntent.toString().contains("filter="))
                  break label443;
            }
            catch (Exception localException5)
            {
              try
              {
                while (true)
                {
                  this.shopSortId = Integer.valueOf(paramIntent.getQueryParameter("sort")).intValue();
                  setSharedObject(TuanSharedDataKey.DEAL_LIST_TAB_IS_SHOP_DATA.toString(), Boolean.valueOf(true));
                  label443: if (paramIntent.toString().contains("screening="))
                    this.screening = paramIntent.getQueryParameter("screening");
                  this.channel = paramIntent.getQueryParameter("channel");
                  this.keyword = paramIntent.getQueryParameter("keyword");
                  return;
                  localException1 = localException1;
                  localException1.printStackTrace();
                  continue;
                  localException2 = localException2;
                  try
                  {
                    this.shopCategoryId = Integer.valueOf(paramIntent.getQueryParameter("category")).intValue();
                    setSharedObject(TuanSharedDataKey.DEAL_LIST_TAB_IS_SHOP_DATA.toString(), Boolean.valueOf(true));
                  }
                  catch (Exception localException6)
                  {
                    localException2.printStackTrace();
                    localException6.printStackTrace();
                  }
                  continue;
                  localException3 = localException3;
                  try
                  {
                    this.shopRegionId = Integer.valueOf(paramIntent.getQueryParameter("region")).intValue();
                    setSharedObject(TuanSharedDataKey.DEAL_LIST_TAB_IS_SHOP_DATA.toString(), Boolean.valueOf(true));
                  }
                  catch (Exception localException7)
                  {
                    localException3.printStackTrace();
                    localException7.printStackTrace();
                  }
                  continue;
                  localException4 = localException4;
                  try
                  {
                    this.shopRangeId = Integer.valueOf(paramIntent.getQueryParameter("range")).intValue();
                    setSharedObject(TuanSharedDataKey.DEAL_LIST_TAB_IS_SHOP_DATA.toString(), Boolean.valueOf(true));
                  }
                  catch (Exception localException8)
                  {
                    localException4.printStackTrace();
                    localException8.printStackTrace();
                  }
                }
              }
              catch (Exception localException5)
              {
                while (true)
                  try
                  {
                    this.shopSortId = Integer.valueOf(paramIntent.getQueryParameter("filter")).intValue();
                    setSharedObject(TuanSharedDataKey.DEAL_LIST_TAB_IS_SHOP_DATA.toString(), Boolean.valueOf(true));
                  }
                  catch (Exception localException9)
                  {
                    localException5.printStackTrace();
                    localException9.printStackTrace();
                  }
              }
            }
          }
        }
      }
    }
  }

  protected void initialFromIntent()
  {
    Object localObject2 = getActivity().getIntent().getData();
    String str = ((Uri)localObject2).getQueryParameter("tab");
    Object localObject1 = ((Uri)localObject2).getQueryParameter("target");
    localObject2 = ((Uri)localObject2).getHost();
    if (("1".equals(str)) || ("tgshoplist".equals(localObject1)) || ("tgshoplist".equals(localObject2)))
      super.initialFromIntent();
    localObject1 = TuanSharedDataKey.FROM_WHERE_KEY;
    if (TextUtils.isEmpty(getSharedString(TuanSharedDataKey.KEYWORD_KEY)));
    for (str = "shoplist"; ; str = "shopsearch")
    {
      setSharedObject((TuanSharedDataKey)localObject1, str);
      return;
    }
  }

  protected void loadKeyword()
  {
    FragmentActivity localFragmentActivity = getActivity();
    if ((localFragmentActivity instanceof AbstractTabListActivity))
      this.keyword = ((AbstractTabListActivity)localFragmentActivity).getKeyword();
  }

  public void onBlur()
  {
    formatCateRegionSave2Shop();
    this.refreshable = true;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.isCreate = true;
    if ((getActivity() != null) && (getActivity().getIntent() != null))
    {
      Uri localUri = getActivity().getIntent().getData();
      if (localUri != null)
      {
        if ("tgshoplist".equals(localUri.getQueryParameter("target")))
          this.isCreate = false;
        if ("tgshoplist".equals(localUri.getHost()))
          this.isCreate = false;
        if ("1".equals(localUri.getQueryParameter("tab")))
          this.isCreate = false;
      }
    }
    if (paramBundle == null)
      return;
    setSharedObject(TuanSharedDataKey.DEAL_LIST_TAB_IS_SHOP_DATA, Boolean.valueOf(paramBundle.getBoolean(TuanSharedDataKey.DEAL_LIST_TAB_IS_SHOP_DATA.toString())));
    setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOP_CATEGORY_ID, Integer.valueOf(paramBundle.getInt(TuanSharedDataKey.DEAL_LIST_SHOP_CATEGORY_ID.toString())));
    setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOP_REGION_ID, Integer.valueOf(paramBundle.getInt(TuanSharedDataKey.DEAL_LIST_SHOP_REGION_ID.toString())));
    setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOP_RANGE_ID, Integer.valueOf(paramBundle.getInt(TuanSharedDataKey.DEAL_LIST_SHOP_RANGE_ID.toString())));
    setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOP_SORT_ID, Integer.valueOf(paramBundle.getInt(TuanSharedDataKey.DEAL_LIST_SHOP_SORT_ID.toString())));
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
  }

  public void onFocus()
  {
    boolean bool = true;
    if (!this.refreshable);
    do
      return;
    while (!isAdded());
    setSharedObject(TuanSharedDataKey.DEAL_LIST_TAB_IS_SHOP_DATA.toString(), Boolean.valueOf(true));
    loadKeyword();
    setSharedObject(TuanSharedDataKey.KEYWORD_KEY.toString(), this.keyword);
    Object localObject = TuanSharedDataKey.DEAL_LIST_IS_SEARCH_MODE.toString();
    if (!TextUtils.isEmpty(this.keyword));
    while (true)
    {
      setSharedObject((String)localObject, Boolean.valueOf(bool));
      formatCateRegionFromShop();
      dispatchMessage(new AgentMessage("deal_list_filter_reset_commend"));
      localObject = (NovaActivity)getActivity();
      GAHelper.instance().setGAPageName("shoptglist");
      GAHelper.instance().setRequestId(getActivity(), UUID.randomUUID().toString(), ((NovaActivity)localObject).gaExtra, GAHelper.instance().getUtmAndMarketingSource(((NovaActivity)localObject).gaExtra, ((NovaActivity)localObject).getIntent().getData()));
      this.refreshable = false;
      return;
      bool = false;
    }
  }

  public void onResume()
  {
    if (!this.isCreate)
    {
      NovaActivity localNovaActivity = (NovaActivity)getActivity();
      if (((localNovaActivity instanceof AbstractTabListActivity)) && ((((AbstractTabListActivity)localNovaActivity).getCurrentFragment() instanceof TuanDealListTabAgentFragment)))
      {
        GAHelper.instance().setGAPageName("shoptglist");
        GAHelper.instance().setRequestId(getActivity(), UUID.randomUUID().toString(), localNovaActivity.gaExtra, GAHelper.instance().getUtmAndMarketingSource(localNovaActivity.gaExtra, localNovaActivity.getIntent().getData()));
      }
    }
    this.isCreate = false;
    super.onResume();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putBoolean(TuanSharedDataKey.DEAL_LIST_TAB_IS_SHOP_DATA.toString(), getSharedBoolean(TuanSharedDataKey.DEAL_LIST_TAB_IS_SHOP_DATA));
    paramBundle.putInt(TuanSharedDataKey.DEAL_LIST_SHOP_CATEGORY_ID.toString(), getSharedInt(TuanSharedDataKey.DEAL_LIST_SHOP_CATEGORY_ID));
    paramBundle.putInt(TuanSharedDataKey.DEAL_LIST_SHOP_REGION_ID.toString(), getSharedInt(TuanSharedDataKey.DEAL_LIST_SHOP_REGION_ID));
    paramBundle.putInt(TuanSharedDataKey.DEAL_LIST_SHOP_RANGE_ID.toString(), getSharedInt(TuanSharedDataKey.DEAL_LIST_SHOP_RANGE_ID));
    paramBundle.putInt(TuanSharedDataKey.DEAL_LIST_SHOP_SORT_ID.toString(), getSharedInt(TuanSharedDataKey.DEAL_LIST_SHOP_SORT_ID));
  }

  public void onSearchClick()
  {
    dispatchMessage(new AgentMessage("deal_list_search_call_up_search_fragment_commend"));
  }

  protected void saveIntentData()
  {
    super.saveIntentData();
    setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOP_CATEGORY_ID, Integer.valueOf(this.shopCategoryId));
    setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOP_REGION_ID, Integer.valueOf(this.shopRegionId));
    setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOP_RANGE_ID, Integer.valueOf(this.shopRangeId));
    setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOP_SORT_ID, Integer.valueOf(this.shopSortId));
  }

  protected void saveKeyword(String paramString)
  {
    FragmentActivity localFragmentActivity = getActivity();
    if ((localFragmentActivity instanceof AbstractTabListActivity))
      ((AbstractTabListActivity)localFragmentActivity).setKeyword(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.fragment.TuanDealListTabAgentFragment
 * JD-Core Version:    0.6.0
 */