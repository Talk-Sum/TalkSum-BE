package talksum.talksum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import talksum.talksum.domain.dto.MemberDto;
import talksum.talksum.domain.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //Member save(MemberDto member);

    //Optional<Member> findById(Long memberId);

    //void update(Long memberId, MemberDto updateParam);

    Optional<Member> findByLoginId(String loginId);

    //Long delete(Long memberId);
}
