package android.support.v7.widget;

import android.support.v4.util.Pools.Pool;
import android.support.v4.util.Pools.SimplePool;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class AdapterHelper
  implements OpReorderer.Callback
{
  private static final boolean DEBUG = false;
  static final int POSITION_TYPE_INVISIBLE = 0;
  static final int POSITION_TYPE_NEW_OR_LAID_OUT = 1;
  private static final String TAG = "AHT";
  final Callback mCallback;
  final boolean mDisableRecycler;
  Runnable mOnItemProcessedCallback;
  final OpReorderer mOpReorderer;
  final ArrayList<UpdateOp> mPendingUpdates = new ArrayList();
  final ArrayList<UpdateOp> mPostponedList = new ArrayList();
  private Pools.Pool<UpdateOp> mUpdateOpPool = new Pools.SimplePool(30);

  AdapterHelper(Callback paramCallback)
  {
    this(paramCallback, false);
  }

  AdapterHelper(Callback paramCallback, boolean paramBoolean)
  {
    this.mCallback = paramCallback;
    this.mDisableRecycler = paramBoolean;
    this.mOpReorderer = new OpReorderer(this);
  }

  private void applyAdd(UpdateOp paramUpdateOp)
  {
    postponeAndUpdateViewHolders(paramUpdateOp);
  }

  private void applyMove(UpdateOp paramUpdateOp)
  {
    postponeAndUpdateViewHolders(paramUpdateOp);
  }

  private void applyRemove(UpdateOp paramUpdateOp)
  {
    int i2 = paramUpdateOp.positionStart;
    int n = 0;
    int m = paramUpdateOp.positionStart + paramUpdateOp.itemCount;
    int i1 = -1;
    int i = paramUpdateOp.positionStart;
    if (i < m)
    {
      int k = 0;
      int j = 0;
      if ((this.mCallback.findViewHolder(i) != null) || (canFindInPreLayout(i)))
      {
        if (i1 == 0)
        {
          dispatchAndUpdateViewHolders(obtainUpdateOp(1, i2, n, null));
          j = 1;
        }
        i1 = 1;
        k = j;
        j = i1;
        label94: if (k == 0)
          break label158;
        i -= n;
        m -= n;
      }
      label158: for (k = 1; ; k = n + 1)
      {
        i += 1;
        n = k;
        i1 = j;
        break;
        if (i1 == 1)
        {
          postponeAndUpdateViewHolders(obtainUpdateOp(1, i2, n, null));
          k = 1;
        }
        j = 0;
        break label94;
      }
    }
    UpdateOp localUpdateOp = paramUpdateOp;
    if (n != paramUpdateOp.itemCount)
    {
      recycleUpdateOp(paramUpdateOp);
      localUpdateOp = obtainUpdateOp(1, i2, n, null);
    }
    if (i1 == 0)
    {
      dispatchAndUpdateViewHolders(localUpdateOp);
      return;
    }
    postponeAndUpdateViewHolders(localUpdateOp);
  }

  private void applyUpdate(UpdateOp paramUpdateOp)
  {
    int k = paramUpdateOp.positionStart;
    int j = 0;
    int i3 = paramUpdateOp.positionStart;
    int i4 = paramUpdateOp.itemCount;
    int i2 = -1;
    int i = paramUpdateOp.positionStart;
    if (i < i3 + i4)
    {
      int n;
      int m;
      if ((this.mCallback.findViewHolder(i) != null) || (canFindInPreLayout(i)))
      {
        n = j;
        int i1 = k;
        if (i2 == 0)
        {
          dispatchAndUpdateViewHolders(obtainUpdateOp(2, k, j, paramUpdateOp.payload));
          n = 0;
          i1 = i;
        }
        m = 1;
        k = i1;
      }
      while (true)
      {
        j = n + 1;
        i += 1;
        i2 = m;
        break;
        n = j;
        m = k;
        if (i2 == 1)
        {
          postponeAndUpdateViewHolders(obtainUpdateOp(2, k, j, paramUpdateOp.payload));
          n = 0;
          m = i;
        }
        j = 0;
        k = m;
        m = j;
      }
    }
    Object localObject = paramUpdateOp;
    if (j != paramUpdateOp.itemCount)
    {
      localObject = paramUpdateOp.payload;
      recycleUpdateOp(paramUpdateOp);
      localObject = obtainUpdateOp(2, k, j, localObject);
    }
    if (i2 == 0)
    {
      dispatchAndUpdateViewHolders((UpdateOp)localObject);
      return;
    }
    postponeAndUpdateViewHolders((UpdateOp)localObject);
  }

  private boolean canFindInPreLayout(int paramInt)
  {
    int k = this.mPostponedList.size();
    int i = 0;
    while (i < k)
    {
      UpdateOp localUpdateOp = (UpdateOp)this.mPostponedList.get(i);
      if (localUpdateOp.cmd == 3)
      {
        if (findPositionOffset(localUpdateOp.itemCount, i + 1) == paramInt)
          return true;
      }
      else if (localUpdateOp.cmd == 0)
      {
        int m = localUpdateOp.positionStart;
        int n = localUpdateOp.itemCount;
        int j = localUpdateOp.positionStart;
        while (true)
        {
          if (j >= m + n)
            break label111;
          if (findPositionOffset(j, i + 1) == paramInt)
            break;
          j += 1;
        }
      }
      label111: i += 1;
    }
    return false;
  }

  private void dispatchAndUpdateViewHolders(UpdateOp paramUpdateOp)
  {
    if ((paramUpdateOp.cmd == 0) || (paramUpdateOp.cmd == 3))
      throw new IllegalArgumentException("should not dispatch add or move for pre layout");
    int i1 = updatePositionWithPostponed(paramUpdateOp.positionStart, paramUpdateOp.cmd);
    int n = 1;
    int i = paramUpdateOp.positionStart;
    int k;
    int m;
    label105: int i2;
    switch (paramUpdateOp.cmd)
    {
    default:
      throw new IllegalArgumentException("op should be remove or update." + paramUpdateOp);
    case 2:
      k = 1;
      m = 1;
      if (m >= paramUpdateOp.itemCount)
        break label296;
      i2 = updatePositionWithPostponed(paramUpdateOp.positionStart + k * m, paramUpdateOp.cmd);
      j = 0;
      switch (paramUpdateOp.cmd)
      {
      default:
        if (j == 0);
      case 2:
      case 1:
      }
    case 1:
    }
    for (int j = n + 1; ; j = n)
    {
      m += 1;
      n = j;
      break label105;
      k = 0;
      break;
      if (i2 == i1 + 1);
      for (j = 1; ; j = 0)
        break;
      if (i2 == i1);
      for (j = 1; ; j = 0)
        break;
      localObject = obtainUpdateOp(paramUpdateOp.cmd, i1, n, paramUpdateOp.payload);
      dispatchFirstPassAndUpdateViewHolders((UpdateOp)localObject, i);
      recycleUpdateOp((UpdateOp)localObject);
      j = i;
      if (paramUpdateOp.cmd == 2)
        j = i + n;
      i1 = i2;
      n = 1;
      i = j;
    }
    label296: Object localObject = paramUpdateOp.payload;
    recycleUpdateOp(paramUpdateOp);
    if (n > 0)
    {
      paramUpdateOp = obtainUpdateOp(paramUpdateOp.cmd, i1, n, localObject);
      dispatchFirstPassAndUpdateViewHolders(paramUpdateOp, i);
      recycleUpdateOp(paramUpdateOp);
    }
  }

  private void postponeAndUpdateViewHolders(UpdateOp paramUpdateOp)
  {
    this.mPostponedList.add(paramUpdateOp);
    switch (paramUpdateOp.cmd)
    {
    default:
      throw new IllegalArgumentException("Unknown update op type for " + paramUpdateOp);
    case 0:
      this.mCallback.offsetPositionsForAdd(paramUpdateOp.positionStart, paramUpdateOp.itemCount);
      return;
    case 3:
      this.mCallback.offsetPositionsForMove(paramUpdateOp.positionStart, paramUpdateOp.itemCount);
      return;
    case 1:
      this.mCallback.offsetPositionsForRemovingLaidOutOrNewView(paramUpdateOp.positionStart, paramUpdateOp.itemCount);
      return;
    case 2:
    }
    this.mCallback.markViewHoldersUpdated(paramUpdateOp.positionStart, paramUpdateOp.itemCount, paramUpdateOp.payload);
  }

  private int updatePositionWithPostponed(int paramInt1, int paramInt2)
  {
    int i = this.mPostponedList.size() - 1;
    int j = paramInt1;
    UpdateOp localUpdateOp;
    if (i >= 0)
    {
      localUpdateOp = (UpdateOp)this.mPostponedList.get(i);
      int k;
      if (localUpdateOp.cmd == 3)
        if (localUpdateOp.positionStart < localUpdateOp.itemCount)
        {
          k = localUpdateOp.positionStart;
          paramInt1 = localUpdateOp.itemCount;
          label62: if ((j < k) || (j > paramInt1))
            break label187;
          if (k != localUpdateOp.positionStart)
            break label147;
          if (paramInt2 != 0)
            break label129;
          localUpdateOp.itemCount += 1;
          label98: paramInt1 = j + 1;
        }
      while (true)
      {
        i -= 1;
        j = paramInt1;
        break;
        k = localUpdateOp.itemCount;
        paramInt1 = localUpdateOp.positionStart;
        break label62;
        label129: if (paramInt2 != 1)
          break label98;
        localUpdateOp.itemCount -= 1;
        break label98;
        label147: if (paramInt2 == 0)
          localUpdateOp.positionStart += 1;
        while (true)
        {
          paramInt1 = j - 1;
          break;
          if (paramInt2 != 1)
            continue;
          localUpdateOp.positionStart -= 1;
        }
        label187: paramInt1 = j;
        if (j >= localUpdateOp.positionStart)
          continue;
        if (paramInt2 == 0)
        {
          localUpdateOp.positionStart += 1;
          localUpdateOp.itemCount += 1;
          paramInt1 = j;
          continue;
        }
        paramInt1 = j;
        if (paramInt2 != 1)
          continue;
        localUpdateOp.positionStart -= 1;
        localUpdateOp.itemCount -= 1;
        paramInt1 = j;
        continue;
        if (localUpdateOp.positionStart <= j)
        {
          if (localUpdateOp.cmd == 0)
          {
            paramInt1 = j - localUpdateOp.itemCount;
            continue;
          }
          paramInt1 = j;
          if (localUpdateOp.cmd != 1)
            continue;
          paramInt1 = j + localUpdateOp.itemCount;
          continue;
        }
        if (paramInt2 == 0)
        {
          localUpdateOp.positionStart += 1;
          paramInt1 = j;
          continue;
        }
        paramInt1 = j;
        if (paramInt2 != 1)
          continue;
        localUpdateOp.positionStart -= 1;
        paramInt1 = j;
      }
    }
    paramInt1 = this.mPostponedList.size() - 1;
    if (paramInt1 >= 0)
    {
      localUpdateOp = (UpdateOp)this.mPostponedList.get(paramInt1);
      if (localUpdateOp.cmd == 3)
        if ((localUpdateOp.itemCount == localUpdateOp.positionStart) || (localUpdateOp.itemCount < 0))
        {
          this.mPostponedList.remove(paramInt1);
          recycleUpdateOp(localUpdateOp);
        }
      while (true)
      {
        paramInt1 -= 1;
        break;
        if (localUpdateOp.itemCount > 0)
          continue;
        this.mPostponedList.remove(paramInt1);
        recycleUpdateOp(localUpdateOp);
      }
    }
    return j;
  }

  AdapterHelper addUpdateOp(UpdateOp[] paramArrayOfUpdateOp)
  {
    Collections.addAll(this.mPendingUpdates, paramArrayOfUpdateOp);
    return this;
  }

  public int applyPendingUpdatesToPosition(int paramInt)
  {
    int m = this.mPendingUpdates.size();
    int k = 0;
    int i = paramInt;
    paramInt = i;
    UpdateOp localUpdateOp;
    if (k < m)
    {
      localUpdateOp = (UpdateOp)this.mPendingUpdates.get(k);
      paramInt = i;
      switch (localUpdateOp.cmd)
      {
      default:
        paramInt = i;
      case 2:
      case 0:
      case 1:
      case 3:
      }
    }
    while (true)
    {
      k += 1;
      i = paramInt;
      break;
      paramInt = i;
      if (localUpdateOp.positionStart > i)
        continue;
      paramInt = i + localUpdateOp.itemCount;
      continue;
      paramInt = i;
      if (localUpdateOp.positionStart > i)
        continue;
      if (localUpdateOp.positionStart + localUpdateOp.itemCount > i)
      {
        paramInt = -1;
        return paramInt;
      }
      paramInt = i - localUpdateOp.itemCount;
      continue;
      if (localUpdateOp.positionStart == i)
      {
        paramInt = localUpdateOp.itemCount;
        continue;
      }
      int j = i;
      if (localUpdateOp.positionStart < i)
        j = i - 1;
      paramInt = j;
      if (localUpdateOp.itemCount > j)
        continue;
      paramInt = j + 1;
    }
  }

  void consumePostponedUpdates()
  {
    int j = this.mPostponedList.size();
    int i = 0;
    while (i < j)
    {
      this.mCallback.onDispatchSecondPass((UpdateOp)this.mPostponedList.get(i));
      i += 1;
    }
    recycleUpdateOpsAndClearList(this.mPostponedList);
  }

  void consumeUpdatesInOnePass()
  {
    consumePostponedUpdates();
    int j = this.mPendingUpdates.size();
    int i = 0;
    if (i < j)
    {
      UpdateOp localUpdateOp = (UpdateOp)this.mPendingUpdates.get(i);
      switch (localUpdateOp.cmd)
      {
      default:
      case 0:
      case 1:
      case 2:
      case 3:
      }
      while (true)
      {
        if (this.mOnItemProcessedCallback != null)
          this.mOnItemProcessedCallback.run();
        i += 1;
        break;
        this.mCallback.onDispatchSecondPass(localUpdateOp);
        this.mCallback.offsetPositionsForAdd(localUpdateOp.positionStart, localUpdateOp.itemCount);
        continue;
        this.mCallback.onDispatchSecondPass(localUpdateOp);
        this.mCallback.offsetPositionsForRemovingInvisible(localUpdateOp.positionStart, localUpdateOp.itemCount);
        continue;
        this.mCallback.onDispatchSecondPass(localUpdateOp);
        this.mCallback.markViewHoldersUpdated(localUpdateOp.positionStart, localUpdateOp.itemCount, localUpdateOp.payload);
        continue;
        this.mCallback.onDispatchSecondPass(localUpdateOp);
        this.mCallback.offsetPositionsForMove(localUpdateOp.positionStart, localUpdateOp.itemCount);
      }
    }
    recycleUpdateOpsAndClearList(this.mPendingUpdates);
  }

  void dispatchFirstPassAndUpdateViewHolders(UpdateOp paramUpdateOp, int paramInt)
  {
    this.mCallback.onDispatchFirstPass(paramUpdateOp);
    switch (paramUpdateOp.cmd)
    {
    default:
      throw new IllegalArgumentException("only remove and update ops can be dispatched in first pass");
    case 1:
      this.mCallback.offsetPositionsForRemovingInvisible(paramInt, paramUpdateOp.itemCount);
      return;
    case 2:
    }
    this.mCallback.markViewHoldersUpdated(paramInt, paramUpdateOp.itemCount, paramUpdateOp.payload);
  }

  int findPositionOffset(int paramInt)
  {
    return findPositionOffset(paramInt, 0);
  }

  int findPositionOffset(int paramInt1, int paramInt2)
  {
    int k = this.mPostponedList.size();
    int j = paramInt2;
    paramInt2 = paramInt1;
    paramInt1 = paramInt2;
    UpdateOp localUpdateOp;
    if (j < k)
    {
      localUpdateOp = (UpdateOp)this.mPostponedList.get(j);
      if (localUpdateOp.cmd == 3)
        if (localUpdateOp.positionStart == paramInt2)
          paramInt1 = localUpdateOp.itemCount;
    }
    while (true)
    {
      j += 1;
      paramInt2 = paramInt1;
      break;
      int i = paramInt2;
      if (localUpdateOp.positionStart < paramInt2)
        i = paramInt2 - 1;
      paramInt1 = i;
      if (localUpdateOp.itemCount > i)
        continue;
      paramInt1 = i + 1;
      continue;
      paramInt1 = paramInt2;
      if (localUpdateOp.positionStart > paramInt2)
        continue;
      if (localUpdateOp.cmd == 1)
      {
        if (paramInt2 < localUpdateOp.positionStart + localUpdateOp.itemCount)
        {
          paramInt1 = -1;
          return paramInt1;
        }
        paramInt1 = paramInt2 - localUpdateOp.itemCount;
        continue;
      }
      paramInt1 = paramInt2;
      if (localUpdateOp.cmd != 0)
        continue;
      paramInt1 = paramInt2 + localUpdateOp.itemCount;
    }
  }

  boolean hasPendingUpdates()
  {
    return this.mPendingUpdates.size() > 0;
  }

  public UpdateOp obtainUpdateOp(int paramInt1, int paramInt2, int paramInt3, Object paramObject)
  {
    UpdateOp localUpdateOp = (UpdateOp)this.mUpdateOpPool.acquire();
    if (localUpdateOp == null)
      return new UpdateOp(paramInt1, paramInt2, paramInt3, paramObject);
    localUpdateOp.cmd = paramInt1;
    localUpdateOp.positionStart = paramInt2;
    localUpdateOp.itemCount = paramInt3;
    localUpdateOp.payload = paramObject;
    return localUpdateOp;
  }

  boolean onItemRangeChanged(int paramInt1, int paramInt2, Object paramObject)
  {
    this.mPendingUpdates.add(obtainUpdateOp(2, paramInt1, paramInt2, paramObject));
    return this.mPendingUpdates.size() == 1;
  }

  boolean onItemRangeInserted(int paramInt1, int paramInt2)
  {
    this.mPendingUpdates.add(obtainUpdateOp(0, paramInt1, paramInt2, null));
    return this.mPendingUpdates.size() == 1;
  }

  boolean onItemRangeMoved(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 1;
    if (paramInt1 == paramInt2)
      return false;
    if (paramInt3 != 1)
      throw new IllegalArgumentException("Moving more than 1 item is not supported yet");
    this.mPendingUpdates.add(obtainUpdateOp(3, paramInt1, paramInt2, null));
    if (this.mPendingUpdates.size() == 1);
    while (true)
    {
      return i;
      i = 0;
    }
  }

  boolean onItemRangeRemoved(int paramInt1, int paramInt2)
  {
    this.mPendingUpdates.add(obtainUpdateOp(1, paramInt1, paramInt2, null));
    return this.mPendingUpdates.size() == 1;
  }

  void preProcess()
  {
    this.mOpReorderer.reorderOps(this.mPendingUpdates);
    int j = this.mPendingUpdates.size();
    int i = 0;
    if (i < j)
    {
      UpdateOp localUpdateOp = (UpdateOp)this.mPendingUpdates.get(i);
      switch (localUpdateOp.cmd)
      {
      default:
      case 0:
      case 1:
      case 2:
      case 3:
      }
      while (true)
      {
        if (this.mOnItemProcessedCallback != null)
          this.mOnItemProcessedCallback.run();
        i += 1;
        break;
        applyAdd(localUpdateOp);
        continue;
        applyRemove(localUpdateOp);
        continue;
        applyUpdate(localUpdateOp);
        continue;
        applyMove(localUpdateOp);
      }
    }
    this.mPendingUpdates.clear();
  }

  public void recycleUpdateOp(UpdateOp paramUpdateOp)
  {
    if (!this.mDisableRecycler)
    {
      paramUpdateOp.payload = null;
      this.mUpdateOpPool.release(paramUpdateOp);
    }
  }

  void recycleUpdateOpsAndClearList(List<UpdateOp> paramList)
  {
    int j = paramList.size();
    int i = 0;
    while (i < j)
    {
      recycleUpdateOp((UpdateOp)paramList.get(i));
      i += 1;
    }
    paramList.clear();
  }

  void reset()
  {
    recycleUpdateOpsAndClearList(this.mPendingUpdates);
    recycleUpdateOpsAndClearList(this.mPostponedList);
  }

  static abstract interface Callback
  {
    public abstract RecyclerView.ViewHolder findViewHolder(int paramInt);

    public abstract void markViewHoldersUpdated(int paramInt1, int paramInt2, Object paramObject);

    public abstract void offsetPositionsForAdd(int paramInt1, int paramInt2);

    public abstract void offsetPositionsForMove(int paramInt1, int paramInt2);

    public abstract void offsetPositionsForRemovingInvisible(int paramInt1, int paramInt2);

    public abstract void offsetPositionsForRemovingLaidOutOrNewView(int paramInt1, int paramInt2);

    public abstract void onDispatchFirstPass(AdapterHelper.UpdateOp paramUpdateOp);

    public abstract void onDispatchSecondPass(AdapterHelper.UpdateOp paramUpdateOp);
  }

  static class UpdateOp
  {
    static final int ADD = 0;
    static final int MOVE = 3;
    static final int POOL_SIZE = 30;
    static final int REMOVE = 1;
    static final int UPDATE = 2;
    int cmd;
    int itemCount;
    Object payload;
    int positionStart;

    UpdateOp(int paramInt1, int paramInt2, int paramInt3, Object paramObject)
    {
      this.cmd = paramInt1;
      this.positionStart = paramInt2;
      this.itemCount = paramInt3;
      this.payload = paramObject;
    }

    String cmdToString()
    {
      switch (this.cmd)
      {
      default:
        return "??";
      case 0:
        return "add";
      case 1:
        return "rm";
      case 2:
        return "up";
      case 3:
      }
      return "mv";
    }

    public boolean equals(Object paramObject)
    {
      if (this == paramObject);
      do
        while (true)
        {
          return true;
          if ((paramObject == null) || (getClass() != paramObject.getClass()))
            return false;
          paramObject = (UpdateOp)paramObject;
          if (this.cmd != paramObject.cmd)
            return false;
          if ((this.cmd == 3) && (Math.abs(this.itemCount - this.positionStart) == 1) && (this.itemCount == paramObject.positionStart) && (this.positionStart == paramObject.itemCount))
            continue;
          if (this.itemCount != paramObject.itemCount)
            return false;
          if (this.positionStart != paramObject.positionStart)
            return false;
          if (this.payload == null)
            break;
          if (!this.payload.equals(paramObject.payload))
            return false;
        }
      while (paramObject.payload == null);
      return false;
    }

    public int hashCode()
    {
      return (this.cmd * 31 + this.positionStart) * 31 + this.itemCount;
    }

    public String toString()
    {
      return Integer.toHexString(System.identityHashCode(this)) + "[" + cmdToString() + ",s:" + this.positionStart + "c:" + this.itemCount + ",p:" + this.payload + "]";
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     android.support.v7.widget.AdapterHelper
 * JD-Core Version:    0.6.0
 */