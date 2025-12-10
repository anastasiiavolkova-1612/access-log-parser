import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LogEntry {
    private final String ipAddr;
    private final LocalDateTime time;
    private final HttpMethod method;
    private final String path;
    private final int responseCode;
    private final int responseSize;
    private final String referer;
    private final UserAgent agent;

    public LogEntry(String logLine) {
        int firstSpace = logLine.indexOf(' ');
        if (firstSpace == -1) throw new IllegalArgumentException("Неверный формат: отсутствует пробел после IP");
        this.ipAddr = logLine.substring(0, firstSpace);

        int startBracket = logLine.indexOf('[') + 1;
        int endBracket = logLine.indexOf(']', startBracket);
        if (startBracket == 0 || endBracket == -1) throw new IllegalArgumentException("Неверный формат: отсутствует дата в []");
        String dateTimeString = logLine.substring(startBracket, endBracket);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
        this.time = LocalDateTime.parse(dateTimeString, formatter);

        int firstQuote = logLine.indexOf('"', endBracket);
        int secondQuote = logLine.indexOf('"', firstQuote + 1);
        if (firstQuote == -1 || secondQuote == -1) throw new IllegalArgumentException("Неверный формат: отсутствуют \"\" для запроса");
        String requestString = logLine.substring(firstQuote + 1, secondQuote);
        String[] requestParts = requestString.split(" ");
        this.method = HttpMethod.valueOf(requestParts[0]);
        this.path = requestParts.length > 1 ? requestParts[1] : "";

        String afterRequest = logLine.substring(secondQuote + 1).trim();
        String[] responseParts = afterRequest.split(" ");
        if (responseParts.length < 2) throw new IllegalArgumentException("Неверный формат: отсутствует код ответа или размер");
        this.responseCode = Integer.parseInt(responseParts[0]);
        this.responseSize = Integer.parseInt(responseParts[1]);

        int thirdQuote = logLine.indexOf('"', secondQuote + 1);
        if (thirdQuote == -1) {
            this.referer = "-";
            this.agent = new UserAgent("-");
        } else {
            int fourthQuote = logLine.indexOf('"', thirdQuote + 1);
            if (fourthQuote == -1) throw new IllegalArgumentException("Неверный формат: не закрыта кавычка для referer");
            this.referer = logLine.substring(thirdQuote + 1, fourthQuote);

            int fifthQuote = logLine.indexOf('"', fourthQuote + 1);
            if (fifthQuote == -1) {
                this.agent = new UserAgent("-");
            } else {
                int sixthQuote = logLine.indexOf('"', fifthQuote + 1);
                if (sixthQuote == -1) throw new IllegalArgumentException("Неверный формат: не закрыта кавычка для User-Agent");
                String userAgentString = logLine.substring(fifthQuote + 1, sixthQuote);
                this.agent = new UserAgent(userAgentString);
            }
        }
        }


    public String getIpAddr() { return ipAddr; }
    public LocalDateTime getTime() { return time; }
    public HttpMethod getMethod() { return method; }
    public String getPath() { return path; }
    public int getResponseCode() { return responseCode; }
    public int getResponseSize() { return responseSize; }
    public String getReferer() { return referer; }
    public UserAgent getAgent() { return agent; }

    @Override
    public String toString() {
        return "--- Log ---\n" +
                "IP Адрес:    " + ipAddr + "\n" +
                "Timestamp:     " + time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) + "\n" +
                "HTTP Метод:   " + method + "\n" +
                "Path:          " + path + "\n" +
                "Response Code: " + responseCode + "\n" +
                "Response Size: " + responseSize + " bytes\n" +
                "Referer:       " + referer + "\n" +
                "User Agent:    " + agent.toString() + "\n" + // Вызываем toString() из UserAgent
                "-----------------";
    }
}