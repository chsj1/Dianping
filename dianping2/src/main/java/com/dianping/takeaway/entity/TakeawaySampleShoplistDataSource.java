package com.dianping.takeaway.entity;

import android.os.Bundle;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.shoplist.data.BaseShopListDataSource;
import com.dianping.base.shoplist.data.DataSource.DataLoader;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.cache.CacheService;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class TakeawaySampleShoplistDataSource extends BaseShopListDataSource
  implements DataSource.DataLoader, RequestHandler<MApiRequest, MApiResponse>
{
  private static final DPObject TA_DEFAULT_CATEGORY = new DPObject("Category").edit().putInt("ID", -1).putInt("ParentID", 0).putString("Name", "全部品类").generate();
  private static final DPObject TA_DEFAULT_FILTER = new DPObject("Pair").edit().putString("ID", "-1").putString("Name", "智能排序").generate();
  protected NovaActivity activity;
  public String curAddress = "";
  private String curMultiFilterIds;
  public String extraInfo = "";
  public boolean forceFinish;
  public int geoType = 1;
  public boolean isShopListPage = true;
  public String lat;
  public String lng;
  private DPObject[] multiFilterNavs;
  public String noShopNotiDetail;
  public String noShopNotiTitle;
  public int noShopReason = -1;
  private ResultStatus resultStatus = ResultStatus.NORMAL;
  public String searchKey = "";
  private TaShoplistLoadListener shoplistLoadListener;
  private DPObject taCurCategory;
  private DPObject[] taMultiCategoryNavs;
  private MApiRequest taShopRequest;

  public TakeawaySampleShoplistDataSource(NovaActivity paramNovaActivity)
  {
    this.activity = paramNovaActivity;
    setTaDefaultFilter();
    setDataLoader(this);
  }

  private void dealEmptyMsg(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      if (!paramString.contains("|"))
        break label44;
      paramString = paramString.split("\\|");
      if (paramString.length == 2)
      {
        this.noShopNotiTitle = paramString[0];
        this.noShopNotiDetail = paramString[1];
      }
    }
    return;
    label44: this.noShopNotiTitle = paramString;
    this.noShopNotiDetail = "";
  }

  protected MApiRequest createShopRequest(int paramInt)
  {
    return null;
  }

  public DPObject curCategory()
  {
    return this.taCurCategory;
  }

  public String curMultiFilterIds()
  {
    return this.curMultiFilterIds;
  }

  public DPObject[] filterCategories()
  {
    return this.taMultiCategoryNavs;
  }

  public DPObject generateFilter()
  {
    return new DPObject().edit().putObject("CurrentMultiCategory", curCategory()).putObject("CurrentSort", curSort()).putString("CurrentMultiFilterIds", curMultiFilterIds()).generate();
  }

  public NovaActivity getActivity()
  {
    return this.activity;
  }

  public GAUserInfo getGAUserInfo()
  {
    GAUserInfo localGAUserInfo = new GAUserInfo();
    localGAUserInfo.query_id = super.queryId();
    return localGAUserInfo;
  }

  public void initFilter(DPObject paramDPObject, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    if (paramDPObject != null)
    {
      setCurCategory(paramDPObject.getObject("CurrentMultiCategory"));
      setCurSort(paramDPObject.getObject("CurrentSort"));
      setCurMultiFilterIds(paramDPObject.getString("CurrentMultiFilterIds"));
    }
    do
    {
      return;
      if ((!TextUtils.isEmpty(paramString2)) && (!TextUtils.isEmpty(paramString1)))
        setCurCategory(new DPObject("Category").edit().putInt("ParentID", Integer.valueOf(paramString1).intValue()).putInt("ID", Integer.valueOf(paramString2).intValue()).putString("Name", "").generate());
      if (TextUtils.isEmpty(paramString3))
        continue;
      setCurSort(new DPObject("Pair").edit().putString("ID", paramString3).putString("Name", "").generate());
    }
    while (TextUtils.isEmpty(paramString4));
    setCurMultiFilterIds(paramString4);
  }

  public void loadData(int paramInt, boolean paramBoolean)
  {
    if (this.taShopRequest != null);
    while (true)
    {
      return;
      this.taShopRequest = createShopRequest(paramInt);
      if (this.taShopRequest != null)
        break;
      this.resultStatus = ResultStatus.ERROR_LOCATE;
      if (this.shoplistLoadListener == null)
        continue;
      this.shoplistLoadListener.loadShopListFinsh(this.resultStatus, null);
      return;
    }
    if (paramBoolean)
      this.activity.mapiCacheService().remove(this.taShopRequest);
    this.activity.mapiService().exec(this.taShopRequest, this);
    super.changeStatus(1);
  }

  public DPObject[] multiFilterNavs()
  {
    return this.multiFilterNavs;
  }

  public void onDestroy()
  {
    if (this.taShopRequest != null)
    {
      this.activity.mapiService().abort(this.taShopRequest, null, true);
      this.taShopRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    SimpleMsg localSimpleMsg;
    if (paramMApiRequest == this.taShopRequest)
    {
      this.taShopRequest = null;
      paramMApiRequest = "错误";
      localSimpleMsg = paramMApiResponse.message();
      if (localSimpleMsg != null)
      {
        paramMApiResponse = localSimpleMsg.toString();
        if (localSimpleMsg.flag() != 3)
          break label81;
      }
    }
    label81: for (paramMApiRequest = ResultStatus.ERROR_LOCATE; ; paramMApiRequest = ResultStatus.ERROR_NETWORK)
    {
      this.resultStatus = paramMApiRequest;
      paramMApiRequest = paramMApiResponse;
      if (this.shoplistLoadListener != null)
      {
        this.shoplistLoadListener.loadShopListFinsh(this.resultStatus, localSimpleMsg);
        paramMApiRequest = paramMApiResponse;
      }
      super.setError(paramMApiRequest);
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.taShopRequest)
    {
      this.taShopRequest = null;
      this.extraInfo = "";
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        if (paramMApiRequest != null)
        {
          super.appendShops(paramMApiRequest);
          paramMApiResponse = getGAUserInfo();
          GAHelper.instance().contextStatisticsEvent(this.activity, "pageturn", paramMApiResponse, "view");
          if (!this.isShopListPage)
          {
            paramMApiResponse.keyword = this.searchKey;
            paramMApiResponse.query_id = paramMApiRequest.getString("QueryID");
            GAHelper.instance().contextStatisticsEvent(this.activity, "keyword", paramMApiResponse, "tap");
          }
          dealEmptyMsg(paramMApiRequest.getString("EmptyMsg"));
          if (!TextUtils.isEmpty(paramMApiRequest.getString("Address")))
            this.curAddress = paramMApiRequest.getString("Address");
          if ((this.geoType == 1) && ((TextUtils.isEmpty(this.lat)) || (TextUtils.isEmpty(this.lng))))
          {
            this.lat = String.valueOf(paramMApiRequest.getDouble("WMLat"));
            this.lng = String.valueOf(paramMApiRequest.getDouble("WMLng"));
            this.geoType = 2;
          }
          updateNavs(paramMApiRequest);
          parseRestObj(paramMApiRequest);
          super.changeStatus(2);
          if (shops().isEmpty())
            break label269;
          this.resultStatus = ResultStatus.NORMAL;
        }
      }
    }
    while (true)
    {
      if (this.shoplistLoadListener != null)
        this.shoplistLoadListener.loadShopListFinsh(this.resultStatus, null);
      return;
      label269: if (this.isShopListPage)
      {
        this.resultStatus = ResultStatus.ERROR_NOSHOP;
        continue;
      }
      this.resultStatus = ResultStatus.ERROR_NOSEARCH;
    }
  }

  public void onRestoreInstanceState(Bundle paramBundle)
  {
    initFilter((DPObject)paramBundle.getParcelable("shopfilter"), "", "", "", "");
    this.searchKey = paramBundle.getString("keyword");
    this.curAddress = paramBundle.getString("address");
    this.lat = paramBundle.getString("lat");
    this.lng = paramBundle.getString("lng");
    this.geoType = paramBundle.getInt("geotype");
    this.noShopReason = paramBundle.getInt("noshopreason", -1);
    this.extraInfo = paramBundle.getString("extrainfo");
    this.forceFinish = paramBundle.getBoolean("onlyfinish");
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putParcelable("shopfilter", generateFilter());
    paramBundle.putString("keyword", this.searchKey);
    paramBundle.putString("address", this.curAddress);
    paramBundle.putString("lat", this.lat);
    paramBundle.putString("lng", this.lng);
    paramBundle.putInt("geotype", this.geoType);
    paramBundle.putInt("noshopreason", this.noShopReason);
    paramBundle.putString("extrainfo", this.extraInfo);
    paramBundle.putBoolean("onlyfinish", this.forceFinish);
  }

  protected void parseRestObj(DPObject paramDPObject)
  {
  }

  public boolean setCurCategory(DPObject paramDPObject)
  {
    if ((paramDPObject == null) || (this.taCurCategory == null))
    {
      this.taCurCategory = paramDPObject;
      return true;
    }
    if (paramDPObject.getInt("ID") == this.taCurCategory.getInt("ID"))
      return false;
    this.taCurCategory = paramDPObject;
    return true;
  }

  public boolean setCurMultiFilterIds(String paramString)
  {
    if ((paramString == null) || (this.curMultiFilterIds == null))
    {
      this.curMultiFilterIds = paramString;
      return true;
    }
    ArrayList localArrayList = new ArrayList();
    localArrayList.addAll(Arrays.asList(this.curMultiFilterIds.split("\\|")));
    Object localObject = new ArrayList();
    ((ArrayList)localObject).addAll(Arrays.asList(paramString.split("\\|")));
    if (localArrayList.size() != ((ArrayList)localObject).size())
    {
      this.curMultiFilterIds = paramString;
      return true;
    }
    localObject = ((ArrayList)localObject).iterator();
    while (((Iterator)localObject).hasNext())
    {
      if (localArrayList.contains((String)((Iterator)localObject).next()))
        continue;
      this.curMultiFilterIds = paramString;
      return true;
    }
    return false;
  }

  public void setLoadingDataStatus()
  {
    super.changeStatus(1);
  }

  public void setTaDefaultFilter()
  {
    setCurCategory(TA_DEFAULT_CATEGORY);
    setCurSort(TA_DEFAULT_FILTER);
    setCurMultiFilterIds("");
  }

  public void setTaShoplistLoadListener(TaShoplistLoadListener paramTaShoplistLoadListener)
  {
    this.shoplistLoadListener = paramTaShoplistLoadListener;
  }

  public void updateNavs(DPObject paramDPObject)
  {
    if (paramDPObject.getString("CurrentMultiFilterIds") != null)
      this.curMultiFilterIds = paramDPObject.getString("CurrentMultiFilterIds");
    if (paramDPObject.getArray("MultiFilterNavs") != null)
      this.multiFilterNavs = paramDPObject.getArray("MultiFilterNavs");
    if (paramDPObject.getObject("CurrentMultiCategory") != null)
      this.taCurCategory = paramDPObject.getObject("CurrentMultiCategory");
    if (paramDPObject.getArray("MultiCategoryNavs") != null)
      this.taMultiCategoryNavs = paramDPObject.getArray("MultiCategoryNavs");
    super.updateNavs(paramDPObject);
  }

  public static enum ResultStatus
  {
    static
    {
      ERROR_NETWORK = new ResultStatus("ERROR_NETWORK", 2);
      ERROR_NOSHOP = new ResultStatus("ERROR_NOSHOP", 3);
      ERROR_LOCATE = new ResultStatus("ERROR_LOCATE", 4);
      ERROR_NOSEARCH = new ResultStatus("ERROR_NOSEARCH", 5);
      $VALUES = new ResultStatus[] { INITIAL, NORMAL, ERROR_NETWORK, ERROR_NOSHOP, ERROR_LOCATE, ERROR_NOSEARCH };
    }
  }

  public static abstract interface TaShoplistLoadListener
  {
    public abstract void loadShopListFinsh(TakeawaySampleShoplistDataSource.ResultStatus paramResultStatus, Object paramObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawaySampleShoplistDataSource
 * JD-Core Version:    0.6.0
 */