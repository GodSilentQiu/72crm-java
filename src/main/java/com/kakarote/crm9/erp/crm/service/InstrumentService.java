package com.kakarote.crm9.erp.crm.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.kakarote.crm9.erp.bi.common.BiTimeUtil;
import com.kakarote.crm9.utils.R;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.*;

public class InstrumentService {
    @Inject
    BiTimeUtil biTimeUtil;
    /**
     * 销售简报
     */
    public R queryBulletin(String status, String userIds,String startTime ,String endTime) {
        //1.今天 2.昨天 3.本周 4.上周 5.本月6.上月7.本季度8.上季度9.本年10上年
        Integer type = biTimeUtil.analyzeType(status);
        if (type == 1) {
            return R.ok().put("data", Db.findFirst(Db.getSqlPara("crm.Instrument.intraday", Kv.by("userIds", userIds))));
        } else if (type == 2) {
            return R.ok().put("data", Db.findFirst(Db.getSqlPara("crm.Instrument.yesterday", Kv.by("userIds", userIds))));
        } else if (type == 3) {
            return R.ok().put("data", Db.findFirst(Db.getSqlPara("crm.Instrument.thisWeek", Kv.by("userIds", userIds))));
        } else if (type == 4) {
            return R.ok().put("data", Db.findFirst(Db.getSqlPara("crm.Instrument.lastWeek", Kv.by("userIds", userIds))));
        } else if (type == 5) {
            return R.ok().put("data", Db.findFirst(Db.getSqlPara("crm.Instrument.theSameMonth", Kv.by("userIds", userIds))));
        } else if (type == 6) {
            return R.ok().put("data", Db.findFirst(Db.getSqlPara("crm.Instrument.lastMonth", Kv.by("userIds", userIds))));
        } else if (type == 7) {
            return R.ok().put("data", Db.findFirst(Db.getSqlPara("crm.Instrument.currentSeason", Kv.by("userIds", userIds))));
        } else if (type == 8) {
            return R.ok().put("data", Db.findFirst(Db.getSqlPara("crm.Instrument.precedingQuarter", Kv.by("userIds", userIds))));
        } else if (type == 9) {
            return R.ok().put("data", Db.findFirst(Db.getSqlPara("crm.Instrument.thisYear", Kv.by("userIds", userIds))));
        } else if (type == 10) {
            return R.ok().put("data", Db.findFirst(Db.getSqlPara("crm.Instrument.lastYear", Kv.by("userIds", userIds))));
        }else if (type == 11){
            return R.ok().put("data", Db.findFirst(Db.getSqlPara("crm.Instrument.custom",
                    Kv.by("userIds", userIds).set("startTime",startTime).set("endTime",endTime))));
        }
        return R.error();
    }


