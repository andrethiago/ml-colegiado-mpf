package br.mp.mpf.carga;

import java.sql.Blob;

import org.apache.commons.io.FilenameUtils;

public class ArquivoIntegra {

	private String nomeArquivo;
	private Blob conteudo;

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public Blob getConteudo() {
		return conteudo;
	}

	public void setConteudo(Blob conteudo) {
		this.conteudo = conteudo;
	}

	public String getExtensao() {
		return FilenameUtils.getExtension(nomeArquivo);
	}

}
