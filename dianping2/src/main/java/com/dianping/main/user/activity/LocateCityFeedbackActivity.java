package com.dianping.main.user.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.KeyboardUtils.SoftKeyboardController;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class LocateCityFeedbackActivity extends NovaActivity
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private EditText city;
  private MApiRequest req;

  public void onClick(View paramView)
  {
    paramView = new ArrayList();
    Object localObject = new StringBuilder("城市错误:" + this.city.getText().toString().trim());
    ((StringBuilder)localObject).append('|');
    if (accountService().token() != null)
      ((StringBuilder)localObject).append(" Token=").append(accountService().token()).append("; ");
    if (!TextUtils.isEmpty(Environment.imei()))
      ((StringBuilder)localObject).append(" Device=").append(Environment.imei()).append("; ");
    if (!TextUtils.isEmpty(Environment.sessionId()))
      ((StringBuilder)localObject).append(" Session=").append(Environment.sessionId()).append("; ");
    ((StringBuilder)localObject).append(" City=").append(cityId()).append("; ");
    paramView.add("content");
    paramView.add(((StringBuilder)localObject).toString());
    localObject = location();
    if (localObject != null)
    {
      DecimalFormat localDecimalFormat = Location.FMT;
      paramView.add("lat");
      paramView.add(String.valueOf(localDecimalFormat.format(((Location)localObject).latitude())));
      paramView.add("lng");
      paramView.add(String.valueOf(localDecimalFormat.format(((Location)localObject).longitude())));
    }
    paramView.add("flag");
    paramView.add(String.valueOf(7));
    this.req = BasicMApiRequest.mapiPost("http://m.api.dianping.com/addfeedback.bin", (String[])paramView.toArray(new String[0]));
    mapiService().exec(this.req, this);
    showProgressDialog("正在提交...");
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.locate_city_feedback);
    setupView();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    this.req = null;
    paramMApiRequest = paramMApiResponse.message();
    if (paramMApiRequest != null)
      Toast.makeText(this, paramMApiRequest.toString(), 1).show();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    paramMApiRequest = (DPObject)paramMApiResponse.result();
    if (paramMApiRequest != null)
      Toast.makeText(this, paramMApiRequest.getString("Content"), 0).show();
    this.req = null;
    finish();
  }

  public void onResume()
  {
    super.onResume();
    KeyboardUtils.getSoftKeyboardController(this.city).show();
  }

  public void setupView()
  {
    this.city = ((EditText)findViewById(R.id.city).findViewById(R.id.itemInput));
    findViewById(R.id.btn_submit).setOnClickListener(this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.activity.LocateCityFeedbackActivity
 * JD-Core Version:    0.6.0
 */