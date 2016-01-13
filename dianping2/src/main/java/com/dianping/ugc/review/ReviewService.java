package com.dianping.ugc.review;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import com.dianping.app.DPApplication;
import com.dianping.ugc.model.UGCReviewItem;
import com.dianping.ugc.service.UGCService;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.string;

public class ReviewService extends UGCService
{
  public static final String ACTION_ADD_REVIEW = "com.dianping.ugc.addreview";
  private static final int ADDREVIEW_NOTIFY_ID = "ReviewService".hashCode();
  public static final int REVIEW_STATUS_FAIL = 2;
  public static final int REVIEW_STATUS_SUCCESS = 3;
  public static final int REVIEW_STATUS_UPLOADING = 1;
  private static final String TAG = "ReviewService";
  private NotificationManager notifyManager = (NotificationManager)DPApplication.instance().getSystemService("notification");

  public static ReviewService getInstance()
  {
    return ReviewService.ReviewServiceInner.INSTANCE;
  }

  private void notifyReview(int paramInt)
  {
    Intent localIntent = new Intent();
    Object localObject = "";
    int i = 0;
    Notification.Builder localBuilder = new Notification.Builder(DPApplication.instance());
    switch (paramInt)
    {
    default:
      paramInt = i;
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      localBuilder.setTicker((CharSequence)localObject).setContentIntent(PendingIntent.getActivity(DPApplication.instance(), 0, localIntent, 0));
      localBuilder.setContentTitle("大众点评").setContentText((CharSequence)localObject);
      localBuilder.setLargeIcon(((BitmapDrawable)DPApplication.instance().getResources().getDrawable(R.drawable.icon)).getBitmap());
      localObject = localBuilder.getNotification();
      ((Notification)localObject).flags = paramInt;
      this.notifyManager.notify(ADDREVIEW_NOTIFY_ID, (Notification)localObject);
      return;
      localObject = DPApplication.instance().getString(R.string.ugc_toast_reviewing);
      localBuilder.setSmallIcon(R.drawable.upload_notification_review_anim);
      paramInt = 2;
      continue;
      localObject = DPApplication.instance().getString(R.string.ugc_toast_review_failed);
      localIntent.setAction("android.intent.action.VIEW");
      localIntent.setData(Uri.parse("dianping://drafts"));
      localBuilder.setSmallIcon(R.drawable.review_fail);
      paramInt = i;
      continue;
      localObject = DPApplication.instance().getString(R.string.ugc_toast_review_success);
      localBuilder.setSmallIcon(R.drawable.review_tick);
      new Thread(new ReviewService.2(this)).start();
      paramInt = i;
    }
  }

  public void review(UGCReviewItem paramUGCReviewItem)
  {
    submit(new ReviewService.1(this, paramUGCReviewItem));
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.ReviewService
 * JD-Core Version:    0.6.0
 */