package com.dianping.base.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.util.DateUtil;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.CheckinListItemListener;
import com.dianping.widget.emoji.EmojiTextView;
import java.util.Date;

public class CheckinListItem extends RelativeLayout
  implements View.OnTouchListener
{
  public static final long DE_SEL_DELAY = 600L;
  DPObject checkin;
  CheckInPhotosView checkinPhotos;
  EmojiTextView content;
  DeselectHandler handler;
  View mask;
  TextView name;
  TextView queuingInfo;
  TextView replyNum;
  TextView shop;
  boolean showTime;
  ShopPower star;
  NetworkThumbView thumb;
  TextView time;

  public CheckinListItem(Context paramContext)
  {
    super(paramContext);
  }

  public CheckinListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void resetStatus()
  {
    if (this.checkinPhotos != null)
      this.checkinPhotos.setSelected(false);
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
    if (this.replyNum != null)
      this.replyNum.setSelected(false);
    if (this.mask != null)
      this.mask.setVisibility(4);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.checkinPhotos = ((CheckInPhotosView)findViewById(R.id.checkin_photos));
    this.thumb = ((NetworkThumbView)findViewById(16908294));
    this.star = ((ShopPower)findViewById(R.id.checkin_star));
    this.name = ((TextView)findViewById(R.id.checkin_name));
    this.time = ((TextView)findViewById(R.id.checkin_time));
    this.queuingInfo = ((TextView)findViewById(R.id.checkin_queuing_info));
    this.shop = ((TextView)findViewById(R.id.checkin_shop));
    this.content = ((EmojiTextView)findViewById(R.id.checkin_content));
    this.replyNum = ((TextView)findViewById(R.id.reply_num));
    this.mask = findViewById(R.id.checkin_icon_mask);
    setOnTouchListener(this);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (this.replyNum != null)
    {
      paramInt1 = this.replyNum.getHeight();
      paramInt2 = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom() - paramInt1) / 2;
      this.replyNum.layout(this.replyNum.getLeft(), paramInt2, this.replyNum.getRight(), paramInt2 + paramInt1);
    }
  }

  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    switch (paramMotionEvent.getAction())
    {
    default:
    case 3:
    }
    do
    {
      return false;
      if (this.shop != null)
        this.shop.setSelected(false);
      if (this.name != null)
        this.name.setSelected(false);
      if (this.mask == null)
        continue;
      this.mask.setVisibility(4);
    }
    while (this.replyNum == null);
    this.replyNum.setSelected(false);
    return false;
  }

  public void setCheckin(DPObject paramDPObject, int paramInt)
  {
    int i = 0;
    resetStatus();
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
      if (this.queuingInfo != null)
        this.queuingInfo.setText(null);
      if (this.time != null)
        this.time.setText(null);
      if (this.shop != null)
        this.shop.setText(null);
      if (this.content != null)
      {
        this.content.setText(null);
        this.content.setVisibility(8);
        if (this.replyNum != null)
          this.replyNum.setText(" + ");
      }
      return;
    }
    DPObject localDPObject = paramDPObject.getObject("User");
    Object localObject2;
    Object localObject1;
    label403: if ((this.thumb != null) && (localDPObject != null))
    {
      localObject2 = this.thumb;
      if ((localDPObject.getString("Avatar") == null) || (localDPObject.getString("Avatar").length() == 0))
      {
        localObject1 = null;
        label207: ((NetworkThumbView)localObject2).setImage((String)localObject1);
      }
    }
    else
    {
      if (this.checkinPhotos != null)
      {
        localObject1 = paramDPObject.getStringArray("Images");
        localObject2 = paramDPObject.getStringArray("ThumbImages");
        this.checkinPhotos.setImageUrls(localObject2, localObject1);
      }
      if (this.star != null)
      {
        this.star.setPower(paramDPObject.getInt("Star"));
        localObject1 = this.star;
        if (paramDPObject.getInt("Star") <= 0)
          break label577;
        paramInt = 0;
        label281: ((ShopPower)localObject1).setVisibility(paramInt);
      }
      if ((this.name != null) && (localDPObject != null))
        this.name.setText(localDPObject.getString("NickName"));
      if (this.queuingInfo != null)
      {
        if ((paramDPObject.getString("Queuing") == null) || ("".equals(paramDPObject.getString("Queuing").trim())))
          break label583;
        this.queuingInfo.setText(paramDPObject.getString("Queuing"));
        this.queuingInfo.setVisibility(0);
      }
      label366: if (this.time != null)
      {
        if (!this.showTime)
          break label595;
        this.time.setText(DateUtil.format2t(new Date(paramDPObject.getTime("Time"))));
        if (paramDPObject.getBoolean("IsShared"))
          break label621;
        this.time.setCompoundDrawablePadding(10);
        localObject1 = getResources().getDrawable(R.drawable.secret_checkin);
        ((Drawable)localObject1).setBounds(0, 0, ((Drawable)localObject1).getIntrinsicWidth(), ((Drawable)localObject1).getIntrinsicHeight());
        this.time.setCompoundDrawables((Drawable)localObject1, null, null, null);
      }
    }
    while (true)
    {
      if (this.shop != null)
        this.shop.setText(paramDPObject.getString("ShopName"));
      if (this.content != null)
      {
        this.content.setEmojiText(paramDPObject.getString("Tips"));
        localObject1 = this.content;
        if (paramDPObject.getString("Tips") != null)
        {
          paramInt = i;
          if (paramDPObject.getString("Tips").length() != 0);
        }
        else
        {
          paramInt = 8;
        }
        ((EmojiTextView)localObject1).setVisibility(paramInt);
      }
      if (this.replyNum == null)
        break;
      if (paramDPObject.getInt("CommentCount") > 0)
        break label635;
      this.replyNum.setText(" + ");
      return;
      localObject1 = localDPObject.getString("Avatar");
      break label207;
      label577: paramInt = 8;
      break label281;
      label583: this.queuingInfo.setVisibility(8);
      break label366;
      label595: this.time.setText(DateUtil.format2(new Date(paramDPObject.getTime("Time"))));
      break label403;
      label621: this.time.setCompoundDrawables(null, null, null, null);
    }
    label635: if (paramDPObject.getInt("CommentCount") < 10)
    {
      this.replyNum.setText(" " + paramDPObject.getInt("CommentCount") + " ");
      return;
    }
    this.replyNum.setText(String.valueOf(paramDPObject.getInt("CommentCount")));
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

  class DeselectHandler extends Handler
  {
    DeselectHandler()
    {
    }

    public void handleMessage(Message paramMessage)
    {
      if ((paramMessage.what == 0) && ((paramMessage.obj instanceof View)))
      {
        View localView = (View)paramMessage.obj;
        localView.setSelected(false);
        if ((localView == CheckinListItem.this.name) && (CheckinListItem.this.mask != null))
          CheckinListItem.this.mask.setVisibility(4);
      }
      super.handleMessage(paramMessage);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.CheckinListItem
 * JD-Core Version:    0.6.0
 */