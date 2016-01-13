package com.dianping.search.deallist.agent;

import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.basic.AbstractSearchFragment.OnSearchFragmentListener;
import com.dianping.base.tuan.agent.TuanCellAgent;
import com.dianping.base.tuan.fragment.TuanSearchFragment;
import com.dianping.base.tuan.utils.TuanSharedDataKey;
import com.dianping.loader.MyResources;
import com.dianping.locationservice.impl286.util.CommonUtil;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class SearchDealAgent extends TuanCellAgent
  implements AbstractSearchFragment.OnSearchFragmentListener
{
  public static final String SEARCH_DEAL = "10SearchDeal";
  static int i;
  protected View backBtn;
  protected String channel;
  protected View clearBtn;
  protected String keyword;
  protected View rootView;
  protected EditText searchTextView;

  public SearchDealAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected void callUpTuanSearchFragment(String paramString)
  {
    TuanSearchFragment localTuanSearchFragment = TuanSearchFragment.createAndAdd(getFragment().getActivity(), null, this.channel, true, getGaPageName());
    localTuanSearchFragment.setKeyword(paramString);
    localTuanSearchFragment.setOnSearchFragmentListener(this);
  }

  protected String getGaPageName()
  {
    return "deallist";
  }

  public ArrayList<NameValuePair> getStaticEventsExtra()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BasicNameValuePair("from", getSharedString(TuanSharedDataKey.FROM_WHERE_KEY)));
    return localArrayList;
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    if ("deal_list_data_analized".equals(paramAgentMessage.what))
    {
      this.keyword = getSharedString(TuanSharedDataKey.KEYWORD_KEY);
      this.searchTextView.setText(this.keyword);
    }
  }

  protected View inflateRootView()
  {
    return this.res.inflate(getContext(), R.layout.search_deal_agent, null, false);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.rootView == null)
      setupView();
    updateView();
    this.keyword = getSharedString(TuanSharedDataKey.KEYWORD_KEY);
    this.channel = getSharedString(TuanSharedDataKey.CHANNEL_KEY);
    this.searchTextView.setText(this.keyword);
  }

  public void onSearchFragmentDetach()
  {
  }

  protected void setupView()
  {
    this.rootView = inflateRootView();
    this.searchTextView = ((EditText)this.rootView.findViewById(R.id.search_edit));
    this.backBtn = this.rootView.findViewById(R.id.back);
    this.clearBtn = this.rootView.findViewById(R.id.clearBtn);
    ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-1, CommonUtil.dip2px(getContext(), 50.0F));
    this.rootView.setLayoutParams(localLayoutParams);
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
      this.searchTextView.setText(str);
      setSharedObject(TuanSharedDataKey.KEYWORD_KEY, str);
      dispatchMessage(new AgentMessage("deal_list_keyword_changed"));
      return;
    }
    localObject = Uri.parse("dianping://deallist").buildUpon();
    str = paramDPObject.getString("Keyword");
    if (!TextUtils.isEmpty(str))
      ((Uri.Builder)localObject).appendQueryParameter("keyword", str);
    localObject = new Intent("android.intent.action.VIEW", Uri.parse(((Uri.Builder)localObject).toString()));
    paramDPObject = paramDPObject.getString("Value");
    if (!TextUtils.isEmpty(paramDPObject))
      ((Intent)localObject).putExtra("value", paramDPObject);
    startActivity((Intent)localObject);
  }

  protected void updateView()
  {
    this.searchTextView.setFocusable(false);
    this.searchTextView.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        paramView = SearchDealAgent.this.searchTextView.getText();
        if (TextUtils.isEmpty(paramView));
        for (paramView = ""; ; paramView = paramView.toString())
        {
          SearchDealAgent.this.callUpTuanSearchFragment(paramView);
          return;
        }
      }
    });
    this.backBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        SearchDealAgent.this.getFragment().getActivity().onBackPressed();
      }
    });
    this.clearBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        SearchDealAgent.this.searchTextView.getText();
        SearchDealAgent.this.callUpTuanSearchFragment("");
      }
    });
    removeAllCells();
    addCell("10SearchDeal", this.rootView);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.deallist.agent.SearchDealAgent
 * JD-Core Version:    0.6.0
 */