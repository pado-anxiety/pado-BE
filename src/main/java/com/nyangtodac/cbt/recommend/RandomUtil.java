package com.nyangtodac.cbt.recommend;

import java.util.Random;

public class RandomUtil {

    private static final Random RANDOM = new Random();

    public static int pickRandom(int size) {
        return RANDOM.nextInt(size);
    }
}
