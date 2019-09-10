DELIMITER ;;

CREATE session_validation(IN sessionID varchar(50), OUT oStatus INT, OUT oMsg varchar(30) )
BEGIN
declare lastActive int(12) DEFAULT 0;
declare currentTimeStamp int(12);
declare session_timeout INT;

select CAST(param_value AS UNSIGNED) INTO session_timeout from settings where param_name='session_timeout';
select last_active into lastActive from USER_SESSION where session_id = sessionID;
select UNIX_TIMESTAMP() into currentTimeStamp;
IF( lastActive = 0 ) then
SET oStatus = -1;
SET oMsg = "Invalid sessionID";
ELSEIF( ABS(currentTimeStamp - lastActive) <= session_timeout) then
UPDATE USER_SESSION set last_active = UNIX_TIMESTAMP() where session_id = sessionID;
select user_id, app_id, build, user_type, user_stage, last_active, jsession,jkey, broker_name, product_alias, is_enable_transPass, account_id, branch_id, default_market_watch, broker_name from USER_SESSION where session_id = sessionID;
SET oStatus = 0;
ELSE
DELETE from USER_SESSION where session_id = sessionID;
SET oStatus = -2;
SET oMsg = "Session expired";
END IF;
END

DELIMITER ;
