﻿--delete from stock_base
select * from stock_base
 where id='sz代码';

delete from stock_base  where id='sz代码';

--INSERT INTO stock_base(id, name, industry) VALUES ('', '', '')

select max(c_date) from stock;

select *
from stock s join stock_base b on s.id=b.id 
where icf_level=4 and c_date=current_date and s.ths_percent>90
order by b.industry;

update stock set ths_percent=0
where c_date=current_date;

select * from stock where c_date=current_date and icf_level>=40;


--and id not in(select id from stock_closed);

--delete  from stock where c_date=current_date



--INSERT INTO stock_closed(id)VALUES ('sz000787');

--errors
select * from stock_error where ec_icf>0 and ec_ths>0 and id not in(select id from stock_closed)
order by ec_icf+ec_ths desc;
--update stock_error set ec_icf=0

--update stock set icf_level=0 where icf_level=99


select count(*) from stock where icf_level=0 and  c_date=current_date +1;


--select * from stock_closed order by id ;
