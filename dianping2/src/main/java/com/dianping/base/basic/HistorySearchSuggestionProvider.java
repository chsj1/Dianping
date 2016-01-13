package com.dianping.base.basic;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class HistorySearchSuggestionProvider extends ContentProvider
{
  public static final String AUTHORITY = "com.dianping.app.DianpingHistorySearchSuggestionProvider";
  public static final String CHANNEL_COLUMN = "channel";
  private static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.searchhistory.historys";
  public static final Uri CONTENT_URI = Uri.parse("content://com.dianping.app.DianpingHistorySearchSuggestionProvider/historys");
  private static final String DATABASE_NAME = "searchhistory.db";
  public static final String DATE_COLUMN = "date";
  public static final String ID_COLUMN = "_ID";
  public static final String KEYWORD_COLUMN = "keyword";
  private static final String ORDER_BY = "date DESC";
  private static final String TABLE_NAME = "historys";
  private static final int URI_MATCH_SUGGEST = 1;
  private static final int VERSION = 1;
  private static final UriMatcher mUriMatcher = new UriMatcher(-1);
  private SQLiteOpenHelper mDBHelper;

  static
  {
    mUriMatcher.addURI("com.dianping.app.DianpingHistorySearchSuggestionProvider", "historys", 1);
  }

  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    if (mUriMatcher.match(paramUri) != 1)
      throw new IllegalArgumentException("Unknow URI " + paramUri);
    int i = this.mDBHelper.getWritableDatabase().delete("historys", paramString, paramArrayOfString);
    getContext().getContentResolver().notifyChange(paramUri, null);
    return i;
  }

  public String getType(Uri paramUri)
  {
    if (mUriMatcher.match(paramUri) == 1)
      return "vnd.android.cursor.dir/vnd.searchhistory.historys";
    throw new IllegalArgumentException("Unknow URI " + paramUri);
  }

  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    if (mUriMatcher.match(paramUri) != 1)
      throw new IllegalArgumentException("Unknow URI " + paramUri);
    if (!paramContentValues.containsKey("date"))
      paramContentValues.put("date", Long.valueOf(System.currentTimeMillis()));
    long l = this.mDBHelper.getWritableDatabase().insert("historys", null, paramContentValues);
    paramContentValues = null;
    if (l > 0L)
      paramContentValues = Uri.withAppendedPath(CONTENT_URI, String.valueOf(l));
    getContext().getContentResolver().notifyChange(paramUri, null);
    return paramContentValues;
  }

  public boolean onCreate()
  {
    this.mDBHelper = new DatabaseHelper(getContext(), 1);
    return true;
  }

  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    if (mUriMatcher.match(paramUri) != 1)
      throw new IllegalArgumentException("Unknow URI " + paramUri);
    paramUri = new SQLiteQueryBuilder();
    paramUri.setTables("historys");
    return paramUri.query(this.mDBHelper.getReadableDatabase(), paramArrayOfString1, paramString1, paramArrayOfString2, null, null, "date DESC");
  }

  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    if (mUriMatcher.match(paramUri) != 1)
      throw new IllegalArgumentException("Unknow URI " + paramUri);
    SQLiteDatabase localSQLiteDatabase = this.mDBHelper.getWritableDatabase();
    paramContentValues.put("date", Long.valueOf(System.currentTimeMillis()));
    int i = localSQLiteDatabase.update("historys", paramContentValues, paramString, paramArrayOfString);
    getContext().getContentResolver().notifyChange(paramUri, null);
    return i;
  }

  private static class DatabaseHelper extends SQLiteOpenHelper
  {
    public DatabaseHelper(Context paramContext, int paramInt)
    {
      super("searchhistory.db", null, paramInt);
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE historys (_ID INTEGER PRIMARY KEY autoincrement, keyword TEXT, channel TEXT, date Long);");
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
      paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTShistorys");
      onCreate(paramSQLiteDatabase);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.HistorySearchSuggestionProvider
 * JD-Core Version:    0.6.0
 */