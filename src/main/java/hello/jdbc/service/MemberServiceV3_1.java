package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 매니저
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {

    private final PlatformTransactionManager transactionManager;
    private final MemberRepositoryV3 memberRepository;

    // 계좌이체 메서드
    public void accountTransfer(String fromId, String toId, int money) {
        // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // 비즈니스 로직
            bizLogic(fromId, toId, money);

            transactionManager.commit(status); // 성공 시 커밋(트랜잭션 종료)
        } catch (Exception e) {
            transactionManager.rollback(status); // 실패 시 롤백(트랜잭션 종료)
            throw new IllegalStateException(e);
        }

    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void release(Connection con) {
        if(con != null) {
            try {
                // 커넥션 풀에서는 사용이 끝난 커넥션을 반납하는 방식이기 때문에
                // 옵션을 원래대로 돌려놓고 반납해야한다.
                con.setAutoCommit(true);
                con.close();
            } catch (Exception e) {
                log.info("error", e);
            }
        }
    }

    // 예외 상황을 만들기 위한 메서드
    private void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
