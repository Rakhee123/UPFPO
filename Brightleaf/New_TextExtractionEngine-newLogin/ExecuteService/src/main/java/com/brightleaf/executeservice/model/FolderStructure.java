package com.brightleaf.executeservice.model;

public enum FolderStructure {

	AE_HOME {
		@Override
		public String getLocation() {
//			return "C:\\AE_HOME";
			return System.getProperty("ae.home");
		}
	},
	BASE_LOCATION {
		@Override
		public String getLocation() {
			return System.getenv("AE_HOME");
		}
	},
	SOURCE_LOCATION {
		@Override
		public String getLocation() {
			return "/Temp_Location/";
		}
	};
	public abstract String getLocation();
}
