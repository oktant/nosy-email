package tech.nosy.nosyemail.nosyemail.dto;


import javax.validation.constraints.NotNull;


public class InputSystemDto {
  @NotNull private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
