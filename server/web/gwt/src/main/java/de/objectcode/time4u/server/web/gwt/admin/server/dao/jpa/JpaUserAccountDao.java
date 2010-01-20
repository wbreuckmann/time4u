package de.objectcode.time4u.server.web.gwt.admin.server.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.web.gwt.admin.client.service.PersonSummary;
import de.objectcode.time4u.server.web.gwt.admin.client.service.UserAccount;
import de.objectcode.time4u.server.web.gwt.admin.server.dao.IUserAccountDao;
import de.objectcode.time4u.server.web.gwt.utils.server.JpaDaoBase;

@Repository("adminUserAccountDao")
@Transactional(propagation = Propagation.MANDATORY)
public class JpaUserAccountDao extends JpaDaoBase implements IUserAccountDao {

	@SuppressWarnings("unchecked")
	public UserAccount.Page findUserAccountPage(int pageNumber, int pageSize,
			UserAccount.Projections sorting, boolean ascending) {
		Query countQuery = entityManager.createQuery("select count(*) from "
				+ UserAccountEntity.class.getName());

		long count = (Long) countQuery.getSingleResult();

		StringBuffer queryString = new StringBuffer("from ");
		queryString.append(UserAccountEntity.class.getName()).append(
				" as u left join fetch u.person");
		queryString.append(" order by");

		if (sorting.isSortable()) {
			queryString.append(" u.").append(sorting.getColumn()).append(
					ascending ? " asc" : " desc");
			if (sorting != UserAccount.Projections.USERID)
				queryString.append(", u.id asc");
		} else
			queryString.append(" u.id asc");

		Query dataQuery = entityManager.createQuery(queryString.toString());

		dataQuery.setFirstResult(pageNumber * pageSize);
		dataQuery.setMaxResults(pageSize);

		List<UserAccountEntity> result = dataQuery.getResultList();

		List<UserAccount> ret = new ArrayList<UserAccount>();

		for (UserAccountEntity userAccountEntity : result) {
			ret.add(toDTO(userAccountEntity));
		}

		return new UserAccount.Page(pageNumber, pageSize, (int) count, ret);
	}

	static PersonSummary toDTO(PersonEntity personEntity) {
		return new PersonSummary(personEntity.getId(),
				personEntity.getActive() == null || personEntity.getActive(),
				personEntity.getGivenName(), personEntity.getSurname(),
				personEntity.getEmail(), personEntity.getLastSynchronize());
	}

	static UserAccount toDTO(UserAccountEntity userAccountEntity) {
		return new UserAccount(userAccountEntity.getUserId(),
				toDTO(userAccountEntity.getPerson()), userAccountEntity
						.getLastLogin());
	}
}
