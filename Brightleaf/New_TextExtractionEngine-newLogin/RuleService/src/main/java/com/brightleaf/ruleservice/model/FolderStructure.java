package com.brightleaf.ruleservice.model;

public enum FolderStructure {

	AE_HOME {
		@Override
		public String getLocation() {
			return System.getProperty("ae.home");
//			return "C:\\AE_HOME";
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
