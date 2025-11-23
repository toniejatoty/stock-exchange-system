USE master;

-- 1. Odbierz uprawnienia do master
REVOKE CONNECT SQL FROM stockapp;
REVOKE VIEW ANY DATABASE FROM stockapp;


GRANT CONNECT SQL TO stockapp;
GRANT VIEW ANY DATABASE TO stockapp;

-- W SSMS wykonaj:
SELECT local_tcp_port FROM sys.dm_exec_connections WHERE session_id = @@SPID;

SELECT 
    local_net_address,
    local_tcp_port
FROM sys.dm_exec_connections 
WHERE session_id = @@SPID;


SELECT @@SERVERNAME as 'Server Name';



SELECT  
    [name] as 'Service Name',
    [protocol_name],
    type_desc,
    state_desc,
    port
FROM sys.dm_tcp_listener_states;



SELECT DISTINCT local_tcp_port 
FROM sys.dm_exec_connections 
WHERE local_tcp_port IS NOT NULL;


SELECT  
    net_transport,
    local_net_address,
    local_tcp_port,
    client_net_address
FROM sys.dm_exec_connections 
WHERE session_id = @@SPID;



SELECT 
    name,
    value,
    value_in_use
FROM sys.configurations
WHERE name LIKE '%tcp%' OR name LIKE '%remote%'

-- W≥πcz remote access
EXEC sp_configure 'remote access', 1;
RECONFIGURE;

-- W≥πcz TCP/IP (moøe wymagaÊ restartu)
EXEC sp_configure 'show advanced options', 1;
RECONFIGURE;
EXEC sp_configure 'remote admin connections', 1;
RECONFIGURE;



-- Sprawdü na jakim porcie nas≥uchuje SQL Server
SELECT DISTINCT local_tcp_port 
FROM sys.dm_exec_connections 
WHERE local_tcp_port IS NOT NULL;

-- Lub sprawdü w error log
EXEC xp_readerrorlog 0, 1, N'Server is listening on';