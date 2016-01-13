package com.dianping.membercard.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.membercard.view.PrepaidCardLayout;
import com.dianping.model.City;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;

public class PrepaidCardFragment extends CardFragment
  implements PrepaidCardDetailRequestTask.PrepaidCardDetailRequestHandler, CardFragment.refreshMemberCardHandler, RequestHandler<MApiRequest, MApiResponse>
{
  private static int REFRESH_MSG = 0;
  private static final String UPDATE_USER_NAME = "com.dianping.action.UPDATE_USER_INFO";
  private PrepaidCardDetailRequestTask cardDetailRequestTask = null;
  View guideView;
  double lat;
  private PullToRefreshListView listView;
  double lng;
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("com.dianping.action.UPDATE_USER_INFO"))
      {
        paramContext = paramIntent.getStringExtra("username");
        Log.v("UpdateName", "MemberCardFragment:" + paramContext);
        if ((PrepaidCardFragment.this.cardObject != null) && (PrepaidCardFragment.this.view != null))
        {
          PrepaidCardFragment.this.cardObject = PrepaidCardFragment.this.cardObject.edit().putString("UserName", paramContext).generate();
          PrepaidCardFragment.this.view.updateUserNameOnly(PrepaidCardFragment.this.cardObject);
        }
      }
    }
  };
  MApiRequest nearestShopRequest;
  Adapter prepaidCardAdapter;
  Handler refreshHandler;
  PrepaidCardLayout view;
  WindowManager wm;

  protected void gotoQrCodeActivity()
  {
    statisticsEvent("paidcardinfo5", "paidcardinfo5_qrcode", "", 0);
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://mymcqrcode?qrcode=" + this.cardObject.getString("QRCode"))));
  }

  public void onCardDetailRequestFailed(MApiResponse paramMApiResponse, int paramInt1, int paramInt2)
  {
    this.listView.onRefreshComplete();
  }

  public void onCardDetailRequestFinish(DPObject paramDPObject, PrepaidCardDetailRequestTask.ResponseDataType paramResponseDataType)
  {
    this.prepaidCardAdapter.clearShop();
    if (paramResponseDataType == PrepaidCardDetailRequestTask.ResponseDataType.CURRENT_CARD_INFO)
    {
      refreshMemberCard(paramDPObject);
      this.listView.onRefreshComplete();
      return;
    }
    this.listView.onRefreshComplete();
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.container)
    {
      paramView = accountService();
      if (paramView.profile() == null)
      {
        paramView.login(this);
        this.needLogin = true;
      }
    }
    else
    {
      return;
    }
    gotoQrCodeActivity();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.refreshHandler = new Handler()
    {
      public void handleMessage(Message paramMessage)
      {
        super.handleMessage(paramMessage);
        if (paramMessage.what == PrepaidCardFragment.REFRESH_MSG)
        {
          paramMessage = (DPObject)paramMessage.getData().get("card");
          PrepaidCardFragment.this.refresh(paramMessage);
        }
      }
    };
    paramBundle = new IntentFilter("com.dianping.action.UPDATE_USER_INFO");
    registerReceiver(this.mReceiver, paramBundle);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.listView = ((PullToRefreshListView)paramLayoutInflater.inflate(R.layout.mc_member_card_layout, paramViewGroup, false));
    this.listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    this.listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener()
    {
      public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
      {
        if ((PrepaidCardFragment.this.cardDetailRequestTask != null) && (PrepaidCardFragment.this.getActivity() != null))
        {
          int i = PrepaidCardFragment.this.cardObject.getInt("PrepaidCardID");
          int j = PrepaidCardFragment.this.cardObject.getInt("AccountID");
          PrepaidCardFragment.this.cardDetailRequestTask.doRequest(i, j);
          PrepaidCardFragment.this.prepaidCardAdapter.clearShop();
        }
        PrepaidCardFragment.this.statisticsEvent("mycard5", "mycard5_detail_refresh", null, 0);
      }
    });
    this.prepaidCardAdapter = new Adapter(null);
    this.listView.setAdapter(this.prepaidCardAdapter);
    if (this.cardDetailRequestTask != null)
      this.cardDetailRequestTask.load(this);
    while (true)
    {
      return this.listView;
      this.cardDetailRequestTask = new PrepaidCardDetailRequestTask(this);
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    unregisterReceiver(this.mReceiver);
  }

  public void onPause()
  {
    super.onPause();
    if ((this.wm != null) && (this.guideView != null))
    {
      this.wm.removeView(this.guideView);
      this.guideView = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.prepaidCardAdapter != null)
    {
      this.prepaidCardAdapter.setShopList((DPObject)paramMApiResponse.result(), this.lat, this.lng);
      this.prepaidCardAdapter.notifyDataSetChanged();
    }
  }

  public void refresh(DPObject paramDPObject)
  {
    this.cardObject = paramDPObject;
    this.prepaidCardAdapter.notifyDataSetChanged();
  }

  public void refreshMemberCard(DPObject paramDPObject)
  {
    Message localMessage = Message.obtain();
    localMessage.what = REFRESH_MSG;
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("card", paramDPObject);
    localMessage.setData(localBundle);
    this.refreshHandler.sendMessage(localMessage);
  }

  public void showGuideView()
  {
    this.wm = ((WindowManager)getActivity().getApplicationContext().getSystemService("window"));
    WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
    localLayoutParams.height = -1;
    localLayoutParams.width = -1;
    localLayoutParams.format = 1;
    localLayoutParams.flags = 1280;
    localLayoutParams.type = 2003;
    this.guideView = LayoutInflater.from(getActivity()).inflate(R.layout.prepaid_card_guide, null);
    Object localObject1 = new int[2];
    Object localObject2 = getView().findViewById(R.id.qrcode);
    ((View)localObject2).getLocationOnScreen(localObject1);
    int i = localObject1[1];
    int j = ((View)localObject2).getHeight();
    localObject1 = this.guideView.findViewById(R.id.image);
    localObject2 = (LinearLayout.LayoutParams)((View)localObject1).getLayoutParams();
    ((LinearLayout.LayoutParams)localObject2).setMargins(0, i - j, 0, 0);
    ((View)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
    this.guideView.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        if (PrepaidCardFragment.this.guideView == null)
          return false;
        PrepaidCardFragment.this.wm.removeView(PrepaidCardFragment.this.guideView);
        PrepaidCardFragment.this.guideView = null;
        return false;
      }
    });
    this.wm.addView(this.guideView, localLayoutParams);
  }

  public void updateNearestShop()
  {
    Object localObject2 = "http://app.t.dianping.com/prepaidcardshoplistgn.bin?prepaidcardid=" + this.cardObject.getInt("PrepaidCardID") + "&start=0" + "&cityid=" + city().id();
    Object localObject3 = accountService();
    Object localObject1 = localObject2;
    if (((AccountService)localObject3).token() != null)
      localObject1 = (String)localObject2 + "&token=" + ((AccountService)localObject3).token();
    localObject3 = locationService().location();
    localObject2 = localObject1;
    if (localObject3 != null)
    {
      this.lat = ((DPObject)localObject3).getDouble("Lat");
      this.lng = ((DPObject)localObject3).getDouble("Lng");
      localObject2 = (String)localObject1 + "&lat=" + this.lat + "&lng=" + this.lng;
    }
    this.nearestShopRequest = BasicMApiRequest.mapiGet((String)localObject2, CacheType.DISABLED);
    mapiService().exec(this.nearestShopRequest, this);
  }

  private class Adapter extends BasicAdapter
  {
    double lat;
    double lng;
    DPObject nearestShop = null;

    private Adapter()
    {
    }

    public void clearShop()
    {
      this.nearestShop = null;
    }

    public int getCount()
    {
      return 1;
    }

    public Object getItem(int paramInt)
    {
      return null;
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      PrepaidCardFragment.this.view = ((PrepaidCardLayout)LayoutInflater.from(PrepaidCardFragment.this.getActivity()).inflate(R.layout.prepaidcard, paramViewGroup, false));
      PrepaidCardFragment.this.view.setData(PrepaidCardFragment.this.cardObject);
      PrepaidCardFragment.this.view.findViewById(R.id.container).setOnClickListener(PrepaidCardFragment.this);
      if (this.nearestShop == null)
        PrepaidCardFragment.this.updateNearestShop();
      while (true)
      {
        paramView = PrepaidCardFragment.this.getActivity().getSharedPreferences("prepaidcard", 0);
        if (!paramView.getBoolean("hasShownGuide", false))
        {
          paramView.edit().putBoolean("hasShownGuide", true).commit();
          PrepaidCardFragment.this.view.post(new Runnable()
          {
            public void run()
            {
              PrepaidCardFragment.this.showGuideView();
            }
          });
        }
        return PrepaidCardFragment.this.view;
        PrepaidCardFragment.this.view.setNearestShop(PrepaidCardFragment.this.cardObject.getInt("PrepaidCardID"), this.nearestShop, this.lat, this.lng);
      }
    }

    public void setShopList(DPObject paramDPObject, double paramDouble1, double paramDouble2)
    {
      this.nearestShop = paramDPObject;
      this.lat = paramDouble1;
      this.lng = paramDouble2;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.fragment.PrepaidCardFragment
 * JD-Core Version:    0.6.0
 */