package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AdapterCellAgent;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class ExploreTopicListAgent extends AdapterCellAgent
{
  private static final String CELL_PLAZA_EXPLORE_TOPICLIST = "100explore.100topiclist";
  private int cagetoryId;
  private TopicListItemAdapter topicAdapter;

  public ExploreTopicListAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void initAdapter()
  {
    this.topicAdapter = new TopicListItemAdapter(getFragment().getActivity());
    addCell("100explore.100topiclist", this.topicAdapter);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.cagetoryId = getFragment().getIntParam("categoryid");
    initAdapter();
  }

  public class TopicListItemAdapter extends BasicLoadAdapter
  {
    public TopicListItemAdapter(Context arg2)
    {
      super();
    }

    public void appendData(DPObject paramDPObject)
    {
      paramDPObject = paramDPObject.getArray("PlazaTopicList");
      if (paramDPObject != null)
      {
        appendDataToList(paramDPObject);
        if ((this.mData.size() == 0) && (this.mEmptyMsg == null))
          this.mEmptyMsg = "数据为空";
        if (paramDPObject.length != 0);
      }
      for (this.mIsEnd = true; ; this.mIsEnd = true)
      {
        notifyDataSetChanged();
        return;
      }
    }

    public MApiRequest createRequest(int paramInt)
    {
      return BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/plaza/getdiscoverycategoryitemtopiclist.bin").buildUpon().appendQueryParameter("categoryid", ExploreTopicListAgent.this.cagetoryId + "").build().toString(), CacheType.DISABLED);
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      DPObject localDPObject = (DPObject)getItem(paramInt);
      if ((paramView instanceof ExploreTopicListViewItem));
      for (paramDPObject = (ExploreTopicListViewItem)paramView; ; paramDPObject = (ExploreTopicListViewItem)ExploreTopicListAgent.this.res.inflate(ExploreTopicListAgent.this.getContext(), R.layout.plaza_explore_topic_listview_item, paramViewGroup, false))
      {
        paramDPObject.setCategoryId(ExploreTopicListAgent.this.cagetoryId);
        paramDPObject.setPlazaTopicInfor(localDPObject);
        paramDPObject.setOnClickListener(new View.OnClickListener(paramDPObject)
        {
          public void onClick(View paramView)
          {
            paramView = new Intent("android.intent.action.VIEW", Uri.parse(this.val$exploreTopicListViewItem.getUrl()));
            ExploreTopicListAgent.this.getContext().startActivity(paramView);
          }
        });
        paramDPObject.setGAString("tag", localDPObject.getString("Title"), paramInt);
        return paramDPObject;
      }
    }

    protected void onRequestComplete(boolean paramBoolean, MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestComplete(paramBoolean, paramMApiRequest, paramMApiResponse);
      if (paramBoolean)
      {
        this.mIsEnd = true;
        return;
      }
      this.mIsEnd = false;
      setErrorMsg("网络连接失败，点击重新加载");
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.ExploreTopicListAgent
 * JD-Core Version:    0.6.0
 */