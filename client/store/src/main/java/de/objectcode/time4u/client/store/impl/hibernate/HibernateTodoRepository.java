package de.objectcode.time4u.client.store.impl.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import de.objectcode.time4u.client.store.api.ITodoRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.api.event.TodoRepositoryEvent;
import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.api.data.TodoGroup;
import de.objectcode.time4u.server.api.data.TodoSummary;
import de.objectcode.time4u.server.api.filter.TodoFilter;
import de.objectcode.time4u.server.entities.TodoAssignmentEntity;
import de.objectcode.time4u.server.entities.TodoBaseEntity;
import de.objectcode.time4u.server.entities.TodoEntity;
import de.objectcode.time4u.server.entities.TodoGroupEntity;
import de.objectcode.time4u.server.entities.context.SessionPersistenceContext;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;
import de.objectcode.time4u.server.entities.revision.SessionRevisionGenerator;

public class HibernateTodoRepository implements ITodoRepository
{
  private final HibernateRepository m_repository;
  private final HibernateTemplate m_hibernateTemplate;

  HibernateTodoRepository(final HibernateRepository repository, final HibernateTemplate hibernateTemplate)
  {
    m_repository = repository;
    m_hibernateTemplate = hibernateTemplate;
  }

  /**
   * {@inheritDoc}
   */
  public Todo getTodo(final String todoId) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<Todo>() {
      public Todo perform(final Session session)
      {
        final TodoEntity todoEntity = (TodoEntity) session.get(TodoEntity.class, todoId);

        if (todoEntity != null) {
          final Todo todo = new Todo();
          todoEntity.toDTO(todo);

          return todo;
        }
        return null;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public TodoGroup getTodoGroup(final String todoGroupId) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<TodoGroup>() {
      public TodoGroup perform(final Session session)
      {
        final TodoGroupEntity todoEntity = (TodoGroupEntity) session.get(TodoGroupEntity.class, todoGroupId);

        if (todoEntity != null) {
          final TodoGroup todoGroup = new TodoGroup();
          todoEntity.toDTO(todoGroup);

          return todoGroup;
        }
        return null;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public TodoSummary getTodoSummary(final String todoId) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<TodoSummary>() {
      public TodoSummary perform(final Session session)
      {
        final TodoBaseEntity todoEntity = (TodoBaseEntity) session.get(TodoBaseEntity.class, todoId);

        if (todoEntity != null) {
          final TodoSummary todo = new TodoSummary();

          todoEntity.toSummaryDTO(todo);

          return todo;
        }
        return null;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public List<TodoSummary> getTodos(final TodoFilter filter, final boolean onlyVisible) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<List<TodoSummary>>() {
      public List<TodoSummary> perform(final Session session)
      {
        final Criteria criteria = createTodoCriteria(session, filter, onlyVisible);
        final List<TodoSummary> result = new ArrayList<TodoSummary>();

        for (final Object row : criteria.list()) {
          if (row instanceof TodoEntity) {
            final Todo todo = new Todo();

            ((TodoEntity) row).toDTO(todo);

            result.add(todo);
          } else if (row instanceof TodoGroupEntity) {
            final TodoGroup todoGroup = new TodoGroup();

            ((TodoGroupEntity) row).toDTO(todoGroup);

            result.add(todoGroup);
          }
        }

        return result;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public List<TodoSummary> getTodoSummaries(final TodoFilter filter, final boolean onlyVisible)
      throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.OperationWithResult<List<TodoSummary>>() {
      public List<TodoSummary> perform(final Session session)
      {
        final Criteria criteria = createTodoCriteria(session, filter, onlyVisible);
        final List<TodoSummary> result = new ArrayList<TodoSummary>();

        for (final Object row : criteria.list()) {
          final TodoSummary todo = new TodoSummary();

          ((TodoBaseEntity) row).toSummaryDTO(todo);

          result.add(todo);
        }

        return result;
      }

    });
  }

  /**
   * {@inheritDoc}
   */
  public void storeTodo(final Todo todo, final boolean modifiedByOwner) throws RepositoryException
  {
    m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation() {
      public void perform(final Session session)
      {
        final IRevisionGenerator revisionGenerator = new SessionRevisionGenerator(session);
        final IRevisionLock revisionLock = revisionGenerator.getNextRevision(EntityType.TODO, null);

        if (todo.getId() == null) {
          todo.setId(m_repository.generateLocalId(EntityType.TODO));
          todo.setCreatedAt(new Date());
        }

        final TodoEntity todoEntity = new TodoEntity(todo.getId(), revisionLock.getLatestRevision(), m_repository
            .getClientId(), todo.getHeader());

        todoEntity.fromDTO(new SessionPersistenceContext(session), todo);
        if (modifiedByOwner) {
          todoEntity.setLastModifiedByClient(m_repository.getClientId());
        }

        session.merge(todoEntity);
        session.flush();

        todoEntity.toDTO(todo);
      }
    });

    m_repository.fireRepositoryEvent(new TodoRepositoryEvent(todo));
  }

  /**
   * {@inheritDoc}
   */
  public void storeTodoGroup(final TodoGroup todoGroup, final boolean modifiedByOwner) throws RepositoryException
  {
    m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation() {
      public void perform(final Session session)
      {
        final IRevisionGenerator revisionGenerator = new SessionRevisionGenerator(session);
        final IRevisionLock revisionLock = revisionGenerator.getNextRevision(EntityType.TODO, null);

        if (todoGroup.getId() == null) {
          todoGroup.setId(m_repository.generateLocalId(EntityType.TODO));
          todoGroup.setCreatedAt(new Date());
        }

        final TodoGroupEntity todoGroupEntity = new TodoGroupEntity(todoGroup.getId(),
            revisionLock.getLatestRevision(), m_repository.getClientId(), todoGroup.getHeader());

        todoGroupEntity.fromDTO(new SessionPersistenceContext(session), todoGroup);
        if (modifiedByOwner) {
          todoGroupEntity.setLastModifiedByClient(m_repository.getClientId());
        }

        session.merge(todoGroupEntity);
        session.flush();

        todoGroupEntity.toDTO(todoGroup);
      }
    });

    m_repository.fireRepositoryEvent(new TodoRepositoryEvent(todoGroup));
  }

  /**
   * {@inheritDoc}
   */
  public void deleteTodo(final TodoSummary todo) throws RepositoryException
  {
    final TodoSummary result = m_hibernateTemplate
        .executeInTransaction(new HibernateTemplate.OperationWithResult<TodoSummary>() {
          public TodoSummary perform(final Session session)
          {
            final TodoBaseEntity todoEntity = (TodoBaseEntity) session.get(TodoBaseEntity.class, todo.getId());

            if (todoEntity == null) {
              return null;
            }

            final IRevisionGenerator revisionGenerator = new SessionRevisionGenerator(session);
            final IRevisionLock revisionLock = revisionGenerator.getNextRevision(EntityType.TODO, null);

            todoEntity.setDeleted(true);
            todoEntity.setRevision(revisionLock.getLatestRevision());
            todoEntity.setLastModifiedByClient(m_repository.getClientId());

            session.flush();

            final TodoSummary result = new TodoSummary();

            todoEntity.toSummaryDTO(result);

            return result;
          }
        });

    if (result != null) {
      m_repository.fireRepositoryEvent(new TodoRepositoryEvent(result));
    }
  }

  private Criteria createTodoCriteria(final Session session, final TodoFilter filter, final boolean onlyVisible)
  {
    Criteria criteria;
    if (filter.getGroup() != null) {
      if (filter.getGroup()) {
        criteria = session.createCriteria(TodoGroupEntity.class, "t");
      } else {
        criteria = session.createCriteria(TodoEntity.class, "t");
      }
    } else {
      criteria = session.createCriteria(TodoBaseEntity.class, "t");
    }

    if (filter.getDeleted() != null) {
      criteria.add(Restrictions.eq("deleted", filter.getDeleted()));
    }
    if (filter.getMinRevision() != null) {
      criteria.add(Restrictions.ge("revision", filter.getMinRevision()));
    }
    if (filter.getMaxRevision() != null) {
      criteria.add(Restrictions.le("revision", filter.getMaxRevision()));
    }
    if (filter.getLastModifiedByClient() != null) {
      criteria.add(Restrictions.eq("lastModifiedByClient", filter.getLastModifiedByClient()));
    }
    if (filter.getGroupId() != null) {
      if (filter.getGroupId().equals("")) {
        criteria.add(Restrictions.isNull("group"));
      } else {
        criteria.add(Restrictions.eq("group.id", filter.getGroupId()));
      }
    }
    if (filter.getTaskId() != null) {
      if (filter.getTaskId().equals("")) {
        criteria.add(Restrictions.isNull("task"));
      } else {
        criteria.add(Restrictions.eq("task.id", filter.getTaskId()));
      }
    }
    if (filter.getTodoStates() != null && filter.getTodoStates().length > 0) {
      criteria.add(Restrictions.in("state", filter.getTodoStates()));
    }
    if (filter.getCompletedAtGe() != null) {
      criteria.add(Restrictions.or(Restrictions.isNull("completedAt"), Restrictions.ge("completedAt", filter
          .getCompletedAtGe())));
    }
    if (filter.getCreatedAtGe() != null) {
      criteria.add(Restrictions.ge("createdAt", filter.getCreatedAtGe()));
    }
    if (filter.getAssignmentFilter() != null) {
      final Disjunction disjunction = Restrictions.disjunction();

      disjunction.add(Restrictions.eq("class", TodoGroupEntity.class));
      if (filter.getAssignmentFilter().isUnassigned()) {
        final DetachedCriteria assignments = DetachedCriteria.forClass(TodoAssignmentEntity.class, "a1");
        assignments.setProjection(Projections.rowCount());
        assignments.add(Restrictions.eqProperty("a1.todoId", "t.id"));

        disjunction.add(Subqueries.eq(0, assignments));
      }
      if (filter.getAssignmentFilter().isAssignedToPerson()) {
        final DetachedCriteria assignments = DetachedCriteria.forClass(TodoAssignmentEntity.class, "a2");
        assignments.setProjection(Projections.rowCount());
        assignments.add(Restrictions.eqProperty("a2.todoId", "t.id"));
        assignments.add(Restrictions.eq("a2.personId", filter.getAssignmentFilter().getPersonId()));

        disjunction.add(Subqueries.lt(0, assignments));
      }
      if (filter.getAssignmentFilter().isAssignedToOther()) {
        final DetachedCriteria assignments = DetachedCriteria.forClass(TodoAssignmentEntity.class, "a3");
        assignments.add(Restrictions.eqProperty("a3.todoId", "t.id"));
        assignments.add(Restrictions.ne("a3.personId", filter.getAssignmentFilter().getPersonId()));
        assignments.setProjection(Projections.rowCount());

        disjunction.add(Subqueries.lt(0, assignments));
      }
      criteria.add(disjunction);
    }
    if (onlyVisible) {
      criteria.createAlias("t.visibleToPersons", "vperson", Criteria.LEFT_JOIN);
      criteria.createAlias("t.visibleToTeams", "vteam", Criteria.LEFT_JOIN);
      criteria.createAlias("vteam.members", "vmember", Criteria.LEFT_JOIN);
      criteria.add(Restrictions.disjunction().add(Restrictions.eq("t.reporter.id", m_repository.getOwner().getId()))
          .add(Restrictions.eq("vperson.id", m_repository.getOwner().getId())).add(
              Restrictions.eq("vmember.id", m_repository.getOwner().getId())));
      criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    }

    switch (filter.getOrder()) {
      case ID:
        criteria.addOrder(Order.asc("id"));
        break;
      case HEADER:
        criteria.addOrder(Order.asc("header"));
        criteria.addOrder(Order.asc("id"));
        break;
    }
    return criteria;
  }
}
