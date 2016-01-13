package com.dianping.shopinfo.education.agent;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
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

public class SchoolOrderAgent extends ShopCellAgent
  implements RequestHandler
{
  private static final String CELL_SCHOOL_ORDER = "0210Basic.02SchoolOrder";
  private DPObject schoolOrder;
  private MApiRequest schoolOrderReq;

  public SchoolOrderAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendRequset()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/edu/schoolrankinginfo.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    this.schoolOrderReq = mapiGet(this, localBuilder.toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.schoolOrderReq, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if ((this.schoolOrder == null) || (TextUtils.isEmpty(this.schoolOrder.getString("Title"))))
      return;
    paramBundle = createTicketCell();
    paramBundle.setTitle(this.schoolOrder.getString("Title"));
    if (this.schoolOrder.getInt("Ranking") > 0)
    {
      localObject = new SpannableString("全国第" + this.schoolOrder.getInt("Ranking") + "");
      ((SpannableString)localObject).setSpan(new AbsoluteSizeSpan(12, true), 0, 3, 33);
      ((SpannableString)localObject).setSpan(new AbsoluteSizeSpan(21, true), 3, ((SpannableString)localObject).length(), 33);
      ((SpannableString)localObject).setSpan(new ForegroundColorSpan(Color.rgb(255, 102, 51)), 3, ((SpannableString)localObject).length(), 33);
      ((SpannableString)localObject).setSpan(new ForegroundColorSpan(Color.rgb(153, 153, 153)), 0, 3, 33);
      paramBundle.getRightText().setVisibility(0);
      paramBundle.getRightText().setText((CharSequence)localObject);
    }
    paramBundle.setIcon(this.res.getDrawable(R.drawable.school_order));
    paramBundle.setTitleLineSpacing(6.4F);
    paramBundle.setTitleMaxLines(1);
    Object localObject = new LinearLayout.LayoutParams(-1, -2);
    ((LinearLayout.LayoutParams)localObject).topMargin = ViewUtils.dip2px(getContext(), 4.3F);
    paramBundle.getTitleView().setLayoutParams((ViewGroup.LayoutParams)localObject);
    paramBundle.setGAString("edu_schoolrank", getGAExtra());
    paramBundle.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        try
        {
          if (!TextUtils.isEmpty(SchoolOrderAgent.this.schoolOrder.getString("DetailLink")))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(SchoolOrderAgent.this.schoolOrder.getString("DetailLink")));
            SchoolOrderAgent.this.getFragment().startActivity(paramView);
          }
          return;
        }
        catch (java.lang.Exception paramView)
        {
        }
      }
    });
    addCell("0210Basic.02SchoolOrder", paramBundle, 256);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequset();
  }

  public void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    if (this.schoolOrderReq == paramRequest)
      this.schoolOrderReq = null;
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    if (paramRequest == this.schoolOrderReq)
    {
      this.schoolOrderReq = null;
      this.schoolOrder = ((DPObject)paramResponse.result());
      if (this.schoolOrder != null)
        dispatchAgentChanged(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.education.agent.SchoolOrderAgent
 * JD-Core Version:    0.6.0
 */