--delete from stock_base
select * from stock_base
 where id='sz002317'
--INSERT INTO stock_base(id, name, industry) VALUES ('', '', '')


select *,
'http://www.icaifu.com/stock/doctora/' || s.id || '.shtml' 
from stock s join stock_base b on s.id=b.id 
where icf_level=50 and tsh_percent>=90  and claw_date=current_date
order by b.industry;


select * from stock where tsh_percent=0 and icf_level=0 and claw_date=current_date and id not in(select id from stock_closed);

--delete  from stock where claw_date=current_date



--INSERT INTO stock_closed(id)VALUES ('sz000787');

--errors
select * from stock_error where ec_icf>0 and ec_ths>0 and id not in(select id from stock_closed)
order by ec_icf+ec_ths desc;
--update stock_error set ec_icf=0

--update stock set icf_level=0 where icf_level=99


select count(*) from stock where icf_level=0 and  claw_date=current_date;

select * from stock where tsh_percent=0 and  claw_date=current_date;

select * from stock_closed order by id ;

