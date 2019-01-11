package br.com.cronos.controle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.dao.DAOMovimentacaoAluno;
import br.com.cronos.modelo.AlunoTurma;
import br.com.cronos.modelo.Certificado;
import br.com.cronos.modelo.Movimentacao;
import br.com.cronos.modelo.Turma;
import br.com.cronos.util.ChamarRelatorio;
import br.com.cronos.util.ExibirMensagem;
import br.com.cronos.util.Mensagem;
import br.com.cronos.util.RecuperarRelacoesProfessor;
import br.com.cronos.util.VerificaSituacaoTurma;

@ViewScoped
@ManagedBean
public class RelatorioSecretariaMB {
	private DAOGenerico dao;
	private Turma turma;
	private DAOMovimentacaoAluno daoMovimentacaoAluno;
	private List<Turma> turmas;

	public RelatorioSecretariaMB() {
		dao = new DAOGenerico();
		daoMovimentacaoAluno = new DAOMovimentacaoAluno();
		turma = new Turma();
		preencherListaTurma();
	}

	public void imprimirCertificadoSituacao() {
		VerificaSituacaoTurma verificaSituacaoTurma = new VerificaSituacaoTurma();
		try {
			verificaSituacaoTurma.recuperarCertificados(turma);
			List<AlunoTurma> alunoTurmas = dao.listar(AlunoTurma.class, " aluno.id is not null ");
			if (!alunoTurmas.isEmpty()) {

				ChamarRelatorio ch = new ChamarRelatorio();
				HashMap parametro = new HashMap<>();
				parametro.put("TURMA", getTurma().getId());

				ch.imprimeRelatorio("situacao.jasper", parametro,
						"Relatório de situação da turma " + getTurma().getDescricao());
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void preencherListaTurma() {
		turmas = dao.listaComStatus(Turma.class);
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

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}
}
