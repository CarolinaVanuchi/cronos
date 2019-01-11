package br.com.cronos.controle;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.cronos.dao.DAOFiltros;
import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.Aluno;
import br.com.cronos.modelo.AlunoTurma;
import br.com.cronos.modelo.AtividadeTurma;
import br.com.cronos.modelo.Certificado;
import br.com.cronos.modelo.GrupoTurma;
import br.com.cronos.util.CalculoEquivalencia;
import br.com.cronos.util.ExibirMensagem;
import br.com.cronos.util.FecharDialog;
import br.com.cronos.util.GeradorSenhas;
import br.com.cronos.util.Mensagem;
import br.com.cronos.util.ValidaPeriodoIncricao;
import br.com.cronos.util.ValidaTopoAtividade;
import br.com.cronos.util.ValidaTopoGrupo;

@SessionScoped
@ManagedBean
public class CertificadoMB {

	private Certificado certificado;
	private UsuarioSessaoMB usuarioSessao;
	private List<Certificado> certificadosAlunos;
	private List<AtividadeTurma> atividadeTurmas;
	private DAOGenerico dao;
	private DAOFiltros daoFiltros;
	private AlunoTurma alunoTurma;
	private CalculoEquivalencia calculoEquivalencia;
	private ValidaTopoGrupo validaTopoGrupo;
	private ValidaTopoAtividade validaTopoAtividade;
	private ValidaPeriodoIncricao validaPeriodoIncricao;
	private Boolean permitePDF;

	public CertificadoMB() {
		criarNovoObjetoCertificado();
		permitePDF = false;
		usuarioSessao = new UsuarioSessaoMB();
		certificadosAlunos = new ArrayList<>();
		atividadeTurmas = new ArrayList<>();
		dao = new DAOGenerico();
		calculoEquivalencia = new CalculoEquivalencia();
		alunoTurma = new AlunoTurma();
		daoFiltros = new DAOFiltros();
		preencherListaCertificadoAlunos();
		validaTopoGrupo = new ValidaTopoGrupo();
		validaTopoAtividade = new ValidaTopoAtividade();
		validaPeriodoIncricao = new ValidaPeriodoIncricao();
	}

	public void salvar() {
		try {
			if (certificado.getQuantidadeMaximaHora() <= 0) {
				ExibirMensagem.exibirMensagem(Mensagem.QUANTIDADE_HORAS);
			} else {
				if (!certificado.getDataInicio().after(certificado.getDataFinalizado())) {
					try {
						certificado.setAluno((Aluno) usuarioSessao.recuperarAluno());
						if (validaPeriodoIncricao.permitirCadastroCertificado(certificado)) {
							ExibirMensagem.exibirMensagem(Mensagem.PERIODO_CERTIFICADO_INVALIDO);
						} else {
							certificado.setDescricao(certificado.getDescricao().replace("'", "").replace("=", "")
									.replace("<", "").replace("&", ""));
							certificado.setInstituicao(certificado.getInstituicao().replace("'", "").replace("=", "")
									.replace("<", "").replace("&", ""));

							if (certificado.getId() == null) {
								certificado.setDataCadastro(new Date());
								certificado.setStatus(true);
								certificado.setSituacao(0);
								certificado.setAtualizado(true);
								if (!validaTopoAtividade.calcularTotalAtividade(certificado)) {
									ExibirMensagem.exibirMensagem(Mensagem.HORA_ATIVIDADE_MAXIMA);
								} else {
									if (!validaTopoGrupo.calcularTotalGrupo(certificado)) {
										ExibirMensagem.exibirMensagem(Mensagem.HORA_GRUPO_MAXIMO);
									} else {
										dao.inserir(certificado);
										certificado.setHoraComputada(
												calculoEquivalencia.calcularHorasCertificado(certificado));
										certificado.setIdGrupoTurma(
												recuperarGrupoTurma(certificado.getAtividadeTurma()).getId());
										dao.alterar(certificado);
										if (certificado.getCaminhoCertificado() != null) {

											String nomeCertificado = gerarNomeCertificado()
													+ GeradorSenhas.gerarSenha();
											Path origem = Paths.get(certificado.getCaminhoCertificado());
											String path = FacesContext.getCurrentInstance().getExternalContext()
													.getRealPath("");

											File diretorio = new File(path + "/certificadoUpload");

											if (!diretorio.exists()) {
												diretorio.mkdirs();
											}
											Path destino = Paths.get(
													diretorio.getCanonicalFile() + "\\" + nomeCertificado + ".pdf");

											Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
											certificado.setCaminhoCertificado(nomeCertificado);
											dao.alterar(certificado);
										}
										ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
										FecharDialog.fecharDialogCertificado();
										criarNovoObjetoCertificado();
										preencherListaCertificadoAlunos();
									}
								}
							} else {
								if (!validaTopoAtividade.calcularTotalAtividade(certificado)) {
									FecharDialog.fecharDialogCertificado();
									ExibirMensagem.exibirMensagem(Mensagem.HORA_ATIVIDADE_MAXIMA);
								} else {
									if (!validaTopoGrupo.calcularTotalGrupo(certificado)) {
										FecharDialog.fecharDialogCertificado();
										ExibirMensagem.exibirMensagem(Mensagem.HORA_GRUPO_MAXIMO);
									} else {
										certificado.setHoraComputada(
												calculoEquivalencia.calcularHorasCertificado(certificado));
										certificado.setIdGrupoTurma(
												recuperarGrupoTurma(certificado.getAtividadeTurma()).getId());
										certificado.setAtualizado(true);
										dao.alterar(certificado);
										if (certificado.getCaminhoCertificado() != null) {

											String nomeCertificado = gerarNomeCertificado()
													+ GeradorSenhas.gerarSenha();
											Path origem = Paths.get(certificado.getCaminhoCertificado());
											String path = FacesContext.getCurrentInstance().getExternalContext()
													.getRealPath("");

											System.out.println("teste  " + path);

											File diretorio = new File(path + "/certificadoUpload");

											if (!diretorio.exists()) {
												diretorio.mkdirs();
											}
											System.out.println("dir " + diretorio);
											Path destino = Paths.get(
													diretorio.getCanonicalFile() + "\\" + nomeCertificado + ".pdf");

											Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
											certificado.setCaminhoCertificado(nomeCertificado);
											dao.alterar(certificado);
										}
										ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
										criarNovoObjetoCertificado();
										FecharDialog.fecharDialogCertificado();
										preencherListaCertificadoAlunos();
									}
								}
							}
						}
					} catch (Exception e) {
						ExibirMensagem.exibirMensagem(Mensagem.ERRO);
						e.printStackTrace();
					}
				} else {
					ExibirMensagem.exibirMensagem(Mensagem.DATA_FINALIZAÇÃO);
				}
			}
		} catch (Exception e) {
			System.err.println("salvar() - CertificadoMB");
			e.printStackTrace();
		}
	}

