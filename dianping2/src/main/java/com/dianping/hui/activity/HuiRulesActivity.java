package com.dianping.hui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.hui.util.HuiUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.Iterator;
import java.util.List;

public class HuiRulesActivity extends NovaActivity
{
  private List<DPObject> rules;
  private String tip;

  private LinearLayout buildRule(DPObject paramDPObject)
  {
    LinearLayout localLinearLayout1 = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.huihui_rule_item, null, false);
    TextView localTextView = (TextView)localLinearLayout1.findViewById(R.id.title);
    ImageView localImageView = (ImageView)localLinearLayout1.findViewById(R.id.split_line);
    LinearLayout localLinearLayout2 = (LinearLayout)localLinearLayout1.findViewById(R.id.content);
    localTextView.setText(paramDPObject.getString("Title"));
    paramDPObject = paramDPObject.getArray("RuleDetailDos");
    if ((paramDPObject != null) && (paramDPObject.length > 0))
    {
      displayDialogContent(localLinearLayout2, paramDPObject);
      localImageView.setVisibility(0);
      localLinearLayout2.setVisibility(0);
      return localLinearLayout1;
    }
    localImageView.setVisibility(8);
    localLinearLayout2.setVisibility(8);
    return localLinearLayout1;
  }

  private void displayDialogContent(LinearLayout paramLinearLayout, DPObject[] paramArrayOfDPObject)
  {
    int j = paramArrayOfDPObject.length;
    int i = 0;
    if (i < j)
    {
      Object localObject = paramArrayOfDPObject[i];
      int k = ((DPObject)localObject).getInt("Type");
      localObject = ((DPObject)localObject).getStringArray("RuleDescs");
      switch (k)
      {
      default:
      case 0:
      case 1:
      case 2:
      }
      while (true)
      {
        i += 1;
        break;
        ((TextView)paramLinearLayout.findViewById(R.id.using_time)).setText(HuiUtils.addSpaceToCnPunc(localObject[0]));
        paramLinearLayout.findViewById(R.id.content_using_time).setVisibility(0);
        continue;
        displayDialogContentRules((LinearLayout)paramLinearLayout.findViewById(R.id.rules), localObject);
        paramLinearLayout.findViewById(R.id.content_rules).setVisibility(0);
        continue;
        ((TextView)paramLinearLayout.findViewById(R.id.valid_time)).setText(HuiUtils.addSpaceToCnPunc(localObject[0]));
        paramLinearLayout.findViewById(R.id.content_valid_time).setVisibility(0);
      }
    }
  }

  private void displayDialogContentRules(LinearLayout paramLinearLayout, String[] paramArrayOfString)
  {
    paramLinearLayout.removeAllViews();
    int j = paramArrayOfString.length;
    int i = 0;
    while (i < j)
    {
      String str = paramArrayOfString[i];
      RelativeLayout localRelativeLayout = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.huihui_discount_popup_content_rule, null, false);
      TextView localTextView = (TextView)localRelativeLayout.findViewById(R.id.detail);
      localTextView.setMaxLines(2147483647);
      localTextView.setText(HuiUtils.addSpaceToCnPunc(str));
      paramLinearLayout.addView(localRelativeLayout);
      i += 1;
    }
  }

  private void initView()
  {
    Object localObject = (TextView)findViewById(R.id.title);
    LinearLayout localLinearLayout1;
    LinearLayout.LayoutParams localLayoutParams;
    Iterator localIterator;
    if (TextUtils.isEmpty(this.tip))
    {
      ((TextView)localObject).setVisibility(8);
      localObject = (LinearLayout)findViewById(R.id.normal_rules);
      localLinearLayout1 = (LinearLayout)findViewById(R.id.special_rules);
      localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
      localLayoutParams.setMargins(0, ViewUtils.dip2px(this, 22.0F), 0, 0);
      localIterator = this.rules.iterator();
    }
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      DPObject localDPObject = (DPObject)localIterator.next();
      LinearLayout localLinearLayout2 = buildRule(localDPObject);
      if (localDPObject.getInt("Type") == 1)
      {
        if (((LinearLayout)localObject).getChildCount() > 0)
        {
          ((LinearLayout)localObject).addView(localLinearLayout2, localLayoutParams);
          continue;
          ((TextView)localObject).setText(this.tip);
          break;
        }
        ((LinearLayout)localObject).addView(localLinearLayout2);
        continue;
      }
      if (localLinearLayout1.getChildCount() > 0)
      {
        localLinearLayout1.addView(localLinearLayout2, localLayoutParams);
        continue;
      }
      localLinearLayout1.addView(localLinearLayout2);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.huihui_rules_activity);
    super.getWindow().setBackgroundDrawable(null);
    setTitle("使用规则");
    this.rules = getIntent().getParcelableArrayListExtra("rules");
    this.tip = getStringParam("tip");
    initView();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hui.activity.HuiRulesActivity
 * JD-Core Version:    0.6.0
 */