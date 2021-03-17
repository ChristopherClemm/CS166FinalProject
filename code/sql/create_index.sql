--customer
CREATE INDEX cust_id ON Customer USING btree(id);
CREATE INDEX cust_fname ON Customer USING btree(fname);
CREATE INDEX cust_lname ON Customer USING btree(lname);
CREATE INDEX cust_gender ON Customer USING btree(gtype);
CREATE INDEX cust_dob ON Customer USING btree(dob);
CREATE INDEX cust_addr ON Customer USING btree(dob);
CREATE INDEX cust_phone ON Customer USING btree(phone);
CREATE INDEX cust_zipcode ON Customer USING btree(zipcode);

--captain
CREATE INDEX cap_id ON Captain USING btree(id);
CREATE INDEX cap_fname ON Captain USING btree(fullname);
CREATE INDEX cap_nationality ON Captain USING btree(nationality);

--cruise
CREATE INDEX cru_num ON Cruise USING btree(cnum); 
CREATE INDEX cru_cost ON Cruise USING btree(cost);
CREATE INDEX cru_num_sold ON Cruise USING btree(num_sold);
CREATE INDEX cru_num_stops ON Cruise USING btree(num_stops);
CREATE INDEX cru_act_depart_date ON Cruise USING btree(actual_departure_date);
CREATE INDEX cru_act_arrival_date ON Cruise USING btree(actual_arrival_date);
CREATE INDEX cru_arrival_port ON Cruise USING btree(arrival_port);
CREATE INDEX cru_departure_port ON Cruise USING btree(departure_port);

--ship
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
CREATE INDEX  
