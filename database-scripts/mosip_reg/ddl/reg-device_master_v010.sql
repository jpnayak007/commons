-- create table section -------------------------------------------------
-- schema 		: reg	    		- Registration Module
-- table 		: device_master		- Master List of device used for registration
-- table alias  : devicem

-- schemas section -------------------------------------------------

-- create schema if reg schema for Registration Module is not exists
create schema if not exists reg
;

-- table section -------------------------------------------------
create table reg.device_master (

	id 			character varying (36) not null,

	name 		character varying (64) not null,
	mac_address character varying (64) not null,
	serial_num 	character varying (64) not null,
	ip_address 	character varying (17) , 

	dspec_id    character varying(36) not null ,   -- master.device_spec.id ,  spec mapped to device_type
		
	lang_code   character varying (3) not null,	-- master.language.code
	
	is_active 	boolean not null,
	cr_by 		character varying (32) not null,
	cr_dtimes	timestamp not null,
	upd_by  	character varying (32),
	upd_dtimes timestamp,
	is_deleted 	boolean,
	del_dtimes timestamp
)
;

-- keys section -------------------------------------------------
 alter table reg.device_master add constraint pk_devicem_id primary key (id)
 ;

-- indexes section -------------------------------------------------
-- create index idx_devicem_<colX> on reg.device_master (colX )
-- ;

-- comments section ------------------------------------------------- 
-- comment on table reg.device_master is 'Table to store master list of device like fingerprint scanner, iris scanner, scanner...etc used at registration centers for individual registration'
-- ;

