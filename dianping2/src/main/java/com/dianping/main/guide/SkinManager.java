package com.dianping.main.guide;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import com.dianping.app.DPApplication;
import com.dianping.base.widget.ButtonSearchBar;
import com.dianping.main.home.HomeCategoryAgent.CategoryAdapter;
import com.dianping.util.BitmapUtils;
import com.dianping.util.DateUtils;
import com.dianping.util.FileUtils;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.NavigationDot;
import com.dianping.widget.pulltorefresh.PullToRefreshRecyclerView;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class SkinManager
{
  public static final int DEFAULT_SKIN_HOME_BG;
  public static final int DEFAULT_SKIN_HOME_CAT_BG;
  public static final int DEFAULT_SKIN_HOME_CAT_DOT;
  public static final int DEFAULT_SKIN_HOME_CAT_DOT_PRESS;
  public static final int DEFAULT_SKIN_SEARCH_ICON;
  public static final int DEFAULT_SKIN_TABBAR_BG;
  public static final int DEFAULT_SKIN_TABBAR_HOME;
  public static final int DEFAULT_SKIN_TABBAR_MY;
  public static final int DEFAULT_SKIN_TABBAR_SEARCH;
  public static final int DEFAULT_SKIN_TABBAR_TUAN;
  public static final int DEFAULT_SKIN_TOPBAR_ARROW;
  public static final int DEFAULT_SKIN_TOPBAR_BG;
  public static final int DEFAULT_SKIN_TOPBAR_NPTIFY_ICON;
  public static final String FILE_SKIN_ZIP = "skin.zip";
  public static final String LOCAL_SKIN_END_TIME = "";
  public static final String LOCAL_SKIN_STRAT_TIME = "";
  public static final String SKIN_AVAILABLE = "available";
  public static final String SKIN_AVAILABLE_FILE = "skinAvailable";
  public static final String SKIN_CAT_TEXT_COLOR = "channelNameTextColor";
  public static final String SKIN_CITY_TEXT_COLOR = "cityTextColor";
  public static final String SKIN_CONFIG = "skin/skinConfig.json";
  public static final String SKIN_END_DATE = "endDate";
  public static final String SKIN_FOLDER = "skin/";
  public static final String SKIN_HOME_BG = "skin/theme_home_bg.png";
  public static final String SKIN_HOME_CAT_BG = "skin/theme_home_cat_cell_bg.png";
  public static final String SKIN_HOME_CAT_DOT = "skin/theme_home_cat_cell_dot.png";
  public static final String SKIN_HOME_CAT_DOT_PRESS = "skin/theme_home_cat_cell_dot_press.png";
  public static final String SKIN_MD5 = "md5";
  public static final String SKIN_MD5_FILE = "skinMD5";
  public static final String SKIN_SEARCH_BG_COLOR = "searchBackgroundColor";
  public static final String SKIN_SEARCH_BORDER_COLOR = "searchBorderColor";
  public static final String SKIN_SEARCH_ICON = "skin/theme_home_topbar_search_icon.png";
  public static final String SKIN_SEARCH_LEFT_ICON = "skin/theme_home_topbar_search_left_icon.png";
  public static final String SKIN_SEARCH_TEXT_COLOR = "searchTextColor";
  public static final String SKIN_SP = "dianping_skin";
  public static final String SKIN_START_DATE = "startDate";
  public static final String SKIN_TABBAR_BG = "skin/theme_tabbar_bg.png";
  public static final String SKIN_TABBAR_HOME = "skin/theme_tabbar_home_icon.png";
  public static final String SKIN_TABBAR_HOME_PRESS = "skin/theme_tabbar_home_icon_press.png";
  public static final String SKIN_TABBAR_MY = "skin/theme_tabbar_me_icon.png";
  public static final String SKIN_TABBAR_MY_PRESS = "skin/theme_tabbar_me_icon_press.png";
  public static final String SKIN_TABBAR_SEARCH = "skin/theme_tabbar_find_icon.png";
  public static final String SKIN_TABBAR_SERACH_PRESS = "skin/theme_tabbar_find_icon_press.png";
  public static final String SKIN_TABBAR_TUAN = "skin/theme_tabbar_coupon_icon.png";
  public static final String SKIN_TABBAR_TUAN_PRESS = "skin/theme_tabbar_coupon_icon_press.png";
  public static final String SKIN_TOPBAR_ARROW = "skin/theme_home_topbar_city_arrow.png";
  public static final String SKIN_TOPBAR_BG = "skin/theme_home_topbar_bg.png";
  public static final String SKIN_TOPBAR_NPTIFY_ICON = "skin/theme_home_topbar_plus_icon.png";
  public static final String SKIN_URL = "url";
  private static String channelNameTextColorStr;
  private static String cityTextColorStr;
  private static String fileDir;
  private static SkinManager instance;
  private static boolean isShowDownSkin;
  private static boolean isShowLocalSkin = false;
  private static String searchBackgroundColorStr;
  private static String searchBorderColorStr;
  private static String searchTextColorStr;
  public DownloadFileAsync downloadFileAsync;
  private Context mContext = DPApplication.instance().getApplicationContext();
  private String mEndDate;
  private String mMd5;
  private JSONObject mSkinJson;
  private String mStartDate;

  static
  {
    isShowDownSkin = false;
    instance = null;
    DEFAULT_SKIN_TABBAR_BG = R.drawable.tab_bar_bg;
    DEFAULT_SKIN_TABBAR_HOME = R.drawable.main_index_home;
    DEFAULT_SKIN_TABBAR_TUAN = R.drawable.main_index_tuan;
    DEFAULT_SKIN_TABBAR_SEARCH = R.drawable.main_index_search;
    DEFAULT_SKIN_TABBAR_MY = R.drawable.main_index_my;
    DEFAULT_SKIN_HOME_BG = R.drawable.main_home_background;
    DEFAULT_SKIN_TOPBAR_BG = R.drawable.main_home_title_background;
    DEFAULT_SKIN_TOPBAR_ARROW = R.drawable.title_home_arrow_down;
    DEFAULT_SKIN_SEARCH_ICON = R.drawable.ic_home_search;
    DEFAULT_SKIN_TOPBAR_NPTIFY_ICON = R.drawable.home_navibar_icon_add;
    DEFAULT_SKIN_HOME_CAT_BG = R.drawable.transparent_bg;
    DEFAULT_SKIN_HOME_CAT_DOT = R.drawable.home_serve_dot;
    DEFAULT_SKIN_HOME_CAT_DOT_PRESS = R.drawable.home_serve_dot_pressed;
  }

  private SkinManager()
  {
    fileDir = this.mContext.getFilesDir() + "/";
  }

  private void dirChecker(String paramString)
  {
    paramString = new File(paramString);
    if (!paramString.isDirectory())
      paramString.mkdirs();
  }

  public static SkinManager getInstance()
  {
    if (instance == null)
      monitorenter;
    try
    {
      if (instance == null)
        instance = new SkinManager();
      return instance;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public static boolean isShowDownSkin(Context paramContext)
  {
    paramContext = paramContext.getSharedPreferences("dianping_skin", 0);
    if (!paramContext.getBoolean("skinAvailable", false))
      return false;
    return DateUtils.isDuringDate(paramContext.getString("startDate", ""), paramContext.getString("endDate", ""));
  }

  public static boolean isShowLocalSkin()
  {
    return DateUtils.isDuringDate("", "");
  }

  public static void setCategoryDotSkin(Context paramContext, NavigationDot paramNavigationDot)
  {
    if ((!isShowLocalSkin) && (!isShowDownSkin))
      return;
    if (isShowLocalSkin);
    for (String str = ""; ; str = fileDir)
    {
      paramNavigationDot.setDotNormalBitmap(BitmapUtils.getBitmapDrawable(paramContext, str + "skin/theme_home_cat_cell_dot.png", DEFAULT_SKIN_HOME_CAT_DOT));
      paramNavigationDot.setDotPressedBitmap(BitmapUtils.getBitmapDrawable(paramContext, str + "skin/theme_home_cat_cell_dot_press.png", DEFAULT_SKIN_HOME_CAT_DOT_PRESS));
      return;
    }
  }

  public static void setCategorySkin(Context paramContext, View paramView, HomeCategoryAgent.CategoryAdapter paramCategoryAdapter, int paramInt1, int paramInt2)
  {
    if ((!isShowLocalSkin) && (!isShowDownSkin));
    do
      return;
    while ((paramInt2 != 1) && (paramInt1 != 1) && (paramInt1 != paramInt2 - 1));
    if (isShowLocalSkin);
    for (String str = ""; ; str = fileDir)
    {
      paramView.setBackgroundDrawable(BitmapUtils.getBitmapDrawable(paramContext, str + "skin/theme_home_cat_cell_bg.png", DEFAULT_SKIN_HOME_CAT_BG));
      paramCategoryAdapter.setSkinable(true);
      return;
    }
  }

  public static void setCategoryTextColor(TextView paramTextView)
  {
    if (com.dianping.util.TextUtils.isValidColor(channelNameTextColorStr))
      paramTextView.setTextColor(Color.parseColor(channelNameTextColorStr));
  }

  public static void setHomeListSkin(Context paramContext, View paramView)
  {
    if ((!isShowLocalSkin) && (!isShowDownSkin))
      return;
    if (isShowLocalSkin);
    for (String str = ""; ; str = fileDir)
    {
      PullToRefreshRecyclerView localPullToRefreshRecyclerView = (PullToRefreshRecyclerView)paramView.findViewById(R.id.main_listview);
      localPullToRefreshRecyclerView.resetHeader(1);
      localPullToRefreshRecyclerView.addItemDecoration(new Decoration(paramContext, R.drawable.common_bg));
      paramView = (ImageView)paramView.findViewById(R.id.listview_skin);
      paramView.setVisibility(0);
      paramView.setImageDrawable(BitmapUtils.getBitmapDrawable(paramContext, str + "skin/theme_home_bg.png", DEFAULT_SKIN_HOME_BG));
      if (!isShowLocalSkin)
        break;
      return;
    }
  }

  public static void setTabHostSkin(Context paramContext, TabHost paramTabHost)
  {
    if ((!isShowLocalSkin) && (!isShowDownSkin))
      return;
    if (isShowLocalSkin);
    for (String str = ""; ; str = fileDir)
    {
      ImageView localImageView1 = (ImageView)paramTabHost.findViewById(R.id.home_tab_home);
      ImageView localImageView2 = (ImageView)paramTabHost.findViewById(R.id.home_tab_tuan);
      ImageView localImageView3 = (ImageView)paramTabHost.findViewById(R.id.home_tab_search);
      ImageView localImageView4 = (ImageView)paramTabHost.findViewById(R.id.home_tab_my);
      localImageView1.setImageDrawable(BitmapUtils.getStateListDrawable(paramContext, str + "skin/theme_tabbar_home_icon.png", str + "skin/theme_tabbar_home_icon_press.png", DEFAULT_SKIN_TABBAR_HOME));
      localImageView2.setImageDrawable(BitmapUtils.getStateListDrawable(paramContext, str + "skin/theme_tabbar_coupon_icon.png", str + "skin/theme_tabbar_coupon_icon_press.png", DEFAULT_SKIN_TABBAR_TUAN));
      localImageView3.setImageDrawable(BitmapUtils.getStateListDrawable(paramContext, str + "skin/theme_tabbar_find_icon.png", str + "skin/theme_tabbar_find_icon_press.png", DEFAULT_SKIN_TABBAR_SEARCH));
      localImageView4.setImageDrawable(BitmapUtils.getStateListDrawable(paramContext, str + "skin/theme_tabbar_me_icon.png", str + "skin/theme_tabbar_me_icon_press.png", DEFAULT_SKIN_TABBAR_MY));
      paramTabHost.getTabWidget().setBackgroundDrawable(BitmapUtils.getBitmapDrawable(paramContext, str + "skin/theme_tabbar_bg.png", DEFAULT_SKIN_TABBAR_BG));
      return;
    }
  }

  public static void setTitleBarSkin(Context paramContext, View paramView)
  {
    if ((!isShowLocalSkin) && (!isShowDownSkin))
      return;
    if (isShowLocalSkin);
    Object localObject2;
    for (Object localObject1 = ""; ; localObject1 = fileDir)
    {
      paramView.setBackgroundDrawable(BitmapUtils.getBitmapDrawable(paramContext, (String)localObject1 + "skin/theme_home_topbar_bg.png", DEFAULT_SKIN_TOPBAR_BG));
      ((ImageView)paramView.findViewById(R.id.notify)).setImageDrawable(BitmapUtils.getBitmapDrawable(paramContext, (String)localObject1 + "skin/theme_home_topbar_plus_icon.png", DEFAULT_SKIN_TOPBAR_NPTIFY_ICON));
      localObject2 = (TextView)paramView.findViewById(R.id.city);
      if (com.dianping.util.TextUtils.isValidColor(cityTextColorStr))
        ((TextView)localObject2).setTextColor(Color.parseColor(cityTextColorStr));
      Object localObject3 = BitmapUtils.getBitmapDrawable(paramContext, (String)localObject1 + "skin/theme_home_topbar_city_arrow.png", DEFAULT_SKIN_TOPBAR_ARROW);
      ((Drawable)localObject3).setBounds(0, 0, ((Drawable)localObject3).getMinimumWidth(), ((Drawable)localObject3).getMinimumHeight());
      ((TextView)localObject2).setCompoundDrawables(null, null, (Drawable)localObject3, null);
      localObject2 = (ButtonSearchBar)paramView.findViewById(R.id.button_search_bar);
      if (com.dianping.util.TextUtils.isValidColor(searchTextColorStr))
        ((ButtonSearchBar)localObject2).getSearchTextView().setHintTextColor(Color.parseColor(searchTextColorStr));
      if ((com.dianping.util.TextUtils.isValidColor(searchBackgroundColorStr)) && (com.dianping.util.TextUtils.isValidColor(searchBorderColorStr)))
      {
        ((ButtonSearchBar)localObject2).setBackgroundResource(R.drawable.home_title_bar);
        localObject3 = (GradientDrawable)((ButtonSearchBar)localObject2).getBackground();
        ((GradientDrawable)localObject3).setStroke(2, Color.parseColor(searchBorderColorStr));
        ((GradientDrawable)localObject3).setColor(Color.parseColor(searchBackgroundColorStr));
      }
      paramView = (ImageView)paramView.findViewById(R.id.searchIcon);
      localObject3 = BitmapUtils.getFileStream(paramContext, (String)localObject1 + "skin/theme_home_topbar_search_left_icon.png");
      if (localObject3 == null)
        break;
      paramView.setVisibility(0);
      paramView.setImageDrawable(BitmapUtils.getBitmapDrawable(paramContext, (InputStream)localObject3));
      ((ButtonSearchBar)localObject2).getSearchIconView().setVisibility(8);
      paramView = new ImageView(paramContext);
      paramView.setImageDrawable(BitmapUtils.getBitmapDrawable(paramContext, (String)localObject1 + "skin/theme_home_topbar_search_icon.png", DEFAULT_SKIN_SEARCH_ICON));
      localObject1 = new LinearLayout.LayoutParams(-2, -2);
      ((LinearLayout.LayoutParams)localObject1).gravity = 16;
      localObject2 = (LinearLayout)((ButtonSearchBar)localObject2).findViewById(R.id.search_layout);
      ((LinearLayout)localObject2).addView(paramView, (ViewGroup.LayoutParams)localObject1);
      ((LinearLayout)localObject2).setPadding(ViewUtils.dip2px(paramContext, 10.0F), 0, ViewUtils.dip2px(paramContext, 10.0F), 0);
      return;
    }
    ((ButtonSearchBar)localObject2).getSearchIconView().setImageDrawable(BitmapUtils.getBitmapDrawable(paramContext, (String)localObject1 + "skin/theme_home_topbar_search_icon.png", DEFAULT_SKIN_SEARCH_ICON));
  }

  public void cancleTask()
  {
    if (this.downloadFileAsync != null)
    {
      this.downloadFileAsync.cancel(true);
      this.downloadFileAsync = null;
    }
  }

  public void clearSkinDir()
  {
    FileUtils.removeAllFiles(new File(fileDir + "skin"));
  }

  public void initColor()
  {
    if ((!isShowLocalSkin) && (!isShowDownSkin))
      return;
    if (isShowLocalSkin);
    String str;
    for (Object localObject = "skin/skinConfig.json"; ; str = fileDir + "skin/skinConfig.json")
    {
      localObject = readFile((String)localObject);
      if (android.text.TextUtils.isEmpty((CharSequence)localObject))
        break;
      try
      {
        localObject = new JSONObject((String)localObject);
        cityTextColorStr = ((JSONObject)localObject).optString("cityTextColor");
        searchTextColorStr = ((JSONObject)localObject).optString("searchTextColor");
        searchBackgroundColorStr = ((JSONObject)localObject).optString("searchBackgroundColor");
        searchBorderColorStr = ((JSONObject)localObject).optString("searchBorderColor");
        channelNameTextColorStr = ((JSONObject)localObject).optString("channelNameTextColor");
        return;
      }
      catch (JSONException localJSONException)
      {
        localJSONException.printStackTrace();
        return;
      }
    }
  }

  public String readFile(String paramString)
  {
    String str2 = "";
    String str1 = str2;
    try
    {
      if (paramString.startsWith("/"))
        str1 = str2;
      for (paramString = new FileInputStream(paramString); ; paramString = this.mContext.getResources().getAssets().open(paramString))
      {
        str1 = str2;
        byte[] arrayOfByte = new byte[paramString.available()];
        str1 = str2;
        paramString.read(arrayOfByte);
        str1 = str2;
        str2 = EncodingUtils.getString(arrayOfByte, "UTF-8");
        str1 = str2;
        paramString.close();
        return str2;
        str1 = str2;
      }
    }
    catch (Exception paramString)
    {
      paramString.printStackTrace();
    }
    return str1;
  }

  public void refresh(String paramString)
  {
    cancleTask();
    this.downloadFileAsync = new DownloadFileAsync();
    if (android.text.TextUtils.isEmpty(paramString))
    {
      isShowLocalSkin = isShowLocalSkin();
      isShowDownSkin = isShowDownSkin(this.mContext);
      initColor();
      return;
    }
    try
    {
      this.mSkinJson = new JSONObject(paramString);
      Object localObject = this.mContext.getSharedPreferences("dianping_skin", 0);
      paramString = ((SharedPreferences)localObject).edit();
      paramString.putBoolean("skinAvailable", this.mSkinJson.getBoolean("available"));
      paramString.commit();
      this.mStartDate = this.mSkinJson.getString("startDate");
      this.mEndDate = this.mSkinJson.getString("endDate");
      localObject = ((SharedPreferences)localObject).getString("skinMD5", "");
      if ((!android.text.TextUtils.isEmpty(this.mSkinJson.getString("md5"))) && (this.mSkinJson.getString("md5").equals(localObject)))
      {
        paramString.putString("startDate", this.mStartDate);
        paramString.putString("endDate", this.mEndDate);
        paramString.commit();
      }
      if ((this.mSkinJson.getBoolean("available")) && (!android.text.TextUtils.isEmpty(this.mSkinJson.getString("md5"))) && (!this.mSkinJson.getString("md5").equals(localObject)) && (!android.text.TextUtils.isEmpty(this.mSkinJson.getString("url"))))
      {
        this.mMd5 = this.mSkinJson.getString("md5");
        resetSkin();
        this.downloadFileAsync.execute(new String[] { this.mSkinJson.getString("url") });
      }
      isShowLocalSkin = isShowLocalSkin();
      isShowDownSkin = isShowDownSkin(this.mContext);
      initColor();
      return;
    }
    catch (JSONException paramString)
    {
      while (true)
        paramString.printStackTrace();
    }
  }

  public void resetSkin()
  {
    this.mContext.getSharedPreferences("dianping_skin", 0).edit().clear().commit();
    clearSkinDir();
  }

  public boolean unzip(String paramString1, String paramString2)
  {
    while (true)
    {
      try
      {
        new File(paramString2 + "skin").mkdir();
        paramString1 = new ZipInputStream(new FileInputStream(paramString1));
        localObject = paramString1.getNextEntry();
        if (localObject == null)
          break;
        Log.v("SKIN_ASYNC", "Unzipping " + ((ZipEntry)localObject).getName());
        if (((ZipEntry)localObject).isDirectory())
        {
          dirChecker(paramString2 + ((ZipEntry)localObject).getName());
          continue;
        }
      }
      catch (Exception paramString1)
      {
        Log.e("SKIN_ASYNC", "unzip", paramString1);
        Log.e("SKIN_ASYNC", "皮肤解压失败");
        return false;
      }
      Object localObject = new FileOutputStream(paramString2 + ((ZipEntry)localObject).getName());
      for (int i = paramString1.read(); i != -1; i = paramString1.read())
        ((FileOutputStream)localObject).write(i);
      paramString1.closeEntry();
      ((FileOutputStream)localObject).close();
    }
    paramString1.close();
    Log.d("SKIN_ASYNC", "皮肤解压成功");
    return true;
  }

  public static class Decoration extends RecyclerView.ItemDecoration
  {
    Drawable mDivider;

    Decoration(Context paramContext, int paramInt)
    {
      this.mDivider = paramContext.getResources().getDrawable(paramInt);
    }

    public void onDrawOver(Canvas paramCanvas, RecyclerView paramRecyclerView, RecyclerView.State paramState)
    {
      int i = paramRecyclerView.getAdapter().getItemCount();
      if (((LinearLayoutManager)paramRecyclerView.getLayoutManager()).findLastVisibleItemPosition() == i - 1)
      {
        paramState = paramRecyclerView.getChildAt(paramRecyclerView.getChildCount() - 1);
        if (paramState.getBottom() != 0)
        {
          this.mDivider.setBounds(0, paramState.getBottom(), paramRecyclerView.getWidth(), paramRecyclerView.getHeight());
          this.mDivider.draw(paramCanvas);
        }
      }
    }
  }

  class DownloadFileAsync extends AsyncTask<String, String, String>
  {
    DownloadFileAsync()
    {
    }

    protected String doInBackground(String[] paramArrayOfString)
    {
      Log.d("SKIN_ASYNC", "开始下载皮肤");
      Object localObject;
      try
      {
        paramArrayOfString = new URL(paramArrayOfString[0]);
        localObject = paramArrayOfString.openConnection();
        ((URLConnection)localObject).connect();
        int i = ((URLConnection)localObject).getContentLength();
        paramArrayOfString = new BufferedInputStream(paramArrayOfString.openStream());
        localObject = new FileOutputStream(new File(SkinManager.this.mContext.getFilesDir(), "skin.zip"));
        byte[] arrayOfByte = new byte[1024];
        long l = 0L;
        while (true)
        {
          int j = paramArrayOfString.read(arrayOfByte);
          if (j == -1)
            break;
          l += j;
          publishProgress(new String[] { "" + (int)(100L * l / i) });
          ((OutputStream)localObject).write(arrayOfByte, 0, j);
        }
      }
      catch (Exception paramArrayOfString)
      {
        paramArrayOfString.printStackTrace();
      }
      Log.d("SKIN_ASYNC", "皮肤下载成功");
      if (SkinManager.this.mMd5.equalsIgnoreCase(FileUtils.md5sum(SkinManager.fileDir + "skin.zip")))
      {
        Log.d("SKIN_ASYNC", "皮肤校验成功");
        if (SkinManager.this.unzip(SkinManager.fileDir + "skin.zip", SkinManager.fileDir))
        {
          paramArrayOfString = SkinManager.this.mContext.getSharedPreferences("dianping_skin", 0).edit();
          paramArrayOfString.putString("skinMD5", SkinManager.this.mMd5);
          paramArrayOfString.putString("startDate", SkinManager.this.mStartDate);
          paramArrayOfString.putString("endDate", SkinManager.this.mEndDate);
          paramArrayOfString.commit();
        }
      }
      while (true)
      {
        new File(SkinManager.fileDir + "skin.zip").delete();
        return null;
        ((OutputStream)localObject).flush();
        ((OutputStream)localObject).close();
        paramArrayOfString.close();
        break;
        Log.d("SKIN_ASYNC", "皮肤校验失败");
      }
    }

    protected void onPostExecute(String paramString)
    {
    }

    protected void onPreExecute()
    {
      super.onPreExecute();
      Log.d("SKIN_ASYNC", "皮肤准备开始下载");
    }

    protected void onProgressUpdate(String[] paramArrayOfString)
    {
      Log.d("SKIN_ASYNC", paramArrayOfString[0]);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.guide.SkinManager
 * JD-Core Version:    0.6.0
 */