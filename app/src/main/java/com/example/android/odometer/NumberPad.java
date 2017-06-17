package com.example.android.odometer;

/**
 * Created by ACornelius on 5/22/2017.
 */

public class NumberPad {

    private final StringBuilder builder = new StringBuilder("0");

    public NumberPad() {
    }

    private void digit(final char digit) {
        if("0".contentEquals(builder)) {
            builder.setCharAt(0, digit);
        } else {
            builder.append(digit);
        }
    }

    public void zero() {
            if(!"0".contentEquals(builder)) {
            builder.append('0');
        }
    }

    public void one() {
        digit('1');
    }

    public void two() {
        digit('2');
    }

    public void three() {
        digit('3');
    }

    public void four() {
        digit('4');
    }

    public void five() {
        digit('5');
    }

    public void six() {
        digit('6');
    }

    public void seven() {
        digit('7');
    }

    public void eight() {
        digit('8');
    }

    public void nine() {
        digit('9');
    }

    public void delete() {
        builder.deleteCharAt(builder.length() - 1);

        if (builder.length() == 0) {
            builder.append('0');
        }
    }

//    public void save() {
//        System.out.println("numberpad save()");
//    }

    public void clearDisplay() {
        while (builder.length() > 1) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.setCharAt(0, '0');
    }

    public CharSequence getCurrentDisplay() {
        return builder;
    }

}
