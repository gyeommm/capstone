package capstoneDesign.houseSharing.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String zipcode;
    private String full_name;
    private String detail;

    public Address(String zipcode, String full_name, String detail) {
        this.full_name = full_name;
        this.zipcode = zipcode;
        this.detail = detail;
    }
}
