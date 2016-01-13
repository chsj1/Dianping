package com.dianping.hotel.shoplist.fragement;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaApplication;
import com.dianping.base.basic.AbstractSearchFragment.OnSearchFragmentListener;
import com.dianping.base.widget.NovaFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.BitmapUtils;
import com.dianping.util.KeyboardUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class HotelSearchSuggestFragment extends NovaFragment
  implements AdapterView.OnItemClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final DPObject CLEARHISTORY = new DPObject().edit().putString("Keyword", "清除搜索记录").generate();
  private static final int HEADER_VIEW_HOT_SEARCH = 0;
  private static final int HEADER_VIEW_HOT_SEARCH_KEYWORDS = 1;
  private static final int HEADER_VIEW_SEARCH_HISTORY = 2;
  private static final int NORMAL = 0;
  private static final Object NO_SUGGESTION = new Object();
  protected static final int SEARCH_MODE_HISTORY = 1;
  protected static final int SEARCH_MODE_SUGGEST = 2;
  private static final int SEARCH_SIZE_LIMIT = 10;
  protected static int SEARCH_SUGGEST_MESSAGE = 0;
  private static final int SEARCH_SUGGEST_TYPE_ADVANCE = 1;
  private static final int SEARCH_SUGGEST_TYPE_HOTWORD = 2;
  private static final int SEARCH_SUGGEST_TYPE_NORMAL = 0;
  private static final int TEXT_MARGIN = 8;
  protected static final String URL = "http://m.api.dianping.com/";
  private static String keyword;
  private int SEARCH_TYPE = 0;
  private DPObject dpobjKeyword;
  protected HistoryAdapter historyListAdapter;
  private ArrayList<DPObject> hotWordList = new ArrayList();
  protected ListView listView;
  private View mClearButton;
  private List<View> mHeaderViews = new ArrayList();
  private AbstractSearchFragment.OnSearchFragmentListener mOnSearchFragmentListener;
  protected int mSearchMode = 1;
  LinearLayout.LayoutParams mTextViewlayoutParams;
  protected String queryid;
  private MApiRequest requestHotWords;
  private MApiRequest requestSuggestWords;
  protected EditText searchEditText;
  private ArrayList<DPObject> searchHistoryList = new ArrayList();
  protected final Handler searchSuggestDelayHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      if ((paramMessage.what == HotelSearchSuggestFragment.SEARCH_SUGGEST_MESSAGE) && ((paramMessage.obj instanceof String)))
      {
        paramMessage = (String)paramMessage.obj;
        HotelSearchSuggestFragment.this.searchSuggest(paramMessage);
      }
    }
  };
  protected BaseSuggestionAdapter suggestListAdapter;
  protected TextWatcher textWatcher;

  private MApiRequest createHotWordsRequest()
  {
    CacheType localCacheType = CacheType.DISABLED;
    StringBuilder localStringBuilder = new StringBuilder("http://m.api.dianping.com/hotel/hotsearchlist.hotel?");
    localStringBuilder.append("cityid=").append(cityId());
    boolean bool2 = false;
    try
    {
      String str = getActivity().getIntent().getData().getQueryParameter("searcharoundcities");
      bool1 = bool2;
      if (!TextUtils.isEmpty(str))
        bool1 = Boolean.parseBoolean(str);
      localStringBuilder.append("&searcharoundcities=").append(bool1);
      this.requestHotWords = mapiGet(this, localStringBuilder.toString(), localCacheType);
      return this.requestHotWords;
    }
    catch (Exception localException)
    {
      while (true)
        boolean bool1 = bool2;
    }
  }

  // ERROR //
  private MApiRequest createSuggestWordsRequest(String paramString)
  {
    // Byte code:
    //   0: getstatic 256	com/dianping/dataservice/mapi/CacheType:HOURLY	Lcom/dianping/dataservice/mapi/CacheType;
    //   3: astore_2
    //   4: new 181	java/lang/StringBuilder
    //   7: dup
    //   8: ldc_w 258
    //   11: invokespecial 185	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   14: astore_3
    //   15: aload_3
    //   16: ldc 187
    //   18: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   21: aload_0
    //   22: invokevirtual 195	com/dianping/hotel/shoplist/fragement/HotelSearchSuggestFragment:cityId	()I
    //   25: invokevirtual 198	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   28: pop
    //   29: aload_1
    //   30: invokestatic 228	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   33: ifne +243 -> 276
    //   36: aload_3
    //   37: ldc_w 260
    //   40: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   43: aload_1
    //   44: ldc_w 262
    //   47: invokestatic 268	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   50: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   53: pop
    //   54: aload_2
    //   55: astore_1
    //   56: aload_0
    //   57: invokevirtual 272	com/dianping/hotel/shoplist/fragement/HotelSearchSuggestFragment:locationService	()Lcom/dianping/locationservice/LocationService;
    //   60: invokeinterface 277 1 0
    //   65: astore_2
    //   66: aload_2
    //   67: ifnull +87 -> 154
    //   70: aload_2
    //   71: ldc_w 279
    //   74: invokevirtual 283	com/dianping/archive/DPObject:getDouble	(Ljava/lang/String;)D
    //   77: dconst_0
    //   78: dcmpl
    //   79: ifeq +75 -> 154
    //   82: aload_2
    //   83: ldc_w 285
    //   86: invokevirtual 283	com/dianping/archive/DPObject:getDouble	(Ljava/lang/String;)D
    //   89: dconst_0
    //   90: dcmpl
    //   91: ifeq +63 -> 154
    //   94: aload_3
    //   95: ldc_w 287
    //   98: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: ldc_w 289
    //   104: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   107: getstatic 295	com/dianping/model/Location:FMT	Ljava/text/DecimalFormat;
    //   110: aload_2
    //   111: ldc_w 279
    //   114: invokevirtual 283	com/dianping/archive/DPObject:getDouble	(Ljava/lang/String;)D
    //   117: invokevirtual 301	java/text/DecimalFormat:format	(D)Ljava/lang/String;
    //   120: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   123: pop
    //   124: aload_3
    //   125: ldc_w 287
    //   128: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: ldc_w 303
    //   134: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   137: getstatic 295	com/dianping/model/Location:FMT	Ljava/text/DecimalFormat;
    //   140: aload_2
    //   141: ldc_w 285
    //   144: invokevirtual 283	com/dianping/archive/DPObject:getDouble	(Ljava/lang/String;)D
    //   147: invokevirtual 301	java/text/DecimalFormat:format	(D)Ljava/lang/String;
    //   150: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   153: pop
    //   154: aload_2
    //   155: ifnull +37 -> 192
    //   158: aload_2
    //   159: ldc_w 305
    //   162: invokevirtual 309	com/dianping/archive/DPObject:getInt	(Ljava/lang/String;)I
    //   165: ifle +27 -> 192
    //   168: aload_3
    //   169: ldc_w 287
    //   172: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   175: ldc_w 311
    //   178: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   181: aload_2
    //   182: ldc_w 305
    //   185: invokevirtual 309	com/dianping/archive/DPObject:getInt	(Ljava/lang/String;)I
    //   188: invokevirtual 198	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   191: pop
    //   192: iconst_0
    //   193: istore 5
    //   195: aload_0
    //   196: invokevirtual 202	com/dianping/hotel/shoplist/fragement/HotelSearchSuggestFragment:getActivity	()Landroid/support/v4/app/FragmentActivity;
    //   199: invokevirtual 208	android/support/v4/app/FragmentActivity:getIntent	()Landroid/content/Intent;
    //   202: invokevirtual 214	android/content/Intent:getData	()Landroid/net/Uri;
    //   205: ldc 216
    //   207: invokevirtual 222	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   210: astore_2
    //   211: iload 5
    //   213: istore 4
    //   215: aload_2
    //   216: invokestatic 228	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   219: ifne +9 -> 228
    //   222: aload_2
    //   223: invokestatic 234	java/lang/Boolean:parseBoolean	(Ljava/lang/String;)Z
    //   226: istore 4
    //   228: aload_3
    //   229: ldc_w 287
    //   232: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   235: ldc_w 313
    //   238: invokevirtual 191	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   241: iload 4
    //   243: invokevirtual 239	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
    //   246: pop
    //   247: aload_0
    //   248: aload_0
    //   249: aload_0
    //   250: aload_3
    //   251: invokevirtual 243	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   254: aload_1
    //   255: invokevirtual 247	com/dianping/hotel/shoplist/fragement/HotelSearchSuggestFragment:mapiGet	(Lcom/dianping/dataservice/RequestHandler;Ljava/lang/String;Lcom/dianping/dataservice/mapi/CacheType;)Lcom/dianping/dataservice/mapi/MApiRequest;
    //   258: putfield 315	com/dianping/hotel/shoplist/fragement/HotelSearchSuggestFragment:requestSuggestWords	Lcom/dianping/dataservice/mapi/MApiRequest;
    //   261: aload_0
    //   262: getfield 315	com/dianping/hotel/shoplist/fragement/HotelSearchSuggestFragment:requestSuggestWords	Lcom/dianping/dataservice/mapi/MApiRequest;
    //   265: areturn
    //   266: astore_1
    //   267: aload_1
    //   268: invokevirtual 318	java/io/UnsupportedEncodingException:printStackTrace	()V
    //   271: aload_2
    //   272: astore_1
    //   273: goto -217 -> 56
    //   276: getstatic 321	com/dianping/dataservice/mapi/CacheType:CRITICAL	Lcom/dianping/dataservice/mapi/CacheType;
    //   279: astore_1
    //   280: goto -224 -> 56
    //   283: astore_2
    //   284: iload 5
    //   286: istore 4
    //   288: goto -60 -> 228
    //
    // Exception table:
    //   from	to	target	type
    //   36	54	266	java/io/UnsupportedEncodingException
    //   195	211	283	java/lang/Exception
    //   215	228	283	java/lang/Exception
  }

  private View getHotWordView(ArrayList<DPObject> paramArrayList)
  {
    if (getActivity() == null)
      return null;
    LinearLayout localLinearLayout2 = new LinearLayout(getActivity());
    localLinearLayout2.setOrientation(1);
    int i = ViewUtils.dip2px(getActivity(), 15.0F);
    localLinearLayout2.setPadding(i, 0, i, 0);
    calcHotWordsCount(paramArrayList, 14, 10, localLinearLayout2);
    int j = paramArrayList.size();
    LinearLayout localLinearLayout1 = null;
    i = 0;
    while (i < j)
    {
      if (i % 3 == 0)
      {
        localLinearLayout1 = new LinearLayout(getActivity());
        localLinearLayout1.setOrientation(0);
        localObject = new LinearLayout.LayoutParams(-1, -2);
        ((LinearLayout.LayoutParams)localObject).bottomMargin = ViewUtils.dip2px(getActivity(), 8.0F);
        localLinearLayout1.setLayoutParams((ViewGroup.LayoutParams)localObject);
        localLinearLayout2.addView(localLinearLayout1);
      }
      Object localObject = getTextView((DPObject)paramArrayList.get(i), 2);
      ((TextView)localObject).setGravity(17);
      ((TextView)localObject).setSingleLine();
      ((TextView)localObject).setEllipsize(TextUtils.TruncateAt.END);
      ((TextView)localObject).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
      ((TextView)localObject).setBackgroundDrawable(getResources().getDrawable(R.drawable.background_round_textview));
      LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)((TextView)localObject).getLayoutParams();
      localLayoutParams.height = ViewUtils.dip2px(getActivity(), 38.0F);
      if (((i + 1) % 3 != 0) && (i != j - 1))
        localLayoutParams.rightMargin = ViewUtils.dip2px(getActivity(), 10.0F);
      localLinearLayout1.addView((View)localObject);
      i += 1;
    }
    localLinearLayout2.setTag(Integer.valueOf(1));
    this.mHeaderViews.add(localLinearLayout2);
    return (View)localLinearLayout2;
  }

  // ERROR //
  private DPObject getSearchHistory()
  {
    // Byte code:
    //   0: invokestatic 464	com/dianping/base/app/NovaApplication:instance	()Lcom/dianping/app/DPApplication;
    //   3: invokevirtual 470	com/dianping/app/DPApplication:getApplicationContext	()Landroid/content/Context;
    //   6: aload_0
    //   7: invokevirtual 473	com/dianping/hotel/shoplist/fragement/HotelSearchSuggestFragment:getFileName	()Ljava/lang/String;
    //   10: invokevirtual 479	android/content/Context:getFileStreamPath	(Ljava/lang/String;)Ljava/io/File;
    //   13: astore_2
    //   14: aconst_null
    //   15: astore 4
    //   17: aconst_null
    //   18: astore_1
    //   19: aconst_null
    //   20: astore_3
    //   21: new 481	java/io/FileInputStream
    //   24: dup
    //   25: aload_2
    //   26: invokespecial 484	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   29: astore_2
    //   30: aload_2
    //   31: invokevirtual 487	java/io/FileInputStream:available	()I
    //   34: istore 5
    //   36: iload 5
    //   38: ifgt +21 -> 59
    //   41: aload_2
    //   42: ifnull +7 -> 49
    //   45: aload_2
    //   46: invokevirtual 490	java/io/FileInputStream:close	()V
    //   49: aconst_null
    //   50: areturn
    //   51: astore_1
    //   52: aload_1
    //   53: invokevirtual 491	java/io/IOException:printStackTrace	()V
    //   56: goto -7 -> 49
    //   59: iload 5
    //   61: newarray byte
    //   63: astore_1
    //   64: aload_2
    //   65: aload_1
    //   66: invokevirtual 495	java/io/FileInputStream:read	([B)I
    //   69: pop
    //   70: new 94	com/dianping/archive/DPObject
    //   73: dup
    //   74: aload_1
    //   75: iconst_0
    //   76: aload_1
    //   77: arraylength
    //   78: invokespecial 498	com/dianping/archive/DPObject:<init>	([BII)V
    //   81: astore_1
    //   82: aload_2
    //   83: ifnull +7 -> 90
    //   86: aload_2
    //   87: invokevirtual 490	java/io/FileInputStream:close	()V
    //   90: aload_1
    //   91: areturn
    //   92: astore_2
    //   93: aload_2
    //   94: invokevirtual 491	java/io/IOException:printStackTrace	()V
    //   97: goto -7 -> 90
    //   100: astore_1
    //   101: aload_3
    //   102: astore_2
    //   103: aload_1
    //   104: astore_3
    //   105: aload_2
    //   106: astore_1
    //   107: aload_3
    //   108: invokevirtual 499	java/io/FileNotFoundException:printStackTrace	()V
    //   111: aload_2
    //   112: ifnull -63 -> 49
    //   115: aload_2
    //   116: invokevirtual 490	java/io/FileInputStream:close	()V
    //   119: aconst_null
    //   120: areturn
    //   121: astore_1
    //   122: aload_1
    //   123: invokevirtual 491	java/io/IOException:printStackTrace	()V
    //   126: aconst_null
    //   127: areturn
    //   128: astore_3
    //   129: aload 4
    //   131: astore_2
    //   132: aload_2
    //   133: astore_1
    //   134: aload_3
    //   135: invokevirtual 491	java/io/IOException:printStackTrace	()V
    //   138: aload_2
    //   139: ifnull -90 -> 49
    //   142: aload_2
    //   143: invokevirtual 490	java/io/FileInputStream:close	()V
    //   146: aconst_null
    //   147: areturn
    //   148: astore_1
    //   149: aload_1
    //   150: invokevirtual 491	java/io/IOException:printStackTrace	()V
    //   153: aconst_null
    //   154: areturn
    //   155: astore_2
    //   156: aload_1
    //   157: ifnull +7 -> 164
    //   160: aload_1
    //   161: invokevirtual 490	java/io/FileInputStream:close	()V
    //   164: aload_2
    //   165: athrow
    //   166: astore_1
    //   167: aload_1
    //   168: invokevirtual 491	java/io/IOException:printStackTrace	()V
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

  private TextView getTextView(DPObject paramDPObject, int paramInt)
  {
    TextView localTextView = new TextView(getActivity());
    if (paramInt == 1)
    {
      localTextView.setLayoutParams(this.mTextViewlayoutParams);
      localTextView.setBackgroundResource(R.drawable.simple_item);
      if (paramDPObject == null)
        break label117;
      localTextView.setText(paramDPObject.getString("Keyword"));
    }
    while (true)
    {
      localTextView.setTextColor(getResources().getColor(R.color.clickable_deep_black));
      localTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
      localTextView.setOnClickListener(new View.OnClickListener(paramInt, paramDPObject)
      {
        public void onClick(View paramView)
        {
          if ((HotelSearchSuggestFragment.this.getActivity() instanceof DPActivity))
          {
            if (1 != this.val$suggestType)
              break label60;
            ((DPActivity)HotelSearchSuggestFragment.this.getActivity()).statisticsEvent("index5", "index_search_adsuggest", this.val$suggest.getString("Keyword"), 0);
            HotelSearchSuggestFragment.this.search(this.val$suggest);
          }
          label60: 
          do
            return;
          while (2 != this.val$suggestType);
          ((DPActivity)HotelSearchSuggestFragment.this.getActivity()).statisticsEvent("index5", "index_search_hotsuggest", "", 0);
          HotelSearchSuggestFragment.this.search(this.val$suggest.edit().putString(HotelSearchSuggestFragment.this.getResources().getString(R.string.search_keyword_ga_suffix), "_hot").generate());
        }
      });
      return localTextView;
      localTextView.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0F));
      localTextView.setBackgroundResource(R.drawable.background_round_textview);
      break;
      label117: localTextView.setText("test");
    }
  }

  public static HotelSearchSuggestFragment newInstance(FragmentActivity paramFragmentActivity)
  {
    return newInstance(paramFragmentActivity, null);
  }

  public static HotelSearchSuggestFragment newInstance(FragmentActivity paramFragmentActivity, String paramString)
  {
    HotelSearchSuggestFragment localHotelSearchSuggestFragment = new HotelSearchSuggestFragment();
    paramFragmentActivity = paramFragmentActivity.getSupportFragmentManager().beginTransaction();
    paramFragmentActivity.add(16908290, localHotelSearchSuggestFragment);
    paramFragmentActivity.addToBackStack(null);
    paramFragmentActivity.commitAllowingStateLoss();
    keyword = paramString;
    return localHotelSearchSuggestFragment;
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

  private void resetListView()
  {
    Iterator localIterator = this.mHeaderViews.iterator();
    while (localIterator.hasNext())
    {
      View localView = (View)localIterator.next();
      this.listView.removeHeaderView(localView);
    }
    this.listView.setAdapter(null);
  }

  // ERROR //
  private void saveHistory()
  {
    // Byte code:
    //   0: new 94	com/dianping/archive/DPObject
    //   3: dup
    //   4: ldc_w 622
    //   7: invokespecial 623	com/dianping/archive/DPObject:<init>	(Ljava/lang/String;)V
    //   10: invokevirtual 101	com/dianping/archive/DPObject:edit	()Lcom/dianping/archive/DPObject$Editor;
    //   13: ldc_w 625
    //   16: aload_0
    //   17: getfield 142	com/dianping/hotel/shoplist/fragement/HotelSearchSuggestFragment:searchHistoryList	Ljava/util/ArrayList;
    //   20: iconst_0
    //   21: anewarray 94	com/dianping/archive/DPObject
    //   24: invokevirtual 629	java/util/ArrayList:toArray	([Ljava/lang/Object;)[Ljava/lang/Object;
    //   27: checkcast 631	[Lcom/dianping/archive/DPObject;
    //   30: invokeinterface 635 3 0
    //   35: invokeinterface 115 1 0
    //   40: astore 5
    //   42: invokestatic 464	com/dianping/base/app/NovaApplication:instance	()Lcom/dianping/app/DPApplication;
    //   45: invokevirtual 470	com/dianping/app/DPApplication:getApplicationContext	()Landroid/content/Context;
    //   48: aload_0
    //   49: invokevirtual 473	com/dianping/hotel/shoplist/fragement/HotelSearchSuggestFragment:getFileName	()Ljava/lang/String;
    //   52: invokevirtual 479	android/content/Context:getFileStreamPath	(Ljava/lang/String;)Ljava/io/File;
    //   55: astore_2
    //   56: aload_2
    //   57: invokevirtual 638	java/io/File:exists	()Z
    //   60: ifne +8 -> 68
    //   63: aload_2
    //   64: invokevirtual 641	java/io/File:createNewFile	()Z
    //   67: pop
    //   68: aconst_null
    //   69: astore 4
    //   71: aconst_null
    //   72: astore_1
    //   73: aconst_null
    //   74: astore_3
    //   75: new 643	java/io/FileOutputStream
    //   78: dup
    //   79: aload_2
    //   80: invokespecial 644	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   83: astore_2
    //   84: aload_2
    //   85: aload 5
    //   87: invokevirtual 648	com/dianping/archive/DPObject:toByteArray	()[B
    //   90: invokevirtual 652	java/io/FileOutputStream:write	([B)V
    //   93: aload_2
    //   94: ifnull +108 -> 202
    //   97: aload_2
    //   98: invokevirtual 653	java/io/FileOutputStream:close	()V
    //   101: return
    //   102: astore_1
    //   103: aload_1
    //   104: invokevirtual 491	java/io/IOException:printStackTrace	()V
    //   107: goto -39 -> 68
    //   110: astore_1
    //   111: aload_1
    //   112: invokevirtual 491	java/io/IOException:printStackTrace	()V
    //   115: return
    //   116: astore_1
    //   117: aload_3
    //   118: astore_2
    //   119: aload_1
    //   120: astore_3
    //   121: aload_2
    //   122: astore_1
    //   123: aload_3
    //   124: invokevirtual 499	java/io/FileNotFoundException:printStackTrace	()V
    //   127: aload_2
    //   128: ifnull -27 -> 101
    //   131: aload_2
    //   132: invokevirtual 653	java/io/FileOutputStream:close	()V
    //   135: return
    //   136: astore_1
    //   137: aload_1
    //   138: invokevirtual 491	java/io/IOException:printStackTrace	()V
    //   141: return
    //   142: astore_3
    //   143: aload 4
    //   145: astore_2
    //   146: aload_2
    //   147: astore_1
    //   148: aload_3
    //   149: invokevirtual 491	java/io/IOException:printStackTrace	()V
    //   152: aload_2
    //   153: ifnull -52 -> 101
    //   156: aload_2
    //   157: invokevirtual 653	java/io/FileOutputStream:close	()V
    //   160: return
    //   161: astore_1
    //   162: aload_1
    //   163: invokevirtual 491	java/io/IOException:printStackTrace	()V
    //   166: return
    //   167: astore_2
    //   168: aload_1
    //   169: ifnull +7 -> 176
    //   172: aload_1
    //   173: invokevirtual 653	java/io/FileOutputStream:close	()V
    //   176: aload_2
    //   177: athrow
    //   178: astore_1
    //   179: aload_1
    //   180: invokevirtual 491	java/io/IOException:printStackTrace	()V
    //   183: goto -7 -> 176
    //   186: astore_3
    //   187: aload_2
    //   188: astore_1
    //   189: aload_3
    //   190: astore_2
    //   191: goto -23 -> 168
    //   194: astore_3
    //   195: goto -49 -> 146
    //   198: astore_3
    //   199: goto -78 -> 121
    //   202: return
    //
    // Exception table:
    //   from	to	target	type
    //   63	68	102	java/io/IOException
    //   97	101	110	java/io/IOException
    //   75	84	116	java/io/FileNotFoundException
    //   131	135	136	java/io/IOException
    //   75	84	142	java/io/IOException
    //   156	160	161	java/io/IOException
    //   75	84	167	finally
    //   123	127	167	finally
    //   148	152	167	finally
    //   172	176	178	java/io/IOException
    //   84	93	186	finally
    //   84	93	194	java/io/IOException
    //   84	93	198	java/io/FileNotFoundException
  }

  private void searchSuggest(String paramString)
  {
    if (this.requestSuggestWords != null)
    {
      mapiService().abort(this.requestSuggestWords, null, true);
      return;
    }
    this.requestSuggestWords = createSuggestWordsRequest(paramString);
    mapiService().exec(this.requestSuggestWords, this);
  }

  public int calcHotWordsCount(ArrayList<DPObject> paramArrayList, int paramInt1, int paramInt2, LinearLayout paramLinearLayout)
  {
    if (!isAdded())
    {
      if (paramArrayList.size() > 3)
        return 3;
      return paramArrayList.size();
    }
    int i = getActivity().getWindowManager().getDefaultDisplay().getWidth();
    int j = i;
    if (paramLinearLayout.getLayoutParams() != null)
      j = i - ((LinearLayout.LayoutParams)paramLinearLayout.getLayoutParams()).leftMargin - ((LinearLayout.LayoutParams)paramLinearLayout.getLayoutParams()).rightMargin;
    int i1 = paramLinearLayout.getPaddingLeft();
    int i2 = paramLinearLayout.getPaddingRight();
    int k = 0;
    paramLinearLayout = new int[3];
    i = 0;
    while ((i < paramArrayList.size()) && (i < 3))
    {
      int n = getTextWidth(((DPObject)paramArrayList.get(i)).getString("Keyword"), paramInt1);
      int m = k;
      if (n > k)
        m = n;
      paramLinearLayout[i] = m;
      i += 1;
      k = m;
    }
    paramInt1 = ViewUtils.dip2px(getActivity(), paramInt2);
    while (true)
    {
      if ((i <= 1) || (paramLinearLayout[(i - 1)] * i + (i - 1) * paramInt1 < j - i1 - i2))
        return i;
      i -= 1;
    }
  }

  public String getFileName()
  {
    return "hotel_search_fragment";
  }

  public View getHeaderView(String paramString, int paramInt)
  {
    if (getActivity() == null)
      return null;
    LinearLayout localLinearLayout = new LinearLayout(getActivity());
    TextView localTextView = new TextView(getActivity());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2, 1.0F);
    localLayoutParams.leftMargin = ViewUtils.dip2px(getActivity(), 15.0F);
    localLayoutParams.height = ViewUtils.dip2px(getActivity(), 50.0F);
    localTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
    localTextView.setLayoutParams(localLayoutParams);
    localTextView.setText(paramString);
    localTextView.setTextColor(getResources().getColor(R.color.light_gray));
    localTextView.setGravity(19);
    localLinearLayout.addView(localTextView);
    localLinearLayout.setTag(Integer.valueOf(paramInt));
    this.mHeaderViews.add(localLinearLayout);
    return localLinearLayout;
  }

  protected int getHistoryCount()
  {
    return this.searchHistoryList.size();
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

  public int getTextWidth(String paramString, int paramInt)
  {
    Paint localPaint = new Paint();
    localPaint.setTextSize(ViewUtils.sp2px(getActivity(), paramInt));
    return (int)localPaint.measureText(paramString);
  }

  protected void initButtonLayoutParams()
  {
    this.mTextViewlayoutParams = new LinearLayout.LayoutParams(-1, -2, 1.0F);
    this.mTextViewlayoutParams.setMargins(8, 8, 8, 8);
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
    sendHotWordRequest();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    try
    {
      if (this.mOnSearchFragmentListener == null)
        this.mOnSearchFragmentListener = ((AbstractSearchFragment.OnSearchFragmentListener)getActivity());
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
        HotelSearchSuggestFragment.this.searchEditText.setText("");
      }
    });
    paramLayoutInflater.findViewById(R.id.searchBtn).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (TextUtils.isEmpty(HotelSearchSuggestFragment.this.searchEditText.getText().toString().trim()))
          return;
        paramView = new DPObject().edit().putString("Keyword", HotelSearchSuggestFragment.this.searchEditText.getText().toString().trim()).putString(HotelSearchSuggestFragment.this.getResources().getString(R.string.search_keyword_ga_suffix), "_button").generate();
        HotelSearchSuggestFragment.this.search(paramView);
      }
    });
    this.listView = ((ListView)paramLayoutInflater.findViewById(16908298));
    BitmapUtils.fixBackgroundRepeat(this.listView);
    this.listView.setDivider(null);
    paramLayoutInflater.findViewById(R.id.back).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        KeyboardUtils.hideKeyboard(HotelSearchSuggestFragment.this.searchEditText);
        if (HotelSearchSuggestFragment.this.getFragmentManager() != null)
          HotelSearchSuggestFragment.this.getFragmentManager().popBackStackImmediate();
      }
    });
    this.searchEditText = ((EditText)paramLayoutInflater.findViewById(R.id.search_edit));
    this.searchEditText.setOnKeyListener(new View.OnKeyListener()
    {
      public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
      {
        if ((paramInt == 66) && (paramKeyEvent.getAction() == 1))
        {
          if (TextUtils.isEmpty(HotelSearchSuggestFragment.this.searchEditText.getText().toString().trim()))
            return true;
          paramView = new DPObject().edit().putString("Keyword", HotelSearchSuggestFragment.this.searchEditText.getText().toString().trim()).generate();
          HotelSearchSuggestFragment.this.search(paramView);
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
          HotelSearchSuggestFragment.this.mClearButton.setVisibility(4);
          HotelSearchSuggestFragment.this.resetListView();
          HotelSearchSuggestFragment.this.listView.addHeaderView(HotelSearchSuggestFragment.this.getHeaderView("热门搜索", 0));
          HotelSearchSuggestFragment.this.listView.addHeaderView(HotelSearchSuggestFragment.this.getHotWordView(HotelSearchSuggestFragment.this.hotWordList));
          if (HotelSearchSuggestFragment.this.getHistoryCount() > 0)
            HotelSearchSuggestFragment.this.listView.addHeaderView(HotelSearchSuggestFragment.this.getHeaderView("搜索历史", 2));
          if (HotelSearchSuggestFragment.this.listView != null)
            HotelSearchSuggestFragment.this.listView.setAdapter(HotelSearchSuggestFragment.this.historyListAdapter);
          HotelSearchSuggestFragment.this.mSearchMode = 1;
        }
        while (true)
        {
          this.mLastKeyword = paramEditable;
          return;
          HotelSearchSuggestFragment.this.mClearButton.setVisibility(0);
          if (paramEditable.equals(this.mLastKeyword))
            continue;
          HotelSearchSuggestFragment.this.searchSuggestDelayHandler.removeMessages(HotelSearchSuggestFragment.SEARCH_SUGGEST_MESSAGE);
          Message localMessage = HotelSearchSuggestFragment.this.searchSuggestDelayHandler.obtainMessage(HotelSearchSuggestFragment.SEARCH_SUGGEST_MESSAGE, paramEditable);
          HotelSearchSuggestFragment.this.searchSuggestDelayHandler.sendMessageDelayed(localMessage, 300L);
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
    paramLayoutInflater.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        return true;
      }
    });
    if (!TextUtils.isEmpty(keyword))
    {
      this.searchEditText.setText(keyword);
      this.searchEditText.setSelection(keyword.length());
      return paramLayoutInflater;
    }
    this.searchEditText.setHint("酒店名、品牌等…");
    return paramLayoutInflater;
  }

  public void onDestroyView()
  {
    super.onDestroyView();
    if (this.requestHotWords != null)
      mapiService().abort(this.requestHotWords, this, true);
    if (this.requestSuggestWords != null)
      mapiService().abort(this.requestSuggestWords, this, true);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = this.mHeaderViews.iterator();
    do
      if (!paramAdapterView.hasNext())
        break;
    while (paramView != (View)paramAdapterView.next());
    do
    {
      do
      {
        do
          while (true)
          {
            return;
            paramAdapterView = this.listView.getItemAtPosition(paramInt);
            if (this.mSearchMode != 1)
              break;
            if (paramAdapterView == CLEARHISTORY)
            {
              removeHistory();
              this.searchHistoryList.clear();
              removeSearchListHeaderView();
              this.historyListAdapter = new HistoryAdapter(this.searchHistoryList);
              this.listView.setAdapter(this.historyListAdapter);
              this.mSearchMode = 1;
              return;
            }
            if (!(paramAdapterView instanceof DPObject))
              continue;
            this.searchEditText.setText(((DPObject)paramAdapterView).getString("Keyword"));
            this.searchEditText.setSelection(this.searchEditText.getText().length());
            search((DPObject)((DPObject)paramAdapterView).edit().putString(getResources().getString(R.string.search_keyword_ga_suffix), "_history").generate());
            return;
          }
        while (this.mSearchMode != 2);
        if (!(paramAdapterView instanceof DPObject))
          continue;
        search((DPObject)((DPObject)paramAdapterView).edit().putString(getResources().getString(R.string.search_keyword_ga_suffix), "_suggest").generate());
        return;
      }
      while (paramAdapterView != NO_SUGGESTION);
      paramAdapterView = this.searchEditText.getText().toString().trim();
    }
    while (TextUtils.isEmpty(paramAdapterView));
    search(new DPObject().edit().putString("Keyword", paramAdapterView).putString(getResources().getString(R.string.search_keyword_ga_suffix), "_suggest").generate());
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.requestSuggestWords)
      this.requestSuggestWords = null;
    do
      return;
    while (paramMApiRequest != this.requestHotWords);
    this.requestHotWords = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.requestHotWords)
    {
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest != null);
    }
    do
    {
      return;
      this.hotWordList.clear();
      paramMApiRequest = paramMApiRequest.getArray("SearchKeywordList");
      if ((paramMApiRequest != null) && (paramMApiRequest.length > 0))
        this.hotWordList.addAll(Arrays.asList(paramMApiRequest));
      if (this.hotWordList.size() > 0)
      {
        resetListView();
        paramMApiRequest = getHeaderView("热门搜索", 0);
        if (paramMApiRequest != null)
          this.listView.addHeaderView(paramMApiRequest);
        paramMApiRequest = getHotWordView(this.hotWordList);
        if (paramMApiRequest != null)
          this.listView.addHeaderView(paramMApiRequest);
        if (getHistoryCount() > 0)
        {
          paramMApiRequest = getHeaderView("搜索历史", 2);
          if (paramMApiRequest != null)
            this.listView.addHeaderView(paramMApiRequest);
        }
        this.listView.setAdapter(this.historyListAdapter);
        this.mSearchMode = 1;
      }
      this.requestHotWords = null;
      return;
    }
    while (paramMApiRequest != this.requestSuggestWords);
    if (((paramMApiResponse.result() instanceof DPObject)) && (getActivity() != null))
    {
      paramMApiResponse = (DPObject)paramMApiResponse.result();
      paramMApiRequest = new ArrayList();
      this.queryid = paramMApiResponse.getString("QueryID");
      if (paramMApiResponse.getArray("List") != null)
        paramMApiRequest.addAll(Arrays.asList(paramMApiResponse.getArray("List")));
      resetListView();
      int i = 0;
      if (paramMApiRequest.size() != 0)
        i = ((DPObject)paramMApiRequest.get(0)).getInt("SuggestType");
      this.listView.setHeaderDividersEnabled(false);
      switch (i)
      {
      default:
        this.mHeaderViews.clear();
        this.listView.setAdapter(getSuggestListAdapter(paramMApiRequest));
        this.mSearchMode = 2;
      case 2:
      case 1:
      }
    }
    while (true)
    {
      this.requestSuggestWords = null;
      return;
      paramMApiResponse = getHeaderView("热门搜索", 0);
      if (paramMApiResponse != null)
        this.listView.addHeaderView(paramMApiResponse);
      paramMApiRequest = getHotWordView(paramMApiRequest);
      if (paramMApiRequest != null)
        this.listView.addHeaderView(paramMApiRequest);
      if (getHistoryCount() > 0)
      {
        paramMApiRequest = getHeaderView("搜索历史", 2);
        if (paramMApiRequest != null)
          this.listView.addHeaderView(paramMApiRequest);
      }
      this.listView.setAdapter(this.historyListAdapter);
      this.mSearchMode = 1;
      continue;
      this.listView.setAdapter(getSuggestListAdapter(paramMApiRequest));
      this.mSearchMode = 2;
    }
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

  protected void search(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    this.dpobjKeyword = paramDPObject;
    if (this.mOnSearchFragmentListener != null)
      this.mOnSearchFragmentListener.startSearch(this.dpobjKeyword);
    removeFromList(this.dpobjKeyword);
    if (!TextUtils.isEmpty(paramDPObject.getString("SearchValue")))
    {
      paramDPObject = paramDPObject.getString("Keyword");
      this.dpobjKeyword = new DPObject().edit().putString("Keyword", paramDPObject).putString(getResources().getString(R.string.search_keyword_ga_suffix), "_button").generate();
    }
    this.searchHistoryList.add(0, this.dpobjKeyword);
    if (this.searchHistoryList.size() == 11)
      this.searchHistoryList.remove(10);
    saveHistory();
    KeyboardUtils.hideKeyboard(this.searchEditText);
    this.listView.setVisibility(8);
    getFragmentManager().popBackStackImmediate();
  }

  protected void sendHotWordRequest()
  {
    initButtonLayoutParams();
    if (this.requestHotWords != null)
    {
      mapiService().abort(this.requestHotWords, null, true);
      return;
    }
    this.requestHotWords = createHotWordsRequest();
    mapiService().exec(this.requestHotWords, this);
  }

  public void setOnSearchFragmentListener(AbstractSearchFragment.OnSearchFragmentListener paramOnSearchFragmentListener)
  {
    this.mOnSearchFragmentListener = paramOnSearchFragmentListener;
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
      return HotelSearchSuggestFragment.NO_SUGGESTION;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (this.suggestionList.size() == 0)
      {
        paramView = (TextView)HotelSearchSuggestFragment.this.getActivity().getLayoutInflater().inflate(17367043, paramViewGroup, false);
        paramView.setText("查找'" + HotelSearchSuggestFragment.this.searchEditText.getText().toString() + "'");
        return paramView;
      }
      DPObject localDPObject = (DPObject)getItem(paramInt);
      if ((paramView instanceof LinearLayout));
      for (paramView = (LinearLayout)paramView; ; paramView = null)
      {
        Object localObject = paramView;
        if (paramView == null)
          localObject = (LinearLayout)HotelSearchSuggestFragment.this.getActivity().getLayoutInflater().inflate(R.layout.search_list_item, paramViewGroup, false);
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
        this.stringList.add(HotelSearchSuggestFragment.CLEARHISTORY);
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
        paramView = HotelSearchSuggestFragment.this.getActivity().getLayoutInflater().inflate(R.layout.search_list_item, paramViewGroup, false);
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
        if (getItem(paramInt) != HotelSearchSuggestFragment.CLEARHISTORY)
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
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.shoplist.fragement.HotelSearchSuggestFragment
 * JD-Core Version:    0.6.0
 */