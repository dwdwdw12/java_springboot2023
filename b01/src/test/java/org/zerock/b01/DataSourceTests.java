package org.zerock.b01;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
@Log4j2
public class DataSourceTests {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testConnection() throws SQLException{

        @Cleanup        //db 연결 후, 작업이 끝나면 연결을 끊음(자원 반납)
        Connection con = dataSource.getConnection();

        log.info("Connection>>>"+con);

        Assertions.assertNotNull(con);  //null 값이면 테스트 실패

    }

}
