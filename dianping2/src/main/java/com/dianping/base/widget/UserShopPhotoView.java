package com.dianping.base.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class UserShopPhotoView extends NovaLinearLayout
{
  private int mAlbumFrameWidth = ViewUtils.getScreenWidthPixels(getContext());
  private int[] mNetworkImageViewArrIds = { R.id.iv_image1, R.id.iv_image2, R.id.iv_image3, R.id.iv_image4, R.id.iv_image5, R.id.iv_image6, R.id.iv_image7, R.id.iv_image8, R.id.iv_image9 };
  private int[] mNetworkImageViewLayoutIds = { R.layout.user_photo_layout_1, R.layout.user_photo_layout_2, R.layout.user_photo_layout_3, R.layout.user_photo_layout_4, R.layout.user_photo_layout_5, R.layout.user_photo_layout_6, R.layout.user_photo_layout_6, R.layout.user_photo_layout_6, R.layout.user_photo_layout_9 };
  ArrayList<DPObject> photoArray = new ArrayList();

  public UserShopPhotoView(Context paramContext)
  {
    this(paramContext, null);
  }

  public UserShopPhotoView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void startPicListActivity(DPObject paramDPObject1, DPObject paramDPObject2)
  {
    if ((paramDPObject1 == null) || (paramDPObject2 == null))
      return;
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://reviewpicturegridlist"));
    if (paramDPObject2 == null);
    for (int i = 0; ; i = paramDPObject2.getInt("UserID"))
    {
      localIntent.putExtra("userId", i);
      localIntent.putExtra("shopId", paramDPObject1.getInt("ID"));
      localIntent.putExtra("shopName", DPObjectUtils.getShopFullName(paramDPObject1));
      getContext().startActivity(localIntent);
      return;
    }
  }

  private boolean startShowCheckinPhotoActivity(View paramView, DPObject paramDPObject1, DPObject paramDPObject2)
  {
    if (paramDPObject2.getInt("FeedType") == 8)
    {
      Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showcheckinphoto"));
      localIntent.putExtra("checkinID", paramDPObject2.getInt("FeedReferId"));
      localIntent.putExtra("fromActivity", "UserRecordActivity");
      localIntent.putExtra("shopname", DPObjectUtils.getShopFullName(paramDPObject1));
      localIntent.putExtra("position", (Integer)paramView.getTag());
      localIntent.putParcelableArrayListExtra("pageList", this.photoArray);
      getContext().startActivity(localIntent);
      return true;
    }
    return false;
  }

  private void startShowPhotoActivity(View paramView, DPObject paramDPObject)
  {
    if ((paramDPObject == null) || (this.photoArray.size() == 0))
      return;
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showphoto"));
    localIntent.putExtra("pageList", this.photoArray);
    localIntent.putExtra("position", (Integer)paramView.getTag());
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(paramDPObject);
    localIntent.putExtra("arrShopObjs", localArrayList);
    localIntent.putExtra("isUserPhotoMode", true);
    paramDPObject = new ByteArrayOutputStream();
    ((BitmapDrawable)((NetworkImageView)paramView).getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, paramDPObject);
    localIntent.putExtra("currentbitmap", paramDPObject.toByteArray());
    getContext().startActivity(localIntent);
  }

  public void setShopPhoto(DPObject[] paramArrayOfDPObject, DPObject paramDPObject)
  {
    removeAllViews();
    if ((paramArrayOfDPObject == null) || (paramArrayOfDPObject.length < 1) || (paramDPObject == null))
      return;
    DPObject localDPObject1 = paramDPObject.getObject("User");
    DPObject localDPObject2 = paramDPObject.getObject("Shop");
    int i = 0;
    while (i < paramArrayOfDPObject.length)
    {
      if ((paramArrayOfDPObject[i] != null) && (paramArrayOfDPObject[i].getObject("ShopPhoto") != null))
        this.photoArray.add(paramArrayOfDPObject[i].getObject("ShopPhoto"));
      i += 1;
    }
    label105: View localView;
    double d;
    if (paramArrayOfDPObject.length < 9)
    {
      i = paramArrayOfDPObject.length;
      localView = LayoutInflater.from(getContext()).inflate(this.mNetworkImageViewLayoutIds[(i - 1)], null);
      d = 0.0D;
      switch (i)
      {
      default:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      }
    }
    TextView localTextView;
    int j;
    while (true)
    {
      localView.setLayoutParams(new LinearLayout.LayoutParams(-1, (int)(this.mAlbumFrameWidth * d)));
      localTextView = (TextView)localView.findViewById(R.id.tv_moreimg);
      addView(localView);
      j = 0;
      if (j < i)
      {
        localObject = localView.findViewById(this.mNetworkImageViewArrIds[j]);
        if (localObject != null)
          break label331;
      }
      if (localTextView == null)
        break;
      if (paramDPObject.getInt("FeedType") != 8)
        break label441;
      localTextView.setVisibility(8);
      return;
      i = 9;
      break label105;
      d = 0.66D;
      continue;
      d = 0.5D;
      continue;
      d = 0.66D;
      continue;
      d = 1.0D;
      continue;
      d = 1.0D;
      continue;
      d = 1.0D;
      continue;
      d = 1.0D;
      continue;
      d = 1.0D;
      continue;
      d = 1.0D;
    }
    label331: Object localObject = (NetworkImageView)localObject;
    ((NetworkImageView)localObject).placeholderLoading = R.drawable.placeholder_loading;
    ((NetworkImageView)localObject).placeholderEmpty = R.drawable.placeholder_empty;
    ((NetworkImageView)localObject).placeholderError = R.drawable.placeholder_error;
    ((NetworkImageView)localObject).setFocusable(false);
    if (j == 0)
      ((NetworkImageView)localObject).setImage(paramArrayOfDPObject[j].getString("BigUrl"));
    while (true)
    {
      ((NetworkImageView)localObject).setTag(Integer.valueOf(j));
      ((NetworkImageView)localObject).setOnClickListener(new View.OnClickListener(localDPObject2, paramDPObject)
      {
        public void onClick(View paramView)
        {
          if (UserShopPhotoView.this.startShowCheckinPhotoActivity(paramView, this.val$shop, this.val$feed))
            return;
          UserShopPhotoView.this.startShowPhotoActivity(paramView, this.val$shop);
        }
      });
      j += 1;
      break;
      ((NetworkImageView)localObject).setImage(paramArrayOfDPObject[j].getString("MiddleUrl"));
    }
    label441: localTextView.setText("更多图片");
    localTextView.setOnClickListener(new View.OnClickListener(localDPObject2, localDPObject1)
    {
      public void onClick(View paramView)
      {
        UserShopPhotoView.this.startPicListActivity(this.val$shop, this.val$user);
      }
    });
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.UserShopPhotoView
 * JD-Core Version:    0.6.0
 */