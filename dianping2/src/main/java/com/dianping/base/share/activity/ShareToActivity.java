package com.dianping.base.share.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListAdapter;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.share.action.base.BaseShare;
import com.dianping.base.share.action.base.CopyShare;
import com.dianping.base.share.action.base.MailShare;
import com.dianping.base.share.action.base.QQShare;
import com.dianping.base.share.action.base.QzoneShare;
import com.dianping.base.share.action.base.SmsShare;
import com.dianping.base.share.action.base.WXQShare;
import com.dianping.base.share.action.base.WXShare;
import com.dianping.base.share.action.base.WeiboShare;
import com.dianping.base.share.adapter.ShareToAdapter;
import com.dianping.base.share.enums.ShareType;
import com.dianping.base.share.model.ShareHolder;
import com.dianping.base.thirdparty.sinaapi.SinaHelper;
import com.dianping.util.Log;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.array;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Response;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class ShareToActivity extends DPActivity
  implements AdapterView.OnItemClickListener, IWeiboHandler.Response
{
  private String mGaAction;
  private String mGaCategory;
  private GridView mGvShare;
  private View mLayShare;
  private View mLayShareBg;
  private final List<BaseShare> mShareList = new ArrayList();
  private Parcelable mShareObj;
  private ShareType mShareType;

  private void initData()
  {
    Intent localIntent = getIntent();
    this.mShareType = ((ShareType)localIntent.getSerializableExtra("shareType"));
    this.mShareObj = localIntent.getParcelableExtra("shareObj");
    this.mGaCategory = localIntent.getStringExtra("gaCategory");
    this.mGaAction = localIntent.getStringExtra("gaAction");
  }

  private List<BaseShare> initDefaultShareList()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new WXShare());
    localArrayList.add(new WXQShare());
    localArrayList.add(new QQShare());
    localArrayList.add(new SmsShare());
    localArrayList.add(new WeiboShare());
    localArrayList.add(new QzoneShare());
    localArrayList.add(new MailShare());
    localArrayList.add(new CopyShare());
    return localArrayList;
  }

  private void initShareList()
  {
    int i = getIntent().getIntExtra("shareItemId", R.array.default_share_item);
    int j = getIntent().getIntExtra("feed", 0);
    ArrayList localArrayList2 = new ArrayList();
    try
    {
      String[] arrayOfString = getResources().getStringArray(i);
      i = 0;
      while (true)
      {
        ArrayList localArrayList1 = localArrayList2;
        if (i >= arrayOfString.length)
          break;
        localArrayList2.add((BaseShare)Class.forName(arrayOfString[i]).newInstance());
        i += 1;
      }
    }
    catch (Exception localList)
    {
      Log.e(localException.toString());
      List localList = initDefaultShareList();
      this.mShareList.clear();
      if (j > 0)
      {
        i = 0;
        while (i < localList.size())
        {
          if ((1 << i & j) != 0)
            this.mShareList.add(localList.get(i));
          i += 1;
        }
      }
      this.mShareList.addAll(localList);
    }
  }

  private void initView()
  {
    this.mLayShareBg = findViewById(R.id.lay_share_bg);
    this.mLayShareBg.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ShareToActivity.this.finish();
        ShareToActivity.this.overridePendingTransition(0, 0);
      }
    });
    this.mLayShare = findViewById(R.id.lay_share);
    Object localObject = AnimationUtils.loadAnimation(this, R.anim.popup_up_in);
    this.mLayShare.startAnimation((Animation)localObject);
    this.mGvShare = ((GridView)findViewById(R.id.gv_share));
    this.mGvShare.setOnItemClickListener(this);
    initShareList();
    localObject = new ShareToAdapter(this, this.mShareList);
    this.mGvShare.setAdapter((ListAdapter)localObject);
  }

  public void finish()
  {
    super.finish();
    com.dianping.base.share.util.ShareUtil.sShareForeground = false;
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (paramInt1 == 100)
      if (paramIntent == null)
      {
        paramIntent = new Intent();
        paramIntent.putExtra("shareResult", "cancel");
        paramIntent.putExtra("shareChannel", "新浪微博");
        setResult(-1, paramIntent);
        setResult(paramInt2);
      }
    while (true)
    {
      finish();
      return;
      Intent localIntent = new Intent();
      localIntent.putExtra("shareResult", paramIntent.getStringExtra("shareResult"));
      localIntent.putExtra("shareChannel", paramIntent.getStringExtra("shareChannel"));
      setResult(-1, localIntent);
      break;
      setResult(0);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.activity_share_to);
    if ((getIntent() != null) && (SinaHelper.getWeiboAPI(this).handleWeiboResponse(getIntent(), this)))
      return;
    initView();
    initData();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = (BaseShare)this.mShareList.get(paramInt);
    paramView = new ArrayList();
    boolean bool;
    switch (2.$SwitchMap$com$dianping$base$share$enums$ShareType[this.mShareType.ordinal()])
    {
    default:
      bool = paramAdapterView.doShare(this, (ShareHolder)this.mShareObj);
      if ((!TextUtils.isEmpty(this.mGaCategory)) && (!TextUtils.isEmpty(this.mGaAction)))
        statisticsEvent(this.mGaCategory, this.mGaAction, paramAdapterView.getLabel(), 0, paramView);
      if (bool)
        break;
      paramView = new Intent();
      paramView.putExtra("shareResult", "cancel");
      paramView.putExtra("shareChannel", paramAdapterView.getLabel());
      setResult(-1, paramView);
      finish();
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
    }
    do
    {
      return;
      bool = paramAdapterView.shareShop(this, (DPObject)this.mShareObj);
      break;
      bool = paramAdapterView.shareWeb(this, (ShareHolder)this.mShareObj);
      paramView.add(new BasicNameValuePair("title", ((ShareHolder)this.mShareObj).title));
      break;
      bool = paramAdapterView.shareDeal(this, (DPObject)this.mShareObj);
      break;
      bool = paramAdapterView.shareApp(this, (ShareHolder)this.mShareObj);
      break;
      bool = paramAdapterView.sharePay(this, (ShareHolder)this.mShareObj);
      break;
      bool = paramAdapterView.shareLuckyMoney(this, (DPObject)this.mShareObj);
      break;
      bool = paramAdapterView.shareHotelProd(this, (DPObject)this.mShareObj);
      break;
    }
    while (((paramAdapterView instanceof WeiboShare)) || ((paramAdapterView instanceof QQShare)) || ((paramAdapterView instanceof WXShare)));
    paramView = new Intent();
    paramView.putExtra("shareResult", "success");
    paramView.putExtra("shareChannel", paramAdapterView.getLabel());
    setResult(-1, paramView);
    finish();
  }

  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    SinaHelper.getWeiboAPI(this).handleWeiboResponse(paramIntent, this);
  }

  protected void onPause()
  {
    super.onPause();
    if (isFinishing())
      com.dianping.base.share.util.ShareUtil.sShareForeground = false;
  }

  public void onResponse(BaseResponse paramBaseResponse)
  {
    Intent localIntent = new Intent();
    localIntent.putExtra("shareChannel", "新浪微博");
    switch (paramBaseResponse.errCode)
    {
    default:
    case 0:
    case 1:
    case 2:
    }
    while (true)
    {
      setResult(-1, localIntent);
      finish();
      return;
      localIntent.putExtra("shareResult", "success");
      continue;
      localIntent.putExtra("shareResult", "cancel");
      continue;
      localIntent.putExtra("shareResult", "fail");
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.activity.ShareToActivity
 * JD-Core Version:    0.6.0
 */