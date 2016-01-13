package com.dianping.membercard.view;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.v1.R.color;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaLinearLayout;

public class CardScoreItem extends NovaLinearLayout
{
  private TextView cardScoreHint;
  private RelativeLayout cardScoreLayer;
  private boolean isBinded = false;

  public CardScoreItem(Context paramContext)
  {
    this(paramContext, null);
  }

  public CardScoreItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    LayoutInflater.from(getContext()).inflate(R.layout.mc_card_score_item, this, true);
    this.cardScoreLayer = ((RelativeLayout)findViewById(R.id.cardscore_layer));
    this.cardScoreHint = ((TextView)findViewById(R.id.cardscore_hint));
  }

  public boolean isBinded()
  {
    return this.isBinded;
  }

  public void setCardScore(DPObject paramDPObject, int paramInt)
  {
    String str1 = paramDPObject.getString("Score");
    String str2 = paramDPObject.getString("Msg");
    paramDPObject = paramDPObject.getString("ScoreUrl");
    if (!TextUtils.isEmpty(str1))
    {
      this.cardScoreHint.setText(str1);
      this.cardScoreHint.setTextColor(getResources().getColor(R.color.orange_red));
    }
    for (this.isBinded = true; TextUtils.isEmpty(paramDPObject); this.isBinded = false)
    {
      this.cardScoreLayer.setClickable(true);
      return;
      this.cardScoreHint.setText(str2);
      this.cardScoreHint.setTextColor(getResources().getColor(R.color.gray));
    }
    this.cardScoreLayer.setClickable(false);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.membercard.view.CardScoreItem
 * JD-Core Version:    0.6.0
 */