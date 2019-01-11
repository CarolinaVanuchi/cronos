package br.com.cronos.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Entity implementation class for Entity: Certificado
 *
 */
@Entity
@Table(name = "tab_certificado")
public class Certificado implements Serializable {

	public Certificado() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_certificado")
	private Long id;

	@NotNull(message = "O campo descrição não pode ser nulo")
	@Column(nullable = false)
	private String descricao;

	@NotNull(message = "O campo data de início não pode ser nulo")
	@Column(name = "data_inicio", nullable = false)
	private Date dataInicio;

	@NotNull(message = "O campo data finalizado não pode ser nulo")
	@Column(name = "data_finalizado", nullable = false)
	private Date dataFinalizado;

	@NotNull(message = "O campo instituição não pode ser nulo")
	@Column(nullable = false)
	private String instituicao;

	@Column(name = "quantidade_maxima_hora")
	private Double quantidadeMaximaHora;

	@Column(nullable = false)
	private Integer situacao;

	@Column(name = "hora_computada")
	private Double horaComputada;

	private String justificativa;

	@Column(name = "justificativa_professor")
	private String justificativaProfessor;

	@Column(name = "caminho_certificado")
	private String caminhoCertificado;

	@Column(name = "data_cadastro", nullable = false)
	private Date dataCadastro;

	@Column(nullable = false)
	private Boolean status;

	@JoinColumn(name = "id_pessoa", nullable = false)
	@ManyToOne
	private Aluno aluno;

	@JoinColumn(name = "id_atividade_turma", nullable = false)
	@ManyToOne
	private AtividadeTurma atividadeTurma;

	@Column(name = "autenticado_secretaria")
	private String autenticadoSecretaria;

	@Column(name = "validado_professor")
	private String validadoProfessor;

	@Column(name = "motivo_alteracao")
	private String motivoAlteracao;

	private Boolean atualizado;

	// Adicionado para permitir que a exibição do grafico se torne mais rapida
	@Column(name = "id_grupo_turma")
	private Long idGrupoTurma;

	public Long getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFinalizado() {
		return dataFinalizado;
	}

	public void setDataFinalizado(Date dataFinalizado) {
		this.dataFinalizado = dataFinalizado;
	}

	public String getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}

	public Double getQuantidadeMaximaHora() {
		return quantidadeMaximaHora;
	}

	public void setQuantidadeMaximaHora(Double quantidadeMaximaHora) {
		this.quantidadeMaximaHora = quantidadeMaximaHora;
	}

	public Integer getSituacao() {
		return situacao;
	}

	public void setSituacao(Integer situacao) {
		this.situacao = situacao;
	}

	public Double getHoraComputada() {
		return horaComputada;
	}

	public void setHoraComputada(Double horaComputada) {
		this.horaComputada = horaComputada;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public String getCaminhoCertificado() {
		return caminhoCertificado;
	}

	public void setCaminhoCertificado(String caminhoCertificado) {
		this.caminhoCertificado = caminhoCertificado;
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

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public AtividadeTurma getAtividadeTurma() {
		return atividadeTurma;
	}

	public void setAtividadeTurma(AtividadeTurma atividadeTurma) {
		this.atividadeTurma = atividadeTurma;
	}

	public String getAutenticadoSecretaria() {
		return autenticadoSecretaria;
	}

	public void setAutenticadoSecretaria(String autenticadoSecretaria) {
		this.autenticadoSecretaria = autenticadoSecretaria;
	}

	public String getValidadoProfessor() {
		return validadoProfessor;
	}

	public void setValidadoProfessor(String validadoProfessor) {
		this.validadoProfessor = validadoProfessor;
	}

	public Long getIdGrupoTurma() {
		return idGrupoTurma;
	}

	public void setIdGrupoTurma(Long idGrupoTurma) {
		this.idGrupoTurma = idGrupoTurma;
	}

	public Boolean getAtualizado() {
		return atualizado;
	}

	public void setAtualizado(Boolean atualizado) {
		this.atualizado = atualizado;
	}

	public String getJustificativaProfessor() {
		return justificativaProfessor;
	}

	public void setJustificativaProfessor(String justificativaProfessor) {
		this.justificativaProfessor = justificativaProfessor;
	}

	public String getMotivoAlteracao() {
		return motivoAlteracao;
	}

	public void setMotivoAlteracao(String motivoAlteracao) {
		this.motivoAlteracao = motivoAlteracao;
	}
}
