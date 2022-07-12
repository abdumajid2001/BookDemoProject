package responce;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
public class AppErrorDto {
    private Timestamp timestamp;
    private String message;
    private String developerMessage;
    private String path;

    public AppErrorDto(String message, String developerMessage, String path) {
        this.timestamp = Timestamp.valueOf(LocalDateTime.now());
        this.message = message;
        this.developerMessage = developerMessage;
        this.path = path;
    }

    @Override
    public String toString() {
        return "{" +
                "timestamp=" + timestamp +
                ", message='" + message + '\'' +
                ", developerMessage='" + developerMessage + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
