package com.hyhscm.myron.eapp.data;

import java.io.Serializable;


/** 
*
* @author jack chen 
* 
*/
public class Areas  implements Serializable{

		private int id;
		private int levels;
		private String name;
		private int pid;
		
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
		
		public int getLevels() {
			return levels;
		}

		public void setLevels(int levels) {
			this.levels = levels;
		}
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public int getPid() {
			return pid;
		}

		public void setPid(int pid) {
			this.pid = pid;
		}
		
}
