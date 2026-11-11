# Preparacao da proxima Release Android TV

Estado: pendente; nao publicar os APKs antigos como se contivessem as correcoes de 05/11.

1. Confirmar o endereco `USUARIO/unityplayTV` no GitHub.
2. Configurar e testar o atualizador para esse repositorio, substituindo a consulta ao OwnTV original.
3. Autorizar a geracao do APK com as correcoes, a mesma assinatura e versionCode maior que 10009.
4. Executar testes, instalar como atualizacao na TV e conferir capas, EPG, OK, tela cheia e PIN parental.
5. Criar commit/tag do codigo exato usado para compilar.
6. Abrir Releases > Draft a new release, selecionar a tag e anexar somente o APK Android TV assinado.
7. Informar as alteracoes e o SHA-256 do APK; salvar como draft ate conferir todos os arquivos.
8. Publicar a Release e validar aviso/download em uma instalacao de versao anterior que ja consulte esse repositorio.

Publicar o primeiro APK cria o canal de distribuicao. Os APKs atuais, que consultam OwnTV, nao passarao
a consultar unityplayTV so porque uma Release foi criada; sera preciso instalar a versao de transicao manualmente.
O GitHub fornece arquivos ZIP/tar dos fontes da tag. Manter tambem as instrucoes de compilacao e licencas.
