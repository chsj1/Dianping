package com.dianping.main.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import com.dianping.adapter.BasicRecyclerAdapter.BasicHolder;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.loader.MyResources;
import com.dianping.util.TextUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class SceneModeAdapter extends HomeAgent.HomeAgentAdapter
{
  private static final int BOOKING = 3;
  private static final int MARKET = 1;
  private static final int MOVIE = 2;
  private static final int QUEUE = 4;
  private static final int TAKEAWAY = 5;
  private static final int TITLE_PERIOD = 3500;
  private static final String Tag = "SceneModeAdapter";
  private SceneModeAgent agent;
  private final Handler handler = new Handler();
  private final int[] index = { 1 };
  private boolean isSlided = false;
  private DPObject obj = null;
  private Runnable runnable;
  private int type_nearbyheadlines = 0;
  private int type_scenemode = 1;

  public SceneModeAdapter(HomeAgent paramHomeAgent)
  {
    super(paramHomeAgent);
    this.agent = ((SceneModeAgent)paramHomeAgent);
    this.type_nearbyheadlines += getDefaultType();
    this.type_scenemode += getDefaultType();
  }

  private int getLeftImageResource(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return 0;
    case 1:
      return R.drawable.home_scen_icon_locate;
    case 2:
      return R.drawable.q_movie;
    case 5:
      return R.drawable.q_wai;
    case 3:
      return R.drawable.q_booking;
    case 4:
    }
    return R.drawable.q_pai;
  }

  private void initNearbyHeadlinesView(RecyclerView.ViewHolder paramViewHolder)
  {
    paramViewHolder = (NearbyHeadlinesHolder)paramViewHolder;
    DPObject[] arrayOfDPObject = this.obj.getArray("HeadlineList");
    if ((arrayOfDPObject == null) || (arrayOfDPObject.length < 1))
    {
      paramViewHolder.title.setCurrentText("");
      paramViewHolder.convertview.setOnClickListener(null);
      closeResources();
      Log.i("SceneModeAdapter", "nearbyheadlines, no titles, return");
      return;
    }
    paramViewHolder.title.setCurrentText(arrayOfDPObject[0].getString("Title"));
    paramViewHolder.convertview.gaUserInfo.biz_id = arrayOfDPObject[0].getString("BizId");
    paramViewHolder.convertview.setGAString(arrayOfDPObject[0].getString("GaLabel"));
    paramViewHolder.convertview.setOnClickListener(new View.OnClickListener(arrayOfDPObject)
    {
      public void onClick(View paramView)
      {
        SceneModeAdapter.this.agent.startActivity(this.val$headlines[0].getString("Schema"));
      }
    });
    if ((arrayOfDPObject.length > 1) && (!this.isSlided))
    {
      this.index[0] = 1;
      this.handler.removeCallbacks(this.runnable);
      this.runnable = new Runnable(arrayOfDPObject, paramViewHolder)
      {
        public void run()
        {
          Object localObject = this.val$headlines;
          int[] arrayOfInt = SceneModeAdapter.this.index;
          int i = arrayOfInt[0];
          arrayOfInt[0] = (i + 1);
          localObject = localObject[(i % this.val$headlines.length)];
          this.val$headlinesHolder.title.setText(((DPObject)localObject).getString("Title"));
          this.val$headlinesHolder.convertview.gaUserInfo.biz_id = ((DPObject)localObject).getString("BizId");
          this.val$headlinesHolder.convertview.setGAString(((DPObject)localObject).getString("GaLabel"));
          this.val$headlinesHolder.convertview.setOnClickListener(new View.OnClickListener((DPObject)localObject)
          {
            public void onClick(View paramView)
            {
              SceneModeAdapter.this.agent.startActivity(this.val$headline.getString("Schema"));
            }
          });
          SceneModeAdapter.this.handler.postDelayed(this, 3500L);
        }
      };
      this.handler.postDelayed(this.runnable, 3500L);
      this.isSlided = true;
    }
    while (true)
    {
      Log.i("SceneModeAdapter", "headlines:" + arrayOfDPObject.length);
      return;
      if (arrayOfDPObject.length != 1)
        continue;
      closeResources();
    }
  }

  private void initSceneModeView(RecyclerView.ViewHolder paramViewHolder)
  {
    SceneModeHolder localSceneModeHolder = (SceneModeHolder)paramViewHolder;
    localSceneModeHolder.title.setText("");
    String[] arrayOfString = this.obj.getStringArray("SubTitle");
    if (TextUtils.isEmpty(this.obj.getString("MainTitle")))
    {
      Log.i("SceneModeAdapter", "scenemode, no maintitle, return");
      return;
    }
    int i;
    if ((arrayOfString != null) && (arrayOfString.length > 0))
      i = 0;
    while (true)
    {
      if (i < arrayOfString.length)
        while (true)
        {
          NetworkImageView localNetworkImageView;
          try
          {
            paramViewHolder = JSONValue.parse(arrayOfString[i]);
            if (!(paramViewHolder instanceof JSONObject))
              break;
            paramViewHolder = (JSONObject)paramViewHolder;
            Object localObject1 = paramViewHolder.get("icon");
            Object localObject2 = paramViewHolder.get("jsontext");
            paramViewHolder = (LinearLayout)localSceneModeHolder.subTitleContainer.getChildAt(i);
            if (paramViewHolder != null)
              continue;
            paramViewHolder = (LinearLayout)this.agent.res.inflate(this.agent.getContext(), R.layout.scene_subtitle, localSceneModeHolder.subTitleContainer, false);
            localSceneModeHolder.subTitleContainer.addView(paramViewHolder);
            localNetworkImageView = (NetworkImageView)paramViewHolder.findViewById(R.id.icon);
            if ((localObject1 instanceof String))
            {
              localNetworkImageView.setVisibility(0);
              localNetworkImageView.setImage((String)localObject1);
              paramViewHolder = (TextView)paramViewHolder.findViewById(R.id.textview);
              if (!(localObject2 instanceof String))
                break;
              if (i != 0)
                break label259;
              paramViewHolder.setSingleLine(true);
              TextUtils.setJsonText((String)localObject2, paramViewHolder);
              break;
              paramViewHolder.setVisibility(0);
              continue;
            }
          }
          catch (Exception paramViewHolder)
          {
            paramViewHolder.printStackTrace();
          }
          localNetworkImageView.setVisibility(4);
          continue;
          label259: paramViewHolder.setSingleLine(false);
        }
      i = localSceneModeHolder.subTitleContainer.getChildCount() - 1;
      while (i > arrayOfString.length - 1)
      {
        localSceneModeHolder.subTitleContainer.getChildAt(i).setVisibility(8);
        i -= 1;
      }
      TextUtils.setJsonText(this.obj.getString("MainTitle"), localSceneModeHolder.title);
      if (!TextUtils.isEmpty(this.obj.getString("PicUrl")))
        localSceneModeHolder.image.setImage(this.obj.getString("PicUrl"));
      i = getLeftImageResource(this.obj.getInt("TagType"));
      if (i > 0)
        if (i == R.drawable.home_scen_icon_locate)
        {
          localSceneModeHolder.leftTopTag.setVisibility(8);
          localSceneModeHolder.leftTopLoc.setVisibility(0);
          localSceneModeHolder.leftTopLoc.setImageResource(i);
          if (TextUtils.isEmpty(this.obj.getString("TimeInfo")))
            break label676;
          localSceneModeHolder.sideTimeLayout.setVisibility(0);
          localSceneModeHolder.arrow.setVisibility(8);
          TextUtils.setJsonText(this.obj.getString("TimeInfo"), localSceneModeHolder.timeText);
        }
      while (true)
      {
        if (!TextUtils.isEmpty(this.obj.getString("DetailInfoSchema")))
          localSceneModeHolder.convertView.setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramView)
            {
              paramView = new Intent("android.intent.action.VIEW", Uri.parse(SceneModeAdapter.this.obj.getString("DetailInfoSchema")));
              try
              {
                SceneModeAdapter.this.agent.startActivity(paramView);
                return;
              }
              catch (Exception paramView)
              {
              }
            }
          });
        localSceneModeHolder.convertView.setGAString("perception");
        localSceneModeHolder.convertView.gaUserInfo.biz_id = this.obj.getString("BizID");
        ((DPActivity)this.agent.getContext()).addGAView(localSceneModeHolder.convertView, 0);
        Log.i("SceneModeAdapter", "getView subtitle=" + null + ",title=" + this.obj.getString("MainTitle") + ",DetailInfoSchema=" + this.obj.getString("DetailInfoSchema") + ",PicUrl=" + this.obj.getString("PicUrl"));
        return;
        localSceneModeHolder.leftTopTag.setVisibility(0);
        localSceneModeHolder.leftTopLoc.setVisibility(8);
        localSceneModeHolder.leftTopTag.setImageResource(i);
        break;
        localSceneModeHolder.leftTopTag.setVisibility(8);
        localSceneModeHolder.leftTopLoc.setVisibility(8);
        break;
        label676: localSceneModeHolder.sideTimeLayout.setVisibility(8);
        localSceneModeHolder.arrow.setVisibility(0);
      }
      i += 1;
    }
  }

  public void closeResources()
  {
    if (this.runnable != null)
    {
      this.handler.removeCallbacks(this.runnable);
      this.runnable = null;
    }
    this.isSlided = true;
  }

  public int getCount()
  {
    if (this.obj != null)
      return 1;
    return 0;
  }

  public int getItemViewType(int paramInt)
  {
    if ((this.obj != null) && (!TextUtils.isEmpty(this.obj.getString("MainTitle"))))
    {
      if (paramInt == 0)
        return this.type_scenemode;
      return this.type_nearbyheadlines;
    }
    if (paramInt == 0)
      return this.type_nearbyheadlines;
    return this.type_scenemode;
  }

  public int getViewTypeCount()
  {
    SceneModeAgent localSceneModeAgent = this.agent;
    return SceneModeAgent.adapterTypeCount;
  }

  public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder().append("SceneModePosition：").append(paramInt).append(",holder:");
    String str;
    if (paramViewHolder == null)
    {
      str = "null";
      Log.i("SceneModeAdapter", str);
      if (!(paramViewHolder instanceof NearbyHeadlinesHolder))
        break label72;
      initNearbyHeadlinesView(paramViewHolder);
    }
    label72: 
    do
    {
      return;
      str = paramViewHolder.getClass().getSimpleName();
      break;
    }
    while (!(paramViewHolder instanceof SceneModeHolder));
    initSceneModeView(paramViewHolder);
  }

  public BasicRecyclerAdapter.BasicHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
  {
    Log.i("SceneModeAdapter", "viewType:" + paramInt);
    if (this.type_nearbyheadlines == paramInt)
      return new NearbyHeadlinesHolder(this.agent.res.inflate(this.agent.getContext(), R.layout.home_nearbyheadlines_item, paramViewGroup, false));
    return new SceneModeHolder(this.agent.res.inflate(this.agent.getContext(), R.layout.home_scenemode_item, paramViewGroup, false));
  }

  public void setContent(DPObject paramDPObject)
  {
    DPObject localDPObject = paramDPObject;
    if (paramDPObject != null)
    {
      String str = paramDPObject.getString("MainTitle");
      DPObject[] arrayOfDPObject = paramDPObject.getArray("HeadlineList");
      localDPObject = paramDPObject;
      if (TextUtils.isEmpty(str))
        if (arrayOfDPObject != null)
        {
          localDPObject = paramDPObject;
          if (arrayOfDPObject.length >= 1);
        }
        else
        {
          closeResources();
          localDPObject = null;
        }
    }
    this.isSlided = false;
    this.obj = localDPObject;
  }

  class NearbyHeadlinesHolder extends BasicRecyclerAdapter.BasicHolder
  {
    ImageView arrow;
    NovaRelativeLayout convertview;
    ImageView image;
    TextSwitcher title;

    public NearbyHeadlinesHolder(View arg2)
    {
      super(localView);
      this.convertview = ((NovaRelativeLayout)localView);
      this.image = ((ImageView)localView.findViewById(R.id.nearby_headlines_img));
      this.arrow = ((ImageView)localView.findViewById(R.id.nearby_headlines_arrow));
      this.title = ((TextSwitcher)localView.findViewById(R.id.nearby_headlines_title));
    }
  }

  class SceneModeHolder extends BasicRecyclerAdapter.BasicHolder
  {
    ImageView arrow;
    NovaLinearLayout convertView;
    NetworkImageView image;
    ImageView leftTopLoc;
    ImageView leftTopTag;
    LinearLayout sideTimeLayout;
    LinearLayout subTitleContainer;
    TextView timeText;
    TextView title;

    public SceneModeHolder(View arg2)
    {
      super(localView);
      this.convertView = ((NovaLinearLayout)localView);
      this.subTitleContainer = ((LinearLayout)localView.findViewById(R.id.subtitle_container));
      this.title = ((TextView)localView.findViewById(R.id.title));
      this.image = ((NetworkImageView)localView.findViewById(R.id.image));
      this.leftTopTag = ((ImageView)localView.findViewById(R.id.tag));
      this.leftTopLoc = ((ImageView)localView.findViewById(R.id.loc));
      this.timeText = ((TextView)localView.findViewById(R.id.time_info));
      this.sideTimeLayout = ((LinearLayout)localView.findViewById(R.id.sideinfo_content));
      this.arrow = ((ImageView)localView.findViewById(R.id.indicator));
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.SceneModeAdapter
 * JD-Core Version:    0.6.0
 */