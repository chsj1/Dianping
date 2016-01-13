package com.dianping.main.user.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.util.DPUrl;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.dianping.widget.pulltorefresh.PullToRefreshScrollView;

public class FeedbackTypeActivity extends NovaActivity
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final String URL_CATEGORY = "http://m.api.dianping.com/user/feedbackcategory.bin";
  private DPObject[] mCategories;
  private int mDepth = 0;
  private BasicMApiRequest mFeedbackTypeRequest;
  private LinearLayout mLayTypeContainer;

  private View createTypeItemView(DPObject paramDPObject)
  {
    View localView = getLayoutInflater().inflate(R.layout.item_feedback_type, this.mLayTypeContainer, false);
    ((TextView)localView.findViewById(R.id.tv_text)).setText(paramDPObject.getString("Name"));
    localView.setOnClickListener(this);
    localView.setTag(paramDPObject);
    return localView;
  }

  private void setupViews(DPObject[] paramArrayOfDPObject)
  {
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length == 0));
    while (true)
    {
      return;
      this.mLayTypeContainer.removeAllViews();
      int j = paramArrayOfDPObject.length;
      int i = 0;
      while (i < j)
      {
        View localView = createTypeItemView(paramArrayOfDPObject[i]);
        this.mLayTypeContainer.addView(localView);
        i += 1;
      }
    }
  }

  public void onBackPressed()
  {
    if (this.mDepth > 0)
    {
      this.mDepth -= 1;
      setupViews(this.mCategories);
      return;
    }
    super.onBackPressed();
  }

  public void onClick(View paramView)
  {
    if ((paramView.getTag() != null) && ((paramView.getTag() instanceof DPObject)))
    {
      paramView = (DPObject)paramView.getTag();
      if ((paramView.getArray("SubCategoryList") != null) && (paramView.getArray("SubCategoryList").length > 0))
      {
        setupViews(paramView.getArray("SubCategoryList"));
        this.mDepth += 1;
      }
    }
    else
    {
      return;
    }
    DPUrl localDPUrl = new DPUrl("dianping://feedback");
    localDPUrl.putParam("flag", paramView.getInt("ID"));
    startActivity(localDPUrl);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.activity_feedback_type);
    this.mLayTypeContainer = ((LinearLayout)findViewById(R.id.lay_type_container));
    ((PullToRefreshScrollView)findViewById(R.id.pull_scrollview)).setMode(PullToRefreshBase.Mode.DISABLED);
    reset();
  }

  protected void onDestroy()
  {
    super.onDestroy();
    if (this.mFeedbackTypeRequest != null)
      mapiService().abort(this.mFeedbackTypeRequest, null, true);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.mFeedbackTypeRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mFeedbackTypeRequest)
    {
      if ((paramMApiResponse.result() instanceof DPObject[]))
      {
        paramMApiRequest = (DPObject[])(DPObject[])paramMApiResponse.result();
        this.mCategories = paramMApiRequest;
        this.mDepth = 0;
        setupViews(paramMApiRequest);
      }
      this.mFeedbackTypeRequest = null;
    }
  }

  public void reset()
  {
    if (this.mFeedbackTypeRequest != null)
      mapiService().abort(this.mFeedbackTypeRequest, null, true);
    this.mFeedbackTypeRequest = new BasicMApiRequest("http://m.api.dianping.com/user/feedbackcategory.bin", "GET", null, CacheType.CRITICAL, false, null);
    mapiService().exec(this.mFeedbackTypeRequest, this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.activity.FeedbackTypeActivity
 * JD-Core Version:    0.6.0
 */