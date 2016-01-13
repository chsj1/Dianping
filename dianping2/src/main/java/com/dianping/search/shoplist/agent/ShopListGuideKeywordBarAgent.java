package com.dianping.search.shoplist.agent;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.dianping.base.app.loader.AgentMessage;
import com.dianping.base.shoplist.agent.ShopListHeaderAgent;
import com.dianping.base.shoplist.data.model.ShopDisplayTagModel;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.imagemanager.DPNetworkImageView.ImageProcessor;
import com.dianping.loader.MyResources;
import com.dianping.search.shoplist.data.NewShopListDataSource;
import com.dianping.search.util.CustomGradientBitmap;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.pulltorefresh.PullToRefreshListView;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ShopListGuideKeywordBarAgent extends ShopListHeaderAgent
{
  protected static final String CELL_UIDE_WORD = "045GuideBar";
  public static final String GUIDE_KEYWORD_BAR = "shoplist/guidekeywordbar";
  private NewShopListDataSource dataSource;
  Map<String, RelativeLayout> imageViewMap = new HashMap();
  protected boolean isRequestFinish = false;
  String mGuideKeyWords;
  ArrayList<ShopDisplayTagModel> mGuideKeywoGrdsModels;
  protected NovaLinearLayout mGuideWordBar;
  protected HorizontalScrollView mGuideWordBarContainer;
  String mSearchKeyword;
  protected View.OnClickListener tagClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = (String)paramView.getTag();
      StringBuilder localStringBuilder = new StringBuilder(ShopListGuideKeywordBarAgent.this.mSearchKeyword);
      localStringBuilder.append(" ");
      localStringBuilder.append(paramView);
      if (!localStringBuilder.toString().equals(""))
      {
        ShopListGuideKeywordBarAgent.this.mGuideWordBarContainer.scrollTo(0, 0);
        ShopListGuideKeywordBarAgent.this.dataSource.setSuggestKeyword(localStringBuilder.toString());
        ShopListGuideKeywordBarAgent.this.dataSource.reset(true);
        ShopListGuideKeywordBarAgent.this.dataSource.reload(false);
        ShopListGuideKeywordBarAgent.this.dataSource.setGuideKeywrodFlag(true);
      }
    }
  };

  public ShopListGuideKeywordBarAgent(Object paramObject)
  {
    super(paramObject);
  }

  private void updateThumbPicGuideword()
  {
    if ((this.mGuideKeywoGrdsModels == null) || (this.mGuideKeywoGrdsModels.size() == 0))
      return;
    this.mGuideWordBar.removeAllViews();
    int i = 0;
    int j = this.dataSource.guideKeywordType;
    Object localObject = this.mGuideKeywoGrdsModels.iterator();
    if (((Iterator)localObject).hasNext())
    {
      ShopDisplayTagModel localShopDisplayTagModel = (ShopDisplayTagModel)((Iterator)localObject).next();
      switch (j)
      {
      default:
        createTextGuideItem(localShopDisplayTagModel.text, i);
      case 0:
      }
      while (true)
      {
        i += 1;
        break;
        createImageGuideItem(localShopDisplayTagModel.text, localShopDisplayTagModel.iconUrl, i);
      }
    }
    localObject = new GAUserInfo();
    GAHelper.instance().contextStatisticsEvent(getContext(), "guided_search", (GAUserInfo)localObject, "view");
  }

  protected void createGuideWordBar()
  {
    this.mGuideWordBarContainer = new HorizontalScrollView(getContext());
    this.mGuideWordBarContainer.setBackgroundColor(getResources().getColor(R.color.shoplist_search_bar_bg));
    Object localObject = null;
    if ((this.dataSource.guideKeywordType == 1) || (this.dataSource.guideKeywordType == 2))
      localObject = new AbsListView.LayoutParams(-1, getResources().getDimensionPixelOffset(R.dimen.search_guide_keyword_height));
    if (this.dataSource.guideKeywordType == 0)
      localObject = new AbsListView.LayoutParams(-1, getResources().getDimensionPixelOffset(R.dimen.search_guide_keyword_image_height));
    if (localObject == null)
      return;
    this.mGuideWordBarContainer.setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.mGuideWordBarContainer.setHorizontalScrollBarEnabled(false);
    this.mGuideWordBar = new NovaLinearLayout(getContext());
    this.mGuideWordBar.setOrientation(0);
    localObject = new LinearLayout.LayoutParams(-2, -1);
    this.mGuideWordBar.setLayoutParams((ViewGroup.LayoutParams)localObject);
    this.mGuideWordBar.setPadding(getResources().getDimensionPixelOffset(R.dimen.search_guide_keyword_bar_margin_horizontal), getResources().getDimensionPixelOffset(R.dimen.search_guide_keyword_bar_margin_vertical), getResources().getDimensionPixelOffset(R.dimen.search_guide_keyword_bar_margin_horizontal), getResources().getDimensionPixelOffset(R.dimen.search_guide_keyword_bar_margin_vertical));
    this.mGuideWordBarContainer.addView(this.mGuideWordBar);
    updateThumbPicGuideword();
  }

  protected void createImageGuideItem(String paramString1, String paramString2, int paramInt)
  {
    if (TextUtils.isEmpty(paramString1))
      return;
    View localView = LayoutInflater.from(getContext()).inflate(R.layout.search_guide_word_image_view, null);
    Object localObject1 = new StringBuilder(this.mSearchKeyword);
    ((StringBuilder)localObject1).append(" ");
    ((StringBuilder)localObject1).append(paramString1);
    localObject1 = (NovaTextView)localView.findViewById(R.id.search_tv_guide);
    DPNetworkImageView localDPNetworkImageView = (DPNetworkImageView)localView.findViewById(R.id.search_thunm_guide);
    Object localObject2 = new StringBuilder();
    int i;
    if (paramString1.length() > 6)
    {
      ((StringBuilder)localObject2).append(paramString1.substring(0, 5));
      ((StringBuilder)localObject2).append("...");
      ((NovaTextView)localObject1).setText(((StringBuilder)localObject2).toString());
      i = ViewUtils.getViewWidth((View)localObject1) + getResources().getDimensionPixelOffset(R.dimen.search_guide_keyword_img_text_margin);
      ((NovaTextView)localObject1).setSingleLine();
      ((NovaTextView)localObject1).setOnClickListener(this.tagClickListener);
      ((NovaTextView)localObject1).setGravity(17);
      ((NovaTextView)localObject1).setClickable(true);
      ((NovaTextView)localObject1).setTag(paramString1);
      localObject2 = new GAUserInfo();
      ((GAUserInfo)localObject2).index = Integer.valueOf(paramInt);
      ((GAUserInfo)localObject2).keyword = this.mSearchKeyword;
      ((GAUserInfo)localObject2).title = paramString1;
      ((NovaTextView)localObject1).setGAString("guided_search", (GAUserInfo)localObject2);
      localDPNetworkImageView.setImageSize(i, getResources().getDimensionPixelOffset(R.dimen.search_guide_keyword_thumb_height));
      localDPNetworkImageView.setPlaceholderScaleType(ImageView.ScaleType.FIT_XY);
      localDPNetworkImageView.setImageProcessor(new DPNetworkImageView.ImageProcessor()
      {
        public Bitmap doPostProcess(Bitmap paramBitmap)
        {
          int i = CustomGradientBitmap.getGradientMainColor(paramBitmap);
          int k = Color.red(i);
          int m = Color.green(i);
          int n = Color.blue(i);
          int j = Color.argb(200, k, m, n);
          k = Color.argb(80, k, m, n);
          GradientDrawable localGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] { i, j, k });
          i = paramBitmap.getWidth();
          j = paramBitmap.getHeight();
          localGradientDrawable.setBounds(0, 0, i, j);
          Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
          Canvas localCanvas = new Canvas(localBitmap);
          localCanvas.drawBitmap(paramBitmap, 0.0F, 0.0F, null);
          localGradientDrawable.draw(localCanvas);
          return localBitmap;
        }
      });
      if ((TextUtils.isEmpty(paramString2)) || ((!paramString2.startsWith("http://")) && (!paramString2.startsWith("https://"))))
        break label367;
      localDPNetworkImageView.setImage(paramString2);
    }
    while (true)
    {
      this.mGuideWordBar.addView(localView);
      return;
      ((NovaTextView)localObject1).setMinEms(4);
      ((StringBuilder)localObject2).append(paramString1);
      ((NovaTextView)localObject1).setText(((StringBuilder)localObject2).toString());
      if (paramString1.length() > 4)
      {
        i = ViewUtils.getViewWidth((View)localObject1) + getResources().getDimensionPixelOffset(R.dimen.search_guide_keyword_img_text_margin);
        break;
      }
      i = ViewUtils.getViewWidth((View)localObject1);
      break;
      label367: localDPNetworkImageView.setImageBitmap(CustomGradientBitmap.drawableToBitmap(getResources().getDrawable(R.drawable.search_guide_default)));
    }
  }

  protected void createTextGuideItem(String paramString, int paramInt)
  {
    if (TextUtils.isEmpty(paramString))
      return;
    Object localObject1 = new StringBuilder(this.mSearchKeyword);
    ((StringBuilder)localObject1).append(" ");
    ((StringBuilder)localObject1).append(paramString);
    localObject1 = (NovaTextView)LayoutInflater.from(getContext()).inflate(R.layout.search_guide_word_view, null);
    Object localObject2 = new LinearLayout.LayoutParams(-2, getResources().getDimensionPixelOffset(R.dimen.search_guide_keyword_text_height));
    int i = getResources().getDimensionPixelOffset(R.dimen.search_guide_keyword_text_margin);
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramString.length() > 6)
    {
      localStringBuilder.append(paramString.substring(0, 5));
      localStringBuilder.append("...");
    }
    while (true)
    {
      ((LinearLayout.LayoutParams)localObject2).setMargins(i, 0, i, 0);
      ((NovaTextView)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
      ((NovaTextView)localObject1).setText(localStringBuilder.toString());
      ((NovaTextView)localObject1).setSingleLine();
      ((NovaTextView)localObject1).setOnClickListener(this.tagClickListener);
      ((NovaTextView)localObject1).setGravity(17);
      ((NovaTextView)localObject1).setClickable(true);
      ((NovaTextView)localObject1).setTag(paramString);
      localObject2 = new GAUserInfo();
      ((GAUserInfo)localObject2).index = Integer.valueOf(paramInt);
      ((GAUserInfo)localObject2).keyword = this.mSearchKeyword;
      ((GAUserInfo)localObject2).title = paramString;
      ((NovaTextView)localObject1).setGAString("guided_search", (GAUserInfo)localObject2);
      this.mGuideWordBar.addView((View)localObject1);
      return;
      ((NovaTextView)localObject1).setMinEms(4);
      localStringBuilder.append(paramString);
    }
  }

  public void handleMessage(AgentMessage paramAgentMessage)
  {
    super.handleMessage(paramAgentMessage);
    if ("deal_list_data_analized".equals(paramAgentMessage.what))
    {
      this.isRequestFinish = true;
      updateView();
    }
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    this.dataSource = ((NewShopListDataSource)getDataSource());
    if (getActivity() == null);
    while (true)
    {
      do
        return;
      while (this.dataSource == null);
      this.mSearchKeyword = this.dataSource.suggestKeyword();
      if (TextUtils.isEmpty(this.mSearchKeyword))
        continue;
      this.mGuideKeywoGrdsModels = this.dataSource.guideKeywordsModels;
      if ((this.mGuideKeywoGrdsModels != null) && (this.mGuideKeywoGrdsModels.size() != 0))
        break;
      if ((!fetchListView()) || (this.mGuideWordBarContainer == null))
        continue;
      this.listView.removeHeaderView(this.mGuideWordBarContainer);
      return;
    }
    this.mGuideKeyWords = this.dataSource.guideKeywords;
    updateView();
  }

  public void updateView()
  {
    if (fetchListView())
    {
      if (this.mGuideWordBarContainer != null)
        this.listView.removeHeaderView(this.mGuideWordBarContainer);
      createGuideWordBar();
      addListHeader(this.mGuideWordBarContainer);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.search.shoplist.agent.ShopListGuideKeywordBarAgent
 * JD-Core Version:    0.6.0
 */