    /**
     * 业绩指标
     */
    public R queryPerformance(String startTime, String endTime, String userIds,String deptIds, Integer status,String type,String allUsetIds) {
            if (StrUtil.isNotEmpty(type)){
                Record r = getTime(type);
                startTime = r.getStr("startTime");
                endTime = r.getStr("endTime");
            }
        //status 1 回款 2.合同
        Record record = Db.findFirst(Db.getSqlPara("crm.Instrument.queryMoneys", Kv.by("startTime", startTime).set("endTime", endTime).set("userIds", allUsetIds)));
        if (record == null) {
            return R.ok().put("data", new Record().set("contractMoneys", 0).set("receivablesMoneys", 0).set("achievementMoneys", 0).set("proportion", 0));
        }
        List<Integer> list = getYear(startTime, endTime);
        BigDecimal money = new BigDecimal(0);
        if (list.size() == 1) {
            List<Record> starts = new ArrayList<>();
            if (StrUtil.isNotEmpty(deptIds)){
                starts.addAll( Db.find(Db.getSqlPara("crm.Instrument.queryTargets", Kv.by("year", list.get(0)).set("status", status).set("deptIds",deptIds))));
            }

            if (StrUtil.isNotEmpty(userIds)){
                starts.addAll(Db.find(Db.getSqlPara("crm.Instrument.queryTargets", Kv.by("year", list.get(0)).set("status", status).set("userIds",userIds))));
            }
            Integer sta = Integer.valueOf(startTime.substring(4, 6));
            Integer en = Integer.valueOf(endTime.substring(4, 6));
            for (Record start: starts) {
                if (start != null) {
                    if (sta <= 1 && en >= 1) {
                        money = money.add(new BigDecimal(start.getStr("january")));
                    }
                    if (sta <= 2 && en >= 1) {
                        money = money.add(new BigDecimal(start.getStr("february")));
                    }
                    if (sta <= 3 && en >= 1) {
                        money = money.add(new BigDecimal(start.getStr("march")));
                    }
                    if (sta <= 4 && en >= 1) {
                        money = money.add(new BigDecimal(start.getStr("april")));
                    }
                    if (sta <= 5 && en >= 1) {
                        money = money.add(new BigDecimal(start.getStr("may")));
                    }
                    if (sta <= 6 && en >= 1) {
                        money = money.add(new BigDecimal(start.getStr("june")));
                    }
                    if (sta <= 7 && en >= 1) {
                        money = money.add(new BigDecimal(start.getStr("july")));
                    }
                    if (sta <= 8 && en >= 1) {
                        money = money.add(new BigDecimal(start.getStr("august")));
                    }
                    if (sta <= 9 && en >= 1) {
                        money = money.add(new BigDecimal(start.getStr("september")));
                    }
                    if (sta <= 10 && en >= 1) {
                        money = money.add(new BigDecimal(start.getStr("october")));
                    }
                    if (sta <= 11 && en >= 1) {
                        money = money.add(new BigDecimal(start.getStr("november")));
                    }
                    if (sta <= 12 && en >= 1) {
                        money = money.add(new BigDecimal(start.getStr("december")));
                    }
                }
            }

        } else {
            for (int i = 1; i < list.size() - 1; i++) {
                Record r = new Record();
                if (StrUtil.isNotEmpty(userIds)){
                    r = Db.findFirst(Db.getSqlPara("crm.Instrument.queryTarget", Kv.by("year", list.get(i)).set("status", status).set("userIds", userIds)));
                    money = money.add(new BigDecimal(r.getStr("achievementTarget")));
                }
                if (StrUtil.isNotEmpty(deptIds)){
                    r = Db.findFirst(Db.getSqlPara("crm.Instrument.queryTarget", Kv.by("year", list.get(i)).set("status", status).set("deptIds", deptIds)));
                    money = money.add(new BigDecimal(r.getStr("achievementTarget")));
                }
            }
            List<Record> starts = new ArrayList<>();
            if (StrUtil.isNotEmpty(deptIds)){
                starts.addAll(Db.find(Db.getSqlPara("crm.Instrument.queryTargets", Kv.by("year", list.get(0)).set("deptIds",deptIds).set("status", status))));
            }
            if (StrUtil.isNotEmpty(userIds)){
                starts.addAll(Db.find(Db.getSqlPara("crm.Instrument.queryTargets", Kv.by("year", list.get(0)).set("userIds",userIds).set("status", status))));
            }
            Integer sta = Integer.valueOf(startTime.substring(4, 6));
            for (Record start: starts) {
                if (start != null) {
                    if (sta <= 1) {
                        money = money.add(new BigDecimal(start.getStr("january")));
                    }
                    if (sta <= 2) {
                        money = money.add(new BigDecimal(start.getStr("february")));
                    }
                    if (sta <= 3) {
                        money = money.add(new BigDecimal(start.getStr("march")));
                    }
                    if (sta <= 4) {
                        money = money.add(new BigDecimal(start.getStr("april")));
                    }
                    if (sta <= 5) {
                        money = money.add(new BigDecimal(start.getStr("may")));
                    }
                    if (sta <= 6) {
                        money = money.add(new BigDecimal(start.getStr("june")));
                    }
                    if (sta <= 7) {
                        money = money.add(new BigDecimal(start.getStr("july")));
                    }
                    if (sta <= 8) {
                        money = money.add(new BigDecimal(start.getStr("august")));
                    }
                    if (sta <= 9) {
                        money = money.add(new BigDecimal(start.getStr("september")));
                    }
                    if (sta <= 10) {
                        money = money.add(new BigDecimal(start.getStr("october")));
                    }
                    if (sta <= 11) {
                        money = money.add(new BigDecimal(start.getStr("november")));
                    }
                    if (sta <= 12) {
                        money = money.add(new BigDecimal(start.getStr("december")));
                    }
                }
            }
            List<Record> ends = new ArrayList<>();
            if (StrUtil.isNotEmpty(deptIds)){
                ends.addAll(Db.find(Db.getSqlPara("crm.Instrument.queryTargets", Kv.by("year", list.get(list.size() - 1)).set("deptIds",deptIds).set("status", status))));
            }
            if (StrUtil.isNotEmpty(userIds)){
                ends.addAll(Db.find(Db.getSqlPara("crm.Instrument.queryTargets", Kv.by("year", list.get(list.size() - 1)).set("userIds",userIds).set("status", status))));
            }
            Integer en = Integer.valueOf(endTime.substring(4, 6));
            for (Record end: ends) {
                if (end != null) {
                    if (en >= 1) {
                        money = money.add(new BigDecimal(end.getStr("january")));
                    }
                    if (en >= 2) {
                        money = money.add(new BigDecimal(end.getStr("february")));
                    }
                    if (en >= 3) {
                        money = money.add(new BigDecimal(end.getStr("march")));
                    }
                    if (en >= 4) {
                        money = money.add(new BigDecimal(end.getStr("april")));
                    }
                    if (en >= 5) {
                        money = money.add(new BigDecimal(end.getStr("may")));
                    }
                    if (en >= 6) {
                        money = money.add(new BigDecimal(end.getStr("june")));
                    }
                    if (en >= 7) {
                        money = money.add(new BigDecimal(end.getStr("july")));
                    }
                    if (en >= 8) {
                        money = money.add(new BigDecimal(end.getStr("august")));
                    }
                    if (en >= 9) {
                        money = money.add(new BigDecimal(end.getStr("september")));
                    }
                    if (en >= 10) {
                        money = money.add(new BigDecimal(end.getStr("october")));
                    }
                    if (en >= 11) {
                        money = money.add(new BigDecimal(end.getStr("november")));
                    }
                    if (en >= 12) {
                        money = money.add(new BigDecimal(end.getStr("december")));
                    }
                }
            }
        }
        record.set("achievementMoneys", money);
        if (money.compareTo(new BigDecimal(0)) == 0) {
            record.set("proportion", 0);
        } else {
            if (status == 1) {
                record.set("proportion", new BigDecimal(record.getStr("receivablesMoneys")).multiply(new BigDecimal(100)).divide(money, 2, BigDecimal.ROUND_HALF_UP));
            } else if (status == 2) {
                record.set("proportion", new BigDecimal(record.getStr("contractMoneys")).multiply(new BigDecimal(100)).divide(money, 2, BigDecimal.ROUND_HALF_UP));
            }
        }
        return R.ok().put("data", record);
    }

