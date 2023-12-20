package com.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TransferController {

    @GetMapping("/transfer")
    public String transferPage() {
        return "transfer";
    }
}
