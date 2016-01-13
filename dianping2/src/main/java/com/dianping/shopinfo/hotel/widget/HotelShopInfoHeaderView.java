package com.dianping.shopinfo.hotel.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.HorizontalImageGallery;
import com.dianping.base.widget.HorizontalImageGallery.OnGalleryImageClickListener;
import com.dianping.base.widget.ShopPower;
import com.dianping.shopinfo.widget.ShopInfoHeaderView;
import com.dianping.util.Log;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HotelShopInfoHeaderView extends ShopInfoHeaderView
{
  private RelativeLayout emptyGallery;
  private HorizontalImageGallery hotelImageGallery;
  private JSONObject hotelJson;
  private TextView hotelRank;
  private TextView hotelTotalReview;
  private ArrayList<DPObject> picList = new ArrayList();
  private DPObject shop;
  private int totalPicCount = 0;
  private View.OnClickListener uploadClickListener;

  public HotelShopInfoHeaderView(Context paramContext)
  {
    this(paramContext, null);
  }

  public HotelShopInfoHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void buildEmptyGallery()
  {
    this.emptyGallery.setVisibility(0);
    this.emptyGallery.setOnClickListener(this.uploadClickListener);
    ((TextView)this.emptyGallery.findViewById(R.id.hotel_image_gallery_empty_text)).setText("上传酒店图片");
    this.hotelImageGallery.setVisibility(8);
  }

  private void buildGallery()
  {
    this.hotelImageGallery.removeAllImages();
    ArrayList localArrayList = new ArrayList();
    if (this.totalPicCount > this.picList.size());
    for (boolean bool = true; ; bool = false)
    {
      int i = 0;
      while (i < this.picList.size())
      {
        localArrayList.add(((DPObject)this.picList.get(i)).getString("ThumbUrl"));
        i += 1;
      }
    }
    this.hotelImageGallery.addImages((String[])localArrayList.toArray(new String[0]), bool);
    this.hotelImageGallery.setOnGalleryImageClickListener(new HorizontalImageGallery.OnGalleryImageClickListener()
    {
      public void onGalleryImageClick(int paramInt1, int paramInt2, Drawable paramDrawable)
      {
        if (HotelShopInfoHeaderView.this.shop == null)
          return;
        if ((paramInt1 == paramInt2 - 1) && (HotelShopInfoHeaderView.this.totalPicCount > 7))
        {
          HotelShopInfoHeaderView.this.uploadClickListener.onClick(null);
          return;
        }
        Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showphotoandmore"));
        localIntent.putExtra("position", paramInt1);
        localIntent.putExtra("totalPicCount", HotelShopInfoHeaderView.this.totalPicCount);
        localIntent.putExtra("categoryTag", "hotel");
        Object localObject = new ArrayList();
        ((ArrayList)localObject).add(HotelShopInfoHeaderView.this.shop);
        localIntent.putParcelableArrayListExtra("arrShopObjs", (ArrayList)localObject);
        localIntent.putParcelableArrayListExtra("pageList", HotelShopInfoHeaderView.this.picList);
        if (paramDrawable != null)
        {
          localObject = new ByteArrayOutputStream();
          ((BitmapDrawable)paramDrawable).getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, (OutputStream)localObject);
          localIntent.putExtra("currentbitmap", ((ByteArrayOutputStream)localObject).toByteArray());
        }
        ((NovaActivity)HotelShopInfoHeaderView.this.getContext()).statisticsEvent("shopinfo5", "shopinfo5_hotelphoto", String.valueOf(HotelShopInfoHeaderView.this.shop.getInt("ID")), paramInt1);
        HotelShopInfoHeaderView.this.getContext().startActivity(localIntent);
      }
    });
    this.hotelImageGallery.setVisibility(0);
  }

  private void buildPicList()
  {
    this.picList.clear();
    while (true)
    {
      try
      {
        JSONArray localJSONArray = this.hotelJson.optJSONArray("picInfo");
        if (localJSONArray != null)
          break label209;
        return;
        if (i < localJSONArray.length())
        {
          Object localObject2 = localJSONArray.getJSONObject(i);
          int j = ((JSONObject)localObject2).optInt("id");
          long l = ((JSONObject)localObject2).optLong("time");
          Object localObject1 = ((JSONObject)localObject2).optString("url");
          String str = ((JSONObject)localObject2).optString("thumbUrl");
          localObject2 = ((JSONObject)localObject2).optString("name");
          localObject1 = new DPObject().edit().putInt("ID", j).putInt("currentPos", i).putInt("ShopID", this.shop.getInt("ID")).putTime("Time", l).putString("Name", (String)localObject2).putString("ThumbUrl", str).putString("Url", (String)localObject1).generate();
          this.picList.add(localObject1);
          j = this.picList.size();
          if (j < 7)
          {
            i += 1;
            continue;
          }
        }
      }
      catch (JSONException localJSONException)
      {
        Log.e(localJSONException.toString());
      }
      return;
      label209: int i = 0;
    }
  }

  private void parseHotelJson()
  {
    if ((this.hotelJson != null) && (this.hotelJson.length() > 0))
      break label17;
    while (true)
    {
      label17: return;
      if (!this.shop.contains("HotelJson"))
        continue;
      String str = this.shop.getString("HotelJson");
      if (TextUtils.isEmpty(str))
        break;
      try
      {
        this.hotelJson = new JSONObject(str);
        if ((this.hotelJson == null) || (this.hotelJson.length() == 0))
          continue;
        setHotelRank();
        setTotalPicCount();
        if (this.totalPicCount == 0)
        {
          buildEmptyGallery();
          return;
        }
      }
      catch (JSONException localJSONException)
      {
        while (true)
          Log.e(localJSONException.toString());
        buildPicList();
        if (this.picList.isEmpty())
        {
          buildEmptyGallery();
          return;
        }
        buildGallery();
      }
    }
  }

  private void setHotelRank()
  {
    String str1 = "";
    String str2 = "";
    Object localObject1 = str1;
    try
    {
      JSONArray localJSONArray = this.hotelJson.optJSONArray("rankstring");
      localObject1 = str1;
      localObject2 = new StringBuilder();
      int i = 0;
      while (true)
      {
        localObject1 = str1;
        if (i >= localJSONArray.length())
          break;
        localObject1 = str1;
        ((StringBuilder)localObject2).append(localJSONArray.getString(i));
        i += 1;
      }
      localObject1 = str1;
      str1 = ((StringBuilder)localObject2).toString();
      localObject1 = str1;
      i = this.hotelJson.optInt("highlight");
      localObject2 = str2;
      localObject3 = str1;
      localObject1 = str1;
      if (i < localJSONArray.length())
      {
        localObject1 = str1;
        localObject2 = localJSONArray.getString(i);
        localObject3 = str1;
      }
      if (!TextUtils.isEmpty((CharSequence)localObject3))
      {
        if (!TextUtils.isEmpty((CharSequence)localObject2))
        {
          i = ((String)localObject3).indexOf((String)localObject2);
          localObject1 = new SpannableString((CharSequence)localObject3);
          ((SpannableString)localObject1).setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), i, ((String)localObject2).length() + i, 17);
          this.hotelRank.setText((CharSequence)localObject1);
          this.hotelRank.setVisibility(0);
        }
      }
      else
        return;
    }
    catch (JSONException localJSONException)
    {
      while (true)
      {
        Log.e(localJSONException.toString());
        Object localObject2 = str2;
        Object localObject3 = localObject1;
        continue;
        this.hotelRank.setText((CharSequence)localObject3);
      }
    }
  }

  private void setShopName()
  {
    this.name.setText(DPObjectUtils.getShopFullName(this.shop));
  }

  private void setShopPower()
  {
    this.power.setPower(this.shop.getInt("ShopPower"));
    this.power.setVisibility(0);
  }

  private void setTotalPicCount()
  {
    this.totalPicCount = this.hotelJson.optInt("picCount");
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.rateSource = null;
    this.name = ((TextView)findViewById(R.id.title_shop_name));
    this.power = ((ShopPower)findViewById(R.id.shop_power));
    this.hotelTotalReview = ((TextView)findViewById(R.id.hotel_total_review));
    this.hotelRank = ((TextView)findViewById(R.id.hotel_ranking));
    this.hotelImageGallery = ((HorizontalImageGallery)findViewById(R.id.hotel_image_gallery));
    this.emptyGallery = ((RelativeLayout)findViewById(R.id.hotel_image_gallery_empty));
  }

  public void setShop(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    this.shop = paramDPObject;
    setShopName();
    setShopPower();
    parseHotelJson();
  }

  public void setTotalReviewTextView(int paramInt)
  {
    this.hotelTotalReview.setText("(" + paramInt + ")");
    this.hotelTotalReview.setVisibility(0);
  }

  public void setUploadClickListen(View.OnClickListener paramOnClickListener)
  {
    this.uploadClickListener = paramOnClickListener;
    if (this.emptyGallery != null)
      this.emptyGallery.setOnClickListener(this.uploadClickListener);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.hotel.widget.HotelShopInfoHeaderView
 * JD-Core Version:    0.6.0
 */