package com.tencent.wns.client.data;

import android.util.SparseArray;
import cloudwns.i.a;
import cloudwns.i.g;
import java.util.HashSet;
import java.util.Set;

public final class WnsError
{

  @Deprecated
  public static final int ACCOUNT_IS_NULL = 538;
  public static final String BUSY_ERROR_MESSAGE = "网络繁忙，请稍后重试";
  public static final int CHECK_SESSION_NEW_SUCCESS = 552;
  public static final int CHECK_SESSION_OLD_FAIL = 553;
  public static final int CHECK_SESSION_OLD_SUCCESS = 551;

  @Deprecated
  public static final int CONNECT_EXCEPTION_ERROR = 534;
  public static final int CONNECT_FAIL = 516;
  public static final int CONNECT_TIME_OUT = 513;
  public static final String DEF_AUTH_ERROR_MESSAGE = "登录失败，请稍后重试";
  public static final String DEF_ERROR_MESSAGE = "操作失败，请稍后重试";

  @Deprecated
  public static final int DNS_ANALYZE_TIMEOUT = 537;
  public static final int DNS_FAIL = 521;
  public static final int E_REG_ALREADY_REGISTERED = 135;
  public static final int E_REG_BIND_ERROR = 133;
  public static final int E_REG_BIND_MAILBOX_FAILED = 139;
  public static final int E_REG_BUSY = 125;
  public static final int E_REG_BUSY_GET_IMG = 123;
  public static final int E_REG_BUSY_VERIFY_IMG = 124;
  public static final int E_REG_DIRTY_ACCOUNT = 122;
  public static final int E_REG_DOWNLOAD_MSG_FAILED = 114;
  public static final int E_REG_GET_MSG_FAILED = 118;
  public static final int E_REG_HAS_REGISTERED_RECENTLY = 120;
  public static final int E_REG_ILLEGAL_MAILBOX = 140;
  public static final int E_REG_MAX_DOWNMSG_LIMITED = 131;
  public static final int E_REG_MAX_LIMITED = 115;
  public static final int E_REG_MAX_MSG_LIMITED = 130;
  public static final int E_REG_MAX_RETRY_LIMITED = 132;
  public static final int E_REG_NETWORK_ERROR = 121;
  public static final int E_REG_NOT_IN_WHITELIST = 137;
  public static final int E_REG_PROTOCOL = 200;
  public static final int E_REG_REGISTERED_ERROR = 136;
  public static final int E_REG_SEND_AUTHMAIL_FAILED = 138;
  public static final int E_REG_SESSION_ERROR = 113;
  public static final int E_REG_SESSION_GET_FAILED = 117;
  public static final int E_REG_SESSION_INIT_ERROR = 112;
  public static final int E_REG_UNBIND_ERROR = 134;
  public static final int E_REG_UNSAFE_PASSWORD = 126;
  public static final int E_REG_WRONG_DEVICE = 129;
  public static final int E_REG_WRONG_LANGUAGE = 128;
  public static final int E_REG_WRONG_PHONE = 111;
  public static final int E_REG_WRONG_REGION = 127;
  public static final int E_REG_WRONG_SESSION_STATE = 119;
  public static final int E_REG_WRONG_TOKEN = 116;
  public static final int E_WTSDK_A1_DECRYPT = 270;
  public static final int E_WTSDK_A1_INVALID = 272;
  public static final int E_WTSDK_DECRYPT = 258;
  public static final int E_WTSDK_DNS = 263;
  public static final int E_WTSDK_ENCODING = 269;
  public static final int E_WTSDK_FATAL_EXCEPTION = 512;
  public static final int E_WTSDK_INVALID_NAME = 264;
  public static final int E_WTSDK_IS_BUSY = 511;
  public static final int E_WTSDK_NO_KEY = 260;
  public static final int E_WTSDK_NO_REG_LEN = 266;
  public static final int E_WTSDK_NO_RET = 256;
  public static final int E_WTSDK_NO_TGT = 262;
  public static final int E_WTSDK_NO_UIN = 259;
  public static final int E_WTSDK_OPENDB_FAIL = 278;
  public static final int E_WTSDK_PENDING = 257;
  public static final int E_WTSDK_PK_LEN = 265;
  public static final int E_WTSDK_PUSH_RECONNECT = 356;
  public static final int E_WTSDK_PUSH_REG = 267;
  public static final int E_WTSDK_SUCCESS_BUT_NULL_A2 = 510;
  public static final int E_WTSDK_SYSTEM = 268;
  public static final int E_WTSDK_TLV_DECRYPT = 271;
  public static final int E_WTSDK_TLV_VERIFY = 261;
  public static final int E_WT_A2_EXPIRED = 15;
  public static final int E_WT_ACCOUNT_IN_BLACKLIST = 33;
  public static final int E_WT_ACCOUNT_IS_DENIED = 40;
  public static final int E_WT_ACCOUNT_IS_DENIED_FOR_STAFF = 113;
  public static final int E_WT_ACCOUNT_IS_FROZEN = 32;
  public static final int E_WT_ACCOUNT_IS_LOCKED = 42;
  public static final int E_WT_ACCOUNT_IS_NOT_TENPAY = 43;
  public static final int E_WT_ACCOUNT_NOT_EXIST = 18;
  public static final int E_WT_ACCOUNT_SERVICE_EXPIRED = 41;
  public static final int E_WT_CLIENTCLG_FAILED = 3;
  public static final int E_WT_EXPIRED_ERROR = 16;
  public static final int E_WT_ILLEGAL_ACCOUNT_NAME = 5;
  public static final int E_WT_ILLEGAL_APPID = 8;
  public static final int E_WT_ILLEGAL_SIG_REQUEST = 11;
  public static final int E_WT_INFO_LACK = 6;
  public static final int E_WT_LOGIN_NOT_ALLOWED = 7;
  public static final int E_WT_LOGIN_PACKAGE_ERROR = 9;
  public static final int E_WT_LOGIN_TOO_OFTEN = 10;
  public static final int E_WT_NEED_ACCOUNT_NAME = 12;
  public static final int E_WT_NEED_SMS_VERIFYCODE = 160;
  public static final int E_WT_NEED_VERIFYCODE = 2;
  public static final int E_WT_NO_PERMISSION_FOR_SIGS = 48;
  public static final int E_WT_SERVER_INNER_TIMEOUT = 154;
  public static final int E_WT_SMS_REQUEST_FAILED = 162;
  public static final int E_WT_SMS_TOO_OFTEN = 161;
  public static final int E_WT_SMS_VERIFY_FAILED = 163;
  public static final int E_WT_TENPAY_DENIED = 44;
  public static final int E_WT_WRONG_PASSWORD = 1;
  public static final int E_WT_WRONG_VERIFY_CODE = 4;
  public static final int E_WT_WTLOGIN_ACCOUNT_NAME_ERROR = 130;
  public static final int E_WT_WTLOGIN_CLIENTCLG_DENIED = 133;
  public static final int E_WT_WTLOGIN_DBSERVER_ERROR = 131;
  public static final int E_WT_WTLOGIN_NOT_ALLOWED = 64;
  public static final int E_WT_WTLOGIN_OPENSYSTEM_ERROR = 134;
  public static final int E_WT_WTLOGIN_OTHERERROR = 14;
  public static final int E_WT_WTLOGIN_PTLOGIN_ERROR = 132;
  public static final int E_WT_WTLOGIN_SECURITY_CENTER_ERROR = 128;
  public static final int E_WT_WTLOGIN_SESSION_ERROR = 129;
  public static final int ILLEGALARGUMENT_EXCEPTION_ERROR = 536;
  public static final int IO_EXCEPTION_ERROR = 535;
  public static final int IP_ADDRESS_NOT_VALID = 558;
  public static final int IP_ADDRESS_NULL = 557;

