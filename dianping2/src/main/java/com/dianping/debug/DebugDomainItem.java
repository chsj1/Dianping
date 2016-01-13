package com.dianping.debug;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.styleable;

public class DebugDomainItem extends RelativeLayout
  implements View.OnClickListener
{
  public static final int ALPHA_TYPE = 3;
  public static final int BETA_TYPE = 1;
  public static final int CUSTOM_TYPE = 5;
  public static final int DIANPING_TYPE = 0;
  public static final int MOBILE_API = 6;
  public static final int PPE_TYPE = 2;
  private static final String TAG = DebugDomainItem.class.getSimpleName();
  public static final int YIMIN_TYPE = 4;
  private int currentSelection = 0;
  private SparseArray domains = new SparseArray();
  private Context mContext;
  private EditText mDebugDomainEditText;
  private Button mDomainSelectButton;

  public DebugDomainItem(Context paramContext)
  {
    this(paramContext, null);
    this.mContext = paramContext;
  }

  public DebugDomainItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.DebugDomainItem);
    paramAttributeSet = localTypedArray.getString(R.styleable.DebugDomainItem_itemDomainSelector);
    String str1 = localTypedArray.getString(R.styleable.DebugDomainItem_itemDianpingDomain);
    String str2 = localTypedArray.getString(R.styleable.DebugDomainItem_itemBetaDomain);
    String str3 = localTypedArray.getString(R.styleable.DebugDomainItem_itemPPEDomain);
    String str4 = localTypedArray.getString(R.styleable.DebugDomainItem_itemAlphaDomain);
    String str5 = localTypedArray.getString(R.styleable.DebugDomainItem_itemYiminDomain);
    String str6 = localTypedArray.getString(R.styleable.DebugDomainItem_itemCustomDomain);
    String str7 = localTypedArray.getString(R.styleable.DebugDomainItem_itemMobileAPIDomain);
    localTypedArray.recycle();
    paramContext = LayoutInflater.from(paramContext).inflate(R.layout.debug_domain_item, this, true);
    this.mDomainSelectButton = ((Button)paramContext.findViewById(R.id.domain_selector));
    this.mDebugDomainEditText = ((EditText)paramContext.findViewById(R.id.debug_domain));
    this.mDomainSelectButton.setText(paramAttributeSet);
    this.mDebugDomainEditText.setHint(str1);
    if (!TextUtils.isEmpty(str1))
      this.domains.append(0, str1);
    if (!TextUtils.isEmpty(str2))
      this.domains.append(1, str2);
    if (!TextUtils.isEmpty(str3))
      this.domains.append(2, str3);
    if (!TextUtils.isEmpty(str4))
      this.domains.append(3, str4);
    if (!TextUtils.isEmpty(str5))
      this.domains.append(4, str5);
    if (!TextUtils.isEmpty(str6))
      this.domains.append(5, str6);
    if (!TextUtils.isEmpty(str7))
      this.domains.append(6, str7);
    this.mDomainSelectButton.setOnClickListener(this);
  }

  public String getCurrentDomain()
  {
    return this.mDebugDomainEditText.getText().toString().trim();
  }

  public String getDomain(int paramInt)
  {
    return (String)this.domains.get(paramInt, null);
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() != R.id.domain_selector)
      return;
    paramView = new String[this.domains.size()];
    int i = 0;
    while (i < this.domains.size())
    {
      paramView[i] = ((String)this.domains.valueAt(i));
      i += 1;
    }
    new AlertDialog.Builder(this.mContext).setTitle("域名选择").setSingleChoiceItems(paramView, this.currentSelection, new DebugDomainItem.2(this, paramView)).setNegativeButton("取消", new DebugDomainItem.1(this)).show();
  }

  public void setCurrentSelection(int paramInt)
  {
    this.currentSelection = paramInt;
  }

  public void setDomain(String paramString)
  {
    this.mDebugDomainEditText.setText(paramString);
    int i = 0;
    while (true)
    {
      if (i < this.domains.size())
      {
        if (this.domains.valueAt(i) == paramString)
          this.currentSelection = i;
      }
      else
      {
        this.currentSelection = 0;
        return;
      }
      i += 1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.debug.DebugDomainItem
 * JD-Core Version:    0.6.0
 */