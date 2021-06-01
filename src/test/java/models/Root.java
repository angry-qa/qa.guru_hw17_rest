package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Root {

  @JsonProperty("data")
  private Data data;

  @JsonProperty("support")
  private Support support;

  @JsonProperty("data")
  public void setData(Data data){
    this.data = data;
  }

  @JsonProperty("data")
  public Data getData(){
    return this.data;
  }

  @JsonProperty("support")
  public void setSupport(Support support){
    this.support = support;
  }

  @JsonProperty("support")
  public Support getSupport(){
    return this.support;
  }
}
