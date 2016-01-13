package com.dianping.takeaway.entity;

import android.net.Uri;
import android.net.Uri.Builder;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NovaFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.widget.view.GAUserInfo;
import java.util.ArrayList;
import java.util.Arrays;

public class TakeawayCommentsDataSource
{
  private MApiRequest commentRequest;
  public ArrayList<DPObject> commentsList = new ArrayList();
  public int curCommentsType = -1;
  private DataLoadListener dataLoadListener;
  public String dpReviewCount;
  private NovaFragment fragment;
  public boolean hasLikedChoice = false;
  public boolean isEnd = false;
  public boolean isLiked = false;
  private RequestHandler<MApiRequest, MApiResponse> mapiHandler = new RequestHandler()
  {
    public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == TakeawayCommentsDataSource.this.commentRequest)
      {
        TakeawayCommentsDataSource.access$002(TakeawayCommentsDataSource.this, null);
        if (TakeawayCommentsDataSource.this.dataLoadListener != null)
          TakeawayCommentsDataSource.this.dataLoadListener.loadCommentsListFinsh(TakeawayNetLoadStatus.STATUS_FAILED, TakeawayCommentsDataSource.TakeawayCommentsPageStatus.LOAD_ERROR);
      }
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      if (paramMApiRequest == TakeawayCommentsDataSource.this.commentRequest)
      {
        if ((paramMApiResponse != null) && ((paramMApiResponse.result() instanceof DPObject)))
        {
          DPObject localDPObject = (DPObject)paramMApiResponse.result();
          if (localDPObject != null)
          {
            paramMApiRequest = localDPObject.getArray("List");
            if ((paramMApiRequest != null) && (paramMApiRequest.length > 0))
              TakeawayCommentsDataSource.this.commentsList.addAll(Arrays.asList(paramMApiRequest));
            TakeawayCommentsDataSource.this.isEnd = localDPObject.getBoolean("IsEnd");
            TakeawayCommentsDataSource.this.nextStartPage = localDPObject.getInt("NextStartPage");
            paramMApiResponse = TakeawayCommentsDataSource.TakeawayCommentsPageStatus.NORMAL;
            paramMApiRequest = paramMApiResponse;
            if (TakeawayCommentsDataSource.this.commentsList.isEmpty())
            {
              TakeawayCommentsDataSource.this.noComment = true;
              paramMApiRequest = paramMApiResponse;
              if (TakeawayCommentsDataSource.this.isFirstReq())
                paramMApiRequest = TakeawayCommentsDataSource.TakeawayCommentsPageStatus.NO_COMMENT;
            }
            paramMApiResponse = localDPObject.getArray("ReviewCate");
            TakeawayCommentsDataSource.this.reviewCateList.clear();
            if ((paramMApiResponse != null) && (paramMApiResponse.length > 0))
            {
              TakeawayCommentsDataSource.this.reviewCateList.addAll(Arrays.asList(paramMApiResponse));
              if (TakeawayCommentsDataSource.this.isFirstReq())
                TakeawayCommentsDataSource.this.curCommentsType = ((DPObject)TakeawayCommentsDataSource.this.reviewCateList.get(0)).getInt("Type");
            }
            TakeawayCommentsDataSource.this.hasLikedChoice = localDPObject.getBoolean("HasLiked");
            TakeawayCommentsDataSource.this.dpReviewCount = localDPObject.getString("DpReviewCount");
            TakeawayCommentsDataSource.this.showEntrance = localDPObject.getBoolean("ShowEntrance");
            if (TakeawayCommentsDataSource.this.dataLoadListener != null)
              TakeawayCommentsDataSource.this.dataLoadListener.loadCommentsListFinsh(TakeawayNetLoadStatus.STATUS_SUCCESS, paramMApiRequest);
          }
        }
        TakeawayCommentsDataSource.access$002(TakeawayCommentsDataSource.this, null);
      }
    }
  };
  public int nextStartPage = 1;
  public boolean noComment = false;
  public ArrayList<DPObject> reviewCateList = new ArrayList();
  public String shopId;
  public String shopName;
  public boolean showEntrance = false;

  public TakeawayCommentsDataSource(NovaFragment paramNovaFragment)
  {
    this.fragment = paramNovaFragment;
  }

  private MApiRequest generateRequest()
  {
    Uri.Builder localBuilder = Uri.parse("http://waimai.api.dianping.com/commentlist.ta").buildUpon().appendQueryParameter("shopid", this.shopId).appendQueryParameter("page", String.valueOf(this.nextStartPage));
    if (!isFirstReq())
      localBuilder.appendQueryParameter("type", String.valueOf(this.curCommentsType)).appendQueryParameter("isliked", String.valueOf(this.isLiked));
    return BasicMApiRequest.mapiGet(localBuilder.toString(), CacheType.DISABLED);
  }

  private boolean isFirstReq()
  {
    return this.curCommentsType < 0;
  }

  public GAUserInfo getGAUserInfo()
  {
    GAUserInfo localGAUserInfo = new GAUserInfo();
    try
    {
      localGAUserInfo.shop_id = Integer.valueOf(Integer.parseInt(this.shopId));
      return localGAUserInfo;
    }
    catch (Exception localException)
    {
      localGAUserInfo.shop_id = Integer.valueOf(0);
    }
    return localGAUserInfo;
  }

  public void loadComments()
  {
    if (this.isEnd);
    do
      return;
    while (this.commentRequest != null);
    this.commentRequest = generateRequest();
    this.fragment.mapiService().exec(this.commentRequest, this.mapiHandler);
  }

  public void onDestroy()
  {
    if (this.commentRequest != null)
    {
      this.fragment.mapiService().abort(this.commentRequest, null, true);
      this.commentRequest = null;
    }
  }

  public void reset(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.curCommentsType = -1;
      this.isLiked = false;
    }
    this.nextStartPage = 1;
    this.isEnd = false;
    this.noComment = false;
    this.commentsList.clear();
  }

  public void setDataLoadListener(DataLoadListener paramDataLoadListener)
  {
    this.dataLoadListener = paramDataLoadListener;
  }

  public static abstract interface DataLoadListener
  {
    public abstract void loadCommentsListFinsh(TakeawayNetLoadStatus paramTakeawayNetLoadStatus, TakeawayCommentsDataSource.TakeawayCommentsPageStatus paramTakeawayCommentsPageStatus);
  }

  public static enum TakeawayCommentsPageStatus
  {
    static
    {
      LOAD_ERROR = new TakeawayCommentsPageStatus("LOAD_ERROR", 3);
      $VALUES = new TakeawayCommentsPageStatus[] { INITIAL_LOADING, NORMAL, NO_COMMENT, LOAD_ERROR };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.entity.TakeawayCommentsDataSource
 * JD-Core Version:    0.6.0
 */