  @Deprecated
  public static final int LOGIN_NOPWD_INDB = 529;
  public static final int NETWORK_DISABLE = 519;
  public static final int NETWORK_WAIT_TIMEOUT = 527;
  public static final int NOT_SUPPORT_SHORT_COMMAND = 563;
  public static final int PING_SEND_FAILED = 600;
  public static final int READ_FAIL = 517;
  public static final int READ_TIME_OUT = 515;
  public static final String REG_ERROR_MESSAGE = "注册失败，请稍后重试";
  public static final int RSP_DATA_INVALID = 601;
  public static final int SEND_DONE_BUT_NETWORK_BROKEN = 532;
  public static final boolean SHOW_RESULT_CODE = false;
  public static final int SUCCESS = 0;
  public static final String TIMEOUT_ERROR_MESSAGE = "网络超时，请稍后重试";
  public static final int TLV_DECODE_FAIL = 554;
  public static final int TLV_DECOMPRESS_FAIL = 555;
  public static final int TLV_WRONG_DECOMPRESS_LENGTH = 556;

  @Deprecated
  public static final int UPLOADER_OPEN_SESSION_FAILED = 900;
  static final Set USE_SERVER_MSG = new HashSet();
  public static final int WNSCLOUD_APP_DECODE = 10003;
  public static final int WNSCLOUD_APP_SHUTDOWN = 10002;
  public static final int WNSCLOUD_APP_TIMEOUT = 10001;
  public static final int WNSCLOUD_CMD_IIIEGAL = 10101;
  public static final int WNSCLOUD_DERECT_RETURN_BEGIN = 10000;
  public static final int WNSCLOUD_DERECT_RETURN_END = 11000;
  public static final int WNSCLOUD_NO_APP_ROUTE = 10000;
  public static final int WNSCLOUD_NO_ROUTER = 10102;
  public static final int WNSCLOUD_SDK_VEIRY_FAIL = 10100;
  public static final int WNS_ASYNC_TIMEOUT = 528;
  public static final int WNS_BACKUP_IP_SESSION = 545;
  public static final int WNS_BUSI_BUFFER_NONE = 539;
  public static final int WNS_CANNOTSEND_INBG = 531;
  public static final int WNS_CDN_IP_SESSION = 546;
  public static final int WNS_CDN_PIC_DECODE_FAIL = 575;
  public static final int WNS_CDN_PIC_FAIL = 573;
  public static final int WNS_CDN_PIC_REDIRECT = 574;
  public static final int WNS_CMD_RESTRICT_RESERVE1 = 4031;
  public static final int WNS_CMD_RESTRICT_RESERVE10 = 4040;
  public static final int WNS_CODE_A2_DECRYPT_ERROR = 1052;
  public static final int WNS_CODE_ACC_B2_INVALID = 1065;
  public static final int WNS_CODE_ACC_DECRYPT_INVALID = 1062;
  public static final int WNS_CODE_ACC_GETKEYST_INVALID = 1061;
  public static final int WNS_CODE_ACC_INVALID_SESSIONHASH = 1053;
  public static final int WNS_CODE_ACC_NO_ROUTE = 2103;
  public static final int WNS_CODE_CMDSVR_UNPACK = 1504;
  public static final int WNS_CODE_DIS_STAT_BEGIN = 2400;
  public static final int WNS_CODE_DIS_STAT_END = 2499;
  public static final int WNS_CODE_LOGIN_3GSVR_BUSY = 1902;
  public static final int WNS_CODE_LOGIN_A2_CHANGED = 1907;
  public static final int WNS_CODE_LOGIN_A2_EXPIRED = 1906;
  public static final int WNS_CODE_LOGIN_A2_ILLEGAL = 1903;
  public static final int WNS_CODE_LOGIN_B2_EXPIRED = 1910;
  public static final int WNS_CODE_LOGIN_CHEKCSOO_FAILED = 1920;
  public static final int WNS_CODE_LOGIN_CODE_ILLEGAL = 1953;
  public static final int WNS_CODE_LOGIN_COMM_ERROR = 1924;
  public static final int WNS_CODE_LOGIN_CREATEUIDFAIL = 1909;
  public static final int WNS_CODE_LOGIN_GETGIDSVR_BUSY = 1925;
  public static final int WNS_CODE_LOGIN_ILLIGAL_APPID = 1912;
  public static final int WNS_CODE_LOGIN_JOSN_FAILED = 1928;
  public static final int WNS_CODE_LOGIN_NORIGHT = 1926;
  public static final int WNS_CODE_LOGIN_NOTOKEN = 1908;
  public static final int WNS_CODE_LOGIN_OPENDI_ILLEGAL = 1951;
  public static final int WNS_CODE_LOGIN_PARAM_ILLEGAL = 1921;
  public static final int WNS_CODE_LOGIN_PARAM_LOST = 1922;
  public static final int WNS_CODE_LOGIN_PID_ERROR = 1954;
  public static final int WNS_CODE_LOGIN_PTLOGIN_BUSY = 1901;
  public static final int WNS_CODE_LOGIN_QQSVR_BUSY = 1919;
  public static final int WNS_CODE_LOGIN_QUA_ALPHA_FORBIDDEN = 1915;
  public static final int WNS_CODE_LOGIN_REQ_METHOD_ERROR = 1923;
  public static final int WNS_CODE_LOGIN_SID_EXPIRED = 1905;
  public static final int WNS_CODE_LOGIN_SID_ILLEGAL = 1904;
  public static final int WNS_CODE_LOGIN_SIG_FILED = 1927;
  public static final int WNS_CODE_LOGIN_TIME_EXPIRED = 1950;
  public static final int WNS_CODE_LOGIN_TOKEN_ILLEGAL = 1952;
  public static final int WNS_CODE_LOGIN_UIDNOTCOMP = 1941;
  public static final int WNS_CODE_LOGIN_WEIXINSVR_BUSY = 1918;
  public static final int WNS_CODE_SUCCESS = 0;
  public static final int WNS_DIAGNOSIS_INSTALLED_FIREWALL = 550;
  public static final int WNS_DIAGNOSIS_NEED_AUTHENTIED = 549;
  public static final int WNS_DOMAIN_IP_SESSION = 544;
  public static final int WNS_HEART_BEAT_CONFIG = 548;
  public static final int WNS_HEART_BEAT_PUSH = 547;
  public static final int WNS_ILLEGAL_ARG = 604;
  public static final int WNS_INVALID_PARAMS = 522;
  public static final int WNS_LOAD_LIBS_FAILED = 562;
  public static final int WNS_LOCAL_B2_INVALID = 585;

