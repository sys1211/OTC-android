package com.ubfx.library.region;

import com.ubfx.library.model.RegionModel;
import com.ubfx.network.request.UBSuccessListener;

import java.util.List;

/**
 * Created by yangchuanzhe on 2019/10/11.
 */
public interface RegionDataProvider {
    List<RegionModel> loadRegions();
    void loadBlackList(UBSuccessListener<List<String>> callback);
}
