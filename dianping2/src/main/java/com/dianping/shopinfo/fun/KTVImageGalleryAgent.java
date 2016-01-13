package com.dianping.shopinfo.fun;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.Uri.Builder;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.ugc.photo.UploadPhotoUtil;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.shopinfo.common.ImageGalleryAgent;
import com.dianping.widget.view.GAHelper;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class KTVImageGalleryAgent extends ImageGalleryAgent
{
  private static final int MAX_IMAGE_COUNT = 7;
  private ArrayList<DPObject> pageList = new ArrayList();
  private int totalCount = 0;

  public KTVImageGalleryAgent(Object paramObject)
  {
    super(paramObject);
  }

  protected MApiRequest getImagesRequest()
  {
    return BasicMApiRequest.mapiGet(Uri.parse("http://m.api.dianping.com/fun/shopdetailktvhead.fn").buildUpon().appendQueryParameter("shopid", String.valueOf(shopId())).toString(), CacheType.NORMAL);
  }

  protected void onEmptyClicked()
  {
    GAHelper.instance().contextStatisticsEvent(getContext(), "toupload", null, "tap");
    UploadPhotoUtil.uploadShopPhoto(getContext(), getShop());
  }

  public void onGalleryImageClick(int paramInt1, int paramInt2, Drawable paramDrawable)
  {
    boolean bool = true;
    if ((paramInt1 == paramInt2 - 1) && (this.totalCount > paramInt2))
    {
      paramDrawable = getShop();
      paramInt1 = paramDrawable.getInt("ID");
      localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://shopphoto"));
      localIntent.putExtra("shopId", paramInt1);
      localIntent.putExtra("objShop", paramDrawable);
      if ((paramDrawable != null) && (paramDrawable.getInt("Status") != 1) && (paramDrawable.getInt("Status") != 4));
      while (true)
      {
        localIntent.putExtra("enableUpload", bool);
        startActivity(localIntent);
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
  }

  protected void requestImageFinish(boolean paramBoolean, MApiResponse paramMApiResponse)
  {
    int i = 7;
    if (paramBoolean)
    {
      int k = getShop().getInt("ID");
      paramMApiResponse = (DPObject)paramMApiResponse.result();
      if (paramMApiResponse != null)
      {
        this.totalCount = paramMApiResponse.getInt("PicTotalCount");
        String[] arrayOfString = paramMApiResponse.getStringArray("Pictures");
        if (arrayOfString != null)
        {
          this.pageList.clear();
          if (arrayOfString.length > 7);
          while (true)
          {
            int j = 0;
            while (true)
            {
              paramMApiResponse = arrayOfString;
              if (j >= i)
                break;
              paramMApiResponse = new DPObject().edit().putString("Url", arrayOfString[j]).putInt("ShopID", k).generate();
              this.pageList.add(paramMApiResponse);
              j += 1;
            }
            i = arrayOfString.length;
          }
        }
        paramMApiResponse = new String[0];
        setImages(paramMApiResponse, this.totalCount);
        return;
      }
      setImages(null, 0);
      return;
    }
    setImages(null, 0);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.fun.KTVImageGalleryAgent
 * JD-Core Version:    0.6.0
 */