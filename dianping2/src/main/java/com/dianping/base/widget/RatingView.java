package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class RatingView extends LinearLayout
{
  RatingBar.OnRatingChangedListener listener;
  private RatingBar ratingBar;
  private TextView ratingLabel;
  String[] stars;
  TextView textView;

  public RatingView(Context paramContext)
  {
    this(paramContext, null);
  }

  public RatingView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    inflate(paramContext, R.layout.rating_view, this);
    initializeRes();
  }

  private void initializeRes()
  {
    this.ratingBar = ((RatingBar)findViewById(R.id.ratingbar));
    this.textView = ((TextView)findViewById(R.id.rating_text));
    this.ratingLabel = ((TextView)findViewById(R.id.rating_label));
    this.ratingBar.setOnRatingChangedListener(new RatingBar.OnRatingChangedListener()
    {
      public void afterRatingChanged(int paramInt)
      {
        if (RatingView.this.listener != null)
          RatingView.this.listener.afterRatingChanged(paramInt);
      }

      public void onRatingChanged(int paramInt)
      {
        if (RatingView.this.listener != null)
        {
          RatingView.this.textView.setText(RatingView.this.stars[(paramInt / 10)]);
          RatingView.this.listener.onRatingChanged(paramInt);
        }
      }
    });
  }

  public void setLabel(String paramString)
  {
    this.ratingLabel.setText(paramString);
  }

  public void setOnRatingChangedListener(RatingBar.OnRatingChangedListener paramOnRatingChangedListener)
  {
    this.listener = paramOnRatingChangedListener;
  }

  public void setStar(int paramInt)
  {
    this.ratingBar.setStar(paramInt);
    this.textView.setText(this.stars[(paramInt / 10)]);
  }

  public void setStarText(String[] paramArrayOfString)
  {
    int j = paramArrayOfString.length;
    this.stars = new String[j + 1];
    this.stars[0] = "";
    int i = 0;
    while (i < j)
    {
      String str = paramArrayOfString[i];
      this.stars[(i + 1)] = str.substring(0, str.length());
      i += 1;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.RatingView
 * JD-Core Version:    0.6.0
 */