package com.dianping.movie.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.Arrays;
import java.util.List;

public class MovieMainToolBarView extends NovaLinearLayout
  implements View.OnClickListener, LoginResultListener
{
  private MovieBadgeButton buttonWithBadge;
  private boolean isLocal = false;
  private List<DPObject> localCells = Arrays.asList(new DPObject[] { new DPObject().edit().putInt("ID", R.drawable.icon_movie_list).putString("Title", "影片").putString("CellData", "dianping://movielist").generate(), new DPObject().edit().putInt("ID", R.drawable.icon_cinema_list).putString("Title", "影院").putString("CellData", "dianping://cinemalist").generate(), new DPObject().edit().putInt("ID", R.drawable.icon_movie_event).putString("Title", "活动").putString("CellData", "dianping://efte?path=src/index.html&unit=unit-dianping-movie-activity").generate(), new DPObject().edit().putInt("ID", R.drawable.icon_movie_mine).putString("Title", "我的").putString("CellData", "dianping://moviemine").putInt("CellType", 1).generate() });
  private String myMovieSchema;

  public MovieMainToolBarView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MovieMainToolBarView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    LayoutInflater.from(getContext()).inflate(R.layout.moviemain_toolbar_view, this, true);
  }

  public MovieBadgeButton createToolbarItem(DPObject paramDPObject, int paramInt)
  {
    MovieBadgeButton localMovieBadgeButton = (MovieBadgeButton)LayoutInflater.from(getContext()).inflate(R.layout.movie_badge_button, this, false);
    localMovieBadgeButton.setButtonContent(paramDPObject, this.isLocal);
    localMovieBadgeButton.setGAString("menu", paramDPObject.getString("Title"), paramInt);
    return localMovieBadgeButton;
  }

  public void onClick(View paramView)
  {
    Object localObject;
    if ((paramView instanceof MovieBadgeButton))
    {
      localObject = ((MovieBadgeButton)paramView).getCell();
      if (((DPObject)localObject).getInt("CellType") != 1)
        break label142;
      ((MovieBadgeButton)paramView).updateBadge("");
      this.myMovieSchema = ((DPObject)localObject).getString("CellData");
      AccountService localAccountService = ((DPActivity)getContext()).accountService();
      if (!TextUtils.isEmpty(localAccountService.token()))
        break label112;
      localAccountService.login(this);
    }
    while (true)
    {
      ((DPActivity)getContext()).statisticsEvent("movie5", "movie5_main_icon_" + paramView.getTag(), "", 0);
      return;
      label112: localObject = new Intent("android.intent.action.VIEW", Uri.parse(((DPObject)localObject).getString("CellData")));
      getContext().startActivity((Intent)localObject);
      continue;
      label142: localObject = new Intent("android.intent.action.VIEW", Uri.parse(((DPObject)localObject).getString("CellData")));
      getContext().startActivity((Intent)localObject);
    }
  }

  public void onLoginCancel(AccountService paramAccountService)
  {
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
    paramAccountService = new Intent("android.intent.action.VIEW", Uri.parse(this.myMovieSchema));
    getContext().startActivity(paramAccountService);
  }

  public void setButtons(List<DPObject> paramList)
  {
    this.isLocal = false;
    setBackgroundResource(R.color.moviemain_toolbar_bg);
    removeAllViews();
    Object localObject;
    if (paramList != null)
    {
      localObject = paramList;
      if (paramList.size() >= 4);
    }
    else
    {
      this.isLocal = true;
      localObject = this.localCells;
    }
    paramList = ((List)localObject).subList(0, 4);
    int i = 0;
    while (i < paramList.size())
    {
      localObject = createToolbarItem((DPObject)paramList.get(i), i);
      ((MovieBadgeButton)localObject).setOnClickListener(this);
      ((MovieBadgeButton)localObject).setTag(Integer.valueOf(i));
      if (((DPObject)paramList.get(i)).getInt("CellType") == 1)
        this.buttonWithBadge = ((MovieBadgeButton)localObject);
      addView((View)localObject);
      if (i < paramList.size() - 1)
        addView(LayoutInflater.from(getContext()).inflate(R.layout.movie_vertical_dot_divider, this, false));
      i += 1;
    }
  }

  public void updateBadge(String paramString)
  {
    if (Build.VERSION.SDK_INT < 11);
    do
      return;
    while (this.buttonWithBadge == null);
    this.buttonWithBadge.updateBadge(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.view.MovieMainToolBarView
 * JD-Core Version:    0.6.0
 */