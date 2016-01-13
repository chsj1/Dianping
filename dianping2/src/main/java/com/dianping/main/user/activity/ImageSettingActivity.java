package com.dianping.main.user.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.DoubleLineCheckView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class ImageSettingActivity extends NovaActivity
  implements View.OnClickListener, LoginResultListener
{
  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.more_show_image)
    {
      paramView = (DoubleLineCheckView)paramView;
      paramView.toggle();
      SharedPreferences.Editor localEditor = preferences().edit();
      localEditor.putBoolean("isShowListImage", paramView.isChecked());
      localEditor.commit();
      if (!paramView.isChecked())
        break label69;
    }
    label69: for (paramView = "开启"; ; paramView = "关闭")
    {
      statisticsEvent("setting5", "setting5_photo_mobilenet", paramView, 0);
      return;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.imagesetting);
    paramBundle = (DoubleLineCheckView)findViewById(R.id.more_show_image);
    paramBundle.setOnClickListener(this);
    paramBundle.setChecked(preferences().getBoolean("isShowListImage", true));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.activity.ImageSettingActivity
 * JD-Core Version:    0.6.0
 */