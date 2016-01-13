package com.dianping.base.tuan.agent;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.GroupAgentFragment;
import com.dianping.base.tuan.widget.DealInfoCommonCell;
import com.dianping.base.widget.CssStyleManager;
import com.dianping.base.widget.TableView;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaRelativeLayout;

public class DealInfoStructExtraAgent extends TuanGroupCellAgent
{
  protected int dealId;
  protected DPObject dpDeal;
  private View.OnClickListener mListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://dealdetailmore"));
      paramView.putExtra("mDeal", DealInfoStructExtraAgent.this.dpDeal);
      DealInfoStructExtraAgent.this.getContext().startActivity(paramView);
    }
  };

  public DealInfoStructExtraAgent(Object paramObject)
  {
    super(paramObject);
  }

  @TargetApi(11)
  protected void createDealDetail(TableView paramTableView, DPObject paramDPObject)
  {
    View localView = this.res.inflate(getContext(), R.layout.webview_with_padding, null, false);
    WebView localWebView = (WebView)localView.findViewById(R.id.webview_content);
    paramTableView.addView(localView);
    localWebView.loadDataWithBaseURL(CssStyleManager.instance(getContext()).getCssFilePath(), CssStyleManager.instance(getContext()).makeHtml(paramDPObject.getString("Name").trim(), false), "text/html", "UTF-8", null);
  }

  protected DPObject[] getStructDetails(Bundle paramBundle)
  {
    if ((this.dpDeal == null) && (paramBundle != null))
    {
      paramBundle = (DPObject)paramBundle.getParcelable("coupon");
      if (paramBundle != null)
        this.dpDeal = paramBundle.getObject("RelativeDeal");
    }
    if (this.dpDeal == null)
      return null;
    return this.dpDeal.getArray("StructedDetails");
  }

  protected boolean isReady()
  {
    return true;
  }

  @TargetApi(11)
  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    int j = 0;
    int i;
    DPObject localDPObject;
    if (paramBundle != null)
    {
      if (paramBundle.getInt("status") != 2)
        break label79;
      i = 1;
      this.dealId = paramBundle.getInt("dealid");
      localDPObject = (DPObject)paramBundle.getParcelable("deal");
      j = i;
      if (this.dpDeal != localDPObject)
      {
        this.dpDeal = localDPObject;
        onDealRetrived(localDPObject);
        j = i;
      }
    }
    if (getContext() == null);
    label79: label114: 
    do
    {
      do
      {
        return;
        i = 0;
        break;
        if (!isReady())
          break label718;
        removeAllCells();
        paramBundle = getStructDetails(paramBundle);
        if ((paramBundle == null) || (paramBundle.length <= 0))
          continue;
        j = 0;
        TableView localTableView;
        Object localObject;
        int k;
        if (j < paramBundle.length)
        {
          localDPObject = paramBundle[j];
          i = localDPObject.getInt("Type");
          if (i < 100)
          {
            localTableView = createCellContainer();
            localTableView.setBackgroundColor(this.res.getColor(R.color.white));
            if (i != 1)
              break label457;
            createDealDetail(localTableView, localDPObject);
            localObject = new DealInfoCommonCell(getContext());
            ((DealInfoCommonCell)localObject).setTitleSize(0, getResources().getDimension(R.dimen.deal_info_agent_title_text_size));
            ((DealInfoCommonCell)localObject).setArrowPreSize(0, getResources().getDimension(R.dimen.deal_info_agent_subtitle_text_size));
            ((DealInfoCommonCell)localObject).setPaddingLeft((int)getResources().getDimension(R.dimen.deal_info_padding_left));
            ((DealInfoCommonCell)localObject).setPaddingRight((int)getResources().getDimension(R.dimen.deal_info_padding_right));
            if (i != 1)
              break label575;
            int m = 0;
            if (this.dpDeal.getInt("DealType") != 8)
              break label546;
            k = 0;
            i = m;
            if (k < paramBundle.length)
            {
              if (paramBundle[k].getInt("Type") != 1000)
                break label537;
              i = 1;
            }
            ((DealInfoCommonCell)localObject).titleLay.setGAString("moredetail");
            ((NovaActivity)getContext()).addGAView(((DealInfoCommonCell)localObject).titleLay, -1, "tuandeal", "tuandeal".equals(((NovaActivity)getContext()).getPageName()));
            ((DealInfoCommonCell)localObject).setIcon(R.drawable.icon_detail);
            if (i == 0)
              break label552;
            ((DealInfoCommonCell)localObject).setArrowPre("更多图文详情");
            ((DealInfoCommonCell)localObject).setTitle(localDPObject.getString("ID").trim(), this.mListener);
            ((DealInfoCommonCell)localObject).addContent(localTableView, false);
            if (!(this.fragment instanceof GroupAgentFragment))
              break label612;
            addCell("040StructExtra" + j + "." + j + "Content", (View)localObject);
          }
        }
        while (true)
        {
          j += 1;
          break label114;
          break;
          localObject = this.res.inflate(getContext(), R.layout.webview_with_padding, null, false);
          WebView localWebView = (WebView)((View)localObject).findViewById(R.id.webview_content);
          localTableView.addView((View)localObject);
          localWebView.loadDataWithBaseURL(CssStyleManager.instance(getContext()).getCssFilePath(), CssStyleManager.instance(getContext()).makeHtml(localDPObject.getString("Name").trim(), false), "text/html", "UTF-8", null);
          break label172;
          k += 1;
          break label275;
          i = 1;
          break label304;
          ((DealInfoCommonCell)localObject).hideArrow();
          ((DealInfoCommonCell)localObject).setTitle(localDPObject.getString("ID").trim());
          break label387;
          if (i == 2)
            ((DealInfoCommonCell)localObject).setIcon(R.drawable.icon_notice);
          ((DealInfoCommonCell)localObject).hideArrow();
          ((DealInfoCommonCell)localObject).setTitle(localDPObject.getString("ID").trim());
          break label387;
          addCell("040StructExtra" + j + "." + j + "Content", (View)localObject);
          addEmptyCell("040StructExtra" + j + "." + j + "Empty");
        }
      }
      while (j == 0);
      addCell("040StructExtra.01LOAD", createLoadingCell());
      return;
      removeAllCells();
    }
    while (j == 0);
    label172: label304: label457: label612: addCell("040StructExtra.01LOAD", createLoadingCell());
    label275: label537: label546: label552: label575: label718: return;
  }

  protected void onDealRetrived(DPObject paramDPObject)
  {
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.tuan.agent.DealInfoStructExtraAgent
 * JD-Core Version:    0.6.0
 */