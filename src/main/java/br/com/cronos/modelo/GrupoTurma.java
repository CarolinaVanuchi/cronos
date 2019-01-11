package br.com.cronos.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Entity implementation class for Entity: GrupoTurma
 *
 */
@Entity
@Table(name = "tab_grupo_turma")
public class GrupoTurma implements Serializable {

	public GrupoTurma() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_grupo_turma")
	private Long id;

	@NotNull(message = "O campo minímo de horas não pode ser nulo")
	@Column(name = "minimo_horas", nullable = false)
	private Double minimoHoras;

	@NotNull(message = "O campo maxímo de horas não pode ser nulo")
	@Column(name = "maximo_horas", nullable = false)
	private Double maximoHoras;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, name = "data_cadastro")
	private Date dataCadastro;

	@Column(nullable = false)
	private Boolean status;

	@NotNull(message = "O campo grupo não pode ser nulo")
	@JoinColumn(name = "id_grupo", nullable = false)
	@ManyToOne
	private Grupo grupo;

	@NotNull(message = "O campo turma não pode ser nulo")
	@JoinColumn(name = "id_turma", nullable = false)
	@ManyToOne
	private Turma turma;

	public Long getId() {
		return id;
	}

	public Double getMinimoHoras() {
		return minimoHoras;
	}

	public void setMinimoHoras(Double minimoHoras) {
		this.minimoHoras = minimoHoras;
	}

	public Double getMaximoHoras() {
		return maximoHoras;
	}

	public void setMaximoHoras(Double maximoHoras) {
		this.maximoHoras = maximoHoras;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

}
