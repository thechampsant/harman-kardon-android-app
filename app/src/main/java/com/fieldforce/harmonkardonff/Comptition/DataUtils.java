package com.fieldforce.harmonkardonff.Comptition;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataUtils {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Map<String, List<CompitionModel>> groupByDate(List<CompitionModel> dataEntries) {

            return dataEntries.stream()
                    .collect(Collectors.groupingBy(CompitionModel::getDate));

    }

}