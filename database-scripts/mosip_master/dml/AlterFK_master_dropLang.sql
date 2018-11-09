
--  DROP LANGUAGE FKeys constraint. 

alter table master.admin_param drop constraint fk_admparm_lang   ;
alter table master.appl_form_type drop constraint fk_applftyp_lang   ;
alter table master.app_detail drop constraint fk_appdtl_lang   ;
alter table master.app_login_method drop constraint fk_applogm_lang   ;
alter table master.module_detail drop constraint fk_moddtl_lang   ;
alter table master.authentication_type drop constraint fk_authtyp_lang   ;
alter table master.biometric_attribute drop constraint fk_bmattr_lang   ;
alter table master.biometric_type drop constraint fk_bmtyp_lang   ;
alter table master.device_master drop constraint fk_devicem_lang   ;
alter table master.device_spec drop constraint fk_dspec_lang   ;
alter table master.device_type drop constraint fk_dtyp_lang   ;
alter table master.doc_category drop constraint fk_doccat_lang   ;
alter table master.doc_format drop constraint fk_docfmt_lang   ;
alter table master.doc_type drop constraint fk_doctyp_lang   ;
alter table master.gender drop constraint fk_gndr_lang   ;
alter table master.global_param drop constraint fk_glbparm_lang   ;
alter table master.id_type drop constraint fk_idtyp_lang   ;
alter table master.introducer_type drop constraint fk_intyp_lang   ;
alter table master.location drop constraint fk_loc_lang   ;
alter table master.loc_holiday drop constraint fk_lochol_lang   ;
alter table master.login_method drop constraint fk_logmeth_lang   ;
alter table master.machine_master drop constraint fk_machm_lang   ;
alter table master.machine_spec drop constraint fk_mspec_lang   ;
alter table master.machine_type drop constraint fk_mtyp_lang   ;
alter table master.message_list drop constraint fk_msglst_lang   ;
alter table master.registration_center drop constraint fk_regcntr_lang   ;
alter table master.reg_center_type drop constraint fk_cntrtyp_lang   ;
alter table master.role_list drop constraint fk_rolelst_lang   ;
alter table master.screen_authorization drop constraint fk_scrauth_lang   ;
alter table master.screen_detail drop constraint fk_scrdtl_lang   ;
alter table master.status_type drop constraint fk_sttyp_lang ;
alter table master.status_list drop constraint fk_status_lang  ;
alter table master.reason_list drop constraint fk_rsnlst_lang ;
alter table master.reason_category drop constraint fk_rsncat_lang ;
alter table master.template_file_format drop constraint fk_tffmt_lang   ;
alter table master.template_type drop constraint fk_tmpltyp_lang   ;
alter table master.template drop constraint fk_tmplt_lang   ;
alter table master.title drop constraint fk_ttl_lang   ;
alter table master.transaction_type  drop constraint fk_trntyp_lang   ;
alter table master.user_detail drop constraint fk_usrdtl_lang   ;
alter table master.user_role drop constraint fk_usrrol_lang   ;
alter table master.valid_document drop constraint fk_valdoc_lang   ;
alter table master.blacklisted_words drop constraint fk_blwrd_lang   ;
alter table master.user_pwd drop constraint fk_usrpwd_lang ;
