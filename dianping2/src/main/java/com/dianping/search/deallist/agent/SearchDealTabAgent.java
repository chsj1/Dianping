package com.dianping.search.deallist.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.shoplist.activity.AbstractTabListActivity;
import com.dianping.base.tuan.utils.TuanSharedDataKey;
import com.dianping.base.widget.CustomImageButton;
import com.dianping.loader.MyResources;
import com.dianping.search.deallist.fragment.TuanDealListAgentFragment;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaImageView;
import com.dianping.widget.view.NovaTextView;

public class SearchDealTabAgent extends SearchDealAgent
  implements View.OnClickListener
{
  NovaImageView mClearKeywordBtn;
  View mKeywordLayout;
  NovaTextView mKeywordView;
  CustomImageButton mLeftButton;

  public SearchDealTabAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void setTabTitle(boolean paramBoolean)
  {
    if (((getFragment() instanceof TuanDealListAgentFragment)) && ((getFragment().getActivity() instanceof AbstractTabListActivity)))
      ((AbstractTabListActivity)getFragment().getActivity()).showTabTitle(paramBoolean);
  }

  protected String getGaPageName()
  {
    return "shoptglist";
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    if ("deal_list_data_analized".equals(paramAgentMessage.what))
    {
      this.keyword = getSharedString(TuanSharedDataKey.KEYWORD_KEY);
      this.mKeywordView.setText(this.keyword);
    }
    if ("deal_list_search_call_up_search_fragment_commend".equals(paramAgentMessage.what))
      callUpTuanSearchFragment("");
  }

  protected View inflateRootView()
  {
    return this.res.inflate(getContext(), R.layout.search_tuan_keyword_title_bar, getParentView(), false);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    if (this.rootView == null)
      setupView();
    updateView();
    this.keyword = getSharedString(TuanSharedDataKey.KEYWORD_KEY);
    this.channel = getSharedString(TuanSharedDataKey.CHANNEL_KEY);
    this.mKeywordView.setText(this.keyword);
  }

  public void onClick(View paramView)
  {
    if (paramView.getId() == R.id.keyword)
    {
      paramView = this.mKeywordView.getText();
      if (TextUtils.isEmpty(paramView))
      {
        paramView = "";
        callUpTuanSearchFragment(paramView);
      }
    }
    do
    {
      return;
      paramView = paramView.toString();
      break;
    }
    while (paramView.getId() != R.id.search_bar_clear);
    callUpTuanSearchFragment("");
  }

  protected void setupView()
  {
    this.rootView = inflateRootView();
    this.mKeywordLayout = this.rootView.findViewById(R.id.search_bar);
    this.mKeywordView = ((NovaTextView)this.rootView.findViewById(R.id.keyword));
    this.mKeywordView.setOnClickListener(this);
    this.mKeywordView.setGAString("search_box_tuan");
    this.mClearKeywordBtn = ((NovaImageView)this.rootView.findViewById(R.id.search_bar_clear));
    this.mClearKeywordBtn.setOnClickListener(this);
    this.mClearKeywordBtn.setGAString("search_box_tuan");
    this.mLeftButton = ((CustomImageButton)this.rootView.findViewById(R.id.left_btn));
    this.mLeftButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        if ((SearchDealTabAgent.this.getFragment() != null) && (SearchDealTabAgent.this.getFragment().getActivity() != null))
          SearchDealTabAgent.this.getFragment().getActivity().onBackPressed();
      }
    });
    removeCell("10SearchDeal");
    addCell("10SearchDeal", this.rootView);
  }

  public void startSearch(DPObject paramDPObject)
  {
    setSharedObject(TuanSharedDataKey.CURRENT_SCREENING_KEY, "");
    setSharedObject(TuanSharedDataKey.NOT_UPDATE_SCREENING_DATA, Boolean.valueOf(false));
    Object localObject = paramDPObject.getString("Url");
    String str = paramDPObject.getString("Keyword");
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      startActivity((String)localObject);
      return;
    }
    if (getSharedBoolean(TuanSharedDataKey.DEAL_LIST_IS_SEARCH_MODE))
    {
      this.mKeywordView.setText(str);
      setSharedObject(TuanSharedDataKey.KEYWORD_KEY, str);
      dispatchMessage(new AgentMessage("deal_list_keyword_changed"));
      return;
    }
    localObject = Uri.parse("dianping://shoplist").buildUpon();
    ((Uri.Builder)localObject).appendQueryParameter("tab", String.valueOf(1));
    str = paramDPObject.getString("Keyword");
    if (!TextUtils.isEmpty(str))
      ((Uri.Builder)localObject).appendQueryParameter("keyword", str);
    localObject = new Intent("android.intent.action.VIEW", Uri.parse(((Uri.Builder)localObject).toString()));
    paramDPObject = paramDPObject.getString("Value");
    if (!TextUtils.isEmpty(paramDPObject))
      ((Intent)localObject).putExtra("value", paramDPObject);
    ((Intent)localObject).putExtra("fromwhere", getSharedString(TuanSharedDataKey.FROM_WHERE_KEY));
    startActivity((Intent)localObject);
  }

  protected void updateView()
  {
    if (getSharedBoolean(TuanSharedDataKey.DEAL_LIST_IS_SEARCH_MODE))
    {
      this.rootView.setVisibility(0);
      setTabTitle(false);
      return;
    }
    this.rootView.setVisibility(8);
    setTabTitle(true);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.agent.SearchDealTabAgent
 * JD-Core Version:    0.6.0
 */