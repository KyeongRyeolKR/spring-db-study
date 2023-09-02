package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UncheckedAppTest {

    @Test
    void unchecked() {
        Controller controller = new Controller();

        assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Exception.class);
    }

    @Test
    void printEx() {
        Controller controller = new Controller();

        try {
            controller.request();
        } catch (Exception e) {
            log.info("ex", e);
        }
    }

    static class Controller {
        Service service = new Service();

        public void request() {
            service.logic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {
        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }
    }

    static class Repository {
        public void call() {
            try {
                runSQL();
            } catch (SQLException e) {
                // 체크 예외를 언체크 예외로 변환해서 넘김
                // 예외 변환 시, 기존 예외를 꼭! 포함해야한다.
                // 포함하지 않으면 대체 어디서 생긴 문제인지 알 방법이 없다.
                throw new RuntimeSQLException(e);
            }
        }

        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}
