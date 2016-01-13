package com.dianping.shopinfo.education.agent;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
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

public class SchoolFlowerOrderAgent extends ShopCellAgent
  implements RequestHandler
{
  private static final String CELL_SCHOOL_ORDER = "0210Basic.03SchoolFlowerOrder";
  private DPObject schoolOrder;
  private MApiRequest schoolOrderReq;

  public SchoolFlowerOrderAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendRequset()
  {
    Uri.Builder localBuilder = Uri.parse("http://mapi.dianping.com/edu/schoolbeautyrankinginfo.bin").buildUpon();
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
    int i = this.schoolOrder.getInt("Ranking");
    Object localObject2 = this.schoolOrder.getString("RankDescription");
    if (i > 0)
    {
      localObject1 = new SpannableStringBuilder();
      localObject2 = new SpannableString((CharSequence)localObject2);
      ((SpannableString)localObject2).setSpan(new AbsoluteSizeSpan(12, true), 0, ((SpannableString)localObject2).length(), 33);
      ((SpannableString)localObject2).setSpan(new ForegroundColorSpan(Color.rgb(153, 153, 153)), 0, ((SpannableString)localObject2).length(), 33);
      SpannableString localSpannableString = new SpannableString(this.schoolOrder.getInt("Ranking") + "");
      localSpannableString.setSpan(new AbsoluteSizeSpan(21, true), 0, localSpannableString.length(), 33);
      localSpannableString.setSpan(new ForegroundColorSpan(Color.rgb(255, 102, 51)), 0, localSpannableString.length(), 33);
      ((SpannableStringBuilder)localObject1).append((CharSequence)localObject2);
      ((SpannableStringBuilder)localObject1).append(localSpannableString);
      paramBundle.getRightText().setVisibility(0);
      paramBundle.getRightText().setText((CharSequence)localObject1);
    }
    paramBundle.setIcon(this.res.getDrawable(R.drawable.school_flower_order));
    paramBundle.setTitleLineSpacing(6.4F);
    paramBundle.setTitleMaxLines(1);
    Object localObject1 = new LinearLayout.LayoutParams(-1, -2);
    ((LinearLayout.LayoutParams)localObject1).topMargin = ViewUtils.dip2px(getContext(), 4.3F);
    paramBundle.getTitleView().setLayoutParams((ViewGroup.LayoutParams)localObject1);
    paramBundle.setGAString("edu_school_beautyrank", getGAExtra());
    paramBundle.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        try
        {
          if (!TextUtils.isEmpty(SchoolFlowerOrderAgent.this.schoolOrder.getString("DetailLink")))
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(SchoolFlowerOrderAgent.this.schoolOrder.getString("DetailLink")));
            SchoolFlowerOrderAgent.this.getFragment().startActivity(paramView);
          }
          return;
        }
        catch (java.lang.Exception paramView)
        {
        }
      }
    });
    addCell("0210Basic.03SchoolFlowerOrder", paramBundle, 256);
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
 * Qualified Name:     com.dianping.shopinfo.education.agent.SchoolFlowerOrderAgent
 * JD-Core Version:    0.6.0
 */