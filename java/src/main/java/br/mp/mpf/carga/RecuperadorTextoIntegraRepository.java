package br.mp.mpf.carga;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class RecuperadorTextoIntegraRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public ArquivoIntegra consultarArquivoIntegra(PecaPedidoColegiado peca) {
		StringBuilder select = new StringBuilder();
		select.append("SELECT i.nome_arquivo as \"nomeArquivo\", ");
		select.append("i.conteudo as \"conteudo\" ");
		select.append("FROM unico.INTEGRA i ");
		select.append("WHERE i.id_integra = :id ");

		Map<String, Object> params = new HashMap<>();
		params.put("id", peca.getIntegra());

		return namedParameterJdbcTemplate.queryForObject(select.toString(), params, new BeanPropertyRowMapper<ArquivoIntegra>(ArquivoIntegra.class));
	}

}
