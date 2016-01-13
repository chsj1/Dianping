package com.dianping.tuan.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.activity.BaseTuanActivity;
import com.dianping.base.util.uuid.UUIDUtil;
import com.dianping.base.widget.TwoLineRadio;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.util.CollectionUtils;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SendCouponActivity extends BaseTuanActivity
  implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, RequestHandler<MApiRequest, MApiResponse>
{
  protected static final String TAG = SendCouponActivity.class.getSimpleName();
  protected Adapter adapter;
  protected String ids;
  protected double latitude;
  protected double longitude;
  protected DPObject mCoupon;
  protected EditText phoneText;
  protected MApiRequest sendCouponRequest;
  protected CharSequence sendPhone;
  protected ArrayList<String> shopIds = new ArrayList();
  protected ListView shopListView;
  protected MApiRequest shopRequest;
  protected String shopTitle;

  private boolean isSelected(String paramString)
  {
    if (TextUtils.isEmpty(paramString));
    Iterator localIterator;
    do
      while (!localIterator.hasNext())
      {
        return false;
        localIterator = this.shopIds.iterator();
      }
    while (!paramString.equalsIgnoreCase((String)localIterator.next()));
    return true;
  }

  private void sendCoupon()
  {
    Log.i(TAG, "sendCoupon=" + CollectionUtils.list2Str(this.shopIds, ","));
    this.sendPhone = this.phoneText.getText();
    if ((TextUtils.isEmpty(this.sendPhone)) || (this.sendPhone.length() != 11))
    {
      this.phoneText.setError(Html.fromHtml("<font color=#ff0000>请输入正确的手机号码</font>"));
      return;
    }
    boolean bool = ((CompoundButton)findViewById(R.id.shop_info_toggle)).isChecked();
    if ((bool) && (this.shopIds.size() == 0))
    {
      showAlertDialog("请选择分店或商户");
      return;
    }
    if (this.sendCouponRequest != null)
    {
      Log.i(TAG, "sendCouponRequest is running");
      return;
    }
    if (bool);
    for (this.sendCouponRequest = BasicMApiRequest.mapiPost("http://app.t.dianping.com/sendgroupongn.bin", new String[] { "token", accountService().token(), "phone", this.sendPhone.toString(), "callid", UUIDUtil.generateUUID(), "couponid", this.mCoupon.getInt("ID") + "", "shopids", CollectionUtils.list2Str(this.shopIds, ",") }); ; this.sendCouponRequest = BasicMApiRequest.mapiPost("http://app.t.dianping.com/sendgroupongn.bin", new String[] { "token", accountService().token(), "phone", this.sendPhone.toString(), "callid", UUIDUtil.generateUUID(), "couponid", this.mCoupon.getInt("ID") + "" }))
    {
      mapiService().exec(this.sendCouponRequest, this);
      showProgressDialog("正在发送...");
      KeyboardUtils.hideKeyboard(this.phoneText);
      return;
    }
  }

  public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
  {
    if (paramCompoundButton.getId() == R.id.shop_info_toggle)
    {
      paramCompoundButton = findViewById(R.id.shop_info_layer);
      if (!paramBoolean)
        break label60;
    }
    label60: for (int i = 0; ; i = 8)
    {
      paramCompoundButton.setVisibility(i);
      if (this.adapter == null)
      {
        this.adapter = new Adapter();
        this.shopListView.setAdapter(this.adapter);
      }
      return;
    }
  }

