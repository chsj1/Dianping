package com.dianping.base.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.locationservice.LocationService;
import com.dianping.v1.R.array;
import com.dianping.v1.R.id;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;

public class LocalBar extends NovaLinearLayout
{
  OnStartRelocateListener mStartRelocateListener;
  ImageView positionCurrent;
  ProgressBar positionProgress;
  TextView positionText;

  public LocalBar(Context paramContext)
  {
    super(paramContext);
  }

  public LocalBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.positionProgress = ((ProgressBar)findViewById(R.id.positionProgress));
    this.positionCurrent = ((ImageView)findViewById(R.id.positionCurrent));
    this.positionText = ((TextView)findViewById(R.id.positionText));
    setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if ("正在定位...".equals(LocalBar.this.positionText.getText()))
          return;
        new AlertDialog.Builder(LocalBar.this.getContext()).setTitle("选择").setItems(R.array.select_changelocation_items, new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            switch (paramInt)
            {
            default:
            case 0:
              do
              {
                return;
                LocalBar.this.gaUserInfo.title = "重新定位";
                GAHelper.instance().contextStatisticsEvent(LocalBar.this.getContext(), "location_anew", LocalBar.this.gaUserInfo, "tap");
                DPApplication.instance().statisticsEvent("shoplist5", "shoplist5_relocate_nearby", "刷新定位", 0);
                DPApplication.instance().locationService().refresh();
                LocalBar.this.positionProgress.setVisibility(0);
                LocalBar.this.positionCurrent.setVisibility(8);
                LocalBar.this.positionText.setText("正在定位...");
                LocalBar.this.gaUserInfo.title = "正在定位...";
              }
              while (LocalBar.this.mStartRelocateListener == null);
              LocalBar.this.mStartRelocateListener.onStartRelocate();
              return;
            case 1:
            }
            paramDialogInterface = LocalBar.this.gaUserInfo.title;
            LocalBar.this.gaUserInfo.title = "自定义地址";
            GAHelper.instance().contextStatisticsEvent(LocalBar.this.getContext(), "location_define", LocalBar.this.gaUserInfo, "tap");
            LocalBar.this.gaUserInfo.title = paramDialogInterface;
            DPApplication.instance().statisticsEvent("shoplist5", "shoplist5_relocate_nearby", "自定义地点", 0);
            ((Activity)LocalBar.this.getContext()).startActivityForResult(new Intent("android.intent.action.VIEW", Uri.parse("dianping://selectlocation")), 10001);
          }
        }).create().show();
      }
    });
  }

  public void setOnStartRelocateListener(OnStartRelocateListener paramOnStartRelocateListener)
  {
    this.mStartRelocateListener = paramOnStartRelocateListener;
  }

  public void setvalue(LocationService paramLocationService)
  {
    if (paramLocationService == null)
      return;
    if (paramLocationService.location() != null)
    {
      this.positionProgress.setVisibility(8);
      this.positionCurrent.setVisibility(0);
      this.positionText.setText(paramLocationService.city().getString("Name") + paramLocationService.address());
    }
    while (true)
    {
      this.gaUserInfo.title = this.positionText.getText().toString();
      return;
      if (paramLocationService.status() < 0)
      {
        this.positionProgress.setVisibility(8);
        this.positionCurrent.setVisibility(8);
        this.positionText.setText("无法获取您当前的位置");
        continue;
      }
      this.positionProgress.setVisibility(0);
      this.positionCurrent.setVisibility(8);
      this.positionText.setText("正在定位...");
    }
  }

  public static abstract interface OnStartRelocateListener
  {
    public abstract void onStartRelocate();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.LocalBar
 * JD-Core Version:    0.6.0
 */