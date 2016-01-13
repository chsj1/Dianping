package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaTextView;
import java.util.HashMap;

public class PlazaFeedIntroView extends NovaLinearLayout
{
  private final int DEFAULT_MAX_LINE = 3;
  private final int EXPAND_SHOW_ALL = 1;
  private final int EXPAND_SHOW_PAERT = 0;
  private NovaTextView commentTextView;
  private TextView expandTextview;
  private int lineCount = 0;

  public PlazaFeedIntroView(Context paramContext)
  {
    super(paramContext);
  }

  public PlazaFeedIntroView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.commentTextView = ((NovaTextView)findViewById(R.id.comment_textview));
    this.expandTextview = ((TextView)findViewById(R.id.expand_textview));
  }

  public void setCommentToContract()
  {
    this.commentTextView.setMaxLines(3);
    this.expandTextview.setText("全文");
  }

  public void setCommentToExpand()
  {
    this.commentTextView.setMaxLines(2147483647);
    this.expandTextview.setText("收起");
  }

  public void setIntroduce(String paramString1, int paramInt, String paramString2, HashMap<Integer, Integer> paramHashMap)
  {
    if (paramHashMap == null)
      return;
    if (TextUtils.isEmpty(paramString2))
    {
      this.commentTextView.setText("");
      this.commentTextView.setVisibility(8);
      this.expandTextview.setText("");
      this.expandTextview.setVisibility(8);
      return;
    }
    this.commentTextView.setText(paramString2);
    this.commentTextView.setGAString("text_fold");
    this.commentTextView.gaUserInfo.biz_id = paramString1;
    this.commentTextView.setVisibility(0);
    this.lineCount = 0;
    this.commentTextView.setMaxLines(2147483647);
    this.expandTextview.setVisibility(8);
    if (this.commentTextView.getLineCount() > 0)
    {
      this.lineCount = this.commentTextView.getLineCount();
      if (this.lineCount > 3)
      {
        this.expandTextview.setVisibility(0);
        if ((paramHashMap.get(Integer.valueOf(paramInt)) != null) && (((Integer)paramHashMap.get(Integer.valueOf(paramInt))).intValue() == 1))
          setCommentToExpand();
      }
    }
    while (true)
    {
      this.expandTextview.setOnClickListener(new View.OnClickListener(paramHashMap, paramInt)
      {
        public void onClick(View paramView)
        {
          if ((this.val$expandMaps.get(Integer.valueOf(this.val$position)) != null) && (((Integer)this.val$expandMaps.get(Integer.valueOf(this.val$position))).intValue() == 1))
          {
            PlazaFeedIntroView.this.setCommentToContract();
            this.val$expandMaps.put(Integer.valueOf(this.val$position), Integer.valueOf(0));
            return;
          }
          PlazaFeedIntroView.this.setCommentToExpand();
          this.val$expandMaps.put(Integer.valueOf(this.val$position), Integer.valueOf(1));
        }
      });
      this.commentTextView.requestLayout();
      return;
      setCommentToContract();
      continue;
      this.expandTextview.setVisibility(8);
      continue;
      this.commentTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(paramHashMap, paramInt)
      {
        public void onGlobalLayout()
        {
          if (PlazaFeedIntroView.this.lineCount > 0)
            return;
          PlazaFeedIntroView.access$002(PlazaFeedIntroView.this, PlazaFeedIntroView.this.commentTextView.getLineCount());
          if (PlazaFeedIntroView.this.lineCount > 3)
          {
            PlazaFeedIntroView.this.expandTextview.setVisibility(0);
            PlazaFeedIntroView.this.setCommentToContract();
            this.val$expandMaps.put(Integer.valueOf(this.val$position), Integer.valueOf(0));
            return;
          }
          PlazaFeedIntroView.this.expandTextview.setVisibility(8);
        }
      });
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaFeedIntroView
 * JD-Core Version:    0.6.0
 */