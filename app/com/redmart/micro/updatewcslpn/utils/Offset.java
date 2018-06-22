package com.redmart.micro.iaslavescaleproxy.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class Offset {
  /*
   * <p> Used to Calculate time Offset between current system
   * and SGT.</p>
   */

    /**
     * Updates wcsLpn for container.
     *
     * @return 200 on success 220 on error .
     */
  public Integer getTimeOffset() {
    Instant current = Instant.now();
    ZoneOffset sgZoneOffset = ZoneId.of("Asia/Singapore").getRules().getOffset(current);
    ZoneOffset localZoneOffset = ZoneId.systemDefault().getRules().getOffset(current);
    return sgZoneOffset.getTotalSeconds() - localZoneOffset.getTotalSeconds();
  }
}
