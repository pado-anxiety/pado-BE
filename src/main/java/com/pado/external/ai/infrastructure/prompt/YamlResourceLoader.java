package com.pado.external.ai.infrastructure.prompt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class YamlResourceLoader {

    private static final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    private YamlResourceLoader() {}

    public static <T> T load(String resourcePath, Class<T> clazz) {
        try (InputStream inputStream = new ClassPathResource(resourcePath).getInputStream()) {
            return objectMapper.readValue(inputStream, clazz);
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException("파일이 존재하지 않거나 역직렬화에 실패했습니다: " + resourcePath + " -> " + clazz.toString());
        }
    }
}