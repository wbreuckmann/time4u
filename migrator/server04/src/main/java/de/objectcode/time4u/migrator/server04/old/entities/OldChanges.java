package de.objectcode.time4u.migrator.server04.old.entities;
// Generated 06.10.2008 17:02:23 by Hibernate Tools 3.2.0.CR1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * OldChanges generated by hbm2java
 */
@Entity
@Table(name="CHANGES"
)
public class OldChanges  implements java.io.Serializable {

  private static final long serialVersionUID = 1L;
  
     private Long id;
     private Long taskId;
     private long personId;
     private Long projectId;

    public OldChanges() {
    }

	
    public OldChanges(long personId) {
        this.personId = personId;
    }
    public OldChanges(Long taskId, long personId, Long projectId) {
       this.taskId = taskId;
       this.personId = personId;
       this.projectId = projectId;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="id", unique=true, nullable=false)
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    @Column(name="task_id")
    public Long getTaskId() {
        return this.taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
    @Column(name="person_id", nullable=false)
    public long getPersonId() {
        return this.personId;
    }
    
    public void setPersonId(long personId) {
        this.personId = personId;
    }
    
    @Column(name="project_id")
    public Long getProjectId() {
        return this.projectId;
    }
    
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }




}

