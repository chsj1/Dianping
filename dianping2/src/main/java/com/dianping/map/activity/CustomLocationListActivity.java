package com.dianping.map.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.ArchiveException;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.ButtonSearchBar;
import com.dianping.base.widget.ButtonSearchBar.ButtonSearchBarListener;
import com.dianping.locationservice.LocationService;
import com.dianping.map.fragment.GooglePlacesSearchFragment;
import com.dianping.map.fragment.GooglePlacesSearchFragment.OnResultItemClickListner;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.lang.reflect.Method;

public class CustomLocationListActivity extends NovaActivity
  implements GooglePlacesSearchFragment.OnResultItemClickListner
{
  private static final int REQUEST_CODE_GOOGLE_PLACES_SEARCH = 0;
  private static final int REQUEST_CODE_MAP_MODE = 1;
  public boolean hideMap;
  ButtonSearchBar mButtonSearchBar;
  GooglePlacesSearchFragment mGooglePlacesSearchFragment;

  private String[] initSearchCenter()
  {
    String[] arrayOfString = new String[2];
    Object localObject1 = getSharedPreferences(getPackageName(), 0).getString("findconditions_region", null);
    if (localObject1 != null)
    {
      localObject1 = ((String)localObject1).split("IAMSPLIT");
      if (localObject1.length == 4)
      {
        arrayOfString[0] = localObject1[0];
        arrayOfString[1] = localObject1[1];
        return arrayOfString;
      }
    }
    Object localObject3 = null;
    localObject1 = localObject3;
    if (locationService().hasLocation());
    try
    {
      localObject1 = (Location)locationService().location().decodeToObject(Location.DECODER);
      if ((localObject1 == null) || (((Location)localObject1).city().id() != cityId()))
      {
        arrayOfString[0] = String.valueOf(city().latitude());
        arrayOfString[1] = String.valueOf(city().longitude());
        return arrayOfString;
      }
    }
    catch (ArchiveException localObject2)
    {
      Object localObject2;
      while (true)
      {
        localArchiveException.printStackTrace();
        localObject2 = localObject3;
      }
      arrayOfString[0] = String.valueOf(localObject2.latitude());
      arrayOfString[1] = String.valueOf(localObject2.longitude());
    }
    return (String)arrayOfString;
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (paramInt2 == -1)
    {
      if (paramInt1 == 1)
      {
        setResult(-1, paramIntent);
        finish();
        return;
      }
      if (paramInt1 == 0)
      {
        onResultItemClick(paramIntent);
        return;
      }
    }
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.customloction_list);
    this.hideMap = getIntent().getBooleanExtra("hideMap", false);
    paramBundle = getIntent().getStringExtra("hint");
    this.mButtonSearchBar = ((ButtonSearchBar)findViewById(R.id.button_search_bar));
    if (TextUtils.isEmpty(paramBundle))
      this.mButtonSearchBar.setHint("输入你想查找的地点");
    while (true)
    {
      paramBundle = initSearchCenter();
      this.mButtonSearchBar.setButtonSearchBarListener(new ButtonSearchBar.ButtonSearchBarListener(paramBundle)
      {
        public void onSearchRequested()
        {
          Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://googleplacessearch?lat=" + this.val$mSearchCenter[0] + "&lng=" + this.val$mSearchCenter[1]));
          CustomLocationListActivity.this.startActivityForResult(localIntent, 0);
          try
          {
            Activity.class.getDeclaredMethod("overridePendingTransition", new Class[] { Integer.TYPE, Integer.TYPE }).invoke(CustomLocationListActivity.this, new Object[] { null, null });
            return;
          }
          catch (Exception localException)
          {
            localException.printStackTrace();
          }
        }
      });
      this.mGooglePlacesSearchFragment = ((GooglePlacesSearchFragment)getSupportFragmentManager().findFragmentById(R.id.google_places_search_list_fragment));
      this.mGooglePlacesSearchFragment.setOnResultItemClickListner(this);
      if (!this.hideMap)
        setRightTitleButton(R.drawable.ic_loc_in_map, new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://customlocationmapbar"));
            CustomLocationListActivity.this.startActivityForResult(paramView, 1);
            CustomLocationListActivity.this.statisticsEvent("localsearch5", "localsearch5_map", "", 0);
          }
        });
      return;
      this.mButtonSearchBar.setHint(paramBundle);
    }
  }

  public void onResultItemClick(Intent paramIntent)
  {
    Intent localIntent = new Intent();
    localIntent.putExtra("lat", Double.valueOf(paramIntent.getStringExtra("lat")));
    localIntent.putExtra("lng", Double.valueOf(paramIntent.getStringExtra("lng")));
    localIntent.putExtra("address", paramIntent.getStringExtra("title"));
    localIntent.putExtra("maptype", 1);
    setResult(-1, localIntent);
    statisticsEvent("area5", "area5_keyword", "", 0);
    finish();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.map.activity.CustomLocationListActivity
 * JD-Core Version:    0.6.0
 */