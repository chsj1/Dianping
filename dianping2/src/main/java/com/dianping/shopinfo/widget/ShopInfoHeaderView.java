package com.dianping.shopinfo.widget;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ShopPower;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;

public class ShopInfoHeaderView extends NovaLinearLayout
{
  protected NetworkImageView icon;
  private View iconLayout;
  protected TextView imgCount;
  protected TextView imgCountZero;
  protected boolean isTourShopInfoHeader = false;
  protected TextView name;
  protected View.OnClickListener onIconClickListener;
  protected TextView oriName;
  protected ShopPower power;
  protected TextView price;
  protected TextView rateSource;
  private View score_text_layout;
  protected TextView voteCount;

  public ShopInfoHeaderView(Context paramContext)
  {
    this(paramContext, null);
  }

  public ShopInfoHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private ArrayList<String> scoreTextArray(String paramString)
  {
    if (paramString == null)
    {
      paramString = null;
      return paramString;
    }
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    int j = 0;
    for (String str = paramString; ; str = str.substring(i + 1))
    {
      paramString = localArrayList;
      if (i == -1)
        break;
      if (i != 0)
        j = str.indexOf(" ") + 1;
      i = str.indexOf(":");
      paramString = localArrayList;
      if (i == -1)
        break;
      localArrayList.add(str.substring(j, i));
    }
  }

  private void setDetailInfo(DPObject paramDPObject)
  {
    setScoreInfo(paramDPObject);
  }

  protected String getFullName(DPObject paramDPObject)
  {
    Object localObject = paramDPObject.getString("Name");
    paramDPObject = paramDPObject.getString("BranchName");
    if (TextUtils.isEmpty((CharSequence)localObject))
      return "";
    localObject = new StringBuilder().append((String)localObject);
    if (TextUtils.isEmpty(paramDPObject));
    for (paramDPObject = ""; ; paramDPObject = "(" + paramDPObject + ")")
      return paramDPObject;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.name = ((TextView)findViewById(R.id.title_shop_name));
    this.oriName = ((TextView)findViewById(R.id.ori_name));
    this.iconLayout = findViewById(R.id.photogallery_img_icon_frame);
    this.score_text_layout = findViewById(R.id.score_text_layout);
    this.icon = ((NetworkImageView)findViewById(R.id.shop_panel_collect_icon));
    this.imgCount = ((TextView)findViewById(R.id.imgCount));
    this.imgCountZero = ((TextView)findViewById(R.id.imgCountZero));
    this.power = ((ShopPower)findViewById(R.id.shop_power));
    this.price = ((TextView)findViewById(R.id.text2_renjun_value));
    this.voteCount = ((TextView)findViewById(R.id.text2_vote_total));
    this.rateSource = ((TextView)findViewById(R.id.text_rate_source));
    Context localContext = GAHelper.instance().getDpActivity(getContext());
    if (((localContext instanceof DPActivity)) && (this.icon != null))
      this.icon.setGAString("viewphoto", ((DPActivity)localContext).getCloneUserInfo());
  }

  protected void setBaseInfo(DPObject paramDPObject)
  {
    this.name.setText(getFullName(paramDPObject));
    if ((this.oriName != null) && (!TextUtils.isEmpty(paramDPObject.getString("OriName"))))
    {
      this.oriName.setVisibility(0);
      this.oriName.setText(paramDPObject.getString("OriName"));
    }
    Object localObject;
    do
      while (true)
      {
        setIconImage(paramDPObject);
        setPrice(paramDPObject);
        this.power.setPower(paramDPObject.getInt("ShopPower"));
        if (paramDPObject.getInt("ShopPower") == 0)
        {
          localObject = findViewById(R.id.text1_layout);
          if (localObject != null)
            ((View)localObject).setVisibility(8);
          localObject = findViewById(R.id.defaulttext_lay1);
          if (localObject != null)
            ((View)localObject).setVisibility(0);
          localObject = findViewById(R.id.text2_layout);
          if (localObject != null)
            ((View)localObject).setVisibility(8);
        }
        localObject = (TextView)findViewById(R.id.rank_site);
        if ((localObject == null) || (TextUtils.isEmpty(paramDPObject.getString("StarGrade"))))
          break;
        ((TextView)localObject).setVisibility(0);
        ((TextView)localObject).setText(paramDPObject.getString("StarGrade"));
        return;
        if (this.oriName == null)
          continue;
        this.oriName.setVisibility(8);
      }
    while (localObject == null);
    ((TextView)localObject).setVisibility(8);
  }

  public void setIconClickListen(View.OnClickListener paramOnClickListener)
  {
    this.onIconClickListener = paramOnClickListener;
    if (this.icon != null)
      this.icon.setOnClickListener(this.onIconClickListener);
  }

