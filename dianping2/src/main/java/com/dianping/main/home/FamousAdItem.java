package com.dianping.main.home;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dianping.base.app.NovaActivity;
import com.dianping.loader.MyResources;
import com.dianping.util.BitmapUtils;
import com.dianping.util.DPUrl;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.id;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaLinearLayout;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONObject;

public class FamousAdItem extends NovaLinearLayout
  implements View.OnClickListener
{
  public static final int TYPE_NORMAL = 0;
  public static final int TYPE_TRIP = 1;
  public int bizId;
  private NetworkImageView famousAdItemImageView;
  private TextView famousAdItemSubtitle;
  private TextView famousAdItemTitle;
  private NetworkImageView famousItemTitleImage;
  public String label;
  private Context mContext;
  private MyResources res;
  private String schema;

  public FamousAdItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
    this.res = MyResources.getResource(FamousAdItem.class);
  }

  private void setLocalImage(String paramString)
  {
    Object localObject = null;
    String str = null;
    try
    {
      paramString = this.mContext.getAssets().open("famous_ad_icons/" + paramString + ".png");
      if (paramString != null)
      {
        str = paramString;
        localObject = paramString;
        Bitmap localBitmap = BitmapUtils.decodeSampledBitmapFromStream(Bitmap.Config.RGB_565, paramString, ViewUtils.getScreenWidthPixels(this.mContext), ViewUtils.getScreenHeightPixels(this.mContext));
        str = paramString;
        localObject = paramString;
        this.famousItemTitleImage.setLocalBitmap(localBitmap);
      }
      if (paramString != null);
      try
      {
        paramString.close();
        return;
      }
      catch (IOException paramString)
      {
        paramString.printStackTrace();
        return;
      }
    }
    catch (IOException paramString)
    {
      do
      {
        localObject = str;
        paramString.printStackTrace();
      }
      while (str == null);
      try
      {
        str.close();
        return;
      }
      catch (IOException paramString)
      {
        paramString.printStackTrace();
        return;
      }
    }
    finally
    {
      if (localObject == null);
    }
    try
    {
      ((InputStream)localObject).close();
      throw paramString;
    }
    catch (IOException localIOException)
    {
      while (true)
        localIOException.printStackTrace();
    }
  }

  public void onClick(View paramView)
  {
    if (!android.text.TextUtils.isEmpty(this.schema))
      getContext().startActivity(new DPUrl(this.schema).getIntent());
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.famousAdItemTitle = ((TextView)findViewById(R.id.famousad_item_tittle));
    this.famousAdItemSubtitle = ((TextView)findViewById(R.id.famousad_item_subtittle));
    this.famousItemTitleImage = ((NetworkImageView)findViewById(R.id.famousad_item_title_image));
    this.famousAdItemImageView = ((NetworkImageView)findViewById(R.id.famousad_item_imageview));
    setOnClickListener(this);
  }

  public void setFamousAd(JSONObject paramJSONObject, int paramInt1, int paramInt2)
  {
    if (paramJSONObject == null)
      return;
    SpannableStringBuilder localSpannableStringBuilder = com.dianping.util.TextUtils.jsonParseText(paramJSONObject.optString("subTitle"));
    if (paramInt2 == 1)
    {
      localObject = com.dianping.util.TextUtils.jsonParseText(paramJSONObject.optString("title"));
      this.famousAdItemTitle.setText((CharSequence)localObject);
      this.gaUserInfo.title = ((SpannableStringBuilder)localObject).toString();
      setGAString(((SpannableStringBuilder)localObject).toString());
      this.famousAdItemSubtitle.setText(localSpannableStringBuilder);
      this.famousAdItemImageView.setImage(paramJSONObject.optString("icon"));
      this.schema = paramJSONObject.optString("schema");
      this.label = paramJSONObject.optString("gaLabel");
      if (!com.dianping.util.TextUtils.isEmpty(paramJSONObject.optString("bizId")))
        this.bizId = Integer.parseInt(paramJSONObject.optString("bizId"));
      setGAString(this.label);
      this.gaUserInfo.biz_id = (this.bizId + "");
      this.gaUserInfo.index = Integer.valueOf(paramInt1);
      ((NovaActivity)getContext()).addGAView(this, paramInt1, "home", "home".equals(((NovaActivity)getContext()).getPageName()));
      return;
    }
    Object localObject = paramJSONObject.optString("titleType");
    if (!com.dianping.util.TextUtils.isEmpty((CharSequence)localObject))
      setLocalImage((String)localObject);
    while (true)
    {
      this.gaUserInfo.title = localSpannableStringBuilder.toString();
      setGAString(localSpannableStringBuilder.toString());
      break;
      localObject = paramJSONObject.optString("picUrl");
      if (com.dianping.util.TextUtils.isEmpty((CharSequence)localObject))
        continue;
      this.famousItemTitleImage.setImage((String)localObject);
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.home.FamousAdItem
 * JD-Core Version:    0.6.0
 */