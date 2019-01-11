package br.com.cronos.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.jboss.logging.annotations.Message;

/**
 * Entity implementation class for Entity: Movimentacao
 *
 */
@Entity
@Table(name = "tab_movimentacao")
public class Movimentacao implements Serializable {

	public Movimentacao() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_movimentacao")
	private Long id;

	@Past(message = "Data movimentação deve ser igual ou menor a atual")
	@NotNull(message = "Data de movimentação não deve ser nula")
	@Column(nullable = false, name = "data_movimentacao")
	@Temporal(TemporalType.DATE)
	private Date dataMovimentacao;

	@Column(name = "data_movimentacao_fim")
	@Temporal(TemporalType.DATE)
	private Date dataMovimentacaoFim;

	@Column(nullable = false)
	private Integer situacao;

	@Column(nullable = false)
	private Boolean status;

	@NotNull(message = "O campo aluno não pode ser nulo")
	@ManyToOne
	@JoinColumn(name = "id_pessoa", nullable = false)
	private Aluno aluno;

	public Long getId() {
		return id;
	}

	public Date getDataMovimentacao() {
		return dataMovimentacao;
	}

	public void setDataMovimentacao(Date dataMovimentacao) {
		this.dataMovimentacao = dataMovimentacao;
	}

	public Integer getSituacao() {
		return situacao;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Date getDataMovimentacaoFim() {
		return dataMovimentacaoFim;
	}

	public void setDataMovimentacaoFim(Date dataMovimentacaoFim) {
		this.dataMovimentacaoFim = dataMovimentacaoFim;
	}

}