  protected void setIconImage(DPObject paramDPObject)
  {
    if (paramDPObject.getBoolean("HasMultiPic"))
    {
      if (this.iconLayout != null)
        this.iconLayout.setVisibility(8);
      paramDPObject = (LinearLayout)findViewById(R.id.shop_detail_frame);
      if (paramDPObject != null)
        paramDPObject.setPadding(0, 0, 0, 0);
    }
    while (true)
    {
      return;
      if ((paramDPObject.contains("PicCount")) && (paramDPObject.getInt("PicCount") == 0) && (TextUtils.isEmpty(paramDPObject.getString("DefaultPic"))))
        if (this.icon != null)
        {
          MyResources localMyResources = MyResources.getResource(ShopInfoHeaderView.class);
          this.icon.setBackgroundResource(R.color.gray_light_background);
          this.icon.setLocalBitmap(BitmapFactory.decodeResource(localMyResources.getResources(), R.drawable.placeholder_default));
          this.icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
          if (this.imgCountZero != null)
          {
            this.imgCountZero.setVisibility(0);
            this.imgCountZero.setText("上传第1张图片");
          }
        }
      while (this.imgCount != null)
      {
        if (paramDPObject.getInt("PicCount") == 0)
          break label249;
        this.imgCount.setVisibility(0);
        this.imgCount.setText("" + paramDPObject.getInt("PicCount"));
        return;
        if (this.icon == null)
          continue;
        this.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.icon.setImage(paramDPObject.getString("DefaultPic"));
      }
    }
    label249: this.imgCount.setVisibility(8);
  }

  protected void setPrice(DPObject paramDPObject)
  {
    if (this.price != null)
    {
      if (!TextUtils.isEmpty(paramDPObject.getString("PriceText")))
        this.price.setText(paramDPObject.getString("PriceText"));
    }
    else
    {
      if ((this.voteCount != null) && (!paramDPObject.getBoolean("IsForeignShop")))
      {
        if (paramDPObject.getInt("VoteTotal") != 0)
          break label134;
        this.voteCount.setVisibility(4);
      }
      return;
    }
    TextView localTextView = this.price;
    StringBuilder localStringBuilder = new StringBuilder().append("￥");
    if (paramDPObject.getInt("AvgPrice") > 0);
    for (String str = Integer.toString(paramDPObject.getInt("AvgPrice")); ; str = "-")
    {
      localTextView.setText(str);
      break;
    }
    label134: this.voteCount.setVisibility(0);
    this.voteCount.setText(paramDPObject.getInt("VoteTotal") + "条");
  }

  protected void setScoreInfo(DPObject paramDPObject)
  {
    Object localObject2 = scoreTextArray(paramDPObject.getString("ScoreText"));
    TextView localTextView;
    Object localObject1;
    if ((localObject2 != null) && (((ArrayList)localObject2).size() > 0))
    {
      if (this.score_text_layout != null)
        this.score_text_layout.setVisibility(0);
      ((TextView)findViewById(R.id.text2_shop_score1)).setText((String)((ArrayList)localObject2).get(0) + ":");
      localTextView = (TextView)findViewById(R.id.text2_shop_score1_value);
      localObject1 = paramDPObject.getString("ScoreEx1");
      if (!TextUtils.isEmpty((CharSequence)localObject1))
      {
        localTextView.setText((CharSequence)localObject1);
        if (((ArrayList)localObject2).size() > 0)
        {
          ((TextView)findViewById(R.id.text2_shop_score2)).setText((String)((ArrayList)localObject2).get(1) + ":");
          localTextView = (TextView)findViewById(R.id.text2_shop_score2_value);
          localObject1 = paramDPObject.getString("ScoreEx2");
          if (TextUtils.isEmpty((CharSequence)localObject1))
            break label305;
          localTextView.setText((CharSequence)localObject1);
        }
        if (((ArrayList)localObject2).size() > 1)
        {
          ((TextView)findViewById(R.id.text2_shop_score3)).setText((String)((ArrayList)localObject2).get(2) + ":");
          localObject1 = (TextView)findViewById(R.id.text2_shop_score3_value);
          localObject2 = paramDPObject.getString("ScoreEx3");
          if (TextUtils.isEmpty((CharSequence)localObject2))
            break label342;
          ((TextView)localObject1).setText((CharSequence)localObject2);
        }
      }
    }
    label305: label342: 
    do
    {
      return;
      if (paramDPObject.getInt("Score1") > 0);
      for (localObject1 = Integer.toString(paramDPObject.getInt("Score1")); ; localObject1 = "-")
      {
        localTextView.setText((CharSequence)localObject1);
        break;
      }
      if (paramDPObject.getInt("Score2") > 0);
      for (localObject1 = Integer.toString(paramDPObject.getInt("Score2")); ; localObject1 = "-")
      {
        localTextView.setText((CharSequence)localObject1);
        break;
      }
      if (paramDPObject.getInt("Score3") > 0);
      for (paramDPObject = Integer.toString(paramDPObject.getInt("Score3")); ; paramDPObject = "-")
      {
        ((TextView)localObject1).setText(paramDPObject);
        return;
      }
    }
    while (this.score_text_layout == null);
    this.score_text_layout.setVisibility(8);
  }

  protected void setScoreSourceInfo(DPObject paramDPObject)
  {
    if ((!paramDPObject.getBoolean("IsRateFromDP")) && (!this.isTourShopInfoHeader) && (this.rateSource != null))
      this.rateSource.setVisibility(0);
  }

  public void setShop(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    setBaseInfo(paramDPObject);
    setDetailInfo(paramDPObject);
  }

  public void setShop(DPObject paramDPObject, int paramInt)
  {
    setShop(paramDPObject);
    setScoreSourceInfo(paramDPObject);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.widget.ShopInfoHeaderView
 * JD-Core Version:    0.6.0
 */