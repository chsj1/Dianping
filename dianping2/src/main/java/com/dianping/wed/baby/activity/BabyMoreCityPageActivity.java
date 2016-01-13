package com.dianping.wed.baby.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.LoadingErrorView;
import com.dianping.widget.LoadingErrorView.LoadRetry;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaTextView;

public class BabyMoreCityPageActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, View.OnClickListener
{
  private static final int REQUEST_STATUS_DONE = 2;
  private static final int REQUEST_STATUS_ERROR = 3;
  private static final int REQUEST_STATUS_INIT = 0;
  static final int REQUEST_STATUS_LOADING = 1;
  private int mRequestStatus = 0;
  LinearLayout mainLayout;
  MApiRequest request;

  private LinearLayout getCityLine(int paramInt1, int paramInt2)
  {
    NovaLinearLayout localNovaLinearLayout = new NovaLinearLayout(this);
    localNovaLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, paramInt1));
    localNovaLinearLayout.setBackgroundColor(getResources().getColor(R.color.gray_light_background));
    localNovaLinearLayout.setOrientation(0);
    localNovaLinearLayout.setGravity(16);
    localNovaLinearLayout.setPadding(0, paramInt2, 0, 0);
    return localNovaLinearLayout;
  }

  private TextView getCityTag(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    if (TextUtils.isEmpty(paramString))
      return null;
    TextView localTextView = new TextView(this);
    localTextView.setText(paramString);
    localTextView.setHeight(paramInt1);
    localTextView.setGravity(17);
    localTextView.setTag(paramString);
    localTextView.setTextColor(getResources().getColor(R.color.dark_black));
    localTextView.setTextSize(2, 14.0F);
    localTextView.setBackgroundResource(R.drawable.background_round_corner_border);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
    localLayoutParams.setMargins(paramInt3, 0, 0, 0);
    localTextView.setLayoutParams(localLayoutParams);
    if (paramString.length() >= 4)
      localTextView.setWidth(paramInt2 * 2 + paramInt3);
    while (true)
    {
      localTextView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          paramView = (String)paramView.getTag();
          if (TextUtils.isEmpty(paramView))
            return;
          BabyMoreCityPageActivity.this.getIntent().putExtra("city", paramView);
          BabyMoreCityPageActivity.this.setResult(-1, BabyMoreCityPageActivity.this.getIntent());
          BabyMoreCityPageActivity.this.finish();
        }
      });
      return localTextView;
      localTextView.setWidth(paramInt2);
    }
  }

  private void updateView(DPObject[] paramArrayOfDPObject)
  {
    if (this.mainLayout == null);
    while (true)
    {
      return;
      this.mainLayout.removeAllViews();
      if (this.mRequestStatus == 1)
      {
        paramArrayOfDPObject = LayoutInflater.from(this).inflate(R.layout.loading_item, this.mainLayout, false);
        this.mainLayout.addView(paramArrayOfDPObject);
        return;
      }
      if (this.mRequestStatus == 3)
      {
        paramArrayOfDPObject = LayoutInflater.from(this).inflate(R.layout.error_item, this.mainLayout, false);
        if ((paramArrayOfDPObject instanceof LoadingErrorView))
          ((LoadingErrorView)paramArrayOfDPObject).setCallBack(new LoadingErrorView.LoadRetry()
          {
            public void loadRetry(View paramView)
            {
              BabyMoreCityPageActivity.this.sendRequest();
            }
          });
        this.mainLayout.addView(paramArrayOfDPObject);
        return;
      }
      if (this.mRequestStatus != 2)
        continue;
      int i2 = ViewUtils.dip2px(this, 15.0F);
      int i = ViewUtils.getScreenWidthPixels(this);
      int i3 = (i - i2 * 5) / 4;
      int i4 = i3 / 2;
      int i5 = (i - i2 * 3) / 2;
      i = 0;
      while (i < paramArrayOfDPObject.length)
      {
        Object localObject2 = paramArrayOfDPObject[i];
        if (localObject2 != null)
        {
          Object localObject1 = ((DPObject)localObject2).getString("Name");
          Object localObject3 = new NovaTextView(this);
          LinearLayout.LayoutParams localLayoutParams1 = new LinearLayout.LayoutParams(-1, -2);
          localLayoutParams1.leftMargin = i2;
          localLayoutParams1.topMargin = ViewUtils.dip2px(this, 15.0F);
          ((NovaTextView)localObject3).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_22));
          ((NovaTextView)localObject3).setTextColor(getResources().getColor(R.color.review_time_color));
          ((NovaTextView)localObject3).setText((CharSequence)localObject1);
          this.mainLayout.addView((View)localObject3, localLayoutParams1);
          localObject1 = new NovaLinearLayout(this);
          ((NovaLinearLayout)localObject1).setOrientation(1);
          localObject2 = ((DPObject)localObject2).getArray("List");
          int j = 0;
          int k = 0;
          int m = 0;
          if (m < localObject2.length)
          {
            localObject3 = localObject2[m].getString("Name");
            if ((localObject3 != null) && (((String)localObject3).length() < 4))
              j += 1;
            while (true)
            {
              m += 1;
              break;
              k += 1;
            }
          }
          label380: int n;
          if (j % 4 == 0)
          {
            m = j / 4;
            if (k % 2 != 0)
              break label611;
            k /= 2;
            n = 0;
          }
          int i1;
          String str;
          NovaTextView localNovaTextView;
          LinearLayout.LayoutParams localLayoutParams2;
          while (true)
          {
            if (n >= m)
              break label639;
            localObject3 = new NovaLinearLayout(this);
            ((NovaLinearLayout)localObject3).setOrientation(0);
            localLayoutParams1 = new LinearLayout.LayoutParams(-1, -2);
            localLayoutParams1.topMargin = ViewUtils.dip2px(this, 10.0F);
            i1 = 0;
            while (true)
              if (i1 < 4)
              {
                int i6 = n * 4 + i1;
                if ((i6 < j) && (i6 < localObject2.length))
                {
                  str = localObject2[i6].getString("Name");
                  localNovaTextView = new NovaTextView(this);
                  localLayoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                  localLayoutParams2.width = i3;
                  localLayoutParams2.height = i4;
                  localLayoutParams2.leftMargin = i2;
                  localNovaTextView.setGravity(17);
                  localNovaTextView.setTextColor(getResources().getColor(R.color.deep_gray));
                  localNovaTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
                  localNovaTextView.setBackgroundResource(R.drawable.background_white_gray);
                  localNovaTextView.setText(str);
                  localNovaTextView.setTag(str);
                  localNovaTextView.setOnClickListener(this);
                  ((NovaLinearLayout)localObject3).addView(localNovaTextView, localLayoutParams2);
                  i1 += 1;
                  continue;
                  m = j / 4 + 1;
                  break;
                  label611: k = k / 2 + 1;
                  break label380;
                }
              }
            ((NovaLinearLayout)localObject1).addView((View)localObject3, localLayoutParams1);
            n += 1;
          }
          label639: m = 0;
          while (m < k)
          {
            localObject3 = new NovaLinearLayout(this);
            ((NovaLinearLayout)localObject3).setOrientation(0);
            localLayoutParams1 = new LinearLayout.LayoutParams(-1, -2);
            localLayoutParams1.topMargin = ViewUtils.dip2px(this, 10.0F);
            n = 0;
            while (n < 2)
            {
              i1 = m * 2 + n + j;
              if (i1 >= localObject2.length)
                break;
              str = localObject2[i1].getString("Name");
              localNovaTextView = new NovaTextView(this);
              localLayoutParams2 = new LinearLayout.LayoutParams(-1, -2);
              localLayoutParams2.width = i5;
              localLayoutParams2.height = i4;
              localLayoutParams2.leftMargin = i2;
              localNovaTextView.setGravity(17);
              localNovaTextView.setTextColor(getResources().getColor(R.color.deep_gray));
              localNovaTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
              localNovaTextView.setText(str);
              localNovaTextView.setTag(str);
              localNovaTextView.setBackgroundResource(R.drawable.background_white_gray);
              localNovaTextView.setOnClickListener(this);
              ((NovaLinearLayout)localObject3).addView(localNovaTextView, localLayoutParams2);
              n += 1;
            }
            ((NovaLinearLayout)localObject1).addView((View)localObject3, localLayoutParams1);
            m += 1;
          }
          this.mainLayout.addView((View)localObject1);
        }
        i += 1;
      }
    }
  }

  public void onClick(View paramView)
  {
    paramView = (String)paramView.getTag();
    if (TextUtils.isEmpty(paramView))
      return;
    getIntent().putExtra("city", paramView);
    setResult(-1, getIntent());
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setTitle("");
    setContentView(R.layout.activity_morecity);
    this.mainLayout = ((LinearLayout)findViewById(R.id.wed_morecity_layout));
    sendRequest();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.request != null)
    {
      mapiService().abort(this.request, this, true);
      this.request = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    this.mRequestStatus = 3;
    updateView(null);
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    paramMApiRequest = (DPObject)paramMApiResponse.result();
    if (paramMApiRequest == null);
    do
    {
      return;
      paramMApiRequest = paramMApiRequest.getArray("List");
    }
    while ((paramMApiRequest == null) || (paramMApiRequest.length == 0));
    this.mRequestStatus = 2;
    updateView(paramMApiRequest);
  }

  void sendRequest()
  {
    this.request = BasicMApiRequest.mapiGet("http://m.api.dianping.com/wedding/photoloclist.bin".toString(), CacheType.DISABLED);
    mapiService().exec(this.request, this);
    this.mRequestStatus = 1;
    updateView(null);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.baby.activity.BabyMoreCityPageActivity
 * JD-Core Version:    0.6.0
 */