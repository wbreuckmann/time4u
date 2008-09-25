package de.objectcode.time4u.client.store.impl.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import de.objectcode.time4u.client.store.api.IWorkItemRepository;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.DayInfo;
import de.objectcode.time4u.server.api.data.DayInfoSummary;
import de.objectcode.time4u.server.api.data.WorkItem;
import de.objectcode.time4u.server.api.filter.DayInfoFilter;
import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;
import de.objectcode.time4u.server.entities.context.SessionPersistenceContext;
import de.objectcode.time4u.server.entities.revision.EntityType;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.SessionRevisionGenerator;

public class HibernateWorkItemRepository implements IWorkItemRepository
{
  private final HibernateRepository m_repository;
  private final HibernateTemplate m_hibernateTemplate;

  HibernateWorkItemRepository(final HibernateRepository repository, final HibernateTemplate hibernateTemplate)
  {
    m_repository = repository;
    m_hibernateTemplate = hibernateTemplate;
  }

  /**
   * {@inheritDoc}
   */
  public DayInfo getDayInfo(final CalendarDay day) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation<DayInfo>() {
      public DayInfo perform(final Session session)
      {
        final Criteria criteria = session.createCriteria(DayInfoEntity.class);
        criteria.add(Restrictions.eq("person.id", m_repository.getOwner().getId()));
        criteria.add(Restrictions.eq("date", day.getDate()));

        final DayInfoEntity entity = (DayInfoEntity) criteria.uniqueResult();

        if (entity == null) {
          return null;
        }

        final DayInfo dayInfo = new DayInfo();

        entity.toDTO(dayInfo);

        return dayInfo;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public List<DayInfoSummary> getDayInfoSummaries(final DayInfoFilter filter) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation<List<DayInfoSummary>>() {
      public List<DayInfoSummary> perform(final Session session)
      {
        final Criteria criteria = session.createCriteria(DayInfoEntity.class);
        criteria.add(Restrictions.eq("person.id", m_repository.getOwner().getId()));
        if (filter.getFrom() != null) {
          criteria.add(Restrictions.ge("date", filter.getFrom().getDate()));
        }
        if (filter.getTo() != null) {
          criteria.add(Restrictions.lt("date", filter.getTo().getDate()));
        }
        if (filter.getMinRevision() != null) {
          criteria.add(Restrictions.ge("revision", filter.getMinRevision()));
        }
        criteria.addOrder(Order.asc("date"));

        final List<?> result = criteria.list();
        final List<DayInfoSummary> dayInfos = new ArrayList<DayInfoSummary>();

        for (final Object row : result) {
          final DayInfoEntity entity = (DayInfoEntity) row;
          final DayInfoSummary dayInfo = new DayInfoSummary();

          entity.toSummaryDTO(dayInfo);
          dayInfos.add(dayInfo);
        }

        return dayInfos;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public WorkItem storeWorkItem(final WorkItem workItem) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation<WorkItem>() {
      public WorkItem perform(final Session session)
      {
        final IRevisionGenerator revisionGenerator = new SessionRevisionGenerator(session);

        final long nextRevision = revisionGenerator.getNextRevision(EntityType.WORKITEM, m_repository.getOwner()
            .getId());

        WorkItemEntity workItemEntity;

        if (workItem.getId() > 0L) {
          workItemEntity = (WorkItemEntity) session.get(WorkItemEntity.class, workItem.getId());

          workItemEntity.getDayInfo().setRevision(nextRevision);

          workItemEntity.fromDTO(new SessionPersistenceContext(session), workItem);

          session.flush();
        } else {
          final Criteria criteria = session.createCriteria(DayInfoEntity.class);
          criteria.add(Restrictions.eq("person.id", m_repository.getOwner().getId()));
          criteria.add(Restrictions.eq("date", workItem.getDay().getDate()));

          DayInfoEntity dayInfoEntity = (DayInfoEntity) criteria.uniqueResult();

          if (dayInfoEntity == null) {
            // This is save because the revision counter locks this section
            dayInfoEntity = new DayInfoEntity();
            dayInfoEntity.setDate(workItem.getDay().getDate());
            dayInfoEntity.setPerson((PersonEntity) session.get(PersonEntity.class, m_repository.getOwner().getId()));
            dayInfoEntity.setRevision(nextRevision);

            session.persist(dayInfoEntity);
          }
          workItemEntity = new WorkItemEntity();

          workItemEntity.setDayInfo(dayInfoEntity);
          workItemEntity.fromDTO(new SessionPersistenceContext(session), workItem);

          session.persist(workItemEntity);
          session.flush();
        }

        final WorkItem workItem = new WorkItem();

        workItemEntity.toDTO(workItem);

        return workItem;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public WorkItem getActiveWorkItem()
  {
    // TODO Auto-generated method stub
    return null;
  }

}
