package com.donkeys_today.server.presentation.api;

import com.donkeys_today.server.common.constants.Constants;
import com.donkeys_today.server.presentation.diary.dto.request.DiaryRequest;
import com.donkeys_today.server.presentation.diary.dto.response.DiaryCalenderResponse;
import com.donkeys_today.server.presentation.diary.dto.response.DiaryCreatedResponse;
import com.donkeys_today.server.presentation.diary.dto.response.DiaryListResponse;
import com.donkeys_today.server.presentation.diary.dto.response.DiaryResponse;
import com.donkeys_today.server.support.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "일기/조회/생성/삭제")
@RequestMapping("/api/v1")
@RestController
public interface DiaryController {

    @Operation(summary = "리스트뷰 일기 조회 ", description = "QueryString 을 이용해 일기(리스트뷰)조회를 합니다.")
    @GetMapping("/calender/list")
    ResponseEntity<ApiResponse<DiaryListResponse>> getDiaryList(
            @RequestParam @Parameter(name = "연도", description = "조회할 연도", required = true) final int year,
            @RequestParam @Parameter(name = "달", description = "조회할 달", required = true) final int month
    );

    @Operation(summary = "캘린더뷰 일기 조회 ", description = "QueryString 을 이용해 일기(캘린더뷰)조회를 합니다.")
    @GetMapping("/calender")
    ResponseEntity<ApiResponse<DiaryCalenderResponse>> getDiaryCalender(
            @RequestParam @Parameter(name = "연도", description = "조회할 연도", required = true) final int year,
            @RequestParam @Parameter(name = "달", description = "조회할 달", required = true) final int month
    );

    @Operation(summary = "일단위 일기 조회 ", description = "QueryString 을 이용해 일단위 일기 조회를 합니다..")
    @GetMapping("/dairy")
    ResponseEntity<ApiResponse<DiaryResponse>> getDiary(
            @RequestParam @Parameter(name = "연도", description = "조회할 연도", required = true) final int year,
            @RequestParam @Parameter(name = "달", description = "조회할 달", required = true) final int month,
            @RequestParam @Parameter(name = "일", description = "조회할 일", required = true) final int day
    );

    @Operation(summary = "일기 작성 ", description = "작성 일자와 일기 리스트를 이용해 일기를 작성하고, 답변을 요청합니다.")
    @PostMapping("/dairy")
    ResponseEntity<ApiResponse<DiaryCreatedResponse>> postDiary(
        @RequestHeader(Constants.AUTHORIZATION) String accessToken,
        @RequestBody @Parameter(name = "연도", description = "작성할 연도", required = true) final DiaryRequest request
    );
}
