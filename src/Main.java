import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        int row, col;
        int[][] matrix = new int[9][9];
        initPuzzle(matrix);

        File in = new File("src/in.txt");
        Scanner scanner = new Scanner(in);
        for (row = 0; row < 9; row++) {
            col = 0;
            while (scanner.hasNext() && col < matrix[row].length) {
                matrix[row][col] = scanner.nextInt();
                col++;
            }
        }

        scanner.close();
        System.out.println("Решить судоку: ");
        printPuzzle(matrix);

        Solver sudokuSolver = new Solver(matrix);
        matrix = sudokuSolver.solve();
        System.out.println("Решение судоку: ");
        printPuzzle(matrix);

        toFile("src/out.txt", matrix);
    }

    private static void toFile(String fileName, int[][] matrix) throws IOException {
        FileWriter writer = new FileWriter(fileName);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                writer.write(Integer.toString(matrix[row][col]) + " ");
            }
            writer.write("\n");
        }

        writer.flush();
        writer.close();
    }

    private static void initPuzzle(int[][] matrix) {
        for (int a = 0; a < 9; a++)
            for (int b = 0; b < 9; b++)
                matrix[a][b] = 0;
    }


    private static void printPuzzle(int[][] matrix) {
        System.out.println("-------------------------------------------------------");
        for (int row = 0; row < 9; row++) {
            System.out.print("| ");
            for (int col = 0; col < 9; col++) {
                System.out.print(" *" + matrix[row][col] + "* ");
                if (((col + 1) % 3) <= 0)
                    System.out.print(" | ");
            }

            System.out.println();
            if (((row + 1) % 3) <= 0) {
                System.out.println("-------------------------------------------------------");
            }
        }
    }
}
