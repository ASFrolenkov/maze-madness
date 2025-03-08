package ru.mazemadness.maze_madness.utils;

import java.security.SecureRandom;
import java.util.Random;

public class RandomData {
    public short getRandomId() {
        return (short) new Random().nextInt(9999);
    }

    public String getRandomColor(){
        Random random = new Random();
        int nextInt = random.nextInt(0xffffff + 1);
        return String.format("0x%06x", nextInt);
    }

    public float getRandomPosition(int min, int max){
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(max - min + 1) + min;
    }
}
