package de.objectcode.time4u.server.ejb.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;

import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.RoleEntity;
import de.objectcode.time4u.server.jaas.service.ILoginServiceLocal;
import de.objectcode.time4u.server.jaas.service.LoginLocal;
import de.objectcode.time4u.server.utils.DefaultPasswordEncoder;
import de.objectcode.time4u.server.utils.IPasswordEncoder;

@Stateless
@Local(ILoginServiceLocal.class)
@LocalBinding(jndiBinding = "time4u-server/LoginServiceBean/local")
public class LoginServiceImpl implements ILoginServiceLocal
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  /**
   * {@inheritDoc}
   */
  public LoginLocal findLogin(final String userId)
  {
    final Query query = m_manager.createQuery("from " + PersonEntity.class.getName() + " p where p.userId = :userId");

    query.setParameter("userId", userId);

    try {
      final PersonEntity person = (PersonEntity) query.getSingleResult();

      return new LoginLocal(person.getId(), person.getUserId(), person.getHashedPassword());
    } catch (final NoResultException e) {
      if ("admin".equals(userId)) {
        final PersonEntity person = initializeAdmin();

        return new LoginLocal(person.getId(), person.getUserId(), person.getHashedPassword());
      }

      return null;
    }
  }

  /**
   * Initialize the admin user in the database.
   */
  private PersonEntity initializeAdmin()
  {
    final IPasswordEncoder encoder = new DefaultPasswordEncoder();

    // TODO: Find a place for serverid
    final String personId = new UUID(1L, 1L).toString();
    final PersonEntity person = new PersonEntity(personId, "admin");

    person.setHashedPassword(encoder.encrypt("admin".toCharArray()));
    person.setName("admin");

    final Query roleQuery = m_manager.createQuery("from " + RoleEntity.class.getName() + " r where r.roleId = :roleId");

    final Set<RoleEntity> roles = new HashSet<RoleEntity>();

    try {
      roleQuery.setParameter("roleId", "user");

      roles.add((RoleEntity) roleQuery.getSingleResult());
    } catch (final NoResultException e) {
      final RoleEntity role = new RoleEntity();
      role.setRoleId("user");
      role.setName("Time4U User");

      m_manager.persist(role);

      roles.add(role);
    }

    try {
      roleQuery.setParameter("roleId", "admin");

      roles.add((RoleEntity) roleQuery.getSingleResult());
    } catch (final NoResultException e) {
      final RoleEntity role = new RoleEntity();
      role.setRoleId("admin");
      role.setName("Time4U Admin");

      m_manager.persist(role);
      roles.add(role);
    }

    person.setRoles(roles);

    m_manager.persist(person);

    return person;
  }
}
