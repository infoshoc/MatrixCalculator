package ua.infoshoc.matrixcalculator;

import android.content.Intent;

/**
 * Created by Infoshoc_2 on 16.05.2015.
 */
public class Element implements Comparable {
    private Double value;

    public static final Element ZERO = new Element(0.0);
    public static final Element ONE = new Element(1.0);
    public static final Element MINUS_ONE = new Element(-1.0);
    public static final Element EPS = new Element(1e-9);

    public Element(Double value ){
        this.value = value;
    }
    public Element(Integer value ){
        this.value = value.doubleValue();
    }

    public Element(String value) {
        if(value.length() == 0) {
            this.value = 0.0;
        } else {
            this.value = Double.valueOf(value);
        }
    }

    public Element multiply(Element operand) { return new Element(value * operand.value); }

    public Element substract(Element operand) { return new Element(value - operand.value); }

    public Element divide(Element operand) { return new Element(value / operand.value); }

    public Element add(Element operand) {
        return new Element(value + operand.value);
    }

    public Element abs() { return new Element(Math.abs(value)); }

    public String getString() { return value.toString(); }

    @Override
    public int compareTo(Object another) {
        return value.compareTo(((Element)another).value);
    }
}
