package com.redmart.micro.updatewcslpn.injector;

import com.redmart.commons.misc.LocalHostNameSupplier;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import play.libs.Json;

import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Module that configures a Jackson ObjectMapper for JSON (de)serialization.
 * This ObjectMapper can be used via injection or through Play's own {@link Json} class .
 */
public class JacksonModule extends AbstractModule {

  public static final String IA_SLAVE_JSON = "IA_SLAVE_JSON";

  @Override
  protected void configure() {
    bind(ObjectMapper.class).annotatedWith(Names.named(IA_SLAVE_JSON)).toProvider(ObjectMapperProvider.class)
        .asEagerSingleton();
  }

  @Singleton
  public static class ObjectMapperProvider implements Provider<ObjectMapper> {

    public ObjectMapperProvider() {
      Json.setObjectMapper(get());
    }

    /**
     * Creates a new ObjectMapper already configured to follow our JSON conventions.
     */
    public ObjectMapper get() {
      ObjectMapper jsonMapper = new ObjectMapper();
      configureForwardCompatibility(jsonMapper);
      configureIso8601Dates(jsonMapper);
      configureFieldVisibility(jsonMapper);
      configureIndentation(jsonMapper);
      configureViewInclusion(jsonMapper);
      return jsonMapper;
    }

    private void configureViewInclusion(ObjectMapper jsonMapper) {
      jsonMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
    }

    private void configureForwardCompatibility(ObjectMapper jsonMapper) {
      jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private void configureIso8601Dates(ObjectMapper jsonMapper) {
      jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      jsonMapper.setDateFormat(new ISO8601DateFormat());

      // Adds more (de)serializers for classes like Period, Duration, etc.
      jsonMapper.registerModule(new JSR310Module());
    }

    private void configureFieldVisibility(ObjectMapper jsonMapper) {
      jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      jsonMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
      jsonMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    private void configureIndentation(ObjectMapper jsonMapper) {
      // Indent JSON everywhere except in Prod.
      jsonMapper.configure(SerializationFeature.INDENT_OUTPUT,
          LocalHostNameSupplier.getHostName().map(name -> !name.startsWith("prod-")).orElse(false));
    }
  }

}