  @Deprecated
  public static final int WNS_LOGGINGIN_ANOTHERUIN = 523;

  @Deprecated
  public static final int WNS_LOGGINGIN_SAMEUIN = 540;
  public static final int WNS_LOGIN_TOKEN_EXPIRED = 584;
  public static final int WNS_LOGOUT_CLEAR = 583;
  public static final int WNS_MAIN_ERR_CONNECT_FAIL = 2;
  public static final int WNS_MAIN_ERR_CONNECT_TIMEOUT = 3;
  public static final int WNS_MAIN_ERR_NETWORK_DISABLE = 14;
  public static final int WNS_MAIN_ERR_OTHER = 10;
  public static final int WNS_MAIN_ERR_PACK_FAIL = 8;
  public static final int WNS_MAIN_ERR_PARAM = 1;
  public static final int WNS_MAIN_ERR_QUICK_VERRIFICATION = 13;
  public static final int WNS_MAIN_ERR_RECV_FAIL = 6;
  public static final int WNS_MAIN_ERR_RECV_TIMEOUT = 7;
  public static final int WNS_MAIN_ERR_REQUEST_CANCEL = 12;
  public static final int WNS_MAIN_ERR_SEND_FAIL = 4;
  public static final int WNS_MAIN_ERR_SEND_TIMEOUT = 5;
  public static final int WNS_MAIN_ERR_SERVER = 11;
  public static final int WNS_MAIN_ERR_SUCCESS = 0;
  public static final int WNS_MAIN_ERR_UNPACK_FAIL = 9;
  public static final int WNS_MALICIOUS_USER_IP_RESERVE1 = 4011;
  public static final int WNS_MALICIOUS_USER_IP_RESERVE10 = 4020;
  public static final int WNS_MALICIOUS_USER_QQ_RESERVE1 = 4001;
  public static final int WNS_MALICIOUS_USER_QQ_RESERVE10 = 4010;
  public static final int WNS_NEED_WIFI_AUTH = 580;
  public static final int WNS_NONE_ACCOUNT = 581;

