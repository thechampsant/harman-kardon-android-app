package mob.field.harmonkardonff.db;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.BrandCategoryModel;
import mob.field.harmonkardonff.entitiymodels.BrandModel;
import mob.field.harmonkardonff.entitiymodels.CompProductModel;
import mob.field.harmonkardonff.entitiymodels.LMTDSalesModel;
import mob.field.harmonkardonff.entitiymodels.MDAT;
import mob.field.harmonkardonff.entitiymodels.MTDSalesModel;
import mob.field.harmonkardonff.entitiymodels.MyInfoModel;
import mob.field.harmonkardonff.entitiymodels.NewSaleModel;
import mob.field.harmonkardonff.entitiymodels.OtherInfoModel;
import mob.field.harmonkardonff.entitiymodels.ProductModel;
import mob.field.harmonkardonff.entitiymodels.StocksModel;
import mob.field.harmonkardonff.entitiymodels.StoreImage;
import mob.field.harmonkardonff.entitiymodels.ViewDisplayModel;
import mob.field.harmonkardonff.quiz.QuizIDTimeDetailModel;
import android.util.Log;
import app.core.base.InnosolsActivity;
import app.core.entitymodels.ImageInfo;

import com.fieldforce.harmonkardonff.MainActivity;

/**
 * @author newINNOSOLS
 * 
 */
public class Database {
	InnosolsActivity Base;

	public Database(InnosolsActivity _main) {
		Base = _main;
	}

	public void LoadDbData() {
		LoadAttendancesFromDb();
		LoadSalesFromLocalDb();
		LoadStockFromLocalDb();
		LoadProductFromLocalDb();
		LoadFootfallFromDb();
		LoadBrandFromLocalDb();
		LoadBrandCategoryFromLocalDb();
//		showDataForQuizDb();
	}

	private void showDataForQuizDb() {
		Base.db.DeleteAll(new QuizIDTimeDetailModel());
			ArrayList<QuizIDTimeDetailModel> arrDetailModels = Base.db.FetchAllData(new QuizIDTimeDetailModel()).where("UserName", MainActivity.MyInfo.EmployeeCode);
			for (QuizIDTimeDetailModel quizIDTimeObj : arrDetailModels) {
				Base.ShowToast(quizIDTimeObj.QuizMinute + ":"
						+ quizIDTimeObj.quizSecond);
			}
			
	}

	public ArrayList<ImageInfo> LoadAllPendingImageFromDb() {
		ImageInfo dummy = new ImageInfo();
		ArrayList<ImageInfo> data = Base.db.FetchAllData(dummy);
		MainActivity.PendingImages = new ArrayList<ImageInfo>();
		MainActivity.PendingImages = data;
		return MainActivity.PendingImages;
	}

	public void DeleteCompProducts() {
		// TODO Auto-generated method stub
		for (CompProductModel pr : MainActivity.MyPLForMOP) {
			if (pr != null)
				pr.Delete();
		}
	}

	public void LoadMOPProductFromLocalDb() {
		// TODO Auto-generated method stub
		CompProductModel dummy = new CompProductModel();
		ArrayList<CompProductModel> data = Base.db.FetchAllData(dummy);
		MainActivity.MyPLForMOP = new ArrayList<CompProductModel>();
		MainActivity.MyPLForMOP = data;
	}

	public StoreImage getStoreImageGUIDForToday(String today) {
		ArrayList<StoreImage> data = Base.db.FetchAllData(new StoreImage())
				.where("ForDate", today)
				.where("UserID", MainActivity.MyInfo.UserID);
		if (data.Count() > 0)
			return data.First();
		else {
			StoreImage store = new StoreImage();
			store.ForDate = today;
			store.InsertOrUpdate();
			return store;
		}
	}

	public void ClearDataBase() {
		CleanAllSale();
		CleanAllDAT();
		CleanAllImages();
		CleanAllStock();
	}

	public void CleanAllImages() {
		for (ImageInfo img : MainActivity.PendingImages.where("UserName",
				MainActivity.MyInfo.EmployeeCode)) {
			img.Delete();
		}
	}

