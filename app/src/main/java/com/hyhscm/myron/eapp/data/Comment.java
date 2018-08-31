package com.hyhscm.myron.eapp.data;

import java.io.Serializable;
import java.math.BigDecimal;

/** 
*
* @author jack chen 
* 
*/
public class Comment  {

		private int id;
		private String content;
		private int type;
		private int userId;
		private String userName;
		private String img;
		private int infoId;
		private int sourceId;
		private String sourceName;
		private int sourceType;
		private java.util.Date creationTime;
		private int creatorId;
		private int updateId;
		private java.util.Date updateTime;
		private BigDecimal credit;
		private int state;
		private String cname;
		private String uimg;
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
		
		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
		
		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
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
		
		public String getImg() {
			return img;
		}

		public void setImg(String img) {
			this.img = img;
		}
		
		public int getInfoId() {
			return infoId;
		}

		public void setInfoId(int infoId) {
			this.infoId = infoId;
		}
		
		public int getSourceId() {
			return sourceId;
		}

		public void setSourceId(int sourceId) {
			this.sourceId = sourceId;
		}
		
		public String getSourceName() {
			return sourceName;
		}

		public void setSourceName(String sourceName) {
			this.sourceName = sourceName;
		}
		
		public int getSourceType() {
			return sourceType;
		}

		public void setSourceType(int sourceType) {
			this.sourceType = sourceType;
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
		
		public BigDecimal getCredit() {
			return credit;
		}

		public void setCredit(BigDecimal credit) {
			this.credit = credit;
		}
		
		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}

		public String getCname() {
			return cname;
		}

		public void setCname(String cname) {
			this.cname = cname;
		}

		public String getUimg() {
			return uimg;
		}

		public void setUimg(String uimg) {
			this.uimg = uimg;
		}
		
}