    /**
     * 获取传过来的年份
     */
    private List<Integer> getYear(String startTime, String endTime) {
        List<Integer> list = new ArrayList<>();
        Integer start = Integer.valueOf(startTime.substring(0, 4));
        Integer end = Integer.valueOf(endTime.substring(0, 4));
        for (int i = start; i <= end; i++) {
            list.add(i);
        }
        return list;
    }
    private Record getTime(String type){
        Record record = new Record();
        String startTime;
        String endTime;
        Integer strYear = 0;
        Integer strMonth = 0;
        Integer endYear = 0;
        Integer endMonth = 0;
        Date date = DateUtil.date();
        if ("year".equals(type)){
            strYear =  DateUtil.year(date);
            endYear =  DateUtil.year(date);
            strMonth = 1;
            endMonth = 12;
        }else if ("lastYear".equals(type)){
           // status = 10;
            strYear =  DateUtil.year(date) - 1;
            endYear =  DateUtil.year(date) - 1;
            strMonth = 1;
            endMonth = 12;
        }else if ("quarter".equals(type)){
           // status = 7;
            int q = DateUtil.quarter(date);
            if (q == 1){
                strMonth = 1;
                endMonth = 3;
            }else if (q == 2){
                strMonth = 4;
                endMonth = 6;
            }else if (q == 3){
                strMonth = 7;
                endMonth = 9;
            }else if (q == 4){
                strMonth = 10;
                endMonth = 12;
            }
            strYear =  DateUtil.year( DateUtil.beginOfQuarter(date));
            endYear = DateUtil.year( DateUtil.endOfQuarter(date));
        }else if ("lastQuarter".equals(type)){
            int q = DateUtil.quarter(date);
            if (q == 1){
                strMonth = 10;
                endMonth = 12;
                strYear =  DateUtil.year(date) -1;
                endYear =  DateUtil.year(date)-1;
            }else if (q == 2){
                strMonth = 1;
                endMonth = 3;
                strYear =  DateUtil.year(date);
                endYear =  DateUtil.year(date);
            }else if (q == 3){
                strMonth = 4;
                endMonth = 6;
                strYear =  DateUtil.year(date);
                endYear =  DateUtil.year(date);
            }else if (q == 4){
                strMonth = 7;
                endMonth = 9;
                strYear =  DateUtil.year(date);
                endYear =  DateUtil.year(date);
            }
        }else if ("month".equals(type)){
           // status = 5;
            strYear =  DateUtil.year( DateUtil.offsetDay(DateUtil.nextMonth(), -1));
            endYear = DateUtil.year( DateUtil.offsetDay(DateUtil.nextMonth(), -1));
            strMonth =  DateUtil.month(DateUtil.offsetDay(DateUtil.nextMonth(), -1))+1;
            endMonth =  DateUtil.month( DateUtil.offsetDay(DateUtil.nextMonth(), -1))+1;
        }else if ("lastMonth".equals(type)){

            strYear =  DateUtil.year(  DateUtil.offsetMonth(date,-1));
            endYear = DateUtil.year( DateUtil.offsetMonth(date,-1));
            strMonth = DateUtil.month(DateUtil.offset(date, DateField.MONTH, -1))+1;
            endMonth =  DateUtil.month( DateUtil.offset(date, DateField.MONTH, -1))+1;
          //  status = 6;
        }else if ("week".equals(type)){
           // status = 3;
            strYear =  DateUtil.year(DateUtil.beginOfWeek(date));
            endYear = DateUtil.year(DateUtil.endOfWeek(date));
            strMonth =  DateUtil.month( DateUtil.beginOfWeek(date)) + 1;
            endMonth =  DateUtil.month( DateUtil.endOfWeek(date)) + 1;
        }else if ("lastWeek".equals(type)){
           // status = 4;
            strYear =  DateUtil.year( DateUtil.lastWeek());
            endYear = DateUtil.year( DateUtil.offsetDay(DateUtil.lastWeek(), 7));
            strMonth =  DateUtil.month( DateUtil.lastWeek()) + 1;
            endMonth =  DateUtil.month(DateUtil.offsetDay(DateUtil.lastWeek(), 7)) + 1;
        }else if ("today".equals(type)){
           // status = 1;
            strYear =  DateUtil.year(date);
            endYear = DateUtil.year(date);
            strMonth =  DateUtil.month(date) + 1;
            endMonth =  DateUtil.month(date) + 1;

        }else if ("yesterday".equals(type)){
           // status = 2;
            strYear =  DateUtil.year( DateUtil.offsetDay(date, -1));
            endYear  =  DateUtil.year( DateUtil.offsetDay(date, -1));
            strMonth = DateUtil.month(DateUtil.offsetDay(date, -1)) +1;
            endMonth =  DateUtil.month(DateUtil.offsetDay(date, -1)) + 1;
        }
        if (strMonth < 10){
            startTime = strYear + "0" + strMonth;
        }else {
            startTime = strYear + "" + strMonth;
        }
        if (endMonth < 10){
            endTime = endYear + "0" + endMonth;
        }else {
            endTime = endYear + "" + endMonth;
        }
        record.set("startTime",startTime);
        record.set("endTime",endTime);
        return  record;
    }

