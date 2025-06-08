# Instruções para Configuração do DataSource no GlassFish

Este documento contém instruções detalhadas para configurar corretamente o DataSource no GlassFish para o sistema PetSystem.

## 1. Problema Identificado

O erro "JNDI lookup failed for the resource: Name: PetSystemPU, Lookup: java:app/JNDI_PETSYSTEM" indica que o GlassFish não consegue encontrar o recurso JNDI configurado no persistence.xml.

## 2. Solução Implementada

Padronizamos o nome JNDI para `jdbc/PetSystemDS` em ambos os arquivos:
- persistence.xml
- glassfish-resources.xml

## 3. Passos para Configuração Manual no GlassFish

Se o recurso JNDI não for criado automaticamente durante o deploy, siga estes passos:

1. Acesse o Console de Administração do GlassFish (geralmente em http://localhost:4848)
2. Faça login com suas credenciais (padrão: admin/admin)
3. No menu lateral, navegue até "Recursos" > "JDBC" > "Connection Pools"
4. Clique em "Novo" para criar um novo pool de conexões:
   - Nome do Pool: `post-gre-sql_petsystem_db_postgresPool`
   - Tipo de Recurso: `javax.sql.DataSource`
   - Nome da Classe do DataSource: `org.postgresql.ds.PGSimpleDataSource`
   - Clique em "Avançar"
5. Na próxima tela, adicione as seguintes propriedades:
   - serverName: `localhost`
   - portNumber: `5432`
   - databaseName: `petsystem_db`
   - User: `postgres`
   - Password: `masterkey`
   - URL: `jdbc:postgresql://localhost:5432/petsystem_db`
   - driverClass: `org.postgresql.Driver`
6. Clique em "Finalizar" para criar o pool
7. Teste a conexão clicando em "Ping" no pool recém-criado
8. Agora, navegue até "Recursos" > "JDBC" > "JDBC Resources"
9. Clique em "Novo" para criar um novo recurso JDBC:
   - Nome JNDI: `jdbc/PetSystemDS` (exatamente como está no persistence.xml)
   - Nome do Pool: `post-gre-sql_petsystem_db_postgresPool`
10. Clique em "OK" para criar o recurso

## 4. Verificação

Para verificar se o recurso está corretamente configurado:
1. Reinicie o GlassFish
2. Faça o deploy do aplicativo novamente
3. Verifique os logs do servidor para confirmar que não há erros de JNDI

## 5. Observações Importantes

- O nome JNDI deve ser **exatamente igual** em ambos os arquivos
- O banco de dados PostgreSQL deve estar em execução na porta 5432
- O banco de dados `petsystem_db` deve existir
- O usuário `postgres` com senha `masterkey` deve ter permissões no banco
