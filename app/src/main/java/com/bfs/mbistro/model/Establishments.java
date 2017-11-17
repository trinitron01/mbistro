package com.bfs.mbistro.model;

import com.squareup.moshi.Json;
import java.util.List;

public class Establishments {

  @Json(name = "establishments")

  private List<EstablishmentContainer> establishments = null;

  public List<EstablishmentContainer> getEstablishments() {
    return establishments;
  }

  public void setEstablishments(List<EstablishmentContainer> establishments) {
    this.establishments = establishments;
  }
}
