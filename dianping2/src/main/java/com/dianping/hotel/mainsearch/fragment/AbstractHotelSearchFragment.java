package com.dianping.hotel.mainsearch.fragment;

import android.content.Context;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaApplication;
import com.dianping.base.widget.NovaFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.BitmapUtils;
import com.dianping.util.KeyboardUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public abstract class AbstractHotelSearchFragment extends NovaFragment
  implements AdapterView.OnItemClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final DPObject CLEARHISTORY;
  private static final int NORMAL = 0;
  private static final Object NO_SUGGESTION;
  protected static final int SEARCH_MODE_HISTORY = 1;
  protected static final int SEARCH_MODE_SUGGEST = 2;
  private static final int SEARCH_SIZE_LIMIT = 10;
  protected static int SEARCH_SUGGEST_MESSAGE;
  private static final String TAG = AbstractHotelSearchFragment.class.getSimpleName();
  private int SEARCH_TYPE = 0;
  public DPObject dpobjKeyword;
  protected HistoryAdapter historyListAdapter;
  protected ListView listView;
  private View mClearButton;
  public OnSearchFragmentListener mOnSearchFragmentListener;
  protected int mSearchMode = 1;
  protected String queryid;
  protected MApiRequest request;
  protected EditText searchEditText;
  ArrayList<DPObject> searchHistoryList = new ArrayList();
  protected final Handler searchSuggestDelayHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if ((paramMessage.what == AbstractHotelSearchFragment.SEARCH_SUGGEST_MESSAGE) && ((paramMessage.obj instanceof String)))
      {
        paramMessage = (String)paramMessage.obj;
        AbstractHotelSearchFragment.this.searchSuggest(paramMessage);
      }
    }
  };
  protected BaseSuggestionAdapter suggestListAdapter;
  protected TextWatcher textWatcher;

  static
  {
    CLEARHISTORY = new DPObject().edit().putString("Keyword", "清除搜索记录").generate();
    NO_SUGGESTION = new Object();
    SEARCH_SUGGEST_MESSAGE = 1;
  }

  // ERROR //
  private DPObject getSearchHistory()
  {
    // Byte code:
    //   0: invokestatic 153	com/dianping/base/app/NovaApplication:instance	()Lcom/dianping/app/DPApplication;
    //   3: invokevirtual 159	com/dianping/app/DPApplication:getApplicationContext	()Landroid/content/Context;
    //   6: aload_0
    //   7: invokevirtual 162	com/dianping/hotel/mainsearch/fragment/AbstractHotelSearchFragment:getFileName	()Ljava/lang/String;
    //   10: invokevirtual 168	android/content/Context:getFileStreamPath	(Ljava/lang/String;)Ljava/io/File;
    //   13: astore_2
    //   14: aconst_null
    //   15: astore 4
    //   17: aconst_null
    //   18: astore_1
    //   19: aconst_null
    //   20: astore_3
    //   21: new 170	java/io/FileInputStream
    //   24: dup
    //   25: aload_2
    //   26: invokespecial 173	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   29: astore_2
    //   30: aload_2
    //   31: invokevirtual 177	java/io/FileInputStream:available	()I
    //   34: istore 5
    //   36: iload 5
    //   38: ifgt +21 -> 59
    //   41: aload_2
    //   42: ifnull +7 -> 49
    //   45: aload_2
    //   46: invokevirtual 180	java/io/FileInputStream:close	()V
    //   49: aconst_null
    //   50: areturn
    //   51: astore_1
    //   52: aload_1
    //   53: invokevirtual 183	java/io/IOException:printStackTrace	()V
    //   56: goto -7 -> 49
    //   59: iload 5
    //   61: newarray byte
    //   63: astore_1
    //   64: aload_2
    //   65: aload_1
    //   66: invokevirtual 187	java/io/FileInputStream:read	([B)I
    //   69: pop
    //   70: new 83	com/dianping/archive/DPObject
    //   73: dup
    //   74: aload_1
    //   75: iconst_0
    //   76: aload_1
    //   77: arraylength
    //   78: invokespecial 190	com/dianping/archive/DPObject:<init>	([BII)V
    //   81: astore_1
    //   82: aload_2
    //   83: ifnull +7 -> 90
    //   86: aload_2
    //   87: invokevirtual 180	java/io/FileInputStream:close	()V
    //   90: aload_1
    //   91: areturn
    //   92: astore_2
    //   93: aload_2
    //   94: invokevirtual 183	java/io/IOException:printStackTrace	()V
    //   97: goto -7 -> 90
    //   100: astore_1
    //   101: aload_3
    //   102: astore_2
    //   103: aload_1
    //   104: astore_3
    //   105: aload_2
    //   106: astore_1
    //   107: aload_3
    //   108: invokevirtual 191	java/io/FileNotFoundException:printStackTrace	()V
    //   111: aload_2
    //   112: ifnull -63 -> 49
    //   115: aload_2
    //   116: invokevirtual 180	java/io/FileInputStream:close	()V
    //   119: aconst_null
    //   120: areturn
    //   121: astore_1
    //   122: aload_1
    //   123: invokevirtual 183	java/io/IOException:printStackTrace	()V
    //   126: aconst_null
    //   127: areturn
    //   128: astore_3
    //   129: aload 4
    //   131: astore_2
    //   132: aload_2
    //   133: astore_1
    //   134: aload_3
    //   135: invokevirtual 183	java/io/IOException:printStackTrace	()V
    //   138: aload_2
    //   139: ifnull -90 -> 49
    //   142: aload_2
    //   143: invokevirtual 180	java/io/FileInputStream:close	()V
    //   146: aconst_null
    //   147: areturn
    //   148: astore_1
    //   149: aload_1
    //   150: invokevirtual 183	java/io/IOException:printStackTrace	()V
    //   153: aconst_null
    //   154: areturn
    //   155: astore_2
    //   156: aload_1
    //   157: ifnull +7 -> 164
    //   160: aload_1
    //   161: invokevirtual 180	java/io/FileInputStream:close	()V
    //   164: aload_2
    //   165: athrow
    //   166: astore_1
    //   167: aload_1
    //   168: invokevirtual 183	java/io/IOException:printStackTrace	()V
    //   171: goto -7 -> 164
    //   174: astore_3
    //   175: aload_2
    //   176: astore_1
    //   177: aload_3
    //   178: astore_2
    //   179: goto -23 -> 156
    //   182: astore_3
    //   183: goto -51 -> 132
    //   186: astore_3
    //   187: goto -82 -> 105
    //
    // Exception table:
    //   from	to	target	type
    //   45	49	51	java/io/IOException
    //   86	90	92	java/io/IOException
    //   21	30	100	java/io/FileNotFoundException
    //   115	119	121	java/io/IOException
    //   21	30	128	java/io/IOException
    //   142	146	148	java/io/IOException
    //   21	30	155	finally
    //   107	111	155	finally
    //   134	138	155	finally
    //   160	164	166	java/io/IOException
    //   30	36	174	finally
    //   59	82	174	finally
    //   30	36	182	java/io/IOException
    //   59	82	182	java/io/IOException
    //   30	36	186	java/io/FileNotFoundException
    //   59	82	186	java/io/FileNotFoundException
  }

  private void removeFromList(DPObject paramDPObject)
  {
    if (paramDPObject.getString("Keyword") == null);
    for (paramDPObject = ""; ; paramDPObject = paramDPObject.getString("Keyword"))
    {
      Object localObject2 = null;
      Iterator localIterator = this.searchHistoryList.iterator();
      Object localObject1;
      do
      {
        localObject1 = localObject2;
        if (!localIterator.hasNext())
          break;
        localObject1 = (DPObject)localIterator.next();
      }
      while (!paramDPObject.equals(((DPObject)localObject1).getString("Keyword")));
      if (localObject1 != null)
        this.searchHistoryList.remove(localObject1);
      return;
    }
  }

  private void removeHistory()
  {
    File localFile = NovaApplication.instance().getApplicationContext().getFileStreamPath(getFileName());
    if (!localFile.delete())
      localFile.deleteOnExit();
  }

  // ERROR //
  private void saveHistory()
  {
    // Byte code:
    //   0: new 83	com/dianping/archive/DPObject
    //   3: dup
    //   4: ldc 233
    //   6: invokespecial 235	com/dianping/archive/DPObject:<init>	(Ljava/lang/String;)V
    //   9: invokevirtual 90	com/dianping/archive/DPObject:edit	()Lcom/dianping/archive/DPObject$Editor;
    //   12: ldc 237
    //   14: aload_0
    //   15: getfield 129	com/dianping/hotel/mainsearch/fragment/AbstractHotelSearchFragment:searchHistoryList	Ljava/util/ArrayList;
    //   18: iconst_0
    //   19: anewarray 83	com/dianping/archive/DPObject
    //   22: invokevirtual 241	java/util/ArrayList:toArray	([Ljava/lang/Object;)[Ljava/lang/Object;
    //   25: checkcast 243	[Lcom/dianping/archive/DPObject;
    //   28: invokeinterface 247 3 0
    //   33: invokeinterface 104 1 0
    //   38: astore 5
    //   40: invokestatic 153	com/dianping/base/app/NovaApplication:instance	()Lcom/dianping/app/DPApplication;
    //   43: invokevirtual 159	com/dianping/app/DPApplication:getApplicationContext	()Landroid/content/Context;
    //   46: aload_0
    //   47: invokevirtual 162	com/dianping/hotel/mainsearch/fragment/AbstractHotelSearchFragment:getFileName	()Ljava/lang/String;
    //   50: invokevirtual 168	android/content/Context:getFileStreamPath	(Ljava/lang/String;)Ljava/io/File;
    //   53: astore_2
    //   54: aload_2
    //   55: invokevirtual 250	java/io/File:exists	()Z
    //   58: ifne +8 -> 66
    //   61: aload_2
    //   62: invokevirtual 253	java/io/File:createNewFile	()Z
    //   65: pop
    //   66: aconst_null
    //   67: astore 4
    //   69: aconst_null
    //   70: astore_1
    //   71: aconst_null
    //   72: astore_3
    //   73: new 255	java/io/FileOutputStream
    //   76: dup
    //   77: aload_2
    //   78: invokespecial 256	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   81: astore_2
    //   82: aload_2
    //   83: aload 5
    //   85: invokevirtual 260	com/dianping/archive/DPObject:toByteArray	()[B
    //   88: invokevirtual 264	java/io/FileOutputStream:write	([B)V
    //   91: aload_2
    //   92: ifnull +108 -> 200
    //   95: aload_2
    //   96: invokevirtual 265	java/io/FileOutputStream:close	()V
    //   99: return
    //   100: astore_1
    //   101: aload_1
    //   102: invokevirtual 183	java/io/IOException:printStackTrace	()V
    //   105: goto -39 -> 66
    //   108: astore_1
    //   109: aload_1
    //   110: invokevirtual 183	java/io/IOException:printStackTrace	()V
    //   113: return
    //   114: astore_1
    //   115: aload_3
    //   116: astore_2
    //   117: aload_1
    //   118: astore_3
    //   119: aload_2
    //   120: astore_1
    //   121: aload_3
    //   122: invokevirtual 191	java/io/FileNotFoundException:printStackTrace	()V
    //   125: aload_2
    //   126: ifnull -27 -> 99
    //   129: aload_2
    //   130: invokevirtual 265	java/io/FileOutputStream:close	()V
    //   133: return
    //   134: astore_1
    //   135: aload_1
    //   136: invokevirtual 183	java/io/IOException:printStackTrace	()V
    //   139: return
    //   140: astore_3
    //   141: aload 4
    //   143: astore_2
    //   144: aload_2
    //   145: astore_1
    //   146: aload_3
    //   147: invokevirtual 183	java/io/IOException:printStackTrace	()V
    //   150: aload_2
    //   151: ifnull -52 -> 99
    //   154: aload_2
    //   155: invokevirtual 265	java/io/FileOutputStream:close	()V
    //   158: return
    //   159: astore_1
    //   160: aload_1
    //   161: invokevirtual 183	java/io/IOException:printStackTrace	()V
    //   164: return
    //   165: astore_2
    //   166: aload_1
    //   167: ifnull +7 -> 174
    //   170: aload_1
    //   171: invokevirtual 265	java/io/FileOutputStream:close	()V
    //   174: aload_2
    //   175: athrow
    //   176: astore_1
    //   177: aload_1
    //   178: invokevirtual 183	java/io/IOException:printStackTrace	()V
    //   181: goto -7 -> 174
    //   184: astore_3
    //   185: aload_2
    //   186: astore_1
    //   187: aload_3
    //   188: astore_2
    //   189: goto -23 -> 166
    //   192: astore_3
    //   193: goto -49 -> 144
    //   196: astore_3
    //   197: goto -78 -> 119
    //   200: return
    //
    // Exception table:
    //   from	to	target	type
    //   61	66	100	java/io/IOException
    //   95	99	108	java/io/IOException
    //   73	82	114	java/io/FileNotFoundException
    //   129	133	134	java/io/IOException
    //   73	82	140	java/io/IOException
    //   154	158	159	java/io/IOException
    //   73	82	165	finally
    //   121	125	165	finally
    //   146	150	165	finally
    //   170	174	176	java/io/IOException
    //   82	91	184	finally
    //   82	91	192	java/io/IOException
    //   82	91	196	java/io/FileNotFoundException
  }

  private void searchSuggest(String paramString)
  {
    if (this.request != null)
    {
      mapiService().abort(this.request, null, true);
      return;
    }
    this.request = createRequest(paramString);
    mapiService().exec(this.request, this);
  }

  public abstract MApiRequest createRequest(String paramString);

  public abstract String getFileName();

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

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    paramBundle = getSearchHistory();
    if ((paramBundle != null) && (paramBundle.getArray("List") != null))
      this.searchHistoryList.addAll(Arrays.asList(paramBundle.getArray("List")));
    this.historyListAdapter = new HistoryAdapter(this.searchHistoryList);
    this.listView.setAdapter(this.historyListAdapter);
    this.mSearchMode = 1;
    this.listView.setOnItemClickListener(this);
    KeyboardUtils.popupKeyboard(this.searchEditText);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    try
    {
      if (this.mOnSearchFragmentListener == null)
        this.mOnSearchFragmentListener = ((OnSearchFragmentListener)getActivity());
      return;
    }
    catch (java.lang.ClassCastException paramBundle)
    {
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
        AbstractHotelSearchFragment.this.searchEditText.setText("");
        paramView = new DPObject().edit().putString("Keyword", "").generate();
        AbstractHotelSearchFragment.this.search(paramView);
      }
    });
    paramLayoutInflater.findViewById(R.id.searchBtn).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (TextUtils.isEmpty(AbstractHotelSearchFragment.this.searchEditText.getText().toString().trim()))
          return;
        paramView = new DPObject().edit().putString("Keyword", AbstractHotelSearchFragment.this.searchEditText.getText().toString().trim()).generate();
        AbstractHotelSearchFragment.this.search(paramView);
      }
    });
    this.listView = ((ListView)paramLayoutInflater.findViewById(16908298));
    BitmapUtils.fixBackgroundRepeat(this.listView);
    this.listView.setDivider(null);
    paramLayoutInflater.findViewById(R.id.back).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        KeyboardUtils.hideKeyboard(AbstractHotelSearchFragment.this.searchEditText);
        if (AbstractHotelSearchFragment.this.getFragmentManager() != null)
          AbstractHotelSearchFragment.this.getFragmentManager().popBackStackImmediate();
      }
    });
    this.searchEditText = ((EditText)paramLayoutInflater.findViewById(R.id.search_edit));
    this.searchEditText.setOnKeyListener(new View.OnKeyListener()
    {
      public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
      {
        if ((paramInt == 66) && (paramKeyEvent.getAction() == 1))
        {
          if (TextUtils.isEmpty(AbstractHotelSearchFragment.this.searchEditText.getText().toString().trim()))
            return true;
          paramView = new DPObject().edit().putString("Keyword", AbstractHotelSearchFragment.this.searchEditText.getText().toString().trim()).generate();
          AbstractHotelSearchFragment.this.search(paramView);
          return true;
        }
        return false;
      }
    });
    this.textWatcher = new TextWatcher()
    {
      String mLastKeyword;

      public void afterTextChanged(Editable paramEditable)
      {
        paramEditable = paramEditable.toString().trim();
        if (TextUtils.isEmpty(paramEditable))
        {
          AbstractHotelSearchFragment.this.mClearButton.setVisibility(4);
          AbstractHotelSearchFragment.this.searchSuggestDelayHandler.removeMessages(AbstractHotelSearchFragment.SEARCH_SUGGEST_MESSAGE);
          AbstractHotelSearchFragment.this.listView.setAdapter(AbstractHotelSearchFragment.this.historyListAdapter);
          AbstractHotelSearchFragment.this.mSearchMode = 1;
        }
        while (true)
        {
          this.mLastKeyword = paramEditable;
          return;
          AbstractHotelSearchFragment.this.mClearButton.setVisibility(0);
          if (paramEditable.equals(this.mLastKeyword))
            continue;
          AbstractHotelSearchFragment.this.searchSuggestDelayHandler.removeMessages(AbstractHotelSearchFragment.SEARCH_SUGGEST_MESSAGE);
          Message localMessage = AbstractHotelSearchFragment.this.searchSuggestDelayHandler.obtainMessage(AbstractHotelSearchFragment.SEARCH_SUGGEST_MESSAGE, paramEditable);
          AbstractHotelSearchFragment.this.searchSuggestDelayHandler.sendMessageDelayed(localMessage, 300L);
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
    if ((this.dpobjKeyword != null) && (!TextUtils.isEmpty(this.dpobjKeyword.getString("Keyword"))))
    {
      this.searchEditText.setText(this.dpobjKeyword.getString("Keyword"));
      this.searchEditText.setSelection(this.dpobjKeyword.getString("Keyword").length());
    }
    paramLayoutInflater.setClickable(true);
    return paramLayoutInflater;
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.request != null)
    {
      mapiService().abort(this.request, this, true);
      this.request = null;
    }
  }

  public void onDetach()
  {
    if (this.mOnSearchFragmentListener != null)
    {
      this.mOnSearchFragmentListener.OnSearchFragmentDetach();
      this.mOnSearchFragmentListener = null;
    }
    super.onDetach();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = this.listView.getItemAtPosition(paramInt);
    if (this.mSearchMode == 1)
      if (paramAdapterView == CLEARHISTORY)
      {
        removeHistory();
        this.searchHistoryList.clear();
        if ((this instanceof HotelSearchFragment))
          ((HotelSearchFragment)(HotelSearchFragment)this).removeSearchListHeaderView();
        this.historyListAdapter = new HistoryAdapter(this.searchHistoryList);
        this.listView.setAdapter(this.historyListAdapter);
        this.mSearchMode = 1;
      }
    label84: 
    do
    {
      do
      {
        do
        {
          break label84;
          do
            return;
          while (!(paramAdapterView instanceof DPObject));
          this.searchEditText.setText(((DPObject)paramAdapterView).getString("Keyword"));
          this.searchEditText.setSelection(this.searchEditText.getText().length());
          search((DPObject)paramAdapterView);
          return;
        }
        while (this.mSearchMode != 2);
        if (!(paramAdapterView instanceof DPObject))
          continue;
        search((DPObject)paramAdapterView);
        return;
      }
      while (paramAdapterView != NO_SUGGESTION);
      paramAdapterView = this.searchEditText.getText().toString().trim();
    }
    while (TextUtils.isEmpty(paramAdapterView));
    search(new DPObject().edit().putString("Keyword", paramAdapterView).generate());
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((this.request == paramMApiRequest) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      if (getActivity() == null)
        this.request = null;
    }
    else
      return;
    paramMApiRequest = (DPObject)paramMApiResponse.result();
    paramMApiResponse = new ArrayList();
    this.queryid = paramMApiRequest.getString("QueryID");
    paramMApiResponse.addAll(Arrays.asList(paramMApiRequest.getArray("List")));
    this.listView.setAdapter(getSuggestListAdapter(paramMApiResponse));
    this.mSearchMode = 2;
    this.request = null;
  }

  protected void search(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    this.dpobjKeyword = paramDPObject;
    if (this.mOnSearchFragmentListener != null)
      this.mOnSearchFragmentListener.startSearch(this.dpobjKeyword);
    removeFromList(this.dpobjKeyword);
    this.searchHistoryList.add(0, this.dpobjKeyword);
    if (this.searchHistoryList.size() == 11)
      this.searchHistoryList.remove(10);
    saveHistory();
    KeyboardUtils.hideKeyboard(this.searchEditText);
    this.listView.setVisibility(8);
    getFragmentManager().popBackStackImmediate();
  }

  private class BaseSuggestionAdapter extends BasicAdapter
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
      return AbstractHotelSearchFragment.NO_SUGGESTION;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (this.suggestionList.size() == 0)
      {
        paramView = (TextView)AbstractHotelSearchFragment.this.getActivity().getLayoutInflater().inflate(17367043, paramViewGroup, false);
        paramView.setText("查找'" + AbstractHotelSearchFragment.this.searchEditText.getText().toString() + "'");
        return paramView;
      }
      DPObject localDPObject = (DPObject)getItem(paramInt);
      if ((paramView instanceof LinearLayout));
      for (paramView = (LinearLayout)paramView; ; paramView = null)
      {
        Object localObject = paramView;
        if (paramView == null)
          localObject = (LinearLayout)AbstractHotelSearchFragment.this.getActivity().getLayoutInflater().inflate(R.layout.search_list_item, paramViewGroup, false);
        ((TextView)((LinearLayout)localObject).findViewById(16908308)).setText(localDPObject.getString("Keyword"));
        ((TextView)((LinearLayout)localObject).findViewById(16908309)).setText("共" + localDPObject.getInt("Count") + "个结果");
        return localObject;
      }
    }

    public void setSuggestionList(ArrayList<DPObject> paramArrayList)
    {
      this.suggestionList.clear();
      this.suggestionList.addAll(paramArrayList);
      notifyDataSetChanged();
    }
  }

  private class HistoryAdapter extends BasicAdapter
  {
    private ArrayList<DPObject> stringList = new ArrayList();

    public HistoryAdapter()
    {
      Collection localCollection;
      this.stringList.addAll(localCollection);
      if (this.stringList.size() > 0)
        this.stringList.add(AbstractHotelSearchFragment.CLEARHISTORY);
    }

    public int getCount()
    {
      return this.stringList.size();
    }

    public Object getItem(int paramInt)
    {
      return this.stringList.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      DPObject localDPObject = (DPObject)getItem(paramInt);
      View localView = paramView;
      paramView = localView;
      if (localView == null)
        paramView = AbstractHotelSearchFragment.this.getActivity().getLayoutInflater().inflate(R.layout.search_list_item, paramViewGroup, false);
      paramViewGroup = (TextView)paramView.findViewById(16908308);
      paramViewGroup.setText(localDPObject.getString("Keyword"));
      if (paramInt == getCount() - 1)
      {
        paramView.findViewById(R.id.divider).setVisibility(8);
        paramView.findViewById(R.id.list_view_end_divider).setVisibility(0);
        if (paramInt != 0)
          break label154;
        paramView.findViewById(R.id.list_view_start_divider).setVisibility(0);
      }
      while (true)
      {
        if (getItem(paramInt) != AbstractHotelSearchFragment.CLEARHISTORY)
          break label169;
        paramViewGroup.setGravity(17);
        return paramView;
        paramView.findViewById(R.id.divider).setVisibility(0);
        paramView.findViewById(R.id.list_view_end_divider).setVisibility(8);
        break;
        label154: paramView.findViewById(R.id.list_view_start_divider).setVisibility(8);
      }
      label169: paramViewGroup.setGravity(19);
      return paramView;
    }
  }

  public static abstract interface OnSearchFragmentListener
  {
    public abstract void OnSearchFragmentDetach();

    public abstract void startSearch(DPObject paramDPObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.mainsearch.fragment.AbstractHotelSearchFragment
 * JD-Core Version:    0.6.0
 */