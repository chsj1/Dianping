package com.dianping.base.basic;

import android.content.ContentValues;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import com.dianping.app.CityConfig;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.util.StringUtil;
import java.net.URLEncoder;
import java.text.DecimalFormat;

public class SuggestionProvider extends SearchRecentSuggestionsProvider
{
  public static final String AUTHORITY = "com.dianping.app.DianpingSuggestionProvider";
  private static final String[] COLUMNS = { "_id", "suggest_text_1", "suggest_text_2", "suggest_intent_data", "com_dianping_suggest_url", "com_dianping_suggest_value" };
  private static final int MAXCOUNT = 20;
  public static final int MODE = 3;
  public static final String SUGGESTION_URL = "com_dianping_suggest_url";
  public static final String SUGGESTION_VALUE = "com_dianping_suggest_value";
  private static int totalRecord;
  private boolean bFirstSearch = true;

  public SuggestionProvider()
  {
    setupSuggestions("com.dianping.app.DianpingSuggestionProvider", 3);
  }

  private Object[] columnValuesOfWord(int paramInt, String paramString1, String paramString2)
  {
    return new Object[] { Integer.valueOf(paramInt), paramString1, paramString2, paramString1, null, null };
  }

  private Object[] columnValuesOfWord(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    return new Object[] { Integer.valueOf(paramInt), paramString1, paramString2, paramString1, paramString3, paramString4 };
  }

  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    if (paramString == null)
      this.bFirstSearch = true;
    return super.delete(paramUri, paramString, paramArrayOfString);
  }

  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    if (this.bFirstSearch)
    {
      this.bFirstSearch = false;
      ContentValues localContentValues = new ContentValues();
      localContentValues.put(android.provider.SearchRecentSuggestions.QUERIES_PROJECTION_1LINE[0], Integer.valueOf(2));
      localContentValues.put(android.provider.SearchRecentSuggestions.QUERIES_PROJECTION_1LINE[1], Long.valueOf(System.currentTimeMillis()));
      localContentValues.put(android.provider.SearchRecentSuggestions.QUERIES_PROJECTION_1LINE[2], "清空搜索记录");
      localContentValues.put(android.provider.SearchRecentSuggestions.QUERIES_PROJECTION_1LINE[3], "清空搜索记录");
      super.insert(paramUri, localContentValues);
      paramContentValues.put("date", Long.valueOf(System.currentTimeMillis()));
      return super.insert(paramUri, paramContentValues);
    }
    if (totalRecord > 20)
      truncateHistory(paramUri, totalRecord - 20);
    return super.insert(paramUri, paramContentValues);
  }

  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    paramUri = super.query(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2);
    if (TextUtils.isEmpty(paramArrayOfString2[0]))
    {
      if (paramUri.getCount() == 0)
      {
        paramUri = new MatrixCursor(COLUMNS);
        paramUri.addRow(columnValuesOfWord(0, "没有搜索记录", ""));
        this.bFirstSearch = true;
        return paramUri;
      }
      totalRecord = paramUri.getCount();
      this.bFirstSearch = false;
      return paramUri;
    }
    if (StringUtil.isAllPunctuation(paramArrayOfString2[0]))
      return paramUri;
    int i = DPApplication.instance().cityConfig().currentCity().id();
    paramString1 = DPApplication.instance().locationService().location();
    paramString2 = paramArrayOfString2[0];
    paramArrayOfString1 = Boolean.valueOf(false);
    paramUri = paramArrayOfString1;
    if (!TextUtils.isEmpty(paramArrayOfString2[1]))
    {
      paramUri = paramArrayOfString1;
      if ("true".equals(paramArrayOfString2[1]))
        paramUri = Boolean.valueOf(true);
    }
    if (paramUri.booleanValue())
    {
      paramArrayOfString1 = "" + "http://m.api.dianping.com/advancedsuggest.bin?cityid=" + i + "&keyword=" + URLEncoder.encode(paramString2);
      paramUri = paramArrayOfString1;
      if (paramString1 != null)
      {
        paramUri = paramArrayOfString1;
        if (paramString1.getDouble("Lat") != 0.0D)
        {
          paramUri = paramArrayOfString1;
          if (paramString1.getDouble("Lng") != 0.0D)
            paramUri = paramArrayOfString1 + "&mylat=" + Location.FMT.format(paramString1.getDouble("Lat")) + "&mylng=" + Location.FMT.format(paramString1.getDouble("Lng")) + "&myacc=" + paramString1.getInt("Accuracy");
        }
      }
    }
    for (paramUri = new BasicMApiRequest(paramUri, "GET", null, CacheType.DISABLED, false, null); ; paramUri = new BasicMApiRequest(paramUri, "GET", null, CacheType.DISABLED, false, null))
    {
      paramString1 = (DPObject)((MApiResponse)DPApplication.instance().mapiService().execSync(paramUri)).result();
      paramArrayOfString1 = new MatrixCursor(COLUMNS);
      if (paramString1 == null)
        break label750;
      if ((paramString1.getArray("List") == null) || (paramString1.getArray("List").length <= 0))
        break label679;
      i = 0;
      while (true)
      {
        paramUri = paramArrayOfString1;
        if (i >= paramString1.getArray("List").length)
          break;
        paramUri = "约" + paramString1.getArray("List")[i].getInt("Count") + "个结果";
        paramArrayOfString2 = paramString1.getArray("List")[i].getString("Url");
        paramString2 = paramString1.getArray("List")[i].getString("Value");
        paramArrayOfString1.addRow(columnValuesOfWord(i, paramString1.getArray("List")[i].getString("Keyword"), paramUri, paramArrayOfString2, paramString2));
        i += 1;
      }
      paramArrayOfString1 = "" + "http://m.api.dianping.com/searchsuggest.bin?cityid=" + i + "&keyword=" + URLEncoder.encode(paramString2);
      paramUri = paramArrayOfString1;
      if (paramString1 == null)
        continue;
      paramUri = paramArrayOfString1;
      if (paramString1.getDouble("Lat") == 0.0D)
        continue;
      paramUri = paramArrayOfString1;
      if (paramString1.getDouble("Lng") == 0.0D)
        continue;
      paramUri = paramArrayOfString1 + "&mylat=" + Location.FMT.format(paramString1.getDouble("Lat")) + "&mylng=" + Location.FMT.format(paramString1.getDouble("Lng")) + "&myacc=" + paramString1.getInt("Accuracy");
    }
    label679: paramUri = paramString1.getString("EmptyMsg");
    if (TextUtils.isEmpty(paramUri))
    {
      paramArrayOfString1.addRow(columnValuesOfWord(0, "查找 “" + paramArrayOfString2[0] + "”", ""));
      return paramArrayOfString1;
    }
    paramArrayOfString1.addRow(columnValuesOfWord(0, paramUri, ""));
    return paramArrayOfString1;
    label750: paramArrayOfString1.addRow(columnValuesOfWord(0, "查找 “" + paramArrayOfString2[0] + "”", ""));
    return paramArrayOfString1;
  }

  protected void truncateHistory(Uri paramUri, int paramInt)
  {
    if (paramInt < 0)
      throw new IllegalArgumentException();
    String str = null;
    if (paramInt > 0);
    try
    {
      str = "_id IN (SELECT _id FROM suggestions ORDER BY date ASC LIMIT " + String.valueOf(paramInt) + " OFFSET 1)";
      delete(paramUri, str, null);
      return;
    }
    catch (java.lang.RuntimeException paramUri)
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.SuggestionProvider
 * JD-Core Version:    0.6.0
 */