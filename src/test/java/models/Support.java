package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Support {

  @JsonProperty("url")
  private String url;

  @JsonProperty("text")
  private String text;

  @JsonProperty("url")
  public void setUrl(String url){
    this.url = url;
  }

  @JsonProperty("url")
  public String getUrl(){
    return this.url;
  }

  @JsonProperty("text")
  public void setText(String text){
    this.text = text;
  }

  @JsonProperty("text")
  public String getText(){
    return this.text;
  }

}
