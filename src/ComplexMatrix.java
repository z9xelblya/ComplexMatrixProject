public final class ComplexMatrix {
    private static final double EPS = 1e-12;
    private final int rows;
    private final int cols;
    private final Complex[][] data;

    public ComplexMatrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Размеры матрицы должны быть положительными.");
        }
        this.rows = rows;
        this.cols = cols;
        data = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = Complex.ZERO;
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Complex get(int row, int col) {
        return data[row][col];
    }

    public void set(int row, int col, Complex value) {
        if (value == null) {
            throw new IllegalArgumentException("Элемент матрицы не может быть null.");
        }
        data[row][col] = value;
    }

    public ComplexMatrix add(ComplexMatrix other) {
        requireSameSize(other);
        ComplexMatrix result = new ComplexMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.data[i][j] = data[i][j].add(other.data[i][j]);
            }
        }
        return result;
    }

    public ComplexMatrix multiply(ComplexMatrix other) {
        if (cols != other.rows) {
            throw new IllegalArgumentException(
                    "Число столбцов первой матрицы должно равняться числу строк второй."
            );
        }

        ComplexMatrix result = new ComplexMatrix(rows, other.cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                Complex sum = Complex.ZERO;
                for (int k = 0; k < cols; k++) {
                    sum = sum.add(data[i][k].multiply(other.data[k][j]));
                }
                result.data[i][j] = sum;
            }
        }
        return result;
    }

    public ComplexMatrix divide(ComplexMatrix other) {
        if (other.rows != other.cols) {
            throw new IllegalArgumentException("Делитель должен быть квадратной матрицей.");
        }
        if (cols != other.rows) {
            throw new IllegalArgumentException(
                    "Число столбцов первой матрицы должно равняться размеру делителя."
            );
        }
        return multiply(other.inverse());
    }

    public ComplexMatrix transpose() {
        ComplexMatrix result = new ComplexMatrix(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.data[j][i] = data[i][j];
            }
        }
        return result;
    }

    public Complex determinant() {
        requireSquare();
        Complex[][] copy = copyData();
        Complex det = Complex.ONE;
        int sign = 1;

        for (int col = 0; col < rows; col++) {
            int pivot = findPivot(copy, col, col);
            if (pivot == -1 || copy[pivot][col].abs() < EPS) {
                return Complex.ZERO;
            }

            if (pivot != col) {
                swapRows(copy, pivot, col);
                sign *= -1;
            }

            Complex pivotValue = copy[col][col];
            det = det.multiply(pivotValue);

            for (int row = col + 1; row < rows; row++) {
                Complex factor = copy[row][col].divide(pivotValue);
                for (int j = col + 1; j < cols; j++) {
                    copy[row][j] = copy[row][j].subtract(factor.multiply(copy[col][j]));
                }
                copy[row][col] = Complex.ZERO;
            }
        }

        return sign < 0 ? det.negate() : det;
    }

    public ComplexMatrix inverse() {
        requireSquare();
        int n = rows;
        Complex[][] left = copyData();
        Complex[][] right = new Complex[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                right[i][j] = i == j ? Complex.ONE : Complex.ZERO;
            }
        }

        for (int col = 0; col < n; col++) {
            int pivot = findPivot(left, col, col);
            if (pivot == -1 || left[pivot][col].abs() < EPS) {
                throw new ArithmeticException("Матрица вырождена, обратной матрицы не существует.");
            }

            if (pivot != col) {
                swapRows(left, pivot, col);
                swapRows(right, pivot, col);
            }

            Complex pivotValue = left[col][col];
            for (int j = 0; j < n; j++) {
                left[col][j] = left[col][j].divide(pivotValue);
                right[col][j] = right[col][j].divide(pivotValue);
            }

            for (int row = 0; row < n; row++) {
                if (row == col) continue;
                Complex factor = left[row][col];
                for (int j = 0; j < n; j++) {
                    left[row][j] = left[row][j].subtract(factor.multiply(left[col][j]));
                    right[row][j] = right[row][j].subtract(factor.multiply(right[col][j]));
                }
            }
        }

        ComplexMatrix result = new ComplexMatrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result.data[i][j] = right[i][j];
            }
        }
        return result;
    }

    private void requireSameSize(ComplexMatrix other) {
        if (rows != other.rows || cols != other.cols) {
            throw new IllegalArgumentException("Для сложения размеры матриц должны совпадать.");
        }
    }

    private void requireSquare() {
        if (rows != cols) {
            throw new IllegalArgumentException("Операция доступна только для квадратной матрицы.");
        }
    }

    private Complex[][] copyData() {
        Complex[][] copy = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(data[i], 0, copy[i], 0, cols);
        }
        return copy;
    }

    private static int findPivot(Complex[][] matrix, int startRow, int col) {
        int pivot = -1;
        double max = 0;
        for (int row = startRow; row < matrix.length; row++) {
            double value = matrix[row][col].abs();
            if (value > max) {
                max = value;
                pivot = row;
            }
        }
        return pivot;
    }

    private static void swapRows(Complex[][] matrix, int first, int second) {
        Complex[] temp = matrix[first];
        matrix[first] = matrix[second];
        matrix[second] = temp;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            result.append("| ");
            for (int j = 0; j < cols; j++) {
                result.append(data[i][j]);
                if (j + 1 < cols) result.append("\t");
            }
            result.append(" |");
            if (i + 1 < rows) result.append(System.lineSeparator());
        }
        return result.toString();
    }
}
