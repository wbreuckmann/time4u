package de.objectcode.time4u.server.ejb.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import de.objectcode.time4u.server.api.ITeamService;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Team;
import de.objectcode.time4u.server.api.data.TeamSummary;
import de.objectcode.time4u.server.api.filter.TeamFilter;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;

@Stateless
@Remote(ITeamService.class)
@org.jboss.annotation.ejb.RemoteBinding(jndiBinding = "time4u-server/TeamService/remote")
@org.jboss.ejb3.annotation.RemoteBinding(jndiBinding = "time4u-server/TeamService/remote")
public class TeamServiceImpl implements ITeamService
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @Resource
  SessionContext m_sessionContext;

  /**
   * {@inheritDoc}
   */
  @RolesAllowed("user")
  public FilterResult<Team> getTeams(final TeamFilter filter)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_sessionContext.getCallerPrincipal()
        .getName());
    final Set<String> allowedTeamIds = new HashSet<String>();

    for (final TeamEntity team : userAccount.getPerson().getResponsibleFor()) {
      allowedTeamIds.add(team.getId());
    }
    for (final TeamEntity team : userAccount.getPerson().getMemberOf()) {
      allowedTeamIds.add(team.getId());
    }

    final Query query = createQuery(filter);
    final List<Team> result = new ArrayList<Team>();

    for (final Object row : query.getResultList()) {
      final TeamEntity teamEntity = (TeamEntity) row;

      if (allowedTeamIds.contains(teamEntity.getId())) {
        final Team team = new Team();

        teamEntity.toDTO(team);

        result.add(team);
      }
    }

    return new FilterResult<Team>(result);
  }

  /**
   * {@inheritDoc}
   */
  @RolesAllowed("user")
  public FilterResult<TeamSummary> getTeamSummaries(final TeamFilter filter)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_sessionContext.getCallerPrincipal()
        .getName());
    final Set<String> allowedTeamIds = new HashSet<String>();

    for (final TeamEntity team : userAccount.getPerson().getResponsibleFor()) {
      allowedTeamIds.add(team.getId());
    }
    for (final TeamEntity team : userAccount.getPerson().getMemberOf()) {
      allowedTeamIds.add(team.getId());
    }

    final Query query = createQuery(filter);
    final List<TeamSummary> result = new ArrayList<TeamSummary>();

    for (final Object row : query.getResultList()) {
      final TeamEntity teamEntity = (TeamEntity) row;

      if (allowedTeamIds.contains(teamEntity.getId())) {
        final TeamSummary team = new TeamSummary();

        teamEntity.toSummaryDTO(team);

        result.add(team);
      }
    }

    return new FilterResult<TeamSummary>(result);
  }

  /**
   * {@inheritDoc}
   */
  @RolesAllowed("user")
  public Team getTeam(final String teamId)
  {
    final TeamEntity teamEntity = m_manager.find(TeamEntity.class, teamId);

    if (teamEntity != null) {
      final Team team = new Team();

      teamEntity.toDTO(team);

      return team;
    }

    return null;
  }

  private Query createQuery(final TeamFilter filter)
  {
    String combineStr = " where ";
    final StringBuffer queryStr = new StringBuffer("from " + TeamEntity.class.getName() + " t");

    if (filter.getDeleted() != null) {
      queryStr.append(combineStr);
      queryStr.append("t.deleted = :deleted");
      combineStr = " and ";
    }

    if (filter.getMinRevision() != null) {
      queryStr.append(combineStr);
      queryStr.append("t.revision >= :minRevision");
      combineStr = " and ";
    }
    if (filter.getMaxRevision() != null) {
      queryStr.append(combineStr);
      queryStr.append("t.revision <= :maxRevision");
      combineStr = " and ";
    }
    if (filter.getLastModifiedByClient() != null) {
      queryStr.append(combineStr);
      queryStr.append("t.lastModifiedByClient = :lastModifiedByClient");
      combineStr = " and ";
    }

    switch (filter.getOrder()) {
      case ID:
        queryStr.append(" order by t.id asc");
        break;
      case NAME:
        queryStr.append(" order by t.name asc, t.id asc");
        break;
    }

    final Query query = m_manager.createQuery(queryStr.toString());

    if (filter.getDeleted() != null) {
      query.setParameter("deleted", filter.getDeleted());
    }
    if (filter.getMinRevision() != null) {
      query.setParameter("minRevision", filter.getMinRevision());
    }
    if (filter.getMaxRevision() != null) {
      query.setParameter("maxRevision", filter.getMaxRevision());
    }
    if (filter.getLastModifiedByClient() != null) {
      query.setParameter("lastModifiedByClient", filter.getLastModifiedByClient());
    }

    return query;
  }

}
