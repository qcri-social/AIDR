# -- updating userName in account table with provider name
# -- New userName = provider-userName like twitter-kushalkantgoyal@gmail.com

delete  FROM aidr_predict.userconnection where profileUrl like '%facebook%' or profileUrl like '%google%';
delete  FROM aidr_predict.account where provider = 'google' or provider = 'facebook';

update userconnection t1, userconnection t2 set  t2.providerUserId = t1.providerUserId where t1.userId =  t2.userID and t1.providerId = "twitter" and t2.providerId = 'springSocialSecurity' ;

update account set user_name = concat(provider,"-",user_name);