package com.dianping.main.user;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.dianping.app.DPActivity;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class ActionMoreDialog extends Dialog
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  protected View contentView;
  private boolean isFans;
  private boolean isInBlacklist;
  private MApiRequest mAddBlacklist;
  protected Context mContext;
  private MApiRequest mRemoveBlacklist;
  private MApiRequest mRemoveFansReq;
  private DPObject mUserObject;
  private int memberId;

  public ActionMoreDialog(Context paramContext, DPObject paramDPObject)
  {
    super(paramContext, 16973840);
    if (paramDPObject == null)
    {
      dismiss();
      return;
    }
    this.mContext = paramContext;
    this.mUserObject = paramDPObject;
    this.contentView = getLayoutInflater().inflate(R.layout.dialog_user_action_more, null, false);
    this.contentView.findViewById(R.id.filler_top).setOnClickListener(this);
    this.contentView.findViewById(R.id.tv_remove_fans).setOnClickListener(this);
    this.contentView.findViewById(R.id.tv_add_blacklist).setOnClickListener(this);
    this.contentView.findViewById(R.id.tv_dismiss).setOnClickListener(this);
    setContentView(this.contentView);
    setCanceledOnTouchOutside(true);
    this.isFans = paramDPObject.getBoolean("IsFans");
    if (!this.isFans)
      this.contentView.findViewById(R.id.tv_remove_fans).setVisibility(8);
    this.isInBlacklist = paramDPObject.getBoolean("IsBlack");
    if (this.isInBlacklist)
      ((TextView)(TextView)this.contentView.findViewById(R.id.tv_add_blacklist)).setText("解除黑名单");
    this.memberId = paramDPObject.getInt("UserID");
  }

  private MApiService mapiService()
  {
    if ((this.mContext instanceof DPActivity))
      return ((DPActivity)this.mContext).mapiService();
    return DPApplication.instance().mapiService();
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if ((i == R.id.filler_top) || (i == R.id.tv_dismiss))
      dismiss();
    do
    {
      return;
      if (i != R.id.tv_remove_fans)
        continue;
      if (this.mRemoveFansReq != null)
        mapiService().abort(this.mRemoveFansReq, this, true);
      this.mRemoveFansReq = BasicMApiRequest.mapiPost("http://m.api.dianping.com/friendship/removefan.bin", new String[] { "memberid", String.valueOf(this.memberId) });
      mapiService().exec(this.mRemoveFansReq, this);
      return;
    }
    while (i != R.id.tv_add_blacklist);
    if (this.isInBlacklist)
    {
      if (this.mRemoveBlacklist != null)
        mapiService().abort(this.mRemoveBlacklist, this, true);
      this.mRemoveBlacklist = BasicMApiRequest.mapiPost("http://m.api.dianping.com/friendship/removeblacklist.bin", new String[] { "memberid", String.valueOf(this.memberId) });
      mapiService().exec(this.mRemoveBlacklist, this);
      return;
    }
    if (this.mAddBlacklist != null)
      mapiService().abort(this.mAddBlacklist, this, true);
    this.mAddBlacklist = BasicMApiRequest.mapiPost("http://m.api.dianping.com/friendship/addblacklist.bin", new String[] { "memberid", String.valueOf(this.memberId) });
    mapiService().exec(this.mAddBlacklist, this);
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mRemoveFansReq)
    {
      this.mRemoveFansReq = null;
      if ((paramMApiResponse.message() != null) && (paramMApiResponse.message().content() != null))
        break label82;
    }
    label82: for (paramMApiRequest = "请求失败，请稍后再试"; ; paramMApiRequest = paramMApiResponse.message().content())
    {
      Toast.makeText(this.mContext, paramMApiRequest, 0).show();
      return;
      if (paramMApiRequest == this.mAddBlacklist)
      {
        this.mAddBlacklist = null;
        break;
      }
      if (paramMApiRequest != this.mRemoveBlacklist)
        break;
      this.mRemoveBlacklist = null;
      break;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest == this.mRemoveFansReq)
    {
      this.mUserObject.edit().putBoolean("IsFans", false);
      this.mRemoveFansReq = null;
    }
    do
    {
      return;
      if (paramMApiRequest != this.mAddBlacklist)
        continue;
      this.mUserObject.edit().putBoolean("IsBlack", true);
      this.mAddBlacklist = null;
      return;
    }
    while (paramMApiRequest != this.mRemoveBlacklist);
    this.mUserObject.edit().putBoolean("IsBlack", false);
    this.mRemoveBlacklist = null;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.user.ActionMoreDialog
 * JD-Core Version:    0.6.0
 */