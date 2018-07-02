package com.ccbuluo.business.platform.carparts.dao;

import io.swagger.annotations.ApiModelProperty;

/**
 * ${file_name}
 *
 * @author wuyibo
 * @date 2018-07-02 14:33:19
 */
public class BasicCarpartsProduct {

    /**
     * 配件基本信息id，引用配件基本信息表(basic_carparts_product)的id字段
     */
    @ApiModelProperty(name = "id", value = "配件基本信息id，引用配件基本信息表(basic_carparts_product)的id字段", hidden = true)
    private  long id;

    //编号
    @ApiModelProperty(name = "", value = "")
    private  String carpartsCode;

    //名称
    @ApiModelProperty(name = "", value = "")
    private  String carpartsName;

    //配件直接关联的分类的code
    @ApiModelProperty(name = "", value = "")
    private  String categoryCode;

    //关联分类的父子分类code路径，逗号隔开
    @ApiModelProperty(name = "", value = "")
    private  String categoryCodePath;

    //适配车型的id或code，目前先空着
    @ApiModelProperty(name = "", value = "")
    private  String fitCarmodel;

    //创建人
    @ApiModelProperty(name = "", value = "")
    private  String creator;

    //创建时间
    @ApiModelProperty(name = "", value = "")
    private  long createTime;

    //更新人
    @ApiModelProperty(name = "", value = "")
    private  String operator;

    //更新时间
    @ApiModelProperty(name = "", value = "")
    private  long operateTime;

    //删除标识
    @ApiModelProperty(name = "", value = "")
    private  int deleteFlag;


    public  long  getId(){
        return  this.id;
    };
    public  void  setId(long id){
        this.id=id;
    }

    public  String  getCarpartsCode(){
        return  this.carpartsCode;
    };
    public  void  setCarpartsCode(String carpartsCode){
        this.carpartsCode=carpartsCode;
    }

    public  String  getCarpartsName(){
        return  this.carpartsName;
    };
    public  void  setCarpartsName(String carpartsName){
        this.carpartsName=carpartsName;
    }

    public  String  getCategoryCode(){
        return  this.categoryCode;
    };
    public  void  setCategoryCode(String categoryCode){
        this.categoryCode=categoryCode;
    }

    public  String  getCategoryCodePath(){
        return  this.categoryCodePath;
    };
    public  void  setCategoryCodePath(String categoryCodePath){
        this.categoryCodePath=categoryCodePath;
    }

    public  String  getFitCarmodel(){
        return  this.fitCarmodel;
    };
    public  void  setFitCarmodel(String fitCarmodel){
        this.fitCarmodel=fitCarmodel;
    }

    public  String  getCreator(){
        return  this.creator;
    };
    public  void  setCreator(String creator){
        this.creator=creator;
    }

    public  long  getCreateTime(){
        return  this.createTime;
    };
    public  void  setCreateTime(long createTime){
        this.createTime=createTime;
    }

    public  String  getOperator(){
        return  this.operator;
    };
    public  void  setOperator(String operator){
        this.operator=operator;
    }

    public  long  getOperateTime(){
        return  this.operateTime;
    };
    public  void  setOperateTime(long operateTime){
        this.operateTime=operateTime;
    }

    public  int  getDeleteFlag(){
        return  this.deleteFlag;
    };
    public  void  setDeleteFlag(int deleteFlag){
        this.deleteFlag=deleteFlag;
    }
}
