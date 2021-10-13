package com.suom.exam.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Setter
@Getter
@ToString
public class Quiz {
    private String number;
    private String question;
    private String additional;
    private Map<String, String> options;
    private String answer;
}
