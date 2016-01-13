package com.dianping.base.basic;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.CustomGridView;
import com.dianping.base.widget.CustomGridView.OnItemClickListener;
import com.dianping.base.widget.NovaFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.BitmapUtils;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractSearchFragment extends NovaFragment
  implements AdapterView.OnItemClickListener, CustomGridView.OnItemClickListener, View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  protected static final DPObject CLEARHISTORY;
  protected static final int HEADER_VIEW_HOT_SEARCH = 0;
  protected static final int HEADER_VIEW_HOT_SEARCH_KEYWORDS = 1;
  protected static final int HEADER_VIEW_SEARCH_HISTORY = 2;
  protected static int HISTORY_SUGGEST_BACK = 0;
  protected static final String HOTWORD_RED_TYPE = "1";
  protected static final String HOTWORD_RED_WITH_ICON = "2";
  public static final String KEY_DONT_START_RESULT_ACTIVITY = "dontStartResultActivity";
  static final Object NO_SUGGESTION;
  protected static final int SEARCH_MODE_HISTORY = 1;
  protected static final int SEARCH_MODE_SUGGEST = 2;
  private static final int SEARCH_SIZE_LIMIT = 10;
  protected static int SEARCH_SUGGEST_MESSAGE;
  private static final String TAG = AbstractSearchFragment.class.getSimpleName();
  LinearLayout containerLayout = null;
  private DPObject dpobjKeyword;
  protected boolean hasHotwordView = true;
  protected HistoryAdapter historyListAdapter;
  protected ListView listView;
  protected View mClearButton;
  protected ContentResolver mContentResolver;
  protected final Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == AbstractSearchFragment.SEARCH_SUGGEST_MESSAGE)
        if ((paramMessage.obj instanceof String))
        {
          paramMessage = (String)paramMessage.obj;
          if (!TextUtils.isEmpty(paramMessage))
            break label52;
          AbstractSearchFragment.this.mSearchMode = 1;
          AbstractSearchFragment.this.searchSuggest(paramMessage);
        }
      label52: 
      do
      {
        return;
        AbstractSearchFragment.this.mSearchMode = 2;
        break;
      }
      while ((paramMessage.what != AbstractSearchFragment.HISTORY_SUGGEST_BACK) || (AbstractSearchFragment.this.searchHistoryList.size() <= 0));
      AbstractSearchFragment.this.historyListAdapter.setHistoryList(AbstractSearchFragment.this.searchHistoryList);
    }
  };
  protected List<View> mHeaderViews = new ArrayList();
  private OnSearchFragmentListener mOnSearchFragmentListener;
  protected int mSearchMode = 1;
  protected View.OnTouchListener onTouchListener = new View.OnTouchListener()
  {
    public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
    {
      switch (paramMotionEvent.getAction())
      {
      default:
        return false;
      case 0:
      }
      ((InputMethodManager)AbstractSearchFragment.this.getActivity().getSystemService("input_method")).hideSoftInputFromWindow(paramView.getWindowToken(), 0);
      return false;
    }
  };
  protected String queryid;
  private String referPageName;
  protected MApiRequest request;
  protected EditText searchEditText;
  protected String searchHint;
  private ArrayList<String> searchHistoryList = new ArrayList();
  protected BaseSuggestionAdapter suggestListAdapter;
  protected TextWatcher textWatcher;

  static
  {
    CLEARHISTORY = new DPObject().edit().putString("Keyword", "清除搜索记录").generate();
    NO_SUGGESTION = new Object();
    SEARCH_SUGGEST_MESSAGE = 1;
    HISTORY_SUGGEST_BACK = 2;
  }

  private JSONObject parseDPObjectToJson(DPObject paramDPObject)
  {
    JSONObject localJSONObject = new JSONObject();
    if (paramDPObject == null);
    while (true)
    {
      return localJSONObject;
      try
      {
        String str = paramDPObject.getString("Keyword");
        if (!TextUtils.isEmpty(str))
          localJSONObject.put("keyword", str);
        str = paramDPObject.getString("Value");
        if (!TextUtils.isEmpty(str))
          localJSONObject.put("value", str);
        str = paramDPObject.getString("Icon");
        if (!TextUtils.isEmpty(str))
          localJSONObject.put("Icon", str);
        str = paramDPObject.getString("Desc");
        if (!TextUtils.isEmpty(str))
          localJSONObject.put("Desc", str);
        str = paramDPObject.getString("Url");
        if (!TextUtils.isEmpty(str))
          localJSONObject.put("Url", str);
        localJSONObject.put("SuggestType", paramDPObject.getInt("SuggestType"));
        str = paramDPObject.getString("DisplayInfo");
        if (!TextUtils.isEmpty(str))
          localJSONObject.put("DisplayInfo", str);
        paramDPObject = paramDPObject.getString("QueryID");
        if (TextUtils.isEmpty(paramDPObject))
          continue;
        localJSONObject.put("QueryID", paramDPObject);
        return localJSONObject;
      }
      catch (JSONException paramDPObject)
      {
        paramDPObject.printStackTrace();
      }
    }
    return localJSONObject;
  }

  private DPObject parseSuggestDPObject(String paramString)
  {
    DPObject.Editor localEditor = new DPObject().edit();
    try
    {
      JSONObject localJSONObject = new JSONObject(paramString);
      localEditor.putString("Keyword", localJSONObject.optString("keyword"));
      localEditor.putString("Value", localJSONObject.optString("value"));
      localEditor.putString("Icon", localJSONObject.optString("Icon"));
      localEditor.putString("Desc", localJSONObject.optString("Desc"));
      localEditor.putString("Url", localJSONObject.optString("Url"));
      localEditor.putInt("SuggestType", localJSONObject.optInt("SuggestType"));
      localEditor.putString("DisplayInfo", localJSONObject.optString("DisplayInfo"));
      localEditor.putString("QueryID", localJSONObject.optString("QueryID"));
      return localEditor.generate();
    }
    catch (JSONException localJSONException)
    {
      while (true)
        localEditor.putString("Keyword", paramString).generate();
    }
  }

  public Uri buildUri(DPObject paramDPObject)
  {
    return null;
  }

  protected View createHistoryItem(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    DPObject localDPObject = (DPObject)this.historyListAdapter.getItem(paramInt);
    Object localObject;
    if ((paramView instanceof NovaLinearLayout))
    {
      paramView = (NovaLinearLayout)paramView;
      localObject = paramView;
      if (paramView == null)
        localObject = (NovaLinearLayout)getActivity().getLayoutInflater().inflate(R.layout.search_list_item, paramViewGroup, false);
      paramView = (TextView)((NovaLinearLayout)localObject).findViewById(16908308);
      paramView.setText(localDPObject.getString("Keyword"));
      if (paramInt != this.historyListAdapter.getCount() - 1)
        break label269;
      ((NovaLinearLayout)localObject).findViewById(R.id.divider).setVisibility(8);
      ((NovaLinearLayout)localObject).findViewById(R.id.list_view_end_divider).setVisibility(0);
      label113: if (paramInt != 0)
        break label297;
      ((NovaLinearLayout)localObject).findViewById(R.id.list_view_start_divider).setVisibility(0);
      label129: if (localDPObject != CLEARHISTORY)
        break label313;
      paramView.setGravity(17);
    }
    while (true)
    {
      ((NovaLinearLayout)localObject).setGAString("history", localDPObject.getString("Keyword"));
      paramViewGroup = localDPObject.getString("QueryID");
      paramView = paramViewGroup;
      if (TextUtils.isEmpty(paramViewGroup))
        paramView = this.queryid;
      ((NovaLinearLayout)localObject).gaUserInfo.index = Integer.valueOf(paramInt);
      ((NovaLinearLayout)localObject).gaUserInfo.keyword = String.valueOf(this.searchEditText.getText());
      if ((paramInt == 0) && (this.queryid == null))
      {
        ((NovaLinearLayout)localObject).gaUserInfo.query_id = null;
        GAHelper.instance().contextStatisticsEvent(((NovaLinearLayout)localObject).getContext(), "history", ((NovaLinearLayout)localObject).gaUserInfo, "view");
      }
      ((NovaLinearLayout)localObject).gaUserInfo.query_id = paramView;
      return localObject;
      paramView = null;
      break;
      label269: ((NovaLinearLayout)localObject).findViewById(R.id.divider).setVisibility(0);
      ((NovaLinearLayout)localObject).findViewById(R.id.list_view_end_divider).setVisibility(8);
      break label113;
      label297: ((NovaLinearLayout)localObject).findViewById(R.id.list_view_start_divider).setVisibility(8);
      break label129;
      label313: paramView.setGravity(19);
      paramView.setPadding((int)(getResources().getDimension(R.dimen.base_suggest_hotword_lr_margin) + getResources().getDimension(R.dimen.base_suggest_hotword_item_lr_margin)), 0, 0, 0);
    }
  }

  public abstract MApiRequest createRequest(String paramString);

  protected View createSuggestionItem(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if ((paramView instanceof NovaLinearLayout))
    {
      paramView = (NovaLinearLayout)paramView;
      ((TextView)paramView.findViewById(16908308)).setText(paramDPObject.getString("Keyword"));
      ((TextView)paramView.findViewById(16908308)).setPadding((int)(getResources().getDimension(R.dimen.base_suggest_hotword_lr_margin) + getResources().getDimension(R.dimen.base_suggest_hotword_item_lr_margin)), 0, 0, 0);
      paramViewGroup = paramDPObject.getString("DisplayInfo");
      if (TextUtils.isEmpty(paramViewGroup))
        break label169;
      ((TextView)paramView.findViewById(16908309)).setText(paramViewGroup);
    }
    while (true)
    {
      paramView.setGAString("suggest", paramDPObject.getString("Keyword"));
      paramView.gaUserInfo.keyword = this.searchEditText.getText().toString().trim();
      paramView.gaUserInfo.query_id = this.queryid;
      return paramView;
      paramView = (NovaLinearLayout)getActivity().getLayoutInflater().inflate(R.layout.search_list_item, paramViewGroup, false);
      break;
      label169: ((TextView)paramView.findViewById(16908309)).setText("共" + paramDPObject.getInt("Count") + "个结果");
    }
  }

  public abstract String getFileName();

  public View getHeaderView(String paramString, int paramInt)
  {
    LinearLayout localLinearLayout = new LinearLayout(getActivity());
    TextView localTextView = new TextView(getActivity());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2, 1.0F);
    localLayoutParams.leftMargin = (int)(getResources().getDimension(R.dimen.base_suggest_hotword_lr_margin) + getResources().getDimension(R.dimen.base_suggest_hotword_item_lr_margin));
    localLayoutParams.height = ViewUtils.dip2px(getActivity(), 40.0F);
    localTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
    localTextView.setLayoutParams(localLayoutParams);
    localTextView.setText(paramString);
    localTextView.setTextColor(getResources().getColor(R.color.light_gray));
    localTextView.setGravity(19);
    localLinearLayout.addView(localTextView);
    localLinearLayout.setTag(Integer.valueOf(paramInt));
    localLinearLayout.setClickable(true);
    this.mHeaderViews.add(localLinearLayout);
    localLinearLayout.setOnTouchListener(this.onTouchListener);
    return localLinearLayout;
  }

  protected int getHistoryCount()
  {
    return this.searchHistoryList.size();
  }

  public DPObject getHistoryWord(int paramInt)
  {
    if ((this.searchHistoryList != null) && (this.searchHistoryList.size() > paramInt))
      return parseSuggestDPObject((String)this.searchHistoryList.get(paramInt));
    return null;
  }

  protected View getHotWordView(ArrayList<DPObject> paramArrayList)
  {
    if (this.containerLayout == null)
    {
      this.containerLayout = new LinearLayout(getActivity());
      this.containerLayout.setOrientation(0);
      int i = getResources().getDimensionPixelOffset(R.dimen.base_suggest_hotword_lr_margin);
      this.containerLayout.setPadding(i, 0, i, 0);
      CustomGridView localCustomGridView = new CustomGridView(getActivity());
      paramArrayList = new HotWordAdapter(paramArrayList);
      localCustomGridView.setStretchAllColumns(true);
      localCustomGridView.setAdapter(paramArrayList);
      localCustomGridView.setOnItemClickListener(this);
      localCustomGridView.setTag(Integer.valueOf(1));
      localCustomGridView.setOnTouchListener(this.onTouchListener);
      this.containerLayout.addView(localCustomGridView);
      this.containerLayout.setClickable(true);
    }
    while (true)
    {
      this.mHeaderViews.add(this.containerLayout);
      return this.containerLayout;
      ((HotWordAdapter)((CustomGridView)this.containerLayout.getChildAt(0)).getAdapter()).setData(paramArrayList);
    }
  }

  protected BaseSuggestionAdapter getSuggestListAdapter(ArrayList<DPObject> paramArrayList)
  {
    if (this.suggestListAdapter == null)
      this.suggestListAdapter = new BaseSuggestionAdapter(paramArrayList);
    while (true)
    {
      return this.suggestListAdapter;
      this.suggestListAdapter.setSuggestionList(paramArrayList);
    }
  }

  protected ArrayList<DPObject> getSuggestListFromResponse(Object paramObject)
  {
    ArrayList localArrayList = new ArrayList();
    if ((paramObject instanceof DPObject))
    {
      paramObject = (DPObject)paramObject;
      this.queryid = paramObject.getString("QueryID");
      if (paramObject.getArray("List") != null)
        localArrayList.addAll(Arrays.asList(paramObject.getArray("List")));
    }
    return localArrayList;
  }

  protected View getSuggestionEmptyView(String paramString, ViewGroup paramViewGroup)
  {
    paramViewGroup = (NovaLinearLayout)getActivity().getLayoutInflater().inflate(R.layout.search_list_item, paramViewGroup, false);
    ((TextView)paramViewGroup.findViewById(16908308)).setText("查找'" + paramString + "'");
    return paramViewGroup;
  }

  protected void hotWordStyle(String paramString, TextView paramTextView, View paramView, int paramInt)
  {
    if ((paramTextView == null) || (paramView == null))
      return;
    if (("1".equals(paramString)) || ("2".equals(paramString)))
      paramTextView.setTextColor(getResources().getColor(R.color.light_red));
    while ("2".equals(paramString))
    {
      paramView.setVisibility(0);
      return;
      paramTextView.setTextColor(getResources().getColor(paramInt));
    }
    paramView.findViewById(R.id.icon).setVisibility(8);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.mContentResolver = getActivity().getContentResolver();
    if ((this.hasHotwordView) && (this.dpobjKeyword == null))
    {
      paramBundle = this.mHandler.obtainMessage(SEARCH_SUGGEST_MESSAGE, "");
      this.mHandler.sendMessage(paramBundle);
    }
    this.historyListAdapter = new HistoryAdapter();
    this.listView.setAdapter(this.historyListAdapter);
    this.mSearchMode = 1;
    this.listView.setOnItemClickListener(this);
    new Thread(new Runnable()
    {
      public void run()
      {
        AbstractSearchFragment.access$002(AbstractSearchFragment.this, HistorySearchSuggestionHelper.queryByChannel(AbstractSearchFragment.this.mContentResolver, AbstractSearchFragment.this.getFileName()));
        AbstractSearchFragment.this.mHandler.removeMessages(AbstractSearchFragment.HISTORY_SUGGEST_BACK);
        AbstractSearchFragment.this.mHandler.sendEmptyMessage(AbstractSearchFragment.HISTORY_SUGGEST_BACK);
      }
    }).start();
    KeyboardUtils.popupKeyboard(this.searchEditText);
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.back)
    {
      KeyboardUtils.hideKeyboard(this.searchEditText);
      if (getFragmentManager() != null)
        getFragmentManager().popBackStackImmediate();
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    try
    {
      if (this.mOnSearchFragmentListener == null)
        this.mOnSearchFragmentListener = ((OnSearchFragmentListener)getActivity());
      label23: if (getArguments() != null)
      {
        this.hasHotwordView = getArguments().getBoolean("hasHotwordView", true);
        this.searchHint = getArguments().getString("searchHint");
      }
      return;
    }
    catch (java.lang.ClassCastException paramBundle)
    {
      break label23;
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.base_search_layout, paramViewGroup, false);
    this.mClearButton = paramLayoutInflater.findViewById(R.id.clearBtn);
    this.mClearButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        AbstractSearchFragment.this.searchEditText.setText("");
      }
    });
    this.listView = ((ListView)paramLayoutInflater.findViewById(16908298));
    BitmapUtils.fixBackgroundRepeat(this.listView);
    this.listView.setCacheColorHint(0);
    this.listView.setDivider(null);
    this.listView.setOnTouchListener(this.onTouchListener);
    paramLayoutInflater.findViewById(R.id.back).setOnClickListener(this);
    this.searchEditText = ((EditText)paramLayoutInflater.findViewById(R.id.search_edit));
    this.searchEditText.setOnKeyListener(new View.OnKeyListener()
    {
      public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
      {
        int j = 0;
        int i = j;
        if (paramInt == 66)
        {
          i = j;
          if (paramKeyEvent.getAction() == 1)
          {
            if (!TextUtils.isEmpty(AbstractSearchFragment.this.searchEditText.getText().toString().trim()))
              break label53;
            i = 1;
          }
        }
        return i;
        label53: GAHelper.instance().contextStatisticsEvent(AbstractSearchFragment.this.getActivity(), "keyboardsearch", AbstractSearchFragment.this.searchEditText.getText().toString().trim(), 0, "tap");
        paramKeyEvent = new DPObject().edit().putString("Keyword", AbstractSearchFragment.this.searchEditText.getText().toString().trim());
        if (AbstractSearchFragment.this.queryid == null);
        for (paramView = ""; ; paramView = AbstractSearchFragment.this.queryid)
        {
          paramView = paramKeyEvent.putString("QueryID", paramView).generate();
          AbstractSearchFragment.this.search(paramView);
          return true;
        }
      }
    });
    this.textWatcher = new TextWatcher()
    {
      String mLastKeyword;

      public void afterTextChanged(Editable paramEditable)
      {
        paramEditable = paramEditable.toString().trim();
        Message localMessage;
        if (TextUtils.isEmpty(paramEditable))
        {
          AbstractSearchFragment.this.mClearButton.setVisibility(4);
          AbstractSearchFragment.this.mHandler.removeMessages(AbstractSearchFragment.SEARCH_SUGGEST_MESSAGE);
          if (AbstractSearchFragment.this.hasHotwordView)
          {
            localMessage = AbstractSearchFragment.this.mHandler.obtainMessage(AbstractSearchFragment.SEARCH_SUGGEST_MESSAGE, paramEditable);
            AbstractSearchFragment.this.mHandler.sendMessage(localMessage);
          }
          AbstractSearchFragment.this.listView.setAdapter(AbstractSearchFragment.this.historyListAdapter);
        }
        for (AbstractSearchFragment.this.mSearchMode = 1; ; AbstractSearchFragment.this.mSearchMode = 2)
        {
          this.mLastKeyword = paramEditable;
          return;
          AbstractSearchFragment.this.mClearButton.setVisibility(0);
          if (paramEditable.equals(this.mLastKeyword))
            continue;
          AbstractSearchFragment.this.mHandler.removeMessages(AbstractSearchFragment.SEARCH_SUGGEST_MESSAGE);
          localMessage = AbstractSearchFragment.this.mHandler.obtainMessage(AbstractSearchFragment.SEARCH_SUGGEST_MESSAGE, paramEditable);
          AbstractSearchFragment.this.mHandler.sendMessage(localMessage);
        }
      }

      public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }

      public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }
    };
    this.searchEditText.addTextChangedListener(this.textWatcher);
    if (!TextUtils.isEmpty(this.searchHint))
      this.searchEditText.setHint(this.searchHint);
    if ((this.dpobjKeyword != null) && (!TextUtils.isEmpty(this.dpobjKeyword.getString("Keyword"))))
    {
      this.searchEditText.setText(this.dpobjKeyword.getString("Keyword"));
      this.searchEditText.setSelection(this.searchEditText.getText().length());
    }
    paramViewGroup = (TextView)paramLayoutInflater.findViewById(R.id.searchBtn);
    paramViewGroup.setOnClickListener(new View.OnClickListener(paramViewGroup)
    {
      public void onClick(View paramView)
      {
        if (TextUtils.isEmpty(AbstractSearchFragment.this.searchEditText.getText().toString().trim()))
          return;
        Object localObject = new GAUserInfo();
        if (TextUtils.isEmpty(AbstractSearchFragment.this.searchEditText.getText().toString().trim()))
        {
          paramView = "";
          ((GAUserInfo)localObject).keyword = paramView;
          ((GAUserInfo)localObject).title = "";
          ((GAUserInfo)localObject).index = Integer.valueOf(-1);
          GAHelper localGAHelper = GAHelper.instance();
          Context localContext = this.val$searchBtn.getContext();
          if (!TextUtils.isEmpty(AbstractSearchFragment.this.searchEditText.getText().toString().trim()))
            break label234;
          paramView = "searchBtn";
          label113: localGAHelper.contextStatisticsEvent(localContext, paramView, (GAUserInfo)localObject, "tap");
          localObject = new DPObject().edit().putString("Keyword", AbstractSearchFragment.this.searchEditText.getText().toString().trim()).putString(AbstractSearchFragment.this.getResources().getString(R.string.search_keyword_ga_suffix), "_button");
          if (AbstractSearchFragment.this.queryid != null)
            break label240;
        }
        label234: label240: for (paramView = ""; ; paramView = AbstractSearchFragment.this.queryid)
        {
          paramView = ((DPObject.Editor)localObject).putString("QueryID", paramView).generate();
          AbstractSearchFragment.this.search(paramView);
          return;
          paramView = AbstractSearchFragment.this.searchEditText.getText().toString().trim();
          break;
          paramView = "buttonsearch";
          break label113;
        }
      }
    });
    paramLayoutInflater.setClickable(true);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    if (this.request != null)
    {
      mapiService().abort(this.request, this, false);
      this.request = null;
    }
    super.onDestroy();
  }

  public void onDetach()
  {
    if (this.mOnSearchFragmentListener != null)
    {
      this.mOnSearchFragmentListener.onSearchFragmentDetach();
      this.mOnSearchFragmentListener = null;
    }
    super.onDetach();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    Object localObject = this.listView.getItemAtPosition(paramInt);
    if (this.mSearchMode == 1)
      if (!(paramView.getTag() instanceof Integer));
    label243: 
    do
    {
      do
      {
        do
        {
          do
          {
            return;
            if (localObject != CLEARHISTORY)
              continue;
            new Thread(new Runnable()
            {
              public void run()
              {
                HistorySearchSuggestionHelper.deleteChannel(AbstractSearchFragment.this.mContentResolver, AbstractSearchFragment.this.getFileName());
              }
            }).start();
            this.searchHistoryList.clear();
            if (this.hasHotwordView)
              removeSearchListHeaderView();
            this.historyListAdapter.setHistoryList(this.searchHistoryList);
            this.mSearchMode = 1;
            return;
          }
          while (!(localObject instanceof DPObject));
          this.searchEditText.setText(((DPObject)localObject).getString("Keyword"));
          this.searchEditText.setSelection(this.searchEditText.getText().length());
          paramAdapterView = ((DPObject)localObject).getString("Value");
          if (paramAdapterView != null)
          {
            paramView = paramAdapterView;
            if (!paramAdapterView.contains("history"))
              if (!TextUtils.isEmpty(paramAdapterView))
                break label243;
          }
          for (paramView = paramAdapterView + "history%3A1"; ; paramView = paramAdapterView + "%3Bhistory%3A1")
          {
            search((DPObject)((DPObject)localObject).edit().putString(getResources().getString(R.string.search_keyword_ga_suffix), "_history").putString("Value", paramView).generate());
            return;
            paramAdapterView = "";
            break;
          }
        }
        while (this.mSearchMode != 2);
        if (!(localObject instanceof DPObject))
          continue;
        paramView = ((DPObject)localObject).edit().putString(getResources().getString(R.string.search_keyword_ga_suffix), "_suggest");
        if (this.queryid == null);
        for (paramAdapterView = ""; ; paramAdapterView = this.queryid)
        {
          search((DPObject)paramView.putString("QueryID", paramAdapterView).generate());
          return;
        }
      }
      while (localObject != NO_SUGGESTION);
      paramAdapterView = this.searchEditText.getText().toString().trim();
    }
    while (TextUtils.isEmpty(paramAdapterView));
    paramView = new DPObject().edit().putString("Keyword", paramAdapterView).putString(getResources().getString(R.string.search_keyword_ga_suffix), "_suggest");
    if (this.queryid == null);
    for (paramAdapterView = ""; ; paramAdapterView = this.queryid)
    {
      search(paramView.putString("QueryID", paramAdapterView).generate());
      return;
    }
  }

  public void onItemClick(CustomGridView paramCustomGridView, View paramView, int paramInt, long paramLong)
  {
    paramCustomGridView = (DPObject)paramView.getTag();
    if ((getActivity() instanceof DPActivity))
    {
      paramView = paramCustomGridView.edit().putString(getResources().getString(R.string.search_keyword_ga_suffix), "_hot").putInt(getResources().getString(R.string.search_keyword_ga_position), paramInt + 1);
      if (this.queryid != null)
        break label89;
    }
    label89: for (paramCustomGridView = ""; ; paramCustomGridView = this.queryid)
    {
      search(paramView.putString("QueryID", paramCustomGridView).generate());
      return;
    }
  }

  public void onPause()
  {
    super.onPause();
    new Handler().post(new Runnable()
    {
      public void run()
      {
        GAHelper.instance().setGAPageName(AbstractSearchFragment.this.referPageName);
        GAHelper.instance().setRequestId(AbstractSearchFragment.this.getActivity(), UUID.randomUUID().toString(), null, false);
      }
    });
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiResponse.message() != null) && (paramMApiResponse.message().content() != null))
      showToast(paramMApiResponse.message().content());
    this.request = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.request == paramMApiRequest)
    {
      if (paramMApiResponse.result() == null)
        break label82;
      if (getActivity() != null)
        break label30;
      this.request = null;
    }
    while (true)
    {
      return;
      label30: paramMApiResponse = getSuggestListFromResponse(paramMApiResponse.result());
      resetListView();
      this.listView.setHeaderDividersEnabled(false);
      if (this.mSearchMode != 2)
        break;
      this.mHeaderViews.clear();
      this.listView.setAdapter(getSuggestListAdapter(paramMApiResponse));
      label82: this.request = null;
      if (getActivity() == null)
        continue;
      paramMApiResponse = new GAUserInfo();
      paramMApiResponse.query_id = this.queryid;
      paramMApiRequest = Uri.parse(paramMApiRequest.url()).getQueryParameter("keyword");
      if (paramMApiRequest == null)
        break label242;
    }
    while (true)
    {
      paramMApiResponse.keyword = paramMApiRequest;
      GAHelper.instance().setRequestId(getActivity(), UUID.randomUUID().toString(), paramMApiResponse, false);
      return;
      if (this.mSearchMode != 1)
        break;
      if (this.hasHotwordView)
      {
        if ((paramMApiResponse != null) && (paramMApiResponse.size() > 0))
          this.listView.addHeaderView(getHeaderView("热门搜索", 0));
        this.listView.addHeaderView(getHotWordView(paramMApiResponse));
      }
      if (getHistoryCount() > 0)
        this.listView.addHeaderView(getHeaderView("搜索历史", 2));
      this.listView.setAdapter(this.historyListAdapter);
      break;
      label242: paramMApiRequest = "";
    }
  }

  public void onResume()
  {
    super.onResume();
    setGAPageName();
    GAHelper.instance().setRequestId(getActivity(), UUID.randomUUID().toString(), null, false);
  }

  public void removeSearchListHeaderView()
  {
    Iterator localIterator = this.mHeaderViews.iterator();
    while (localIterator.hasNext())
    {
      View localView = (View)localIterator.next();
      if ((localView.getTag() == null) || (!(localView.getTag() instanceof Integer)) || (Integer.parseInt(localView.getTag().toString()) != 2))
        continue;
      this.listView.removeHeaderView(localView);
    }
  }

  protected void resetListView()
  {
    Iterator localIterator = this.mHeaderViews.iterator();
    while (localIterator.hasNext())
    {
      View localView = (View)localIterator.next();
      this.listView.removeHeaderView(localView);
    }
    this.listView.setAdapter(null);
  }

  protected void search(DPObject paramDPObject)
  {
    if (paramDPObject == null);
    String str;
    do
    {
      return;
      str = paramDPObject.getString("Keyword");
      paramDPObject.getString("Value");
    }
    while (TextUtils.isEmpty(str));
    this.dpobjKeyword = paramDPObject;
    if (this.mOnSearchFragmentListener != null)
      this.mOnSearchFragmentListener.startSearch(this.dpobjKeyword);
    new Thread(new Runnable()
    {
      public void run()
      {
        String str = AbstractSearchFragment.this.getFileName();
        int i;
        if (!TextUtils.isEmpty(str))
        {
          i = str.indexOf(",");
          if (i != -1)
            break label68;
        }
        while (true)
        {
          JSONObject localJSONObject = AbstractSearchFragment.this.parseDPObjectToJson(AbstractSearchFragment.this.dpobjKeyword);
          if (!TextUtils.isEmpty(localJSONObject.toString()))
            HistorySearchSuggestionHelper.insert(AbstractSearchFragment.this.mContentResolver, localJSONObject.toString(), str);
          return;
          label68: str = str.substring(0, i);
        }
      }
    }).start();
    KeyboardUtils.hideKeyboard(this.searchEditText);
    this.listView.setVisibility(8);
    getFragmentManager().popBackStackImmediate();
  }

  protected void searchSuggest(String paramString)
  {
    if (this.request != null)
    {
      mapiService().abort(this.request, null, true);
      this.request = null;
    }
    this.request = createRequest(paramString);
    if (this.request == null)
      return;
    mapiService().exec(this.request, this);
  }

  protected void setGAPageName()
  {
    if ((getActivity() instanceof NovaActivity))
      this.referPageName = ((NovaActivity)getActivity()).getPageName();
    GAHelper.instance().setGAPageName("suggest");
  }

  public void setKeyword(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
      this.dpobjKeyword = new DPObject().edit().putString("Keyword", paramString).generate();
  }

  public void setOnSearchFragmentListener(OnSearchFragmentListener paramOnSearchFragmentListener)
  {
    this.mOnSearchFragmentListener = paramOnSearchFragmentListener;
  }

  protected final class BaseSuggestionAdapter extends BasicAdapter
  {
    private ArrayList<DPObject> suggestionList = new ArrayList();

    public BaseSuggestionAdapter()
    {
      Collection localCollection;
      this.suggestionList.addAll(localCollection);
    }

    public int getCount()
    {
      if (this.suggestionList.size() == 0)
        return 1;
      return this.suggestionList.size();
    }

    public Object getItem(int paramInt)
    {
      if (paramInt < this.suggestionList.size())
        return this.suggestionList.get(paramInt);
      return AbstractSearchFragment.NO_SUGGESTION;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = AbstractSearchFragment.this.suggestListAdapter.getItem(paramInt);
      if (localObject == AbstractSearchFragment.NO_SUGGESTION)
        return AbstractSearchFragment.this.getSuggestionEmptyView(AbstractSearchFragment.this.searchEditText.getText().toString(), paramViewGroup);
      return AbstractSearchFragment.this.createSuggestionItem((DPObject)localObject, paramInt, paramView, paramViewGroup);
    }

    public void setSuggestionList(ArrayList<DPObject> paramArrayList)
    {
      this.suggestionList.clear();
      this.suggestionList.addAll(paramArrayList);
      notifyDataSetChanged();
    }
  }

  protected class HistoryAdapter extends BasicAdapter
  {
    private ArrayList<DPObject> historyData = new ArrayList();

    public HistoryAdapter()
    {
    }

    public HistoryAdapter()
    {
      Collection localCollection;
      this.historyData.addAll(localCollection);
      if (this.historyData.size() > 0)
        this.historyData.add(AbstractSearchFragment.CLEARHISTORY);
    }

    public int getCount()
    {
      return this.historyData.size();
    }

    public Object getItem(int paramInt)
    {
      return this.historyData.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      return AbstractSearchFragment.this.createHistoryItem(paramInt, paramView, paramViewGroup);
    }

    public void setHistoryList(ArrayList<String> paramArrayList)
    {
      this.historyData.clear();
      if ((paramArrayList != null) && (paramArrayList.size() > 0))
      {
        int i = 0;
        while (i < paramArrayList.size())
        {
          Object localObject = (String)paramArrayList.get(i);
          localObject = AbstractSearchFragment.this.parseSuggestDPObject((String)localObject);
          this.historyData.add(localObject);
          i += 1;
        }
      }
      if (this.historyData.size() > 0)
        this.historyData.add(AbstractSearchFragment.CLEARHISTORY);
      notifyDataSetChanged();
    }
  }

  private class HotWordAdapter extends BaseAdapter
  {
    protected ArrayList<DPObject> tableData = null;

    public HotWordAdapter()
    {
      Object localObject;
      this.tableData = localObject;
    }

    public int getCount()
    {
      if (this.tableData == null)
        return 0;
      return this.tableData.size();
    }

    public Object getItem(int paramInt)
    {
      return this.tableData.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public String getItemName(int paramInt)
    {
      if (this.tableData.get(paramInt) == null)
        return " ";
      return ((DPObject)this.tableData.get(paramInt)).getString("Keyword");
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      DPObject localDPObject = (DPObject)getItem(paramInt);
      Object localObject2 = getItemName(paramInt);
      String str = localDPObject.getString("Templateid");
      Object localObject1;
      if (paramInt % 3 == 0)
      {
        localObject1 = new TableRow(paramViewGroup.getContext());
        paramView = (NovaRelativeLayout)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.find_hot_item, (TableRow)localObject1, false);
        paramViewGroup = (TextView)paramView.findViewById(R.id.text);
        paramViewGroup.setText((CharSequence)localObject2);
        ((TableRow)localObject1).addView(paramView);
        paramView.setTag(localDPObject);
        localObject2 = (LinearLayout.LayoutParams)paramView.getLayoutParams();
        if (paramInt < (getCount() - 1) / 3 * 3)
          break label309;
      }
      label309: for (((LinearLayout.LayoutParams)localObject2).bottomMargin = 0; ; ((LinearLayout.LayoutParams)localObject2).bottomMargin = AbstractSearchFragment.this.getResources().getDimensionPixelOffset(R.dimen.base_suggest_hotword_item_bottom_margin))
      {
        paramView.setLayoutParams((ViewGroup.LayoutParams)localObject2);
        localObject2 = paramView.findViewById(R.id.icon);
        AbstractSearchFragment.this.hotWordStyle(str, paramViewGroup, (View)localObject2, R.color.text_gray);
        paramView.setGAString("hotsuggest", localDPObject.getString("Keyword"));
        paramView.gaUserInfo.query_id = AbstractSearchFragment.this.queryid;
        paramView.gaUserInfo.keyword = String.valueOf(AbstractSearchFragment.this.searchEditText.getText());
        paramView.gaUserInfo.index = Integer.valueOf(paramInt);
        if (paramInt == 0)
          GAHelper.instance().contextStatisticsEvent(paramView.getContext(), "hotsuggest", paramView.gaUserInfo, "view");
        ((View)localObject1).setOnTouchListener(AbstractSearchFragment.this.onTouchListener);
        return localObject1;
        localObject1 = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.find_hot_item, ((CustomGridView)paramViewGroup).getCurRow(), false);
        paramView = (NovaRelativeLayout)localObject1;
        paramViewGroup = (TextView)paramView.findViewById(R.id.text);
        paramViewGroup.setText((CharSequence)localObject2);
        paramView.setTag(localDPObject);
        break;
      }
    }

    public void setData(ArrayList<DPObject> paramArrayList)
    {
      this.tableData = paramArrayList;
      notifyDataSetChanged();
    }
  }

  public static abstract interface OnSearchFragmentListener
  {
    public abstract void onSearchFragmentDetach();

    public abstract void startSearch(DPObject paramDPObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.AbstractSearchFragment
 * JD-Core Version:    0.6.0
 */