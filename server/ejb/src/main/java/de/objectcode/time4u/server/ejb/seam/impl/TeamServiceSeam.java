package de.objectcode.time4u.server.ejb.seam.impl;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.RaiseEvent;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;

import de.objectcode.time4u.server.api.data.SynchronizableType;
import de.objectcode.time4u.server.ejb.seam.api.ITeamServiceLocal;
import de.objectcode.time4u.server.entities.TeamEntity;
import de.objectcode.time4u.server.entities.revision.ILocalIdGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;

@Stateless
@Local(ITeamServiceLocal.class)
@LocalBinding(jndiBinding = "time4u-server/seam/TeamServiceSeam/local")
@Name("TeamService")
@AutoCreate
@Scope(ScopeType.CONVERSATION)
public class TeamServiceSeam implements ITeamServiceLocal
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @EJB
  private IRevisionGenerator m_revisionGenerator;

  @EJB
  private ILocalIdGenerator m_idGenerator;

  @DataModel("admin.teamList")
  List<TeamEntity> m_teams;

  @SuppressWarnings("unchecked")
  @Factory("admin.teamList")
  @Observer("admin.teamList.updated")
  @RolesAllowed("user")
  public void initTeams()
  {
    final Query query = m_manager.createQuery("from " + TeamEntity.class.getName() + " t");

    m_teams = query.getResultList();
  }

  public TeamEntity getTeam(final String id)
  {
    return m_manager.find(TeamEntity.class, id);
  }

  @RaiseEvent("admin.teamList.updated")
  public void storeTeam(final TeamEntity teamEntity)
  {
    final IRevisionLock revisionLock = m_revisionGenerator.getNextRevision(SynchronizableType.TEAM, null);
    
    if ( teamEntity.getId() == null )
      teamEntity.setId(m_idGenerator.generateLocalId(SynchronizableType.TEAM));
    teamEntity.setRevision(revisionLock.getLatestRevision());
    teamEntity.setLastModifiedByClient(m_idGenerator.getClientId());
    m_manager.merge(teamEntity);
  }

}
