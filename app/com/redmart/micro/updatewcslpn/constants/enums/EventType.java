package com.redmart.micro.updatewcslpn.constants.enums;

import java.util.HashMap;

public enum EventType {
  ADJUSTMENT("40"),
  LOCATE("80"),
  PO_PUT_AWAY("140"),
  TRANSFER("60"),
  UNLOCATE("260");

  private static final HashMap<String, EventType> stringToRoutingKeyMap = new HashMap<>();

  static {
    for (EventType type : EventType.values()) {
      stringToRoutingKeyMap.put(type.key, type);
    }
  }

  String key;

  EventType(String key) {
    this.key = key;
  }

  /**
   * get event type.
   *
   * @param type .
   * @return event type.
   */
  public static EventType getType(String type) {
    return stringToRoutingKeyMap.get(type);
  }

}
