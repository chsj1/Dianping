package com.dianping.travel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.NovaListActivity;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class SceneryOrderNoticeActivity extends NovaListActivity
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    super.setContentView(R.layout.scenery_order_notice);
    Parcelable[] arrayOfParcelable = getIntent().getParcelableArrayExtra("notices");
    Object localObject = null;
    paramBundle = (Bundle)localObject;
    int i;
    if (arrayOfParcelable != null)
    {
      paramBundle = (Bundle)localObject;
      if (arrayOfParcelable.length > 0)
      {
        localObject = new DPObject[arrayOfParcelable.length];
        i = 0;
        while (true)
        {
          paramBundle = (Bundle)localObject;
          if (i >= arrayOfParcelable.length)
            break;
          localObject[i] = ((DPObject)arrayOfParcelable[i]);
          i += 1;
        }
      }
    }
    if (paramBundle != null)
    {
      localObject = (ViewGroup)findViewById(R.id.noticesContent);
      i = 0;
      if (i < arrayOfParcelable.length)
      {
        View localView = LayoutInflater.from(this).inflate(R.layout.scenery_order_notice_item, null);
        if (TextUtils.isEmpty(paramBundle[i].getString("ID")))
        {
          localView.findViewById(R.id.noticeName).setVisibility(8);
          label141: if (!TextUtils.isEmpty(paramBundle[i].getString("Name")))
            break label218;
          localView.findViewById(R.id.noticeContent).setVisibility(8);
        }
        for (int j = 0; ; j = 1)
        {
          if (j != 0)
            ((ViewGroup)localObject).addView(localView);
          i += 1;
          break;
          ((TextView)localView.findViewById(R.id.noticeName)).setText(paramBundle[i].getString("ID"));
          break label141;
          label218: ((TextView)localView.findViewById(R.id.noticeContent)).setText(Html.fromHtml(paramBundle[i].getString("Name")));
        }
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.travel.SceneryOrderNoticeActivity
 * JD-Core Version:    0.6.0
 */