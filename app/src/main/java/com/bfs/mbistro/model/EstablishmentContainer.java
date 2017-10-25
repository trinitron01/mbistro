package com.bfs.mbistro.model;

import com.squareup.moshi.Json;

public class EstablishmentContainer {

    @Json(name = "establishment")
    private Establishment establishment;

    public Establishment getEstablishment() {
        return establishment;
    }

    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }

}
