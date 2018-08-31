package com.hyhscm.myron.eapp.net;

import com.hyhscm.myron.eapp.activity.Home.HomeSearch;

/**
 * Created by Jason on 2017/12/18.
 */

public class Url {

    //    NSString*  const WEBPATH = @"http://m.hyhscm.com/";
//    NSString*  const IMGPATH = @"http://8.8.8.8:8080/web.server/";
//    NSString*  const HOSTNAME = @"http://8.8.8.8:8080/web.server"; //测试地址
//    NSString*  const HOSTNAME1 = @"http://10.10.10.18:8080/web.server/";
//    NSString*  const SERVERNAME = @"yjhm";
//    http://m.hyhscm.com/newsInfo.html?id=2943
//
    public static String WEBPATH = "http://m.hyhscm.com/";
    public static String HOSTNAME = "http://hyh.hyhscm.com";   //正式
    public static String IMAGEPATH = "http://hyh.hyhscm.com/"; //图片地址;
//    public static String WEBPATH = "http://10.10.10.88:8080/web.server/";
//    public static  String IMAGEPATH = "http://10.10.10.88:8080/web.server/"; //图片地址;
//    public static  String HOSTNAME = "http://10.10.10.88:8080/web.server"; //测试


    public static String SERVERNAME = "/yjhm";  //服务器名
    public static String GETVFCODE_LOGIN = "/verification/loginCode"; // 获取login验证码，本地无效 (parm phone)
    public static String GETVFCODE_REGIST = "/verification/registerCode"; // 获取注册验证码，本地无效 (parm phone)
    public static String GETVFCODE_CHANGECODE = "/verification/changeCode"; // 修改密码获取验证码，本地无效 (parm phone)

    public static String LOGIN = "/user/login";
    public static String CHANGEPWD = "/user/change"; //修改密码
    public static String CHANGECODEPWD = "/user/changeCode"; //忘记密码

    //index
    public static String INDEX = SERVERNAME + "/index"; //首页banner
    public static String HOMEDATA = SERVERNAME + "/uindex";//首页数据
    //需求
    public static String DEMAND = SERVERNAME + "/dq"; //获取需求列表
    public static String DEMAND_MY = SERVERNAME + "/dqu"; //获取wode需求列表
    public static String DEMAND_COMMENT = SERVERNAME + "/dcq"; //需求评论列表
    public static String DEMANDBYID = SERVERNAME + "/di";//根据id获取
    //    public static  String  DEMANDCOMMENT_DEL=HOSTNAME+SERVERNAME+"/did"; //删除需求评价
    public static String DEMAND_CREATE = SERVERNAME + "/df"; //发布需求
    public static String DEMAND_REMOVE = SERVERNAME + "/did"; //删除需求
    public static String DEMANDCOMMENT_ADD = SERVERNAME + "/drc"; //添加需求评论
    public static String DEMANDCOMMENT_DEL = SERVERNAME + "/rcd"; //删除需求评论

    //招商
    public static String MERCHANTLIST = SERVERNAME + "/pq"; //招商列表
    public static String MERCHANT_DETAIL = SERVERNAME + "/p"; //招商详情
    public static String MERCHANT_CREATE = SERVERNAME + "/ps";//发布招商
    public static String MERCHANT_REMOVE = SERVERNAME + "/pd"; //删除招商
    public static String MERCHANTCOMMENT = SERVERNAME + "/pcq"; //招商评价列表
    public static String MERCHANTCOMMENT_ADD = SERVERNAME + "/prc"; //添加招商评价
    public static String MERCHANTCOMMENT_DEL = SERVERNAME + "/rcd"; //删除招商评价

