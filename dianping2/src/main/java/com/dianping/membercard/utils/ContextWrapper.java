package com.dianping.membercard.utils;

import android.content.Context;
import com.dianping.app.DPFragment;
import com.dianping.base.app.NovaActivity;

public class ContextWrapper
{
  private NovaActivity activity;
  private Object context;
  private ContextType contextType;
  private DPFragment fragment;

  public ContextWrapper(Object paramObject)
  {
    this.context = paramObject;
    if ((paramObject != null) && ((paramObject instanceof NovaActivity)))
    {
      this.activity = ((NovaActivity)paramObject);
      this.contextType = ContextType.NOVA_ACTIVITY;
    }
    do
      return;
    while ((paramObject == null) || (!(paramObject instanceof DPFragment)));
    this.fragment = ((DPFragment)paramObject);
    this.activity = ((NovaActivity)this.fragment.getActivity());
    this.contextType = ContextType.DPFragment;
  }

  public NovaActivity getActivity()
  {
    if (isContextActivity())
      return this.activity;
    if (isContextFragment())
      return (NovaActivity)this.fragment.getActivity();
    return null;
  }

  public Context getContext()
  {
    if (isContextActivity())
      return this.activity;
    return this.fragment.getActivity();
  }

  public DPFragment getFragment()
  {
    return this.fragment;
  }

  public Object getOrignalContext()
  {
    return this.context;
  }

  public boolean isContextActivity()
  {
    return this.contextType == ContextType.NOVA_ACTIVITY;
  }

  public boolean isContextFragment()
  {
    return this.contextType == ContextType.DPFragment;
  }

  private static enum ContextType
  {
    static
    {
      DPFragment = new ContextType("DPFragment", 1);
      $VALUES = new ContextType[] { NOVA_ACTIVITY, DPFragment };
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.utils.ContextWrapper
 * JD-Core Version:    0.6.0
 */