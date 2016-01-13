package com.dianping.takeaway.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import java.util.ArrayList;

public class TakeawayLegalInfoActivity extends NovaActivity
{
  private ArrayList bigLegalImgsUrl = new ArrayList();
  private NetworkImageView[] imageViews;
  private Parcelable[] legalImgs;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.takeaway_legalinfo_activity);
    if (paramBundle == null);
    for (this.legalImgs = getIntent().getParcelableArrayExtra("legalimgs"); ; this.legalImgs = paramBundle.getParcelableArray("legalimgs"))
    {
      this.imageViews = new NetworkImageView[3];
      this.imageViews[0] = ((NetworkImageView)super.findViewById(R.id.legalimg1));
      this.imageViews[1] = ((NetworkImageView)super.findViewById(R.id.legalimg2));
      this.imageViews[2] = ((NetworkImageView)super.findViewById(R.id.legalimg3));
      i = 0;
      while (i < this.legalImgs.length)
      {
        this.bigLegalImgsUrl.add(new DPObject().edit().putString("Url", ((DPObject)this.legalImgs[i]).getString("LargePicUrl")).generate());
        i += 1;
      }
    }
    int i = 0;
    while (i < this.legalImgs.length)
    {
      this.imageViews[i].setImage(((DPObject)this.legalImgs[i]).getString("SmallPicUrl"));
      this.imageViews[i].setVisibility(0);
      this.imageViews[i].setOnClickListener(new View.OnClickListener(i)
      {
        public void onClick(View paramView)
        {
          paramView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showcheckinphoto"));
          paramView.putExtra("position", this.val$position);
          paramView.putParcelableArrayListExtra("pageList", TakeawayLegalInfoActivity.this.bigLegalImgsUrl);
          TakeawayLegalInfoActivity.this.startActivity(paramView);
        }
      });
      i += 1;
    }
    i = this.legalImgs.length;
    while (i < 3)
    {
      this.imageViews[i].setVisibility(4);
      i += 1;
    }
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    this.legalImgs = paramBundle.getParcelableArray("legalimgs");
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelableArray("legalimgs", this.legalImgs);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.takeaway.activity.TakeawayLegalInfoActivity
 * JD-Core Version:    0.6.0
 */