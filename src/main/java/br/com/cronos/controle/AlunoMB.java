package br.com.cronos.controle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.dao.DAOMovimentacaoAluno;
import br.com.cronos.modelo.Aluno;
import br.com.cronos.modelo.AlunoTurma;
import br.com.cronos.modelo.Movimentacao;
import br.com.cronos.modelo.Turma;
import br.com.cronos.util.AtualizaHorasCertificado;
import br.com.cronos.util.CriptografiaSenha;
import br.com.cronos.util.ExibirMensagem;
import br.com.cronos.util.FecharDialog;
import br.com.cronos.util.Mensagem;
import br.com.cronos.util.RecuperarRelacoesProfessor;
import br.com.cronos.util.ValidacoesGerirUsuarios;

@SessionScoped
@ManagedBean
public class AlunoMB {

	private Aluno aluno;
	private Movimentacao movimentacao;
	private List<Turma> turmas;
	private DAOGenerico dao;
	private DAOMovimentacaoAluno daoMovimentacaoAluno;
	private List<Movimentacao> alunosAtivos;
	private List<Movimentacao> alunosTrancados;
	private Movimentacao auxMovimentacao;
	private AlunoTurma alunoTurma;
	private ValidacoesGerirUsuarios validacoesGerirUsuarios;
	private List<Turma> turmasProfessor;
	private List<Movimentacao> alunosAtivosProfessor;
	private List<Movimentacao> alunosTrancadosProfessor;
	private RecuperarRelacoesProfessor relacoesProfessor;
	private AtualizaHorasCertificado atualizaHorasCertificado;

	public AlunoMB() {
		criarNovoObjetoAluno();
		dao = new DAOGenerico();
		daoMovimentacaoAluno = new DAOMovimentacaoAluno();
		movimentacao = new Movimentacao();
		turmasProfessor = new ArrayList<>();
		turmas = new ArrayList<>();
		alunosAtivos = new ArrayList<>();
		alunosTrancados = new ArrayList<>();
		auxMovimentacao = new Movimentacao();
		alunoTurma = new AlunoTurma();
		validacoesGerirUsuarios = new ValidacoesGerirUsuarios();
		alunosAtivosProfessor = new ArrayList<>();
		alunosTrancadosProfessor = new ArrayList<>();
		relacoesProfessor = new RecuperarRelacoesProfessor();
		atualizaHorasCertificado = new AtualizaHorasCertificado();
		atualizarListas();
	}

