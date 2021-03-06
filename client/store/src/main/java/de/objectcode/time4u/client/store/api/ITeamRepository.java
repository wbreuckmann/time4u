package de.objectcode.time4u.client.store.api;

import java.util.List;

import de.objectcode.time4u.server.api.data.Team;
import de.objectcode.time4u.server.api.data.TeamSummary;
import de.objectcode.time4u.server.api.filter.TeamFilter;

/**
 * Client side team repository interface.
 * 
 * @author junglas
 */
public interface ITeamRepository
{
  /**
   * Get a team summary by its id.
   * 
   */
  TeamSummary getTeamSummary(String teamId) throws RepositoryException;

  /**
   * Get all teams matching a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return All teams matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<Team> getTeams(TeamFilter filter) throws RepositoryException;

  /**
   * Get all teams matching a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return All teams matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<TeamSummary> getTeamSummaries(TeamFilter filter) throws RepositoryException;

  /**
   * Store information of a team.
   * 
   * @param team
   *          The team to be stored
   * @param modifiedByOwner
   *          <tt>true</tt> If the modification is done by the repository owner (in UI this should always be
   *          <tt>true</tt>)
   * @return The stored person (including generated id for new persons)
   * @throws RepositoryException
   *           on error
   */
  void storeTeam(final Team team, final boolean modifiedByOwner) throws RepositoryException;
}
