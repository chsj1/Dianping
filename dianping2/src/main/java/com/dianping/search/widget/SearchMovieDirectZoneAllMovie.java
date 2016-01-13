package com.dianping.search.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;

public class SearchMovieDirectZoneAllMovie extends NovaLinearLayout
{
  private TextView mSearchMovieMoreTxt;

  public SearchMovieDirectZoneAllMovie(Context paramContext)
  {
    this(paramContext, null);
  }

  public SearchMovieDirectZoneAllMovie(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mSearchMovieMoreTxt = ((TextView)findViewById(R.id.txt_search_movie_more));
  }

  public void setSearchMoreText(String paramString)
  {
    if (this.mSearchMovieMoreTxt != null)
      this.mSearchMovieMoreTxt.setText(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.widget.SearchMovieDirectZoneAllMovie
 * JD-Core Version:    0.6.0
 */