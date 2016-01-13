package com.dianping.base.widget.phototag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;

public class PictureTagView extends RelativeLayout
  implements View.OnClickListener
{
  private Context context;
  private TagCicleClickListener mCicleListener;
  private View mTagCicleLay;
  private View mTagLay;
  private View mTagLeftContentLay;
  private View mTagRightContentLay;
  private TextView mTagTextViewLeft;
  private TextView mTagTextViewRight;
  public TagItem tagItem;

  public PictureTagView(Context paramContext, TagItem paramTagItem)
  {
    super(paramContext);
    this.context = paramContext;
    this.tagItem = paramTagItem;
    initViews();
    init();
  }

  public void directionChange()
  {
    switch (1.$SwitchMap$com$dianping$base$widget$phototag$PictureTagView$Direction[this.tagItem.direction.ordinal()])
    {
    default:
      return;
    case 1:
      this.mTagLeftContentLay.setVisibility(0);
      this.mTagRightContentLay.setVisibility(8);
      return;
    case 2:
    }
    this.mTagLeftContentLay.setVisibility(8);
    this.mTagRightContentLay.setVisibility(0);
  }

  public TextView getLeftTextView()
  {
    return this.mTagTextViewLeft;
  }

  protected void init()
  {
    this.mTagCicleLay.setOnClickListener(this);
    directionChange();
  }

  protected void initViews()
  {
    LayoutInflater.from(this.context).inflate(R.layout.ugc_tagview_layout, this, true);
    this.mTagLay = findViewById(R.id.tag_lay);
    this.mTagCicleLay = findViewById(R.id.tag_cicle_lay);
    this.mTagLeftContentLay = findViewById(R.id.tag_content_left_lay);
    this.mTagRightContentLay = findViewById(R.id.tag_content_right_lay);
    this.mTagTextViewLeft = ((TextView)findViewById(R.id.tag_text_left));
    this.mTagTextViewRight = ((TextView)findViewById(R.id.tag_text_right));
  }

  public void onClick(View paramView)
  {
    if ((paramView.getId() == R.id.tag_cicle_lay) && (this.mCicleListener != null))
      this.mCicleListener.onClick();
  }

  public void setTagCicleClickListener(TagCicleClickListener paramTagCicleClickListener)
  {
    this.mCicleListener = paramTagCicleClickListener;
  }

  public void updateContent(TagItem paramTagItem)
  {
    this.mTagTextViewLeft.setText(paramTagItem.tagContent);
    this.mTagTextViewRight.setText(paramTagItem.tagContent);
  }

  public static enum Direction
  {
    static
    {
      $VALUES = new Direction[] { Left, Right };
    }
  }

  public static abstract interface TagCicleClickListener
  {
    public abstract void onClick();
  }

  public static class TagItem
  {
    public static final int TYPE_ADDRESS_MOVIE = 2;
    public static final int TYPE_ADDRESS_NORMAL = 1;
    public static final int TYPE_TAG_ADDRESS = 2;
    public static final int TYPE_TAG_TEXT = 1;
    public static int tagid;
    public final int ID;
    public int addressType = 1;
    public PictureTagView.Direction direction = PictureTagView.Direction.Right;
    public String tagContent;
    public int type = 1;
    public int x;
    public int y;

    public TagItem()
    {
      int i = tagid + 1;
      tagid = i;
      this.ID = i;
    }

    public TagItem(int paramInt)
    {
      this.ID = paramInt;
    }

    public TagItem(int paramInt1, int paramInt2)
    {
      this.ID = paramInt1;
      this.type = paramInt2;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.widget.phototag.PictureTagView
 * JD-Core Version:    0.6.0
 */