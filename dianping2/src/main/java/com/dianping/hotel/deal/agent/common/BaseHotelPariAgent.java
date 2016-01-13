package com.dianping.hotel.deal.agent.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import com.dianping.archive.DPObject;
import com.dianping.base.tuan.agent.TuanGroupCellAgent;
import com.dianping.base.tuan.widget.DealInfoCommonCell;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.text.MessageFormat;

public class BaseHotelPariAgent extends TuanGroupCellAgent
{
  public static final int PARI_TYPE_BOOKING_NOTICE = 10;
  public static final int PARI_TYPE_FRIENDLY_REMINDER = 18;
  public static final int PARI_TYPE_MODIFICATION_NOTICE = 11;
  public static final int PARI_TYPE_PACKAGE_DETAILS = 12;
  public static final int PARI_TYPE_PURCHASING_NOTICE = 16;
  public static final int PARI_TYPE_REFUND_NOTICE = 17;
  public static final int PARI_TYPE_RULE_REMINDER = 14;
  public static final int PARI_TYPE_USAGE = 15;
  public static final int PARI_TYPE_VALIDITY = 13;
  public static final String htmlFormat = "<html><head><meta name=\\\"viewport\\\" content=\\\"width=%d, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\\\" /><style type='text/css'>{0}</style></head><body><div id='box_web_content'>{1}</div></body></html>";
  protected DealInfoCommonCell commCell;
  protected View contentView;
  protected String cssStyle;
  protected DPObject dpHotelProdDetail;
  protected View footerTextView;
  protected DPObject[] structDetailInfo;
  protected WebView webContent;

  public BaseHotelPariAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected int getPariType()
  {
    return -1;
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getContext() == null);
    do
    {
      do
        return;
      while (paramBundle == null);
      this.dpHotelProdDetail = ((DPObject)paramBundle.getParcelable("hotelproddetail"));
    }
    while (this.dpHotelProdDetail == null);
    if (this.commCell == null)
      setupView();
    this.structDetailInfo = this.dpHotelProdDetail.getArray("StructuredDetailList");
    this.cssStyle = this.dpHotelProdDetail.getString("StructuredDetailCss");
    updateView();
  }

  public void setContentHtml(String paramString)
  {
    this.webContent.loadDataWithBaseURL("", paramString, "text/html", "UTF-8", "");
  }

  public void setFooterText(View.OnClickListener paramOnClickListener)
  {
    if (this.footerTextView != null)
    {
      this.footerTextView.setVisibility(0);
      if (this.footerTextView.getParent() == null)
        this.commCell.addContent(this.footerTextView, true, paramOnClickListener);
    }
  }

  public void setHeaderText(String paramString)
  {
    this.commCell.setTitle(paramString);
    this.commCell.hideArrow();
  }

  public void setHeaderTextWithAccordions(String paramString)
  {
    setHeaderTextWithAccordions(paramString, Boolean.valueOf(false));
  }

  public void setHeaderTextWithAccordions(String paramString, Boolean paramBoolean)
  {
    ImageView localImageView = (ImageView)this.commCell.findViewById(R.id.indicator);
    if (!paramBoolean.booleanValue())
    {
      this.contentView.setVisibility(8);
      localImageView.setImageDrawable(this.res.getDrawable(R.drawable.mini_arrow_down));
    }
    while (true)
    {
      this.commCell.setTitle(paramString, new View.OnClickListener(localImageView)
      {
        public void onClick(View paramView)
        {
          if (BaseHotelPariAgent.this.contentView.getVisibility() == 0)
          {
            BaseHotelPariAgent.this.contentView.setVisibility(8);
            this.val$idView.setImageDrawable(BaseHotelPariAgent.this.res.getDrawable(R.drawable.mini_arrow_down));
            return;
          }
          BaseHotelPariAgent.this.contentView.setVisibility(0);
          this.val$idView.setImageDrawable(BaseHotelPariAgent.this.res.getDrawable(R.drawable.mini_arrow_up));
        }
      });
      return;
      localImageView.setImageDrawable(this.res.getDrawable(R.drawable.mini_arrow_up));
    }
  }

  public void setupView()
  {
    removeAllCells();
    this.contentView = this.res.inflate(getContext(), R.layout.webview_with_padding, null, false);
    this.webContent = ((WebView)this.contentView.findViewById(R.id.webview_content));
    this.webContent.setWebViewClient(new HotelHtmlWebViewClient(null));
    this.footerTextView = this.res.inflate(getContext(), R.layout.tuan_detail_more, getParentView(), false);
    this.footerTextView.setVisibility(8);
    this.commCell = new DealInfoCommonCell(getContext());
    this.commCell.addContent(this.contentView, false);
    addCell("", this.commCell);
  }

  public void updateView()
  {
    int j = getPariType();
    if (j < 0);
    Boolean localBoolean;
    do
    {
      do
        return;
      while ((this.structDetailInfo == null) || (this.structDetailInfo.length <= 0));
      localBoolean = Boolean.valueOf(false);
      int i = 0;
      while (i < this.structDetailInfo.length)
      {
        DPObject localDPObject = this.structDetailInfo[i];
        if (localDPObject.getInt("Type") == j)
        {
          setContentHtml(MessageFormat.format("<html><head><meta name=\\\"viewport\\\" content=\\\"width=%d, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\\\" /><style type='text/css'>{0}</style></head><body><div id='box_web_content'>{1}</div></body></html>", new Object[] { this.cssStyle, localDPObject.getString("Name").trim() }));
          setHeaderText(localDPObject.getString("ID").trim());
          localBoolean = Boolean.valueOf(true);
        }
        i += 1;
      }
    }
    while (localBoolean.booleanValue());
    removeAllCells();
  }

  private class HotelHtmlWebViewClient extends WebViewClient
  {
    private HotelHtmlWebViewClient()
    {
    }

    public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
    {
      if ((paramString != null) && ((Uri.parse(paramString).getScheme().equals("tel")) || (Uri.parse(paramString).getScheme().equals("http")) || (Uri.parse(paramString).getScheme().equals("https")) || (Uri.parse(paramString).getScheme().equals("dianping"))))
      {
        paramWebView = new Intent("android.intent.action.VIEW", Uri.parse(paramString));
        BaseHotelPariAgent.this.startActivity(paramWebView);
      }
      return true;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.deal.agent.common.BaseHotelPariAgent
 * JD-Core Version:    0.6.0
 */