package ru.mazemadness.maze_madness.utils;

public class Utils {

    public Boolean isValidName(String playerName){
        if (playerName == null || playerName.isEmpty()){
            return false;
        }
        if (playerName.length() < 3 || playerName.length() > 16) {
            return false;
        }
        for (char ch : playerName.toCharArray()){
            if (!Character.isLetterOrDigit(ch) && ch != '_'){
                return false;
            }
        }
        if (playerName.equalsIgnoreCase("nigger") || playerName.equalsIgnoreCase("faggot")){
            return false;
        }
        return true;
    }
}
