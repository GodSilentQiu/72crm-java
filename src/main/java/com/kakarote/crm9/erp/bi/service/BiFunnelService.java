package com.kakarote.crm9.erp.bi.service;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.erp.bi.common.BiTimeUtil;
import com.kakarote.crm9.utils.R;

import java.util.List;

public class BiFunnelService {
    @Inject
    BiTimeUtil biTimeUtil;


    public R sellFunnel(Integer deptId, Long userId, String type, String startTime, String endTime,Integer typeId){
        Record record = new Record();
        record.set("deptId",deptId).set("userId",userId).set("type",type).set("startTime",startTime).set("endTime",endTime);
        biTimeUtil.analyzeType(record);
        String userIds = record.getStr("userIds");
        Integer ststus = biTimeUtil.analyzeType(type);
        List<Record> list = Db.find(Db.getSqlPara("bi.funnel.sellFunnel",
                Kv.by("userIds",userIds).set("type",ststus).set("startTime",startTime).
                        set("endTime",endTime).set("typeId",typeId)));
        return R.ok().put("data",list);
    }
    /**
     * 新增商机分析
     * @author zxy
     */
    public R addBusinessAnalyze(Integer deptId,Long userId,String type,String startTime,String endTime){
        Record record = new Record();
        record.set("deptId",deptId).set("userId",userId).set("type",type).set("startTime",startTime).set("endTime",endTime);
        biTimeUtil.analyzeType(record);
        Integer cycleNum = record.getInt("cycleNum");
        String sqlDateFormat = record.getStr("sqlDateFormat");
        String userIds = record.getStr("userIds");
        Integer beginTime = record.getInt("beginTime");
        StringBuffer sqlStringBuffer = new StringBuffer();
        for (int i=1; i <= cycleNum;i++){
            sqlStringBuffer.append("select '").append(beginTime).append("' as type,IFNULL((select count(1) from businessview where DATE_FORMAT(create_time,'")
                    .append(sqlDateFormat).append("') = '").append(beginTime).append("' and owner_user_id in (").append(userIds)
                    .append(")),0) as businessNum,IFNULL(sum(money),0) as businessMoney from 72crm_crm_business  where DATE_FORMAT(create_time,'")
                    .append(sqlDateFormat).append("') = '").append(beginTime).append("' and owner_user_id in (").append(userIds).append(")");
            if (i != cycleNum){
                sqlStringBuffer.append(" union all ");
            }
            beginTime = biTimeUtil.estimateTime(beginTime);
        }
        List<Record> recordList = Db.find(sqlStringBuffer.toString());
        return R.ok().put("data",recordList);
    }
    public R sellFunnelList(Integer deptId, Long userId, String type, String startTime, String endTime){
        Record record = new Record();
        record.set("deptId",deptId).set("userId",userId).set("type",type).set("startTime",startTime).set("endTime",endTime);
        biTimeUtil.analyzeType(record);
        String userIds = record.getStr("userIds");
        Integer ststus = biTimeUtil.analyzeType(type);
        List<Record> list = Db.find(Db.getSqlPara("bi.funnel.sellFunnelList",
                Kv.by("userIds",userIds).set("type",ststus).set("startTime",startTime).
                        set("endTime",endTime)));
        return R.ok().put("data",list);
    }
    /**
     * 商机转化率分析
     * @author zxy
     */
    public R win(Integer deptId,Long userId,String type,String startTime,String endTime){
        Record record = new Record();
        record.set("deptId",deptId).set("userId",userId).set("type",type).set("startTime",startTime).set("endTime",endTime);
        biTimeUtil.analyzeType(record);
        Integer cycleNum = record.getInt("cycleNum");
        String sqlDateFormat = record.getStr("sqlDateFormat");
        String userIds = record.getStr("userIds");
        Integer beginTime = record.getInt("beginTime");
        StringBuffer sqlStringBuffer = new StringBuffer();
        for (int i=1; i <= cycleNum;i++){
            sqlStringBuffer.append("select '").append(beginTime).append("' as type,IFNULL((select count(1) from businessview where DATE_FORMAT(create_time,'")
                    .append(sqlDateFormat).append("') = '").append(beginTime).append("'and is_end = 1 and owner_user_id in (").append(userIds)
                    .append(")),0) as businessEnd,COUNT(1) as businessNum,").append(" IFNULL((select count(1) from businessview where DATE_FORMAT(create_time,'")
                    .append(sqlDateFormat).append("') = '").append(beginTime).append("'and is_end = 1 and owner_user_id in (").append(userIds).
                    append(")) / COUNT(1),0 )").append(" as proportion ").
                    append(" from 72crm_crm_business  where DATE_FORMAT(create_time,'")
                    .append(sqlDateFormat).append("') = '").append(beginTime).append("' and owner_user_id in (").append(userIds).append(")");
            if (i != cycleNum){
                sqlStringBuffer.append(" union all ");
            }
            beginTime = biTimeUtil.estimateTime(beginTime);
        }
        List<Record> recordList = Db.find(sqlStringBuffer.toString());
        return R.ok().put("data",recordList);
    }
}