	public void CleanAllSale() {
		for (NewSaleModel sales : MainActivity.MySales.where("UserName",
				MainActivity.MyInfo.EmployeeCode)) {
			sales.Delete();
		}
	}

	public void CleanAllPendingSale() {
		for (NewSaleModel sales : MainActivity.MySales.where("UserName",
				MainActivity.MyInfo.EmployeeCode).where("IsUpdated", "false")) {
			sales.Delete();
		}
	}

	public void CleanAllSavedSale() {
		for (NewSaleModel sales : MainActivity.MySales.where("UserName",
				MainActivity.MyInfo.EmployeeCode).where("IsUpdated", "true")) {
			sales.Delete();
		}
	}

	public void CleanAllDAT() {
		for (MDAT dat : MainActivity.MyAttendances.where("UserName",
				MainActivity.MyInfo.EmployeeCode)) {
			dat.Delete();
		}
	}

	public void CleanAllSavedDAT() {
		// TODO Auto-generated method stub
		for (MDAT dat : MainActivity.MyAttendances.where("UserName",
				MainActivity.MyInfo.EmployeeCode).where("IsOfflineOnly",
				"false")) {
			dat.Delete();
		}
	}

	public void CleanAllPendingDAT() {
		for (MDAT dat : MainActivity.MyAttendances.where("UserName",
				MainActivity.MyInfo.EmployeeCode)
				.where("IsOfflineOnly", "true")) {
			dat.Delete();
		}
	}

	public ArrayList<MTDSalesModel> LoadMTDSalesFromDb() {
		MTDSalesModel dummy = new MTDSalesModel();
		ArrayList<MTDSalesModel> data = Base.db.FetchAllData(dummy).where(
				"UserName", MainActivity.MyInfo.EmployeeCode);
		return data;
	}

	public void SaveMTDSalesInDb(ArrayList<MTDSalesModel> data) {
		MTDSalesModel dummy = new MTDSalesModel();
		Base.db.DeleteAll(dummy);
		Base.db.InsertAll(data);
	}

	public ArrayList<LMTDSalesModel> LoadLMTDSalesFromDb() {
		ArrayList<LMTDSalesModel> data = Base.db.FetchAllData(
				new LMTDSalesModel()).where("UserName",
				MainActivity.MyInfo.EmployeeCode);
		return data;
	}

	public void SaveLMTDSalesInDb(ArrayList<LMTDSalesModel> data) {
		Base.db.DeleteAll(new LMTDSalesModel());
		Base.db.InsertAll(data);
	}

	public ArrayList<NewSaleModel> LoadSalesFromLocalDb() {
		NewSaleModel dummy = new NewSaleModel();
		ArrayList<NewSaleModel> data = Base.db.FetchAllData(dummy).where(
				"UserName", MainActivity.MyInfo.EmployeeCode);
		MainActivity.MySales = new ArrayList<NewSaleModel>();
		MainActivity.MySales = data;
		return data;
	}

	public ArrayList<StocksModel> LoadStockFromLocalDb() {

		ArrayList<StocksModel> data = Base.db.FetchAllData(new StocksModel())
				.where("UserName", MainActivity.MyInfo.EmployeeCode);
		MainActivity.MyStocks = new ArrayList<StocksModel>();
		MainActivity.MyStocks = data;
		return data;
	}

	public boolean LoadMyInfoFromLocalDb(String UserName) {
		ArrayList<MyInfoModel> data = new ArrayList<MyInfoModel>();
		if (UserName != null)
			data = Base.db.FetchAllData(new MyInfoModel()).where(
					"EmployeeCode", UserName);
		MainActivity.MyInfo = new MyInfoModel();
		if (data.Count() > 0)
			MainActivity.MyInfo = data.Last();

		return MainActivity.MyInfo != null;
	}

	public void LoadAttendancesFromDb() {

		MDAT dummy = new MDAT();
		ArrayList<MDAT> data = Base.db.FetchAllData(dummy).where("UserName",
				MainActivity.MyInfo.EmployeeCode);
		MainActivity.MyAttendances = new ArrayList<MDAT>();
		MainActivity.MyAttendances = data;
	}