  @Deprecated
  public static final int WNS_NOTLOGGEDIN = 524;
  public static final int WNS_NOT_LOGIN = 533;
  public static final int WNS_NOT_READY = 520;
  public static final int WNS_OPEN_SESSION_FAILED_IN_NETWORK_DISABLED = 570;
  public static final int WNS_OPEN_SESSION_FAILED_PIC_FAILED_IN_BACKGROUND = 565;
  public static final int WNS_OPEN_SESSION_FAILED_PIC_FAILED_IN_FOREGROUND = 564;
  public static final int WNS_OPEN_SESSION_FAILED_PIC_FAILED_IN_POWERSAVING = 566;
  public static final int WNS_OPEN_SESSION_FAILED_PIC_SUC_IN_BACKGROUND = 560;
  public static final int WNS_OPEN_SESSION_FAILED_PIC_SUC_IN_FOREGROUND = 559;
  public static final int WNS_OPEN_SESSION_FAILED_PIC_SUC_IN_POWERSAVING = 561;
  public static final int WNS_OPEN_SESSION_FAILED_WIFI_REDIRECT_IN_BACKGROUND = 568;
  public static final int WNS_OPEN_SESSION_FAILED_WIFI_REDIRECT_IN_FOREGROUND = 567;
  public static final int WNS_OPEN_SESSION_FAILED_WIFI_REDIRECT_IN_POWERSAVING = 569;
  public static final int WNS_OPTI_IP_SESSION = 541;
  public static final int WNS_PACKAGE_ERROR = 526;
  public static final int WNS_PACKAGE_RECEIVING = 530;
  public static final int WNS_QUA_RESTRICT_RESERVE1 = 4021;
  public static final int WNS_QUA_RESTRICT_RESERVE10 = 4030;
  public static final int WNS_QUICK_VERIFICATION_UNSUPPORTED_OP = 901;
  public static final int WNS_RECENTLY_IP_SESSION = 543;
  public static final int WNS_REDIRECT_IP_SESSION = 542;
  public static final int WNS_REPORT_LOG_FAIL = 582;
  public static final int WNS_SCORE_IP_SESSION = 572;
  public static final int WNS_SCORE_IP_SUCCESS = 571;
  public static final int WNS_SDK_BIND_FAIL_BACKGROUND = 602;
  public static final int WNS_SDK_BIND_FAIL_FOREGROUND = 603;
  public static final int WNS_SDK_ERROR_CEIL = 999;
  public static final int WNS_SDK_ERROR_FLOOR = 512;
  public static final int WNS_SSO_ERROR = 3020;
  public static final int WNS_TRY_LATER = 605;
  public static final int WNS_UID_ERROR = 3013;
  public static final int WNS_WTLOGIN_UNHANDLED_ERROR = 525;
  public static final int WRITE_FAIL = 518;
  public static final int WRITE_TIME_OUT = 514;
  private static SparseArray a;
  private static SparseArray b;
  private static SparseArray c;
  private static final Set d;

