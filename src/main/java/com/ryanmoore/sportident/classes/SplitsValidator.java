package com.ryanmoore.sportident.classes;

import java.util.Arrays;

import com.ryanmoore.sportident.database.Result;
import com.ryanmoore.sportident.database.Split;
import com.ryanmoore.sportident.database.Splits;

public class SplitsValidator {
    Integer[] course;

    public SplitsValidator(Integer[] course) {
        this.course = course;
    }

    int courseIndex = 0;

    public Result.Status validate(Splits splits) {

        if (splits.startTime == null) {
            return Result.Status.DNS;
        }
        if (splits.finishTime == null) {
            return Result.Status.DNF;
        }

        if (splits.controls.isEmpty() && course.length > 0) {
            return Result.Status.MP;
        }
        try {
            for (Split split : splits.controls) {
                if (split.controlNumber == course[courseIndex]) {
                    courseIndex++;
                    if (courseIndex == course.length) {
                        return Result.Status.OK;
                    }
                } else {
                    // control was incorrect
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return Result.Status.MP;
        }
        return Result.Status.MP;
    }
}
