package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class ScoreView extends LinearLayout
{
  int index;
  OnRatingChangedListener listener;
  private ScoreBar scoreBar;
  private TextView scoreLabel;
  String[] scores;
  TextView textView;

  public ScoreView(Context paramContext)
  {
    this(paramContext, null);
  }

  public ScoreView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    inflate(paramContext, R.layout.score_view, this);
    initializeRes();
  }

  private void initializeRes()
  {
    this.scoreBar = ((ScoreBar)findViewById(R.id.scorebar));
    this.textView = ((TextView)findViewById(R.id.score_text));
    this.scoreLabel = ((TextView)findViewById(R.id.score_label));
    this.scoreBar.setOnRatingChangedListener(new ScoreBar.OnRatingChangedListener()
    {
      public void afterRatingChanged()
      {
        if (ScoreView.this.listener != null)
          ScoreView.this.listener.afterRatingChanged(ScoreView.this.index);
      }

      public void onRatingChanged()
      {
        if (ScoreView.this.listener != null)
        {
          ScoreView.this.textView.setText(ScoreView.this.scores[(ScoreView.this.score() / 10)]);
          ScoreView.this.listener.onRatingChanged(ScoreView.this.index);
        }
      }
    });
  }

  public int score()
  {
    return this.scoreBar.score();
  }

  public void setLabel(String paramString)
  {
    this.scoreLabel.setText(paramString);
  }

  public void setOnRatingChangedListener(int paramInt, OnRatingChangedListener paramOnRatingChangedListener)
  {
    this.listener = paramOnRatingChangedListener;
    this.index = paramInt;
  }

  public void setScore(int paramInt)
  {
    this.scoreBar.setScore((paramInt + 1) * 10);
    this.textView.setText(this.scores[(paramInt + 1)]);
  }

  public void setScoreText(String[] paramArrayOfString)
  {
    int j = paramArrayOfString.length;
    this.scores = new String[j + 1];
    this.scores[0] = "";
    int i = 0;
    while (i < j)
    {
      String str = paramArrayOfString[(j - i - 1)];
      this.scores[(i + 1)] = str.substring(0, str.length() - 3);
      i += 1;
    }
  }

  public static abstract interface OnRatingChangedListener
  {
    public abstract void afterRatingChanged(int paramInt);

    public abstract void onRatingChanged(int paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.ScoreView
 * JD-Core Version:    0.6.0
 */