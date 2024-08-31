package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;


public class BrandingImageModel extends DataEntity<BrandingImageModel> {

		static String TableName="BrandingImageModel";
		
		public String brandingName;
		public String YesSelected = "false";
		public String imagePresent = "false";
		public String imageUploaded= "false";
		public String imagePath= "";
		public String docId = "";
		
		
		public BrandingImageModel() {
			super(TableName);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void RegisterMappings() {
		}
		@Override
		public BrandingImageModel GetNewObject() {
			// TODO Auto-generated method stub
			return new BrandingImageModel();
		}


	}
