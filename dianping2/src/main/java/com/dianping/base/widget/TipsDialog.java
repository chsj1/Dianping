package com.dianping.base.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.v1.R.anim;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class TipsDialog extends GuideDialog
{
  String content;
  int contentGravity = 3;
  TextView contentTv;
  Context context;
  TipsType direct;
  int distanceX;
  int distanceY;
  ImageView downIv;
  FrameLayout frameLayout;
  LinearLayout linearLayout1;
  LinearLayout linearLayout2;
  LinearLayout linearLayoutMain;
  float paddingLeft = 10.0F;
  float paddingRight = 10.0F;
  ImageView upIv;

  public TipsDialog(Context paramContext, String paramString, TipsType paramTipsType, int paramInt1, int paramInt2)
  {
    super(paramContext, R.layout.tips_dialog);
    this.context = paramContext;
    this.content = paramString;
    this.direct = paramTipsType;
    this.distanceX = paramInt1;
    this.distanceY = paramInt2;
  }

  public TipsDialog(Context paramContext, String paramString, TipsType paramTipsType, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    this(paramContext, paramString, paramTipsType, paramInt1, paramInt2);
    this.paddingLeft = paramInt3;
    this.paddingRight = paramInt4;
    this.contentGravity = paramInt5;
  }

  private void init()
  {
    this.contentTv.setText(this.content);
    this.contentTv.setGravity(this.contentGravity);
    Bitmap localBitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.tip_up);
    int i = localBitmap.getWidth();
    int j = localBitmap.getHeight();
    if ((this.direct == TipsType.UP_LEFT) || (this.direct == TipsType.UP_RIGHT))
    {
      this.upIv.setVisibility(0);
      this.downIv.setVisibility(4);
      this.frameLayout.setPadding(dip2px(this.context, this.paddingLeft), this.distanceY + j, dip2px(this.context, this.paddingRight), 0);
      if (this.direct == TipsType.UP_RIGHT)
      {
        this.linearLayout1.setPadding(dip2px(this.context, 15.0F), dip2px(this.context, 7.0F), 0, 0);
        this.linearLayout2.setGravity(3);
        this.upIv.setPadding(this.distanceX - dip2px(this.context, this.paddingLeft) - i / 2 - dip2px(this.context, 20.0F), 0, 0, 0);
        return;
      }
      this.linearLayout1.setPadding(0, dip2px(this.context, 7.0F), dip2px(this.context, 15.0F), 0);
      this.upIv.setPadding(this.distanceX - dip2px(this.context, this.paddingLeft) - i / 2, 0, 0, 0);
      return;
    }
    if ((this.direct == TipsType.DOWN_LEFT) || (this.direct == TipsType.DOWN_RIGHT))
    {
      this.upIv.setVisibility(4);
      this.downIv.setVisibility(0);
      this.linearLayoutMain.setGravity(80);
      this.frameLayout.setPadding(dip2px(this.context, this.paddingLeft), 0, dip2px(this.context, this.paddingRight), this.distanceY);
      if (this.direct == TipsType.DOWN_RIGHT)
      {
        this.linearLayout1.setPadding(dip2px(this.context, 20.0F), dip2px(this.context, 7.0F), 0, 0);
        this.linearLayout2.setGravity(3);
        this.downIv.setPadding(this.distanceX - dip2px(this.context, this.paddingLeft) - i / 2 - dip2px(this.context, 20.0F), 0, 0, 0);
        return;
      }
      this.linearLayout1.setPadding(0, dip2px(this.context, 7.0F), dip2px(this.context, 15.0F), 0);
      this.downIv.setPadding(this.distanceX - dip2px(this.context, this.paddingLeft) - i / 2, 0, 0, 0);
      return;
    }
    this.upIv.setVisibility(4);
    this.downIv.setVisibility(4);
    this.linearLayoutMain.setGravity(80);
    this.frameLayout.setPadding(dip2px(this.context, this.paddingLeft), 0, dip2px(this.context, this.paddingRight), this.distanceY);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.linearLayoutMain = ((LinearLayout)findViewById(R.id.tip_dialog_linearMain));
    this.linearLayoutMain.getBackground().setAlpha(120);
    this.linearLayout1 = ((LinearLayout)findViewById(R.id.tip_dialog_linear1));
    this.linearLayout2 = ((LinearLayout)findViewById(R.id.tip_dialog_linear2));
    this.frameLayout = ((FrameLayout)findViewById(R.id.tip_dialog_frame));
    this.upIv = ((ImageView)findViewById(R.id.tip_dialog_upImg));
    this.downIv = ((ImageView)findViewById(R.id.tip_dialog_downImg));
    this.contentTv = ((TextView)findViewById(R.id.tip_dialog_content));
    init();
  }

  protected void playAnimation()
  {
    new Handler()
    {
      public void handleMessage(Message paramMessage)
      {
        paramMessage = AnimationUtils.loadAnimation(TipsDialog.this.context, R.anim.tip_enter);
        TipsDialog.this.frameLayout.startAnimation(paramMessage);
        paramMessage = AnimationUtils.loadAnimation(TipsDialog.this.context, R.anim.tip_back_show);
        TipsDialog.this.linearLayoutMain.startAnimation(paramMessage);
      }
    }
    .sendEmptyMessage(1);
  }

  public static enum TipsType
  {
    static
    {
      DOWN_LEFT = new TipsType("DOWN_LEFT", 2);
      DOWN_RIGHT = new TipsType("DOWN_RIGHT", 3);
      $VALUES = new TipsType[] { UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.TipsDialog
 * JD-Core Version:    0.6.0
 */