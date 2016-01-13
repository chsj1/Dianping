package com.dianping.base.basic;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import java.util.ArrayList;
import java.util.HashSet;
import org.json.JSONException;
import org.json.JSONObject;

public class HistorySearchSuggestionHelper
{
  public static final int MAX_LIMIT = 10;

  private static String createJsonString(String paramString1, String paramString2)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("keyword", paramString1);
      localJSONObject.put("value", paramString2);
      label24: return localJSONObject.toString();
    }
    catch (JSONException paramString1)
    {
      break label24;
    }
  }

  public static void deleteChannel(ContentResolver paramContentResolver, String paramString)
  {
    paramString = paramString.split(",");
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    while (i < paramString.length)
    {
      if (i > 0)
        localStringBuilder.append(" or ");
      localStringBuilder.append("channel=?");
      i += 1;
    }
    paramContentResolver.delete(HistorySearchSuggestionProvider.CONTENT_URI, localStringBuilder.toString(), paramString);
  }

  public static boolean exists(ContentResolver paramContentResolver, String paramString1, String paramString2)
  {
    paramContentResolver = paramContentResolver.query(HistorySearchSuggestionProvider.CONTENT_URI, new String[] { "keyword" }, "channel=? and keyword=?", new String[] { paramString1, paramString2 }, null);
    boolean bool = paramContentResolver.moveToFirst();
    paramContentResolver.close();
    return bool;
  }

  public static void insert(ContentResolver paramContentResolver, String paramString1, String paramString2)
  {
    if (exists(paramContentResolver, paramString2, paramString1))
    {
      update(paramContentResolver, paramString2, paramString1);
      return;
    }
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("keyword", paramString1);
    localContentValues.put("channel", paramString2);
    paramContentResolver.insert(HistorySearchSuggestionProvider.CONTENT_URI, localContentValues);
    truncate(paramContentResolver, paramString2);
  }

  public static void insert(ContentResolver paramContentResolver, String paramString1, String paramString2, String paramString3)
  {
    if (exists(paramContentResolver, paramString3, paramString1))
    {
      update(paramContentResolver, paramString3, paramString1, paramString2);
      return;
    }
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("keyword", createJsonString(paramString1, paramString2));
    localContentValues.put("channel", paramString3);
    paramContentResolver.insert(HistorySearchSuggestionProvider.CONTENT_URI, localContentValues);
    truncate(paramContentResolver, paramString3);
  }

  public static ArrayList<String> queryByChannel(ContentResolver paramContentResolver, String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    HashSet localHashSet = new HashSet();
    paramString = paramString.split(",");
    Object localObject2 = new StringBuilder();
    int i = 0;
    while (i < paramString.length)
    {
      if (i > 0)
        ((StringBuilder)localObject2).append(" or ");
      ((StringBuilder)localObject2).append("channel=?");
      i += 1;
    }
    Object localObject1 = HistorySearchSuggestionProvider.CONTENT_URI;
    localObject2 = ((StringBuilder)localObject2).toString();
    paramContentResolver = paramContentResolver.query((Uri)localObject1, new String[] { "keyword" }, (String)localObject2, paramString, null);
    if (paramContentResolver.moveToNext())
      paramString = paramContentResolver.getString(paramContentResolver.getColumnIndex("keyword"));
    while (true)
    {
      try
      {
        localObject1 = new JSONObject(paramString).optString("keyword");
        if ((TextUtils.isEmpty((CharSequence)localObject1)) || (localHashSet.contains(localObject1)))
          break;
        localHashSet.add(localObject1);
        localArrayList.add(paramString);
        if (localArrayList.size() != 10)
          break;
        paramContentResolver.close();
        return localArrayList;
      }
      catch (JSONException localJSONException)
      {
      }
      if (localHashSet.contains(paramString))
        break;
      localHashSet.add(paramString);
    }
  }

  public static ArrayList<DPObject> queryForChannel(ContentResolver paramContentResolver, String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    HashSet localHashSet = new HashSet();
    paramString = paramString.split(",");
    Object localObject2 = new StringBuilder();
    int i = 0;
    while (i < paramString.length)
    {
      if (i > 0)
        ((StringBuilder)localObject2).append(" or ");
      ((StringBuilder)localObject2).append("channel=?");
      i += 1;
    }
    Object localObject1 = HistorySearchSuggestionProvider.CONTENT_URI;
    localObject2 = ((StringBuilder)localObject2).toString();
    Cursor localCursor = paramContentResolver.query((Uri)localObject1, new String[] { "keyword" }, (String)localObject2, paramString, null);
    while (true)
    {
      if (localCursor.moveToNext())
      {
        paramString = localCursor.getString(localCursor.getColumnIndex("keyword"));
        localObject1 = "";
        paramContentResolver = paramString;
      }
      try
      {
        localObject2 = new JSONObject(paramString);
        paramContentResolver = paramString;
        paramString = ((JSONObject)localObject2).getString("keyword");
        paramContentResolver = paramString;
        localObject2 = ((JSONObject)localObject2).getString("value");
        localObject1 = localObject2;
        paramContentResolver = paramString;
        label162: if (localHashSet.contains(paramContentResolver))
          continue;
        localHashSet.add(paramContentResolver);
        localArrayList.add(new DPObject().edit().putString("Keyword", paramContentResolver).putString("Value", (String)localObject1).generate());
        if (localArrayList.size() != 10)
          continue;
        localCursor.close();
        return localArrayList;
      }
      catch (JSONException paramString)
      {
        break label162;
      }
    }
  }

  public static void truncate(ContentResolver paramContentResolver, String paramString)
  {
    paramString = paramContentResolver.query(HistorySearchSuggestionProvider.CONTENT_URI, new String[] { "_ID" }, "channel = ?", new String[] { paramString }, null);
    if ((paramString != null) && (paramString.getCount() > 10))
    {
      paramString.moveToPosition(9);
      while (paramString.moveToNext())
        paramContentResolver.delete(HistorySearchSuggestionProvider.CONTENT_URI, "_ID=?", new String[] { paramString.getInt(paramString.getColumnIndex("_ID")) + "" });
    }
    if (paramString != null)
      paramString.close();
  }

  public static void update(ContentResolver paramContentResolver, String paramString1, String paramString2)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("keyword", paramString2);
    localContentValues.put("channel", paramString1);
    paramContentResolver.update(HistorySearchSuggestionProvider.CONTENT_URI, localContentValues, "channel=? and keyword=?", new String[] { paramString1, paramString2 });
  }

  public static void update(ContentResolver paramContentResolver, String paramString1, String paramString2, String paramString3)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("keyword", createJsonString(paramString2, paramString3));
    localContentValues.put("channel", paramString1);
    paramContentResolver.update(HistorySearchSuggestionProvider.CONTENT_URI, localContentValues, "channel=? and keyword=?", new String[] { paramString1, paramString2 });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.HistorySearchSuggestionHelper
 * JD-Core Version:    0.6.0
 */