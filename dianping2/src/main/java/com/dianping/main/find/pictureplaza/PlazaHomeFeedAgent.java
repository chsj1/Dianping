package com.dianping.main.find.pictureplaza;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.model.Location;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlazaHomeFeedAgent extends PlazaAdapterCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String CELL_PLAZA_HOTUGC = "0300plaza.01HotUgc";
  private final int IS_LIKE_ACTION_TYPE_NO = 0;
  private final int IS_LIKE_ACTION_TYPE_YES = 1;
  private MApiRequest deleteRequest;
  private FeedListAdapter feedAdapter;
  private MApiRequest feedRequest;
  protected List<DPObject> feedSource = new ArrayList();
  private boolean isLocal = true;
  private String mEmptyMsg = null;
  final BroadcastReceiver mFeedReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.action.PlazaFeedLikeStateChange".equals(paramIntent.getAction()))
      {
        int i = paramIntent.getIntExtra("position", 0);
        paramContext = paramIntent.getStringExtra("feedid");
        boolean bool = paramIntent.getBooleanExtra("islike", false);
        PlazaHomeFeedAgent.this.feedAdapter.updateLikeStatus(i, paramContext, bool);
      }
    }
  };
  private boolean mIsEnd = false;
  private int mNextStartIndex = 0;
  private MApiRequest mReqLike;

  public PlazaHomeFeedAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void deleteReq(String paramString, int paramInt)
  {
    if (this.deleteRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/review/deleteugcfeed.bin").buildUpon();
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("mainid");
    localArrayList.add(paramString);
    localArrayList.add("feedtype");
    localArrayList.add(String.valueOf(String.valueOf(paramInt)));
    this.deleteRequest = BasicMApiRequest.mapiPost(localBuilder.build().toString(), (String[])localArrayList.toArray(new String[localArrayList.size()]));
    mapiService().exec(this.deleteRequest, this);
    showProgressDialog("正在删除...");
  }

  private void initAdapter()
  {
    this.feedAdapter = new FeedListAdapter();
    this.feedAdapter.setSource(this.feedSource);
    addCell("0300plaza.01HotUgc", this.feedAdapter);
  }

  private void loadNewPage(boolean paramBoolean)
  {
    if (this.mIsEnd)
      return;
    this.mEmptyMsg = null;
    reqFeedInfo(paramBoolean, this.mNextStartIndex);
  }

  private void reqFeedInfo(boolean paramBoolean, int paramInt)
  {
    if (this.feedRequest != null)
      return;
    Object localObject = "";
    String str = "";
    Location localLocation = location();
    if (localLocation != null)
    {
      localObject = localLocation.latitude() + "";
      str = localLocation.longitude() + "";
    }
    localObject = Uri.parse("http://m.api.dianping.com/plaza/getplazafeedlist.bin").buildUpon().appendQueryParameter("cityid", cityId() + "").appendQueryParameter("start", paramInt + "").appendQueryParameter("lng", str).appendQueryParameter("lat", (String)localObject);
    if (paramBoolean)
      ((Uri.Builder)localObject).appendQueryParameter("samecity", "true");
    while (true)
    {
      this.feedRequest = BasicMApiRequest.mapiGet(((Uri.Builder)localObject).build().toString(), CacheType.DISABLED);
      mapiService().exec(this.feedRequest, this);
      return;
      ((Uri.Builder)localObject).appendQueryParameter("samecity", "false");
    }
  }

  private void sendLikeReq(int paramInt1, String paramString, boolean paramBoolean, int paramInt2)
  {
    if (this.mReqLike != null)
    {
      getFragment().mapiService().abort(this.mReqLike, this, true);
      return;
    }
    int i = 1;
    if (paramBoolean)
      i = 0;
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/review/ugcfavor.bin").buildUpon();
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("mainid");
    localArrayList.add(paramString);
    localArrayList.add("actiontype");
    localArrayList.add(String.valueOf(i));
    localArrayList.add("feedtype");
    localArrayList.add(String.valueOf(paramInt2));
    localArrayList.add("originuserid");
    localArrayList.add(String.valueOf(paramInt1));
    this.mReqLike = BasicMApiRequest.mapiPost(localBuilder.build().toString(), (String[])localArrayList.toArray(new String[localArrayList.size()]));
    getFragment().mapiService().exec(this.mReqLike, this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    initAdapter();
    paramBundle = new IntentFilter();
    paramBundle.addAction("com.dianping.action.PlazaFeedLikeStateChange");
    LocalBroadcastManager.getInstance(getContext()).registerReceiver(this.mFeedReceiver, paramBundle);
  }

  public void onDestroy()
  {
    super.onDestroy();
    try
    {
      LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(this.mFeedReceiver);
      return;
    }
    catch (Exception localException)
    {
    }
  }

  protected void onRefresh()
  {
    this.feedAdapter = null;
    this.mNextStartIndex = 0;
    this.mIsEnd = false;
    this.mEmptyMsg = null;
    this.isLocal = true;
    this.feedSource.clear();
    initAdapter();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mReqLike == paramMApiRequest)
    {
      this.mReqLike = null;
      return;
    }
    if (this.deleteRequest == paramMApiRequest)
    {
      this.deleteRequest = null;
      dismissDialog();
      return;
    }
    this.feedRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mReqLike == paramMApiRequest)
      this.mReqLike = null;
    do
    {
      return;
      if (this.feedRequest != paramMApiRequest)
        continue;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        this.mIsEnd = paramMApiRequest.getBoolean("IsEnd");
        this.mNextStartIndex = paramMApiRequest.getInt("NextStartIndex");
        this.mEmptyMsg = paramMApiRequest.getString("EmptyMsg");
        paramMApiRequest = paramMApiRequest.getArray("List");
        if ((paramMApiRequest == null) || (paramMApiRequest.length == 0))
        {
          this.mEmptyMsg = "这里没有更多图趣了，去别的地方看看吧";
          this.mIsEnd = true;
        }
      }
      while (true)
      {
        this.feedRequest = null;
        this.feedAdapter.notifyDataSetChanged();
        return;
        paramMApiResponse = new ArrayList();
        int i = 0;
        while (i < paramMApiRequest.length)
        {
          paramMApiResponse.add(paramMApiRequest[i]);
          i += 1;
        }
        this.feedSource.addAll(paramMApiResponse);
        continue;
        this.mEmptyMsg = "这里没有更多图趣了，去别的地方看看吧";
        this.mIsEnd = true;
      }
    }
    while (this.deleteRequest != paramMApiRequest);
    this.deleteRequest = null;
    this.feedAdapter.updateDeleteStatus();
    dismissDialog();
  }

  public class FeedListAdapter extends BasicAdapter
  {
    private String curFeedId = null;
    private int curPosition = 0;
    private HashMap<Integer, Integer> expandMaps = new HashMap();
    private OnFeedItemListener feedItemListener = new OnFeedItemListener()
    {
      public void onItemDeleteListener(int paramInt1, String paramString, int paramInt2)
      {
        if (PlazaHomeFeedAgent.this.accountService().token() == null)
        {
          PlazaHomeFeedAgent.this.accountService().login(new LoginResultListener(paramString, paramInt2, paramInt1)
          {
            public void onLoginCancel(AccountService paramAccountService)
            {
            }

            public void onLoginSuccess(AccountService paramAccountService)
            {
              PlazaHomeFeedAgent.this.deleteReq(this.val$id, this.val$feedType);
              PlazaHomeFeedAgent.FeedListAdapter.access$302(PlazaHomeFeedAgent.FeedListAdapter.this, this.val$id);
              PlazaHomeFeedAgent.FeedListAdapter.access$402(PlazaHomeFeedAgent.FeedListAdapter.this, this.val$position);
            }
          });
          return;
        }
        PlazaHomeFeedAgent.this.deleteReq(paramString, paramInt2);
        PlazaHomeFeedAgent.FeedListAdapter.access$302(PlazaHomeFeedAgent.FeedListAdapter.this, paramString);
        PlazaHomeFeedAgent.FeedListAdapter.access$402(PlazaHomeFeedAgent.FeedListAdapter.this, paramInt1);
      }

      public void onLikeClickListener(int paramInt1, int paramInt2, String paramString, boolean paramBoolean, int paramInt3)
      {
        if (PlazaHomeFeedAgent.this.accountService().token() == null)
        {
          PlazaHomeFeedAgent.this.accountService().login(new LoginResultListener(paramInt2, paramString, paramBoolean, paramInt1, paramInt3)
          {
            public void onLoginCancel(AccountService paramAccountService)
            {
            }

            public void onLoginSuccess(AccountService paramAccountService)
            {
              PlazaHomeFeedAgent.FeedListAdapter.this.updateLikeStatus(this.val$position, this.val$id, this.val$isLike);
              PlazaHomeFeedAgent.this.sendLikeReq(this.val$userId, this.val$id, this.val$isLike, this.val$feedType);
            }
          });
          return;
        }
        PlazaHomeFeedAgent.FeedListAdapter.this.updateLikeStatus(paramInt2, paramString, paramBoolean);
        PlazaHomeFeedAgent.this.sendLikeReq(paramInt1, paramString, paramBoolean, paramInt3);
      }
    };
    private HashMap<Integer, Integer> imageIndexMaps = new HashMap();
    private List<DPObject> source;

    public FeedListAdapter()
    {
    }

    private View createFeedItem(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      PlazaFeedItem localPlazaFeedItem = null;
      if (paramDPObject == null)
        return null;
      if ((paramView instanceof PlazaFeedItem))
        localPlazaFeedItem = (PlazaFeedItem)paramView;
      paramView = localPlazaFeedItem;
      if (localPlazaFeedItem == null)
        paramView = (PlazaFeedItem)PlazaHomeFeedAgent.this.res.inflate(PlazaHomeFeedAgent.this.getContext(), R.layout.plaza_home_feed_item, paramViewGroup, false);
      paramView.setFeedUgc(paramDPObject, paramInt, this.expandMaps, this.imageIndexMaps);
      paramView.setFeedItemListener(this.feedItemListener);
      return paramView;
    }

    public int getCount()
    {
      if (PlazaHomeFeedAgent.this.mIsEnd)
      {
        int i;
        if (this.source.size() == 0)
          i = 1;
        int j;
        do
        {
          return i;
          j = this.source.size();
          i = j;
        }
        while (!PlazaHomeFeedAgent.this.isLocal);
        return j + 1;
      }
      return this.source.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      if ((this.source != null) && (PlazaHomeFeedAgent.this.mIsEnd) && (this.source.size() == 0) && (PlazaHomeFeedAgent.this.isLocal))
        return LOADING;
      if (paramInt < this.source.size())
        return this.source.get(paramInt);
      if ((PlazaHomeFeedAgent.this.mIsEnd) && (!PlazaHomeFeedAgent.this.isLocal))
        return EMPTY;
      if (PlazaHomeFeedAgent.this.mEmptyMsg == null)
        return LOADING;
      return ERROR;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public int getItemViewType(int paramInt)
    {
      Object localObject = getItem(paramInt);
      if (localObject == ERROR)
        return 0;
      if (localObject == LOADING)
        return 1;
      if (localObject == EMPTY)
        return 2;
      return 3;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
        return createFeedItem((DPObject)localObject, paramInt, paramView, paramViewGroup);
      if (localObject == LOADING)
      {
        if ((PlazaHomeFeedAgent.this.mIsEnd) && (PlazaHomeFeedAgent.this.isLocal))
        {
          PlazaHomeFeedAgent.access$502(PlazaHomeFeedAgent.this, false);
          PlazaHomeFeedAgent.access$602(PlazaHomeFeedAgent.this, false);
          PlazaHomeFeedAgent.access$702(PlazaHomeFeedAgent.this, null);
          PlazaHomeFeedAgent.access$802(PlazaHomeFeedAgent.this, 0);
        }
        if (PlazaHomeFeedAgent.this.mEmptyMsg == null)
          PlazaHomeFeedAgent.this.loadNewPage(PlazaHomeFeedAgent.this.isLocal);
        return getLoadingView(paramViewGroup, paramView);
      }
      if (localObject == EMPTY)
        return getEmptyView("", "都看完啦！", paramViewGroup, paramView);
      return getFailedView(PlazaHomeFeedAgent.this.mEmptyMsg, new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          PlazaHomeFeedAgent.this.loadNewPage(PlazaHomeFeedAgent.this.isLocal);
        }
      }
      , paramViewGroup, paramView);
    }

    public int getViewTypeCount()
    {
      return 10;
    }

    public void setSource(List<DPObject> paramList)
    {
      this.source = paramList;
    }

    public void updateDeleteStatus()
    {
      if (this.curFeedId == null);
      DPObject localDPObject;
      do
      {
        do
          return;
        while (!(getItem(this.curPosition) instanceof DPObject));
        localDPObject = (DPObject)getItem(this.curPosition);
      }
      while (!(localDPObject.getInt("FeedId") + "").equals(this.curFeedId));
      this.source.remove(localDPObject);
      notifyDataSetChanged();
      if ((this.expandMaps != null) && (this.expandMaps.size() > 0) && (this.expandMaps.containsKey(Integer.valueOf(this.curPosition))))
        this.expandMaps.remove(Integer.valueOf(this.curPosition));
      if ((this.imageIndexMaps != null) && (this.imageIndexMaps.size() > 0) && (this.imageIndexMaps.containsKey(Integer.valueOf(this.curPosition))))
        this.imageIndexMaps.remove(Integer.valueOf(this.curPosition));
      this.curFeedId = null;
      this.curPosition = 0;
    }

    public void updateLikeStatus(int paramInt, String paramString, boolean paramBoolean)
    {
      Object localObject;
      if ((getItem(paramInt) instanceof DPObject))
      {
        localObject = (DPObject)getItem(paramInt);
        if ((((DPObject)localObject).getInt("FeedId") + "").equals(paramString));
      }
      else
      {
        return;
      }
      int i = ((DPObject)localObject).getInt("LikeCount");
      if (paramBoolean)
      {
        i -= 1;
        paramString = this.source;
        localObject = ((DPObject)localObject).edit();
        if (paramBoolean)
          break label137;
      }
      label137: for (paramBoolean = true; ; paramBoolean = false)
      {
        paramString.set(paramInt, ((DPObject.Editor)localObject).putBoolean("IsLike", paramBoolean).putInt("LikeCount", i).generate());
        notifyDataSetChanged();
        return;
        i += 1;
        break;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaHomeFeedAgent
 * JD-Core Version:    0.6.0
 */