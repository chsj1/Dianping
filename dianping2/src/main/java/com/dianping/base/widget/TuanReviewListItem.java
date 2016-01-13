package com.dianping.base.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.dianping.app.DPApplication;
import com.dianping.archive.DPObject;
import com.dianping.base.app.NovaApplication;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TuanReviewListItem extends LinearLayout
{
  private static final int MAX_LINES = 7;
  protected final String DATE_TIME_FORMAT = "MM-dd HH:mm";
  protected final String YEAR_DATE_FORMAT = "yyyy-MM-dd";
  protected TextView contentTextView;
  protected TextView dateTextView;
  protected int dealId;
  protected DPObject[] dpPictures;
  protected DPObject dpTuanReview;
  protected ImageView expandView;
  protected boolean isExpand;
  protected View.OnClickListener mListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      paramView = TuanReviewListItem.this;
      boolean bool;
      if (!TuanReviewListItem.this.isExpand)
      {
        bool = true;
        paramView.isExpand = bool;
        if (!TuanReviewListItem.this.isExpand)
          break label77;
        TuanReviewListItem.this.expandView.setImageResource(R.drawable.ic_filter_up);
        label45: paramView = TuanReviewListItem.this.contentTextView;
        if (!TuanReviewListItem.this.isExpand)
          break label93;
      }
      label77: label93: for (int i = 2147483647; ; i = 7)
      {
        paramView.setMaxLines(i);
        return;
        bool = false;
        break;
        TuanReviewListItem.this.expandView.setImageResource(R.drawable.ic_filter_down);
        break label45;
      }
    }
  };
  protected ShopPower mShopStar;
  protected int maxImageCount = 0;
  protected TextView nameTextView;
  protected TableLayout photosContainer;
  protected TextView shopTextView;
  protected TextView statusView;

  public TuanReviewListItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public TuanReviewListItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void addPhotoView(DPObject[] paramArrayOfDPObject)
  {
    int i;
    int j;
    int k;
    int m;
    3 local3;
    TableRow localTableRow;
    if ((paramArrayOfDPObject != null) && (paramArrayOfDPObject.length > 0))
    {
      this.photosContainer.setVisibility(0);
      i = ViewUtils.getScreenWidthPixels(getContext());
      if (this.maxImageCount == 0)
        this.maxImageCount = 4;
      j = ViewUtils.dip2px(getContext(), 11.0F);
      k = (i - j * 3 - getResources().getDimensionPixelSize(R.dimen.table_item_padding) * 2) / 4;
      m = Math.min(this.maxImageCount, paramArrayOfDPObject.length);
      this.photosContainer.removeAllViews();
      local3 = new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          TuanReviewListItem.this.go2PhotoPreview((NetworkImageView)paramView, ((Integer)paramView.getTag()).intValue());
        }
      };
      localTableRow = null;
      i = 0;
    }
    while (i < m)
    {
      if (i % 4 == 0)
      {
        localTableRow = new TableRow(getContext());
        this.photosContainer.addView(localTableRow, new LinearLayout.LayoutParams(-1, -2));
      }
      Object localObject1 = LayoutInflater.from(getContext()).inflate(R.layout.tuan_review_item_photo, localTableRow, false);
      Object localObject2 = (ViewGroup.MarginLayoutParams)((View)localObject1).getLayoutParams();
      ((ViewGroup.MarginLayoutParams)localObject2).width = k;
      ((ViewGroup.MarginLayoutParams)localObject2).height = k;
      ((ViewGroup.MarginLayoutParams)localObject2).rightMargin = j;
      localObject2 = (NetworkImageView)((View)localObject1).findViewById(R.id.picture);
      ((NetworkImageView)localObject2).setImage(paramArrayOfDPObject[i].getString("Thumb"));
      ((NetworkImageView)localObject2).setOnClickListener(local3);
      ((NetworkImageView)localObject2).setTag(Integer.valueOf(i));
      localTableRow.addView((View)localObject1);
      if ((m < paramArrayOfDPObject.length) && (i == 3))
      {
        localObject1 = (TextView)((View)localObject1).findViewById(R.id.tag);
        ((TextView)localObject1).setText(paramArrayOfDPObject.length + "张");
        ((TextView)localObject1).setVisibility(0);
        ((NetworkImageView)localObject2).setOnClickListener(new View.OnClickListener(paramArrayOfDPObject)
        {
          public void onClick(View paramView)
          {
            TuanReviewListItem.this.maxImageCount = this.val$photos.length;
            TuanReviewListItem.this.addPhotoView(this.val$photos);
          }
        });
      }
      i += 1;
      continue;
      this.photosContainer.setVisibility(8);
    }
  }

  protected void go2PhotoPreview(NetworkImageView paramNetworkImageView, int paramInt)
  {
    Intent localIntent;
    if ((this.dpTuanReview != null) && (this.dpPictures != null) && (this.dpPictures.length > 0))
    {
      localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showcheckinphoto"));
      localIntent.putExtra("shopname", this.dpTuanReview.getString("ShopName"));
      localIntent.putExtra("position", paramInt);
      localIntent.putExtra("fromActivity", "TuanReviewDetailActiviy");
    }
    try
    {
      paramNetworkImageView = ((BitmapDrawable)paramNetworkImageView.getDrawable()).getBitmap();
      Object localObject = new ByteArrayOutputStream();
      paramNetworkImageView.compress(Bitmap.CompressFormat.JPEG, 90, (OutputStream)localObject);
      localIntent.putExtra("currentbitmap", ((ByteArrayOutputStream)localObject).toByteArray());
      label118: paramNetworkImageView = new ArrayList();
      localObject = this.dpPictures;
      int i = localObject.length;
      paramInt = 0;
      while (paramInt < i)
      {
        paramNetworkImageView.add(localObject[paramInt]);
        paramInt += 1;
      }
      localIntent.putParcelableArrayListExtra("pageList", paramNetworkImageView);
      getContext().startActivity(localIntent);
      NovaApplication.instance().statisticsEvent("tuan5", "tuan5_review_photo", "dealgroupid", this.dealId);
      return;
    }
    catch (java.lang.Exception paramNetworkImageView)
    {
      break label118;
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.nameTextView = ((TextView)findViewById(R.id.name));
    this.dateTextView = ((TextView)findViewById(R.id.date));
    this.contentTextView = ((TextView)findViewById(R.id.content));
    this.shopTextView = ((TextView)findViewById(R.id.shop));
    this.mShopStar = ((ShopPower)findViewById(R.id.shop_power));
    this.expandView = ((ImageView)findViewById(R.id.expand));
    this.photosContainer = ((TableLayout)findViewById(R.id.review_photos_container));
  }

  public void setDealId(int paramInt)
  {
    this.dealId = paramInt;
  }

  public void showItem(DPObject paramDPObject)
  {
    this.isExpand = false;
    this.maxImageCount = 0;
    this.dpTuanReview = paramDPObject;
    this.nameTextView.setText(paramDPObject.getString("UserName"));
    Object localObject;
    if (paramDPObject.getTime("PostDate") > 0L)
    {
      localObject = Calendar.getInstance();
      int i = ((Calendar)localObject).get(1);
      ((Calendar)localObject).setTimeInMillis(paramDPObject.getTime("PostDate"));
      if (i == ((Calendar)localObject).get(1))
      {
        localObject = new SimpleDateFormat("MM-dd HH:mm");
        this.dateTextView.setText(((DateFormat)localObject).format(Long.valueOf(paramDPObject.getTime("PostDate"))));
      }
    }
    else
    {
      if (!TextUtils.isEmpty(paramDPObject.getString("ShopName")))
        break label241;
      this.shopTextView.setVisibility(8);
    }
    while (true)
    {
      this.mShopStar.setPower(paramDPObject.getInt("Score"));
      this.dpPictures = paramDPObject.getArray("PictureList");
      addPhotoView(this.dpPictures);
      this.expandView.setVisibility(8);
      this.expandView.setOnClickListener(this.mListener);
      this.contentTextView.setOnClickListener(this.mListener);
      this.contentTextView.setMaxLines(2147483647);
      this.contentTextView.setText(paramDPObject.getString("Content"));
      this.contentTextView.post(new Runnable()
      {
        public void run()
        {
          if ((TuanReviewListItem.this.contentTextView.getMeasuredHeight() - TuanReviewListItem.this.contentTextView.getPaddingTop() - TuanReviewListItem.this.contentTextView.getPaddingBottom()) / TuanReviewListItem.this.contentTextView.getLineHeight() > 7)
          {
            TuanReviewListItem.this.expandView.setVisibility(0);
            TuanReviewListItem.this.expandView.setImageResource(R.drawable.ic_filter_down);
            TuanReviewListItem.this.contentTextView.setMaxLines(7);
            return;
          }
          TuanReviewListItem.this.expandView.setVisibility(8);
        }
      });
      return;
      localObject = new SimpleDateFormat("yyyy-MM-dd");
      break;
      label241: this.shopTextView.setText(paramDPObject.getString("ShopName"));
      this.shopTextView.setVisibility(0);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.TuanReviewListItem
 * JD-Core Version:    0.6.0
 */