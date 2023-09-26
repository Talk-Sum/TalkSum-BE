package talksum.talksum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import talksum.talksum.domain.dto.MemberDto;
import talksum.talksum.domain.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //Member save(MemberDto member);

    //Optional<Member> findById(Long memberId);

    //void update(Long memberId, MemberDto updateParam);

    Optional<Member> findByLoginId(String loginId);

    Optional<Member> findByName(String name);


    @Modifying
    @Query("UPDATE Member e SET e.name = :newName WHERE e.memberId = :memberId")
    int updateName(@Param("newName") String newName, @Param("memberId") Long memberId);

    //Long delete(Long memberId);
}
