package ua.infoshoc.matrixcalculator;

/**
 * Created by Infoshoc_2 on 15-Sep-15.
 */
public class Element implements Comparable {

    private int numerator, denominator;
    private static int gcd(int a, int b) {
        while (b != 0) {
            int tmp = a % b;
            a = b;
            b = tmp;
        }
        return a;
    }
    private static int lcm(int a, int b) {
        return a / gcd(a, b) * b;
    }
    private static int sign(int a) {
        return a >= 0 ? 1 : -1;
    }
    private static int pow10(int x) { int result = 1; for (int exp = 0; exp < x; ++exp) result *= 10; return result;}

    public static final Element MINUS_ONE = new Element(-1, 1);
    public static final Element ZERO = new Element(0, 1);
    public static final Element ONE = new Element(1, 1);

    Element (int numerator, int denominator) {
        int reduction = Math.abs(gcd(numerator, denominator));
        int thisSign = sign(numerator) * sign(denominator);
        this.numerator = thisSign * Math.abs(numerator) / reduction;
        this.denominator = Math.abs(denominator) / reduction;
    }

    Element (int value) {
        this(value, 1);
    }

    Element (String value) {
        // TODO parse expressions
        if (value.length() == 0) {
            numerator = 0;
            denominator = 1;
        } else {
            String[] tokens = value.split("/");
            if (tokens.length == 1) {
                String[] doubleParts = tokens[0].split("\\.");
                if (doubleParts.length == 1) {
                    numerator = Integer.parseInt(tokens[0]);
                    denominator = 1;
                } else if (doubleParts.length == 2) {
                    Element fraction = (new Element(Integer.parseInt(doubleParts[0]))).add(new Element(Integer.parseInt(doubleParts[1]), pow10(doubleParts[1].length())));
                    numerator = fraction.numerator;
                    denominator = fraction.denominator;
                } else {
                    // TODO propagate it
                    throw new ArithmeticException();
                }

            } else if (tokens.length == 2) {
                numerator = Integer.parseInt(tokens[0]);
                denominator = Integer.parseInt(tokens[1]);
            } else {
                // TODO propagate it
                throw new ArithmeticException();
            }
        }
    }

    @Override
    public int compareTo(Object another) {
        Element val = (Element) another;

        return numerator * val.denominator - denominator * val.numerator;
    }

    public String getString() {
        if (denominator == 1) {
            return String.valueOf(numerator);
        }
        return String.valueOf(numerator) + "/" + String.valueOf(denominator);
    }

    public Element add(Element another) {
        int commonDenominator = lcm(denominator, another.denominator);
        int newNumerator = numerator * commonDenominator / denominator + another.numerator * commonDenominator / another.denominator;
        int reduction = Math.abs(gcd(commonDenominator, newNumerator));
        commonDenominator /= reduction;
        newNumerator /= reduction;
        return new Element(newNumerator, commonDenominator);
    }

    public Element multiply(Element another) {
        int reduction[] = {Math.abs(gcd(numerator, another.denominator)), Math.abs(gcd(denominator, another.numerator))};
        return new Element(numerator / reduction[0] * another.numerator / reduction[1], denominator / reduction[1] * another.denominator / reduction[0]);
    }

    public Element substract(Element another) {
        return this.add(another.multiply(Element.MINUS_ONE));
    }

    public Element divide(Element another) {
        if (another.compareTo(Element.ZERO) == 0) {
            throw new ArithmeticException();
        }
        return this.multiply(new Element(another.denominator, another.numerator));
    }

    public Element abs(){
        return new Element(Math.abs(numerator), Math.abs(denominator));
    }
}
