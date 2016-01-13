package com.dianping.wed.home.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentActivity;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.wed.home.fragment.HomeMainFragment;
import com.dianping.widget.PagerAdapter;
import com.dianping.widget.VerticalViewPager;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HomeMainActivity extends AgentActivity
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private MApiRequest homeSearchIconRequest;
  private VerticalViewPager mViewPager;
  private ViewGroup toolbarView;

  private void initView()
  {
    getTitleBar().addRightViewItem("search", R.drawable.navibar_icon_search, new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = new Intent("android.intent.action.VIEW");
        try
        {
          String str1 = URLEncoder.encode("dianping://searchshoplist?categoryid=90", "UTF-8");
          String str2 = URLEncoder.encode("http://m.api.dianping.com/advancedsuggest.bin?cityid=" + HomeMainActivity.this.cityId() + "&myacc=65.000000", "UTF-8");
          paramView.setData(Uri.parse("dianping://websearch?searchurl=" + str1 + "&keywordurl=" + str2 + "&defaultkey=HomeSuggestSearchHistory"));
          HomeMainActivity.this.startActivity(paramView);
          return;
        }
        catch (UnsupportedEncodingException paramView)
        {
          paramView.printStackTrace();
        }
      }
    });
    this.toolbarView = ((ViewGroup)findViewById(16908307));
    this.toolbarView.setVisibility(8);
    this.mViewPager = ((VerticalViewPager)findViewById(R.id.viewpager));
    this.mViewPager.setAdapter(new MyPagerAdapter(null));
    this.mViewPager.setPadding(0, 0, 0, 0);
  }

  protected AgentFragment getAgentFragment()
  {
    return null;
  }

  protected void initViewAgentView(Bundle paramBundle)
  {
    super.setContentView(R.layout.shop_info_frame);
    statisticsEvent("homemain6", "homemain6_home", "", 0, null);
    Object localObject = Uri.parse("http://m.api.dianping.com/wedding/homesearchicon.bin").buildUpon();
    ((Uri.Builder)localObject).appendQueryParameter("cityid", cityId() + "");
    this.homeSearchIconRequest = BasicMApiRequest.mapiGet(((Uri.Builder)localObject).build().toString(), CacheType.DISABLED);
    mapiService().exec(this.homeSearchIconRequest, this);
    initView();
    if (paramBundle == null)
    {
      paramBundle = new HomeMainFragment();
      localObject = getSupportFragmentManager().beginTransaction();
      ((FragmentTransaction)localObject).add(paramBundle, "content");
      ((FragmentTransaction)localObject).commit();
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.homeSearchIconRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest == this.homeSearchIconRequest) && ((paramMApiResponse.result() instanceof DPObject)))
    {
      paramMApiRequest = ((DPObject)paramMApiResponse.result()).getString("Url");
      if (!TextUtils.isEmpty(paramMApiRequest))
      {
        paramMApiResponse = getTitleBar().findRightViewItemByTag("search");
        if (paramMApiResponse != null)
          paramMApiResponse.setOnClickListener(new View.OnClickListener(paramMApiRequest)
          {
            public void onClick(View paramView)
            {
              paramView = new Intent("android.intent.action.VIEW");
              paramView.setData(Uri.parse(this.val$url));
              HomeMainActivity.this.startActivity(paramView);
            }
          });
      }
    }
  }

  private class MyPagerAdapter extends PagerAdapter
  {
    private MyPagerAdapter()
    {
    }

    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
    {
      paramViewGroup.removeView((View)paramObject);
    }

    public int getCount()
    {
      return 1;
    }

    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
    {
      View localView = ((HomeMainFragment)HomeMainActivity.this.getSupportFragmentManager().findFragmentByTag("content")).mFragmentView;
      paramViewGroup.addView(localView);
      return localView;
    }

    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return paramView == paramObject;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.home.activity.HomeMainActivity
 * JD-Core Version:    0.6.0
 */