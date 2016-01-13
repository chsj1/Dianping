package com.dianping.shopinfo.movie.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.ExpandAnimation;
import com.dianping.base.widget.ExpandAnimation.OnExpendActionListener;
import com.dianping.base.widget.ExpandAnimation.OnExpendAnimationListener;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.HashMap;

public class MovieScheduleView extends NovaLinearLayout
  implements View.OnClickListener, ExpandAnimation.OnExpendActionListener, ExpandAnimation.OnExpendAnimationListener
{
  static final int DEFAULT_MOVIE_MAX_SHOW_NUMBER_FROM_CINEMA = 8;
  static final int DEFAULT_MOVIE_MAX_SHOW_NUMBER_FROM_SHOP = 3;
  private static final int EXPAND_STATUS_ANIMATE = 1;
  private static final int EXPAND_STATUS_NORMAL = 0;
  private HashMap<Integer, DPObject> buyButtonStatusMap = new HashMap();
  private Context context;
  private int defaultMovieMaxShowNumber = 3;
  private DPObject dpMovie;
  private ArrayList<DPObject> dpMovieSchedule;
  private DPObject dpShop;
  private int expandStatus = 0;
  private NovaLinearLayout expandView;
  private int fromWhere;
  private boolean isExpand = false;
  private LinearLayout layerMovieScheduleExpand;
  private LinearLayout layerMovieScheduleList;
  private String moreHint;

  public MovieScheduleView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MovieScheduleView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.context = paramContext;
  }

  private View createMovieShowItem(DPObject paramDPObject)
  {
    int i = paramDPObject.getInt("MovieShowStatus");
    Object localObject2 = (DPObject)this.buyButtonStatusMap.get(Integer.valueOf(i));
    Object localObject1;
    if (localObject2 == null)
      localObject1 = "选座";
    for (localObject2 = ""; ; localObject2 = ((DPObject)localObject2).getString("ToastText"))
    {
      paramDPObject = paramDPObject.edit().putString("ToastText", (String)localObject2).putString("BuyTicketButtonText", (String)localObject1).generate();
      localObject1 = (MovieShowScheduleListItemView)LayoutInflater.from(getContext()).inflate(R.layout.movie_show_schedule_list_item, this.layerMovieScheduleList, false);
      ((MovieShowScheduleListItemView)localObject1).setScheduleListItemView(paramDPObject);
      ((MovieShowScheduleListItemView)localObject1).setTag(paramDPObject);
      ((MovieShowScheduleListItemView)localObject1).setOnClickListener(this);
      return localObject1;
      localObject1 = ((DPObject)localObject2).getString("ButtonText");
    }
  }

  private void setBackground(View paramView, int paramInt)
  {
    int i = paramView.getPaddingBottom();
    int j = paramView.getPaddingTop();
    int k = paramView.getPaddingRight();
    int m = paramView.getPaddingLeft();
    paramView.setBackgroundResource(paramInt);
    paramView.setPadding(m, j, k, i);
  }

  private void setExpandAction()
  {
    if ((this.layerMovieScheduleExpand == null) || (this.expandStatus == 1))
      return;
    ExpandAnimation localExpandAnimation = new ExpandAnimation(this.layerMovieScheduleExpand, 500);
    localExpandAnimation.setOnAnimationListener(this);
    localExpandAnimation.setOnExpendActionListener(this);
    this.layerMovieScheduleExpand.startAnimation(localExpandAnimation);
  }

  private void setExpandState()
  {
    if (this.expandView == null)
      return;
    if (this.isExpand)
    {
      ((ImageView)this.expandView.findViewById(R.id.expand_arrow)).setImageResource(R.drawable.arrow_up_shop);
      ((TextView)this.expandView.findViewById(R.id.expand_hint)).setText("收起");
      return;
    }
    ((ImageView)this.expandView.findViewById(R.id.expand_arrow)).setImageResource(R.drawable.arrow_down_shop);
    ((TextView)this.expandView.findViewById(R.id.expand_hint)).setText(this.moreHint);
  }

  public void onAnimationEnd()
  {
    super.onAnimationEnd();
    this.expandStatus = 0;
    setExpandState();
  }

  public void onAnimationStart()
  {
    super.onAnimationStart();
    this.expandStatus = 1;
  }

  public void onClick(View paramView)
  {
    boolean bool = true;
    if (paramView.getTag() == "EXPAND")
      if (!this.isExpand)
      {
        if (this.fromWhere != 1)
          break label61;
        DPApplication.instance().statisticsEvent("shopinfo5", "shopinfo5_movie", "查看全部场次", 0);
      }
    label60: label61: 
    do
    {
      do
      {
        break label60;
        break label60;
        if (!this.isExpand);
        while (true)
        {
          this.isExpand = bool;
          setExpandAction();
          return;
          if (this.fromWhere != 2)
            break;
          DPApplication.instance().statisticsEvent("movie5", "movie5_cinemainfo_allmovieshow", "查看全部场次", 0);
          break;
          bool = false;
        }
        paramView = paramView.getTag();
      }
      while (!DPObjectUtils.isDPObjectof(paramView, "MovieShow"));
      Object localObject1 = (DPObject)paramView;
      Object localObject2 = ((DPObject)localObject1).getString("ThirdPartySeatUrl");
      int i = ((DPObject)localObject1).getInt("BuyTicketButtonStatus");
      Object localObject3 = ((DPObject)localObject1).getString("ToastText");
      if (!TextUtils.isEmpty((CharSequence)localObject3))
      {
        localObject3 = Toast.makeText(this.context, (CharSequence)localObject3, 0);
        ((Toast)localObject3).setGravity(16, 0, 0);
        ((Toast)localObject3).show();
      }
      if (i == 0)
      {
        if (!TextUtils.isEmpty((CharSequence)localObject2))
          break label286;
        localObject2 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://movieseat"));
        ((Intent)localObject2).putExtra("movie", this.dpMovie);
        ((Intent)localObject2).putExtra("movieshow", (Parcelable)localObject1);
        ((Intent)localObject2).putExtra("shop", this.dpShop);
        getContext().startActivity((Intent)localObject2);
        localObject1 = new Intent("com.dianping.movie.THIRD_PARTY_SEAT_URL_CLEAR");
        getContext().sendBroadcast((Intent)localObject1);
      }
      while (this.fromWhere == 1)
      {
        DPApplication.instance().statisticsEvent("shopinfo5", "shopinfo5_movie", "选座", 0);
        return;
        localObject1 = new Intent("android.intent.action.VIEW", Uri.parse("dianping://webmovieseat?url=" + (String)localObject2));
        ((Intent)localObject1).putExtra("title", this.dpShop.getString("Name"));
        getContext().startActivity((Intent)localObject1);
      }
    }
    while (this.fromWhere != 2);
    label286: DPApplication.instance().statisticsEvent("movie5", "movie5_cinemainfo_buyticket", "" + ((DPObject)paramView).getInt("ID"), 0);
  }

  public void onExpendAction(View paramView)
  {
  }

  public void setMovieSchedule(ArrayList<DPObject> paramArrayList, DPObject paramDPObject1, DPObject paramDPObject2, int paramInt, HashMap<Integer, DPObject> paramHashMap)
  {
    this.buyButtonStatusMap.clear();
    this.buyButtonStatusMap.putAll(paramHashMap);
    this.dpMovieSchedule = paramArrayList;
    this.dpShop = paramDPObject1;
    this.dpMovie = paramDPObject2;
    this.fromWhere = paramInt;
    if (paramInt == 1)
      this.defaultMovieMaxShowNumber = 3;
    while (true)
    {
      this.layerMovieScheduleList = new LinearLayout(getContext());
      this.layerMovieScheduleList.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
      this.layerMovieScheduleList.setOrientation(1);
      paramInt = 0;
      while ((paramInt < this.dpMovieSchedule.size()) && (paramInt < this.defaultMovieMaxShowNumber))
      {
        this.layerMovieScheduleList.addView(createMovieShowItem((DPObject)this.dpMovieSchedule.get(paramInt)));
        paramInt += 1;
      }
      if (paramInt != 2)
        continue;
      this.defaultMovieMaxShowNumber = 8;
    }
    if (this.dpMovieSchedule.size() > this.defaultMovieMaxShowNumber)
    {
      this.layerMovieScheduleExpand = new LinearLayout(getContext());
      this.layerMovieScheduleExpand.setOrientation(1);
      paramArrayList = new LinearLayout.LayoutParams(-1, -2);
      if (!this.isExpand)
      {
        paramArrayList.bottomMargin = (int)(-getResources().getDimension(R.dimen.movie_schedule_item_height) * (this.dpMovieSchedule.size() - this.defaultMovieMaxShowNumber));
        this.layerMovieScheduleExpand.setVisibility(8);
      }
      this.layerMovieScheduleExpand.setLayoutParams(paramArrayList);
      paramInt = this.defaultMovieMaxShowNumber;
      while (paramInt < this.dpMovieSchedule.size())
      {
        this.layerMovieScheduleExpand.addView(createMovieShowItem((DPObject)this.dpMovieSchedule.get(paramInt)));
        paramInt += 1;
      }
      this.layerMovieScheduleList.addView(this.layerMovieScheduleExpand);
      this.expandView = ((NovaLinearLayout)LayoutInflater.from(getContext()).inflate(R.layout.movie_expand_view, this.layerMovieScheduleList, false));
      this.expandView.setTag("EXPAND");
      this.moreHint = ("查看全部" + this.dpMovieSchedule.size() + "个场次");
      ((TextView)this.expandView.findViewById(R.id.expand_hint)).setText(this.moreHint);
      setBackground(this.expandView, R.drawable.cell_item_white);
      this.expandView.setClickable(true);
      this.expandView.setOnClickListener(this);
      this.expandView.setGAString("allmovieshow");
      this.layerMovieScheduleList.addView(this.expandView);
      setExpandState();
    }
    addView(this.layerMovieScheduleList);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.movie.view.MovieScheduleView
 * JD-Core Version:    0.6.0
 */