	public void salvar() {
		try {
			if (aluno.getId() == null) {
				if (validacoesGerirUsuarios.buscarUsuarios(aluno)) {
					ExibirMensagem.exibirMensagem(Mensagem.USUARIO);
				} else if (validacoesGerirUsuarios.buscarRA(aluno)) {
					ExibirMensagem.exibirMensagem(Mensagem.RA);
				} else {
					if ((alunoTurma.getDataMudanca() == null) || (alunoTurma.getDataMudanca().equals(""))) {
						ExibirMensagem.exibirMensagem(Mensagem.DATA_ALUNO_TURMA);
					} else if (alunoTurma.getDataMudanca().before(alunoTurma.getTurma().getDataInicioTurma())) {
						ExibirMensagem.exibirMensagem(Mensagem.DATA_MOVIMENTACAO);
					} else {						
						aluno.setDataCadastro(new Date());
						aluno.setStatus(true);
						aluno.setPerfil("ALUNO");
						if (aluno.getSenha().isEmpty()) {
							aluno.setSenha("123");
						}
						aluno.setSenha(CriptografiaSenha.criptografar(aluno.getSenha()));
						dao.inserir(aluno);

						alunoTurma.setAluno(aluno);
						alunoTurma.setStatus(true);
						alunoTurma.setMomentoMudanca(new Date());
						dao.inserir(alunoTurma);

						movimentacao.setDataMovimentacao(alunoTurma.getDataMudanca());
						movimentacao.setSituacao(1);
						movimentacao.setAluno(aluno);
						movimentacao.setStatus(true);
						dao.inserir(movimentacao);

						FecharDialog.fecharDialogAluno();
						FecharDialog.fecharDialogAluno();
						ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
						criarNovoObjetoAluno();
						atualizarListas();
					}
				}
			} else {
				if ((validacoesGerirUsuarios.buscarUsuarios(aluno))
						&& (validacoesGerirUsuarios.buscarUsuarioAlterar(aluno))) {
					ExibirMensagem.exibirMensagem(Mensagem.USUARIO);
				} else if ((validacoesGerirUsuarios.buscarRA(aluno))
						&& (validacoesGerirUsuarios.buscarRaAlterar(aluno))) {
					ExibirMensagem.exibirMensagem(Mensagem.RA);
				} else if (alunoTurma.getDataMudanca().before(alunoTurma.getTurma().getDataInicioTurma())) {
					ExibirMensagem.exibirMensagem(Mensagem.DATA_MOVIMENTACAO);
				} else {
					if (aluno.getSenha().isEmpty()) {
						aluno.setSenha("123");
					}
					aluno.setSenha(CriptografiaSenha.criptografar(aluno.getSenha()));
					dao.alterar(aluno);

					if (permitirCadastrarAlunoTurma(aluno).getTurma().getId() != alunoTurma.getTurma().getId()) {
						alunoTurma.setAluno(aluno);
						alunoTurma.setStatus(true);
						alunoTurma.setMomentoMudanca(new Date());
						dao.inserir(alunoTurma);
						atualizaHorasCertificado.alterarHoras(alunoTurma);
					}
					criarNovoObjetoAluno();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					FecharDialog.fecharDialogAluno();
					atualizarListas();
				}
			}

		} catch (

		Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}
	}

