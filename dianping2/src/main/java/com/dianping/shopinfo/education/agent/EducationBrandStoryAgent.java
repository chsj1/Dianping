package com.dianping.shopinfo.education.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.TicketCell;
import com.dianping.dataservice.Request;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.Response;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.shopinfo.base.ShopCellAgent;
import com.dianping.shopinfo.fragment.ShopInfoFragment;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;

public class EducationBrandStoryAgent extends ShopCellAgent
  implements RequestHandler
{
  private static final String CELL_BRAND_STORY = "0210Basic.03brandStory";
  private DPObject brandStory;
  private MApiRequest brandStoryReq;

  public EducationBrandStoryAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendRequset()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/edu/shopbrandinfo.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    this.brandStoryReq = mapiGet(this, localBuilder.toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.brandStoryReq, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if ((this.brandStory == null) || (TextUtils.isEmpty(this.brandStory.getString("BrandStory"))))
      return;
    paramBundle = createTicketCell();
    paramBundle.setTitle(this.brandStory.getString("BrandStory"));
    paramBundle.setIcon(this.res.getDrawable(R.drawable.scenic_intro_icon));
    paramBundle.setTitleLineSpacing(6.4F);
    paramBundle.setTitleMaxLines(2);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
    localLayoutParams.topMargin = ViewUtils.dip2px(getContext(), 4.3F);
    paramBundle.getTitleView().setLayoutParams(localLayoutParams);
    paramBundle.setGAString("edu_brandstory", getGAExtra());
    paramBundle.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        try
        {
          if (!TextUtils.isEmpty(EducationBrandStoryAgent.this.brandStory.getString("DetailLink")))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(EducationBrandStoryAgent.this.brandStory.getString("DetailLink")));
            EducationBrandStoryAgent.this.getFragment().startActivity(paramView);
          }
          return;
        }
        catch (java.lang.Exception paramView)
        {
        }
      }
    });
    addCell("0210Basic.03brandStory", paramBundle, 256);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequset();
  }

  public void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    if (this.brandStoryReq == paramRequest)
      this.brandStoryReq = null;
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    if (paramRequest == this.brandStoryReq)
    {
      this.brandStoryReq = null;
      this.brandStory = ((DPObject)paramResponse.result());
      if (this.brandStory != null)
        dispatchAgentChanged(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.agent.EducationBrandStoryAgent
 * JD-Core Version:    0.6.0
 */