package com.dianping.movie.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.CircleImageView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public class MovieUserProfileView extends NovaLinearLayout
{
  private static final int GENDER_FEMALE = 2;
  private static final int GENDER_MALE = 1;
  private ImageView mGenderIcon;
  private ImageView mLocateIcon;
  private CircleImageView mUserImageView;
  private TextView mUserNameTextView;
  private TextView mUserSignTextView;
  private View mUsuallyCinemasLayer;
  private TextView mUsuallyCinemasTextView;
  private DPObject movieUser;

  public MovieUserProfileView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MovieUserProfileView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }

  private void init()
  {
    LayoutInflater.from(getContext()).inflate(R.layout.movie_user_profile, this, true);
    this.mUserImageView = ((CircleImageView)findViewById(R.id.avatar_icon));
    this.mGenderIcon = ((ImageView)findViewById(R.id.gender_icon));
    this.mLocateIcon = ((ImageView)findViewById(R.id.locate_icon));
    this.mUserNameTextView = ((TextView)findViewById(R.id.user_nick_name));
    this.mUserSignTextView = ((TextView)findViewById(R.id.user_sign));
    this.mUsuallyCinemasTextView = ((TextView)findViewById(R.id.usually_cinemas));
    this.mUsuallyCinemasLayer = findViewById(R.id.usually_cinemas_layer);
  }

  public void setMovieUser(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    this.movieUser = paramDPObject;
    setOnClickListener(new MovieUserProfileView.1(this));
    this.mUserImageView.setImage(this.movieUser.getString("Avatar120c120"));
    if (!TextUtils.isEmpty(this.movieUser.getString("Avatar1024c1024")))
      this.mUserImageView.setOnClickListener(new MovieUserProfileView.2(this));
    switch (this.movieUser.getInt("Gender"))
    {
    default:
      this.mGenderIcon.setVisibility(8);
      this.mUserNameTextView.setText(this.movieUser.getString("NickName"));
      if (TextUtils.isEmpty(this.movieUser.getString("Sign")))
        break;
      this.mUserSignTextView.setText(this.movieUser.getString("Sign"));
      this.mUserSignTextView.setVisibility(0);
    case 2:
    case 1:
    }
    while (true)
    {
      if (!TextUtils.isEmpty(this.movieUser.getString("ConsumedCinema")))
        break label243;
      this.mUsuallyCinemasLayer.setVisibility(8);
      return;
      this.mGenderIcon.setImageResource(R.drawable.female_icon);
      this.mGenderIcon.setVisibility(0);
      break;
      this.mGenderIcon.setImageResource(R.drawable.male_icon);
      this.mGenderIcon.setVisibility(0);
      break;
      this.mUserSignTextView.setVisibility(8);
    }
    label243: this.mUsuallyCinemasTextView.setText(this.movieUser.getString("ConsumedCinema"));
    this.mUsuallyCinemasLayer.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.view.MovieUserProfileView
 * JD-Core Version:    0.6.0
 */