package com.dianping.movie.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.DPApplication;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationListener;
import com.dianping.locationservice.LocationService;
import com.dianping.model.Location;
import com.dianping.movie.util.MovieFileRWer;
import com.dianping.movie.util.MovieFileRWer.FileLineParser;
import com.dianping.util.KeyboardUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaButton;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class MovieSearchActivity extends NovaActivity
  implements TextWatcher, View.OnClickListener, TextView.OnEditorActionListener, RequestHandler<MApiRequest, MApiResponse>, LocationListener
{
  public static final int DEFAULT_SEARCH_TYPE = 0;
  private static final int MAX_HISTORY_SIZE = 6;
  public static final int SEARCH_CINEMA_ONLY = 2;
  public static final int SEARCH_MOVIE_ONLY = 1;
  private NovaButton cancelButton;
  private ImageView clearButton;
  private SearchHistoryAdapter historyAdapter;
  private View historyLayout;
  private ArrayList<String> historyList;
  private ListView historyListView;
  private boolean isLocated = false;
  private String keyword = "";
  private MApiRequest movieSuggestRequest;
  private int myAccuracy = 0;
  private double myLatitude = 0.0D;
  private double myLongitude = 0.0D;
  private int scope = 0;
  private EditText searchEditText;
  private MovieSuggestAdapter suggestAdapter;
  private ArrayList<DPObject> suggestList;
  private ListView suggestListView;

  private void addSearchKeyword(String paramString)
  {
    if ((paramString == null) || (TextUtils.isEmpty(paramString.trim())))
      return;
    removeFromList(paramString);
    while (this.historyList.size() >= 6)
      this.historyList.remove(0);
    this.historyList.add(paramString);
    saveAddressHistory();
  }

  private void configListView()
  {
    this.suggestListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        paramAdapterView = (DPObject)MovieSearchActivity.this.suggestList.get(paramInt);
        if (paramAdapterView != null)
          MovieSearchActivity.this.processSuggest(paramAdapterView.getString("Value"), paramAdapterView.getString("Url"));
        GAHelper.instance().contextStatisticsEvent(MovieSearchActivity.this, "searchsuggest", null, paramInt, "tap");
      }
    });
    this.historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        if (paramInt == MovieSearchActivity.this.historyList.size())
        {
          MovieSearchActivity.this.historyList.clear();
          MovieSearchActivity.this.saveAddressHistory();
          MovieSearchActivity.this.historyAdapter.notifyDataSetChanged();
          MovieSearchActivity.this.historyLayout.setVisibility(8);
          GAHelper.instance().contextStatisticsEvent(MovieSearchActivity.this, "clear_history", null, paramInt, "tap");
          return;
        }
        MovieSearchActivity.this.processKeyword(MovieSearchActivity.this.historyAdapter.getItem(paramInt));
        GAHelper.instance().contextStatisticsEvent(MovieSearchActivity.this, "searchhistory", null, paramInt, "tap");
      }
    });
  }

  private void getMyLocation()
  {
    Object localObject2 = DPApplication.instance().locationService().location();
    Object localObject1;
    if (localObject2 != null)
      localObject1 = null;
    try
    {
      localObject2 = (Location)((DPObject)localObject2).decodeToObject(Location.DECODER);
      localObject1 = localObject2;
      if (localObject1 != null)
      {
        this.isLocated = true;
        this.myLatitude = localObject1.offsetLatitude();
        this.myLongitude = localObject1.offsetLongitude();
        this.myAccuracy = localObject1.accuracy();
      }
      return;
    }
    catch (ArchiveException localArchiveException)
    {
      while (true)
        localArchiveException.printStackTrace();
    }
  }

  private void getShopHistory()
  {
    this.historyList = new MovieFileRWer("movie_search_history").readFileToList(new MovieFileRWer.FileLineParser()
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
    this.suggestAdapter.notifyDataSetChanged();
    this.suggestListView.setVisibility(8);
  }

  private boolean isLocated()
  {
    return this.isLocated;
  }

  private void processKeyword(String paramString)
  {
    addSearchKeyword(paramString);
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://moviesearch?scope=" + this.scope));
    localIntent.putExtra("keyword", paramString);
    localIntent.setFlags(67108864);
    startActivity(localIntent);
    finish();
  }

  private void processSuggest(String paramString1, String paramString2)
  {
    addSearchKeyword(paramString1);
    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(paramString2)));
    finish();
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
    new MovieFileRWer("movie_search_history").writeListToFile(this.historyList);
  }

  private void sendSearchRequest(String paramString)
  {
    if (this.movieSuggestRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://app.movie.dianping.com/moviesuggestmv.bin?").buildUpon();
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    localBuilder.appendQueryParameter("keyword", paramString);
    switch (this.scope)
    {
    default:
      localBuilder.appendQueryParameter("scope", String.valueOf(0));
    case 2:
    case 1:
    }
    while (true)
    {
      if (isLocated())
      {
        localBuilder.appendQueryParameter("lat", Location.FMT.format(this.myLatitude));
        localBuilder.appendQueryParameter("lng", Location.FMT.format(this.myLongitude));
        localBuilder.appendQueryParameter("accuracy", String.valueOf(this.myAccuracy));
      }
      this.movieSuggestRequest = BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
      mapiService().exec(this.movieSuggestRequest, this);
      return;
      localBuilder.appendQueryParameter("scope", String.valueOf(2));
      continue;
      localBuilder.appendQueryParameter("scope", String.valueOf(1));
    }
  }

  public void afterTextChanged(Editable paramEditable)
  {
    if (TextUtils.isEmpty(paramEditable))
    {
      this.clearButton.setVisibility(8);
      if (this.historyList.size() > 0)
        this.historyLayout.setVisibility(0);
      hideSuggestView();
      return;
    }
    this.clearButton.setVisibility(0);
    this.historyLayout.setVisibility(8);
    paramEditable = paramEditable.toString();
    if (TextUtils.isEmpty(paramEditable.trim()))
    {
      hideSuggestView();
      return;
    }
    GAHelper.instance().contextStatisticsEvent(this, "movie_search", this.searchEditText.getText().toString().trim(), 0, "tap");
    sendSearchRequest(paramEditable.trim());
  }

  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void finish()
  {
    ((InputMethodManager)getSystemService("input_method")).hideSoftInputFromWindow(this.searchEditText.getWindowToken(), 0);
    super.finish();
  }

  public String getPageName()
  {
    setPageId("9040017");
    return "moviesuggest";
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (i == R.id.clear_button)
    {
      this.searchEditText.setText("");
      ((InputMethodManager)getSystemService("input_method")).showSoftInput(this.searchEditText, 2);
    }
    do
      return;
    while (i != R.id.cancel_button);
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getIntent();
    this.scope = paramBundle.getIntExtra("scope", 0);
    this.keyword = paramBundle.getStringExtra("keyword");
    this.historyList = new ArrayList();
    this.suggestList = new ArrayList();
    getShopHistory();
    super.hideTitleBar();
    super.setContentView(R.layout.movie_search_activity);
    super.getWindow().setBackgroundDrawable(null);
    this.historyLayout = findViewById(R.id.historyListLayout);
    this.clearButton = ((ImageView)findViewById(R.id.clear_button));
    this.clearButton.setOnClickListener(this);
    this.cancelButton = ((NovaButton)findViewById(R.id.cancel_button));
    this.cancelButton.setGAString("cancel");
    this.cancelButton.setOnClickListener(this);
    this.searchEditText = ((EditText)findViewById(R.id.movie_search_edit));
    if (!TextUtils.isEmpty(this.keyword))
    {
      this.searchEditText.setText(this.keyword);
      this.searchEditText.setSelection(this.keyword.length());
      this.clearButton.setVisibility(0);
    }
    switch (this.scope)
    {
    default:
      this.searchEditText.setHint("输入影片名或影院名");
    case 2:
    case 1:
    }
    while (true)
    {
      this.searchEditText.clearFocus();
      this.searchEditText.addTextChangedListener(this);
      this.searchEditText.setImeOptions(3);
      this.searchEditText.setOnEditorActionListener(this);
      this.suggestListView = ((ListView)findViewById(R.id.suggestList));
      this.suggestAdapter = new MovieSuggestAdapter();
      this.suggestListView.setAdapter(this.suggestAdapter);
      this.historyListView = ((ListView)findViewById(R.id.historyList));
      this.historyListView.setOnTouchListener(new View.OnTouchListener()
      {
        public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
        {
          if (paramMotionEvent.getAction() == 0)
          {
            MovieSearchActivity.this.searchEditText.clearFocus();
            KeyboardUtils.hideKeyboard(MovieSearchActivity.this.searchEditText);
          }
          return false;
        }
      });
      this.historyAdapter = new SearchHistoryAdapter();
      this.historyListView.setAdapter(this.historyAdapter);
      configListView();
      if (this.historyList.size() == 0)
        this.historyLayout.setVisibility(8);
      getMyLocation();
      return;
      this.searchEditText.setHint("输入影院名");
      continue;
      this.searchEditText.setHint("输入影片名");
    }
  }

  protected void onDestroy()
  {
    if (this.movieSuggestRequest != null)
    {
      mapiService().abort(this.movieSuggestRequest, this, true);
      this.movieSuggestRequest = null;
    }
    super.onDestroy();
  }

  public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
  {
    int j = 0;
    int i;
    if (paramInt != 3)
    {
      i = j;
      if (paramInt == 0)
      {
        i = j;
        if (paramKeyEvent.getKeyCode() != 66);
      }
    }
    else
    {
      if (!TextUtils.isEmpty(this.searchEditText.getText().toString().trim()))
        break label127;
      switch (this.scope)
      {
      default:
        paramTextView = Toast.makeText(this, "请输入影片名或影院名", 0);
        paramTextView.setGravity(17, 0, 0);
        paramTextView.show();
      case 2:
      case 1:
      }
    }
    while (true)
    {
      i = 1;
      return i;
      paramTextView = Toast.makeText(this, "请输入影院名", 0);
      break;
      paramTextView = Toast.makeText(this, "请输入影片名", 0);
      break;
      label127: processKeyword(this.searchEditText.getText().toString().trim());
      GAHelper.instance().contextStatisticsEvent(this, "search", this.searchEditText.getText().toString().trim(), 0, "tap");
    }
  }

  public void onLocationChanged(LocationService paramLocationService)
  {
    getMyLocation();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.movieSuggestRequest)
      this.movieSuggestRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    paramMApiResponse = paramMApiResponse.result();
    if (paramMApiRequest == this.movieSuggestRequest)
    {
      if ((!DPObjectUtils.isDPObjectof(paramMApiResponse, "MovieSuggestList")) || (((DPObject)paramMApiResponse).getArray("List") == null) || (((DPObject)paramMApiResponse).getArray("List").length <= 0))
        break label103;
      paramMApiRequest = (DPObject)paramMApiResponse;
      this.suggestList.clear();
      this.suggestList.addAll(Arrays.asList(paramMApiRequest.getArray("List")));
      this.suggestAdapter.notifyDataSetChanged();
      this.suggestListView.setVisibility(0);
    }
    while (true)
    {
      this.movieSuggestRequest = null;
      return;
      label103: this.suggestList.clear();
      this.suggestAdapter.notifyDataSetChanged();
      this.suggestListView.setVisibility(0);
    }
  }

  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  class MovieSuggestAdapter extends BasicAdapter
  {
    MovieSuggestAdapter()
    {
    }

    public int getCount()
    {
      return MovieSearchActivity.this.suggestList.size();
    }

    public DPObject getItem(int paramInt)
    {
      return (DPObject)MovieSearchActivity.this.suggestList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
      {
        paramView = MovieSearchActivity.this.getLayoutInflater().inflate(R.layout.movie_suggest_item, null, true);
        paramViewGroup = new MovieSearchActivity.MovieSuggestAdapter.ViewHolder(this, null);
        paramViewGroup.scopeNameTv = ((TextView)paramView.findViewById(R.id.scope_name_tv));
        paramViewGroup.valueTv = ((TextView)paramView.findViewById(R.id.value_tv));
        paramViewGroup.displayInfoTv = ((TextView)paramView.findViewById(R.id.displayinfo_tv));
        paramView.setTag(paramViewGroup);
      }
      while (true)
      {
        DPObject localDPObject = getItem(paramInt);
        if (localDPObject != null)
        {
          paramViewGroup.scopeNameTv.setText(localDPObject.getString("Scope"));
          paramViewGroup.valueTv.setText(localDPObject.getString("Value"));
          paramViewGroup.displayInfoTv.setText(localDPObject.getString("DisplayInfo"));
        }
        return paramView;
        paramViewGroup = (MovieSearchActivity.MovieSuggestAdapter.ViewHolder)paramView.getTag();
      }
    }
  }

  class SearchHistoryAdapter extends BasicAdapter
  {
    SearchHistoryAdapter()
    {
    }

    public int getCount()
    {
      if (MovieSearchActivity.this.historyList.size() != 0)
        return MovieSearchActivity.this.historyList.size() + 1;
      return 0;
    }

    public String getItem(int paramInt)
    {
      if ((MovieSearchActivity.this.historyList.size() != 0) && (paramInt == MovieSearchActivity.this.historyList.size()))
        return "清除搜索记录";
      return (String)MovieSearchActivity.this.historyList.get(MovieSearchActivity.this.historyList.size() - 1 - paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramInt < MovieSearchActivity.this.historyList.size());
      for (paramView = MovieSearchActivity.this.getLayoutInflater().inflate(R.layout.movie_history_search_item, null, true); ; paramView = MovieSearchActivity.this.getLayoutInflater().inflate(R.layout.movie_history_clear_item, null, true))
      {
        ((TextView)paramView).setText(getItem(paramInt));
        return paramView;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.movie.activity.MovieSearchActivity
 * JD-Core Version:    0.6.0
 */