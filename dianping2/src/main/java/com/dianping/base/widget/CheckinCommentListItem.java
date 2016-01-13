package com.dianping.base.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.v1.R.id;
import com.dianping.widget.CheckinListItemListener;
import com.dianping.widget.emoji.EmojiTextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CheckinCommentListItem extends LinearLayout
{
  static final SimpleDateFormat FMT = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
  EmojiTextView checkin_content;
  CheckInPhotosView checkin_pics;
  TextView checkin_time;
  TextView checkin_user_name;
  int clickType = -1;
  TextView errorMsg;
  DPObject itemCheckin;
  View layCheckinContaner;
  TextView reply;

  public CheckinCommentListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public DPObject getCheckin()
  {
    return this.itemCheckin;
  }

  public int getClickType()
  {
    return this.clickType;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.checkin_user_name = ((TextView)findViewById(R.id.checkin_user_name));
    this.checkin_time = ((TextView)findViewById(R.id.checkin_time));
    this.checkin_content = ((EmojiTextView)findViewById(R.id.checkin_content));
    this.checkin_pics = ((CheckInPhotosView)findViewById(R.id.checkin_photos));
    this.reply = ((TextView)findViewById(R.id.checkin_reply));
    this.errorMsg = ((TextView)findViewById(R.id.checkin_errormsg));
    this.layCheckinContaner = findViewById(R.id.lay_checkin_container);
  }

  public void setCheckinCommentListItem(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    this.itemCheckin = paramDPObject;
    Object localObject2;
    Object localObject1;
    if (paramDPObject.getObject("User") != null)
    {
      localObject2 = this.checkin_user_name;
      if (paramDPObject.getObject("User").getString("NickName") != null)
      {
        localObject1 = paramDPObject.getObject("User").getString("NickName");
        ((TextView)localObject2).setText((CharSequence)localObject1);
      }
    }
    else
    {
      localObject2 = this.checkin_time;
      if (paramDPObject.getTime("Time") == 0L)
        break label224;
      localObject1 = FMT.format(new Date(paramDPObject.getTime("Time")));
      label91: ((TextView)localObject2).setText((CharSequence)localObject1);
      setContent(paramDPObject);
      if (this.checkin_pics != null)
      {
        localObject1 = paramDPObject.getStringArray("Images");
        localObject2 = paramDPObject.getStringArray("ThumbImages");
        if (((localObject2 == null) || (localObject2.length == 0)) && ((localObject1 == null) || (localObject1.length == 0)))
          break label230;
        this.checkin_pics.setVisibility(0);
        this.checkin_pics.setImageUrls(localObject2, localObject1);
      }
      label157: if (paramDPObject.getInt("CommentCount") == 0)
        break label242;
      this.reply.setVisibility(0);
      this.reply.setText(String.valueOf(paramDPObject.getInt("CommentCount")));
    }
    while (true)
    {
      if (paramDPObject.getInt("ID") != -1)
        break label254;
      this.checkin_time.setVisibility(8);
      this.errorMsg.setVisibility(0);
      return;
      localObject1 = "";
      break;
      label224: localObject1 = "";
      break label91;
      label230: this.checkin_pics.setVisibility(8);
      break label157;
      label242: this.reply.setVisibility(8);
    }
    label254: this.checkin_time.setVisibility(0);
    this.errorMsg.setVisibility(8);
  }

  public void setContent(DPObject paramDPObject)
  {
    this.checkin_content.setText("");
    this.checkin_content.setVisibility(8);
    if (paramDPObject == null)
      break label22;
    label22: 
    do
      return;
    while ((paramDPObject.getString("Tips") == null) || (paramDPObject.getString("Tips").length() == 0));
    this.checkin_content.setVisibility(0);
    int i = 0;
    while (true)
    {
      int i2 = i;
      if (i2 >= paramDPObject.getString("Tips").length())
        break;
      int i3 = -1;
      int i4 = 0;
      int i5 = 0;
      int k = 0;
      int j = 0;
      int m = i3;
      i = i5;
      int n = i4;
      int i1;
      if (paramDPObject.getStringArray("Keywords") != null)
      {
        i1 = 0;
        while (true)
        {
          k = j;
          m = i3;
          i = i5;
          n = i4;
          if (i1 >= paramDPObject.getStringArray("Keywords").length)
            break;
          i = j;
          if (paramDPObject.getStringArray("Keywords")[i1] != null)
          {
            i = j;
            if (!paramDPObject.getStringArray("Keywords")[i1].equals(""))
              i = j + 1;
          }
          i1 += 1;
          j = i;
        }
      }
      while (i < k)
      {
        localObject = paramDPObject.getStringArray("Keywords")[i];
        i3 = paramDPObject.getString("Tips").indexOf((String)localObject, i2);
        if (i != 0)
        {
          i1 = m;
          j = n;
          if (i3 >= 0)
          {
            i1 = m;
            j = n;
            if (i3 >= m);
          }
        }
        else
        {
          i1 = i3;
          j = i;
        }
        i += 1;
        m = i1;
        n = j;
      }
      if (m == -1)
      {
        this.checkin_content.append(this.checkin_content.stringToEmojiString(paramDPObject.getString("Tips").substring(i2, paramDPObject.getString("Tips").length())));
        return;
      }
      i = m + paramDPObject.getStringArray("Keywords")[n].length();
      if (i2 < m)
        this.checkin_content.append(this.checkin_content.stringToEmojiString(paramDPObject.getString("Tips").substring(i2, m)));
      Object localObject = new SpannableString(paramDPObject.getStringArray("Keywords")[n]);
      ((SpannableString)localObject).setSpan(new ForegroundColorSpan(Color.argb(255, 238, 119, 0)), 0, paramDPObject.getStringArray("Keywords")[n].length(), 33);
      this.checkin_content.append((CharSequence)localObject);
    }
  }

  public void setItemOnClickListener(View.OnClickListener paramOnClickListener)
  {
    this.layCheckinContaner.setOnClickListener(new View.OnClickListener(paramOnClickListener)
    {
      public void onClick(View paramView)
      {
        if (this.val$listener != null)
        {
          if (CheckinCommentListItem.this.itemCheckin.getInt("ID") <= 0)
            break label44;
          CheckinCommentListItem.this.clickType = 2;
        }
        while (true)
        {
          this.val$listener.onClick(CheckinCommentListItem.this);
          return;
          label44: if (CheckinCommentListItem.this.itemCheckin.getInt("ID") < 0)
          {
            CheckinCommentListItem.this.clickType = 3;
            continue;
          }
          CheckinCommentListItem.this.clickType = 4;
        }
      }
    });
    this.checkin_pics.setPhotoTouchListener(new CheckinListItemListener(paramOnClickListener)
    {
      public void onTouchPhoto(String[] paramArrayOfString1, String[] paramArrayOfString2, int paramInt, byte[] paramArrayOfByte)
      {
        if (this.val$listener != null)
        {
          if (CheckinCommentListItem.this.itemCheckin.getInt("ID") >= 0)
            break label109;
          CheckinCommentListItem.this.clickType = 3;
          this.val$listener.onClick(CheckinCommentListItem.this);
        }
        while (true)
        {
          paramArrayOfString2 = new ArrayList();
          int j = paramArrayOfString1.length;
          int i = 0;
          while (i < j)
          {
            String str = paramArrayOfString1[i];
            paramArrayOfString2.add(new DPObject().edit().putString("Url", str).generate());
            i += 1;
          }
          label109: if (CheckinCommentListItem.this.itemCheckin.getInt("ID") != 0)
            continue;
          CheckinCommentListItem.this.clickType = 4;
          this.val$listener.onClick(CheckinCommentListItem.this);
        }
        paramArrayOfString1 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showcheckinphoto"));
        paramArrayOfString1.putExtra("fromActivity", "CheckinBoardActivity");
        paramArrayOfString1.putExtra("shopname", CheckinCommentListItem.this.itemCheckin.getString("ShopName"));
        paramArrayOfString1.putExtra("position", paramInt);
        paramArrayOfString1.putExtra("currentbitmap", paramArrayOfByte);
        paramArrayOfString1.putParcelableArrayListExtra("pageList", paramArrayOfString2);
        paramArrayOfString1.putExtra("checkinID", CheckinCommentListItem.this.itemCheckin.getInt("ID"));
        if (CheckinCommentListItem.this.getContext() != null)
        {
          CheckinCommentListItem.this.getContext().startActivity(paramArrayOfString1);
          ((NovaActivity)CheckinCommentListItem.this.getContext()).statisticsEvent("viewcheckin5", "viewcheckin5_pic", "", 0);
        }
      }
    });
  }

  public void setUserNameOnClickListener(View.OnClickListener paramOnClickListener)
  {
    this.checkin_user_name.setOnClickListener(new View.OnClickListener(paramOnClickListener)
    {
      public void onClick(View paramView)
      {
        if (this.val$listener != null)
          if (CheckinCommentListItem.this.itemCheckin.getInt("ID") == -1)
            break label45;
        label45: for (CheckinCommentListItem.this.clickType = 0; ; CheckinCommentListItem.this.clickType = 3)
        {
          this.val$listener.onClick(CheckinCommentListItem.this);
          return;
        }
      }
    });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.CheckinCommentListItem
 * JD-Core Version:    0.6.0
 */