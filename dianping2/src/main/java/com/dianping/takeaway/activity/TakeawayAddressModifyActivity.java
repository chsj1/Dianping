package com.dianping.takeaway.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.takeaway.view.TAToastView;
import com.dianping.util.KeyboardUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class TakeawayAddressModifyActivity extends NovaActivity
{
  public final int REQUEST_CODE_POI_SEARCH = 1;
  public EditText addressView;
  public Button confirmBtn;
  public Context context = this;
  public TAToastView loadingView;
  public MApiRequest modifyAddressRequest;
  public DPObject originAddress;
  public EditText phoneView;
  public DPObject poiInfo;
  public EditText poiView;
  public RequestHandler<MApiRequest, MApiResponse> requestHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      TakeawayAddressModifyActivity.this.loadingView.hideToast();
      if ((paramMApiRequest == TakeawayAddressModifyActivity.this.modifyAddressRequest) && (paramMApiResponse != null))
      {
        paramMApiRequest = paramMApiResponse.message();
        if (paramMApiRequest != null)
        {
          TakeawayAddressModifyActivity.this.showShortToast(paramMApiRequest.content());
          if ((paramMApiRequest.flag() != 0) && (TakeawayAddressModifyActivity.this.originAddress != null))
          {
            paramMApiRequest = new Intent("com.dianping.takeaway.UPDATE_ADDRESS_LIST");
            TakeawayAddressModifyActivity.this.sendBroadcast(paramMApiRequest);
          }
        }
        TakeawayAddressModifyActivity.this.modifyAddressRequest = null;
      }
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      TakeawayAddressModifyActivity.this.loadingView.hideToast();
      if (paramMApiRequest == TakeawayAddressModifyActivity.this.modifyAddressRequest)
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          if (paramMApiRequest != null)
          {
            paramMApiRequest = paramMApiRequest.getArray("MyUserAddress");
            if ((paramMApiRequest == null) || (paramMApiRequest.length == 0))
              break label110;
            paramMApiResponse = new Intent();
            paramMApiResponse.putExtra("address", paramMApiRequest[0]);
            TakeawayAddressModifyActivity.this.setResult(-1, paramMApiResponse);
          }
        }
      while (true)
      {
        TakeawayAddressModifyActivity.this.finish();
        TakeawayAddressModifyActivity.this.modifyAddressRequest = null;
        return;
        label110: TakeawayAddressModifyActivity.this.showToast("数据错误，请重试");
      }
    }
  };
  private String shopId;

  private void initView()
  {
    if (this.originAddress == null);
    for (String str = "新增送餐地址"; ; str = "修改送餐地址")
    {
      super.setTitle(str);
      super.getTitleBar().setLeftView(R.drawable.ic_back_u, new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          TakeawayAddressModifyActivity.this.back();
        }
      });
      this.loadingView = ((TAToastView)findViewById(R.id.loading_view));
      this.poiView = ((EditText)findViewById(R.id.poi_content));
      this.poiView.setOnTouchListener(new View.OnTouchListener()
      {
        public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
        {
          TakeawayAddressModifyActivity.this.addressView.clearFocus();
          TakeawayAddressModifyActivity.this.phoneView.clearFocus();
          return false;
        }
      });
      this.poiView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          TakeawayAddressModifyActivity.this.hideKeyboard();
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawaypoisearch"));
          String str = TakeawayAddressModifyActivity.this.poiView.getText().toString();
          if (!TextUtils.isEmpty(str))
            paramView.putExtra("poi", str);
          TakeawayAddressModifyActivity.this.startActivityForResult(paramView, 1);
        }
      });
      this.addressView = ((EditText)findViewById(R.id.address_detail));
      this.addressView.setOnFocusChangeListener(new View.OnFocusChangeListener()
      {
        public void onFocusChange(View paramView, boolean paramBoolean)
        {
          if (!paramBoolean)
            TakeawayAddressModifyActivity.this.addressView.setSelection(0);
        }
      });
      this.phoneView = ((EditText)findViewById(R.id.phone));
      this.confirmBtn = ((Button)findViewById(R.id.confirm_btn));
      this.confirmBtn.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          TakeawayAddressModifyActivity.this.statisticsEvent("takeaway6", "takeaway6_newadd_saveclk", "", 0);
          TakeawayAddressModifyActivity.this.hideKeyboard();
          if (TextUtils.isEmpty(TakeawayAddressModifyActivity.this.poiView.getText().toString().trim()))
          {
            TakeawayAddressModifyActivity.this.showCustomToast("请选择送餐地址");
            return;
          }
          paramView = TakeawayAddressModifyActivity.this.phoneView.getText().toString();
          if ((TextUtils.isEmpty(paramView.trim())) || (paramView.length() != 11))
          {
            TakeawayAddressModifyActivity.this.showCustomToast("请输入11位手机号码");
            TakeawayAddressModifyActivity.this.phoneView.requestFocus();
            return;
          }
          TakeawayAddressModifyActivity.this.modifyAddressTask(paramView);
        }
      });
      if (this.originAddress != null)
      {
        if ((!this.originAddress.getBoolean("NeedUpdate")) && (!TextUtils.isEmpty(this.originAddress.getString("Poi"))))
        {
          this.poiView.setText(this.originAddress.getString("Poi"));
          this.addressView.setText(this.originAddress.getString("Address"));
          this.addressView.setSelection(this.addressView.getText().length());
        }
        this.phoneView.setText(this.originAddress.getString("Phone"));
        this.phoneView.setSelection(this.phoneView.getText().length());
      }
      return;
    }
  }

  public void back()
  {
    hideKeyboard();
    Intent localIntent = new Intent();
    localIntent.putExtra("isfrommodify", true);
    setResult(0, localIntent);
    super.finish();
  }

  public String getPageName()
  {
    return "takeawayaddressmodify";
  }

  protected void hideKeyboard()
  {
    KeyboardUtils.hideKeyboard(this.poiView);
    KeyboardUtils.hideKeyboard(this.addressView);
    KeyboardUtils.hideKeyboard(this.phoneView);
  }

  public void modifyAddressTask(String paramString)
  {
    if (this.modifyAddressRequest != null)
      return;
    Location localLocation = super.location();
    if (localLocation == null)
    {
      showToast("定位失败，请重试");
      return;
    }
    this.loadingView.showToast("正在保存地址...", true);
    ArrayList localArrayList = new ArrayList();
    if (this.originAddress != null)
    {
      localArrayList.add("operation");
      localArrayList.add("2");
      localArrayList.add("useraddresskey");
      localArrayList.add(String.valueOf(this.originAddress.getInt("AddressKey")));
      String str = this.addressView.getText().toString();
      if (!TextUtils.isEmpty(str))
      {
        localArrayList.add("address");
        localArrayList.add(str);
      }
      localArrayList.add("shopid");
      localArrayList.add(this.shopId);
      localArrayList.add("phone");
      localArrayList.add(paramString);
      localArrayList.add("locatecityid");
      localArrayList.add(String.valueOf(localLocation.city().id()));
      localArrayList.add("cityid");
      localArrayList.add(String.valueOf(super.cityId()));
      paramString = null;
      if (this.poiInfo == null)
        break label357;
      paramString = this.poiInfo;
    }
    while (true)
    {
      if (paramString != null)
      {
        localArrayList.add("poi");
        localArrayList.add(paramString.getString("Poi"));
        localArrayList.add("lat");
        localArrayList.add(String.valueOf(paramString.getDouble("Lat")));
        localArrayList.add("lng");
        localArrayList.add(String.valueOf(paramString.getDouble("Lng")));
        localArrayList.add("type");
        localArrayList.add(String.valueOf(paramString.getInt("Type")));
      }
      this.modifyAddressRequest = BasicMApiRequest.mapiPost("http://waimai.api.dianping.com/changeusercontact.ta", (String[])localArrayList.toArray(new String[0]));
      super.mapiService().exec(this.modifyAddressRequest, this.requestHandler);
      return;
      localArrayList.add("operation");
      localArrayList.add("1");
      break;
      label357: if (this.originAddress == null)
        continue;
      paramString = this.originAddress;
    }
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == 1) && (paramInt2 == -1) && (paramIntent != null))
    {
      this.poiInfo = ((DPObject)paramIntent.getParcelableExtra("PoiAddress"));
      if (this.poiInfo != null)
        this.poiView.setText(this.poiInfo.getString("Poi"));
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.takeaway_activity_addressmodify);
    Intent localIntent = super.getIntent();
    if (paramBundle == null)
      this.originAddress = ((DPObject)localIntent.getParcelableExtra("address"));
    for (this.shopId = super.getStringParam("shopid"); ; this.shopId = paramBundle.getString("shopid"))
    {
      initView();
      return;
      this.originAddress = ((DPObject)paramBundle.getParcelable("address"));
    }
  }

  protected void onDestroy()
  {
    if (this.modifyAddressRequest != null)
    {
      super.mapiService().abort(this.modifyAddressRequest, null, true);
      this.modifyAddressRequest = null;
    }
    super.onDestroy();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
    {
      back();
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("address", this.originAddress);
    paramBundle.putString("shopid", this.shopId);
  }

  public void showCustomToast(String paramString)
  {
    Toast localToast = Toast.makeText(this.context, "请输入送餐地址", 0);
    localToast.setGravity(17, 0, 0);
    TAToastView localTAToastView = new TAToastView(this.context);
    localTAToastView.showToast(paramString, false);
    localToast.setView(localTAToastView);
    localToast.show();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.activity.TakeawayAddressModifyActivity
 * JD-Core Version:    0.6.0
 */