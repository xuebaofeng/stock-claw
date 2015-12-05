
select *,'http://www.icaifu.com/stock/doctora/' || s.id || '.shtml' from stock s join stock_base b on s.id=b.id 
where icf_level=50 and tsh_percent>=90  and claw_date=current_date;


select * from stock s1 join stock s2 on s2.id=s1.id and s1.claw_date=current_date-1 and s2.claw_date=current_date
where s1.tsh_percent= 0 and s1.tsh_percent=0 and s2.icf_level=0 and s1.icf_level=0 order by s1.id;


select * from stock_base order by id;

INSERT INTO stock_closed(id)VALUES ('sh600991');
INSERT INTO stock_closed(id)VALUES ('sh600001');
INSERT INTO stock_closed(id)VALUES ('sh600003');
INSERT INTO stock_closed(id)VALUES ('sh600296');


select * from stock_error where error_count>0 order by error_count desc;

select * from stock where icf_level=0 and  claw_date=current_date;

select * from stock where tsh_percent=0 and  claw_date=current_date;
