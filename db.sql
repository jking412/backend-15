-- we don't know how to generate root <with-no-name> (class Root) :(

grant select on performance_schema.* to 'mysql.session'@localhost;

grant trigger on sys.* to 'mysql.sys'@localhost;

grant audit_abort_exempt, firewall_exempt, select, system_user on *.* to 'mysql.infoschema'@localhost;

grant audit_abort_exempt, authentication_policy_admin, backup_admin, clone_admin, connection_admin, firewall_exempt, persist_ro_variables_admin, session_variables_admin, shutdown, super, system_user, system_variables_admin on *.* to 'mysql.session'@localhost;

grant audit_abort_exempt, firewall_exempt, system_user on *.* to 'mysql.sys'@localhost;

grant allow_nonexistent_definer, alter, alter routine, application_password_admin, audit_abort_exempt, audit_admin, authentication_policy_admin, backup_admin, binlog_admin, binlog_encryption_admin, clone_admin, connection_admin, create, create role, create routine, create tablespace, create temporary tables, create user, create view, delete, drop, drop role, encryption_key_admin, event, execute, file, firewall_exempt, flush_optimizer_costs, flush_status, flush_tables, flush_user_resources, group_replication_admin, group_replication_stream, index, innodb_redo_log_archive, innodb_redo_log_enable, insert, lock tables, passwordless_user_admin, persist_ro_variables_admin, process, references, reload, replication client, replication slave, replication_applier, replication_slave_admin, resource_group_admin, resource_group_user, role_admin, select, sensitive_variables_observer, service_connection_admin, session_variables_admin, set_any_definer, show databases, show view, show_routine, shutdown, super, system_user, system_variables_admin, table_encryption_admin, telemetry_log_admin, trigger, update, xa_recover_admin, grant option on *.* to root;

grant allow_nonexistent_definer, alter, alter routine, application_password_admin, audit_abort_exempt, audit_admin, authentication_policy_admin, backup_admin, binlog_admin, binlog_encryption_admin, clone_admin, connection_admin, create, create role, create routine, create tablespace, create temporary tables, create user, create view, delete, drop, drop role, encryption_key_admin, event, execute, file, firewall_exempt, flush_optimizer_costs, flush_status, flush_tables, flush_user_resources, group_replication_admin, group_replication_stream, index, innodb_redo_log_archive, innodb_redo_log_enable, insert, lock tables, passwordless_user_admin, persist_ro_variables_admin, process, references, reload, replication client, replication slave, replication_applier, replication_slave_admin, resource_group_admin, resource_group_user, role_admin, select, sensitive_variables_observer, service_connection_admin, session_variables_admin, set_any_definer, show databases, show view, show_routine, shutdown, super, system_user, system_variables_admin, table_encryption_admin, telemetry_log_admin, trigger, update, xa_recover_admin, grant option on *.* to root@localhost;

create table constants
(
    constant_key   varchar(255) not null
        primary key,
    constant_value varchar(255) null,
    description    varchar(255) null,
    data_type      varchar(31)  null
);

create table images
(
    image_id            int auto_increment
        primary key,
    image_name          varchar(255) null,
    image_tag           varchar(31)  null,
    image_size_in_bytes bigint       null,
    architecture        varchar(255) null,
    os                  varchar(255) null,
    author              varchar(255) null,
    created_at          timestamp    null
);

create table pod_info
(
    pod_id            int auto_increment
        primary key,
    cpu_req           decimal(8, 2)  null,
    cpu_limit         decimal(8, 2)  null,
    memory_req        decimal(10, 2) null,
    memory_limit      decimal(10, 2) null,
    passwd            varchar(100)   null,
    pod_name          varchar(50)    null,
    host_name         varchar(50)    null,
    container_name    varchar(50)    null,
    image_id          int            null,
    image_pull_policy varchar(20)    null,
    labels            varchar(200)   null
);

create table container_ports
(
    port_id  int          not null
        primary key,
    port_num int          null,
    pod_id   int          null,
    protocol varchar(255) null,
    constraint container_ports_pod_info_pod_id_fk
        foreign key (pod_id) references pod_info (pod_id)
);

create table scripts
(
    script_id      int  not null
        primary key,
    script_content text null
);

create table security_group
(
    group_id    int          not null
        primary key,
    group_name  varchar(255) null,
    description varchar(255) null
);

create table network_info
(
    network_id        int         not null
        primary key,
    network_name      varchar(50) null,
    cluster_ip        varchar(15) null,
    security_group_id int         null,
    create_time       datetime    null,
    pod_id            int         null,
    constraint network_info_pod_info_pod_id_fk
        foreign key (pod_id) references pod_info (pod_id),
    constraint network_info_security_group_group_id_fk
        foreign key (security_group_id) references security_group (group_id)
);

create table security_group_ports
(
    port_id     int auto_increment
        primary key,
    port_name   varchar(255) null,
    port        int          null,
    protocol    varchar(255) null,
    description varchar(255) null,
    group_id    int          null,
    constraint security_group_ports_security_group_group_id_fk
        foreign key (group_id) references security_group (group_id)
);

create table user_info
(
    user_id  bigint auto_increment
        primary key,
    username varchar(50)  null,
    email    varchar(100) null,
    password varchar(100) null
);

create table volume_info
(
    volume_id   int auto_increment
        primary key,
    volume_name varchar(50) null,
    pod_id      int         null,
    pod_name    int         null,
    pod_path    int         null,
    size_mb     bigint      null,
    constraint volume_info_pod_info_pod_id_fk
        foreign key (pod_id) references pod_info (pod_id)
);

