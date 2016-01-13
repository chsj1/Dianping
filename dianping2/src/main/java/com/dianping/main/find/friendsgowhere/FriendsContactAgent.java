package com.dianping.main.find.friendsgowhere;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.app.loader.AdapterCellAgent;
import com.dianping.base.app.loader.AdapterCellAgent.BasicCellAgentAdapter;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.widget.CustomImageButton;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class FriendsContactAgent extends AdapterCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private static final String FRIENDS_CONTACT_TAG = "020FriendsContact";
  private Adapter mAdapter;
  private View mAgentView;
  private DPObject mDPObj;
  private DPObject mError;
  private MApiRequest mRequest;

  public FriendsContactAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void sendRequest()
  {
    if (this.mRequest != null)
      return;
    this.mError = null;
    this.mRequest = BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/friendship/friendshipnotice.bin").toString(), CacheType.DISABLED);
    getFragment().mapiService().exec(this.mRequest, this);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if ((this.mDPObj != null) && (!TextUtils.isEmpty(this.mDPObj.getString("Noticemsg"))))
    {
      this.mAdapter = new Adapter(null);
      addCell("020FriendsContact", this.mAdapter);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    sendRequest();
  }

  public void onDestroy()
  {
    super.onDestroy();
    if (this.mRequest != null)
    {
      getFragment().mapiService().abort(this.mRequest, this, true);
      this.mRequest = null;
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.mRequest == paramMApiRequest)
    {
      this.mDPObj = null;
      this.mRequest = null;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if ((paramMApiRequest != null) && (this.mRequest == paramMApiRequest))
    {
      this.mRequest = null;
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        this.mDPObj = ((DPObject)paramMApiResponse.result());
        dispatchAgentChanged(false);
      }
    }
  }

  private class Adapter extends AdapterCellAgent.BasicCellAgentAdapter
  {
    private Adapter()
    {
      super();
    }

    public int getCount()
    {
      if (FriendsContactAgent.this.mDPObj == null)
        return 0;
      return 1;
    }

    public Object getItem(int paramInt)
    {
      return null;
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (FriendsContactAgent.this.mAgentView == null)
        FriendsContactAgent.access$202(FriendsContactAgent.this, FriendsContactAgent.this.res.inflate(FriendsContactAgent.this.getContext(), R.layout.find_friends_go_where_contact, paramViewGroup, false));
      setViewData();
      return FriendsContactAgent.this.mAgentView;
    }

    public void setViewData()
    {
      CustomImageButton localCustomImageButton;
      if ((FriendsContactAgent.this.mAgentView != null) && (FriendsContactAgent.this.mDPObj != null))
      {
        localCustomImageButton = (CustomImageButton)FriendsContactAgent.this.mAgentView.findViewById(R.id.ignore);
        if (TextUtils.isEmpty(FriendsContactAgent.this.mDPObj.getString("Bottommsg")))
          break label165;
        ((TextView)FriendsContactAgent.this.mAgentView.findViewById(R.id.tip_text)).setText(FriendsContactAgent.this.mDPObj.getString("Bottommsg"));
        localCustomImageButton.setVisibility(8);
      }
      while ((TextUtils.isEmpty(FriendsContactAgent.this.mDPObj.getString("Noticemsg"))) || (TextUtils.isEmpty(FriendsContactAgent.this.mDPObj.getString("Buttonmsg"))) || (TextUtils.isEmpty(FriendsContactAgent.this.mDPObj.getString("Linkurl"))))
      {
        FriendsContactAgent.this.mAgentView.findViewById(R.id.main_layout).setVisibility(8);
        return;
        label165: FriendsContactAgent.this.mAgentView.findViewById(R.id.tip_layout).setVisibility(8);
        ((DPActivity)FriendsContactAgent.this.getContext()).addGAView(localCustomImageButton, -1);
      }
      ((TextView)FriendsContactAgent.this.mAgentView.findViewById(R.id.title_text)).setText(FriendsContactAgent.this.mDPObj.getString("Noticemsg"));
      ((Button)FriendsContactAgent.this.mAgentView.findViewById(R.id.check_button)).setText(FriendsContactAgent.this.mDPObj.getString("Buttonmsg"));
      View localView = FriendsContactAgent.this.mAgentView.findViewById(R.id.findfriend);
      ((DPActivity)FriendsContactAgent.this.getContext()).addGAView(localView, -1);
      localView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          FriendsContactAgent.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(FriendsContactAgent.this.mDPObj.getString("Linkurl"))));
        }
      });
      localCustomImageButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          DPActivity.preferences(FriendsContactAgent.this.getContext()).edit().putBoolean("FriendsShowContactAgent", false).apply();
          FriendsContactAgent.access$102(FriendsContactAgent.this, null);
          FriendsContactAgent.Adapter.this.notifyDataSetChanged();
        }
      });
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.friendsgowhere.FriendsContactAgent
 * JD-Core Version:    0.6.0
 */