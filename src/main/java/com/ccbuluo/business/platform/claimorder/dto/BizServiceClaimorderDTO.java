
package com.ccbuluo.business.platform.claimorder.dto;

import java.util.Date;

/**
 * @author zhangkangjian
 * @date 2018-09-08 11:04:42
 */
public class BizServiceClaimorderDTO {


    private  Long id;

    //维修单的编号
    private  String claimOrdno;

    //维修单的编号
    private  String serviceOrdno;

    //将三包件发给平台时的物流单号
    private  String trackingNo;

    //将三包件推给平台的地址
    private  String refundAdress;

    //索赔单的状态
    private  String docStatus;

    //索赔机构的编号
    private  String claimOrgno;

    //索赔机构的名称
    private  String claimOrgname;

    //索赔的金额
    private  double claimAmount;

    //实际赔偿的金额
    private  double actualAmount;

    //赔偿时间
    private Date repayTime;

    //创建人
    private  String creator;

    //创建时间
    private  Long createTime;

    //更新人
    private  String operator;

    //更新时间
    private  Long operateTime;

    //删除标识
    private  int deleteFlag;

    //备注
    private  String remark;


    public  Long  getId(){
        return  this.id;
    };
    public  void  setId(Long id){
        this.id=id;
    }

    public  String  getClaimOrdno(){
        return  this.claimOrdno;
    };
    public  void  setClaimOrdno(String claimOrdno){
        this.claimOrdno=claimOrdno;
    }

    public  String  getServiceOrdno(){
        return  this.serviceOrdno;
    };
    public  void  setServiceOrdno(String serviceOrdno){
        this.serviceOrdno=serviceOrdno;
    }

    public  String  getTrackingNo(){
        return  this.trackingNo;
    };
    public  void  setTrackingNo(String trackingNo){
        this.trackingNo=trackingNo;
    }

    public  String  getRefundAdress(){
        return  this.refundAdress;
    };
    public  void  setRefundAdress(String refundAdress){
        this.refundAdress=refundAdress;
    }

    public  String  getDocStatus(){
        return  this.docStatus;
    };
    public  void  setDocStatus(String docStatus){
        this.docStatus=docStatus;
    }

    public  String  getClaimOrgno(){
        return  this.claimOrgno;
    };
    public  void  setClaimOrgno(String claimOrgno){
        this.claimOrgno=claimOrgno;
    }

    public  String  getClaimOrgname(){
        return  this.claimOrgname;
    };
    public  void  setClaimOrgname(String claimOrgname){
        this.claimOrgname=claimOrgname;
    }

    public  double  getClaimAmount(){
        return  this.claimAmount;
    };
    public  void  setClaimAmount(double claimAmount){
        this.claimAmount=claimAmount;
    }

    public  double  getActualAmount(){
        return  this.actualAmount;
    };
    public  void  setActualAmount(double actualAmount){
        this.actualAmount=actualAmount;
    }

    public  Date  getRepayTime(){
        return  this.repayTime;
    };
    public  void  setRepayTime(Date repayTime){
        this.repayTime=repayTime;
    }

    public  String  getCreator(){
        return  this.creator;
    };
    public  void  setCreator(String creator){
        this.creator=creator;
    }

    public  Long  getCreateTime(){
        return  this.createTime;
    };
    public  void  setCreateTime(Long createTime){
        this.createTime=createTime;
    }

    public  String  getOperator(){
        return  this.operator;
    };
    public  void  setOperator(String operator){
        this.operator=operator;
    }

    public  Long  getOperateTime(){
        return  this.operateTime;
    };
    public  void  setOperateTime(Long operateTime){
        this.operateTime=operateTime;
    }

    public  int  getDeleteFlag(){
        return  this.deleteFlag;
    };
    public  void  setDeleteFlag(int deleteFlag){
        this.deleteFlag=deleteFlag;
    }

    public  String  getRemark(){
        return  this.remark;
    };
    public  void  setRemark(String remark){
        this.remark=remark;
    }


}