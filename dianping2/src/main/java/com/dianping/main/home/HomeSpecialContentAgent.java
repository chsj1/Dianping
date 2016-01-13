package com.dianping.main.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.dianping.adapter.BasicRecyclerAdapter.BasicHolder;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.loader.MyResources;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.OnLoadChangeListener;
import com.dianping.widget.view.NovaLinearLayout;
import org.json.JSONObject;

public class HomeSpecialContentAgent extends HomeAgent
{
  private Adapter adapter;

  public HomeSpecialContentAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.adapter.setSpecialContentItem(getHomeData());
    this.adapter.notifyMergeItemRangeChanged();
    if ((this.adapter.specialContentItem != null) && (!this.adapter.shouldShowPic))
    {
      this.adapter.picHolder.pic.setImageSize(ViewUtils.getScreenWidthPixels(getContext()) - ViewUtils.dip2px(getContext(), 5.0F) * 2, 0);
      this.adapter.picHolder.pic.setRequireBeforeAttach(true);
      this.adapter.picHolder.pic.setImage(this.adapter.specialContentItem.optString("icon"));
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.adapter = new Adapter(null);
    addCell("42SpecialContent", this.adapter);
  }

  private class Adapter extends HomeAgent.HomeAgentAdapter
  {
    PicHolder picHolder = new PicHolder(HomeSpecialContentAgent.this.res.inflate(HomeSpecialContentAgent.this.getContext(), R.layout.home_specialcontent_item, null, false));
    boolean shouldShowPic = false;
    JSONObject specialContentItem;

    private Adapter()
    {
      super();
    }

    public int getCount()
    {
      if ((this.specialContentItem == null) || (TextUtils.isEmpty(this.specialContentItem.optString("icon"))) || (!this.shouldShowPic))
        return 0;
      return 1;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
    {
      ((PicHolder)paramViewHolder).pic.setImageSize(ViewUtils.getScreenWidthPixels(HomeSpecialContentAgent.this.getContext()) - ViewUtils.dip2px(HomeSpecialContentAgent.this.getContext(), 5.0F) * 2, 0);
      ((PicHolder)paramViewHolder).pic.setImage(this.specialContentItem.optString("icon"));
      String str = this.specialContentItem.optString("schema");
      ((PicHolder)paramViewHolder).contentView.setOnClickListener(new View.OnClickListener(str)
      {
        public void onClick(View paramView)
        {
          if (!TextUtils.isEmpty(this.val$url))
            HomeSpecialContentAgent.this.startActivity(this.val$url);
        }
      });
      ((PicHolder)paramViewHolder).contentView.gaUserInfo.biz_id = this.specialContentItem.optString("bizId");
      ((PicHolder)paramViewHolder).contentView.setGAString(this.specialContentItem.optString("gaLabel"));
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      return this.picHolder;
    }

    public void setSpecialContentItem(JSONObject paramJSONObject)
    {
      if (paramJSONObject == null);
      for (paramJSONObject = null; ; paramJSONObject = paramJSONObject.optJSONObject("sepcialContent"))
      {
        this.specialContentItem = paramJSONObject;
        return;
      }
    }

    class PicHolder extends BasicRecyclerAdapter.BasicHolder
      implements OnLoadChangeListener
    {
      NovaLinearLayout contentView;
      DPNetworkImageView pic;

      public PicHolder(View arg2)
      {
        super(localView);
        this.contentView = ((NovaLinearLayout)localView);
        this.pic = ((DPNetworkImageView)localView.findViewById(R.id.image));
        this.pic.setLoadChangeListener(this);
      }

      public void onImageLoadFailed()
      {
        HomeSpecialContentAgent.Adapter.this.shouldShowPic = false;
      }

      public void onImageLoadStart()
      {
        HomeSpecialContentAgent.Adapter.this.shouldShowPic = false;
      }

      public void onImageLoadSuccess(Bitmap paramBitmap)
      {
        HomeSpecialContentAgent.Adapter.this.shouldShowPic = true;
        HomeSpecialContentAgent.Adapter.this.notifyMergeItemRangeChanged();
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.HomeSpecialContentAgent
 * JD-Core Version:    0.6.0
 */