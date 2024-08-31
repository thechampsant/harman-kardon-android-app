package com.fieldforce.floorhygiene.retrofitFloor;

import com.fieldforce.floorhygiene.models.ModelHeadStore;
import com.ariston.training_module.modules.training_module.models.TrainingParentModel;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by deepakkanyan on 22/07/20 at 3:29 PM.
 */
public interface FloorApis {

    @GET("GetStoresFloorHygiene")
    Call<ModelHeadStore> getFloorStoreList(
            @Query("LoginID") String loginID,
            @Query("storeID") String storeID

    );

    @GET("")
    Call<ModelHeadStore> uploadImagesDocIDs(
            @Query("uploadBy") String uploadBy,
            @Query("DocID") String docID,
            @Query("DocType") String docType

    );


    @GET("SaveStoresFloorHygiene")
    Call<ModelHeadStore> uploadFullStoreData(
            @Query("DateFor") String DateFor,
            @Query("StoreId") String StoreId,
            @Query("DocIds") String DocIds,
            @Query("LoginId") String LoginId,
            @Query("storeID") String storeID

    );



}
