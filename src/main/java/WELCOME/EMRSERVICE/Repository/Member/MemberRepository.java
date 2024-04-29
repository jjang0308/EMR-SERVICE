package WELCOME.EMRSERVICE.Repository.Member;


import WELCOME.EMRSERVICE.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByPatientLoginId(String username);


}

