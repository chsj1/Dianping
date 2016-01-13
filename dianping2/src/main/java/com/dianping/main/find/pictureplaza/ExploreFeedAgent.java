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
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.loader.AdapterCellAgent;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.loader.MyResources;
import com.dianping.model.Location;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.HashMap;

public class ExploreFeedAgent extends AdapterCellAgent
{
  private static final String CELL_PLAZA_EXPLORE_HOTFEED = "0100plaza.01HotFeed";
  private FeedListAdapter feedAdapter;
  private int feedType;
  final BroadcastReceiver mFeedReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("com.dianping.action.PlazaFeedLikeStateChange".equals(paramIntent.getAction()))
      {
        int i = paramIntent.getIntExtra("position", 0);
        paramContext = paramIntent.getStringExtra("feedid");
        boolean bool = paramIntent.getBooleanExtra("islike", false);
        ExploreFeedAgent.this.feedAdapter.updateLikeStatus(i, paramContext, bool);
      }
    }
  };

  public ExploreFeedAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void initAdapter()
  {
    this.feedAdapter = new FeedListAdapter(getContext());
    addCell("0100plaza.01HotFeed", this.feedAdapter);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.feedType = getFragment().getIntParam("feedtype", 0);
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

  public class FeedListAdapter extends FeedBasicAdapter
  {
    public FeedListAdapter(Context arg2)
    {
      super();
    }

    public MApiRequest createRequest(int paramInt)
    {
      String str1 = "";
      String str2 = "";
      Location localLocation = ExploreFeedAgent.this.location();
      if (localLocation != null)
      {
        str1 = localLocation.latitude() + "";
        str2 = localLocation.longitude() + "";
      }
      return BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/plaza/getdiscoveryitemfeedlist.bin").buildUpon().appendQueryParameter("type", ExploreFeedAgent.this.feedType + "").appendQueryParameter("start", paramInt + "").appendQueryParameter("lng", str2).appendQueryParameter("lat", str1).build().toString(), CacheType.DISABLED);
    }

    protected void dismissFeedProgressDialog()
    {
      ExploreFeedAgent.this.getFragment().dismissDialog();
    }

    public int getCount()
    {
      if (this.mIsEnd)
      {
        if (this.mData.size() == 0)
          return 1;
        return this.mData.size() + 1;
      }
      return this.mData.size() + 1;
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((paramView instanceof PlazaFeedItem));
      for (paramView = (PlazaFeedItem)paramView; ; paramView = null)
      {
        Object localObject = paramView;
        if (paramView == null)
          localObject = (PlazaFeedItem)ExploreFeedAgent.this.res.inflate(ExploreFeedAgent.this.getContext(), R.layout.plaza_home_feed_item, paramViewGroup, false);
        ((PlazaFeedItem)localObject).setFeedUgc(paramDPObject, paramInt, this.expandMaps, this.imageIndexMaps);
        ((PlazaFeedItem)localObject).setFeedItemListener(this.feedItemListener);
        return localObject;
      }
    }

    protected void onRequestComplete(boolean paramBoolean, MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestComplete(paramBoolean, paramMApiRequest, paramMApiResponse);
      if (!paramBoolean)
        setErrorMsg("网络连接失败，点击重新加载");
      do
        return;
      while (!isEnd());
      setEmptyMsg("这里没有更多图趣了，去别的地方看看吧");
    }

    protected void showFeedProgressDialog(String paramString)
    {
      ExploreFeedAgent.this.getFragment().showProgressDialog(paramString);
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
      this.mData.remove(localDPObject);
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
        paramString = this.mData;
        localObject = ((DPObject)localObject).edit();
        if (paramBoolean)
          break label135;
      }
      label135: for (paramBoolean = true; ; paramBoolean = false)
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
 * Qualified Name:     com.dianping.main.find.pictureplaza.ExploreFeedAgent
 * JD-Core Version:    0.6.0
 */