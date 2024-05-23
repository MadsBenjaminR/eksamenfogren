package app.services;

import app.persistence.ConnectionPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class PartsListCalcTest {

    private static final String USER = System.getenv("JDBC_USER");
    private static final String PASSWORD =  System.getenv("JDBC_PASSWORD");
    private static final String URL = System.getenv("JDBC_CONNECTION_STRING");
    private static final String DB = System.getenv("JDBC_DB");

    ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);



    @Test
    @DisplayName("Beregner antal stolper nødvendige hvis der maks må være 330 cm imellem")
    void calcPosts(){

        PartsListCalc partsListCalc = new PartsListCalc(340,400);



        //assertEquals(4, PartsListCalc.calcPoles(340));
    }

    @Test
    void calcBeams(){

        assertEquals(1, PartsListCalc.calcBeams(230, connectionPool));
    }

    @Test
    void calcBeamQuantity(){
        assertEquals(4,PartsListCalc.calcBeamQuantity(780, connectionPool) );
    }

}