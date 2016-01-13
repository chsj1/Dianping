package com.dianping.base.share.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianping.base.share.action.base.BaseShare;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.ArrayList;
import java.util.List;

public class ShareToAdapter extends BaseAdapter
{
  private final Context mContext;
  private final List<BaseShare> mShareList = new ArrayList();

  public ShareToAdapter(Context paramContext, List<BaseShare> paramList)
  {
    this.mContext = paramContext;
    setData(paramList);
  }

  private void setHolder(ViewHolder paramViewHolder, BaseShare paramBaseShare)
  {
    paramViewHolder.ivShareIcon.setImageResource(paramBaseShare.getIconResId());
    paramViewHolder.tvShareText.setText(paramBaseShare.getLabel());
    paramViewHolder.layout.setGAString(paramBaseShare.getElementId());
  }

  public int getCount()
  {
    return this.mShareList.size();
  }

  public Object getItem(int paramInt)
  {
    return this.mShareList.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView == null)
    {
      paramView = LayoutInflater.from(this.mContext).inflate(R.layout.share_to_item, null);
      paramViewGroup = new ViewHolder();
      paramViewGroup.ivShareIcon = ((ImageView)paramView.findViewById(R.id.iv_share_icon));
      paramViewGroup.tvShareText = ((TextView)paramView.findViewById(R.id.tv_share_text));
      paramViewGroup.layout = ((NovaLinearLayout)paramView);
      paramView.setTag(paramViewGroup);
    }
    while (true)
    {
      setHolder(paramViewGroup, (BaseShare)this.mShareList.get(paramInt));
      return paramView;
      paramViewGroup = (ViewHolder)paramView.getTag();
    }
  }

  public void setData(List<BaseShare> paramList)
  {
    if (paramList == null);
    do
      return;
    while (paramList.isEmpty());
    this.mShareList.clear();
    this.mShareList.addAll(paramList);
  }

  public void setDataAndNotify(List<BaseShare> paramList)
  {
    setData(paramList);
    notifyDataSetChanged();
  }

  static class ViewHolder
  {
    ImageView ivShareIcon;
    NovaLinearLayout layout;
    TextView tvShareText;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.share.adapter.ShareToAdapter
 * JD-Core Version:    0.6.0
 */