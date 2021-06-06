package modelslombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Root {

  @JsonProperty("data")
  private User data;

  @JsonProperty("support")
  private Support support;
}
