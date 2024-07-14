package com.donkeys_today.server.application.diary;

import com.donkeys_today.server.domain.diary.Diary;
import com.donkeys_today.server.domain.user.User;
import com.donkeys_today.server.infrastructure.diary.DiaryRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiaryRetriever {

  private final DiaryRepository diaryRepository;

  public List<Diary> getDiariesByUserAndDateBetween(User user, LocalDateTime currentTime){
    LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
    LocalDateTime end = currentTime;
    return diaryRepository.findDiariesByUserAndCreatedAtBetween(user, start,end);
  }


}