	public void inativar(Certificado certificado) {
		try {
			certificado.setStatus(false);
			dao.alterar(certificado);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		preencherListaCertificadoAlunos();
		criarNovoObjetoCertificado();
	}

	public static String gerarNomeCertificado() {
		SimpleDateFormat momento = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return momento.format(new Date());
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

	public void upload(FileUploadEvent evento) {
		try {
			UploadedFile arquivoUpload = evento.getFile();
			if (!arquivoUpload.getFileName().isEmpty()) {
				Path arquivoTemp = Files.createTempFile(null, null);
				Files.copy(arquivoUpload.getInputstream(), arquivoTemp, StandardCopyOption.REPLACE_EXISTING);
				certificado.setCaminhoCertificado(arquivoTemp.toString());
				permitePDF = true;
			}
		} catch (Exception e) {
			System.err.println("Erro em: upload");
			e.printStackTrace();
		}
	}

	public void criarNovoObjetoCertificado() {
		permitePDF = false;
		certificado = new Certificado();
	}

	public void preencherListaCertificadoAlunos() {
		try {
			certificadosAlunos = daoFiltros.certificadosAlunos(usuarioSessao.recuperarAluno().getId(), 0);
		} catch (Exception e) {
			System.err.println("preencherListaCertificadoAlunos");
			e.printStackTrace();
		}
	}

	private Long recuperarTurmaAluno() {
		alunoTurma = (AlunoTurma) daoFiltros.buscarTurmaAluno(usuarioSessao.recuperarAluno().getId()).get(0);
		return alunoTurma.getTurma().getId();
	}

	public void preencherListaAtividadeTurma() {
		try {
			atividadeTurmas = daoFiltros.atividadesTurmaAluno(recuperarTurmaAluno());
		} catch (Exception e) {
			System.err.println("preencherListaAtividadeTurma");
			e.printStackTrace();
		}
	}

	public GrupoTurma recuperarGrupoTurma(AtividadeTurma atividadeTurma) {
		GrupoTurma grupoTurma = new GrupoTurma();
		try {
			grupoTurma = (GrupoTurma) dao
					.listar(GrupoTurma.class, " grupo = " + atividadeTurma.getAtividade().getGrupo().getId()
							+ " and turma = " + atividadeTurma.getTurma().getId())
					.get(0);
		} catch (Exception e) {
			System.err.println("recuperarGrupoTurma");
			e.printStackTrace();
		}
		return grupoTurma;
	}

	public void permitirPDF(Certificado certificado) {
		permitePDF = false;
		try {
			if ((certificado.getCaminhoCertificado() == null) || (certificado.getCaminhoCertificado().equals(""))) {
				permitePDF = false;
			} else {
				permitePDF = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Certificado getCertificado() {
		return certificado;
	}

	public void setCertificado(Certificado certificado) {
		this.certificado = certificado;
	}

	public List<AtividadeTurma> getAtividadeTurmas() {
		return atividadeTurmas;
	}

	public void setAtividadeTurmas(List<AtividadeTurma> atividadeTurmas) {
		this.atividadeTurmas = atividadeTurmas;
	}

	public List<Certificado> getCertificadosAlunos() {
		return certificadosAlunos;
	}

	public void setCertificadosAlunos(List<Certificado> certificadosAlunos) {
		this.certificadosAlunos = certificadosAlunos;
	}

	public Boolean getPermitePDF() {
		return permitePDF;
	}

	public void setPermitePDF(Boolean permitePDF) {
		this.permitePDF = permitePDF;
	}

}
