package com.dianping.wed.baby.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.loading.LoadingLayout;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import java.util.List;

public class BabyPhotoActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  final String API_URL = "http://m.api.dianping.com/wedding/scrollpiclist.bin";
  ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener()
  {
    public void onPageScrollStateChanged(int paramInt)
    {
    }

    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
    {
    }

    public void onPageSelected(int paramInt)
    {
      BabyPhotoActivity.this.viewIndex.setText(paramInt + 1 + "/" + BabyPhotoActivity.this.photoObjs.length);
      BabyPhotoActivity.this.viewName.setText(BabyPhotoActivity.this.getObjName(paramInt));
    }
  };
  PagerAdapter pagerAdapter = new PagerAdapter()
  {
    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
    {
      paramViewGroup.removeView((View)BabyPhotoActivity.this.photoViews.get(paramInt));
    }

    public int getCount()
    {
      return BabyPhotoActivity.this.photoViews.size();
    }

    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
    {
      paramViewGroup.addView((View)BabyPhotoActivity.this.photoViews.get(paramInt));
      return BabyPhotoActivity.this.photoViews.get(paramInt);
    }

    public boolean isViewFromObject(View paramView, Object paramObject)
    {
      return paramView == paramObject;
    }
  };
  int photoIndex;
  DPObject[] photoObjs;
  MApiRequest photoRequest;
  List<View> photoViews;
  String picRefType;
  String shopId;
  TextView viewDes;
  TextView viewIndex;
  TextView viewName;
  ViewPager viewPager;

  String getObjDes(int paramInt)
  {
    DPObject[] arrayOfDPObject = this.photoObjs[paramInt].getArray("Desc");
    if ((arrayOfDPObject != null) && (arrayOfDPObject.length > 0))
    {
      paramInt = 0;
      while (paramInt < arrayOfDPObject.length)
      {
        if ("teacherCer".equals(arrayOfDPObject[paramInt].getString("ID")))
          return arrayOfDPObject[paramInt].getString("Name");
        paramInt += 1;
      }
    }
    return "";
  }

  String getObjName(int paramInt)
  {
    DPObject[] arrayOfDPObject = this.photoObjs[paramInt].getArray("Desc");
    if ((arrayOfDPObject != null) && (arrayOfDPObject.length > 0))
    {
      paramInt = 0;
      while (paramInt < arrayOfDPObject.length)
      {
        if (("cerDes".equals(arrayOfDPObject[paramInt].getString("ID"))) || ("teacherName".equals(arrayOfDPObject[paramInt].getString("ID"))))
          return arrayOfDPObject[paramInt].getString("Name");
        paramInt += 1;
      }
    }
    return "";
  }

  public void onClick(View paramView)
  {
    onBackPressed();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.wed_activity_babyphoto);
    if ((getIntent() != null) && (getIntent().getData() != null))
    {
      this.shopId = getStringParam("shopid");
      this.picRefType = getStringParam("picreftype");
    }
    for (this.photoIndex = getIntParam("index"); ; this.photoIndex = paramBundle.getInt("index"))
    {
      do
      {
        this.viewPager = ((ViewPager)findViewById(R.id.viewpager_babyphoto));
        this.viewName = ((TextView)findViewById(R.id.textview_babyphoto));
        this.viewDes = ((TextView)findViewById(R.id.textview_babydes));
        this.viewIndex = ((TextView)findViewById(R.id.textview_babyphoto_index));
        ((ImageButton)findViewById(R.id.imagebutton_babyphoto_back)).setOnClickListener(this);
        hideTitleBar();
        sendPhotoRequest();
        return;
      }
      while ((paramBundle == null) || (!paramBundle.containsKey("shopid")));
      this.shopId = paramBundle.getString("shopid");
      this.picRefType = paramBundle.getString("picreftype");
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.photoRequest)
    {
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      paramMApiResponse = paramMApiRequest.getString("Title");
      if (!TextUtils.isEmpty(paramMApiResponse))
        setBabyTitle(paramMApiResponse);
      this.photoObjs = paramMApiRequest.getArray("List");
      if ((this.photoObjs != null) && (this.photoObjs.length > 0))
      {
        if ((this.photoIndex < 0) || (this.photoIndex >= this.photoObjs.length))
          this.photoIndex = 0;
        if (this.photoViews == null)
        {
          this.photoViews = new ArrayList();
          this.viewPager.setAdapter(this.pagerAdapter);
          this.viewPager.setOnPageChangeListener(this.onPageChangeListener);
        }
        while (true)
        {
          int i = 0;
          while (i < this.photoObjs.length)
          {
            paramMApiRequest = new LoadingLayout(this);
            paramMApiRequest.creatLoadingLayout(false, true, true);
            paramMApiRequest.setImageUrl(this.photoObjs[i].getString("PicUrl"));
            this.photoViews.add(paramMApiRequest);
            i += 1;
          }
          this.photoViews.clear();
          this.pagerAdapter.notifyDataSetChanged();
        }
        this.pagerAdapter.notifyDataSetChanged();
        this.viewPager.setCurrentItem(this.photoIndex);
        this.viewIndex.setText(this.photoIndex + 1 + "/" + this.photoObjs.length);
        this.viewName.setText(getObjName(this.photoIndex));
        if (!TextUtils.isEmpty(getObjDes(this.photoIndex)))
        {
          this.viewDes.setVisibility(0);
          this.viewDes.setText(getObjDes(this.photoIndex));
        }
      }
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("shopid", this.shopId);
    paramBundle.putString("picreftype", this.picRefType);
    paramBundle.putInt("index", this.photoIndex);
  }

  void sendPhotoRequest()
  {
    if (this.photoRequest != null)
      return;
    this.photoRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/wedding/scrollpiclist.bin").buildUpon().appendQueryParameter("shopid", this.shopId).appendQueryParameter("picreftype", this.picRefType).toString(), CacheType.NORMAL);
    mapiService().exec(this.photoRequest, this);
  }

  void setBabyTitle(String paramString)
  {
    TextView localTextView = (TextView)findViewById(R.id.textview_babyphoto_title);
    if (localTextView != null)
      localTextView.setText(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.activity.BabyPhotoActivity
 * JD-Core Version:    0.6.0
 */