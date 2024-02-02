package capstoneDesign.houseSharing;

import capstoneDesign.houseSharing.response.CommonResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> JoinSizeNotMatchException(MethodArgumentNotValidException e) {
        e.printStackTrace();

        return new ResponseEntity<>(new CommonResponse(false,
                e.getClass().getSimpleName() + " : 입력 사이즈가 안 맞거나, null이 들어온 경우"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CommonResponse> JoinDuplicateIdException(DataIntegrityViolationException e) {
        e.printStackTrace();

        return new ResponseEntity<>(new CommonResponse(false,
                e.getClass().getSimpleName() + " : 중복 오류"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse> JoinFormatNotReadException(HttpMessageNotReadableException e) {
        e.printStackTrace();

        return new ResponseEntity<>(new CommonResponse(false,
                e.getClass().getSimpleName() + " : request 포맷이 맞지 않는 경우"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> JoinETCException(Exception e) {
        e.printStackTrace();

        return new ResponseEntity<>(new CommonResponse(false,
                e.getClass().getSimpleName() +" : 기타 오류"),
                HttpStatus.BAD_REQUEST);
    }

}
