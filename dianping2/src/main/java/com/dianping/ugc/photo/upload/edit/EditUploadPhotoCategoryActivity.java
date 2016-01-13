package com.dianping.ugc.photo.upload.edit;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.dianping.adapter.BasicLoadAdapter;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.CustomEditText;
import com.dianping.base.widget.TitleBar;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.model.Location;
import com.dianping.ugc.model.UploadPhotoData;
import com.dianping.ugc.model.UploadPhotoTagData;
import com.dianping.ugc.photo.upload.edit.utils.SelectCategoryPicUtils;
import com.dianping.util.BitmapUtils;
import com.dianping.util.TextUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.v1.R.string;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaListView;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

public class EditUploadPhotoCategoryActivity extends NovaActivity
  implements View.OnClickListener, RequestHandler<MApiRequest, MApiResponse>
{
  private static final String INTERESTTAG = "兴趣标签";
  private static final String POSITION = "地点";
  private MApiRequest commonTagsRequest;
  private NovaListView communityList;
  private LinearLayout dishLayout;
  private EditText dishName;
  private EditText dishPrice;
  private boolean enablePoi;
  private boolean enableTag;
  private View endView;
  private TextView headerText;
  private View headerView;
  private int index;
  private boolean isClickable = true;
  private boolean isNewAddAciton = true;
  private boolean isPoiTextFinished = false;
  private boolean isPoiTextUnInput = true;
  private boolean isSearch = false;
  private boolean isTag = false;
  private boolean isTagTextUnInput = true;
  private String keyWord;
  private CommunityPhotoAdapter mCPAdapter;
  private int mOrderId;
  private UploadPhotoData mPhotoData;
  private UploadPhotoTagData mPhotoTagData;
  private FrameLayout mSelectLayout;
  private View mSelectMain;
  private int mShopId;
  private EditText otherPhotoTitleText;
  private DPObject[] recommendDishList;
  private ArrayList<String> styleToSelect = new ArrayList();

  private int[] computeEndViewPosition()
  {
    int[] arrayOfInt = new int[2];
    this.endView = findViewById(R.id.photo_editcategory_title_mid);
    this.endView.getLocationOnScreen(arrayOfInt);
    return arrayOfInt;
  }

  private String correctPrice(String paramString)
  {
    int i = paramString.indexOf(".");
    String str = paramString;
    if (i != -1)
    {
      str = paramString;
      if (paramString.length() - i - 1 == 0)
        str = paramString.substring(0, i);
    }
    return str;
  }

  private void directToDetailEditView()
  {
    this.isNewAddAciton = false;
    this.mSelectMain.setVisibility(8);
    this.mSelectLayout.setOnClickListener(null);
    findViewById(R.id.ugc_photo_editcategory_title).setVisibility(0);
    findViewById(R.id.photo_editcategory_title_mid).setVisibility(0);
    DPNetworkImageView localDPNetworkImageView = (DPNetworkImageView)findViewById(R.id.photo_editcategory_title_mid);
    Object localObject2 = null;
    Object localObject1 = null;
    CustomEditText localCustomEditText = null;
    int i = this.mPhotoTagData.tagType;
    UploadPhotoTagData localUploadPhotoTagData = this.mPhotoTagData;
    if (i == 1)
    {
      localObject1 = "兴趣标签";
      setImageIconInCycle(localDPNetworkImageView, "兴趣标签");
      localObject2 = findViewById(R.id.ugc_photo_edit_detail_tag);
      localCustomEditText = (CustomEditText)((View)localObject2).findViewById(R.id.ugc_photo_edit_tag_tip);
      showInterestView((View)localObject2, "兴趣标签");
    }
    while (true)
    {
      if (localObject2 != null)
      {
        ((View)localObject2).setVisibility(0);
        ((View)localObject2).setTag(localObject1);
        setCancelAndFinish((View)localObject2, false);
        AnimationManager.generateAlphaAnimation((View)localObject2, 0.0F, 1.0F, 500);
        if (localCustomEditText != null)
        {
          localObject1 = localCustomEditText.mEdit;
          if (localObject1 != null)
          {
            localObject2 = ((EditText)localObject1).getText().toString();
            if (!TextUtils.isEmpty((CharSequence)localObject2))
              ((EditText)localObject1).setSelection(((String)localObject2).length());
          }
        }
      }
      return;
      i = this.mPhotoTagData.tagType;
      localUploadPhotoTagData = this.mPhotoTagData;
      if (i != 2)
        continue;
      localObject1 = "地点";
      setImageIconInCycle(localDPNetworkImageView, "地点");
      localObject2 = findViewById(R.id.ugc_photo_edit_detail_poi);
      localCustomEditText = (CustomEditText)((View)localObject2).findViewById(R.id.ugc_photo_edit_poi_tip);
      showPoiView((View)localObject2, "地点");
    }
  }

  private void generateRecommendDishView(LinearLayout paramLinearLayout)
  {
    LinearLayout.LayoutParams localLayoutParams1 = new LinearLayout.LayoutParams(-2, -2);
    localLayoutParams1.setMargins(0, 0, ViewUtils.dip2px(this, 10.0F), ViewUtils.dip2px(this, 10.0F));
    Object localObject1 = null;
    LinearLayout.LayoutParams localLayoutParams2 = new LinearLayout.LayoutParams(-1, -1);
    localLayoutParams2.setMargins(0, 0, ViewUtils.dip2px(this, 5.0F), 0);
    int m = ViewUtils.dip2px(this, 5.0F);
    int n = View.MeasureSpec.makeMeasureSpec(0, 0);
    int i = 0;
    label111: Object localObject2;
    if ((this.recommendDishList != null) && (this.recommendDishList.length > 0))
    {
      int j;
      int k;
      int i1;
      if (this.recommendDishList.length > 9)
      {
        j = 9;
        k = 0;
        if (k >= j)
          return;
        String str = this.recommendDishList[k].getString("Name");
        localObject2 = new TextView(this);
        ((TextView)localObject2).setTag(str);
        ((TextView)localObject2).setTextColor(getResources().getColor(R.color.white));
        ((TextView)localObject2).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
        ((TextView)localObject2).setBackgroundDrawable(getResources().getDrawable(R.drawable.ugc_photo_edit_recommend_dish_rect));
        ((TextView)localObject2).setText(str);
        ((TextView)localObject2).setOnClickListener(new View.OnClickListener(str)
        {
          public void onClick(View paramView)
          {
            EditUploadPhotoCategoryActivity.this.dishName.setText(this.val$recommendDishName);
          }
        });
        i1 = getViewWidth((View)localObject2, n);
        if (i >= i1 + m)
          break label330;
        if (localObject1 != null)
          paramLinearLayout.addView((View)localObject1, localLayoutParams1);
        localObject1 = new LinearLayout(this);
        ((LinearLayout)localObject1).addView((View)localObject2, localLayoutParams2);
      }
      for (i = getWindowManager().getDefaultDisplay().getWidth() - ViewUtils.dip2px(this, 15.0F) * 2 - i1 - m; ; i = i - i1 - m)
      {
        if (k == j - 1)
          paramLinearLayout.addView((View)localObject1, localLayoutParams1);
        k += 1;
        break label111;
        j = this.recommendDishList.length;
        break;
        label330: ((LinearLayout)localObject1).addView((View)localObject2, localLayoutParams2);
      }
    }
    else
    {
      localLayoutParams2.gravity = 1;
      localObject1 = new TextView(this);
      ((TextView)localObject1).setTextColor(getResources().getColor(R.color.white));
      ((TextView)localObject1).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
      ((TextView)localObject1).setText(R.string.ugc_photo_edit_first_dish);
      localObject2 = new LinearLayout(this);
      ((LinearLayout)localObject2).addView((View)localObject1, localLayoutParams2);
      paramLinearLayout.addView((View)localObject2, localLayoutParams1);
    }
  }

  private NovaLinearLayout generateSelectStyleView(String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    NovaLinearLayout localNovaLinearLayout = new NovaLinearLayout(this);
    localNovaLinearLayout.setGAString("type");
    Object localObject = new LinearLayout.LayoutParams(ViewUtils.dip2px(this, 80.0F), ViewUtils.dip2px(this, 80.0F));
    localNovaLinearLayout.setBackgroundResource(R.drawable.ugc_photo_detail_background_circle);
    localNovaLinearLayout.setLayoutParams((ViewGroup.LayoutParams)localObject);
    localNovaLinearLayout.setGravity(17);
    localNovaLinearLayout.setOrientation(1);
    localNovaLinearLayout.setTag(R.id.ugc_photo_cate_name, paramString);
    localObject = new DPNetworkImageView(this);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(ViewUtils.dip2px(this, 26.0F), ViewUtils.dip2px(this, 26.0F));
    setImageIconInCycle((DPNetworkImageView)localObject, paramString);
    localNovaLinearLayout.addView((View)localObject, localLayoutParams);
    localObject = new TextView(this);
    localLayoutParams = new LinearLayout.LayoutParams(-2, -2);
    ((TextView)localObject).setLayoutParams(localLayoutParams);
    localLayoutParams.topMargin = ViewUtils.dip2px(this, 6.0F);
    ((TextView)localObject).setTag(paramString);
    ((TextView)localObject).setTextColor(getResources().getColor(R.color.white));
    ((TextView)localObject).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_11));
    ((TextView)localObject).setText(paramString);
    localNovaLinearLayout.setOnClickListener(this);
    localNovaLinearLayout.addView((View)localObject, localLayoutParams);
    if (!paramBoolean1)
      if (this.mPhotoData != null)
      {
        if (this.mPhotoData.categoryName != null)
          localNovaLinearLayout.setGAString("editType");
        if (((paramBoolean2) && (this.mPhotoData.poiShopId != null)) || (paramString.equals(this.mPhotoData.categoryName)))
          localNovaLinearLayout.setBackgroundResource(R.drawable.ugc_photo_detail_background_circle_chosen);
        int i = 0;
        if ("兴趣标签".equals(paramString))
        {
          localObject = this.mPhotoData.tags.iterator();
          while (((Iterator)localObject).hasNext())
          {
            if (((UploadPhotoTagData)((Iterator)localObject).next()).tagType != 1)
              continue;
            i += 1;
          }
          if (i >= 10)
          {
            localNovaLinearLayout.setBackgroundResource(R.drawable.ugc_photo_detail_background_circle_unclickable);
            localNovaLinearLayout.setOnClickListener(null);
          }
        }
      }
    while (paramString.equals("地点"))
    {
      localNovaLinearLayout.setTag(R.id.ugc_photo_click_type, "poi");
      return localNovaLinearLayout;
      ((TextView)localObject).setVisibility(8);
    }
    if (paramString.equals("兴趣标签"))
    {
      localNovaLinearLayout.setTag(R.id.ugc_photo_click_type, "interesttag");
      return localNovaLinearLayout;
    }
    if (paramString.equals("菜"))
    {
      localNovaLinearLayout.setTag(R.id.ugc_photo_click_type, "dish");
      return localNovaLinearLayout;
    }
    localNovaLinearLayout.setTag(R.id.ugc_photo_click_type, "others");
    return (NovaLinearLayout)localNovaLinearLayout;
  }

  private int getViewWidth(View paramView, int paramInt)
  {
    paramView.measure(paramInt, paramInt);
    return paramView.getMeasuredWidth();
  }

  private void initMainViews()
  {
    this.mSelectLayout.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        EditUploadPhotoCategoryActivity.this.finish();
      }
    });
    LinearLayout localLinearLayout2 = (LinearLayout)findViewById(R.id.photo_select_catetories);
    if ((this.styleToSelect != null) && (this.styleToSelect.size() > 0))
    {
      LinearLayout localLinearLayout1 = new LinearLayout(this);
      LinearLayout.LayoutParams localLayoutParams1 = new LinearLayout.LayoutParams(-2, -2);
      localLayoutParams1.bottomMargin = ViewUtils.dip2px(this, 15.0F);
      localLayoutParams1.gravity = 1;
      if (this.styleToSelect.size() == 4);
      for (int i = 2; ; i = 3)
      {
        int j = 0;
        while (j < this.styleToSelect.size())
        {
          boolean bool = false;
          if ("地点".equals(this.styleToSelect.get(j)))
            bool = true;
          if (j % i == 0)
          {
            localLinearLayout1 = new LinearLayout(this);
            localLinearLayout1.setGravity(17);
            localLinearLayout1.setOrientation(0);
            localLinearLayout2.addView(localLinearLayout1, localLayoutParams1);
          }
          NovaLinearLayout localNovaLinearLayout = generateSelectStyleView((String)this.styleToSelect.get(j), false, bool);
          LinearLayout.LayoutParams localLayoutParams2 = new LinearLayout.LayoutParams(localNovaLinearLayout.getLayoutParams());
          localLayoutParams2.setMargins(ViewUtils.dip2px(this, 15.0F), 0, ViewUtils.dip2px(this, 15.0F), 0);
          localLinearLayout1.addView(localNovaLinearLayout, localLayoutParams2);
          j += 1;
        }
      }
    }
  }

  private void jumpToDetailPage(View paramView1, View paramView2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    this.mSelectLayout.setOnClickListener(null);
    AnimationManager localAnimationManager = new AnimationManager(this.mSelectMain, 500);
    localAnimationManager.getClass();
    localAnimationManager.setAnimationListener(new AnimationManager.AnimationListener(localAnimationManager, paramView1)
    {
      public void onAnimationEnd(Animation paramAnimation)
      {
        super.onAnimationEnd(paramAnimation);
        EditUploadPhotoCategoryActivity.access$202(EditUploadPhotoCategoryActivity.this, true);
        String str = (String)this.val$clickView.getTag(R.id.ugc_photo_cate_name);
        EditUploadPhotoCategoryActivity.this.findViewById(R.id.ugc_photo_editcategory_title).setVisibility(0);
        paramAnimation = null;
        boolean bool2 = true;
        Object localObject;
        boolean bool1;
        if ("dish".equals(this.val$clickView.getTag(R.id.ugc_photo_click_type)))
        {
          localObject = EditUploadPhotoCategoryActivity.this.findViewById(R.id.photo_editcategory_dish);
          paramAnimation = (CustomEditText)((View)localObject).findViewById(R.id.photo_editcategory_dish_name_edit);
          EditUploadPhotoCategoryActivity.access$102(EditUploadPhotoCategoryActivity.this, paramAnimation.mEdit);
          EditUploadPhotoCategoryActivity.this.dishName.setHint(R.string.ugc_photo_edit_dishname_tip);
          paramAnimation = (CustomEditText)((View)localObject).findViewById(R.id.photo_editcategory_dish_price_edit);
          EditUploadPhotoCategoryActivity.access$302(EditUploadPhotoCategoryActivity.this, paramAnimation.mEdit);
          EditUploadPhotoCategoryActivity.this.dishPrice.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });
          EditUploadPhotoCategoryActivity.this.dishPrice.setHint(R.string.ugc_photo_edit_dishprice_tip);
          EditUploadPhotoCategoryActivity.this.dishPrice.setInputType(8192);
          EditUploadPhotoCategoryActivity.this.dishPrice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ugc_edit_photo_dish_price_icon, 0, 0, 0);
          EditUploadPhotoCategoryActivity.this.dishPrice.setCompoundDrawablePadding(ViewUtils.dip2px(EditUploadPhotoCategoryActivity.this, 5.0F));
          paramAnimation = Pattern.compile("^(?!0[^\\.])\\d+\\.?\\d{0,2}");
          EditUploadPhotoCategoryActivity.this.dishPrice.addTextChangedListener(new EditUploadPhotoCategoryActivity.4.1(this, paramAnimation));
          if ((EditUploadPhotoCategoryActivity.this.mPhotoData != null) && (str.equals(EditUploadPhotoCategoryActivity.this.mPhotoData.categoryName)))
          {
            if (EditUploadPhotoCategoryActivity.this.mPhotoData.title != null)
              EditUploadPhotoCategoryActivity.this.dishName.setText(EditUploadPhotoCategoryActivity.this.mPhotoData.title);
            if (EditUploadPhotoCategoryActivity.this.mPhotoData.price != null)
              EditUploadPhotoCategoryActivity.this.dishPrice.setText(EditUploadPhotoCategoryActivity.this.mPhotoData.price);
          }
          EditUploadPhotoCategoryActivity.this.keybordOut(EditUploadPhotoCategoryActivity.this.dishName);
          EditUploadPhotoCategoryActivity.access$602(EditUploadPhotoCategoryActivity.this, (LinearLayout)((View)localObject).findViewById(R.id.photo_editcategory_dish_layout));
          if (EditUploadPhotoCategoryActivity.this.mShopId == 0)
          {
            paramAnimation = (Animation)localObject;
            bool1 = bool2;
            if (EditUploadPhotoCategoryActivity.this.mOrderId == 0);
          }
          else
          {
            EditUploadPhotoCategoryActivity.this.requestRecommendTag();
            bool1 = bool2;
            paramAnimation = (Animation)localObject;
          }
        }
        while (true)
        {
          if (paramAnimation != null)
          {
            paramAnimation.setVisibility(0);
            paramAnimation.setTag(str);
            EditUploadPhotoCategoryActivity.this.setCancelAndFinish(paramAnimation, bool1);
            AnimationManager.generateAlphaAnimation(paramAnimation, 0.0F, 1.0F, 500);
          }
          return;
          if ("others".equals(this.val$clickView.getTag(R.id.ugc_photo_click_type)))
          {
            paramAnimation = EditUploadPhotoCategoryActivity.this.findViewById(R.id.photo_editcategory_others);
            localObject = (CustomEditText)paramAnimation.findViewById(R.id.photo_editcategory_edit_others);
            EditUploadPhotoCategoryActivity.access$1002(EditUploadPhotoCategoryActivity.this, ((CustomEditText)localObject).mEdit);
            EditUploadPhotoCategoryActivity.this.otherPhotoTitleText.setHint(R.string.ugc_photo_other_hint);
            if ((EditUploadPhotoCategoryActivity.this.mPhotoData != null) && (str.equals(EditUploadPhotoCategoryActivity.this.mPhotoData.categoryName)) && (EditUploadPhotoCategoryActivity.this.mPhotoData.title != null))
              EditUploadPhotoCategoryActivity.this.otherPhotoTitleText.setText(EditUploadPhotoCategoryActivity.this.mPhotoData.title);
            EditUploadPhotoCategoryActivity.this.keybordOut(EditUploadPhotoCategoryActivity.this.otherPhotoTitleText);
            bool1 = bool2;
            continue;
          }
          if ("poi".equals(this.val$clickView.getTag(R.id.ugc_photo_click_type)))
          {
            bool1 = false;
            paramAnimation = EditUploadPhotoCategoryActivity.this.findViewById(R.id.ugc_photo_edit_detail_poi);
            EditUploadPhotoCategoryActivity.this.showPoiView(paramAnimation, str);
            continue;
          }
          bool1 = bool2;
          if (!"interesttag".equals(this.val$clickView.getTag(R.id.ugc_photo_click_type)))
            continue;
          bool1 = false;
          paramAnimation = EditUploadPhotoCategoryActivity.this.findViewById(R.id.ugc_photo_edit_detail_tag);
          EditUploadPhotoCategoryActivity.this.showInterestView(paramAnimation, str);
        }
      }

      public void onAnimationStart(Animation paramAnimation)
      {
        super.onAnimationStart(paramAnimation);
        EditUploadPhotoCategoryActivity.access$202(EditUploadPhotoCategoryActivity.this, false);
      }
    });
    localAnimationManager.selectCategoryAnimation(paramView1, paramView2, this.endView, paramArrayOfInt1, paramArrayOfInt2);
  }

  private void keybordOut(View paramView)
  {
    paramView.setFocusable(true);
    paramView.setFocusableInTouchMode(true);
    paramView.requestFocus();
    ((InputMethodManager)paramView.getContext().getSystemService("input_method")).showSoftInput(paramView, 0);
  }

  private MApiRequest photoPoiRequest(int paramInt, double paramDouble1, double paramDouble2)
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/plaza/searchsuggestpoi.bin").buildUpon();
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    if ((paramDouble1 != 0.0D) && (paramDouble2 != 0.0D))
    {
      localBuilder.appendQueryParameter("lat", String.valueOf(paramDouble1));
      localBuilder.appendQueryParameter("lng", String.valueOf(paramDouble2));
    }
    localBuilder.appendQueryParameter("issearch", String.valueOf(this.isSearch));
    if (this.keyWord != null)
      localBuilder.appendQueryParameter("keyword", this.keyWord);
    localBuilder.appendQueryParameter("start", String.valueOf(paramInt));
    localBuilder.appendQueryParameter("limit", "50");
    return BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
  }

  private MApiRequest photoTagRequest(int paramInt)
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/plaza/searchsuggesttag.bin").buildUpon();
    localBuilder.appendQueryParameter("cityid", String.valueOf(cityId()));
    if (this.keyWord != null)
      localBuilder.appendQueryParameter("keyword", this.keyWord);
    localBuilder.appendQueryParameter("start", String.valueOf(paramInt));
    localBuilder.appendQueryParameter("limit", "50");
    return BasicMApiRequest.mapiGet(localBuilder.build().toString(), CacheType.DISABLED);
  }

  private void requestRecommendTag()
  {
    Uri.Builder localBuilder = Uri.parse("http://m.api.dianping.com/review/commontags.bin").buildUpon();
    localBuilder.appendQueryParameter("shopid", String.valueOf(this.mShopId));
    localBuilder.appendQueryParameter("orderid", String.valueOf(this.mOrderId));
    this.commonTagsRequest = mapiGet(this, localBuilder.build().toString(), CacheType.NORMAL);
    mapiService().exec(this.commonTagsRequest, this);
  }

  private void setCancelAndFinish(View paramView, boolean paramBoolean)
  {
    findViewById(R.id.photo_editcategory_edit_cancel).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        EditUploadPhotoCategoryActivity.this.finish();
      }
    });
    View localView = findViewById(R.id.photo_editcategory_edit_ok);
    if (paramBoolean)
    {
      localView.setOnClickListener(new View.OnClickListener(paramView)
      {
        public void onClick(View paramView)
        {
          paramView.setTag(this.val$view.getTag());
          EditUploadPhotoCategoryActivity.this.setResultForOK(paramView);
        }
      });
      return;
    }
    localView.setVisibility(8);
  }

  private void setCommunityDetailView(EditText paramEditText, String paramString)
  {
    this.communityList.setSelector(17170445);
    this.headerView = LayoutInflater.from(this).inflate(R.layout.ugc_photo_edit_community_head_layout, null, false);
    this.headerView.setOnClickListener(null);
    this.headerText = ((TextView)this.headerView.findViewById(R.id.ugc_photo_community_head_text));
    this.communityList.addHeaderView(this.headerView);
    this.mCPAdapter = new CommunityPhotoAdapter(this);
    this.communityList.setAdapter(this.mCPAdapter);
    this.communityList.setOnScrollListener(new AbsListView.OnScrollListener()
    {
      public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
      {
      }

      public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
      {
        paramAbsListView = (InputMethodManager)EditUploadPhotoCategoryActivity.this.getSystemService("input_method");
        if ((paramAbsListView != null) && (paramInt == 1))
          paramAbsListView.hideSoftInputFromWindow(EditUploadPhotoCategoryActivity.this.getWindow().getDecorView().getWindowToken(), 0);
      }
    });
    paramEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
    {
      public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
      {
        EditUploadPhotoCategoryActivity.access$1402(EditUploadPhotoCategoryActivity.this, true);
        EditUploadPhotoCategoryActivity.access$1502(EditUploadPhotoCategoryActivity.this, true);
        EditUploadPhotoCategoryActivity.this.mCPAdapter.reset();
        return true;
      }
    });
    paramEditText.addTextChangedListener(new UploadPhotoEditTextWatch(paramString)
    {
      public void afterTextChanged(Editable paramEditable)
      {
        super.afterTextChanged(paramEditable);
        EditUploadPhotoCategoryActivity.access$1402(EditUploadPhotoCategoryActivity.this, false);
        EditUploadPhotoCategoryActivity.access$1702(EditUploadPhotoCategoryActivity.this, paramEditable.toString().trim());
        if ((EditUploadPhotoCategoryActivity.this.keyWord != null) && (EditUploadPhotoCategoryActivity.this.keyWord.length() > 0))
          if (EditUploadPhotoCategoryActivity.this.isTag)
          {
            EditUploadPhotoCategoryActivity.access$1902(EditUploadPhotoCategoryActivity.this, false);
            paramEditable = "使用并添加为新标签： # " + EditUploadPhotoCategoryActivity.this.keyWord + " #";
            paramEditable = EditUploadPhotoCategoryActivity.this.setHighLightText(paramEditable);
            EditUploadPhotoCategoryActivity.this.headerText.setText(paramEditable);
            EditUploadPhotoCategoryActivity.this.headerText.setTextSize(0, EditUploadPhotoCategoryActivity.this.getResources().getDimensionPixelSize(R.dimen.text_size_15));
            EditUploadPhotoCategoryActivity.this.headerText.setTextColor(EditUploadPhotoCategoryActivity.this.getResources().getColor(R.color.white));
            EditUploadPhotoCategoryActivity.this.headerView.setOnClickListener(new EditUploadPhotoCategoryActivity.7.1(this));
          }
        while (true)
        {
          EditUploadPhotoCategoryActivity.this.mCPAdapter.reset();
          return;
          EditUploadPhotoCategoryActivity.access$2502(EditUploadPhotoCategoryActivity.this, false);
          EditUploadPhotoCategoryActivity.access$1502(EditUploadPhotoCategoryActivity.this, false);
          EditUploadPhotoCategoryActivity.this.headerView.setVisibility(8);
          continue;
          EditUploadPhotoCategoryActivity.this.headerView.setOnClickListener(null);
          if (EditUploadPhotoCategoryActivity.this.isTag)
          {
            EditUploadPhotoCategoryActivity.access$1902(EditUploadPhotoCategoryActivity.this, true);
            EditUploadPhotoCategoryActivity.this.headerText.setText(R.string.ugc_photo_edit_tag_recomment_tip);
            continue;
          }
          EditUploadPhotoCategoryActivity.access$2502(EditUploadPhotoCategoryActivity.this, true);
          EditUploadPhotoCategoryActivity.this.headerView.setVisibility(0);
          EditUploadPhotoCategoryActivity.this.headerText.setText(R.string.ugc_photo_edit_poi_recommend_tip);
        }
      }
    });
  }

  private SpannableStringBuilder setHighLightText(String paramString)
  {
    int i = paramString.indexOf(this.keyWord);
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(paramString);
    localSpannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, i, 18);
    localSpannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), i, this.keyWord.length() + i, 18);
    localSpannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), this.keyWord.length() + i, paramString.length(), 18);
    return localSpannableStringBuilder;
  }

  private void setImageIconInCycle(DPNetworkImageView paramDPNetworkImageView, String paramString)
  {
    String str = (String)SelectCategoryPicUtils.categoryPicMap.get(paramString);
    paramString = str;
    if (str == null)
      paramString = "其他";
    paramDPNetworkImageView.setImageWithAssetCache("ugc_photo_category_icon/ugc_category_icon_" + paramString + ".png", null, null);
  }

  private void setResultForOK(View paramView)
  {
    Intent localIntent = getIntent();
    if (this.mPhotoData != null)
    {
      if (((paramView.getTag() instanceof String)) && (!this.enablePoi) && (!this.enableTag))
        this.mPhotoData.categoryName = ((String)paramView.getTag());
      if (this.dishPrice != null)
        this.mPhotoData.price = correctPrice(this.dishPrice.getText().toString()).trim();
      if (!"菜".equals(this.mPhotoData.categoryName))
        this.mPhotoData.price = null;
      if (this.dishName != null)
        this.mPhotoData.title = this.dishName.getText().toString().trim();
      if (this.otherPhotoTitleText != null)
        this.mPhotoData.title = this.otherPhotoTitleText.getText().toString().trim();
      localIntent.putExtra("photoData", this.mPhotoData);
    }
    localIntent.putExtra("tagData", this.mPhotoTagData);
    localIntent.putExtra("index", this.index);
    setResult(-1, localIntent);
    finish();
  }

  private void showInterestView(View paramView, String paramString)
  {
    this.isTag = true;
    EditText localEditText = ((CustomEditText)paramView.findViewById(R.id.ugc_photo_edit_tag_tip)).mEdit;
    localEditText.setHint(R.string.ugc_tag_hint);
    localEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
    if ((this.mPhotoTagData != null) && (this.mPhotoTagData.content != null))
      localEditText.setText(this.mPhotoTagData.content);
    this.communityList = ((NovaListView)paramView.findViewById(R.id.ugc_photo_edit_tag_list));
    setCommunityDetailView(localEditText, paramString);
    this.headerText.setText(R.string.ugc_photo_edit_tag_recomment_tip);
    this.communityList.setOnItemClickListener(new AdapterView.OnItemClickListener(paramString)
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        if (EditUploadPhotoCategoryActivity.this.isNewAddAciton)
        {
          if (!EditUploadPhotoCategoryActivity.this.isTagTextUnInput)
            break label129;
          if ((paramView instanceof NovaLinearLayout))
            ((NovaLinearLayout)paramView).setGAString("chooseInterestRecommend");
        }
        while (true)
        {
          paramAdapterView = (TextView)paramView.findViewById(R.id.ugc_photo_tag);
          if (EditUploadPhotoCategoryActivity.this.mPhotoTagData != null)
          {
            paramAdapterView = paramAdapterView.getText().toString();
            EditUploadPhotoCategoryActivity.this.mPhotoTagData.content = paramAdapterView.substring(1, paramAdapterView.length() - 1).trim();
            paramAdapterView = EditUploadPhotoCategoryActivity.this.mPhotoTagData;
            paramAdapterView.setTagType(1);
          }
          paramView.setTag(this.val$cateName);
          EditUploadPhotoCategoryActivity.this.setResultForOK(paramView);
          return;
          label129: if (!(paramView instanceof NovaLinearLayout))
            continue;
          ((NovaLinearLayout)paramView).setGAString("chooseInterestSuggest");
        }
      }
    });
  }

  private void showPoiView(View paramView, String paramString)
  {
    EditText localEditText = ((CustomEditText)paramView.findViewById(R.id.ugc_photo_edit_poi_tip)).mEdit;
    localEditText.setHint(R.string.ugc_poi_hint);
    this.communityList = ((NovaListView)paramView.findViewById(R.id.ugc_photo_edit_poi_list));
    if ((this.mPhotoData != null) && (this.mPhotoData.poiShopName != null))
      localEditText.setText(this.mPhotoData.poiShopName);
    setCommunityDetailView(localEditText, paramString);
    this.headerText.setText(R.string.ugc_photo_edit_poi_recommend_tip);
    this.communityList.setOnItemClickListener(new AdapterView.OnItemClickListener(paramString)
    {
      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
      {
        if (EditUploadPhotoCategoryActivity.this.isNewAddAciton)
        {
          if (!EditUploadPhotoCategoryActivity.this.isPoiTextFinished)
            break label172;
          if ((paramView instanceof NovaLinearLayout))
            ((NovaLinearLayout)paramView).setGAString("choosePoiSearch");
        }
        while (true)
        {
          paramAdapterView = (TextView)paramView.findViewById(R.id.ugc_photo_poi_name);
          if (EditUploadPhotoCategoryActivity.this.mPhotoData != null)
          {
            EditUploadPhotoCategoryActivity.this.mPhotoData.poiShopName = paramAdapterView.getText().toString();
            if ((paramAdapterView.getTag() instanceof Integer))
              EditUploadPhotoCategoryActivity.this.mPhotoData.poiShopId = String.valueOf(paramAdapterView.getTag());
          }
          if (EditUploadPhotoCategoryActivity.this.mPhotoTagData != null)
          {
            UploadPhotoTagData localUploadPhotoTagData = EditUploadPhotoCategoryActivity.this.mPhotoTagData;
            localUploadPhotoTagData.setTagType(2);
            EditUploadPhotoCategoryActivity.this.mPhotoTagData.content = paramAdapterView.getText().toString();
          }
          paramView.setTag(this.val$cateName);
          EditUploadPhotoCategoryActivity.this.setResultForOK(paramView);
          return;
          label172: if (EditUploadPhotoCategoryActivity.this.isPoiTextUnInput)
          {
            if (!(paramView instanceof NovaLinearLayout))
              continue;
            ((NovaLinearLayout)paramView).setGAString("choosePoiRecommend");
            continue;
          }
          if (!(paramView instanceof NovaLinearLayout))
            continue;
          ((NovaLinearLayout)paramView).setGAString("choosePoiSuggest");
        }
      }
    });
  }

  public String getPageName()
  {
    return "editPic";
  }

  protected TitleBar initCustomTitle()
  {
    return TitleBar.build(this, 2);
  }

  protected boolean needTitleBarShadow()
  {
    return false;
  }

  public void onClick(View paramView)
  {
    Object localObject;
    int[] arrayOfInt;
    NovaLinearLayout localNovaLinearLayout;
    if (this.isClickable)
    {
      localObject = new Rect();
      getWindow().getDecorView().getWindowVisibleDisplayFrame((Rect)localObject);
      int i = ((Rect)localObject).top;
      localObject = new int[2];
      paramView.getLocationOnScreen(localObject);
      localObject[1] -= i;
      arrayOfInt = computeEndViewPosition();
      arrayOfInt[1] -= i;
      localNovaLinearLayout = generateSelectStyleView(String.valueOf(paramView.getTag(R.id.ugc_photo_cate_name)), true, false);
      localNovaLinearLayout.setClickable(false);
      if (Build.VERSION.SDK_INT > 10)
        break label163;
      FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(ViewUtils.dip2px(this, 80.0F), ViewUtils.dip2px(this, 80.0F));
      localLayoutParams.gravity = 48;
      localLayoutParams.leftMargin = localObject[0];
      localLayoutParams.topMargin = localObject[1];
      this.mSelectLayout.addView(localNovaLinearLayout, localLayoutParams);
    }
    while (true)
    {
      jumpToDetailPage(paramView, localNovaLinearLayout, localObject, arrayOfInt);
      return;
      label163: localNovaLinearLayout.setX(localObject[0]);
      localNovaLinearLayout.setY(localObject[1]);
      this.mSelectLayout.addView(localNovaLinearLayout);
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.ugc_photo_editcategory);
    this.mSelectLayout = ((FrameLayout)findViewById(R.id.photo_editcategory_select_layout));
    this.mSelectMain = findViewById(R.id.photo_editcategory_select_main);
    this.mShopId = getIntParam("shopId", 0);
    this.mOrderId = getIntParam("orderid", 0);
    this.enablePoi = getBooleanParam("enablePOI", false);
    this.enableTag = getBooleanParam("enableTag", false);
    boolean bool = getBooleanParam("enableCategory", false);
    if (this.enablePoi)
      this.styleToSelect.add("地点");
    if (this.enableTag)
      this.styleToSelect.add("兴趣标签");
    Object localObject;
    if (bool)
    {
      localObject = getStringParam("category").split(",");
      i = 0;
      while (i < localObject.length)
      {
        if ((!"null".equals(localObject[i])) && (!"全部".equals(localObject[i])))
          this.styleToSelect.add(localObject[i]);
        i += 1;
      }
    }
    int i = getIntParam("tagId");
    this.index = getIntParam("index");
    if (paramBundle == null)
    {
      this.mPhotoData = ((UploadPhotoData)getIntent().getParcelableExtra("photoData"));
      if (this.mPhotoData != null)
      {
        paramBundle = this.mPhotoData.tags.iterator();
        while (paramBundle.hasNext())
        {
          localObject = (UploadPhotoTagData)paramBundle.next();
          if (i != ((UploadPhotoTagData)localObject).tagId)
            continue;
          this.mPhotoTagData = ((UploadPhotoTagData)localObject);
        }
        if ((this.mPhotoTagData == null) || (this.mPhotoTagData.content == null))
          break label348;
        directToDetailEditView();
      }
    }
    while (true)
    {
      if (this.mPhotoTagData == null)
        this.mPhotoTagData = new UploadPhotoTagData(0, 0.0D, 0.0D);
      return;
      this.mPhotoData = ((UploadPhotoData)paramBundle.getParcelable("photoData"));
      break;
      label348: initMainViews();
    }
  }

  protected void onDestroy()
  {
    if (this.commonTagsRequest != null)
    {
      mapiService().abort(this.commonTagsRequest, this, true);
      this.commonTagsRequest = null;
    }
    super.onDestroy();
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.commonTagsRequest = null;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    this.commonTagsRequest = null;
    if ((paramMApiResponse.result() instanceof DPObject))
    {
      paramMApiRequest = (DPObject)paramMApiResponse.result();
      if (paramMApiRequest != null)
      {
        this.recommendDishList = paramMApiRequest.getArray("List");
        generateRecommendDishView(this.dishLayout);
      }
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("photoData", this.mPhotoData);
  }

  public void setContentView(int paramInt)
  {
    super.setContentView(paramInt);
    ((FrameLayout)findViewById(16908290)).setBackgroundResource(0);
  }

  class CommunityPhotoAdapter extends BasicLoadAdapter
  {
    public CommunityPhotoAdapter(Context arg2)
    {
      super();
    }

    public void appendData(DPObject paramDPObject)
    {
      if (this.mIsPullToRefresh)
      {
        this.mIsPullToRefresh = false;
        this.mData.clear();
      }
      this.mEmptyMsg = paramDPObject.getString("EmptyMsg");
      this.mIsEnd = paramDPObject.getBoolean("IsEnd");
      this.mNextStartIndex = paramDPObject.getInt("NextStartIndex");
      this.mRecordCount = paramDPObject.getInt("RecordCount");
      this.mQueryId = paramDPObject.getString("QueryID");
      paramDPObject = paramDPObject.getArray("List");
      if (paramDPObject != null)
      {
        appendDataToList(paramDPObject);
        if ((this.mData.size() == 0) && (this.mEmptyMsg == null))
          this.mEmptyMsg = "数据为空";
        if (paramDPObject.length != 0);
      }
      for (this.mIsEnd = true; ; this.mIsEnd = true)
      {
        if (!EditUploadPhotoCategoryActivity.this.isSearch)
          this.mIsEnd = true;
        notifyDataSetChanged();
        return;
      }
    }

    public MApiRequest createRequest(int paramInt)
    {
      if (EditUploadPhotoCategoryActivity.this.isTag)
        return EditUploadPhotoCategoryActivity.this.photoTagRequest(paramInt);
      double[] arrayOfDouble = BitmapUtils.readPictureCoordinate(EditUploadPhotoCategoryActivity.this.mPhotoData.photoPath);
      double d3 = arrayOfDouble[0];
      double d4 = arrayOfDouble[1];
      double d2 = d3;
      double d1 = d4;
      if (d3 == 0.0D)
      {
        d2 = d3;
        d1 = d4;
        if (d4 == 0.0D)
        {
          d2 = d3;
          d1 = d4;
          if (EditUploadPhotoCategoryActivity.this.location() != null)
          {
            d2 = EditUploadPhotoCategoryActivity.this.location().latitude();
            d1 = EditUploadPhotoCategoryActivity.this.location().longitude();
          }
        }
      }
      return EditUploadPhotoCategoryActivity.this.photoPoiRequest(paramInt, d2, d1);
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = super.getView(paramInt, paramView, paramViewGroup);
      paramViewGroup = getItem(paramInt);
      if ((paramViewGroup == ERROR) || (paramViewGroup == LOADING))
        paramView.setVisibility(8);
      do
        return paramView;
      while (paramViewGroup != EMPTY);
      if ((!EditUploadPhotoCategoryActivity.this.isTag) && ((paramView instanceof NovaTextView)))
      {
        ((NovaTextView)paramView).setText(R.string.ugc_photo_edit_poi_nocontent_tip);
        ((NovaTextView)paramView).setCompoundDrawables(null, null, null, null);
        ((NovaTextView)paramView).setGravity(1);
        return paramView;
      }
      paramView.setVisibility(8);
      return paramView;
    }

    protected View itemViewWithData(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      String str;
      if (EditUploadPhotoCategoryActivity.this.isTag)
      {
        if ((paramView == null) || (paramView.getId() != R.id.ugc_photo_tag_item_layout))
        {
          paramView = LayoutInflater.from(EditUploadPhotoCategoryActivity.this).inflate(R.layout.ugc_photo_edit_tag_item_layout, paramViewGroup, false);
          paramViewGroup = (TextView)paramView.findViewById(R.id.ugc_photo_tag);
          str = "# " + paramDPObject.getString("TagName") + " #";
          if ((EditUploadPhotoCategoryActivity.this.keyWord == null) || (EditUploadPhotoCategoryActivity.this.keyWord.length() <= 0) || (!str.contains(EditUploadPhotoCategoryActivity.this.keyWord)))
            break label196;
          paramViewGroup.setText(EditUploadPhotoCategoryActivity.this.setHighLightText(str));
        }
        while (true)
        {
          paramViewGroup = (TextView)paramView.findViewById(R.id.ugc_photo_tag_count);
          paramInt = paramDPObject.getInt("Count");
          if (paramInt <= 0)
            break label206;
          paramViewGroup.setText(paramInt + "条");
          paramViewGroup.setVisibility(0);
          return paramView;
          break;
          label196: paramViewGroup.setText(str);
        }
        label206: paramViewGroup.setVisibility(8);
        return paramView;
      }
      if ((paramView == null) || (paramView.getId() != R.id.ugc_photo_poi_item_layout))
      {
        paramView = LayoutInflater.from(EditUploadPhotoCategoryActivity.this).inflate(R.layout.ugc_photo_edit_poi_item_layout, paramViewGroup, false);
        paramViewGroup = (TextView)paramView.findViewById(R.id.ugc_photo_poi_name);
        paramViewGroup.setTag(Integer.valueOf(paramDPObject.getInt("ShopId")));
        str = paramDPObject.getString("ShopName");
        if ((EditUploadPhotoCategoryActivity.this.keyWord == null) || (EditUploadPhotoCategoryActivity.this.keyWord.length() <= 0) || (!str.contains(EditUploadPhotoCategoryActivity.this.keyWord)))
          break label359;
        paramViewGroup.setText(EditUploadPhotoCategoryActivity.this.setHighLightText(str));
      }
      while (true)
      {
        ((TextView)paramView.findViewById(R.id.ugc_photo_poi_position)).setText(paramDPObject.getString("ShopAddress"));
        return paramView;
        break;
        label359: paramViewGroup.setText(str);
      }
    }

    public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
    {
      super.onRequestFinish(paramMApiRequest, paramMApiResponse);
      if ((EditUploadPhotoCategoryActivity.this.isNewAddAciton) && (EditUploadPhotoCategoryActivity.this.isPoiTextFinished))
        GAHelper.instance().contextStatisticsEvent(EditUploadPhotoCategoryActivity.this, "choosePoiSearch", null, "view");
    }

    public void reset()
    {
      super.reset();
      this.mReq = null;
    }
  }

  class UploadPhotoEditTextWatch
    implements TextWatcher
  {
    UploadPhotoEditTextWatch()
    {
    }

    public void afterTextChanged(Editable paramEditable)
    {
    }

    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
    {
    }

    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
    {
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.ugc.photo.upload.edit.EditUploadPhotoCategoryActivity
 * JD-Core Version:    0.6.0
 */