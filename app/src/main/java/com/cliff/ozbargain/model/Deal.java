package com.cliff.ozbargain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Clifford on 26/11/2015.
 */
public class Deal implements Parcelable{

    private static DateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy hh:mm");
    private String title;
    private Date date;
    private int posRating;
    private int negRating;
    private String description;
    private String imageUri;
    private String creator;
    private String ozDealLink;
    private List<String> categories;
    private List<String> comments;
    private String extDealUrl;
    private int commentCount;
    private int clickCount;

    public Deal() {
    }

    public Deal(String title, Date date, int posRating,int negRating,  String description, String imageUri) {
        this.title = title;
        this.date = date;
        this.posRating = posRating;
        this.negRating=negRating;
        this.description = description;
        this.imageUri = imageUri;
    }

    public Deal(Parcel source) {
        setTitle(source.readString());
        setDescription(source.readString());
        setImageUri(source.readString());
        setNegRating(source.readInt());
        setPosRating(source.readInt());
        setDate(new Date(source.readLong()));

    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getOzDealLink() {
        return ozDealLink;
    }

    public void setOzDealLink(String ozDealLink) {
        this.ozDealLink = ozDealLink;
    }

    public List<String> getCategories() {
        if (categories == null){
            categories = new ArrayList<>();
        }
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getComments() {
        if (comments == null){
            comments = new ArrayList<>();
        }
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public String getExtDealUrl() {
        return extDealUrl;
    }

    public void setExtDealUrl(String extDealUrl) {
        this.extDealUrl = extDealUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public  Date getDateObj(){
        if(date!=null) {
            return date;
        }
        return new Date();
    }
    public String getDate() {
        return dateFormat.format(getDateObj());
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPosRating() {
        return posRating;
    }

    public void setPosRating(int posRating) {
        this.posRating = posRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getNegRating() {
        return negRating;
    }

    public void setNegRating(int negRating) {
        this.negRating = negRating;
    }


    public float getRating() {
        return getPosRating()-getNegRating();


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(getTitle());
        dest.writeString(getDescription());
        dest.writeString(getImageUri());
        dest.writeInt(getNegRating());
        dest.writeInt(getPosRating());
        dest.writeLong(getDateObj().getTime());
    }

    public static final Parcelable.Creator<Deal> CREATOR =new Parcelable.Creator<Deal>(){

        @Override
        public Deal createFromParcel(Parcel source) {
            return new Deal(source);
        }

        @Override
        public Deal[] newArray(int size) {
            return new Deal[size];
        }
    };
}
