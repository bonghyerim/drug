package com.bonghyerim.drug;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

class MarkerData implements Parcelable {
    private Marker marker;
    private LatLng location;

    protected MarkerData(Parcel in) {
        location = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<MarkerData> CREATOR = new Creator<MarkerData>() {
        @Override
        public MarkerData createFromParcel(Parcel in) {
            return new MarkerData(in);
        }

        @Override
        public MarkerData[] newArray(int size) {
            return new MarkerData[size];
        }
    };

    public MarkerData(Marker marker, LatLng location) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeParcelable(location, i);
    }

    // 생성자, Getter, Setter 등 필요한 메서드 구현

    // Parcelable 구현에 관련된 코드
}