  static
  {
    USE_SERVER_MSG.add(Integer.valueOf(1941));
    USE_SERVER_MSG.add(Integer.valueOf(1950));
    USE_SERVER_MSG.add(Integer.valueOf(1951));
    USE_SERVER_MSG.add(Integer.valueOf(1952));
    USE_SERVER_MSG.add(Integer.valueOf(1953));
    USE_SERVER_MSG.add(Integer.valueOf(1954));
    a = new SparseArray();
    b = new SparseArray();
    c = new SparseArray();
    d = new HashSet();
    d.add(Integer.valueOf(600));
    d.add(Integer.valueOf(532));
    d.add(Integer.valueOf(520));
    d.add(Integer.valueOf(1065));
    a.put(0, "success");
    a.put(1, "invalid parameter");
    a.put(2, "connect failed");
    a.put(3, "connect timeout");
    a.put(4, "send failed");
    a.put(5, "send timeout");
    a.put(6, "receive failed");
    a.put(7, "receive timeout");
    a.put(8, "pack data error");
    a.put(9, "unpack data error");
    a.put(10, "system error");
    a.put(11, "server error");
    a.put(12, "request has been canceled");
    a.put(13, "quck verification mode");
    a.put(14, "network disable");
    b.put(0, "成功");
    b.put(513, "当前网络不可用，请检查网络设置");
    b.put(514, "网络超时，请稍后重试");
    b.put(515, "网络超时，请稍后重试");
    b.put(516, "当前网络不可用，请检查网络设置");
    b.put(517, "网络繁忙，请稍后重试");
    b.put(518, "网络繁忙，请稍后重试");
    b.put(519, "当前网络不可用，请检查网络设置");
    b.put(521, "网络繁忙，请稍后重试");
    b.put(522, "网络繁忙，请稍后重试");
    b.put(526, "网络繁忙，请稍后重试");
    b.put(527, "网络繁忙，请稍后重试");
    b.put(528, "操作超时，请稍后重试");
    b.put(532, "网络连接不可用，请重新连接");
    b.put(1, "密码错误");
    b.put(2, "请输入验证码");
    b.put(3, "长时间未操作，请重新登录");
    b.put(4, "请输入正确验证码");
    b.put(5, "请输入正确用户名/密码");
    b.put(6, "请输入正确用户名/密码");
    b.put(7, "账号异常，请登录QQ安全中心查看详情");
    b.put(8, "无法识别的第三方");
    b.put(9, "网络繁忙，请稍后重试");
    b.put(10, "网络繁忙，请稍后重试");
    b.put(11, "网络繁忙，请稍后重试");
    b.put(12, "请输入正确用户名/密码");
    b.put(14, "网络繁忙，请稍后重试");
    b.put(15, "密码已过期，请重新登录");
    b.put(16, "长时间未操作，请重新登录");
    b.put(18, "请输入正确用户名/密码");
    b.put(32, "您的帐号长期未登录已经冻结，建议您到http://zc.qq.com 申请一个新号码使用。");
    b.put(33, "您的帐号由于存在安全风险，已启用临时登录限制，解除限制后即可正常登录。解除地址：\nhttp://aq.qq.com/xz");
    b.put(40, "您的帐号暂被冻结，请点击http://aq.qq.com/007查看详情");
    b.put(41, "您的QQ号码服务已到期，\n请尽快点击http://haoma.qq.com/expire/续费。\n固定电话拨打16885886可快捷续费");
    b.put(42, "您的帐号已锁定，解锁请查看http://aq.qq.com/mp?id=1&source_id=2040");
    b.put(43, "请输入正确用户名/密码");
    b.put(44, "请输入正确用户名/密码");
    b.put(48, "账号异常，请登录QQ安全中心查看详情");
    b.put(113, "账号异常，请登录QQ安全中心查看详情");
    b.put(128, "账号异常，请登录QQ安全中心查看详情");
    b.put(129, "网络繁忙，请稍后重试");
    b.put(154, "网络繁忙，请稍后重试");
    b.put(130, "请输入正确用户名/密码");
    b.put(131, "请输入正确用户名/密码");
    b.put(132, "网络繁忙，请稍后重试");
    b.put(133, "网络繁忙，请稍后重试");
    b.put(134, "网络繁忙，请稍后重试");
    b.put(160, "请输入短信验证码");
    b.put(161, "请输入短信验证码");
    b.put(162, "请输入短信验证码");
    b.put(163, "请输入短信验证码");
    b.put(901, "Quick verification mode does not support the operation");
    c.put(-1000, "网络超时，请稍后重试");
    c.put(112, "注册失败，请稍后重试");
    c.put(111, "手机号码错误");
    c.put(113, "注册失败，请稍后重试");
    c.put(114, "下发短信验证码失败");
    c.put(115, "达到最大频率限制");
    c.put(116, "注册失败，请稍后重试");
    c.put(117, "注册失败，请稍后重试");
    c.put(118, "短信验证失败");
    c.put(119, "注册失败，请稍后重试");
    c.put(120, "该手机号近期注册多次，请更换手机号或过段时间再来");
    c.put(121, "注册失败，请稍后重试");
    c.put(122, "注册失败，请稍后重试");
    c.put(123, "网络繁忙，请稍后重试");
    c.put(124, "网络繁忙，请稍后重试");
    c.put(125, "网络繁忙，请稍后重试");
    c.put(126, "密码安全系数较低，可尝试用字母、数字、符号的组合");
    c.put(127, "错误的国家地区");
    c.put(128, "该语言暂不支持");
    c.put(130, "达到短信发送最大次数限制");
    c.put(131, "达到短信验证码最大次数限制");
    c.put(132, "达到失败重试最大次数");
    c.put(133, "绑定失败");
    c.put(134, "解绑失败");
    c.put(135, "注册失败：已经注册过帐号");
    c.put(136, "注册失败，请稍后重试");
    c.put(137, "该邮箱安全系数较低，请更换");
    c.put(138, "发送激活邮件失败");
    c.put(139, "绑定邮箱失败");
    c.put(140, "该邮箱安全系数较低，请更换");
    c.put(200, "注册失败，请稍后重试");
    b.put(256, "网络繁忙，请稍后重试");
    b.put(263, "网络繁忙，请稍后重试");
    b.put(264, "请输入正确用户名/密码");
    b.put(272, "密码票据丢失或格式错误");
    b.put(1907, "密码已修改，请重新输入密码");
    b.put(4001, "该号码异常，存在安全风险，请定期修改密码");
    b.put(4011, "该网络异常，请一段时间后再试");
    b.put(4021, "该版本异常，请更新到最新版本");
    b.put(4031, "该操作已过期");
    b.put(583, "");
    b.put(585, "登录态异常，请重新登录");
    b.put(601, "解包失败");
  }

