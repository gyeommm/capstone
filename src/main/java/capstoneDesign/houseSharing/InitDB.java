package capstoneDesign.houseSharing;

import capstoneDesign.houseSharing.domain.Member;
import capstoneDesign.houseSharing.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class InitDB implements InitializingBean {

    private final MemberService memberService;

    @Override
    @Transactional
    public void afterPropertiesSet() throws Exception {
        Member member1 = Member.createMember("김상지", true,
                LocalDate.of(1998, 5, 13), "tkdwl", "12345");

        Member member2 = Member.createMember("정인겸", true,
                LocalDate.of(1998, 11, 17), "dlsrua", "12345");

        memberService.join(member1);
        memberService.join(member2);
    }
}
