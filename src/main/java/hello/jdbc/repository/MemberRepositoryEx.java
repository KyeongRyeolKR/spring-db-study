package hello.jdbc.repository;

import hello.jdbc.domain.Member;

import java.sql.SQLException;

/**
 * MemberRepository를 추상화해서 인터페이스를 만들었다.
 * 하지만 SQLException이라는 코드 때문에 JDBC에 의존하고 있다.
 * 인터페이스를 만든 이유가 구현체를 쉽게 변경하기 위함인데,
 * 이미 인터페이스가 특정 구현 기술에 의존하게 되어 버렸다.
 * 만약 이후에 JDBC가 아닌 다른 기술을 도입한다면 인터페이스 자체를 변경해야한다.
 *
 * 해결법 : 런타임 예외를 사용하면 따로 선언해주지 않아도 되기 때문에 특정 기술에 종속적일 필요가 없다.
 */
public interface MemberRepositoryEx {
    Member save(Member member) throws SQLException;
    Member findById(String memberId) throws SQLException;
    void update(String memberId, int money) throws SQLException;
    void delete(String memberId) throws SQLException;
}
