package com.kenzahn.zahn.model;

public class PodcastModel
{
    String PodcastTopic;
    String Status;
    int StatusTypeID;
    int ContactID;
    int SortOrder;
    String podcastDetails;

    public PodcastModel(String podcastTopic, String status, int statusTypeID, int contactID, int sortOrder, String podcastDetails) {
        PodcastTopic = podcastTopic;
        Status = status;
        StatusTypeID = statusTypeID;
        ContactID = contactID;
        SortOrder = sortOrder;
        this.podcastDetails = podcastDetails;
    }

    public String getPodcastTopic() {
        return PodcastTopic;
    }

    public String getStatus() {
        return Status;
    }

    public int getStatusTypeID() {
        return StatusTypeID;
    }

    public int getContactID() {
        return ContactID;
    }

    public int getSortOrder() {
        return SortOrder;
    }

    public String getPodcastDetails() {
        return podcastDetails;
    }
}
