package com.kenzahn.zahn.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PodcastListModel {
    String LastClassDate;
    int PodcastID;
    String CreateDate;
    String PodcastURL;
    String PodcastName;
    String Description;
    int PodcastTopicID;
    boolean Active;
    String PodcastTopic;

    public PodcastListModel(String lastClassDate, int podcastID, String createDate, String podcastURL, String podcastName, int podcastTopicID, boolean active,String Description,String PodcastTopic) {
        LastClassDate = lastClassDate;
        PodcastID = podcastID;
        CreateDate = createDate;
        PodcastURL = podcastURL;
        PodcastName = podcastName;
        PodcastTopicID = podcastTopicID;
        Active = active;
        this.Description=Description;
        this.PodcastTopic=PodcastTopic;
    }

    public String getDescription() {
        return Description;
    }

    public String getPodcastTopic() {
        return PodcastTopic;
    }

    public String getLastClassDate() {
        return LastClassDate;
    }

    public int getPodcastID() {
        return PodcastID;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public String getPodcastURL() {
        return PodcastURL;
    }

    public String getPodcastName() {
        return PodcastName;
    }

    public int getPodcastTopicID() {
        return PodcastTopicID;
    }

    public boolean isActive() {
        return Active;
    }
}
