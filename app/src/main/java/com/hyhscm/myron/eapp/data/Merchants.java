package com.hyhscm.myron.eapp.data;

import java.io.Serializable;


/** 
*
* @author jack chen 
* 展厅详情
*/
public class Merchants implements Serializable {

		private int id;
		private String name;
		private String numbers;
		private String img;
		private String content;
		private String remark;
		private int hit;
		private int userId;
		private String userName;
		private int companyId;
		private String companyName;
		private int type;
		private int state;
		private String address;
		private java.util.Date creationTime;
		private int creatorId;
		private int updateId;
		private java.util.Date updateTime;
		private int isCommend;
		private int sort;
		private String labels;
		private String linkmanTel;
		private String linkman;
		private int pnum;
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public String getNumbers() {
			return numbers;
		}

		public void setNumbers(String numbers) {
			this.numbers = numbers;
		}
		
		public String getImg() {
			return img;
		}

		public void setImg(String img) {
			this.img = img;
		}
		
		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
		
		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}
		
		public int getHit() {
			return hit;
		}

		public void setHit(int hit) {
			this.hit = hit;
		}
		
		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}
		
		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}
		
		public int getCompanyId() {
			return companyId;
		}

		public void setCompanyId(int companyId) {
			this.companyId = companyId;
		}
		
		public String getCompanyName() {
			return companyName;
		}

		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}
		
		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}
		
		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}
		
		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}
		
		public java.util.Date getCreationTime() {
			return creationTime;
		}

		public void setCreationTime(java.util.Date creationTime) {
			this.creationTime = creationTime;
		}
		
		public int getCreatorId() {
			return creatorId;
		}

		public void setCreatorId(int creatorId) {
			this.creatorId = creatorId;
		}
		
		public int getUpdateId() {
			return updateId;
		}

		public void setUpdateId(int updateId) {
			this.updateId = updateId;
		}
		
		public java.util.Date getUpdateTime() {
			return updateTime;
		}

		public void setUpdateTime(java.util.Date updateTime) {
			this.updateTime = updateTime;
		}
		
		public int getIsCommend() {
			return isCommend;
		}

		public void setIsCommend(int isCommend) {
			this.isCommend = isCommend;
		}
		
		public int getSort() {
			return sort;
		}

		public void setSort(int sort) {
			this.sort = sort;
		}
		
		public String getLabels() {
			return labels;
		}

		public void setLabels(String labels) {
			this.labels = labels;
		}
		
		public String getLinkmanTel() {
			return linkmanTel;
		}

		public void setLinkmanTel(String linkmanTel) {
			this.linkmanTel = linkmanTel;
		}
		
		public String getLinkman() {
			return linkman;
		}

		public void setLinkman(String linkman) {
			this.linkman = linkman;
		}

		public int getPnum() {
			return pnum;
		}

		public void setPnum(int pnum) {
			this.pnum = pnum;
		}
		
}
