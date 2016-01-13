package com.dianping.base.app.loader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import com.dianping.app.Environment;
import com.dianping.loader.MyResources;
import com.dianping.util.Log;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.widget.view.NovaLinearLayout;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Map<Ljava.lang.String;Lcom.dianping.base.app.loader.AgentInfo;>;
import java.util.Set;

public abstract class GroupAgentFragment extends AgentFragment
{
  public static final int STYLE_CLICKABLE = 1;
  public static final int STYLE_FLAT_CELL = 1024;
  public static final int STYLE_INDICATOR = 256;
  public static final int STYLE_INNER_DIVIDER = 64;
  public static final int STYLE_NO_BACKGROUND = 16;
  private static final Comparator<GroupCell> cellComparator = new Comparator()
  {
    public int compare(GroupAgentFragment.GroupCell paramGroupCell1, GroupAgentFragment.GroupCell paramGroupCell2)
    {
      if (paramGroupCell1.childName.equals(paramGroupCell2.childName))
        return paramGroupCell1.name.compareTo(paramGroupCell2.name);
      return paramGroupCell1.childName.compareTo(paramGroupCell2.childName);
    }
  };
  Drawable cellArrow;
  final View.OnClickListener cellClickListener = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
      Iterator localIterator = GroupAgentFragment.this.cells.values().iterator();
      while (localIterator.hasNext())
      {
        GroupAgentFragment.GroupCell localGroupCell = (GroupAgentFragment.GroupCell)(Cell)localIterator.next();
        if (localGroupCell.view != paramView)
          continue;
        ((GroupCellAgent)localGroupCell.owner).onCellClick(localGroupCell.name, localGroupCell.view);
      }
    }
  };
  Drawable cellMaskBottom;
  Drawable cellMaskMiddle;
  Drawable cellMaskMiddleInner;
  Drawable cellMaskSingle;
  Drawable cellMaskTop;
  Drawable cellMaskTopInner;
  Rect rect = new Rect();
  MyResources res;

  private View generateInnerDivier()
  {
    FragmentActivity localFragmentActivity = getActivity();
    if (localFragmentActivity == null)
      return null;
    View localView = new View(localFragmentActivity);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 1);
    localLayoutParams.leftMargin = ViewUtils.dip2px(localFragmentActivity, 15.0F);
    localView.setBackgroundColor(getResources().getColor(R.color.inner_divider));
    localView.setLayoutParams(localLayoutParams);
    return localView;
  }

  private Map<String, AgentInfo> getDefaultAgentList()
  {
    Object localObject1 = generaterDefaultConfigAgentList();
    if ((localObject1 == null) || (((ArrayList)localObject1).size() == 0))
    {
      localObject1 = null;
      return localObject1;
    }
    Iterator localIterator = ((ArrayList)localObject1).iterator();
    do
      while (true)
      {
        if (!localIterator.hasNext())
          break label170;
        Object localObject3 = (AgentListConfig)localIterator.next();
        try
        {
          if (!((AgentListConfig)localObject3).shouldShow())
            continue;
          Object localObject2 = ((AgentListConfig)localObject3).getAgentInfoList();
          localObject1 = localObject2;
          if (localObject2 != null)
            break;
          localObject2 = new LinkedHashMap();
          localObject3 = ((AgentListConfig)localObject3).getAgentList().entrySet().iterator();
          while (true)
          {
            localObject1 = localObject2;
            if (!((Iterator)localObject3).hasNext())
              break;
            localObject1 = (Map.Entry)((Iterator)localObject3).next();
            ((Map)localObject2).put(((Map.Entry)localObject1).getKey(), new AgentInfo((Class)((Map.Entry)localObject1).getValue(), ""));
          }
        }
        catch (Exception localException)
        {
        }
      }
    while (!Environment.isDebug());
    throw new RuntimeException("there has a exception");
    label170: return (Map<String, AgentInfo>)(Map<String, AgentInfo>)(Map<String, AgentInfo>)null;
  }

  public void addCell(CellAgent paramCellAgent, String paramString, View paramView, int paramInt)
  {
    String str = getCellName(paramCellAgent, paramString);
    int i = str.indexOf('.');
    if (i < 0)
      throw new RuntimeException("cell name must be <group>.<child>");
    if (!(paramCellAgent instanceof GroupCellAgent))
      throw new RuntimeException("agent must extends GroupCellAgent");
    GroupCell localGroupCell = new GroupCell();
    localGroupCell.owner = paramCellAgent;
    localGroupCell.name = paramString;
    localGroupCell.groupName = str.substring(0, i);
    localGroupCell.childName = str.substring(i + 1);
    localGroupCell.view = paramView;
    localGroupCell.style = paramInt;
    this.cells.put(str, localGroupCell);
    notifyCellChanged();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.res = MyResources.getResource(getClass());
  }

  protected void setDefaultAgent()
  {
    Object localObject = getDefaultAgentList();
    if ((localObject == null) || (((Map)localObject).size() == 0));
    while (true)
    {
      return;
      try
      {
        localObject = ((Map)localObject).entrySet().iterator();
        while (((Iterator)localObject).hasNext())
        {
          Map.Entry localEntry = (Map.Entry)((Iterator)localObject).next();
          if (this.agents.containsKey(localEntry.getKey()))
            continue;
          this.agentList.add(localEntry.getKey());
          CellAgent localCellAgent = (CellAgent)((AgentInfo)localEntry.getValue()).agentClass.getConstructor(new Class[] { Object.class }).newInstance(new Object[] { this });
          localCellAgent.index = ((AgentInfo)localEntry.getValue()).index;
          this.agents.put(localEntry.getKey(), localCellAgent);
        }
      }
      catch (Exception localException)
      {
        Log.e(TAG, localException.toString());
      }
    }
  }

  protected void updateAgentContainer()
  {
    if (getActivity() == null)
      return;
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    int j = agentContainerView().getChildCount();
    while (i < j)
    {
      localObject1 = agentContainerView().getChildAt(i);
      if ((localObject1 instanceof CellContainer))
      {
        localArrayList.add((CellContainer)localObject1);
        ((CellContainer)localObject1).reset();
      }
      i += 1;
    }
    agentContainerView().removeAllViews();
    HashMap localHashMap = new HashMap();
    Object localObject3 = this.cells.values().iterator();
    Object localObject4;
    while (((Iterator)localObject3).hasNext())
    {
      localObject4 = (Cell)((Iterator)localObject3).next();
      localObject2 = (ArrayList)localHashMap.get(((GroupCell)localObject4).groupName);
      localObject1 = localObject2;
      if (localObject2 == null)
      {
        localObject1 = new ArrayList();
        localHashMap.put(((GroupCell)localObject4).groupName, localObject1);
      }
      ((ArrayList)localObject1).add((GroupCell)localObject4);
    }
    Object localObject1 = new ArrayList();
    ((ArrayList)localObject1).addAll(localHashMap.keySet());
    Collections.sort((List)localObject1);
    Object localObject2 = ((ArrayList)localObject1).iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject3 = (ArrayList)localHashMap.get((String)((Iterator)localObject2).next());
      Collections.sort((List)localObject3, cellComparator);
      i = 0;
      j = ((ArrayList)localObject3).size();
      while (i < j)
      {
        localObject4 = (GroupCell)((ArrayList)localObject3).get(i);
        try
        {
          if (localArrayList.size() > 0);
          for (localObject1 = (CellContainer)localArrayList.remove(localArrayList.size() - 1); ; localObject1 = new CellContainer(getActivity()))
          {
            ((GroupCell)localObject4).container = ((CellContainer)localObject1);
            ((CellContainer)localObject1).set(((GroupCell)localObject4).view, ((GroupCell)localObject4).style, i, j);
            agentContainerView().addView((View)localObject1);
            break;
          }
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
          i += 1;
        }
      }
    }
  }

  protected Cell updateCell(Cell paramCell, CellAgent paramCellAgent, String paramString)
  {
    if (!(paramCell instanceof GroupCell))
      throw new RuntimeException("cell must be groupCell");
    paramCellAgent = getCellName(paramCellAgent, paramString);
    int i = paramCellAgent.indexOf('.');
    ((GroupCell)paramCell).groupName = paramCellAgent.substring(0, i);
    ((GroupCell)paramCell).childName = paramCellAgent.substring(i + 1);
    return paramCell;
  }

  class CellContainer extends NovaLinearLayout
  {
    private Drawable arrow;
    Drawable bg = GroupAgentFragment.this.res.getDrawable(R.drawable.cell_item);
    private Drawable mask;

    public CellContainer(Context arg2)
    {
      super();
      setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
      setOrientation(1);
      setGravity(16);
    }

    protected void dispatchDraw(Canvas paramCanvas)
    {
      super.dispatchDraw(paramCanvas);
      if (this.arrow != null)
      {
        int i = getWidth() - getPaddingRight() - this.arrow.getIntrinsicWidth() * 2;
        int j = getHeight();
        int k = getPaddingTop();
        int m = getPaddingBottom();
        j = getPaddingTop() + (j - k - m - this.arrow.getIntrinsicHeight()) / 2;
        this.arrow.setBounds(i, j, this.arrow.getIntrinsicWidth() + i, this.arrow.getIntrinsicHeight() + j);
        this.arrow.draw(paramCanvas);
      }
      if (this.mask != null)
      {
        this.mask.setBounds(0, 0, getWidth(), getHeight());
        this.mask.draw(paramCanvas);
      }
    }

    public void reset()
    {
      removeAllViews();
      this.mask = null;
      this.arrow = null;
      setClickable(false);
      setSelected(false);
      setMinimumHeight(0);
      setPadding(0, 0, 0, 0);
    }

    public void set(View paramView, int paramInt1, int paramInt2, int paramInt3)
    {
      addView(paramView);
      if ((paramInt1 & 0x1) != 0)
      {
        setClickable(true);
        paramView.setBackgroundResource(R.drawable.list_item);
        paramView.setOnClickListener(GroupAgentFragment.this.cellClickListener);
      }
      if ((paramInt1 & 0x10) != 0)
      {
        setBackgroundDrawable(null);
        this.mask = null;
        if ((paramInt1 & 0x100) == 0)
          break label658;
        if (GroupAgentFragment.this.cellArrow == null)
          GroupAgentFragment.this.cellArrow = GroupAgentFragment.this.res.getDrawable(R.drawable.arrow);
        this.arrow = GroupAgentFragment.this.cellArrow;
        label100: if (this.mask == null)
          break label666;
        this.mask.getPadding(GroupAgentFragment.this.rect);
        paramInt2 = this.mask.getMinimumHeight();
        paramInt1 = paramInt2;
        if (this.arrow != null)
          paramInt1 = Math.max(paramInt2, this.arrow.getIntrinsicHeight() + GroupAgentFragment.this.rect.top + GroupAgentFragment.this.rect.bottom);
        setMinimumHeight(paramInt1);
        setPadding(GroupAgentFragment.this.rect.left, GroupAgentFragment.this.rect.top, GroupAgentFragment.this.rect.right, GroupAgentFragment.this.rect.bottom);
        if (this.arrow != null)
          paramView.setPadding(this.arrow.getIntrinsicWidth(), 0, this.arrow.getIntrinsicWidth() * 2, 0);
      }
      while (true)
      {
        invalidate();
        return;
        setBackgroundDrawable(this.bg);
        if ((paramInt1 & 0x400) != 0)
        {
          this.mask = null;
          break;
        }
        if (paramInt3 == 1)
        {
          if (GroupAgentFragment.this.cellMaskSingle == null)
            GroupAgentFragment.this.cellMaskSingle = GroupAgentFragment.this.res.getDrawable(R.drawable.cell_single);
          this.mask = GroupAgentFragment.this.cellMaskSingle;
          break;
        }
        if (paramInt2 == 0)
        {
          localObject = paramView.getTag(R.id.group_agent_inner_divider);
          if ((localObject != null) && ((((Integer)localObject).intValue() & 0x40) != 0))
          {
            localObject = GroupAgentFragment.this.generateInnerDivier();
            if (localObject != null)
              addView((View)localObject);
            if (GroupAgentFragment.this.cellMaskTopInner == null)
              GroupAgentFragment.this.cellMaskTopInner = GroupAgentFragment.this.res.getDrawable(R.drawable.cell_top_inner);
            this.mask = GroupAgentFragment.this.cellMaskTopInner;
            break;
          }
          if (GroupAgentFragment.this.cellMaskTop == null)
            GroupAgentFragment.this.cellMaskTop = GroupAgentFragment.this.res.getDrawable(R.drawable.cell_top);
          this.mask = GroupAgentFragment.this.cellMaskTop;
          break;
        }
        if (paramInt2 == paramInt3 - 1)
        {
          if (GroupAgentFragment.this.cellMaskBottom == null)
            GroupAgentFragment.this.cellMaskBottom = GroupAgentFragment.this.res.getDrawable(R.drawable.cell_bottom);
          this.mask = GroupAgentFragment.this.cellMaskBottom;
          break;
        }
        Object localObject = paramView.getTag(R.id.group_agent_inner_divider);
        if ((localObject != null) && ((((Integer)localObject).intValue() & 0x40) != 0))
        {
          localObject = GroupAgentFragment.this.generateInnerDivier();
          if (localObject != null)
            addView((View)localObject);
          if (GroupAgentFragment.this.cellMaskMiddleInner == null)
            GroupAgentFragment.this.cellMaskMiddleInner = GroupAgentFragment.this.res.getDrawable(R.drawable.cell_middle_inner);
          this.mask = GroupAgentFragment.this.cellMaskMiddleInner;
          break;
        }
        if (GroupAgentFragment.this.cellMaskMiddle == null)
          GroupAgentFragment.this.cellMaskMiddle = GroupAgentFragment.this.res.getDrawable(R.drawable.cell_middle);
        this.mask = GroupAgentFragment.this.cellMaskMiddle;
        break;
        label658: this.arrow = null;
        break label100;
        label666: paramInt1 = 0;
        if (this.arrow != null)
          paramInt1 = this.arrow.getIntrinsicHeight();
        setMinimumHeight(paramInt1);
        setPadding(0, 0, 0, 0);
        if (this.arrow == null)
          continue;
        paramView.setPadding(this.arrow.getIntrinsicWidth(), 0, this.arrow.getIntrinsicWidth() * 2, 0);
      }
    }
  }

  static class GroupCell extends Cell
  {
    String childName;
    GroupAgentFragment.CellContainer container;
    String groupName;
    int style;
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.app.loader.GroupAgentFragment
 * JD-Core Version:    0.6.0
 */