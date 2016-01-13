package com.dianping.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.style;
import com.dianping.v1.R.styleable;
import com.dianping.widget.view.NovaLinearLayout;

public class DPBasicItem extends NovaLinearLayout
{
  public static final int TEXT_TYPE_BLACK_COLOR = 8;
  public static final int TEXT_TYPE_BOLD = 16;
  public static final int TEXT_TYPE_GRAY_COLOR = 4;
  public static final int TEXT_TYPE_SMALL = 1;
  public static final int TEXT_TYPE_YELLOW_COLOR = 2;
  private int arrowImageResID;
  private int checked;
  private boolean clickable;
  private String count;
  private int count_textType;
  private String input;
  private String input_hint;
  private int input_maxLength;
  private int input_textType;
  private int input_type;
  private ImageView itemArrow;
  private CheckBox itemCheckBox;
  private TextView itemCount;
  private DPEditText itemInput;
  private NetworkImageView itemLeft1stPic;
  private ImageView itemLeft2ndPic;
  private ImageView itemRight1stPic;
  private ImageView itemRight2ndPic;
  private TextView itemSubtitle;
  private TextView itemTitle;
  private LinearLayout itemTitleLay;
  private Context mContext;
  private int right1stImageResID;
  private int right2ndImageResID;
  private boolean show1stPic;
  private boolean show2ndPic;
  private String subTitle;
  private int subTitle_textType;
  private String title;
  Spanned titleSpan;
  private int title_textType;

  public DPBasicItem(Context paramContext)
  {
    this(paramContext, null);
    this.mContext = paramContext;
    setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
  }