	public void inativar(Movimentacao movimentacao) {
		try {
			movimentacao.setSituacao(0);
			movimentacao.setDataMovimentacao(new Date());
			movimentacao.setStatus(false);
			movimentacao.getAluno().setStatus(false);
			dao.inserir(movimentacao);
			dao.alterar(movimentacao.getAluno());
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			inativarCertificados(movimentacao.getAluno().getId());
			inativarMovimentacoes(movimentacao.getAluno().getId());
			inativarAlunoTurma(movimentacao.getAluno().getId());
			atualizarListas();
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		criarNovoObjetoAluno();
	}

	// trancamentos, cancelamentos e abandonos
	public void inativarMovimentacoes() {
		try {
			if (((auxMovimentacao.getDataMovimentacao())
					.before(permitirCadastrarMovimentacao(movimentacao.getAluno()).getDataMovimentacao()))
					|| (auxMovimentacao.getDataMovimentacao())
							.equals(permitirCadastrarMovimentacao(movimentacao.getAluno()).getDataMovimentacao())) {
				ExibirMensagem.exibirMensagem(Mensagem.MOVIMENTACOES_ANTERIORES);
			} else {

				Date dataFinal = auxMovimentacao.getDataMovimentacao();
				Calendar calendarData = Calendar.getInstance();
				calendarData.setTime(dataFinal);
				calendarData.add(Calendar.DATE, -1);
				Date dataInicial = calendarData.getTime();

				Movimentacao mov = new Movimentacao();
				mov = (Movimentacao) daoMovimentacaoAluno.listarTodos(Movimentacao.class,
						" a.dataMovimentacao = (select max(b.dataMovimentacao) "
								+ " from Movimentacao b where b.aluno = a.aluno) "
								+ " and a.status is true and a.aluno.status = true and a.aluno = "
								+ movimentacao.getAluno().getId())
						.get(0);

				mov.setDataMovimentacaoFim(dataInicial);
				dao.alterar(mov);

				auxMovimentacao.setAluno(movimentacao.getAluno());
				auxMovimentacao.setStatus(true);
				dao.inserir(auxMovimentacao);
				FecharDialog.fecharDialogAlunoTrancamento();
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				auxMovimentacao = new Movimentacao();
				atualizarListas();
				criarNovoObjetoAluno();
			}
		} catch (Exception e) {
			System.out.println("trancar");
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}

	}

	public void ativar() {
		try {
			if (((auxMovimentacao.getDataMovimentacao())
					.before(permitirCadastrarMovimentacao(movimentacao.getAluno()).getDataMovimentacao()))
					|| (auxMovimentacao.getDataMovimentacao())
							.equals(permitirCadastrarMovimentacao(movimentacao.getAluno()).getDataMovimentacao())) {
				ExibirMensagem.exibirMensagem(Mensagem.MOVIMENTACOES_ANTERIORES);
			} else {
				Date dataFinal = auxMovimentacao.getDataMovimentacao();
				Calendar calendarData = Calendar.getInstance();
				calendarData.setTime(dataFinal);
				calendarData.add(Calendar.DATE, -1);
				Date dataInicial = calendarData.getTime();

				Movimentacao mov = new Movimentacao();
				mov = (Movimentacao) daoMovimentacaoAluno.listarTodos(Movimentacao.class,
						" a.dataMovimentacao = (select max(b.dataMovimentacao) "
								+ " from Movimentacao b where b.aluno = a.aluno) "
								+ " and a.status is true and a.aluno.status = true and a.aluno = "
								+ movimentacao.getAluno().getId())
						.get(0);

				mov.setDataMovimentacaoFim(dataInicial);
				dao.alterar(mov);

				auxMovimentacao.setAluno(movimentacao.getAluno());
				auxMovimentacao.setSituacao(1);
				auxMovimentacao.setStatus(true);
				dao.inserir(auxMovimentacao);
				FecharDialog.fecharDialogAlunoDestrancar();
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				auxMovimentacao = new Movimentacao();
				atualizarListas();
				criarNovoObjetoAluno();
			}
		} catch (Exception e) {
			System.err.println("Erro em destrancar");
			e.printStackTrace();

			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}

	}

	public List<Turma> completarTurma(String str) {
		preencherListaTurma();
		List<Turma> turmasSelecionadas = new ArrayList<>();
		for (Turma t : turmas) {
			if (t.getDescricao().startsWith(str)) {
				turmasSelecionadas.add(t);
			}
		}
		return turmasSelecionadas;
	}

	public void criarNovoObjetoAluno() {
		aluno = new Aluno();
		movimentacao = new Movimentacao();
		alunoTurma = new AlunoTurma();
	}

	public void preencherListaAlunos() {
		alunosAtivos = new ArrayList<>();
		alunosAtivos = daoMovimentacaoAluno.buscarAtivo();
	}

	public void preencherListaTrancados() {
		alunosTrancados = new ArrayList<>();
		alunosTrancados = daoMovimentacaoAluno.buscarTrancado();
	}

	public void preencherListaTurma() {
		turmas = new ArrayList<>();
		turmas = dao.listaComStatus(Turma.class);
	}

	public void atualizarListas() {
		preencherListaAlunos();
		preencherListaTrancados();
		preencherListaAlunosAtivosProfessor();
		preencherListaAlunosTrancadosProfessor();
	}

	public void inativarCertificados(Long id) {
		try {
			dao.update(" Certificado set status = false where aluno = " + id);
			ExibirMensagem.exibirMensagem(Mensagem.INATIVAR_CERTIFICADOS);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO_INATIVAR_CERTIFICADOS);
		}
	}

