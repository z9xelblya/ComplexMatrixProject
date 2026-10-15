import java.util.Locale;

public final class Complex {
    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE = new Complex(1, 0);

    private final double real;
    private final double imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public Complex add(Complex other) {
        return new Complex(real + other.real, imaginary + other.imaginary);
    }

    public Complex subtract(Complex other) {
        return new Complex(real - other.real, imaginary - other.imaginary);
    }

    public Complex multiply(Complex other) {
        return new Complex(
                real * other.real - imaginary * other.imaginary,
                real * other.imaginary + imaginary * other.real
        );
    }

    public Complex divide(Complex other) {
        double denominator = other.real * other.real + other.imaginary * other.imaginary;
        if (denominator < 1e-24) {
            throw new ArithmeticException("Деление на ноль комплексного числа.");
        }

        return new Complex(
                (real * other.real + imaginary * other.imaginary) / denominator,
                (imaginary * other.real - real * other.imaginary) / denominator
        );
    }

    public Complex negate() {
        return new Complex(-real, -imaginary);
    }

    public double abs() {
        return Math.hypot(real, imaginary);
    }

    public boolean isZero() {
        return abs() < 1e-12;
    }

    @Override
    public String toString() {
        double r = Math.abs(real) < 1e-10 ? 0 : real;
        double i = Math.abs(imaginary) < 1e-10 ? 0 : imaginary;

        if (i == 0) {
            return format(r);
        }
        if (r == 0) {
            if (i == 1) return "i";
            if (i == -1) return "-i";
            return format(i) + "i";
        }

        String sign = i < 0 ? " - " : " + ";
        double absI = Math.abs(i);
        String imagPart = absI == 1 ? "i" : format(absI) + "i";
        return format(r) + sign + imagPart;
    }

    private static String format(double value) {
        if (Math.abs(value - Math.rint(value)) < 1e-10) {
            return String.format(Locale.US, "%.0f", value);
        }
        return String.format(Locale.US, "%.4f", value).replaceAll("0+$", "").replaceAll("\\.$", "");
    }
}