    //产品
    public static String GOODS = SERVERNAME + "/gq"; //产品列表
    public static String CATEGORY = SERVERNAME + "/category";//产品筛选分类
    public static String PRODUCTBYID = SERVERNAME + "/gi";//产品详情
    public static String PRODUCT_CATEGORY = SERVERNAME+"/pccq";//招商产品分类；
    public static String PRODUCT_INVISIBLE = SERVERNAME+"/pm";//产品上下架;
    //展厅
    public static String COMPANY = SERVERNAME + "/mqt";//展厅列表
    public static String COMPANY_DETAIL = SERVERNAME + "/m";//展厅详情
    //    public static  String OWNPRODUCT =  SERVERNAME + "/pqu";//展厅产品列表
    //1220
    public static String UPLOAD = "/FileController/upload"; //上传图片
    public static String REGIST = "/user/register";//注册
    public static String INPUTINFO = SERVERNAME + "/bizs";//完善信息
    //新闻
    public static String NEWS = SERVERNAME + "/nq"; //新闻列表
    public static String NEWSBYID = SERVERNAME + "/ni"; //get news  by id

    public static String NEWS_DETAIL = WEBPATH + "newsInfo.html";//新闻详情
    //我的

    public static String HEBILIST = SERVERNAME + "/jf"; //hebi列表
    public static String HEBIDETAIL = SERVERNAME + "/ujf"; //我的合币详情

    public static String MYCOMPANY = SERVERNAME + "/m"; //我的展厅
    public static String UPDATEMINE = SERVERNAME + "/ms"; //更新我的展厅

    public static String USER_INFO = SERVERNAME + "/user"; // 获取个人信息
    public static String USER_CHANGE = SERVERNAME + "/users"; // 修改个人信息

    public static String USER_AGENT = SERVERNAME + "/biz"; // 获取个人代理设置信息
    public static String USER_AGENT_CHANGE = SERVERNAME + "/bizs"; // 提交代理设置信息

    public static String USER_VERIFY = SERVERNAME + "/accred"; // 获取深度认证信息
    public static String USER_VERIFY_CHANGE = SERVERNAME + "/accredApply"; // 修改深度认证信息
    public static String FEEDBACK = SERVERNAME + "/fb";//意见反馈；
    //im
    public static String CONTACT = SERVERNAME + "/cc"; // 联系人表
    public static String CONTACT_INFO = SERVERNAME + "/cci"; // 联系人明细
    public static String CONTACT_SEARCH = SERVERNAME + "/cu"; // 查找用户列表
    public static String FRIEND_REQUEST = SERVERNAME + "/ccf"; // 最近好友请求列表
    public static String FRIEDN_DOREQUEST = SERVERNAME + "/ccaf"; // 请求信息处理
    public static String FRIEND_ADD = SERVERNAME + "/cca"; // 添加好友
    public static String FRIEND_DELETE = SERVERNAME + "/ccd";//删除联系人
    public static String TAGCATOGORY = SERVERNAME + "/lq"; //筛选标签
    public static String TAGHOSPITAL ="http://hyh.hyhscm.com"+SERVERNAME + "/aq"; //筛选地区

    public static String SYSMSGLIST = SERVERNAME + "/mmq";//系统消息
    public static String WXLOGIN = "/user/wxlogin";

    public static String FOCUSLIST = SERVERNAME + "/cp"; //关注动态
   //微信分享地址
    public static String WX_DEMAND = WEBPATH + "demandInfo.html?ws=1&su="; //0
    public static String WX_PRODUCTMERCHANT = WEBPATH + "productMerchant.html?ws=1&su=";//1
    public static String WX_NEWS = WEBPATH + "newsInfo.html?uid=undefined&ws=1&su=";//2

    public static String MAKETOP =  SERVERNAME + "/top"; // 置顶
    public static String SIGNINFO= SERVERNAME+"/signq";// 签到信息
    public static String SIGN = SERVERNAME+"/sign"; //签到
    public static String TOPNUM = SERVERNAME+"/topNum"; //首页头部数字；

    public static String SHARE_CALLBACK = SERVERNAME+"/share";//
//    public static String ABOUT_GOLDBEAN = "http://10.10.10.159/m_echain/integral.html"; //test
    public static String ABOUT_GOLDBEAN = WEBPATH+"integral.html";// 正式
    public static String REGIST_AGREEMENT = WEBPATH+"about/privacy.html";


    public static void setIHostName(String host) {
        HOSTNAME = host;
    }
    public static void setImagePath(String path) {
        IMAGEPATH = path;
    }
    public static void setWebPath (String path){WEBPATH= path;}

}
