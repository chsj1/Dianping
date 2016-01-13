package com.dianping.ugc.photo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.dianping.accountservice.LoginResultListener;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.ugc.widget.FullScreenShopPhotoItem;
import com.dianping.base.widget.WordGroupLayout;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView.OnHeaderClickListener;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersListAdapterWrapper;
import java.util.ArrayList;

public abstract class AlbumDetailBaseActivity extends NovaActivity
  implements View.OnClickListener, AdapterView.OnItemClickListener, LoginResultListener, StickyGridHeadersGridView.OnHeaderClickListener
{
  protected ImageAdapter adapter;
  protected DPObject dpObjShop;
  protected TextView emptyTV;
  StickyGridHeadersGridView gridView;
  protected boolean isMore;
  protected ArrayList<DPObject> mFullScreenPhotos = new ArrayList();
  StickyGridHeadersListAdapterWrapper mWrapper;
  protected String shopId;
  private ArrayList<DPObject> tagList;

  void addDishDescription(String[] paramArrayOfString)
  {
    if ((paramArrayOfString != null) && (paramArrayOfString.length > 0))
    {
      ArrayList localArrayList = new ArrayList();
      int i = 0;
      while (i < paramArrayOfString.length)
      {
        localArrayList.add(new DPObject().edit().putString("Name", paramArrayOfString[i]).generate());
        i += 1;
      }
      this.tagList = localArrayList;
    }
  }

  public void onClick(View paramView)
  {
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.album_detail_layout);
    if (paramBundle != null)
    {
      this.tagList = paramBundle.getParcelableArrayList("tagList");
      this.shopId = paramBundle.getString("shopId");
      this.dpObjShop = ((DPObject)paramBundle.getParcelable("dpObjShop"));
    }
    this.gridView = ((StickyGridHeadersGridView)findViewById(R.id.gallery_gridview));
    this.gridView.setAreHeadersSticky(false);
    this.gridView.setOnHeaderClickListener(this);
    setupEmptyView();
    setEmptyMsg("没有数据");
    this.adapter = new ImageAdapter(this);
    this.mWrapper = new StickyGridHeadersListAdapterWrapper(this.adapter)
    {
      public int getCountForHeader(int paramInt)
      {
        int j = 0;
        int i = j;
        if (AlbumDetailBaseActivity.this.tagList != null)
        {
          i = j;
          if (AlbumDetailBaseActivity.this.tagList.size() > 0)
            i = 0 + 1;
        }
        if (paramInt == i + AlbumDetailBaseActivity.this.mFullScreenPhotos.size() - 1)
          return AlbumDetailBaseActivity.this.adapter.getCount();
        return 0;
      }

      public View getHeaderView(int paramInt, View paramView, ViewGroup paramViewGroup)
      {
        DPObject localDPObject;
        if ((AlbumDetailBaseActivity.this.tagList != null) && (AlbumDetailBaseActivity.this.tagList.size() > 0))
        {
          if (paramInt == 0)
          {
            paramView = AlbumDetailBaseActivity.this.getLayoutInflater().inflate(R.layout.dish_description_layout, AlbumDetailBaseActivity.this.gridView, false);
            if (paramView != null)
            {
              paramViewGroup = (WordGroupLayout)paramView.findViewById(R.id.dish_description);
              if (paramViewGroup != null)
              {
                paramViewGroup.enableWordCloudEffect();
                paramViewGroup.setButtonMargin(1);
                paramViewGroup.setButtonBackgroudResId(0);
                paramViewGroup.setTabList(AlbumDetailBaseActivity.this.tagList);
              }
            }
            return paramView;
          }
          localDPObject = (DPObject)AlbumDetailBaseActivity.this.mFullScreenPhotos.get(paramInt - 1);
          if (localDPObject == null)
            break label206;
          if (!(paramView instanceof FullScreenShopPhotoItem))
            break label184;
        }
        label184: for (paramView = (FullScreenShopPhotoItem)paramView; ; paramView = (FullScreenShopPhotoItem)LayoutInflater.from(AlbumDetailBaseActivity.this).inflate(R.layout.full_screen_pic_layout, paramViewGroup, false))
        {
          paramView.setMarkVisibility(localDPObject.getBoolean("IsOfficial"));
          paramView.setImageUrl(localDPObject.getString("ThumbUrl"));
          paramView.setImageName(localDPObject.getString("Name"));
          return paramView;
          localDPObject = (DPObject)AlbumDetailBaseActivity.this.mFullScreenPhotos.get(paramInt);
          break;
        }
        label206: return null;
      }

      public int getNumHeaders()
      {
        int j = 0;
        int i = j;
        if (AlbumDetailBaseActivity.this.tagList != null)
        {
          i = j;
          if (AlbumDetailBaseActivity.this.tagList.size() > 0)
            i = 0 + 1;
        }
        return i + AlbumDetailBaseActivity.this.mFullScreenPhotos.size();
      }
    };
    this.gridView.setAdapter(this.mWrapper);
    this.gridView.setOnItemClickListener(this);
    setRightTitleButton(R.drawable.navibar_icon_addpic, this);
  }

  public void onFullScreenPicClick(AdapterView<?> paramAdapterView, View paramView, int paramInt)
  {
  }

  public void onHeaderClick(AdapterView<?> paramAdapterView, View paramView, long paramLong)
  {
    if ((this.tagList != null) && (this.tagList.size() > 0))
    {
      if (paramLong == 0L)
        return;
      paramLong -= 1L;
    }
    while (true)
    {
      onFullScreenPicClick(paramAdapterView, paramView, (int)paramLong);
      return;
    }
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelableArrayList("tagList", this.tagList);
    paramBundle.putString("shopId", this.shopId);
    paramBundle.putParcelable("dpObjShop", this.dpObjShop);
  }

  abstract MApiRequest photoTask(String paramString, int paramInt);

  protected void setEmptyMsg(String paramString)
  {
    this.emptyTV.setText(paramString);
  }

  protected void setupEmptyView()
  {
    this.emptyTV = ((TextView)findViewById(R.id.gallery_empty));
    Drawable localDrawable = getResources().getDrawable(R.drawable.empty_page_nothing);
    localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
    this.emptyTV.setCompoundDrawablePadding(8);
    this.emptyTV.setCompoundDrawables(localDrawable, null, null, null);
    this.emptyTV.setMovementMethod(LinkMovementMethod.getInstance());
    this.gridView.setEmptyView(this.emptyTV);
  }

  class ImageAdapter extends BasicLoadAdapter
  {
    private int albumHeight;

    public ImageAdapter(Context arg2)
    {
      super();
      int i = ViewUtils.getScreenWidthPixels(localContext) * 45 / 100 * 138 / 201;
      this.albumHeight = (ViewUtils.dip2px(localContext, 4.0F) + i);
    }

    public MApiRequest createRequest(int paramInt)
    {
      return AlbumDetailBaseActivity.this.photoTask(AlbumDetailBaseActivity.this.shopId, paramInt);
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = super.getView(paramInt, paramView, paramViewGroup);
      paramViewGroup = getItem(paramInt);
      if (paramViewGroup == ERROR)
      {
        paramViewGroup = (AbsListView.LayoutParams)paramView.getLayoutParams();
        paramViewGroup.height = this.albumHeight;
        paramView.setLayoutParams(paramViewGroup);
      }
      do
        return paramView;
      while (paramViewGroup != LOADING);
      paramViewGroup = (AbsListView.LayoutParams)paramView.getLayoutParams();
      paramViewGroup.height = this.albumHeight;
      paramView.setLayoutParams(paramViewGroup);
      return paramView;
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Object localObject;
      View localView;
      int i;
      if ((paramView == null) || (paramView.getId() != R.id.item_of_photo_album))
      {
        paramView = AlbumDetailBaseActivity.this.getLayoutInflater().inflate(R.layout.item_of_shop_photo, paramViewGroup, false);
        paramView.findViewById(R.id.lay_round_corner).setBackgroundResource(R.drawable.round_corner_white);
        localObject = (TextView)paramView.findViewById(R.id.text_name);
        ((TextView)localObject).setText(paramDPObject.getString("Name"));
        paramViewGroup = paramView.findViewById(R.id.lay_shadow);
        ViewUtils.hideView(paramViewGroup, false);
        localView = paramView.findViewById(R.id.lay_img_desc);
        ViewUtils.hideView(localView, false);
        paramInt = ViewUtils.getScreenWidthPixels(AlbumDetailBaseActivity.this);
        if (!AlbumDetailBaseActivity.this.isMore)
          break label225;
        ViewUtils.showView((View)localObject);
        i = paramInt * 45 / 100;
        paramInt = i * 300 / 280;
      }
      while (true)
      {
        paramView.getLayoutParams().width = i;
        paramView.getLayoutParams().height = paramInt;
        localObject = (NetworkImageView)paramView.findViewById(R.id.img_shop_photo);
        ((NetworkImageView)localObject).setImageHandler(new AlbumDetailBaseActivity.ImageAdapter.1(this, paramDPObject, paramViewGroup, localView));
        ((NetworkImageView)localObject).setImage(paramDPObject.getString("ThumbUrl"));
        ViewUtils.hideView(paramView.findViewById(R.id.divider), false);
        ViewUtils.hideView(paramView.findViewById(R.id.lay_shop_detail), false);
        return paramView;
        break;
        label225: ViewUtils.hideView((View)localObject, false);
        i = paramInt * 29 / 100;
        paramInt = i;
        boolean bool = paramDPObject.getBoolean("IsOfficial");
        localObject = (ImageView)paramView.findViewById(R.id.iv_offical_mark);
        if (bool)
        {
          ViewUtils.showView((View)localObject);
          continue;
        }
        ViewUtils.hideView((View)localObject, false);
      }
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestFinish(paramMApiRequest, paramMApiResponse);
      if ((paramMApiResponse.result() instanceof DPObject))
      {
        paramMApiRequest = (DPObject)paramMApiResponse.result();
        if (paramMApiRequest.getInt("StartIndex") == 0)
          AlbumDetailBaseActivity.this.addDishDescription(paramMApiRequest.getStringArray("PhotoTagList"));
        paramMApiRequest = paramMApiRequest.getArray("List");
        if ((paramMApiRequest != null) && (paramMApiRequest.length > 0))
        {
          int i = 0;
          while (i < paramMApiRequest.length)
          {
            paramMApiResponse = paramMApiRequest[i];
            if (paramMApiResponse.getBoolean("IsFullPic"))
            {
              AlbumDetailBaseActivity.this.mFullScreenPhotos.add(paramMApiResponse);
              AlbumDetailBaseActivity.this.gridView.setEmptyView(null);
            }
            i += 1;
          }
        }
        AlbumDetailBaseActivity.this.mWrapper.notifyDataSetChanged();
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.photo.AlbumDetailBaseActivity
 * JD-Core Version:    0.6.0
 */