import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private final Map<String, Integer> osCounts;
    private final Map<String, Integer> browserCounts;
    private final Map<HttpMethod, Integer> methodCounts;
    private final Map<String, Integer> errorCounts;


    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;
        this.osCounts = new HashMap<>();
        this.browserCounts = new HashMap<>();
        this.methodCounts = new HashMap<>();
        this.errorCounts = new HashMap<>();
    }

    public void addEntry(LogEntry entry) {
        this.totalTraffic += (int) entry.getResponseSize();
        LocalDateTime entryTime = entry.getTime();

        if (this.minTime == null || entryTime.isBefore(this.minTime)) {
            this.minTime = entryTime;
        }

        if (this.maxTime == null || entryTime.isAfter(this.maxTime)) {
            this.maxTime = entryTime;
        }
        UserAgent agent = entry.getAgent();

        String os = agent.getOsType();
        osCounts.put(os, osCounts.getOrDefault(os, 0) + 1);

        String browser = agent.getBrowser();
        browserCounts.put(browser, browserCounts.getOrDefault(browser, 0) + 1);

        HttpMethod method = entry.getMethod();
        methodCounts.put(method, methodCounts.getOrDefault(method, 0) + 1);
    }
    public void addError(Exception e) {
        String errorMessage = e.getMessage();
        if (errorMessage == null) {
            errorMessage = e.getClass().getName(); // Если сообщения нет, берем имя класса исключения
        }
        errorCounts.put(errorMessage, errorCounts.getOrDefault(errorMessage, 0) + 1);
    }
    public int getTrafficRate() {
        if (minTime == null || maxTime == null) {
            return 0;
        }

        Duration duration = Duration.between(minTime, maxTime);
        int hours = (int) duration.toHours();

        if (hours == 0) {
            return totalTraffic;
        }

        return totalTraffic / hours;
    }
    public Map<String, Integer> getOsCounts() {
        return osCounts;
    }
    public Map<String, Integer> getBrowserCounts() {
        return browserCounts;
    }
    public Map<HttpMethod, Integer> getMethodCounts() {
        return methodCounts;
}
    public Map<String, Integer> getErrorCounts() {
        return errorCounts;
    }
}