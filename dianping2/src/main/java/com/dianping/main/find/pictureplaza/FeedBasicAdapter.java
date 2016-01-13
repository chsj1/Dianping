package com.dianping.main.find.pictureplaza;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import com.dianping.accountservice.AccountService;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class FeedBasicAdapter extends BasicLoadAdapter
{
  private final int IS_LIKE_ACTION_TYPE_NO = 0;
  private final int IS_LIKE_ACTION_TYPE_YES = 1;
  private Context context;
  protected String curFeedId = null;
  protected int curPosition = 0;
  protected MApiRequest deleteRequest;
  protected HashMap<Integer, Integer> expandMaps = new HashMap();
  protected OnFeedItemListener feedItemListener = new OnFeedItemListener()
  {
    public void onItemDeleteListener(int paramInt1, String paramString, int paramInt2)
    {
      AccountService localAccountService = ((DPActivity)FeedBasicAdapter.this.context).accountService();
      if (localAccountService.token() == null)
      {
        localAccountService.login(new LoginResultListener(paramString, paramInt2, paramInt1)
        {
          public void onLoginCancel(AccountService paramAccountService)
          {
          }

          public void onLoginSuccess(AccountService paramAccountService)
          {
            FeedBasicAdapter.this.deleteReq(this.val$id, this.val$feedType);
            FeedBasicAdapter.this.curFeedId = this.val$id;
            FeedBasicAdapter.this.curPosition = this.val$position;
          }
        });
        return;
      }
      FeedBasicAdapter.this.deleteReq(paramString, paramInt2);
      FeedBasicAdapter.this.curFeedId = paramString;
      FeedBasicAdapter.this.curPosition = paramInt1;
    }

    public void onItemEditListener(int paramInt, String paramString)
    {
      FeedBasicAdapter.this.curPosition = paramInt;
      FeedBasicAdapter.this.curFeedId = paramString;
    }

    public void onLikeClickListener(int paramInt1, int paramInt2, String paramString, boolean paramBoolean, int paramInt3)
    {
      AccountService localAccountService = ((DPActivity)FeedBasicAdapter.this.context).accountService();
      if (localAccountService.token() == null)
      {
        localAccountService.login(new LoginResultListener(paramInt2, paramString, paramBoolean, paramInt1, paramInt3)
        {
          public void onLoginCancel(AccountService paramAccountService)
          {
          }

          public void onLoginSuccess(AccountService paramAccountService)
          {
            FeedBasicAdapter.this.updateLikeStatus(this.val$position, this.val$id, this.val$isLike);
            FeedBasicAdapter.this.sendLikeReq(this.val$userId, this.val$id, this.val$isLike, this.val$feedType);
          }
        });
        return;
      }
      FeedBasicAdapter.this.updateLikeStatus(paramInt2, paramString, paramBoolean);
      FeedBasicAdapter.this.sendLikeReq(paramInt1, paramString, paramBoolean, paramInt3);
    }
  };
  protected HashMap<Integer, Integer> imageIndexMaps = new HashMap();
  protected MApiRequest mReqLike;

  public FeedBasicAdapter(Context paramContext)
  {
    super(paramContext);
    this.context = paramContext;
  }

  private MApiService getMapiService()
  {
    return (MApiService)((DPActivity)this.context).getService("mapi");
  }

  protected void deleteReq(String paramString, int paramInt)
  {
    if (this.deleteRequest != null)
      return;
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/review/deleteugcfeed.bin").buildUpon();
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("mainid");
    localArrayList.add(paramString);
    localArrayList.add("feedtype");
    localArrayList.add(String.valueOf(String.valueOf(paramInt)));
    this.deleteRequest = BasicMApiRequest.mapiPost(localBuilder.build().toString(), (String[])localArrayList.toArray(new String[localArrayList.size()]));
    getMapiService().exec(this.deleteRequest, this);
    showFeedProgressDialog("正在删除...");
  }

  protected void dismissFeedProgressDialog()
  {
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.deleteRequest == paramMApiRequest)
    {
      dismissFeedProgressDialog();
      this.deleteRequest = null;
      return;
    }
    if (this.mReqLike == paramMApiRequest)
    {
      this.mReqLike = null;
      return;
    }
    if ((paramMApiResponse.message() == null) || (paramMApiResponse.message().content() == null));
    for (paramMApiRequest = "请求失败，请稍后再试"; ; paramMApiRequest = paramMApiResponse.message().content())
    {
      setErrorMsg(paramMApiRequest);
      this.mReq = null;
      return;
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.deleteRequest == paramMApiRequest)
    {
      updateDeleteStatus();
      dismissFeedProgressDialog();
      this.deleteRequest = null;
      return;
    }
    if (this.mReqLike == paramMApiRequest)
    {
      this.mReqLike = null;
      return;
    }
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      appendData((DPObject)paramMApiResponse.result());
      this.mReq = null;
      return;
    }
    if (paramMApiResponse.message() == null);
    for (paramMApiRequest = "请求失败，请稍后再试"; ; paramMApiRequest = paramMApiResponse.message().content())
    {
      setErrorMsg(paramMApiRequest);
      break;
    }
  }

  public void reset()
  {
    super.reset();
    this.expandMaps = new HashMap();
    this.imageIndexMaps = new HashMap();
    this.curFeedId = null;
    this.curPosition = 0;
  }

  protected void sendLikeReq(int paramInt1, String paramString, boolean paramBoolean, int paramInt2)
  {
    int i = 1;
    if (this.mReqLike != null)
    {
      getMapiService().abort(this.mReqLike, this, true);
      return;
    }
    if (paramBoolean)
      i = 0;
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/review/ugcfavor.bin").buildUpon();
    ArrayList localArrayList = new ArrayList();
    localArrayList.add("mainid");
    localArrayList.add(paramString);
    localArrayList.add("actiontype");
    localArrayList.add(String.valueOf(i));
    localArrayList.add("feedtype");
    localArrayList.add(String.valueOf(paramInt2));
    localArrayList.add("originuserid");
    localArrayList.add(String.valueOf(paramInt1));
    this.mReqLike = BasicMApiRequest.mapiPost(localBuilder.build().toString(), (String[])localArrayList.toArray(new String[localArrayList.size()]));
    getMapiService().exec(this.mReqLike, this);
  }

  protected void showFeedProgressDialog(String paramString)
  {
  }

  protected void updateDeleteStatus()
  {
    if (this.curFeedId == null);
    DPObject localDPObject;
    do
    {
      do
        return;
      while (!(getItem(this.curPosition) instanceof DPObject));
      localDPObject = (DPObject)getItem(this.curPosition);
    }
    while (!localDPObject.getString("MainId").equals(this.curFeedId));
    this.mData.remove(localDPObject);
    int i = this.curPosition + 1;
    while (i <= this.mData.size())
    {
      if (this.expandMaps.containsKey(Integer.valueOf(i)))
      {
        this.expandMaps.put(Integer.valueOf(i - 1), this.expandMaps.get(Integer.valueOf(i)));
        this.expandMaps.remove(Integer.valueOf(i));
      }
      i += 1;
    }
    i = this.curPosition + 1;
    while (i <= this.mData.size())
    {
      if (this.imageIndexMaps.containsKey(Integer.valueOf(i)))
      {
        this.imageIndexMaps.put(Integer.valueOf(i - 1), this.imageIndexMaps.get(Integer.valueOf(i)));
        this.imageIndexMaps.remove(Integer.valueOf(i));
      }
      i += 1;
    }
    notifyDataSetChanged();
    this.curFeedId = null;
    this.curPosition = 0;
  }

  public void updateLikeStatus(int paramInt, String paramString, boolean paramBoolean)
  {
    Object localObject;
    if ((getItem(paramInt) instanceof DPObject))
    {
      localObject = (DPObject)getItem(paramInt);
      if (((DPObject)localObject).getString("MainId").equals(paramString));
    }
    else
    {
      return;
    }
    int i = ((DPObject)localObject).getInt("LikeCount");
    if (paramBoolean)
    {
      i -= 1;
      paramString = this.mData;
      localObject = ((DPObject)localObject).edit();
      if (paramBoolean)
        break label118;
    }
    label118: for (paramBoolean = true; ; paramBoolean = false)
    {
      paramString.set(paramInt, ((DPObject.Editor)localObject).putBoolean("IsLike", paramBoolean).putInt("LikeCount", i).generate());
      notifyDataSetChanged();
      return;
      i += 1;
      break;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.pictureplaza.FeedBasicAdapter
 * JD-Core Version:    0.6.0
 */