	public void LoadProductFromLocalDb() {

		ProductModel dummy = new ProductModel();
		ArrayList<ProductModel> data = Base.db.FetchAllData(dummy);
		MainActivity.MyProductList = new ArrayList<ProductModel>();
		MainActivity.MyProductList = data;
	}
	public void LoadBrandCategoryFromLocalDb() {
		MainActivity.MyBrandCat.clear();
		BrandCategoryModel dummy = new BrandCategoryModel();
		ArrayList<BrandCategoryModel> data = Base.db.FetchAllData(dummy);
		MainActivity.MyBrandCat = new ArrayList<BrandCategoryModel>();
		MainActivity.MyBrandCat = data;
	}
	public void LoadBrandFromLocalDb() {

		MainActivity.MyBrand.clear();
		BrandModel dummy = new BrandModel();
		ArrayList<BrandModel> data = Base.db.FetchAllData(dummy);
		MainActivity.MyBrand = new ArrayList<BrandModel>();
		MainActivity.MyBrand = data;
	}
	public void LoadViewDisplayFromLocalDb() {

		MainActivity.displayModels.clear();
		ViewDisplayModel dummy = new ViewDisplayModel();
		ArrayList<ViewDisplayModel> data = Base.db.FetchAllData(dummy);
		MainActivity.displayModels = new ArrayList<ViewDisplayModel>();
		MainActivity.displayModels = data;
	}
	public ArrayList<OtherInfoModel> LoadFootfallFromDb() {

		ArrayList<OtherInfoModel> data = Base.db
				.FetchAllData(new OtherInfoModel());
		MainActivity.footfalls = new ArrayList<OtherInfoModel>();
		MainActivity.footfalls = data;
		return MainActivity.footfalls;
	}

	public ArrayList<OtherInfoModel> getFootfallFromDb() {

		if (MainActivity.footfalls.Count() < 1)
			return LoadFootfallFromDb();
		return MainActivity.footfalls;
	}

	/**
	 * @param myProductList
	 * 
	 */
	public boolean InsertUpdateDeleteProducts(
			ArrayList<ProductModel> myProductList) {

		try {

			for (ProductModel product : myProductList) {
				ProductModel oldPM = product.Single("Code", product.Code);
				if (oldPM != null) {
					product.guid = oldPM.guid;
				}
				if (product.IsBlocked.equalsIgnoreCase("false"))
					product.InsertOrUpdate();
				else {
					product.Delete();
				}
			}
			this.LoadProductFromLocalDb();
			return true;
		} catch (Exception ex) {
			Log.e("Error ", ex.toString());
			return false;
		}
	}

	/**
	 * 
	 */
	public void DeleteSaleProducts() {

		for (ProductModel pr : MainActivity.MyProductList) {
			if (pr != null)
				pr.Delete();
		}
	}

	public void SetSaleProductLastUpdatedOn() {
		Base.SavePreferences("PUpdatedOn",
				this.Base.GetCurrentDateTimeInString());
	}

	public void SetMOPProductLastUpdatedOn() {
		Base.SavePreferences("MOP_P_UpdatedOn",
				this.Base.GetCurrentDateTimeInString());
	}

	public void CleanAllSavedStock() {

		for (StocksModel stock : MainActivity.MyStocks.where("UserName",
				MainActivity.MyInfo.EmployeeCode).where("IsUpdated", "true")) {
			stock.Delete();
		}
	}

	public void CleanAllPendingStock() {
		for (StocksModel stock : MainActivity.MyStocks.where("UserName",
				MainActivity.MyInfo.EmployeeCode).where("IsUpdated", "false")) {
			stock.Delete();
		}

	}

	public void CleanAllStock() {
		for (StocksModel stock : MainActivity.MyStocks.where("UserName",
				MainActivity.MyInfo.EmployeeCode)) {
			stock.Delete();
		}

	}

}
