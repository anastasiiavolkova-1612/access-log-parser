import java.io.File;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.Set;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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
                System.out.println();
                try {
                    FileReader fileReader = new FileReader(path);
                    BufferedReader reader = new BufferedReader(fileReader);
                    String line;
                    int lineCount = 0;

                    Statistics statistics = new Statistics();
                    int lineNumber = 0;
                    int skippedLines = 0;
                    int successfulParseCount = 0;
                    boolean continueDetailedOutput = false;

                    while ((line = reader.readLine()) != null) {
                        lineCount++;
                        lineNumber++;
                        int length = line.length();
                        try {
                        if (length > 1024) {
                            throw new RuntimeException("В файле найдена строка длиной более 1024 символов");
                        }
                        LogEntry entry = new LogEntry(line);
                        statistics.addEntry(entry);
                        successfulParseCount++;

                            if (successfulParseCount <= 10) {
                                System.out.println();
                                System.out.println(entry);

                                if (successfulParseCount == 10) {
                                    System.out.println("\nВнимание: Выведены первые 10 строк.");
                                    System.out.println("Хотите продолжить подробный вывод для остальных строк или вывести общую статистику? (да/нет)");
                                    if (scanner.nextLine().equalsIgnoreCase("да")) {
                                        continueDetailedOutput = true;
                                    }
                                }
                            }
                            else if (continueDetailedOutput) {
                                System.out.println(entry);
                            }
                    } catch (Exception e) {
                            skippedLines++;
                            statistics.addError(e);
                        }}
                    long trafficRate = statistics.getTrafficRate();

                    System.out.println();
                    System.out.println("Средний объем трафика за час: " + trafficRate + " байт/час");
                    System.out.println();
                    System.out.println("Общее количество строк в файле = "+ lineCount);
                    if (skippedLines > 0) {
                        System.out.println("Количество некорректных строк: " + skippedLines + ", подробнее ниже");
                    }
                    Map<String, Integer> errorStats = statistics.getErrorCounts();
                    if (!errorStats.isEmpty()) {
                        System.out.println("Статистика по ошибкам:");
                        for (Map.Entry<String, Integer> entry : errorStats.entrySet()) {
                            System.out.println("- " + entry.getKey() + ": " + entry.getValue() + " раз");
                        }
                    }
                    System.out.println();
                    System.out.println("Статистика по методам запросов:");
                    Map<HttpMethod, Integer> methodStats = statistics.getMethodCounts();
                    for (Map.Entry<HttpMethod, Integer> entry : methodStats.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue() + " запросов");
                    }
                    System.out.println();
                    System.out.println("Статистика по браузерам:");
                    Map<String, Integer> browserStats = statistics.getBrowserCounts();
                    for (Map.Entry<String, Integer> entry : browserStats.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue() + " запросов");
                    }
                    System.out.println();
                    System.out.println("Статистика по операционным системам:");
                    Map<String, Integer> osStats = statistics.getOsCounts();
                    for (Map.Entry<String, Integer> entry : osStats.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue() + " запросов");
                    }
                    System.out.println();
                    System.out.println("Статистика по долям операционных систем:");
                    Map<String, Double> osProportions = statistics.getOsProportionStatistics();
                    for (Map.Entry<String, Double> entry : osProportions.entrySet()) {
                        System.out.printf("%s: %.2f%%%n", entry.getKey(), entry.getValue() * 100);
                    }

                    System.out.println();
                    System.out.println("Список существующих страниц (код 200):");
                    Set<String> pages = statistics.getAllExistingPages();
                    int pageCount = 0;
                    for (String page : pages) {
                        pageCount++;
                        System.out.println("- " + page);

                        if (pageCount == 10 && pages.size() > 10) {
                            System.out.println("... (показаны первые 10 из " + pages.size() + " страниц)");
                            System.out.println("Показать остальные? (да/нет)");
                            if (!scanner.nextLine().equalsIgnoreCase("да")) {
                                break;
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}