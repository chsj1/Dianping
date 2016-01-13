package com.dianping.base.update.utils.download;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Pair;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public abstract class DownloadManagerCompat
{
  public static final String ACTION_DOWNLOAD_CANCELLED = "android.intent.action.DOWNLOAD_CANCELLED";
  public static final String ACTION_DOWNLOAD_COMPLETE = "android.intent.action.DOWNLOAD_COMPLETE";
  public static final String ACTION_DOWNLOAD_FAILED = "android.intent.action.DOWNLOAD_FAILED";
  public static final String ACTION_NOTIFICATION_CLICKED = "android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED";
  public static final String COLUMN_BYTES_DOWNLOADED_SO_FAR = "bytes_so_far";
  public static final String COLUMN_DESCRIPTION = "description";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_LAST_MODIFIED_TIMESTAMP = "last_modified_timestamp";
  public static final String COLUMN_LOCAL_FILENAME = "local_filename";
  public static final String COLUMN_LOCAL_URI = "local_uri";
  public static final String COLUMN_MEDIA_TYPE = "media_type";
  public static final String COLUMN_STATUS = "status";
  public static final String COLUMN_TITLE = "title";
  public static final String COLUMN_TOTAL_SIZE_BYTES = "total_size";
  public static final String COLUMN_URI = "uri";
  public static final String EXTRA_DOWNLOAD_ID = "extra_download_id";
  public static final int STATUS_FAILED = 16;
  public static final int STATUS_PENDING = 1;
  public static final int STATUS_RUNNING = 2;
  public static final int STATUS_SUCCESSFUL = 8;
  private static DownloadManagerCompat instance;

  public static DownloadManagerCompat createInstance(Context paramContext)
  {
    if (instance != null)
      return instance;
    instance = new DownloadManagerBase(paramContext);
    return instance;
  }

  public abstract long addCompletedDownload(String paramString1, String paramString2, boolean paramBoolean1, String paramString3, String paramString4, long paramLong, boolean paramBoolean2);

  public abstract long enqueue(RequestCompat paramRequestCompat);

  public abstract String getMimeTypeForDownloadedFile(long paramLong);

  public boolean getNotificationVisibility()
  {
    return true;
  }

  public abstract Uri getUriForDownloadedFile(long paramLong);

  public abstract ParcelFileDescriptor openDownloadedFile(long paramLong)
    throws FileNotFoundException;

  public abstract Cursor query(DownloadManagerCompat.QueryCompat paramQueryCompat);

  public abstract int remove(long[] paramArrayOfLong);

  public void setNotificationVisibility(boolean paramBoolean)
  {
  }

  public static class RequestCompat
  {
    public static final int VISIBILITY_HIDDEN = 2;
    public static final int VISIBILITY_VISIBLE = 0;
    public volatile boolean _cancel;
    private int mAllowedNetworkTypes = -1;
    private CharSequence mDescription;
    private Uri mDestinationUri;
    private Intent mIntent;
    private boolean mIsVisibleInDownloadsUi = true;
    private boolean mMeteredAllowed = true;
    private String mMimeType;
    private int mNotificationVisibility = 0;
    private List<Pair<String, String>> mRequestHeaders = new ArrayList();
    private boolean mRoamingAllowed = true;
    private CharSequence mTitle;
    private Uri mUri;

    public RequestCompat(Uri paramUri)
    {
      if (paramUri == null)
        throw new NullPointerException();
      String str = paramUri.getScheme();
      if ((str == null) || ((!str.equals("http")) && (!str.equals("https"))))
        throw new IllegalArgumentException("Can only download HTTP/HTTPS URIs: " + paramUri);
      this.mUri = paramUri;
    }

    RequestCompat(String paramString)
    {
      this.mUri = Uri.parse(paramString);
    }

    private void setDestinationFromBase(File paramFile, String paramString)
    {
      if (paramString == null)
        throw new NullPointerException("subPath cannot be null");
      this.mDestinationUri = Uri.withAppendedPath(Uri.fromFile(paramFile), paramString);
    }

    public int getNotificationVisibility()
    {
      return this.mNotificationVisibility;
    }

    public Uri mDestinationUri()
    {
      return this.mDestinationUri;
    }

    public Intent mIntent()
    {
      return this.mIntent;
    }

    public CharSequence mTitle()
    {
      return this.mTitle;
    }

    public Uri mUri()
    {
      return this.mUri;
    }

    public SharedPreferences saveToSharedPreferences(SharedPreferences paramSharedPreferences)
    {
      paramSharedPreferences.edit().putString("uri", this.mUri.toString()).putString("local_uri", this.mDestinationUri.toString()).putString("title", (String)this.mTitle).putString("local_filename", this.mDestinationUri.getPath().substring(this.mDestinationUri.getPath().lastIndexOf('/'))).putString("description", (String)this.mDescription).putString("media_type", this.mMimeType).putLong("last_modified_timestamp", System.currentTimeMillis()).putBoolean("roaming_allowed", this.mRoamingAllowed).putBoolean("metered_allowed", this.mMeteredAllowed).putBoolean("is_visible_in_downloads_ui", this.mIsVisibleInDownloadsUi).putInt("allowed_network_types", this.mAllowedNetworkTypes).commit();
      return paramSharedPreferences;
    }

    public RequestCompat setDestinationInExternalPublicDir(String paramString1, String paramString2)
    {
      if (Build.VERSION.SDK_INT >= 8)
        paramString1 = Environment.getExternalStoragePublicDirectory(paramString1);
      while (true)
        if (paramString1.exists())
        {
          if (paramString1.isDirectory())
            break;
          throw new IllegalStateException(paramString1.getAbsolutePath() + " already exists and is not a directory");
          paramString1 = new File(Environment.getExternalStorageDirectory().getPath() + "/" + paramString1);
          continue;
        }
        else
        {
          if (paramString1.mkdirs())
            break;
          throw new IllegalStateException("Unable to create directory: " + paramString1.getAbsolutePath());
        }
      setDestinationFromBase(paramString1, paramString2);
      return this;
    }

    public RequestCompat setDestinationInInternalFilesDir(Context paramContext, String paramString)
    {
      setDestinationFromBase(paramContext.getFilesDir(), paramString);
      return this;
    }

    public RequestCompat setIntent(Intent paramIntent)
    {
      this.mIntent = paramIntent;
      return this;
    }

    public RequestCompat setMimeType(String paramString)
    {
      this.mMimeType = paramString;
      return this;
    }

    public RequestCompat setNotificationVisibility(int paramInt)
    {
      this.mNotificationVisibility = paramInt;
      return this;
    }

    public RequestCompat setTitle(CharSequence paramCharSequence)
    {
      this.mTitle = paramCharSequence;
      return this;
    }

    public RequestCompat setVisibleInDownloadsUi(boolean paramBoolean)
    {
      this.mIsVisibleInDownloadsUi = paramBoolean;
      return this;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.update.utils.download.DownloadManagerCompat
 * JD-Core Version:    0.6.0
 */