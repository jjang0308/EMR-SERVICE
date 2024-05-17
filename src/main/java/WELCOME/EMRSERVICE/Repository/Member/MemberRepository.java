package WELCOME.EMRSERVICE.Repository.Member;


import WELCOME.EMRSERVICE.Domain.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
//     Optional<Member> findByPatientLoginId(String username);

     Member findByPatientLoginId(String username);


}

