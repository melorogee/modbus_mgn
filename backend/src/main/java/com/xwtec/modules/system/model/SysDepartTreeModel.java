package com.xwtec.modules.system.model;

import com.xwtec.modules.system.entity.SysDepart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 部门表 存储树结构数据的实体类
 * <p>
 * 
 * @author Steve
 * @Since 2019-01-22 
 */
public class SysDepartTreeModel implements Serializable{
	
    private static final long serialVersionUID = 1L;
    
    /** 对应SysDepart中的id字段,前端数据树中的key*/
    private String key;

    /** 对应SysDepart中的id字段,前端数据树中的value*/
    private String value;

    /** 对应depart_name字段,前端数据树中的title*/
    private String title;
    

    // 以下所有字段均与SysDepart相同
    
    private String id;

    private String parentId;

    private String departName;

    private Integer departOrder;

    private String orgType;

    private String orgLevel;

    private String orgCode;

    private String status;

    private String delFlag;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String remark;

    private List<SysDepartTreeModel> children = new ArrayList<>();


    /**
     * 将SysDepart对象转换成SysDepartTreeModel对象
     * @param sysDepart
     */
	public SysDepartTreeModel(SysDepart sysDepart) {
		this.key = sysDepart.getId();
        this.value = sysDepart.getId();
        this.title = sysDepart.getDepartName();
        this.id = sysDepart.getId();
        this.parentId = sysDepart.getParentId();
        this.departName = sysDepart.getDepartName();
        this.departOrder = sysDepart.getDepartOrder();
        this.orgType = sysDepart.getOrgType();
        this.orgLevel = sysDepart.getOrgLevel();
        this.orgCode = sysDepart.getOrgCode();
        this.status = sysDepart.getStatus();
        this.delFlag = sysDepart.getDelFlag();
        this.createBy = sysDepart.getCreateBy();
        this.createTime = sysDepart.getCreateTime();
        this.updateBy = sysDepart.getUpdateBy();
        this.updateTime = sysDepart.getUpdateTime();
        this.remark = sysDepart.getRemark();
    }
    
    
    public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SysDepartTreeModel> getChildren() {
        return children;
    }

    public void setChildren(List<SysDepartTreeModel> children) {
        this.children = children;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }



    public Integer getDepartOrder() {
        return departOrder;
    }

    public void setDepartOrder(Integer departOrder) {
        this.departOrder = departOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public SysDepartTreeModel() { }

    /**
     * 重写equals方法
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysDepartTreeModel model = (SysDepartTreeModel) o;
        return Objects.equals(id, model.id) &&
                Objects.equals(parentId, model.parentId) &&
                Objects.equals(departName, model.departName) &&
                Objects.equals(departOrder, model.departOrder) &&
                Objects.equals(orgType, model.orgType) &&
                Objects.equals(orgLevel, model.orgLevel) &&
                Objects.equals(orgCode, model.orgCode) &&
                Objects.equals(status, model.status) &&
                Objects.equals(delFlag, model.delFlag) &&
                Objects.equals(createBy, model.createBy) &&
                Objects.equals(createTime, model.createTime) &&
                Objects.equals(updateBy, model.updateBy) &&
                Objects.equals(updateTime, model.updateTime) &&
                Objects.equals(children, model.children)&&
                Objects.equals(remark,model.remark);
    }
    
    /**
     * 重写hashCode方法
     */
    @Override
    public int hashCode() {

        return Objects.hash(id, parentId, departName,
        		departOrder, orgType,orgLevel, orgCode,status,
                delFlag, createBy, createTime, updateBy, updateTime, remark,
        		children);
    }

}
