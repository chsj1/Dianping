package com.dianping.main.find;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.accountservice.AccountListener;
import com.dianping.accountservice.AccountService;
import com.dianping.app.CityConfig;
import com.dianping.app.CityConfig.SwitchListener;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AdapterCellAgent;
import com.dianping.base.app.loader.AdapterCellAgent.BasicCellAgentAdapter;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.CircleImageView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.main.guide.MainActivity;
import com.dianping.model.City;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.view.NovaRelativeLayout;

public class FindFriendsGoWhereAgent extends AdapterCellAgent
  implements CityConfig.SwitchListener, RequestHandler<MApiRequest, MApiResponse>, AccountListener
{
  private static final String FIND_FRIENDS_AGENT_APPVERSION = "FindFriendsAgentAppVersion";
  private static final String FRIENDS_GO_WHERE_TAG = "05FriendsGoWhere.";
  private static final int MAX_RETRY_COUNT = 3;
  public static final String SEND_REQUEST_ACTION = "com.dianping.action.FindFriendsGoWhereAgent_SEND_REQUEST";
  public static int SEND_REQUEST_FLAG = 0;
  private boolean VIEW_SET = false;
  private Adapter adapter;
  private View cell = null;
  private int mAppVersion;
  private CellDataViewHolder mCellDataViewHolder = new CellDataViewHolder();
  private DPObject mDpObj = null;
  DPObject mError;
  private PackageInfo mPackageInfo;
  private MApiRequest mRequest = null;
  private int mRetryCount = 0;
  private BroadcastReceiver sendRequestReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (FindFriendsGoWhereAgent.this.mRequest != null)
      {
        FindFriendsGoWhereAgent.this.getFragment().mapiService().abort(FindFriendsGoWhereAgent.this.mRequest, FindFriendsGoWhereAgent.this, true);
        FindFriendsGoWhereAgent.access$002(FindFriendsGoWhereAgent.this, null);
      }
      FindFriendsGoWhereAgent.this.sendRequest();
    }
  };
  SharedPreferences sharedPreferences = DPActivity.preferences(getContext());

  public FindFriendsGoWhereAgent(Object paramObject)
  {
    super(paramObject);
  }

  private View createContentCell(DPObject paramDPObject)
  {
    if (getContext() == null)
      return null;
    View localView = this.res.inflate(getContext(), R.layout.find_friendgowhere_container, getParentView(), false);
    initViews(localView);
    this.mCellDataViewHolder.content.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        FindFriendsGoWhereAgent.SEND_REQUEST_FLAG = 1;
        if (FindFriendsGoWhereAgent.this.sharedPreferences.getInt("FindFriendsAgentAppVersion", 0) != FindFriendsGoWhereAgent.this.mAppVersion)
          FindFriendsGoWhereAgent.this.sharedPreferences.edit().putInt("FindFriendsAgentAppVersion", FindFriendsGoWhereAgent.this.mAppVersion).apply();
        FindFriendsGoWhereAgent.this.setAllViewGone();
        paramView = null;
        if (FindFriendsGoWhereAgent.this.mDpObj != null)
          paramView = FindFriendsGoWhereAgent.this.mDpObj.getString("Url");
        if ((paramView != null) && (!paramView.equals("")))
        {
          Intent localIntent = new Intent("android.intent.action.VIEW");
          localIntent.setData(Uri.parse(paramView));
          FindFriendsGoWhereAgent.this.startActivity(localIntent);
        }
        FindFriendsGoWhereAgent.this.sharedPreferences.edit().putInt(MainActivity.LOCAL_REDALERT_NUM, 0).commit();
        ((MainActivity)(MainActivity)FindFriendsGoWhereAgent.this.getFragment().getActivity()).updateMessageCountInTab(0, "发现");
      }
    });
    setCellData(paramDPObject);
    return localView;
  }

  void initViews(View paramView)
  {
    this.mCellDataViewHolder.content = paramView.findViewById(R.id.content);
    if (this.mCellDataViewHolder.content != null)
    {
      ((NovaRelativeLayout)this.mCellDataViewHolder.content).setGAString("explore_feed");
      this.mCellDataViewHolder.title = ((TextView)this.mCellDataViewHolder.content.findViewById(R.id.title));
      this.mCellDataViewHolder.unRead = ((TextView)this.mCellDataViewHolder.content.findViewById(R.id.unread));
      this.mCellDataViewHolder.newImage = ((ImageView)this.mCellDataViewHolder.content.findViewById(R.id.ic_new));
      int i = 0;
      while (i < this.mCellDataViewHolder.picViews.length)
      {
        this.mCellDataViewHolder.picViews[i] = ((CircleImageView)this.mCellDataViewHolder.content.findViewById(this.mCellDataViewHolder.iconViewResId[i]));
        i += 1;
      }
      this.VIEW_SET = true;
    }
  }

  public void onAccountChanged(AccountService paramAccountService)
  {
    sendRequest();
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.VIEW_SET)
    {
      setCellData(this.mDpObj);
      return;
    }
    this.adapter.notifyDataSetChanged();
  }

  public void onCitySwitched(City paramCity1, City paramCity2)
  {
    SEND_REQUEST_FLAG = 1;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    DPApplication.instance().cityConfig().addListener(this);
    DPApplication.instance().accountService().addListener(this);
    try
    {
      this.mPackageInfo = getFragment().getActivity().getPackageManager().getPackageInfo(getFragment().getActivity().getPackageName(), 0);
      this.mAppVersion = this.mPackageInfo.versionCode;
      this.adapter = new Adapter(null);
      addCell("05FriendsGoWhere.", this.adapter);
      SEND_REQUEST_FLAG = 1;
      paramBundle = new IntentFilter();
      paramBundle.addAction("com.dianping.action.FindFriendsGoWhereAgent_SEND_REQUEST");
      getFragment().registerReceiver(this.sendRequestReceiver, paramBundle);
      getFragment().cityConfig().addListener(this);
      return;
    }
    catch (PackageManager.NameNotFoundException paramBundle)
    {
      while (true)
        paramBundle.printStackTrace();
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    DPApplication.instance().cityConfig().removeListener(this);
    DPApplication.instance().accountService().removeListener(this);
    if (this.mRequest != null)
    {
      getFragment().mapiService().abort(this.mRequest, this, true);
      this.mRequest = null;
    }
    getFragment().unregisterReceiver(this.sendRequestReceiver);
    getFragment().cityConfig().removeListener(this);
  }

  public void onProfileChanged(AccountService paramAccountService)
  {
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mRequest == paramMApiRequest)
    {
      this.mRetryCount += 1;
      if (!(paramMApiResponse.error() instanceof DPObject))
        break label67;
    }
    label67: for (this.mError = ((DPObject)paramMApiResponse.error()); ; this.mError = new DPObject())
    {
      this.mRequest = null;
      if (this.mRetryCount < 3)
        break;
      this.mRetryCount = 0;
      dispatchAgentChanged(false);
      return;
    }
    sendRequest();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest != null) && (this.mRequest == paramMApiRequest))
    {
      this.mRequest = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        this.mDpObj = ((DPObject)paramMApiResponse.result());
        dispatchAgentChanged(false);
      }
    }
  }

  public void onResume()
  {
    super.onResume();
    if (SEND_REQUEST_FLAG == 1)
    {
      if (this.mRequest != null)
      {
        getFragment().mapiService().abort(this.mRequest, this, true);
        this.mRequest = null;
      }
      SEND_REQUEST_FLAG = 0;
      sendRequest();
    }
  }

  void sendRequest()
  {
    if (this.mRequest != null)
      return;
    this.mDpObj = null;
    this.mError = null;
    this.mRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/discovery/getdiscovertopmodule.bin").buildUpon().appendQueryParameter("cityid", cityId() + "").toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mRequest, this);
  }

  void setAllViewGone()
  {
    if (this.VIEW_SET == true)
    {
      int i = 0;
      while (i < this.mCellDataViewHolder.picViews.length)
      {
        this.mCellDataViewHolder.picViews[i].setVisibility(8);
        i += 1;
      }
      this.mCellDataViewHolder.unRead.setVisibility(8);
      this.mCellDataViewHolder.newImage.setVisibility(8);
    }
  }

  public void setCellData(DPObject paramDPObject)
  {
    setAllViewGone();
    if ((this.mCellDataViewHolder != null) && (this.mCellDataViewHolder.content != null) && (paramDPObject != null))
    {
      this.mCellDataViewHolder.title.setText(paramDPObject.getString("Title"));
      String[] arrayOfString = paramDPObject.getStringArray("UserPicList");
      String str;
      int i;
      if (paramDPObject.getInt("UnReadCount") > 9)
      {
        str = "9+";
        this.mCellDataViewHolder.unRead.setBackgroundResource(R.drawable.red_dot_unread_rectangle);
        i = this.sharedPreferences.getInt("local_redalert_num", 0);
        if (paramDPObject.getInt("UnReadCount") == 0)
          break label298;
        if (this.sharedPreferences.getInt("FindFriendsAgentAppVersion", 0) != this.mAppVersion)
          this.sharedPreferences.edit().putInt("FindFriendsAgentAppVersion", this.mAppVersion).apply();
        this.mCellDataViewHolder.unRead.setVisibility(0);
        this.mCellDataViewHolder.unRead.setText(str);
        ((NovaRelativeLayout)this.mCellDataViewHolder.content).getGAUserInfo().title = str;
      }
      for (((NovaRelativeLayout)this.mCellDataViewHolder.content).getGAUserInfo().biz_id = Integer.toString(i); ; ((NovaRelativeLayout)this.mCellDataViewHolder.content).getGAUserInfo().biz_id = "无红点")
      {
        if (arrayOfString == null)
          break label339;
        int j = arrayOfString.length;
        if (j == 0)
          return;
        i = 0;
        while (i < j)
        {
          this.mCellDataViewHolder.picViews[i].setVisibility(0);
          this.mCellDataViewHolder.picViews[i].setImage(arrayOfString[i]);
          i += 1;
        }
        str = Integer.toString(paramDPObject.getInt("UnReadCount"));
        this.mCellDataViewHolder.unRead.setBackgroundResource(R.drawable.red_dot_unread);
        break;
        label298: ((NovaRelativeLayout)this.mCellDataViewHolder.content).getGAUserInfo().title = "无红点";
      }
      label339: ((NovaRelativeLayout)this.mCellDataViewHolder.content).getGAUserInfo().title = "无红点";
      ((NovaRelativeLayout)this.mCellDataViewHolder.content).getGAUserInfo().biz_id = "无红点";
      if (this.sharedPreferences.getInt("FindFriendsAgentAppVersion", 0) != this.mAppVersion)
        this.mCellDataViewHolder.newImage.setVisibility(0);
    }
  }

  private class Adapter extends AdapterCellAgent.BasicCellAgentAdapter
  {
    private Adapter()
    {
      super();
    }

    public int getCount()
    {
      if (((FindFriendsGoWhereAgent.this.mRequest != null) && (FindFriendsGoWhereAgent.this.mDpObj == null) && (FindFriendsGoWhereAgent.this.mError == null)) || ((FindFriendsGoWhereAgent.this.mDpObj == null) && (FindFriendsGoWhereAgent.this.mError != null)) || ((FindFriendsGoWhereAgent.this.mDpObj != null) && (FindFriendsGoWhereAgent.this.mDpObj.getBoolean("Show"))))
        return 1;
      return 0;
    }

    public Object getItem(int paramInt)
    {
      return FindFriendsGoWhereAgent.this.mDpObj;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((FindFriendsGoWhereAgent.this.mRequest != null) && (FindFriendsGoWhereAgent.this.mDpObj == null) && (FindFriendsGoWhereAgent.this.mError == null))
      {
        FindFriendsGoWhereAgent.access$402(FindFriendsGoWhereAgent.this, getLoadingView(paramViewGroup, paramView, ViewUtils.dip2px(FindFriendsGoWhereAgent.this.getContext(), 91.0F)));
        FindFriendsGoWhereAgent.access$502(FindFriendsGoWhereAgent.this, false);
      }
      while (true)
      {
        paramView = new AbsListView.LayoutParams(-1, -2);
        FindFriendsGoWhereAgent.this.cell.setLayoutParams(paramView);
        return FindFriendsGoWhereAgent.this.cell;
        if ((FindFriendsGoWhereAgent.this.mDpObj == null) && (FindFriendsGoWhereAgent.this.mError != null))
        {
          FindFriendsGoWhereAgent.access$402(FindFriendsGoWhereAgent.this, getFailedView("网络连接失败 点击重新加载", new LoadingErrorView.LoadRetry()
          {
            public void loadRetry(View paramView)
            {
              FindFriendsGoWhereAgent.this.sendRequest();
              FindFriendsGoWhereAgent.this.dispatchAgentChanged(false);
            }
          }
          , paramViewGroup, paramView, ViewUtils.dip2px(FindFriendsGoWhereAgent.this.getContext(), 91.0F)));
          FindFriendsGoWhereAgent.access$502(FindFriendsGoWhereAgent.this, false);
          continue;
        }
        if ((FindFriendsGoWhereAgent.this.mDpObj == null) || (!FindFriendsGoWhereAgent.this.mDpObj.getBoolean("Show")))
          continue;
        FindFriendsGoWhereAgent.access$402(FindFriendsGoWhereAgent.this, FindFriendsGoWhereAgent.this.createContentCell(FindFriendsGoWhereAgent.this.mDpObj));
      }
    }
  }

  class CellDataViewHolder
  {
    View content;
    final int[] iconViewResId = { R.id.user_pic_one, R.id.user_pic_two, R.id.user_pic_three };
    ImageView newImage;
    CircleImageView[] picViews = new CircleImageView[3];
    TextView title;
    TextView unRead;

    CellDataViewHolder()
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.FindFriendsGoWhereAgent
 * JD-Core Version:    0.6.0
 */