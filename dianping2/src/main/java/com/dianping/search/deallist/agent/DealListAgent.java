package com.dianping.search.deallist.agent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.shoplist.data.model.ShopDataModel;
import com.dianping.base.shoplist.widget.ShopListItem;
import com.dianping.base.tuan.agent.TuanCellAgent;
import com.dianping.base.tuan.agent.TuanFilterDefaultDPObject;
import com.dianping.base.tuan.utils.TuanSharedDataKey;
import com.dianping.base.tuan.widget.ViewItemLocation;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.DealListItem;
import com.dianping.base.widget.NovaFragment;
import com.dianping.base.widget.ViewItemInterface;
import com.dianping.base.widget.ViewItemType;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.loader.MyResources;
import com.dianping.search.deallist.widget.AggViewItem;
import com.dianping.search.deallist.widget.AggViewItem.OnAggItemClickListener;
import com.dianping.search.deallist.widget.AggViewItem.OnMainItemClickListener;
import com.dianping.search.deallist.widget.BannerViewItem;
import com.dianping.search.deallist.widget.HuiViewItem;
import com.dianping.search.deallist.widget.MarketAggViewItem;
import com.dianping.search.deallist.widget.MarketAggViewItem.OnAggItemClickListener;
import com.dianping.search.deallist.widget.RightTagViewItem;
import com.dianping.search.deallist.widget.TitleTipViewItem;
import com.dianping.search.deallist.widget.ViewItemFactory;
import com.dianping.search.deallist.widget.WarningTitleTipViewItem;
import com.dianping.util.network.NetworkUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Locale;
import java.util.UUID;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class DealListAgent extends TuanCellAgent
  implements AdapterView.OnItemClickListener
{
  protected static final String DEAL_LIST = "30DealList";
  public static final String DEF_FILTER_EMPTY_MSG = "此条件下没有团购哦，换个地区或分类看看吧。";
  public static final String DEF_FILTER_TODAY_EMPTY_MSG = "此条件下没有今日发布的团购哦，换个地区或分类看看吧。";
  protected static final DecimalFormat FMT = new DecimalFormat("#.00000");
  private static final SimpleDateFormat SDF = new SimpleDateFormat("MM-dd", Locale.getDefault());
  protected double accuracy;
  protected String aggType;
  protected DPObject data;
  protected DealAdapter dealAdapter;
  protected View dealListEmptyView;
  protected FrameLayout emptyView;
  protected String from_title;
  protected boolean isFirstPage = true;
  protected double latitude;
  protected PullToRefreshListView listView;
  protected double longitude;
  protected final AggViewItem.OnAggItemClickListener onDealItemClickListener = new AggViewItem.OnAggItemClickListener()
  {
    public void onClick(DPObject paramDPObject1, DPObject paramDPObject2, int paramInt1, int paramInt2)
    {
      if (!DPObjectUtils.isDPObjectof(paramDPObject2, "ViewItem"));
      while (true)
      {
        return;
        paramDPObject1 = ViewItemType.parseFromValue(paramDPObject2.getInt("Type"));
        String str = paramDPObject2.getString("Link");
        if (!TextUtils.isEmpty(str))
        {
          paramDPObject1 = new Intent("android.intent.action.VIEW", Uri.parse(str));
          DealListAgent.this.startActivity(paramDPObject1);
          return;
        }
        int i;
        int j;
        int k;
        if (paramDPObject1 == ViewItemType.HUI)
        {
          i = 0;
          j = 0;
          k = 0;
          paramInt2 = i;
          paramInt1 = j;
        }
        try
        {
          paramDPObject1 = paramDPObject2.getObject("Hui");
          paramInt2 = i;
          paramInt1 = j;
          i = paramDPObject1.getInt("HuiId");
          paramInt2 = i;
          paramInt1 = j;
          j = paramDPObject1.getInt("PayShopId");
          paramInt2 = i;
          paramInt1 = j;
          int m = paramDPObject1.getInt("RelatedDealId");
          paramInt1 = m;
          paramInt2 = i;
          i = paramInt1;
          paramDPObject1 = new Intent("android.intent.action.VIEW", Uri.parse(String.format("dianping://huidetail?id=%d&shopid=%d&dealid=%d", new Object[] { Integer.valueOf(paramInt2), Integer.valueOf(j), Integer.valueOf(i) })));
          DealListAgent.this.startActivity(paramDPObject1);
          return;
          if (paramDPObject1 != ViewItemType.TUAN_DEAL)
            continue;
          paramDPObject1 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuandeal"));
          paramDPObject1.putExtra("deal", paramDPObject2.getObject("Deal"));
          DealListAgent.this.startActivity(paramDPObject1);
          return;
        }
        catch (java.lang.Exception paramDPObject1)
        {
          while (true)
          {
            i = k;
            j = paramInt1;
          }
        }
      }
    }
  };
  protected final AggViewItem.OnMainItemClickListener onMainClickListener = new AggViewItem.OnMainItemClickListener()
  {
    public void onClick(DPObject paramDPObject, int paramInt)
    {
      if (!DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem"));
      do
      {
        return;
        paramDPObject = paramDPObject.getObject("Shop");
      }
      while (!DPObjectUtils.isDPObjectof(paramDPObject, "Shop"));
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + paramDPObject.getInt("ID")));
      localIntent.putExtra("shopId", paramDPObject.getInt("ID"));
      localIntent.putExtra("shop", paramDPObject);
      DealListAgent.this.startActivity(localIntent);
    }
  };
  protected final MarketAggViewItem.OnAggItemClickListener onMarketAggDealItemClickListener = new MarketAggViewItem.OnAggItemClickListener()
  {
    public void onClick(DPObject paramDPObject, int paramInt1, int paramInt2)
    {
      if (!DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem"));
      do
      {
        return;
        paramDPObject = paramDPObject.getString("Link");
      }
      while (TextUtils.isEmpty(paramDPObject));
      paramDPObject = new Intent("android.intent.action.VIEW", Uri.parse(paramDPObject));
      DealListAgent.this.startActivity(paramDPObject);
    }
  };
  protected String queryId;
  protected PullToRefreshListView.OnRefreshListener refreshListener = new PullToRefreshListView.OnRefreshListener()
  {
    public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
    {
      DealListAgent.this.dealAdapter.pullToReset(true);
    }
  };
  protected String requestId = UUID.randomUUID().toString();
  protected View rootView;

  public DealListAgent(Object paramObject)
  {
    super(paramObject);
    if ((paramObject instanceof NovaFragment))
    {
      this.from_title = ((NovaFragment)paramObject).getStringParam("banner_title");
      if (TextUtils.isEmpty(this.from_title))
        this.from_title = ((NovaFragment)paramObject).getStringParam("category_title");
      if (TextUtils.isEmpty(this.from_title))
        this.from_title = ((NovaFragment)paramObject).getStringParam("operation_title");
    }
  }

  public ArrayList<NameValuePair> addStaticEventsExtra(ArrayList<NameValuePair> paramArrayList)
  {
    paramArrayList.add(new BasicNameValuePair("from", getSharedString(TuanSharedDataKey.FROM_WHERE_KEY)));
    return paramArrayList;
  }

  protected String aggViewItemExpandGaAction()
  {
    return "tuan5_juhe_expand";
  }

  protected void analyseResponse(DPObject paramDPObject)
  {
    this.listView.onRefreshComplete();
    this.queryId = paramDPObject.getString("QueryID");
    this.dealAdapter.appendViewItem(paramDPObject);
    if (this.dealAdapter.getDataList().size() <= 0)
      Log.w("tuan_deal_list_warning", "DataList.size <= 0");
    setSharedObject(TuanSharedDataKey.DEAL_LIST_SHOW_MALL, null);
    doGAHelperStatisticEvent(this.queryId, getKeyword());
    if (this.isFirstPage)
    {
      DPObject[] arrayOfDPObject = paramDPObject.getArray("NaviBars");
      Object localObject7 = null;
      Object localObject3 = null;
      Object localObject8 = null;
      Object localObject1 = null;
      Object localObject9 = null;
      Object localObject2 = null;
      Object localObject4 = localObject7;
      Object localObject5 = localObject8;
      Object localObject6 = localObject9;
      if (arrayOfDPObject != null)
      {
        localObject4 = localObject7;
        localObject5 = localObject8;
        localObject6 = localObject9;
        if (arrayOfDPObject.length > 0)
        {
          int j = arrayOfDPObject.length;
          int i = 0;
          localObject4 = localObject3;
          localObject5 = localObject1;
          localObject6 = localObject2;
          if (i < j)
          {
            localObject4 = arrayOfDPObject[i];
            if (((DPObject)localObject4).getInt("Type") == 1)
            {
              localObject6 = localObject1;
              localObject5 = localObject4;
            }
            while (true)
            {
              i += 1;
              localObject3 = localObject5;
              localObject1 = localObject6;
              break;
              if (((DPObject)localObject4).getInt("Type") == 2)
              {
                localObject5 = localObject3;
                localObject6 = localObject4;
                continue;
              }
              localObject5 = localObject3;
              localObject6 = localObject1;
              if (((DPObject)localObject4).getInt("Type") != 4)
                continue;
              localObject2 = localObject4;
              localObject5 = localObject3;
              localObject6 = localObject1;
            }
          }
        }
      }
      setSharedObject(TuanSharedDataKey.CATEGORY_NAVI_KEY, localObject4);
      setSharedObject(TuanSharedDataKey.REGION_NAVI_KEY, localObject5);
      setSharedObject(TuanSharedDataKey.SORT_NAVI_KEY, localObject6);
      localObject1 = paramDPObject.getArray("TagNavis");
      setSharedObject(TuanSharedDataKey.CATEGORY_TAG_NAVIS_KEY, localObject1);
      if (!getSharedBoolean(TuanSharedDataKey.NOT_UPDATE_SCREENING_DATA))
      {
        paramDPObject = paramDPObject.getArray("ScreeningList");
        setSharedObject(TuanSharedDataKey.SCREENING_LIST_KEY, paramDPObject);
      }
      setSharedObject(TuanSharedDataKey.CURRENT_CATEGORY_KEY, findSelectedNavi((DPObject)localObject4));
      setSharedObject(TuanSharedDataKey.CURRENT_REGION_KEY, findSelectedNavi(localObject5));
      setSharedObject(TuanSharedDataKey.CURRENT_SORT_KEY, findSelectedNavi(localObject6));
      dispatchMessage(new AgentMessage("deal_list_data_analized"));
    }
    this.isFirstPage = false;
  }

  protected void clickDeal(DPObject paramDPObject)
  {
    int i = paramDPObject.getInt("DealType");
    if (i == 7)
      return;
    if (i == 5)
    {
      paramDPObject = paramDPObject.getString("Link");
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://web?url=" + URLEncoder.encode(paramDPObject))));
      return;
    }
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://tuandeal"));
    localIntent.putExtra("deal", paramDPObject);
    startActivity(localIntent);
  }

  protected void clickShop(ShopDataModel paramShopDataModel)
  {
    StringBuilder localStringBuilder = new StringBuilder("dianping://shopinfo");
    localStringBuilder.append("?").append("id=").append(paramShopDataModel.shopId);
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(localStringBuilder.toString())));
  }

  // ERROR //
  public StringBuilder createDealRequestUrl(int paramInt)
  {
    // Byte code:
    //   0: iload_1
    //   1: ifne +8 -> 9
    //   4: aload_0
    //   5: iconst_1
    //   6: putfield 107	com/dianping/search/deallist/agent/DealListAgent:isFirstPage	Z
    //   9: new 296	java/lang/StringBuilder
    //   12: dup
    //   13: invokespecial 298	java/lang/StringBuilder:<init>	()V
    //   16: astore_2
    //   17: aload_2
    //   18: ldc_w 356
    //   21: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   24: pop
    //   25: aload_2
    //   26: ldc_w 358
    //   29: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   32: pop
    //   33: aload_2
    //   34: ldc_w 360
    //   37: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   40: pop
    //   41: aload_0
    //   42: getstatic 363	com/dianping/base/tuan/utils/TuanSharedDataKey:CITY_ID_KEY	Lcom/dianping/base/tuan/utils/TuanSharedDataKey;
    //   45: invokevirtual 367	com/dianping/search/deallist/agent/DealListAgent:getSharedInt	(Lcom/dianping/base/tuan/utils/TuanSharedDataKey;)I
    //   48: ifle +460 -> 508
    //   51: aload_2
    //   52: aload_0
    //   53: getstatic 363	com/dianping/base/tuan/utils/TuanSharedDataKey:CITY_ID_KEY	Lcom/dianping/base/tuan/utils/TuanSharedDataKey;
    //   56: invokevirtual 367	com/dianping/search/deallist/agent/DealListAgent:getSharedInt	(Lcom/dianping/base/tuan/utils/TuanSharedDataKey;)I
    //   59: invokevirtual 349	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   62: pop
    //   63: aload_0
    //   64: invokevirtual 371	com/dianping/search/deallist/agent/DealListAgent:getCurrentRegion	()Lcom/dianping/archive/DPObject;
    //   67: ifnull +58 -> 125
    //   70: aload_0
    //   71: invokevirtual 371	com/dianping/search/deallist/agent/DealListAgent:getCurrentRegion	()Lcom/dianping/archive/DPObject;
    //   74: ldc 233
    //   76: invokevirtual 237	com/dianping/archive/DPObject:getInt	(Ljava/lang/String;)I
    //   79: iconst_2
    //   80: if_icmpne +446 -> 526
    //   83: aload_2
    //   84: ldc_w 373
    //   87: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: aload_0
    //   91: invokevirtual 371	com/dianping/search/deallist/agent/DealListAgent:getCurrentRegion	()Lcom/dianping/archive/DPObject;
    //   94: ldc_w 375
    //   97: invokevirtual 237	com/dianping/archive/DPObject:getInt	(Ljava/lang/String;)I
    //   100: invokevirtual 349	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   103: pop
    //   104: aload_2
    //   105: ldc_w 377
    //   108: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   111: aload_0
    //   112: invokevirtual 371	com/dianping/search/deallist/agent/DealListAgent:getCurrentRegion	()Lcom/dianping/archive/DPObject;
    //   115: ldc_w 379
    //   118: invokevirtual 187	com/dianping/archive/DPObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   121: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   124: pop
    //   125: aload_0
    //   126: invokevirtual 382	com/dianping/search/deallist/agent/DealListAgent:getCurrentCategory	()Lcom/dianping/archive/DPObject;
    //   129: ifnull +45 -> 174
    //   132: aload_2
    //   133: ldc_w 384
    //   136: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   139: aload_0
    //   140: invokevirtual 382	com/dianping/search/deallist/agent/DealListAgent:getCurrentCategory	()Lcom/dianping/archive/DPObject;
    //   143: ldc_w 375
    //   146: invokevirtual 237	com/dianping/archive/DPObject:getInt	(Ljava/lang/String;)I
    //   149: invokevirtual 349	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   152: pop
    //   153: aload_2
    //   154: ldc_w 386
    //   157: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   160: aload_0
    //   161: invokevirtual 382	com/dianping/search/deallist/agent/DealListAgent:getCurrentCategory	()Lcom/dianping/archive/DPObject;
    //   164: ldc_w 379
    //   167: invokevirtual 187	com/dianping/archive/DPObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   170: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   173: pop
    //   174: aload_0
    //   175: invokevirtual 389	com/dianping/search/deallist/agent/DealListAgent:getCurrentSort	()Lcom/dianping/archive/DPObject;
    //   178: ifnull +24 -> 202
    //   181: aload_2
    //   182: ldc_w 391
    //   185: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   188: aload_0
    //   189: invokevirtual 389	com/dianping/search/deallist/agent/DealListAgent:getCurrentSort	()Lcom/dianping/archive/DPObject;
    //   192: ldc_w 375
    //   195: invokevirtual 237	com/dianping/archive/DPObject:getInt	(Ljava/lang/String;)I
    //   198: invokevirtual 349	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   201: pop
    //   202: aload_0
    //   203: invokevirtual 395	com/dianping/search/deallist/agent/DealListAgent:location	()Lcom/dianping/model/Location;
    //   206: ifnull +37 -> 243
    //   209: aload_0
    //   210: aload_0
    //   211: invokevirtual 395	com/dianping/search/deallist/agent/DealListAgent:location	()Lcom/dianping/model/Location;
    //   214: invokevirtual 400	com/dianping/model/Location:latitude	()D
    //   217: putfield 402	com/dianping/search/deallist/agent/DealListAgent:latitude	D
    //   220: aload_0
    //   221: aload_0
    //   222: invokevirtual 395	com/dianping/search/deallist/agent/DealListAgent:location	()Lcom/dianping/model/Location;
    //   225: invokevirtual 404	com/dianping/model/Location:longitude	()D
    //   228: putfield 406	com/dianping/search/deallist/agent/DealListAgent:longitude	D
    //   231: aload_0
    //   232: aload_0
    //   233: invokevirtual 395	com/dianping/search/deallist/agent/DealListAgent:location	()Lcom/dianping/model/Location;
    //   236: invokevirtual 408	com/dianping/model/Location:accuracy	()I
    //   239: i2d
    //   240: putfield 410	com/dianping/search/deallist/agent/DealListAgent:accuracy	D
    //   243: aload_0
    //   244: getfield 402	com/dianping/search/deallist/agent/DealListAgent:latitude	D
    //   247: dconst_0
    //   248: dcmpl
    //   249: ifeq +24 -> 273
    //   252: aload_2
    //   253: ldc_w 412
    //   256: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   259: getstatic 74	com/dianping/search/deallist/agent/DealListAgent:FMT	Ljava/text/DecimalFormat;
    //   262: aload_0
    //   263: getfield 402	com/dianping/search/deallist/agent/DealListAgent:latitude	D
    //   266: invokevirtual 416	java/text/DecimalFormat:format	(D)Ljava/lang/String;
    //   269: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   272: pop
    //   273: aload_0
    //   274: getfield 406	com/dianping/search/deallist/agent/DealListAgent:longitude	D
    //   277: dconst_0
    //   278: dcmpl
    //   279: ifeq +24 -> 303
    //   282: aload_2
    //   283: ldc_w 418
    //   286: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   289: getstatic 74	com/dianping/search/deallist/agent/DealListAgent:FMT	Ljava/text/DecimalFormat;
    //   292: aload_0
    //   293: getfield 406	com/dianping/search/deallist/agent/DealListAgent:longitude	D
    //   296: invokevirtual 416	java/text/DecimalFormat:format	(D)Ljava/lang/String;
    //   299: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   302: pop
    //   303: aload_0
    //   304: getfield 410	com/dianping/search/deallist/agent/DealListAgent:accuracy	D
    //   307: dconst_0
    //   308: dcmpl
    //   309: ifeq +18 -> 327
    //   312: aload_2
    //   313: ldc_w 420
    //   316: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   319: aload_0
    //   320: getfield 410	com/dianping/search/deallist/agent/DealListAgent:accuracy	D
    //   323: invokevirtual 423	java/lang/StringBuilder:append	(D)Ljava/lang/StringBuilder;
    //   326: pop
    //   327: aload_2
    //   328: ldc_w 425
    //   331: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   334: iload_1
    //   335: invokevirtual 349	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   338: pop
    //   339: aload_0
    //   340: invokevirtual 222	com/dianping/search/deallist/agent/DealListAgent:getKeyword	()Ljava/lang/String;
    //   343: invokestatic 137	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   346: ifne +225 -> 571
    //   349: aload_0
    //   350: aload_0
    //   351: invokevirtual 222	com/dianping/search/deallist/agent/DealListAgent:getKeyword	()Ljava/lang/String;
    //   354: invokevirtual 428	com/dianping/search/deallist/agent/DealListAgent:setSearchEmpty	(Ljava/lang/String;)V
    //   357: aload_2
    //   358: ldc_w 430
    //   361: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   364: aload_0
    //   365: invokevirtual 222	com/dianping/search/deallist/agent/DealListAgent:getKeyword	()Ljava/lang/String;
    //   368: ldc_w 432
    //   371: invokestatic 435	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   374: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   377: pop
    //   378: aload_0
    //   379: invokevirtual 438	com/dianping/search/deallist/agent/DealListAgent:getScreening	()Ljava/lang/String;
    //   382: astore_3
    //   383: aload_3
    //   384: invokestatic 137	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   387: ifne +21 -> 408
    //   390: aload_2
    //   391: ldc_w 440
    //   394: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   397: aload_3
    //   398: ldc_w 432
    //   401: invokestatic 435	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   404: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   407: pop
    //   408: aload_0
    //   409: invokevirtual 443	com/dianping/search/deallist/agent/DealListAgent:getChannel	()Ljava/lang/String;
    //   412: invokestatic 137	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   415: ifne +18 -> 433
    //   418: aload_2
    //   419: ldc_w 445
    //   422: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   425: aload_0
    //   426: invokevirtual 443	com/dianping/search/deallist/agent/DealListAgent:getChannel	()Ljava/lang/String;
    //   429: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   432: pop
    //   433: aload_0
    //   434: invokevirtual 449	com/dianping/search/deallist/agent/DealListAgent:getFragment	()Lcom/dianping/base/app/loader/AgentFragment;
    //   437: invokevirtual 455	com/dianping/base/app/loader/AgentFragment:getActivity	()Landroid/support/v4/app/FragmentActivity;
    //   440: invokestatic 461	com/dianping/util/network/NetworkUtils:isWIFIConnection	(Landroid/content/Context;)Z
    //   443: ifeq +180 -> 623
    //   446: aload_2
    //   447: ldc_w 463
    //   450: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   453: pop
    //   454: aload_0
    //   455: invokestatic 99	java/util/UUID:randomUUID	()Ljava/util/UUID;
    //   458: invokevirtual 103	java/util/UUID:toString	()Ljava/lang/String;
    //   461: putfield 105	com/dianping/search/deallist/agent/DealListAgent:requestId	Ljava/lang/String;
    //   464: aload_2
    //   465: ldc_w 465
    //   468: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   471: aload_0
    //   472: getfield 105	com/dianping/search/deallist/agent/DealListAgent:requestId	Ljava/lang/String;
    //   475: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   478: pop
    //   479: aload_0
    //   480: getstatic 215	com/dianping/base/tuan/utils/TuanSharedDataKey:DEAL_LIST_SHOW_MALL	Lcom/dianping/base/tuan/utils/TuanSharedDataKey;
    //   483: invokevirtual 157	com/dianping/search/deallist/agent/DealListAgent:getSharedString	(Lcom/dianping/base/tuan/utils/TuanSharedDataKey;)Ljava/lang/String;
    //   486: astore_3
    //   487: aload_3
    //   488: invokestatic 137	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   491: ifne +15 -> 506
    //   494: aload_2
    //   495: ldc_w 467
    //   498: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   501: aload_3
    //   502: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   505: pop
    //   506: aload_2
    //   507: areturn
    //   508: aload_2
    //   509: aload_0
    //   510: invokevirtual 449	com/dianping/search/deallist/agent/DealListAgent:getFragment	()Lcom/dianping/base/app/loader/AgentFragment;
    //   513: invokevirtual 471	com/dianping/base/app/loader/AgentFragment:city	()Lcom/dianping/model/City;
    //   516: invokevirtual 476	com/dianping/model/City:id	()I
    //   519: invokevirtual 349	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   522: pop
    //   523: goto -460 -> 63
    //   526: aload_0
    //   527: invokevirtual 371	com/dianping/search/deallist/agent/DealListAgent:getCurrentRegion	()Lcom/dianping/archive/DPObject;
    //   530: ldc 233
    //   532: invokevirtual 237	com/dianping/archive/DPObject:getInt	(Ljava/lang/String;)I
    //   535: iconst_3
    //   536: if_icmpne -411 -> 125
    //   539: aload_2
    //   540: ldc_w 478
    //   543: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   546: aload_0
    //   547: invokevirtual 371	com/dianping/search/deallist/agent/DealListAgent:getCurrentRegion	()Lcom/dianping/archive/DPObject;
    //   550: ldc_w 375
    //   553: invokevirtual 237	com/dianping/archive/DPObject:getInt	(Ljava/lang/String;)I
    //   556: invokevirtual 349	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   559: pop
    //   560: goto -435 -> 125
    //   563: astore_3
    //   564: aload_3
    //   565: invokevirtual 481	java/lang/Exception:printStackTrace	()V
    //   568: goto -190 -> 378
    //   571: aload_0
    //   572: invokevirtual 389	com/dianping/search/deallist/agent/DealListAgent:getCurrentSort	()Lcom/dianping/archive/DPObject;
    //   575: ifnull +31 -> 606
    //   578: ldc_w 483
    //   581: aload_0
    //   582: invokevirtual 389	com/dianping/search/deallist/agent/DealListAgent:getCurrentSort	()Lcom/dianping/archive/DPObject;
    //   585: ldc_w 375
    //   588: invokevirtual 187	com/dianping/archive/DPObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   591: invokevirtual 488	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   594: ifeq +12 -> 606
    //   597: aload_0
    //   598: ldc 29
    //   600: invokevirtual 491	com/dianping/search/deallist/agent/DealListAgent:setEmpty	(Ljava/lang/String;)V
    //   603: goto -225 -> 378
    //   606: aload_0
    //   607: ldc 26
    //   609: invokevirtual 491	com/dianping/search/deallist/agent/DealListAgent:setEmpty	(Ljava/lang/String;)V
    //   612: goto -234 -> 378
    //   615: astore_3
    //   616: aload_3
    //   617: invokevirtual 492	java/io/UnsupportedEncodingException:printStackTrace	()V
    //   620: goto -212 -> 408
    //   623: aload_2
    //   624: ldc_w 494
    //   627: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   630: pop
    //   631: goto -177 -> 454
    //
    // Exception table:
    //   from	to	target	type
    //   357	378	563	java/lang/Exception
    //   390	408	615	java/io/UnsupportedEncodingException
  }

  protected void doGAHelperStatisticEvent(String paramString1, String paramString2)
  {
    GAUserInfo localGAUserInfo = getGAExtra();
    localGAUserInfo.query_id = paramString1;
    localGAUserInfo.keyword = paramString2;
    GAHelper.instance().contextStatisticsEvent(getContext(), "searchdealgn.bin", localGAUserInfo, "view");
  }

  public DPObject findSelectedNavi(DPObject paramDPObject)
  {
    if (paramDPObject == null)
    {
      paramDPObject = null;
      return paramDPObject;
    }
    if ((paramDPObject.getBoolean("Selected")) && (paramDPObject.getArray("Subs") != null) && (paramDPObject.getArray("Subs").length != 0))
    {
      DPObject[] arrayOfDPObject = paramDPObject.getArray("Subs");
      int j = arrayOfDPObject.length;
      int i = 0;
      while (true)
      {
        if (i >= j)
          break label116;
        DPObject localDPObject = arrayOfDPObject[i];
        if (localDPObject.getBoolean("Selected"))
        {
          paramDPObject = localDPObject;
          if (localDPObject.getArray("Subs") == null)
            break;
          paramDPObject = localDPObject;
          if (localDPObject.getArray("Subs").length == 0)
            break;
          return findSelectedNavi(localDPObject);
        }
        i += 1;
      }
    }
    label116: return paramDPObject;
  }

  protected String getChannel()
  {
    return getSharedString(TuanSharedDataKey.CHANNEL_KEY);
  }

  protected DPObject getCurrentCategory()
  {
    return getSharedDPObject(TuanSharedDataKey.CURRENT_CATEGORY_KEY);
  }

  protected DPObject getCurrentRegion()
  {
    return getSharedDPObject(TuanSharedDataKey.CURRENT_REGION_KEY);
  }

  protected DPObject getCurrentSort()
  {
    return getSharedDPObject(TuanSharedDataKey.CURRENT_SORT_KEY);
  }

  protected DealListType getDealListType()
  {
    Object localObject = getSharedObject(TuanSharedDataKey.DEAL_LIST_TYPE);
    DealListType localDealListType2 = DealListType.COMMON;
    DealListType localDealListType1 = localDealListType2;
    if (localObject != null)
    {
      localDealListType1 = localDealListType2;
      if ((localObject instanceof DealListType))
        localDealListType1 = (DealListType)localObject;
    }
    return localDealListType1;
  }

  protected String getKeyword()
  {
    return getSharedString(TuanSharedDataKey.KEYWORD_KEY);
  }

  protected String getScreening()
  {
    return getSharedString(TuanSharedDataKey.CURRENT_SCREENING_KEY);
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    if (("deal_list_start_request_COMMEND".equals(paramAgentMessage.what)) || ("deal_list_filter_data_changed".equals(paramAgentMessage.what)) || ("deal_list_keyword_changed".equals(paramAgentMessage.what)))
      this.dealAdapter.reset();
  }

  protected boolean isListEmpty(DPObject paramDPObject)
  {
    if (paramDPObject == null);
    do
      return true;
    while ((paramDPObject.getArray("List") == null) || (paramDPObject.getArray("List").length == 0));
    return false;
  }

  protected boolean isListEmpty(DPObject[] paramArrayOfDPObject)
  {
    if (paramArrayOfDPObject == null);
    do
      return true;
    while (paramArrayOfDPObject.length == 0);
    return false;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.listView == null)
      setupView();
    updateView();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    DPObject localDPObject;
    int j;
    int k;
    int m;
    int i;
    if (paramAdapterView == this.listView)
    {
      paramAdapterView = paramAdapterView.getItemAtPosition(paramInt);
      if (DPObjectUtils.isDPObjectof(paramAdapterView, "ViewItem"))
      {
        localDPObject = (DPObject)paramAdapterView;
        ViewItemType localViewItemType = ViewItemType.parseFromValue(localDPObject.getInt("Type"));
        if (localViewItemType == ViewItemType.SHOP)
        {
          paramView = paramView.getTag();
          if (DPObjectUtils.isDPObjectof(paramView, "ViewItem"))
            clickShop(new ShopDataModel(((DPObject)paramView).getObject("Shop")));
        }
        if ((localViewItemType == ViewItemType.TUAN_DEAL) || (localViewItemType == ViewItemType.RECOMMEND_DEAL))
          clickDeal(((DPObject)paramAdapterView).getObject("Deal"));
        if (localViewItemType == ViewItemType.HUI)
        {
          j = 0;
          k = 0;
          m = 0;
          i = j;
          paramInt = k;
        }
      }
    }
    try
    {
      paramAdapterView = localDPObject.getObject("Hui");
      i = j;
      paramInt = k;
      j = paramAdapterView.getInt("HuiId");
      i = j;
      paramInt = k;
      k = paramAdapterView.getInt("PayShopId");
      i = j;
      paramInt = k;
      int n = paramAdapterView.getInt("RelatedDealId");
      paramInt = n;
      i = j;
      j = paramInt;
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse(String.format("dianping://huidetail?id=%d&shopid=%d&dealid=%d", new Object[] { Integer.valueOf(i), Integer.valueOf(k), Integer.valueOf(j) }))));
      return;
    }
    catch (java.lang.Exception paramAdapterView)
    {
      while (true)
      {
        j = m;
        k = paramInt;
      }
    }
  }

  protected void setEmpty(String paramString)
  {
    ((TextView)this.dealListEmptyView.findViewById(R.id.title)).setText(paramString);
  }

  protected void setEmptyView(View paramView)
  {
    if (this.emptyView != null)
    {
      this.listView.setEmptyView(this.emptyView);
      if (paramView != null)
      {
        this.emptyView.removeAllViews();
        this.emptyView.addView(paramView);
      }
    }
  }

  protected void setSearchEmpty(String paramString)
  {
    ((TextView)this.dealListEmptyView.findViewById(R.id.title)).setText("找不到与");
    paramString = new SpannableString(paramString);
    paramString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_color_orange)), 0, paramString.length(), 33);
    ((TextView)this.dealListEmptyView.findViewById(R.id.title)).append(paramString);
    ((TextView)this.dealListEmptyView.findViewById(R.id.title)).append("相关的团购");
    this.dealListEmptyView.findViewById(R.id.ic_tips).setVisibility(0);
    this.dealListEmptyView.findViewById(R.id.ic_fail_message).setVisibility(0);
    ((TextView)this.dealListEmptyView.findViewById(R.id.ic_fail_message)).setText("或暂时没有与");
    ((TextView)this.dealListEmptyView.findViewById(R.id.ic_fail_message)).append(paramString);
    ((TextView)this.dealListEmptyView.findViewById(R.id.ic_fail_message)).append("相关的团购");
  }

  protected void setupView()
  {
    this.listView = ((PullToRefreshListView)this.res.inflate(getContext(), R.layout.deal_list_agent, null, false));
    this.dealAdapter = new DealAdapter(getContext());
    this.dealListEmptyView = this.res.inflate(getContext(), R.layout.deal_list_empty_view, this.emptyView, false);
    this.listView.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 1.0F));
    this.listView.setBackgroundDrawable(null);
    this.listView.setSelector(R.drawable.list_item);
  }

  protected void updateView()
  {
    if (getCurrentCategory() == null)
      setSharedObject(TuanSharedDataKey.CURRENT_CATEGORY_KEY, TuanFilterDefaultDPObject.ALL_CATEGORY);
    if (getCurrentRegion() == null)
      setSharedObject(TuanSharedDataKey.CURRENT_REGION_KEY, TuanFilterDefaultDPObject.ALL_REGION);
    if (getCurrentSort() == null)
      setSharedObject(TuanSharedDataKey.CURRENT_SORT_KEY, TuanFilterDefaultDPObject.DEFAULT_SORT);
    this.listView.setAdapter(this.dealAdapter);
    this.listView.setOnItemClickListener(this);
    this.listView.setOnRefreshListener(this.refreshListener);
    this.dealAdapter.reset();
    removeAllCells();
    addCell("30DealList", this.listView);
    addCell("30DealList1", this.dealListEmptyView);
  }

  protected class DealAdapter extends BasicLoadAdapter
  {
    protected int recommendTitlePosition = -1;
    private int shopAggCount = 2;
    private boolean shouldShowImage = DPActivity.preferences().getBoolean("isShowListImage", true);

    public DealAdapter(Context arg2)
    {
      super();
    }

    private boolean isImageOn()
    {
      return (this.shouldShowImage) || (NetworkUtils.isWIFIConnection(DPApplication.instance()));
    }

    protected void addControl(View paramView, int paramInt)
    {
      if ((paramView instanceof AggViewItem))
      {
        paramView = (AggViewItem)paramView;
        paramView.setOnAggItemClickListener(DealListAgent.this.onDealItemClickListener);
        paramView.setOnMainItemClickListener(DealListAgent.this.onMainClickListener);
        paramView.setListPosition(paramInt);
      }
      do
      {
        return;
        if (!(paramView instanceof MarketAggViewItem))
          continue;
        paramView = (MarketAggViewItem)paramView;
        paramView.setOnAggItemClickListener(DealListAgent.this.onMarketAggDealItemClickListener);
        paramView.setListPosition(paramInt);
        return;
      }
      while (((paramView instanceof BannerViewItem)) || ((paramView instanceof TitleTipViewItem)) || ((paramView instanceof RightTagViewItem)) || ((paramView instanceof WarningTitleTipViewItem)));
      paramView.setBackgroundDrawable(DealListAgent.this.getContext().getResources().getDrawable(R.drawable.deal_list_item_bg));
    }

    protected void addInfo(View paramView, DPObject paramDPObject, int paramInt)
    {
      if (!DPObjectUtils.isDPObjectof(paramDPObject, "ViewItem"));
      String str;
      do
      {
        do
          return;
        while (paramView == null);
        str = paramDPObject.getString("QueryId");
        if ((paramView instanceof AggViewItem))
        {
          setAggViewItemGaUserInfo(paramDPObject.getObject("Agg"), (AggViewItem)paramView, paramInt, str);
          return;
        }
        if ((paramView instanceof DealListItem))
        {
          setTuanDealGAUserInfo(paramDPObject.getObject("Deal"), (DealListItem)paramView, paramInt, str);
          return;
        }
        if (!(paramView instanceof HuiViewItem))
          continue;
        setHuiDealGAUserInfo(paramDPObject.getObject("Hui"), (HuiViewItem)paramView, paramInt, str);
        return;
      }
      while (!(paramView instanceof ShopListItem));
      setExtendedShopGAUserInfo(paramDPObject.getObject("Shop"), (ShopListItem)paramView, paramInt, str);
    }

    public void appendViewItem(DPObject paramDPObject)
    {
      if (DealListAgent.this.listView == null)
        return;
      DealListAgent.this.listView.onRefreshComplete();
      if (DPObjectUtils.isDPObjectof(paramDPObject))
      {
        DPObject[] arrayOfDPObject = paramDPObject.getArray("ViewItems");
        if (!DealListAgent.this.isListEmpty(arrayOfDPObject))
        {
          if (DealListAgent.this.isFirstPage)
          {
            this.mData.clear();
            AggViewItem.records.clear();
          }
          this.mData.addAll(Arrays.asList(arrayOfDPObject));
        }
        this.mIsEnd = paramDPObject.getBoolean("IsEnd");
        this.mRecordCount = paramDPObject.getInt("RecordCount");
        this.mNextStartIndex = paramDPObject.getInt("NextStartIndex");
        this.mEmptyMsg = paramDPObject.getString("EmptyMsg");
        this.mQueryId = paramDPObject.getString("QueryID");
        this.shopAggCount = paramDPObject.getInt("ShopAggLimit");
      }
      this.mErrorMsg = paramDPObject.getString("ErrorMsg");
      notifyDataSetChanged();
    }

    public MApiRequest createRequest(int paramInt)
    {
      StringBuilder localStringBuilder = DealListAgent.this.createDealRequestUrl(paramInt);
      return DealListAgent.this.mapiGet(DealListAgent.this.dealAdapter, localStringBuilder.toString(), CacheType.DISABLED);
    }

    public int getItemViewType(int paramInt)
    {
      Object localObject1 = getItem(paramInt);
      if (localObject1 == ERROR)
        return 0;
      if (localObject1 == LOADING)
        return 1;
      if (localObject1 == EMPTY)
        return 2;
      Object localObject2;
      if (DPObjectUtils.isDPObjectof(localObject1, "ViewItem"))
        localObject2 = ViewItemType.parseFromValue(((DPObject)localObject1).getInt("Type"));
      switch (DealListAgent.5.$SwitchMap$com$dianping$base$widget$ViewItemType[localObject2.ordinal()])
      {
      default:
        return -1;
      case 1:
        return 3;
      case 2:
        return 4;
      case 3:
        return 5;
      case 4:
        localObject1 = ((DPObject)localObject1).getObject("Agg");
        if (DPObjectUtils.isDPObjectof(localObject1, "ViewAggItem"))
        {
          localObject2 = ((DPObject)localObject1).getArray("AggItems");
          int i = ((DPObject)localObject1).getInt("ShowCount");
          paramInt = 0;
          if (localObject2 != null)
            paramInt = localObject2.length;
          if (paramInt > i)
            paramInt = i;
          while (true)
            return paramInt + 13;
        }
        return -1;
      case 5:
        return 7;
      case 6:
        return 8;
      case 7:
        return 9;
      case 8:
        return 10;
      case 9:
        return 11;
      case 10:
        return 12;
      case 11:
      }
      return 13;
    }

    public int getRowIndex(int paramInt)
    {
      int i = paramInt;
      if (isRecommendDeal(paramInt))
        i = paramInt - (this.recommendTitlePosition + 1);
      return i;
    }

    public int getViewTypeCount()
    {
      return 100;
    }

    public boolean isEnabled(int paramInt)
    {
      return DPObjectUtils.isDPObjectof(getItem(paramInt), "ViewItem");
    }

    public boolean isRecommendDeal(int paramInt)
    {
      if (this.recommendTitlePosition < 0);
      do
        return false;
      while (paramInt <= this.recommendTitlePosition);
      return true;
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = null;
      paramViewGroup = localObject;
      if (ViewItemFactory.match(paramDPObject, paramView))
      {
        paramViewGroup = localObject;
        if ((paramView instanceof ViewItemInterface))
        {
          ((ViewItemInterface)paramView).updateData(paramDPObject, DealListAgent.this.latitude, DealListAgent.this.longitude, isImageOn());
          paramViewGroup = paramView;
        }
      }
      paramView = paramViewGroup;
      if (paramViewGroup == null)
        paramView = ViewItemFactory.getView(DealListAgent.this.getContext(), paramDPObject, isImageOn(), DealListAgent.this.latitude, DealListAgent.this.longitude, ViewItemLocation.TUAN_DEAL_LIST);
      if (paramView != null)
      {
        addControl(paramView, paramInt);
        addInfo(paramView, paramDPObject, paramInt);
        return paramView;
      }
      return new View(DealListAgent.this.getContext());
    }

    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestFailed(paramMApiRequest, paramMApiResponse);
      DealListAgent.this.listView.onRefreshComplete();
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      DealListAgent.this.listView.onRefreshComplete();
      this.mReq = null;
      if (paramMApiResponse == null)
        return;
      onRequestComplete(true, paramMApiRequest, paramMApiResponse);
      paramMApiRequest = paramMApiResponse.result();
      DealListAgent.this.analyseResponse((DPObject)paramMApiRequest);
    }

    public void reset()
    {
      this.recommendTitlePosition = -1;
      super.reset();
    }

    protected void setAggViewItemGaUserInfo(DPObject paramDPObject, AggViewItem paramAggViewItem, int paramInt, String paramString)
    {
      if (!DPObjectUtils.isDPObjectof(paramDPObject, "ViewAggItem"));
      do
        return;
      while (paramAggViewItem == null);
      paramAggViewItem.gaUserInfo.query_id = paramString;
      paramAggViewItem.gaUserInfo.index = Integer.valueOf(paramInt);
      paramAggViewItem.setGAString("juhe");
      paramAggViewItem.setExpandGaAction(DealListAgent.this.aggViewItemExpandGaAction());
    }

    protected void setExtendedShopGAUserInfo(DPObject paramDPObject, ShopListItem paramShopListItem, int paramInt, String paramString)
    {
      paramDPObject = new ShopDataModel(paramDPObject);
      paramShopListItem.gaUserInfo.shop_id = Integer.valueOf(paramDPObject.shopId);
      paramShopListItem.gaUserInfo.index = Integer.valueOf(paramInt);
      paramShopListItem.gaUserInfo.query_id = paramString;
      paramShopListItem.gaUserInfo.title = paramDPObject.fullName;
      paramShopListItem.setGAString("piece");
      GAHelper.instance().statisticsEvent(paramShopListItem, "view");
    }

    protected void setHuiDealGAUserInfo(DPObject paramDPObject, HuiViewItem paramHuiViewItem, int paramInt, String paramString)
    {
      if (!DPObjectUtils.isDPObjectof(paramDPObject, "HuiDetail"));
      do
        return;
      while (paramHuiViewItem == null);
      paramHuiViewItem.gaUserInfo.query_id = paramString;
      paramHuiViewItem.gaUserInfo.index = Integer.valueOf(paramInt);
      paramHuiViewItem.gaUserInfo.shop_id = Integer.valueOf(paramDPObject.getInt("PayShopId"));
      paramHuiViewItem.gaUserInfo.butag = Integer.valueOf(paramDPObject.getInt("HuiId"));
      paramHuiViewItem.setGAString("shanhui_item");
      GAHelper.instance().statisticsEvent(paramHuiViewItem, "view");
    }

    protected void setTuanDealGAUserInfo(DPObject paramDPObject, DealListItem paramDealListItem, int paramInt, String paramString)
    {
      if (!DPObjectUtils.isDPObjectof(paramDPObject, "Deal"));
      do
        return;
      while (paramDealListItem == null);
      paramDealListItem.gaUserInfo.dealgroup_id = Integer.valueOf(paramDPObject.getInt("ID"));
      paramDealListItem.gaUserInfo.deal_id = Integer.valueOf(paramDPObject.getInt("DealID"));
      paramDealListItem.gaUserInfo.category_id = Integer.valueOf(paramDPObject.getInt("CategoryID"));
      paramDealListItem.gaUserInfo.query_id = paramString;
      paramDealListItem.gaUserInfo.keyword = DealListAgent.this.getKeyword();
      paramDealListItem.gaUserInfo.index = Integer.valueOf(paramInt);
      paramDealListItem.setGAString("nojuhe_item");
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.agent.DealListAgent
 * JD-Core Version:    0.6.0
 */