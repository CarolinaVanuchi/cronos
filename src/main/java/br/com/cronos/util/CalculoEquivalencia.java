package br.com.cronos.util;

import br.com.cronos.modelo.Certificado;

public class CalculoEquivalencia {

	public Double calculo(Double horasMaxima, Double equivalencia1, Double equivalencia2) {
		Double horasCalculadas = 0.0;
		try {
			horasCalculadas = ((horasMaxima * equivalencia1) / equivalencia2);

		} catch (Exception e) {
			System.err.println("CalculoEquivalencia ");
			e.printStackTrace();
		}
		return horasCalculadas;
	}

	public Double calcularHorasCertificado(Certificado certificado) {
		Double horaComputada = 0.0;
		try {
			if ((certificado.getAtividadeTurma().getHoraUnica())
					&& (certificado.getAtividadeTurma().getQuantidadeHoraUnica() != null)
					&& (certificado.getAtividadeTurma().getQuantidadeHoraUnica() > 0)) {
				horaComputada = certificado.getAtividadeTurma().getQuantidadeHoraUnica();
			} else if ((!certificado.getAtividadeTurma().getHoraUnica())
					&& (certificado.getAtividadeTurma().getEquivalencia() != null)
					&& (certificado.getAtividadeTurma().getEquivalenciaHora() != null)
					&& (certificado.getAtividadeTurma().getEquivalencia() > 0
							&& certificado.getAtividadeTurma().getEquivalenciaHora() > 0)) {
				horaComputada = calculo(certificado.getQuantidadeMaximaHora(),
						certificado.getAtividadeTurma().getEquivalenciaHora(),
						certificado.getAtividadeTurma().getEquivalencia());
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.EQUIVALENCIA_NAO_CADASTRADA);
			}
		} catch (Exception e) {
			System.err.println("Erro calcularHorasCertificado ");
			e.printStackTrace();
		}
		return horaComputada;
	}

}
