package com.term.fastingdatecounter.api.main.controller;

import com.term.fastingdatecounter.api.user.controller.dto.dto.SessionUser;
import com.term.fastingdatecounter.api.user.domain.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Tag(name = "메인 API")
@RequiredArgsConstructor
@Controller
public class MainController {

    @Operation(summary = "메인 페이지")
    @GetMapping("/")
    public String index(@LoginUser SessionUser user) {
        if (user != null) {
            return "redirect:/food";
        }
        return "index";
    }

    @Operation(summary = "배포 테스트 페이지")
    @GetMapping("/deploy")
    public String test() {
        return "deploy";
    }
}
