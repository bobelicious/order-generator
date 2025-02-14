package com.augusto.productsystemapi.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import feign.form.spring.converter.SpringManyMultipartFilesReader;


@Configuration
public class FeignSupportConfig {
  @Autowired
  private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    Encoder feignFormEncoder() {

        ObjectFactory<HttpMessageConverters> messageConverters = 
            () -> new HttpMessageConverters(new MappingJackson2HttpMessageConverter());
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
  }

    @Bean
    Decoder feignDecoder() {
    List<HttpMessageConverter<?>> springConverters = messageConverters.getObject().getConverters();

    List<HttpMessageConverter<?>> decoderConverters =
        new ArrayList<HttpMessageConverter<?>>(springConverters.size() + 1);

    decoderConverters.addAll(springConverters);
    decoderConverters.add(new SpringManyMultipartFilesReader(4096));

    HttpMessageConverters httpMessageConverters = new HttpMessageConverters(decoderConverters);

    return new SpringDecoder(new ObjectFactory<HttpMessageConverters>() {

      @SuppressWarnings("null")
      @Override
      public HttpMessageConverters getObject() {
        return httpMessageConverters;
      }
    });
  }

  @Bean
  Logger.Level feignLoggerLevel() {
    return Logger.Level.HEADERS; // Altere para BASIC, HEADERS ou FULL conforme necessário.
  }
}
