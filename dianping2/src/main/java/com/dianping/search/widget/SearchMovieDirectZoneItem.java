package com.dianping.search.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.search.shoplist.data.model.SearchDirectZoneModel;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;

public class SearchMovieDirectZoneItem extends NovaLinearLayout
{
  public static final int MOVIEONINFOFLAG_NONE = 0;
  public static final int MOVIEONINFOFLAG_PRESALE = 2;
  public static final int MOVIEONINFOFLAG_SPECIALDISCOUNT = 1;
  public static final String RMB = "¥";
  private SearchDirectZoneModel dpMovieOnInfo;
  private View movieBuilderText;
  private TextView movieDiscountTag;
  private TextView movieHint;
  private NetworkImageView movieImage;
  private TextView movieName;
  private ImageView movieOnInfoFlagIcon;

  public SearchMovieDirectZoneItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public SearchMovieDirectZoneItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.movieImage = ((NetworkImageView)findViewById(R.id.movie_image));
    this.movieOnInfoFlagIcon = ((ImageView)findViewById(R.id.movieoninfo_flag_icon));
    this.movieDiscountTag = ((TextView)findViewById(R.id.movie_discount_tag));
    this.movieBuilderText = findViewById(R.id.movie_builder_text);
    this.movieName = ((TextView)findViewById(R.id.movie_name));
    this.movieHint = ((TextView)findViewById(R.id.movie_hint));
  }

  public void setMovieGuide(SearchDirectZoneModel paramSearchDirectZoneModel)
  {
    this.dpMovieOnInfo = paramSearchDirectZoneModel;
    if (this.dpMovieOnInfo == null)
      return;
    this.movieImage.setImage(paramSearchDirectZoneModel.picUrl);
    int i = paramSearchDirectZoneModel.getMovieLabelType();
    if (i == 2)
    {
      this.movieOnInfoFlagIcon.setImageDrawable(getResources().getDrawable(R.drawable.search_movieoninfo_flag_presale_new));
      this.movieOnInfoFlagIcon.setVisibility(0);
      paramSearchDirectZoneModel = "";
      if (!TextUtils.isEmpty(this.dpMovieOnInfo.mPrice))
        break label195;
      this.movieBuilderText.setVisibility(8);
      label84: this.movieDiscountTag.setText(paramSearchDirectZoneModel);
      this.movieName.setText(this.dpMovieOnInfo.title);
      paramSearchDirectZoneModel = this.dpMovieOnInfo.mScore;
      if ((!TextUtils.isEmpty(paramSearchDirectZoneModel)) && (!"0".equals(paramSearchDirectZoneModel)))
        break label232;
      this.movieHint.setText("");
    }
    while (true)
    {
      this.movieHint.setBackgroundResource(R.drawable.search_movie_point_bg);
      return;
      if (i == 1)
      {
        this.movieOnInfoFlagIcon.setImageDrawable(getResources().getDrawable(R.drawable.search_movie_discount_small));
        this.movieOnInfoFlagIcon.setVisibility(0);
        break;
      }
      this.movieOnInfoFlagIcon.setVisibility(8);
      break;
      label195: paramSearchDirectZoneModel = "¥" + this.dpMovieOnInfo.mPrice;
      this.movieBuilderText.setVisibility(0);
      break label84;
      label232: this.movieHint.setText(paramSearchDirectZoneModel);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.widget.SearchMovieDirectZoneItem
 * JD-Core Version:    0.6.0
 */