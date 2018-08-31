package com.hyhscm.myron.eapp.data;

import java.io.Serializable;


/** 
*
* @author jack chen 
* 
*/
public class User  implements Serializable{

		private int id;
		private String name;
		private String numbers;
		private String phone;
		private String password;
		private String mail;
		private String address;
		private int enable;
		private int state;
		private String pushkey;
		private String img;
		private java.util.Date creationTime;
		private int creatorId;
		private int updateId;
		private java.util.Date updateTime;
		private int type;
		private String aid;
		private String uiid;
		private String userName;
		private String areas;
		private String companyName;
		private int status;
		private String qrcode;
		private String pic;
		private String sign;
		private int vip;
		private int vipl;
		
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
		
		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}
		
		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
		
		public String getMail() {
			return mail;
		}

		public void setMail(String mail) {
			this.mail = mail;
		}
		
		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}
		
		public int getEnable() {
			return enable;
		}

		public void setEnable(int enable) {
			this.enable = enable;
		}
		
		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}
		
		public String getPushkey() {
			return pushkey;
		}

		public void setPushkey(String pushkey) {
			this.pushkey = pushkey;
		}
		
		public String getImg() {
			return img;
		}

		public void setImg(String img) {
			this.img = img;
			this.pic=img;
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
		
		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}
		
		public String getAid() {
			return aid;
		}

		public void setAid(String aid) {
			this.aid = aid;
		}
		
		public String getUiid() {
			return uiid;
		}

		public void setUiid(String uiid) {
			this.uiid = uiid;
		}
		
		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}
		
		public String getAreas() {
			return areas;
		}

		public void setAreas(String areas) {
			this.areas = areas;
		}
		
		public String getCompanyName() {
			return companyName;
		}

		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}
		
		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}
		
		public String getQrcode() {
			return qrcode;
		}

		public void setQrcode(String qrcode) {
			this.qrcode = qrcode;
		}
		
		public String getPic() {
			pic=img;
			return pic;
		}

		public void setPic(String pic) {
			this.pic = pic;
			this.pic=this.img;
		}
		
		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}
		
		public int getVip() {
			return vip;
		}

		public void setVip(int vip) {
			this.vip = vip;
		}
		
		public int getVipl() {
			return vipl;
		}

		public void setVipl(int vipl) {
			this.vipl = vipl;
		}
		
}
