package de.objectcode.time4u.migrator.server05.old.entities;
// Generated 06.10.2008 17:02:23 by Hibernate Tools 3.2.0.CR1


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * OldPersonsToTeamsId generated by hbm2java
 */
@Embeddable
public class OldPersonsToTeamsId  implements java.io.Serializable {

  private static final long serialVersionUID = 1L;
  
     private long personId;
     private long teamId;

    public OldPersonsToTeamsId() {
    }

    public OldPersonsToTeamsId(long personId, long teamId) {
       this.personId = personId;
       this.teamId = teamId;
    }
   

    @Column(name="person_id", nullable=false)
    public long getPersonId() {
        return this.personId;
    }
    
    public void setPersonId(long personId) {
        this.personId = personId;
    }

    @Column(name="team_id", nullable=false)
    public long getTeamId() {
        return this.teamId;
    }
    
    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof OldPersonsToTeamsId) ) return false;
		 OldPersonsToTeamsId castOther = ( OldPersonsToTeamsId ) other; 
         
		 return (this.getPersonId()==castOther.getPersonId())
 && (this.getTeamId()==castOther.getTeamId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + (int) this.getPersonId();
         result = 37 * result + (int) this.getTeamId();
         return result;
   }   


}


