package com.donkeys_today.server.infrastructure.diary;

import com.donkeys_today.server.application.diary.DiaryPublisher;
import com.donkeys_today.server.application.diary.dto.DiaryMessage;
import com.donkeys_today.server.domain.diary.Diary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisDiaryPublisher implements DiaryPublisher {

  private static final Duration TTL = Duration.ofHours(2);
  private static final Duration TEST_TTL = Duration.ofSeconds(10);
  private static final Logger log = LoggerFactory.getLogger(RedisDiaryPublisher.class);
  private final RedisTemplate<String, Object> redisTemplate;
  private final String eventPrefix = "__keyevent@0__:expired ";
  private final ObjectMapper mapper;

  @Override
  public void publishDiaryMessage(DiaryMessage message) {
    String key = "diaryMessage:"+message.userId();
    String expiryKey = "diaryMessage_expiry:"+message.userId();
    String serializedMessage = serializaeMessageToString(message);
    redisTemplate.opsForValue().set(key,serializedMessage);
    redisTemplate.opsForValue().set(expiryKey,"", 10, TimeUnit.SECONDS);
  }

  private String serializaeMessageToString(DiaryMessage message)  {
    try {
      return mapper.writeValueAsString(message);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public DiaryMessage convertDiariesToMessage(List<Diary> savedDiaries) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String savedDate = savedDiaries.getFirst().getCreatedAt().format(formatter);
    Long userId = savedDiaries.getFirst().getUser().getId();

    String message = IntStream.range(0, savedDiaries.size())
        .mapToObj(i -> (i + 1) + ". " + savedDiaries.get(i).getContent())
        .collect(Collectors.joining(", "));
    log.info("message : {}", message);
    return DiaryMessage.of(userId, message, savedDate);
  }

  private String generateKey() {
    return UUID.randomUUID().toString();
  }

}