  public DPBasicItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.DPBasicItem);
    this.title = paramAttributeSet.getString(R.styleable.DPBasicItem_dptitle);
    this.subTitle = paramAttributeSet.getString(R.styleable.DPBasicItem_subTitle);
    this.input = paramAttributeSet.getString(R.styleable.DPBasicItem_input);
    this.input_hint = paramAttributeSet.getString(R.styleable.DPBasicItem_input_hint);
    this.input_type = paramAttributeSet.getInt(R.styleable.DPBasicItem_input_type, 1);
    this.input_maxLength = paramAttributeSet.getInt(R.styleable.DPBasicItem_input_maxLength, 0);
    this.count = paramAttributeSet.getString(R.styleable.DPBasicItem_count);
    this.checked = paramAttributeSet.getInt(R.styleable.DPBasicItem_checked, 0);
    this.title_textType = paramAttributeSet.getInt(R.styleable.DPBasicItem_title_textType, 0);
    this.subTitle_textType = paramAttributeSet.getInt(R.styleable.DPBasicItem_subTitle_textType, 0);
    this.count_textType = paramAttributeSet.getInt(R.styleable.DPBasicItem_count_textType, 0);
    this.input_textType = paramAttributeSet.getInt(R.styleable.DPBasicItem_input_textType, 0);
    this.clickable = paramAttributeSet.getBoolean(R.styleable.DPBasicItem_clickable, false);
    this.arrowImageResID = paramAttributeSet.getResourceId(R.styleable.DPBasicItem_arrowImage, 0);
    this.right1stImageResID = paramAttributeSet.getResourceId(R.styleable.DPBasicItem_right1stPic, 0);
    this.show1stPic = paramAttributeSet.getBoolean(R.styleable.DPBasicItem_show1stPic, false);
    this.right2ndImageResID = paramAttributeSet.getResourceId(R.styleable.DPBasicItem_right2ndPic, 0);
    this.show2ndPic = paramAttributeSet.getBoolean(R.styleable.DPBasicItem_show2ndPic, false);
    paramAttributeSet.recycle();
    if (isInEditMode())
      return;
    setupView(paramContext);
  }

  private int dip2px(float paramFloat)
  {
    return (int)(paramFloat * this.mContext.getResources().getDisplayMetrics().density + 0.5F);
  }

  private void setTextType(TextView paramTextView, int paramInt)
  {
    Resources localResources = this.mContext.getResources();
    if (paramInt == 0)
      return;
    if ((paramInt & 0x1) == 1)
      paramTextView.setTextAppearance(this.mContext, R.style.content_page_small_text);
    if ((paramInt & 0x2) == 2)
      paramTextView.setTextColor(localResources.getColorStateList(R.color.text_yellow_color_selector));
    if ((paramInt & 0x4) == 4)
      paramTextView.setTextColor(localResources.getColorStateList(R.color.text_gray_color_selector));
    if ((paramInt & 0x8) == 8)
      paramTextView.setTextColor(localResources.getColorStateList(R.color.black));
    if ((paramInt & 0x10) == 16)
    {
      paramTextView.getPaint().setFakeBoldText(true);
      return;
    }
    paramTextView.getPaint().setFakeBoldText(false);
  }

  private void setupView(Context paramContext)
  {
    Object localObject = paramContext.getResources();
    ColorStateList localColorStateList = ((Resources)localObject).getColorStateList(R.color.text_color_default);
    this.itemTitleLay = new LinearLayout(paramContext);
    this.itemTitleLay.setDuplicateParentStateEnabled(true);
    this.itemTitleLay.setGravity(16);
    this.itemTitle = new TextView(paramContext);
    this.itemTitle.setId(R.id.itemTitle);
    this.itemTitle.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
    this.itemTitle.setText(this.title);
    this.itemTitle.setDuplicateParentStateEnabled(true);
    this.itemTitle.setTextAppearance(paramContext, 16973892);
    this.itemTitle.setTextColor(localColorStateList);
    this.itemTitle.setSingleLine(true);
    this.itemTitle.setEllipsize(TextUtils.TruncateAt.END);
    this.itemTitle.setPadding(0, 0, dip2px(10.0F), 0);
    this.itemTitle.setTypeface(Typeface.create(Typeface.DEFAULT, 0));
    setTextType(this.itemTitle, this.title_textType);
    this.itemTitleLay.addView(this.itemTitle);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(dip2px(30.0F), dip2px(30.0F));
    localLayoutParams.setMargins(0, 0, dip2px(10.0F), 0);
    this.itemLeft1stPic = new NetworkImageView(paramContext);
    this.itemLeft1stPic.setId(R.id.itemRight1stPic);
    this.itemLeft1stPic.setLayoutParams(localLayoutParams);
    this.itemLeft1stPic.setDuplicateParentStateEnabled(true);
    this.itemTitleLay.addView(this.itemLeft1stPic);
    this.itemLeft2ndPic = new ImageView(paramContext);
    this.itemLeft2ndPic.setId(R.id.itemRight2ndPic);
    this.itemLeft2ndPic.setLayoutParams(localLayoutParams);
    this.itemLeft2ndPic.setDuplicateParentStateEnabled(true);
    this.itemTitleLay.addView(this.itemLeft2ndPic);
    this.itemSubtitle = new TextView(paramContext);
    this.itemSubtitle.setId(R.id.itemSubTitle);
    this.itemSubtitle.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
    this.itemSubtitle.setText(this.subTitle);
    this.itemSubtitle.setDuplicateParentStateEnabled(true);
    this.itemSubtitle.setTextAppearance(paramContext, 16973892);
    this.itemSubtitle.setTextColor(localColorStateList);
    this.itemSubtitle.setSingleLine(true);
    this.itemSubtitle.setEllipsize(TextUtils.TruncateAt.END);
    setTextType(this.itemSubtitle, this.subTitle_textType);
    this.itemTitleLay.addView(this.itemSubtitle);
    addView(this.itemTitleLay);
    this.itemInput = new DPEditText(paramContext);
    this.itemInput.setId(R.id.itemInput);
    localLayoutParams = new LinearLayout.LayoutParams(0, -2, 1.0F);
    this.itemInput.setLayoutParams(localLayoutParams);
    this.itemInput.setGravity(16);
    this.itemInput.setText(this.input);
    this.itemInput.setDuplicateParentStateEnabled(true);
    this.itemInput.setTextAppearance(paramContext, 16973892);
    this.itemInput.setTextColor(localColorStateList);
    this.itemInput.setSingleLine(true);
    this.itemInput.setEllipsize(TextUtils.TruncateAt.END);
    this.itemInput.setHint(this.input_hint);
    this.itemInput.setInputType(this.input_type);
    this.itemInput.setMaxLength(this.input_maxLength);
    this.itemInput.setBackgroundDrawable(null);
    this.itemInput.setPadding(0, 0, 0, 0);
    setTextType(this.itemInput, this.input_textType);
    addView(this.itemInput);
    this.itemCount = new TextView(paramContext);
    this.itemCount.setId(R.id.itemCount);
    this.itemCount.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
    this.itemCount.setText(this.count);
    this.itemCount.setMaxWidth(dip2px(180.0F));
    this.itemCount.setDuplicateParentStateEnabled(true);
    this.itemCount.setTextAppearance(paramContext, 16973892);
    this.itemCount.setTextColor(((Resources)localObject).getColorStateList(R.color.text_gray_color_selector));
    this.itemCount.setPadding(0, 0, 0, 0);
    setTextType(this.itemCount, this.count_textType);
    addView(this.itemCount);
    this.itemCheckBox = new CheckBox(paramContext);
    this.itemCheckBox.setId(R.id.itemCheckBox);
    this.itemCheckBox.setLayoutParams(new LinearLayout.LayoutParams(dip2px(26.0F), dip2px(25.0F)));
    localObject = this.itemCheckBox;
    boolean bool;
    if (this.checked == 1)
    {
      bool = true;
      ((CheckBox)localObject).setChecked(bool);
      this.itemCheckBox.setPadding(0, 0, 0, 0);
      addView(this.itemCheckBox);
      localObject = new LinearLayout.LayoutParams(dip2px(30.0F), dip2px(30.0F));
      ((LinearLayout.LayoutParams)localObject).setMargins(dip2px(10.0F), 0, 0, 0);
      this.itemRight1stPic = new ImageView(paramContext);
      this.itemRight1stPic.setId(R.id.itemRight1stPic);
      this.itemRight1stPic.setLayoutParams((ViewGroup.LayoutParams)localObject);
      this.itemRight1stPic.setDuplicateParentStateEnabled(true);
      if (this.right1stImageResID == 0)
        break label1141;
      this.itemRight1stPic.setImageResource(this.right1stImageResID);
      label950: addView(this.itemRight1stPic);
      this.itemRight2ndPic = new ImageView(paramContext);
      this.itemRight2ndPic.setId(R.id.itemRight2ndPic);
      this.itemRight2ndPic.setLayoutParams((ViewGroup.LayoutParams)localObject);
      this.itemRight2ndPic.setDuplicateParentStateEnabled(true);
      if (this.right2ndImageResID == 0)
        break label1152;
      this.itemRight2ndPic.setImageResource(this.right2ndImageResID);
      label1014: addView(this.itemRight2ndPic);
      this.itemArrow = new ImageView(paramContext);
      this.itemArrow.setId(R.id.itemArrow);
      this.itemArrow.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
      this.itemArrow.setPadding(dip2px(10.0F), 0, 0, 0);
      this.itemArrow.setDuplicateParentStateEnabled(true);
      if (this.arrowImageResID == 0)
        break label1163;
      this.itemArrow.setImageResource(this.arrowImageResID);
    }
    while (true)
    {
      addView(this.itemArrow);
      build();
      setGravity(16);
      setMinimumHeight(dip2px(45.0F));
      return;
      bool = false;
      break;
      label1141: this.itemRight1stPic.setImageResource(0);
      break label950;
      label1152: this.itemRight2ndPic.setImageResource(0);
      break label1014;
      label1163: this.itemArrow.setImageResource(R.drawable.arrow);
    }
  }

  public void build()
  {
    int j = 8;
    Object localObject = this.itemTitle;
    int i;
    if (this.title == null)
    {
      i = 8;
      ((TextView)localObject).setVisibility(i);
      localObject = this.itemSubtitle;
      if (this.subTitle != null)
        break label268;
      i = 8;
      label38: ((TextView)localObject).setVisibility(i);
      localObject = this.itemInput;
      if ((this.input_hint == null) && (this.input == null))
        break label273;
      i = 0;
      label64: ((DPEditText)localObject).setVisibility(i);
      localObject = this.itemCount;
      if (this.count == null)
        break label279;
      i = 0;
      label83: ((TextView)localObject).setVisibility(i);
      localObject = this.itemCheckBox;
      if (this.checked != 0)
        break label285;
      i = 8;
      label103: ((CheckBox)localObject).setVisibility(i);
      localObject = this.itemRight1stPic;
      if (!isShow1stPic())
        break label290;
      i = 0;
      label122: ((ImageView)localObject).setVisibility(i);
      localObject = this.itemRight2ndPic;
      if (!isShow2ndPic())
        break label296;
      i = 0;
      label141: ((ImageView)localObject).setVisibility(i);
      this.itemLeft1stPic.setVisibility(8);
      this.itemLeft2ndPic.setVisibility(8);
      localObject = this.itemArrow;
      i = j;
      if (isClickable())
        i = 0;
      ((ImageView)localObject).setVisibility(i);
      if ((this.input_hint == null) && (this.input == null))
        break label302;
      this.itemTitleLay.setLayoutParams(new LinearLayout.LayoutParams(-2, -2, 0.0F));
    }
    while (true)
    {
      if ((this.input_hint != null) || (this.subTitle != null))
        this.title_textType |= 4;
      setTextType(this.itemTitle, this.title_textType);
      setClickable(this.clickable);
      return;
      i = 0;
      break;
      label268: i = 0;
      break label38;
      label273: i = 8;
      break label64;
      label279: i = 8;
      break label83;
      label285: i = 0;
      break label103;
      label290: i = 8;
      break label122;
      label296: i = 8;
      break label141;
      label302: this.itemTitleLay.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0F));
    }
  }

  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    int i = R.id.itemInput;
    int j = getId();
    Object localObject = (DPEditText)findViewById(R.id.itemInput);
    if (localObject == null)
      super.dispatchRestoreInstanceState(paramSparseArray);
    do
    {
      return;
      Parcelable localParcelable = (Parcelable)paramSparseArray.get(i - j);
      if (localParcelable != null)
        ((DPEditText)localObject).onRestoreInstanceState(localParcelable);
      i = R.id.itemCheckBox;
      j = getId();
      localObject = (CheckBox)findViewById(R.id.itemCheckBox);
      paramSparseArray = (Parcelable)paramSparseArray.get(i ^ j);
    }
    while (paramSparseArray == null);
    ((CheckBox)localObject).onRestoreInstanceState(paramSparseArray);
  }

  protected void dispatchSaveInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    int i = R.id.itemInput;
    int j = getId();
    Object localObject = (DPEditText)findViewById(R.id.itemInput);
    if (localObject == null)
      super.dispatchSaveInstanceState(paramSparseArray);
    do
    {
      return;
      localObject = ((DPEditText)localObject).onSaveInstanceState();
      if (localObject != null)
        paramSparseArray.put(i - j, localObject);
      i = R.id.itemCheckBox;
      j = getId();
      localObject = ((CheckBox)findViewById(R.id.itemCheckBox)).onSaveInstanceState();
    }
    while (localObject == null);
    paramSparseArray.put(i ^ j, localObject);
  }

  public String getCount()
  {
    return this.count;
  }

  public int getCountTextType()
  {
    return this.count_textType;
  }

  public String getInputHint()
  {
    return this.input_hint;
  }

  public int getInputMaxLength()
  {
    return this.input_maxLength;
  }

  public String getInputText()
  {
    return this.input;
  }

  public int getInputTextType()
  {
    return this.input_textType;
  }

  public int getInputType()
  {
    return this.input_type;
  }

  public ImageView getItemArrow()
  {
    return this.itemArrow;
  }

  public CheckBox getItemCheckBox()
  {
    return this.itemCheckBox;
  }

  public TextView getItemCount()
  {
    return this.itemCount;
  }

  public DPEditText getItemInput()
  {
    return this.itemInput;
  }

  public NetworkImageView getItemLeft1stPic()
  {
    return this.itemLeft1stPic;
  }

  public ImageView getItemLeft2ndPic()
  {
    return this.itemLeft2ndPic;
  }

  public ImageView getItemRight1stPic()
  {
    return this.itemRight1stPic;
  }

  public ImageView getItemRight2ndPic()
  {
    return this.itemRight2ndPic;
  }

  public TextView getItemSubtitle()
  {
    return this.itemSubtitle;
  }

  public TextView getItemTitle()
  {
    return this.itemTitle;
  }

  public LinearLayout getItemTitleLay()
  {
    return this.itemTitleLay;
  }

  public String getSubTitle()
  {
    return this.subTitle;
  }

  public int getSubTitleTextType()
  {
    return this.subTitle_textType;
  }

  public String getTitle()
  {
    return this.title;
  }

  public int getTitleTextType()
  {
    return this.title_textType;
  }

  public boolean isClickable()
  {
    return this.clickable;
  }

  public boolean isShow1stPic()
  {
    return this.show1stPic;
  }

  public boolean isShow2ndPic()
  {
    return this.show2ndPic;
  }

  public TextView itemTitle()
  {
    return this.itemTitle;
  }

  public void setArrowImage(int paramInt)
  {
    this.itemArrow.setImageResource(paramInt);
  }

  public void setClickable(boolean paramBoolean)
  {
    super.setClickable(paramBoolean);
    this.clickable = paramBoolean;
  }

  public void setCount(String paramString)
  {
    this.count = paramString;
    this.itemCount.setText(paramString);
  }

  public void setCountTextType(int paramInt)
  {
    this.count_textType = paramInt;
    setTextType(this.itemCount, paramInt);
  }

  public void setHint(String paramString)
  {
    this.input_hint = paramString;
    this.itemInput.setHint(paramString);
  }

  public void setInputMaxLength(int paramInt)
  {
    this.input_maxLength = paramInt;
    this.itemInput.setMaxLength(paramInt);
  }

  public void setInputText(String paramString)
  {
    this.input = paramString;
    this.itemInput.setText(paramString);
  }

  public void setInputTextType(int paramInt)
  {
    this.input_textType = paramInt;
    setTextType(this.itemInput, paramInt);
  }

  public void setInputType(int paramInt)
  {
    this.input_type = paramInt;
    this.itemInput.setInputType(paramInt);
  }

  public void setSubTitle(String paramString)
  {
    this.subTitle = paramString;
    this.itemSubtitle.setText(paramString);
  }

  public void setSubTitleTextType(int paramInt)
  {
    this.subTitle_textType = paramInt;
    setTextType(this.itemSubtitle, paramInt);
  }

  public void setTitle(Spanned paramSpanned)
  {
    this.titleSpan = paramSpanned;
    this.itemTitle.setText(paramSpanned);
  }

  public void setTitle(String paramString)
  {
    this.title = paramString;
    this.itemTitle.setText(paramString);
  }

  public void setTitleTextType(int paramInt)
  {
    this.title_textType = paramInt;
    setTextType(this.itemTitle, paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.widget.DPBasicItem
 * JD-Core Version:    0.6.0
 */