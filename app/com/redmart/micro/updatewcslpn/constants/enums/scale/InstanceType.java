package com.redmart.micro.updatewcslpn.constants.enums.scale;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum InstanceType {
  WEST_FC_SCALE_2013("west_fc_scale_2013"),
  WEST_FC_SCALE_2017("west_fc_scale_2017");

  private static final Map<String, InstanceType> stringToInstanceTypeMap = new HashMap<>();

  static {
    for (InstanceType availabilityType : InstanceType.values()) {
      stringToInstanceTypeMap.put(availabilityType.getType(), availabilityType);
    }
  }

  private String type;

  InstanceType(String type) {
    this.type = type;
  }

  /**
   * get instance type.
   *
   * @param type .
   * @return optional instance type.
   */
  public static Optional<InstanceType> getInstanceType(String type) {
    return Optional.ofNullable(stringToInstanceTypeMap.get(type.toLowerCase()));
  }

  public String getType() {
    return type;
  }

  @JsonValue
  public String toValue() {
    return this.type;
  }
}
