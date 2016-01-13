package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.app.loader.AdapterCellAgent;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.layout;

public class PlazaHomeFansAgent extends AdapterCellAgent
{
  private static final String CELL_MY_HONEY = "0100honey.01honey";

  public PlazaHomeFansAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void initView()
  {
    addCell("0100honey.01honey", new HoneyAdapter(getFragment().getActivity(), null));
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (accountService().token() == null)
    {
      accountService().login(new LoginResultListener()
      {
        public void onLoginCancel(AccountService paramAccountService)
        {
        }

        public void onLoginSuccess(AccountService paramAccountService)
        {
          PlazaHomeFansAgent.this.initView();
        }
      });
      return;
    }
    initView();
  }

  private class HoneyAdapter extends BasicLoadAdapter
  {
    private Context mContext;

    private HoneyAdapter(Context arg2)
    {
      super();
      this.mContext = localContext;
    }

    public MApiRequest createRequest(int paramInt)
    {
      Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/plaza/getuserfollowtopics.bin").buildUpon();
      localBuilder.appendQueryParameter("start", paramInt + "");
      return BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
    }

    protected TextView getEmptyView(String paramString1, String paramString2, ViewGroup paramViewGroup, View paramView)
    {
      if (paramView == null)
        paramString1 = null;
      while (true)
      {
        paramString2 = paramString1;
        if (paramString1 == null)
        {
          paramString2 = (TextView)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.simple_list_item_18, paramViewGroup, false);
          paramString2.setTag(EMPTY);
          paramString1 = paramViewGroup.getResources().getDrawable(R.drawable.empty_page_nothing);
          paramString1.setBounds(0, 0, paramString1.getIntrinsicWidth(), paramString1.getIntrinsicHeight());
          paramString2.setCompoundDrawablePadding(8);
          paramString2.setCompoundDrawables(paramString1, null, null, null);
          paramString2.setMovementMethod(LinkMovementMethod.getInstance());
        }
        paramString2.setText(Html.fromHtml("您还没有关注的话题"));
        return paramString2;
        if (paramView.getTag() == EMPTY)
        {
          paramString1 = (TextView)paramView;
          continue;
        }
        paramString1 = null;
      }
    }

    public int getItemViewType(int paramInt)
    {
      Object localObject = getItem(paramInt);
      if (localObject == ERROR)
        return 0;
      if (localObject == LOADING)
        return 1;
      if (localObject == EMPTY)
        return 2;
      return 3;
    }

    public int getViewTypeCount()
    {
      return 8;
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject = getItem(paramInt);
      if ((paramView instanceof PlazaTopicFansItem));
      for (paramDPObject = (PlazaTopicFansItem)paramView; ; paramDPObject = null)
      {
        paramView = paramDPObject;
        if (paramDPObject == null)
          paramView = (PlazaTopicFansItem)LayoutInflater.from(this.mContext).inflate(R.layout.plaza_topic_fan_layout, paramViewGroup, false);
        if (localObject != null)
        {
          paramView.setTopicInfo((DPObject)localObject);
          paramView.setGAString("topic");
          paramView.gaUserInfo.biz_id = String.valueOf(((DPObject)localObject).getInt("TopicId"));
          ((NovaActivity)PlazaHomeFansAgent.this.getContext()).addGAView(paramView, paramInt);
        }
        return paramView;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.PlazaHomeFansAgent
 * JD-Core Version:    0.6.0
 */