import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Матрицы комплексных чисел ===");

        while (true) {
            System.out.println();
            System.out.println("1. Сложить матрицы");
            System.out.println("2. Перемножить матрицы");
            System.out.println("3. Разделить матрицы (A * inverse(B))");
            System.out.println("4. Транспонировать матрицу");
            System.out.println("5. Вычислить определитель");
            System.out.println("0. Выход");

            int choice = readInt("Выберите операцию: ");

            try {
                switch (choice) {
                    case 1: {
                        ComplexMatrix a = readMatrix("A");
                        ComplexMatrix b = readMatrix("B");
                        System.out.println("A + B =");
                        System.out.println(a.add(b));
                        break;
                    }
                    case 2: {
                        ComplexMatrix a = readMatrix("A");
                        ComplexMatrix b = readMatrix("B");
                        System.out.println("A * B =");
                        System.out.println(a.multiply(b));
                        break;
                    }
                    case 3: {
                        ComplexMatrix a = readMatrix("A");
                        ComplexMatrix b = readMatrix("B");
                        System.out.println("A / B = A * inverse(B) =");
                        System.out.println(a.divide(b));
                        break;
                    }
                    case 4: {
                        ComplexMatrix a = readMatrix("A");
                        System.out.println("Транспонированная матрица:");
                        System.out.println(a.transpose());
                        break;
                    }
                    case 5: {
                        ComplexMatrix a = readMatrix("A");
                        System.out.println("Определитель: " + a.determinant());
                        break;
                    }
                    case 0:
                        System.out.println("Завершение программы.");
                        return;
                    default:
                        System.out.println("Нет такой операции.");
                }
            } catch (IllegalArgumentException | ArithmeticException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private static ComplexMatrix readMatrix(String name) {
        System.out.println("Матрица " + name + ":");
        int rows = readPositiveInt("Количество строк: ");
        int cols = readPositiveInt("Количество столбцов: ");
        ComplexMatrix matrix = new ComplexMatrix(rows, cols);

        System.out.println("Введите каждый элемент как: действительная мнимая");
        System.out.println("Например, для 3 - 2i введите: 3 -2");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                while (true) {
                    System.out.print("Элемент [" + (i + 1) + "][" + (j + 1) + "]: ");
                    try {
                        double real = Double.parseDouble(SCANNER.next());
                        double imaginary = Double.parseDouble(SCANNER.next());
                        matrix.set(i, j, new Complex(real, imaginary));
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Введите два числа, например: 3 -2");
                    }
                }
            }
        }
        System.out.println("Введена матрица:");
        System.out.println(matrix);
        return matrix;
    }

    private static int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) return value;
            System.out.println("Введите положительное целое число.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.next();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }
    }
}
