package com.dianping.booking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.Environment;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.booking.view.BookingOrderItem;
import com.dianping.configservice.impl.ConfigHelper;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.model.UserProfile;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.Arrays;

public class MyBookingListActivity extends NovaActivity
  implements View.OnClickListener, PullToRefreshListView.OnRefreshListener, AdapterView.OnItemClickListener
{
  private static final String BOOKING_BIND_PHONE = "com.dianping.booking.BOOKING_BIND_PHONE";
  private static final String BOOKING_COMPLAIN = "com.dianping.booking.BOOKING_COMPLAIN";
  private static final String BOOKING_DETAIL_REFRESH = "com.dianping.booking.BOOKING_DETAIL_REFRESH";
  private static final String REMOVE_ITEM = "com.dianping.orderlist.removeitem";
  private String bindTipContent;
  private ArrayList<DPObject> bookingList = new ArrayList();
  private ImageView[] creditLevelImage = new ImageView[5];
  private RelativeLayout creditLevelView;
  private TextView curPhoneNum;
  private String errorMsg;
  private NovaLinearLayout headerLayout;
  private boolean isEnd;
  private int lastCreditLevel;
  private TextView latestBindTelNo;
  private ImageView levelChange;
  private String levelTipContent;
  private View loadingView;
  private MApiRequest mGetRecordRequest;
  private IntentFilter mIntentFilter;
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (paramIntent.getAction().equals("com.dianping.orderlist.removeitem"))
      {
        j = paramIntent.getIntExtra("bookingId", 0);
        if (j > 0)
        {
          i = 0;
          if (i < MyBookingListActivity.this.bookingList.size())
          {
            if (j != ((DPObject)MyBookingListActivity.this.bookingList.get(i)).getInt("ID"))
              break label89;
            MyBookingListActivity.this.bookingList.remove(i);
          }
        }
        MyBookingListActivity.this.myBookingListAdapter.notifyDataSetChanged();
      }
      label89: 
      do
      {
        return;
        i += 1;
        break;
        if (paramIntent.getAction().equals("com.dianping.booking.BOOKING_COMPLAIN"))
        {
          paramContext = (DPObject)paramIntent.getExtras().getParcelable("bookingRecord");
          j = paramContext.getInt("ID");
          i = 0;
          while (true)
          {
            if (i < MyBookingListActivity.this.bookingList.size())
            {
              if (j == ((DPObject)MyBookingListActivity.this.bookingList.get(i)).getInt("ID"))
                MyBookingListActivity.this.bookingList.set(i, paramContext);
            }
            else
            {
              MyBookingListActivity.this.myBookingListAdapter.notifyDataSetChanged();
              return;
            }
            i += 1;
          }
        }
        if (!paramIntent.getAction().equals("com.dianping.booking.BOOKING_BIND_PHONE"))
          continue;
        MyBookingListActivity.this.myBookingListAdapter.reset();
        MyBookingListActivity.this.loadingView.setVisibility(0);
        MyBookingListActivity.this.myBookingListView.setVisibility(8);
        MyBookingListActivity.MyBookingListAdapter.access$400(MyBookingListActivity.this.myBookingListAdapter);
        return;
      }
      while (!paramIntent.getAction().equals("com.dianping.booking.BOOKING_DETAIL_REFRESH"));
      paramContext = (DPObject)paramIntent.getExtras().getParcelable("bookingRecord");
      int j = paramIntent.getExtras().getInt("replaceId");
      int i = 0;
      while (true)
      {
        if (i < MyBookingListActivity.this.bookingList.size())
        {
          if (j == ((DPObject)MyBookingListActivity.this.bookingList.get(i)).getInt("ID"))
            MyBookingListActivity.this.bookingList.set(i, paramContext);
        }
        else
        {
          MyBookingListActivity.this.myBookingListAdapter.notifyDataSetChanged();
          return;
        }
        i += 1;
      }
    }
  };
  private MyBookingListAdapter myBookingListAdapter;
  private PullToRefreshListView myBookingListView;
  private int nextStartIndex;
  private ImageView remindClose;
  private RelativeLayout remindView;
  private String tel;
  private ImageView warningClose;
  private TextView warningContent;
  private RelativeLayout warningView;

  private void addHeader()
  {
    this.headerLayout = ((NovaLinearLayout)LayoutInflater.from(this).inflate(R.layout.my_order_list_tips, null, false));
    this.remindView = ((RelativeLayout)this.headerLayout.findViewById(R.id.remind_view));
    this.remindClose = ((ImageView)this.headerLayout.findViewById(R.id.remind_close));
    this.remindClose.setOnClickListener(this);
    this.warningView = ((RelativeLayout)this.headerLayout.findViewById(R.id.warning_view));
    this.warningContent = ((TextView)this.headerLayout.findViewById(R.id.warning_content));
    this.warningClose = ((ImageView)this.headerLayout.findViewById(R.id.warning_close));
    this.warningClose.setOnClickListener(this);
    this.creditLevelView = ((RelativeLayout)this.headerLayout.findViewById(R.id.credit_level_view));
    this.creditLevelImage[0] = ((ImageView)this.headerLayout.findViewById(R.id.level_image_1));
    this.creditLevelImage[1] = ((ImageView)this.headerLayout.findViewById(R.id.level_image_2));
    this.creditLevelImage[2] = ((ImageView)this.headerLayout.findViewById(R.id.level_image_3));
    this.creditLevelImage[3] = ((ImageView)this.headerLayout.findViewById(R.id.level_image_4));
    this.creditLevelImage[4] = ((ImageView)this.headerLayout.findViewById(R.id.level_image_5));
    this.levelChange = ((ImageView)this.headerLayout.findViewById(R.id.level_change));
    this.latestBindTelNo = ((TextView)this.headerLayout.findViewById(R.id.latest_bind_phoneno));
    this.creditLevelView.setOnClickListener(this);
    if (ConfigHelper.enableYY)
      this.myBookingListView.addHeaderView(this.headerLayout);
  }

  private void appendOrders(DPObject paramDPObject)
  {
    DPObject[] arrayOfDPObject = paramDPObject.getArray("List");
    int i = paramDPObject.getInt("StartIndex");
    this.isEnd = paramDPObject.getBoolean("IsEnd");
    this.tel = paramDPObject.getString("Tel");
    this.levelTipContent = paramDPObject.getString("LevelTip");
    this.bindTipContent = paramDPObject.getString("BindTip");
    DPObject localDPObject = paramDPObject.getObject("CreditInfo");
    this.lastCreditLevel = localDPObject.getInt("LastCreditLevel");
    if (((arrayOfDPObject == null) || (arrayOfDPObject.length == 0)) && (!this.isEnd))
      this.isEnd = true;
    if ((arrayOfDPObject != null) && (i == this.nextStartIndex))
    {
      if (this.nextStartIndex == 0)
        this.bookingList.clear();
      this.bookingList.addAll(Arrays.asList(arrayOfDPObject));
      this.nextStartIndex = paramDPObject.getInt("NextStartIndex");
    }
    if ((this.bookingList != null) && (this.bookingList.size() > 0))
      refreshHeaderView(true);
    while (true)
    {
      initCreditLevelInfo(localDPObject);
      this.myBookingListAdapter.notifyDataSetChanged();
      return;
      refreshHeaderView(false);
      if ((this.tel != null) && (this.tel.trim().length() != 0))
      {
        this.curPhoneNum.setText("当前手机号" + this.tel);
        this.curPhoneNum.setVisibility(0);
        continue;
      }
      this.curPhoneNum.setVisibility(8);
    }
  }

  private void getBookingRecordTask(String paramString1, String paramString2, int paramInt)
  {
    if (this.mGetRecordRequest != null)
      return;
    paramString2 = String.format("%sclientUUID=%s&startIndex=%s", new Object[] { "http://rs.api.dianping.com/getbookinghistory.yy?", paramString2, Integer.valueOf(paramInt) });
    if (TextUtils.isEmpty(paramString1));
    for (paramString1 = paramString2; ; paramString1 = String.format("%s&token=%s", new Object[] { paramString2, paramString1 }))
    {
      this.mGetRecordRequest = BasicMApiRequest.mapiGet(paramString1, CacheType.CRITICAL);
      mapiService().exec(this.mGetRecordRequest, new RequestHandler()
      {
        public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
        {
          MyBookingListActivity.access$602(MyBookingListActivity.this, paramMApiResponse.message().toString());
          MyBookingListActivity.this.myBookingListAdapter.notifyDataSetChanged();
          MyBookingListActivity.this.loadingView.setVisibility(8);
          MyBookingListActivity.this.myBookingListView.setVisibility(0);
          MyBookingListActivity.access$702(MyBookingListActivity.this, null);
        }

        public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
        {
          if ((paramMApiResponse.result() instanceof DPObject))
          {
            MyBookingListActivity.this.myBookingListView.onRefreshComplete();
            MyBookingListActivity.this.loadingView.setVisibility(8);
            MyBookingListActivity.this.myBookingListView.setVisibility(0);
            MyBookingListActivity.this.appendOrders((DPObject)paramMApiResponse.result());
          }
          while (true)
          {
            MyBookingListActivity.access$702(MyBookingListActivity.this, null);
            return;
            MyBookingListActivity.access$602(MyBookingListActivity.this, paramMApiResponse.message().toString());
            MyBookingListActivity.this.myBookingListAdapter.notifyDataSetChanged();
          }
        }
      });
      return;
    }
  }

  private int getRemindOccurrenceNum()
  {
    return getSharedPreferences("bookinglist", 0).getInt("remindOccurrenceNum", 0);
  }

  private void initCreditLevelImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int j = paramInt1 / 2;
    int i = 0;
    while (i < j)
    {
      this.creditLevelImage[i].setImageResource(paramInt4);
      i += 1;
    }
    if (paramInt1 % 2 != 0)
    {
      this.creditLevelImage[j].setImageResource(paramInt3);
      paramInt1 = j + 1;
      while (paramInt1 < this.creditLevelImage.length)
      {
        this.creditLevelImage[paramInt1].setImageResource(paramInt2);
        paramInt1 += 1;
      }
    }
    paramInt1 = j;
    while (paramInt1 < this.creditLevelImage.length)
    {
      this.creditLevelImage[paramInt1].setImageResource(paramInt2);
      paramInt1 += 1;
    }
  }

  private void initCreditLevelInfo(DPObject paramDPObject)
  {
    int i = paramDPObject.getInt("LastCreditIncrement");
    int j = paramDPObject.getInt("WarningType");
    paramDPObject = paramDPObject.getString("WarningMsg");
    switch (j)
    {
    default:
      switch (i)
      {
      default:
        label80: if (this.lastCreditLevel == 0)
          initCreditLevelImage(this.lastCreditLevel, R.drawable.gray_star, R.drawable.gray_star, R.drawable.gray_star);
      case 0:
      case 1:
      case -1:
      }
    case 0:
    case 1:
    case 2:
    }
    while (true)
    {
      this.latestBindTelNo.setText(this.tel);
      return;
      this.warningContent.setText(paramDPObject);
      this.warningContent.setTextColor(getResources().getColor(R.color.white));
      this.warningView.setBackgroundColor(getResources().getColor(R.color.text_color_warning));
      this.warningView.setVisibility(0);
      this.warningClose.setVisibility(8);
      break;
      this.warningContent.setText(paramDPObject);
      this.warningContent.setTextColor(getResources().getColor(R.color.deep_orange_red));
      this.warningView.setBackgroundResource(R.drawable.main_background);
      this.warningView.setVisibility(0);
      this.warningClose.setVisibility(0);
      break;
      this.warningView.setVisibility(8);
      break;
      this.levelChange.setVisibility(8);
      break label80;
      this.levelChange.setImageResource(R.drawable.yy_icon_upgrade);
      this.levelChange.setVisibility(0);
      break label80;
      this.levelChange.setImageResource(R.drawable.yy_icon_demotion);
      this.levelChange.setVisibility(0);
      break label80;
      if ((this.lastCreditLevel >= 1) && (this.lastCreditLevel <= 10))
      {
        initCreditLevelImage(this.lastCreditLevel, R.drawable.gray_star, R.drawable.golden_star_half, R.drawable.golden_star);
        continue;
      }
      if ((this.lastCreditLevel >= 11) && (this.lastCreditLevel <= 20))
      {
        initCreditLevelImage(this.lastCreditLevel - 10, R.drawable.gray_diamond, R.drawable.blue_diamond_half, R.drawable.blue_diamond);
        continue;
      }
      if ((this.lastCreditLevel >= 21) && (this.lastCreditLevel <= 30))
      {
        initCreditLevelImage(this.lastCreditLevel - 20, R.drawable.gray_crown, R.drawable.blue_crown_half, R.drawable.blue_crown);
        continue;
      }
      if ((this.lastCreditLevel >= 31) && (this.lastCreditLevel <= 40))
      {
        initCreditLevelImage(this.lastCreditLevel - 30, R.drawable.gray_crown, R.drawable.golden_crown_half, R.drawable.golden_crown);
        continue;
      }
      if (this.lastCreditLevel <= 40)
        continue;
      i = 0;
      while (i < this.creditLevelImage.length)
      {
        this.creditLevelImage[i].setImageResource(R.drawable.golden_crown);
        i += 1;
      }
    }
  }

  private void initViewLayout()
  {
    getTitleBar().addRightViewItem("查看饭票", "checkticket", new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://discountlist"));
        MyBookingListActivity.this.startActivity(paramView);
      }
    });
    this.loadingView = findViewById(R.id.loading_view);
    View localView = findViewById(R.id.empty_view);
    Button localButton = (Button)localView.findViewById(R.id.search_order);
    TextView localTextView = (TextView)localView.findViewById(R.id.cooperation_entrance);
    this.curPhoneNum = ((TextView)localView.findViewById(R.id.current_phonenum));
    this.myBookingListView = ((PullToRefreshListView)findViewById(R.id.mybooking_list));
    this.myBookingListView.setEmptyView(localView);
    this.myBookingListView.setVisibility(8);
    addHeader();
    localButton.setOnClickListener(this);
    localTextView.setOnClickListener(this);
    this.myBookingListView.setOnItemClickListener(this);
    this.myBookingListView.setOnRefreshListener(this);
    this.myBookingListAdapter = new MyBookingListAdapter();
    this.myBookingListView.setAdapter(this.myBookingListAdapter);
    this.myBookingListAdapter.loadNewPage();
  }

  private void refreshHeaderView(boolean paramBoolean)
  {
    if ((paramBoolean) && (ConfigHelper.enableYY))
    {
      this.headerLayout.setVisibility(0);
      if (getRemindOccurrenceNum() < 1)
      {
        this.remindView.setVisibility(0);
        return;
      }
      this.remindView.setVisibility(8);
      return;
    }
    this.headerLayout.setVisibility(8);
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 100);
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.search_order)
    {
      statisticsEvent("mybooking6", "mybooking6_noorder_login", "", 0);
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://bookingbindphone"));
      paramView.putExtra("phone", "");
      paramView.putExtra("editable", true);
      paramView.putExtra("validation", 2);
      startActivity(paramView);
    }
    do
      while (true)
      {
        return;
        if (i == R.id.cooperation_entrance)
        {
          statisticsEvent("mybooking6", "mybooking6_merchant", "", 0);
          startActivity(new Intent("android.intent.action.VIEW", Uri.parse("dianping://bookingcooperation")));
          return;
        }
        if (i == R.id.remind_close)
        {
          if (!ViewUtils.isShow(this.remindView))
            continue;
          this.remindView.setVisibility(8);
          paramView = getSharedPreferences("bookinglist", 0).edit();
          paramView.putInt("remindOccurrenceNum", getRemindOccurrenceNum() + 1);
          paramView.commit();
          return;
        }
        if (i != R.id.warning_close)
          break;
        if (!ViewUtils.isShow(this.warningView))
          continue;
        this.warningView.setVisibility(8);
        return;
      }
    while (i != R.id.credit_level_view);
    paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://mybookinglevel"));
    paramView.putExtra("tel", this.tel);
    paramView.putExtra("levelTip", this.levelTipContent);
    paramView.putExtra("bindTip", this.bindTipContent);
    paramView.putExtra("lastCreditLevel", this.lastCreditLevel);
    startActivity(paramView);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.my_booking_order_layout);
    super.getWindow().setBackgroundDrawable(null);
    initViewLayout();
    this.mIntentFilter = new IntentFilter();
    this.mIntentFilter.addAction("com.dianping.orderlist.removeitem");
    this.mIntentFilter.addAction("com.dianping.booking.BOOKING_COMPLAIN");
    this.mIntentFilter.addAction("com.dianping.booking.BOOKING_BIND_PHONE");
    this.mIntentFilter.addAction("com.dianping.booking.BOOKING_DETAIL_REFRESH");
    registerReceiver(this.mReceiver, this.mIntentFilter);
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mReceiver != null)
      unregisterReceiver(this.mReceiver);
    if (this.mGetRecordRequest != null)
    {
      mapiService().abort(this.mGetRecordRequest, null, true);
      this.mGetRecordRequest = null;
    }
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramInt -= this.myBookingListView.getHeaderViewsCount();
    if ((paramInt < 0) || (paramInt >= this.bookingList.size()))
      return;
    if (((DPObject)this.bookingList.get(paramInt)).getInt("ReadStatus") == 1)
    {
      paramAdapterView = ((DPObject)this.bookingList.get(paramInt)).edit();
      paramAdapterView.putInt("ReadStatus", 0);
      this.bookingList.set(paramInt, paramAdapterView.generate());
      this.myBookingListAdapter.notifyDataSetChanged();
    }
    paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://bookinginfo"));
    paramView = new Bundle();
    paramView.putParcelable("bookingRecord", (Parcelable)this.bookingList.get(paramInt));
    paramAdapterView.putExtras(paramView);
    startActivity(paramAdapterView);
    statisticsEvent("mybooking5", "mybooking5_list", "" + ((DPObject)this.bookingList.get(paramInt)).getInt("Status"), 0);
    GAHelper.instance().contextStatisticsEvent(this, "order", null, "tap");
  }

  public void onRefresh(PullToRefreshListView paramPullToRefreshListView)
  {
    if (paramPullToRefreshListView.getId() == R.id.mybooking_list)
      this.myBookingListAdapter.reset();
  }

  private class MyBookingListAdapter extends BasicAdapter
  {
    public MyBookingListAdapter()
    {
    }

    private boolean loadNewPage()
    {
      if (MyBookingListActivity.this.isEnd);
      do
        return false;
      while (MyBookingListActivity.this.mGetRecordRequest != null);
      MyBookingListActivity.access$602(MyBookingListActivity.this, null);
      MyBookingListActivity localMyBookingListActivity = MyBookingListActivity.this;
      if (MyBookingListActivity.this.getAccount() == null);
      for (String str = ""; ; str = MyBookingListActivity.this.getAccount().token())
      {
        localMyBookingListActivity.getBookingRecordTask(str, Environment.uuid(), MyBookingListActivity.this.nextStartIndex);
        notifyDataSetChanged();
        return true;
      }
    }

    public int getCount()
    {
      if (MyBookingListActivity.this.isEnd)
        return MyBookingListActivity.this.bookingList.size();
      return MyBookingListActivity.this.bookingList.size() + 1;
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < MyBookingListActivity.this.bookingList.size())
        return MyBookingListActivity.this.bookingList.get(paramInt);
      if (MyBookingListActivity.this.errorMsg == null)
        return LOADING;
      return ERROR;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((localObject instanceof DPObject))
      {
        boolean bool1;
        if ((paramView instanceof BookingOrderItem))
        {
          paramView = (BookingOrderItem)paramView;
          paramView.setBookingOrder((DPObject)localObject);
          if (paramInt != 0)
            break label107;
          bool1 = true;
          label43: if ((!MyBookingListActivity.this.isEnd) || (paramInt != MyBookingListActivity.this.bookingList.size() - 1))
            break label113;
        }
        label107: label113: for (boolean bool2 = true; ; bool2 = false)
        {
          paramView.setExtraSpaceVisibility(bool1);
          paramView.setLineTopVisibility(bool1);
          paramView.setLineBottomVisibility(bool2);
          return paramView;
          paramView = new BookingOrderItem(MyBookingListActivity.this);
          break;
          bool1 = false;
          break label43;
        }
      }
      if (localObject == LOADING)
      {
        loadNewPage();
        if (!MyBookingListActivity.this.bookingList.isEmpty())
          return LayoutInflater.from(MyBookingListActivity.this).inflate(R.layout.booking_list_loading_view, null);
        return LayoutInflater.from(MyBookingListActivity.this).inflate(R.layout.loading_item, null);
      }
      return getFailedView(MyBookingListActivity.this.errorMsg, new MyBookingListActivity.MyBookingListAdapter.1(this), paramViewGroup, paramView);
    }

    public void reset()
    {
      if (MyBookingListActivity.this.mGetRecordRequest != null)
      {
        MyBookingListActivity.this.mapiService().abort(MyBookingListActivity.this.mGetRecordRequest, null, true);
        MyBookingListActivity.access$702(MyBookingListActivity.this, null);
      }
      MyBookingListActivity.this.bookingList.clear();
      MyBookingListActivity.access$902(MyBookingListActivity.this, 0);
      MyBookingListActivity.access$802(MyBookingListActivity.this, false);
      MyBookingListActivity.access$602(MyBookingListActivity.this, null);
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.MyBookingListActivity
 * JD-Core Version:    0.6.0
 */