package com.dianping.shopinfo.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaButton;
import com.dianping.widget.view.NovaLinearLayout;

public class FriendRelatedCell extends NovaLinearLayout
{
  private NovaButton findFriendsButton;
  private TextView ignoreTextView;
  private Context mContext;
  private TextView mainTitle;
  private TextView subTitle;

  public FriendRelatedCell(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
  }

  public void setInfo(DPObject paramDPObject)
  {
    this.mainTitle = ((TextView)findViewById(R.id.main_title_tv));
    this.subTitle = ((TextView)findViewById(R.id.sub_title_tv));
    this.findFriendsButton = ((NovaButton)findViewById(R.id.find_friends_btn));
    this.ignoreTextView = ((TextView)findViewById(R.id.ignore_tv));
    this.mainTitle.setText(paramDPObject.getString("Title"));
    this.subTitle.setText(paramDPObject.getString("SubTitle"));
    this.findFriendsButton.setText(paramDPObject.getString("SchemeTitle"));
    this.findFriendsButton.setGAString("FriendsSearch");
    this.findFriendsButton.setOnClickListener(new View.OnClickListener(paramDPObject)
    {
      public void onClick(View paramView)
      {
        try
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$friendsInfo.getString("Scheme")));
          FriendRelatedCell.this.mContext.startActivity(paramView);
          return;
        }
        catch (Exception paramView)
        {
          paramView.printStackTrace();
        }
      }
    });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.FriendRelatedCell
 * JD-Core Version:    0.6.0
 */