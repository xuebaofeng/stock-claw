select *,'http://www.icaifu.com/stock/doctora/' || s.id || '.shtml' from stock s join stock_base b on s.id=b.id where claw_date=current_date and icf_level=50 and tsh_percent>=90;

--select * from stock s1 join stock s2 on s2.id=s1.id and s1.claw_date=current_date-1 and s2.claw_date=current_date
--where s1.icf_level>s2.icf_level

--select * from stock s1 join stock s2 on s2.id=s1.id and s1.claw_date=current_date-1 and s2.claw_date=current_date
--where s1.tsh_percent>s2.tsh_percent or s1.icf_level>s2.icf_level
