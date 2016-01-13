package com.dianping.shopinfo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.TitleBar;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import java.util.ArrayList;

public class WayCardActivity extends NovaActivity
  implements View.OnClickListener
{
  private void init()
  {
    Object localObject2 = (ImageView)findViewById(R.id.activity_close);
    Object localObject1 = (DPNetworkImageView)findViewById(R.id.card_backgroud);
    Object localObject3 = (TextView)findViewById(R.id.orishopname);
    Object localObject4 = (TextView)findViewById(R.id.chshopname);
    Object localObject5 = (TextView)findViewById(R.id.enshopname);
    TextView localTextView1 = (TextView)findViewById(R.id.oriaddressname);
    TextView localTextView2 = (TextView)findViewById(R.id.chaddressname);
    TextView localTextView3 = (TextView)findViewById(R.id.enaddressname);
    ((ImageView)localObject2).setOnClickListener(this);
    localObject2 = new ArrayList();
    ((ArrayList)localObject2).add(localObject3);
    ((ArrayList)localObject2).add(localObject4);
    ((ArrayList)localObject2).add(localObject5);
    localObject5 = (DPObject)getIntent().getParcelableExtra("addressCard");
    if (localObject5 == null);
    while (true)
    {
      return;
      localObject4 = ((DPObject)localObject5).getString("PicUrl");
      localObject3 = ((DPObject)localObject5).getStringArray("Address");
      localObject5 = ((DPObject)localObject5).getStringArray("Name");
      if (!TextUtils.isEmpty((CharSequence)localObject4))
        ((DPNetworkImageView)localObject1).setImage((String)localObject4);
      if (localObject5 != null)
      {
        i = 0;
        while ((i < localObject5.length) && (i < 3))
        {
          if (!TextUtils.isEmpty(localObject5[i]))
          {
            ((TextView)((ArrayList)localObject2).get(i)).setText(localObject5[i]);
            ((TextView)((ArrayList)localObject2).get(i)).setVisibility(0);
          }
          i += 1;
        }
      }
      localObject1 = new ArrayList();
      ((ArrayList)localObject1).add(localTextView1);
      ((ArrayList)localObject1).add(localTextView2);
      ((ArrayList)localObject1).add(localTextView3);
      if (localObject3 == null)
        continue;
      int i = 0;
      while ((i < localObject3.length) && (i < 3))
      {
        if (!TextUtils.isEmpty(localObject3[i]))
        {
          ((TextView)((ArrayList)localObject1).get(i)).setText(localObject3[i]);
          ((TextView)((ArrayList)localObject1).get(i)).setVisibility(0);
        }
        i += 1;
      }
    }
  }

  public void addTitleBarShadow()
  {
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  public void onClick(View paramView)
  {
    finish();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getWindow().setFlags(1024, 1024);
    setContentView(R.layout.waycard_layout);
    init();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.activity.WayCardActivity
 * JD-Core Version:    0.6.0
 */