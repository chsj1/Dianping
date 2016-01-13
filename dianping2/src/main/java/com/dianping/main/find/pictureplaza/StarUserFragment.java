package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NovaFragment;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.color;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaListView;

public class StarUserFragment extends NovaFragment
{
  private NovaListView mListView;
  int mTopicId = 1;
  int mType = 1;

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.mListView.setAdapter(new Adapter(getActivity()));
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.mListView = new NovaListView(getActivity());
    this.mListView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    this.mListView.setDivider(null);
    this.mListView.setBackgroundColor(getResources().getColor(R.color.white));
    paramLayoutInflater = new View(getActivity());
    paramLayoutInflater.setLayoutParams(new ViewGroup.LayoutParams(-1, 1));
    paramLayoutInflater.setBackgroundResource(R.color.line_gray);
    paramViewGroup = new FrameLayout(getActivity());
    paramViewGroup.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    paramViewGroup.addView(this.mListView);
    paramViewGroup.addView(paramLayoutInflater);
    return paramViewGroup;
  }

  public void setData(int paramInt1, int paramInt2)
  {
    this.mType = paramInt1;
    this.mTopicId = paramInt2;
  }

  class Adapter extends BasicLoadAdapter
  {
    private Context context;

    public Adapter(Context arg2)
    {
      super();
      this.context = localContext;
    }

    public MApiRequest createRequest(int paramInt)
    {
      return BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/plaza/getplazastaruser.bin").buildUpon().appendQueryParameter("topicid", String.valueOf(StarUserFragment.this.mTopicId)).appendQueryParameter("type", String.valueOf(StarUserFragment.this.mType)).build().toString(), CacheType.NORMAL);
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if ((paramView instanceof PlazaStarUserItem));
      for (paramView = (PlazaStarUserItem)paramView; ; paramView = null)
      {
        Object localObject = paramView;
        if (paramView == null)
          localObject = (PlazaStarUserItem)LayoutInflater.from(this.context).inflate(R.layout.find_plaza_staruser_item, paramViewGroup, false);
        ((PlazaStarUserItem)localObject).setStarUserData(paramDPObject, paramInt);
        int i = 0;
        paramDPObject = paramDPObject.getObject("User");
        if (paramDPObject != null)
          i = paramDPObject.getInt("UserID");
        ((PlazaStarUserItem)localObject).getGAUserInfo().biz_id = String.valueOf(i);
        ((PlazaStarUserItem)localObject).setGAString("prouser_profile", null, paramInt);
        ((DPActivity)StarUserFragment.this.getActivity()).addGAView((View)localObject, paramInt);
        return localObject;
      }
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        localObject = ((DPObject)paramMApiResponse.result()).getArray("List");
        if (localObject != null)
          appendData(localObject);
        while (true)
        {
          this.mReq = null;
          onRequestComplete(true, paramMApiRequest, paramMApiResponse);
          return;
          setEmptyMsg("请求失败，请稍后再试");
        }
      }
      if (paramMApiResponse.message() == null);
      for (Object localObject = "请求失败，请稍后再试"; ; localObject = paramMApiResponse.message().content())
      {
        setErrorMsg((String)localObject);
        break;
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.StarUserFragment
 * JD-Core Version:    0.6.0
 */