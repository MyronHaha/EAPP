package com.hyphenate.easeui.utils;

import com.hyphenate.easeui.model.Contract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 2018/1/24.
 */

public class Global {
        public static String IMAGEPATH = "http://hyh.hyhscm.com"; //图片地址;
//    public static  String IMAGEPATH = "http://8.8.8.8:8080/web.server"; //图片地址;
   public static List<Contract> allContractList = new ArrayList<>();
   public static  List<Contract> contractList = new ArrayList();
    public static List getContractList(){
        return  contractList;
    }
    public static String userImg = "";
    public static Contract getNickByUUid(String UUID) {
//        Map<String, EMConversation> map = EMClient.getInstance().chatManager().getAllConversations();
//        List<EMConversation> list = mapTransitionList(map);
        Contract contract = new Contract();
        contract.setNname("消息");
        contract.setUiid(UUID);
        if (Global.contractList.size() > 0) {
            String nick = UUID;
            for (Contract c : Global.contractList) {

                if (c.getUiid().toLowerCase().equals(UUID.toLowerCase())) {
                    nick = c.getNname();
                    contract = c;
                }
            }
            return contract;
        }
        return contract;
    }

    public static Contract getNickByAll(String UUID) {
//        Map<String, EMConversation> map = EMClient.getInstance().chatManager().getAllConversations();
//        List<EMConversation> list = mapTransitionList(map);
        Contract contract = new Contract();
        contract.setNname("消息");
        contract.setUiid(UUID);
        if (Global.contractList.size() > 0) {
//            String nick = UUID;
            Boolean isFind = false;
            for (Contract c : Global.contractList) {

                if (c.getUiid().toLowerCase().equals(UUID.toLowerCase())) {
//                    nick = c.getNname();
                    contract = c;
                    isFind = true;
                }
            }

            if (isFind) {
                return contract;
            }else{
                if(Global.allContractList.size()>0){
                    for (Contract c : Global.allContractList) {

                        if (c.getUiid().toLowerCase().equals(UUID.toLowerCase())) {
                            contract = c;
                            isFind = true;
                        }
                    }
                    return contract;
                }

            }

        }
        return contract;
    }

    public  static void setCurrentUserImgUrl(String url){
        userImg= url;
    }
    public static String getCurrentUserImgUrl(){
        return userImg;
    }
}
