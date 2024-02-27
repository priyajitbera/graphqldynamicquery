package com.example.graphqldynamicquery.repository.impl;

import com.example.graphqldynamicquery.dto.graphqlquery.OrderType;
import com.example.graphqldynamicquery.dto.graphqlquery.PersonQuery;
import com.example.graphqldynamicquery.dto.graphqlquery.expression.impl.BooleanExpression;
import com.example.graphqldynamicquery.dto.graphqlquery.expression.impl.IntegerExpression;
import com.example.graphqldynamicquery.dto.graphqlquery.expression.impl.LocalDateExpression;
import com.example.graphqldynamicquery.dto.graphqlquery.expression.impl.StringExpression;
import com.example.graphqldynamicquery.entity.Person;
import com.example.graphqldynamicquery.model.Paginated;
import com.example.graphqldynamicquery.repository.PersonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class PersonRepositoryImpl implements PersonRepository {

    private final EntityManager entityManager;

    public PersonRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Paginated<Person> searchPerson(PersonQuery personQuery) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // fetch count & result
        long totalCount = fetchCount(personQuery, cb);
        List<Person> result = fetchResults(personQuery, cb);

        // pagination info
        int pageIndex = personQuery.getPageIndex();
        int pageSize = personQuery.getPageSize();

        // create response model and return
        long pageCount = totalCount / pageSize;
        long responsePageSize = Math.min(pageSize, result.size());
        return new Paginated<>(result, (long) pageIndex, responsePageSize, totalCount, pageCount);
    }

    private long fetchCount(PersonQuery personQuery, CriteriaBuilder cb) {

        CriteriaQuery<Long> cr = cb.createQuery(Long.class);
        Root<Person> root = cr.from(Person.class);

        // create predicates from PersonQuery
        Predicate[] predicates = createPredicates(personQuery, root, cb);


        // apply predicates and orders
        cr.select(cb.count(root))
                .where(predicates);

        // prepare typed queries
        TypedQuery<Long> countTypedQuery = entityManager.createQuery(cr);


        // execute count query
        log.info("Before executing count query");
        long totalCount = countTypedQuery.getSingleResult();
        log.info("After executing count query");

        return totalCount;
    }

    private List<Person> fetchResults(PersonQuery personQuery, CriteriaBuilder cb) {

        CriteriaQuery<Person> cr = cb.createQuery(Person.class);
        Root<Person> root = cr.from(Person.class);

        // create predicates from PersonQuery
        Predicate[] predicates = createPredicates(personQuery, root, cb);

        // create orders
        Order[] orders = createOrders(personQuery, root, cb);

        // apply predicates and orders
        cr.select(root)
                .where(predicates)
                .orderBy(orders);

        // pagination info
        int pageIndex = personQuery.getPageIndex();
        int pageSize = personQuery.getPageSize();

        // prepare typed queries
        TypedQuery<Person> typedQuery = entityManager.createQuery(cr)
                .setFirstResult(pageIndex * pageSize)
                .setMaxResults(pageSize);

        // execute result query
        log.info("Before executing result query");
        List<Person> result = typedQuery.getResultList();
        log.info("After executing result query");

        return result;
    }

    private Predicate[] createPredicates(PersonQuery personQuery, Root<Person> root, CriteriaBuilder cb) {

        if (personQuery.getAnd() != null) {
            Predicate predicate = createPredicateAndChain(personQuery.getAnd(), root, cb);
            return new Predicate[]{predicate};

        } else if (personQuery.getOr() != null) {
            Predicate predicate = createPredicateOrChain(personQuery.getOr(), root, cb);
            return new Predicate[]{predicate};

        } else {
            return new Predicate[]{}; // empty array i.e no filter criteria
        }
    }

    private Predicate createPredicateAndChain(List<PersonQuery.Filter> filters, Root<Person> root, CriteriaBuilder cb) {

        Predicate[] predicates = filters.stream().map(filter -> createPredicate(filter, root, cb))
                .toList()
                .toArray(new Predicate[]{});

        return cb.and(predicates);
    }

    private Predicate createPredicateOrChain(List<PersonQuery.Filter> filters, Root<Person> root, CriteriaBuilder cb) {

        Predicate[] predicates = filters.stream().map(filter -> createPredicate(filter, root, cb))
                .toList()
                .toArray(new Predicate[]{});

        return cb.or(predicates);
    }

    private Predicate createPredicate(PersonQuery.Filter filter, Root<Person> root, CriteriaBuilder cb) {

        // one PersonQuery.Filter object is expected to contain exactly one expression
        // i.e. one not null value

        Predicate predicate;
        // attribute Person.id
        if (filter.getId() != null) {
            predicate = createPredicate(filter.getId(), "id", root, cb);
        }
        // attribute Person.name
        else if (filter.getName() != null) {
            predicate = createPredicate(filter.getName(), "name", root, cb);
        }
        // attribute Person.dateOfBirth
        else if (filter.getDateOfBirth() != null) {
            predicate = createPredicate(filter.getDateOfBirth(), "dateOfBirth", root, cb);
        }
        // attribute Person.emailVerified
        else if (filter.getEmailVerified() != null) {
            predicate = createPredicate(filter.getEmailVerified(), "emailVerified", root, cb);
        }
        // attribute Person.creditScore
        else if (filter.getCreditScore() != null) {
            predicate = createPredicate(filter.getCreditScore(), "creditScore", root, cb);
        } else {
            throw new RuntimeException("No filter expression provided for any attribute");
        }
        if (filter.getAnd() != null) {
            return cb.and(predicate, createPredicate(filter.getAnd(), root, cb));
        }
        if (filter.getOr() != null) {
            return cb.or(predicate, createPredicate(filter.getOr(), root, cb));
        }
        return predicate;
    }

    private Predicate createPredicate(StringExpression expression, String attribute, Root<Person> root, CriteriaBuilder cb) {

        // one expression object is expected contain exacly one operation
        // i.e. exactly one non null value

        Path<String> path = root.get(attribute);

        // operator equals
        if (expression.getEquals() != null) {
            return cb.equal(path, expression.getEquals().getValue());
        }
        // operator notEquals
        else if (expression.getNotEquals() != null) {
            return cb.notEqual(path, expression.getNotEquals().getValue());
        }
        // operator in
        else if (expression.getIn() != null) {
            return path.in(expression.getIn().getValues());
        }
        // operator notIn
        else if (expression.getNotIn() != null) {
            return cb.not(path.in(expression.getNotIn().getValues()));
        }

        // operator isNull
        else if (expression.getIsNull() != null) {
            return cb.isNull(path);
        }

        // operator isNull
        else if (expression.getIsNotNull() != null) {
            return cb.isNotNull(path);
        }

        // operator like
        else if (expression.getLike() != null) {
            return cb.like(path, expression.getLike().getValue());
        }
        // operator notLike
        else if (expression.getNotLike() != null) {
            return cb.notLike(path, expression.getNotLike().getValue());
        }

        // if not operation provided
        throw new RuntimeException("No operation provided");
    }

    private Predicate createPredicate(BooleanExpression expression, String attribute, Root<Person> root, CriteriaBuilder cb) {

        // one expression object is expected contain exacly one operation
        // i.e. exactly one non null value

        Path<Boolean> path = root.get(attribute);
        // operator equals
        if (expression.getEquals() != null) {
            return cb.equal(path, expression.getEquals().getValue());
        }
        // operator notEquals
        else if (expression.getNotEquals() != null) {
            return path.in(expression.getNotEquals().getValue());
        }
        // operator in
        else if (expression.getIn() != null) {
            return path.in(expression.getIn().getValues());
        }
        // operator notIn
        else if (expression.getNotIn() != null) {
            return cb.not(path.in(expression.getNotIn().getValues()));
        }

        // operator isNull
        else if (expression.getIsNull() != null) {
            return cb.isNull(path);
        }

        // operator isNull
        else if (expression.getIsNotNull() != null) {
            return cb.isNotNull(path);
        }

        // if not operation provided
        throw new RuntimeException("No operation provided");
    }

    private Predicate createPredicate(IntegerExpression expression, String attribute, Root<Person> root, CriteriaBuilder cb) {

        // one expression object is expected contain exacly one operation
        // i.e. exactly one non null value

        Path<Integer> path = root.get(attribute);
        // operator equals
        if (expression.getEquals() != null) {
            return cb.equal(path, expression.getEquals().getValue());
        }
        // operator in
        else if (expression.getIn() != null) {
            return path.in(expression.getIn().getValues());
        }
        // operator greaterThan
        else if (expression.getGreaterThan() != null) {
            return cb.greaterThan(path, expression.getGreaterThan().getValue());
        }
        // operator greaterThanOrEqualTo
        else if (expression.getGreaterThanOrEqualTo() != null) {
            return cb.greaterThanOrEqualTo(path, expression.getGreaterThanOrEqualTo().getValue());
        }
        // operator lessThan
        else if (expression.getLessThan() != null) {
            return cb.lessThan(path, expression.getLessThan().getValue());
        }
        // operator lessThanOrEqualTo
        else if (expression.getLessThanOrEqualTo() != null) {
            return cb.lessThanOrEqualTo(path, expression.getLessThanOrEqualTo().getValue());
        }

        // if not operation provided
        throw new RuntimeException("No operation provided");
    }

    private Predicate createPredicate(LocalDateExpression expression, String attribute, Root<Person> root, CriteriaBuilder cb) {

        // one expression object is expected contain exacly one operation
        // i.e. exactly one non null value

        Path<LocalDate> path = root.get(attribute);
        // operator equals
        if (expression.getEquals() != null) {
            System.out.println(expression.getEquals().getValue().getClass().getName());
            System.out.println(expression.getEquals().getValue());
            return cb.equal(path, expression.getEquals().getValue());
        }
        // operator notEquals
        else if (expression.getNotEquals() != null) {
            System.out.println(expression.getNotEquals().getValue().getClass().getName());
            System.out.println(expression.getNotEquals().getValue());
            return cb.notEqual(path, expression.getNotEquals().getValue());
        }
        // operator in
        else if (expression.getIn() != null) {
            return path.in(expression.getIn().getValues());
        }
        // operator notIn
        else if (expression.getNotIn() != null) {
            return cb.not(path.in(expression.getNotIn().getValues()));
        }


        // operator greaterThan
        else if (expression.getGreaterThan() != null) {
            return cb.greaterThan(path, expression.getGreaterThan().getValue());
        }
        // operator greaterThanOrEqualTo
        else if (expression.getGreaterThanOrEqualTo() != null) {
            return cb.greaterThanOrEqualTo(path, expression.getGreaterThanOrEqualTo().getValue());
        }
        // operator lessThan
        else if (expression.getLessThan() != null) {
            return cb.lessThan(path, expression.getLessThan().getValue());
        }
        // operator lessThanOrEqualTo
        else if (expression.getLessThanOrEqualTo() != null) {
            return cb.lessThanOrEqualTo(path, expression.getLessThanOrEqualTo().getValue());
        }

        // operator isNull
        else if (expression.getIsNull() != null) {
            return cb.isNull(path);
        }

        // operator isNull
        else if (expression.getIsNotNull() != null) {
            return cb.isNotNull(path);
        }

        // if not operation provided
        throw new RuntimeException("No operation provided");
    }

    private Order[] createOrders(PersonQuery personQuery, Root<Person> root, CriteriaBuilder cb) {

        return personQuery.getOrders().stream()
                .map(order -> createOrder(order, root, cb))
                .toList().toArray(new Order[]{});
    }

    private Order createOrder(PersonQuery.Order order, Root<Person> root, CriteriaBuilder cb) {

        // one Order object contains info to order by any one Person class' attribute
        // i.e. exactly one non null value expected

        // attribute Person.id;
        if (order.getId() != null) {
            return createOrder("id", order.getId(), root, cb);
        }
        // attribute Person.name;
        else if (order.getName() != null) {
            return createOrder("name", order.getName(), root, cb);
        }
        // attribute Person.dateOfBirth;
        else if (order.getDateOfBirth() != null) {
            return createOrder("dateOfBirth", order.getDateOfBirth(), root, cb);
        }
        // attribute Person.emailVerified;
        else if (order.getEmailVerified() != null) {
            return createOrder("emailVerified", order.getEmailVerified(), root, cb);
        }
        // attribute Person.creditScore;
        else if (order.getCreditScore() != null) {
            return createOrder("creditScore", order.getCreditScore(), root, cb);
        }
        throw new RuntimeException("No order provided for any attribute");
    }

    private Order createOrder(String attribute, OrderType orderType, Root<Person> root, CriteriaBuilder cb) {

        if (orderType == null || OrderType.ASC.equals(orderType)) {
            return cb.asc(root.get(attribute));
        }
        // when OrderType.DESC
        else {
            return cb.desc(root.get(attribute));
        }
    }
}
