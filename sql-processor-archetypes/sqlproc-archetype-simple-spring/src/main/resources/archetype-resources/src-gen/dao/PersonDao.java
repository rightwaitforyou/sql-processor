#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.dao;

import java.util.List;
import org.sqlproc.engine.SqlSession;
import org.sqlproc.engine.SqlControl;
import ${package}.model.Person;

public interface PersonDao {
  
  public Person insert(SqlSession sqlSession, Person person, SqlControl sqlControl);
  
  public Person insert(Person person, SqlControl sqlControl);
  
  public Person insert(SqlSession sqlSession, Person person);
  
  public Person insert(Person person);
  
  public Person get(SqlSession sqlSession, Person person, SqlControl sqlControl);
  	
  public Person get(Person person, SqlControl sqlControl);
  	
  public Person get(SqlSession sqlSession, Person person);
  	
  public Person get(Person person);
  
  public int update(SqlSession sqlSession, Person person, SqlControl sqlControl);
  
  public int update(Person person, SqlControl sqlControl);
  
  public int update(SqlSession sqlSession, Person person);
  
  public int update(Person person);
  
  public int delete(SqlSession sqlSession, Person person, SqlControl sqlControl);
  
  public int delete(Person person, SqlControl sqlControl);
  
  public int delete(SqlSession sqlSession, Person person);
  
  public int delete(Person person);
  
  public List<Person> list(SqlSession sqlSession, Person person, SqlControl sqlControl);
  
  public List<Person> list(Person person, SqlControl sqlControl);
  
  public List<Person> list(SqlSession sqlSession, Person person);
  
  public List<Person> list(Person person);
}
