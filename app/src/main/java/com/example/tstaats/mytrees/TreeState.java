package com.example.tstaats.mytrees;

import java.util.Date;

public class TreeState {

    private String rootTreeName;
    private String treeStateImageUrl;
    private String treeStateDescription;
    private Date created;
    private Date updated;
    private String objectId;

    public String getRootTreeName() {
        return rootTreeName;
    }

    public void setRootTreeName(String rootTreeName) {
        this.rootTreeName = rootTreeName;
    }

    public String getTreeStateImageUrl() {
        return treeStateImageUrl;
    }

    public void setTreeStateImageUrl(String treeStateImageUrl) {
        this.treeStateImageUrl = treeStateImageUrl;
    }

    public String getTreeStateDescription() {
        return treeStateDescription;
    }

    public void setTreeStateDescription(String treeStateDescription) {
        this.treeStateDescription = treeStateDescription;
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
