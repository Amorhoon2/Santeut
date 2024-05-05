package com.santeut.auth.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Slf4j
public class AgeUtil {

    public int calculateAge(String birth){

        LocalDate currentDate = LocalDate.now();

        int currentYear = currentDate.getYear();
        String lastTwo = String.valueOf(currentYear).substring(2);
        int lastTwoYear = Integer.parseInt(lastTwo);

        birth = birth.substring(0, 2);
        int userBirth = Integer.parseInt(birth);

        if (userBirth >= 0 && userBirth <= lastTwoYear){
            String prefixYear = "20";

            prefixYear = prefixYear + birth;
        return (currentYear-Integer.parseInt(prefixYear))+1;
        }
        else {
            String prefixYear = "19";
            prefixYear = prefixYear + birth;

            return currentYear-Integer.parseInt(prefixYear)+1;
        }

    }
}
