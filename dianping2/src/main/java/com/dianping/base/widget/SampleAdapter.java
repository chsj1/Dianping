package com.dianping.base.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.NetworkImageView;
import java.util.ArrayList;
import java.util.Arrays;

public class SampleAdapter extends BaseAdapter
{
  private ArrayList<DPObject> mDatas = new ArrayList();
  private LayoutInflater mLayoutInflater;

  public SampleAdapter(Context paramContext, int paramInt)
  {
    this.mLayoutInflater = LayoutInflater.from(paramContext);
  }

  public void addItems(DPObject[] paramArrayOfDPObject)
  {
    this.mDatas.addAll(Arrays.asList(paramArrayOfDPObject));
  }

  public int getCount()
  {
    if (this.mDatas == null)
      return 0;
    return this.mDatas.size();
  }

  public Object getItem(int paramInt)
  {
    if ((this.mDatas == null) || (paramInt >= this.mDatas.size()))
      return null;
    return (DPObject)this.mDatas.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    DPObject localDPObject;
    String str1;
    String str2;
    String str3;
    if (paramView == null)
    {
      paramView = this.mLayoutInflater.inflate(R.layout.find_forein_city_item, paramViewGroup, false);
      paramViewGroup = new ViewHolder();
      paramViewGroup.networkImageView = ((NetworkImageView)paramView.findViewById(R.id.image));
      paramViewGroup.title = ((TextView)paramView.findViewById(R.id.title));
      paramViewGroup.content = ((TextView)paramView.findViewById(R.id.content));
      paramView.setTag(paramViewGroup);
      localDPObject = (DPObject)this.mDatas.get(paramInt);
      str1 = localDPObject.getString("Title");
      str2 = localDPObject.getString("PicUrl");
      str3 = localDPObject.getString("Content");
      if ((str1 == null) || (TextUtils.isEmpty(str1)))
        break label263;
      paramViewGroup.title.setVisibility(0);
      paramViewGroup.title.setText(str1);
      label142: if ((str3 == null) || (TextUtils.isEmpty(str3)))
        break label275;
      paramViewGroup.content.setVisibility(0);
      paramViewGroup.content.setText(str3);
      label172: if ((str2 == null) || (TextUtils.isEmpty(str2)))
        break label287;
      paramViewGroup.networkImageView.setVisibility(0);
      paramViewGroup.networkImageView.setImage(str2);
      paramViewGroup.networkImageView.setTag(R.id.image_url, str2);
    }
    while (true)
    {
      paramViewGroup.networkImageView.setImage(str2);
      paramViewGroup.title.setText(str1);
      paramViewGroup.content.setText(str3);
      paramView.setTag(R.id.dp_object, localDPObject);
      return paramView;
      paramViewGroup = (ViewHolder)paramView.getTag();
      break;
      label263: paramViewGroup.title.setVisibility(8);
      break label142;
      label275: paramViewGroup.content.setVisibility(8);
      break label172;
      label287: paramViewGroup.networkImageView.setVisibility(8);
    }
  }

  static class ViewHolder
  {
    TextView content;
    NetworkImageView networkImageView;
    TextView title;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.SampleAdapter
 * JD-Core Version:    0.6.0
 */