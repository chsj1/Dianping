package android.support.v4.app;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;

public abstract class FragmentContainer
{
  @Nullable
  public abstract View onFindViewById(@IdRes int paramInt);

  public abstract boolean onHasView();
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     android.support.v4.app.FragmentContainer
 * JD-Core Version:    0.6.0
 */