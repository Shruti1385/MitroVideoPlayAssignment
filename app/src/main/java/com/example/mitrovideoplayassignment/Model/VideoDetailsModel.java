package com.example.mitrovideoplayassignment.Model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class VideoDetailsModel implements Parcelable {
    private String videoName;
    private String videoCreatedDate;
    Uri videoUri;

    public VideoDetailsModel() {
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoCreatedDate() {
        return videoCreatedDate;
    }

    public void setVideoCreatedDate(String videoCreatedDate) {
        this.videoCreatedDate = videoCreatedDate;
    }

    public Uri getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //Note: writeString and readString must have same order/sequence
        dest.writeString(videoName);
        dest.writeString(videoCreatedDate);
        dest.writeString(videoUri.toString());
    }

    public static final Creator<VideoDetailsModel> CREATOR
            = new Parcelable.Creator<VideoDetailsModel>() {
        public VideoDetailsModel createFromParcel(Parcel in) {
            return new VideoDetailsModel(in);
        }

        public VideoDetailsModel[] newArray(int size) {
            return new VideoDetailsModel[size];
        }
    };

    public VideoDetailsModel(Parcel in) {
        Uri.Builder builder = new Uri.Builder();


        videoName = in.readString();
        videoCreatedDate = in.readString();
        videoUri= builder.path(in.readString()).build();
    }
}
