package com.dianping.shopinfo.movie.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.DateUtil;
import com.dianping.util.DateUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MovieShowBlockListView extends NovaLinearLayout
  implements View.OnClickListener, MovieScheduleEmptyView.NextAvailableDateListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final String DATES_REQUEST_URL = "http://app.movie.dianping.com/movieshowblockbydatesmv.bin?";
  public static final int FROM_CINEMA = 2;
  public static final int FROM_SHOP = 1;
  private static final int MOVIE_FLAG_DISCOUNT_MASK = 1;
  private final String DATE_FORMAT_MONTH_DAY = "M-d";
  private final String DATE_FORMAT_YEAR_MONTH_DAY = "yyyy-MM-dd";
  private final int DEFAULT_MAXNUMBER_MOVIE_DISCOUNT_SCHEDULE = 2;
  private final int MSG_DATE_CHANGED = 2;
  private final int MSG_MOVIE_CHANGED = 1;
  private final int MSG_SCROLL_POSTER = 4;
  private final int MSG_SPECIFY_DATE = 3;
  private final String STRING_DAYAFTERTOMORROW = "后天";
  private final String STRING_TODAY = "今天";
  private final String STRING_TOMORROW = "明天";
  private HashMap<String, LinkedHashMap<Date, ArrayList<DPObject>>> allMovieShowMap = new HashMap();
  private HashMap<Integer, DPObject> buyButtonStuatusMap = new HashMap();
  private LinearLayout contentLayer;
  private Context context;
  private Date currentDate = null;
  private int currentHSVX = 0;
  private int currentMovieIndex = 0;
  private String dates = "";
  private Date dayAfterTomorrow;
  private ArrayList<DPObject> dpMovieList = new ArrayList();
  private ArrayList<DPObject> dpMovieListWithDates = new ArrayList();
  private DPObject dpSelectedMovie = null;
  private ArrayList<DPObject> dpSelectedMovieDiscountSchedule = new ArrayList();
  private ArrayList<DPObject> dpSelectedMovieShow = new ArrayList();
  private ArrayList<Date> dpSelectedMovieShowDates = new ArrayList();
  private ArrayList<DPObject> dpSelectedMovieShowSchedule = new ArrayList();
  private DPObject dpSelectedMovieWithdates = null;
  private DPObject dpShop;
  private final int[] editionFlagMask = { 8, 4, 2, 1 };
  private int fromWhere = 1;
  private ImageView ivMovieEditionFlag;
  private LinearLayout layerMovieDiscountSchedule;
  private LinearLayout layerMoviePoster;
  private LinearLayout layerMovieProfile;
  private LinearLayout layerMovieShowSchedule;
  private LinearLayout layerMovieTips;
  private int leftHolderWidth;
  private LinearLayout loadingRetryLayer;
  private DPObject mDateResultObj;
  private DPObject mResultObj;
  private MApiService mService;
  private ArrayList<DPObject> movieDiscountNaviItemList = new ArrayList();
  private LinkedHashMap<Date, ArrayList<DPObject>> movieDiscountSchedule = new LinkedHashMap();
  private MApiRequest movieShowBlockByDatesClickRequest;
  private MApiRequest movieShowBlockByDatesRequest;
  private int movieShowCount;
  private ArrayList<DPObject> movieTips = new ArrayList();
  private Handler msgHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == 1)
      {
        paramMessage = paramMessage.getData();
        MovieShowBlockListView.access$002(MovieShowBlockListView.this, paramMessage.getInt("index"));
        if (MovieShowBlockListView.this.currentMovieIndex == 0)
          MovieShowBlockListView.this.onDefaultMovieChanged();
      }
      while (true)
      {
        return;
        MovieShowBlockListView.this.onMovieChanged();
        return;
        int j;
        int i;
        if (paramMessage.what == 2)
        {
          paramMessage = paramMessage.getData();
          MovieShowBlockListView.access$302(MovieShowBlockListView.this, (Date)paramMessage.getSerializable("date"));
          j = paramMessage.getInt("index");
          i = 0;
          if (j > 1)
            if (j >= MovieShowBlockListView.this.dpSelectedMovieShowDates.size() - 1)
              break label162;
          label162: for (i = MovieShowBlockListView.this.screenWidth / 3 * (j - 1); ; i = MovieShowBlockListView.this.screenWidth / 3 * (j - 2))
          {
            MovieShowBlockListView.this.svMovieShowDates.setSmoothScrollingEnabled(true);
            MovieShowBlockListView.this.svMovieShowDates.smoothScrollTo(i, 0);
            MovieShowBlockListView.this.onDateChanged();
            return;
          }
        }
        if (paramMessage.what == 3);
        try
        {
          TimeUnit.MILLISECONDS.sleep(100L);
          label197: paramMessage = paramMessage.getData();
          MovieShowBlockListView.access$302(MovieShowBlockListView.this, (Date)paramMessage.getSerializable("date"));
          ((RadioButton)MovieShowBlockListView.this.rgMovieShowDate.getChildAt(0)).setChecked(false);
          ((RadioButton)MovieShowBlockListView.this.rgMovieShowDate.getChildAt(paramMessage.getInt("index"))).setChecked(true);
          j = paramMessage.getInt("index");
          i = 0;
          if (j > 1)
            if (j >= MovieShowBlockListView.this.dpSelectedMovieShowDates.size() - 1)
              break label357;
          label357: for (i = MovieShowBlockListView.this.screenWidth / 3 * (j - 1); ; i = MovieShowBlockListView.this.screenWidth / 3 * (j - 2))
          {
            MovieShowBlockListView.this.svMovieShowDates.setSmoothScrollingEnabled(true);
            MovieShowBlockListView.this.svMovieShowDates.smoothScrollTo(i, 0);
            MovieShowBlockListView.this.onDateChanged();
            MovieShowBlockListView.access$902(MovieShowBlockListView.this, 0);
            MovieShowBlockListView.access$1002(MovieShowBlockListView.this, null);
            return;
          }
          if (paramMessage.what != 4)
            continue;
          paramMessage = paramMessage.getData();
          MovieShowBlockListView.this.svMoviePoster.setSmoothScrollingEnabled(true);
          MovieShowBlockListView.this.svMoviePoster.smoothScrollTo(paramMessage.getInt("scrollPosition"), 0);
          return;
        }
        catch (InterruptedException localInterruptedException)
        {
          break label197;
        }
      }
    }
  };
  private int posterItemWidth;
  private RadioGroup rgMovieShowDate;
  private int screenWidth;
  private int selectedMovieIdInitial;
  private String shopId = "";
  private Date specifiedDate;
  private HorizontalScrollView svMoviePoster;
  private HorizontalScrollView svMovieShowDates;
  private Date today;
  private boolean todayHasMovieShow = false;
  private String token;
  private Date tomorrow;
  private TextView tvMovieCount;
  private TextView tvMovieMinutes;
  private TextView tvMovieName;
  private TextView tvMovieScore;

  public MovieShowBlockListView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MovieShowBlockListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.context = paramContext;
  }

  private ProgressBar getProgressBar()
  {
    ProgressBar localProgressBar = new ProgressBar(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
    int i = ViewUtils.dip2px(getContext(), 18.0F);
    localLayoutParams.bottomMargin = i;
    localLayoutParams.topMargin = i;
    localLayoutParams.gravity = 1;
    localProgressBar.setLayoutParams(localLayoutParams);
    return localProgressBar;
  }

  private String getWeeday(Date paramDate)
  {
    Calendar localCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
    localCalendar.setTime(paramDate);
    switch (localCalendar.get(7))
    {
    default:
      return "";
    case 1:
      return "周日";
    case 2:
      return "周一";
    case 3:
      return "周二";
    case 4:
      return "周三";
    case 5:
      return "周四";
    case 6:
      return "周五";
    case 7:
    }
    return "周六";
  }

  private void onDateChanged()
  {
    setMovieTips();
    setMovieDiscountSchedule();
    setMovieShowSchedule();
  }

  private void onDefaultMovieChanged()
  {
    updateMovieProfile();
    prepareMovieShowSchedule();
    prepareMovieDiscountSchedule();
    setMovieSchedule();
  }

  private void onMovieChanged()
  {
    updateMovieProfile();
    prepareMovieDiscountSchedule();
    int i = this.dpSelectedMovie.getInt("ID");
    if ((LinkedHashMap)this.allMovieShowMap.get(String.valueOf(i)) == null)
    {
      long[] arrayOfLong = this.dpSelectedMovieWithdates.getTimeArray("DateList");
      this.dates = "";
      if ((arrayOfLong != null) && (arrayOfLong.length > 0))
      {
        if (arrayOfLong.length < 3)
        {
          i = 0;
          if (i < arrayOfLong.length)
          {
            if (arrayOfLong.length - 1 == i)
              this.dates += DateUtils.formatDate2TimeZone(arrayOfLong[i], "yyyy-MM-dd", "GMT+8");
            while (true)
            {
              i += 1;
              break;
              this.dates = (this.dates + DateUtils.formatDate2TimeZone(arrayOfLong[i], "yyyy-MM-dd", "GMT+8") + ",");
            }
          }
        }
        else
        {
          i = 0;
          if (i < 3)
          {
            if (2 == i)
              this.dates += DateUtils.formatDate2TimeZone(arrayOfLong[i], "yyyy-MM-dd", "GMT+8");
            while (true)
            {
              i += 1;
              break;
              this.dates = (this.dates + DateUtils.formatDate2TimeZone(arrayOfLong[i], "yyyy-MM-dd", "GMT+8") + ",");
            }
          }
        }
        this.layerMovieShowSchedule.removeAllViews();
        this.layerMovieShowSchedule.addView(getProgressBar());
        requestMovieShowBlockByDates(this.dates);
      }
      return;
    }
    prepareShowDates();
    setMovieSchedule();
  }

  private void prepareDateMovieShowSchedule()
  {
    if (this.mDateResultObj.getArray("MovieShowList") != null)
    {
      int i = this.dpSelectedMovie.getInt("ID");
      Object localObject2 = (LinkedHashMap)this.allMovieShowMap.get(String.valueOf(i));
      Object localObject1 = localObject2;
      if (localObject2 == null)
        localObject1 = new LinkedHashMap();
      ArrayList localArrayList2 = new ArrayList(Arrays.asList(this.mDateResultObj.getArray("MovieShowList")));
      if (localArrayList2.size() > 0)
      {
        i = 0;
        if (i < localArrayList2.size())
        {
          DPObject localDPObject = (DPObject)localArrayList2.get(i);
          if (localDPObject == null);
          while (true)
          {
            i += 1;
            break;
            localObject2 = new Date(localDPObject.getTime("ShowTime"));
            Date localDate = new Date(((Date)localObject2).getYear(), ((Date)localObject2).getMonth(), ((Date)localObject2).getDate());
            ArrayList localArrayList1 = (ArrayList)((LinkedHashMap)localObject1).get(localDate);
            localObject2 = localArrayList1;
            if (localArrayList1 == null)
              localObject2 = new ArrayList();
            ((ArrayList)localObject2).add(localDPObject);
            ((LinkedHashMap)localObject1).put(localDate, localObject2);
          }
        }
      }
      else
      {
        ((LinkedHashMap)localObject1).put(this.currentDate, new ArrayList());
      }
    }
  }

  private void prepareMovieDiscountSchedule()
  {
    int i = this.dpSelectedMovie.getInt("ID");
    this.movieDiscountSchedule.clear();
    this.movieDiscountSchedule.put(this.today, new ArrayList());
    Iterator localIterator1 = this.movieDiscountNaviItemList.iterator();
    while (localIterator1.hasNext())
    {
      DPObject localDPObject = (DPObject)localIterator1.next();
      Object localObject = localDPObject.getArray("MovieDateList");
      if ((localObject == null) || (localObject.length == 0))
        continue;
      Iterator localIterator2 = new ArrayList(Arrays.asList(localObject)).iterator();
      while (localIterator2.hasNext())
      {
        localObject = (DPObject)localIterator2.next();
        if (((DPObject)localObject).getInt("MovieID") != i)
          continue;
        localObject = new Date(((DPObject)localObject).getTime("Date"));
        Date localDate = new Date(((Date)localObject).getYear(), ((Date)localObject).getMonth(), ((Date)localObject).getDate());
        ArrayList localArrayList = (ArrayList)this.movieDiscountSchedule.get(localDate);
        localObject = localArrayList;
        if (localArrayList == null)
        {
          localObject = new ArrayList();
          this.movieDiscountSchedule.put(localDate, localObject);
        }
        ((ArrayList)localObject).add(localDPObject.getObject("MovieDiscount"));
      }
    }
  }

  private void prepareMovieShowSchedule()
  {
    this.dpSelectedMovieShow.clear();
    String str;
    LinkedHashMap localLinkedHashMap;
    long l;
    Object localObject;
    int i;
    label157: DPObject localDPObject;
    if (this.mResultObj.getArray("MovieShowList") != null)
    {
      str = String.valueOf(this.dpSelectedMovie.getInt("ID"));
      if ((LinkedHashMap)this.allMovieShowMap.get(str) == null)
      {
        localLinkedHashMap = new LinkedHashMap();
        if (this.specifiedDate == null)
        {
          l = this.mResultObj.getTime("SpecifiedDate");
          if (l != 0L)
            break label202;
          localObject = null;
          this.specifiedDate = ((Date)localObject);
          if (this.specifiedDate != null)
            this.selectedMovieIdInitial = this.dpSelectedMovie.getInt("ID");
        }
        this.dpSelectedMovieShow.addAll(Arrays.asList(this.mResultObj.getArray("MovieShowList")));
        localLinkedHashMap.clear();
        localLinkedHashMap.put(this.today, new ArrayList());
        i = 0;
        if (i >= this.dpSelectedMovieShow.size())
          break label313;
        localDPObject = (DPObject)this.dpSelectedMovieShow.get(i);
        if (localDPObject != null)
          break label215;
      }
    }
    while (true)
    {
      i += 1;
      break label157;
      prepareShowDates();
      return;
      label202: localObject = new Date(l);
      break;
      label215: localObject = new Date(localDPObject.getTime("ShowTime"));
      Date localDate = new Date(((Date)localObject).getYear(), ((Date)localObject).getMonth(), ((Date)localObject).getDate());
      ArrayList localArrayList = (ArrayList)localLinkedHashMap.get(localDate);
      localObject = localArrayList;
      if (localArrayList == null)
        localObject = new ArrayList();
      ((ArrayList)localObject).add(localDPObject);
      localLinkedHashMap.put(localDate, localObject);
      if (!DateUtils.isSameDay(localDate, this.today))
        continue;
      this.todayHasMovieShow = true;
    }
    label313: this.allMovieShowMap.put(str, localLinkedHashMap);
    prepareShowDates();
  }

  private void prepareMovieTips()
  {
    if ((this.mResultObj.getArray("MovieTipList") != null) && (this.mResultObj.getArray("MovieTipList").length > 0))
      this.movieTips.addAll(Arrays.asList(this.mResultObj.getArray("MovieTipList")));
  }

  private void prepareShowDates()
  {
    long[] arrayOfLong = this.dpSelectedMovieWithdates.getTimeArray("DateList");
    this.dpSelectedMovieShowDates = new ArrayList();
    if ((arrayOfLong != null) && (arrayOfLong.length > 0))
    {
      int i = 0;
      while (i < arrayOfLong.length)
      {
        Date localDate = new Date(arrayOfLong[i]);
        localDate = new Date(localDate.getYear(), localDate.getMonth(), localDate.getDate());
        this.dpSelectedMovieShowDates.add(localDate);
        i += 1;
      }
      if (!DateUtils.isSameDay((Date)this.dpSelectedMovieShowDates.get(0), this.today))
        this.dpSelectedMovieShowDates.add(0, this.today);
    }
  }

  private void requestMovieShowBlockByDates(String paramString)
  {
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/movieshowblockbydatesmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", this.shopId);
    if (this.dpSelectedMovie.getInt("ID") > 0)
      localBuilder.appendQueryParameter("movieid", String.valueOf(this.dpSelectedMovie.getInt("ID")));
    if (this.token != null)
      localBuilder.appendQueryParameter("token", this.token);
    if (paramString != null)
      localBuilder.appendQueryParameter("dates", paramString);
    this.movieShowBlockByDatesRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    this.mService.exec(this.movieShowBlockByDatesRequest, this);
  }

  private void requestMovieShowBlockByDatesClick(String paramString)
  {
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/movieshowblockbydatesmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("shopid", this.shopId);
    if (this.dpSelectedMovie.getInt("ID") > 0)
      localBuilder.appendQueryParameter("movieid", String.valueOf(this.dpSelectedMovie.getInt("ID")));
    if (this.token != null)
      localBuilder.appendQueryParameter("token", this.token);
    if (paramString != null)
      localBuilder.appendQueryParameter("dates", paramString);
    this.movieShowBlockByDatesClickRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    this.mService.exec(this.movieShowBlockByDatesClickRequest, this);
  }

  private void resetPostLayer()
  {
    int i = 0;
    while (i < this.layerMoviePoster.getChildCount())
    {
      this.layerMoviePoster.getChildAt(i).setSelected(false);
      i += 1;
    }
  }

  private void sendDateChangeMsg(Date paramDate)
  {
    Message localMessage = Message.obtain();
    int j = this.dpSelectedMovieShowDates.indexOf(paramDate);
    int i = j;
    if (j == -1)
      i = 0;
    Bundle localBundle = new Bundle();
    localBundle.putSerializable("date", paramDate);
    localBundle.putInt("index", i);
    localMessage.setData(localBundle);
    localMessage.what = 2;
    this.msgHandler.sendMessage(localMessage);
  }

  private void sendMovieChangeMsg(int paramInt)
  {
    Message localMessage = Message.obtain();
    Bundle localBundle = new Bundle();
    localBundle.putInt("index", paramInt);
    localMessage.setData(localBundle);
    localMessage.what = 1;
    this.msgHandler.sendMessage(localMessage);
  }

  private void setDefaultMoviePoster()
  {
    resetPostLayer();
    setSelectedMoviePoster(0);
    sendMovieChangeMsg(0);
  }

  private void setDefaultMovieShowDate()
  {
    Object localObject1 = (RadioButton)this.rgMovieShowDate.getChildAt(0);
    ((RadioButton)localObject1).setChecked(true);
    sendDateChangeMsg((Date)((RadioButton)localObject1).getTag());
    if (this.selectedMovieIdInitial != 0)
    {
      Object localObject2 = null;
      int i = 0;
      localObject1 = localObject2;
      if (this.specifiedDate != null)
      {
        j = this.dpSelectedMovieShowDates.indexOf(this.specifiedDate);
        i = j;
        localObject1 = localObject2;
        if (j != -1)
        {
          localObject1 = this.specifiedDate;
          i = j;
        }
      }
      int j = i;
      localObject2 = localObject1;
      if (localObject1 == null)
      {
        j = i;
        localObject2 = localObject1;
        if (!this.todayHasMovieShow)
        {
          j = i;
          localObject2 = localObject1;
          if (this.dpSelectedMovieShowDates.size() >= 2)
          {
            localObject2 = (Date)this.dpSelectedMovieShowDates.get(1);
            j = 1;
          }
        }
      }
      if (localObject2 != null)
      {
        localObject1 = Message.obtain();
        Bundle localBundle = new Bundle();
        localBundle.putSerializable("date", (Serializable)localObject2);
        localBundle.putInt("index", j);
        ((Message)localObject1).setData(localBundle);
        ((Message)localObject1).what = 3;
        this.msgHandler.sendMessage((Message)localObject1);
      }
    }
  }

  private void setMovieDiscountSchedule()
  {
    this.layerMovieDiscountSchedule.removeAllViews();
    this.dpSelectedMovieDiscountSchedule = ((ArrayList)this.movieDiscountSchedule.get(this.currentDate));
    if ((this.dpSelectedMovieDiscountSchedule != null) && (this.dpSelectedMovieDiscountSchedule.size() > 0))
    {
      this.layerMovieDiscountSchedule.removeAllViews();
      int i = 0;
      Object localObject;
      if (i < this.dpSelectedMovieDiscountSchedule.size())
      {
        localObject = (MovieDiscountDescView)LayoutInflater.from(this.context).inflate(R.layout.movie_discount_desc_view, this.layerMovieDiscountSchedule, false);
        ((MovieDiscountDescView)localObject).setMovieDiscount((DPObject)this.dpSelectedMovieDiscountSchedule.get(i));
        ((MovieDiscountDescView)localObject).setTag(this.dpSelectedMovieDiscountSchedule.get(i));
        if (i < 2)
          ((MovieDiscountDescView)localObject).setVisibility(0);
        while (true)
        {
          this.layerMovieDiscountSchedule.addView((View)localObject);
          i += 1;
          break;
          ((MovieDiscountDescView)localObject).setVisibility(8);
        }
      }
      if (this.dpSelectedMovieDiscountSchedule.size() > 2)
      {
        localObject = LayoutInflater.from(this.context).inflate(R.layout.movie_expand_view, this.layerMovieDiscountSchedule, false);
        TextView localTextView = (TextView)((View)localObject).findViewById(R.id.expand_hint);
        ImageView localImageView = (ImageView)((View)localObject).findViewById(R.id.expand_arrow);
        localTextView.setText("查看更多" + (this.dpSelectedMovieDiscountSchedule.size() - 2) + "条优惠");
        localImageView.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down_shop));
        ((View)localObject).setTag("TOEXPAND");
        ((View)localObject).setVisibility(0);
        ((View)localObject).setOnClickListener(new View.OnClickListener(localTextView, localImageView, (View)localObject)
        {
          public void onClick(View paramView)
          {
            paramView = (String)paramView.getTag();
            int i;
            if (paramView.equals("TOEXPAND"))
            {
              i = 0;
              while (i < MovieShowBlockListView.this.layerMovieDiscountSchedule.getChildCount())
              {
                MovieShowBlockListView.this.layerMovieDiscountSchedule.getChildAt(i).setVisibility(0);
                if (i == MovieShowBlockListView.this.layerMovieDiscountSchedule.getChildCount() - 1)
                {
                  this.val$tvExpandHint.setText("收起");
                  this.val$ivExpandArrow.setImageDrawable(MovieShowBlockListView.this.getResources().getDrawable(R.drawable.arrow_up_shop));
                  this.val$expandView.setTag("EXPANDED");
                }
                i += 1;
              }
            }
            if (paramView.equals("EXPANDED"))
            {
              i = 0;
              if (i < MovieShowBlockListView.this.layerMovieDiscountSchedule.getChildCount())
              {
                paramView = MovieShowBlockListView.this.layerMovieDiscountSchedule.getChildAt(i);
                if (i < 2)
                  paramView.setVisibility(0);
                while (true)
                {
                  i += 1;
                  break;
                  if (i == MovieShowBlockListView.this.layerMovieDiscountSchedule.getChildCount() - 1)
                  {
                    this.val$tvExpandHint.setText("查看更多" + (MovieShowBlockListView.this.dpSelectedMovieDiscountSchedule.size() - 2) + "条优惠");
                    this.val$ivExpandArrow.setImageDrawable(MovieShowBlockListView.this.getResources().getDrawable(R.drawable.arrow_down_shop));
                    this.val$expandView.setTag("TOEXPAND");
                    continue;
                  }
                  paramView.setVisibility(8);
                }
              }
            }
          }
        });
        this.layerMovieDiscountSchedule.addView((View)localObject);
      }
      this.layerMovieDiscountSchedule.setVisibility(0);
      return;
    }
    this.layerMovieDiscountSchedule.setVisibility(8);
  }

  private void setMoviePoster()
  {
    this.layerMoviePoster.removeAllViews();
    if (this.dpMovieList.size() == 0)
      return;
    this.posterItemWidth = ViewUtils.dip2px(getContext(), 84.0F);
    Object localObject = new LinearLayout(getContext());
    ((LinearLayout)localObject).setBackgroundResource(17170445);
    this.leftHolderWidth = (this.screenWidth / 2 - this.posterItemWidth / 2);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(this.leftHolderWidth, -1);
    ((LinearLayout)localObject).setLayoutParams(localLayoutParams);
    this.layerMoviePoster.addView((View)localObject);
    int i = 0;
    while (i < this.movieShowCount)
    {
      localObject = (DPObject)this.dpMovieList.get(i);
      if (localObject == null)
      {
        i += 1;
        continue;
      }
      FrameLayout localFrameLayout = (FrameLayout)LayoutInflater.from(getContext()).inflate(R.layout.movie_poster_view, this.layerMoviePoster, false);
      ((NetworkImageView)localFrameLayout.findViewById(R.id.movie_poster_image)).setImage(((DPObject)localObject).getString("Image"));
      ImageView localImageView = (ImageView)localFrameLayout.findViewById(R.id.movie_label_image);
      if ((((DPObject)localObject).getInt("Flag") & 0x1) > 0)
      {
        localImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_movie_discount_small));
        localImageView.setVisibility(0);
      }
      while (true)
      {
        localFrameLayout.setTag(Integer.valueOf(i));
        localFrameLayout.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            if ((paramView instanceof FrameLayout))
            {
              paramView = (Integer)paramView.getTag();
              MovieShowBlockListView.this.switchSelectedMovie(paramView.intValue());
            }
          }
        });
        this.layerMoviePoster.addView(localFrameLayout);
        break;
        localImageView.setVisibility(8);
      }
    }
    localObject = new LinearLayout(getContext());
    ((LinearLayout)localObject).setBackgroundResource(17170445);
    ((LinearLayout)localObject).setLayoutParams(localLayoutParams);
    this.layerMoviePoster.addView((View)localObject);
    setDefaultMoviePoster();
  }

  private void setMovieSchedule()
  {
    this.rgMovieShowDate.removeAllViews();
    Date[] arrayOfDate = (Date[])(Date[])this.dpSelectedMovieShowDates.toArray(new Date[this.dpSelectedMovieShowDates.size()]);
    int i = 0;
    if (i < arrayOfDate.length)
    {
      RadioButton localRadioButton = (RadioButton)LayoutInflater.from(getContext()).inflate(R.layout.movie_date_item, this.rgMovieShowDate, false);
      String str = DateUtils.formatDate2TimeZone(arrayOfDate[i], "M-d", "GMT+8");
      Object localObject;
      if (arrayOfDate[i].equals(this.today))
        localObject = "(今天)";
      while (true)
      {
        localRadioButton.setText(str + (String)localObject);
        localRadioButton.setTag(arrayOfDate[i]);
        localRadioButton.setBackgroundResource(R.drawable.movie_date_middle_item_background);
        localObject = (LinearLayout.LayoutParams)localRadioButton.getLayoutParams();
        ((LinearLayout.LayoutParams)localObject).width = (this.screenWidth / 3);
        localRadioButton.setLayoutParams((ViewGroup.LayoutParams)localObject);
        localRadioButton.setButtonDrawable(new StateListDrawable());
        localRadioButton.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            if ((paramView instanceof RadioButton))
            {
              paramView = (Date)paramView.getTag();
              MovieShowBlockListView.this.sendDateChangeMsg(paramView);
              GAHelper.instance().contextStatisticsEvent(MovieShowBlockListView.this.context, "moviedate", "选择日期", 0, "tap");
            }
          }
        });
        localObject = (ArrayList)this.movieDiscountSchedule.get(arrayOfDate[i]);
        if ((localObject != null) && (((ArrayList)localObject).size() > 0))
        {
          localObject = ((ArrayList)localObject).iterator();
          while (((Iterator)localObject).hasNext())
          {
            if (!((DPObject)((Iterator)localObject).next()).getBoolean("MarkOnDate"))
              continue;
            localObject = getResources().getDrawable(R.drawable.hui_btn_rest);
            ((Drawable)localObject).setBounds(0, 0, ((Drawable)localObject).getIntrinsicWidth(), ((Drawable)localObject).getIntrinsicHeight());
            localRadioButton.setCompoundDrawables(null, null, (Drawable)localObject, null);
          }
        }
        this.rgMovieShowDate.addView(localRadioButton);
        i += 1;
        break;
        if (arrayOfDate[i].equals(this.tomorrow))
        {
          localObject = "(明天)";
          continue;
        }
        if (arrayOfDate[i].equals(this.dayAfterTomorrow))
        {
          localObject = "(后天)";
          continue;
        }
        localObject = "(" + getWeeday(arrayOfDate[i]) + ")";
      }
    }
    setDefaultMovieShowDate();
  }

  private void setMovieShowSchedule()
  {
    this.layerMovieShowSchedule.removeAllViews();
    int i = this.dpSelectedMovie.getInt("ID");
    Object localObject = (LinkedHashMap)this.allMovieShowMap.get(String.valueOf(i));
    if (localObject == null)
    {
      this.layerMovieShowSchedule.addView(getProgressBar());
      this.dates = DateUtils.formatDate2TimeZone(this.currentDate, "yyyy-MM-dd", "GMT+8");
      requestMovieShowBlockByDatesClick(this.dates);
      return;
    }
    this.dpSelectedMovieShowSchedule = ((ArrayList)((LinkedHashMap)localObject).get(this.currentDate));
    if ((DateUtils.isSameDay(this.currentDate, this.today)) && ((this.dpSelectedMovieShowSchedule == null) || (this.dpSelectedMovieShowSchedule.size() == 0)))
    {
      localObject = ((LinkedHashMap)localObject).keySet();
      localObject = (Date[])(Date[])((Set)localObject).toArray(new Date[((Set)localObject).size()]);
      MovieScheduleEmptyView localMovieScheduleEmptyView = (MovieScheduleEmptyView)LayoutInflater.from(getContext()).inflate(R.layout.movie_schedule_empty_view, this.layerMovieShowSchedule, false);
      localMovieScheduleEmptyView.setMovieScheduleEmpty(localObject);
      localMovieScheduleEmptyView.setOnClickTryNextDateListener(this);
      this.layerMovieShowSchedule.addView(localMovieScheduleEmptyView);
      return;
    }
    if (this.dpSelectedMovieShowSchedule == null)
    {
      this.layerMovieShowSchedule.addView(getProgressBar());
      this.dates = DateUtils.formatDate2TimeZone(this.currentDate, "yyyy-MM-dd", "GMT+8");
      requestMovieShowBlockByDatesClick(this.dates);
      return;
    }
    localObject = (MovieScheduleView)LayoutInflater.from(getContext()).inflate(R.layout.movie_schedule_view, this.layerMovieShowSchedule, false);
    ((MovieScheduleView)localObject).setMovieSchedule(this.dpSelectedMovieShowSchedule, this.dpShop, this.dpSelectedMovie, this.fromWhere, this.buyButtonStuatusMap);
    this.layerMovieShowSchedule.addView((View)localObject);
  }

  private void setMovieTips()
  {
    this.layerMovieTips.setVisibility(8);
    if ((this.movieTips == null) || (this.movieTips.size() == 0))
      return;
    int i = 0;
    label29: DPObject localDPObject;
    if (i < this.movieTips.size())
    {
      localDPObject = (DPObject)this.movieTips.get(i);
      if (localDPObject != null)
        break label63;
    }
    label63: 
    do
    {
      i += 1;
      break label29;
      break;
    }
    while ((!DateUtils.isSameDay(new Date(localDPObject.getTime("Time")), this.currentDate)) || (TextUtils.isEmpty(localDPObject.getString("Tip"))));
    ((TextView)this.layerMovieTips.findViewById(R.id.tip_content)).setText(localDPObject.getString("Tip"));
    this.layerMovieTips.setVisibility(0);
  }

  private void setSelectedMoviePoster(int paramInt)
  {
    this.dpSelectedMovie = ((DPObject)this.dpMovieList.get(paramInt));
    this.dpSelectedMovieWithdates = ((DPObject)this.dpMovieListWithDates.get(paramInt));
    this.layerMoviePoster.setTag(Integer.valueOf(this.dpSelectedMovie.getInt("ID")));
    this.layerMoviePoster.getChildAt(paramInt + 1).setSelected(true);
  }

  private void showDatesRequestLoadingRetryView(String paramString, LoadingErrorView.LoadRetry paramLoadRetry)
  {
    View localView = LayoutInflater.from(this.context).inflate(R.layout.error_item, this.loadingRetryLayer, false);
    ((TextView)localView.findViewById(16908308)).setText(paramString);
    if ((localView instanceof LoadingErrorView))
      ((LoadingErrorView)localView).setCallBack(paramLoadRetry);
    this.layerMovieShowSchedule.removeAllViews();
    this.layerMovieShowSchedule.addView(localView);
  }

  private void switchSelectedMovie(int paramInt)
  {
    int i = this.posterItemWidth;
    Message localMessage = Message.obtain();
    Bundle localBundle = new Bundle();
    localBundle.putInt("scrollPosition", paramInt * i);
    localMessage.setData(localBundle);
    localMessage.what = 4;
    this.msgHandler.sendMessage(localMessage);
    resetPostLayer();
    setSelectedMoviePoster(paramInt);
    if (this.currentMovieIndex == paramInt);
    do
    {
      return;
      sendMovieChangeMsg(paramInt);
      if (this.fromWhere != 1)
        continue;
      DPApplication.instance().statisticsEvent("shopinfo5", "shopinfo5_movie", "选择电影", 0);
      return;
    }
    while (this.fromWhere != 2);
    DPApplication.instance().statisticsEvent("movie5", "movie5_cinemainfo_movie", this.dpSelectedMovie.getInt("ID") + "|" + paramInt, 0);
  }

  private void updateMovieProfile()
  {
    this.tvMovieName.setText(this.dpSelectedMovie.getString("Name"));
    String str = this.dpSelectedMovie.getString("Grade");
    int i;
    if ((TextUtils.isEmpty(str)) || ("0".equals(str)))
    {
      this.tvMovieScore.setVisibility(8);
      this.tvMovieMinutes.setText("片长:" + this.dpSelectedMovie.getInt("Minutes") + "分钟");
      int m = this.dpSelectedMovie.getInt("EditionFlag");
      if (m <= 0)
        break label332;
      int k = -1;
      i = 0;
      label118: int j = k;
      if (i < this.editionFlagMask.length)
      {
        if ((this.editionFlagMask[i] & m) <= 0)
          break label235;
        j = i;
      }
      if ((j < 0) || (j >= this.editionFlagMask.length))
        break label322;
      switch (j)
      {
      default:
      case 0:
      case 1:
      case 2:
      case 3:
      }
    }
    while (true)
    {
      this.ivMovieEditionFlag.setVisibility(0);
      return;
      this.tvMovieScore.setVisibility(0);
      this.tvMovieScore.setText(str + "分");
      break;
      label235: i += 1;
      break label118;
      this.ivMovieEditionFlag.setImageDrawable(getResources().getDrawable(R.drawable.movie_editionflag_4d));
      continue;
      this.ivMovieEditionFlag.setImageDrawable(getResources().getDrawable(R.drawable.movie_editionflag_imax3d));
      continue;
      this.ivMovieEditionFlag.setImageDrawable(getResources().getDrawable(R.drawable.movie_editionflag_imax));
      continue;
      this.ivMovieEditionFlag.setImageDrawable(getResources().getDrawable(R.drawable.movie_editionflag_3d));
    }
    label322: this.ivMovieEditionFlag.setVisibility(8);
    return;
    label332: this.ivMovieEditionFlag.setVisibility(8);
  }

  public void abortRequest()
  {
    if (this.movieShowBlockByDatesRequest != null)
    {
      this.mService.abort(this.movieShowBlockByDatesRequest, this, true);
      this.movieShowBlockByDatesRequest = null;
    }
    if (this.movieShowBlockByDatesClickRequest != null)
    {
      this.mService.abort(this.movieShowBlockByDatesClickRequest, this, true);
      this.movieShowBlockByDatesClickRequest = null;
    }
  }

  public void initFirstMovieShowBlock(DPObject paramDPObject1, DPObject paramDPObject2, int paramInt1, Date paramDate, int paramInt2, String paramString1, String paramString2, MApiService paramMApiService)
  {
    if ((paramDPObject1.getArray("MovieTipList") != null) && (paramDPObject1.getArray("MovieTipList").length > 0))
      this.movieTips.addAll(Arrays.asList(paramDPObject1.getArray("MovieTipList")));
    if ((paramDPObject1.getArray("MovieShowList") != null) && (paramDPObject1.getArray("MovieShowList").length > 0))
      this.dpSelectedMovieShow.addAll(Arrays.asList(paramDPObject1.getArray("MovieShowList")));
    if ((paramDPObject1.getArray("DiscountNaviList") != null) && (paramDPObject1.getArray("DiscountNaviList").length > 0))
      this.movieDiscountNaviItemList.addAll(Arrays.asList(paramDPObject1.getArray("DiscountNaviList")));
    if ((paramDPObject1.getArray("MovieNaviList") != null) && (paramDPObject1.getArray("MovieNaviList").length > 0))
      this.dpMovieListWithDates.addAll(Arrays.asList(paramDPObject1.getArray("MovieNaviList")));
    DPObject[] arrayOfDPObject = paramDPObject1.getArray("BuyTicketButtonList");
    this.buyButtonStuatusMap.clear();
    if ((arrayOfDPObject != null) && (arrayOfDPObject.length > 0))
    {
      int i = 0;
      while (i < arrayOfDPObject.length)
      {
        this.buyButtonStuatusMap.put(Integer.valueOf(arrayOfDPObject[i].getInt("MovieShowStatus")), arrayOfDPObject[i]);
        i += 1;
      }
    }
    this.token = paramString1;
    this.shopId = paramString2;
    this.mService = paramMApiService;
    this.mResultObj = paramDPObject1;
    this.selectedMovieIdInitial = paramInt2;
    this.specifiedDate = paramDate;
    this.dpShop = paramDPObject2;
    this.fromWhere = paramInt1;
    this.movieShowCount = this.dpMovieListWithDates.size();
    this.dpMovieList.clear();
    paramInt1 = 0;
    while (paramInt1 < this.movieShowCount)
    {
      this.dpMovieList.add(((DPObject)this.dpMovieListWithDates.get(paramInt1)).getObject("Movie"));
      paramInt1 += 1;
    }
    this.tvMovieCount.setText(" (上映" + this.movieShowCount + "部)");
    setMoviePoster();
    this.contentLayer.setVisibility(0);
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.movieinfo)
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://moviedetail?movieid=" + this.dpSelectedMovie.getInt("ID")));
      getContext().startActivity(paramView);
      if (this.fromWhere != 1)
        break label86;
      DPApplication.instance().statisticsEvent("shopinfo5", "shopinfo5_movie", "影片详情", 0);
    }
    label86: 
    do
      return;
    while (this.fromWhere != 2);
    DPApplication.instance().statisticsEvent("movie5", "movie5_cinemainfo_movieinfo", "影片详情", 0);
  }

  public void onClickNextAvailableDate()
  {
    RadioButton localRadioButton = (RadioButton)this.rgMovieShowDate.getChildAt(1);
    if (localRadioButton != null)
    {
      localRadioButton.setChecked(true);
      sendDateChangeMsg((Date)localRadioButton.getTag());
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.loadingRetryLayer = ((LinearLayout)findViewById(R.id.loading_retry_layer));
    this.loadingRetryLayer.setVisibility(8);
    this.contentLayer = ((LinearLayout)findViewById(R.id.content_layer));
    this.contentLayer.setVisibility(0);
    this.tvMovieCount = ((TextView)findViewById(R.id.movie_count));
    this.layerMoviePoster = ((LinearLayout)findViewById(R.id.layer_movieposter));
    this.svMoviePoster = ((HorizontalScrollView)findViewById(R.id.movie_poster_scroll));
    this.svMoviePoster.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        switch (paramMotionEvent.getAction())
        {
        default:
        case 1:
        }
        do
        {
          return false;
          MovieShowBlockListView.access$1202(MovieShowBlockListView.this, MovieShowBlockListView.this.svMoviePoster.getScrollX());
          j = (MovieShowBlockListView.this.currentHSVX + MovieShowBlockListView.this.posterItemWidth / 2) / MovieShowBlockListView.this.posterItemWidth;
        }
        while (MovieShowBlockListView.this.dpMovieList.size() == 0);
        int i = j;
        if (j >= MovieShowBlockListView.this.dpMovieList.size())
          i = MovieShowBlockListView.this.dpMovieList.size() - 1;
        int j = i;
        if (i < 0)
          j = 0;
        MovieShowBlockListView.this.switchSelectedMovie(j);
        return false;
      }
    });
    this.svMovieShowDates = ((HorizontalScrollView)findViewById(R.id.movie_showdates_scroll));
    this.layerMovieProfile = ((LinearLayout)findViewById(R.id.movieinfo));
    this.layerMovieProfile.setOnClickListener(this);
    this.tvMovieName = ((TextView)findViewById(R.id.movie_name));
    this.tvMovieScore = ((TextView)findViewById(R.id.movie_score));
    this.tvMovieMinutes = ((TextView)findViewById(R.id.movie_minutes));
    this.ivMovieEditionFlag = ((ImageView)findViewById(R.id.movie_editionflag));
    this.layerMovieDiscountSchedule = ((LinearLayout)findViewById(R.id.layer_moviediscountschedule));
    this.rgMovieShowDate = ((RadioGroup)findViewById(R.id.rg_movieshow_date));
    this.layerMovieShowSchedule = ((LinearLayout)findViewById(R.id.layer_movieshowschedule));
    this.layerMovieTips = ((LinearLayout)findViewById(R.id.layer_movietip));
    long l = DateUtil.currentTimeMillis();
    Date localDate1 = new Date(l);
    Date localDate2 = new Date(86400000L + l);
    Date localDate3 = new Date(172800000L + l);
    this.today = new Date(localDate1.getYear(), localDate1.getMonth(), localDate1.getDate());
    this.tomorrow = new Date(localDate2.getYear(), localDate2.getMonth(), localDate2.getDate());
    this.dayAfterTomorrow = new Date(localDate3.getYear(), localDate3.getMonth(), localDate3.getDate());
    this.screenWidth = ((WindowManager)getContext().getSystemService("window")).getDefaultDisplay().getWidth();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.message();
    if (paramMApiRequest == this.movieShowBlockByDatesRequest)
    {
      Toast.makeText(this.context, paramMApiResponse.content(), 1).show();
      showDatesRequestLoadingRetryView(paramMApiResponse.content(), new LoadingErrorView.LoadRetry()
      {
        public void loadRetry(View paramView)
        {
          MovieShowBlockListView.this.layerMovieShowSchedule.removeAllViews();
          MovieShowBlockListView.this.layerMovieShowSchedule.addView(MovieShowBlockListView.this.getProgressBar());
          MovieShowBlockListView.this.requestMovieShowBlockByDates(MovieShowBlockListView.this.dates);
        }
      });
    }
    do
      return;
    while (paramMApiRequest != this.movieShowBlockByDatesClickRequest);
    Toast.makeText(this.context, paramMApiResponse.content(), 1).show();
    showDatesRequestLoadingRetryView(paramMApiResponse.content(), new LoadingErrorView.LoadRetry()
    {
      public void loadRetry(View paramView)
      {
        MovieShowBlockListView.this.layerMovieShowSchedule.removeAllViews();
        MovieShowBlockListView.this.layerMovieShowSchedule.addView(MovieShowBlockListView.this.getProgressBar());
        MovieShowBlockListView.this.requestMovieShowBlockByDatesClick(MovieShowBlockListView.this.dates);
      }
    });
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest == this.movieShowBlockByDatesRequest)
      if (DPObjectUtils.isDPObjectof(paramMApiResponse, "MovieShowBlockNew"))
      {
        this.mResultObj = ((DPObject)paramMApiResponse);
        prepareMovieShowSchedule();
        prepareMovieTips();
        setMovieSchedule();
      }
    do
      return;
    while (paramMApiRequest != this.movieShowBlockByDatesClickRequest);
    this.mDateResultObj = ((DPObject)paramMApiResponse);
    prepareDateMovieShowSchedule();
    setMovieShowSchedule();
  }

  public void showLoadingProgress()
  {
    this.loadingRetryLayer.removeAllViews();
    this.loadingRetryLayer.addView(getProgressBar());
  }

  public void showLoadingRetryView(String paramString, LoadingErrorView.LoadRetry paramLoadRetry)
  {
    View localView = LayoutInflater.from(this.context).inflate(R.layout.error_item, this.loadingRetryLayer, false);
    ((TextView)localView.findViewById(16908308)).setText(paramString);
    if ((localView instanceof LoadingErrorView))
      ((LoadingErrorView)localView).setCallBack(paramLoadRetry);
    this.loadingRetryLayer.removeAllViews();
    this.loadingRetryLayer.addView(localView);
    this.loadingRetryLayer.setVisibility(0);
    this.contentLayer.setVisibility(8);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.movie.view.MovieShowBlockListView
 * JD-Core Version:    0.6.0
 */