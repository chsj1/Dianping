package com.dianping.hotel.tuan.widget;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HotelTouristInfo extends LinearLayout
{
  private EditText nameEdit;
  private EditText phoneEdit;
  private TextView textView1;
  private TextView textView2;

  public HotelTouristInfo(Context paramContext)
  {
    this(paramContext, null);
  }

  public HotelTouristInfo(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView();
  }

  private void initView()
  {
    LayoutInflater.from(getContext()).inflate(R.layout.hotel_tourist_info, this, true);
    this.textView1 = ((TextView)findViewById(R.id.text1));
    this.textView2 = ((TextView)findViewById(R.id.text2));
    this.nameEdit = ((EditText)findViewById(R.id.people_name));
    this.phoneEdit = ((EditText)findViewById(R.id.phone));
  }

  private boolean validateName(EditText paramEditText)
  {
    String str2 = paramEditText.getText().toString();
    String str1 = null;
    if (TextUtils.isEmpty(str2))
      str1 = "姓名不能为空";
    while (str1 != null)
    {
      paramEditText.requestFocus();
      paramEditText.setError(Html.fromHtml("<font color=#ff0000>" + str1 + "</font>"));
      return false;
      str2 = str2.trim();
      if (Pattern.compile("^[a-zA-Z一-龥]+$").matcher(str2).matches())
        continue;
      str1 = "姓名只能为英文和汉字";
    }
    return true;
  }

  private boolean validatePhone(EditText paramEditText)
  {
    String str2 = paramEditText.getText().toString().trim();
    String str1 = null;
    if (str2.length() < 11)
      str1 = "手机号码必须为11位";
    while (str1 != null)
    {
      paramEditText.requestFocus();
      paramEditText.setError(Html.fromHtml("<font color=#ff0000>" + str1 + "</font>"));
      return false;
      if (str2.startsWith("1"))
        continue;
      str1 = "请输入正确的手机号";
    }
    return true;
  }

  public boolean checkInput()
  {
    if (!validateName(this.nameEdit));
    do
      return false;
    while (!validatePhone(this.phoneEdit));
    return true;
  }

  public String getName()
  {
    return this.nameEdit.getText().toString().trim();
  }

  public String getPhone()
  {
    return this.phoneEdit.getText().toString().trim();
  }

  public void hideKeyBoard()
  {
    InputMethodManager localInputMethodManager = (InputMethodManager)getContext().getSystemService("input_method");
    if (this.nameEdit != null)
      localInputMethodManager.hideSoftInputFromWindow(this.nameEdit.getWindowToken(), 0);
    if (this.phoneEdit != null)
      localInputMethodManager.hideSoftInputFromInputMethod(this.phoneEdit.getWindowToken(), 0);
  }

  public void setName(String paramString)
  {
    this.nameEdit.setText(paramString);
  }

  public void setPhone(String paramString)
  {
    this.phoneEdit.setText(paramString);
  }

  public void setTextView1(String paramString)
  {
    this.textView1.setText(paramString);
  }

  public void setTextView2(String paramString)
  {
    this.textView2.setText(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.tuan.widget.HotelTouristInfo
 * JD-Core Version:    0.6.0
 */