package com.dianping.base.widget;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.statistics.StatisticsService;
import com.dianping.util.Log;
import com.dianping.v1.R.drawable;
import com.dianping.widget.NavigationDot;
import com.dianping.widget.NetworkImageView;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainBannerView extends BaseBannerView
  implements View.OnClickListener
{
  protected JSONArray mAnnounceJsonArray;
  View.OnClickListener mBannerCloseListener;
  protected SharedPreferences mPref;

  public MainBannerView(Context paramContext)
  {
    super(paramContext);
    initView();
  }

  public MainBannerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView();
  }

  private static JSONArray getJsonArray(ArrayList<DPObject> paramArrayList)
  {
    if ((paramArrayList != null) && (paramArrayList.size() > 0))
    {
      JSONArray localJSONArray = new JSONArray();
      int i = 0;
      while (true)
      {
        Object localObject1 = localJSONArray;
        if (i >= paramArrayList.size())
          break label119;
        localObject1 = (DPObject)paramArrayList.get(i);
        if (localObject1 != null);
        try
        {
          JSONObject localJSONObject = new JSONObject();
          localJSONObject.put("iD", ((DPObject)localObject1).getInt("ID"));
          localJSONObject.put("image", ((DPObject)localObject1).getString("Image"));
          localJSONObject.put("url", ((DPObject)localObject1).getString("Url"));
          localJSONArray.put(localJSONObject);
          i += 1;
        }
        catch (JSONException localJSONException)
        {
          while (true)
            localJSONException.printStackTrace();
        }
      }
    }
    Object localObject2 = null;
    label119: return (JSONArray)localObject2;
  }

  public static boolean shouldShowAnnounces(ArrayList<DPObject> paramArrayList, SharedPreferences paramSharedPreferences)
  {
    return shouldShowAnnounces(getJsonArray(paramArrayList), paramSharedPreferences);
  }

  public static boolean shouldShowAnnounces(JSONArray paramJSONArray, SharedPreferences paramSharedPreferences)
  {
    int k = 1;
    if ((paramJSONArray == null) || (paramJSONArray.length() == 0))
    {
      j = 0;
      return j;
    }
    int j = k;
    while (true)
    {
      try
      {
        if (!paramSharedPreferences.getBoolean("announce_closed", false))
          break;
        String str = paramSharedPreferences.getString("announce_list", null);
        paramSharedPreferences = new ArrayList(100);
        if (!TextUtils.isEmpty(str))
        {
          paramSharedPreferences.addAll(Arrays.asList(str.split(",")));
          break label152;
          if (i >= paramJSONArray.length())
            continue;
          str = paramJSONArray.getJSONObject(i).optString("iD");
          boolean bool = paramSharedPreferences.contains(str.hashCode() + "");
          j = k;
          if (!bool)
            break;
          i += 1;
          continue;
          return false;
        }
      }
      catch (Exception paramJSONArray)
      {
        return false;
      }
      label152: int i = 0;
    }
  }

  public View getCloseButton()
  {
    return this.mBtnClose;
  }

  public void hide()
  {
    this.announcelayHead.setVisibility(8);
  }

  public void initView()
  {
    setCloseDrawable(R.drawable.banner_close_normal);
    setNavigationDotNormalDrawable(R.drawable.banner_dot_normal);
    setNavigationDotPressedDrawable(R.drawable.banner_dot_selected);
  }

  public void onClick(View paramView)
  {
    paramView = (Announce)paramView.getTag();
    Object localObject = paramView.mContent;
    if ((TextUtils.isEmpty((CharSequence)localObject)) || (((String)localObject).startsWith("http://")));
    while (true)
    {
      try
      {
        localObject = URLEncoder.encode((String)localObject, "UTF-8");
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.setData(Uri.parse("dianping://complexweb?url=" + (String)localObject));
        getContext().startActivity(localIntent);
        ((StatisticsService)DPApplication.instance().getService("statistics")).event("hotellist5", "hotellist5_banner", paramView.mTitle, 0, null);
        return;
      }
      catch (java.io.UnsupportedEncodingException paramView)
      {
        return;
      }
      if (((String)localObject).startsWith("dianping://"))
      {
        try
        {
          localObject = new Intent("android.intent.action.VIEW", Uri.parse((String)localObject));
          getContext().startActivity((Intent)localObject);
        }
        catch (Exception localException)
        {
        }
        continue;
      }
      new AlertDialog.Builder(getContext()).setTitle("公告栏").setMessage(localException).setPositiveButton("关闭", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
        }
      }).show();
    }
  }

  public void setAnnounce(ArrayList<DPObject> paramArrayList, SharedPreferences paramSharedPreferences)
  {
    setAnnounce(getJsonArray(paramArrayList), paramSharedPreferences);
  }

  public void setAnnounce(ArrayList<DPObject> paramArrayList, SharedPreferences paramSharedPreferences, boolean paramBoolean)
  {
    setAnnounce(getJsonArray(paramArrayList), paramSharedPreferences, paramBoolean);
  }

  public void setAnnounce(JSONArray paramJSONArray, SharedPreferences paramSharedPreferences)
  {
    setAnnounce(paramJSONArray, paramSharedPreferences, true);
  }

  public void setAnnounce(JSONArray paramJSONArray, SharedPreferences paramSharedPreferences, boolean paramBoolean)
  {
    if ((paramJSONArray == null) || (paramJSONArray.length() == 0))
      return;
    int k;
    if (paramBoolean)
    {
      i = 2;
      this.HACK_ITEM_COUNT = i;
      this.mPref = paramSharedPreferences;
      this.mAnnounceJsonArray = paramJSONArray;
      k = paramJSONArray.length();
      if ((k <= 1) || (this.HACK_ITEM_COUNT != 2))
        break label457;
    }
    label286: label442: label457: for (int i = k + 2; ; i = k)
    {
      int j = 0;
      while (true)
      {
        if (j < i)
        {
          if (k > 1);
          try
          {
            String str1;
            String str2;
            int m;
            if (this.HACK_ITEM_COUNT == 2)
              if (j == 0)
              {
                paramSharedPreferences = paramJSONArray.getJSONObject(k - 1);
                str1 = paramSharedPreferences.optString("url");
                str2 = paramSharedPreferences.optString("image");
                m = paramSharedPreferences.optInt("iD");
                if ((this.mImageViews.size() > j) && (this.mImageViews.get(j) != null))
                  break label286;
                paramSharedPreferences = new BaseBannerView.AdaptiveNetworkImageView(this, getContext());
                if (this.mImageViews.size() > j)
                  this.mImageViews.remove(j);
                this.mImageViews.add(j, paramSharedPreferences);
              }
            while (true)
            {
              ((NetworkImageView)paramSharedPreferences).setImage(str2);
              if (TextUtils.isEmpty(str1))
                break label442;
              paramSharedPreferences.setTag(new Announce(String.valueOf(m), str1));
              paramSharedPreferences.setOnClickListener(this);
              break label442;
              if (j == i - 1)
              {
                paramSharedPreferences = paramJSONArray.getJSONObject(0);
                break;
              }
              paramSharedPreferences = paramJSONArray.getJSONObject(j - 1);
              break;
              paramSharedPreferences = paramJSONArray.getJSONObject(j);
              break;
              paramSharedPreferences = (View)this.mImageViews.get(j);
            }
          }
          catch (JSONException paramSharedPreferences)
          {
            Log.e(getClass().getName(), "", paramSharedPreferences);
          }
        }
        else
        {
          this.mNaviDot.setTotalDot(k);
          if (k == 1)
            this.mNaviDot.setVisibility(8);
          while (true)
          {
            j = this.mImageViews.size() - 1;
            while (j >= i)
            {
              this.mImageViews.remove(j);
              j -= 1;
            }
            this.mNaviDot.setVisibility(0);
          }
          this.mPager.getAdapter().notifyDataSetChanged();
          paramJSONArray = this.mPager;
          if ((paramBoolean) && (k > 1));
          for (i = 1; ; i = 0)
          {
            paramJSONArray.setCurrentItem(i);
            setBannerCloseListener();
            return;
          }
        }
        j += 1;
      }
      i = 0;
      break;
    }
  }

  public void setBannerCloseListener()
  {
    setBtnOnCloseListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if (MainBannerView.this.mBannerCloseListener != null)
          MainBannerView.this.mBannerCloseListener.onClick(paramView);
        SharedPreferences.Editor localEditor = MainBannerView.this.mPref.edit();
        paramView = MainBannerView.this.mPref.getString("announce_list", null);
        ArrayList localArrayList = new ArrayList(20);
        if (!TextUtils.isEmpty(paramView))
          localArrayList.addAll(Arrays.asList(paramView.split(",")));
        int i = 0;
        try
        {
          while (i < MainBannerView.this.mAnnounceJsonArray.length())
          {
            paramView = String.valueOf(MainBannerView.this.mAnnounceJsonArray.getJSONObject(i).optInt("iD"));
            if ((!TextUtils.isEmpty(paramView)) && (!localArrayList.contains(paramView.hashCode() + "")))
              localArrayList.add(0, paramView.hashCode() + "");
            i += 1;
          }
        }
        catch (JSONException paramView)
        {
          paramView.printStackTrace();
          StringBuilder localStringBuilder = new StringBuilder();
          paramView = localArrayList;
          if (localArrayList.size() > 20)
            paramView = localArrayList.subList(0, 20);
          paramView = paramView.iterator();
          while (paramView.hasNext())
            localStringBuilder.append((String)paramView.next()).append(",");
          localEditor.putString("announce_list", localStringBuilder.toString());
          localEditor.putBoolean("announce_closed", true).commit();
        }
      }
    });
  }

  public void setBannerCloseListener(View.OnClickListener paramOnClickListener)
  {
    this.mBannerCloseListener = paramOnClickListener;
  }

  public void show()
  {
    this.announcelayHead.setVisibility(0);
  }

  class Announce
  {
    String mContent;
    String mTitle;

    public Announce(String paramString1, String arg3)
    {
      this.mTitle = paramString1;
      Object localObject;
      this.mContent = localObject;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.MainBannerView
 * JD-Core Version:    0.6.0
 */