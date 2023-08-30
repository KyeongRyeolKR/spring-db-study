package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    // 계좌이체 메서드
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            con.setAutoCommit(false); // 트랜잭션 시작

            // 비즈니스 로직
            // 같은 커넥션(세션)을 사용하기 위해 커넥션을 파라미터로 넘김
            bizLogic(con, fromId, toId, money);

            con.commit(); // 성공 시 커밋(트랜잭션 종료)
        } catch (Exception e) {
            con.rollback(); // 실패 시 롤백(트랜잭션 종료)
            throw new IllegalStateException(e);
        } finally {
            // 커넥션 사용 후 커넥션 정리
            release(con);
        }

    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);
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
