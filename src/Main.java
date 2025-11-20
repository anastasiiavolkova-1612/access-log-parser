import java.io.File;
import java.util.Scanner;

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
            }
            System.out.println("Путь указан верно");
            validPathsCount++;
            System.out.println("Это файл номер " + validPathsCount);
        }
    }
}