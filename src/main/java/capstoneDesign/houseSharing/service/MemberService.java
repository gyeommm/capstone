package capstoneDesign.houseSharing.service;

import capstoneDesign.houseSharing.domain.Member;
import capstoneDesign.houseSharing.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
        return memberRepository.save(member);
    }

    public Member findMember(Long id) {
        return memberRepository.findOne(id);
    }

    @Transactional
    public void withdraw(Long id) {
        memberRepository.deleteOne(id);
    }

    @Transactional
    public void updateAverageScore(Long id, int average) {
        Member member = memberRepository.findOne(id);
        member.setAverageScore(average);
    }
}
