package com.dianping.movie.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.tuan.widget.RMBLabelItem;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.CustomEditText;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.movie.util.MovieUtil;
import com.dianping.movie.view.MovieSelectSeatView;
import com.dianping.movie.view.SeatImageView;
import com.dianping.movie.view.SeatImageView.OnItemSelectListener;
import com.dianping.util.DateUtil;
import com.dianping.util.DateUtils;
import com.dianping.util.DeviceUtils;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieSeatSelectionActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final int CREATE_MOVIE_TICKET_EXPIRE_FLAG = 2;
  private static final int MOVIESHOW_EXPIRE_FLAG = 1;
  public static int maxBookingCount;
  private Button btnSelectSeat;
  private MApiRequest cancelUnPaidOrderRequest;
  private boolean createOrderAsync = false;
  private boolean createTicketAsyncCanceled = false;
  private int currentFailedQueryTimes = 1;
  private int currentQueryTimes = 1;
  private int defaultFailedQueryTimes = 3;
  private int defaultQueryTimes = 15;
  private int discountId;
  private DPObject dpMovie;
  private DPObject dpMovieShow;
  private DPObject dpMovieTicketOrder;
  private DPObject dpSeatPlan;
  private DPObject dpShop;
  private CustomEditText etTicketPhone;
  private final DecimalFormat fnum = new DecimalFormat("##0.00");
  private MApiRequest getMovieSeatPriceRequest;
  private String hallName = "";
  private boolean isOrderSubmitted = false;
  private LinearLayout loadingRetryLayer;
  private Handler mHandler;
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("com.dianping.movie.REFRESH_SEATING"))
      {
        MovieSeatSelectionActivity.access$102(MovieSeatSelectionActivity.this, null);
        MovieSeatSelectionActivity.this.setSeatView(MovieSeatSelectionActivity.this.selectSeatList);
        MovieSeatSelectionActivity.this.setTotalAmount();
        MovieSeatSelectionActivity.this.requestSeatingPlan();
      }
      if ((paramIntent.getAction().equals("com.dianping.movie.REFRESH_MOVIESHOWSCHEDULE")) && (MovieSeatSelectionActivity.this.refreshMovieSeatPrice))
        MovieSeatSelectionActivity.this.requestMovieSeatPrice();
    }
  };
  private String movieName = "";
  private MovieSelectSeatView movieSeatView;
  private MApiRequest movieShowDetailRequest;
  private int movieShowId;
  private ProgressBar progress;
  private MApiRequest queryOrderRequest;
  private Runnable queryOrderRunnable = new Runnable()
  {
    public void run()
    {
      MovieSeatSelectionActivity.this.requestQueryOrder();
    }
  };
  private boolean refreshMovieSeatPrice = true;
  private RMBLabelItem rmblabelitem;
  private SeatImageView seatImageView;
  private MApiRequest seatingPlanRequest;
  private ArrayList<DPObject> selectSeatList;
  private LinearLayout selectedSeatLayout;
  private String seqno = "";
  private String shopName = "";
  private boolean showSpecialTips = true;
  private MApiRequest submitOrderRequest;
  private MApiRequest submitOrderRequestAsync;
  private String sumMoney;
  private String thirdPartyName = "";
  private LinkedHashMap<Integer, String> totalAmountPrice = new LinkedHashMap();
  private TextView tvMovieName;
  private TextView tvMovieStatus;
  private TextView tvSelectTip;
  private TextView tvShowDate;
  private TextView tvSumMoney;
  private int unpaidOrderId;
  private boolean validateSeatAtClient = false;

  private float calcSumMoney()
  {
    if (this.selectSeatList == null);
    int i;
    do
    {
      return 0.0F;
      i = this.selectSeatList.size();
    }
    while (!this.totalAmountPrice.containsKey(Integer.valueOf(i)));
    return Float.valueOf((String)this.totalAmountPrice.get(Integer.valueOf(i))).floatValue();
  }

  private void cancelUpaidOrder()
  {
    if (this.cancelUnPaidOrderRequest != null)
      return;
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("token");
    localArrayList.add(getAccount().token());
    localArrayList.add("movieshowid");
    localArrayList.add(String.valueOf(this.movieShowId));
    this.cancelUnPaidOrderRequest = BasicMApiRequest.mapiPost("http://app.movie.dianping.com/rs/cancelunpaidordermv.bin", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.cancelUnPaidOrderRequest, this);
  }

  private boolean checkPhone()
  {
    String str = this.etTicketPhone.mEdit.getText().toString().trim();
    if (TextUtils.isEmpty(str))
    {
      this.etTicketPhone.requestFocus();
      showToast("请填写取票手机号");
      KeyboardUtils.popupKeyboard(this.etTicketPhone);
      return false;
    }
    if (!Pattern.compile("(\\d{11})").matcher(str).matches())
    {
      this.etTicketPhone.requestFocus();
      showToast("手机号格式错误");
      return false;
    }
    return true;
  }

  private String formatDate(long paramLong)
  {
    long l = DateUtil.currentTimeMillis();
    Calendar localCalendar1 = Calendar.getInstance();
    localCalendar1.setTimeInMillis(l);
    Calendar localCalendar2 = Calendar.getInstance();
    localCalendar2.setTimeInMillis(paramLong);
    if ((localCalendar1.get(1) == localCalendar2.get(1)) && (localCalendar1.get(2) == localCalendar2.get(2)))
    {
      int i = localCalendar2.get(5) - localCalendar1.get(5);
      if (i == 0)
        return "今天" + DateUtils.formatDate2TimeZone(paramLong, "MM月dd日(E) HH:mm", "GMT+8");
      if (i == 1)
        return "明天" + DateUtils.formatDate2TimeZone(paramLong, "MM月dd日(E) HH:mm", "GMT+8");
      if (i == 2)
        return "后天" + DateUtils.formatDate2TimeZone(paramLong, "MM月dd日(E) HH:mm", "GMT+8");
    }
    return DateUtils.formatDate2TimeZone(paramLong, "yyyy年MM月dd日(E) HH:mm", "GMT+8");
  }

  private void goToPayOrder()
  {
    Intent localIntent;
    int i;
    Bundle localBundle;
    int j;
    if ((this.dpMovieTicketOrder != null) || (this.unpaidOrderId > 0))
    {
      localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://moviepayorder"));
      i = 0;
      if (this.dpMovieTicketOrder == null)
        break label200;
      i = this.dpMovieTicketOrder.getInt("ID");
      localIntent.putExtra("orderid", i + "");
      localIntent.putExtra("callbackurl", "dianping://purchasemovieticketresult");
      localIntent.putExtra("callbackfailurl", "dianping://home");
      localBundle = new Bundle();
      j = 0;
      if (this.dpMovieTicketOrder == null)
        break label215;
      j = this.dpMovieTicketOrder.getInt("ShopID");
    }
    while (true)
    {
      localBundle.putInt("shopid", j);
      localBundle.putInt("orderid", i);
      localIntent.putExtra("payextra", localBundle);
      if (this.discountId > 0)
        localIntent.putExtra("moviediscountid", this.discountId);
      if (this.dpMovieTicketOrder != null)
        localIntent.putExtra("movieticketorder", this.dpMovieTicketOrder);
      startActivity(localIntent);
      return;
      label200: if (this.unpaidOrderId <= 0)
        break;
      i = this.unpaidOrderId;
      break;
      label215: if (this.dpShop == null)
        continue;
      j = this.dpShop.getInt("ID");
    }
  }

  private void requestMovieSeatPrice()
  {
    if ((this.movieShowId <= 0) || (this.dpMovieShow == null));
    do
      return;
    while (this.getMovieSeatPriceRequest != null);
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/rs/getmovieseatpricemv.bin?").buildUpon();
    localBuilder.appendQueryParameter("movieshowid", String.valueOf(this.movieShowId));
    if (isLogined())
      localBuilder.appendQueryParameter("token", accountService().token());
    if (this.discountId > 0)
      localBuilder.appendQueryParameter("discountid", String.valueOf(this.discountId));
    this.getMovieSeatPriceRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.getMovieSeatPriceRequest, this);
  }

  private void requestMovieShowDetail()
  {
    if (this.movieShowId <= 0);
    do
      return;
    while (this.movieShowDetailRequest != null);
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/movieshowdetailmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("movieshowid", String.valueOf(this.movieShowId));
    this.movieShowDetailRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.movieShowDetailRequest, this);
  }

  private void requestQueryOrder()
  {
    if (this.queryOrderRequest != null)
      return;
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("token");
    localArrayList.add(getAccount().token());
    if (this.dpMovieShow != null)
    {
      localArrayList.add("movieshowid");
      localArrayList.add(String.valueOf(this.dpMovieShow.getInt("ID")));
    }
    localArrayList.add("discountid");
    localArrayList.add(String.valueOf(this.discountId));
    localArrayList.add("presubmitprice");
    localArrayList.add(String.valueOf(calcSumMoney()));
    localArrayList.add("seqno");
    localArrayList.add(this.seqno);
    this.queryOrderRequest = BasicMApiRequest.mapiPost("http://app.movie.dianping.com/rs/querycreateorderresultmv.bin", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.queryOrderRequest, this);
  }

  private void requestSeatingPlan()
  {
    if (this.seatingPlanRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/rs/seatingplanmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("movieshowid", String.valueOf(this.movieShowId));
    this.seatingPlanRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
    mapiService().exec(this.seatingPlanRequest, this);
    this.progress.setVisibility(0);
    this.movieSeatView.setVisibility(8);
    this.loadingRetryLayer.setVisibility(8);
  }

  private void requestSubmitTicketOrder()
  {
    if (!isLogined())
      accountService().login(this);
    do
      return;
    while (this.submitOrderRequest != null);
    if ((this.selectSeatList == null) || (this.selectSeatList.size() < 0))
    {
      showToast("请您先选择座位再提交", 0);
      return;
    }
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("token");
    localArrayList.add(getAccount().token());
    if (this.dpMovieShow != null)
    {
      localArrayList.add("movieshowid");
      localArrayList.add(String.valueOf(this.dpMovieShow.getInt("ID")));
    }
    String str2 = "";
    String str1 = "";
    int i = 0;
    if (i < this.selectSeatList.size())
    {
      if (i != this.selectSeatList.size() - 1)
        str2 = str2 + ((DPObject)this.selectSeatList.get(i)).getInt("ID") + ",";
      for (str1 = str1 + ((DPObject)this.selectSeatList.get(i)).getString("Name") + ","; ; str1 = str1 + ((DPObject)this.selectSeatList.get(i)).getString("Name"))
      {
        i += 1;
        break;
        str2 = str2 + ((DPObject)this.selectSeatList.get(i)).getInt("ID");
      }
    }
    if (!TextUtils.isEmpty(str2))
    {
      localArrayList.add("seatid");
      localArrayList.add(str2);
    }
    if (!TextUtils.isEmpty(str1))
    {
      localArrayList.add("seatname");
      localArrayList.add(str1);
    }
    localArrayList.add("mobileno");
    localArrayList.add(this.etTicketPhone.mEdit.getText().toString().trim());
    localArrayList.add("callid");
    localArrayList.add(UUID.randomUUID().toString());
    localArrayList.add("cityid");
    localArrayList.add(String.valueOf(cityId()));
    localArrayList.add("discountid");
    localArrayList.add(String.valueOf(this.discountId));
    localArrayList.add("presubmitprice");
    localArrayList.add(String.valueOf(calcSumMoney()));
    localArrayList.add("cx");
    localArrayList.add(DeviceUtils.cxInfo("submitmovieorder"));
    this.submitOrderRequest = BasicMApiRequest.mapiPost("http://app.movie.dianping.com/rs/createmovieticketordermv.bin", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.submitOrderRequest, this);
    showProgressDialog("正在为您预定座位,请稍候...");
    MovieUtil.savePhoneNo(this, this.etTicketPhone.mEdit.getText().toString().trim());
  }

  private void requestSubmitTicketOrderAsync()
  {
    this.createTicketAsyncCanceled = false;
    this.currentQueryTimes = 1;
    this.currentFailedQueryTimes = 1;
    if (!isLogined())
      accountService().login(this);
    do
      return;
    while (this.submitOrderRequestAsync != null);
    if ((this.selectSeatList == null) || (this.selectSeatList.size() < 0))
    {
      showToast("请您先选择座位再提交", 0);
      return;
    }
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("token");
    localArrayList.add(getAccount().token());
    if (this.dpMovieShow != null)
    {
      localArrayList.add("movieshowid");
      localArrayList.add(String.valueOf(this.dpMovieShow.getInt("ID")));
      localArrayList.add("hallname");
      localArrayList.add(this.dpMovieShow.getString("HallName"));
      localArrayList.add("showtime");
      localArrayList.add(String.valueOf(this.dpMovieShow.getTime("ShowTime")));
    }
    if (this.dpMovie != null)
    {
      localArrayList.add("moviename");
      localArrayList.add(this.dpMovie.getString("Name"));
    }
    if (this.dpShop != null)
    {
      localArrayList.add("shopid");
      localArrayList.add(String.valueOf(this.dpShop.getInt("ID")));
    }
    String str2 = "";
    String str1 = "";
    int i = 0;
    if (i < this.selectSeatList.size())
    {
      if (i != this.selectSeatList.size() - 1)
        str2 = str2 + ((DPObject)this.selectSeatList.get(i)).getInt("ID") + ",";
      for (str1 = str1 + ((DPObject)this.selectSeatList.get(i)).getString("Name") + ","; ; str1 = str1 + ((DPObject)this.selectSeatList.get(i)).getString("Name"))
      {
        i += 1;
        break;
        str2 = str2 + ((DPObject)this.selectSeatList.get(i)).getInt("ID");
      }
    }
    if (!TextUtils.isEmpty(str2))
    {
      localArrayList.add("seatid");
      localArrayList.add(str2);
    }
    if (!TextUtils.isEmpty(str1))
    {
      localArrayList.add("seatname");
      localArrayList.add(str1);
    }
    localArrayList.add("mobileno");
    localArrayList.add(this.etTicketPhone.mEdit.getText().toString().trim());
    localArrayList.add("callid");
    localArrayList.add(UUID.randomUUID().toString());
    localArrayList.add("cityid");
    localArrayList.add(String.valueOf(cityId()));
    localArrayList.add("discountid");
    localArrayList.add(String.valueOf(this.discountId));
    localArrayList.add("presubmitprice");
    localArrayList.add(String.valueOf(calcSumMoney()));
    localArrayList.add("cx");
    localArrayList.add(DeviceUtils.cxInfo("submitmovieorder"));
    this.submitOrderRequestAsync = BasicMApiRequest.mapiPost("http://app.movie.dianping.com/rs/createmovieticketorderasyncmv.bin", (String[])localArrayList.toArray(new String[0]));
    mapiService().exec(this.submitOrderRequestAsync, this);
    showProgressDialog("正在为您预订座位,请稍候...");
    MovieUtil.savePhoneNo(this, this.etTicketPhone.mEdit.getText().toString().trim());
  }

  private void setSeatView(ArrayList<DPObject> paramArrayList)
  {
    if ((this.selectSeatList != null) && (paramArrayList.size() > 0))
    {
      this.isOrderSubmitted = false;
      this.btnSelectSeat.setEnabled(true);
      this.btnSelectSeat.setText("提交订单");
      this.selectedSeatLayout.setVisibility(0);
      this.selectedSeatLayout.removeAllViews();
      this.tvSelectTip.setVisibility(8);
      int j = R.color.review_delete_gray_color;
      String[] arrayOfString = new String[paramArrayList.size()];
      int i = 0;
      while (i < paramArrayList.size())
      {
        arrayOfString[i] = ((DPObject)paramArrayList.get(i)).getString("Name");
        i += 1;
      }
      MovieUtil.showSelectedSeats(this, this.selectedSeatLayout, arrayOfString, j, 0);
      return;
    }
    this.btnSelectSeat.setEnabled(false);
    this.btnSelectSeat.setText("请选座位");
    this.selectedSeatLayout.setVisibility(8);
    this.tvSelectTip.setVisibility(0);
  }

  private void setTotalAmount()
  {
    float f2 = calcSumMoney();
    float f1 = f2;
    if (f2 < 0.0F)
      f1 = 0.0F;
    this.sumMoney = this.fnum.format(f1);
    this.rmblabelitem.setRMBLabelValue(f1);
  }

  private void setTotalAmountAsOriginPrice()
  {
    this.totalAmountPrice.clear();
    int i = 0;
    while (i <= maxBookingCount)
    {
      double d1 = i;
      double d2 = Double.valueOf(this.dpMovieShow.getString("Price")).doubleValue();
      this.totalAmountPrice.put(Integer.valueOf(i), String.valueOf(d1 * d2));
      i += 1;
    }
  }

  private void setupMovieShowData()
  {
    if (this.dpMovieShow != null)
    {
      this.movieShowId = this.dpMovieShow.getInt("ID");
      if (this.discountId <= 0)
        this.discountId = this.dpMovieShow.getInt("DefaultDiscountID");
      this.hallName = this.dpMovieShow.getString("HallName");
      this.thirdPartyName = this.dpMovieShow.getString("ThirdPartyName");
      String str = this.dpMovieShow.getString("Language") + "/" + this.dpMovieShow.getString("Dimensional");
      this.tvMovieStatus.setText(str);
      this.tvShowDate.setText(formatDate(this.dpMovieShow.getTime("ShowTime")));
      requestMovieSeatPrice();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Object localObject;
    int i;
    if (paramBundle != null)
    {
      this.dpMovie = ((DPObject)paramBundle.getParcelable("dpMovie"));
      this.dpMovieShow = ((DPObject)paramBundle.getParcelable("dpMovieShow"));
      this.dpSeatPlan = ((DPObject)paramBundle.getParcelable("dpSeatPlan"));
      this.dpShop = ((DPObject)paramBundle.getParcelable("dpShop"));
      this.shopName = paramBundle.getString("ShopName");
      this.movieName = paramBundle.getString("movieName");
      this.movieShowId = paramBundle.getInt("movieShowId");
      this.discountId = paramBundle.getInt("discountId");
      this.isOrderSubmitted = paramBundle.getBoolean("isOrderSubmitted");
      this.dpMovieTicketOrder = ((DPObject)paramBundle.getParcelable("dpMovieTicketOrder"));
      setPageId("9040006");
      setContentView(R.layout.movie_seat_select_activity);
      this.mHandler = new Handler();
      this.rmblabelitem = ((RMBLabelItem)findViewById(R.id.sum_money));
      this.rmblabelitem.setRMBLabelValue(0.0D);
      this.seatImageView = ((SeatImageView)findViewById(R.id.seat_img));
      this.btnSelectSeat = ((Button)findViewById(R.id.commitorder));
      this.tvMovieName = ((TextView)findViewById(R.id.name));
      this.tvMovieStatus = ((TextView)findViewById(R.id.movie_status));
      this.tvShowDate = ((TextView)findViewById(R.id.show_date));
      this.selectedSeatLayout = ((LinearLayout)findViewById(R.id.selected_seat_layout));
      this.tvSelectTip = ((TextView)findViewById(R.id.select_tip));
      this.movieSeatView = ((MovieSelectSeatView)findViewById(R.id.movie_select_seat_view));
      this.progress = ((ProgressBar)findViewById(R.id.progress));
      this.etTicketPhone = ((CustomEditText)findViewById(R.id.ticket_phone));
      paramBundle = this.etTicketPhone.getLayoutParams();
      paramBundle.height = ViewUtils.dip2px(this, 54.0F);
      paramBundle.width = (ViewUtils.getScreenWidthPixels(this) - ViewUtils.dip2px(this, 130.0F));
      this.etTicketPhone.setLayoutParams(paramBundle);
      this.etTicketPhone.setPadding(ViewUtils.dip2px(this, 10.0F), 0, 0, 0);
      this.etTicketPhone.mEdit.setEnabled(true);
      paramBundle = MovieUtil.loadPhoneNo(this, accountService());
      this.etTicketPhone.mEdit.setText(paramBundle);
      localObject = this.etTicketPhone.mEdit;
      if (paramBundle != null)
        break label970;
      i = 0;
      label426: ((EditText)localObject).setSelection(i);
      this.loadingRetryLayer = ((LinearLayout)findViewById(R.id.loading_retry_layer));
      paramBundle = LayoutInflater.from(this).inflate(R.layout.error_item, this.loadingRetryLayer, false);
      if ((paramBundle instanceof LoadingErrorView))
        ((LoadingErrorView)paramBundle).setCallBack(new LoadingErrorView.LoadRetry()
        {
          public void loadRetry(View paramView)
          {
            MovieSeatSelectionActivity.this.requestSeatingPlan();
          }
        });
      this.loadingRetryLayer.addView(paramBundle);
      this.loadingRetryLayer.setVisibility(8);
      this.seatImageView.addOnItemSelectListener(new SeatImageView.OnItemSelectListener()
      {
        public void onItemSelect()
        {
          MovieSeatSelectionActivity.access$102(MovieSeatSelectionActivity.this, MovieSeatSelectionActivity.this.seatImageView.getSelectSeats());
          MovieSeatSelectionActivity.this.setSeatView(MovieSeatSelectionActivity.this.selectSeatList);
          MovieSeatSelectionActivity.this.setTotalAmount();
          GAHelper.instance().contextStatisticsEvent(MovieSeatSelectionActivity.this, "seatselect", null, 0, "tap");
        }
      });
      setTotalAmount();
      this.btnSelectSeat.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          if (MovieSeatSelectionActivity.this.checkPhone())
          {
            if (!MovieSeatSelectionActivity.this.validateSeatAtClient)
              break label91;
            if (!MovieSeatSelectionActivity.this.seatImageView.isAllowed())
              break label77;
            if (MovieSeatSelectionActivity.this.isOrderSubmitted)
              MovieSeatSelectionActivity.this.goToPayOrder();
          }
          else
          {
            return;
          }
          if (MovieSeatSelectionActivity.this.createOrderAsync)
          {
            MovieSeatSelectionActivity.this.requestSubmitTicketOrderAsync();
            return;
          }
          MovieSeatSelectionActivity.this.requestSubmitTicketOrder();
          return;
          label77: Toast.makeText(MovieSeatSelectionActivity.this, "选座时，请尽量选连在一起的座位，不要留下单个的空闲座位", 0).show();
          return;
          label91: if (MovieSeatSelectionActivity.this.isOrderSubmitted)
          {
            MovieSeatSelectionActivity.this.goToPayOrder();
            return;
          }
          if (MovieSeatSelectionActivity.this.createOrderAsync)
          {
            MovieSeatSelectionActivity.this.requestSubmitTicketOrderAsync();
            return;
          }
          MovieSeatSelectionActivity.this.requestSubmitTicketOrder();
        }
      });
      this.btnSelectSeat.setEnabled(false);
      this.btnSelectSeat.setText("请选座位");
      this.tvSelectTip.setText("一次最多选" + maxBookingCount + "个座位");
      setTitle(this.shopName);
      this.tvMovieName.setText(this.movieName);
      if (this.dpMovieShow != null)
        break label978;
      requestMovieShowDetail();
    }
    while (true)
    {
      paramBundle = new IntentFilter("com.dianping.movie.REFRESH_SEATING");
      registerReceiver(this.mReceiver, paramBundle);
      paramBundle = new IntentFilter("com.dianping.movie.REFRESH_MOVIESHOWSCHEDULE");
      registerReceiver(this.mReceiver, paramBundle);
      return;
      localObject = getIntent();
      this.dpShop = ((DPObject)((Intent)localObject).getParcelableExtra("shop"));
      this.dpMovie = ((DPObject)((Intent)localObject).getParcelableExtra("movie"));
      this.dpMovieShow = ((DPObject)((Intent)localObject).getParcelableExtra("movieshow"));
      if (this.dpShop == null)
      {
        this.shopName = "";
        label720: if (this.dpMovie != null)
          break label911;
        paramBundle = "";
        label730: this.movieName = paramBundle;
        if ((this.dpShop == null) || (this.dpMovie == null) || (this.dpMovieShow == null))
        {
          paramBundle = ((Intent)localObject).getData();
          if (paramBundle.getQueryParameter("shopname") != null)
            break label925;
          this.shopName = "";
          label777: if (paramBundle.getQueryParameter("moviename") != null)
            break label939;
          this.movieName = "";
          label793: if (paramBundle.getQueryParameter("movieshowid") != null)
            break label953;
        }
      }
      label911: label925: label939: label953: for (this.movieShowId = 0; ; this.movieShowId = Integer.parseInt(paramBundle.getQueryParameter("movieshowid")))
      {
        this.discountId = getIntParam("discountid", 0);
        this.isOrderSubmitted = false;
        break;
        this.shopName = this.dpShop.getString("Name");
        paramBundle = this.dpShop.getString("BranchName");
        if ((TextUtils.isEmpty(paramBundle)) || (this.shopName.contains(paramBundle)))
          break label720;
        this.shopName = (this.shopName + "(" + paramBundle + ")");
        break label720;
        paramBundle = this.dpMovie.getString("Name");
        break label730;
        this.shopName = paramBundle.getQueryParameter("shopname");
        break label777;
        this.movieName = paramBundle.getQueryParameter("moviename");
        break label793;
      }
      label970: i = paramBundle.length();
      break label426;
      label978: setupMovieShowData();
    }
  }

  protected void onDestroy()
  {
    if (this.movieSeatView != null)
      this.movieSeatView.recycle();
    if (this.mReceiver != null)
      unregisterReceiver(this.mReceiver);
    if (this.seatingPlanRequest != null)
    {
      mapiService().abort(this.seatingPlanRequest, this, true);
      this.seatingPlanRequest = null;
    }
    if (this.movieShowDetailRequest != null)
    {
      mapiService().abort(this.movieShowDetailRequest, this, true);
      this.movieShowDetailRequest = null;
    }
    if (this.getMovieSeatPriceRequest != null)
    {
      mapiService().abort(this.getMovieSeatPriceRequest, this, true);
      this.getMovieSeatPriceRequest = null;
    }
    if (this.submitOrderRequest != null)
    {
      mapiService().abort(this.submitOrderRequest, this, true);
      this.submitOrderRequest = null;
    }
    if (this.submitOrderRequestAsync != null)
    {
      mapiService().abort(this.submitOrderRequestAsync, this, true);
      this.submitOrderRequestAsync = null;
    }
    if (this.queryOrderRequest != null)
    {
      mapiService().abort(this.queryOrderRequest, this, true);
      this.queryOrderRequest = null;
      this.mHandler.removeCallbacks(this.queryOrderRunnable);
    }
    if (this.cancelUnPaidOrderRequest != null)
    {
      mapiService().abort(this.cancelUnPaidOrderRequest, this, true);
      this.cancelUnPaidOrderRequest = null;
    }
    super.onDestroy();
  }

  public void onLoginCancel(AccountService paramAccountService)
  {
  }

  public void onLoginSuccess(AccountService paramAccountService)
  {
    if (this.createOrderAsync)
    {
      requestSubmitTicketOrderAsync();
      return;
    }
    requestSubmitTicketOrder();
  }

  protected void onNewGAPager(GAUserInfo paramGAUserInfo)
  {
    GAUserInfo localGAUserInfo = paramGAUserInfo;
    if (paramGAUserInfo == null)
      localGAUserInfo = new GAUserInfo();
    localGAUserInfo.biz_id = String.valueOf(this.movieShowId);
    super.onNewGAPager(localGAUserInfo);
  }

  public void onProgressDialogCancel()
  {
    this.createTicketAsyncCanceled = true;
    if ((this.createOrderAsync) && (this.submitOrderRequestAsync != null))
    {
      mapiService().abort(this.submitOrderRequestAsync, this, true);
      this.submitOrderRequestAsync = null;
    }
    if ((this.createOrderAsync) && (this.queryOrderRequest != null))
    {
      mapiService().abort(this.queryOrderRequest, this, true);
      this.queryOrderRequest = null;
      this.mHandler.removeCallbacks(this.queryOrderRunnable);
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.message();
    if (paramMApiRequest == this.seatingPlanRequest)
    {
      this.progress.setVisibility(8);
      this.movieSeatView.setVisibility(0);
      this.seatingPlanRequest = null;
      if (paramMApiResponse.flag() == 1)
      {
        this.refreshMovieSeatPrice = false;
        sendBroadcast(new Intent("com.dianping.movie.REFRESH_MOVIESHOWSCHEDULE"));
        new AlertDialog.Builder(this).setTitle("温馨提示").setMessage(paramMApiResponse.content()).setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            MovieSeatSelectionActivity.this.finish();
          }
        }).show();
      }
    }
    label522: 
    do
    {
      while (true)
      {
        return;
        ((TextView)this.loadingRetryLayer.findViewById(16908308)).setText(paramMApiResponse.content());
        this.loadingRetryLayer.setVisibility(0);
        this.movieSeatView.setVisibility(8);
        this.progress.setVisibility(8);
        return;
        if (paramMApiRequest == this.movieShowDetailRequest)
        {
          this.dpMovieShow = null;
          super.showToast(paramMApiResponse.toString());
          this.movieShowDetailRequest = null;
          return;
        }
        if (paramMApiRequest == this.getMovieSeatPriceRequest)
        {
          super.showToast(paramMApiResponse.toString());
          this.getMovieSeatPriceRequest = null;
          setTotalAmountAsOriginPrice();
          requestSeatingPlan();
          return;
        }
        if (paramMApiRequest != this.queryOrderRequest)
          break label522;
        this.queryOrderRequest = null;
        if (paramMApiResponse.flag() != 2)
          break;
        dismissDialog();
        if (isFinishing())
          continue;
        new AlertDialog.Builder(this).setTitle("温馨提示").setMessage(paramMApiResponse.content()).setPositiveButton("确定", null).show();
        return;
      }
      if (paramMApiResponse.flag() == 1)
      {
        if (this.currentQueryTimes <= 5)
          if (!this.createTicketAsyncCanceled)
            this.mHandler.postDelayed(this.queryOrderRunnable, this.currentQueryTimes * 1000);
        while (true)
        {
          this.currentQueryTimes += 1;
          return;
          if (this.currentQueryTimes <= this.defaultQueryTimes)
          {
            if (this.createTicketAsyncCanceled)
              continue;
            this.mHandler.postDelayed(this.queryOrderRunnable, 5000L);
            continue;
          }
          dismissDialog();
          this.mHandler.removeCallbacks(this.queryOrderRunnable);
          if (isFinishing())
            continue;
          new AlertDialog.Builder(this).setTitle("温馨提示").setMessage(paramMApiResponse.content()).setPositiveButton("确定", null).show();
        }
      }
      if (this.currentFailedQueryTimes <= this.defaultFailedQueryTimes)
        if (!this.createTicketAsyncCanceled)
          this.mHandler.postDelayed(this.queryOrderRunnable, 1000L);
      while (true)
      {
        this.currentFailedQueryTimes += 1;
        return;
        dismissDialog();
        this.mHandler.removeCallbacks(this.queryOrderRunnable);
        if (isFinishing())
          continue;
        new AlertDialog.Builder(this).setTitle("温馨提示").setMessage(paramMApiResponse.content()).setPositiveButton("确定", null).show();
      }
      if (paramMApiRequest == this.submitOrderRequest)
      {
        dismissDialog();
        this.submitOrderRequest = null;
        if ((paramMApiResponse.flag() == 1) && (!TextUtils.isEmpty(paramMApiResponse.data())) && (TextUtils.isDigitsOnly(paramMApiResponse.data())))
        {
          this.unpaidOrderId = Integer.parseInt(paramMApiResponse.data());
          new AlertDialog.Builder(this).setTitle("温馨提示").setMessage(paramMApiResponse.content()).setPositiveButton("现在支付", new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              GAHelper.instance().contextStatisticsEvent(MovieSeatSelectionActivity.this, "paynow", null, 0, "tap");
              MovieSeatSelectionActivity.this.goToPayOrder();
            }
          }).setNegativeButton("取消订单", new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              GAHelper.instance().contextStatisticsEvent(MovieSeatSelectionActivity.this, "cancelorder", null, 0, "tap");
              MovieSeatSelectionActivity.this.cancelUpaidOrder();
            }
          }).setCancelable(false).create().show();
        }
        while (true)
        {
          this.btnSelectSeat.setEnabled(true);
          return;
          if (paramMApiResponse.flag() == 2)
          {
            this.refreshMovieSeatPrice = false;
            sendBroadcast(new Intent("com.dianping.movie.REFRESH_MOVIESHOWSCHEDULE"));
            new AlertDialog.Builder(this).setTitle("温馨提示").setMessage(paramMApiResponse.content()).setPositiveButton("确定", new DialogInterface.OnClickListener()
            {
              public void onClick(DialogInterface paramDialogInterface, int paramInt)
              {
                MovieSeatSelectionActivity.this.finish();
              }
            }).show();
            continue;
          }
          new AlertDialog.Builder(this).setTitle("温馨提示").setMessage(paramMApiResponse.content()).setPositiveButton("确定", null).show();
        }
      }
      if (paramMApiRequest != this.submitOrderRequestAsync)
        continue;
      dismissDialog();
      this.submitOrderRequestAsync = null;
      if ((paramMApiResponse.flag() == 1) && (!TextUtils.isEmpty(paramMApiResponse.data())) && (TextUtils.isDigitsOnly(paramMApiResponse.data())))
      {
        this.unpaidOrderId = Integer.parseInt(paramMApiResponse.data());
        new AlertDialog.Builder(this).setTitle("温馨提示").setMessage(paramMApiResponse.content()).setPositiveButton("现在支付", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            GAHelper.instance().contextStatisticsEvent(MovieSeatSelectionActivity.this, "paynow", null, 0, "tap");
            MovieSeatSelectionActivity.this.goToPayOrder();
          }
        }).setNegativeButton("取消订单", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            GAHelper.instance().contextStatisticsEvent(MovieSeatSelectionActivity.this, "cancelorder", null, 0, "tap");
            MovieSeatSelectionActivity.this.cancelUpaidOrder();
          }
        }).setCancelable(false).create().show();
      }
      while (true)
      {
        this.btnSelectSeat.setEnabled(true);
        return;
        if (paramMApiResponse.flag() == 2)
        {
          this.refreshMovieSeatPrice = false;
          sendBroadcast(new Intent("com.dianping.movie.REFRESH_MOVIESHOWSCHEDULE"));
          new AlertDialog.Builder(this).setTitle("温馨提示").setMessage(paramMApiResponse.content()).setPositiveButton("确定", new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              MovieSeatSelectionActivity.this.finish();
            }
          }).show();
          continue;
        }
        new AlertDialog.Builder(this).setTitle("温馨提示").setMessage(paramMApiResponse.content()).setPositiveButton("确定", null).show();
      }
    }
    while (paramMApiRequest != this.cancelUnPaidOrderRequest);
    this.cancelUnPaidOrderRequest = null;
    super.showToast(paramMApiResponse.toString());
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest == this.seatingPlanRequest)
      if (DPObjectUtils.isDPObjectof(paramMApiResponse, "SeatingPlan"))
      {
        this.dpSeatPlan = ((DPObject)paramMApiResponse);
        maxBookingCount = this.dpSeatPlan.getInt("SeatCountLimit");
        this.tvSelectTip.setText("一次最多选" + maxBookingCount + "个座位");
        this.createOrderAsync = this.dpSeatPlan.getBoolean("CreateOrderAsync");
        this.defaultQueryTimes = this.dpSeatPlan.getInt("PollingCreateOrderTimes");
        this.validateSeatAtClient = this.dpSeatPlan.getBoolean("ValidateSeatAtClient");
        this.movieSeatView.drawView(this.dpSeatPlan, this.hallName, this.thirdPartyName);
        this.progress.setVisibility(8);
        this.loadingRetryLayer.setVisibility(8);
        this.movieSeatView.setVisibility(0);
        paramMApiRequest = this.dpSeatPlan.getString("SpecialTips");
        paramMApiResponse = this.dpSeatPlan.getString("UnpaidOrderTip");
        this.unpaidOrderId = this.dpSeatPlan.getInt("OrderId");
        if ((TextUtils.isEmpty(paramMApiResponse)) || (this.isOrderSubmitted))
          break label336;
        this.showSpecialTips = false;
        if (this.unpaidOrderId != 0)
          new AlertDialog.Builder(this).setTitle("温馨提示").setMessage(paramMApiResponse).setPositiveButton("现在支付", new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              GAHelper.instance().contextStatisticsEvent(MovieSeatSelectionActivity.this, "paynow", null, 0, "tap");
              MovieSeatSelectionActivity.this.goToPayOrder();
            }
          }).setNegativeButton("取消订单", new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
              GAHelper.instance().contextStatisticsEvent(MovieSeatSelectionActivity.this, "cancelorder", null, 0, "tap");
              MovieSeatSelectionActivity.this.cancelUpaidOrder();
            }
          }).setCancelable(this.isOrderSubmitted).create().show();
      }
      else
      {
        this.seatingPlanRequest = null;
      }
    label336: 
    do
    {
      do
      {
        do
        {
          return;
          new AlertDialog.Builder(this).setTitle("温馨提示").setMessage(paramMApiResponse).setPositiveButton("确定", new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
            }
          }).create().show();
          break;
          if ((TextUtils.isEmpty(paramMApiRequest)) || (!this.showSpecialTips) || (this.isOrderSubmitted))
            break;
          new AlertDialog.Builder(this).setTitle("温馨提示").setMessage(paramMApiRequest).setPositiveButton("确定", new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
            }
          }).create().show();
          break;
          if (paramMApiRequest == this.movieShowDetailRequest)
          {
            if (DPObjectUtils.isDPObjectof(paramMApiResponse, "MovieShow"))
              this.dpMovieShow = ((DPObject)paramMApiResponse);
            setupMovieShowData();
            this.movieShowDetailRequest = null;
            return;
          }
          if (paramMApiRequest == this.getMovieSeatPriceRequest)
          {
            paramMApiResponse = (DPObject)paramMApiResponse;
            int i;
            if ((paramMApiResponse.getArray("List") != null) && (paramMApiResponse.getArray("List").length > 0))
            {
              paramMApiRequest = new ArrayList();
              paramMApiRequest.addAll(Arrays.asList(paramMApiResponse.getArray("List")));
              this.totalAmountPrice.clear();
              i = 0;
            }
            while (i < paramMApiRequest.size())
            {
              paramMApiResponse = (DPObject)paramMApiRequest.get(i);
              int j = paramMApiResponse.getInt("SeatNum");
              paramMApiResponse = paramMApiResponse.getString("TotalAmount");
              this.totalAmountPrice.put(Integer.valueOf(j), paramMApiResponse);
              i += 1;
              continue;
              setTotalAmountAsOriginPrice();
            }
            this.getMovieSeatPriceRequest = null;
            requestSeatingPlan();
            return;
          }
          if (paramMApiRequest != this.submitOrderRequest)
            break label675;
          dismissDialog();
          this.submitOrderRequest = null;
        }
        while (!DPObjectUtils.isDPObjectof(paramMApiResponse, "MovieTicketOrder"));
        this.dpMovieTicketOrder = ((DPObject)paramMApiResponse);
        if (!TextUtils.isEmpty(this.dpMovieTicketOrder.getString("ErrorMsg")))
          sendBroadcast(new Intent("com.dianping.movie.REFRESH_MOVIESHOWSCHEDULE"));
        while (true)
        {
          sendBroadcast(new Intent("com.dianping.movie.CREATE_ORDER"));
          this.isOrderSubmitted = true;
          goToPayOrder();
          return;
          sendBroadcast(new Intent("com.dianping.movie.REFRESH_SEATING"));
        }
        if (paramMApiRequest != this.queryOrderRequest)
          break label781;
        dismissDialog();
        this.queryOrderRequest = null;
      }
      while (!DPObjectUtils.isDPObjectof(paramMApiResponse, "MovieTicketOrder"));
      this.dpMovieTicketOrder = ((DPObject)paramMApiResponse);
      if (!TextUtils.isEmpty(this.dpMovieTicketOrder.getString("ErrorMsg")))
        sendBroadcast(new Intent("com.dianping.movie.REFRESH_MOVIESHOWSCHEDULE"));
      while (true)
      {
        sendBroadcast(new Intent("com.dianping.movie.CREATE_ORDER"));
        this.isOrderSubmitted = true;
        goToPayOrder();
        return;
        sendBroadcast(new Intent("com.dianping.movie.REFRESH_SEATING"));
      }
      if (paramMApiRequest != this.submitOrderRequestAsync)
        continue;
      this.submitOrderRequestAsync = null;
      if (DPObjectUtils.isDPObjectof(paramMApiResponse, "SimpleMsg"))
        this.seqno = ((DPObject)paramMApiResponse).getString("Data");
      requestQueryOrder();
      return;
    }
    while (paramMApiRequest != this.cancelUnPaidOrderRequest);
    label675: this.cancelUnPaidOrderRequest = null;
    label781: this.showSpecialTips = false;
    sendBroadcast(new Intent("com.dianping.movie.REFRESH_SEATING"));
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("dpMovie", this.dpMovie);
    paramBundle.putParcelable("dpMovieShow", this.dpMovieShow);
    paramBundle.putParcelable("dpSeatPlan", this.dpSeatPlan);
    paramBundle.putParcelable("dpShop", this.dpShop);
    paramBundle.putString("shopName", this.shopName);
    paramBundle.putString("movieName", this.movieName);
    paramBundle.putInt("movieShowId", this.movieShowId);
    paramBundle.putInt("discountId", this.discountId);
    paramBundle.putParcelable("dpMovieTicketOrder", this.dpMovieTicketOrder);
    paramBundle.putBoolean("isOrderSubmitted", this.isOrderSubmitted);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MovieSeatSelectionActivity
 * JD-Core Version:    0.6.0
 */