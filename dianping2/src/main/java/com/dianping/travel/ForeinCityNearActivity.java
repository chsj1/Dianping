package com.dianping.travel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.SampleAdapter;
import com.dianping.base.widget.StaggeredGridView;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.Location;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class ForeinCityNearActivity extends NovaActivity
  implements RequestHandler<MApiRequest, MApiResponse>, AbsListView.OnScrollListener, AdapterView.OnItemClickListener
{
  private static final int REQUEST_COUNT = 25;
  private static final String TAG = ForeinCityNearActivity.class.getSimpleName();
  private LinearLayout distanceContainer;
  private ImageView distanceImageView;
  private TextView distanceTextView;
  private DPObject exploreDPObject;
  private DPObject[] exploreListDPObject;
  private boolean firstRequest = true;
  private View footerView;
  private boolean isEnd = false;
  private SampleAdapter mAdapter;
  private MApiRequest mApiRequest;
  private StaggeredGridView mGridView;
  private boolean mHasRequestedMore;
  protected ArrayList<DPObject> photos = new ArrayList();
  private int startIndex = 0;

  private void addItems(DPObject[] paramArrayOfDPObject)
  {
    if (paramArrayOfDPObject == null)
      return;
    this.mHasRequestedMore = false;
    runOnUiThread(new Runnable(paramArrayOfDPObject)
    {
      public void run()
      {
        ForeinCityNearActivity.this.mAdapter.addItems(this.val$dpObject);
        ForeinCityNearActivity.this.mAdapter.notifyDataSetChanged();
        if (ForeinCityNearActivity.this.isEnd)
          ForeinCityNearActivity.this.mGridView.removeFooterView(ForeinCityNearActivity.this.footerView);
        if (ForeinCityNearActivity.this.firstRequest)
        {
          ForeinCityNearActivity.this.showItemDistance(0);
          ForeinCityNearActivity.access$502(ForeinCityNearActivity.this, false);
        }
      }
    });
  }

  private void sendRequest(int paramInt)
  {
    if (this.mApiRequest != null)
      mapiService().abort(this.mApiRequest, this, true);
    this.mGridView.removeFooterView(this.footerView);
    this.mGridView.addFooterView(this.footerView);
    String str = "http://m.api.dianping.com/explore.overseas?cityid=" + cityId() + "&lat=" + location().latitude() + "&lng=" + location().longitude() + "&start=" + this.startIndex + "&limit=" + 25;
    com.dianping.util.Log.d(TAG, "url = " + str);
    this.mApiRequest = BasicMApiRequest.mapiGet(str, CacheType.HOURLY);
    mapiService().exec(this.mApiRequest, this);
  }

  private void showDistance(int paramInt)
  {
    if (paramInt < 100)
    {
      this.distanceTextView.setText("<100m");
      this.distanceImageView.setImageResource(R.drawable.find_walking_white);
      return;
    }
    if (paramInt < 500)
    {
      this.distanceTextView.setText("<500m");
      this.distanceImageView.setImageResource(R.drawable.find_walking_white);
      return;
    }
    if (paramInt < 1000)
    {
      this.distanceTextView.setText("<1km");
      this.distanceImageView.setImageResource(R.drawable.find_walking_white);
      return;
    }
    if (paramInt < 2000)
    {
      this.distanceTextView.setText("<2km");
      this.distanceImageView.setImageResource(R.drawable.find_walking_white);
      return;
    }
    if (paramInt < 3000)
    {
      this.distanceTextView.setText("<3km");
      this.distanceImageView.setImageResource(R.drawable.find_bus_white);
      return;
    }
    if (paramInt < 5000)
    {
      this.distanceTextView.setText("<5km");
      this.distanceImageView.setImageResource(R.drawable.find_bus_white);
      return;
    }
    if (paramInt < 10000)
    {
      this.distanceTextView.setText("<10km");
      this.distanceImageView.setImageResource(R.drawable.find_bus_white);
      return;
    }
    if (paramInt < 20000)
    {
      this.distanceTextView.setText("<20km");
      this.distanceImageView.setImageResource(R.drawable.find_train_white);
      return;
    }
    if (paramInt < 100000)
    {
      this.distanceTextView.setText("<100km");
      this.distanceImageView.setImageResource(R.drawable.find_train_white);
      return;
    }
    if (paramInt < 500000)
    {
      this.distanceTextView.setText("<500km");
      this.distanceImageView.setImageResource(R.drawable.find_train_white);
      return;
    }
    this.distanceTextView.setText(">500km");
    this.distanceImageView.setImageResource(R.drawable.find_airplane_white);
  }

  private void showItemDistance(int paramInt)
  {
    if (this.mAdapter == null);
    DPObject localDPObject;
    do
    {
      return;
      localDPObject = (DPObject)this.mAdapter.getItem(paramInt);
    }
    while (localDPObject == null);
    showDistance(localDPObject.getInt("Distance"));
  }

  @SuppressLint({"NewApi"})
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.forein_city_nearby_activity);
    this.mGridView = ((StaggeredGridView)findViewById(R.id.grid_view));
    this.mAdapter = new SampleAdapter(this, (ViewUtils.getScreenWidthPixels(this) - ViewUtils.dip2px(this, 8.0F) * 3) / 2);
    this.footerView = getLayoutInflater().inflate(R.layout.loading_item, null);
    this.mGridView.addFooterView(this.footerView);
    this.mGridView.setAdapter(this.mAdapter);
    this.mGridView.setOnScrollListener(this);
    this.mGridView.setOnItemClickListener(this);
    this.distanceContainer = ((LinearLayout)findViewById(R.id.distance_container));
    this.distanceTextView = ((TextView)findViewById(R.id.distance));
    this.distanceImageView = ((ImageView)findViewById(R.id.distance_image));
    if (paramBundle != null)
    {
      this.exploreDPObject = ((DPObject)paramBundle.getParcelable("ExploreDPObject"));
      if (this.exploreDPObject != null)
      {
        this.exploreListDPObject = this.exploreDPObject.getArray("List");
        this.mApiRequest = null;
        addItems(this.exploreListDPObject);
      }
    }
    sendRequest(this.startIndex);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = (DPObject)paramView.getTag(R.id.dp_object);
    if (paramAdapterView != null)
    {
      paramAdapterView = paramAdapterView.getString("Url");
      if ((paramAdapterView != null) && (!TextUtils.isEmpty(paramAdapterView)))
      {
        paramView = new Intent("android.intent.action.VIEW", Uri.parse(paramAdapterView));
        statisticsEvent("overseas", "oversea_findnear_click", paramAdapterView, paramInt);
        startActivity(paramView);
      }
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    com.dianping.util.Log.d(TAG, "onRequestFinish");
    if ((paramMApiRequest != null) && (this.mApiRequest == paramMApiRequest))
    {
      this.mApiRequest = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        this.exploreDPObject = ((DPObject)paramMApiResponse.result());
        this.startIndex = this.exploreDPObject.getInt("NextStartIndex");
        this.isEnd = this.exploreDPObject.getBoolean("IsEnd");
        this.exploreListDPObject = this.exploreDPObject.getArray("List");
        this.mApiRequest = null;
        addItems(this.exploreListDPObject);
        if (this.distanceContainer != null)
          this.distanceContainer.post(new Runnable()
          {
            public void run()
            {
              ForeinCityNearActivity.this.distanceContainer.setVisibility(0);
            }
          });
      }
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("ExploreDPObject", this.exploreDPObject);
  }

  public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
  {
    showItemDistance(paramInt1);
    if ((!this.mHasRequestedMore) && (paramInt1 + paramInt2 >= paramInt3) && (!this.isEnd))
    {
      android.util.Log.d(TAG, "onScroll lastInScreen - so load more");
      this.mHasRequestedMore = true;
      sendRequest(this.startIndex);
    }
  }

  public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.ForeinCityNearActivity
 * JD-Core Version:    0.6.0
 */