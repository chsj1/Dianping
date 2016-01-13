package com.dianping.base.widget;

public class SectionListItem
{
  public Object item;
  public String section;

  public SectionListItem(Object paramObject, String paramString)
  {
    this.item = paramObject;
    this.section = paramString;
  }

  public String toString()
  {
    return this.item.toString();
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.SectionListItem
 * JD-Core Version:    0.6.0
 */