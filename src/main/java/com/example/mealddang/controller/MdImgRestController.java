package com.example.mealddang.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.mealddang.model.entity.MdImgUpload;
import com.example.mealddang.model.entity.MdNutResult;
import com.example.mealddang.service.MdImgService;


@RestController
public class MdImgRestController {
    
    @Autowired
    private MdImgService mdImgService;

    // 글로벌변수
    private String originImgPath = "";
    private String username ="";

    // 이미지 업로드 메소드
    @PostMapping(value = {"/api-upload"}, consumes = {"multipart/form-data"})
    public ResponseEntity<Void> uploadImg(@Validated @RequestParam(value = "imgfile", required = false) MultipartFile imgfile, Authentication authentication) throws Exception {
        username = authentication.getName();

        // 서버로컬에 이미지 저장
        MdImgUpload mdImgUpload = mdImgService.imgUploader(MdImgUpload.builder().build(), username, imgfile);

        // 글로벌변수 originImgPath 업데이트
        originImgPath = mdImgUpload.getOriginPath();

        // View로 데이터 전달하지 않고 HTTP 상태 200 OK만 반환
        return ResponseEntity.ok().build();
    }

    // [미완] 욜로 모델로 원본 이미지 전달 및 결과 받는 메소드
    @GetMapping(value = {"/api-yolo"})
    public ResponseEntity<?> getYoloResult() {
        // 업로드 이미지 원본을 욜로모델에게 전달
        // 코드 짜야함. 아래는 dummy result
        String resultPath = "/";
        String resultLabel = "김치찌개";

        // 분석 결과 저장 (MdYoloResult 엔티티 n개 생성)
        mdImgService.saveYoloResult(originImgPath, resultPath);

        // 분석 결과 저장2 (MDNutResult 엔티티 n개 생성)
        MdNutResult mdNutResult = mdImgService.saveNutResult(username, originImgPath, resultPath, resultLabel);

        // View로 데이터 전달 n개?
        Map<String, Object> response = new HashMap<>();
        response.put("mdNutResult", mdNutResult);

        return ResponseEntity.ok().body(response);
    }

    // 이미지 삭제 (MdImgUpload, MdNutResult, MdYoloResult 모두?) : 보류
    @GetMapping("/api-delete")
    public void deleteImg() {
    }
}
