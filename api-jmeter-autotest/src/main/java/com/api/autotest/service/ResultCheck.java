package com.api.autotest.service;

/**
 * Created by harvey.xu on 2017/10/17.
 */
public class ResultCheck {

//    public static boolean reconResultCheck(ArrayList<HashMap<String, String>> channelCodeList, JavaSamplerContext ctx) {
//        System.out.println("------------------------------校验数据库对账结果开始------------------------------");
//        Map<String, Boolean> channelReconResultMap = new HashMap<>();
//        boolean checkResult = false;
//        // 循环判断所有渠道对账结果
//        for (int i = 0; i < channelCodeList.size(); i++) {
//            String actualAcquireTradePlatformStatusStr;
//            String actualAcquireTradeChannelStatusStr;
//            String actualAgentPayTradePlatformStatusStr;
//            String actualAgentPayTradeChannelStatusStr;
//            String actualReconExpAcquireTradeExpCodeStr;
//            String actualReconExpAgentPayTradeExpCodeStr;
//            Boolean acquireTradePlatformStatusCheck = true;
//            Boolean acquireTradeChannelStatusCheck = true;
//            Boolean agentPayTradePlatformStatusCheck = true;
//            Boolean agentPayTradeChannelStatusCheck = true;
//            Boolean reconExpAcquireTradeExpCodeCheck = true;
//            Boolean reconExpAgentPayTradeExpCodeCheck = true;
//
//            if (channelCodeList.get(i).get("channel_code").equals("000000")) {
//                continue;
//            }
//            // 获取acquire_trade表对应平台交易的status
//            String getAcquireTradePlatformStatusSql = "SELECT t.`status` FROM acquire_trade t WHERE t.sub_channel_code='"
//                    + channelCodeList.get(i).get("channel_code") + "' AND t.trade_day='"
//                    + ctx.getParameter("tradeDate") + "' ORDER BY `status`";
//            ArrayList<HashMap<String, String>> actualAcquireTradePlatformStatusList = MysqlConnectionUtils.query(getAcquireTradePlatformStatusSql);
//            if (actualAcquireTradePlatformStatusList.size() > 0) {
//                actualAcquireTradePlatformStatusStr = ArrayUtils.arrayList2String(actualAcquireTradePlatformStatusList);
//                if (!actualAcquireTradePlatformStatusStr.equals(ctx.getParameter("expAcquireTradePlatformStatus"))) {
//                    acquireTradePlatformStatusCheck = false;
//                }
//                System.out.println("[" + channelCodeList.get(i).get("channel_code") + "]收单表对应平台交易对账状态预期结果 ："
//                        + ctx.getParameter("expAcquireTradePlatformStatus")
//                        + "<---->收单表对应平台交易对账状态实际结果 ：" + actualAcquireTradePlatformStatusStr);
//            }
//            // 获取acquire_trade表对应渠道交易的status
//            String getAcquireTradeChannelStatusSql = "SELECT t.`status` FROM acquire_trade t WHERE t.channel_code='"
//                    + channelCodeList.get(i).get("channel_code") + "' AND t.trade_day='"
//                    + ctx.getParameter("tradeDate") + "' ORDER BY `status`";
//            ArrayList<HashMap<String, String>> actualAcquireTradeChannelStatusList = MysqlConnectionUtils.query(getAcquireTradeChannelStatusSql);
//            if (actualAcquireTradeChannelStatusList.size() > 0) {
//                actualAcquireTradeChannelStatusStr = ArrayUtils.arrayList2String(actualAcquireTradeChannelStatusList);
//                if (!actualAcquireTradeChannelStatusStr.equals(ctx.getParameter("expAcquireTradeChannelStatus"))) {
//                    acquireTradeChannelStatusCheck = false;
//                }
//                System.out.println("[" + channelCodeList.get(i).get("channel_code") + "]收单表对应渠道交易对账状态预期结果 ："
//                        + ctx.getParameter("expAcquireTradeChannelStatus")
//                        + "<---->收单表对应渠道交易对账状态实际结果 ：" + actualAcquireTradeChannelStatusStr);
//            }
//            // 获取agentpay_trade表对应平台交易的status
//            String getAgentpayTradePlatformStatusSql = "SELECT t.`status` FROM agentpay_trade t WHERE t.sub_channel_code='"
//                    + channelCodeList.get(i).get("channel_code") + "' AND t.trade_day='"
//                    + ctx.getParameter("tradeDate") + "' ORDER BY `status`";
//            ArrayList<HashMap<String, String>> actualAgentpayTradePlatformStatusList = MysqlConnectionUtils.query(getAgentpayTradePlatformStatusSql);
//            if (actualAgentpayTradePlatformStatusList.size() > 0) {
//                actualAgentPayTradePlatformStatusStr = ArrayUtils.arrayList2String(actualAgentpayTradePlatformStatusList);
//                if (!actualAgentPayTradePlatformStatusStr.equals(ctx.getParameter("expAgentPayTradePlatformStatus"))) {
//                    agentPayTradePlatformStatusCheck = false;
//                }
//                System.out.println("[" + channelCodeList.get(i).get("channel_code") + "]代付表对应平台交易对账状态预期结果 ："
//                        + ctx.getParameter("expAgentPayTradePlatformStatus")
//                        + "<---->代付表对应平台交易对账状态实际结果 ：" + actualAgentPayTradePlatformStatusStr);
//            }
//            // 获取agentpay_trade表对应渠道交易的status
//            String getAgentpayTradeChannelStatusSql = "SELECT t.`status` FROM agentpay_trade t WHERE t.channel_code='"
//                    + channelCodeList.get(i).get("channel_code") + "' AND t.trade_day='"
//                    + ctx.getParameter("tradeDate") + "' ORDER BY `status`";
//            ArrayList<HashMap<String, String>> actualAgentPayTradeChannelStatusList = MysqlConnectionUtils.query(getAgentpayTradeChannelStatusSql);
//            if (actualAgentPayTradeChannelStatusList.size() > 0) {
//                actualAgentPayTradeChannelStatusStr = ArrayUtils.arrayList2String(actualAgentPayTradeChannelStatusList);
//                if (!actualAgentPayTradeChannelStatusStr.equals(ctx.getParameter("expAgentPayTradeChannelStatus"))) {
//                    agentPayTradeChannelStatusCheck = false;
//                }
//                System.out.println("[" + channelCodeList.get(i).get("channel_code") + "]代付表对应渠道交易对账状态预期结果 ："
//                        + ctx.getParameter("expAgentPayTradeChannelStatus")
//                        + "<---->代付表对应渠道交易对账状态实际结果 ：" + actualAgentPayTradeChannelStatusStr);
//            }
//            // 获取recon_exp表支付记录的exp_code
//            String getReconExpAcquireTradeExpCodeSql = "SELECT t.exp_code FROM recon_exp t WHERE t.channel_code='"
//                    + channelCodeList.get(i).get("channel_code") + "' AND t.trade_day='"
//                    + ctx.getParameter("tradeDate") + "' AND t.service_type='001' ORDER BY t.exp_code;";
//            ArrayList<HashMap<String, String>> actualReconExpAcquireTradeExpCodeList = MysqlConnectionUtils.query(getReconExpAcquireTradeExpCodeSql);
//            if (actualReconExpAcquireTradeExpCodeList.size() > 0) {
//                actualReconExpAcquireTradeExpCodeStr = ArrayUtils.arrayList2String(actualReconExpAcquireTradeExpCodeList);
//                if (!actualReconExpAcquireTradeExpCodeStr.equals(ctx.getParameter("expReconExpAcquireTradeExpCode"))) {
//                    reconExpAcquireTradeExpCodeCheck = false;
//                }
//                System.out.println("[" + channelCodeList.get(i).get("channel_code") + "]差异表对应渠道支付交易差错码预期结果 ："
//                        + ctx.getParameter("expReconExpAcquireTradeExpCode")
//                        + "<---->差异表对应渠道支付交易差错码实际结果 ：" + actualReconExpAcquireTradeExpCodeStr);
//            }
//            // 获取recon_exp表代付记录的exp_code
//            String getReconExpAgentPayTradeExpCodeSql = "SELECT t.exp_code FROM recon_exp t WHERE t.channel_code='"
//                    + channelCodeList.get(i).get("channel_code") + "' AND t.trade_day='"
//                    + ctx.getParameter("tradeDate") + "' AND t.service_type='002' ORDER BY t.exp_code;";
//            ArrayList<HashMap<String, String>> actualReconExpAgentPayTradeExpCodeList = MysqlConnectionUtils.query(getReconExpAgentPayTradeExpCodeSql);
//            if (actualReconExpAgentPayTradeExpCodeList.size() > 0) {
//                actualReconExpAgentPayTradeExpCodeStr = ArrayUtils.arrayList2String(actualReconExpAgentPayTradeExpCodeList);
//                if (!actualReconExpAgentPayTradeExpCodeStr.equals(ctx.getParameter("expReconExpAgentPayTradeExpCode"))) {
//                    reconExpAgentPayTradeExpCodeCheck = false;
//                }
//                System.out.println("[" + channelCodeList.get(i).get("channel_code") + "]差异表对应渠道代付交易差错码预期结果 ："
//                        + ctx.getParameter("expReconExpAgentPayTradeExpCode")
//                        + "<---->差异表对应渠道代付交易差错码实际结果 ：" + actualReconExpAgentPayTradeExpCodeStr);
//            }
//            // 判断
//            if (acquireTradePlatformStatusCheck && acquireTradeChannelStatusCheck
//                    && agentPayTradePlatformStatusCheck && agentPayTradeChannelStatusCheck
//                    && reconExpAcquireTradeExpCodeCheck && reconExpAgentPayTradeExpCodeCheck) {
//                System.out.println(">>>>>>>>>>>>渠道[" + channelCodeList.get(i).get("channel_code") + "]对账成功!");
//                channelReconResultMap.put(channelCodeList.get(i).get("channel_code"), true);
//            } else {
//                System.out.println(">>>>>>>>>>>>渠道[" + channelCodeList.get(i).get("channel_code") + "]对账失败!");
//                channelReconResultMap.put(channelCodeList.get(i).get("channel_code"), false);
//            }
//        }
//        // 判断所有渠道是否都成功
//        for (Boolean flag : channelReconResultMap.values()) {
//            if (!flag) {
//                checkResult = false;
//                break;
//            }
//            checkResult = true;
//        }
//        System.out.println("------------------------------校验数据库对账结果完成------------------------------");
//
//        return checkResult;
//    }
//
//    public static boolean tradeResultCheck(ArrayList<HashMap<String, String>> acTradeList, JavaSamplerContext ctx) {
//        System.out.println("------------------------------校验数据库代付账务结果开始------------------------------");
//        Map<String, Boolean> acTradeResultMap = new HashMap<>();
//        boolean checkResult = false;
//
//        String acTradeOrderStatusStr;
//        String acTradeDetailStatusStr;
//        String acTradeOrderNumberStr;
//        String acTradeDetailNumberStr;
//        String acTransactionNumberStr;
//        String acSequentialNumberStr;
//        Boolean acTradeOrderStatusCheck = true;
//        Boolean acTradeDetailStatusCheck = true;
//        Boolean acTradeOrderNumberCheck = true;
//        Boolean acTradeDetailNumberCheck = true;
//        Boolean acTransactionNumberCheck = true;
//        Boolean acSequentialNumberCheck = true;
//
//        if (!acTradeList.isEmpty()) {
//            // 获取AC_TRADE_ORDER表对应代付的status
//            String getAcTradeOrderStatusSql = "SELECT status FROM ACCOUNT.AC_TRADE_ORDER t where t.trade_no = '" + acTradeList.get(0).get("TRADE_NO") + "' ORDER BY trade_no";
//            ArrayList<HashMap<String, String>> acTradeOrderStatusList = OracleConnectionUtils.query(getAcTradeOrderStatusSql);
//            if (acTradeOrderStatusList.size() > 0) {
//                acTradeOrderStatusStr = ArrayUtils.arrayList2String(acTradeOrderStatusList);
//                if (!acTradeOrderStatusStr.equals(ctx.getParameter("expAcTradeOrderStatus"))) {
//                    acTradeOrderStatusCheck = false;
//                }
//                System.out.println("   代付账务订单表记账状态预期结果 ："
//                        + ctx.getParameter("expAcTradeOrderStatus")
//                        + "   代付账务订单表记账状态实际结果 ：" + acTradeOrderStatusStr);
//            }
//            // 获取AC_TRADE_DETAIL表对应代付的status
//            String getAcTradeDetailStatusSql = "SELECT status FROM ACCOUNT.AC_TRADE_DETAIL t where t.trade_no = '" + acTradeList.get(0).get("TRADE_NO") + "' ORDER BY trade_no, trade_detail_no";
//            ArrayList<HashMap<String, String>> acTradeDetailStatusList = OracleConnectionUtils.query(getAcTradeDetailStatusSql);
//            if (acTradeDetailStatusList.size() > 0) {
//                acTradeDetailStatusStr = ArrayUtils.arrayList2String(acTradeDetailStatusList);
//                if (!acTradeDetailStatusStr.equals(ctx.getParameter("expAcTradeDetailStatus"))) {
//                    acTradeDetailStatusCheck = false;
//                }
//                System.out.println("   代付账务详情表记账状态预期结果 ："
//                        + ctx.getParameter("expAcTradeDetailStatus")
//                        + "   代付账务详情表记账状态实际结果 ：" + acTradeDetailStatusStr);
//            }
//            // 获取AC_TRADE_ORDER表新增条数
//            String getAcTradeOrderNumberSql = "SELECT count(*) FROM ACCOUNT.AC_TRADE_ORDER t where t.trade_no = '" + acTradeList.get(0).get("TRADE_NO") + "'";
//            ArrayList<HashMap<String, String>> acTradeOrderNumberList = OracleConnectionUtils.query(getAcTradeOrderNumberSql);
//            if (acTradeOrderNumberList.size() > 0) {
//                acTradeOrderNumberStr = ArrayUtils.arrayList2String(acTradeOrderNumberList);
//                if (!acTradeOrderNumberStr.equals(ctx.getParameter("expAcTradeOrderNumber"))) {
//                    acTradeOrderNumberCheck = false;
//                }
//                System.out.println("   代付账务订单表新增条数预期结果 ："
//                        + ctx.getParameter("expAcTradeOrderNumber")
//                        + "   代付账务订单表新增条数实际结果 ：" + acTradeOrderNumberStr);
//            }
//            // 获取AC_TRADE_DETAIL表新增条数
//            String getAcTradeDetailNumberSql = "SELECT count(*) FROM ACCOUNT.AC_TRADE_DETAIL t where t.trade_no = '" + acTradeList.get(0).get("TRADE_NO") + "'";
//            ArrayList<HashMap<String, String>> acTradeDetailNumberList = OracleConnectionUtils.query(getAcTradeDetailNumberSql);
//            if (acTradeDetailNumberList.size() > 0) {
//                acTradeDetailNumberStr = ArrayUtils.arrayList2String(acTradeDetailNumberList);
//                if (!acTradeDetailNumberStr.equals(ctx.getParameter("expAcTradeDetailNumber"))) {
//                    acTradeDetailNumberCheck = false;
//                }
//                System.out.println("   代付账务详情表新增条数预期结果 ："
//                        + ctx.getParameter("expAcTradeDetailNumber")
//                        + "   代付账务详情表新增条数实际结果 ：" + acTradeDetailNumberStr);
//            }
//            // 获取ac_transaction表新增条数
//            String getAcTransactionNumberSql = "SELECT count(*) FROM ACCOUNT.ac_transaction t where t.transaction_code = '" + acTradeList.get(0).get("TRANSACTION_CODE") + "' ORDER BY id";
//            ArrayList<HashMap<String, String>> acTransactionNumberList = OracleConnectionUtils.query(getAcTransactionNumberSql);
//            if (acTransactionNumberList.size() > 0) {
//                acTransactionNumberStr = ArrayUtils.arrayList2String(acTransactionNumberList);
//                if (!acTransactionNumberStr.equals(ctx.getParameter("expAcTransactionNumber"))) {
//                    acTransactionNumberCheck = false;
//                }
//                System.out.println("   ac_transaction表新增条数预期结果 ："
//                        + ctx.getParameter("expAcTransactionNumber")
//                        + "   ac_transaction表新增条数实际结果 ：" + acTransactionNumberStr);
//            }
//            // 获取ac_sequential表新增条数
//            String getAcSequentialNumberSql = "SELECT count(*) FROM account.ac_sequential t2 where t2.transaction_id in (select id from ACCOUNT.ac_transaction t where t.transaction_code = '" + acTradeList.get(0).get("TRANSACTION_CODE") + "')";
//            ArrayList<HashMap<String, String>> acSequentialNumberList = OracleConnectionUtils.query(getAcSequentialNumberSql);
//            if (acSequentialNumberList.size() > 0) {
//                acSequentialNumberStr = ArrayUtils.arrayList2String(acSequentialNumberList);
//                if (!acSequentialNumberStr.equals(ctx.getParameter("expAcSequentialNumber"))) {
//                    acSequentialNumberCheck = false;
//                }
//                System.out.println("   ac_sequential表新增条数预期结果 ："
//                        + ctx.getParameter("expAcSequentialNumber")
//                        + "   ac_sequential表新增条数实际结果 ：" + acSequentialNumberStr);
//            }
//
//            // 判断
//            if (acTradeOrderStatusCheck && acTradeDetailStatusCheck && acTransactionNumberCheck && acSequentialNumberCheck &&acTradeOrderNumberCheck && acTradeDetailNumberCheck) {
//                System.out.println(">>>>>>>>>>>>代付账务接口记账成功!");
//                //acTradeResultMap.put(acTradeList.get(0).get("trade_no"), true);
//                checkResult = true;
//            } else {
//                System.out.println(">>>>>>>>>>>>代付账务接口记账失败!");
//                //acTradeResultMap.put(acTradeList.get(0).get("trade_no"), false);
//                checkResult = false;
//            }
//        } else {
//
//                System.out.println(">>>>>>>>>>>>代付账务接口记账成功!");
//                checkResult = true;
//
//            }
//
//        System.out.println("------------------------------校验数据库代付账务结果完成------------------------------");
//
//        return checkResult;
//
//        }
//
//    public static boolean tradeResultCheck2(ArrayList<HashMap<String, String>> acTradeList, JavaSamplerContext ctx) {
//        System.out.println("------------------------------校验数据库支付账务结果开始------------------------------");
//        Map<String, Boolean> acTradeResultMap = new HashMap<>();
//        boolean checkResult = false;
//
//        String acTradeOrderStatusStr;
//        String acTradeOrderNumberStr;
//        String acTransactionNumberStr;
//        String acSequentialNumberStr;
//        Boolean acTradeOrderStatusCheck = true;
//        Boolean acTradeOrderNumberCheck = true;
//        Boolean acTransactionNumberCheck = true;
//        Boolean acSequentialNumberCheck = true;
//
//        if (!acTradeList.isEmpty()) {
//            // 获取AC_TRADE_ORDER表对应代付的status
//            String getAcTradeOrderStatusSql = "SELECT status FROM ACCOUNT.AC_TRADE_ORDER t where t.trade_no = '" + acTradeList.get(0).get("TRADE_NO") + "' ORDER BY trade_no";
//            ArrayList<HashMap<String, String>> acTradeOrderStatusList = OracleConnectionUtils.query(getAcTradeOrderStatusSql);
//            if (acTradeOrderStatusList.size() > 0) {
//                acTradeOrderStatusStr = ArrayUtils.arrayList2String(acTradeOrderStatusList);
//                if (!acTradeOrderStatusStr.equals(ctx.getParameter("expAcTradeOrderStatus"))) {
//                    acTradeOrderStatusCheck = false;
//                }
//                System.out.println("   支付账务订单表记账状态预期结果 ："
//                        + ctx.getParameter("expAcTradeOrderStatus")
//                        + "   支付账务订单表记账状态实际结果 ：" + acTradeOrderStatusStr);
//            }
//
//            // 获取AC_TRADE_ORDER表新增条数
//            String getAcTradeOrderNumberSql = "SELECT count(*) FROM ACCOUNT.AC_TRADE_ORDER t where t.trade_no = '" + acTradeList.get(0).get("TRADE_NO") + "'";
//            ArrayList<HashMap<String, String>> acTradeOrderNumberList = OracleConnectionUtils.query(getAcTradeOrderNumberSql);
//            if (acTradeOrderNumberList.size() > 0) {
//                acTradeOrderNumberStr = ArrayUtils.arrayList2String(acTradeOrderNumberList);
//                if (!acTradeOrderNumberStr.equals(ctx.getParameter("expAcTradeOrderNumber"))) {
//                    acTradeOrderNumberCheck = false;
//                }
//                System.out.println("   支付账务订单表新增条数预期结果 ："
//                        + ctx.getParameter("expAcTradeOrderNumber")
//                        + "   支付账务订单表新增条数实际结果 ：" + acTradeOrderNumberStr);
//            }
//
//            // 获取ac_transaction表新增条数
//            String getAcTransactionNumberSql = "SELECT count(*) FROM ACCOUNT.ac_transaction t where t.transaction_code = '" + acTradeList.get(0).get("TRANSACTION_CODE") + "' ORDER BY id";
//            ArrayList<HashMap<String, String>> acTransactionNumberList = OracleConnectionUtils.query(getAcTransactionNumberSql);
//            if (acTransactionNumberList.size() > 0) {
//                acTransactionNumberStr = ArrayUtils.arrayList2String(acTransactionNumberList);
//                if (!acTransactionNumberStr.equals(ctx.getParameter("expAcTransactionNumber"))) {
//                    acTransactionNumberCheck = false;
//                }
//                System.out.println("   ac_transaction表新增条数预期结果 ："
//                        + ctx.getParameter("expAcTransactionNumber")
//                        + "   ac_transaction表新增条数实际结果 ：" + acTransactionNumberStr);
//            }
//            // 获取ac_sequential表新增条数
//            String getAcSequentialNumberSql = "SELECT count(*) FROM account.ac_sequential t2 where t2.transaction_id in (select id from ACCOUNT.ac_transaction t where t.transaction_code = '" + acTradeList.get(0).get("TRANSACTION_CODE") + "')";
//            ArrayList<HashMap<String, String>> acSequentialNumberList = OracleConnectionUtils.query(getAcSequentialNumberSql);
//            if (acSequentialNumberList.size() > 0) {
//                acSequentialNumberStr = ArrayUtils.arrayList2String(acSequentialNumberList);
//                if (!acSequentialNumberStr.equals(ctx.getParameter("expAcSequentialNumber"))) {
//                    acSequentialNumberCheck = false;
//                }
//                System.out.println("   ac_sequential表新增条数预期结果 ："
//                        + ctx.getParameter("expAcSequentialNumber")
//                        + "   ac_sequential表新增条数实际结果 ：" + acSequentialNumberStr);
//            }
//
//            // 判断
//            if (acTradeOrderStatusCheck && acTransactionNumberCheck && acSequentialNumberCheck &&acTradeOrderNumberCheck) {
//                System.out.println(">>>>>>>>>>>>支付账务接口记账成功!");
//                //acTradeResultMap.put(acTradeList.get(0).get("trade_no"), true);
//                checkResult = true;
//            } else {
//                System.out.println(">>>>>>>>>>>>支付账务接口记账失败!");
//                //acTradeResultMap.put(acTradeList.get(0).get("trade_no"), false);
//                checkResult = false;
//            }
//        } else {
//
//            System.out.println(">>>>>>>>>>>>支付账务接口记账成功!");
//            checkResult = true;
//
//        }
//
//        System.out.println("------------------------------校验数据库支付账务结果完成------------------------------");
//
//        return checkResult;
//
//    }

}
