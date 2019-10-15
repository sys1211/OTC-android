package com.ubfx.library.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.ubfx.library.region.RegionManager;

/**
 * Created by chuanzheyang on 2017/8/15.
 */

public class RegionModel implements Parcelable {
    public String name = "";
    public String callingcode = "";
    @SerializedName("id")
    public String nameShort = "";
    public String cate_cn = "";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.callingcode);
        dest.writeString(this.nameShort);
        dest.writeString(this.cate_cn);
    }

    public RegionModel() {
    }

    protected RegionModel(Parcel in) {
        this.name = in.readString();
        this.callingcode = in.readString();
        this.nameShort = in.readString();
        this.cate_cn = in.readString();
    }

    public static final Parcelable.Creator<RegionModel> CREATOR = new Parcelable.Creator<RegionModel>() {
        @Override
        public RegionModel createFromParcel(Parcel source) {
            return new RegionModel(source);
        }

        @Override
        public RegionModel[] newArray(int size) {
            return new RegionModel[size];
        }
    };

    public boolean isCN() {
        return nameShort.equals(RegionManager.getChinaShort());
    }

    public boolean isCommonUse() {
        return isCN() || nameShort.equals("TW") || nameShort.equals("AU") || nameShort.equals("NZ");
    }
}
