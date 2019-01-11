package br.com.cronos.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Entity implementation class for Entity: AtividadeTurma
 *
 */
@Entity
@Table(name = "tab_atividade_turma")
public class AtividadeTurma implements Serializable {

	public AtividadeTurma() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_atividade_turma")
	private Long id;

	@NotNull(message = "O campo minímo de horas não pode ser nulo")
	@Column(name = "minimo_horas", nullable = false)
	private Double minimoHoras;

	@NotNull(message = "O campo maxímo de horas não pode ser nulo")
	@Column(name = "maximo_horas", nullable = false)
	private Double maximoHoras;

	@Column(name = "hora_unica")
	private Boolean horaUnica;

	private Double equivalencia;

	@Column(name = "equivalencia_hora")
	private Double equivalenciaHora;

	@Column(name = "quantidade_hora_unica")
	private Double quantidadeHoraUnica;

	@Column(nullable = false)
	private Boolean status;

	@NotNull(message = "O campo atividade não pode ser nulo")
	@JoinColumn(name = "id_atividade", nullable = false)
	@ManyToOne
	private Atividade atividade;

	@NotNull(message = "O campo turma não pode ser nulo")
	@JoinColumn(name = "id_turma", nullable = false)
	@ManyToOne
	private Turma turma;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, name = "data_cadastro")
	private Date dataCadastro;

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

	public Double getEquivalencia() {
		return equivalencia;
	}

	public void setEquivalencia(Double equivalencia) {
		this.equivalencia = equivalencia;
	}

	public Double getEquivalenciaHora() {
		return equivalenciaHora;
	}

	public void setEquivalenciaHora(Double equivalenciaHora) {
		this.equivalenciaHora = equivalenciaHora;
	}

	public Double getQuantidadeHoraUnica() {
		return quantidadeHoraUnica;
	}

	public void setQuantidadeHoraUnica(Double quantidadeHoraUnica) {
		this.quantidadeHoraUnica = quantidadeHoraUnica;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Boolean getHoraUnica() {
		return horaUnica;
	}

	public void setHoraUnica(Boolean horaUnica) {
		this.horaUnica = horaUnica;
	}

}
