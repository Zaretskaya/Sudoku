public class Solver {

    private int NUM = 9;
    private int ROW = 3;
    private int COL = 3;
    private int BLt = 3;

    private int[][] incoming = new int[NUM][NUM]; // Матрица проблемы
    private boolean[][] tagRow = new boolean[NUM][NUM]; // Теги строк
    private boolean[][] tagCol = new boolean[NUM][NUM]; // Теги колонок
    private boolean[][] tagBlt = new boolean[NUM][NUM]; // Теги блоков

    public Solver(int[][] inputMatrix) {
        initTag();
        initPuzzle();

        int b = 0;
        while (b < NUM) {
            int t = 0;
            while (t < NUM) {
                if (inputMatrix[b][t] < 1 || inputMatrix[b][t] > NUM) {
                } else {
                    incoming[b][t] = inputMatrix[b][t];
                }
                t++;
            }
            b++;
        }

        fillTags();
    }

    public int[][] solve() {
        if (solution()) {
            return incoming;
        } else {
            System.out.println("Ошибка: задача не может быть решена");
            return null;
        }
    }

    private void initTag() {
        for (int b = 0; b < NUM; b++) {
            for (int t = 0; t < NUM; t++) {
                tagRow[b][t] = false;
                tagCol[b][t] = false;
                tagBlt[b][t] = false;
            }
        }
    }

    private void initPuzzle() {
        int b = 0;
        while (b < NUM) {
            int t = 0;
            while (t < NUM) {
                incoming[b][t] = 0;
                t++;
            }
            b++;
        }
    }


    private void fillTags() {
        int a= 0;
        while (a< NUM) {
            int b = 0;
            while (b < NUM) {
                setTag(a, b, incoming[a][b]);
                b++;
            }
            a++;
        }
    }


    private boolean fit(int a, int b, int c) {
        c--;

        if (tagRow[a][c])
            return false;

        if (tagCol[c][b])
            return false;

        return !tagBlt[(a/ BLt) * BLt + b / BLt][c];
    }


    private void setTag(int a, int b, int m) {
        if (m == 0)
            return;
        m--;
        tagRow[a][m] = true;
        tagCol[m][b] = true;
        tagBlt[(a/ BLt) * BLt + b / BLt][m] = true;
    }


    private void resetTag(int a, int b, int m) {
        if (m == 0)
            return;

        m--;
        tagRow[a][m] = false;
        tagCol[m][b] = false;
        tagBlt[(a/ BLt) * BLt + b / BLt][m] = false;
    }


    private boolean solution() {
        logic();
        //printPuzzle();
        return bactward(0, 0, incoming);
    }


    private void logic() {
        int a, b, t, l = 0, row = 0, col = 0, cnt = 0;

        boolean notOnlyOne; // Используется для проверки, действительно ли можно положить единственное значение
        boolean value;  // Устанавливается как только мы выполнили присвание в процессе while

        while (true) {
            value = false; // Допустим, мы не выполнили присванивание
            for (t = 0; t < NUM; t++) { // Проверяем все блоки на наличие "точных" ячеек
                for (l = 1; l < (NUM + 1); l++) {
                    cnt = 0;
                    notOnlyOne = false; // Допустим, у нас нет "точных" ячеек

                    for (a= (t / COL) * COL; a< ((t / COL) * COL + COL); a++) {
                        b = (t % COL) * ROW;
                        while (b < ((t % COL) * ROW + ROW)) {
                            if (incoming[a][b] > 0) // Пропускаем заполненные ячейки
                            {
                                b++;
                                continue;
                            }

                            if (fit(a, b, l)) { // Если находим пустую ячейку, проверяем валдиность текущего значения
                                row = a;
                                col = b;
                                cnt++;
                            }

                            if (cnt > 1) { // Проверяем, что это единственная "точная" ячейка
                                notOnlyOne = true; // Если несколько валидных значений, выходим
                                break;
                            }
                            b++;
                        }

                        if (notOnlyOne) // Если несколько валидных значений, выходим
                            break;
                    }

                    if (cnt == 1) { // Если нашли единственное валидное значение, присваиваем его
                        incoming[row][col] = l;
                        setTag(row, col, l); // Устанавливаем теги
                        value = true; // Запоминаем, что выполнили присваиваение
                    }
                }
            }

            for (a= 0; a< NUM; a++) { // Проверяем все колонки
                for (l = 1; l < NUM; l++) {
                    cnt = 0;
                    for (b = 0; b < NUM; b++) {
                        if (incoming[a][b] > 0) // Пропускаем заполненные ячейки
                            continue;

                        if (fit(a, b, l)) { // Если находим пустую ячейку, проверяем валдиность текущего значения
                            row = a;
                            col = b;
                            cnt++;
                        }

                        if (cnt > 1) { // Проверяем, что это единственная "точная" ячейка
                            notOnlyOne = true;  // Если несколько валидных значений, выходим
                            break;
                        }
                    }

                    if (cnt == 1) { // Если нашли единственное валидное значение, присваиваем его
                        incoming[row][col] = l;
                        setTag(row, col, l);  // Устанавливаем теги
                        value = true;  // Запоминаем, что выполнили присваиваение
                    }
                }
            }

            for (a= 0; a< NUM; a++) { // Проверяем все строки
                for (l = 1; l < NUM; l++) {
                    cnt = 0;
                    b = 0;
                    while (b < NUM) {
                        if (incoming[b][a] > 0) // Пропускаем заполненные ячейки
                        {
                            b++;
                            continue;
                        }

                        if (fit(b, a, l)) {  // Если находим пустую ячейку, проверяем валдиность текущего значения
                            row = b;
                            col = a;
                            cnt++;
                        }

                        if (cnt > 1) { // Проверяем, что это единственная "точная" ячейка
                            notOnlyOne = true;  // Если несколько валидных значений, выходим
                            break;
                        }
                        b++;
                    }

                    if (cnt == 1) { // Если нашли единственное валидное значение, присваиваем его
                        incoming[row][col] = l;
                        setTag(row, col, l); // Устанавливаем теги
                        value = true; // Запоминаем, что выполнили присваиваение
                    }
                }
            }
            if (!value) // Если не происвоили в процессе итерации, выходим из цикла
                break;
        }
    }

    private boolean bactward(int a, int b, int[][] mat) {
        if (a== NUM) { // Эта функция проверяет строчку за строчкой
            a= 0;
            if (++b == NUM) // Как только проверили строку, идем дальше
                return true; // Если достигли финальной ячейки, задача решена
        }

        if (mat[a][b] > 0) // Пропускаем заполненые ячейки
            return bactward(a+ 1, b, mat); // Проверяем следующую строку текущей колонки

        int val = 1;
        while (val <= NUM) { // Нашли пустую ячейку, проверяем с 1 до 9
            if (fit(a, b, val)) { // Если значение валидно, заполняем ячейку
                mat[a][b] = val;
                setTag(a, b, val); // Устанавливаем теги для проверки следующего значения
                if (bactward(a+ 1, b, mat)) // Вызываем функцию решения повторно
                    return true; // Если она возвращает true, значенит задача решена
                else // Если она возвращает false, задача не решена, сбрасываем теги и идем на один шаг назад
                    resetTag(a, b, mat[a][b]);
            }
            val++;
        }
        resetTag(a, b, mat[a][b]); // Сбрасываем теги и идем на один шаг назад
        mat[a][b] = 0; // Обнуляем значение для следующей итерации
        return false;
    }

}
