package com.dianping.debug;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.base.app.NovaActivity;
import com.dianping.util.PermissionCheckHelper;
import com.dianping.util.PermissionCheckHelper.PermissionCallbackListener;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class PermissionTestActivity extends NovaActivity
  implements View.OnClickListener, PermissionCheckHelper.PermissionCallbackListener
{
  private static final int REQUEST_CODE_BASE = 100;
  LinearLayout mContentLay;
  String[] mPermissions = { "android.permission.READ_PHONE_STATE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.CAMERA", "android.permission.READ_SMS", "android.permission.READ_CONTACTS", "android.permission.WRITE_SETTINGS", "android.permission.GET_TASKS", "android.permission.WRITE_EXTERNAL_STORAGE" };

  private void initPermissionList()
  {
    int i = 0;
    if (i < this.mPermissions.length)
    {
      LinearLayout localLinearLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.permission_item, this.mContentLay, false);
      Object localObject = (Button)localLinearLayout.findViewById(R.id.button);
      ((Button)localObject).setOnClickListener(this);
      ((Button)localObject).setText(this.mPermissions[i].substring("android.permission.".length()));
      ((Button)localObject).setTag(Integer.valueOf(i));
      localObject = (TextView)localLinearLayout.findViewById(R.id.text);
      if (PermissionCheckHelper.isPermissionGranted(this, this.mPermissions[i]))
        ((TextView)localObject).setText("granted");
      while (true)
      {
        this.mContentLay.addView(localLinearLayout, i);
        i += 1;
        break;
        ((TextView)localObject).setText("not granted");
      }
    }
  }

  private void requestPermission(String[] paramArrayOfString, int paramInt)
  {
    PermissionCheckHelper.instance().requestPermissions(this, paramInt + 100, paramArrayOfString, paramArrayOfString, this);
  }

  public void onClick(View paramView)
  {
    if ((paramView instanceof Button))
    {
      Button localButton = (Button)paramView;
      int i = ((Integer)paramView.getTag()).intValue();
      paramView = this.mPermissions[i];
      if (!TextUtils.isEmpty(paramView))
        requestPermission(new String[] { paramView }, i);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_permission_test);
    this.mContentLay = ((LinearLayout)findViewById(R.id.content_lay));
    initPermissionList();
  }

  public void onPermissionCheckCallback(int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt)
  {
    if ((paramArrayOfInt != null) && (paramArrayOfInt.length > 0))
      if (paramArrayOfInt[0] != 0)
        break label72;
    label72: for (paramArrayOfInt = " granted"; ; paramArrayOfInt = " denied")
    {
      ((TextView)((LinearLayout)this.mContentLay.getChildAt(paramInt - 100)).findViewById(R.id.text)).setText(paramArrayOfInt);
      Log.d("Alex", paramArrayOfString[0] + paramArrayOfInt);
      return;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.debug.PermissionTestActivity
 * JD-Core Version:    0.6.0
 */