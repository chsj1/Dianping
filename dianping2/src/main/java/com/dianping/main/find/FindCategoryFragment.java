package com.dianping.main.find;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import com.dianping.adapter.BasicAdapter;
import com.dianping.app.DPFragment;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.CategoryGridView;
import com.dianping.base.widget.CategoryGridView.OnItemClickListener;
import com.dianping.util.BitmapUtils;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import com.dianping.widget.view.NovaTextView;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class FindCategoryFragment extends DPFragment
{
  private static final String ARG_SCHEMA = "schema";
  private static final int COLUMN_COUNT = 4;
  private ArrayList<DPObject> childCategory = new ArrayList();
  private ArrayList<DPObject> parentCategory = new ArrayList();
  private DPObject[] recommendCategoryList = new DPObject[0];
  private String schema;
  private ScrollView scrollView;

  private View createCategoryListView(Context paramContext, ViewGroup paramViewGroup)
  {
    paramViewGroup.removeAllViews();
    LinearLayout localLinearLayout = new LinearLayout(paramContext);
    localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    localLinearLayout.setOrientation(1);
    localLinearLayout.addView(createRecommendCategoryListView(paramContext, paramViewGroup));
    int i = 0;
    while (i < this.parentCategory.size())
    {
      View localView = LayoutInflater.from(paramContext).inflate(R.layout.more_cateogry_item, paramViewGroup, false);
      TextView localTextView = (TextView)localView.findViewById(R.id.parent_category);
      NetworkImageView localNetworkImageView = (NetworkImageView)localView.findViewById(R.id.icon);
      CategoryGridView localCategoryGridView = (CategoryGridView)localView.findViewById(R.id.category_grid);
      ArrayList localArrayList1 = findChildrenCategoryWithLimit(((DPObject)this.parentCategory.get(i)).getInt("ID"), 2147483647, 0);
      int k = localArrayList1.size();
      if (localArrayList1.size() % 4 != 0)
      {
        int j = 0;
        while (j < 4 - k % 4)
        {
          localArrayList1.add(new DPObject());
          j += 1;
        }
      }
      ArrayList localArrayList2 = new ArrayList();
      localArrayList2.addAll(localArrayList1);
      localCategoryGridView.setAdapter(new SubCategoryAdapter(localArrayList2, localTextView, localNetworkImageView, ((DPObject)this.parentCategory.get(i)).getString("Name"), getLocalImage(String.valueOf(((DPObject)this.parentCategory.get(i)).getInt("ID")))));
      localCategoryGridView.setOnItemClickListener(new CategoryGridView.OnItemClickListener()
      {
        public void onItemClick(CategoryGridView paramCategoryGridView, View paramView, int paramInt, long paramLong)
        {
          paramCategoryGridView = (DPObject)paramCategoryGridView.getAdapter().getItem(paramInt);
          if (!TextUtils.isEmpty(paramCategoryGridView.getString("Schema")))
          {
            paramCategoryGridView = new Intent("android.intent.action.VIEW", Uri.parse(paramCategoryGridView.getString("Schema")));
            FindCategoryFragment.this.startActivity(paramCategoryGridView);
          }
          do
            return;
          while (TextUtils.isEmpty(paramCategoryGridView.getString("Name")));
          if (FindCategoryFragment.this.schema.contains("morecategory"))
          {
            FindCategoryFragment.this.startActivity("dianping://localshoplist?categoryid=" + paramCategoryGridView.getInt("ID"));
            return;
          }
          paramCategoryGridView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://categoryshoplist?categoryid=" + paramCategoryGridView.getInt("ID")));
          paramCategoryGridView.putExtra("IsFromCategoryPage", true);
          FindCategoryFragment.this.startActivity(paramCategoryGridView);
        }
      });
      localLinearLayout.addView(localView);
      i += 1;
    }
    return localLinearLayout;
  }

  private Bitmap getLocalImage(String paramString)
  {
    Object localObject2 = null;
    Object localObject1 = null;
    try
    {
      Object localObject3 = getLocalCacheInputStream(paramString);
      paramString = (String)localObject3;
      if (localObject3 == null)
      {
        localObject1 = localObject3;
        localObject2 = localObject3;
        paramString = getContext().getAssets().open("more_category_icons/more_icon_none.png");
      }
      if (paramString != null)
      {
        localObject1 = paramString;
        localObject2 = paramString;
        localObject3 = BitmapUtils.decodeSampledBitmapFromStream(Bitmap.Config.RGB_565, paramString, ViewUtils.getScreenWidthPixels(getContext()), ViewUtils.getScreenHeightPixels(getContext()));
        if (paramString != null);
        try
        {
          paramString.close();
          return localObject3;
        }
        catch (IOException paramString)
        {
          while (true)
            paramString.printStackTrace();
        }
      }
      if (paramString != null);
      try
      {
        paramString.close();
        return null;
      }
      catch (IOException paramString)
      {
        while (true)
          paramString.printStackTrace();
      }
    }
    catch (IOException paramString)
    {
      while (true)
      {
        localObject2 = localObject1;
        paramString.printStackTrace();
        if (localObject1 == null)
          continue;
        try
        {
          ((InputStream)localObject1).close();
        }
        catch (IOException paramString)
        {
          paramString.printStackTrace();
        }
      }
    }
    finally
    {
      if (localObject2 == null);
    }
    try
    {
      ((InputStream)localObject2).close();
      throw paramString;
    }
    catch (IOException localIOException)
    {
      while (true)
        localIOException.printStackTrace();
    }
  }

  public static FindCategoryFragment newInstance(String paramString)
  {
    FindCategoryFragment localFindCategoryFragment = new FindCategoryFragment();
    Bundle localBundle = new Bundle();
    localBundle.putString("schema", paramString);
    localFindCategoryFragment.setArguments(localBundle);
    return localFindCategoryFragment;
  }

  public View createRecommendCategoryListView(Context paramContext, ViewGroup paramViewGroup)
  {
    paramContext = LayoutInflater.from(paramContext).inflate(R.layout.more_cateogry_item, paramViewGroup, false);
    paramViewGroup = (TextView)paramContext.findViewById(R.id.parent_category);
    NetworkImageView localNetworkImageView = (NetworkImageView)paramContext.findViewById(R.id.icon);
    CategoryGridView localCategoryGridView = (CategoryGridView)paramContext.findViewById(R.id.category_grid);
    ArrayList localArrayList = new ArrayList();
    localArrayList.addAll(Arrays.asList(this.recommendCategoryList));
    int j = localArrayList.size();
    if (localArrayList.size() % 4 != 0)
    {
      int i = 0;
      while (i < 4 - j % 4)
      {
        localArrayList.add(new DPObject());
        i += 1;
      }
    }
    localCategoryGridView.setAdapter(new SubCategoryAdapter(localArrayList, paramViewGroup, localNetworkImageView, "热门分类", getLocalImage(String.valueOf(-2147483648))));
    localCategoryGridView.setOnItemClickListener(new CategoryGridView.OnItemClickListener()
    {
      public void onItemClick(CategoryGridView paramCategoryGridView, View paramView, int paramInt, long paramLong)
      {
        paramCategoryGridView = (DPObject)paramCategoryGridView.getAdapter().getItem(paramInt);
        if (!TextUtils.isEmpty(paramCategoryGridView.getString("Schema")))
        {
          paramCategoryGridView = new Intent("android.intent.action.VIEW", Uri.parse(paramCategoryGridView.getString("Schema")));
          FindCategoryFragment.this.startActivity(paramCategoryGridView);
        }
        do
          return;
        while (TextUtils.isEmpty(paramCategoryGridView.getString("Name")));
        if (FindCategoryFragment.this.schema.contains("morecategory"))
        {
          FindCategoryFragment.this.startActivity("dianping://localshoplist?categoryid=" + paramCategoryGridView.getInt("ID"));
          return;
        }
        paramCategoryGridView = new Intent("android.intent.action.VIEW", Uri.parse("dianping://categoryshoplist?categoryid=" + paramCategoryGridView.getInt("ID")));
        paramCategoryGridView.putExtra("IsFromCategoryPage", true);
        FindCategoryFragment.this.startActivity(paramCategoryGridView);
      }
    });
    return paramContext;
  }

  public ArrayList<DPObject> findChildrenCategoryWithLimit(int paramInt1, int paramInt2, int paramInt3)
  {
    int j = 0;
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    while (true)
    {
      if (i < this.childCategory.size())
      {
        DPObject localDPObject = (DPObject)this.childCategory.get(i);
        k = j;
        if (localDPObject.getInt("ParentID") == paramInt1)
        {
          if (j < paramInt3)
            break label86;
          localArrayList.add(localDPObject);
        }
      }
      label86: for (int k = j; localArrayList.size() >= paramInt2; k = j + 1)
        return localArrayList;
      i += 1;
      j = k;
    }
  }

  protected InputStream getLocalCacheInputStream(String paramString)
  {
    try
    {
      InputStream localInputStream = getContext().getAssets().open("more_category_icons/more_icon_" + paramString + ".png");
      return localInputStream;
    }
    catch (IOException localIOException)
    {
      Log.d("home_icons not exist:" + paramString);
    }
    return null;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.schema = getArguments().getString("schema");
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(R.layout.find_category_layout, paramViewGroup, false);
    this.scrollView = ((ScrollView)paramLayoutInflater.findViewById(R.id.scrollview));
    this.scrollView.addView(createCategoryListView(this.scrollView.getContext(), this.scrollView));
    return paramLayoutInflater;
  }

  public void setCategoryRecom(DPObject paramDPObject)
  {
    if (paramDPObject == null);
    do
    {
      DPObject[] arrayOfDPObject;
      do
      {
        do
        {
          return;
          arrayOfDPObject = paramDPObject.getArray("CategoryList");
        }
        while ((arrayOfDPObject == null) || (arrayOfDPObject.length == 0));
        this.recommendCategoryList = paramDPObject.getArray("CategoryRecomList");
      }
      while ((this.recommendCategoryList == null) || (this.recommendCategoryList.length == 0));
      int j = arrayOfDPObject.length;
      int i = 0;
      if (i >= j)
        continue;
      paramDPObject = arrayOfDPObject[i];
      if (paramDPObject.getInt("ParentID") == 0)
        this.parentCategory.add(paramDPObject);
      while (true)
      {
        i += 1;
        break;
        this.childCategory.add(paramDPObject);
      }
    }
    while (this.scrollView == null);
    this.scrollView.addView(createCategoryListView(this.scrollView.getContext(), this.scrollView));
  }

  private class SubCategoryAdapter extends BasicAdapter
  {
    private Bitmap bitmap;
    private int columnCount = 4;
    private NetworkImageView iconView;
    private ArrayList<DPObject> subCategory;
    String textString;
    private TextView tittleView;

    public SubCategoryAdapter(TextView paramNetworkImageView, NetworkImageView paramString, String paramBitmap, Bitmap arg5)
    {
      this.subCategory = paramNetworkImageView;
      Object localObject1;
      this.textString = localObject1;
      this.tittleView = paramString;
      this.iconView = paramBitmap;
      Object localObject2;
      this.bitmap = localObject2;
    }

    public int getCount()
    {
      return this.subCategory.size();
    }

    public DPObject getItem(int paramInt)
    {
      return (DPObject)this.subCategory.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return ((DPObject)this.subCategory.get(paramInt)).getInt("ID");
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramView = getItem(paramInt);
      String str = paramView.getString("Name");
      int i = paramView.getInt("Label");
      View localView2;
      if (paramInt == 0)
      {
        paramView = new TableRow(paramViewGroup.getContext());
        paramView.setBackgroundResource(R.drawable.shape_top_corner_no_bottom_line);
        paramViewGroup = (ViewGroup)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.find_category_item, (TableRow)paramView, false);
        localObject = (NovaTextView)paramViewGroup.findViewById(R.id.tv_cell_text);
        ((NovaTextView)localObject).setText(str);
        localView1 = paramViewGroup.findViewById(R.id.ic_new);
        localView1.setVisibility(8);
        localView2 = paramViewGroup.findViewById(R.id.ic_hot);
        localView2.setVisibility(8);
        if ("热门分类".equals(this.textString))
        {
          ((NovaTextView)localObject).gaUserInfo.category_id = Integer.valueOf(0);
          ((NovaTextView)localObject).setGAString("classification", str, 0);
          if (i == 2)
            localView1.setVisibility(0);
        }
        while (true)
        {
          if (getItem(paramInt).getBoolean("Highlight"))
            ((NovaTextView)localObject).setTextColor(FindCategoryFragment.this.getResources().getColor(R.color.tuan_common_orange));
          paramViewGroup.setBackgroundResource(R.drawable.more_category_table_view_item);
          ((TableRow)paramView).addView(paramViewGroup);
          this.tittleView.setText(this.textString);
          this.iconView.setLocalBitmap(this.bitmap);
          return paramView;
          if (i != 1)
            continue;
          localView2.setVisibility(0);
          continue;
          ((NovaTextView)localObject).gaUserInfo.category_id = Integer.valueOf(1);
          if ("全部".equals(str))
          {
            ((NovaTextView)localObject).setGAString("classification", str + this.textString, 1);
            continue;
          }
          ((NovaTextView)localObject).setGAString("classification", str, 1);
        }
      }
      if ((paramInt % this.columnCount == 0) && (paramInt == getCount() - this.columnCount))
      {
        paramView = new TableRow(paramViewGroup.getContext());
        paramView.setBackgroundResource(R.drawable.shape_bottom_corner_no_top_line);
        paramViewGroup = (ViewGroup)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.find_category_item, (TableRow)paramView, false);
        localObject = (NovaTextView)paramViewGroup.findViewById(R.id.tv_cell_text);
        ((NovaTextView)localObject).setText(str);
        localView1 = paramViewGroup.findViewById(R.id.ic_new);
        localView1.setVisibility(8);
        localView2 = paramViewGroup.findViewById(R.id.ic_hot);
        localView2.setVisibility(8);
        if ("热门分类".equals(this.textString))
        {
          ((NovaTextView)localObject).gaUserInfo.category_id = Integer.valueOf(0);
          ((NovaTextView)localObject).setGAString("classification", str, 0);
          if (i == 2)
            localView1.setVisibility(0);
        }
        while (true)
        {
          if (getItem(paramInt).getBoolean("Highlight"))
            ((NovaTextView)localObject).setTextColor(FindCategoryFragment.this.getResources().getColor(R.color.tuan_common_orange));
          paramViewGroup.setBackgroundResource(R.drawable.more_category_table_view_item);
          ((TableRow)paramView).addView(paramViewGroup);
          break;
          if (i != 1)
            continue;
          localView2.setVisibility(0);
          continue;
          ((NovaTextView)localObject).gaUserInfo.category_id = Integer.valueOf(1);
          if ("全部".equals(str))
          {
            ((NovaTextView)localObject).setGAString("classification", str + this.textString, 1);
            continue;
          }
          ((NovaTextView)localObject).setGAString("classification", str, 1);
        }
      }
      if (paramInt % this.columnCount == 0)
      {
        paramView = new TableRow(paramViewGroup.getContext());
        paramView.setBackgroundResource(R.drawable.shape_no_corner_without_bottom);
        paramViewGroup = (ViewGroup)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.find_category_item, (TableRow)paramView, false);
        localObject = (NovaTextView)paramViewGroup.findViewById(R.id.tv_cell_text);
        ((NovaTextView)localObject).setText(str);
        localView1 = paramViewGroup.findViewById(R.id.ic_new);
        localView1.setVisibility(8);
        localView2 = paramViewGroup.findViewById(R.id.ic_hot);
        localView2.setVisibility(8);
        if ("热门分类".equals(this.textString))
        {
          ((NovaTextView)localObject).gaUserInfo.category_id = Integer.valueOf(0);
          ((NovaTextView)localObject).setGAString("classification", str, 0);
          if (i == 2)
            localView1.setVisibility(0);
        }
        while (true)
        {
          if (getItem(paramInt).getBoolean("Highlight"))
            ((NovaTextView)localObject).setTextColor(FindCategoryFragment.this.getResources().getColor(R.color.tuan_common_orange));
          paramViewGroup.setBackgroundResource(R.drawable.more_category_table_view_item);
          ((TableRow)paramView).addView(paramViewGroup);
          break;
          if (i != 1)
            continue;
          localView2.setVisibility(0);
          continue;
          ((NovaTextView)localObject).gaUserInfo.category_id = Integer.valueOf(1);
          if ("全部".equals(str))
          {
            ((NovaTextView)localObject).setGAString("classification", str + this.textString, 1);
            continue;
          }
          ((NovaTextView)localObject).setGAString("classification", str, 1);
        }
      }
      paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.find_category_item, ((CategoryGridView)paramViewGroup).getCurRow(), false);
      paramViewGroup = (NovaTextView)paramView.findViewById(R.id.tv_cell_text);
      paramViewGroup.setText(str);
      Object localObject = paramView.findViewById(R.id.ic_new);
      ((View)localObject).setVisibility(8);
      View localView1 = paramView.findViewById(R.id.ic_hot);
      localView1.setVisibility(8);
      if ("热门分类".equals(this.textString))
      {
        paramViewGroup.gaUserInfo.category_id = Integer.valueOf(0);
        paramViewGroup.setGAString("classification", str, 0);
        if (i == 2)
          ((View)localObject).setVisibility(0);
      }
      while (true)
      {
        if (getItem(paramInt).getBoolean("Highlight"))
          paramViewGroup.setTextColor(FindCategoryFragment.this.getResources().getColor(R.color.tuan_common_orange));
        paramView.setBackgroundResource(R.drawable.more_category_table_view_item);
        break;
        if (i != 1)
          continue;
        localView1.setVisibility(0);
        continue;
        paramViewGroup.gaUserInfo.category_id = Integer.valueOf(1);
        if ("全部".equals(str))
        {
          paramViewGroup.setGAString("classification", str + this.textString, 1);
          continue;
        }
        paramViewGroup.setGAString("classification", str, 1);
      }
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.find.FindCategoryFragment
 * JD-Core Version:    0.6.0
 */