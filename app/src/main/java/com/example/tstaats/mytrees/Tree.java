package com.example.tstaats.mytrees;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tree {

    private String treeName;
    private String treeImageUrl;
    private String treeDescription;
    private Date created;
    private Date updated;
    private String objectId;
    private String userEmail;
    private List<TreeState> treeStates;

    public void addTreeState(TreeState treeState){
        if (treeStates == null){
            treeStates = new ArrayList<>();
        }
        treeStates.add(treeState);
    }

    public List<TreeState> getTreeStates() {
        return treeStates;
    }

    public void setTreeStates(List<TreeState> treeStates) {
        this.treeStates = treeStates;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    public String getTreeImageUrl() {
        return treeImageUrl;
    }

    public void setTreeImageUrl(String treeImageUrl) {
        this.treeImageUrl = treeImageUrl;
    }

    public String getTreeDescription() {
        return treeDescription;
    }

    public void setTreeDescription(String treeDescription) {
        this.treeDescription = treeDescription;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
