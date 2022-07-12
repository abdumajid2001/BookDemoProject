package responce;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataDto<T> {
    private T data;

    private AppErrorDto appErrorDto;

    public DataDto(T data) {
        this.data = data;
    }

    public DataDto(AppErrorDto appErrorDto) {
        this.appErrorDto = appErrorDto;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                ", appErrorDto=" + appErrorDto +
                '}';
    }
}
