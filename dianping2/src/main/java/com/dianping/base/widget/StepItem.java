package com.dianping.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;

public class StepItem extends LinearLayout
{
  private TextView detailView;
  private TextView instructionView;
  private ImageView lineView;
  private ImageView typeView;

  public StepItem(Context paramContext)
  {
    super(paramContext);
  }

  public StepItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private int getArrowSource(Direction paramDirection)
  {
    switch (1.$SwitchMap$com$dianping$base$widget$StepItem$Direction[paramDirection.ordinal()])
    {
    default:
      return R.drawable.map_arrow_crcle;
    case 1:
      return R.drawable.map_arrow_up;
    case 2:
      return R.drawable.map_arrow_left;
    case 3:
      return R.drawable.map_arrow_right;
    case 4:
      return R.drawable.map_arrow_left_;
    case 5:
    }
    return R.drawable.map_arrow_right_;
  }

  private int getPictureSource(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return 0;
    case 1:
      return R.drawable.map_bar_walk;
    case 2:
      return R.drawable.map_bar_subway;
    case 3:
      return R.drawable.map_bar_subway;
    case 4:
    }
    return R.drawable.map_bar_car;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.typeView = ((ImageView)findViewById(R.id.type));
    this.instructionView = ((TextView)findViewById(R.id.instruction));
    this.lineView = ((ImageView)findViewById(R.id.line));
    this.detailView = ((TextView)findViewById(R.id.detail));
  }

  public void setStep(int paramInt1, String paramString1, String paramString2, Boolean paramBoolean, String paramString3, int paramInt2)
  {
    if (paramInt2 == 1)
    {
      paramString3 = this.typeView;
      if (paramBoolean.booleanValue())
      {
        paramInt1 = R.drawable.map_line_dot;
        paramString3.setImageResource(paramInt1);
        this.instructionView.setText(paramString2);
        paramString2 = this.lineView;
        if (!paramBoolean.booleanValue())
          break label121;
      }
    }
    label121: for (paramInt1 = 8; ; paramInt1 = 0)
    {
      paramString2.setVisibility(paramInt1);
      this.detailView.setText(paramString1);
      return;
      paramInt1 = getPictureSource(paramInt1);
      break;
      paramString3 = Direction.getEnumByDir(paramString3);
      ImageView localImageView = this.typeView;
      if (paramBoolean.booleanValue());
      for (paramInt1 = R.drawable.map_line_dot; ; paramInt1 = getArrowSource(paramString3))
      {
        localImageView.setImageResource(paramInt1);
        break;
      }
    }
  }

  private static enum Direction
  {
    String direction;

    static
    {
      ARROW_LEFT = new Direction("ARROW_LEFT", 1, "左转");
      ARROW_RIGHT = new Direction("ARROW_RIGHT", 2, "右转");
      ARROW_UP_LEFT = new Direction("ARROW_UP_LEFT", 3, "偏左转");
      ARROW_UP_RIGHT = new Direction("ARROW_UP_RIGHT", 4, "偏右转");
      DEFAULT = new Direction("DEFAULT", 5, "");
      $VALUES = new Direction[] { ARROW_UP, ARROW_LEFT, ARROW_RIGHT, ARROW_UP_LEFT, ARROW_UP_RIGHT, DEFAULT };
    }

    private Direction(String paramString)
    {
      this.direction = paramString;
    }

    public static Direction getEnumByDir(String paramString)
    {
      Object localObject;
      if (paramString == null)
      {
        localObject = DEFAULT;
        return localObject;
      }
      Direction[] arrayOfDirection = values();
      if (arrayOfDirection == null)
        return DEFAULT;
      int j = arrayOfDirection.length;
      int i = 0;
      while (true)
      {
        if (i >= j)
          break label63;
        Direction localDirection = arrayOfDirection[i];
        localObject = localDirection;
        if (paramString.equals(localDirection.direction))
          break;
        i += 1;
      }
      label63: return (Direction)DEFAULT;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.StepItem
 * JD-Core Version:    0.6.0
 */