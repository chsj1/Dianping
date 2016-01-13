package com.dianping.booking;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.booking.util.BookingHistorySearchManager;
import com.dianping.booking.util.BookingHistorySearchManager.MyBookingSearch;
import com.dianping.booking.util.BookingSearch;
import com.dianping.booking.util.OrderSource;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookingShopSearchActivity extends NovaActivity
{
  public static final String HISTORY_FILE_NAME = "booking_shop_history";
  public static final int MAX_HISTORY_SIZE = 10;
  private BookingHistorySearchManager addressManager;
  private int bookingPerson;
  private long bookingTime;
  private Button cancelButton;
  private ImageView clearButton;
  private BookingHistoryAdapter historyAdapter;
  private ListView historyListView;
  private TextView historyTitleHeaderView;
  private MApiRequest hotSearchRequest;
  private HotWordAdapter hotwordAdapter;
  private GridView hotwordGridView;
  private View hotwordHeaderView;
  List<DPObject> hotwordItems = new ArrayList();
  private String keyword = "";
  private int orderSource;
  private RequestHandler<MApiRequest, MApiResponse> requestHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == BookingShopSearchActivity.this.hotSearchRequest)
        BookingShopSearchActivity.access$002(BookingShopSearchActivity.this, null);
      do
        return;
      while (paramMApiRequest != BookingShopSearchActivity.this.suggestRequest);
      BookingShopSearchActivity.access$402(BookingShopSearchActivity.this, null);
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == BookingShopSearchActivity.this.hotSearchRequest)
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiRequest = ((DPObject)paramMApiResponse.result()).getArray("HotItems");
          if ((paramMApiRequest != null) && (paramMApiRequest.length != 0))
          {
            BookingShopSearchActivity.this.hotwordItems.addAll(Arrays.asList(paramMApiRequest));
            BookingShopSearchActivity.this.hotwordAdapter.notifyDataSetChanged();
            paramMApiRequest = new LinearLayout.LayoutParams(-1, (int)Math.ceil(paramMApiRequest.length / 3) * ViewUtils.dip2px(BookingShopSearchActivity.this, 52.0F));
            paramMApiRequest.setMargins(ViewUtils.dip2px(BookingShopSearchActivity.this.hotwordGridView.getContext(), 12.0F), 0, ViewUtils.dip2px(BookingShopSearchActivity.this.hotwordGridView.getContext(), 12.0F), 0);
            BookingShopSearchActivity.this.hotwordGridView.setLayoutParams(paramMApiRequest);
            BookingShopSearchActivity.this.hotwordHeaderView.setVisibility(0);
          }
        }
        else
        {
          BookingShopSearchActivity.access$002(BookingShopSearchActivity.this, null);
        }
      do
      {
        do
        {
          return;
          BookingShopSearchActivity.this.hotwordHeaderView.setVisibility(8);
          break;
        }
        while (paramMApiRequest != BookingShopSearchActivity.this.suggestRequest);
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiResponse = ((DPObject)paramMApiResponse.result()).getArray("List");
          BookingShopSearchActivity.this.suggestItems.clear();
          int i = 0;
          while (i < paramMApiResponse.length)
          {
            BookingShopSearchActivity.this.suggestItems.add(paramMApiResponse[i]);
            i += 1;
          }
          BookingShopSearchActivity.this.suggestAdapter.notifyDataSetChanged();
        }
        BookingShopSearchActivity.access$402(BookingShopSearchActivity.this, null);
        if (Uri.parse(paramMApiRequest.url()).getQueryParameter("keyword").equals(BookingShopSearchActivity.this.keyword))
          continue;
        BookingShopSearchActivity.this.keywordSuggest();
      }
      while ((TextUtils.isEmpty(BookingShopSearchActivity.this.keyword)) || (BookingShopSearchActivity.this.suggestListView.isShown()));
      BookingShopSearchActivity.this.historyListView.setVisibility(8);
      BookingShopSearchActivity.this.suggestListView.setVisibility(0);
    }
  };
  private EditText searchEditText;
  private int src;
  private BookingSuggestAdapter suggestAdapter;
  List<DPObject> suggestItems = new ArrayList();
  private ListView suggestListView;
  private MApiRequest suggestRequest;
  private String suggestStr = "";
  private String tagId;

  private void hotSearchTask(String paramString, int paramInt)
  {
    if (this.hotSearchRequest != null)
      return;
    String str = String.format("http://rs.api.dianping.com/hotsearch.yy?cityID=%s", new Object[] { Integer.valueOf(paramInt) });
    if (TextUtils.isEmpty(paramString));
    for (paramString = str; ; paramString = String.format("%s&token=%s", new Object[] { str, paramString }))
    {
      this.hotSearchRequest = BasicMApiRequest.mapiGet(paramString, CacheType.NORMAL);
      super.mapiService().exec(this.hotSearchRequest, this.requestHandler);
      return;
    }
  }

  private boolean isFromBookingChannel()
  {
    return this.orderSource == OrderSource.KeyWordSearch.fromType;
  }

  private void keywordSuggest()
  {
    if (TextUtils.isEmpty(this.keyword.trim()))
    {
      this.suggestListView.setVisibility(8);
      this.historyListView.setVisibility(0);
      return;
    }
    sendSuggestRequest(this.keyword);
  }

  private void processKeyword(String paramString)
  {
    if ((paramString != null) && (!TextUtils.isEmpty(paramString.trim())))
    {
      this.addressManager.addAddress(new BookingHistorySearchManager.MyBookingSearch(paramString));
      this.historyAdapter.notifyDataSetChanged();
      this.historyTitleHeaderView.setVisibility(0);
    }
    Intent localIntent = new Intent();
    localIntent.putExtra("keyword", paramString);
    if (isFromBookingChannel())
    {
      String str = String.format("dianping://bookingshoplist?ordersource=%s", new Object[] { Integer.valueOf(this.orderSource) });
      paramString = str;
      if (this.bookingTime > 0L)
      {
        paramString = str;
        if (this.bookingPerson > 0)
          paramString = String.format("%s&bookingdate=%s&bookingpersonnum=%s", new Object[] { str, Long.valueOf(this.bookingTime), Integer.valueOf(this.bookingPerson) });
      }
      localIntent.setAction("android.intent.action.VIEW");
      localIntent.setData(Uri.parse(paramString));
      localIntent.setFlags(67108864);
      super.startActivity(localIntent);
    }
    while (true)
    {
      finish();
      return;
      super.setResult(-1, localIntent);
    }
  }

  private void sendSuggestRequest(String paramString)
  {
    if (this.suggestRequest != null)
      return;
    this.suggestStr = paramString;
    this.suggestRequest = BasicMApiRequest.mapiGet(String.format("http://rs.api.dianping.com/searchsuggestion.yy?cityid=%s&keyword=%s", new Object[] { Integer.valueOf(cityId()), Uri.encode(paramString) }), CacheType.DISABLED);
    super.mapiService().exec(this.suggestRequest, this.requestHandler);
  }

  private void setupView()
  {
    super.hideTitleBar();
    this.clearButton = ((ImageView)findViewById(R.id.clear_button));
    this.clearButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        BookingShopSearchActivity.this.searchEditText.setText("");
        ((InputMethodManager)BookingShopSearchActivity.this.searchEditText.getContext().getSystemService("input_method")).showSoftInput(BookingShopSearchActivity.this.searchEditText, 2);
      }
    });
    this.cancelButton = ((Button)findViewById(R.id.cancel_button));
    this.cancelButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        BookingShopSearchActivity.this.finish();
      }
    });
    this.searchEditText = ((EditText)findViewById(R.id.shop_search_edit));
    this.searchEditText.clearFocus();
    this.searchEditText.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramEditable)
      {
        ImageView localImageView = BookingShopSearchActivity.this.clearButton;
        if (TextUtils.isEmpty(paramEditable));
        for (int i = 8; ; i = 0)
        {
          localImageView.setVisibility(i);
          BookingShopSearchActivity.access$602(BookingShopSearchActivity.this, paramEditable.toString());
          BookingShopSearchActivity.this.keywordSuggest();
          return;
        }
      }

      public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }

      public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }
    });
    this.searchEditText.setImeOptions(6);
    this.searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
    {
      public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
      {
        if ((paramInt == 6) || ((paramInt == 0) && (paramKeyEvent.getKeyCode() == 66)))
        {
          paramTextView = BookingShopSearchActivity.this.searchEditText.getText().toString();
          BookingShopSearchActivity.this.processKeyword(paramTextView);
          paramKeyEvent = BookingShopSearchActivity.this;
          if (BookingShopSearchActivity.this.isFromBookingChannel());
          for (paramInt = 1; ; paramInt = 2)
          {
            paramKeyEvent.statisticsEvent("booking6", "booking6_channel_list_searchbox", paramTextView, paramInt);
            return true;
          }
        }
        return false;
      }
    });
    this.hotwordHeaderView = LayoutInflater.from(this).inflate(R.layout.booking_shopsearch_hotword, null);
    ((TextView)this.hotwordHeaderView.findViewById(R.id.hot_word_title)).setText("猜你喜欢");
    this.hotwordGridView = ((GridView)this.hotwordHeaderView.findViewById(R.id.hot_word_view));
    this.hotwordAdapter = new HotWordAdapter();
    this.hotwordGridView.setAdapter(this.hotwordAdapter);
    this.hotwordGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        paramAdapterView = (DPObject)paramView.getTag();
        if ((paramAdapterView != null) && (!TextUtils.isEmpty(paramAdapterView.getString("ActionUrl"))))
        {
          paramView = paramAdapterView.getString("ActionUrl");
          paramAdapterView = paramView;
          if (BookingShopSearchActivity.this.isFromBookingChannel())
            paramAdapterView = String.format("%s&ordersource=%s", new Object[] { paramView, Integer.valueOf(BookingShopSearchActivity.access$1400(BookingShopSearchActivity.this)) });
          paramView = paramAdapterView;
          if (BookingShopSearchActivity.this.bookingTime > 0L)
          {
            paramView = paramAdapterView;
            if (BookingShopSearchActivity.this.bookingPerson > 0)
              paramView = String.format("%s&bookingdate=%s&bookingpersonnum=%s", new Object[] { paramAdapterView, Long.valueOf(BookingShopSearchActivity.access$1500(BookingShopSearchActivity.this)), Integer.valueOf(BookingShopSearchActivity.access$1600(BookingShopSearchActivity.this)) });
          }
          paramAdapterView = paramView;
          if (!TextUtils.isEmpty(BookingShopSearchActivity.this.tagId))
            paramAdapterView = String.format("%s&tagid=%s", new Object[] { paramView, BookingShopSearchActivity.access$1700(BookingShopSearchActivity.this) });
          paramAdapterView = new Intent("android.intent.action.VIEW", Uri.parse(String.format("%s&src=%s", new Object[] { paramAdapterView, Integer.valueOf(BookingShopSearchActivity.access$1800(BookingShopSearchActivity.this)) })));
          paramAdapterView.setFlags(67108864);
          BookingShopSearchActivity.this.startActivity(paramAdapterView);
          BookingShopSearchActivity.this.finish();
          paramAdapterView = BookingShopSearchActivity.this;
          paramView = "" + paramInt + 1;
          if (!BookingShopSearchActivity.this.isFromBookingChannel())
            break label288;
        }
        label288: for (paramInt = 1; ; paramInt = 2)
        {
          paramAdapterView.statisticsEvent("booking6", "booking6_channel_search_promo", paramView, paramInt);
          return;
        }
      }
    });
    this.hotwordHeaderView.setVisibility(8);
    this.historyTitleHeaderView = ((TextView)LayoutInflater.from(this).inflate(R.layout.booking_shopsearch_title, null));
    Object localObject = new AbsListView.LayoutParams(-1, ViewUtils.dip2px(this, 50.0F));
    this.historyTitleHeaderView.setText("搜索历史");
    this.historyTitleHeaderView.setLayoutParams((ViewGroup.LayoutParams)localObject);
    localObject = this.historyTitleHeaderView;
    if (this.addressManager.getHistorySize() != 0);
    for (int i = 0; ; i = 8)
    {
      ((TextView)localObject).setVisibility(i);
      this.historyListView = ((ListView)findViewById(R.id.history_list));
      this.historyListView.addHeaderView(this.hotwordHeaderView);
      this.historyListView.addHeaderView(this.historyTitleHeaderView);
      this.historyAdapter = new BookingHistoryAdapter(null);
      this.historyListView.setAdapter(this.historyAdapter);
      this.historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
        public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
        {
          paramInt -= BookingShopSearchActivity.this.historyListView.getHeaderViewsCount();
          if (paramInt == BookingShopSearchActivity.this.addressManager.getHistorySize())
          {
            BookingShopSearchActivity.this.addressManager.clearHistory();
            BookingShopSearchActivity.this.historyAdapter.notifyDataSetChanged();
            BookingShopSearchActivity.this.historyTitleHeaderView.setVisibility(8);
            return;
          }
          paramAdapterView = BookingShopSearchActivity.this.historyAdapter.getItem(paramInt);
          BookingShopSearchActivity.this.processKeyword(paramAdapterView);
          paramView = BookingShopSearchActivity.this;
          if (BookingShopSearchActivity.this.isFromBookingChannel());
          for (paramInt = 1; ; paramInt = 2)
          {
            paramView.statisticsEvent("booking6", "booking6_channel_search_history", paramAdapterView, paramInt);
            return;
          }
        }
      });
      this.suggestListView = ((ListView)findViewById(R.id.suggest_list));
      this.suggestAdapter = new BookingSuggestAdapter(null);
      this.suggestListView.setAdapter(this.suggestAdapter);
      this.suggestListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
        public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
        {
          if (BookingShopSearchActivity.this.suggestItems.size() > 0)
          {
            BookingShopSearchActivity.this.processKeyword(((DPObject)BookingShopSearchActivity.this.suggestItems.get(paramInt)).getString("Word"));
            BookingShopSearchActivity.this.statisticsEvent("booking6", "booking6_channel_suggest", ((DPObject)BookingShopSearchActivity.this.suggestItems.get(paramInt)).getString("Word"), paramInt + 1);
            return;
          }
          BookingShopSearchActivity.this.processKeyword(BookingShopSearchActivity.this.searchEditText.getText().toString());
        }
      });
      if (!TextUtils.isEmpty(this.keyword))
      {
        this.searchEditText.setText(this.keyword);
        this.searchEditText.setSelection(this.keyword.length());
        this.clearButton.setVisibility(0);
      }
      return;
    }
  }

  public void finish()
  {
    ((InputMethodManager)getSystemService("input_method")).hideSoftInputFromWindow(this.searchEditText.getWindowToken(), 0);
    super.finish();
    overridePendingTransition(0, 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.booking_shopsearch);
    super.getWindow().setBackgroundDrawable(null);
    if (paramBundle != null)
    {
      this.keyword = paramBundle.getString("oldkeyword");
      this.bookingPerson = paramBundle.getInt("bookingpersonnum");
      this.bookingTime = paramBundle.getLong("bookingdate");
      this.tagId = paramBundle.getString("tagid");
      this.orderSource = paramBundle.getInt("ordersource");
      this.src = paramBundle.getInt("src");
      this.addressManager = new BookingHistorySearchManager("booking_shop_history", 10);
      setupView();
      if (accountService() != null)
        break label204;
    }
    label204: for (paramBundle = ""; ; paramBundle = accountService().token())
    {
      hotSearchTask(paramBundle, cityId());
      return;
      this.keyword = super.getStringParam("oldkeyword");
      this.bookingPerson = super.getIntParam("bookingpersonnum", -1);
      this.bookingTime = super.getLongParam("bookingdate", -1L);
      this.tagId = super.getStringParam("tagid");
      this.orderSource = super.getIntParam("ordersource", -1);
      this.src = super.getIntParam("src", 0);
      break;
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.hotSearchRequest != null)
    {
      mapiService().abort(this.hotSearchRequest, null, true);
      this.hotSearchRequest = null;
    }
    if (this.suggestRequest != null)
    {
      mapiService().abort(this.suggestRequest, null, true);
      this.suggestRequest = null;
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("oldkeyword", this.keyword);
    paramBundle.putInt("bookingpersonnum", this.bookingPerson);
    paramBundle.putLong("bookingdate", this.bookingTime);
    paramBundle.putString("tagid", this.tagId);
    paramBundle.putInt("ordersource", this.orderSource);
    paramBundle.putInt("src", this.src);
    super.onSaveInstanceState(paramBundle);
  }

  private class BookingHistoryAdapter extends BasicAdapter
  {
    private BookingHistoryAdapter()
    {
    }

    public int getCount()
    {
      if (BookingShopSearchActivity.this.addressManager.getHistorySize() == 0)
        return 0;
      return BookingShopSearchActivity.this.addressManager.getHistorySize() + 1;
    }

    public String getItem(int paramInt)
    {
      if ((BookingShopSearchActivity.this.addressManager.getHistorySize() != 0) && (paramInt == BookingShopSearchActivity.this.addressManager.getHistorySize()))
        return "清除搜索记录";
      return ((BookingSearch)BookingShopSearchActivity.this.addressManager.getAddressList().get(paramInt)).getSearchKey();
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = BookingShopSearchActivity.this.getLayoutInflater().inflate(R.layout.booking_search_history_item, null, true);
      paramViewGroup = (TextView)paramView.findViewById(R.id.content);
      View localView = paramView.findViewById(R.id.divider);
      if (paramInt < BookingShopSearchActivity.this.addressManager.getHistorySize())
      {
        paramViewGroup.setGravity(19);
        localView.setVisibility(0);
      }
      while (true)
      {
        paramViewGroup.setText(getItem(paramInt));
        return paramView;
        paramViewGroup.setGravity(17);
        localView.setVisibility(8);
      }
    }
  }

  private class BookingSuggestAdapter extends BasicAdapter
  {
    private BookingSuggestAdapter()
    {
    }

    public int getCount()
    {
      if (BookingShopSearchActivity.this.suggestItems.size() == 0)
        return 1;
      return BookingShopSearchActivity.this.suggestItems.size();
    }

    public String getItem(int paramInt)
    {
      return null;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (BookingShopSearchActivity.this.suggestItems.size() == 0)
      {
        paramView = (TextView)BookingShopSearchActivity.this.getLayoutInflater().inflate(17367043, paramViewGroup, false);
        paramView.setText("查找'" + BookingShopSearchActivity.this.suggestStr + "'");
        return paramView;
      }
      DPObject localDPObject = (DPObject)BookingShopSearchActivity.this.suggestItems.get(paramInt);
      if ((paramView instanceof LinearLayout));
      for (paramView = (LinearLayout)paramView; ; paramView = null)
      {
        Object localObject = paramView;
        if (paramView == null)
          localObject = (LinearLayout)BookingShopSearchActivity.this.getLayoutInflater().inflate(R.layout.search_list_item, paramViewGroup, false);
        ((TextView)((LinearLayout)localObject).findViewById(16908308)).setText(localDPObject.getString("Word"));
        paramView = localDPObject.getString("Desc");
        ((TextView)((LinearLayout)localObject).findViewById(16908309)).setText(paramView);
        return localObject;
      }
    }
  }

  class HotWordAdapter extends BaseAdapter
  {
    public HotWordAdapter()
    {
    }

    public int getCount()
    {
      if (BookingShopSearchActivity.this.hotwordItems == null)
        return 0;
      return BookingShopSearchActivity.this.hotwordItems.size();
    }

    public Object getItem(int paramInt)
    {
      return BookingShopSearchActivity.this.hotwordItems.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = (DPObject)getItem(paramInt);
      paramViewGroup = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.booking_shopsearch_hotword_item, null);
      ViewUtils.setVisibilityAndContent((TextView)paramViewGroup.findViewById(R.id.hotword_content), paramView.getString("Title"));
      paramViewGroup.setTag(paramView);
      return paramViewGroup;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.booking.BookingShopSearchActivity
 * JD-Core Version:    0.6.0
 */