package com.dianping.ugc.review.add.agent;

import android.webkit.JavascriptInterface;
import android.widget.EditText;
import com.dianping.ugc.model.UGCReviewItem;
import java.util.ArrayList;

class EditViewAddReviewAgent$JsCallback
{
  EditViewAddReviewAgent$JsCallback(EditViewAddReviewAgent paramEditViewAddReviewAgent)
  {
  }

  @JavascriptInterface
  public int getPhotoCount()
  {
    return this.this$0.getReviewData().mPhotos.size();
  }

  @JavascriptInterface
  public String getReviewBody()
  {
    if (EditViewAddReviewAgent.access$100(this.this$0) == null)
      return "";
    return EditViewAddReviewAgent.access$100(this.this$0).getText().toString();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.review.add.agent.EditViewAddReviewAgent.JsCallback
 * JD-Core Version:    0.6.0
 */