cd target/helm/repo

$file = Get-ChildItem -Filter sdjpa-jdbc-template-v*.tgz | Select-Object -First 1
$APPLICATION_NAME = Get-ChildItem -Directory | Where-Object { $_.LastWriteTime -ge $file.LastWriteTime } | Select-Object -ExpandProperty Name

helm uninstall $APPLICATION_NAME --namespace sdjpa-jdbc-template