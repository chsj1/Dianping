package com.dianping.hotel.ugc;

import android.text.Html;
import com.dianping.ugc.model.UGCReviewItem;
import com.dianping.ugc.model.UploadPhotoData;
import java.util.ArrayList;

public class ReviewItemHelper
{
  public static ReviewItem updateReviewItem(ReviewItem paramReviewItem, UGCReviewItem paramUGCReviewItem)
  {
    if ("0".equals(paramUGCReviewItem.averagePrice));
    for (Object localObject = ""; ; localObject = paramUGCReviewItem.averagePrice)
    {
      paramReviewItem.avgPrice = ((String)localObject);
      paramReviewItem.shopPower = Integer.parseInt(paramUGCReviewItem.star);
      paramReviewItem.content = Html.fromHtml(paramUGCReviewItem.comment);
      localObject = new String[paramUGCReviewItem.mPhotos.size()];
      int i = 0;
      while (i < paramUGCReviewItem.mPhotos.size())
      {
        localObject[i] = ((UploadPhotoData)paramUGCReviewItem.mPhotos.get(i)).photoPath;
        i += 1;
      }
    }
    paramReviewItem.thumbnailsPhotos = ((String)localObject);
    paramReviewItem.photos = ((String)localObject);
    return (ReviewItem)paramReviewItem;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.hotel.ugc.ReviewItemHelper
 * JD-Core Version:    0.6.0
 */