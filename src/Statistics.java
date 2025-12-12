import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private final Map<String, Integer> osCounts;
    private final Map<String, Integer> browserCounts;
    private final Map<HttpMethod, Integer> methodCounts;
    private final Map<String, Integer> errorCounts;
    private final Set<String> existingPages;


    public Statistics() {
        this.totalTraffic = 0L;
        this.minTime = null;
        this.maxTime = null;
        this.osCounts = new HashMap<>();
        this.browserCounts = new HashMap<>();
        this.methodCounts = new HashMap<>();
        this.errorCounts = new HashMap<>();
        this.existingPages = new HashSet<>();
    }

    public void addEntry(LogEntry entry) {
        this.totalTraffic += entry.getResponseSize();
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
        if (entry.getResponseCode() == 200) {
            existingPages.add(entry.getPath());
        }
    }
    public void addError(Exception e) {
        String errorMessage = e.getMessage();
        if (errorMessage == null) {
            errorMessage = e.getClass().getName();
        }
        errorCounts.put(errorMessage, errorCounts.getOrDefault(errorMessage, 0) + 1);
    }
    public long getTrafficRate() {
        if (minTime == null || maxTime == null) return 0L;
        Duration duration = Duration.between(minTime, maxTime);
        long hours = duration.toHours();
        if (hours == 0) return totalTraffic;
        return totalTraffic / hours;
    }
    public Set<String> getAllExistingPages() {
        return existingPages;
    }
    public Map<String, Double> getOsProportionStatistics() {
        Map<String, Double> osProportions = new HashMap<>();
        double totalOsCount = 0;

        for (int count : osCounts.values()) {
            totalOsCount += count;
        }

        if (totalOsCount == 0) {
            return osProportions;
        }

        for (Map.Entry<String, Integer> entry : osCounts.entrySet()) {
            double proportion = (double) entry.getValue() / totalOsCount;
            osProportions.put(entry.getKey(), proportion);
        }

        return osProportions;
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