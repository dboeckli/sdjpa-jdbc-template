{{/*
Expand the name of the chart.
*/}}
{{- define "sdjpa-jdbc-template-mysql.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "sdjpa-jdbc-template-mysql.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "sdjpa-jdbc-template-mysql.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "sdjpa-jdbc-template-mysql.labels" -}}
helm.sh/chart: {{ include "sdjpa-jdbc-template-mysql.chart" . }}
{{ include "sdjpa-jdbc-template-mysql.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "sdjpa-jdbc-template-mysql.selectorLabels" -}}
app.kubernetes.io/name: {{ include "sdjpa-jdbc-template-mysql.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the FQDN for the service
*/}}
{{- define "sdjpa-jdbc-template-mysql.serviceFQDN" -}}
{{- $fullname := include "sdjpa-jdbc-template-mysql.fullname" . -}}
{{- printf "%s.%s.svc.cluster.local" $fullname .Release.Namespace }}
{{- end }}