    /**
     * 销售漏斗
     */
    public R queryBusiness(String userIds, String deptIds, Integer typeId, Date startTime, Date endTime) {
        Record record = Db.findFirst(Db.getSqlPara("crm.Instrument.queryBusiness",
                Kv.by("userIds", userIds).set("typeId", typeId)
                        .set("startTime", startTime).set("endTime", endTime).set("deptIds", deptIds)));
        List<Record> records = Db.find(Db.getSqlPara("crm.Instrument.queryBusinessStatistics",
                Kv.by("userIds", userIds).set("typeId", typeId)
                        .set("startTime", startTime).set("endTime", endTime).set("deptIds", deptIds)));
        if(record!=null){
            record.set("record", records);
        }
        return R.ok().put("data", record);
    }

    /**
     * 销售趋势（新）
     * type 1.今天 2.昨天 3.本周 4.上周 5.本月6.上月7.本季度8.上季度9.本年10上年11.自定义
     * userIds 员工id拼写id之间用‘,’隔开
     * deptIds 部门id
     */
    public R salesTrend(String type,String userIds,String startTime,String endTime){
        Record record = new Record();
        record.set("type",type).set("startTime",startTime).set("endTime",endTime);
        biTimeUtil.analyzeType(record);
        Integer cycleNum = record.getInt("cycleNum");
        String sqlDateFormat = record.getStr("sqlDateFormat");
        Integer beginTime = record.getInt("beginTime");
        StringBuffer sqlStringBuffer = new StringBuffer();
        for (int i=1; i <= cycleNum;i++){
            sqlStringBuffer.append("select '").append(beginTime).
                    append("' as type , IFNULL(SUM(money),0) as contractMoneys, (SELECT  IFNULL(SUM(money),0) FROM 72crm_crm_receivables WHERE DATE_FORMAT( return_time, '")
                    .append(sqlDateFormat).append("' ) = '").append(beginTime).append("' and check_status = 2 AND owner_user_id in ( ").append(userIds).append(" )) as receivablesMoneys ").
                    append("  FROM 72crm_crm_contract as ccco where DATE_FORMAT( ccco.order_date, '").append(sqlDateFormat).append("') = '").
                    append(beginTime).append("'  and ccco.check_status = 2 AND owner_user_id in (").append(userIds).append(")");
            if (i != cycleNum){
                sqlStringBuffer.append(" union all ");
            }
            beginTime = biTimeUtil.estimateTime(beginTime);
        }
        List<Record> recordList = Db.find(sqlStringBuffer.toString());
        Integer ststus = biTimeUtil.analyzeType(type);
        Record totlaContractMoney = Db.findFirst(Db.getSqlPara("crm.Instrument.queryContractMoeny",
                Kv.by("userIds",userIds).set("type",ststus).set("startTime",startTime).
                        set("endTime",endTime)));
        Record totlaReceivablesMoney = Db.findFirst(Db.getSqlPara("crm.Instrument.queryReceivablesMoeny",
                Kv.by("userIds",userIds).set("type",ststus).set("startTime",startTime).
                        set("endTime",endTime)));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",recordList);
        if (totlaContractMoney != null){
            jsonObject.put("totlaContractMoney",totlaContractMoney.getBigDecimal("money"));
        }else {
            jsonObject.put("totlaContractMoney",0);
        }
        if (totlaReceivablesMoney != null){
            jsonObject.put("totlaReceivablesMoney",totlaReceivablesMoney.getBigDecimal("money"));
        }else {
            jsonObject.put("totlaReceivablesMoney",0);
        }
        return R.ok().put("data",jsonObject);
    }

