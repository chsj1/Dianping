package com.dianping.movie.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public class MovieFloatBadgeButton extends NovaLinearLayout
  implements LoginResultListener
{
  private DPObject myMovieCell;
  private TextView tvBadge;
  private TextView tvTitle;

  public MovieFloatBadgeButton(Context paramContext)
  {
    super(paramContext);
  }

  public MovieFloatBadgeButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    LayoutInflater.from(getContext()).inflate(R.layout.movie_float_badge_button, this, true);
    this.tvTitle = ((TextView)findViewById(R.id.title_tv));
    this.tvBadge = ((TextView)findViewById(R.id.badge_tv));
    this.tvBadge.setVisibility(8);
  }

  public void onLoginCancel(AccountService paramAccountService)
  {
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
    paramAccountService = new Intent("android.intent.action.VIEW", Uri.parse(this.myMovieCell.getString("CellData")));
    getContext().startActivity(paramAccountService);
  }

  public void setButtonContent(DPObject paramDPObject)
  {
    this.myMovieCell = paramDPObject;
    this.tvTitle.setText(paramDPObject.getString("Title"));
    setOnClickListener(new MovieFloatBadgeButton.1(this));
  }

  public void updateBadge(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
    {
      this.tvBadge.setVisibility(8);
      return;
    }
    this.tvBadge.setText(paramString);
    this.tvBadge.setVisibility(0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.view.MovieFloatBadgeButton
 * JD-Core Version:    0.6.0
 */