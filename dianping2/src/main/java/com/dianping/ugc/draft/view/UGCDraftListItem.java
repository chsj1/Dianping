package com.dianping.ugc.draft.view;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.base.widget.BaseMulDeletableItem;
import com.dianping.base.widget.ShopPower;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.ugc.model.UGCContentItem;
import com.dianping.ugc.model.UGCReviewItem;
import com.dianping.ugc.model.UGCUploadCommunityPhotoItem;
import com.dianping.ugc.model.UGCUploadPhotoItem;
import com.dianping.ugc.model.UploadPhotoData;
import com.dianping.ugc.model.UploadPhotoTagData;
import com.dianping.ugc.review.ReviewService;
import com.dianping.ugc.uploadphoto.communityphoto.AddCommunityPhotoService;
import com.dianping.ugc.uploadphoto.shopphoto.AddShopPhotoService;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.v1.R.string;
import com.dianping.widget.view.NovaTextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class UGCDraftListItem extends BaseMulDeletableItem
  implements View.OnClickListener
{
  private static final DateFormat FMT_DATE = new SimpleDateFormat("yyyy-MM-dd");
  private static final int PHOTO_VIEW_SIZE = 4;
  public static HashMap<String, Long> resendMap = new HashMap();
  private TextView comment;
  private TextView dateAndSource;
  private UGCContentItem draft = null;
  private boolean editable = false;
  private DPNetworkImageView[] imageView = new DPNetworkImageView[4];
  private boolean isEdit = false;
  private TextView more;
  private LinearLayout photoLayout;
  private NovaTextView resend;
  private TextView shopName;
  private ShopPower shopPower;
  private int viewSideLength;

  public UGCDraftListItem(Context paramContext)
  {
    super(paramContext);
    this.viewSideLength = (ViewUtils.getScreenWidthPixels(paramContext) - ViewUtils.dip2px(getContext(), 10.0F) * 5 >> 2);
  }

  public UGCDraftListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.viewSideLength = (ViewUtils.getScreenWidthPixels(paramContext) - ViewUtils.dip2px(getContext(), 10.0F) * 5 >> 2);
  }

  private int setPhotos(ArrayList<UploadPhotoData> paramArrayList)
  {
    int j = 0;
    int i = 0;
    if ((paramArrayList == null) || (paramArrayList.size() == 0))
    {
      this.photoLayout.setVisibility(8);
      return 0;
    }
    int k;
    int m;
    if (this.editable)
    {
      k = 0;
      while ((k < paramArrayList.size()) && (k < 4))
      {
        this.imageView[k].setImage(((UploadPhotoData)paramArrayList.get(k)).photoPath);
        this.imageView[k].setVisibility(0);
        k += 1;
      }
      m = paramArrayList.size();
      if (m <= 4)
        break label254;
      this.photoLayout.setVisibility(0);
      this.more.setText(String.format(getResources().getString(R.string.ugc_draft_more_count), new Object[] { Integer.valueOf(m) }));
      this.more.setVisibility(0);
    }
    while (true)
    {
      if (this.editable)
      {
        return 0;
        paramArrayList = paramArrayList.iterator();
        while (true)
        {
          k = j;
          m = i;
          if (!paramArrayList.hasNext())
            break;
          UploadPhotoData localUploadPhotoData = (UploadPhotoData)paramArrayList.next();
          if (localUploadPhotoData.success)
            continue;
          k = i + 1;
          i = k;
          if (j >= 4)
            continue;
          this.imageView[j].setImage(localUploadPhotoData.photoPath);
          this.imageView[j].setVisibility(0);
          j += 1;
          i = k;
        }
        label254: if (m > 0)
        {
          this.photoLayout.setVisibility(0);
          this.more.setVisibility(8);
          while (k < 4)
          {
            this.imageView[k].setVisibility(4);
            k += 1;
          }
        }
        this.photoLayout.setVisibility(8);
        continue;
      }
      return m;
    }
  }

  public void onClick(View paramView)
  {
    if ("uploadcommunityphoto".equals(this.draft.getType()))
      AddCommunityPhotoService.getInstance().uploadCommunityPhoto((UGCUploadCommunityPhotoItem)this.draft);
    while (true)
    {
      this.resend.setText(R.string.ugc_draft_resending);
      resendMap.put(this.draft.id, Long.valueOf(this.draft.time));
      ViewUtils.disableView(this.resend);
      return;
      if ("uploadphoto".equals(this.draft.getType()))
      {
        AddShopPhotoService.getInstance().uploadShopPhoto((UGCUploadPhotoItem)this.draft);
        continue;
      }
      if (!"review".equals(this.draft.getType()))
        continue;
      ReviewService.getInstance().review((UGCReviewItem)this.draft);
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.shopName = ((TextView)findViewById(R.id.draft_shop_name));
    this.dateAndSource = ((TextView)findViewById(R.id.draft_date_source));
    this.resend = ((NovaTextView)findViewById(R.id.resend));
    this.resend.setGAString("button_resend");
    this.resend.setOnClickListener(this);
    this.shopPower = ((ShopPower)findViewById(R.id.draft_shop_power));
    this.comment = ((TextView)findViewById(R.id.draft_comment));
    this.imageView[0] = ((DPNetworkImageView)findViewById(R.id.draft_photo1));
    this.imageView[1] = ((DPNetworkImageView)findViewById(R.id.draft_photo2));
    this.imageView[2] = ((DPNetworkImageView)findViewById(R.id.draft_photo3));
    this.imageView[3] = ((DPNetworkImageView)findViewById(R.id.draft_photo4));
    this.photoLayout = ((LinearLayout)findViewById(R.id.draft_photoLayout));
    this.more = ((TextView)findViewById(R.id.draft_more_photo));
    Object localObject = this.photoLayout.getLayoutParams();
    ((ViewGroup.LayoutParams)localObject).height = this.viewSideLength;
    this.photoLayout.setLayoutParams((ViewGroup.LayoutParams)localObject);
    localObject = this.imageView[0].getLayoutParams();
    ((ViewGroup.LayoutParams)localObject).width = this.viewSideLength;
    this.imageView[0].setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.imageView[1].setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.imageView[2].setLayoutParams((ViewGroup.LayoutParams)localObject);
    localObject = (FrameLayout)findViewById(R.id.draft_more_photo_container);
    ViewGroup.LayoutParams localLayoutParams = ((FrameLayout)localObject).getLayoutParams();
    localLayoutParams.width = this.viewSideLength;
    ((FrameLayout)localObject).setLayoutParams(localLayoutParams);
  }

  public void setDraft(UGCContentItem paramUGCContentItem)
  {
    this.draft = paramUGCContentItem;
    Object localObject2 = paramUGCContentItem.getType();
    this.editable = paramUGCContentItem.editable;
    int i = 0;
    Object localObject1;
    if ("review".equals(localObject2))
    {
      Object localObject3 = this.shopName;
      if (TextUtils.isEmpty(paramUGCContentItem.shopName))
      {
        localObject1 = ((UGCReviewItem)paramUGCContentItem).tuanTitle;
        ((TextView)localObject3).setText((CharSequence)localObject1);
        label60: localObject3 = FMT_DATE.format(new Date(paramUGCContentItem.time));
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append((String)localObject3 + " ");
        if (!"uploadcommunityphoto".equals(localObject2))
          break label441;
        localObject2 = (UGCUploadCommunityPhotoItem)paramUGCContentItem;
        int j = 0;
        i = j;
        if (((UGCUploadCommunityPhotoItem)localObject2).mPhotos != null)
        {
          i = j;
          if (((UGCUploadCommunityPhotoItem)localObject2).mPhotos.size() > 0)
          {
            localObject3 = ((UGCUploadCommunityPhotoItem)localObject2).mPhotos.iterator();
            while (true)
            {
              i = j;
              if (!((Iterator)localObject3).hasNext())
                break;
              Object localObject4 = (UploadPhotoData)((Iterator)localObject3).next();
              if ((((UploadPhotoData)localObject4).tags == null) || (((UploadPhotoData)localObject4).tags.size() <= 0))
                continue;
              localObject4 = ((UploadPhotoTagData)((UploadPhotoData)localObject4).tags.get(0)).content;
              if ((localObject4 == null) || (((String)localObject4).length() <= 0))
                continue;
              i = 1;
              this.shopName.setText("#" + (String)localObject4 + "#");
            }
          }
        }
        if (i == 0)
          this.shopName.setText(R.string.ugc_draft_community);
        this.shopPower.setVisibility(8);
        localObject3 = ((UGCUploadCommunityPhotoItem)paramUGCContentItem).text;
        if ((localObject3 == null) || (((String)localObject3).length() <= 0))
          break label429;
        this.comment.setText((CharSequence)localObject3);
        this.comment.setVisibility(0);
        label339: ((StringBuilder)localObject1).append(getResources().getString(R.string.ugc_draft_community));
        i = setPhotos(((UGCUploadCommunityPhotoItem)localObject2).mPhotos);
        label364: if (!this.editable)
          break label625;
        ((StringBuilder)localObject1).append(getResources().getString(R.string.ugc_draft_save));
        this.resend.setVisibility(8);
      }
    }
    while (true)
    {
      while (true)
      {
        this.dateAndSource.setText(((StringBuilder)localObject1).toString());
        return;
        localObject1 = paramUGCContentItem.shopName;
        break;
        this.shopName.setText(paramUGCContentItem.shopName);
        break label60;
        label429: this.comment.setVisibility(8);
        break label339;
        label441: if ("uploadphoto".equals(localObject2))
        {
          this.shopPower.setVisibility(8);
          this.comment.setVisibility(8);
          ((StringBuilder)localObject1).append(getResources().getString(R.string.ugc_draft_add_shop_photo));
          i = setPhotos(((UGCUploadPhotoItem)paramUGCContentItem).mPhotos);
          break label364;
        }
        if (!"review".equals(localObject2))
          break label364;
        try
        {
          i = Integer.valueOf(((UGCReviewItem)paramUGCContentItem).star).intValue();
          this.shopPower.setPower(i);
          this.shopPower.setVisibility(0);
          localObject2 = ((UGCReviewItem)paramUGCContentItem).comment;
          if ((localObject2 != null) && (((String)localObject2).length() > 0))
          {
            this.comment.setText((CharSequence)localObject2);
            this.comment.setVisibility(0);
            ((StringBuilder)localObject1).append(getResources().getString(R.string.ugc_draft_review));
            i = setPhotos(((UGCReviewItem)paramUGCContentItem).mPhotos);
          }
        }
        catch (NumberFormatException localNumberFormatException)
        {
          while (true)
          {
            i = 0;
            continue;
            this.comment.setVisibility(8);
          }
        }
      }
      label625: Long localLong = (Long)resendMap.get(paramUGCContentItem.id);
      if ((localLong != null) && (localLong.equals(Long.valueOf(paramUGCContentItem.time))))
      {
        this.resend.setEnabled(false);
        this.resend.setText(R.string.ugc_draft_resending);
      }
      while (true)
      {
        if (i != 0)
          break label719;
        ((StringBuilder)localObject1).append(getResources().getString(R.string.ugc_draft_send_fail));
        break;
        this.resend.setEnabled(true);
        this.resend.setText(R.string.ugc_draft_resend);
      }
      label719: ((StringBuilder)localObject1).append(String.format(getResources().getString(R.string.ugc_draft_upload_photo_fail), new Object[] { Integer.valueOf(i) }));
    }
  }

  public void setEditable(boolean paramBoolean)
  {
    this.isEdit = paramBoolean;
    if (!this.editable)
    {
      if (!this.isEdit)
        break label34;
      this.resend.setVisibility(8);
    }
    while (true)
    {
      super.setEditable(paramBoolean);
      return;
      label34: this.resend.setVisibility(0);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.draft.view.UGCDraftListItem
 * JD-Core Version:    0.6.0
 */