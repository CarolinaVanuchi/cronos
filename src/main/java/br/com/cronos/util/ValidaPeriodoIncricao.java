package br.com.cronos.util;

import java.util.ArrayList;
import java.util.List;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.Aluno;
import br.com.cronos.modelo.Certificado;
import br.com.cronos.modelo.Movimentacao;

public class ValidaPeriodoIncricao {
	private DAOGenerico dao = new DAOGenerico();
	private List<Movimentacao> movimentacoes = new ArrayList<>();

	private void recuperarMovimentacao(Aluno aluno) {
		try {
			movimentacoes = dao.listar(Movimentacao.class, " situacao = 1 and aluno = " + aluno.getId());
		} catch (Exception e) {
			System.err.println("Erro em recuperarMovimentacao");
			e.printStackTrace();
		}
	}

	public Boolean permitirCadastroCertificado(Certificado certificado) {
		recuperarMovimentacao(certificado.getAluno());
		Boolean retorno = true;
		try {
			for (int i = 0; i <= movimentacoes.size() - 1; i++) {
				if ((i == movimentacoes.size() - 1)) {
					if (((certificado.getDataInicio().after(movimentacoes.get(i).getDataMovimentacao()))
							|| (certificado.getDataInicio().equals(movimentacoes.get(i).getDataMovimentacao())))) {
						retorno = false;
						break;
					}
				} else {
					if (((certificado.getDataInicio().after(movimentacoes.get(i).getDataMovimentacao()))
							|| (certificado.getDataInicio().equals(movimentacoes.get(i).getDataMovimentacao())))
							&& ((certificado.getDataFinalizado().before(movimentacoes.get(i).getDataMovimentacaoFim()))
									|| (certificado.getDataFinalizado()
											.equals(movimentacoes.get(i).getDataMovimentacaoFim())))) {
						retorno = false;
						break;
					}

				}
			}
		} catch (Exception e) {
			System.err.println("Erro em permitirCadastroCertificado ");
			e.printStackTrace();
		}
		return retorno;
	}

}
