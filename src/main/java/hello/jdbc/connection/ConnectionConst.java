package hello.jdbc.connection;

// 상수를 정의해놓은 클래스이기 때문에 abstract로 객체 생성을 막음!
public abstract class ConnectionConst {

    public static final String URL = "jdbc:h2:tcp://localhost/~/test";
    public static final String USERNAME = "sa";
    public static final String PASSWORD = "";
}
