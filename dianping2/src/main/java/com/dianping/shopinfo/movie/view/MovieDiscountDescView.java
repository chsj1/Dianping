package com.dianping.shopinfo.movie.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dianping.app.DPActivity;
import com.dianping.archive.DPObject;
import com.dianping.base.widget.ColorBorderTextView;
import com.dianping.util.CollectionUtils;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;
import java.util.Arrays;

public class MovieDiscountDescView extends NovaLinearLayout
  implements View.OnClickListener
{
  private DPObject dpMovieDiscount;
  private TextView movieDiscountDescHint;
  private ColorBorderTextView movieDiscountDescLabel;
  private Dialog popup;

  public MovieDiscountDescView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MovieDiscountDescView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private Dialog buildDialog()
  {
    if (this.popup == null)
    {
      this.popup = new Dialog(getContext());
      this.popup.requestWindowFeature(1);
      this.popup.setContentView(R.layout.movie_discount_popup);
      this.popup.getWindow().setBackgroundDrawableResource(R.color.transparent);
      this.popup.getWindow().setLayout(-1, -2);
      this.popup.getWindow().setGravity(1);
      this.popup.setCanceledOnTouchOutside(true);
    }
    ((TextView)this.popup.findViewById(R.id.title)).setText(this.dpMovieDiscount.getString("Label"));
    ((TextView)this.popup.findViewById(R.id.tv_using_instruction)).setText(this.dpMovieDiscount.getString("Hint"));
    LinearLayout localLinearLayout = (LinearLayout)this.popup.findViewById(R.id.content_rules);
    Object localObject = this.dpMovieDiscount.getStringArray("RuleList");
    if ((localObject != null) && (localObject.length >= 0))
    {
      localObject = CollectionUtils.list2Str(Arrays.asList(localObject), "\n");
      ((TextView)localLinearLayout.findViewById(R.id.tv_rules)).setText((CharSequence)localObject);
      localLinearLayout.setVisibility(0);
    }
    while (true)
    {
      return this.popup;
      localLinearLayout.setVisibility(8);
    }
  }

  public void onClick(View paramView)
  {
    ((DPActivity)getContext()).statisticsEvent("movie5", "movie5_piece_activitytips", this.dpMovieDiscount.getInt("ID") + "", 0);
    this.popup = buildDialog();
    this.popup.show();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.movieDiscountDescLabel = ((ColorBorderTextView)findViewById(R.id.moviediscountdesc_label));
    this.movieDiscountDescHint = ((TextView)findViewById(R.id.moviediscountdesc_hint));
  }

  public void setMovieDiscount(DPObject paramDPObject)
  {
    if (paramDPObject == null)
      return;
    this.dpMovieDiscount = paramDPObject;
    paramDPObject = this.dpMovieDiscount.getString("Label");
    if (!TextUtils.isEmpty(paramDPObject))
    {
      this.movieDiscountDescLabel.setTextColor(getResources().getColor(R.color.light_red));
      this.movieDiscountDescLabel.setBorderColor(getResources().getColor(R.color.light_red));
      this.movieDiscountDescLabel.setText(paramDPObject);
      this.movieDiscountDescLabel.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_12));
      int i = ViewUtils.dip2px(getContext(), 3.0F);
      int j = ViewUtils.dip2px(getContext(), 2.0F);
      this.movieDiscountDescLabel.setPadding(i, j, i, j);
      this.movieDiscountDescLabel.setVisibility(0);
      paramDPObject = this.dpMovieDiscount.getString("Hint");
      if (TextUtils.isEmpty(paramDPObject))
        break label201;
      this.movieDiscountDescHint.setText(paramDPObject);
      this.movieDiscountDescHint.setVisibility(0);
    }
    while (true)
    {
      if ((this.dpMovieDiscount.getStringArray("RuleList") != null) && (this.dpMovieDiscount.getStringArray("RuleList").length != 0))
        break label212;
      setEnabled(false);
      return;
      this.movieDiscountDescLabel.setVisibility(4);
      break;
      label201: this.movieDiscountDescHint.setVisibility(4);
    }
    label212: setEnabled(true);
    setOnClickListener(this);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.shopinfo.movie.view.MovieDiscountDescView
 * JD-Core Version:    0.6.0
 */