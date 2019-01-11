package br.com.cronos.modelo;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Entity implementation class for Entity: ProfessorCurso
 *
 */
@Entity
@Table(name = "tab_professor_curso")
public class ProfessorCurso implements Serializable {

	public ProfessorCurso() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_professor_curso")
	private Long id;

	@Column(nullable = false)
	private Boolean status;

	@NotNull(message = "O campo curso não pode ser nulo")
	@JoinColumn(name = "id_curso", nullable = false)
	@ManyToOne
	private Curso curso;

	@NotNull(message = "O campo professor não pode ser nulo")
	@JoinColumn(name = "id_pessoa", nullable = false)
	@ManyToOne
	private Professor professor;

	public Long getId() {
		return id;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

}
