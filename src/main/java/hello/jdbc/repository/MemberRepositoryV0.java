package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, member.getMemberId()); // 1번째 ? 에 들어갈 값
            pstmt.setInt(2, member.getMoney()); // 2번째 ? 에 들어갈 값

            pstmt.executeUpdate(); // 실행

            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            // 예외가 발생하든 안하든 DB 사용이 끝나면 무조건 닫아주기! (리소스 누수 방지)
            // 역순으로 닫아주면 된다.
            close(con, pstmt, null);
        }
    }

    // 사용한 자원들을 닫아주는 메서드
    private void close(Connection con, Statement stmt, ResultSet rs) {
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if(stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if(con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