  private static String a(int paramInt)
  {
    String str1 = null;
    if (a != null)
      str1 = (String)a.get(paramInt);
    String str2 = str1;
    if (str1 == null)
      str2 = "操作失败，请稍后重试";
    return str2;
  }

  private static String b(int paramInt)
  {
    Object localObject2 = null;
    if (b != null)
      localObject2 = (String)b.get(paramInt);
    Object localObject1;
    if ((paramInt >= 2400) && (paramInt < 2499))
      localObject1 = (String)b.get(2400);
    while (true)
    {
      localObject2 = localObject1;
      if (localObject1 == null)
        localObject2 = "操作失败，请稍后重试";
      return localObject2;
      if ((paramInt >= 4001) && (paramInt <= 4010))
      {
        localObject1 = (String)b.get(4001);
        continue;
      }
      if ((paramInt >= 4011) && (paramInt <= 4020))
      {
        localObject1 = (String)b.get(4011);
        continue;
      }
      if ((paramInt >= 4021) && (paramInt <= 4030))
      {
        localObject1 = (String)b.get(4021);
        continue;
      }
      localObject1 = localObject2;
      if (paramInt < 4031)
        continue;
      localObject1 = localObject2;
      if (paramInt > 4040)
        continue;
      localObject1 = (String)b.get(4031);
    }
  }

