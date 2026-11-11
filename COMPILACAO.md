# Compilacao e verificacao

Prerequisitos: JDK 21, Android SDK compativel com os `compileSdk` declarados nos projetos,
licencas Android aceitas e acesso aos repositorios Maven. Use os Gradle Wrappers incluidos.
Configure `JAVA_HOME` e `ANDROID_HOME` localmente, ou informe o SDK em `local.properties` (ignorado pelo Git).
Dependencias nativas precompiladas sao obtidas do Maven indicado nos settings; os fontes e scripts estao em OwnTV_libmpv.

## Android TV: somente testes, sem APK

No PowerShell, a partir da raiz do repositorio:

```powershell
Set-Location .\OwnTV
.\gradlew.bat -Pandroid.onlyEnableUnitTestForTheTestedBuildType=false :app:testStandardReleaseUnitTest :app:compileStandardDebugAndroidTestKotlin
```

Os testes de interface precisam ser executados em uma TV ou emulador conectado para validar o comportamento real.

## Compilar APK (somente quando autorizado)

Antes de distribuir, defina um numero interno maior que o instalado (a revisao TV r7 usa 10009),
um nome de versao definitivo e o repositorio de atualizacoes da unityplayTV.
Nao use os valores de desenvolvimento `99999` / `99.99.99` para publicar.

```powershell
# Substitua os valores de exemplo pela versao aprovada.
$env:VERSION_CODE = '10010'
$env:VERSION_NAME = '5.1.1'
Set-Location .\OwnTV
.\gradlew.bat :app:assembleStandardRelease
```

Para atualizar instalacoes existentes, assine com a mesma chave unityplayTV ja utilizada.
Sem assinatura release configurada, o APK produzido nao deve ser distribuido como atualizacao.
O projeto aceita as variaveis KEYSTORE_FILE, KEYSTORE_PASSWORD, KEY_ALIAS e KEY_PASSWORD;
configure-as somente no ambiente local seguro, nunca em arquivos versionados, prints ou mensagens.
Mantenha uma copia segura da chave fora do repositorio. Nao trocar a chave para publicar.

Para celular, use o projeto `OwnTV_Mobile`; nao misture o APK mobile e o TV em uma mesma Release
enquanto o atualizador nao distinguir os arquivos por plataforma.
