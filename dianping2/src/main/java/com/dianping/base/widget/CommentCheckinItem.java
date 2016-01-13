package com.dianping.base.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.util.DateUtil;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.CheckinListItemListener;
import com.dianping.widget.emoji.EmojiTextView;
import java.util.Date;

public class CommentCheckinItem extends LinearLayout
  implements View.OnClickListener
{
  public static final long DE_SEL_DELAY = 600L;
  private static Drawable lockDrawable;
  private static Drawable lockDrawable2;
  LinearLayout address;
  DPObject checkin;
  CheckInPhotosView checkinPhotos;
  EmojiTextView content;
  DeselectHandler handler;
  View mask;
  TextView name;
  TextView queuingInfo;
  View secret;
  TextView shop;
  boolean showTime;
  ShopPower star;
  NetworkThumbView thumb;
  TextView time;
  int userId;

  public CommentCheckinItem(Context paramContext)
  {
    super(paramContext);
    loadDrawable();
  }

  public CommentCheckinItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    loadDrawable();
  }

  private void loadDrawable()
  {
    if (lockDrawable == null)
    {
      lockDrawable = getResources().getDrawable(R.drawable.secret_checkin);
      lockDrawable.setBounds(0, 0, lockDrawable.getIntrinsicWidth(), lockDrawable.getIntrinsicHeight());
    }
    if (lockDrawable2 == null)
    {
      lockDrawable2 = getResources().getDrawable(R.drawable.secret_checkin2);
      lockDrawable2.setBounds(0, 0, lockDrawable2.getIntrinsicWidth(), lockDrawable.getIntrinsicHeight());
    }
  }

  private void resetStatus()
  {
    if (this.thumb != null)
      this.thumb.setSelected(false);
    if (this.star != null)
      this.star.setSelected(false);
    if (this.name != null)
      this.name.setSelected(false);
    if (this.time != null)
      this.time.setSelected(false);
    if (this.queuingInfo != null)
      this.queuingInfo.setSelected(false);
    if (this.shop != null)
      this.shop.setSelected(false);
    if (this.content != null)
      this.content.setSelected(false);
    if (this.mask != null)
      this.mask.setVisibility(4);
  }

  public void onClick(View paramView)
  {
    if ((paramView == this.name) || (paramView == this.thumb))
      if (this.checkin != null)
      {
        if (this.checkin.getObject("User").getInt("UserID") == this.userId)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://user"));
          paramView.putExtra("user", this.checkin.getObject("User"));
          getContext().startActivity(paramView);
          DPApplication.instance().statisticsEvent("checkin5", "viewcheckin5_detail_userprofile", "" + this.checkin.getInt("ID"), 0);
        }
      }
      else
      {
        if (this.handler == null)
          this.handler = new DeselectHandler(null);
        this.handler.sendMessageDelayed(this.handler.obtainMessage(0, this.name), 600L);
      }
    do
    {
      return;
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://user?userid=" + this.checkin.getObject("User").getInt("UserID")));
      break;
    }
    while (paramView != this.address);
    if ((this.address != null) && (this.checkin != null))
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopinfo?id=" + this.checkin.getInt("ShopID")));
      getContext().startActivity(paramView);
      DPApplication.instance().statisticsEvent("checkin5", "viewcheckin5_detail_shopinfo", "" + this.checkin.getInt("ID"), 0);
    }
    if (this.handler == null)
      this.handler = new DeselectHandler(null);
    this.handler.sendMessageDelayed(this.handler.obtainMessage(0, this.shop), 600L);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.checkinPhotos = ((CheckInPhotosView)findViewById(R.id.checkin_photos));
    this.thumb = ((NetworkThumbView)findViewById(16908294));
    this.star = ((ShopPower)findViewById(R.id.checkin_star));
    this.name = ((TextView)findViewById(R.id.checkin_name));
    this.time = ((TextView)findViewById(R.id.checkin_time));
    this.shop = ((TextView)findViewById(R.id.checkin_shop));
    this.address = ((LinearLayout)findViewById(R.id.address));
    if (this.address != null)
      this.address.setOnClickListener(this);
    this.content = ((EmojiTextView)findViewById(R.id.checkin_content));
    this.secret = findViewById(R.id.secret);
    this.mask = findViewById(R.id.checkin_icon_mask);
    if (this.name != null)
      this.name.setOnClickListener(this);
    if (this.star != null)
      this.star.setOnClickListener(this);
    if (this.time != null)
      this.time.setOnClickListener(this);
    if (this.content != null)
      this.content.setOnClickListener(this);
    if (this.thumb != null)
      this.thumb.setOnClickListener(this);
    setOnClickListener(this);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public void setCheckin(DPObject paramDPObject, int paramInt)
  {
    int i = 0;
    resetStatus();
    this.userId = paramInt;
    this.checkin = paramDPObject;
    if (paramDPObject == null)
    {
      if (this.thumb != null)
        this.thumb.setImage(null);
      if (this.star != null)
      {
        this.star.setPower(0);
        this.star.setVisibility(8);
      }
      if (this.name != null)
        this.name.setText(null);
      if (this.time != null)
        this.time.setText(null);
      if (this.shop != null)
        this.shop.setText(null);
      if (this.content != null)
      {
        this.content.setText(null);
        this.content.setVisibility(8);
      }
    }
    Object localObject2;
    Object localObject1;
    while (true)
    {
      return;
      localObject2 = paramDPObject.getObject("User");
      if ((this.thumb != null) && (localObject2 != null))
      {
        NetworkThumbView localNetworkThumbView = this.thumb;
        if ((((DPObject)localObject2).getString("Avatar") != null) && (((DPObject)localObject2).getString("Avatar").length() != 0))
          break;
        localObject1 = null;
        localNetworkThumbView.setImage((String)localObject1);
      }
      else
      {
        if ((this.name != null) && (localObject2 != null))
          this.name.setText(((DPObject)localObject2).getString("NickName"));
        if (this.checkinPhotos != null)
        {
          localObject1 = paramDPObject.getStringArray("Images");
          localObject2 = paramDPObject.getStringArray("ThumbImages");
          this.checkinPhotos.setImageUrls(localObject2, localObject1);
        }
        if (this.time != null)
        {
          if (!this.showTime)
            break label450;
          this.time.setText(DateUtil.format2t(new Date(paramDPObject.getTime("Time"))));
          label288: if (paramDPObject.getBoolean("IsShared"))
            break label477;
          this.time.setCompoundDrawablePadding(10);
          this.secret.setVisibility(0);
        }
        label315: if (this.shop != null)
          this.shop.setText(paramDPObject.getString("ShopName"));
        if (this.content != null)
        {
          this.content.setEmojiText(paramDPObject.getString("Tips"));
          localObject1 = this.content;
          if ((paramDPObject.getString("Tips") != null) && (paramDPObject.getString("Tips").length() != 0))
            break label491;
          paramInt = 8;
          label388: ((EmojiTextView)localObject1).setVisibility(paramInt);
        }
        if (this.star == null)
          continue;
        this.star.setPower(paramDPObject.getInt("Star"));
        localObject1 = this.star;
        if (paramDPObject.getInt("Star") <= 0)
          break label496;
      }
    }
    label450: label477: label491: label496: for (paramInt = i; ; paramInt = 8)
    {
      ((ShopPower)localObject1).setVisibility(paramInt);
      return;
      localObject1 = ((DPObject)localObject2).getString("Avatar");
      break;
      this.time.setText(DateUtil.format2(new Date(paramDPObject.getTime("Time"))));
      break label288;
      this.time.setCompoundDrawables(null, null, null, null);
      break label315;
      paramInt = 0;
      break label388;
    }
  }

  public void setPhotoTouchListener(CheckinListItemListener paramCheckinListItemListener)
  {
    if (this.checkinPhotos != null)
      this.checkinPhotos.setPhotoTouchListener(paramCheckinListItemListener);
  }

  public void setShowTime(boolean paramBoolean)
  {
    if (paramBoolean != this.showTime)
    {
      this.showTime = paramBoolean;
      invalidate();
    }
  }

  private class DeselectHandler extends Handler
  {
    private DeselectHandler()
    {
    }

    public void handleMessage(Message paramMessage)
    {
      if ((paramMessage.what == 0) && ((paramMessage.obj instanceof View)))
      {
        View localView = (View)paramMessage.obj;
        localView.setSelected(false);
        if ((localView == CommentCheckinItem.this.name) && (CommentCheckinItem.this.mask != null))
          CommentCheckinItem.this.mask.setVisibility(4);
      }
      super.handleMessage(paramMessage);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.CommentCheckinItem
 * JD-Core Version:    0.6.0
 */