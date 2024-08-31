package com.fieldforce.floorhygiene;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.floorhygiene.models.ModelHeadStore;
import com.fieldforce.floorhygiene.models.ModelStoreData;
import com.fieldforce.floorhygiene.retrofitFloor.MyRetrofitService;
import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.harmonkardonff.R;
import com.ariston.training_module.utility.ItemDecorationAlbumColumns;
import com.fieldforce.utility.widgets.RobotoTextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import mob.field.harmonkardonff.entitiymodels.MyInfoModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by deepakkanyan on 21/07/20 at 8:54 PM.
 */
public class HygieneStoreListActivity extends Activity {


    RecyclerView rvLayout;
    Button fabAddNew;
    ProgressBar pbFloorHygiene;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_hygiene);
        rvLayout = findViewById(R.id.rvStoreList);
        fabAddNew = findViewById(R.id.fabAddNew);
        pbFloorHygiene = findViewById(R.id.pb_floor_hygiene);
        rvLayout.setLayoutManager(new LinearLayoutManager(this));
        getStoreList();
        fabAddNew.setOnClickListener(v -> startActivityForResult(new Intent(this, HygieneUploadStoreImagesActivity.class), 10721));
        findViewById(R.id.icon_back_btn).setOnClickListener(v -> finish());


    }

    private void getStoreList() {
        pbFloorHygiene.setVisibility(View.VISIBLE);
        MyInfoModel modelInfo = MainActivity.MyInfo;
        MyRetrofitService.getFloorApis().
                getFloorStoreList(modelInfo.UserID, modelInfo.StoreID).enqueue(new Callback<ModelHeadStore>() {
            @Override
            public void onResponse(Call<ModelHeadStore> call, Response<ModelHeadStore> response) {
                pbFloorHygiene.setVisibility(View.GONE);

                ModelHeadStore store = response.body();

                if (store != null && store.data != null && store.data.size() > 0) {

                    rvLayout.setAdapter(new AdapterFloorHygieneList(store.data));

                } else {
                    Toast.makeText(HygieneStoreListActivity.this, "No Data Found'", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<ModelHeadStore> call, Throwable t) {
                pbFloorHygiene.setVisibility(View.GONE);

                Toast.makeText(HygieneStoreListActivity.this, "Internet Connection Error'", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10721 && resultCode == Activity.RESULT_OK) {
            getStoreList();
        }
    }
}


