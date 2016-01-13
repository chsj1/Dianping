package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.base.widget.CustomImageButton;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaTextView;

public class PlazaFeedCommentView extends NovaLinearLayout
  implements View.OnClickListener
{
  private CustomImageButton commentImage;
  private FeedCommentListener feedCommentListener;
  private FeedMoreButtonListener feedMoreButtonListener;
  private NovaTextView likeAndCommentNumText;
  private CustomImageButton likeImage;
  private CustomImageButton moreImage;

  public PlazaFeedCommentView(Context paramContext)
  {
    super(paramContext);
  }

  public PlazaFeedCommentView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onClick(View paramView)
  {
    if (this.feedCommentListener == null);
    int i;
    do
    {
      return;
      i = paramView.getId();
      if (i == R.id.like_comment_number_text)
      {
        this.feedCommentListener.onCommentPraiseTextClick();
        return;
      }
      if (i == R.id.do_like)
      {
        this.feedCommentListener.onPraiseClick();
        return;
      }
      if (i != R.id.do_comment)
        continue;
      this.feedCommentListener.onCommentClick();
      return;
    }
    while (i != R.id.do_more);
    if (this.feedMoreButtonListener != null)
    {
      this.feedMoreButtonListener.onMoreClick();
      return;
    }
    this.feedCommentListener.onMoreClick();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.likeAndCommentNumText = ((NovaTextView)findViewById(R.id.like_comment_number_text));
    this.likeImage = ((CustomImageButton)findViewById(R.id.do_like));
    this.commentImage = ((CustomImageButton)findViewById(R.id.do_comment));
    this.moreImage = ((CustomImageButton)findViewById(R.id.do_more));
    this.likeAndCommentNumText.setOnClickListener(this);
    this.likeImage.setOnClickListener(this);
    this.commentImage.setOnClickListener(this);
    this.moreImage.setOnClickListener(this);
  }

  public void setCommentPraise(String paramString1, String paramString2, boolean paramBoolean)
  {
    this.likeImage.gaUserInfo.biz_id = paramString1;
    this.commentImage.gaUserInfo.biz_id = paramString1;
    if (TextUtils.isEmpty(paramString2))
    {
      this.likeAndCommentNumText.setText("");
      this.likeAndCommentNumText.setVisibility(8);
    }
    while (true)
    {
      this.likeImage.setBackgroundResource(0);
      if (!paramBoolean)
        break;
      this.likeImage.setBackgroundResource(R.drawable.icon_like_pressed);
      return;
      this.likeAndCommentNumText.setText(paramString2);
      this.likeAndCommentNumText.setVisibility(0);
      this.likeAndCommentNumText.setGAString("comments_list");
      this.likeAndCommentNumText.gaUserInfo.biz_id = paramString1;
    }
    this.likeImage.setBackgroundResource(R.drawable.icon_like_normal);
  }

  public void setFeedCommentListener(FeedCommentListener paramFeedCommentListener)
  {
    this.feedCommentListener = paramFeedCommentListener;
  }

  public void setFeedMoreButtonListener(FeedMoreButtonListener paramFeedMoreButtonListener)
  {
    this.feedMoreButtonListener = paramFeedMoreButtonListener;
  }

  public static abstract interface FeedCommentListener
  {
    public abstract void onCommentClick();

    public abstract void onCommentPraiseTextClick();

    public abstract void onMoreClick();

    public abstract void onPraiseClick();
  }

  public static abstract interface FeedMoreButtonListener
  {
    public abstract void onMoreClick();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaFeedCommentView
 * JD-Core Version:    0.6.0
 */