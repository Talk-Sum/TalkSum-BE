package talksum.talksum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import talksum.talksum.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // CRUD impl

}
