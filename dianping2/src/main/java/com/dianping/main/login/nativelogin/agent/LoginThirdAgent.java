package com.dianping.main.login.nativelogin.agent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import com.dianping.base.app.loader.AgentFragment;
import com.dianping.base.app.loader.CellAgent;
import com.dianping.base.share.business.ThirdLoginUrl;
import com.dianping.base.widget.RouteFrameLayout;
import com.dianping.loader.MyResources;
import com.dianping.v1.R.color;
import com.dianping.v1.R.drawable;
import com.dianping.v1.R.id;
import com.dianping.v1.R.layout;
import com.dianping.widget.view.NovaTextView;
import java.util.ArrayList;
import java.util.List;

public class LoginThirdAgent extends CellAgent
  implements AdapterView.OnItemClickListener
{
  public static int REQUESTCODE = 1001;
  private MyAdapter mAdapter;
  private GridView mGridView;
  private List<ThirdLoginInfo> mList = new ArrayList();
  private RouteFrameLayout mRouteLay;
  private RelativeLayout mThirdLoginLayout;

  public LoginThirdAgent(Object paramObject)
  {
    super(paramObject);
  }

  // ERROR //
  public static boolean isMIUI()
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore 5
    //   3: iconst_0
    //   4: istore 4
    //   6: aconst_null
    //   7: astore_1
    //   8: aconst_null
    //   9: astore_2
    //   10: new 46	java/util/Properties
    //   13: dup
    //   14: invokespecial 47	java/util/Properties:<init>	()V
    //   17: astore_3
    //   18: new 49	java/io/FileInputStream
    //   21: dup
    //   22: new 51	java/io/File
    //   25: dup
    //   26: invokestatic 57	android/os/Environment:getRootDirectory	()Ljava/io/File;
    //   29: ldc 59
    //   31: invokespecial 62	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   34: invokespecial 65	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   37: astore_0
    //   38: aload_3
    //   39: aload_0
    //   40: invokevirtual 69	java/util/Properties:load	(Ljava/io/InputStream;)V
    //   43: aload_3
    //   44: ldc 71
    //   46: invokevirtual 75	java/util/Properties:getProperty	(Ljava/lang/String;)Ljava/lang/String;
    //   49: invokestatic 81	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   52: istore 6
    //   54: iload 6
    //   56: ifne +6 -> 62
    //   59: iconst_1
    //   60: istore 4
    //   62: iload 4
    //   64: istore 5
    //   66: aload_0
    //   67: ifnull +11 -> 78
    //   70: aload_0
    //   71: invokevirtual 84	java/io/FileInputStream:close	()V
    //   74: iload 4
    //   76: istore 5
    //   78: iload 5
    //   80: ireturn
    //   81: astore_0
    //   82: aload_2
    //   83: astore_0
    //   84: aload_0
    //   85: ifnull -7 -> 78
    //   88: aload_0
    //   89: invokevirtual 84	java/io/FileInputStream:close	()V
    //   92: iconst_0
    //   93: ireturn
    //   94: astore_0
    //   95: iconst_0
    //   96: ireturn
    //   97: astore_0
    //   98: aload_1
    //   99: ifnull +7 -> 106
    //   102: aload_1
    //   103: invokevirtual 84	java/io/FileInputStream:close	()V
    //   106: aload_0
    //   107: athrow
    //   108: astore_0
    //   109: iload 4
    //   111: ireturn
    //   112: astore_1
    //   113: goto -7 -> 106
    //   116: astore_2
    //   117: aload_0
    //   118: astore_1
    //   119: aload_2
    //   120: astore_0
    //   121: goto -23 -> 98
    //   124: astore_1
    //   125: goto -41 -> 84
    //
    // Exception table:
    //   from	to	target	type
    //   10	38	81	java/lang/Exception
    //   88	92	94	java/lang/Exception
    //   10	38	97	finally
    //   70	74	108	java/lang/Exception
    //   102	106	112	java/lang/Exception
    //   38	54	116	finally
    //   38	54	124	java/lang/Exception
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == REQUESTCODE) && (paramInt2 == 64033))
      super.getFragment().getActivity().finish();
  }

  public void onAgentChanged(Bundle paramBundle)
  {
    super.onAgentChanged(paramBundle);
    if (this.mThirdLoginLayout == null)
    {
      this.mThirdLoginLayout = ((RelativeLayout)this.res.inflate(getContext(), R.layout.third_login_layout, getParentView(), false));
      this.mList.add(new ThirdLoginInfo(64, "微信", R.drawable.weixin_btn));
      this.mList.add(new ThirdLoginInfo(32, "QQ", R.drawable.qq_btn));
      this.mList.add(new ThirdLoginInfo(1, "新浪", R.drawable.weibo_btn));
      this.mList.add(new ThirdLoginInfo(16, "支付宝", R.drawable.zhifubao_btn));
      if (isMIUI())
        this.mList.add(new ThirdLoginInfo(128, "小米", R.drawable.xiaomi_btn));
      this.mAdapter = new MyAdapter(this.mList);
      this.mGridView = ((GridView)this.mThirdLoginLayout.findViewById(R.id.route_step));
      this.mGridView.setAdapter(this.mAdapter);
      this.mGridView.setOnItemClickListener(this);
      this.mRouteLay = ((RouteFrameLayout)this.mThirdLoginLayout.findViewById(R.id.route_framlayout));
      this.mRouteLay.setDragBarValue(38);
      this.mRouteLay.setArrowSrc(R.drawable.navibar_arrow_up, R.drawable.navibar_arrow_down);
      this.mRouteLay.updateFrameLayout(2);
      super.addCell("080thirdlogin", this.mThirdLoginLayout);
    }
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    paramAdapterView = ThirdLoginUrl.getThirdLoginUrl(((ThirdLoginInfo)this.mAdapter.getItem(paramInt)).getFeed());
    paramView = new Intent("android.intent.action.VIEW");
    paramView.setData(Uri.parse("dianping://loginweb"));
    paramView.putExtra("url", paramAdapterView);
    paramView.putExtra("isFromNative", true);
    paramView.setFlags(131072);
    super.startActivityForResult(paramView, REQUESTCODE);
  }

  class MyAdapter extends BaseAdapter
  {
    List<LoginThirdAgent.ThirdLoginInfo> list = new ArrayList();

    public MyAdapter()
    {
      Object localObject;
      this.list = localObject;
    }

    public int getCount()
    {
      return this.list.size();
    }

    public Object getItem(int paramInt)
    {
      return this.list.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return 0L;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
      {
        paramView = new NovaTextView(LoginThirdAgent.this.getContext());
        paramView.setTextSize(15.0F);
        paramView.setGravity(17);
        paramView.setTextColor(LoginThirdAgent.this.getResources().getColor(R.color.text_gray));
        paramView.setBackgroundResource(R.drawable.showve_img_bg);
      }
      while (true)
      {
        paramView.setText(((LoginThirdAgent.ThirdLoginInfo)this.list.get(paramInt)).getName() + "登录");
        paramView.setGAString("third_party", ((LoginThirdAgent.ThirdLoginInfo)this.list.get(paramInt)).getName());
        paramView.setCompoundDrawablesWithIntrinsicBounds(((LoginThirdAgent.ThirdLoginInfo)this.list.get(paramInt)).getImgid(), 0, 0, 0);
        return paramView;
        paramView = (NovaTextView)paramView;
      }
    }
  }

  class ThirdLoginInfo
  {
    private int feed;
    private int imgid;
    private String name;

    public ThirdLoginInfo(int paramString, String paramInt1, int arg4)
    {
      this.feed = paramString;
      this.name = paramInt1;
      int i;
      this.imgid = i;
    }

    public int getFeed()
    {
      return this.feed;
    }

    public int getImgid()
    {
      return this.imgid;
    }

    public String getName()
    {
      return this.name;
    }
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.dianping.main.login.nativelogin.agent.LoginThirdAgent
 * JD-Core Version:    0.6.0
 */