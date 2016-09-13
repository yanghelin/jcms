package com.jspxcms.core.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;

/**
 * WorkflowStep
 * 
 * @author liufang
 * 
 */
@Entity
@Table(name = "cms_workflow_step")
public class WorkflowStep implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	// @Transient
	// public Set<User> getUsers() {
	// List<Role> roles = getRoles();
	// if (CollectionUtils.isNotEmpty(roles)) {
	// Set<User> users = new HashSet<User>();
	// for (Role role : roles) {
	// for (User user : role.getUsers()) {
	// users.add(user);
	// }
	// }
	// return users;
	// } else {
	// return Collections.emptySet();
	// }
	// }

	@Transient
	public Role getRole() {
		List<Role> roles = getRoles();
		if (CollectionUtils.isNotEmpty(roles)) {
			return roles.iterator().next();
		} else {
			return null;
		}
	}

	@Transient
	public List<Role> getRoles() {
		List<WorkflowStepRole> stepRoles = getStepRoles();
		if (stepRoles == null) {
			return null;
		}
		List<Role> roles = new ArrayList<Role>(stepRoles.size());
		for (WorkflowStepRole stepRole : stepRoles) {
			roles.add(stepRole.getRole());
		}
		return roles;
	}

	@Transient
	public void applyDefaultValue() {
		if (getSeq() == null) {
			setSeq(Integer.MAX_VALUE);
		}
	}

	private Integer id;
	private List<WorkflowStepRole> stepRoles = new ArrayList<WorkflowStepRole>(
			0);

	private Workflow workflow;

	private String name;
	private Integer seq;

	@Id
	@Column(name = "f_workflowstep_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_workflow_step", pkColumnValue = "cms_workflow_step", table = "t_id_table", pkColumnName = "f_table", valueColumnName = "f_id_value", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_workflow_step")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "step")
	@OrderBy("roleIndex")
	public List<WorkflowStepRole> getStepRoles() {
		return stepRoles;
	}

	public void setStepRoles(List<WorkflowStepRole> stepRoles) {
		this.stepRoles = stepRoles;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_workflow_id", nullable = false)
	public Workflow getWorkflow() {
		return this.workflow;
	}

	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}

	@Column(name = "f_name", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "f_seq", nullable = false)
	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}
}