  private static String c(int paramInt)
  {
    String str1 = null;
    if (c != null)
      str1 = (String)c.get(paramInt);
    String str2 = str1;
    if (str1 == null)
      str2 = b(paramInt);
    return str2;
  }

  public static boolean canRetry(int paramInt)
  {
    return d.contains(Integer.valueOf(paramInt));
  }

  public static int convertToMainErrorCode(int paramInt)
  {
    if (paramInt > 999)
    {
      if ((paramInt >= 10000) && (paramInt <= 11000))
        return paramInt;
      return 11;
    }
    switch (paramInt)
    {
    default:
      paramInt = 10;
    case 0:
    case 551:
    case 552:
    case 571:
    case 522:
    case 536:
    case 604:
    case 513:
    case 521:
    case 534:
    case 537:
    case 514:
    case 532:
    case 515:
    case 527:
    case 516:
    case 520:
    case 517:
    case 518:
    case 526:
    case 539:
    case 554:
    case 555:
    case 556:
    case 519:
    case 901:
    }
    while (true)
    {
      return paramInt;
      paramInt = 0;
      continue;
      paramInt = 1;
      continue;
      paramInt = 3;
      continue;
      paramInt = 5;
      continue;
      paramInt = 7;
      continue;
      paramInt = 2;
      continue;
      paramInt = 6;
      continue;
      paramInt = 4;
      continue;
      paramInt = 9;
      continue;
      paramInt = 14;
    }
  }

  public static String getErrorMessage(int paramInt)
  {
    return b(paramInt);
  }

  public static String getErrorMessage(int paramInt, String paramString)
  {
    if ((paramString != null) && (USE_SERVER_MSG.contains(Integer.valueOf(paramInt))))
      return paramString;
    return b(paramInt);
  }

  public static String getLoginMessage(int paramInt)
  {
    Object localObject1;
    switch (paramInt)
    {
    default:
      localObject1 = null;
    case 584:
    case 1941:
    case 1950:
    case 1951:
    case 1952:
    case 1953:
    case 585:
    }
    while (true)
    {
      Object localObject2 = localObject1;
      if (localObject1 == null)
        localObject2 = getErrorMessage(paramInt);
      localObject1 = localObject2;
      if (((String)localObject2).equals("操作失败，请稍后重试"))
        localObject1 = "登录失败，请稍后重试";
      return localObject1;
      localObject1 = (String)a.a().e().a("ExpireTimeMsg", "登录失败，请稍后重试");
      continue;
      localObject1 = (String)a.a().e().a("ReLoginMsg", "登录失败，请稍后重试");
      continue;
      localObject1 = (String)a.a().e().a("ReLoginMsg", "登录失败，请稍后重试");
    }
  }

  public static String getMainErrorMessage(int paramInt)
  {
    return a(paramInt);
  }

  public static String getRegErrorMessage(int paramInt)
  {
    return c(paramInt);
  }
}

/* Location:           C:\Users\xuetong\Desktop\dazhongdianping7.9.6\ProjectSrc\classes-dex2jar.jar
 * Qualified Name:     com.tencent.wns.client.data.WnsError
 * JD-Core Version:    0.6.0
 */