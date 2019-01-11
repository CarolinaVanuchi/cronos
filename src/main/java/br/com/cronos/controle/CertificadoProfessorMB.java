package br.com.cronos.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.AtividadeTurma;
import br.com.cronos.modelo.Certificado;
import br.com.cronos.modelo.GrupoTurma;
import br.com.cronos.modelo.Professor;
import br.com.cronos.util.CalculoEquivalencia;
import br.com.cronos.util.ExibirMensagem;
import br.com.cronos.util.FecharDialog;
import br.com.cronos.util.Mensagem;
import br.com.cronos.util.RecuperarRelacoesProfessor;
import br.com.cronos.util.ValidaTopoAtividade;

@SessionScoped
@ManagedBean
public class CertificadoProfessorMB {
	private Certificado certificado;
	private DAOGenerico dao;
	private UsuarioSessaoMB usuarioSessao;
	private List<Certificado> certificadosPendentes;
	private List<Certificado> certificadosInvalidos;
	private List<Certificado> certificadosValidados;
	private List<AtividadeTurma> atividadeTurmas;
	private RecuperarRelacoesProfessor relacoesProfessor;
	private Boolean calculaEquivalencia;
	private CalculoEquivalencia calculoEquivalencia;
	private Double horaCertificado;

	public CertificadoProfessorMB() {
		certificado = new Certificado();
		dao = new DAOGenerico();
		usuarioSessao = new UsuarioSessaoMB();
		certificadosPendentes = new ArrayList<>();
		certificadosInvalidos = new ArrayList<>();
		certificadosValidados = new ArrayList<>();
		atividadeTurmas = new ArrayList<>();
		calculoEquivalencia = new CalculoEquivalencia();
		horaCertificado = 0.0;
		preencherListaPendentes();
		preencherListaValidados();
		preencherListaInvalidos();
	}

	public void validar() {
		try {
			certificado.setSituacao(3);
			Professor professorRecuperarSesao = (Professor) usuarioSessao.recuperarProfessor();
			certificado.setValidadoProfessor(
					professorRecuperarSesao.getNome() + ", " + professorRecuperarSesao.getUsuario());
			dao.alterar(certificado);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogCertificadoValidarProfessor();
			preencherListaPendentes();
			preencherListaValidados();
			preencherListaInvalidos();
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		criarNovoCertificado();
	}

	public void invalidar() {
		try {
			certificado.setSituacao(4);
			Professor professorRecuperarSesao = (Professor) usuarioSessao.recuperarProfessor();
			certificado.setValidadoProfessor(
					professorRecuperarSesao.getNome() + ", " + professorRecuperarSesao.getUsuario());
			dao.alterar(certificado);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogCertificadoInvalidarProfessor();
			preencherListaPendentes();
			preencherListaValidados();
			preencherListaInvalidos();
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		criarNovoCertificado();
	}

	public void pender() {
		try {
			certificado.setSituacao(1);
			Professor professorRecuperarSesao = (Professor) usuarioSessao.recuperarProfessor();
			certificado.setValidadoProfessor(
					professorRecuperarSesao.getNome() + ", " + professorRecuperarSesao.getUsuario());
			dao.alterar(certificado);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogPendente();
			preencherListaPendentes();
			preencherListaValidados();
			preencherListaInvalidos();
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		criarNovoCertificado();
	}

	public void alterar() {
		if (certificado.getQuantidadeMaximaHora() <= 0) {
			ExibirMensagem.exibirMensagem(Mensagem.QUANTIDADE_HORAS);
		} else {
			if (!certificado.getDataInicio().after(certificado.getDataFinalizado())) {
				try {
					certificado.setDescricao(certificado.getDescricao().replace("'", "").replace("=", "")
							.replace("<", "").replace("&", ""));
					certificado.setInstituicao(certificado.getInstituicao().replace("'", "").replace("=", "")
							.replace("<", "").replace("&", ""));

					if (calculaEquivalencia) {
						certificado.setHoraComputada(calculoEquivalencia.calcularHorasCertificado(certificado));
					}

					dao.alterar(certificado);
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					FecharDialog.fecharDialogCertificado();
					preencherListaPendentes();
					preencherListaValidados();
					criarNovoCertificado();
				} catch (Exception e) {
					ExibirMensagem.exibirMensagem(Mensagem.ERRO);
				}
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.DATA_FINALIZAÇÃO);
			}
		}

	}

	private void preencherListaPendentes() {
		relacoesProfessor = new RecuperarRelacoesProfessor();
		certificadosPendentes = relacoesProfessor.recuperarCertificados(1);
	}

	private void preencherListaValidados() {
		relacoesProfessor = new RecuperarRelacoesProfessor();
		certificadosValidados = relacoesProfessor.recuperarCertificados(3);
	}

	private void preencherListaInvalidos() {
		relacoesProfessor = new RecuperarRelacoesProfessor();
		certificadosInvalidos = relacoesProfessor.recuperarCertificados(4);
	}

	public void preencherListaAtividadeTurma() {
		relacoesProfessor = new RecuperarRelacoesProfessor();
		atividadeTurmas = relacoesProfessor.recuperarAtividadeTurmasProfessor();
	}

	public List<AtividadeTurma> completarAtividadeTurma(String str) {
		preencherListaAtividadeTurma();
		List<AtividadeTurma> atividadeTurmaSelecionados = new ArrayList<>();
		for (AtividadeTurma at : atividadeTurmas) {
			if (at.getAtividade().getDescricao().toLowerCase().startsWith(str)) {
				atividadeTurmaSelecionados.add(at);
			}
		}
		return atividadeTurmaSelecionados;
	}

	public GrupoTurma recuperarGrupoTurma(AtividadeTurma atividadeTurma) {
		GrupoTurma grupoTurma = new GrupoTurma();
		grupoTurma = (GrupoTurma) dao
				.listar(GrupoTurma.class, " grupo = " + atividadeTurma.getAtividade().getGrupo().getId()
						+ " and turma = " + atividadeTurma.getTurma().getId())
				.get(0);
		return grupoTurma;
	}

	private void criarNovoCertificado() {
		certificado = new Certificado();
	}

	public Certificado getCertificado() {
		return certificado;
	}

	public void setCertificado(Certificado certificado) {
		this.certificado = certificado;
	}

	public List<Certificado> getCertificadosPendentes() {
		return certificadosPendentes;
	}

	public void setCertificadosPendentes(List<Certificado> certificadosPendentes) {
		this.certificadosPendentes = certificadosPendentes;
	}

	public List<Certificado> getCertificadosInvalidos() {
		return certificadosInvalidos;
	}

	public void setCertificadosInvalidos(List<Certificado> certificadosInvalidos) {
		this.certificadosInvalidos = certificadosInvalidos;
	}

	public List<Certificado> getCertificadosValidados() {
		return certificadosValidados;
	}

	public void setCertificadosValidados(List<Certificado> certificadosValidados) {
		this.certificadosValidados = certificadosValidados;
	}

	public Boolean getCalculaEquivalencia() {
		return calculaEquivalencia;
	}

	public void setCalculaEquivalencia(Boolean calculaEquivalencia) {
		this.calculaEquivalencia = calculaEquivalencia;
	}

	public Double getHoraCertificado() {
		return horaCertificado;
	}

	public void setHoraCertificado(Double horaCertificado) {
		this.horaCertificado = horaCertificado;
	}
}
