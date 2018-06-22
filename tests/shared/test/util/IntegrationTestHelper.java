package test.util;

import static org.junit.Assert.assertFalse;

import com.redmart.micro.iaslavescaleproxy.injector.JacksonModule;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.core.j.JavaResultExtractor;
import play.mvc.Result;

import java.io.IOException;
import java.util.List;

public class IntegrationTestHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationTestHelper.class);

  private ObjectMapper objectMapper = new JacksonModule.ObjectMapperProvider().get();

  /**
   * Parse json to list of POJO.
   *
   * @param clazz      class to map request body to.
   * @param jsonString controller result.
   * @return list of POJO object mapped.
   */
  public <T> List<T> parseList(Class<T> clazz, String jsonString) {
    if (jsonString == null) {
      assertFalse("Error mapping the null result", Boolean.TRUE);
      return null;
    }

    try {
      JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
      return objectMapper.readValue(jsonString, type);
    } catch (IOException e) {
      assertFalse("get result from json io exception", false);
      LOGGER.warn("exception:", e);
      return null;
    }
  }

  /**
   * Parse json to list of POJO.
   *
   * @param clazz      class to map request body to.
   * @return list of POJO object mapped.
   */
  public <T> List<T> parseList(Class<T> clazz, Result result) {
    if (result == null) {
      assertFalse("Error mapping the null result", Boolean.TRUE);
      return null;
    }

    try {
      JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
      return objectMapper.readValue(JavaResultExtractor.getBody(result, 0L), type);
    } catch (IOException e) {
      assertFalse("get result from result io exception", false);
      LOGGER.warn("exception:", e);
      return null;
    }
  }

  /**
   * Parse result  to POJO.
   *
   * @param clazz  class to map request body to.
   * @param result controller result.
   * @return POJO object mapped.
   */
  public <T> T parseResult(Class<T> clazz, Result result) {
    if (result == null) {
      assertFalse("Error mapping the null result", Boolean.TRUE);
      return null;
    }
    return parseResult(clazz, JavaResultExtractor.getBody(result, 0L));
  }

  public <T> T parseResult(Class<T> clazz, byte[] bytes) {
    try {
      return objectMapper.readValue(bytes, clazz);
    } catch (IOException e) {
      assertFalse("get result from bytes io exception", false);
      LOGGER.warn("exception:", e);
      return null;
    }
  }
}