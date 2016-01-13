package com.dianping.wed.home.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.base.util.DPObjectUtils;
import com.dianping.base.widget.NovaFragment;
import com.dianping.dataservice.RequestHandler;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.dataservice.mapi.MApiService;
import com.dianping.model.SimpleMsg;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.wed.home.activity.HomeDesignProductListActivity;
import com.dianping.wed.weddingfeast.activity.WeddingShopPhotoGalleryActivity;
import com.dianping.widget.NetworkImageView;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class HomeDesignProductPhotoGalleryFragment extends NovaFragment
  implements RequestHandler<MApiRequest, MApiResponse>, AdapterView.OnItemClickListener
{
  private static final String API_URL_PRODUCT = "http://m.api.dianping.com/wedding/productlist.bin?";
  private static final int REQUEST_LIMIT = 20;
  private static final String REQUEST_PAGE_NAME = "productlist";
  private static final String TAG = HomeDesignProductPhotoGalleryFragment.class.getSimpleName();
  private int albumFrameHeight;
  private int albumFrameWidth;
  private String categoryDesc;
  private int coverStyleType;
  private String emptyMsg;
  private TextView emptyTV;
  private String errorMsg;
  private View fragmentView = null;
  private GridView gridView;
  private boolean isEmptySource;
  private boolean isEnd = false;
  private boolean isTaskRunning = false;
  private HomeDesignProductPhotoGalleryFragment.PhotoAdapter photoAdapter;
  private ArrayList<DPObject> photoList;
  private MApiRequest photoRequest;
  private int screenWidth;
  private DPObject shop;
  private int shopId;
  private int start;
  private int verticalAlbumHeight;

  private void photoTask(int paramInt1, int paramInt2)
  {
    MApiService localMApiService = (MApiService)getService("mapi");
    StringBuffer localStringBuffer = new StringBuffer();
    if (this.isEmptySource)
      return;
    localStringBuffer.append("http://m.api.dianping.com/wedding/productlist.bin?").append("shopid=").append(paramInt2 + "").append("&start=").append(paramInt1).append("&limit=").append(20).append("&pageName=").append("productlist");
    this.photoRequest = BasicMApiRequest.mapiGet(localStringBuffer.toString(), CacheType.NORMAL);
    localMApiService.exec(this.photoRequest, this);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (TextUtils.isEmpty(this.categoryDesc))
      this.categoryDesc = "产品";
    this.emptyTV = ((TextView)this.fragmentView.findViewById(R.id.gallery_empty));
    paramBundle = getResources().getDrawable(R.drawable.empty_page_nothing);
    paramBundle.setBounds(0, 0, paramBundle.getIntrinsicWidth(), paramBundle.getIntrinsicHeight());
    this.emptyTV.setCompoundDrawablePadding(8);
    this.emptyTV.setCompoundDrawables(paramBundle, null, null, null);
    this.emptyTV.setMovementMethod(LinkMovementMethod.getInstance());
    this.gridView.setEmptyView(this.emptyTV);
    this.emptyMsg = "商户暂时没有录入";
    this.emptyTV.setText(Html.fromHtml(this.emptyMsg));
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.screenWidth = ViewUtils.getScreenWidthPixels(getActivity());
    this.albumFrameWidth = (this.screenWidth * 45 / 100);
    this.albumFrameHeight = (this.albumFrameWidth * 210 / 280);
    this.verticalAlbumHeight = (this.albumFrameWidth * 374 / 280);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.fragmentView = paramLayoutInflater.inflate(R.layout.shop_wedding_photo_gallery, paramViewGroup, false);
    this.gridView = ((GridView)this.fragmentView.findViewById(R.id.gallery_gridview));
    if (this.photoList == null)
      this.photoList = new ArrayList();
    this.photoAdapter = new HomeDesignProductPhotoGalleryFragment.PhotoAdapter(this);
    this.gridView.setAdapter(this.photoAdapter);
    this.gridView.setOnItemClickListener(this);
    this.photoAdapter.notifyDataSetChanged();
    return this.fragmentView;
  }

  public void onDetach()
  {
    super.onDetach();
    if (this.photoRequest != null)
      mapiService().abort(this.photoRequest, null, true);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if ((this.photoList != null) && (this.photoList.size() > paramInt))
    {
      paramAdapterView = new ArrayList();
      paramAdapterView.add(new BasicNameValuePair("shopid", this.shopId + ""));
      statisticsEvent("shopinfow", "shopinfow_photo_item", "" + paramInt, 0, paramAdapterView);
      ArrayList localArrayList = new ArrayList();
      Object localObject = getActivity();
      paramAdapterView = null;
      if ((localObject instanceof WeddingShopPhotoGalleryActivity))
      {
        paramAdapterView = (WeddingShopPhotoGalleryActivity)localObject;
        localArrayList.add(paramAdapterView.dpObjShop());
        paramAdapterView = DPObjectUtils.getShopFullName(paramAdapterView.dpObjShop());
      }
      localObject = new Intent("android.intent.action.VIEW", Uri.parse("dianping://showphoto"));
      ((Intent)localObject).putExtra("pageList", this.photoList);
      ((Intent)localObject).putExtra("position", paramInt);
      ((Intent)localObject).putExtra("arrShopObjs", localArrayList);
      ((Intent)localObject).putExtra("name", paramAdapterView);
      paramAdapterView = (NetworkImageView)paramView.findViewById(R.id.img_shop_photo);
      if ((BitmapDrawable)paramAdapterView.getDrawable() != null)
      {
        paramView = new ByteArrayOutputStream();
        ((BitmapDrawable)paramAdapterView.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, paramView);
        ((Intent)localObject).putExtra("currentbitmap", paramView.toByteArray());
      }
      startActivity((Intent)localObject);
    }
  }

  public void onRequestFailed(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (getActivity() == null);
    while (true)
    {
      return;
      this.isTaskRunning = false;
      this.photoRequest = null;
      paramMApiRequest = null;
      try
      {
        paramMApiResponse = paramMApiResponse.message().content();
        paramMApiRequest = paramMApiResponse;
        label32: if (paramMApiRequest == null)
          continue;
        this.errorMsg = paramMApiRequest;
        this.photoAdapter.notifyDataSetChanged();
        return;
      }
      catch (java.lang.Exception paramMApiResponse)
      {
        break label32;
      }
    }
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (paramMApiRequest != this.photoRequest)
      return;
    this.isTaskRunning = false;
    paramMApiResponse = (DPObject)paramMApiResponse.result();
    this.isEnd = paramMApiResponse.getBoolean("IsEnd");
    paramMApiRequest = paramMApiResponse.getString("CategoryDesc");
    this.start = paramMApiResponse.getInt("NextStartIndex");
    this.coverStyleType = paramMApiResponse.getInt("CoverStyleType");
    paramMApiResponse = paramMApiResponse.getArray("List");
    int i = 0;
    while (i < paramMApiResponse.length)
    {
      this.photoList.add(paramMApiResponse[i]);
      i += 1;
    }
    this.photoAdapter.notifyDataSetChanged();
    ((HomeDesignProductListActivity)getActivity()).updateTitleByDesc(paramMApiRequest);
  }

  public void setArgument(Bundle paramBundle)
  {
    if (paramBundle == null)
      return;
    this.shopId = paramBundle.getInt("shopId");
    this.shop = ((DPObject)paramBundle.getParcelable("shop"));
    this.categoryDesc = paramBundle.getString("categoryDesc");
    this.isEmptySource = paramBundle.getBoolean("isEmptySource");
    this.photoAdapter.notifyDataSetChanged();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.wed.home.fragment.HomeDesignProductPhotoGalleryFragment
 * JD-Core Version:    0.6.0
 */