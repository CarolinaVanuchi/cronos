package br.com.cronos.controle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.Certificado;
import br.com.cronos.modelo.GrupoTurma;
import br.com.cronos.modelo.Movimentacao;
import br.com.cronos.modelo.Turma;
import br.com.cronos.util.ChamarRelatorio;
import br.com.cronos.util.ExibirMensagem;
import br.com.cronos.util.Mensagem;
import br.com.cronos.util.RecuperarRelacoesProfessor;

@ViewScoped
@ManagedBean
public class RelatorioProfessorMB {
	private DAOGenerico dao;
	private Movimentacao aluno;
	private Turma turma;
	private List<Movimentacao> alunosAtivosProfessor;
	private RecuperarRelacoesProfessor relacoesProfessor;
	private List<Turma> turmas;
	private List<GrupoTurma> grupoTurmas;
	private GrupoTurma grupoTurma;

	public RelatorioProfessorMB() {
		dao = new DAOGenerico();
		relacoesProfessor = new RecuperarRelacoesProfessor();
		turma = new Turma();
		grupoTurma = new GrupoTurma();
		turmas = new ArrayList<>();
		grupoTurmas = new ArrayList<>();
		aluno = new Movimentacao();
	}

	public void imprimirCertificadoGrupo() {
		try {
			List<Certificado> certificados = dao.listar(Certificado.class,
					" situacao = 3 and aluno = " + getAluno().getAluno().getId());
			if (!certificados.isEmpty()) {
				Certificado cs = certificados.get(0);

				ChamarRelatorio ch = new ChamarRelatorio();
				HashMap parametro = new HashMap<>();
				parametro.put("ALUNO", getAluno().getAluno().getId());

				ch.imprimeRelatorio("grupo.jasper", parametro,
						"Relatório por grupo do aluno " + aluno.getAluno().getNome());
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void imprimirCertificadoGrupoTurma() {
		try {
			List<Certificado> certificados = dao.listar(Certificado.class, " situacao = 3 and idGrupoTurma = "
					+ getGrupoTurma().getGrupo().getId() + " and atividadeTurma.turma.id = " + turma.getId());
			if (!certificados.isEmpty()) {
				Certificado cs = certificados.get(0);

				ChamarRelatorio ch = new ChamarRelatorio();
				HashMap parametro = new HashMap<>();
				parametro.put("GRUPO", grupoTurma.getId());
				parametro.put("TURMA", turma.getId());

				ch.imprimeRelatorio("detalhado-turma.jasper", parametro, "Relatório detalhado turma "
						+ grupoTurma.getGrupo().getDescricao() + " " + turma.getDescricao());
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void imprimirGrupoAluno() {
		try {
			List<Certificado> certificados = dao.listar(Certificado.class, " situacao = 3 and aluno = "
					+ getAluno().getAluno().getId() + " and  idGrupoTurma = " + grupoTurma.getId());

			if (!certificados.isEmpty()) {
				Certificado cs = certificados.get(0);

				ChamarRelatorio ch = new ChamarRelatorio();
				HashMap parametro = new HashMap<>();
				parametro.put("ALUNO_GRUPO", getAluno().getAluno().getId());
				parametro.put("GRUPO_ALUNO", grupoTurma.getId());
				ch.imprimeRelatorio("detalhado-aluno.jasper", parametro,
						"Relatório por grupo do aluno " + getAluno().getAluno().getNome());
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}
	}

	public void preencherListaAlunosProfessor() {
		alunosAtivosProfessor = new ArrayList<>();
		alunosAtivosProfessor = relacoesProfessor.recuperarAlunoMovimentacaoAtivo();
	}

	public List<Movimentacao> completarMovimentacaoProfessor(String str) {
		preencherListaAlunosProfessor();
		List<Movimentacao> alunoSelecionado = new ArrayList<>();
		for (Movimentacao a : alunosAtivosProfessor) {
			if (a.getAluno().getNome().startsWith(str)) {
				alunoSelecionado.add(a);
			}
		}
		return alunoSelecionado;
	}

	public void preencherListaTurma() {
		relacoesProfessor = new RecuperarRelacoesProfessor();
		turmas = relacoesProfessor.recuperarTurmasProfessor();
	}

	public void preencherListaGrupoTurma() {
		relacoesProfessor = new RecuperarRelacoesProfessor();
		grupoTurmas = relacoesProfessor.recuperarGrupoTurmasProfessor();
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

	public List<GrupoTurma> completarGrupoTurma(String str) {
		preencherListaGrupoTurma();
		List<GrupoTurma> grupoTurmaSelecionado = new ArrayList<>();
		for (GrupoTurma g : grupoTurmas) {
			if (g.getGrupo().getDescricao().toLowerCase().startsWith(str)) {
				grupoTurmaSelecionado.add(g);
			}
		}
		return grupoTurmaSelecionado;
	}

	public Movimentacao getAluno() {
		return aluno;
	}

	public void setAluno(Movimentacao aluno) {
		this.aluno = aluno;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public GrupoTurma getGrupoTurma() {
		return grupoTurma;
	}

	public void setGrupoTurma(GrupoTurma grupoTurma) {
		this.grupoTurma = grupoTurma;
	}

}
