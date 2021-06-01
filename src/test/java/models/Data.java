package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Data {
    @JsonProperty("id")
    private int id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("first_name")
    private String first_name;

    @JsonProperty("last_name")
    private String last_name;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("id")
    public void setId(int id){
        this.id = id;
    }

    @JsonProperty("id")
    public int getId(){
        return this.id;
    }

    @JsonProperty("email")
    public void setEmail(String email){
        this.email = email;
    }

    @JsonProperty("email")
    public String getEmail(){
        return this.email;
    }

    @JsonProperty("first_name")
    public void setFirst_name(String first_name){
        this.first_name = first_name;
    }

    @JsonProperty("first_name")
    public String getFirst_name(){
        return this.first_name;
    }

    @JsonProperty("last_name")
    public void setLast_name(String last_name){
        this.last_name = last_name;
    }

    @JsonProperty("last_name")
    public String getLast_name(){
        return this.last_name;
    }

    @JsonProperty("avatar")
    public void setAvatar(String avatar){
        this.avatar = avatar;
    }

    @JsonProperty("avatar")
    public String getAvatar(){
        return this.avatar;
    }

}
