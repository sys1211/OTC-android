package com.ubfx.track;

import android.support.annotation.IntDef;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by yangchuanzhe on 2019/4/28.
 */


public class ADParamsBuilder {


    @IntDef({ADType.banner, ADType.pop, ADType.launch, ADType.fastEntrance, ADType.vaneHome, ADType.vaneFindTop, ADType.vaneFindBottom, ADType.vaneMine})
    public @interface ADType {
        int banner = 0;
        int pop = 1;
        int launch = 2;
        int fastEntrance = 3;

        int vaneHome = 11;
        int vaneFindTop = 12;
        int vaneFindBottom = 13;
        int vaneMine = 14;
    }

    private String name;
    private int position;
    @ADType
    private int type;


    public ADParamsBuilder(String name, int position, @ADType int type) {
        this.name = name;
        this.position = position;
        this.type = type;
    }

    public ADParamsBuilder(String name, @ADType int type) {
        this.name = name;
        this.type = type;
    }

    public JSONObject build() {
        JSONObject params = new JSONObject();
        try {
            params.put("banner_name", name);
            params.put("banner_position", position + "");
            params.put("banner_type", type + "");
            params.put("banner_client", "App");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }
}
