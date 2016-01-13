package com.dianping.movie.view;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.util.DateUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;
import java.text.DecimalFormat;

public class MovieInfoView extends NovaLinearLayout
{
  public static int FROM_CINEMATLIST = 1;
  public static int FROM_MOVIEDETAIL = 2;
  private TextView area;
  DecimalFormat df = new DecimalFormat("#.0");
  private TextView director;
  private final int[] editionFlagMask = { 8, 4, 2, 1 };
  private TextView grade;
  private TextView gradeHint;
  private NetworkImageView iconImageView;
  private LinearLayout layerGrade;
  private LinearLayout layerShowDate;
  private TextView mainPerformer;
  private TextView minutes;
  private ImageView movieEdition;
  private TextView showDate;
  private TextView sort;

  public MovieInfoView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MovieInfoView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }

  private void init()
  {
    LayoutInflater.from(getContext()).inflate(R.layout.movie_info_view, this, true);
    this.iconImageView = ((NetworkImageView)findViewById(R.id.icon_img));
    this.movieEdition = ((ImageView)findViewById(R.id.movie_edition));
    this.layerGrade = ((LinearLayout)findViewById(R.id.layer_grade));
    this.grade = ((TextView)findViewById(R.id.grade));
    this.gradeHint = ((TextView)findViewById(R.id.grade_hint));
    this.layerShowDate = ((LinearLayout)findViewById(R.id.layer_show_date));
    this.showDate = ((TextView)findViewById(R.id.show_date));
    this.director = ((TextView)findViewById(R.id.director));
    this.mainPerformer = ((TextView)findViewById(R.id.performer));
    this.sort = ((TextView)findViewById(R.id.sort));
    this.minutes = ((TextView)findViewById(R.id.minutes));
    this.area = ((TextView)findViewById(R.id.area));
  }

  public void setMovieInfo(DPObject paramDPObject, boolean paramBoolean, int paramInt)
  {
    this.iconImageView.setImage(paramDPObject.getString("Image"));
    int m = paramDPObject.getInt("EditionFlag");
    int i;
    int j;
    if (m > 0)
    {
      int k = -1;
      i = 0;
      j = k;
      if (i < this.editionFlagMask.length)
      {
        if ((this.editionFlagMask[i] & m) <= 0)
          break label285;
        j = i;
      }
      if ((j < 0) || (j >= this.editionFlagMask.length));
    }
    label112: String str;
    switch (j)
    {
    default:
      this.movieEdition.setVisibility(0);
      if (paramBoolean)
      {
        this.layerGrade.setVisibility(0);
        str = paramDPObject.getString("Grade");
        if ((!TextUtils.isEmpty(str)) && (!"0".equals(str)))
          break;
        this.grade.setText("暂无评分");
        this.gradeHint.setVisibility(8);
      }
    case 0:
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      this.director.setText(paramDPObject.getString("Director"));
      if (paramInt == FROM_CINEMATLIST)
      {
        this.mainPerformer.setMaxLines(2);
        this.mainPerformer.setEllipsize(TextUtils.TruncateAt.END);
      }
      this.mainPerformer.setText(paramDPObject.getString("MainPerformer"));
      this.sort.setText(paramDPObject.getString("Sort"));
      this.minutes.setText(paramDPObject.getInt("Minutes") + "分钟");
      this.area.setText(paramDPObject.getString("Area"));
      return;
      label285: i += 1;
      break;
      this.movieEdition.setImageDrawable(getResources().getDrawable(R.drawable.movie_editionflag_4d));
      break label112;
      this.movieEdition.setImageDrawable(getResources().getDrawable(R.drawable.movie_editionflag_imax3d));
      break label112;
      this.movieEdition.setImageDrawable(getResources().getDrawable(R.drawable.movie_editionflag_imax));
      break label112;
      this.movieEdition.setImageDrawable(getResources().getDrawable(R.drawable.movie_editionflag_3d));
      break label112;
      this.grade.setText(str);
      continue;
      this.layerShowDate.setVisibility(0);
      this.showDate.setText(DateUtils.formatDate2TimeZone(paramDPObject.getTime("ShowDate"), "yyyy-MM-dd", "GMT+8") + "日上映");
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.view.MovieInfoView
 * JD-Core Version:    0.6.0
 */