package com.codingchallenge.app.utils;

import java.util.List;
import java.util.Random;

public class Randomizer<T> {

    public Randomizer() {
    }

    public T getRandomElement(List<T> list) {
        Random random = new Random();
        if (list != null && list.size() > 0)
            return list.get(random.nextInt(list.size()));
        else
            return null;
    }
}
