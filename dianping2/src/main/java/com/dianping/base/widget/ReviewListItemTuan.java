package com.dianping.base.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class ReviewListItemTuan extends LinearLayout
{
  private ArrayList<DPObject> mTuanReviewList = new ArrayList();

  public ReviewListItemTuan(Context paramContext)
  {
    super(paramContext);
  }

  public ReviewListItemTuan(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void updateView()
  {
    removeAllViews();
    removeAllViews();
    int i = 0;
    if (i < this.mTuanReviewList.size())
    {
      LinearLayout localLinearLayout = (LinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.tuan_review_in_review_item, this, false);
      TextView localTextView1 = (TextView)localLinearLayout.findViewById(R.id.tuan_name);
      localTextView1.setTag(((DPObject)this.mTuanReviewList.get(i)).getString("Url"));
      localTextView1.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = (String)paramView.getTag();
          if (!TextUtils.isEmpty(paramView))
          {
            if ((ReviewListItemTuan.this.getContext() instanceof DPActivity))
              ((DPActivity)ReviewListItemTuan.this.getContext()).statisticsEvent("shopinfo5", "shopinfo5_review_deal", "", 0);
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramView));
            ReviewListItemTuan.this.getContext().startActivity(paramView);
          }
        }
      });
      TextView localTextView2 = (TextView)localLinearLayout.findViewById(R.id.tuan_tip);
      Object localObject = (TextView)localLinearLayout.findViewById(R.id.tuan_header);
      if (i == 0)
      {
        ((TextView)localObject).setVisibility(0);
        label120: localObject = ((DPObject)this.mTuanReviewList.get(i)).getString("Name");
        localTextView1.getPaint().setFlags(8);
        localTextView1.setText((CharSequence)localObject);
        if (i != this.mTuanReviewList.size() - 1)
          break label197;
        localTextView2.setVisibility(0);
      }
      while (true)
      {
        addView(localLinearLayout);
        i += 1;
        break;
        ((TextView)localObject).setVisibility(8);
        break label120;
        label197: localTextView2.setVisibility(8);
      }
    }
  }

  public void setTuanReview(ArrayList<DPObject> paramArrayList)
  {
    if ((paramArrayList != null) && (paramArrayList.size() > 0))
    {
      setVisibility(0);
      this.mTuanReviewList.clear();
      this.mTuanReviewList.addAll(paramArrayList);
      updateView();
      return;
    }
    setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ReviewListItemTuan
 * JD-Core Version:    0.6.0
 */