    public R sellFunnel(  String type,String userIds, String startTime, String endTime,Integer typeId){
        Record record = new Record();
        record.set("type",type).set("startTime",startTime).set("endTime",endTime);
        biTimeUtil.analyzeType(record);
        Integer ststus = biTimeUtil.analyzeType(type);
        List<Record> list = Db.find(Db.getSqlPara("bi.funnel.sellFunnel",
                Kv.by("userIds",userIds).set("type",ststus).set("startTime",startTime).
                        set("endTime",endTime).set("typeId",typeId)));
        Record sum_money = Db.findFirst(Db.getSqlPara("bi.funnel.sellFunnelSum",
                Kv.by("userIds",userIds).set("type",ststus).set("startTime",startTime).
                        set("endTime",endTime).set("typeId",typeId)));
        Record sum_shu = Db.findFirst(Db.getSqlPara("bi.funnel.sellFunnelSum",
                Kv.by("userIds",userIds).set("type",ststus).set("startTime",startTime).
                        set("endTime",endTime).set("typeId",typeId).set("isEnd",2)));
        Record sum_ying = Db.findFirst(Db.getSqlPara("bi.funnel.sellFunnelSum",
                Kv.by("userIds",userIds).set("type",ststus).set("startTime",startTime).
                        set("endTime",endTime).set("typeId",typeId).set("isEnd",1)));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",list);
        if (sum_money != null){
            jsonObject.put("sum_money",sum_money.getBigDecimal("money"));
        }else {
            jsonObject.put("sum_money",0);
        }
        if (sum_shu != null){
            jsonObject.put("sum_shu",sum_shu.getBigDecimal("money"));
        }else {
            jsonObject.put("sum_shu",0);
        }
        if (sum_ying != null){
            jsonObject.put("sum_ying",sum_ying.getBigDecimal("money"));
        }else {
            jsonObject.put("sum_ying",0);
        }



        return R.ok().put("data",jsonObject);
    }

}
