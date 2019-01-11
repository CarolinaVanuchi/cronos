package br.com.cronos.util;

import java.util.ArrayList;
import java.util.List;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.AlunoTurma;
import br.com.cronos.modelo.Atividade;
import br.com.cronos.modelo.AtividadeTurma;
import br.com.cronos.modelo.Certificado;

public class AtualizaHorasCertificado {

	private DAOGenerico dao;
	private List<Certificado> certificados;
	private List<Atividade> atividadesCertificados;
	private List<AtividadeTurma> atividadeTurmas;
	private CalculoEquivalencia calculoEquivalencia;

	public AtualizaHorasCertificado() {
		dao = new DAOGenerico();
		certificados = new ArrayList<>();
		atividadesCertificados = new ArrayList<>();
		atividadeTurmas = new ArrayList<>();
		calculoEquivalencia = new CalculoEquivalencia();
	}

	public void buscarCertificados(AlunoTurma alunoTurma) {
		try {
			certificados = new ArrayList<>();
			certificados = (List<Certificado>) dao.listar(Certificado.class,
					" aluno = " + alunoTurma.getAluno().getId());
		} catch (Exception e) {
			System.err.println("buscarCertificados");
			e.printStackTrace();
		}
	}

	public void verificarAtividades(AlunoTurma alunoTurma) {
		try {
			buscarCertificados(alunoTurma);
			for (int i = 0; i <= certificados.size() - 1; i++) {
				atividadesCertificados.add(certificados.get(i).getAtividadeTurma().getAtividade());
			}
		} catch (Exception e) {
			System.err.println("Erro em verificarAtividades");
			e.printStackTrace();
		}
	}

	public void buscarAtividades(AlunoTurma alunoTurma) {
		try {
			atividadeTurmas = new ArrayList<>();
			verificarAtividades(alunoTurma);

			for (int i = 0; i <= atividadesCertificados.size() - 1; i++) {
				atividadeTurmas.addAll(dao.listar(AtividadeTurma.class, " atividade = "
						+ atividadesCertificados.get(i).getId() + " and turma = " + alunoTurma.getTurma().getId()));

			}
		} catch (Exception e) {
			System.err.println("Erro em buscarAtividades:");
			e.printStackTrace();
		}
	}

	public void alterarHoras(AlunoTurma alunoTurma) {
		try {
			buscarCertificados(alunoTurma);
			if (certificados.size() > 0) {
				buscarAtividades(alunoTurma);
				buscarCertificados(alunoTurma);
				dao.update(" Certificado set atualizado = false where aluno = " + alunoTurma.getAluno().getId());

				for (int i = 0; i <= certificados.size() - 1; i++) {
					for (int c = 0; c <= atividadeTurmas.size() - 1; c++) {
						if (certificados.get(i).getAtividadeTurma().getAtividade().getId() == atividadeTurmas.get(c)
								.getAtividade().getId()) {

							dao.update(" Certificado set atividadeTurma = " + atividadeTurmas.get(c).getId()
									+ " where id = " + certificados.get(i).getId());

							certificados.get(i).setHoraComputada(
									calculoEquivalencia.calcularHorasCertificado(certificados.get(i)));
							dao.alterar(certificados.get(i));

							dao.update(" Certificado set atualizado = true where id = " + certificados.get(i).getId());
						}
					}
				}
				dao.update(" Certificado set status = false where aluno = " + alunoTurma.getAluno().getId()
						+ " and atualizado = false ");

			}
		} catch (

		Exception e) {
			System.err.println("Erro alterarHoras");
			e.printStackTrace();
		}
	}

}
