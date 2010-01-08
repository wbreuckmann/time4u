package de.objectcode.time4u.server.web.gwt.login.server;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.entities.account.UserRoleEntity;

public class Time4UUserDetailsService extends JpaDaoSupport implements
		UserDetailsService {

	@Transactional(readOnly = false)
	public UserDetails loadUserByUsername(final String userId)
			throws UsernameNotFoundException, DataAccessException {
		return (UserDetails) getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager entityManager)
					throws PersistenceException {
				Query query = entityManager.createQuery("from "
						+ UserAccountEntity.class.getName()
						+ " a where a.userId = :userId");

				query.setParameter("userId", userId);

				UserAccountEntity account = (UserAccountEntity) query
						.getSingleResult();

				if (account != null) {
					return new Time4UUserDetails(account);
				}
				return null;
			}
		});
	}

	public static class Time4UUserDetails implements UserDetails {
		private static final long serialVersionUID = 1L;

		UserAccountEntity userAccountEntity;
		GrantedAuthority[] grantedAuthorities;

		public Time4UUserDetails(UserAccountEntity userAccountEntity) {
			this.userAccountEntity = userAccountEntity;
			this.grantedAuthorities = new GrantedAuthority[userAccountEntity
					.getRoles().size()];

			int i = 0;
			for (UserRoleEntity role : userAccountEntity.getRoles()) {
				this.grantedAuthorities[i++] = new GrantedAuthorityImpl("ROLE_" + role
						.getRoleId().toUpperCase());
			}
		}

		public GrantedAuthority[] getAuthorities() {
			return grantedAuthorities;
		}

		public String getPassword() {
			return userAccountEntity.getHashedPassword();
		}

		public String getUsername() {
			return userAccountEntity.getUserId();
		}

		public boolean isAccountNonExpired() {
			return true;
		}

		public boolean isAccountNonLocked() {
			return true;
		}

		public boolean isCredentialsNonExpired() {
			return true;
		}

		public boolean isEnabled() {
			return true;
		}
	}
}
