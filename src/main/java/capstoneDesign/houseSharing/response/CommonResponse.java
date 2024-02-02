package capstoneDesign.houseSharing.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonResponse {
    private boolean success;
    private String message;
}
