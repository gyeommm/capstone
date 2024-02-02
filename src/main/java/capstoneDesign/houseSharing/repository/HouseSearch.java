package capstoneDesign.houseSharing.repository;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class HouseSearch {          // 주거 검색 조건
    private LocalDateTime start;
    private LocalDateTime end;
}
