package com.dianping.main.messagecenter.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.widget.DoubleLineCheckView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PushSettingActivity extends BaseSettingActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String SWITCH_DESC = "Desc";
  private static final String SWITCH_ID = "Id";
  private static final String SWITCH_OPEN = "Open";
  private static final String SWITCH_TITLE = "Title";
  public static final String TAG = PushSettingActivity.class.getSimpleName();

  private void bindData()
  {
    this.mLvSetting.setAdapter(this.mSettingAdapter);
    setEmptyView();
  }

  private String data2Config()
  {
    JSONArray localJSONArray = new JSONArray();
    Iterator localIterator = this.mSettingObjList.iterator();
    while (localIterator.hasNext())
    {
      DPObject localDPObject = (DPObject)localIterator.next();
      JSONObject localJSONObject = new JSONObject();
      try
      {
        localJSONObject.put("ID".toLowerCase(), localDPObject.getInt("Id"));
        localJSONObject.put("Status".toLowerCase(), localDPObject.getBoolean("Open"));
        localJSONArray.put(localJSONObject);
      }
      catch (JSONException localJSONException)
      {
        Log.e(TAG, "json error", localJSONException);
      }
    }
    return localJSONArray.toString();
  }

  private boolean getCurrentPushStatus()
  {
    boolean bool2 = true;
    boolean bool1 = bool2;
    if (this.mSettingObjList != null)
    {
      bool1 = bool2;
      if (this.mSettingObjList.size() >= 1)
      {
        DPObject localDPObject = (DPObject)this.mSettingObjList.get(0);
        bool1 = bool2;
        if (localDPObject != null)
          bool1 = localDPObject.getBoolean("Open");
      }
    }
    return bool1;
  }

  private void initVariable()
  {
    this.mSettingAdapter = new SettingAdapter(this);
    this.mSettingObjList = new ArrayList();
  }

  private void initView()
  {
    this.mLaySetting = ((LinearLayout)findViewById(R.id.lay_push_setting));
    this.mLvSetting = ((ListView)findViewById(R.id.lv_push_setting));
  }

  private boolean isPushEnabled()
  {
    return getSharedPreferences("push", 0).getBoolean("enable", true);
  }

  private void setFailedView(String paramString)
  {
    removeOldErrorView();
    this.mErrorView = getFailedView(paramString, new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        PushSettingActivity.this.removeOldErrorView();
        PushSettingActivity.this.setEmptyView();
        PushSettingActivity.this.sendQuerySettingReq();
      }
    });
    this.mLaySetting.addView(this.mErrorView);
    removeOldEmptyView();
    ViewUtils.hideView(this.mLvSetting, true);
  }

  private void setPushStatus(boolean paramBoolean)
  {
    getSharedPreferences("push", 0).edit().putBoolean("enable", paramBoolean).commit();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.activity_push_setting);
    initView();
    initVariable();
    bindData();
    sendQuerySettingReq();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest.equals(this.mQuerySettingReq))
    {
      this.mQuerySettingReq = null;
      setFailedView(paramMApiResponse.message().content());
    }
    do
      return;
    while (!paramMApiRequest.equals(this.mUpdateSettingReq));
    this.mUpdateSettingReq = null;
    showToast("网络错误，更新设置失败");
    finish();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest.equals(this.mQuerySettingReq))
    {
      this.mQuerySettingReq = null;
      paramMApiRequest = paramMApiResponse.result();
      if ((paramMApiRequest instanceof DPObject))
      {
        paramMApiRequest = (DPObject)paramMApiRequest;
        this.mSettingObjList.clear();
        paramMApiRequest = paramMApiRequest.getArray("NotificationSwitches");
        if (paramMApiRequest == null)
          break label107;
        this.mSettingObjList.addAll(Arrays.asList(paramMApiRequest));
        this.mConfig = data2Config();
        this.mSettingAdapter.notifyDataSetChanged();
        ViewUtils.showView(this.mLvSetting);
        bool = getCurrentPushStatus();
        if (bool != isPushEnabled())
          setPushStatus(bool);
      }
    }
    label107: 
    do
    {
      return;
      setFailedView("服务器错误，请稍候再试");
      break;
    }
    while (!paramMApiRequest.equals(this.mUpdateSettingReq));
    this.mUpdateSettingReq = null;
    boolean bool = getCurrentPushStatus();
    if (bool != isPushEnabled())
    {
      setPushStatus(bool);
      if (!bool)
        sendBroadcast(new Intent("com.dianping.action.Intent.ACTION_SHUTDOWN"));
    }
    showToast("更新设置成功");
    finish();
  }

  protected void sendQuerySettingReq()
  {
    if (this.mQuerySettingReq != null)
    {
      mapiService().abort(this.mQuerySettingReq, this, true);
      this.mQuerySettingReq = null;
    }
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/notificationsetting.bin").buildUpon();
    if (!TextUtils.isEmpty(this.mToken))
      localBuilder.appendQueryParameter("token", this.mToken);
    this.mQuerySettingReq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    mapiService().exec(this.mQuerySettingReq, this);
  }

  protected void sendUpdateSettingReq()
  {
    if (this.mConfig == null)
      return;
    String str = data2Config();
    if (this.mConfig.equals(str))
    {
      finish();
      return;
    }
    if (this.mUpdateSettingReq != null)
    {
      mapiService().abort(this.mUpdateSettingReq, this, true);
      this.mUpdateSettingReq = null;
    }
    this.mConfig = str;
    this.mUpdateSettingReq = BasicMApiRequest.mapiPost("http://m.api.dianping.com/updatenotificationsetting.bin", new String[] { "config", str, "token", this.mToken });
    mapiService().exec(this.mUpdateSettingReq, this);
  }

  void updateSettingData(DPObject paramDPObject)
  {
    Iterator localIterator = this.mSettingObjList.iterator();
    while (localIterator.hasNext())
    {
      DPObject localDPObject = (DPObject)localIterator.next();
      if (localDPObject.getInt("Id") != paramDPObject.getInt("Id"))
        continue;
      int i = this.mSettingObjList.indexOf(localDPObject);
      this.mSettingObjList.set(i, paramDPObject);
    }
  }

  class SettingAdapter extends BasicAdapter
  {
    private final Context mContext;

    public SettingAdapter(Context arg2)
    {
      Object localObject;
      this.mContext = localObject;
    }

    public int getCount()
    {
      return PushSettingActivity.this.mSettingObjList.size();
    }

    public Object getItem(int paramInt)
    {
      return PushSettingActivity.this.mSettingObjList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      DPObject localDPObject = (DPObject)getItem(paramInt);
      if (paramView != null)
        paramViewGroup = (LinearLayout)paramView;
      for (paramView = (SwitchHolder)paramViewGroup.getTag(); ; paramView = new SwitchHolder(paramViewGroup))
      {
        paramView.setItem(localDPObject);
        paramViewGroup.setTag(paramView);
        return paramViewGroup;
        paramViewGroup = (LinearLayout)LayoutInflater.from(this.mContext).inflate(R.layout.push_setting_item, paramViewGroup, false);
      }
    }

    class SwitchHolder
    {
      DPObject mItemObj;
      DoubleLineCheckView mLaySwitch;

      public SwitchHolder(ViewGroup arg2)
      {
        ViewGroup localViewGroup;
        setHost(localViewGroup);
      }

      public void setHost(ViewGroup paramViewGroup)
      {
        this.mLaySwitch = ((DoubleLineCheckView)paramViewGroup.findViewById(R.id.lay_switch));
      }

      public void setItem(DPObject paramDPObject)
      {
        this.mItemObj = paramDPObject;
        String str1 = paramDPObject.getString("Title");
        String str2 = paramDPObject.getString("Desc");
        boolean bool = paramDPObject.getBoolean("Open");
        this.mLaySwitch.setLine1Text(str1);
        this.mLaySwitch.setLine2Text(str2);
        this.mLaySwitch.setTwoLineMode();
        this.mLaySwitch.setChecked(bool);
        this.mLaySwitch.setMyOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            boolean bool;
            if (!PushSettingActivity.SettingAdapter.SwitchHolder.this.mItemObj.getBoolean("Open"))
            {
              bool = true;
              if (!bool)
                break label121;
            }
            label121: for (paramView = "setting5_notify_on"; ; paramView = "setting5_notify_off")
            {
              String str = PushSettingActivity.SettingAdapter.SwitchHolder.this.mItemObj.getString("Title");
              PushSettingActivity.this.statisticsEvent("setting5", paramView, str, 0);
              PushSettingActivity.SettingAdapter.SwitchHolder.this.mItemObj = PushSettingActivity.SettingAdapter.SwitchHolder.this.mItemObj.edit().putBoolean("Open", bool).generate();
              PushSettingActivity.this.updateSettingData(PushSettingActivity.SettingAdapter.SwitchHolder.this.mItemObj);
              PushSettingActivity.SettingAdapter.this.notifyDataSetChanged();
              return;
              bool = false;
              break;
            }
          }
        });
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.messagecenter.activity.PushSettingActivity
 * JD-Core Version:    0.6.0
 */