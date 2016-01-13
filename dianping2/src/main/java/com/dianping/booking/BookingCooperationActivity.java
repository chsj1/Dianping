package com.dianping.booking;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookingCooperationActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private CompoundButton femaleCheckBox;
  private MApiRequest mRequest;
  private CompoundButton maleCheckBox;
  private EditText name;
  private EditText phone;
  private TextView rightBtn;
  private EditText shopName;

  private boolean checkInput()
  {
    clearError();
    String str = this.name.getText().toString().trim();
    if (TextUtils.isEmpty(str))
    {
      showError(this.name, "您的姓名");
      return false;
    }
    if (!Pattern.compile("^[a-zA-Z一-龥]+$").matcher(str).matches())
    {
      new AlertDialog.Builder(this).setMessage("请输入真实姓名").setPositiveButton("知道了", null).show().setCanceledOnTouchOutside(true);
      return false;
    }
    if (TextUtils.isEmpty(this.shopName.getText().toString().trim()))
    {
      showError(this.shopName, "请输入餐厅名称");
      return false;
    }
    str = this.phone.getText().toString().trim();
    if (TextUtils.isEmpty(str))
    {
      showError(this.phone, "餐厅老板的手机号");
      return false;
    }
    if ((str.length() < 11) || (str.indexOf("1") != 0))
    {
      new AlertDialog.Builder(this).setMessage("请输入真实手机号").setPositiveButton("知道了", null).show().setCanceledOnTouchOutside(true);
      return false;
    }
    return true;
  }

  private void clearError()
  {
    this.name.setError(null);
    this.shopName.setError(null);
    this.phone.setError(null);
  }

  private void cooperationTask()
  {
    if (this.mRequest != null)
      return;
    String str2 = this.name.getText().toString().trim();
    String str3 = this.shopName.getText().toString().trim();
    String str1 = str2;
    if (str2.length() > 12)
      str1 = str2.substring(0, 12);
    this.mRequest = BasicMApiRequest.mapiPost("http://rs.api.dianping.com/cooperationinfo.yy", new String[] { "name", str1, "phone", this.phone.getText().toString().trim(), "shopName", str3, "gender", String.valueOf(getBookingGender()) });
    mapiService().exec(this.mRequest, this);
    showProgressDialog("正在提交信息，请稍候...");
  }

  private int getBookingGender()
  {
    if (this.femaleCheckBox.isChecked())
      return 10;
    return 20;
  }

  private void hideSoftKeyBoard()
  {
    InputMethodManager localInputMethodManager = (InputMethodManager)getSystemService("input_method");
    if (this.name != null)
      localInputMethodManager.hideSoftInputFromWindow(this.name.getWindowToken(), 0);
    if (this.shopName != null)
      localInputMethodManager.hideSoftInputFromWindow(this.shopName.getWindowToken(), 0);
    if (this.phone != null)
      localInputMethodManager.hideSoftInputFromInputMethod(this.phone.getWindowToken(), 0);
  }

  private void showError(EditText paramEditText, String paramString)
  {
    paramEditText.requestFocus();
    if (paramString != null)
    {
      SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(paramString);
      localSpannableStringBuilder.setSpan(new ForegroundColorSpan(-16777216), 0, paramString.length(), 0);
      paramEditText.setError(localSpannableStringBuilder);
    }
  }

  private void submitCooperationInfo()
  {
    if (!checkInput())
      return;
    new AlertDialog.Builder(this).setTitle("确认提交").setMessage("只差一步您就可以向千万会员打开预订之门，确认提交申请？").setPositiveButton("提交", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        BookingCooperationActivity.this.cooperationTask();
      }
    }).setNegativeButton("取消", null).show();
    statisticsEvent("mybooking5", "mybooking5_merchantsubmit", "", 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getWindow().setBackgroundDrawable(null);
    super.setContentView(R.layout.booking_cooperation_layout);
    this.name = ((EditText)findViewById(R.id.name));
    this.shopName = ((EditText)findViewById(R.id.shop_name));
    this.phone = ((EditText)findViewById(R.id.phone));
    paramBundle = new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        BookingCooperationActivity.this.clearError();
        return false;
      }
    };
    this.name.setOnTouchListener(paramBundle);
    this.shopName.setOnTouchListener(paramBundle);
    this.phone.setOnTouchListener(paramBundle);
    this.femaleCheckBox = ((CompoundButton)findViewById(R.id.female));
    this.maleCheckBox = ((CompoundButton)findViewById(R.id.male));
    this.femaleCheckBox.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        BookingCooperationActivity.this.femaleCheckBox.setChecked(true);
        BookingCooperationActivity.this.maleCheckBox.setChecked(false);
      }
    });
    this.maleCheckBox.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        BookingCooperationActivity.this.femaleCheckBox.setChecked(false);
        BookingCooperationActivity.this.maleCheckBox.setChecked(true);
      }
    });
    this.rightBtn = ((TextView)findViewById(R.id.title_button));
    this.rightBtn.setVisibility(0);
    this.rightBtn.setText("提交");
    this.rightBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        BookingCooperationActivity.this.hideSoftKeyBoard();
        BookingCooperationActivity.this.submitCooperationInfo();
      }
    });
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.mRequest != null)
    {
      mapiService().abort(this.mRequest, this, true);
      this.mRequest = null;
    }
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 4)
      statisticsEvent("mybooking6", "mybooking6_merchant_back", "", 0);
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  protected void onLeftTitleButtonClicked()
  {
    statisticsEvent("mybooking6", "mybooking6_merchant_back", "", 0);
    super.onLeftTitleButtonClicked();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.mRequest)
    {
      if (paramMApiResponse != null)
        showMessageDialog(paramMApiResponse.message());
      this.mRequest = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    dismissDialog();
    if (paramMApiRequest == this.mRequest)
    {
      if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
      {
        statisticsEvent("mybooking6", "mybooking6_merchant_submit", "", 0);
        showShortToast(((DPObject)paramMApiResponse.result()).getString("Content"));
        finish();
      }
      this.mRequest = null;
    }
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    this.name.setText(paramBundle.getString("name"));
    this.shopName.setText(paramBundle.getString("shopname"));
    this.phone.setText(paramBundle.getString("phone"));
    super.onRestoreInstanceState(paramBundle);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("name", this.name.getText().toString());
    paramBundle.putString("shopname", this.shopName.getText().toString());
    paramBundle.putString("phone", this.phone.getText().toString());
    super.onSaveInstanceState(paramBundle);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.BookingCooperationActivity
 * JD-Core Version:    0.6.0
 */