  public void onClick(View paramView)
  {
    if ((paramView instanceof TwoLineRadio))
    {
      paramView = (TwoLineRadio)paramView;
      if (paramView.isChecked())
        break label34;
    }
    label34: for (boolean bool = true; ; bool = false)
    {
      paramView.setChecked(bool);
      if (paramView.getTag() != null)
        break;
      return;
    }
    if (paramView.isChecked())
      this.shopIds.add(paramView.getTag().toString());
    while (true)
    {
      Log.i(TAG, CollectionUtils.list2Str(this.shopIds, ","));
      return;
      this.shopIds.remove(paramView.getTag().toString());
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.ids = getIntent().getStringExtra("ids");
    this.shopTitle = getIntent().getStringExtra("shoptitle");
    this.mCoupon = ((DPObject)getIntent().getParcelableExtra("coupon"));
    if (this.mCoupon == null)
    {
      finish();
      return;
    }
    if (location() != null)
    {
      this.latitude = location().latitude();
      this.longitude = location().longitude();
    }
    setContentView(R.layout.send_coupon_layout);
    this.phoneText = ((EditText)findViewById(R.id.phone_number));
    if (TextUtils.isEmpty(this.shopTitle))
    {
      findViewById(R.id.title).setVisibility(8);
      if (!TextUtils.isEmpty(this.ids))
        break label192;
      findViewById(R.id.layer1).setVisibility(8);
    }
    while (true)
    {
      setTitleButton("发送", new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          SendCouponActivity.this.sendCoupon();
        }
      });
      return;
      ((TextView)findViewById(R.id.title)).setText(this.shopTitle);
      break;
      label192: ((CompoundButton)findViewById(R.id.shop_info_toggle)).setOnCheckedChangeListener(this);
      this.shopListView = ((ListView)findViewById(R.id.shop_list));
    }
  }

  protected void onDestroy()
  {
    if (this.shopRequest != null)
    {
      mapiService().abort(this.shopRequest, this, true);
      this.shopRequest = null;
    }
    if (this.sendCouponRequest != null)
    {
      mapiService().abort(this.sendCouponRequest, this, true);
      this.sendCouponRequest = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    paramMApiResponse = paramMApiResponse.message();
    if (paramMApiRequest == this.shopRequest)
    {
      this.shopRequest = null;
      this.adapter.setErrorMsg(paramMApiResponse.content());
      this.adapter.notifyDataSetChanged();
    }
    do
      return;
    while (this.sendCouponRequest != paramMApiRequest);
    this.sendCouponRequest = null;
    Toast.makeText(this, paramMApiResponse.content(), 0).show();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      paramMApiResponse = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest != this.shopRequest)
        break label86;
      this.shopRequest = null;
      this.adapter.setEnd(paramMApiResponse.getBoolean("IsEnd"));
      this.adapter.setErrorMsg(null);
      this.adapter.appendShops(Arrays.asList(paramMApiResponse.getArray("List")));
      this.adapter.notifyDataSetChanged();
    }
    label86: 
    do
      return;
    while (this.sendCouponRequest != paramMApiRequest);
    this.sendCouponRequest = null;
    Toast.makeText(this, paramMApiResponse.getString("Content"), 0).show();
    if (!TextUtils.isEmpty(this.sendPhone))
      preferences().edit().putString("lastPhoneNo", this.sendPhone.toString()).commit();
    finish();
  }

  protected void onResume()
  {
    super.onResume();
    this.sendPhone = preferences().getString("lastPhoneNo", null);
    if (!TextUtils.isEmpty(this.sendPhone))
      this.phoneText.setText(this.sendPhone);
  }

  class Adapter extends BasicAdapter
  {
    private String errorMsg;
    private boolean isEnd;
    private ArrayList<DPObject> shoplist = new ArrayList();

    Adapter()
    {
    }

    private void loadShopList()
    {
      if (SendCouponActivity.this.shopRequest != null)
      {
        Log.i(SendCouponActivity.TAG, "shopRequest is running");
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("http://app.t.dianping.com/");
      localStringBuilder.append("shoplistgn.bin");
      localStringBuilder.append("?ids=").append(SendCouponActivity.this.ids);
      localStringBuilder.append("&start=").append(this.shoplist.size());
      if ((SendCouponActivity.this.latitude != 0.0D) && (SendCouponActivity.this.longitude != 0.0D))
      {
        localStringBuilder.append("&lat=").append(SendCouponActivity.this.latitude);
        localStringBuilder.append("&lng=").append(SendCouponActivity.this.longitude);
      }
      SendCouponActivity.this.shopRequest = BasicMApiRequest.mapiGet(localStringBuilder.toString(), CacheType.DISABLED);
      SendCouponActivity.this.mapiService().exec(SendCouponActivity.this.shopRequest, SendCouponActivity.this);
    }

    public void appendShops(List<DPObject> paramList)
    {
      if ((paramList != null) && (paramList.size() > 0))
        this.shoplist.addAll(paramList);
    }

    public int getCount()
    {
      if (this.isEnd)
        return this.shoplist.size();
      return this.shoplist.size() + 1;
    }

    public String getFullName(DPObject paramDPObject)
    {
      StringBuilder localStringBuilder = new StringBuilder().append(paramDPObject.getString("Name"));
      if ((paramDPObject.getString("BranchName") == null) || (paramDPObject.getString("BranchName").length() == 0));
      for (paramDPObject = ""; ; paramDPObject = "(" + paramDPObject.getString("BranchName") + ")")
        return paramDPObject;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < this.shoplist.size())
        return this.shoplist.get(paramInt);
      if (TextUtils.isEmpty(this.errorMsg))
        return LOADING;
      return ERROR;
    }

    public long getItemId(int paramInt)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
        return ((DPObject)localObject).getInt("ID");
      if (localObject == LOADING)
        return -paramInt;
      return -2147483648L;
    }

    public int getItemViewType(int paramInt)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
        return 0;
      if (localObject == LOADING)
        return 1;
      return 2;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
      {
        localObject = (DPObject)localObject;
        if (paramView != null);
        for (paramView = (TwoLineRadio)paramView; ; paramView = null)
        {
          paramViewGroup = paramView;
          if (paramView == null)
          {
            paramViewGroup = new TwoLineRadio(SendCouponActivity.this);
            paramViewGroup.setPadding(0, 5, 0, 5);
            paramViewGroup.setOnClickListener(SendCouponActivity.this);
            paramViewGroup.setRadioButtonDrawable(R.drawable.checkbox_bg);
          }
          paramViewGroup.setText(getFullName((DPObject)localObject));
          paramViewGroup.setDesc(((DPObject)localObject).getString("Address"));
          paramViewGroup.setTag(Integer.valueOf(((DPObject)localObject).getInt("ID")));
          if (!SendCouponActivity.this.isSelected(String.valueOf(((DPObject)localObject).getInt("ID"))))
            break;
          paramViewGroup.setChecked(true);
          return paramViewGroup;
        }
        paramViewGroup.setChecked(false);
        return paramViewGroup;
      }
      if ((localObject == LOADING) && (TextUtils.isEmpty(this.errorMsg)))
      {
        loadShopList();
        return getLoadingView(paramViewGroup, paramView);
      }
      return (View)getFailedView(this.errorMsg, new SendCouponActivity.Adapter.1(this), paramViewGroup, paramView);
    }

    public int getViewTypeCount()
    {
      return 3;
    }

    public void setEnd(boolean paramBoolean)
    {
      this.isEnd = paramBoolean;
    }

    public void setErrorMsg(String paramString)
    {
      this.errorMsg = paramString;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.tuan.activity.SendCouponActivity
 * JD-Core Version:    0.6.0
 */