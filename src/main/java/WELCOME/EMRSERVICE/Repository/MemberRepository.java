package WELCOME.EMRSERVICE.Repository;


import WELCOME.EMRSERVICE.Domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByPatientLoginId(String username);
}

