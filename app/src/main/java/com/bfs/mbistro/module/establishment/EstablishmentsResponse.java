package com.bfs.mbistro.module.establishment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EstablishmentsResponse {

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
