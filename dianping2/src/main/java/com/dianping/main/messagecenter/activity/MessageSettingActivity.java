package com.dianping.main.messagecenter.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TableView;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.Log;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.NetworkImageView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageSettingActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, CompoundButton.OnCheckedChangeListener
{
  private static final String K_SETTING_TITLE = "Title";
  private static final String K_SWITCH_EXPAND = "Expand";
  protected static final String K_SWITCH_ID = "ID";
  protected static final String K_SWITCH_IMAGE = "Image";
  private static final String K_SWITCH_LIST = "SwitchList";
  protected static final String K_SWITCH_NAME = "Name";
  protected static final String K_SWITCH_OFF_DESC = "OffDesc";
  protected static final String K_SWITCH_ON_DESC = "OnDesc";
  protected static final String K_SWITCH_STATUS = "Status";
  protected static final String K_TOKEN = "token";
  private static final String K_TYPE = "Type";
  public static final String TAG = MessageSettingActivity.class.getSimpleName();
  private static final int TYPE_SWITCH = 1;
  private static final int TYPE_TITLE = 0;
  protected String mConfig;
  private View mEmptyView;
  private View mErrorView;
  private View mLoadingView;
  private LinearLayout mPushDescLay;
  protected MApiRequest mQuerySettingReq;
  private TableView mSettingListLay;
  protected ArrayList<DPObject> mSettingObjList = new ArrayList();
  protected MApiRequest mUpdateSettingReq;

  private String data2Config()
  {
    JSONArray localJSONArray = new JSONArray();
    Iterator localIterator = this.mSettingObjList.iterator();
    while (localIterator.hasNext())
    {
      DPObject localDPObject = (DPObject)localIterator.next();
      if (localDPObject.getInt("Type") == 0)
        continue;
      JSONObject localJSONObject = new JSONObject();
      try
      {
        localJSONObject.put("ID".toLowerCase(Locale.getDefault()), localDPObject.getInt("ID"));
        localJSONObject.put("Status".toLowerCase(Locale.getDefault()), localDPObject.getBoolean("Status"));
        localJSONArray.put(localJSONObject);
      }
      catch (JSONException localJSONException)
      {
        Log.e(TAG, "json error", localJSONException);
      }
    }
    return localJSONArray.toString();
  }

  private void initSettingData(List<DPObject> paramList)
  {
    this.mSettingObjList.clear();
    paramList = paramList.iterator();
    while (paramList.hasNext())
    {
      Object localObject1 = (DPObject)paramList.next();
      Object localObject2 = ((DPObject)localObject1).getString("Title");
      localObject2 = new DPObject().edit().putInt("Type", 0).putString("Title", (String)localObject2).generate();
      this.mSettingObjList.add(localObject2);
      localObject1 = Arrays.asList(((DPObject)localObject1).getArray("SwitchList")).iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (DPObject)((Iterator)localObject1).next();
        int i = ((DPObject)localObject2).getInt("ID");
        String str1 = ((DPObject)localObject2).getString("Name");
        String str2 = ((DPObject)localObject2).getString("Image");
        String str3 = ((DPObject)localObject2).getString("OnDesc");
        String str4 = ((DPObject)localObject2).getString("OffDesc");
        boolean bool = ((DPObject)localObject2).getBoolean("Status");
        localObject2 = new DPObject().edit().putInt("Type", 1).putInt("ID", i).putString("Name", str1).putString("Image", str2).putString("OnDesc", str3).putString("OffDesc", str4).putBoolean("Status", bool).putBoolean("Expand", false).generate();
        this.mSettingObjList.add(localObject2);
      }
    }
  }

  private void initTitleBar()
  {
    super.getTitleBar().setLeftView(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        MessageSettingActivity.this.onBackPressed();
      }
    });
  }

  private void postNotificationFormList()
  {
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
    this.mUpdateSettingReq = BasicMApiRequest.mapiPost("http://m.api.dianping.com/updatenotificationform.bin", new String[] { "config", str, "token", accountService().token() });
    mapiService().exec(this.mUpdateSettingReq, this);
  }

  private void setupView()
  {
    super.setContentView(R.layout.activity_push_cancel);
    this.mSettingListLay = ((TableView)findViewById(R.id.lv_push_cancel));
    this.mPushDescLay = ((LinearLayout)findViewById(R.id.push_desc));
    ((TextView)findViewById(R.id.tv_desc)).setText(Html.fromHtml("如果您不想收到推送（如下图）<br/>请在\"<font color='#000000'>我的-设置-消息提醒设置</font>\"中关闭"));
    this.mEmptyView = findViewById(R.id.empty_view);
    this.mErrorView = findViewById(R.id.error_view);
    if ((this.mErrorView instanceof LoadingErrorView))
      ((LoadingErrorView)this.mErrorView).setCallBack(new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          MessageSettingActivity.this.requestNotificationFormList();
        }
      });
    this.mLoadingView = findViewById(R.id.loading_view);
    initTitleBar();
  }

  private void updateSettingData(DPObject paramDPObject)
  {
    Iterator localIterator = this.mSettingObjList.iterator();
    while (localIterator.hasNext())
    {
      DPObject localDPObject = (DPObject)localIterator.next();
      if ((localDPObject.getInt("Type") == 0) || (localDPObject.getInt("ID") != paramDPObject.getInt("ID")))
        continue;
      int i = this.mSettingObjList.indexOf(localDPObject);
      this.mSettingObjList.set(i, paramDPObject);
    }
  }

  private void updateSettingsList()
  {
    if (this.mSettingObjList.size() == 0);
    while (true)
    {
      return;
      Iterator localIterator = this.mSettingObjList.iterator();
      while (localIterator.hasNext())
      {
        DPObject localDPObject = (DPObject)localIterator.next();
        if (localDPObject.getInt("Type") == 0)
        {
          localLinearLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.setting_title, null, false);
          ((TextView)localLinearLayout.findViewById(R.id.tv_setting_title)).setText(localDPObject.getString("Title"));
          this.mSettingListLay.addView(localLinearLayout);
          continue;
        }
        LinearLayout localLinearLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.push_cancel_item, null, false);
        ((NetworkImageView)localLinearLayout.findViewById(R.id.icon)).setImage(localDPObject.getString("Image"));
        ((TextView)localLinearLayout.findViewById(R.id.tv_name)).setText(localDPObject.getString("Name"));
        CompoundButton localCompoundButton = (CompoundButton)localLinearLayout.findViewById(R.id.cb_switch);
        localCompoundButton.setChecked(localDPObject.getBoolean("Status"));
        localCompoundButton.setOnCheckedChangeListener(this);
        localCompoundButton.setTag(localDPObject);
        this.mSettingListLay.addView(localLinearLayout);
      }
    }
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onBackPressed()
  {
    super.onBackPressed();
    if (this.mConfig == null)
    {
      finish();
      return;
    }
    postNotificationFormList();
  }

  public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
  {
    DPObject localDPObject = (DPObject)paramCompoundButton.getTag();
    String str = localDPObject.getString("Name");
    if (paramBoolean);
    for (paramCompoundButton = "setting5_unsubscribe_on"; ; paramCompoundButton = "setting5_unsubscribe_off")
    {
      statisticsEvent("setting5", paramCompoundButton, str, 0);
      updateSettingData(localDPObject.edit().putBoolean("Status", paramBoolean).generate());
      return;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setupView();
    if (getIntent().getIntExtra("setting", 0) == 0)
    {
      super.setTitle("消息退订");
      this.mPushDescLay.setVisibility(0);
    }
    while (true)
    {
      requestNotificationFormList();
      return;
      super.setTitle("消息设置");
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.mQuerySettingReq != null)
    {
      mapiService().abort(this.mQuerySettingReq, this, true);
      this.mQuerySettingReq = null;
    }
    if (this.mUpdateSettingReq != null)
    {
      mapiService().abort(this.mUpdateSettingReq, this, true);
      this.mUpdateSettingReq = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest.equals(this.mQuerySettingReq))
    {
      this.mQuerySettingReq = null;
      paramMApiRequest = paramMApiResponse.message().content();
      this.mEmptyView.setVisibility(8);
      this.mErrorView.setVisibility(0);
      ((TextView)this.mErrorView.findViewById(16908308)).setText(paramMApiRequest);
      this.mLoadingView.setVisibility(8);
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
      if ((paramMApiRequest instanceof DPObject[]))
      {
        initSettingData(Arrays.asList((DPObject[])(DPObject[])paramMApiRequest));
        this.mConfig = data2Config();
        updateSettingsList();
        this.mErrorView.setVisibility(8);
        this.mLoadingView.setVisibility(8);
      }
    }
    do
    {
      return;
      this.mEmptyView.setVisibility(0);
      break;
    }
    while (!paramMApiRequest.equals(this.mUpdateSettingReq));
    this.mUpdateSettingReq = null;
    showToast("更新设置成功");
    finish();
  }

  void requestNotificationFormList()
  {
    if (this.mQuerySettingReq != null)
    {
      mapiService().abort(this.mQuerySettingReq, this, true);
      this.mQuerySettingReq = null;
    }
    String str = accountService().token();
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/notificationform.bin").buildUpon();
    if (!TextUtils.isEmpty(str))
      localBuilder.appendQueryParameter("token", str);
    this.mQuerySettingReq = BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    mapiService().exec(this.mQuerySettingReq, this);
    this.mEmptyView.setVisibility(8);
    this.mErrorView.setVisibility(8);
    this.mLoadingView.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.messagecenter.activity.MessageSettingActivity
 * JD-Core Version:    0.6.0
 */