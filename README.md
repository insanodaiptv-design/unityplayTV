# unityplayTV

Player IPTV para Android TV e Android, personalizado a partir do OwnTV.
O aplicativo nao inclui canais, filmes, series ou assinaturas; as listas pertencem ao usuario.

## Estrutura

- `OwnTV/`: aplicativo Android TV (`com.unityplaytv.tv`).
- `OwnTV_Mobile/`: aplicativo Android para celular (`com.unityplaytv.mobile`).
- `OwnTV_Core/`: nucleo compartilhado, dados e reproducao.
- `OwnTV_libmpv/`: fontes do wrapper e scripts do motor nativo.
- `validation/`: verificacoes de identidade visual e integracao.

Os nomes internos OwnTV permanecem para preservar a estrutura e os creditos originais.

## Estado desta copia

Inclui as correcoes de capas, EPG do provedor e selecao de canais por OK, verificadas em 05/11/2026.
A revisao TV passou por 183 testes JVM e 28 verificacoes estruturais; testes Android de interface foram compilados.
A reproducao e o visual dessas correcoes ainda precisam de validacao em TV fisica.
Esta preparacao nao gerou nem publicou APK novo.

**Atualizacoes proprias ainda pendentes de configuracao:** o codigo atual consulta o repositorio do OwnTV original.
Antes do proximo APK destinado a distribuicao, configurar o repositorio da unityplayTV e testar a verificacao,
a selecao do APK Android TV, a assinatura e a comparacao das versoes. Publicar um arquivo sozinho nao altera apps ja instalados.

## Compilar e verificar

Consulte [COMPILACAO.md](COMPILACAO.md). Os quatro diretorios devem permanecer lado a lado.
Nao ha chave de assinatura, senhas de listas, SDK ou cache Gradle neste repositorio.
Os APKs assinados devem ser publicados em **Releases**, junto com o codigo correspondente a cada versao.

## Creditos e licencas

Derivado de [OwnTV](https://github.com/ahXN00/OwnTV),
[OwnTV Core](https://github.com/ahXN00/OwnTV_Core) e
[OwnTV libmpv](https://github.com/ahXN00/OwnTV_libmpv).
Consulte [ORIGENS.json](ORIGENS.json), [LICENSE](LICENSE) e as licencas nos subprojetos.
Os avisos de copyright e as licencas dos componentes foram preservados.
As alteracoes da unityplayTV estao identificadas em [ALTERACOES.md](ALTERACOES.md).
