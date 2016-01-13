package com.dianping.shopinfo.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.widget.view.GAHelper;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class CommonImageGalleryAgent extends ImageGalleryAgent
{
  private ArrayList<DPObject> pageList;
  private int totalCount;

  public CommonImageGalleryAgent(Object paramObject)
  {
    super(paramObject);
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (getShop() == null);
    int j;
    boolean bool;
    do
    {
      do
        return;
      while (getShopStatus() != 0);
      removeAllCells();
      paramBundle = getShop();
      j = paramBundle.getInt("ID");
      bool = paramBundle.getBoolean("HasMultiPic");
      this.totalCount = paramBundle.getInt("PicCount");
      paramBundle = paramBundle.getArray("AdvancedPics");
    }
    while ((!bool) || (paramBundle == null));
    int k = paramBundle.length;
    ArrayList localArrayList = new ArrayList(k);
    this.pageList = new ArrayList(k);
    int i = 0;
    while (i < k)
    {
      localArrayList.add(paramBundle[i].getString("ThumbUrl"));
      DPObject localDPObject = new DPObject().edit().putInt("ShopID", j).putString("Url", paramBundle[i].getString("Url")).putTime("Time", paramBundle[i].getLong("UploadTime")).putString("Name", paramBundle[i].getString("Name")).generate();
      this.pageList.add(localDPObject);
      i += 1;
    }
    setImages((String[])localArrayList.toArray(new String[0]), true);
  }

  protected void onEmptyClicked()
  {
    GAHelper.instance().contextStatisticsEvent(getContext(), "toupload", null, "tap");
    UploadPhotoUtil.uploadShopPhoto(getContext(), getShop());
  }

  public void onGalleryImageClick(int paramInt1, int paramInt2, Drawable paramDrawable)
  {
    boolean bool = true;
    if (paramInt1 == paramInt2 - 1)
    {
      paramDrawable = getShop();
      paramInt2 = paramDrawable.getInt("ID");
      localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopphoto"));
      localIntent.putExtra("shopId", paramInt2);
      localIntent.putExtra("objShop", paramDrawable);
      if ((paramDrawable != null) && (paramDrawable.getInt("Status") != 1) && (paramDrawable.getInt("Status") != 4));
      while (true)
      {
        localIntent.putExtra("enableUpload", bool);
        startActivity(localIntent);
        GAHelper.instance().contextStatisticsEvent(getContext(), "photo", "全部图片", paramInt1, "tap");
        return;
        bool = false;
      }
    }
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showphotoandmore"));
    localIntent.putExtra("position", paramInt1);
    localIntent.putExtra("totalPicCount", this.totalCount);
    Object localObject = new ArrayList();
    ((ArrayList)localObject).add(getShop());
    localIntent.putParcelableArrayListExtra("arrShopObjs", (ArrayList)localObject);
    localIntent.putParcelableArrayListExtra("pageList", this.pageList);
    if (paramDrawable != null)
    {
      localObject = new ByteArrayOutputStream();
      ((BitmapDrawable)paramDrawable).getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, (OutputStream)localObject);
      localIntent.putExtra("currentbitmap", ((ByteArrayOutputStream)localObject).toByteArray());
    }
    getContext().startActivity(localIntent);
    GAHelper.instance().contextStatisticsEvent(getContext(), "photo", null, paramInt1, "tap");
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.common.CommonImageGalleryAgent
 * JD-Core Version:    0.6.0
 */