	public void inativarMovimentacoes(Long id) {
		try {
			dao.update(" Movimentacao set status = false where aluno = " + id);
			ExibirMensagem.exibirMensagem(Mensagem.INATIVAR_MOVIMENTACOES);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO_INATIVAR_MOVIMENTACOES);
		}
	}

	public void inativarAlunoTurma(Long id) {
		try {
			dao.update(" AlunoTurma set status = false where aluno = " + id);
			ExibirMensagem.exibirMensagem(Mensagem.INATIVAR_ALUNO_TURMA);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO_INATIVAR_ALUNO_TURMA);
		}
	}

	public Turma validarDataTurmaComAluno() {
		Turma turma = new Turma();
		turma = (Turma) dao.listar(Turma.class, " id = " + alunoTurma.getTurma().getId()).get(0);
		return turma;
	}

	public AlunoTurma permitirCadastrarAlunoTurma(Aluno aluno) {
		AlunoTurma alunoTurma = new AlunoTurma();
		alunoTurma = (AlunoTurma) daoMovimentacaoAluno.buscarMaiorAlunoTurma(aluno.getId()).get(0);
		return alunoTurma;
	}

	public Movimentacao permitirCadastrarMovimentacao(Aluno aluno) {
		Movimentacao mov = new Movimentacao();
		mov = (Movimentacao) daoMovimentacaoAluno.buscarMaiorMovimentacao(aluno.getId()).get(0);
		return mov;
	}

	public List<Turma> completarTurmaProfessor(String str) {
		preencherListaTurmaProfessor();
		List<Turma> turmasSelecionadas = new ArrayList<>();
		for (Turma t : turmasProfessor) {
			if (t.getDescricao().startsWith(str)) {
				turmasSelecionadas.add(t);
			}
		}
		return turmasSelecionadas;
	}

	public void preencherListaTurmaProfessor() {
		relacoesProfessor = new RecuperarRelacoesProfessor();
		turmasProfessor = relacoesProfessor.recuperarTurmasProfessor();
	}

	public void preencherListaAlunosAtivosProfessor() {
		alunosAtivosProfessor = new ArrayList<>();
		alunosAtivosProfessor = relacoesProfessor.recuperarAlunoMovimentacaoAtivo();
	}

	public void preencherListaAlunosTrancadosProfessor() {
		alunosTrancadosProfessor = new ArrayList<>();
		alunosTrancadosProfessor = relacoesProfessor.recuperarAlunoMovimentacaoTrancados();
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Movimentacao getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public List<Movimentacao> getAlunosAtivos() {
		return alunosAtivos;
	}

	public void setAlunosAtivos(List<Movimentacao> alunosAtivos) {
		this.alunosAtivos = alunosAtivos;
	}

	public List<Movimentacao> getAlunosTrancados() {
		return alunosTrancados;
	}

	public void setAlunosTrancados(List<Movimentacao> alunosTrancados) {
		this.alunosTrancados = alunosTrancados;
	}

	public AlunoTurma getAlunoTurma() {
		return alunoTurma;
	}

	public void setAlunoTurma(AlunoTurma alunoTurma) {
		this.alunoTurma = alunoTurma;
	}

	public Movimentacao getAuxMovimentacao() {
		return auxMovimentacao;
	}

	public void setAuxMovimentacao(Movimentacao auxMovimentacao) {
		this.auxMovimentacao = auxMovimentacao;
	}

	public List<Turma> getTurmasProfessor() {
		return turmasProfessor;
	}

	public void setTurmasProfessor(List<Turma> turmasProfessor) {
		this.turmasProfessor = turmasProfessor;
	}

	public List<Movimentacao> getAlunosAtivosProfessor() {
		return alunosAtivosProfessor;
	}

	public void setAlunosAtivosProfessor(List<Movimentacao> alunosAtivosProfessor) {
		this.alunosAtivosProfessor = alunosAtivosProfessor;
	}

	public List<Movimentacao> getAlunosTrancadosProfessor() {
		return alunosTrancadosProfessor;
	}

	public void setAlunosTrancadosProfessor(List<Movimentacao> alunosTrancadosProfessor) {
		this.alunosTrancadosProfessor = alunosTrancadosProfessor;
	}

}
