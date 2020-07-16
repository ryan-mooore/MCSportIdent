package com.spurposting.sportident.classes;

import com.spurposting.sportident.database.Split;
import com.spurposting.sportident.database.Splits;

public class SplitsValidator {
    Integer[] course;

    public SplitsValidator(Integer[] course) {
        this.course = course;
    }

    int courseIndex = 0;
    public boolean validate(Splits splits) {
        if (splits.controls.isEmpty() && course.length > 0) {
            return false;
        }
        try {
            for (Split split : splits.controls) {
                if (split.controlNumber == course[courseIndex]) {
                    courseIndex++;
                    if (courseIndex == course.length) {
                        return true;
                    }
                } else {
                    //control was incorrect
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    return false;
    }
}
