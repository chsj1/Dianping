package com.dianping.shopinfo.base;

import android.widget.Toast;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.shopinfo.fragment.ShopInfoFragment;

public class PostAgent extends ShopCellAgent
  implements RequestHandler<MApiRequest, MApiResponse>
{
  private boolean isDestory;
  private MApiRequest request;

  public PostAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void dismissProgressDialog()
  {
    if (this.isDestory)
      return;
    this.fragment.dismissProgressDialog();
  }

  public void abortRequest()
  {
    if (this.request != null)
    {
      getFragment().mapiService().abort(this.request, this, true);
      this.request = null;
      dismissProgressDialog();
    }
  }

  public void onDestroy()
  {
    this.isDestory = true;
    if (this.request != null)
      getFragment().mapiService().abort(this.request, this, true);
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiResponse.message() != null)
    {
      paramMApiRequest = new StringBuilder();
      if (paramMApiResponse.message().title() != null)
        paramMApiRequest.append(paramMApiResponse.message().title());
      if (paramMApiResponse.message().content() != null)
      {
        if (paramMApiRequest.length() > 0)
          paramMApiRequest.append(" - ");
        paramMApiRequest.append(paramMApiResponse.message().content());
      }
      if (paramMApiRequest.length() == 0)
        paramMApiRequest.append("发生未知错误");
      if (getContext() != null)
        Toast.makeText(getContext(), paramMApiRequest.toString(), 1).show();
    }
    this.request = null;
    dismissProgressDialog();
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.request = null;
    dismissProgressDialog();
  }

  public void sendRequest(MApiRequest paramMApiRequest)
  {
    this.request = paramMApiRequest;
    if ((getFragment() != null) && (getFragment().mapiService() != null))
      getFragment().mapiService().exec(this.request, this);
  }

  public void sendRequest(MApiRequest paramMApiRequest, String paramString)
  {
    this.request = paramMApiRequest;
    getFragment().mapiService().exec(this.request, this);
    showProgressDialog(paramString);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.base.PostAgent
 * JD-Core Version:    0.6.0
 */