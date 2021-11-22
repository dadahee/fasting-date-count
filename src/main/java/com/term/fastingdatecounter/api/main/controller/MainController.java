package com.term.fastingdatecounter.api.main.controller;

import com.term.fastingdatecounter.api.user.controller.dto.dto.SessionUser;
import com.term.fastingdatecounter.api.user.domain.LoginUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "메인 API")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MainController {

    @GetMapping
    public String index(@LoginUser SessionUser user) {
        if (user != null) {
            return "redirect:/food";
        }
        return "index";
    }
}
