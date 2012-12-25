package org.sqlproc.sample.simple.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlproc.engine.SqlControl;
import org.sqlproc.engine.SqlCrudEngine;
import org.sqlproc.engine.SqlEngineFactory;
import org.sqlproc.engine.SqlQueryEngine;
import org.sqlproc.engine.SqlSessionFactory;
import org.sqlproc.engine.impl.SqlStandardControl;
import org.sqlproc.sample.simple.model.Movie;
import org.sqlproc.sample.simple.model.NewBook;
import org.sqlproc.sample.simple.model.Person;

public class PersonDao1 {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private SqlEngineFactory sqlEngineFactory;
    private SqlSessionFactory sqlSessionFactory;

    public PersonDao1(SqlEngineFactory sqlEngineFactory, SqlSessionFactory sqlSessionFactory) {
        this.sqlEngineFactory = sqlEngineFactory;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public Person insert(Person person, SqlControl sqlControl) {
        if (logger.isTraceEnabled()) {
            logger.trace("insert person: " + person + " " + sqlControl);
        }
        SqlCrudEngine sqlInsertPerson = sqlEngineFactory.getCrudEngine("INSERT_PERSON");
        int count = sqlInsertPerson.insert(sqlSessionFactory.getSqlSession(), person);
        if (logger.isTraceEnabled()) {
            logger.trace("insert person result: " + count + " " + person);
        }
        return (count > 0) ? person : null;
    }

    public Person insert(Person person) {
        return insert(person, null);
    }

    public Person get(Person person, SqlControl sqlControl) {
        if (logger.isTraceEnabled()) {
            logger.trace("get get: " + person + " " + sqlControl);
        }
        SqlCrudEngine sqlEngine = sqlEngineFactory.getCrudEngine("GET_PERSON");
        sqlControl = getMoreResultClasses(person, sqlControl);
        Person personGot = sqlEngine.get(sqlSessionFactory.getSqlSession(), Person.class, person, sqlControl);
        if (logger.isTraceEnabled()) {
            logger.trace("get person result: " + personGot);
        }
        return personGot;
    }

    public Person get(Person person) {
        return get(person, null);
    }

    public int update(Person person, SqlControl sqlControl) {
        if (logger.isTraceEnabled()) {
            logger.trace("update person: " + person + " " + sqlControl);
        }
        SqlCrudEngine sqlEngine = sqlEngineFactory.getCrudEngine("UPDATE_PERSON");
        int count = sqlEngine.update(sqlSessionFactory.getSqlSession(), person);
        if (logger.isTraceEnabled()) {
            logger.trace("update person result count: " + count);
        }
        return count;
    }

    public int update(Person person) {
        return update(person, null);
    }

    public int delete(Person person, SqlControl sqlControl) {
        if (logger.isTraceEnabled()) {
            logger.trace("delete person: " + person + " " + sqlControl);
        }
        SqlCrudEngine sqlEngine = sqlEngineFactory.getCrudEngine("DELETE_PERSON");
        int count = sqlEngine.delete(sqlSessionFactory.getSqlSession(), person);
        if (logger.isTraceEnabled()) {
            logger.trace("delete person result count: " + count);
        }
        return count;
    }

    public int delete(Person person) {
        return delete(person, null);
    }

    public List<Person> list(Person person, SqlControl sqlControl) {
        if (logger.isTraceEnabled()) {
            logger.trace("list person: " + person + " " + sqlControl);
        }
        SqlQueryEngine sqlEngine = sqlEngineFactory.getQueryEngine("SELECT_PERSON");
        sqlControl = getMoreResultClasses(person, sqlControl);
        List<Person> personList = sqlEngine.query(sqlSessionFactory.getSqlSession(), Person.class, person, sqlControl);
        if (logger.isTraceEnabled()) {
            logger.trace("list person size: " + ((personList != null) ? personList.size() : "null"));
        }
        return personList;
    }

    public List<Person> list(Person person) {
        return list(person, null);
    }

    SqlControl getMoreResultClasses(Person person, SqlControl sqlControl) {
        if (sqlControl != null && sqlControl.getMoreResultClasses() != null)
            return sqlControl;
        Map<String, Class<?>> moreResultClasses = null;
        if (person != null && person.toInit(Person.Association.library)) {
            moreResultClasses = new HashMap<String, Class<?>>();
            moreResultClasses.put("movie", Movie.class);
            moreResultClasses.put("book", NewBook.class);
        }
        if (moreResultClasses != null) {
            sqlControl = new SqlStandardControl(sqlControl);
            ((SqlStandardControl) sqlControl).setMoreResultClasses(moreResultClasses);
        }
        return sqlControl;
    }
}