import java.io.File;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int validPathsCount = 0;

        while (true) {
            System.out.println("Введите путь к файлу:");
            String path = scanner.nextLine();

            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isFile = file.isFile();

            if (!fileExists || !isFile) {
                System.out.println("Файл не существует или указан путь к папке");
                continue;
            } else {
                System.out.println("Путь указан верно");
                validPathsCount++;
                System.out.println("Это файл номер " + validPathsCount);

                try {
                    FileReader fileReader = new FileReader(path);
                    BufferedReader reader = new BufferedReader(fileReader);
                    String line;
                    int lineCount = 0;
                    int maxLength = 0;
                    int minLength = Integer.MAX_VALUE;

                    while ((line = reader.readLine()) != null) {
                        lineCount++;
                        int length = line.length();

                        if (length > 1024) {
                            throw new RuntimeException("В файле найдена строка длиной более 1024 символов");
                        }

                        if (length > maxLength) {
                            maxLength = length;
                        }

                        if (length < minLength) {
                            minLength = length;
                        }
                    }

                    System.out.println("Общее количество строк в файле: " + lineCount);
                    System.out.println("Длина самой длинной строки в файле: " + maxLength);
                    System.out.println("Длина самой короткой строки в файле: " + minLength);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}