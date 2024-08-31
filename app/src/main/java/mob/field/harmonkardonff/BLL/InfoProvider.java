package mob.field.harmonkardonff.BLL;



import com.fieldforce.harmonkardonff.MainActivity;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.ProductModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import app.core.base.InnosolsActivity;

public class InfoProvider {

	private InnosolsActivity BaseActivity;
	public InfoProvider(InnosolsActivity a)
	{
		BaseActivity=a;
	}
	public void SaveMOPDeletedOnInSharedPreferences(String deletedOn) {
		// TODO Auto-generated method stub
		SharedPreferences sf = BaseActivity
				.getPreferences(Context.MODE_PRIVATE);
		Editor Ed = sf.edit();
		Ed.putString("MOP_P_DeletedOn", deletedOn);
		Ed.commit();
	}

	public void SaveMOPUpdatedOnInSharedPreferences(String updatedOn) {
		// TODO Auto-generated method stub
		SharedPreferences sf = BaseActivity
				.getPreferences(Context.MODE_PRIVATE);
		Editor Ed = sf.edit();
		Ed.putString("MOP_P_UpdatedOn", updatedOn);
		Ed.commit();
	}

	public void DeleteProductsFromLocalDb(
			ArrayList<ProductModel> productsForDelete) {
		// TODO Auto-generated method stub
		/*
		 * for (ProductModel product: productsForDelete) {
		 * if(IsExistsInDatabase(product.PID)) { String whereClause =
		 * TProducts.COLUMN_NAME_PRODUCT_ID + " LIKE ?"; String[] whereArgs = {
		 * String.valueOf(product.PID) };
		 * 
		 * int count = db.delete(TProducts.TABLE_NAME, whereClause,whereArgs); }
		 * }
		 */
	}

	public void SaveUpdatedOnInSharedPreferences(String updatedOn) {
		// TODO Auto-generated method stub
		SharedPreferences sf = BaseActivity
				.getPreferences(Context.MODE_PRIVATE);
		Editor Ed = sf.edit();
		Ed.putString("PUpdatedOn", updatedOn);
		Ed.commit();
	}

	public void SaveDeletedOnInSharedPreferences(String deletedOn) {
		// TODO Auto-generated method stub
		SharedPreferences sf = BaseActivity
				.getPreferences(Context.MODE_PRIVATE);
		Editor Ed = sf.edit();
		Ed.putString("PDeletedOn", deletedOn);
		Ed.commit();
	}

	public void SaveProductsInLocalDb(ArrayList<ProductModel> myProductList) {
		// TODO Auto-generated method stub

		MainActivity.Database.InsertUpdateDeleteProducts(myProductList);

	}
}
