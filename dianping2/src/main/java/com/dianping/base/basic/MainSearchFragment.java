package com.dianping.base.basic;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import com.dianping.archive.DPObject;
import com.dianping.archive.DPObject.Editor;
import com.dianping.base.app.NovaActivity;
import com.dianping.base.widget.CustomGridView;
import com.dianping.base.widget.CustomGridView.OnItemClickListener;
import com.dianping.base.widget.NetworkThumbView;
import com.dianping.dataservice.mapi.BasicMApiRequest;
import com.dianping.dataservice.mapi.CacheType;
import com.dianping.dataservice.mapi.MApiRequest;
import com.dianping.dataservice.mapi.MApiResponse;
import com.dianping.imagemanager.DPNetworkImageView;
import com.dianping.locationservice.LocationService;
import com.dianping.model.City;
import com.dianping.model.Location;
import com.dianping.util.ViewUtils;
import com.dianping.v1.R.color;
import com.dianping.v1.R.dimen;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.GAHelper;
import com.dianping.widget.view.GAUserInfo;
import com.dianping.widget.view.NovaLinearLayout;
import com.dianping.widget.view.NovaRelativeLayout;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ArrayList<Lcom.dianping.archive.DPObject;>;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MainSearchFragment extends AbstractSearchFragment
  implements CustomGridView.OnItemClickListener
{
  protected static final int ADVANCED_SUGGEST = 1;
  private static final DPObject EMPTY = new DPObject();
  protected static final int HOT_WORD = 2;
  protected static final int NORMAL_SUGGEST = 0;
  protected static final int SUGGEST_DRECTIN = 3;
  private static final String URL = "http://m.api.dianping.com/";
  protected String endPoint = "advancedsuggest.bin";
  private boolean hasTableIcon = true;
  protected String hotWordApi = "hotsuggest.bin";
  protected int mCategoryId = 0;
  private int mSuggestTableColumns = 4;

  private ArrayList<DPObject> getSuggestGroupsFromResponse(Object paramObject)
  {
    ArrayList localArrayList = new ArrayList();
    DPObject[] arrayOfDPObject1;
    if ((paramObject instanceof DPObject))
    {
      arrayOfDPObject1 = ((DPObject)paramObject).getArray("List");
      this.queryid = ((DPObject)paramObject).getString("QueryID");
      this.mSuggestTableColumns = initSuggestTableColumns(arrayOfDPObject1);
      if (this.mSuggestTableColumns != 0)
        break label58;
    }
    while (true)
    {
      return localArrayList;
      label58: if (arrayOfDPObject1 == null)
        continue;
      int k = 0;
      int i = 0;
      while (i < arrayOfDPObject1.length)
      {
        if (this.hasTableIcon)
        {
          paramObject = arrayOfDPObject1[i].getString("Icon");
          localArrayList.add(new DPObject().edit().putString("Icon", paramObject).generate());
        }
        DPObject[] arrayOfDPObject2 = arrayOfDPObject1[i].getArray("SuggestList");
        int j = this.mSuggestTableColumns;
        if (this.hasTableIcon)
          j = this.mSuggestTableColumns - 1;
        int m = 0;
        while (m < j)
        {
          DPObject localDPObject = EMPTY;
          paramObject = localDPObject;
          if (arrayOfDPObject2 != null)
          {
            paramObject = localDPObject;
            if (m < arrayOfDPObject2.length)
              paramObject = arrayOfDPObject2[m].edit().putInt("RealPosition", k).generate();
          }
          localArrayList.add(paramObject);
          k += 1;
          m += 1;
        }
        i += 1;
      }
    }
  }

  private int initSuggestTableColumns(DPObject[] paramArrayOfDPObject)
  {
    this.hasTableIcon = true;
    int k = 0;
    int i = 0;
    if (paramArrayOfDPObject != null)
    {
      int j = 0;
      while (true)
      {
        k = i;
        if (j >= paramArrayOfDPObject.length)
          break;
        DPObject[] arrayOfDPObject = paramArrayOfDPObject[j].getArray("SuggestList");
        k = i;
        if (arrayOfDPObject != null)
          k = Math.max(i, arrayOfDPObject.length);
        if ((android.text.TextUtils.isEmpty(paramArrayOfDPObject[j].getString("Icon"))) && (this.hasTableIcon))
          this.hasTableIcon = false;
        j += 1;
        i = k;
      }
    }
    if (k == 0);
    do
      return k;
    while (!this.hasTableIcon);
    return k + 1;
  }

  public static MainSearchFragment newInstance(FragmentActivity paramFragmentActivity)
  {
    return newInstance(paramFragmentActivity, 0);
  }

  public static MainSearchFragment newInstance(FragmentActivity paramFragmentActivity, int paramInt)
  {
    MainSearchFragment localMainSearchFragment = new MainSearchFragment();
    localMainSearchFragment.mCategoryId = paramInt;
    paramFragmentActivity = paramFragmentActivity.getSupportFragmentManager().beginTransaction();
    paramFragmentActivity.add(16908290, localMainSearchFragment);
    paramFragmentActivity.addToBackStack(null);
    paramFragmentActivity.commitAllowingStateLoss();
    return localMainSearchFragment;
  }

  private void setIconImage(DPNetworkImageView paramDPNetworkImageView, String paramString)
  {
    if (android.text.TextUtils.isEmpty(paramString));
    int i;
    do
    {
      return;
      if (paramString.startsWith("http"))
      {
        paramDPNetworkImageView.setImage(paramString);
        return;
      }
      i = getResources().getIdentifier(paramString, "drawable", getContext().getPackageName());
    }
    while (i == 0);
    paramDPNetworkImageView.setImageDrawable(getResources().getDrawable(i));
  }

  protected View createHistoryItem(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    DPObject localDPObject = (DPObject)this.historyListAdapter.getItem(paramInt);
    Object localObject1;
    Object localObject2;
    if (localDPObject == CLEARHISTORY)
    {
      paramView = (NovaLinearLayout)getActivity().getLayoutInflater().inflate(R.layout.search_history_clear_list_item, paramViewGroup, false);
      ((TextView)paramView.findViewById(R.id.title)).setText(localDPObject.getString("Keyword"));
      paramViewGroup = paramView.findViewById(R.id.list_view_start_divider);
      localObject1 = paramView.findViewById(R.id.list_view_end_divider);
      localObject2 = paramView.findViewById(R.id.divider);
      if ((paramViewGroup != null) && (localObject1 != null) && (localObject2 != null))
      {
        if (paramInt != 0)
          break label446;
        paramViewGroup.setVisibility(0);
        ((View)localObject2).setVisibility(0);
        ((View)localObject1).setVisibility(8);
      }
    }
    while (true)
    {
      paramView.setGAString("history", localDPObject.getString("Keyword"));
      localObject1 = localDPObject.getString("QueryID");
      paramViewGroup = (ViewGroup)localObject1;
      if (android.text.TextUtils.isEmpty((CharSequence)localObject1))
        paramViewGroup = this.queryid;
      paramView.gaUserInfo.index = Integer.valueOf(paramInt);
      paramView.gaUserInfo.keyword = String.valueOf(this.searchEditText.getText());
      if ((paramInt == 0) && (this.queryid == null))
      {
        paramView.gaUserInfo.query_id = null;
        GAHelper.instance().contextStatisticsEvent(getContext(), "history", paramView.gaUserInfo, "view");
      }
      paramView.gaUserInfo.query_id = paramViewGroup;
      return paramView;
      if (localDPObject.getInt("SuggestType") == 3)
      {
        paramView = (NovaLinearLayout)getActivity().getLayoutInflater().inflate(R.layout.search_history_directin_list_item, paramViewGroup, false);
        setIconImage((DPNetworkImageView)paramView.findViewById(R.id.icon), localDPObject.getString("Icon"));
        paramViewGroup = (TextView)paramView.findViewById(R.id.title);
        localObject1 = (TextView)paramView.findViewById(R.id.sub_title);
        paramViewGroup.setText(localDPObject.getString("Keyword"));
        ((TextView)localObject1).setText(localDPObject.getString("Desc"));
        ((TextView)paramView.findViewById(R.id.right_content)).setVisibility(8);
        break;
      }
      paramViewGroup = (NovaLinearLayout)getActivity().getLayoutInflater().inflate(R.layout.search_history_list_item, paramViewGroup, false);
      localObject2 = (DPNetworkImageView)paramViewGroup.findViewById(R.id.icon);
      localObject1 = localDPObject.getString("Icon");
      paramView = (View)localObject1;
      if (android.text.TextUtils.isEmpty((CharSequence)localObject1))
        paramView = "search_suggest_search";
      setIconImage((DPNetworkImageView)localObject2, paramView);
      ((TextView)paramViewGroup.findViewById(R.id.title)).setText(localDPObject.getString("Keyword"));
      paramView = paramViewGroup;
      break;
      label446: if (paramInt == this.historyListAdapter.getCount() - 1)
      {
        paramViewGroup.setVisibility(8);
        ((View)localObject2).setVisibility(8);
        ((View)localObject1).setVisibility(0);
        continue;
      }
      paramViewGroup.setVisibility(8);
      ((View)localObject2).setVisibility(0);
      ((View)localObject1).setVisibility(8);
    }
  }

  public MApiRequest createRequest(String paramString)
  {
    Object localObject2 = new StringBuilder().append("http://m.api.dianping.com/");
    Object localObject1;
    if (this.mSearchMode == 1)
      localObject1 = this.hotWordApi;
    while (true)
    {
      localObject1 = new StringBuilder((String)localObject1 + "?");
      ((StringBuilder)localObject1).append("cityid=").append(cityId());
      if ((this.mSearchMode == 1) && (location() != null))
      {
        localObject2 = location().city();
        if (localObject2 != null)
          ((StringBuilder)localObject1).append("&locatecityid=").append(((City)localObject2).id());
      }
      if (!android.text.TextUtils.isEmpty(paramString));
      try
      {
        ((StringBuilder)localObject1).append("&keyword=").append(URLEncoder.encode(paramString, "utf-8"));
        if (this.mCategoryId != 0)
          ((StringBuilder)localObject1).append("&categoryid=").append(String.valueOf(this.mCategoryId));
        localObject2 = locationService().location();
        if ((localObject2 != null) && (((DPObject)localObject2).getDouble("Lat") != 0.0D) && (((DPObject)localObject2).getDouble("Lng") != 0.0D))
        {
          ((StringBuilder)localObject1).append("&").append("mylat=").append(Location.FMT.format(((DPObject)localObject2).getDouble("Lat")));
          ((StringBuilder)localObject1).append("&").append("mylng=").append(Location.FMT.format(((DPObject)localObject2).getDouble("Lng")));
          if ((localObject2 != null) && (((DPObject)localObject2).getInt("Accuracy") > 0))
            ((StringBuilder)localObject1).append("&").append("myacc=").append(((DPObject)localObject2).getInt("Accuracy"));
          if (!android.text.TextUtils.isEmpty(paramString))
            break label384;
          return BasicMApiRequest.mapiGet(((StringBuilder)localObject1).toString(), CacheType.CRITICAL);
          localObject1 = this.endPoint;
        }
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        while (true)
        {
          localUnsupportedEncodingException.printStackTrace();
          continue;
          if (this.mSearchMode != 1)
            continue;
          ((StringBuilder)localObject1).append("&").append("mylat=").append(Location.FMT.format(0.0D));
          ((StringBuilder)localObject1).append("&").append("mylng=").append(Location.FMT.format(0.0D));
        }
      }
    }
    label384: return (MApiRequest)(MApiRequest)BasicMApiRequest.mapiGet(((StringBuilder)localObject1).toString(), CacheType.NORMAL);
  }

  protected View createSuggestionItem(DPObject paramDPObject, int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (paramDPObject.isClass("Suggest"))
    {
      int i = paramDPObject.getInt("SuggestType");
      Object localObject2;
      Object localObject1;
      if (i == 3)
      {
        paramViewGroup = (NovaLinearLayout)getActivity().getLayoutInflater().inflate(R.layout.search_history_directin_list_item, paramViewGroup, false);
        localObject2 = (DPNetworkImageView)paramViewGroup.findViewById(R.id.icon);
        localObject1 = paramDPObject.getString("Icon");
        paramView = (View)localObject1;
        if (android.text.TextUtils.isEmpty((CharSequence)localObject1))
          paramView = "search_suggest_poi";
        setIconImage((DPNetworkImageView)localObject2, paramView);
        paramView = (TextView)paramViewGroup.findViewById(R.id.title);
        localObject1 = (TextView)paramViewGroup.findViewById(R.id.sub_title);
        paramView.setText(paramDPObject.getString("Keyword"));
        ((TextView)localObject1).setText(paramDPObject.getString("Desc"));
        ((TextView)paramViewGroup.findViewById(R.id.right_content)).setText(paramDPObject.getString("DisplayInfo"));
        paramViewGroup.setGAString("suggest_unique", paramDPObject.getString("Keyword"));
        paramDPObject = paramViewGroup;
        paramView = paramDPObject.findViewById(R.id.list_view_start_divider);
        paramViewGroup = paramDPObject.findViewById(R.id.list_view_end_divider);
        localObject1 = paramDPObject.findViewById(R.id.divider);
        if ((paramView != null) && (paramViewGroup != null) && (localObject1 != null))
        {
          if (paramInt != 0)
            break label546;
          paramView.setVisibility(0);
          ((View)localObject1).setVisibility(0);
          paramViewGroup.setVisibility(8);
        }
      }
      while (true)
      {
        paramDPObject.gaUserInfo.keyword = this.searchEditText.getText().toString().trim();
        paramDPObject.gaUserInfo.query_id = this.queryid;
        return paramDPObject;
        paramViewGroup = (NovaLinearLayout)getActivity().getLayoutInflater().inflate(R.layout.search_history_list_item, paramViewGroup, false);
        DPNetworkImageView localDPNetworkImageView = (DPNetworkImageView)paramViewGroup.findViewById(R.id.icon);
        localObject2 = (TextView)paramViewGroup.findViewById(R.id.right_content);
        localObject1 = paramDPObject.getString("Icon");
        paramView = (View)localObject1;
        if (android.text.TextUtils.isEmpty((CharSequence)localObject1))
          paramView = "search_suggest_search";
        setIconImage(localDPNetworkImageView, paramView);
        paramView = (TextView)paramViewGroup.findViewById(R.id.title);
        paramView.setText(paramDPObject.getString("Keyword"));
        localObject1 = paramDPObject.getString("DisplayInfo");
        if (!android.text.TextUtils.isEmpty((CharSequence)localObject1))
        {
          ((TextView)localObject2).setText((CharSequence)localObject1);
          ((TextView)localObject2).setVisibility(0);
        }
        while (true)
        {
          if (i != 1)
            break label511;
          paramView.setTextColor(getResources().getColor(R.color.advance_suggest_blue));
          paramViewGroup.setGAString("suggest_otherplace", paramDPObject.getString("Keyword"));
          ((NovaActivity)getActivity()).addGAView(paramViewGroup, paramInt);
          paramDPObject = paramViewGroup;
          break;
          if (paramDPObject.getInt("Count") <= 0)
            continue;
          ((TextView)localObject2).setText("共" + paramDPObject.getInt("Count") + "个结果");
          ((TextView)localObject2).setVisibility(0);
        }
        label511: paramView.setTextColor(getResources().getColor(R.color.deep_gray));
        paramViewGroup.setGAString("suggest", paramDPObject.getString("Keyword"));
        paramDPObject = paramViewGroup;
        break;
        label546: if (paramInt == this.suggestListAdapter.getCount() - 1)
        {
          paramView.setVisibility(8);
          ((View)localObject1).setVisibility(8);
          paramViewGroup.setVisibility(0);
          continue;
        }
        paramView.setVisibility(8);
        ((View)localObject1).setVisibility(0);
        paramViewGroup.setVisibility(8);
      }
    }
    if ((paramView instanceof NovaRelativeLayout));
    for (paramView = (NovaRelativeLayout)paramView; ; paramView = (NovaRelativeLayout)getActivity().getLayoutInflater().inflate(R.layout.suggest_list_direct_zone_item, paramViewGroup, false))
    {
      ((NetworkThumbView)paramView.findViewById(R.id.thumb)).setImage(paramDPObject.getString("PicUrl"));
      ((TextView)paramView.findViewById(R.id.title)).setText(paramDPObject.getString("Title"));
      ((TextView)paramView.findViewById(R.id.abstract_text)).setText(com.dianping.util.TextUtils.highLightShow(getActivity(), paramDPObject.getString("Abstract"), R.color.tuan_common_orange));
      ((TextView)paramView.findViewById(R.id.sub_title)).setText(paramDPObject.getString("Subtitle"));
      ((TextView)paramView.findViewById(R.id.high_light)).setText(com.dianping.util.TextUtils.highLightShow(getActivity(), paramDPObject.getString("ClickTips"), R.color.tuan_common_orange));
      paramView.setOnClickListener(new View.OnClickListener(paramDPObject)
      {
        public void onClick(View paramView)
        {
          paramView = this.val$suggestion.getString("ClickUrl");
          if (!android.text.TextUtils.isEmpty(paramView))
          {
            MainSearchFragment.this.startActivity(paramView);
            MainSearchFragment.this.getFragmentManager().popBackStackImmediate();
          }
        }
      });
      paramView.setGAString("suggest_direct");
      paramView.gaUserInfo.keyword = this.searchEditText.getText().toString().trim();
      paramView.gaUserInfo.index = Integer.valueOf(paramInt);
      paramView.gaUserInfo.query_id = this.queryid;
      GAHelper.instance().contextStatisticsEvent(getActivity(), "suggest_direct", paramView.gaUserInfo, "view");
      return paramView;
    }
  }

  public String getFileName()
  {
    return "find_main_search_fragment,find_tuan_search_fragment";
  }

  public View getHeaderView(String paramString, int paramInt)
  {
    LinearLayout localLinearLayout = new LinearLayout(getActivity());
    TextView localTextView = new TextView(getActivity());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-2, -2, 1.0F);
    localLayoutParams.leftMargin = (int)(getResources().getDimension(R.dimen.base_suggest_hotword_lr_margin) + getResources().getDimension(R.dimen.base_suggest_hotword_item_lr_margin));
    localLayoutParams.height = ViewUtils.dip2px(getActivity(), 36.0F);
    localTextView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
    localTextView.setLayoutParams(localLayoutParams);
    localTextView.setText(paramString);
    localTextView.setTextColor(getResources().getColor(R.color.light_gray));
    localTextView.setGravity(19);
    localLinearLayout.addView(localTextView);
    localLinearLayout.setTag(Integer.valueOf(paramInt));
    localLinearLayout.setClickable(true);
    this.mHeaderViews.add(localLinearLayout);
    localLinearLayout.setOnTouchListener(this.onTouchListener);
    return localLinearLayout;
  }

  protected View getHotWordView(ArrayList<DPObject> paramArrayList)
  {
    LinearLayout localLinearLayout = new LinearLayout(getActivity());
    localLinearLayout.setOrientation(0);
    int i = getResources().getDimensionPixelOffset(R.dimen.base_suggest_hotword_lr_margin);
    localLinearLayout.setPadding(i, i, i, ViewUtils.dip2px(getContext(), 4.0F));
    CustomGridView localCustomGridView = new CustomGridView(getActivity());
    if (this.hasTableIcon)
    {
      i = 1;
      while (i < this.mSuggestTableColumns)
      {
        localCustomGridView.setColumnStretchable(i, true);
        i += 1;
      }
    }
    localCustomGridView.setStretchAllColumns(true);
    paramArrayList = new NewHotWordAdapter(paramArrayList);
    localCustomGridView.setHorizontalDivider(getResources().getDrawable(R.drawable.search_hotword_horizontal_drawable));
    localCustomGridView.setVerticalDivider(getResources().getDrawable(R.drawable.search_hotword_vertical_drawable));
    localCustomGridView.setAdapter(paramArrayList);
    localCustomGridView.setOnItemClickListener(this);
    localCustomGridView.setTag(Integer.valueOf(1));
    localCustomGridView.setOnTouchListener(this.onTouchListener);
    localLinearLayout.addView(localCustomGridView);
    localLinearLayout.setClickable(true);
    this.mHeaderViews.add(localLinearLayout);
    return localLinearLayout;
  }

  protected ArrayList<DPObject> getSuggestListFromResponse(Object paramObject)
  {
    ArrayList localArrayList = new ArrayList();
    if ((paramObject instanceof DPObject))
    {
      Object localObject = ((DPObject)paramObject).getObject("DirectZoneResult");
      if (localObject != null)
      {
        localObject = ((DPObject)localObject).getArray("List");
        if (localObject != null)
          localArrayList.addAll(Arrays.asList(localObject));
      }
    }
    localArrayList.addAll(super.getSuggestListFromResponse(paramObject));
    return (ArrayList<DPObject>)localArrayList;
  }

  public void onRequestFinish(MApiRequest paramMApiRequest, MApiResponse paramMApiResponse)
  {
    if (this.request == paramMApiRequest)
    {
      if (paramMApiResponse.result() == null)
        break label82;
      if (getActivity() != null)
        break label30;
      this.request = null;
    }
    while (true)
    {
      return;
      label30: resetListView();
      this.listView.setHeaderDividersEnabled(false);
      if (this.mSearchMode != 2)
        break;
      paramMApiResponse = getSuggestListFromResponse(paramMApiResponse.result());
      this.mHeaderViews.clear();
      this.listView.setAdapter(getSuggestListAdapter(paramMApiResponse));
      label82: this.request = null;
      if (getActivity() == null)
        continue;
      paramMApiResponse = new GAUserInfo();
      paramMApiResponse.query_id = this.queryid;
      paramMApiRequest = Uri.parse(paramMApiRequest.url()).getQueryParameter("keyword");
      if (paramMApiRequest == null)
        break label239;
    }
    while (true)
    {
      paramMApiResponse.keyword = paramMApiRequest;
      GAHelper.instance().setRequestId(getActivity(), UUID.randomUUID().toString(), paramMApiResponse, false);
      return;
      if (this.mSearchMode != 1)
        break;
      paramMApiResponse = getSuggestGroupsFromResponse(paramMApiResponse.result());
      if ((this.hasHotwordView) && (paramMApiResponse != null) && (paramMApiResponse.size() > 0))
        this.listView.addHeaderView(getHotWordView(paramMApiResponse));
      if (getHistoryCount() > 0)
        this.listView.addHeaderView(getHeaderView("搜索历史", 2));
      this.listView.setAdapter(this.historyListAdapter);
      break;
      label239: paramMApiRequest = "";
    }
  }

  private class NewHotWordAdapter extends BaseAdapter
  {
    protected ArrayList<DPObject> tableData = null;

    public NewHotWordAdapter()
    {
      Object localObject;
      this.tableData = localObject;
    }

    public int getCount()
    {
      if (this.tableData == null)
        return 0;
      return this.tableData.size();
    }

    public DPObject getItem(int paramInt)
    {
      return (DPObject)this.tableData.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      DPObject localDPObject = getItem(paramInt);
      Object localObject1;
      Object localObject2;
      if (paramInt % MainSearchFragment.this.mSuggestTableColumns == 0)
      {
        localObject1 = new TableRow(paramViewGroup.getContext());
        if (MainSearchFragment.this.hasTableIcon)
        {
          paramView = (NovaRelativeLayout)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.search_find_hot_title_item, (TableRow)localObject1, false);
          paramViewGroup = (DPNetworkImageView)paramView.findViewById(R.id.icon);
          localObject2 = localDPObject.getString("Icon");
          MainSearchFragment.this.setIconImage(paramViewGroup, (String)localObject2);
          ((TableRow)localObject1).addView(paramView);
          paramView.setTag(localDPObject);
          paramViewGroup = (ViewGroup)localObject1;
        }
      }
      while (true)
      {
        if (paramInt == 0)
          GAHelper.instance().contextStatisticsEvent(paramView.getContext(), "hotsuggest", paramView.gaUserInfo, "view");
        paramViewGroup.setOnTouchListener(MainSearchFragment.this.onTouchListener);
        return paramViewGroup;
        paramView = (NovaRelativeLayout)LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.search_find_hot_word_item, (TableRow)localObject1, false);
        paramViewGroup = (TextView)paramView.findViewById(R.id.text);
        localObject2 = paramView.findViewById(R.id.icon);
        String str1 = localDPObject.getString("Keyword");
        paramViewGroup.setText(str1);
        String str2 = localDPObject.getString("Templateid");
        MainSearchFragment.this.hotWordStyle(str2, paramViewGroup, (View)localObject2, R.color.deep_gray);
        paramView.setTag(localDPObject);
        paramView.setGAString("hotsuggest", str1);
        paramView.gaUserInfo.query_id = MainSearchFragment.this.queryid;
        paramView.gaUserInfo.keyword = String.valueOf(MainSearchFragment.this.searchEditText.getText());
        paramView.gaUserInfo.index = Integer.valueOf(localDPObject.getInt("RealPosition"));
        ((TableRow)localObject1).addView(paramView);
        paramViewGroup = (ViewGroup)localObject1;
        continue;
        paramViewGroup = LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.search_find_hot_word_item, ((CustomGridView)paramViewGroup).getCurRow(), false);
        paramView = (NovaRelativeLayout)paramViewGroup;
        localObject1 = (TextView)paramView.findViewById(R.id.text);
        localObject2 = paramView.findViewById(R.id.icon);
        str1 = localDPObject.getString("Keyword");
        ((TextView)localObject1).setText(str1);
        str2 = localDPObject.getString("Templateid");
        MainSearchFragment.this.hotWordStyle(str2, (TextView)localObject1, (View)localObject2, R.color.deep_gray);
        paramView.setTag(localDPObject);
        paramView.setGAString("hotsuggest", str1);
        paramView.gaUserInfo.query_id = MainSearchFragment.this.queryid;
        paramView.gaUserInfo.keyword = String.valueOf(MainSearchFragment.this.searchEditText.getText());
        paramView.gaUserInfo.index = Integer.valueOf(localDPObject.getInt("RealPosition"));
      }
    }

    public void setData(ArrayList<DPObject> paramArrayList)
    {
      this.tableData = paramArrayList;
      notifyDataSetChanged();
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.base.basic.MainSearchFragment
 * JD-Core Version:    0.6.0
 */