package com.nyangtodac.act.domain;

import lombok.Getter;

@Getter
public class FiveFourThreeTwoOne {

    private final String five;
    private final String four;
    private final String three;
    private final String two;
    private final String one;

    public FiveFourThreeTwoOne(String five, String four, String three, String two, String one) {
        this.five = five;
        this.four = four;
        this.three = three;
        this.two = two;
        this.one = one;
    }
}
