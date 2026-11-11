# Verificacao da copia para publicacao

- Fontes exportados da revisao TV testada; arquivos alterados em 05/11 comparados byte a byte com os fontes validados.
- 183 testes JVM e 28 verificacoes estruturais aprovados na revisao de origem.
- Testes Android de interface compilados; validacao em aparelho pendente.
- Excluidos APKs, caches, pastas de build, configuracoes locais, chaves e arquivos de senhas.
- Varredura por formatos comuns de tokens e chaves privadas sem ocorrencias nos arquivos exportados.
- Nenhum APK novo gerado e nenhuma publicacao remota executada nesta preparacao.

A varredura automatica complementa a revisao dos arquivos; nao comprova ausencia de todo segredo possivel.
