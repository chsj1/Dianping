package com.dianping.takeaway.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.dianping.accountservice.AccountService;
import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.model.SimpleMsg;
import com.dianping.takeaway.util.TakeawayFileRWer;
import com.dianping.takeaway.util.TakeawayFileRWer.FileLineParser;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import java.util.ArrayList;
import java.util.Iterator;

public class TakeawayShopSearchActivity extends NovaActivity
  implements TextWatcher, View.OnClickListener, TextView.OnEditorActionListener
{
  private static final int MAX_HISTORY_SIZE = 10;
  private EditText addressEditText;
  private Button cancelButton;
  private ImageView clearButton;
  private int geoType;
  private TakeawayHistoryAdapter historyAdapter;
  private ArrayList<String> historyList;
  private ListView historyListView;
  private String keyword = "";
  private String lat = "";
  private String lng = "";
  protected RequestHandler<MApiRequest, MApiResponse> mapiHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == TakeawayShopSearchActivity.this.searchRequest)
      {
        TakeawayShopSearchActivity.this.dismissDialog();
        if ((paramMApiResponse == null) || (paramMApiResponse.message() == null))
          break label57;
        TakeawayShopSearchActivity.this.showToastMsg(paramMApiResponse.message().content());
      }
      while (true)
      {
        TakeawayShopSearchActivity.access$002(TakeawayShopSearchActivity.this, null);
        return;
        label57: TakeawayShopSearchActivity.this.showToastMsg("网络不给力哦，请稍后再试");
      }
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == TakeawayShopSearchActivity.this.searchRequest)
      {
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          paramMApiRequest = (DPObject)paramMApiResponse.result();
          if (paramMApiRequest != null)
          {
            paramMApiRequest = paramMApiRequest.getArray("ShopResult");
            TakeawayShopSearchActivity.this.processSearchResult(paramMApiRequest);
          }
        }
        TakeawayShopSearchActivity.access$002(TakeawayShopSearchActivity.this, null);
      }
    }
  };
  private MApiRequest searchRequest;
  private TakeawayShopAdapter shopAdapter;
  private ListView shopListView;
  private int source = 0;
  private ArrayList<TAShopSuggest> suggestList;

  private void addShopKeyword(String paramString)
  {
    if ((paramString == null) || (TextUtils.isEmpty(paramString.trim())))
      return;
    removeFromList(paramString);
    while (this.historyList.size() >= 10)
      this.historyList.remove(0);
    this.historyList.add(paramString);
    saveAddressHistory();
  }

  private void configListView()
  {
    this.shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        paramAdapterView = ((TakeawayShopSearchActivity.TAShopSuggest)TakeawayShopSearchActivity.this.suggestList.get(paramInt)).title;
        TakeawayShopSearchActivity.this.processKeyword(paramAdapterView, ((TakeawayShopSearchActivity.TAShopSuggest)TakeawayShopSearchActivity.this.suggestList.get(paramInt)).extraInfo);
        TakeawayShopSearchActivity.this.statisticsEvent("takeaway6", "takeaway6_keyword_suggestclk", paramAdapterView, 0);
        paramView = new GAUserInfo();
        paramView.keyword = paramAdapterView;
        GAHelper.instance().contextStatisticsEvent(TakeawayShopSearchActivity.this, "suggest", paramView, "tap");
      }
    });
    this.historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        if (paramInt == TakeawayShopSearchActivity.this.historyList.size())
        {
          TakeawayShopSearchActivity.this.historyList.clear();
          TakeawayShopSearchActivity.this.saveAddressHistory();
          TakeawayShopSearchActivity.this.historyAdapter.notifyDataSetChanged();
          return;
        }
        TakeawayShopSearchActivity.this.processKeyword(TakeawayShopSearchActivity.this.historyAdapter.getItem(paramInt), "");
      }
    });
  }

  private void getShopHistory()
  {
    this.historyList = new TakeawayFileRWer("takeaway_shop_history").readFileToList(new TakeawayFileRWer.FileLineParser()
    {
      public String parse(String paramString)
      {
        return paramString;
      }
    });
  }

  private void hideSuggestView()
  {
    this.suggestList.clear();
    this.shopAdapter.notifyDataSetChanged();
    this.shopListView.setVisibility(8);
  }

  private void processKeyword(String paramString1, String paramString2)
  {
    addShopKeyword(paramString1);
    if (this.source == 0)
    {
      localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://takeawayshoplist"));
      localIntent.putExtra("lat", this.lat);
      localIntent.putExtra("lng", this.lng);
      localIntent.putExtra("geotype", this.geoType);
      localIntent.putExtra("keyword", paramString1);
      localIntent.putExtra("noshopreason", 1);
      localIntent.putExtra("extrainfo", paramString2);
      startActivity(localIntent);
      finish();
      return;
    }
    Intent localIntent = new Intent();
    localIntent.putExtra("keyword", paramString1);
    localIntent.putExtra("extrainfo", paramString2);
    setResult(-1, localIntent);
    finish();
  }

  private void processSearchResult(DPObject[] paramArrayOfDPObject)
  {
    this.suggestList.clear();
    int j = paramArrayOfDPObject.length;
    int i = 0;
    while (i < j)
    {
      DPObject localDPObject = paramArrayOfDPObject[i];
      TAShopSuggest localTAShopSuggest = new TAShopSuggest(null);
      localTAShopSuggest.title = localDPObject.getString("Title");
      localTAShopSuggest.itemCount = localDPObject.getString("ItemCount");
      localTAShopSuggest.extraInfo = localDPObject.getString("ExtraInfo");
      localTAShopSuggest.matchToken = localDPObject.getString("MatchToken");
      this.suggestList.add(localTAShopSuggest);
      i += 1;
    }
    this.shopAdapter.notifyDataSetChanged();
    this.shopListView.setVisibility(0);
  }

  private void removeFromList(String paramString)
  {
    Iterator localIterator = this.historyList.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      if (!paramString.equals(str))
        continue;
      this.historyList.remove(str);
    }
  }

  private void saveAddressHistory()
  {
    new TakeawayFileRWer("takeaway_shop_history").writeListToFile(this.historyList);
  }

  private void sendSearchRequest(String paramString)
  {
    if (TextUtils.isEmpty(paramString));
    do
      return;
    while (this.searchRequest != null);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("keyword");
    localArrayList.add(paramString);
    localArrayList.add("cityid");
    localArrayList.add(String.valueOf(cityId()));
    localArrayList.add("lat");
    localArrayList.add(this.lat);
    localArrayList.add("lng");
    localArrayList.add(this.lng);
    localArrayList.add("locatecityid");
    if (location() == null);
    for (paramString = ""; ; paramString = String.valueOf(location().city().id()))
    {
      localArrayList.add(paramString);
      paramString = accountService().token();
      if (!TextUtils.isEmpty(paramString))
      {
        localArrayList.add("token");
        localArrayList.add(paramString);
      }
      this.searchRequest = BasicMApiRequest.mapiPost("http://waimai.api.dianping.com/shopsuggest.ta?", (String[])localArrayList.toArray(new String[0]));
      mapiService().exec(this.searchRequest, this.mapiHandler);
      return;
    }
  }

  private void setupView()
  {
    super.hideTitleBar();
    super.setContentView(R.layout.takeaway_shopsearch);
    super.getWindow().setBackgroundDrawable(null);
    this.clearButton = ((ImageView)findViewById(R.id.clear_button));
    this.clearButton.setOnClickListener(this);
    this.cancelButton = ((Button)findViewById(R.id.cancel_button));
    this.cancelButton.setOnClickListener(this);
    this.addressEditText = ((EditText)findViewById(R.id.shop_search_edit));
    if (!TextUtils.isEmpty(this.keyword))
    {
      this.addressEditText.setText(this.keyword);
      this.addressEditText.setSelection(this.keyword.length());
      this.clearButton.setVisibility(0);
    }
    this.addressEditText.clearFocus();
    this.addressEditText.addTextChangedListener(this);
    this.addressEditText.setImeOptions(6);
    this.addressEditText.setOnEditorActionListener(this);
    this.shopListView = ((ListView)findViewById(R.id.suggestList));
    this.shopListView.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        if (paramMotionEvent.getAction() == 0)
        {
          TakeawayShopSearchActivity.this.addressEditText.clearFocus();
          KeyboardUtils.hideKeyboard(TakeawayShopSearchActivity.this.addressEditText);
        }
        return false;
      }
    });
    this.shopAdapter = new TakeawayShopAdapter();
    this.shopListView.setAdapter(this.shopAdapter);
    this.historyListView = ((ListView)findViewById(R.id.historyList));
    this.historyListView.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        if (paramMotionEvent.getAction() == 0)
        {
          TakeawayShopSearchActivity.this.addressEditText.clearFocus();
          KeyboardUtils.hideKeyboard(TakeawayShopSearchActivity.this.addressEditText);
        }
        return false;
      }
    });
    this.historyAdapter = new TakeawayHistoryAdapter();
    this.historyListView.setAdapter(this.historyAdapter);
    configListView();
  }

  private void showToastMsg(String paramString)
  {
    paramString = Toast.makeText(this, paramString, 0);
    paramString.setGravity(17, 0, 0);
    paramString.show();
  }

  public void afterTextChanged(Editable paramEditable)
  {
    if (TextUtils.isEmpty(paramEditable))
    {
      this.clearButton.setVisibility(8);
      this.historyListView.setVisibility(0);
      hideSuggestView();
      return;
    }
    this.clearButton.setVisibility(0);
    this.historyListView.setVisibility(8);
    paramEditable = paramEditable.toString();
    if (TextUtils.isEmpty(paramEditable.trim()))
    {
      hideSuggestView();
      return;
    }
    sendSearchRequest(paramEditable);
  }

  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void finish()
  {
    if (this.addressEditText != null)
      ((InputMethodManager)getSystemService("input_method")).hideSoftInputFromWindow(this.addressEditText.getWindowToken(), 0);
    super.finish();
    if (this.source == 1)
      overridePendingTransition(0, 0);
  }

  public String getPageName()
  {
    return "takeawayshoplist";
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.clear_button)
    {
      this.addressEditText.setText("");
      ((InputMethodManager)getSystemService("input_method")).showSoftInput(this.addressEditText, 2);
    }
    do
      return;
    while (i != R.id.cancel_button);
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.lat = super.getStringParam("lat");
    this.lng = super.getStringParam("lng");
    this.geoType = super.getIntParam("geotype", 1);
    this.source = super.getIntParam("source", 0);
    this.keyword = super.getStringParam("oldkeyword");
    this.historyList = new ArrayList();
    this.suggestList = new ArrayList();
    getShopHistory();
    setupView();
  }

  public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt == 6) || ((paramInt == 0) && (paramKeyEvent.getKeyCode() == 66)))
    {
      if (TextUtils.isEmpty(this.addressEditText.getText()))
        showToastMsg("请输入商户名、菜品");
      while (true)
      {
        return true;
        processKeyword(this.addressEditText.getText().toString(), "");
      }
    }
    return false;
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt == 4) && (this.source == 1) && (getWindow().getAttributes().softInputMode != 4))
    {
      finish();
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  private static class TAShopSuggest
  {
    String extraInfo;
    String itemCount;
    String matchToken;
    String title;
  }

  class TakeawayHistoryAdapter extends BasicAdapter
  {
    TakeawayHistoryAdapter()
    {
    }

    public int getCount()
    {
      if (!TakeawayShopSearchActivity.this.historyList.isEmpty())
        return TakeawayShopSearchActivity.this.historyList.size() + 1;
      return 0;
    }

    public String getItem(int paramInt)
    {
      if ((!TakeawayShopSearchActivity.this.historyList.isEmpty()) && (paramInt == TakeawayShopSearchActivity.this.historyList.size()))
        return "清除搜索记录";
      return (String)TakeawayShopSearchActivity.this.historyList.get(TakeawayShopSearchActivity.this.historyList.size() - 1 - paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramInt < TakeawayShopSearchActivity.this.historyList.size());
      for (paramView = TakeawayShopSearchActivity.this.getLayoutInflater().inflate(R.layout.takeaway_history_address_item, null, true); ; paramView = TakeawayShopSearchActivity.this.getLayoutInflater().inflate(R.layout.takeaway_history_clear_item, null, true))
      {
        ((TextView)paramView).setText(getItem(paramInt));
        return paramView;
      }
    }
  }

  class TakeawayShopAdapter extends BasicAdapter
  {
    TakeawayShopAdapter()
    {
    }

    public int getCount()
    {
      return TakeawayShopSearchActivity.this.suggestList.size();
    }

    public TakeawayShopSearchActivity.TAShopSuggest getItem(int paramInt)
    {
      return (TakeawayShopSearchActivity.TAShopSuggest)TakeawayShopSearchActivity.this.suggestList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = paramView;
      if (paramView == null)
        paramViewGroup = TakeawayShopSearchActivity.this.getLayoutInflater().inflate(R.layout.takeaway_suggest_shop_item, null, true);
      paramView = (TakeawayShopSearchActivity.TAShopSuggest)TakeawayShopSearchActivity.this.suggestList.get(paramInt);
      TextView localTextView1 = (TextView)paramViewGroup.findViewById(R.id.shop_name);
      TextView localTextView2 = (TextView)paramViewGroup.findViewById(R.id.shop_info);
      if (!TextUtils.isEmpty(paramView.title))
      {
        localTextView1.setText(paramView.title);
        localTextView1.setVisibility(0);
        if (TextUtils.isEmpty(paramView.itemCount))
          break label139;
        localTextView2.setText(paramView.itemCount);
        localTextView2.setVisibility(0);
      }
      while (true)
      {
        ViewUtils.setVisibilityAndContent((TextView)paramViewGroup.findViewById(R.id.shop_keyword), paramView.matchToken);
        return paramViewGroup;
        localTextView1.setVisibility(4);
        break;
        label139: localTextView2.setVisibility(4);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.activity.TakeawayShopSearchActivity
 * JD-Core Version:    0.6.0
 */