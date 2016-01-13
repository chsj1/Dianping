package com.dianping.shopinfo.fun;

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

public class ScenicIntroAgent extends ShopCellAgent
  implements RequestHandler
{
  private static final String CELL_SCENIC_INTRO = "0200Basic.OOIntro";
  private MApiRequest introReq;
  private boolean isShow = false;
  private DPObject scenicBrief;
  private TicketCell ticketCell = null;

  public ScenicIntroAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendRequset()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/fun/getscenicbrief.fn").buildUpon();
    localBuilder.appendQueryParameter("shopid", shopId() + "");
    this.introReq = mapiGet(this, localBuilder.toString(), CacheType.NORMAL);
    getFragment().mapiService().exec(this.introReq, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    removeAllCells();
    if ((!this.isShow) || (this.scenicBrief == null) || (TextUtils.isEmpty(this.scenicBrief.getString("BriefIntro"))))
      return;
    if (this.ticketCell == null)
      this.ticketCell = createTicketCell();
    this.ticketCell.setTitle(this.scenicBrief.getString("BriefIntro"));
    this.ticketCell.setIcon(this.res.getDrawable(R.drawable.scenic_intro_icon));
    this.ticketCell.setTitleLineSpacing(6.4F);
    this.ticketCell.setTitleMaxLines(2);
    this.ticketCell.setGAString("sceneryintro", getGAExtra());
    paramBundle = new LinearLayout.LayoutParams(-1, -2);
    paramBundle.topMargin = ViewUtils.dip2px(getContext(), 4.3F);
    this.ticketCell.getTitleView().setLayoutParams(paramBundle);
    this.ticketCell.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        try
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse(ScenicIntroAgent.this.scenicBrief.getString("ScenicDetailURL")));
          ScenicIntroAgent.this.startActivity(paramView);
          return;
        }
        catch (java.lang.Exception paramView)
        {
        }
      }
    });
    addCell("0200Basic.OOIntro", this.ticketCell, 256);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequset();
  }

  public void onRequestFailed(Request paramRequest, Response paramResponse)
  {
    if (this.introReq == paramRequest)
      this.introReq = null;
  }

  public void onRequestFinish(Request paramRequest, Response paramResponse)
  {
    if (paramRequest == this.introReq)
    {
      this.introReq = null;
      this.scenicBrief = ((DPObject)paramResponse.result());
      if (this.scenicBrief != null)
        this.isShow = this.scenicBrief.getBoolean("Showable");
      dispatchAgentChanged(false);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.fun.ScenicIntroAgent
 * JD-Core Version:    0.6.0
 */