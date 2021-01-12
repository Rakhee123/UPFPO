package com.brightleaf.exportservice.model;

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
			String baseFolderLocation = System.getenv("AE_HOME");
			return baseFolderLocation;
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
