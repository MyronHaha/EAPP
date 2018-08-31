package com.hyhscm.myron.eapp.data;

import android.support.annotation.NonNull;

import java.io.Serializable;


/** 
*
* @author jack chen 
* 
*/
public class Labelinfo implements Serializable  {

		private int id;
		private String name;
		private String text;
		private int value;
		private int pid;
		private int type;
		private int sort;
		private boolean isSelected;
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
		
		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
		
		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
		
		public int getPid() {
			return pid;
		}

		public void setPid(int pid) {
			this.pid = pid;
		}
		
		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}
		
		public int getSort() {
			return sort;
		}

		public void setSort(int sort) {
			this.sort = sort;
		}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}
}
