package com.bfs.mbistro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Establishments {

    @SerializedName("establishments")
    @Expose
    private List<EstablishmentContainer> establishments = null;

    public List<EstablishmentContainer> getEstablishments() {
        return establishments;
    }

    public void setEstablishments(List<EstablishmentContainer> establishments) {
        this.establishments = establishments;
    }

}
