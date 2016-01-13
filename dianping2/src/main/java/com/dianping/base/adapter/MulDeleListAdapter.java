package com.dianping.base.adapter;

import com.dianping.adapter.BasicAdapter;
import com.dianping.archive.DPObject;
import java.util.ArrayList;

public abstract class MulDeleListAdapter extends BasicAdapter
{
  protected ArrayList<Integer> checkList = new ArrayList();
  protected int checkedSize = 0;
  protected ArrayList<DPObject> dataList = new ArrayList();
  protected boolean isEdit = false;

  public void appendCheckList(int paramInt)
  {
    int i = 0;
    while (i < paramInt)
    {
      this.checkList.add(Integer.valueOf(0));
      i += 1;
    }
  }

  public ArrayList<Integer> getCheckList()
  {
    return this.checkList;
  }

  public int getChecked(int paramInt)
  {
    if (paramInt < this.checkList.size())
      return ((Integer)this.checkList.get(paramInt)).intValue();
    return 0;
  }

  public int getCheckedSize()
  {
    return this.checkedSize;
  }

  public ArrayList<DPObject> getDataList()
  {
    return this.dataList;
  }

  public ArrayList<DPObject> getDeleteList()
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    while (i < this.dataList.size())
    {
      if (((Integer)this.checkList.get(i)).intValue() > 0)
        localArrayList.add(this.dataList.get(i));
      i += 1;
    }
    return localArrayList;
  }

  public boolean isEdit()
  {
    return this.isEdit;
  }

  public void itemBeChecked(int paramInt)
  {
    int i;
    if (getChecked(paramInt) > 0)
    {
      i = 0;
      setCheckedSize(this.checkedSize - 1);
    }
    while (true)
    {
      setChecked(paramInt, i);
      notifyDataSetChanged();
      return;
      i = 1;
      setCheckedSize(this.checkedSize + 1);
    }
  }

  public void resetCheckList()
  {
    this.checkedSize = 0;
    this.checkList.clear();
    int i = 0;
    while (i < this.dataList.size())
    {
      this.checkList.add(Integer.valueOf(0));
      i += 1;
    }
  }

  public void setChecked(int paramInt1, int paramInt2)
  {
    if (paramInt1 < this.checkList.size())
      this.checkList.set(paramInt1, Integer.valueOf(paramInt2));
  }

  public void setCheckedSize(int paramInt)
  {
    this.checkedSize = paramInt;
  }

  public void setIsEdit(boolean paramBoolean)
  {
    this.isEdit = paramBoolean;
    notifyDataSetChanged();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.adapter.MulDeleListAdapter
 * JD-Core Version:    0.6.0
 */