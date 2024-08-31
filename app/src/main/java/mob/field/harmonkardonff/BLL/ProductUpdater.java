package mob.field.harmonkardonff.BLL;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.AppConfigModel;
import mob.field.harmonkardonff.entitiymodels.BrandCategoryModel;
import mob.field.harmonkardonff.entitiymodels.BrandModel;
import mob.field.harmonkardonff.entitiymodels.ProductModel;
import mob.field.harmonkardonff.entitiymodels.ViewDisplayModel;
import mob.field.harmonkardonff.services.WebService;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.InnosolsActivity;
import app.core.model.Response;
import app.core.utils.Dialog;

import com.fieldforce.harmonkardonff.MainActivity;

public class ProductUpdater {

	public InnosolsActivity BaseActivity;
	WebService server = new WebService();

	public ProductUpdater() {
		this.BaseActivity = MainActivity.Current;
	}

	public ProductUpdater(InnosolsActivity a) {
		BaseActivity = a;
	}

	public void UpdateProducts() {
		if (BaseActivity.isNetworkFoundDialog())
			TryUpdateProducts();

	}
	public void checkIfConfigUpdateRequire()
	{
		if (BaseActivity.isNetworkAvailable())
			tryConfigUpdateForProduct();
	}
	
	public void tryConfigUpdateForProduct() {
		BackgroundProcess bp = new BackgroundProcess(BaseActivity, false);
		bp.setbackgroundProcess(new IProcess() {

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void processResponse(Object arg0) throws Exception {
				// TODO Auto-generated method stub
				Response response = (Response) arg0;
				ArrayList<AppConfigModel> arrAppConfigModel = MainActivity.Current.db
						.FetchAllData(new AppConfigModel());
				AppConfigModel appConfig = arrAppConfigModel.get(0);
				if(appConfig.AppConfigVersion==null||appConfig.AppConfigVersion=="0.0"||appConfig.AppConfigVersion=="0")
					return;
				if (MainActivity.Current.ToDouble(appConfig.AppConfigVersion) < MainActivity.Current
						.ToDouble(response.AppVersion)) {
					TryUpdateProducts();
				}

			}

			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				return server.getLatestProductConfigVer();
			}
		});
		bp.execute(null, null, null);

	}

	private void TryUpdateProducts() {
		BackgroundProcess bp = new BackgroundProcess(BaseActivity)
				.setProgressMessage("loading products from server..");
		bp.setbackgroundProcess(new IProcess() {

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void processResponse(Object arg0) throws Exception {
				// TODO Auto-generated method stub
				Response response = (Response) arg0;
				if (response.status.equalsIgnoreCase("false")) {
					new Dialog(BaseActivity).setTitle("Error").show(
							response.errormsg);
					BaseActivity.ShowToast("Unable to load products");
				} else {

					ArrayList<ProductModel> products = response.data;
					AppConfigModel appConfigModel = new AppConfigModel();
					appConfigModel.AppConfigVersion = response.AppVersion;
					MainActivity.Current.db.DeleteAll(new AppConfigModel());
					appConfigModel.InsertOrUpdate();
					
					if (products == null)
						new Dialog(BaseActivity).setTitle("Message").show(
								"No products found!");
					else if (products.Count() > 0) {
						TryAddProducts(products);
					} else
						new Dialog(BaseActivity).setTitle("Message").show(
								"No products found!");
				}
			}

			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				return server.TryUpdateProducts();
			}
		});
		bp.execute(null, null, null);
	}

	boolean IsAdded = false;

	private void TryAddProducts(final ArrayList<ProductModel> products) {
		IsAdded = false;
		BackgroundProcess bp = new BackgroundProcess(BaseActivity)
				.setProgressMessage("adding products to phone..");
				bp.setbackgroundProcess(new IProcess() {

			@Override
			public void processResponse(Object arg0) throws Exception {
				// TODO Auto-generated method stub
				if (IsAdded)
					BaseActivity.ShowToast("Product updated sucessfully!");
			}

			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				UpdateProductInDataBase(products);
				IsAdded = true;
				return null;
			}
		});
		bp.execute(null, null, null);
	}

	private void UpdateProductInDataBase(ArrayList<ProductModel> products) {
		MainActivity.Current.db.DeleteAll(new ProductModel());
		MainActivity.Current.db.InsertAll(products);
		MainActivity.Database.LoadProductFromLocalDb();
	}

	public void UpdateBrandMapping() {
		if (BaseActivity.isNetworkFoundDialog()) {
			updateBrandMapping();
			updateBrandCategory();
		}

	}

	private void updateBrandCategory() {
		BackgroundProcess bp = new BackgroundProcess(BaseActivity)
				.setProgressMessage("loading brand category from server..");
		bp.setbackgroundProcess(new IProcess() {

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void processResponse(Object arg0) throws Exception {
				// TODO Auto-generated method stub
				Response response = (Response) arg0;
				if (response.status.equalsIgnoreCase("false")) {
					new Dialog(BaseActivity).setTitle("Error").show(
							response.errormsg);
					BaseActivity.ShowToast("Unable to load Brand Category");
				} else {
					ArrayList<BrandCategoryModel> products = response.data;
					if (products == null)
						new Dialog(BaseActivity).setTitle("Message").show(
								"No Brand Category found!");
					else if (products.Count() > 0) {
						TryAddBrandCategory(products);
					} else
						new Dialog(BaseActivity).setTitle("Message").show(
								"No Brand Category found!");
				}
			}

			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				return server.TryUpdateBrandCategory();
			}
		});
		bp.execute(null, null, null);

	}

	boolean isBrandCatAdded = false;

	private void TryAddBrandCategory(
	final ArrayList<BrandCategoryModel> products) {
		IsAdded = false;
		BackgroundProcess bp = new BackgroundProcess(BaseActivity)
				.setProgressMessage("adding brand category to phone..");
		bp.setbackgroundProcess(new IProcess() {

			@Override
			public void processResponse(Object arg0) throws Exception {
				// TODO Auto-generated method stub
				if (isBrandCatAdded)
					BaseActivity
							.ShowToast("Brand Category updated sucessfully!");
			}

			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				UpdateBrandCatInDataBase(products);
				isBrandCatAdded = true;
				return null;
			}
		});
		bp.execute(null, null, null);
	}

	private void UpdateBrandCatInDataBase(ArrayList<BrandCategoryModel> products) {
		MainActivity.Current.db.DeleteAll(new BrandCategoryModel());
		MainActivity.Current.db.InsertAll(products);
		MainActivity.Database.LoadBrandCategoryFromLocalDb();
	}

	private void updateBrandMapping() {
		BackgroundProcess bp = new BackgroundProcess(BaseActivity)
				.setProgressMessage("loading brand mapping from server..");
		bp.setbackgroundProcess(new IProcess() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void processResponse(Object arg0) throws Exception {
				// TODO Auto-generated method stub
				Response response = (Response) arg0;
				if (response.status.equalsIgnoreCase("false")) {
					new Dialog(BaseActivity).setTitle("Error").show(
							response.errormsg);
					BaseActivity.ShowToast("Unable to load brand mapping");
				} else {
					ArrayList<BrandModel> products = response.data;
					if (products == null)
						new Dialog(BaseActivity).setTitle("Message").show(
								"No Brand Mapping Found!");
					else if (products.Count() > 0) {
						TryAddBrand(products);
					} else
						new Dialog(BaseActivity).setTitle("Message").show(
								"No Brand Mapping Found!");
				}
			}

			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				return server.TryUpdateBrand();
			}
		});
		bp.execute(null, null, null);
	}

	boolean isBrandAdded = false;

	private void TryAddBrand(final ArrayList<BrandModel> products) {
		IsAdded = false;
		BackgroundProcess bp = new BackgroundProcess(BaseActivity)
				.setProgressMessage("adding brand mapping to phone..");
		bp.setbackgroundProcess(new IProcess() {

			@Override
			public void processResponse(Object arg0) throws Exception {
				// TODO Auto-generated method stub
				if (isBrandAdded)
					BaseActivity
							.ShowToast("Brand mapping updated sucessfully!");
			}

			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				UpdateBrandInDataBase(products);
				isBrandAdded = true;
				return null;
			}
		});
		bp.execute(null, null, null);
	}

	private void UpdateBrandInDataBase(ArrayList<BrandModel> products) {
		MainActivity.Current.db.DeleteAll(new BrandModel());
		MainActivity.Current.db.InsertAll(products);
		MainActivity.Database.LoadBrandFromLocalDb();
	}

	/*
	 * private JSONArray jsonArray; public void updateBrandMapping(){ String
	 * apiUrl = server.ApiUrl; // String apiUrl =
	 * "http://192.168.200.174:4422/IspMobile/GetBrandList"; apiUrl +=
	 * "GetBrandList"; SendRegisterId refresh = new
	 * SendRegisterId(BaseActivity,apiUrl); refresh.setResponse(new
	 * AsynResponse() {
	 * 
	 * @Override public void response(JSONObject result) { if(result!=null){ try
	 * { jsonArray = result.getJSONArray("data"); } catch (JSONException e) {
	 * e.printStackTrace(); } if(jsonArray.length()!=0){
	 * loadProductsFromServer(jsonArray); } } } }); refresh.execute(); }
	 */
	private void updateViewDisplayMapping() {
		BackgroundProcess bp = new BackgroundProcess(BaseActivity)
				.setProgressMessage("loading Display from server..");
		bp.setbackgroundProcess(new IProcess() {

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void processResponse(Object arg0) throws Exception {
				// TODO Auto-generated method stub
				Response response = (Response) arg0;
				if (response.status.equalsIgnoreCase("false")) {
					new Dialog(BaseActivity).setTitle("Error").show(
							response.errormsg);
					BaseActivity.ShowToast("Unable to load Display");
				} else {
					ArrayList<ViewDisplayModel> products = response.data;
					if (products == null)
						new Dialog(BaseActivity).setTitle("Message").show(
								"No products found!");
					else if (products.Count() > 0) {
						TryAddViewDisplay(products);
					} else
						new Dialog(BaseActivity).setTitle("Message").show(
								"No products found!");
				}
			}

			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				return server.TryUpdateBrand();
			}
		});
		bp.execute(null, null, null);
	}

	boolean isViewDisplayAdded = false;

	private void TryAddViewDisplay(final ArrayList<ViewDisplayModel> products) {
		IsAdded = false;
		BackgroundProcess bp = new BackgroundProcess(BaseActivity)
				.setProgressMessage("adding display to page..");
		bp.setbackgroundProcess(new IProcess() {

			@Override
			public void processResponse(Object arg0) throws Exception {
				// TODO Auto-generated method stub
				if (isViewDisplayAdded)
					BaseActivity.ShowToast("Product updated sucessfully!");
			}

			@Override
			public Object underProcess() throws Exception {
				// TODO Auto-generated method stub
				UpdateViewDisplayInDataBase(products);
				isBrandAdded = true;
				return null;
			}
		});
		bp.execute(null, null, null);
	}

	private void UpdateViewDisplayInDataBase(
			ArrayList<ViewDisplayModel> products) {
		MainActivity.Current.db.DeleteAll(new ViewDisplayModel());
		MainActivity.Current.db.InsertAll(products);
		MainActivity.Database.LoadViewDisplayFromLocalDb();
	}

}
