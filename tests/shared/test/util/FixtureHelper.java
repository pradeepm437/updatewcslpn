package test.util;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;

public class FixtureHelper {

  public static String readFixture(String filePath) throws IOException {
    return Resources.toString(Resources.getResource(filePath), Charsets.UTF_8);
  }

  public static byte[] readFixtureAsBytes(String filePath) throws IOException {
    return Resources.toByteArray(Resources.getResource(filePath));
  }
}
