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
                    int googlebotCount = 0;
                    int yandexbotCount = 0;

                    while ((line = reader.readLine()) != null) {
                        lineCount++;
                        int length = line.length();

                        if (length > 1024) {
                            throw new RuntimeException("В файле найдена строка длиной более 1024 символов");
                        }
                        String[] parts = line.split("\"");
                        if (parts.length > 1) {
                            String userAgent = parts[parts.length - 1];

                            int firstBracket = userAgent.indexOf('(');
                            int lastBracket = userAgent.indexOf(')');

                            if (firstBracket != -1 && lastBracket != -1) {
                                String fragmentInBrackets = userAgent.substring(firstBracket + 1, lastBracket);
                                String[] fragments = fragmentInBrackets.split(";");

                                if (fragments.length >= 2) {
                                    String botFragment = fragments[1].trim();

                                    int slashIndex = botFragment.indexOf('/');
                                    String botName = "";
                                    if (slashIndex != -1) {
                                        botName = botFragment.substring(0, slashIndex);
                                    } else {
                                        botName = botFragment;
                                    }

                                    if (botName.equals("Googlebot")) {
                                        googlebotCount++;
                                    } else if (botName.equals("YandexBot")) {
                                        yandexbotCount++;
                                    }
                                }
                            }
                        }
                    }

                    System.out.println("Общее количество строк в файле: " + lineCount);
                    double totalRequests = lineCount;
                    double googleShare = (double) googlebotCount / totalRequests;
                    double yandexShare = (double) yandexbotCount / totalRequests;

                    System.out.println("Доля запросов от Googlebot: " + googleShare);
                    System.out.println("Доля запросов от YandexBot: " + yandexShare);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}